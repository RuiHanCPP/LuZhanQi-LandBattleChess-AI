package Parse;

//This class is used to parse turn msg
public class ParseTurnRecMsg {
   //Parse function
   //Input: the String needs to be parsed
   //<msg0>  ::=  (go <which>)
   //<which> ::=  1  |  2
    
   //Return: the base receive message
   //if the string is a correct syntax
   //Otherwise, return null;
   
   //Parse which
   //If the string is a correct syntax
   //return the which.
   //Otherwise, return 0.
   static public int ParseWhich(String str) {
      int iWhich = 0;        
      if (str.length() >= 5 &&
          str.subSequence(0, 3).equals("(go") &&
          str.charAt(str.length() - 1) == ')' )  {
         String which = Character.toString(str.charAt(3));
         try {
            iWhich = Integer.parseInt(which);
         } catch(NumberFormatException ea) {
            // empty
         }
      }
      return iWhich;
   }
}
