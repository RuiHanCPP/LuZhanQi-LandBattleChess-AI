package RevMsg;

//First Receive Message Class
//This class is the data structure of message type"Msg0".
public class Msg0RecMsg extends BaseRevMsg {

   //<msg0>  ::=  (init <which> time/move <seconds>)
   //<which> ::=  1  |  2
   //<seconds> ::=  <digits>
   //              |  <digits> . <digits>
   //<digits>    ::=  <digit>
   //              |  <digit> <digits>
   //<digit>     ::=  0 | 1 | 2 | 3 | 4
   //                | 5 | 6 | 7 | 8 | 9
   private int which;
   private double time;
   
   //In this construction method, we can set type, which, time and bReverse.
   public Msg0RecMsg(int type, int which, double time, boolean bReverse) {
      super(type, bReverse);
      this.type = type;
      this.which = which;
      this.time = time;    
   }
              
   //getter method
   //We can get which by calling this method
   public int GetWhich() {
      return which;    
   }   
    
   //getter method
   //We can get time by calling this method
   public double GetTime() {
      return time;    
   }
}

