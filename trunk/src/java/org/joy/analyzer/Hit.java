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
    public Hit(String body)
    {
        this.body=body;
        weight=0;
        pos=new Vector<Integer>();
    }

    public Vector<Integer> getPos() {
        return pos;
    }

    public void setPos(Vector<Integer> pos) {
        this.pos = pos;
    }

    public String getBody() {
        return body;
    }

    public double getWeight() {
        return weight;
    }



    public void setWeight(double weight) {
        this.weight = weight;
    }
    String body;
    double weight;
    Vector<Integer> pos;
}
