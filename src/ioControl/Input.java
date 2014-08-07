package ioControl;
import java.io.*;
import java.util.*;

public interface Input {
   // revFirstMsg():
   // This method is called to receive the first
   // message from the referee.
   // It returns a FirstRecMsg object, which
   // contains the information parsed from the
   // first message.
   public ArrayList<String> revMsg(int which);
}

// a scanner version of input implementation
// buggy, cannot read more than one line
class ScannerInput implements Input{       
   private final boolean DEBUG = false;
   private final boolean DEBUG_FILE = false;
   
   // given which, return the arrayList of string
   // with multiple lines stored
   public ArrayList<String> revMsg(int which) {        
      Scanner sc = new Scanner(System.in);
      StringBuilder str = new StringBuilder();
      String holder;
      sc.useDelimiter("\n");

      while (sc.hasNext()) {
         holder = sc.next();
         str.append(holder);
         if (DEBUG_FILE) {
            File flog = new File("log" + (char)('0' + which));
            FileWriter fw;
            try {
               fw = new FileWriter(flog,true);
               fw.append(holder);
               fw.append("\n");
               fw.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }

      if (DEBUG) {
         //Print the input
         System.out.println(str);
      }
      return splitMsg(str.toString());
   }
   
   // given a string, combined with multiple messages
   // split it into multiple messages
   private ArrayList<String> splitMsg(String str) {
      ArrayList<String> array = new ArrayList<String>();
      int curPos;
      int len = str.length();
      int countLeft = 0, countRight = 0;
      int startIndex = 0;
      char ch;
      for (curPos = 0; curPos < len; ++curPos) {
         ch = str.charAt(curPos);
         if (ch == '(') {
            ++countLeft;
         }
         else if (ch == ')') {
            ++countRight;
         }
         if (countLeft > 0 && countRight > 0 
            && countLeft == countRight) {
            if (curPos != len - 1) {
               array.add(str.substring(startIndex, curPos + 1));
               startIndex = curPos + 1;
               countLeft = 0;
               countRight = 0;
            }
            else {
               array.add(str);
            }
         }
      }      
      return array;
   }
}

// the bufferedReader version of input implementation
// in use, no bugs, can read multiple lines with the help
// of ready() method
class LineInput implements Input{
   
   private final boolean DEBUG = false;
   private final boolean DEBUG_FILE = false;
   
   // given which, return the arrayList of string
   // with multiple lines stored
   public ArrayList<String> revMsg(int which) {        
      // Read the first message from standard input
      StringBuilder str = new StringBuilder();
      String holder;
        
      BufferedReader in = new BufferedReader(new 
                          InputStreamReader(System.in));
      try {
         holder = in.readLine();
         str.append(holder);
         if (DEBUG_FILE) {
            File flog = new File("log" + (char)('0' + which));
            FileWriter fw = new FileWriter(flog,true);
            fw.append(holder + " ");
            fw.append("\n");
            fw.close();
         }
         while (in.ready()) {
            int ch = in.read();
            if (ch != -1) {
               str.append((char)ch);
               if (DEBUG_FILE) {
                  File flog = new File("log" + (char)('0' + which));
                  FileWriter fw = new FileWriter(flog,true);
                  fw.append((char)ch);
                  fw.close();
               }
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      if (DEBUG) {
         //Print the input
         System.out.println(str);
      }        
      return splitMsg(str.toString());
   }
   
   // given a string may stored mutiple messages form referee
   // split them into different ones, given the message sent
   // from referee has no error
   private ArrayList<String> splitMsg(String str) {
      ArrayList<String> array = new ArrayList<String>();
      int curPos;
      int len = str.length();
      int countLeft = 0, countRight = 0;
      int startIndex = 0;
      char ch;
      for (curPos = 0; curPos < len; ++curPos) {
         ch = str.charAt(curPos);
         if (ch == '(') {
            ++countLeft;
         }
         else if (ch == ')') {
            ++countRight;
         }
         if (countLeft > 0 && countRight > 0 
            && countLeft == countRight) {
            if (curPos != len - 1) {
               array.add(str.substring(startIndex, curPos + 1));
               startIndex = curPos + 1;
               countLeft = 0;
               countRight = 0;
            }
            else {
               array.add(str);
            }
         }
      }       
      return array;
   }
}
