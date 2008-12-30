/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.html;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 *
 * @author Lamfeeling
 */
public class Tag {

    public static final double MAX_ANCHOR_DEN = 0.5;
    private Node node = null;
    private String text = "";
    private String anchorText = "";
    private int numInfoNodes = 0;
    private double weight = 0;

    public Tag(Node node) {
        this.node = node;
        text = getInnerText(node, false);
        anchorText = getAnchorText();
        System.out.println(text);
        System.out.println("");
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            numInfoNodes = getNumInfoNode((Element) node);
        }
    }
    //平滑函数

    private static double fn(double x) {
        if (x > 0.8f) {
            return 0.8f;
        }
        return x;
    }

    public boolean isExtra() {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (Utility.isLargeNode((Element) node)) {
                if (anchorDensity() > 0.5) {
                    return true;
                }
            }
        }
        return false;
    }

    public double weight(int totalT, int totalA, int totalNumInfoNodes) {
        double weight = 0;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element e = (Element) node;
            weight += Utility.isTableNodes(e) ? .1 : 0;
            weight += Utility.isLargeNode(e) ? .1 : 0;
            weight += 0.2 * fn(numInfoNodes / (double) (totalNumInfoNodes));
            weight -= Utility.containsInput(e) ? .5 : 0;
        }

        if (Utility.containsNoise(text)) {
            weight -= 0.5;
        }

        weight += 1.0 - anchorDensity();
        weight += share(totalA, totalT);
        this.weight = weight;
        return weight;
    }

    /**
     * 定义：锚文本和整体文本的比值
     * @note 特别的：如果text或者anchorText为空，那么这个anchorDensity为0
     * @return
     */
    private double anchorDensity() {
        int anchorLen = anchorText.length();
        int textLen = text.length();
        if (anchorLen == 0 || textLen == 0) {
            return 0;
        }
        return anchorLen / (double) textLen;
    }

    private double share(int totalA, int totalT) {
        if (totalA == 0) {
            return 1.6 * fn((double) text.length() / totalT);
        }
        return 1.6 * fn((double) text.length() / totalT) - .8 * anchorText.length() / totalA;
    }

    /**
     * 
     * @param viewMode
     * @return
     */
    public String getInnerText(boolean viewMode) {
        if (viewMode) {
            return getInnerText(node, viewMode);
        } else {
            return text;
        }
    }

    public int getNumInfoNodes() {
        return numInfoNodes;
    }

    private String getAnchorText() {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            return getAnchorText((Element) node);
        }
        return "";
    }

    public Node getNode() {
        return node;
    }

    public double getWeight() {
        return weight;
    }

    /**
     *
     * @param node
     * @return the text in the node and its offspring
     */
    public static String getInnerText(Node node, boolean viewMode) {
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

    public static String getAnchorText(Element e) {
        StringBuilder anchorLen = new StringBuilder();
        // get anchor text length
        NodeList anchors = e.getElementsByTagName("A");
        for (int i = 0; i < anchors.getLength(); i++) {
            anchorLen.append(getInnerText(anchors.item(i), false));
        }
        return anchorLen.toString();
    }

    /**
     * 当前这个节点下包含多少个InfoNode?
     * @param e
     * @return 当前这个节点下包含多少个InfoNode?
     */
    public static int getNumInfoNode(Element e) {
        int num = Utility.isInfoNode(e) ? 1 : 0;
        NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                num += getNumInfoNode((Element) children.item(i));
            }
        }
        return num;
    }
}
