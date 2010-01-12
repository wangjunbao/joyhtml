package org.joy.analyzer;

/**
 * Analyzer is the operator for doing some analysis for a single document. By
 * extending this class, many operations can be intergrated with JoyDoc system
 * 
 * Each analyzer has an input and output, which are indicated by parameter K,
 * and E.
 * 
 * Each analyzer is also a pipe line that can be cooperated with other analyzer,
 * by specifying proper input and output classes and using pipelineAnalyzer.
 * 
 * @param <K>
 *            Input Class for this analyzer
 * @param <E>
 *            Output Class for this analyzer
 * @author Song Liu(lamfeeling2@gmail.com)
 */
public abstract class Analyzer<K, E> {

    protected Document doc;
    protected Paragraph para;
    protected K input;
    protected E output;

    public Analyzer() {
    }

    /**
     * set the document you want to analyze with.
     * 
     * @param doc
     */
    public void setDoc(Document doc) {
	this.doc = doc;
    }

    /**
     * constructor
     * 
     * @param doc
     *            the document you want to analyze with.
     */
    public Analyzer(Document doc) {
	this.doc = doc;
    }

    /**
     * input resource for this analyzer
     * 
     * @param inputResource
     * 
     */
    public void input(K inputResource) {
	input = inputResource;
    }

    /**
     * output for this analyzer
     * 
     * @return output
     */
    public E output() {
	return output;
    }

    public Class<?> getInputClass() {
	return input.getClass();
    }

    public Class<?> getOutputClass() {
	return output.getClass();
    }

    /**
     * do actual operation on document.
     */
    public abstract void doAnalyze();
}
