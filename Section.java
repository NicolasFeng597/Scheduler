import java.util.ArrayList;
import java.util.List;

public class Section {
   String sName;
   List<Timeslot> timeslots = new ArrayList<>();

   // sections aren't filled immediately, they're done while parsing data.txt
   public Section(String sName) {
      this.sName = sName;
   }
}
