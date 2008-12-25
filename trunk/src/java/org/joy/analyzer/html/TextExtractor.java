/**
 * the package id used to optimize the dom tree
 */
package org.joy.analyzer.html;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xml.sax.InputSource;
import org.cyberneko.html.parsers.DOMParser;
import org.joy.analyzer.Paragraph;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

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

        private int numInfoNode = 0;
        /**
         * store the number of text not in anchor in this node and its offspring
         */
        private int textLen = 0;
        /**
         * store the number of text not in anchor in this node and its offspring
         */
        private int anchorTextLen = 0;
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
            this.anchorTextLen = numAnchorText;
            this.textLen = numText;
            this.text = text;

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                this.numInfoNode = Utility.numInfoNode((Element) node);
            }
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
                    weight += Utility.isTableNodes(e) ? .1 : 0;
                    weight += Utility.isLargeNode(e) ? .1 : 0;
                    weight += 0.2 * fn(numInfoNode / (double) (numTotalnfoNodes));
                }
                if(Utility.containsNoise(text)){
                    weight -=0.5;
                }
                weight += 1.0- (double)anchorTextLen/ textLen;
                if (TextExtractor.this.totalTextLen != 0 && TextExtractor.this.totalAnchorTextLen != 0) {
                    weight += 1.6 * fn((double) textLen / TextExtractor.this.totalTextLen) -
                            .8 * anchorTextLen / TextExtractor.this.totalAnchorTextLen;
                } else if (TextExtractor.this.totalTextLen != 0) {
                    weight += 1.6 * fn((double) textLen / TextExtractor.this.totalTextLen);
                } else if (TextExtractor.this.totalAnchorTextLen != 0) {
                    weight += -.8 * anchorTextLen / TextExtractor.this.totalAnchorTextLen;
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

        @Override
        public String toString() {
            return "local weight" + fn(3.0 * textLen / TextExtractor.this.totalTextLen -
                    2.0 * anchorTextLen / TextExtractor.this.totalAnchorTextLen);
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

    private int anchorTextLen(Element e) {
        int anchorLen = 0;
        // get anchor text length
        NodeList anchors = e.getElementsByTagName("A");
        for (int i = 0; i < anchors.getLength(); i++) {
            anchorLen += getInnerText(anchors.item(i), false).length();
        }
        return anchorLen;
    }

    private void cleanup(Element e) {
        NodeList c = e.getChildNodes();
        for (int i = 0; i < c.getLength(); i++) {
            if (c.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element t = (Element) c.item(i);
                if (Utility.isInvalidNode(t)) {
                    e.removeChild(c.item(i));
                } else {
                    cleanup(t);
                }
            }
        }
    }

    /**
     * 
     * @return string whole text on the web
     */
    public String extract() {
        Node body = doc.getElementsByTagName("BODY").item(0);
        //cleanup, remove the invalid tags
        cleanup((Element) body);
        String whole = getInnerText(body, true);

        totalTextLen = getInnerText(body, false).length();
        // get anchor text length
        totalAnchorTextLen = anchorTextLen((Element) body);

        numTotalnfoNodes = Utility.numInfoNode((Element) body);
        evaluateNodes(body);

        String bodyText;
        if (markList.size() == 0) {
            bodyText = "";
        } else {
            //sort the mark list
            Collections.sort(markList);
            Mark mark = markList.get(markList.size() - 1);
            bodyText = getInnerText(mark.node, true);
        }

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
                if (viewMode && Utility.needSpace(element)) {
                    t += " ";
                }
                nodeText.append(t);
            }
            if (viewMode && Utility.isHeading(element)) {
                nodeText.append("</" + element.getTagName() + ">");
            }
            //break the line, if the element is a REAL BIG tag, such as DIV,TABLE
            if (viewMode &&
                    Utility.needWarp(element) &&
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
                //totalAnchorTextLen += getInnerText(element, false).length();
            } else if (Utility.isInvalidNode(element)) {
                return;
            } else {
                // get anchor text length
                int anchorTextLen = anchorTextLen(element);

                String text = getInnerText(node, false);
                int textLen = text.length();
                if (textLen != 0) {
                    markList.add(new Mark(node, text,
                            textLen,
                            anchorTextLen));
                }
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
