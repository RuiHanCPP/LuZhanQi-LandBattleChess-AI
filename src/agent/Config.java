package agent;

import boardConfig.Piece.Rank;
import java.util.*;
import lzqExpert.*;
import boardConfig.Board;
import boardConfig.Piece;

/*
The class of configuration.
This class stores the board status and which expert the agent is using.
*/
public class Config {
   
   // The board of this whole game
   private Board board; // the board in the game configuration
   private Expert expert; // the expert of the AI
   
   private final static int NumofConfigs = 1; 
   // the number of configurations - 1
   
   private final static boolean DEBUG = false;
   private final static int ColNum = 5; // the number of columns
   private final static int RowNum = 12; // the number of rows
   private final static int OneSideRowNum = RowNum / 2;
   private static boolean reverse; 
   //reverse marks whether the agent will reverse the strategy and config
   
   /*
   Config(boolean rev): constructor of the config class
   Given: Whether or not to reverse the strategy and config
   Returns: A Config object, storing the initial board and initializing
            the expert of the AI.(The number of initial config is randomly
            chosen.)
   */
   public Config(boolean rev) {
      board = new Board();
      Random random = new Random();
      int num = Math.abs(random.nextInt()) % NumofConfigs;
      new InitConfig(num, board);
      expert = new Expert(rev);
      reverse = rev;
   }
   
   /*
   Board getBoard():
   Returns: the Board object.
   */
   public Board getBoard() {
      if (DEBUG) {
         System.out.println(board.getPiece("A1").getRank());
      }
      return board;
   }
   
   /*
   String OutInitConfig():
   Returns: The initconfig as a string.
   */
   public String OutInitConfig() {
      StringBuilder str = new StringBuilder();
      str.append("(");
      int col = 0;
      int row = 0;
      for (; col < ColNum; ++col) {
         // for our board configuration
         // output all the pieces that is on our side
         // in student consensus syntax
         for (row = 0; row < OneSideRowNum; ++row) {
            Piece p = board.getPiece(row, col);
            if (p.getOurSide() && p.getRank() != Rank.Unknown 
                 && p.getRank() != Rank.Empty){
               str.append("(" + translatePos(row, col) + " ");
               str.append(translateRank(p.getRank()) + ") ");
            }
         }
      }
      str.delete(str.length() - 1, str.length());
      str.append(")");
      if (DEBUG) {
         System.out.println(str);
      }
      return str.toString();
   }
   
   /*
   String getNextMove() :
   Returns: The next move decided by the expert.
   */
   public String getNextMove() {
      
      String Str = expert.nextMove(board);
      if (Str == null || Str.length() == 0) {
         return "(resign)";
      }
      return Str;
   }
   
   /*
   void setFlag(String pos):
   Given: a position represented by a string.
   Effect: Set the given position as flag. The position can
           be ours or other's.
   */
   public void setFlag(String pos) {
      board.setFlag(pos);
   }
   
   //void setResult(String src, String tgt, String compare):
   // given the two position of a move, represented by strings
   // and the comparison result of the move, in a string
   // change the board according to the outcome
   // such as moving pieces, and/or deleting pieces
   public void setResult(String src, String tgt, String compare) {
      if (compare == null) {
         // simple move
         board.movePiece(src, tgt);
         board.updatePiece(tgt);
      }
      else if (compare.equals(">")) {
         // the piece on the source position
         // killed the piece on the target position
         board.updatePiece(src, tgt, false);
         board.deletePiece(tgt);
         board.movePiece(src, tgt);
      }
      else if (compare.equals("<")) {
         // the piece on the source position
         // was killed by the piece on the target one
         board.updatePiece(tgt, src, true);
         board.deletePiece(src);
      }
      else if (compare.equals("=")) {
         // both two pieces were killed
         board.updatePiece(src, tgt);
         board.deletePiece(src);
         board.deletePiece(tgt);
      }
      else {
         if (DEBUG) {
            System.out.println("src:" + src);
            System.out.println("tgt:" + tgt);
            System.out.println("com:" + compare);
         }
         throw new IllegalArgumentException
         ("Invalid compare input!"); 
      }      
      board.updateHQs();
   }

   //String translateRank(Rank rank):   
   // given a rank of the piece
   // return the piece's rank in consensus syntax, in a string
   private String translateRank(Rank rank) {
      // translate the name of ranks inside the program
      // into the syntax of communications
      switch(rank) {
      case Flag:   {return "F";}
      case LandMine:   {return "L";}
      case Bomb:   {return "B";}
      case SiLing:   {return "9";}
      case JunZhang:   {return "8";}
      case ShiZhang:   {return "7";}
      case LvZhang:   {return "6";}
      case TuanZhang:   {return "5";}
      case YingZhang:   {return "4";}
      case LianZhang:   {return "3";}
      case PaiZhang:   {return "2";}
      case Engineer:   {return "1";}
      case Unknown:   {return "n";}
      case Empty:   {return "e";}
      default: {
         throw new RuntimeException
            ("Cannot translate the rank!\n");
         }
      }
   }
   
   //String translatePos(int row, int col):
   // Given: the row number and the column number of a position
   // return the position in consensus syntax in a string
   // the method should be called only for output init config
   private String translatePos(int row, int col) {
      if (row < RowNum && col < ColNum && row >= 0 && col >= 0) {
         // if the row and column number is valid for our grid
         StringBuilder str = new StringBuilder();
         str.append("");
         if (reverse) {
            str.append((char)('E' - col));
         }
         else {
            str.append((char)('A' + col));
         }
        
         // here the row will only smaller than 6
         str.append((char)('1' + row));
         return str.toString();
      }
      else {
         if (DEBUG) {
            System.out.println("row = " + row);
            System.out.println("col = " + col);
         }
         throw new RuntimeException("Position invalid!\n");
      }
   }
}
