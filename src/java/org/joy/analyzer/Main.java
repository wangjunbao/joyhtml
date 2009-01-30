/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import org.joy.analyzer.html.HTMLDocument;
import org.joy.analyzer.html.ParseException;
import org.joy.nlp.ACWordSpliter;

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
        String data = org.joy.analyzer.html.Utility.getWebContent(
                "http://scst.suda.edu.cn/article/20081229085402467.html");

        HTMLDocument doc = HTMLDocument.createHTMLDocument(
                "http://scst.suda.edu.cn/article/20081229085402467.html", data);

        FileWriter w = new FileWriter("c:/output.txt");
        w.write(doc.getContent());
        w.close();
        HitAnalyzer a = new HitAnalyzer(doc, new ACWordSpliter());
        System.setOut(new PrintStream("c:/a.txt"));
        a.doAnalyze();
        System.out.println(Arrays.toString(a.getTermSet().toArray(new String[0])));
        Hit[] hits = a.getHits().toArray(new Hit[0]);
        Arrays.sort(hits);
        System.out.println(Arrays.toString(hits));

    }
}
