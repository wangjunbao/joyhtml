/**
 * the package id used to optimize the dom tree
 */
package org.joy.analyzer.html;

import java.util.*;
import java.io.*;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import org.cyberneko.html.parsers.DOMParser;
import org.joy.analyzer.Utility;

/**
 * @version 1.0
 * @author JINJUN
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

        public Mark(Node node) {
            super();
            if (node != null) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if (Utility.isLinkNode((Element) node)) {
                        this.numAnchorText = Utility.filter(node.getTextContent()).length();
                    } 
                } else if (node.getNodeType() == Node.TEXT_NODE) {
                    this.numText = Utility.filter(node.getTextContent()).length();
                }

                TextExtractor.this.numTotalText += numText;
                TextExtractor.this.numTotalAnchorText += numAnchorText;
            }
            this.node = node;
        }

        private void increaseTextCount(int numText) {
            this.numText += numText;
            TextExtractor.this.numTotalText += numText;
        }

        private void increaseAnchorTextCount(int numAnchorText) {
            this.numAnchorText += numAnchorText;
            TextExtractor.this.numTotalAnchorText += numAnchorText;
        }

        public void increaseMark(Mark mark) {
            increaseTextCount(mark.numText);
            increaseAnchorTextCount(mark.numAnchorText);
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
        public double getPriority() {
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
                    priority += fn(Utility.numInfoNode(e) / (float) (numTotalnfoNodes));
                }
                if (TextExtractor.this.numTotalText != 0 && TextExtractor.this.numTotalAnchorText != 0) {
                    priority += fn(1.0 * numText / TextExtractor.this.numTotalText - 1.0 * numAnchorText / TextExtractor.this.numTotalAnchorText);
                } else if (TextExtractor.this.numTotalText != 0) {
                    priority += fn(1.0 * numText / TextExtractor.this.numTotalText);
                } else if (TextExtractor.this.numTotalAnchorText != 0) {
                    priority += fn(-1.0 * numAnchorText / TextExtractor.this.numTotalAnchorText);
                } else {
                    priority = 0;
                }
            }

            return priority;
        }

        @Override
        public int compareTo(Mark mark) {
            if (this.getPriority() > mark.getPriority()) {
                return 1;
            }
            return -1;
        }
    }
    /**
     * store the number of text not in anchor
     */
    private int numTotalText = 0;
    /**
     * store the number of text not in anchor
     */
    private int numTotalAnchorText = 0;
    private int numTotalnfoNodes = 0;
    /**
     * visit every node and count the factors that affect the priority
     * of this node
     */
    private List<Mark> markList = new ArrayList<Mark>();
    private Document doc;

    public TextExtractor(Document doc) {
        super();
        this.doc = doc;

    }

    public String getHeadings() {
        //get headings
        StringBuilder sb = new StringBuilder();
        for (String tagName : Utility.HEADING_TAGS) {
            NodeList t = doc.getElementsByTagName(tagName);
            for (int i = 0; i < t.getLength(); i++) {
                String s = Utility.filter(t.item(i).getTextContent());
                sb.append(s + "\n");
            }
        }
        return sb.toString();
    }

    /**
     *
     * @return string Content text
     */
    public String getContentText() {
        Node body = doc.getElementsByTagName("BODY").item(0);
        numTotalnfoNodes = Utility.numInfoNode((Element) body);
        countNode(body);

        //sort the mark list
        Collections.sort(markList);
        for (Mark m : markList) {
            System.out.println(m.priority + " " + m.node);
        }
        Mark mark = markList.get(markList.size() - 1);

        return getHeadings() + getText(mark.node);
    }

    /**
     *
     * @param node
     * @return the text in the node and its offspring
     */
    private String getText(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            return Utility.filter(((Text) node).getData());
        }


        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            if (Utility.isInvalidNode(element)) {
                return "";
            }
            String nodeText = "";
            NodeList list = element.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                nodeText += getText(list.item(i));
            }
            return nodeText;
        }

        return "";
    }

    /**
     *
     * @param node
     * @return Mark information about node
     * this method is used to collect the information and calculate priority
     */
    private Mark countNode(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            return new Mark(node);
        }

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            if (Utility.isLinkNode(element)) {
                // When it is a anchor tag, add to the mark list
                Mark mark = new Mark(node);
                return mark;
            } else if (Utility.isInvalidNode(element)) {
                return new Mark(null);
            } else {
                NodeList list = element.getChildNodes();
                Mark mark = new Mark(node);
                for (int i = 0; i < list.getLength(); i++) {
                    Mark t = countNode(list.item(i));
                    mark.increaseMark(t);
                }
                markList.add(mark);
                return mark;
            }
        }

        return new Mark(null);
    }

    /**
     *
     * @param args
     * @throws Exception
     * ��������
     * ����ʹ�õĻ�ṹ
     */
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("d://res2/20081217142301442.html"));
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(reader));
        Document doc = parser.getDocument();

        TextExtractor optimiser = new TextExtractor(doc);
        String text = optimiser.getContentText();
        System.out.println(text);

        FileWriter fw = new FileWriter("c:/a.txt");
        fw.write(text);
        fw.close();

    }
}
