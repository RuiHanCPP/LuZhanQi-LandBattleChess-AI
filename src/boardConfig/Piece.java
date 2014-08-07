package boardConfig;

/*
  The class of Piece.
  An object of this class can be an opponent piece 
  or our piece.
*/
public class Piece {
    
   public enum Rank {
      // Here, since all three of us are Chinese and 
      // are quite unfamiliar with the ranks in English,
      // We name those ranks(except engineer) in Chinese
      Flag, 
      LandMine, 
      Bomb, 
      SiLing, // Field Marshal in Chinese, rank 9
      JunZhang, // General in Chinese, rank 8
      ShiZhang, // Major General in Chinese, rank 7
      LvZhang, // Brigadier General in Chinese, rank 6
      TuanZhang, // Colonel in Chinese, rank 5
      YingZhang, // Major in Chinese, rank 4
      LianZhang, // Captain in Chinese, rank 3
      PaiZhang, // Lieutenant in Chinese, rank 2
      Engineer, // rank 1
      Unknown,
      Empty
   }
    
   private Rank rank; // the rank of the piece
   private boolean ourSide; // the side of the piece

   // a list of ranks that calculate who is bigger than whom
   private static final Rank[] rankComList = {Rank.Bomb, Rank.SiLing, 
      Rank.JunZhang, Rank.ShiZhang, Rank.LvZhang, Rank.TuanZhang,
      Rank.YingZhang, Rank.LianZhang, Rank.PaiZhang, Rank.Engineer};
   
   // the canBexxx constant record if the piece is able
   // to be xxx, it has nothing to do with its actual rank
   private boolean canBeBomb;
   private boolean canBeMine;
   private final boolean canBeFlag;
   
   // the minRank is the minimum possible rank of the piece
   // or it will always remain unknown if it is unable to 
   // "eat" a piece
   private Rank minRank;
    
   // Constructor of our piece.
   public Piece(Rank r, boolean side) {
      rank = r;
      ourSide = side;
      canBeBomb = false;
      canBeMine = false;
      minRank = Rank.Unknown;
      canBeFlag = false;
   }
   
   // constructor for opponent piece
   public Piece(Rank r, boolean side, boolean cbB, 
         boolean cbM, boolean cbF) {
      rank = r;
      ourSide = side;
      canBeBomb = cbB;
      canBeMine = cbM;
      minRank = Rank.Unknown;
      canBeFlag = cbF;
   }
    
   // getter
   public Rank getRank() {
      return rank;
   }
    
   // setter
   public void setRank(Rank r) {
      rank = r;
   }
    
   // getter
   public boolean getOurSide() {
      return ourSide;
   }
    
   // setter
   public void setOurSide(boolean ours) {
      ourSide = ours;
   }
   
   // getter
   public final boolean getcBeBomb(){
      return canBeBomb;
   }
   public boolean getcBeMine(){
      return canBeMine;
   }
   public final boolean getcBeFlag(){
      return canBeFlag;
   }
   public Rank getMinRank() {
      return minRank;
   }

   // the opponent piece had moved
   public void pMoved() {
      if (!ourSide && canBeMine) {
         canBeMine = false;
      }
   }
   
   /*
   void pKilled(Rank r):
   Given: The rank of the piece killed by this piece.
   Effect: Update the minimum expecting rank of this piece.
   */
   public void pKilled(Rank r) {
      if (!ourSide) {
         // enemy piece killed our piece!
         // so it's not bomb, land mine, or flag
         // and its rank is definitely greater than the piece
         // it has killed, if it didn't killed a land mine
         // if we lost land mine, the enemy piece is engineer for sure
         if (minRank.equals(Rank.Unknown) && !r.equals(Rank.LandMine)) {
            minRank = getHigherRank(r);
         }
         else if (!r.equals(Rank.LandMine)) {
            if (rankGreaterThan(r, minRank)) {
               minRank = r;
            }
         }
         else {
            setRank(Rank.Engineer);
            canBeMine = false;
            minRank = Rank.Engineer;
            canBeBomb = false;
         }
      }
   }
    
   // return a higher rank of the given one
   // the given one should be the one that was killed for sure
   // rank must be troops rank, not bomb, mine, flag, unknown, empty
   // if the given rank is field marshal(Siling), then the function
   // will return bomb, not itself
   public static Rank getHigherRank(Rank rank) {
      int i;
      for (i = 1; i < rankComList.length; ++i) {
         if (rank.equals(rankComList[i])) {
            if (i == 1) {
               // Ouch, we lost our Field Marshal
               return Rank.Bomb;
            }
            else {
               return rankComList[i - 1];
            }
         }
      }
       
      throw new IllegalArgumentException("Invalid rank given!"); 
   }
    
   // compare the two ranks, if the first one is greater, return true
   // otherwise return false, indicating r2 is less than or equal to
   // r1. However, the two ranks must be comparable
   // Which is to say, they are not empty, unknown, mine, flag
   public static boolean rankGreaterThan(Rank r1, Rank r2) {
      int i;
      for (i = 0; i < rankComList.length; ++i) {
         if (r1.equals(rankComList[i]) && !r1.equals(r2)) {
            return true;
         }
         else if (r2.equals(rankComList[i])) {
            return false;
         }
      }     
  
      throw new IllegalArgumentException("Invalid rank given!"); 
   }
   
   // given a rank
   // return if this rank is movable in our view
   public static boolean movableRank(Rank r) {
      if (r.equals(Rank.Empty) || r.equals(Rank.Flag) || 
          r.equals(Rank.Unknown) || r.equals((Rank.LandMine))) {
         return false;
      }
      return true;
   }
}
