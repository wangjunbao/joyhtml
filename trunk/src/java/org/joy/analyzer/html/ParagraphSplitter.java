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
    private int offset = 0;

    /**
     * 构造一个段落分解器
     * @param body 需要分解的文章正文部分的半HTML文本
     * @param whole 需要分解的文章的全文半HTML文本
     */
    public ParagraphSplitter(String body, String whole) {
        this.body = body;
        this.whole = whole;
    }

    /**
     * split all the paragraphs from given text and add them to the paragraph list
     * @return
     */
    public List<Paragraph> split() {
        //reset the offset
        offset = 0;
        try {
            ArrayList<Paragraph> paragraphList = new ArrayList<Paragraph>();
            //seperate the main body with other parts of the whold text
            int start = whole.indexOf(body);
            int end = start + body.length();

            split(whole.substring(0, start), paragraphList, .1);
            //now, add the main body
            split(body, paragraphList, .3);

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

        double lastWeight = 0;
        while (line != null) {
            double weight = base;
            if (line.startsWith("<H")) {
                //在这里给文档的主题相关度加上显示上的得分因素，比如大标题等等
                weight += .3;
            }
            if (line.startsWith("<TITLE")) {
                weight += .7;
            }
            String t = line.replaceAll("(</*H[1-9]>)|(</*TITLE>)", "");
            if (!t.trim().equals("")) {
                if (weight == lastWeight) {
                    //如果和上一个段落同样的权重，合并两段落
                    String p = paraList.get(paraList.size() - 1).getText() + "\r\n" + t;
                    paraList.get(paraList.size() - 1).setText(p);
                    //计算下一个paragraph的偏移
                    offset += t.length()+2;
                } else {
                    //如果不一样，开始一个新的段落
                    paraList.add(new Paragraph(t, weight, offset));
                    lastWeight = weight;
                    //计算下一个paragraph的偏移
                    offset += t.length();
                }
            }
            line = r.readLine();
        }
    }
}
