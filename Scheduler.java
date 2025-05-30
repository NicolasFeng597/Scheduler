import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

public class Scheduler {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // getting schedule size
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many courses do you want in your schedule?");
        int courseCount = scanner.nextInt();
        scanner.close();

        // the entire schedule is made up of iCourseCount courses
        // Course[] schedule = parseInput(courseCount);

        // TODO: here we'd ask for more scheduling constraints, but let's just find all possible schedules
        // List<List<Section>> possibleSchedules = findPossibleSchedules();
    }

    // parsing input from data.txt
    public static Course[] parseInput(int courseCount) throws FileNotFoundException, IOException {
        Course[] schedule = new Course[courseCount];
        BufferedReader br = new BufferedReader(new FileReader("data.txt"));

        String line = br.readLine();
        int currentCourseNumber = 0;
        while (line != null) {
            // each course starts with an empty line
            if (line == "") {
                line = br.readLine(); // the next line is the course name
                Course currentCourse = new Course(line);

                /* each section takes four lines:
                 * 0: section number and name
                 * 1: class time and room assignment
                 * 2: enrolled count
                 * 3: limit and books
                 */
                int lineType = 0; // the current line's type

                line = br.readLine(); // the next line starts the sections

                // builds each section before appending it to currentCourse
                // for all the lines in the current course
                Section currentSection = null;
                while (line != null && line != "") {
                    // parse each line based on its type
                    switch (lineType % 4) {
                        case 0:
                            // section number and name
                            currentSection = new Section(); // one section every 4 lines
                            int sectionNumber = Integer.parseInt(line.split(" ")[0]);
                            currentSection.sectionNumber = sectionNumber;

                            currentSection.name = line.split(" ")[1];
                            break;
                        case 1:
                            // class time and room assignment
                            String timeslotData = line.split(",Room assignment")[0];
                            Timeslot sectionTimeslot = Timeslot.parseTimeslotLine(timeslotData);
                            currentSection.timeslot = sectionTimeslot;

                            currentSection.room = line.split(",Room assignment")[1].strip();
                            break;
                        case 2:
                            // enrolled count
                            currentSection.enrolled = Integer.parseInt(line.split(" ")[1]);
                            break;
                        case 3:
                            // limit and books
                            // currentSection.limit = Integer.parseInt(line.split(" ")[0]);
                            // TODO: kinda tricky cause sometimes there's a book and other times not
                            
                            currentCourse.sections.add(currentSection);
                            // don't need to empty currentSection, reinitialized in case 0
                            break;
                    }
                    line = br.readLine();
                    lineType++;
                }

                schedule[currentCourseNumber] = currentCourse;
                currentCourseNumber++;
            }
        }

        br.close();
        return schedule;
    }

    public static List<List<Section>> findPossibleSchedules(Course[] schedule) {
        return null;
    }
}

