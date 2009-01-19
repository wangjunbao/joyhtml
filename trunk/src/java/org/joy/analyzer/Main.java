/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.joy.analyzer.html.Anchor;
import org.joy.analyzer.html.HTMLDocument;
import org.joy.analyzer.html.ParseException;

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
        FileReader r = new FileReader("test.htm");
        BufferedReader br = new BufferedReader(r);
        String line = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }
        br.close();

        HTMLDocument doc = HTMLDocument.createHTMLDocument("http://news.sina.com.cn", sb.toString());
        for (Anchor a : doc.getAnchors()) {
            if (a != null) {
                System.out.println(a.getText() + "   =>   " + a.getURL());
            }



        //       FileWriter w = new FileWriter("d:/test.txt");
        //w.write(doc.getBodyText());//.replaceAll("\\r\\n+", "\r\n"));
        // w.close();
        }

        for (Paragraph p : doc.getParagraphs()) {
            if (p != null) {
                System.out.println(p.getText() + "     -----" + p.getWeight());
            }
        }
    }
}
