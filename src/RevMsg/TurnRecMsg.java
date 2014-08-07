package RevMsg;

//The turn message class
//This class is the data structure of message type"Turn".
public class TurnRecMsg extends BaseRevMsg {
   
   //<msg0>  ::=  (go <which>)
   //<which> ::=  1  |  2
   private int which;
   
   //In this construction method, we can set type, which and bReverse.
   public TurnRecMsg(int type, int which, boolean bReverse) {
      super(type, bReverse);
      this.which = which;
   }

   //getter method
   //We can get which by calling this method
   public int GetWhich() {
      return which;
   }
}
