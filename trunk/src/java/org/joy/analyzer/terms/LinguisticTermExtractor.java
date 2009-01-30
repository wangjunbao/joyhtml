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
 *
 * @author Administrator
 */
public class LinguisticTermExtractor extends TermExtractor {

    protected String pattern = "(( [^\\s]+/a[a-z]*)|( [^\\s]+/n[a-zZ]*))+( [^\\s]+/cc)*( [^\\s]+/n[a-z]*)";

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
            terms.add(sb.toString());
        }
        return terms;
    }
}
