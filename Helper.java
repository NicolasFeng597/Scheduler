import java.util.ArrayList;
import java.util.List;

public class Helper {
   // returns all combinations from the given course array with specified size r
    public static List<Course[]> combinations(List<Course> courseList, int r) {
        // number of total courses
        int n = courseList.size();
        
        // last item in courseList, used a lot so better to reference once
        Course last = courseList.get(n - 1);

        // a list of all combinations, each one having length r
        List<Course[]> combos = new ArrayList<>();

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
        
        // the current combination
        Course[] combo = new Course[r];

        // tracks the index of each course in combo
        int[] comboIndexes = new int[r];
        
        // initializes combo and comboIndexes to the 0th to r-1th items in courseList
        for (int i = 0; i < r; i++) {
            combo[i] = courseList.get(i);
            comboIndexes[i] = i;
        }

        while (true) {
            combos.add(combo);

            if (combo[r - 1] == last) {
                // from the second to last number place to the 0th number place:
                for (int i = r - 2; i >= 0; i--) {
                    // TODO: efficiency gain by adding a reverse-sorted combos list?
                    // for the x from last number place, if it isn't the x from last digit 
                    // in the "base"
                    if (combo[i] != courseList.get(n - r + i)) { // me when i'm unreadable-ah
                        // "add" 1 to combo[i] 
                        comboIndexes[i]++;
                        combo[i] = courseList.get(comboIndexes[i]);

                        // for all the slots after i, they equal combo[i] +1, +2, +3, etc.
                        
                    }
                }
            }

            // moves the last number place to the next digit in the "base"
            comboIndexes[r - 1]++;
            combo[r - 1] = courseList.get(comboIndexes[r - 1]);
        }

        // return combos;
 }
}
