/**
 * the package id used to optimize the dom tree
 */
package org.joy.analyzer.html;

import java.util.*;
import java.io.*;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import org.cyberneko.html.parsers.DOMParser;

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

        public Mark(int numText, int numAnchorText, Node node) {
            super();
            this.numText = numText;
            this.numAnchorText = numAnchorText;
            this.node = node;
            TextExtractor.this.numTotalText += numText;
            TextExtractor.this.numTotalAnchorText += numAnchorText;
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

        /**
         * account the priority
         */
        public double getPriority() {
            /**
             * this is the strategy to account the priority
             *
             */
            if (priority == -3) {
                if (TextExtractor.this.numTotalText != 0 && TextExtractor.this.numTotalAnchorText != 0) {
                    priority = 1.0 * numText / TextExtractor.this.numTotalText - 1.0 * numAnchorText / TextExtractor.this.numTotalAnchorText;
                } else if (TextExtractor.this.numTotalText != 0) {
                    priority = 1.0 * numText / TextExtractor.this.numTotalText;
                } else if (TextExtractor.this.numTotalAnchorText != 0) {
                    priority = -1.0 * numAnchorText / TextExtractor.this.numTotalAnchorText;
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
    /**
     * visit every node and count the factors that affect the priority
     * of this node
     */
    private List<Mark> markList = new ArrayList<Mark>();
    private Document doc;
    private static final String[] INVALID_TAGS = {"STYLE", "COMMENT", "SCRIPT", "OPTION", "LI"};
    private static final String[] IMPORTANT_TAGS = {"TITLE", "H1", "H2", "H3"};

    public TextExtractor(Document doc) {
        super();
        this.doc = doc;
    }

    /**
     *
     * @param tag
     * @return
     * Remove some "invalid node", such as "STYLE", "COMMENT", "SCRIPT", "OPTION", "LI"
     */
    private boolean isInvalid(String tag) {
        for (int i = 0; i < INVALID_TAGS.length; i++) {
            if (tag.matches("(?i)" + INVALID_TAGS[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * add some important tags, such as "TITLE","H1","H2","H3"
     */
    private String getImportantText() {
        String text = "";
        for (int i = 0; i < IMPORTANT_TAGS.length; i++) {
            NodeList list = doc.getElementsByTagName(IMPORTANT_TAGS[i]);
            for (int j = 0; j < list.getLength(); j++) {
                text += getText(list.item(j)) + "\n";
            }
        }

        return text + "\n";
    }

    /**
     *
     * @return string Content text
     */
    public String getContentText() {
        Node body = doc.getElementsByTagName("BODY").item(0);
        countNode(body);

        Mark marker = markList.get(0);
        for (int i = 1; i < markList.size(); i++) {
            if (marker.compareTo(markList.get(i)) <= 0) {
                marker = markList.get(i);
            }
        }

        return getImportantText() + getText(marker.node);
    }

    /**
     *
     * @param node
     * @return the text in the node and its offspring
     */
    private String getText(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            return ((Text) node).getData().trim();
        }


        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            if (isInvalid(element.getTagName().trim())) {
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
            Text text = (Text) node;
            return new Mark(text.getData().trim().length(), 0, null);
        }

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String tag = element.getTagName().trim();
            if (tag.matches("(?i)a")) {
                // When it is a anchor tag, add to the anchor list
                NodeList anchorList = element.getChildNodes();
                for (int i = 0; i < anchorList.getLength(); i++) {
                    Node anchorNode = anchorList.item(i);
                    if (anchorNode.getNodeType() != Node.TEXT_NODE) {
                        continue;
                    }

                    Text text = (Text) anchorNode;
                    Mark mark = new Mark(0, text.getData().trim().length(), null);
                    return mark;
                }
            } else if (isInvalid(tag)) {
                return new Mark(0, 0, null);
            } else {
                NodeList list = element.getChildNodes();
                Mark mark = new Mark(0, 0, node);
                for (int i = 0; i < list.getLength(); i++) {
                    Mark t = countNode(list.item(i));
                    mark.increaseMark(t);
                }
                markList.add(mark);
                return mark;
            }
        }

        return new Mark(0, 0, null);
    }

    /**
     *
     * @param args
     * @throws Exception
     * ��������
     * ����ʹ�õĻ�ṹ
     */
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("c:/CharacterData (Java 2 Platform SE 6).htm"));
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(reader));
        Document doc = parser.getDocument();

        TextExtractor optimiser = new TextExtractor(doc);
        String text = optimiser.getContentText();
        System.out.println(text);

    }
}
