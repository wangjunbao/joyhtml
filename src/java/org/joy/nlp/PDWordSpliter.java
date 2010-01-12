package org.joy.nlp;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import net.paoding.analysis.analyzer.PaodingAnalyzer;
import net.paoding.analysis.analyzer.PaodingAnalyzerBean;

/**
 * Paoding word spliter, written in java.
 * 
 * @author Andy
 * 
 */
public class PDWordSpliter extends WordSpliter {
    private static PaodingAnalyzer analyzer = new PaodingAnalyzer();
    static {
	analyzer.setMode("max-word-length");
    }

    @Override
    public void close() {
    }

    @Override
    public String[] split(String text) {
	try {
	    TokenStream stream = analyzer.tokenStream("",
		    new StringReader(text));
	    Token token;
	    Vector<String> tokens = new Vector<String>();
	    while ((token = stream.next()) != null) {
		tokens.add(token.termText());
	    }
	    stream.close();
	    return tokens.toArray(new String[0]);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    @Override
    public String split(String text, boolean isTagged) {
	throw new NotImplementedException();
    }

    public static void main(String[] args) {
	System.out.println(Arrays.asList(new PDWordSpliter()
		.split("欧盟轮值主席国主持每一届欧盟领导人峰会。")));
    }
}
