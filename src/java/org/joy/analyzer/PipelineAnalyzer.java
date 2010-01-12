package org.joy.analyzer;

/**
 * Pipe line analyzer, which is also an analyzer, can be used to coordinate
 * several analyzer's operation
 * 
 * @author Song Liu(lamfeeling2@gmail.com)
 * 
 * @param <K> input class for this analyzer
 * @param <E> output class for this analyzer
 */
public class PipelineAnalyzer<K, E> extends Analyzer<K, E> {
    protected Analyzer<Object, Object>[] analyzers;

    public PipelineAnalyzer(Analyzer<Object, Object>[] analyzers) {
	this.analyzers = analyzers;
    }

    @Override
    public void doAnalyze() {
	output = null;
	Object in = input;
	for (Analyzer<Object, Object> a : analyzers) {
	    a.setDoc(doc);
	    a.input(in);
	    a.doAnalyze();
	    in = a.output();
	}
	output = (E) in;
    }

}
