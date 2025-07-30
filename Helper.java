import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Helper {
    // returns all combinations from the given course array with specified size r
    public static List<Course[]> combinations(List<Course> courseList, int r) {
        // number of total courses
        int n = courseList.size();

        // last item in courseList, used a lot so better to reference once
        Course last = courseList.get(n - 1);

        // a list of all combinations, each Course[] having length r
        List<Course[]> combos = new ArrayList<>();

        /*
         * functions as a base n number of length r, with each digit
         * representing the ith item in courseList
         *
         * at every iteration, the number must be in ascending order with
         * no repeats
         *
         * then, "add one" to the number every iteration, adding one to
         * the next digit after reaching the nth digit, but maintaining
         * ascending order, thus the last digit isn't set to 0 but the
         * next digit after the digit before it
         */

        // the current combination
        // TODO: rewrite code so u don't have to use this current combo var
        Course[] combo = new Course[r];

        // tracks the index of each course in combo
        int[] comboIndexes = new int[r];

        // initializes combo to the 0th to r-1th items in courseList and comboIndexes to 0 to r-1
        for (int i = 0; i < r; i++) {
            combo[i] = courseList.get(i);
            comboIndexes[i] = i;
        }

        while (true) {
            combos.add(combo.clone());

            // "add one" to combo
            if (combo[r - 1] == last) {
                // from the second to last number place to the 0th number place:
                for (int i = r - 2; i >= 0; i--) {
                    // TODO: efficiency gain by adding a reverse-sorted combos list?
                    // for the x from last number place, if it isn't the x from last digit
                    // in the "base"
                    // for a list of 12 courses with r = 5,
                    // if the combo[3] course isn't the 11th course,
                    // if the combo[2] course isn't the 10th course, etc.
                    // r - i - 1 = how many from the end
                    // n - ^^^ - 1 = which one it should be (the -1 is for index)
                    // n - (r - i - 1) - 1 = n - r + i + 1 - 1 = n - r + i
                    if (combo[i] != courseList.get(n - r + i)) {
                        // "add" 1 to combo[i]
                        comboIndexes[i]++;
                        combo[i] = courseList.get(comboIndexes[i]);

                        // for all the slots after i, they equal combo[i] +1, +2, +3, etc.
                        for (int j = i + 1; j < r; j++) {
                            comboIndexes[j] = comboIndexes[j - 1] + 1;
                            combo[j] = courseList.get(comboIndexes[j]);
                        }

                        break;
                    } else {
                        // if this number place is the last number place, continue the for loop
                        // until the first number place is reached

                        if (i == 0) return combos;
                    }
                }
            } else {
                // moves the last number place to the next digit in the "base"
                comboIndexes[r - 1]++;
                combo[r - 1] = courseList.get(comboIndexes[r - 1]);
            }
        }
    }

    // decomposes the course_combo into a list of Sections gather from each course's "sections" variable
    public static List<Section> courses_to_sections(Course[] course_combo) {
        List<Section> all_sections = new ArrayList<>();

        for (Course c : course_combo) {
            for (Section s : c.sections) all_sections.add(s);
        }

        return all_sections;
    }

    /*
    // chooses a Timeslot from each Section in all_sections, then compiles all choices (finds the Cartesian Product)
    static int[] comboIndexes;
    static int n;
    static int[] section_lengths;
    static List<Timeslot[]> sections;
    // sets the vars above given the list of all_sections

    public static void set_sections(List<Section> all_sections) {
        n = all_sections.size();
        comboIndexes = new int[n];

        section_lengths = new int[n];
        for (int i = 0; i < n; i++) section_lengths[i] = all_sections.get(i).timeslots.size();

        sections = new ArrayList<>();
        for (int i = 0; i < n; i++) sections.add(all_sections.get(i).timeslots.toArray(new Timeslot[0]));
    }
*/
    /*
    // add one to the current_schedule, returns true if trying to add one past the highest combo
    public static boolean increment_current_schedule() {
        if (comboIndexes[n - 1] == section_lengths[n - 1] - 1) { // if last digit
            for (int i = n - 2; i > -1; i--) {
                if (comboIndexes[i] == section_lengths[i] - 1) { // if last digit
                    if (i == 0) return false; // full list of 999...999
                    comboIndexes[i] = 0;
                } else {
                    comboIndexes[i]++;
                    // sets all digits after i to 0
                    for (int j = i + 1; j < n; j++) comboIndexes[j] = 0;
                    return true;
                }
            }
        } else comboIndexes[n - 1]++;
        return true;
    }

    // creates a Timeslot[] from comboIndexes
    public static Timeslot[] generate_combo_from_indexes() {
        Timeslot[] combo = new Timeslot[n];
        for (int i = 0; i < n; i++) combo[i] = sections.get(i)[comboIndexes[i]];
        return combo;
    }

    public static List<Timeslot[]> choose_timeslots(List<Section> all_sections) {
        // TODO: implement static vars
        List<Timeslot[]> all_schedules = new ArrayList<>();
        int n = all_sections.size();

        // work through the options like a multi-base number, each number place being a Section and
        // each digit being a Timeslot
        Timeslot[] current_schedule;
        current_schedule = new Timeslot[n];
        // first initialize to all 0 digits
        // TODO: prolly don't need to do this, make more efficient
        for (int i = 0; i < n; i++) current_schedule[i] = all_sections.get(i).timeslots.get(0);

        // tracks the index of each timeslot in the current schedule
        comboIndexes = new int[n];

        // first, find the lowest schedule that doesn't conflict
        current_number_place: for (int i = 0; i < n; i++) { // i is the current number place
            // each number place starts at its 0th index

            // for each number place, check if any of the prior number places overlap with the current one
            for (int j = i - 1; j >= 0; j--) {
                // if they do, increment the current one and restart at the same number place
                // if this number is the last one, recursively increase the prior number place until "999...999"
                if (is_overlapping(current_schedule[j], current_schedule[i])) {
                    if (comboIndexes[i] == all_sections.get(i).timeslots.size()) {
                        for (int k = i; k >= 0; k--) {
                            if (comboIndexes[k] == all_sections.get(k).timeslots.size()) {
                                if (k == 0) return all_schedules; // empty list
                                comboIndexes[k] = 0;
                                current_schedule[k] = all_sections.get(k).timeslots.get(0);
                            }
                        }
                    } else {
                        comboIndexes[i]++;
                        i--;
                        continue current_number_place;
                    }
                }
                // if they don't just move on to the next number
            }
        }
        all_schedules.add(current_schedule.clone());

        // then, increment the last number place and compute overlaps
        // TODO: big time save if you can just edit comboIndexes instead of setting current_schedule at the same time
        while (true) {
            int k = 0;
            // add one to the last number place, and if there's an overlap in the added digits, add one
            // adding one with mixed base
            for (int i = n - 1; i >= 0; i--) {
                if (comboIndexes[n] == all_sections.get(n).timeslots.size()) {
                    if (i == 0) return all_schedules; // just the current list
                    comboIndexes[n] = 0;
                    current_schedule[n] = all_sections.get(n).timeslots.get(0);
                } else {
                    comboIndexes[n]++;
                    current_schedule[n] = all_sections.get(n).timeslots.get(comboIndexes[n]);
                    k = n - i - 1; // represents that k digits were edited, but - 1 for indexing
                    break;
                }
            }

            // suppose when n=15 and the last 5 digits are incremented, check if the 11th digit overlaps
            // with all of them, if the 12th digit overlaps with everything except 11, ...,
            // and check if the 15th digit overlaps with everything except 11-14
            // generally, for k ending digits, check if the jth digit
            // overlaps with the k+ith digit only if ((j < n - k - 1 || j > k - 1 + i) && k - 1 + i != j)
            // (note the code does k - 1)

            b1: for (int i = 0; i <= k; i++) {
                for (int j = 0; j < n; j++) {
                    if (!((j < n - k || j > k + i) && k + i != j)) continue;
                    if (k + i > j) {
                        if (is_overlapping(current_schedule[j], current_schedule[k + i])) {
                            comboIndexes[j]++;
                            current_schedule[j] = all_sections.get(j).timeslots.get(comboIndexes[j]);

                            // you can set the number places after this digit to their 0th value
                            for (int l = j + 1; l < n; l++) {
                                comboIndexes[l] = 0;
                                current_schedule[l] = all_sections.get(l).timeslots.get(0);
                            }

                            // if this was done, then set k to j and restart the loop without incrementing
                            if (j + 1 < n) {
                                k = j;
                                continue b1; // TODO: what to name branch?
                            }
                        }
                    } else {
                        if (is_overlapping(current_schedule[k + i], current_schedule[j])) {
                            comboIndexes[k + i]++;
                            current_schedule[k + i] = all_sections.get(k + i).timeslots.get(comboIndexes[k + i]);

                            // you can set the number places after this digit to their 0th value
                            for (int l = k + i + 1; l < n; l++) {
                                comboIndexes[l] = 0;
                                current_schedule[l] = all_sections.get(l).timeslots.get(0);
                            }

                            // if this was done, then set k to k + i and restart the loop without incrementing
                            if (k + i + 1 < n) {
                                k = k + i;
                                continue b1; // TODO: what to name branch?
                            }
                        }
                    }
                }
            }
            all_schedules.add(current_schedule.clone());
        }
    }

    // static int[] comboIndexes;
    // static int n;
    // static int[] section_lengths;
    // static List<Timeslot[]> sections;

    // Brute force
    public static List<Timeslot[]> choose_timeslots_dumb(List<Section> all_sections) {
        set_sections(all_sections);

        List<Timeslot[]> all_schedules = new ArrayList<Timeslot[]>();

        boolean flag_ah = true;
        // check 000...000 once
        b1: for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (is_overlapping(sections.get(i)[comboIndexes[i]], sections.get(j)[comboIndexes[j]])) {
                    flag_ah = false;
                    // System.out.print("overlapped schedule: ");
                    // for (int k = 0; k < n; k++) {
                    //     System.out.print(sections.get(k)[comboIndexes[k]] + " ");
                    // }
                    // System.out.println();
                    break b1;
                }
            }
        }
        if (flag_ah) all_schedules.add(generate_combo_from_indexes());

        while (increment_current_schedule()) {
            // for (int i = 0; i < n; i++) System.out.print(comboIndexes[i] + " ");
            // System.out.println();

            flag_ah = true;
            b2: for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (is_overlapping(sections.get(i)[comboIndexes[i]], sections.get(j)[comboIndexes[j]])) {
                        flag_ah = false;
                        // System.out.print("overlapped schedule: ");
                        // for (int k = 0; k < n; k++) {
                        //     System.out.print(sections.get(k)[comboIndexes[k]] + " ");
                        // }
                        // System.out.println();
                        break b2;
                    }
                }
            }
            if (flag_ah) all_schedules.add(generate_combo_from_indexes());
        }
        return all_schedules;
    }

    */


    // For each Timeslot key, the value is a set of all overlapping timeslots
    // TODO: time efficiency if you make each timeslot combo a unique number? or smth
    private static HashMap<Timeslot[], Boolean> hashMapOverlaps = new HashMap<>();
    // Private method which checks/stores overlaps in hashMapOverlaps
    // just ensure that intro is a earlier number place than old
    private static boolean is_overlapping(Timeslot t1, Timeslot t2) {
        // checks if in the list
        Timeslot[] t_combined = {t1, t2};
        if (hashMapOverlaps.containsKey(t_combined)) return hashMapOverlaps.get(t_combined);

        // otherwise, compute overlap
        boolean overlaps = false;
        for (int i = 0; i < 5; i ++){ //TODO: use magic number DAYS
            if (t1.time[i].intersects(t2.time[i])) {
                overlaps = true;
                break;
            }
        }

        hashMapOverlaps.put(t_combined, overlaps);
        // if (overlaps) System.err.println("is_Overlapping " + t1 +", " + t2);
        return overlaps;
        /*
        // Does the list not contain intro or old?
        // if (!hashMapOverlaps.containsKey(intro) || !hashMapOverlaps.containsKey(old)) {
        //     // Update HashMap
        //     // Add one another to the respective hash tables, initializing if
        //     // necessary
        //     if (hashMapOverlaps.containsKey(intro)){
        //         hashMapOverlaps.get(intro).put(old, overlap);
        //     } else {
        //         HashMap<Timeslot, Boolean> newIntroMap = new HashMap<Timeslot, Boolean>();
        //         newIntroMap.put(old, overlap);
        //         hashMapOverlaps.put(intro, newIntroMap);
        //     }
        //     if (hashMapOverlaps.containsKey(old)){
        //         hashMapOverlaps.get(old).put(intro, overlap);
        //     } else {
        //         HashMap<Timeslot, Boolean> newOldMap = new HashMap<Timeslot, Boolean>();
        //         newOldMap.put(old, overlap);
        //         hashMapOverlaps.put(intro, newOldMap);
        //     }

        // } else {
        //     // The hashMapOverlaps contains both, their comparisons have just not been added
        //     hashMapOverlaps.get(intro).put(old, overlap);
        //     hashMapOverlaps.get(old).put(intro, overlap);
        // }

        // return overlap;
        */
    }



















    /*
     *  Ave's Attempted Dynamic Course Parsing Methods
     */

    // Increment Method
// Rolls over, also increments place if needed (returns place)
//
    private static int increment(int[] current, int[] maxValues, int place){
        boolean carryOver = true;


        while (carryOver){
            if (place  <= -1) {
                break;
            }

           // System.out.println("Current:" + current[place]);
           // System.out.println("MaxValue: " + maxValues[place]);

            current[place] = current[place] + 1;
            if (current[place] >= maxValues[place]){
                current[place] = 0;
                place--;
            } else {
                carryOver = false;
            }
        }
        return place;
    }

    private static void appendValidSchedule(List<List<Timeslot>> working_schedules, int[] current, List<List<Timeslot>> sets){
        int sections = current.length;
        List<Timeslot> listOfSlots = new ArrayList<>(sections);

        for (int j = 0; j < sections; j++){
            listOfSlots.add(sets.get(j).get(current[j]));
        }

        /*Timeslot[] listOfSlots = new Timeslot[sections];
        for (int i = 0; i < sections; i++){
            listOfSlots[i] = sets.get(i).get(current[i]);
        }
        */

        working_schedules.add(listOfSlots);
    }

    // Ave's dynamic method

    public static List<List<Timeslot>> dynamicSectionOverlap(List<List<Timeslot>> sets){
        int place = 0;
        List<List<Timeslot>> working_schedules = new ArrayList<>();

        int numberOfSections = sets.size();
        int[] current = new int[numberOfSections];
        int[] maxValue = new int[numberOfSections];

        // Set the maxValue of each section
        for (int i = 0; i < numberOfSections; i++){
            maxValue[i] = sets.get(i).size();
           // System.out.println("Index:" + i + " MaxVal: "  + maxValue[i]);
        }



        while (place > -1){
            // Okay, I am looking at place. Does it match/overlap the
            // one before it?
            boolean overlap = false;
            //System.out.println("Place: " + place);
            Timeslot currentFinalTimeslot = sets.get(place).get(current[place]);
            for (int i = place - 1; i >= 0; i--){
                // Does it overlap prior values?
                Timeslot iterationTimeslot = sets.get(i).get(current[i]);
                if (i != place){ // accounts for if place is zero
                    if (is_overlapping(iterationTimeslot, currentFinalTimeslot)) // ITERATIONTIMESLOT OVERLAPS WITH CURRENTTIMESLOT
                    {
                        overlap = true;
                        break;
                    }

                }

            }


            // Checks for overlap using prior overlap output, increments current if needed
            // also returns a new value of place, if rollover occurs
            if (overlap){
               // System.out.println("Place: k" + place);
                place = increment(current, maxValue, place);
            } else {
                if (place >= numberOfSections - 1){  // If at the end of iterating, this is a valid time.
                    appendValidSchedule(working_schedules, current, sets);
                    // Then, increment
                    place = increment(current, maxValue, place);
                } else { // This is not a filled timeslot. Iterate to the next level
                    place++;
                    current[place] = 0;
                }
            }
        }

        return working_schedules;

    }




}



