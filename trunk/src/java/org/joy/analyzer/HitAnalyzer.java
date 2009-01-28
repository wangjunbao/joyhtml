/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joy.analyzer.scoring.FrequencyScorer;
import org.joy.analyzer.scoring.Scorer;
import org.joy.analyzer.terms.SimpleTermExtractor;
import org.joy.analyzer.terms.TermExtractor;
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

    public List<Hit> getHits() {
        return hitList;
    }

    @Override
    public void doAnalyze() {
        try {
            doAnalyze(FrequencyScorer.class, SimpleTermExtractor.class);
        } catch (InstantiationException ex) {
            Logger.getLogger(HitAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(HitAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void doAnalyze(Class<? extends Scorer> scorerClass, Class<? extends TermExtractor> extractorClass) throws InstantiationException, IllegalAccessException {
        Scorer scorer = scorerClass.newInstance();
        TermExtractor extractor = extractorClass.newInstance();

        scorer.load(doc.getParagraphs());
        extractor.load(spliter.splitToWords(doc.getContent()));
        termSet = extractor.getTerms();
        
        String content = doc.getContent();
        for (String term : termSet) {
            int index = content.indexOf(term);
            Hit h = new Hit(term);
            while (index != -1) {
                h.setScore(scorer.getScore(h.getTerm()));
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
