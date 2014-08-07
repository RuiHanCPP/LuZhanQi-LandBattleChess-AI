package lzqExpert;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import boardConfig.Board;
import boardConfig.Piece;
import boardConfig.Piece.Rank;

public abstract class BaseExpert{
   
   final static int ColNum = 5; // the number of the columns
   final static int RowNum = 12; // the number of the rows
   private Position flagPos; // the position of the flag
   final static boolean DEBUG = false;
   final static boolean TEST = false;
   protected static boolean reverse;
    
   BaseExpert(boolean rev, final Board b) {
      if (b.getPiece(0, 1).getRank().equals(Rank.Flag)) {
         flagPos = new Position(0, 1);
      }
      else {
         flagPos = new Position(0, 3);
      }
      
      reverse = rev;
   }
   
   // implement this!
   // return the string of next move
   abstract public String nextMove(final Board b); 

   // given the board, an piece that is on our side, and
   // the tgt position(doesn't have to be empty), get the
   // array of positions that the piece has to move to
   // before one move away from tgt position
   // return the output string of the movement
   // (may need to move other pieces)
   protected String 
   shortestPath(final Board b, final Position src, final Position tgt) {
      
      Piece piece = b.getPiece(src.getRow(), src.getCol());
      int i = 0, j = 0;
      
      if (!piece.getOurSide()) {
         throw new IllegalArgumentException("MUST BE OUR PIECE TO MOVE");
      }
      
      Rank rank = piece.getRank();
      Position piecePos = src;
      ArrayList<Position> pArray = 
         piecePos.adjacentP
         (b, rank.equals(Rank.Engineer), piece.getOurSide());
      // pArray: the array of positions that the given piece can 
      //         reach within one move
      
      if (DEBUG) {
         System.out.print(src.getRow());
         System.out.print(",");
         System.out.println(src.getCol());
         System.out.println(rank);
         for (i = 0; i < pArray.size(); ++i) {
            System.out.print(pArray.get(i).getRow());
            System.out.print(",");
            System.out.println(pArray.get(i).getCol());
         }
      }
      
      // the target is empty only when it is empty, or
      // an enemy piece that we can attack
      boolean tgtEmpty = (b.getPiece
      (tgt.getRow(),tgt.getCol()).getRank().equals(Rank.Empty) ||
      !b.getPiece(tgt.getRow(),tgt.getCol()).getOurSide());

      ArrayList<Position> endpArray = null;
      
      
      if (isAdjacent(pArray, piecePos, tgt)) {
         // if the target position is in our adjacent array of positions
         // may need to move another piece if the target piece is not empty
         if (DEBUG) {
            System.out.println("isAdjacent");
            System.out.print(tgtEmpty);
         }
         
         if (tgtEmpty) {
            // if it's "empty", just move to that position
            return posToString(src, tgt);
         }
         else {
            // if not then we need to move the piece on the target position
            // without revealing to the enemy that this piece is an
            // engineer or not
            endpArray = tgt.adjacentP(b, false, piece.getOurSide());
            for (i = 0; i < endpArray.size(); ++i) {
               if (pArray.contains(endpArray.get(i)) ||
                   !b.getPiece(endpArray.get(i).getRow(), 
                               endpArray.get(i).getCol()).getRank().
                               equals(Rank.Empty)) {
                  break;
               }
            }
            // found a position that this position can move to
            // here this piece should not attack any enemy pieces
            if (i == endpArray.size()) {
               // if the piece on the target position cannot move
               // then just move the given piece to the position
               // that is nearest to the target one
               for (i = 0; i < pArray.size(); ++i) {
                  if (distTwoPos(pArray.get(i), tgt) == 1 && 
                      endpArray.contains(pArray.get(i))) {
                     return posToString(src, pArray.get(i));
                  }
               }
            }
            if (endpArray.size() > 0) {
               // if the piece on the target position can move away
               // let it move
               return posToString(tgt, endpArray.get(0));
            }
         }
      }
      
      // here the target position is not adjacent to the current one
      HashMap<Position, Position> map = new HashMap<Position, Position>();
      
      if (!tgtEmpty) {
         endpArray = tgt.adjacentP(b, false, piece.getOurSide());
      }
      
      // the array list of position that we can reach
      ArrayList<Position> rPosList = new ArrayList<Position>();
      
      rPosList.add(piecePos);
      map.put(piecePos, null);
      for (j = 0; j < rPosList.size(); ++j) {
         // for each position, add it's nearby position that is reachable
         // which means the position is empty, or is enemy pieces
         // no further consideration is needed for enemy pieces, or,
         // attack only once, in this array of positions
         piecePos = rPosList.get(j);
         pArray = piecePos.adjacentP
         (b, rank.equals(Rank.Engineer), piece.getOurSide());
         for (i = 0; i < pArray.size(); ++i) {
            if (!map.containsKey(pArray.get(i))) {
               // the position is not in the hash map, add it
               if (DEBUG) {
                  System.out.println("not contain");
               }
               map.put(pArray.get(i), piecePos);
               rPosList.add(pArray.get(i));
            }
         }
         if (tgtEmpty) {
            if (map.containsKey(tgt)) {
               // if we add the target position into the array
               // using BFS, then we've got (one of) the shortest
               // path from the given position to the target one
               break;
            }
         }
         else {
            for (i = 0; i < endpArray.size(); ++i) {
               if (map.containsKey(endpArray.get(i)) && 
                   b.getPiece(endpArray.get(i).getRow(), 
                              endpArray.get(i).getCol()).getRank().
                              equals(Rank.Empty)) {
                  // for the cases that the target position is not empty
                  // we need to check if we can reach a position that is
                  // adjacent to the target position
                  // if we found such one, searching finished!
                  break;
               }
            }
            if (i != endpArray.size()) {
               // finish searching
               break;
            }
         }
         if (DEBUG) {
            System.out.print(j);
            System.out.print(",");
            System.out.println(rPosList.size());
         }
      }
      
      if (j != rPosList.size()) {
         // if we didn't wind up searching all the reachable position
         // yet getting no result
         pArray = new ArrayList<Position>();
         if (tgtEmpty) {
            pArray.add(tgt);
         }
         else {
            for (j = 0; j < endpArray.size(); ++j) {
               if (map.containsKey(endpArray.get(j))) {
                  // make sure we've found a path to a position
                  // adjacent to the target one if it is not empty
                  break;
               }
            }
            pArray.add(endpArray.get(j));
         }
         
         for (i = 0; piecePos != null; ++i) {
            // the map containing the key, which is it's position
            // and the value, which is the precedent position
            // of the position as key
            pArray.add(piecePos);
            piecePos = map.get(piecePos);
         }
         
         return posToString(pArray);
      }
      
      // path not found
      return null;
   }
   
   
   // given pArray, which is the adjacent list of src position
   // and position target
   // to see if src position and tgt position is adjacent
   private boolean isAdjacent
   (final ArrayList<Position> pArray, final Position src, 
    final Position tgt) {
      
      int i;
      for (i = 0; i < pArray.size(); ++i) {
         if (pArray.get(i).equals(tgt)) {
            // found the target position in source position's 
            // adjacent array of positions
            break;
         }
      }
      if (i == pArray.size()) {
         if ((distTwoPos(src, tgt) == 1) && 
              (src.getRow() + tgt.getRow() != 11 || 
               src.getCol() + tgt.getCol() != 2 &&
               src.getCol() + tgt.getCol() != 6)){
            // nearby, but occupied by pieces from same sides
            return true;
         }
         else if ((distTwoPos(src, tgt) == 2) && 
               (src.getRow() != tgt.getRow()) &&
               (src.getCol() != tgt.getCol()) &&
               (src.isCamp() || tgt.isCamp())) {
            // nearby, but occupied by pieces from same sides
            
            return true;
         }
         return false;
      }
      else {
         return true;
      }
   }
   
   // given a ArrayList of positions, return a string
   // representing it in consensus syntax
   protected String posToString(final ArrayList<Position> pArray) {
      int size = pArray.size();
      if (size == 1) {
         throw new IllegalArgumentException
          ("The size of the position array is 1!"); 
      }
      Position src = pArray.get(size - 1);
      Position tgt = pArray.get(size - 2);
      
      return posToString(src, tgt);
   }
   
   // given the board, and the position of enemy piece that is our target
   // return an array list of position, that if the array list is empty
   // then no pieces nearby can kill the target
   // if its size == 1, then the first element is the only one that can
   // attack the target
   // if its size == 2, then the first element is the biggest piece that
   // can attack the target, and the second element is the second biggest
   // piece that can also attack the target
   protected ArrayList<Position> getBgPiecesNrby(Board b, Position tgt) {
      ArrayList<Position> posArray = tgt.adjacentP(b, false, false);
      int i;
      Rank r1 = Rank.Engineer;
      Position pos1 = null;
      Rank r2 = Rank.Engineer;
      Position pos2 = null;
      Piece p = null;
      
      for (i = 0; i < posArray.size(); ++i) {
         // for all the position that is adjacent to the current one
         p = b.getPiece(posArray.get(i).getRow(), 
                        posArray.get(i).getCol());
         if (Piece.movableRank(p.getRank())) {
            // get the biggest one
            if (!Piece.rankGreaterThan(r1, p.getRank())) {
               r1 = p.getRank();
               pos1 = posArray.get(i);
            }
            else if (!Piece.rankGreaterThan(r2, p.getRank())) {
               r2 = p.getRank();
               pos2 = posArray.get(i);
            }  
         }
      }
      
      if (pos1 != null) {
         // if at least one piece was found
         posArray = new ArrayList<Position>();
         posArray.add(pos1);
         if (pos2 != null) {
            // if two pieces was found
            posArray.add(pos2);
         }
         return posArray;
      }
      else {
         // no piece was found
         return null;
      }
   }
   
   // given the board, and the position of enemy piece that is our target
   // return an array list of position, that if the array list is empty
   // then no pieces nearby can kill the target
   // if its size == 1, then the first element is the only one that can
   // attack the target
   // if its size == 2, then the first element is the smallest piece that
   // can attack the target, and the second element is the second smallest
   // piece that can also attack the target
   // no returning piece will be engineers
   protected ArrayList<Position> getSmPiecesNrby(Board b, Position tgt) {
      ArrayList<Position> posArray = tgt.adjacentP(b, false, false);
      int i;
      Rank r1 = Rank.Bomb;
      Position pos1 = null;
      Rank r2 = Rank.Bomb;
      Position pos2 = null;
      Piece p = null;
      
      for (i = 0; i < posArray.size(); ++i) {
         // for all the position that is adjacent to the current one
         p = b.getPiece(posArray.get(i).getRow(), 
                        posArray.get(i).getCol());
         // get the smallest one
         if (Piece.movableRank(p.getRank())) {
            if (Piece.rankGreaterThan(p.getRank(), r1)) {
               if (!p.getRank().equals(Rank.Engineer)) {
                  r1 = p.getRank();
                  pos1 = posArray.get(i);
               }
            }
            else if (!Piece.rankGreaterThan(p.getRank(), r2)) {
               if (!p.getRank().equals(Rank.Engineer)) {
                  r2 = p.getRank();
                  pos2 = posArray.get(i);
               }
            }  
         }
      }
      
      if (pos1 != null) {
         // if at least one piece was found
         posArray = new ArrayList<Position>();
         posArray.add(pos1);
         if (pos2 != null) {
            // if found two pieces
            posArray.add(pos2);
         }
         return posArray;
      }
      else {
         // found no piece
         return null;
      }
   }

   // given a source position and a target position
   // return the string representing this move in consensus syntax
   protected String posToString(final Position src, final Position tgt) {
      
      StringBuilder strBuild = new StringBuilder();
      strBuild.append("(");
      strBuild.append(getColChar(src.getCol()));
      if (src.getRow() > 8) {
         // the row number in consensus syntax is greater than 9
         strBuild.append((char)'1');
         strBuild.append((char)('0' + src.getRow() - 9));
      }
      else {
         strBuild.append((char)('1' + src.getRow()));
      }
      strBuild.append(" ");
      strBuild.append(getColChar(tgt.getCol()));
      if (tgt.getRow() > 8) {
         // the row number in consensus syntax is greater than 9
         strBuild.append((char)'1');
         strBuild.append((char)('0' + tgt.getRow() - 9));
      }
      else {
         strBuild.append((char)('1' + tgt.getRow()));
      }
      strBuild.append(")");
      
      return strBuild.toString();
   }
   
   // given the column number of internal expression of column
   // translate it to the character representing the column in student
   // consensus syntax
   private char getColChar(int col) {
      if (reverse) {
         return (char) ('E' - col);
      }
      else {
         return (char) ('A' + col);
      }
   }
   
   // given two position
   // return the Manhattan distance between the two positions
   protected int distTwoPos(final Position p1, final Position p2) {
      
      return (Math.abs(p1.getCol() - p2.getCol()) + 
              Math.abs(p1.getRow() - p2.getRow()));
   }
   
   // given the board
   // return the array list of position of our biggest piece
   // it may contains two positions
   // NOTE the user need to see what piece it is and which one is closer
   protected ArrayList<Position> getBiggestPos(Board b) {
      
      return b.getBiggestPPos();
   }
   
   // given the board
   // return the number of pieces in our side that is movable
   protected int getNumOfMovablePiece(Board b) {
      int count = 0;
      for (int row = 0; row < RowNum; ++row) {
         for (int col = 0; col < ColNum; ++col) {
            Piece p = b.getPiece(row, col);
            if (Piece.movableRank(p.getRank()) && p.getOurSide()) {
               count += 1;
            }
         }
      }
      
      return count;
   }
   
   // given the board, and the rank of our piece
   // return an ArrayList of Positions that have the same rank of the 
   // given one on it
   protected ArrayList<Position> findPiecePos(Board b, Rank rank) {
      ArrayList<Position> piecePos = new ArrayList<Position>();
      for (int row = 0; row < RowNum; ++row) {
         for (int col = 0; col < ColNum; ++col) {
            Piece p = b.getPiece(row, col);
            if (p.getRank().equals(rank) && p.getOurSide()) {
               piecePos.add(new Position(row, col));
            }
         }
      }
      
      return piecePos;
   }

   // getter
   protected Position getFlagPos() {
      return flagPos;
   }
   
   
   // the method checking if the move is legal, base on our knowledge so far
   // given the board, and the string of next move in student consensus syntax
   // if the move is legal, then just return the move given
   // if the move is illegal, then output the error message into the file
   // if the file test is set to true
   // and the method will just return null
   protected String legalMove(Board b, String move) {
      
      // get source pos and target pos
      if (move != null) {
         Position src, tgt;
         int srcRow, srcCol;
         int tgtRow, tgtCol;
         srcRow = b.getRowNumber(move.substring(1, move.indexOf(' ')));
         if (reverse) {
            srcCol = 4 - b.getColNumber(move.substring(1, move.indexOf(' ')));
         }
         else {
            srcCol = b.getColNumber(move.substring(1, move.indexOf(' ')));
         }
         src = new Position(srcRow, srcCol);
      
         tgtRow = b.getRowNumber(move.substring(move.indexOf(' ') + 1, 
                                                move.indexOf(')')));
         if (reverse) {
            tgtCol = 4 - b.getColNumber(move.substring(move.indexOf(' ') + 1, 
                                                       move.indexOf(')')));
         }
         else {
            tgtCol = b.getColNumber(move.substring(move.indexOf(' ') + 1, 
                                                   move.indexOf(')')));
         }
         tgt = new Position(tgtRow, tgtCol);
      
         // We need to see if target position is in the adjacent list of src
         Piece piece = b.getPiece(srcRow, srcCol);
         boolean isEngineer = piece.getRank().equals(Rank.Engineer);
         boolean isOurs = piece.getOurSide();
      
         if (src.adjacentP(b, isEngineer, isOurs).indexOf(tgt) != -1) {
            // in the adjacent list, move is legal
            return move;
         }
         else {
            // illegal move!
            if (TEST) {
               outputErrorMove(b);
            }
         }
      }
      else {
         // illegal move!
         if (TEST) {
            outputErrorMove(b);
         }
      }
      
      return null;
   }
   
   // the simple method of printing the board config into a file 
   // when debugging
   private void outputErrorMove(Board b) {
      
      try {
         FileWriter file = new FileWriter("errorConfig.txt", true);
         int i, j;
         
         for (i = 0; i < ColNum; ++i) {
            for (j = 0; j < RowNum; ++j) {
               file.write("board.setPiece(new Piece(Rank." + 
                          b.getPiece(j, i).getRank().toString() + ", " +
                          b.getPiece(j, i).getOurSide() + ", " +
                          b.getPiece(j, i).getcBeBomb() + ", " +
                          b.getPiece(j, i).getcBeMine() + ", " +
                          b.getPiece(j, i).getcBeFlag() + "), \"" +
                          translatePos(j, i) + "\");");
            }
         }
         file.close();
      
      } catch(Exception e) {
         e.printStackTrace();
      }
      
   }
   
   // given the row number and col number
   // return the string of the position it representing
   // only used for debugging
   private String translatePos(int row, int col) {
      if (row < RowNum && col < ColNum && row >= 0 && col >= 0) {
         // if the row and column number is valid for our grid
         StringBuilder str = new StringBuilder();
         str.append("");
         str.append((char)('A' + col));
         if (row < 9) {
            str.append((char)('1' + row));
         }
         else {
            str.append('1');
            str.append((char)('0' + row - 9));
         }
         return str.toString();
      }
      else {
         throw new RuntimeException("Position invalid!\n");
      }
   }
}
