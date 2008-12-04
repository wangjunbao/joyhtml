/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joy.analyzer;

/**
 *
 * @author Lamfeeling
 */
public abstract class Document {
    private String content;

    public Document(String content) {
        this.content = content;
    }

    public abstract String getParagraphs();
     
    public String getContent() {
        return content;
    }

    
}
