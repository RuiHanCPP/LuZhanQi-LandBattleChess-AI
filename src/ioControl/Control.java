package ioControl;
import java.io.IOException;
import java.util.ArrayList;
import Parse.ParseRecMsg;
import RevMsg.*;
import agent.*;

public class Control {
   // The class of the control of the whole program
   // It mainly works on deciding what module to call
   // and return the message to IO class
   
   private static final boolean DEBUG = false;
   private static double timePMove; // the value for time per move
   private static int which; // record who the AI is, 1 or 2
   private static boolean reverse; // reverse all the strategy and configs
    
   public static void main(String args[]) throws IOException {
      if (Math.random() >= 0.5) {
         reverse = true;
      }
      else {
         reverse= false;
      }
      Config con = new Config(reverse);
      String str = "";
      Output out = new GetOutput();
      ArrayList<String> array;
              
      // 1. Receive the first message 
      //    from the referee        
      Input inControl = new LineInput();  
      while (true) {
         array = inControl.revMsg(which);
         for (int i = 0; i < array.size(); ++i) {
            BaseRevMsg brm = ParseRecMsg.Parse(array.get(i), reverse);
            if (brm == null) {
               if (DEBUG) {
                  System.out.println("Syntax is not correct!");
               }
               throw new RuntimeException
               ("Syntax is not correct! Null");
            }
            // Msg0    
            if (brm.getType() == 1) {
   
               Msg0RecMsg msg = (Msg0RecMsg) brm;
      
               if(msg != null) { 
                  which = msg.GetWhich();
                  timePMove = msg.GetTime();
                  double timestamp = System.currentTimeMillis();
                  str = con.OutInitConfig();
                  /*
                  if (((System.currentTimeMillis() - timestamp)
                      / 1000) > timePMove) {
                     //throw new RuntimeException("Time's up");
                  }
                  */
                  while (((System.currentTimeMillis() - timestamp)
                      / 1000) < Math.min(0.75, timePMove)) {
                     continue;
                  }

                  out.outputMsg(str);
               }
            }
            
            //GameEnd
            else if (brm.getType() == 2) {
               
               GameEndRecMsg msg = (GameEndRecMsg) brm;
               if (msg != null) { 
                  int winner = msg.GetWinner();
                  if (DEBUG) {
                     if (winner == which) {
                        System.out.println("I win");
                     }
                     else if ((winner == 1 && which == 2) || 
                        winner == 2 && which == 1) {
                        System.out.println("I lose");
                     }
                     else {
                        System.out.println("Who wins??");
                     }
                  }
                  
                  return;
               }
            }
               
            //Illegal 
            else if (brm.getType() == 3) {
               IllegalRecMsg msg = (IllegalRecMsg) brm;
               if (msg != null) { 
                  //String srcpos = msg.GetSrcpos();
                  //String destpos = msg.GetDestpos();
                  //throw new RuntimeException("Illegal last msg");
                  out.outputMsg("(resign)");
                  //str = con.getNextMove();
                  //out.outputMsg(str);
               }
            }
            
            // Turn
            else if (brm.getType() == 4) {
               TurnRecMsg msg = (TurnRecMsg) brm;
               if (msg != null) { 
                  if (msg.GetWhich() == which) {
                     double timestamp = System.currentTimeMillis();
                     str = con.getNextMove();
                     /*
                     if (((System.currentTimeMillis() - timestamp)
                           / 1000) > timePMove) {
                          throw new RuntimeException("Time's up");
                     }
                     */
                     while (((System.currentTimeMillis() - timestamp)
                           / 1000) < Math.min(0.75, timePMove)) {
                        continue;
                     }

                     out.outputMsg(str);
                  }
               }
            }
            
            // Outcome
            else if (brm.getType() == 5) {
               OutcomeRecMsg msg = (OutcomeRecMsg) brm;
               if (msg != null) { 
                  String srcpos = msg.GetSrcPos();
                  String destpos = msg.GetDestPos();
                  String comp = msg.GetComp(); 
                  //if comp is null, then no compare
                  
                  con.setResult(srcpos, destpos, comp);
               }
            }
               
            // Flag
            else if( brm.getType() == 6) {
               FlagRecMsg msg = (FlagRecMsg) brm;
               if(msg != null) { 
                  if (which != msg.GetWhich()) {
                     String pos = msg.GetPos();
                     con.setFlag(pos);
                  }
               }
            }
            else {
               if (DEBUG) {
                  System.out.println("Syntax is not correct!");
               }
               throw new RuntimeException("Syntax is not correct!");
            }
         }
      }
   }
}
