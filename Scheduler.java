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

        scanner.close();
    }

    public static List<List<Section>> findPossibleSchedules(Course[] schedule) {
        return null;
    }
}