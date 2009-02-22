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
                "http://news.sina.com.cn/c/2008-12-11/155516828944.shtml");

        HTMLDocument doc = HTMLDocument.createHTMLDocument(
                "http://news.sina.com.cn/c/2008-12-11/155516828944.shtml", data);

        HitAnalyzer a = new HitAnalyzer(doc, new ACWordSpliter());
        a.doAnalyze();
        System.out.println(Arrays.toString(a.getTermSet().toArray(new String[0])));
        Hit[] hits = a.getHits().toArray(new Hit[0]);
        System.out.println(Arrays.toString(hits));

    }
}
