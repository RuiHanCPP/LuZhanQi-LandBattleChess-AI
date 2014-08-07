package lzqExpert;

import java.util.ArrayList;

import boardConfig.Board;
import boardConfig.Piece;
import boardConfig.Piece.Rank;

/*
    Class : KillEngineerExpert
    If the last moving opponent piece is an Engineer,
    then kill it.
*/
public class KillEngineerExpert extends BaseExpert {
   
   KillEngineerExpert(boolean rev, Board b) {
      super(rev, b);
   }

   public String nextMove(Board b) {
      Piece LastLostPc = b.getLastLostPc();
      if (LastLostPc.getRank() == Rank.LandMine) {
         Position lastPieceLostPos = b.getLastLostPos();
         ArrayList<Position> alp = getSmPiecesNrby(b,lastPieceLostPos);
         if (alp.size() > 0) {
            Position srcPos = alp.get(0);
            return posToString(srcPos, lastPieceLostPos);
         }
      }
      return null;
   }
}
