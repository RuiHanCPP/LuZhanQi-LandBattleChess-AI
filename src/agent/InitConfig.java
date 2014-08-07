package agent;

import boardConfig.Board;
import boardConfig.Piece;
import boardConfig.Piece.Rank;

public class InitConfig {
   
   private final static boolean DEBUG = false;

   // basic constructor, this constructor should always be called
   // given the number of initial config set, and the board
   // set up the board for b
   public InitConfig(int num, Board b) {
      initConfig(num, b);
      b.setBiggestP();
   }

   //void initConfig(int num, Board board):   
   // initialize the board of our side
   // given the number of initial config and the board
   // initialize the board base on which config to choose
   private static final void initConfig(int num, Board board) {
      // initialize all the pieces, empty should not be on our side
      // the opponont's pieces should not be on our side
      initOppConfig(board);
      if (num < 0) {
         // configuration 1, defending config
         board.setPiece(new Piece(Rank.LandMine, true), "A1");
         board.setPiece(new Piece(Rank.Flag, true), "B1");
         board.setPiece(new Piece(Rank.LandMine, true), "C1");
         board.setPiece(new Piece(Rank.PaiZhang, true), "D1");
         board.setPiece(new Piece(Rank.PaiZhang, true), "E1");
      
         board.setPiece(new Piece(Rank.JunZhang, true), "A2");
         board.setPiece(new Piece(Rank.TuanZhang, true), "B2");
         board.setPiece(new Piece(Rank.ShiZhang, true), "C2");
         board.setPiece(new Piece(Rank.Engineer, true), "D2");
         board.setPiece(new Piece(Rank.LandMine, true), "E2");
      
         board.setPiece(new Piece(Rank.LvZhang, true), "A3");
         board.setPiece(new Piece(Rank.Empty, false), "B3");
         board.setPiece(new Piece(Rank.Bomb, true), "C3");
         board.setPiece(new Piece(Rank.Empty, false), "D3");
         board.setPiece(new Piece(Rank.LianZhang, true), "E3");
      
         board.setPiece(new Piece(Rank.LvZhang, true), "A4");
         board.setPiece(new Piece(Rank.YingZhang, true), "B4");
         board.setPiece(new Piece(Rank.Empty, false), "C4");
         board.setPiece(new Piece(Rank.Engineer, true), "D4");
         board.setPiece(new Piece(Rank.LianZhang, true), "E4");
      
         board.setPiece(new Piece(Rank.Bomb, true), "A5");
         board.setPiece(new Piece(Rank.Empty, false), "B5");
         board.setPiece(new Piece(Rank.Engineer, true), "C5");
         board.setPiece(new Piece(Rank.Empty, false), "D5");
         board.setPiece(new Piece(Rank.PaiZhang, true), "E5");
      
         board.setPiece(new Piece(Rank.ShiZhang, true), "A6");
         board.setPiece(new Piece(Rank.TuanZhang, true), "B6");
         board.setPiece(new Piece(Rank.SiLing, true), "C6");
         board.setPiece(new Piece(Rank.YingZhang, true), "D6");
         board.setPiece(new Piece(Rank.LianZhang, true), "E6");
         board.setIsDefend(true);
      }
      else if (num >= 0) {
         // configuration 2, attacking config
         board.setPiece(new Piece(Rank.LianZhang, true), "A1");
         board.setPiece(new Piece(Rank.PaiZhang, true), "B1");
         board.setPiece(new Piece(Rank.LianZhang, true), "C1");
         board.setPiece(new Piece(Rank.Flag, true), "D1");
         board.setPiece(new Piece(Rank.LvZhang, true), "E1");
      
         board.setPiece(new Piece(Rank.LandMine, true), "A2");
         board.setPiece(new Piece(Rank.LandMine, true), "B2");
         board.setPiece(new Piece(Rank.LandMine, true), "C2");
         board.setPiece(new Piece(Rank.Bomb, true), "D2");
         board.setPiece(new Piece(Rank.LvZhang, true), "E2");
      
         board.setPiece(new Piece(Rank.YingZhang, true), "A3");
         board.setPiece(new Piece(Rank.Empty, false), "B3");
         board.setPiece(new Piece(Rank.TuanZhang, true), "C3");
         board.setPiece(new Piece(Rank.Empty, false), "D3");
         board.setPiece(new Piece(Rank.Bomb, true), "E3");
      
         board.setPiece(new Piece(Rank.PaiZhang, true), "A4");
         board.setPiece(new Piece(Rank.YingZhang, true), "B4");
         board.setPiece(new Piece(Rank.Empty, false), "C4");
         board.setPiece(new Piece(Rank.ShiZhang, true), "D4");
         board.setPiece(new Piece(Rank.ShiZhang, true), "E4");
      
         board.setPiece(new Piece(Rank.LianZhang, true), "A5");
         board.setPiece(new Piece(Rank.Empty, false), "B5");
         board.setPiece(new Piece(Rank.TuanZhang, true), "C5");
         board.setPiece(new Piece(Rank.Empty, false), "D5");
         board.setPiece(new Piece(Rank.JunZhang, true), "E5");
      
         board.setPiece(new Piece(Rank.PaiZhang, true), "A6");
         board.setPiece(new Piece(Rank.Engineer, true), "B6");
         board.setPiece(new Piece(Rank.Engineer, true), "C6");
         board.setPiece(new Piece(Rank.Engineer, true), "D6");
         board.setPiece(new Piece(Rank.SiLing, true), "E6");
         board.setIsDefend(false);
      }
      else {
         // the code is called only when error occurs.
         if (DEBUG) {
            System.out.println("Error occured when choosing init config");
            System.out.println("The config number given is " + num);
         }
         initConfig(1, board);
      }
   }

   //void initOppConfig(Board board):    
   // initialize the board of the opponont's side
   // given the board, where the configuration of our side was done
   // initialize the board of the opponont's side
   private final static void initOppConfig(Board board) {
      board.setPiece(new Piece
            (Rank.Unknown, false, false, false, false), "A7");
      board.setPiece(new Piece
            (Rank.Unknown, false, false, false, false), "B7");
      board.setPiece(new Piece
            (Rank.Unknown, false, false, false, false), "C7");
      board.setPiece(new Piece
            (Rank.Unknown, false, false, false, false), "D7");
      board.setPiece(new Piece
            (Rank.Unknown, false, false, false, false), "E7");
     
      board.setPiece(new Piece
            (Rank.Unknown, false, true, false, false), "A8");
      board.setPiece(new Piece(Rank.Empty, false), "B8");
      board.setPiece(new Piece
            (Rank.Unknown, false, true, false, false), "C8");
      board.setPiece(new Piece(Rank.Empty, false), "D8");
      board.setPiece(new Piece
            (Rank.Unknown, false, true, false, false), "E8");
        
      board.setPiece(new Piece
            (Rank.Unknown, false, true, false, false), "A9");
      board.setPiece(new Piece
            (Rank.Unknown, false, true, false, false), "B9");
      board.setPiece(new Piece(Rank.Empty, false), "C9");
      board.setPiece(new Piece
            (Rank.Unknown, false, true, false, false), "D9");
      board.setPiece(new Piece
            (Rank.Unknown, false, true, false, false), "E9");
        
      board.setPiece(new Piece
            (Rank.Unknown, false, true, false, false), "A10");
      board.setPiece(new Piece(Rank.Empty, false), "B10");
      board.setPiece(new Piece
            (Rank.Unknown, false, true, false, false), "C10");
      board.setPiece(new Piece(Rank.Empty, false), "D10");
      board.setPiece(new Piece
            (Rank.Unknown, false, true, false, false), "E10");
       
      board.setPiece(new Piece
            (Rank.Unknown, false, true, true, false), "A11");
      board.setPiece(new Piece
            (Rank.Unknown, false, true, true, false), "B11");
      board.setPiece(new Piece
            (Rank.Unknown, false, true, true, false), "C11");
      board.setPiece(new Piece
            (Rank.Unknown, false, true, true, false), "D11");
      board.setPiece(new Piece
            (Rank.Unknown, false, true, true, false), "E11");
        
      board.setPiece(new Piece
            (Rank.Unknown, false, true, true, false), "A12");
      board.setPiece(new Piece
            (Rank.Unknown, false, true, true, true), "B12");
      board.setPiece(new Piece
            (Rank.Unknown, false, true, true, false), "C12");
      board.setPiece(new Piece
            (Rank.Unknown, false, true, true, true), "D12");
      board.setPiece(new Piece
            (Rank.Unknown, false, true, true, false), "E12");
   }
}
