/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.Arrays;
import org.joy.analyzer.html.HTMLDocument;
import org.joy.analyzer.html.ParseException;
import org.joy.nlp.WordSpliter;

/**
 *
 * @author Lamfeeling
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
        // TODO 在这里添加测试代码
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL("http://sports.sina.com.cn/n/2009-01-19/11484178900.shtml").openStream()));
        String line = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }
        br.close();

        HTMLDocument doc = HTMLDocument.createHTMLDocument("http://sports.sina.com.cn/n/2009-01-19/11484178900.shtml", sb.toString());

        FileWriter w = new FileWriter("c:/output.txt");
        w.write(doc.getContent());
        w.close();
        HitAnalyzer a = new HitAnalyzer(doc, new WordSpliter());
        a.doAnalyze();
        System.setOut(new PrintStream("c:/a.txt"));
        System.out.println(Arrays.toString(a.getTermSet().toArray(new String[0])));
        System.out.println(Arrays.toString(a.getHits().toArray(new Hit[0])));

    }
}
