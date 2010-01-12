package org.joy.nlp;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

/**
 * Word spliter using lucene analyzer
 * @author lamfeeling
 *
 */
public class LuceneWordSpliter extends WordSpliter {
    private static Analyzer ana = new StandardAnalyzer();

    @Override
    public void close() {

    }

    @Override
    public String[] split(String text) {
	ArrayList<String> words = new ArrayList<String>();
	TokenStream stream = ana.tokenStream("", new StringReader(text));
	Token t;
	try {
	    t = stream.next();
	    while (t != null) {
		words.add(t.termText());
		t = stream.next();
	    }
	    return words.toArray(new String[0]);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    @Override
    public String split(String text, boolean isTagged) {
	return null;
    }

    public static void main(String[] args) {
	WordSpliter w = new LuceneWordSpliter();
	System.out.println(w.split("科学发展观", true));
	System.out.println(Arrays.asList(w.split("中欧领导人商定尽快会晤。,The words are different and fucking damn it!")));
	w.splitToWords("他从马上摔下来。");
	System.out.println(w.split("圆明园兔首鼠首将被拍卖。", false));
    }
}
