/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

import java.util.Vector;

/**
 *
 * @author Lamfeeling
 */
public class Hit {

    private double score;
    private String term;
    private Vector<Integer> pos;

    public Hit(String body) {
        this.term = body;
        pos = new Vector<Integer>();
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Vector<Integer> getPos() {
        return pos;
    }

    public void addPos(int pos) {
        this.pos.add(pos);
    }

    public String getTerm() {
        return term;
    }
}
