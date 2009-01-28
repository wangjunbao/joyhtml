/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

/**
 * 分析器抽象类，这个类完成一个对文档的分析操作，
 * 可以是Hits提取，命名实体提取，以及其他的自然语言分析。
 * @author Lamfeeling
 */
public abstract class Analyzer {

    protected Document doc;

    /**
     * 分析器构造函数
     * @param doc 传入一个文档对象以供分析用途。
     */
    public Analyzer(Document doc) {
        this.doc = doc;
    }

    /**
     * 执行分析操作。子类可以继承此方法以用于分析操作。
     */
    public abstract void doAnalyze();
}
