package RevMsg;

//The flag message class
//This class is the data structure of message type"flag".
public class FlagRecMsg extends BaseRevMsg {

   //<flag>      ::=  (flag <which> <position>)
   //<which>     ::=  1
   //             | 2
   //<position>  ::=  
   //              |  A1 | A2 | A3 | A4 | A5 | A6 
   //              | A7  | A8 | A9 | A10 | A11 | A12
   //              |  B1 | B2 | B3 | B4 | B5 | B6 
   //              | B7 | B8 | B9 | B10 | B11 | B12
   //              |  C1 | C2 | C3 | C4 | C5 | C6 
   //              | C7 | C8 | C9 | C10 | C11 | C12
   //              |  D1 | D2 | D3 | D4 | D5 | D6 
   //              | D7 | D8 | D9 | D10 | D11 | D12
   //              |  E1 | E2 | E3 | E4 | E5 | E6 
   //              | E7 | E8 | E9 | E10 | E11 | E12
   private int which;
   private String pos;

   // In this construction method, we can set type, 
   // which and pos and bReverse.
   public FlagRecMsg(int type, int which, String pos, boolean bReverse) {
      super(type,bReverse);
      this.which = which;
      if (bReverse) {
         this.pos = reversePos(pos); 
      }
      else {
         this.pos = pos;
      }
   }

   //getter method
   //We can get which by calling this method
   public int GetWhich() {
      return which;
   }
   
   //getter method
   //We can get pos by calling this method
   public String GetPos() {
      return pos;
   }
}
