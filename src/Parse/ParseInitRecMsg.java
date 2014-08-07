package Parse;

//This class is used to parse Init msg
public class ParseInitRecMsg {
    
   //Parse function
   //Input: the String needs to be parsed
   //<msg0>  ::=  (init <which> time/move <seconds>)
   //<which> ::=  1  |  2
   //<seconds> ::=  <digits>
   //              |  <digits> . <digits>
   //<digits>    ::=  <digit>
   //              |  <digit> <digits>
   //<digit>     ::=  0 | 1 | 2 | 3 | 4
   //                | 5 | 6 | 7 | 8 | 9
   //Return: the base receive message
   //if the string is a correct syntax
   //Otherwise, return null;

   //Parse which
   //If the string is a correct syntax
   //return the which.
   //Otherwise, return 0.
   static public int ParseWhich(String str) {
      int iWhich = 0;
        
      if (str.length() >= 7 &&
          str.subSequence(0, 5).equals("(init") ) {
         String which = Character.toString(str.charAt(5));
         try {
            iWhich = Integer.parseInt(which);
         } catch(NumberFormatException ea) {
            // empty
         }
      }
      return iWhich;
   }
    
   //parse time
   //If the string is a correct syntax
   //return the time.
   //Otherwise, return 0.0.
   static public double ParseTime(String str) {
      double dTime = 0.0;
        
      String sub1 = str.subSequence(6, 15).toString();
      if (str.length() >= 17 &&
          sub1.equals("time/move") &&
          str.charAt(str.length() - 1) == ')' ) {
         String time = str.subSequence(15,
                            str.length() - 1).toString();
         try {
            dTime = Double.parseDouble(time);
         } catch(NumberFormatException ea) {
            // empty
         } 
      }
      return dTime;
   }
}
