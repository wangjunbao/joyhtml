/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.terms;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joy.nlp.Word;

/**
 * LinguisticTermExtractor merges the words with specific pattern of POS tags
 * 
 * @author Song Liu(lamfeeling2@gmail.com)
 */
public class LinguisticTermExtractor extends TermExtractor {

    protected String pattern = "(( [^\\s]+/a[a-z]*)|( [^\\s]+/b[a-z]*)|( [^\\s]+/n[a-zZ]*))+( [^\\s]+/n[a-z]*)";
    private final static int MAX_LENGTH = 6;

    @Override
    public HashSet<String> getTerms() {
	Pattern p1 = Pattern.compile(pattern);
	Matcher matcher = p1.matcher(taggedtext);
	HashSet<String> terms = new HashSet<String>();
	while (matcher.find()) {
	    String t = matcher.group().trim();
	    StringBuffer sb = new StringBuffer();
	    for (String s : t.split("\\s")) {
		sb.append(new Word(s).getText());
	    }
	    if (sb.length() <= MAX_LENGTH) {
		terms.add(sb.toString());
	    }
	}
	return terms;
    }
}
