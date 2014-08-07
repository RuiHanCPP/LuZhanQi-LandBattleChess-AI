package RevMsg;

//Base Receive Message Class
//Other msg structur extends from this class
public abstract class BaseRevMsg {
   
   //a member variable int type,
   //referring to the msg type
   protected int type;

   //a member variable boolean type
   //referring to whether the init board should be reversed
   protected boolean bReverse;
   
   //In this construction method, we can set type.
   BaseRevMsg(int type, boolean bReverse) {
      this.type = type;
      this.bReverse = bReverse;
   }
   
   //reverse the pos
   protected String reversePos(String pos){
      if (pos == null) {
         return null;
      }

      if (pos.charAt(0) == 'A') {
         return pos.replace('A', 'E');
      }
      else if (pos.charAt(0) == 'E') {
         return pos.replace('E', 'A');
      }
      else if (pos.charAt(0) == 'B') {
         return pos.replace('B', 'D');
      }
      else if (pos.charAt(0) == 'D') {
         return pos.replace('D', 'B');
      }
      return pos;
   }
   
   //getter method
   //return the member variable "type"
   public int getType() {
      return type;
   }
   
   //getter method
   //return the member variable "bReverse"
   public boolean getReverse() {
      return bReverse;
   }
   
}

