package Parse;

//This class is used to parse a string
public class ParseString {

   //<whitespace>  <character tabulation>
   //| <linefeed> | <line tabulation> | <form feed>
   //| <carriage return> | <next line>
   //| <any character whose category is Zs, Zl, or Zp>
    
   //<character tabulation> : U+0009
   static protected char ct = 0X0009;
    
   //<linefeed> : U+000A
   static protected char lf = 0X000A;
    
   //<line tabulation> : U+000B
    static protected char tb = 0X000B;
    
   //<form feed> : U+000C
   static protected char fd = 0X000C;
    
   //<carriage return> : U+000D
   static protected char cr = 0X000D;
    
   //<next line> : U+0009
   static protected char nl = 0X0009;
    
   //<any character whose category is Zs, Zl, or Zp> :
   //                          U+0020 U+2028 U+2029
   static protected char zs = 0X0020;
   static protected char zl = 0X2028;
   static protected char zp = 0X2029;

   //Remove WhiteSpace
   //Inputs a string
   //Return the String without WhiteSpace
   static public String RemoveWhiteSpace(String str) {
      
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < str.length(); i++) {
         if (!isWhiteSpace(str.charAt(i))) {
            sb.append(str.charAt(i));
         }
      }
      return sb.toString();
   }
    
   //Verify if a character is a whitespace
   //Inputs a character, if it is a whitespace
   //return true
   //Otherwise, return false
   static protected boolean isWhiteSpace(char ch) {
      return (ch == ct || ch == lf || ch == tb ||
              ch == fd || ch == cr || ch == nl ||
              ch == zs || ch == zl || ch == zp);
   }
}
