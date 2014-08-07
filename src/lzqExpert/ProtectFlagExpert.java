package lzqExpert;

import java.util.ArrayList;

import boardConfig.Board;
import boardConfig.Piece;
import boardConfig.Piece.Rank;

/*
   Class : ProtectFlagExpert
   This class implements methods for protecting our flag.
   When there is an opponent piece near our flag,
   use the nearest highest rank piece to destroy it.
*/
public class ProtectFlagExpert extends BaseExpert {
   int oppRow = 0;
   int oppCol = 0;
   
   ProtectFlagExpert(boolean rev, Board b) {
      super(rev, b);
   }

   public String nextMove(Board b) {
      Piece tgt = FindOppPieceNearFlag(b);
      if (tgt != null) {
         Position tgtpos = new Position(oppRow, oppCol);
         ArrayList<Position> alp = getBgPiecesNrby(b,tgtpos);
         if (alp.size() > 0) {
            return posToString(alp.get(0), tgtpos);
         }
      }
      return null;
   }
   
   protected Piece FindOppPieceNearFlag(Board b) {
      Position flagpos = getFlagPos();
      int row = flagpos.getRow();
      int col = flagpos.getCol();

      oppRow = (row == 1) ? (row + 1): (row - 1);
      oppCol = 0;
      for(oppCol = col - 1; oppCol <= col + 1; oppCol++) {
         Piece piece = b.getPiece(oppRow, oppCol);
         if (piece.getOurSide() == false &&
             (piece.getRank() != Rank.Unknown || 
              piece.getRank() != Rank.Empty)) {
            return piece;
         }
      }
      return null;
   }
}
