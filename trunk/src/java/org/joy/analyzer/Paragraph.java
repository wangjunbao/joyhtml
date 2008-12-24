/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

import java.util.ArrayList;

/**
 *
 * @author Lamfeeling
 */
public class Paragraph {

    private String text;
    private double weight;
    private ArrayList<String> highlight = new ArrayList<String>();

    public Paragraph() {
    }

    public Paragraph(String text, double weight) {
        this.text = text;
        this.weight = weight;
    }

    public String getText() {
        return text;
    }

    public double getWeight() {
        return weight;
    }
}
