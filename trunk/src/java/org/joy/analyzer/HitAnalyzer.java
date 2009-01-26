/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.joy.analyzer.scoring.Scorer;
import org.joy.analyzer.scoring.ZeroScorer;
import org.joy.nlp.WordSpliter;

/**
 *
 * @author Administrator
 */
public class HitAnalyzer extends Analyzer {

    private WordSpliter spliter;
    private List<Hit> hitList = new ArrayList<Hit>();
    private Set<String> termSet;

    public HitAnalyzer(Document doc, WordSpliter spliter) {
        super(doc);
        this.spliter = spliter;
    }

    private HashSet<String> getTerms(String s) {
        String[] terms = spliter.split(s);
        return new HashSet<String>(Arrays.asList(terms));
    }

    public List<Hit> getHits() {
        return hitList;
    }

    @Override
    public void doAnalyze() {
        doAnalyze(new ZeroScorer());
    }

    public void doAnalyze(Scorer s) {
        termSet = getTerms(doc.getContent());
        String content = doc.getContent();
        for (String term : termSet) {
            int index = content.indexOf(term);
            Hit h = new Hit(term);
            while (index != -1) {
                h.setScore(s.getScore(h.getTerm()));
                h.addPos(index);
                index = content.indexOf(term, index + 1);
            }
            hitList.add(h);
        }
    }

    public Set<String> getTermSet() {
        return termSet;
    }
}
