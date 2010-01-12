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
 * find all the hits from one document.
 * 
 * @author Song Liu(lamfeeling2@Gmail.com)
 */
public class HitAnalyzer extends Analyzer<List<Word>, List<Hit>> {

    private List<Hit> hitList = new ArrayList<Hit>();
    private Set<String> termSet = new HashSet<String>();

    public HitAnalyzer() {
    }

    /**
     * construct a HitAnalyzer
     * 
     * @param doc
     *            document you want to analyze with
     */
    public HitAnalyzer(Document doc) {
	super(doc);
    }

    /**
     * get Hits for this document
     * 
     * @return hits for this document
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
		// ignore the case
		int index = p.getText().toLowerCase().indexOf(term);
		while (index != -1) {
		    h.addPos(index + p.getOffset());
		    // ignore the case
		    index = p.getText().toLowerCase().indexOf(term, index + 1);
		}
	    }
	    h.setScore(scorer.getScore(h.getTerm(), h.getPos()));
	    hitList.add(h);
	}
	// sort hitList
	Collections.sort(hitList);
	output = hitList;
    }

    public Set<String> getTermSet() {
	return termSet;
    }

}
