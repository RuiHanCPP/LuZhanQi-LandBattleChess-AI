package Parse;

//This class is used to parse Flag msg
public class ParseFlagRecMsg {
   //Parse function
   //Input: the String needs to be parsed
   //<flag>      ::=  (flag <which> <position>)
   //<which>     ::=  1
   //                | 2
   //<position>  ::=  A1 | A2 | A3 | A4 | A5 | A6 |
   //              |  A7 | A8 | A9 | A10 | A11 | A12
   //              |  B1 | B2 | B3 | B4 | B5 | B6 
   //              | B7 | B8 | B9 | B10 | B11 | B12
   //              |  C1 | C2 | C3 | C4 | C5 | C6 
   //              | C7 | C8 | C9 | C10 | C11 | C12
   //              |  D1 | D2 | D3 | D4 | D5 | D6 
   //              | D7 | D8 | D9 | D10 | D11 | D12
   //              |  E1 | E2 | E3 | E4 | E5 | E6 
   //              | E7 | E8 | E9 | E10 | E11 | E12
   
   //Return: the base receive message
   //if the string is a correct syntax
   //Otherwise, return null;

   //Parse Which
   //Inputs a string, returns iWhich if the string can be parsed
   //Otherwise, return 0
   static public int ParseWhich(String str) {    
      int iWhich = 0;
           
      if (str.length() >= 9 &&
          str.subSequence(0, 5).equals("(flag")  &&
          str.charAt(str.length() - 1) == ')') {
         String which = Character.toString(str.charAt(5));   
         try {
            iWhich = Integer.parseInt(which);       
         } catch(NumberFormatException ea) {
            // for now it is empty
         }   
      }    
      return iWhich;  
   }
   
   //Parse Position
   //Inputs a string, returns pos if the string can be parsed
   //Otherwise, return null
   static public String ParsePos(String str) {
      if (str.length() >= 9 &&
          str.subSequence(0, 5).equals("(flag") &&
          str.charAt(str.length() - 1) == ')') {
         String pos = str.subSequence(6, str.length() - 1).toString();
         if (ParsePosition.IsPos(pos)) {
            return pos;
         }
      }   
      return null;
   }
}
