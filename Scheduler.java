import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Scheduler {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // getting schedule size
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many courses do you want in your schedule?");
        int courseCount = scanner.nextInt();
        scanner.close();

        // the entire schedule is made up of iCourseCount courses
        Course[] schedule = parseInput(courseCount);
    }

    // parsing input from data.txt
    public static Course[] parseInput(int courseCount) throws FileNotFoundException, IOException {
        Course[] schedule = new Course[courseCount];
        BufferedReader br = new BufferedReader(new FileReader("data.txt"));

        String line = br.readLine(); //
        while (line != null) {
            // each course starts with an empty line
            if (line == "") {
                line = br.readLine(); // the next line is the course name
                Course currentCourse = new Course(line);

                line = br.readLine(); // the next line starts the sections
                /* each section takes four lines:
                 * 0: section number and name
                 * 1: class time and room assignment
                 * 2: enrolled count
                 * 3: limit and books
                 */
                int lineType = 0; // the current line's type
                
                // for all the lines in the current section
                while (line != null && line != "") {
                    // parse each line based on its type
                    switch (lineType) {
                        case 0:
                            // section number and name
                            schedule[lineType] = new Course(line);
                            break;
                        case 1:
                            // class time and room assignment
                            break;
                        case 2:
                            // enrolled count
                            break;
                        case 3:
                            // limit and books
                            break;
                    }
                    line = br.readLine();
                    lineType++;
                }
            }
        }
        return schedule;
    }
}

