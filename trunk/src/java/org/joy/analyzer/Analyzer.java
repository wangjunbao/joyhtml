/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joy.analyzer;

/**
 *
 * @author Lamfeeling
 */
public abstract class Analyzer {
    protected Document doc;
    public Analyzer(Document doc){
        this.doc = doc;
    }

    public abstract void doAnalyze();
}
