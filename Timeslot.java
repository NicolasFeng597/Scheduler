import java.util.BitSet;

// individual timeslots of a certain section, such as L01, L02, B01, B02, etc.
public class Timeslot {
   String name; // the name of the timeslot, including the letter of the section and number, like "L01"
   String room; // the room assignment
   int timeslotNumber; // the unique number assigned to each timeslot by the registrar, like 22204 or 20672
   int enrolled; // the number of enrolled students in the timeslot
   int limit; // the limit to the number of students in the timeslot
   String parentCourse; // the course that this timeslot is a part of, like "EGR 156" or "COS 126"

   /*
    * the earliest a course can start is 8:30 AM, and the latest it can end is 10:20 PM
    * each course starts and ends at a time divisible by 5 minutes
    * thus, the entire day can be represented by (10:20 PM - 8:30 AM) / 5 minutes = 166 booleans
    * this boolean array can be used in each Timeslot class,
    * where each True indicates if a certain course is ongoing at that time
    * a school week can be represented by boolean[5][166]
    */
    
   // a BitSet is a long array of booleans, less computationally efficient but more easy to implement
   // each index is M, Tu, W, Th, F
   // TODO: more efficient to set empty BitSets to null, or can we just use BitSet.isEmpty()?
   // TODO: what if implemented as BitSet[] ongoing = BitSet[5];?
   BitSet[] time;
}