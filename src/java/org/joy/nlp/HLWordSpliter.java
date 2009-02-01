/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joy.nlp;

import java.util.Arrays;

/**
 *
 * @author Administrator
 */
public class HLWordSpliter implements  WordSpliter {

    private static HLS h=null;
    private final static Object waiter = new Object();

    public HLWordSpliter() {
        synchronized(waiter)
        {
            if(h==null)
            {
                h=new HLS();
                h.HLSplitInit("");
            }
        }
    }

    public void close() {
        synchronized(waiter)
        {
            if(h!=null)
            {
                h.HLFreeSplit();
                h=null;
            }
        }

        
    }

    public String[] split(String text) {
       return split(text,false).split(" ");
    }

    public String split(String text, boolean isTagged) {
        synchronized(waiter)
        {
            if(h==null)
            {
                System.out.println("分词系统已经关闭！");
                return null;
            }
            else
            {
                return h.HLS_ParagraphProcess(text, isTagged ? 1:0);
            }
        }

       
    }

    public Word[] splitToWords(String text) {
      String[] s=split(text,true).trim().split(" ");
      Word[] words=new Word[s.length];
      for(int i=0;i<s.length;i++)
      {
          String wordText=s[i].split("/")[0];
          String wordTag=s[i].split("/")[1];

          words[i]=new Word(wordText,wordTag);
      }

      return words;
    }





     public static void main(String[] args) {
        HLWordSpliter h= new HLWordSpliter();
        System.out.println(h.split("美国中央情报局是特务间谍机构。", true));

       for( Word w:h.splitToWords("世界十大色狼之柳松大侠！"))
       {
           System.out.print(w.getText()+"/"+w.getTag()+" ");
       }
         System.out.println("");


        System.out.println(h.split("中国人是好人", false));
        h.close();
        System.out.println(h.split("中国人是好人", false));
    }

}
