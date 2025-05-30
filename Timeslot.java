import java.util.BitSet;

public class Timeslot {
   String room;
   int sectionNumber;
   int enrolled;
   int limit;
   
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
   BitSet[] time = {new BitSet(166), new BitSet(166), new BitSet(166), new BitSet(166), new BitSet(166)};

   void parseTimeslotLine(String line) {
      BitSet[] time = {new BitSet(166), new BitSet(166), new BitSet(166), new BitSet(166), new BitSet(166)};
      
      // section 0 should be days, section 1 should be time, and section 2 is a duplicate of section 1
      String[] lineSections = line.split(",");

      String[] timeSection = lineSections[1].split(" – ");
      /* 
       * converting times to a range for the BitSet:
       * 8:30 AM would be 0, 8:35 AM would be 1, ..., 10:20 PM would be 165
       */
      // convert to 24 hr time
      int startTime = Integer.parseInt(timeSection[0].split(" ")[0].replaceFirst(":", ""));
      if (timeSection[0].split(" ")[1].equals("p.m")) startTime += 1200;

      int endTime = Integer.parseInt(timeSection[1].split(" ")[0].replaceFirst(":", ""));
      if (timeSection[1].split(" ")[1].equals("p.m")) endTime += 1200;

      // converting to a range for the BitSet
      startTime = (startTime - 830) / 5;
      endTime = (endTime - 830) / 5;

      String[] days = lineSections[0].split(" ");
      // for each day, fill that BitSet in ongoing
      for (String i : days) {
         int day;
         if (i.equals("M")) day = 0;
         else if (i.equals("Tu")) day = 1;
         else if (i.equals("W")) day = 2;
         else if (i.equals("Th")) day = 3;
         else day = 4; // F
         
         time[day].set(startTime, endTime);
      }

      this.time = time;
   }
}