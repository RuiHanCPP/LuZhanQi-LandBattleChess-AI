package lzqExpert;

import java.util.ArrayList;

import boardConfig.Board;
import boardConfig.Piece;
import boardConfig.Piece.Rank;

public class FullAttackExpert extends BaseExpert {
   
   // the col number of the right railway
   private final static int RightCol = 4;
   
   // the row number of the upper railway
   private final static int upperRailRow = 10;
   
   // the position of the enemyFlag, may not be true
   private static Position enemyFlag;
   
   // the position of our flag
   private static Position ourFlag;
   
   // the position of the camp that is right above our flag
   private static Position importantCamp;
   
   // the position of the left enemy HQ
   private final static Position enemyHQL = new Position(11, 1);
   // the position of the right enemy HQ
   private final static Position enemyHQR = new Position(11, 3);
   

   FullAttackExpert(boolean rev, Board b) {
      super(rev, b);
      findEnemyFlag(b);
   }

   @Override
   /*
      String nextMove(Board b):
      Given: The board
      Returns: Next move. in string.
      
      INVARIANTS OF THE ATTACKING AI:
      1. The initial config is set as attacking config. (Precon)
      2. (a) When the first-wave-attack(FWA) can be conducted, always do it.
         (b) When FWA cannot be conducted & second-wave-attack is available,
             do the SWA.
         (c) When neither FWA & SWA is available, conduct the basic
             defending measure.
         (d) When neither FWA & SWA is available, and defending measure 
             is done, do the all-attack module.
         (e) If none of the methods above are available, then ignore 
             any defending measure in the future.
         (f) If none of the methods, we would have no piece available, 
             RESIGN.   
   */
   public String nextMove(Board b) {
      
      if (b.getIsDefend()) {
         return null;
      }
      
      // if the strategy is attack at all costs
      // then attack when possible
      // our right-most four fighters are alive
      // attack along the right railway!
      if (b.getWeHaveSiling() || b.getWeHaveJunzhang() ||
          (b.getBiggestRank().equals(Rank.Bomb)) && 
          (findPiecePos(b, Rank.ShiZhang).size() == 2 ||
           findPiecePos(b, Rank.Bomb).size() == 2)) {
         return super.legalMove(b, firstWaveAttackMove(b));
      }
      
      // before we lose all two Brigadier General
      // unleash the second wave of attack!
      if (b.getBiggestRank().equals(Rank.Bomb) ||
          b.getBiggestRank().equals(Rank.ShiZhang) ||
          b.getBiggestRank().equals(Rank.LvZhang)) {
         return super.legalMove(b, secondWaveAttackMove(b));
      }
      
      // well, our attack earlier turned out to be a failure
      // need some basic defense measure
      setOurFlag(b);
      Piece p = 
         b.getPiece(importantCamp.getRow(), importantCamp.getCol());
      int r = importantCamp.getRow() - 1;
      int c = importantCamp.getCol();
      String str;
      if (!b.getPiece(r, c).getRank().equals(Rank.Empty) && 
          !b.getPiece(r, c).getOurSide()) {
         str = defendEnemy(b, r, c);
         if (str != null) {
            return super.legalMove(b, str);
         }
      }
      c = importantCamp.getCol() + 1;
      if (!b.getPiece(r, c).getRank().equals(Rank.Empty) && 
          !b.getPiece(r, c).getOurSide()) {
         str = defendEnemy(b, r, c);
         if (str != null) {
            return super.legalMove(b, str);
         }
      }
      c = importantCamp.getCol() - 1;
      if (!b.getPiece(r, c).getRank().equals(Rank.Empty) && 
          !b.getPiece(r, c).getOurSide()) {
         str = defendEnemy(b, r, c);
         if (str != null) {
            return super.legalMove(b, str);
         }
      }
      if (p.getRank().equals(Rank.Empty)) {
         return super.legalMove(b, captureImpCamp(b));
      }
      
      // all attack!
      str = allAttack(b);
      if (str != null) {
         return super.legalMove(b, str);
      }
      
      return null;
   }
   
   // given the board
   // update the position of the enemy's flag
   // this may be inaccurate due to insufficient information
   private static void findEnemyFlag(Board b) {
      int row, col;
      
      row = enemyHQL.getRow();
      col = enemyHQL.getCol();
      
      // if it is certain that enemy flag is on the left
      if (b.getPiece(row, col).getRank().equals(Rank.Flag) &&
          !b.getPiece(row, col).getOurSide()) {
         enemyFlag = new Position(row, col);
      }
      else {
         row = enemyHQR.getRow();
         col = enemyHQR.getCol();
         
         // if we haven't capture the right side HQ
         // then we just assume the flag is on the right side
         if (!b.getPiece(row, col).getOurSide() &&
             !b.getPiece(row, col).getRank().equals(Rank.Empty) && 
             b.getPiece(row, col).getcBeFlag()) {
            enemyFlag = new Position(row, col);
         }
         else {
            // this HQ is captured by us, or it is empty now,
            // the other HQ must have the flag
            row = enemyHQL.getRow();
            col = enemyHQL.getCol();
            enemyFlag = new Position(row, col);
         }
      }
   }
   
   // given the board
   // update the position of our flag
   private static void setOurFlag(Board b) {
      int row = 0;
      int col = 1;
      
      if (b.getPiece(row, col).getRank().equals(Rank.Flag) &&
          b.getPiece(row, col).getOurSide()) {
         ourFlag = new Position(row, col);
      }
      else {
         col = 3;
         if (b.getPiece(row, col).getRank().equals(Rank.Flag) &&
             b.getPiece(row, col).getOurSide()) {
            ourFlag = new Position(row, col);
         }
      }
       
      importantCamp = 
         new Position (ourFlag.getRow() + 2, ourFlag.getCol());
   }
   
   // Given the board, where we still have the first four pieces
   // which are in priority descending order, Field Marshal, General
   // the Major General with the highest row number, and the Bomb
   // with the highest row number.
   // The rules for attacking is as follows:
   // NOTE: All left right up and down directions is in our perspective.
   // 1.1 If we have engineers, and they can get to the upper railway
   //     attack the first one they can get to, if that piece can be landMine.
   // 1.2 If there is any opponent's piece on the right side railway,
   //     destroy it (go as far as possible on the right railway)
   // 2.  If the position is right below the assuming oppFlag position
   //     capture the flag.
   // 3.  Go to leftmost possible position on the upper railway.
   private String firstWaveAttackMove(Board b) {
      
      // first locate the position of the attacking piece
      Position attackPPos;
      ArrayList<Position> piecePositions;
      String str;
      
      if (b.getWeHaveSiling()) {
         attackPPos = super.findPiecePos(b, Rank.SiLing).get(0);
      }
      else if (b.getWeHaveJunzhang()) {
         attackPPos = super.findPiecePos(b, Rank.JunZhang).get(0);
      }
      else {
         piecePositions = super.findPiecePos(b, Rank.ShiZhang);
         if (piecePositions.size() == 1) {
            // the first Major General is gone
            
            attackPPos = super.findPiecePos(b, Rank.Bomb).get(0);
            if (attackPPos.getRow() == 1) {
               attackPPos = super.findPiecePos(b, Rank.Bomb).get(1);
            }
         }
         else{
            attackPPos = piecePositions.get(0);
            if (attackPPos.getRow() == 3 && attackPPos.getCol() == 3) {
               attackPPos = piecePositions.get(1);
            }
         }
      }
      // now the position of attacking piece is attackPPos
      
      if (attackPPos.getCol() == RightCol) {
         int i;
         for (i = RightCol; i > 0; --i) {
            if (b.getPiece(10, i).getOurSide() ||
                (!b.getPiece(10, i).getRank().equals(Rank.Empty) &&
                 b.getPiece(10, i).getcBeMine())) {
               break;
            }
         }
         if (i != 0 && !b.getPiece(10, i).getOurSide() && 
             !b.getPiece(10, i).getRank().equals(Rank.Empty) &&
             b.getPiece(10, i).getcBeMine()) {
            // rule 1.1, if we still have engineers
            for (int j = RightCol; j > 0; --j) {
               if (b.getPiece(5, j).getRank().equals(Rank.Engineer) &&
                   b.getPiece(5, j).getOurSide()) {
                  // rule 1.1 we have engineers!
                  str = shortestPath(
                        b, new Position(5, j), new Position(10, i));
                  if (str != null && posToString(new Position(5, j),
                                                 new Position(10, i)).
                                                 equals(str)) {
                     return str;
                  }
               }
            }
         }
         if (i != 0 && b.getPiece(10, i).getRank().equals(Rank.Engineer) &&
             b.getPiece(10, i).getOurSide()) {
            if (i == enemyFlag.getCol()) {
               return super.posToString(new Position(10, i), enemyFlag);
            }
            return super.posToString
                   (new Position(10, i), new Position(10, i - 1));
         }
         else if (attackPPos.getRow() != upperRailRow) {
            // we are in rule 1.2
            int row = attackPPos.getRow();
            for (; row < upperRailRow + 1; ++row) {
               if (!b.getPiece(row, RightCol).getRank().equals(Rank.Empty) &&
                   !b.getPiece(row, RightCol).getOurSide()) {
                  return super.posToString
                  (attackPPos, new Position(row, RightCol));
               }
            }
            return super.posToString
                   (attackPPos, new Position(upperRailRow, RightCol));
         }
         else {
            if (attackPPos.getRow() == enemyFlag.getRow() - 1 && 
                attackPPos.getCol() == enemyFlag.getCol()) {
               // we are in rule 2
               // which is unlikely to happen
               throw new IllegalArgumentException
               ("The right column has no HQ!");
            }
            else {
               // we are in rule 3
               int row = attackPPos.getRow();
               int col = attackPPos.getCol();
               for (; col >= 0; --col) {
                  if ((!b.getPiece(row, col).getRank().equals(Rank.Empty) &&
                       !b.getPiece(row, col).getOurSide()) || 
                       col == enemyFlag.getCol()) {
                     break;
                  }
               }
               return super.posToString
                      (attackPPos, new Position(row, col));
            }
         }
      }
      else {
         if (attackPPos.getRow() == enemyFlag.getRow() - 1 && 
             attackPPos.getCol() == enemyFlag.getCol()) {
            // we are in rule 2
            return super.posToString(attackPPos, enemyFlag);
         }
         else {
            // we are in rule 3
            int row = attackPPos.getRow();
            int col = attackPPos.getCol();
            for (; col >= 0; --col) {
               if ((!b.getPiece(row, col).getRank().equals(Rank.Empty) &&
                    !b.getPiece(row, col).getOurSide()) || 
                    col == enemyFlag.getCol()) {
                  break;
               }
            }
            return super.posToString
            (attackPPos, new Position(row, col));
         }
      }
   }

   // Given the board, where we still have Brigadier General
   // and the board is an attack configuration, attack at all costs. 
   // The rules for attacking is as follows:
   // 1.  If has major general and it is not on the right rail, move right.
   // 2.1 If the opponent's flag is on the left, check the right-most 
   //     maybe-mine pieces on the upper railway which is reachable with
   //     engineers that can reach that position.
   // 2.2 If has bomb and it is not on the right rail, move to E3.
   // 3.1 If has major general and it is on the right rail, using major
   //     general to attack.
   // 3.2 If has bomb and it is on the right rail and no major general left
   //     bomb is the attack piece.
   // 3.3 If no major general and bomb, the brigadier general with higher
   //     row number will be the attack piece.
   // 3.4 If only one brigadier general left, and it is on the down-right
   //     corner, then move up one position.
   // 3.5 If only one brigadier general left, and it is not on the 
   //     down-right corner, then it is the attack piece.
   // 4.  If there is any opponent's piece on the right side railway,
   //     destroy it using attack piece.
   // 5.1 If it is known that the enemy flag is on the left side;
   //     if the position is above the assuming oppFlag 
   //     position capture the flag.
   // 5.2 Go to leftmost possible position.
   // 6.  When the attack piece reached the upperRailRow, while still on
   //     the right railway, goes up to the up-right corner.
   // 7.  If the attack piece is on the up-right corner, go left.
   private String secondWaveAttackMove(Board b) {
      
      // first locate the position of the attacking piece
      Position attackPPos, movePiece;
      ArrayList<Position> piecePositions;
      String str;
      
      piecePositions = super.findPiecePos(b, Rank.ShiZhang);
      if (piecePositions.size() == 1 && 
          piecePositions.get(0).getRow() == 3 &&
          piecePositions.get(0).getCol() == 3) {
         // rule 1
         movePiece = piecePositions.get(0);
         return super.posToString(movePiece, new Position(3, RightCol));
      }
      
      int i;
      for (i = RightCol; i > 0; --i) {
         if (b.getPiece(10, i).getOurSide() ||
             (!b.getPiece(10, i).getRank().equals(Rank.Empty) &&
              b.getPiece(10, i).getcBeMine())) {
            break;
         }
      }
      if (i != 0 && !b.getPiece(10, i).getOurSide() && 
          !b.getPiece(10, i).getRank().equals(Rank.Empty) &&
          b.getPiece(10, i).getcBeMine()) {
         // rule 2.1, if we still have engineers
         for (int j = RightCol; j > 0; --j) {
            if (b.getPiece(5, j).getRank().equals(Rank.Engineer) &&
                b.getPiece(5, j).getOurSide()) {
               // rule 2.1 we have engineers!
               str = shortestPath(
                     b, new Position(5, j), new Position(10, i));
               if (super.posToString
                   (new Position(5, j), new Position(10, i)).equals(str)) {
                  return str;
               }
            }
         }
      }
      if (i != 0 && b.getPiece(10, i).getRank().equals(Rank.Engineer) &&
          b.getPiece(10, i).getOurSide()) {
         if (i == enemyFlag.getCol()) {
            return super.posToString(new Position(10, i), enemyFlag);
         }
         return super.posToString
                (new Position(10, i), new Position(10, i - 1));
      }
      piecePositions = super.findPiecePos(b, Rank.Bomb);
      if (piecePositions.size() == 1 && 
          piecePositions.get(0).getRow() <= 2 &&
          piecePositions.get(0).getCol() != RightCol) {
         
         // rule 2.2
         movePiece = piecePositions.get(0);
         str = super.shortestPath(b, movePiece, new Position(2, 4));
         if (str != null) {
            return str;
         }
         else {
            throw new IllegalArgumentException
            ("Unable to get there! This is impossible!");
         }
      }
      
      piecePositions = super.findPiecePos(b, Rank.ShiZhang);
      if (piecePositions.size() == 1 && 
          !(piecePositions.get(0).getRow() == 3 &&
          piecePositions.get(0).getCol() == 3)) {
         // rule 3.1
         attackPPos = piecePositions.get(0);
      }
      else {
         piecePositions = super.findPiecePos(b, Rank.Bomb);
         if (piecePositions.size() == 1 && 
             (piecePositions.get(0).getRow() > 2 ||
              piecePositions.get(0).getCol() == RightCol &&
              piecePositions.get(0).getRow() == 2)) {
            // rule 3.2
            attackPPos = piecePositions.get(0);
         }
         else {
            piecePositions = super.findPiecePos(b, Rank.LvZhang);
            if (piecePositions.size() == 2) {
               // rule 3.3
               if (piecePositions.get(0).getRow() > 
                   piecePositions.get(1).getRow()) {
                  attackPPos = piecePositions.get(0);
               }
               else {
                  attackPPos = piecePositions.get(1);
               }
            }
            else {
               movePiece = piecePositions.get(0);
               if (movePiece.getRow() == 0 && 
                   movePiece.getCol() == RightCol) {
                  // rule 3.4
                  return super.posToString
                         (movePiece, new Position(1, RightCol));
               }
               else {
                  // rule 3.5
                  attackPPos = piecePositions.get(0);
               }
            }
         }
      }
      
      // now the position of attacking piece is attackPPos
      
      if (attackPPos.getCol() == RightCol) {
         if (attackPPos.getRow() != upperRailRow) {
            if (attackPPos.getRow() !=  11) {
               // we are in rule 4
               int row = attackPPos.getRow();
               for (; row < upperRailRow + 1; ++row) {
                  if (!b.getPiece(row, RightCol)
                         .getRank().equals(Rank.Empty) &&
                      !b.getPiece(row, RightCol).getOurSide()) {
                     return super.posToString
                            (attackPPos, new Position(row, RightCol));
                  }
               }
               return super.posToString
                      (attackPPos, new Position(upperRailRow, RightCol));
            }
            else {
               // we are in rule 7
               int row = attackPPos.getRow();
               int col = attackPPos.getCol();
               return super.posToString
                      (attackPPos, new Position(row, col - 1));
            }
         }
         else {
            // on the upper row rail
            if (enemyFlag.getCol() == enemyHQL.getCol()) {
               // we are in rule 5
               if (attackPPos.getRow() == enemyFlag.getRow() - 1 && 
                   attackPPos.getCol() == enemyFlag.getCol()) {
                  // rule 5.1
                  return super.posToString(attackPPos, enemyFlag);
               }
               else {
                  // we are in rule 5.2
                  int row = attackPPos.getRow();
                  int col = attackPPos.getCol();
                  for (; col >= 0; --col) {
                     if ((!b.getPiece(row, col).getRank().equals(Rank.Empty)
                          && !b.getPiece(row, col).getOurSide()) || 
                          col == enemyFlag.getCol()) {
                        break;
                     }
                  }
                  return super.posToString
                         (attackPPos, new Position(row, col));
               }
            }
            else {
               // we are in rule 6
               int row = attackPPos.getRow();
               int col = attackPPos.getCol();
               return super.posToString
                      (attackPPos, new Position(row + 1, col));
            }
         }
      }
      else {
         // we are in rule 5
         if (attackPPos.getRow() == enemyFlag.getRow() - 1 && 
             attackPPos.getCol() == enemyFlag.getCol()) {
            // rule 5.1
            return super.posToString(attackPPos, enemyFlag);
         }
         else {
            // we are in rule 5.2
            int row = attackPPos.getRow();
            int col = attackPPos.getCol();
            for (; col >= 0; --col) {
               if ((!b.getPiece(row, col).getRank().equals(Rank.Empty) &&
                    !b.getPiece(row, col).getOurSide()) || 
                    col == enemyFlag.getCol()) {
                  break;
               }
            }
            return super.posToString
                   (attackPPos, new Position(row, col));
         }
      }
   }
   
   // given the board
   // get the string of move that capture the important camp
   private String captureImpCamp(Board b) {
      ArrayList<Position> bgList = b.getBiggestPPos();
      String str;
      for (int i = 0; i < bgList.size(); ++i) {
         str = super.shortestPath(b, bgList.get(i), importantCamp);
         if (str != null) {
            return str;
         }
      }
      
      // if can't find one to protect the important camp....
      // go attacking... we are losing anyway..
      return allAttack(b);
   }
   
   // if there is enemy at the three railway position near the 
   // flag, then use our "biggest" piece to destroy it!
   // return the move, if it is not null
   // the priority of defending is lower than first and second 
   // wave of attacking
   private String defendEnemy(Board b, int row, int col) {
      ArrayList<Position> arr = 
         super.getBgPiecesNrby(b, new Position(row, col));
      
      if (arr != null) {
         return super.posToString(arr.get(0), new Position(row, col));
      }
      else {
         return null;
      }
   }
   
   // if we lose all the major pieces, our only hope is to destroy the
   // enemy before he can pull back his troops to defend
   // so we will try to attack at all costs!
   private String allAttack(Board b) {
      
      int row, col;
      // we need a method for full assault
      // beware the cases of inaccessible moves
      
      // first locate the position of the attacking piece
      Position attackPPos = null;
      
      // from right to left, up to down
      // find the first piece that is not in defending position
      // and set it as our desperate attack piece
      for (row = 11; row > -1; --row) {
         for (col = 4; col > -1; --col) {
            if ((row != importantCamp.getRow() &&
                 col != importantCamp.getCol()) &&
                 b.getPiece(row, col).getOurSide() && 
                 Piece.movableRank(b.getPiece(row, col).getRank()) &&
                 new Position(row, col).
                    adjacentP(b, false, true).size() > 0) {
               attackPPos = new Position(row, col);
               break;
            }
         }
         if (col > -1) {
            // we've found a suitable attack piece
            // further searching is not needed
            break;
         }
      }
      
      // if we didn't find any attack piece available
      // simply move the defend piece, while continue
      // protecting the flag
      if (row == -1) {
         row = importantCamp.getRow();
         col = importantCamp.getCol();
         if (b.getPiece(row, col).getOurSide()) {
            return posToString
                   (importantCamp, new Position(row - 1, col));
         }
         return null;
      }
      
      // now the position of attacking piece is attackPPos
      // First along the right column to attack
      // if it reached the upper railway, turn left and attack along
      // the upper railway until it can get to the flag
      // if the attacking piece is right below the enemy's flag
      // capture it
      if (attackPPos.getCol() == RightCol) {
         // if the attack piece is on the right column
         if (attackPPos.getRow() != upperRailRow) {
            // not on upper railway
            row = attackPPos.getRow();
            for (; row < upperRailRow + 1; ++row) {
               // goes up as far as possible
               if (!b.getPiece(row, RightCol).getRank().equals(Rank.Empty) &&
                   !b.getPiece(row, RightCol).getOurSide()) {
                  return super.posToString
                         (attackPPos, new Position(row, RightCol));
               }
            }
            // or go to the corner of the right column and the upper railway
            return super.posToString
                   (attackPPos, new Position(upperRailRow, RightCol));
         }
         else {
            // the attack piece is on the upper rail way
            if (attackPPos.getRow() == enemyFlag.getRow() - 1 && 
                attackPPos.getCol() == enemyFlag.getCol()) {
               throw new IllegalArgumentException
               ("The right column has no HQ!");
            }
            else {
               // then it should turn left
               // or directly head for the enemy's flag
               row = attackPPos.getRow();
               col = attackPPos.getCol();
               for (; col >= 0; --col) {
                  if ((!b.getPiece(row, col).getRank().equals(Rank.Empty) &&
                       !b.getPiece(row, col).getOurSide()) || 
                       col == enemyFlag.getCol()) {
                     break;
                  }
               }
               if (col < 0 || col > 4) {
                  String str = shortestPath(b,
                        new Position
                        (row, attackPPos.getCol()), enemyFlag);
                  if (str == null) {
                     str = super.shortestPath(b,
                           new Position(row, attackPPos.getCol()), 
                           new Position(enemyFlag.getRow() - 1, 
                                        enemyFlag.getCol()));
                  }
                  return str;
               }
               return super.posToString
                      (attackPPos, new Position(row, col));
            }
         }
      }
      else {
         // the piece is not on the right column
         if (attackPPos.getRow() > 5) {
            // if the piece is on the enemy's region
            if (attackPPos.getRow() == enemyFlag.getRow() - 1 && 
                attackPPos.getCol() == enemyFlag.getCol()) {
               // right below the enemy's flag, capture it!
               return super.posToString(attackPPos, enemyFlag);
            }
            else {
               // head for enemy's flag
               row = attackPPos.getRow();
               col = attackPPos.getCol();
               for (; col >= 0; --col) {
                  if ((!b.getPiece(row, col).getRank().equals(Rank.Empty) &&
                       !b.getPiece(row, col).getOurSide()) || 
                       col == enemyFlag.getCol()) {
                     break;
                  }
               }
               if (col < 0 || col > 4) {
                  String str = shortestPath(b,
                                            new Position
                                            (row, attackPPos.getCol()), 
                                             enemyFlag);
                  if (str == null) {
                     str = shortestPath(b,
                           new Position(row, attackPPos.getCol()), 
                           new Position(enemyFlag.getRow() - 1, 
                                        enemyFlag.getCol()));
                  }
                  return str;
               }
               return super.posToString
                      (attackPPos, new Position(row, col));
            }
         }
         else {
            // go to the attack position
            return super.shortestPath
                   (b, attackPPos, new Position(5, RightCol));
         }
      }
   }
}
