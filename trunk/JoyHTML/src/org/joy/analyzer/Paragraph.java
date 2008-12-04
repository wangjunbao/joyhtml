/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joy.analyzer;

/**
 *
 * @author Lamfeeling
 */
public class Paragraph {
    private String text;
    private int weight;

    public Paragraph() {
    }

    public Paragraph(String text, int weight) {
        this.text = text;
        this.weight = weight;
    }

    public String getText() {
        return text;
    }

    public int getWeight() {
        return weight;
    }

    
}
