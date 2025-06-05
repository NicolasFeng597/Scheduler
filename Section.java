import java.util.ArrayList;
import java.util.List;

// the sections in a class, like lecture, precept, lab, etc.
public class Section {
   char name; // the letter that denotes the type of section (L, P, B, etc.)

   // a list of the timeslots that are in this section, such as L01, L02, etc.
   // isn't filled immediately, filled while parsing data.txt
   List<Timeslot> timeslots = new ArrayList<>();
} 
