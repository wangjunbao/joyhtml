/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.joy.analyzer.html.Anchor;
import org.joy.analyzer.html.HTMLDocument;
import org.joy.analyzer.html.KeyWords;
import org.joy.analyzer.html.ParseException;
import org.joy.analyzer.nlp.Spliter;

/**
 *
 * @author Lamfeeling
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException, FileNotFoundException, IOException {
        // TODO 在这里添加测试代码

        BufferedReader br = new BufferedReader(new InputStreamReader(new URL("http://sports.sina.com.cn/n/2009-01-19/11484178900.shtml").openStream()));
        String line = br.readLine();
        StringBuffer sb =new StringBuffer();
        while(line!=null){
            sb.append(line);
            line = br.readLine();
        }
        br.close();

        HTMLDocument doc = HTMLDocument.createHTMLDocument("http://sports.sina.com.cn/n/2009-01-19/11484178900.shtml", sb.toString());
//        for(Anchor a:doc.getAnchors()){
//            if(a!=null)
//            System.out.println(a.getText()+"   =>   "+a.getURL());



     //       FileWriter w = new FileWriter("d:/test.txt");
        //w.write(doc.getBodyText());//.replaceAll("\\r\\n+", "\r\n"));
       // w.close();
       // }

//         for(Paragraph p:doc.getParagraphs())
//            {
//                if(p!=null)
//                {
//                   System.out.println(p.getText()+"     -----"+p.getWeight()+"----"+p.getOffset());
//                }
//            }

        KeyWords keywords=new KeyWords(doc.getParagraphs());
        for(Hit hit:keywords.getHitList())
        {
            System.out.print(hit.getBody()+"---"+hit.getWeight()+"---");
            for(int i=0;i<hit.getPos().size();i++)
            {
                System.out.print(hit.getPos().get(i)+"  ");
            }
            System.out.println();
        }
    }
}
