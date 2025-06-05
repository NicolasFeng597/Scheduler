import java.util.List;
import java.util.ArrayList;

// an entire course, like EGR 156 or COS 126
public class Course {
   String name; // the name of a course: "EGR 156", "COS 126" etc.
   
   // a list of all the unique sections in a course, such as precepts, lectures, labs, etc.
   // doesn't mean the specific timeslots for each section, just each category as a whole
   // isn't filled immediately, filled while parsing data.txt
   List<Section> sections = new ArrayList<>();
}
