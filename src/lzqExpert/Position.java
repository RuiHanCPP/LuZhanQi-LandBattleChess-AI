 package lzqExpert;

import java.util.ArrayList;
import java.util.HashMap;
import boardConfig.Board;
import boardConfig.Piece.Rank;

public class Position extends Object {
   private int row;
   private int col;
   
   final int ColNum = 5; // the number of the columns
   final int RowNum = 12; // the number of the rows
   
   // constructor of position
   public Position(int r, int c) {
      row = r;
      col = c;
   }
   
   // default constructor
   public Position() {
      row = 0;
      col = 0;
   }
   
   // getter
   public int getRow() {
      return row;
   }
   public int getCol() {
      return col;
   }
   
   // returns true if the position is on the parallel railways
   public boolean onRowRail() {
      // if the row is railway
      
      if (row == 1 || row == 5 || row == 6 || row == 10) {
         return true;
      }
      else {
         return false;
      }
   }
   
   // return true if the position is on the vertical railways
   public boolean onColumnRail() {
      // if the column is railway
      
      if (col == 0 || col == 4) {
         if (row != 0 && row != 11) {
            return true;
         }
         else {
            return false;
         }
      }
      else if (col == 2 && (row == 5 || row == 6)) {
         return true;
      }
      else {
         return false;
      }
   }
   
   // returns true if the given position is a camp
   public boolean isCamp() {
      // decide if a position is a camp
      
      if (((row == 2 || row == 4 || row == 7 || row == 9) && 
            (col == 1 || col == 3)) || (row == 3 && col == 2) ||
            (row == 8 && col == 2)) {
         return true;
      }
      else {
         return false;
      }
   }
   
   /*
   void pAdd
   (ArrayList<Position> array, int r, int c, Board b, boolean side):
      Given: An array list of positions.
             The row & col number of a position.
             A board.
             The side of this given position.
      Effect: Add the new position to the array if the array does not
              contain this position.
   */
   private void pAdd
   (ArrayList<Position> array, int r, int c, Board b, boolean side) {
      Position p = new Position(r, c);
      if (b.getPiece(r, c).getRank().equals(Rank.Empty) ||
          (b.getPiece(r, c).getOurSide() != side && !p.isCamp())) {
         array.add(p);
      }
   }
   
   /*
   ArrayList<Position> adjacentP
   (Board b, boolean isEngineer, boolean pieceSide) :
      Given: A board.
             If this piece is an Engineer.
             If this piece is ours.
      Precondition: The current position must have a piece on it and
                    must be movable.
      Returns: The array list of positions that the piece can reach.
   */
   public ArrayList<Position> adjacentP
   (Board b, boolean isEngineer, boolean pieceSide) {
      // return the list of positions which adjacent to
      // the current one
      
      ArrayList<Position> array = new ArrayList<Position>();
      
      if (this.onColumnRail()) {
         Rank r = null;
         
         // on the mid-railway
         // add the possible position to the adjacent array
         if (col == 2 && (row == 5 || row == 6)) {
            if (row == 5) {
               r = b.getPiece(6, col).getRank();
               if (r.equals(Rank.Empty)) {
                  array.add(new Position(6, col));
               }
               else {
                  if (b.getPiece(6, col).getOurSide() != pieceSide) {
                     array.add(new Position(6, col));
                  }
               }
            }
            else {
               r = b.getPiece(5, col).getRank();
               if (r.equals(Rank.Empty)) {
                  array.add(new Position(5, col));
               }
               else {
                  if (b.getPiece(5, col).getOurSide() != pieceSide) {
                     array.add(new Position(5, col));
                  }
               }
            }
         }
         else {
            // on the rest of the column railway
            // adding all the reachable position on the column
            // to the array
            for (int num = row + 1; num < 11; ++num) {
               r = b.getPiece(num, col).getRank();
               if (r.equals(Rank.Empty)) {
                  array.add(new Position(num, col));
               }
               else {
                  if (b.getPiece(num, col).getOurSide() != pieceSide) {
                     array.add(new Position(num, col));
                  }
                  break;
               }
            }
            for (int num = row - 1; num > 0; --num) {
               r = b.getPiece(num, col).getRank();
               if (r.equals(Rank.Empty)) {
                  array.add(new Position(num, col));
               }
               else {
                  if (b.getPiece(num, col).getOurSide() != pieceSide) {
                     array.add(new Position(num, col));
                  }
                  break;
               }
            }
         }
      }
      
      if (this.onRowRail()) {
         Rank r = null;
         
         // add all the reachable position on the row railway
         // to the array
         for (int num = col + 1; num < 5; ++num) {
            r = b.getPiece(row, num).getRank();
            if (r.equals(Rank.Empty)) {
               array.add(new Position(row, num));
            }
            else {
               if (b.getPiece(row, num).getOurSide() != pieceSide) {
                  array.add(new Position(row, num));
               }
               break;
            }
         }
         for (int num = col - 1; num >= 0; --num) {
            r = b.getPiece(row, num).getRank();
            if (r.equals(Rank.Empty)) {
               array.add(new Position(row, num));
            }
            else {
               if (b.getPiece(row, num).getOurSide() != pieceSide) {
                  array.add(new Position(row, num));
               }
               break;
            }
         }
      }
      
      if (isEngineer) {
         // engineers feel so special that
         // they asked for special moves
         int i;
         
         for (i = 0; i < array.size(); ++i) {
            if (array.get(i).onColumnRail() && 
                array.get(i).onRowRail()) {
               break;
            }
         }
         
         // check if the array contains a turning point
         // of the two railway, if yes, then the engineer
         // can turn around on the railway
         if (i != array.size()) {
            this.posAddEngineer(array, b);
         }
      }
      
      // Connect the special positions that is adjacent but
      // not vertical or horizontal adjacent to the given position
      // this part of the code is hard written mostly because
      // it is very hard to come up with a general rules for
      // these adjacent situations,
      // so the code may be very ugly and hard to maintain.
      if ((row < 6 && row > 0) || (row > 6 && row <= 11)) {
         this.pAdd(array, row - 1, col, b, pieceSide);
      }
      if ((row >= 0 && row < 5) || (row > 5 && row < 11)) {
         this.pAdd(array, row + 1, col, b, pieceSide);
      }
      if (col > 0 && col <= 11) {
         this.pAdd(array, row, col - 1, b, pieceSide);
      }
      if (col >= 0 && col < 4) {
         this.pAdd(array, row, col + 1, b, pieceSide);
      }
      if (isCamp()) {
         this.pAdd(array, row + 1, col + 1, b, pieceSide);
         this.pAdd(array, row + 1, col - 1, b, pieceSide);
         this.pAdd(array, row - 1, col + 1, b, pieceSide);
         this.pAdd(array, row - 1, col - 1, b, pieceSide);
      }
      else {
         if (col == 0 && 
             (row == 1 || row == 3 || row == 6 || row == 8)) {
            this.pAdd(array, row + 1, col + 1, b, pieceSide);
         }
         else if (col == 2 && (row == 1 || row == 6)) {
            this.pAdd(array, row + 1, col + 1, b, pieceSide);
         }
         
         if (col == 4 && 
             (row == 1 || row == 3 || row == 6 || row == 8)) {
            this.pAdd(array, row + 1, col - 1, b, pieceSide);
         }
         else if (col == 2 && (row == 1 || row == 6)) {
            this.pAdd(array, row + 1, col - 1, b, pieceSide);
         }
         
         if (col == 4 && 
             (row == 10 || row == 8 || row == 5 || row == 3)) {
            this.pAdd(array, row - 1, col - 1, b, pieceSide);
         }
         else if (col == 2 && (row == 5 || row == 10)) {
            this.pAdd(array, row - 1, col - 1, b, pieceSide);
         }
         
         if (col == 0 && 
             (row == 10 || row == 8 || row == 5 || row == 3)) {
            this.pAdd(array, row - 1, col + 1, b, pieceSide);
         }
         else if (col == 2 && (row == 5 || row == 10)) {
            this.pAdd(array, row - 1, col + 1, b, pieceSide);
         }
         
         if (row == 5 && col == 2) {
            this.pAdd(array, row + 1, col, b, pieceSide);
         }
         else if (row == 6 && col == 2) {
            this.pAdd(array, row - 1, col, b, pieceSide);
         }
      }
      
      return array;
   }
   
   
   // return true if the position is HQ
   public boolean isHQ() {
      if ((row == 0 || row == 11) && (col == 1 || col == 3)) {
         return true;
      }
      else {
         return false;
      }
   }
   
   /*
   posAddEngineer(ArrayList<Position> array, Board b):
      Given: An array list of Position.
             A board.
      Caution: This method is called when an Engineer can turn around.
      Effect: Update the array List with all the positions that
              the position, which is occupied by our engineer
              can reach on the board
   */
   private void posAddEngineer(ArrayList<Position> array, Board b) {
      HashMap<Position, Object> map = new HashMap<Position, Object>();
      int i;
      int r, c;
      Position p1 = null, p2 = null;
      Rank rank = null;
      
      for (i = 0; i < array.size(); ++i) {
         // add all the positions in the direct adjacent array
         // into the hash map, with its value set to be null
         if (!map.containsKey(array.get(i))) {
            map.put(array.get(i), null);
         }
      }
      for (i = 0; i < array.size(); ++i) {
         if (array.get(i).onColumnRail() && array.get(i).onRowRail() &&
             b.getPiece(array.get(i).getRow(), array.get(i).getCol())
               .getRank().equals(Rank.Empty)) {
            // the current pos is a turning point on the railway
            p1 = null; // search for turning point above the current pos
            p2 = null; // search for turning point below the current pos
            boolean canTurn = false;
            r = array.get(i).getRow();
            c = array.get(i).getCol();
            if (r < RowNum - 1) {
               p1 = new Position(r + 1, c);
               if (p1.onColumnRail() && 
                   !b.getPiece(p1.getRow(), p1.getCol()).getOurSide()) {
                  // if the engineer can turn around on this turning point
                  // which indicates that the positions on the both sides of
                  // the turning point is reachable for our engineer
                  canTurn = true;
               }
               else {
                  p1 = null;
               }
            }
            else {
               p1 = null;
            }
            if (r > 0) {
               p2 = new Position(r - 1, c);
               if (p2.onColumnRail() && 
                   !b.getPiece(p2.getRow(), p2.getCol()).getOurSide()) {
                  // if the engineer can turn around on this turning point
                  // which indicates that the positions on the both sides of
                  // the turning point is reachable for our engineer
                  canTurn = true;
               }
               else {
                  p2 = null;
               }
            }
            else {
               p2 = null;
            }
            
            if (canTurn) {
               
               // the engineer can definitely make turns here
               if ((p1 == null || !map.containsKey(p1)) && 
                    (p2 == null || !map.containsKey(p2)) &&
                     p1 != p2) {
                  for (int num = r + 1; num < 11; ++num) {
                     // goes up, and add all the reachable points
                     // that was not reachable before turning
                     // in to the map
                     p1 = new Position(num, c);
                     if (!p1.onColumnRail()) { break;}
                     rank = b.getPiece(num, c).getRank();

                     if (rank.equals(Rank.Empty)) {
                        array.add(p1);
                        map.put(p1, null);
                     }
                     else {
                        if (!b.getPiece(num, c).getOurSide() &&
                            !map.containsKey(p1)) {
                           array.add(p1);
                           map.put(p1, null);
                        }
                        break;
                     }
                  }
                  for (int num = r - 1; num > 0; --num) {
                     // goes down, and add all the reachable points
                     // that was not reachable before turning
                     // in to the map
                     rank = b.getPiece(num, c).getRank();
                     p1 = new Position(num, c);
                     if (!p1.onColumnRail()) { break;}

                     if (rank.equals(Rank.Empty)) {
                        array.add(p1);
                        map.put(p1, null);
                     }
                     else {
                        if (!b.getPiece(num, c).getOurSide() &&
                            !map.containsKey(p1)) {
                           array.add(p1);
                           map.put(p1, null);
                        }
                        break;
                     }
                  }
               }
            }
            
            canTurn = false;
            
            if (c < ColNum - 1) {
               p1 = new Position(r, c + 1);
               // search for the turning point left of the current pos
               if (p1.onRowRail() && 
                   !b.getPiece(p1.getRow(), p1.getCol()).getOurSide()) {
                  canTurn = true;
               }
               else {
                  p1 = null;
               }
            }
            else {
               p1 = null;
            }
            if (c > 0) {
               p2 = new Position(r, c - 1);
               // search for the turning point left of the current pos
               if (p2.onRowRail() && 
                   !b.getPiece(p2.getRow(), p2.getCol()).getOurSide()) {
                  canTurn = true;
               }
               else {
                  p2 = null;
               }
            }
            else {
               p2 = null;
            }
            
            if (canTurn) {
               
               // the engineer can definitely make turns here
               if ((p1 == null || !map.containsKey(p1)) && 
                    (p2 == null || !map.containsKey(p2)) &&
                     p1 != p2) {
                  for (int num = c + 1; num < 5; ++num) {
                     // goes right, and add all the reachable points
                     // that was not reachable before turning
                     // in to the map
                     rank = b.getPiece(r, num).getRank();
                     p1 = new Position(r, num);
                     if (rank.equals(Rank.Empty)) {
                        array.add(p1);
                        map.put(p1, null);
                     }
                     else {
                        if (!b.getPiece(r, num).getOurSide() &&
                            !map.containsKey(p1)) {
                           array.add(p1);
                           map.put(p1, null);
                        }
                        break;
                     }
                  }
                  for (int num = c - 1; num >= 0; --num) {
                     // goes left, and add all the reachable points
                     // that was not reachable before turning
                     // in to the map
                     rank = b.getPiece(r, num).getRank();
                     p1 = new Position(r, num);
                     if (rank.equals(Rank.Empty)) {
                        array.add(p1);
                        map.put(p1, null);
                     }
                     else {
                        if (!b.getPiece(r, num).getOurSide() &&
                            !map.containsKey(p1)) {
                           array.add(p1);
                           map.put(p1, null);
                        }
                        break;
                     }
                  }
               }
            }
         }
      }
   }
   
   // equal method
   // for comparing and hash code calculating
   public boolean equals(Object obj) {
      if (getClass() == obj.getClass()) {
         // type is matched
         final Position p = (Position) obj;
         if (p.getCol() == col && p.getRow() == row) {
            // same row and column number indicates that the 
            // two positions are equal
            return true;
         }
         else {
            return false;
         }
      }
      else {
         return false;
      }
   }
   
   // my hash code
   public int hashCode() {
      return row * 31 + col * 131;
   }
}
