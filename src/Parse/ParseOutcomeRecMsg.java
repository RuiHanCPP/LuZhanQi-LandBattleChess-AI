package Parse;

//This class is used to parse Outcome msg
public class ParseOutcomeRecMsg {
   //Parse function
   //Input: the String needs to be parsed
   //<outcome>   ::=  (outcome <result>)
   //<result>    ::=  <move>
   //            || <compare> <move>
    
   //Return: the base receive message
   //if the string is a correct syntax
   //Otherwise, return null;
   
   static private String str = null;
   static private String Comp = null;
   
   //Parse source position of the move
   //Returns the srcpos if the string is in correct format
   //Otherwise, returns null
   static public String ParseSrcPos() {
      if (str.charAt(0) != '(' || str.charAt(str.length() - 1) != ')') {
         return null;
      }
      if (str.length() >= 3) {
         String srcpos = str.subSequence(1, 4).toString();
         if (ParsePosition.IsPos(srcpos)) {
            str = str.subSequence(4, str.length()).toString();
            return srcpos;
         }
      }
      if (str.length() >= 2) {
         String srcpos = str.subSequence(1, 3).toString();
         if (ParsePosition.IsPos(srcpos)) {
            str = str.subSequence(3, str.length()).toString();
            return srcpos;
         }
      }
      return null;
   }
   
   //Parse destination position of the move
   //Returns the destpos if the string is in correct format
   //Otherwise, returns null
   static public String ParseDestPos() {
      if (str == null) {
         return str;
      }
      
      str = str.substring(0, str.length() - 1);
      if (ParsePosition.IsPos(str)) {
         return str;
      }
      return null;
   }
   
   //Getter method
   //Return the compare
   static public String ParseCompare() {
      return Comp;
   }
   
   //Set String
   //Gives a string
   //Set comp and the str after moving compare
   //according to its format
   static public void SetString(String s) {
      str = s;
      if (str.length() >= 15 &&
          str.subSequence(0, 8).equals("(outcome") &&
          str.charAt(str.length() - 1) == ')') {
         
         if (str.charAt(8) == '>' ||
             str.charAt(8) == '=' ||
             str.charAt(8) == '<') {
            Comp = Character.toString(str.charAt(8));
            str = str.subSequence(9, str.length() -1).toString();
         }
         else {
            Comp = null;
            str = str.subSequence(8, str.length() -1).toString();
         }
      }
   }
}
