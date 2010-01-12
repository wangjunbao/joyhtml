/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.nlp;

import java.util.Vector;

/**
 * Word spliter. Splits text into words. It's similar to Lucene Analyzer
 * 
 * @author Song Liu(lamfeeling2@gmail.com)
 */
public abstract class WordSpliter {

    /**
     * close words spliter, release possessed resources
     */
    public abstract void close();

    /**
     * split text into words array
     * 
     * @param text
     * 
     * @return words in String array
     */
    public abstract String[] split(String text);

    /**
     * return pos tagged result
     * 
     * @param text
     * 
     * @param isTagged
     *            return pos tagged result?
     * 
     * @return splitted words
     */
    public abstract String split(String text, boolean isTagged);

    /**
     * Split text into Word array
     * 
     * @param text
     * 
     * @return result array of Word objects
     */
    public Word[] splitToWords(String text) {
	String[] res = split(text);
	Vector<Word> words = new Vector<Word>();
	for (String t : res) {
	    words.add(new Word(t + "/x"));
	}
	return words.toArray(new Word[0]);
    }
}
