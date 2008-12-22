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
        private double weight = -3;
        private String text;

        public Mark(Node node, String text, int numText, int numAnchorText) {
            this.node = node;
            this.numAnchorText = numAnchorText;
            this.numText = numText;
            this.text = text;
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
            if (weight == -3) {
                if (node != null &&
                        node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;
                    weight += Utility.isImportantNode(e) ? 1 : 0;
                    weight += Utility.isLargeNode(e) ? 0.5 : 0;
                    // priority += Utility.isHeading(e) ? 1 : 0;
                    weight += 0.5 * fn(Utility.numInfoNode(e) / (float) (numTotalnfoNodes));
                }
                if(text.toLowerCase().contains("copyright") ||
                        text.toLowerCase().contains("all rights reserved")||
                        text.toLowerCase().contains("版权")||
                        text.toLowerCase().contains("©")||
                        text.toLowerCase().contains("上一页")||
                        text.toLowerCase().contains("下一页")){
                        weight -= .5;
                }
                if (TextExtractor.this.totalTextLen != 0 && TextExtractor.this.totalAnchorTextLen != 0) {
                    weight += fn(4.0 * numText / TextExtractor.this.totalTextLen - 2.0 * numAnchorText / TextExtractor.this.totalAnchorTextLen);
                } else if (TextExtractor.this.totalTextLen != 0) {
                    weight += fn(4.0 * numText / TextExtractor.this.totalTextLen);
                } else if (TextExtractor.this.totalAnchorTextLen != 0) {
                    weight += fn(-2.0 * numAnchorText / TextExtractor.this.totalAnchorTextLen);
                } else {
                    weight = 0;
                }
            }

            return weight;
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
        String whole = getInnerText(body, true);
        totalTextLen = getInnerText(body, false).length();

        numTotalnfoNodes = Utility.numInfoNode((Element) body);
        evaluateNodes(body);
        //sort the mark list
        Collections.sort(markList);
        Mark mark = markList.get(markList.size() - 1);
        String bodyText = getInnerText(mark.node, true);

        //extract all the paragraphs, add them to the paragraph list
        paragraphList = new ParagraphExtractor(bodyText, whole).extract();
        return bodyText;
    }

    /**
     *
     * @param node
     * @return the text in the node and its offspring
     */
    private String getInnerText(Node node, boolean viewMode) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            return Utility.filter(((Text) node).getData());
        }

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            if (Utility.isInvalidNode(element)) {
                return "";
            }
            StringBuilder nodeText = new StringBuilder();
            //replace the line break with space,
            //beacause inappropriate line break may cause the paragraph corrupt.
            if (viewMode && element.getTagName().equals("BR")) {
                nodeText.append(" ");
            }
            //let the appearance tags stay
            if (viewMode && Utility.isHeading(element)) {
                nodeText.append("<" + element.getTagName() + ">");
            }
            NodeList list = element.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                String t = getInnerText(list.item(i), viewMode);
                //whether we need to add extra space?
                if (viewMode && Utility.isSpace(element)) {
                    t += " ";
                }
                nodeText.append(t);
            }
            if (viewMode && Utility.isHeading(element)) {
                nodeText.append("</" + element.getTagName() + ">");
            }
            //break the line, if the element is a REAL BIG tag, such as DIV,TABLE
            if (viewMode &&
                    Utility.isParagraph(element) &&
                    nodeText.toString().trim().length() != 0) {
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
    private void evaluateNodes(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            return;
        }

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            if (Utility.isLinkNode(element)) {
                totalAnchorTextLen += getInnerText(element, false).length();
            } else if (Utility.isInvalidNode(element)) {
                return;
            } else {
                // get anchor text length
                int anchorTextLen = 0;
                NodeList anchors = element.getElementsByTagName("A");
                for (int i = 0; i < anchors.getLength(); i++) {
                    anchorTextLen += getInnerText(anchors.item(i), false).length();
                }
                String text = getInnerText(node, false);
                int textLen = text.length();
                markList.add(new Mark(node, text,
                        textLen,
                        anchorTextLen));
                NodeList list = element.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    evaluateNodes(list.item(i));
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

            System.out.print(f.getName() + "....");

            TextExtractor extractor = new TextExtractor(doc);
            String str = extractor.extract();
            StringBuffer sb = new StringBuffer();
            for (Paragraph p : extractor.getParagraphList()) {
                sb.append(p.getText() + "\r\n" + p.getWeight() + "\r\n");
            //System.out.print(p.getWeight()+"...\n");
            }
            System.out.println(extractor.totalAnchorTextLen + ":  " + extractor.totalTextLen + "\t" +
                    extractor.totalAnchorTextLen / (float) extractor.totalTextLen);

            //System.out.println(str);
            FileWriter fw = new FileWriter("d:/res3/" + f.getName() + ".txt");
            fw.write(sb.toString());
            fw.close();
        }
    }
}
