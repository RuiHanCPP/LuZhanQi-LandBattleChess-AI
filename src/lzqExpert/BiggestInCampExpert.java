package lzqExpert;
import java.util.ArrayList;

import boardConfig.Board;

/*
   Class : BiggestInCampExpert
   This class makes sure that there is always a high rank piece
   near our flag. 
*/
public class BiggestInCampExpert extends BaseExpert {
   BiggestInCampExpert(boolean rev, Board b) {
      super(rev, b);
   }
   
   public String nextMove(Board b) {
      ArrayList<Position> alp = b.getBiggestPPos();
      Position srcPos = alp.get(0);
      
      Position flagpos = getFlagPos();
      Position tgtPos = null;
      if (flagpos.RowNum == 1) {
         tgtPos = new Position(flagpos.RowNum + 1, flagpos.ColNum);
      }
      if (flagpos.RowNum == 12) {
         tgtPos = new Position(flagpos.RowNum - 1, flagpos.ColNum);
      }
      if (tgtPos != null ) {
         return shortestPath(b, srcPos, tgtPos);
      }
      return null;
   }
}
