import java.io.IOException;
import java.util.Scanner;
import java.io.FileNotFoundException;
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

    public static List<List<Section>> findPossibleSchedules(Course[] schedule) {
        return null;
    }
}
