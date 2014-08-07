package Parse;
import RevMsg.BaseRevMsg;

//Parse Message Base Class
public class ParseRecMsg {
   
   //Set debug sign
   private static final boolean DEBUG = false;
   
   //This method is used to pars a string
   //Inputs a string, returns a BaseRecMsg
   static public BaseRevMsg Parse(String s, boolean bReverse) {
        
      if (DEBUG) {
         System.out.println(s);
      }
       
      if (s == null) { return null; }
        
      String str = ParseString.RemoveWhiteSpace(s);
        
      if (str.length() >= 7 &&
          str.subSequence(0, 5).equals("(init")) {
         return ParseInitRecMsg(str, bReverse);
      }
        
      if (str.length() >= 6 &&
          str.subSequence(0, 4).equals("(end")) {
         return ParseGameEndRecMsg(str, bReverse);
      }
        
      if (str.length() >= 10 &&
          str.subSequence(0, 8).equals("(illegal")) {
         return ParseIllegalRecMsg(str, bReverse);
      }
        
      if (str.length() >= 5 && 
          str.subSequence(0, 3).equals("(go")) {
         return ParseTurnRecMsg(str, bReverse);
      }
        
      if (str.length() >= 15 && 
          str.subSequence(0, 8).equals("(outcome")) {
         return ParseOutcomeRecMsg(str, bReverse);    
      }
           
      if (str.length() >= 9 && 
          str.subSequence(0, 5).equals("(flag")) {
         return ParseFlagRecMsg(str, bReverse);
      }
        
   
      return null;
   }  
    
   //Parse Init Message
   //Inputs a string which is probably in Init Msg format
   //Returns BaseRevMsg if it is in correct format
   //Otherwise, return null
   static private BaseRevMsg ParseInitRecMsg(String str, boolean bReverse) {
      int which = ParseInitRecMsg.ParseWhich(str);        
      if (which != 1 && which != 2) {
         return null;
      }
        
      double time = ParseInitRecMsg.ParseTime(str);
        
      if (!(time > 0.0)) {
         return null;
      }
      return RecMsgFactory.CreateInitRecMsg(1, which, time, bReverse);
   }
    
   //Parse GameEnd Message
   //Inputs a string which is probably in Game End format
   //Returns BaseRevMsg if it is in correct format
   //Otherwise, return null
   static private BaseRevMsg ParseGameEndRecMsg
                             (String str, boolean bReverse) {       
      int iWinner = ParseGameEndRecMsg.ParseWinner(str);        
      if (iWinner != 0 && iWinner != 1 && iWinner != 2) {
         return null;
      }        
      return RecMsgFactory.CreateGameEndRecMsg(2, iWinner, bReverse);
   }
    
   //Parse Illegal Message
   //Inputs a string which is probably in Illegal msg format
   //Returns BaseRevMsg if it is in correct format
   //Otherwise, return null
   static private BaseRevMsg ParseIllegalRecMsg
                             (String str, boolean bReverse) {      
      ParseIllegalRecMsg.ParsePos(str);
      String SrcPos = ParseIllegalRecMsg.ParseSrcPos();
      String DestPos = ParseIllegalRecMsg.ParseDestPos();        
      if (SrcPos == null || DestPos == null) {
         return null;
      }        
      return RecMsgFactory.CreateIllegalRecMsg(3, SrcPos, DestPos, bReverse);
   }
    
   //Parse Turn Message
   //Inputs a string which is probably in Turn msg format
   //Returns BaseRevMsg if it is in correct format
   //Otherwise, return null
   static private BaseRevMsg ParseTurnRecMsg(String str, boolean bReverse) {       
      int which = ParseTurnRecMsg.ParseWhich(str);       
      if (which != 1 && which != 2) {
         return null;
      }        
      return RecMsgFactory.CreateTurnRecMsg(4, which, bReverse);
   }
    
   //Parse Outcome Message
   //Inputs a string which is probably in Outcome msg format
   //Returns BaseRevMsg if it is in correct format
   //Otherwise, return null
   static private BaseRevMsg ParseOutcomeRecMsg
                             (String str, boolean bReverse) {       
      String srcpos;
      String destpos;
      String comp;       
      ParseOutcomeRecMsg.SetString(str);
      comp = ParseOutcomeRecMsg.ParseCompare();
      srcpos = ParseOutcomeRecMsg.ParseSrcPos();
      destpos = ParseOutcomeRecMsg.ParseDestPos();         
      if (srcpos == null || destpos == null) {
         return null;
      }
      return RecMsgFactory.CreateOutcomeRecMsg
                           (5, srcpos, destpos, comp, bReverse);
   }
    
   //Parse Flag Message
   //Inputs a string which is probably in Flag msg format
   //Returns BaseRevMsg if it is in correct format
   //Otherwise, return null
   static private BaseRevMsg ParseFlagRecMsg(String str, boolean bReverse) {      
      int which = ParseFlagRecMsg.ParseWhich(str);       
      String pos = ParseFlagRecMsg.ParsePos(str);
        
      if ((which != 1 && which != 2) || pos == null ) {
         return null;
      }
        
      return RecMsgFactory.CreateFlagRecMsg(6, which, pos, bReverse);
   }
}
