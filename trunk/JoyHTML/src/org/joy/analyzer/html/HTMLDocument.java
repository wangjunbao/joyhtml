/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joy.analyzer.html;

import org.joy.analyzer.Document;

/**
 *
 * @author Lamfeeling
 */
public class HTMLDocument extends Document{

    public HTMLDocument createHTMLDocument(String str){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected HTMLDocument(String content) {
        super(content);
    }

    @Override
    public String getParagraphs() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Anchor[] getAnchors(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
