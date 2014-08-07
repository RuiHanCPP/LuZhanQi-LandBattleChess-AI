package boardConfig;

import java.util.ArrayList;
import lzqExpert.Position;
import boardConfig.Piece.Rank;

/* The class of Board.
   An object of this class stores information of board status.
*/
public class Board {
   
   final int ColNum = 5; // the number of the columns
   final int RowNum = 12; // the number of the rows
   final int OneSideRowNum = RowNum / 2; // the number of rows for one side
   public boolean DEBUG = false;
   
   private Piece lastPieceLost; // record the last piece we lost
   private Position lastPieceLostPos; 
   // record the position of the last piece we lost
    
   // the grid of the board
   private Piece[][] chessBoard = new Piece[RowNum][ColNum];
   
   private boolean oppHasJunzhang; // if the enemy has general
   private boolean oppHasSiling; // if the enemy has field marshal
   // the two value above may not be accurate, that when return true
   // the enemy may have general/field marshal
   private boolean weHaveJunzhang; // if we have the general
   private boolean weHaveSiling; // if we have the field marshal
   
   private Rank biggestRank; // the rank of biggest piece
   private ArrayList<Position> biggestPPos;
   
   private int peaceMoveCount; // the number of peace moves
   
   private boolean isDefend; // if the strategy is defending
    
   // initialize the board with empty pieces
   // the board now is initialized but not usable
   public Board() {
      int i, j;
      for (i = 0; i < RowNum; ++i) {
         for (j = 0; j < ColNum; ++j) {
            chessBoard[i][j] = new Piece(Rank.Empty, false);
         }
      }
      lastPieceLost = null;
      lastPieceLostPos = null;
      oppHasJunzhang = true;
      oppHasSiling = true;
      weHaveJunzhang = true;
      weHaveSiling = true;
      biggestRank = Rank.SiLing;
      peaceMoveCount = 0;
      biggestPPos = new ArrayList<Position>();
   }
    
   // naive getter, given row and col
   // return the piece indicated
   public Piece getPiece(int row, int col) {
      return chessBoard[row][col];
   }
    
   // naive getter, given row and col
   // return the piece indicated
   public void setPiece(Piece p, int row, int col) {
      chessBoard[row][col] = p;
   }
    
   // advanced getter, enable the position such as A2 B4
   // to exist in the code, ease the coding.
   public Piece getPiece(String s) {
      int row = getRowNumber(s);
      int col = getColNumber(s);
      return getPiece(row, col);
   }
    
   // advanced setter, enable the position such as A2 B4
   // to exist in the code, ease the coding.
   public void setPiece(Piece p, String s) {
      int row = getRowNumber(s);
      int col = getColNumber(s);
      setPiece(p, row, col);
   }
    

   /*
   void movePiece(String src, String tgt):
   Given: A source position in string & target position in string.
   Precondition: The target position must be valid & empty.
   Effect: Move source piece to the target position.
   Caution: The function does not check if the move is valid,
            which means it only cares about the src and tgt 
            position instead of how to get there.
   */
   public void movePiece(String src, String tgt) {
      // move a piece to an empty position
      int rowSrc = getRowNumber(src);
      int colSrc = getColNumber(src);
      int rowTgt = getRowNumber(tgt);
      int colTgt = getColNumber(tgt);
       
      // make sure the src is non-empty
      Piece srcP = getPiece(rowSrc, colSrc);
      if (srcP.getRank().equals(Rank.Empty)) {
         // error occurred when moving from a empty position
         throw new IllegalArgumentException
         ("Unable to move from an empty position!"); 
      }
      
      // make sure the tgt is empty
      Piece tgtP = getPiece(rowTgt, colTgt);
      if (!tgtP.getRank().equals(Rank.Empty)) {
         throw new IllegalArgumentException
         ("Unable to move directly to a non-empty position!"); 
      }
       
      // set the new piece, and delete the old one
      setPiece(srcP, rowTgt, colTgt);
      deletePiece(src);
   }
    
   /*
   void deletePiece(String pos):
   Given: A position in string.
   Precondition: The position must be valid & not empty.
   Effect: Delete the piece of the given position.
   */
   public void deletePiece(String pos) {
      int row = getRowNumber(pos);
      int col = getColNumber(pos);
      Piece empty = new Piece(Rank.Empty, false);
      Piece p = getPiece(row, col);
      if (p.getRank().equals(Rank.Empty)) {
         throw new IllegalArgumentException
         ("Unable to delete an empty position!"); 
      }
       
      // set it empty
      setPiece(empty, row, col);
   }
   
   // get row number based on the string given
   // the string must contains the row number of a position
   // in the syntax of student consensus
   public int getRowNumber(String s) {
      if (s.length() == 2 && s.charAt(0) >= 'A' && s.charAt(0) <= 'E'
         && s.charAt(1) >= '1'&& s.charAt(1) <= '9') {
         return (s.charAt(1) - '1');
      }
      else if (s.length() == 3 && s.charAt(0) >= 'A' 
              && s.charAt(0) <= 'E' && s.charAt(1) == '1'
              && s.charAt(2) >= '0' && s.charAt(2) <= '2') {
         int row = 10 + s.charAt(2) - '1';
         return row;
      }
      else {
         // this part of the code should never be executed
         throw new IllegalArgumentException("WRONG LOCATION!"); 
      }
   }
   
   // get col number based on the string given
   // the string must contains the col number of a position
   // in the syntax of student consensus
   public int getColNumber(String s) {
      if (s.length() == 2 && s.charAt(0) >= 'A' && s.charAt(0) <= 'E'
         && s.charAt(1) >= '1'&& s.charAt(1) <= '9') {
         return (s.charAt(0) - 'A');
      }
      else if (s.length() == 3 && s.charAt(0) >= 'A' 
              && s.charAt(0) <= 'E' && s.charAt(1) == '1'
              && s.charAt(2) >= '0' && s.charAt(2) <= '2') {
         int col = s.charAt(0) - 'A';
         return col;
      }
      else {
         // this part of the code should never be executed
         throw new IllegalArgumentException("WRONG LOCATION!"); 
      }
   }
   
   /*
   void updatePiece
   (String winnerPos, String loserPos, boolean suicide):
   Given: the position of the winner piece (in string);
          the position of the loser piece (in string);
          suicide: True if loser is the attacker.
                   Else false.
   Effect: Update all the information stored in this Board object.
           eg. weHaveSiling, weHaveJunZhang, oppHaveSiling ...
   */
   public void updatePiece
   (String winnerPos, String loserPos, boolean suicide) {
      int lrow = getRowNumber(loserPos);
      int lcol = getColNumber(loserPos);
      int wrow = getRowNumber(winnerPos);
      int wcol = getColNumber(winnerPos);
      
      // locate our loser
      Piece loser = getPiece(loserPos); 
      
      if (loser.getOurSide()) {
         // if the loser is in ourside
         // oh.. so sad
         if (!suicide) {
            lastPieceLostPos = new Position(lrow, lcol);
            lastPieceLost = loser;
            // keep track of the last piece we've lost
         }
         getPiece(wrow, wcol).pMoved();
         getPiece(wrow, wcol).pKilled(getPiece(lrow, lcol).getRank());
         // update the enemy piece to eliminate the possibility that
         // the enemy piece is bomb, land mine, engineer or not
         
         if (loser.getRank().equals((Rank.SiLing))) {
            weHaveSiling = false;
         }
         else if (loser.getRank().equals((Rank.JunZhang))) {
            weHaveJunzhang = false;
         }
         if (loser.getRank().equals(biggestRank)) {
            loser.setRank(Rank.LandMine);
            // when losing our biggest rank, we will first set it
            // to land mine, and then check it
            setBiggestP();
         }
      }
      else {
         if (loser.getMinRank().equals(Rank.JunZhang)) {
            oppHasJunzhang = false;
         }
      }
      peaceMoveCount = 0;
   }
   
   /*
   void updatePiece(String pos1, String pos2):
   Given: The position of piece 1 and piece 2.
   Precondition: Both of piece 1 and piece 2 are killed.
   Effect: Update the state of this Board object. 
   */
   public void updatePiece(String pos1, String pos2) {
      Piece pc1 = getPiece(pos1);
      Piece pc2 = getPiece(pos2);
      if (pc1.getOurSide()) {
         if (!pc2.getcBeBomb() && !pc1.getRank().equals(Rank.Bomb)) {
            if (pc1.getRank().equals(Rank.SiLing)) {
               oppHasSiling = false;
            }
            else if (pc1.getRank().equals(Rank.JunZhang)) {
               oppHasJunzhang = false;
            }
         }
         if (pc1.getRank().equals((Rank.SiLing))) {
            weHaveSiling = false;
         }
         else if (pc1.getRank().equals((Rank.JunZhang))) {
            weHaveJunzhang = false;
         }
         if (pc1.getRank().equals(biggestRank)) {
            pc1.setRank(Rank.LandMine);
            setBiggestP();
         }
      }
      else {
         if (!pc1.getcBeBomb() && !pc2.getRank().equals(Rank.Bomb)) {
            if (pc2.getRank().equals(Rank.SiLing)) {
               oppHasSiling = false;
            }
            else if (pc2.getRank().equals(Rank.JunZhang)) {
               oppHasJunzhang = false;
            }
         }
         if (pc2.getRank().equals((Rank.SiLing))) {
            weHaveSiling = false;
         }
         else if (pc2.getRank().equals((Rank.JunZhang))) {
            weHaveJunzhang = false;
         }
         if (pc2.getRank().equals(biggestRank)) {
            pc2.setRank(Rank.LandMine);
            setBiggestP();
         }
      }
      peaceMoveCount = 0;
   }
   
   // find all the position of pieces that are our biggest 
   // and add them into that biggestPPost array list
   // update the biggest Rank at the same time
   public void setBiggestP() {
      biggestRank = Rank.Engineer;
      for (int row = 0; row < RowNum; ++row) {
         // for all pieces on the board
         // find the piece with the biggest rank on the board
         // that belongs to us
         for (int col = 0; col < ColNum; ++col) {
            Piece p = getPiece(row, col);
            if (!Piece.rankGreaterThan(biggestRank, p.getRank())) {
               biggestRank = p.getRank();
            }
         }
      }
      biggestPPos.clear();
      for (int row = 0; row < RowNum; ++row) {
         // and the number of position of the biggest piece may not be just 1
         // search all the pieces and find all the positions of the biggest
         // pieces
         for (int col = 0; col < ColNum; ++col) {
            Piece p = getPiece(row, col);
            if (p.getRank().equals(biggestRank) && p.getOurSide()) {
               biggestPPos.add(new Position(row, col));
            }
         }
      }
      if (Piece.rankGreaterThan(Rank.SiLing, biggestRank)) {
         weHaveSiling = false;
      }
      if (Piece.rankGreaterThan(Rank.JunZhang, biggestRank)) {
         weHaveJunzhang = false;
      }
   }
   
   /*
   void updatePiece(String pos):
   Given: The position of a piece just moved from one place 
          to another (in string).
   Effect: Update the states of this Board object.
   */
   public void updatePiece(String pos) {
      int row = getRowNumber(pos);
      int col = getColNumber(pos);
      if (!getPiece(row, col).getOurSide()) {
         // simply update the property of enemy pieces
         getPiece(row, col).pMoved();
      }
      peaceMoveCount += 1;
   }
   
   // given the position of the flag that the referee sent
   // update the flag position of the board 
   // and if the enemy has field marshal
   public void setFlag(String pos) {
      Piece p = getPiece(pos);
      if (!p.getOurSide()) {
         p.setRank(Rank.Flag);
         oppHasSiling = false;
      }
   }
   
   // the pieces in all the HQs is not movable
   // so we can simply change them to land mines
   public void updateHQs() {
      
      // simply check the two positions
      // to find the HQ
      int row = 0;
      
      int col = 1;
      updateHQ(row, col);
      col = 3;
      updateHQ(row, col);
      
      row = 11;
      
      col = 1;
      updateHQ(row, col);
      col = 3;
      updateHQ(row, col);
   }
   
   // for a certain HQ, the pieces in HQ is not movable
   // so we can simply change them to land mines
   private void updateHQ(int row, int col) {
      Piece p = getPiece(row, col);
      if (p.getRank().equals((Rank.SiLing))) {
         weHaveSiling = false;
      }
      else if (p.getRank().equals((Rank.JunZhang))) {
         weHaveJunzhang = false;
      }
      if (Piece.movableRank(getPiece(row, col).getRank())) {
         getPiece(row, col).setRank(Rank.LandMine);
      }
      setBiggestP();
   }
   
   // the basic method for finding a certain piece
   // this method should be called only for debugging
   // it is not effective at all
   public ArrayList<Position> findPiecePos(Rank rank) {
      ArrayList<Position> piecePos = new ArrayList<Position>();
      for (int row = 0; row < RowNum; ++row) {
         for (int col = 0; col < ColNum; ++col) {
            Piece p = getPiece(row, col);
            if (p.getRank().equals(rank) && p.getOurSide()) {
               piecePos.add(new Position(row, col));
            }
         }
      }
      
      return piecePos;
   }
   
   // the method of updating big pieces
   // which is Field Marshal and General
   // this method should be called only when debugging
   public void settingBigPieces() {
      if (findPiecePos(Rank.SiLing).size() > 0) {
         weHaveSiling = true;
         // we still have our field marshal
      }
      if (findPiecePos(Rank.JunZhang).size() > 0) {
         weHaveJunzhang = true;
         // we still have our General
      }
   }
   
   
   // setter
   public void setIsDefend(boolean isD) {
      isDefend = isD;
   }
   
   // getter
   public Position getLastLostPos() {
      return lastPieceLostPos;
   }
   public Piece getLastLostPc() {
      return lastPieceLost;
   }
   public boolean oppHasSiling() {
      return oppHasSiling;
   }
   public boolean oppHasJunzhang() {
      return oppHasJunzhang;
   }
   public Rank getBiggestRank() {
      return biggestRank;
   }
   public ArrayList<Position> getBiggestPPos() {
      return biggestPPos;
   }
   public int getPeaceMoveCount() {
      return peaceMoveCount;
   }
   public boolean getIsDefend() {
      return isDefend;
   }
   public boolean getWeHaveSiling() {
      return weHaveSiling;
   }
   public boolean getWeHaveJunzhang() {
      return weHaveJunzhang;
   }
}
