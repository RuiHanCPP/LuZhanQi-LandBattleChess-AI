package Parse;

//This class is used to parse Illegal msg
public class ParseIllegalRecMsg {
   
   //Parse function
   //Input: the String needs to be parsed
   //<illegal>   ::=  (illegal <move>)
   //<move>      ::=  ( <position> <position> )
   //<position>  ::=  
   //        |  A1 | A2 | A3 | A4 | A5 | A6 | A7 | A8 | A9 | A10 | A11 | A12
   //        |  B1 | B2 | B3 | B4 | B5 | B6 | B7 | B8 | B9 | B10 | B11 | B12
   //        |  C1 | C2 | C3 | C4 | C5 | C6 | C7 | C8 | C9 | C10 | C11 | C12
   //        |  D1 | D2 | D3 | D4 | D5 | D6 | D7 | D8 | D9 | D10 | D11 | D12
   //        |  E1 | E2 | E3 | E4 | E5 | E6 | E7 | E8 | E9 | E10 | E11 | E12
   
   //Return: the base receive message
   //if the string is a correct syntax
   //Otherwise, return null;
    
   static private String Pos;
   
   // Get Positions String
   // Inputs a string
   // sets the pos if it is in correct format
   // Otherwise, sets the pos to null
   static public void ParsePos(String str) {
      if (str.length() >= 15 &&
          str.subSequence(0, 9).toString().equals("(illegal(") &&
          str.charAt(str.length() - 1) == ')' &&
          str.charAt(str.length() - 2) == ')') {
         Pos = str.subSequence(9, str.length() - 2).toString();
      }
      else {
         Pos = null;
      }
   }
   
   //String ParseSrcPos():
   // Inputs a string
   // sets pos 
   // returns srcpos if it is in correct format
   // Otherwise, returns null
   static public String ParseSrcPos() {
      if (Pos == null) {
         return null;
      }
      if (Pos.length() >= 3) {
         String srcpos = Pos.subSequence(0, 3).toString();
         if (ParsePosition.IsPos(srcpos)) {
            Pos = Pos.subSequence(3, Pos.length()).toString();
            return srcpos;
         }
      }
      if (Pos.length() >= 2) {
         String srcpos = Pos.subSequence(0, 2).toString();
         if (ParsePosition.IsPos(srcpos)) {
            Pos = Pos.subSequence(2, Pos.length()).toString();
            return srcpos;
         }
      }
      return null;
   }
   
   //Parse DestPos
   // sets pos 
   // returns destpos if it is in correct format
   // Otherwise, returns null
   static public String ParseDestPos() {
      if (Pos == null) {
         return Pos;
      }
      
      if (ParsePosition.IsPos(Pos)) {
         return Pos;
      }
      return null;
   }
}
