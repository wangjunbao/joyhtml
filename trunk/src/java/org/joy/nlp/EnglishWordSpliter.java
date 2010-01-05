package org.joy.nlp;

import java.util.Vector;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class EnglishWordSpliter extends WordSpliter {

    @Override
    public void close() {

    }

    @Override
    public String[] split(String text) {
	String[] words = text.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]+", "").split("\\s+");
	return words;
    }

    @Override
    public String split(String text, boolean isTagged) {
	throw new NotImplementedException();
    }

    @Override
    public Word[] splitToWords(String text) {
	String[] res = split(text);
	Vector<Word> words = new Vector<Word>();
	for (String t : res) {
	    words.add(new Word(t + "/x"));
	}
	return words.toArray(new Word[0]);
    }

}
