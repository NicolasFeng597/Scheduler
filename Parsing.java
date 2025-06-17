import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class Parsing {
   // parsing input from selected file
    public static List<Course> generateCourses(String file_path) throws FileNotFoundException, IOException {
        List<Course> courses = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file_path));

        String line = br.readLine();
        while (line != null) {
            // each course starts with an empty line
            if (line.isEmpty()) {
                Course currentCourse = new Course();
                List<Section> sections = new ArrayList<>();

                line = br.readLine(); // the next line is the course name
                currentCourse.name = line;

                /* each timeslot is four lines:
                 * 0: timeslot number and name
                 * 1: class time and room assignment
                 * 2: enrolled, limit, closed, books (no books since no URL in copied data)
                 */
                int lineType = 0; // the current line's type

                line = br.readLine(); // the next line starts the timeslots

                // builds each timeslot, then puts it into its respective Section
                // if that Section doesn't exist yet, create it
                // if that Section does exist, append it to that Section's timeslots
                Timeslot timeslot = null;
                char timeslotSectionLetter = '\0';
                while (line != null && !line.isEmpty()) {
                    // parse each line based on its type
                    switch (lineType % 3) {
                        case 0:
                            // timeslot number, name, and parentCourse
                            timeslot = new Timeslot(); // one timeslot every 4 lines
                            int timeslotNumber = Integer.parseInt(line.split("\t")[0]);
                            timeslot.timeslotNumber = timeslotNumber;

                            timeslot.name = line.split("\t")[1];
                            timeslotSectionLetter = timeslot.name.charAt(0);

                            timeslot.parentCourse = currentCourse.name;
                            break;
                        case 1:
                            // class time and room assignment
                            // edge case where the line's "To be determined", in which case
                            // it should be an empty timeslot.time
                            if (line.strip().equals("To be determined")) {
                                timeslot.time = new BitSet[] {new BitSet(166), new BitSet(166), new BitSet(166), new BitSet(166), new BitSet(166)};
                                timeslot.room = "";
                                break;
                            }

                            String[] timeslotData = line.split(",");
                            // timeslot.parseTimeslotLine(timeslotData);
                            timeslot.time = Parsing.parseTimeslotLine(
                                Arrays.copyOf(timeslotData, 2));

                            timeslot.room = timeslotData[2].strip();
                            break;
                        case 2:
                            // enrolled, limit, closed, and books
                            String[] line_data = line.split("\t");
                            // the first entry is always enrolled/limit
                            timeslot.enrolled = Integer.parseInt(line_data[0].split("/")[0]);
                            timeslot.limit = Integer.parseInt(line_data[0].split("/")[1]);

                            // If the split data has multiple entries, only the second entry can be
                            // closed
                            if (line_data.length > 1) {
                                if (line_data[1].equals("Closed")) timeslot.closed = true;
                            }

                            // put the current finished timeslot into its section
                            addSection:
                            {
                                for (Section s : sections) {
                                    // if the timeslot is in this existing section, append it
                                    if (s.name == timeslotSectionLetter) {
                                        s.timeslots.add(timeslot);
                                        break addSection;
                                    }
                                }
                                // if we got to here, that means this section doesn't exist yet
                                Section newSection = new Section();
                                newSection.name = timeslotSectionLetter;
                                newSection.timeslots.add(timeslot);
                                sections.add(newSection);
                            }
                            break;
                    }
                    line = br.readLine();
                    lineType++;
                }

                currentCourse.sections = sections;
                courses.add(currentCourse);
            }
        }

        br.close();
        return courses;
    }


    public static BitSet[] parseTimeslotLine(String[] lineSections) {
        BitSet[] result = {new BitSet(166), new BitSet(166), new BitSet(166), new BitSet(166), new BitSet(166)};
        // = ["M W", "10:40 am – 12:00 pm", "10:40 a.m. to 12:00 p.m.""]
        String[] timeSection = lineSections[1].split(" – ");
        // = ["10:40 am", "12:00 pm"]

        /*
        * converting times to a range for the BitSet:
        * 8:30 AM would be 0, 8:35 AM would be 1, ..., 10:20 PM would be 165
        */
        // convert to 24 hr time
        int startTime = Integer.parseInt(timeSection[0].split(" ")[0].replaceFirst(":", ""));
        // "10:40 am" -> ["10:40", "am"][0] -> "10:40" -> "1040" -> 1040
        if (timeSection[0].split(" ")[1].equals("pm") && startTime / 100 != 12) startTime += 1200;

        int endTime = Integer.parseInt(timeSection[1].split(" ")[0].replaceFirst(":", ""));
        // "12:00 pm" -> ["12:00", "pm"][0] -> "12:00" -> "1200" -> 1200
        if (timeSection[1].split(" ")[1].equals("pm") && endTime / 100 != 12) endTime += 1200;

        // converting to a range for the BitSet
        startTime = (startTime - 830) / 5;
        endTime = (endTime - 830) / 5;

        String[] days = lineSections[0].split(" ");
        // for each day, fill that BitSet in ongoing
        for (String i : days) {
            int day;
            if (i.equals("M")) day = 0;
            else if (i.equals("T")) day = 1;
            else if (i.equals("W")) day = 2;
            else if (i.equals("Th")) day = 3;
            else day = 4; // F

            result[day].set(startTime, endTime);
        }

        return result;
    }
}
