/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

import java.text.DecimalFormat;
import java.util.Vector;

/**
 * Hit类表示一个关键词在文章当中的所有命中位置， 以及它在文章当中所占比重的抽象。
 * 
 * @author Lamfeeling
 */
public class Hit implements Comparable<Hit> {

	private double score;
	private String term;
	private Vector<Integer> pos;

	/**
	 * 用一個关键词文本初始化一个Hit，初始化后，pos为一个无长度的链表
	 * 
	 * @param term
	 *            Hit的关键词文本。
	 */
	public Hit(String term) {
		this.term = term;
		pos = new Vector<Integer>();
	}

	/**
	 * 获取这个关键词的分数。
	 * 
	 * @return 关键词分数
	 */
	public double getScore() {
		return score;
	}

	/**
	 * 设置这个Hit的分数
	 * 
	 * @param score
	 *            关键词的分数
	 */
	public void setScore(double score) {
		this.score = score;
	}

	/**
	 * 获取这个关键词hit中的在文本中的所有位置
	 * 
	 * @return 关键词在文本中的所有位置
	 */
	public Vector<Integer> getPos() {
		return pos;
	}

	/**
	 * 像該hit中添加一個文章位置
	 * 
	 * @param pos
	 */
	public void addPos(int pos) {
		this.pos.add(pos);
	}

	/**
	 * 设置这个Hit的关键词
	 * @param term 关键词文本
	 */
	public void setTerm(String term) {
		this.term = term;
	}

	/**
	 * 获取这个关键词的文本
	 * 
	 * @return 关键词的文本
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * Hit类的字符串表示，用于测试，不应用于阁下的编码中
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return term + ":" + new DecimalFormat("0.00").format(score);
	}

	public int compareTo(Hit h) {
		return Double.compare(h.getScore(), getScore());
	}
}
