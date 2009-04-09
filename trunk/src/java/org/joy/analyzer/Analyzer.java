/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

/**
 * 分析器抽象类，这个类完成一个对文档的分析操作， 
 * 可以是Hits提取，命名实体提取，以及其他的自然语言分析。
 * 
 * 每个分析器都使用一个给定的输入和输出来完成一个指定的操作。
 * @param <K>
 *            这个分析器的输入类别
 * @param <E>
 *            这个分析器的输出类别
 * @author Lamfeeling
 */
public abstract class Analyzer<K, E> {

	protected Document doc;
	protected Paragraph para;
	protected K input;
	protected E output;

	public Analyzer() {
	}
	
	/**
	 * 设置这个Analyzer需要分析的Paragraph,如果这个分析器不是基于Paragraph的，这个参数可以不设置
	 * 
	 * @param para
	 */
	public void setPara(Paragraph para) {
		this.para = para;
	}
	
	/**
	 * 设置这个Analyzer需要分析的Doc,如果这个分析器不是基于Docment的，这个参数可以不设置
	 * 
	 * @param doc
	 */
	public void setDoc(Document doc) {
		this.doc = doc;
	}

	/**
	 * 分析器构造函数
	 * 
	 * @param doc
	 *            传入一个文档对象以供分析用途。
	 */
	public Analyzer(Document doc) {
		this.doc = doc;
	}

	/**
	 * 这个分析器的管道方法，输入一个资源
	 * 
	 * @param inputResource
	 *            输入资源
	 */
	public void input(K inputResource) {
		input = inputResource;
	}

	/**
	 * 这个分析器的管道方法，输出
	 * 
	 * @return 输出的资源
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
	 * 执行分析操作。子类可以继承此方法以用于分析操作。
	 */
	public abstract void doAnalyze();
}
