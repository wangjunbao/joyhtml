/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.nlp;

/**
 *
 * @author Administrator
 */
public class Word {

    private String text;
    private String tag;

    public Word() {
    }

    public Word(String s) {
        int i = s.lastIndexOf("/");
        tag = s.substring(i + 1, s.length());
        text = s.substring(0, i);
    }




    public Word(String text, String tag) {
        this.text = text;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String getText() {
        return text;
    }

    
}
