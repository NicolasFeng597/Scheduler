import java.util.ArrayList;
import java.util.List;

public class Helper {
   // returns all combinations from the given course array with specified size r
   public static List<Course[]> combinations(List<Course> courseList, int r) {
      // number of total courses
      int n = courseList.size();
      
      // a list of all combinations, each one having length r
      List<Course[]> combinations = new ArrayList<>();

      /*
       * functions as a base n number of length r, with each digit 
       * representing the ith item in courseList
       * 
       * at every iteration, the number must be in ascending order with
       * no repeats
       * 
       * then, "add one" to the number every iteration, adding one to
       * the next digit after reaching the nth digit, but maintaining
       * ascending order, thus the last digit isn't set to 0 but the
       * next digit after the digit before it
       */ 
      
      
      return combinations;
   }
}
