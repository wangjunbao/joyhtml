/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.html;

import org.cyberneko.html.parsers.DOMParser;
import org.joy.analyzer.Document;

/**
 *
 * @author Lamfeeling
 */
public class HTMLDocument extends Document {

    private Document doc;

    public HTMLDocument createHTMLDocument(String str) {
        DOMParser parser = new DOMParser();
        Document doc = (Document) parser.getDocument();
        //TODO: add parser code here

        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected HTMLDocument(String content, Document doc) {
        super(content);
        this.doc = doc;
    }

    @Override
    public String getParagraphs() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Anchor[] getAnchors() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
