package ioControl;
import java.io.*;
public interface Output {
   public void outputMsg(String str);
}

class GetOutput implements Output {
/*********************************************
<initial>   ::=  ( <inits> )
<inits>     ::=  
                |  <init> <inits>
<init>      ::=  ( <position> <piece> )
<position>  ::=  A1 | A2 | A3 | A4 | A5 | A6 
          | A7 | A8 | A9 | A10 | A11 | A12
          |  B1 | B2 | B3 | B4 | B5 | B6 | B7 
          | B8 | B9 | B10 | B11 | B12
          |  C1 | C2 | C3 | C4 | C5 | C6 | C7 
          | C8 | C9 | C10 | C11 | C12
          |  D1 | D2 | D3 | D4 | D5 | D6 | D7 
          | D8 | D9 | D10 | D11 | D12
          |  E1 | E2 | E3 | E4 | E5 | E6 | E7 
          | E8 | E9 | E10 | E11 | E12
<piece>     ::=  F | L | B | 1 | 2 | 3 | 4 | 5 
               | 6 | 7 | 8 | 9
*********************************************/
   
   // output the given string to standard output
   public void outputMsg(String str) {
      //System.out.println(str);
      OutputStreamWriter osw = new OutputStreamWriter(System.out);
      BufferedWriter wr = new BufferedWriter(osw);
      try {
         wr.write(str);
         wr.flush();
      } catch(IOException e) {
         System.out.println(e);
      }
   }
}
