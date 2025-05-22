import java.util.List;
import java.util.ArrayList;

public class Course {
   String sName;
   List<Section> sections = new ArrayList<>();

   // courses aren't filled immediately, they're done while parsing data.txt
   public Course(String sName) {
      this.sName = sName;
   }
}
