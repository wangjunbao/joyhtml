/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joy.analyzer.scoring.PWFScorer;
import org.joy.analyzer.scoring.Scorer;
import org.joy.analyzer.terms.SimpleTermExtractor;
import org.joy.analyzer.terms.TermExtractor;
import org.joy.nlp.Word;

/**
 * 用于分析和提取文章中Hit的分析器
 * 
 * @author Administrator
 */
public class HitAnalyzer extends Analyzer<List<Word>, List<Hit>> {

    private List<Hit> hitList = new ArrayList<Hit>();
    private Set<String> termSet = new HashSet<String>();

    public HitAnalyzer() {
    }

    /**
     * 构造一个HitAnaylzer
     * 
     * @param doc
     *            所要分析的文档对象
     * @param spliter
     *            所用的分词器
     */
    public HitAnalyzer(Document doc) {
	super(doc);
    }

    /**
     * 获取分析结果
     * 
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
	    Logger.getLogger(HitAnalyzer.class.getName()).log(Level.SEVERE,
		    null, ex);
	} catch (IllegalAccessException ex) {
	    Logger.getLogger(HitAnalyzer.class.getName()).log(Level.SEVERE,
		    null, ex);
	}
    }

    public void doAnalyze(Class<? extends Scorer> scorerClass,
	    Class<? extends TermExtractor> extractorClass)
	    throws InstantiationException, IllegalAccessException {
	hitList.clear();
	termSet.clear();

	Scorer scorer = scorerClass.newInstance();
	TermExtractor extractor = extractorClass.newInstance();

	scorer.setParagraphs(doc.getParagraphs());
	extractor.setWords(input.toArray(new Word[0]));
	termSet = extractor.getTerms();

	for (String term : termSet) {
	    Hit h = new Hit(term);
	    for (Paragraph p : doc.paragraphs) {
		//ignore the case
		int index = p.getText().toLowerCase().indexOf(term);
		while (index != -1) {
		    h.addPos(index + p.getOffset());
		    //ignore the case
		    index = p.getText().toLowerCase().indexOf(term, index + 1);
		}
	    }
	    h.setScore(scorer.getScore(h.getTerm(), h.getPos()));
	    hitList.add(h);
	}
	// 排序hitList
	Collections.sort(hitList);
	output = hitList;
    }

    public Set<String> getTermSet() {
	return termSet;
    }

}
