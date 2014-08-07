package lzqExpert;

import java.util.ArrayList;

import boardConfig.Board;
import boardConfig.Piece;
import boardConfig.Piece.Rank;

/*
   Class : BombExpert
   This class makes it possible that when one of our piece,
   whose rank is greater than "TuanZhang", is killed, then
   we use Bomb to kill the opponent piece.
*/
public class BombExpert extends BaseExpert {
   BombExpert(boolean rev, Board b) {
      super(rev, b);
   }

   public String nextMove(Board b) {
      Piece lastPieceLost = b.getLastLostPc();
      if (Piece.rankGreaterThan(lastPieceLost.getRank(), Rank.TuanZhang)) {
         Position lastPieceLostPos = b.getLastLostPos();
         ArrayList<Position> alp = getBgPiecesNrby(b,lastPieceLostPos);
         for (int i = 0; i < alp.size(); i++) {
            int srcRow = alp.get(i).RowNum;
            int srcCol = alp.get(i).ColNum;
            Piece p = b.getPiece(srcRow, srcCol);
            if (p.getRank() == Rank.Bomb ) {
               Position src = new Position(srcRow, srcRow);
               return posToString(src, lastPieceLostPos);
            }
         }
      }
      return null;
   }
}
