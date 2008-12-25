/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joy.analyzer.Paragraph;

/**
 * 从给定的半html文档字符中提取出段落
 * @author Lamfeeling
 */
public class ParagraphExtractor {

    private String body,  whole;

    public ParagraphExtractor(String body, String whole) {
        this.body = body;
        this.whole = whole;
    }

    /**
     * extract all the paragraphs from given text and add them to the paragraph list
     * @param bodyText
     * @param whole
     * @throws java.io.IOException
     */
    public List<Paragraph> extract() {
        try {
            ArrayList<Paragraph> paragraphList = new ArrayList<Paragraph>();
            //seperate the main body with other parts of the whold text
            int start = whole.indexOf(body);
            int end = start + body.length();

            parse(whole.substring(0, start), paragraphList, .1);
            //now, add the main body
            parse(body, paragraphList,.3);

            parse(whole.substring(end), paragraphList, .1);
            return paragraphList;
        } catch (IOException ex) {
            Logger.getLogger(ParagraphExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * parse the semi-html text
     * @param str
     * @param paraList
     * @param base
     * @throws java.io.IOException
     */
    private void parse(String str, ArrayList<Paragraph> paraList, double base) throws IOException {
        BufferedReader r = new BufferedReader(new StringReader(str));
        String line = r.readLine();
        while (line != null) {
            double weight = base;
            if (line.startsWith("<H")) {
                //在這裡計算文檔的權重
                weight += .3;
            }
            paraList.add(new Paragraph(line.replaceAll("</*H[1-9]>", ""), weight));
            line = r.readLine();
        }
    }
}
