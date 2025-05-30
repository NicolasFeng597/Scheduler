import java.util.List;
import java.util.ArrayList;

public class Course {
   String name;
   
   // courses aren't filled immediately, they're done while parsing data.txt
   List<Section> sections = new ArrayList<>();
}
