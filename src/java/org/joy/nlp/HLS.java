/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joy.nlp;

/**
 *
 * @author hd
 */
public class HLS {
     public native boolean HLSplitInit(String path);

      public native void HLFreeSplit();

      public native String HLS_ParagraphProcess(String content, int bPOSTagged);

      static
      {
          System.loadLibrary("HLS");
      }

      public static void main(String[] args) {
          HLS h=new HLS();
          h.HLSplitInit("");
          System.out.println(h.HLS_ParagraphProcess("胡新华是老大。", 1));
          System.out.println(h.HLS_ParagraphProcess("JOY柳松好。。。。", 0));
          h.HLFreeSplit();
      }



}
