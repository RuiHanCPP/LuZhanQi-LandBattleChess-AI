package RevMsg;

//The game end message class
//This class is the data structure of message type"game end".
public class GameEndRecMsg extends BaseRevMsg {

   //<game_end>  ::=  (end <winner>)
   //<winner>    ::=  0
   //             | <which>
   //<which>     ::=  1
   //             | 2
   private int winner;
   
   //In this construction method, we can set type, winner and bReverse.
   public GameEndRecMsg(int type, int winner, boolean bReverse) {
      super(type, bReverse);
      this.winner = winner;
   }
   
   //getter method
   //We can get winner by calling this method
   public int GetWinner() {
      return winner;    
   }  
   
}
