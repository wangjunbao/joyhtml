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
 * 把给定的半html文档字符按照行，分裂成段落
 * @author Lamfeeling
 */
public class ParagraphSplitter {

    private String body,  whole;

    public ParagraphSplitter(String body, String whole) {
        this.body = body;
        this.whole = whole;
    }

    /**
     * split all the paragraphs from given text and add them to the paragraph list
     * @throws java.io.IOException
     */
    public List<Paragraph> split() {
        try {
            ArrayList<Paragraph> paragraphList = new ArrayList<Paragraph>();
            //seperate the main body with other parts of the whold text
            int start = whole.indexOf(body);
            int end = start + body.length();

            split(whole.substring(0, start), paragraphList, .1);
            //now, add the main body
            split(body, paragraphList,.3);

            split(whole.substring(end), paragraphList, .1);
            return paragraphList;
        } catch (IOException ex) {
            Logger.getLogger(ParagraphSplitter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * 分解给定文本中的每一行，并且把残存的视觉标签转化成得分，累加到主题的分里，
     * 并且把它们存储进paragraphList中
     * @param str 给定的文本
     * @param paraList 需要存储的paraList
     * @param base 基准得分。
     * @throws java.io.IOException
     */
    private void split(String str, ArrayList<Paragraph> paraList, double base) throws IOException {
        BufferedReader r = new BufferedReader(new StringReader(str));
        String line = r.readLine();
        while (line != null) {
            double weight = base;
            if (line.startsWith("<H")) {
                //在这里给文档的主题相关度加上显示上的得分因素，比如大标题等等
                weight += .3;
            }
            paraList.add(new Paragraph(line.replaceAll("</*H[1-9]>", ""), weight));
            line = r.readLine();
        }
    }
}
