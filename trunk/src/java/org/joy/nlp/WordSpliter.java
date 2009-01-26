/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.nlp;

import ICTCLAS.I3S.AC.ICTCLAS30;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class WordSpliter {

    private static ICTCLAS30 i = null;
    private final static Object waiter = new Object();

    public WordSpliter() {
        synchronized (waiter) {
            if (i == null) {
                try {
                    i = new ICTCLAS30();
                    i.ICTCLAS_Init("".getBytes("gb2312"));
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(WordSpliter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public String[] split(String text) {
        return split(text, false).split("\\s+");
    }

    public String split(String text, boolean isTagged) {
        synchronized (waiter) {
            if (i == null) {
                System.err.println("分词系统已经被关闭");
                return null;
            }
            try {
                return new String(i.ICTCLAS_ParagraphProcess(text.getBytes("gb2312"), isTagged ? 1 : 0), "gb2312");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(WordSpliter.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }
    }

    public void close() {
        synchronized (waiter) {
            if (i != null) {
                i.ICTCLAS_Exit();
                i = null;
            }
        }
    }

    public static void main(String[] args) {
        WordSpliter w = new WordSpliter();
        System.out.println(w.split("中国人是好人", false));
        System.out.println(Arrays.asList(w.split("中国人都是好人啊！")));
        w.close();
        System.out.println(w.split("中国人是好人", false));
    }
}