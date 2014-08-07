package RevMsg;

//The outcome message class
//This class is the data structure of message type"Outcome".
public class OutcomeRecMsg extends BaseRevMsg {

   //<outcome>   ::=  (outcome <result>)
   //<result>    ::=  <move>
   //            || <compare> <move>
   private String srcpos;// refers to the first move
   private String destpos;// refers to the second move
   private String comp;// refers to compare
   
   // In this construction method, we can set type, srcpos,
   // destpos, comp and bReverse.
   public OutcomeRecMsg
   (int type, String srcpos,String destpos,String comp, boolean bReverse) {
      super(type, bReverse);
      if (bReverse) {
         this.srcpos = reversePos(srcpos); 
         this.destpos = reversePos(destpos); 
      }
      else {
         this.srcpos = srcpos;
         this.destpos = destpos;
      }
      this.comp = comp;
   }

   //getter method
   //We can get srcpos by calling this method
   public String GetSrcPos() {
      return srcpos;
   }
   
   //getter method
   //We can get destpos by calling this method
   public String GetDestPos() {
      return destpos;
   }
   
   //getter method
   //We can get comp by calling this method
   public String GetComp() {
      return comp;
   }
}
