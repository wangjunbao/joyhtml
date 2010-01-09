/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.nlp.ac;

import ICTCLAS.I3S.AC.ICTCLAS30;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joy.nlp.Word;
import org.joy.nlp.WordSpliter;

/**
 * 中科院分词ICTCLAS分词，分词可以再x86的机器上使用。
 * 
 * @author Administrator
 */
public class ACWordSpliter extends WordSpliter {

    private static ICTCLAS30 i = null;
    private final static Object waiter = new Object();
    private static final String DIC_HOME = "/tmp/";

    public ACWordSpliter() {
	synchronized (waiter) {
	    if (i == null) {
		try {
		    System.out.println("Initialising dicts...");

		    FileOutputStream fos = new FileOutputStream(DIC_HOME
			    + "Configure.xml");
		    InputStream is = this.getClass().getResourceAsStream(
			    "Configure.xml");
		    byte[] buf = new byte[1024];
		    int length = is.read(buf);
		    while (length != -1) {
			fos.write(buf, 0, length);
			length = is.read(buf);
		    }
		    fos.close();

		    fos = new FileOutputStream(DIC_HOME + "dicts.zip");
		    is = this.getClass().getResourceAsStream("dicts.zip");
		    buf = new byte[1024];
		    length = is.read(buf);
		    while (length != -1) {
			fos.write(buf, 0, length);
			length = is.read(buf);
		    }
		    fos.close();
		    Runtime.getRuntime().exec(
			    "unzip -o" + DIC_HOME + "dicts.zip -d " + DIC_HOME+"dicts/");

		    i = new ICTCLAS30();
		    i.ICTCLAS_Init(DIC_HOME.getBytes("gb2312"));

		} catch (Exception ex) {
		    Logger.getLogger(ACWordSpliter.class.getName()).log(
			    Level.SEVERE, null, ex);
		}
	    } else {
		System.err.println("尝试多次初始化分词词典！");
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
		return new String(i.ICTCLAS_ParagraphProcess(text
			.getBytes("gb2312"), isTagged ? 1 : 0), "gb2312")
			.trim();
	    } catch (UnsupportedEncodingException ex) {
		Logger.getLogger(ACWordSpliter.class.getName()).log(
			Level.SEVERE, null, ex);
	    }
	    return null;
	}
    }

    public Word[] splitToWords(String text) {
	String t = new String(split(text, true));
	String[] s = t.trim().split("\\s");
	ArrayList<Word> words = new ArrayList<Word>();
	for (String cell : s) {
	    if (cell.contains("/")) {
		words.add(new Word(cell));
	    }
	}
	return words.toArray(new Word[0]);
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
	ACWordSpliter w = new ACWordSpliter();
	System.out.println(w.split("科学发展观", true));
	System.out.println(Arrays.asList(w.split("刘挺拔出宝剑！")));
	new ACWordSpliter();
	w.splitToWords("他从马上摔下来。");
	w.close();
	System.out.println(w.split("圆明园兔首鼠首将被拍卖。", false));
    }
}
