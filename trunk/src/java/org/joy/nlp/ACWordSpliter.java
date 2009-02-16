/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.nlp;

import ICTCLAS.I3S.AC.ICTCLAS30;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Administrator
 */
public class ACWordSpliter implements WordSpliter {

	private static ICTCLAS30 i = null;
	private final static Object waiter = new Object();

	public ACWordSpliter() {
		synchronized (waiter) {
			if (i == null) {
				try {
					System.out.println("Initialising dicts...");
					i = new ICTCLAS30();
					i.ICTCLAS_Init("".getBytes("gb2312"));
				} catch (UnsupportedEncodingException ex) {
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
						.getBytes("gb2312"), isTagged ? 1 : 0), "gb2312");
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
		ArrayList<Word> words = new ArrayList();
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
		System.out.println(w.split("美国中央情报局是特务间谍机构。", true));
		System.out.println(Arrays.asList(w.split("中国人都是好人啊！")));
		new ACWordSpliter();
		w.splitToWords("中国大陆是共产党控制的。");
		w.close();
		System.out.println(w.split("中国人是好人", false));
	}
}
