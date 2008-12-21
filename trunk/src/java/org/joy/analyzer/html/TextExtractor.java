/**
 * the package id used to optimize the dom tree
 */
package org.joy.analyzer.html;

import java.util.*;
import java.io.*;
import java.util.ArrayList;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import org.cyberneko.html.parsers.DOMParser;
import org.joy.analyzer.Paragraph;

/**
 * @version 1.0
 * @author JINJUN
 * @modified by Liu Song
 * <pre>the class provide the method to get the most important part
 * of the dom tree,so you can keep point at the most useful text
 * with least time
 *
 *      Usage:
 *      BufferedReader reader = new BufferedReader(new FileReader(...));
 *		DOMParser parser = new DOMParser();
 *		parser.parse(new InputSource(reader));
 *		Document doc = parser.getDocument();

 *		HTMLDocumentOptimizer optimiser = new HTMLDocumentOptimizer(doc);	
 *  	String text = optimiser.getImportantText();
 *  	System.out.println(text);
 *  </pre>
 */
public class TextExtractor {

    /**
     * @return the paragraphList
     */
    public List<Paragraph> getParagraphList() {
        return paragraphList;
    }

    /**
     * a class store the node information
     * and count the priority
     */
    private class Mark implements Comparable<Mark> {

        /**
         * store the number of text not in anchor in this node and its offspring
         */
        private int numText = 0;
        /**
         * store the number of text not in anchor in this node and its offspring
         */
        private int numAnchorText = 0;
        /**
         * store the associated node
         *  "Associated Node" ? Not clearly specified. -- Liu Song
         */
        public Node node = null;
        /**
         * store the priority,when it equals -3,mean unavailable
         */
        private double priority = -3;

        public Mark(Node node, int numText, int numAnchorText) {
            this.node = node;
            this.numAnchorText = numAnchorText;
            this.numText = numText;
        }
        //平滑函数

        private double fn(double x) {
            if (x > 0.8f) {
                return 0.8f;
            }
            return x;
        }

        /**
         * account the priority
         */
        public double weight() {
            /**
             * this is the strategy to account the priority
             *
             */
            if (priority == -3) {
                if (node != null &&
                        node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;
                    priority += Utility.isImportantNode(e) ? 1 : 0;
                    priority += Utility.isLargeNode(e) ? 0.5 : 0;
                    // priority += Utility.isHeading(e) ? 1 : 0;
                    priority += 0.5*fn(Utility.numInfoNode(e) / (float) (numTotalnfoNodes));
                }
                if (TextExtractor.this.totalTextLen != 0 && TextExtractor.this.totalAnchorTextLen != 0) {
                    priority += fn(1.0 * numText / TextExtractor.this.totalTextLen - 1.0 * numAnchorText / TextExtractor.this.totalAnchorTextLen);
                } else if (TextExtractor.this.totalTextLen != 0) {
                    priority += fn(1.0 * numText / TextExtractor.this.totalTextLen);
                } else if (TextExtractor.this.totalAnchorTextLen != 0) {
                    priority += fn(-1.0 * numAnchorText / TextExtractor.this.totalAnchorTextLen);
                } else {
                    priority = 0;
                }
            }

            return priority;
        }

        @Override
        public int compareTo(Mark mark) {
            if (this.weight() > mark.weight()) {
                return 1;
            }
            return -1;
        }
    }
    /**
     * store the number of text not in anchor
     */
    private int totalTextLen = 0;
    /**
     * store the number of text not in anchor
     */
    private int totalAnchorTextLen = 0;
    private int numTotalnfoNodes = 0;
    /**
     * visit every node and count the factors that affect the priority
     * of this node
     */
    private List<Mark> markList = new ArrayList<Mark>();
    private List<Paragraph> paragraphList = new ArrayList<Paragraph>();
    private Document doc;

    public TextExtractor(Document doc) {
        super();
        this.doc = doc;

    }

    /**
     * 
     * @return string whole text on the web
     */
    public String extract() {
        Node body = doc.getElementsByTagName("BODY").item(0);
        String whole = getInnerText(body);
        totalTextLen = whole.length();
        numTotalnfoNodes = Utility.numInfoNode((Element) body);

        countNode(body);
        //sort the mark list
        Collections.sort(markList);
        Mark mark = markList.get(markList.size() - 1);
        String bodyText = getInnerText(mark.node);

        //extract all the paragraphs, add them to the paragraph list
        paragraphList = new ParagraphExtractor(bodyText, whole).extract();
        return bodyText;
    }

    /**
     *
     * @param node
     * @return the text in the node and its offspring
     */
    private String getInnerText(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            return Utility.filter(((Text) node).getData());
        }

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            if (Utility.isInvalidNode(element)) {
                return "";
            }

            StringBuilder nodeText = new StringBuilder();

            //get the highlight
            if (Utility.isHeading(element)) {
                nodeText.append("<" + element.getTagName() + ">");
            }
            NodeList list = element.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                String t = getInnerText(list.item(i));
                //whether we need to add extra space?
                if (Utility.isSpace(element)) {
                    t += " ";
                }
                nodeText.append(t);
            }
            if (Utility.isHeading(element)) {
                nodeText.append("</" + element.getTagName() + ">");
            }
            //break the line
            if (Utility.isParagraph(element) && nodeText.toString().trim().length() != 0) {
                nodeText.append("\r\n");
            }
            return nodeText.toString().replaceAll("[\r\n]+", "\r\n");
        }

        return "";
    }

    /**
     *
     * @param node
     * @return Mark information about node
     * this method is used to collect the information and calculate priority
     */
    private void countNode(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            return;
        }

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            if (Utility.isLinkNode(element)) {
                totalAnchorTextLen += getInnerText(element).length();
            } else if (Utility.isInvalidNode(element)) {
                return;
            } else {
                int anchorTextLen = 0;
                NodeList anchors = element.getElementsByTagName("A");
                for (int i = 0; i < anchors.getLength(); i++) {
                    anchorTextLen += getInnerText(anchors.item(i)).length();
                }
                int textLen = getInnerText(node).length();
                markList.add(new Mark(node,
                        textLen,
                        anchorTextLen));
                NodeList list = element.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    countNode(list.item(i));
                }
            }
        }

        return;
    }

    /**
     *
     * @param args
     * @throws Exception
     * ��������
     * ����ʹ�õĻ�ṹ
     */
    public static void main(String[] args) throws Exception {
        File folder = new File("d:/res2/");
        for (File f : folder.listFiles()) {
            DOMParser parser = new DOMParser();
            BufferedReader reader = new BufferedReader(new FileReader("d://res2/" + f.getName()));
            parser.parse(new InputSource(reader));
            Document doc = parser.getDocument();

            TextExtractor extractor = new TextExtractor(doc);
            String str = extractor.extract();
            StringBuffer sb = new StringBuffer();
            for (Paragraph p : extractor.getParagraphList()) {
                sb.append(p.getText() + "\r\n" + p.getWeight() + "\r\n");
            }
            System.out.println(str);
            FileWriter fw = new FileWriter("d:/res3/" + f.getName() + ".txt");
            fw.write(sb.toString());
            fw.close();
        }
    }
}
