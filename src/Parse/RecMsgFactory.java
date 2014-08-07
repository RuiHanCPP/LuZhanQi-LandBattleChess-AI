package Parse;
import RevMsg.FlagRecMsg;
import RevMsg.GameEndRecMsg;
import RevMsg.IllegalRecMsg;
import RevMsg.Msg0RecMsg;
import RevMsg.OutcomeRecMsg;
import RevMsg.TurnRecMsg;

//Received Message Factory
//Provides static methods to create message class
public class RecMsgFactory {
    
   //Inputs type, which, time
   //Returns a new Msg0RecMsg
   public static Msg0RecMsg CreateInitRecMsg
   (int type, int which, double time, boolean bReverse) {
      return new Msg0RecMsg(type, which, time, bReverse);
   }
    
   //Inputs type, winner
   //Returns a new GameEndRecMsg
   public static GameEndRecMsg CreateGameEndRecMsg
                               (int type, int winner, boolean bReverse) {
      return new GameEndRecMsg(type, winner, bReverse);
   }
    
   //Inputs type, srcpos and destpos
   //Returns a new IllegalRecMsg
   public static IllegalRecMsg CreateIllegalRecMsg
                               (int type, String srcpos, String destpos, 
                                boolean bReverse) {
      return new IllegalRecMsg(type, srcpos, destpos, bReverse);
   }
    
   //Inputs type and which
   //Returns a new TurnRecMsg
   public static TurnRecMsg CreateTurnRecMsg
                            (int type, int which, boolean bReverse) {
      return new TurnRecMsg(type, which, bReverse);
   }
    
   //Inputs type, srcpos, destpos and comp
   //Returns a new OutcomeRecMsg
   public static OutcomeRecMsg CreateOutcomeRecMsg
                               (int type, String srcpos, String destpos,
                                String comp, boolean bReverse) {
      return new OutcomeRecMsg(type, srcpos, destpos, comp, bReverse);
   }
    
   //Inputs type, which and pos
   //Returns a new FlagRecMsg
   public static FlagRecMsg CreateFlagRecMsg
                            (int type, int which, String pos, 
                             boolean bReverse) {
      return new FlagRecMsg(type, which, pos, bReverse);
   }
}
