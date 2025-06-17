import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Scheduler {

    // Instance variable which stores the number of days (should always
    // be 5, even if requested for Fridays off)
    private final static int DAYS = 5;
    // How many sets of 5 minutes are in a class day
    private final static int TIME_PERIODS = 166;

    private static HashMap<Timeslot, HashMap<Timeslot, Boolean>> hashMapOverlaps;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // getting schedule size
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many courses do you want in your schedule?");
        int courseCount = scanner.nextInt();
        scanner.nextLine(); // throws away the \n after the inputted int

        List<Course> courses = Parsing.generateCourses("nick_data.txt");

        // find all combinations of courses with courseCount items
        List<Course[]> course_combos = Helper.combinations(courses, courseCount);

        // stuff for must including that class
        System.out.println("Any mandatory classes to include? Separate multiple classes with a comma. (press enter to skip)");
        for (int i = 0; i < courses.size() - 1; i++) {
            System.out.print(courses.get(i).name + ", ");
        }
        System.out.println(courses.get(courses.size() - 1).name);

        String included_whole = scanner.nextLine();
        String[] included_split = included_whole.split(",");
        for (int i = 0; i < included_split.length; i++) included_split[i] = included_split[i].trim();

        // how many sections in total are from all the mandatory classes
        int included_sections = 0;
        for (String c : included_split) {
            for (int i = 0; i < courses.size(); i++) {
                if (courses.get(i).name.equals(c)) included_sections += courses.get(i).sections.size();
            }
        }

        System.out.println("Include closed classes? Enter Yes or No.");
        String include_closed = scanner.nextLine();
        boolean show_closed = include_closed.strip().toLowerCase().equals("yes");

        // actual logic for finding stuff

        List<List<Timeslot>> all_schedules = new ArrayList<>();

        // for each course combo in course_combos, we want to find all the possible schedules that come out of that combo
        //for (Course[] course_combo : course_combos) {
        // first, convert the Course[] into a List<Section> of all sections from that combo
        // so, if I had a Course[5] with each Course having 3 Sections, the resultant length(List<Section>) = 15
        // List<Section> combo_sections = Helper.courses_to_sections(course_combo);

        // then, convert the list of sections to a list of all combos of timeslots
        // List<Timeslot[]> combo_schedules = Helper.choose_timeslots_dumb(combo_sections);

        //    List<List<Timeslot>> combo_schedules = getValidSchedulesAve(course_combos);
        // add all these options to all schedule
        //   all_schedules.addAll(combo_schedules);
        // }
        List<List<Timeslot>> combo_schedules = getValidSchedulesAve(course_combos);
        all_schedules.addAll(combo_schedules);
        System.out.println(all_schedules.size());

        System.out.println("found " + all_schedules.size() + " possible schedules");

        int valid_schedules_counter = 0;
        int classes_included_saw;
        // for all valid schedules,
        schedule_iter:
        for (int i = 0; i < all_schedules.size(); i++) {
            // filters
            classes_included_saw = 0;
            for (Timeslot t : all_schedules.get(i)) {
                // System.out.println("current timeslot " + t);
                // if this current schedule has a mandatory section, it saw one more
                for (String e : included_split) {
                    // System.out.println("included class: " + e + ", parent course" + t.parentCourse + ", overlap = " + t.parentCourse.equals(e));
                    if (t.parentCourse.equals(e)) classes_included_saw++;
                }

                // if this current schedule has a closed timeslot, go to the nexts
                // System.out.println(t + " " + t.closed);
                if (!show_closed && t.closed) continue schedule_iter;
            }

            // System.out.println("was this schedule included: " + (classes_included_saw != included_sections));
            if (classes_included_saw != included_sections) {
                // System.out.print("schedule not included: ");
                // for (Timeslot t : all_schedules.get(i)) {
                //     System.out.print(t + " ");
                // }
                // System.out.println();
                continue schedule_iter;
            }

            // TODO: filters for days and times off

            // at this point, the current schedule passed all the filters, so print it out
            System.out.print("Schedule " + (valid_schedules_counter + 1) + ": ");
            for (Timeslot t : all_schedules.get(i)) {
                System.out.print(t + " ");
            }
            System.out.println();
            valid_schedules_counter++;
        }
        System.out.println(valid_schedules_counter);
        scanner.close();
    }

    // returns a list of all possible combinations of Courses with length courseCount,
    // with each combination being unique

    // Generates a dataset which stores a list of all timeslots for each
    // course, formatted in such a way each type of class (e.g. precept, lecture)
    // must be filled
    public static List<List<Timeslot>> timeExpandedCombinations(Course[] courseList){
        // TODO: Add errors/throws if necessary
        // TODO: Make defensive copy of time object?
        List<List<Timeslot>> finalSectionList = new ArrayList<>();
        for (Course course : courseList)
            for (Section section : course.sections)
                finalSectionList.add(section.timeslots);
        return finalSectionList;
    }


    private static List<List<Timeslot>> allComboTimeslotsSort(Course[] courseList){

        List<List<Timeslot>> finalSectionList = new ArrayList<>();

        int netSections = 0;
        for (Course course: courseList)
            netSections += course.sections.size();

        Section[] sections = new Section[netSections];

        int index = 0;
        for (Course course: courseList)
            for (Section section: course.sections){
                sections[index] = section;
                index++;
            }


        Arrays.sort(sections);
        for (Section section: sections)
            finalSectionList.add(section.timeslots);

        return finalSectionList;
    }

    private static List<List<Timeslot>> allComboTimeslotsSort1(Course[] courseList){

        List<List<Timeslot>> finalSectionList = new ArrayList<>();
        List<Section> sections = new ArrayList<>();

        for (Course course: courseList)
            sections.addAll(course.sections);

        //  Collections.sort(sections);

        for (Section section: sections)
            finalSectionList.add(section.timeslots);

        return finalSectionList;
    }

    // TODO: This is brute force implementation. Implement this same process
    // with hashsets

    // Utilizes all methods in the scheduler class to generate
    // a List<List<Timeslot>>, a list of all combinations of timeslots that
    // make valid schedules
    // does so by calling combinations, and then the recursive methods
    // getCartesianProduct and getCartesianProductHelper
    public static List<List<Timeslot>> getAllValidSchedules(List<Course[]> course_combos){
        // Run combinations to find all possible combinations of courses

        // Run the recursive methods to determine what of these
        // course combinations can generate valid schedules
        // this is updated in result
        List<List<Timeslot>> result = new ArrayList<>();
        for(Course[] courseList : course_combos){
            getCartesianProduct(timeExpandedCombinations(courseList), result);
        }

        return result;
    }

    public static List<List<Timeslot>> getValidSchedulesAve(List<Course[]> course_combos){
        List<List<Timeslot>> result = new ArrayList<>();
        int done = 0;

        for(Course[] courseList : course_combos){
            result.addAll(Helper.dynamicSectionOverlap(allComboTimeslotsSort(courseList)));
            // System.out.println("Done: " + done + " Total: " + course_combos.size());
        }

        return result;
    }


    // Initializer for the recursive function getCartesianProductHelper
    // This function allows for the implementation of restrictions
    private static void getCartesianProduct(List<List<Timeslot>> sets, List<List<Timeslot>> result) {
        // Initialize the restrictions list
        BitSet[] restrictions = new BitSet[DAYS];
        for (int i = 0; i < DAYS; i ++)
            restrictions[i] = new BitSet(TIME_PERIODS);
        getCartesianProductHelperBrute(sets, 0, new ArrayList<>(), result, restrictions);
    }

    // Recursive method, iterates through all possible lecture times, precept, etc. to find combinations
    // that do NOT have time overlaps
    // these proper schedules are added to result
    // if no valid schedules, no updates will be made to result
    // TODO: Efficiency and errors: does not yet have dynamic programming in
    // effect, i.e. if two courses cannot run at the same time it will
    // still check every iteration they are paired
    private static void getCartesianProductHelperBrute(List<List<Timeslot>> sets, int index, List<Timeslot> current,
                                                       List<List<Timeslot>> result, BitSet[] scheduleTimes) {
        // If one from each list is added, return/add the list
        if (index == sets.size()) {
            result.add(new ArrayList<>(current));
            return;
        }

        // List of all timeslots currently in the schedule (not complete)
        List<Timeslot> currentSet = sets.get(index);
        for (Timeslot element: currentSet) {
            // Only want to do this process if the timeslot
            // is valid/fits into the current set
            BitSet[] newScheduleTimes = new BitSet[DAYS];
            boolean noOverlap = true;

            // Check for overlap with schedule times
            // Iterates through the 5 days
            // Generates a new schedule including this timeslot
            // while this process occurs
            for (int i = 0; i < newScheduleTimes.length; i++){
                BitSet tempDaySchedule = (BitSet) scheduleTimes[i].clone();
                // Do the two overlap?
                if (tempDaySchedule.intersects(element.time[i])){
                    noOverlap = false;
                    break;
                }
                // Updates the Schedule if this day is acceptable
                tempDaySchedule.or(element.time[i]);
                newScheduleTimes[i] = tempDaySchedule;
            }

            // Initiate recursion
            if (noOverlap){
                // Adds the element
                current.add(element);
                // Recursion, to next slot
                getCartesianProductHelperBrute(sets, index + 1, current, result, newScheduleTimes);
                // Removes the element so the next iteration does not include it
                current.remove(current.size() - 1);
            }
        }
    }




    private static void getCartesianProductHelperHashMap(List<List<Timeslot>> sets, int index, List<Timeslot> current,
                                                         List<List<Timeslot>> result) {
        // If one from each list is added, return/add the list
        if (index == sets.size()) {
            result.add(new ArrayList<>(current));
            return;
        }

        // List of all timeslots currently in the schedule (not complete)
        List<Timeslot> currentSet = sets.get(index);

        // Element is the NEW section, that could be added
        // current is the current schedule that so far has no overlaps
        // current is not yet added to the list

        for (Timeslot element: currentSet){
            // Check overlap
            boolean overlap = false;
            for (Timeslot approved : current){
                // Checks if the two overlap, if so,
                // break the function
                if (checkOverlap(element, approved)) {
                    overlap = true;
                    break;
                }

            }
            // Recurse if no overlap
            if (!overlap){
                current.add(element);
                // Recursion, to next slot
                getCartesianProductHelperHashMap(sets, index + 1, current, result);
                // Removes the element so the next iteration does not include it
                current.remove(current.size() - 1);
            }

        }


    }


    // Private method which checks for overlap
    private static boolean checkOverlap(Timeslot intro, Timeslot old) {
        // If intro is not even on the hashmap, add it, then add its
        // comparisons to old

        // Does it already exist?
        if (hashMapOverlaps.containsKey(intro) && hashMapOverlaps.get(intro).containsKey(old)){
            return (hashMapOverlaps.get(intro).get(old));
        }


        // Else, they have not been compared yet.

        // Run comparison
        boolean overlap = false;
        for (int i = 0; i < old.time.length; i ++){
            if (old.time[i].intersects(intro.time[i])) {
                overlap = true;
                break;
            }
        }


        // Does the list not contain intro or old?
        if (!hashMapOverlaps.containsKey(intro) || !hashMapOverlaps.containsKey(old)) {
            // Update HashMap
            // Add one another to the respective hash tables, initializing if
            // necessary
            if (hashMapOverlaps.containsKey(intro)){
                hashMapOverlaps.get(intro).put(old, overlap);
            } else {
                HashMap<Timeslot, Boolean> newIntroMap = new HashMap<Timeslot, Boolean>();
                newIntroMap.put(old, overlap);
                hashMapOverlaps.put(intro, newIntroMap);
            }
            if (hashMapOverlaps.containsKey(old)){
                hashMapOverlaps.get(old).put(intro, overlap);
            } else {
                HashMap<Timeslot, Boolean> newOldMap = new HashMap<Timeslot, Boolean>();
                newOldMap.put(old, overlap);
                hashMapOverlaps.put(intro, newOldMap);
            }

        } else {
            // The hashMapOverlaps contains both, their comparisons have just not been added
            hashMapOverlaps.get(intro).put(old, overlap);
            hashMapOverlaps.get(old).put(intro, overlap);
        }

        return overlap;
    }


    // End class
}