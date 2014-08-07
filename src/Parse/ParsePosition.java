package Parse;

//This class is used to parse Position
public class ParsePosition {
   
   //This method is used to verify 
   //if the string in the position format
   //if it is, returns true, otherwise, returns false
   static public boolean IsPos(String str) {
      if (str.length() == 2) {
         if ((str.charAt(0) >= 'A' && str.charAt(0) <= 'E') &&
             (str.charAt(1) >= '1' && str.charAt(1) <= '9')){
            return true;
         }
      }
      if (str.length() == 3) {
         if ((str.charAt(0) >= 'A' && str.charAt(0) <= 'E') &&
             (str.charAt(1) == '1') &&
             (str.charAt(2) >= '0' && str.charAt(2) <= '2')){
            return true;
         }
      }
      return false;
   }
}
