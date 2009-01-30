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
import org.joy.analyzer.scoring.PWFScorer;
import org.joy.analyzer.scoring.Scorer;
import org.joy.analyzer.terms.LinguisticTermExtractor;
import org.joy.analyzer.terms.SimpleTermExtractor;
import org.joy.analyzer.terms.TermExtractor;
import org.joy.nlp.ACWordSpliter;

/**
 * 用于分析和提取文章中Hit的分析器
 * @author Administrator
 */
public class HitAnalyzer extends Analyzer {

    private ACWordSpliter spliter;
    private List<Hit> hitList = new ArrayList<Hit>();
    private Set<String> termSet;

    public HitAnalyzer() {
    }

    public void setSpliter(ACWordSpliter spliter) {
        this.spliter = spliter;
    }

    /**
     * 构造一个HitAnaylzer
     * @param doc 所要分析的文档对象
     * @param spliter 所用的分词器
     */
    public HitAnalyzer(Document doc, ACWordSpliter spliter) {
        super(doc);
        this.spliter = spliter;
    }

    /**
     * 获取分析结果
     * @return 该片文章当中的Hits
     */
    public List<Hit> getHits() {
        return hitList;
    }

    @Override
    public void doAnalyze() {
        try {
            doAnalyze(PWFScorer.class, SimpleTermExtractor.class);
        } catch (InstantiationException ex) {
            Logger.getLogger(HitAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(HitAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void doAnalyze(Class<? extends Scorer> scorerClass, Class<? extends TermExtractor> extractorClass) throws InstantiationException, IllegalAccessException {
        Scorer scorer = scorerClass.newInstance();
        TermExtractor extractor = extractorClass.newInstance();

        scorer.setParagraphs(doc.getParagraphs());
        extractor.setWords(spliter.splitToWords(doc.getContent()));
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
