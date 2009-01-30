/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.terms;

import java.util.HashSet;
import org.joy.nlp.Word;

/**
 *
 * @author Administrator
 */
public abstract class TermExtractor {

    protected Word[] words;
    protected String taggedtext;
    
    public TermExtractor() {
    }

    public void setWords(Word[] words) {
        this.words = words;
        StringBuffer sb = new StringBuffer();
        for(Word w :words){
            sb.append(w.getText()+"/"+w.getTag()+" ");
        }
        taggedtext = sb.toString();
    }

    public abstract HashSet<String> getTerms();
}
