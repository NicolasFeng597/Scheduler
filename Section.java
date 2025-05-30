import java.util.ArrayList;
import java.util.List;

public class Section {
   String name;

   // timeslots aren't filled immediately, they're done while parsing data.txt
   List<Timeslot> timeslots = new ArrayList<>();
}
