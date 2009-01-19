/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joy.analyzer.html;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joy.analyzer.Hit;
import org.joy.analyzer.Paragraph;
import org.joy.analyzer.nlp.Spliter;

/**
 *
 * @author hd
 */
public class KeyWords {

    List <Hit> hitList;


    String[] keyWords;
    Set<String> keyWordsSet;
    List <Paragraph>paragraphLists=new ArrayList<Paragraph>() ;

    public KeyWords(List <Paragraph>paragraphLists) {


        this.paragraphLists=paragraphLists;
        getKeyWords();
        getHits();
    }





    private void getHits() {
        hitList=new ArrayList<Hit>() ;

        keyWordsSet=new HashSet<String>();


        gekeyWordsSet();

        dealHits();


    }

    private void dealHits() {

        Iterator<String> it=keyWordsSet.iterator();
        while(it.hasNext())
        {
            String key=it.next();

           callWeightAndPos(key);
        }

    }
     private void callWeightAndPos(String key) {
           Hit  hit=new Hit(key);
         Vector <Integer>pos=new Vector<Integer>();
        double weight=0;

        Pattern p=Pattern.compile(key);
      
        for(Paragraph para:paragraphLists)
        {
            String paraText=para.getText();
            double paraWeight=para.getWeight();
            int paraOffset=para.getOffset();

            Matcher m=p.matcher(paraText);

            while(m.find())
            {
                weight+=paraWeight;
                int n=paraOffset+m.end()-key.length()+1;
                pos.add(n);
            }

        }
        hit.setWeight(weight);
        hit.setPos(pos);
        hitList.add(hit);

    }


     private void getKeyWords() {
         String whole="";
         for(Paragraph p:paragraphLists)
         {
             whole+=p.getText();
         }
//         System.out.println(whole);


       Spliter s = new Spliter();
       keyWords= s.split2(whole);

    }
    private void gekeyWordsSet() {
        for(int i=0;i<keyWords.length;i++)
        {
            keyWordsSet.add(keyWords[i]);
        }

    }


    public List<Hit> getHitList() {
        return hitList;
    }


}
