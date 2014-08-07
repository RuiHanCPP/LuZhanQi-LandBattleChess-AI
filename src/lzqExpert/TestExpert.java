package lzqExpert;


import boardConfig.Board;
import boardConfig.Piece;
import boardConfig.Piece.Rank;

public class TestExpert extends BaseExpert {
   
   // this class is only for debugging

   TestExpert(boolean rev, Board b) {
      super(rev, b);
      boardInit(b);
   }
   
   public void boardInit(Board board) {
      board.setPiece(new Piece
            (Rank.Unknown, false, false, false, false), "A7");
      
      board.setBiggestP();
      board.settingBigPieces();
   }

   @Override
   public String nextMove(Board b) {
      
      BaseExpert expert = new FullAttackExpert(reverse, b);
      String str = expert.nextMove(b);
      System.out.println(b.getPiece(11, 3).getRank());
      
      return str;      
   }   
}
