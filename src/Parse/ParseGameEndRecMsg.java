package Parse;

//This class is used to parse GameEnd msg
public class ParseGameEndRecMsg {
   //Parse function
   //Input: the String needs to be parsed
   //<game_end>  ::=  (end <winner>)
   //<winner>    ::=  0
   //               | <which>
   //<which>     ::=  1
   //                |  2
   //Return: the base receive message
   //if the string is a correct syntax
   //Otherwise, return null;
    
   //Parse Winner
   //Gives a string
   //returns iWinner if it is in correct format
   //Otherwise, returns -1
   static public int ParseWinner(String str) {
      int iWinner = -1;    
      if (str.length() >= 4 &&
          str.subSequence(0, 4).equals("(end") &&
          str.charAt(str.length() - 1) == ')') {
         String winner = Character.toString(str.charAt(4));
         try {
            iWinner = Integer.parseInt(winner);
         } catch(NumberFormatException ea) {
            // empty
         }
      }
      return iWinner;
   }
}
