package lzqExpert;

import boardConfig.Board;

public class Expert {
   
   // basic constructor, given if the initial config is
   // defending config or not, and the board
   private static boolean DEBUG = false;
   private boolean reverse;
   
   public Expert (boolean rev) {
      // For now it is empty
      reverse = rev;
   }
   
   // the expert system of the game AI
   // base on our rule base system, return
   public String nextMove(Board b) {
      String str = "";
      
      if (DEBUG) {
         BaseExpert expert = new TestExpert(false, b);
         str = expert.nextMove(b);
         
         DEBUG = false;
         return str;
      }
      
      // call our full attack expert to get the next move
      BaseExpert expert = new FullAttackExpert(reverse, b);
      str = expert.nextMove(b);
      
      return str;
   }
}


