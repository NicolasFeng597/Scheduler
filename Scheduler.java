import java.io.IOException;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // getting schedule size
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many courses do you want in your schedule?");
        int courseCount = scanner.nextInt();

        List<Course> courses = Parsing.generateCourses();

        // find all combinations of courses with courseCount items
        // Helper returns a list of course arrays : a list of all possible 5
        // courses, not considering overlaps, no repeats
        
        // rewrite each combination into sections of each course

        // find all combinations of timeslots for each combination of sections

        scanner.close();
    }

    // returns a list of all possible combinations of Courses with length courseCount,
    // with each combination being unique
    public static List<Course[]> combinations() {
        return new ArrayList<Course[]>();
    }


    // Returns a List<List<Section>> which contains the required sections of each course
    // the outermost list object is each section of each class (e.g L, P, B... L, P), 
    // the innermost is the respective timeslots of each of these sections
    // each
    private static List<List<Section>> sectionLists(Course[] courses){

        // Go through each course in courses
        for (int courseNumber = 0; courseNumber < courses.length; courseNumber++){
            // Go through each section in each course
            for (Section courseSection : courses[courseNumber] ){
                // Each course has a list of sections
                // Each section is associated with a char name and a list of timeslots that also have names
                // Is each char name L, P, B? 
            }
        }
      

    }


}
