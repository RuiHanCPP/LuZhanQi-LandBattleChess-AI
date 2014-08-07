package lzqExpert;

import java.util.ArrayList;
import boardConfig.Board;

/*
   Class: KillChessExpert
   If we have Siling, then we use the Siling to attack.
*/
public class KillChessExpert extends BaseExpert {  
   KillChessExpert(boolean rev, Board b) {
      super(rev, b);
   }

   public String nextMove(Board b) {
      if (b.getWeHaveSiling() == true) {
         //bpp: Biggest piece position
         ArrayList<Position> bpp = b.getBiggestPPos();
         Position pos = bpp.get(0);
         //pap: Piece adjacent positions.
         ArrayList<Position> pap = pos.adjacentP(b, false, false);
         if (pap.size() > 0) {
            return posToString(pos, pap.get(0));
         }
      }
      return null;
   }
}
