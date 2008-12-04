/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joy.analyzer;

import java.util.List;

/**
 * ³éÏóÎÄµµÀà
 * @author Lamfeeling
 */
public abstract class Document {
    private String content;

    public Document(String content) {
        this.content = content;
    }

    public abstract List<Paragraph> getParagraphs();
    public abstract String getTitle();
    
    public String getContent() {
        return content;
    }
}
