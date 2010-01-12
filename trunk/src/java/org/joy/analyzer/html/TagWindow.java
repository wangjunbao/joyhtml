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
 * An abstract for a HTML tag. It may contains some informations that used for
 * TextExtractor to decide whether it is a main body tag
 * 
 * @author Song Liu(lamfeeling2@gmail.com)
 */
public class TagWindow {

    private Node node = null;
    private String text = "";
    private String anchorText = "";
    private int numInfoNodes = 0;

    /**
     * construct a TagWindow using DOM Object Node.
     * 
     * @param node
     *            w3cNode object
     */
    public TagWindow(Node node) {
	this.node = node;
	text = getInnerText(node, false);
	anchorText = getAnchorText();

	if (node.getNodeType() == Node.ELEMENT_NODE) {
	    numInfoNodes = getNumInfoNode((Element) node);
	}
    }

    /**
     * Smooth function. We use a step function for efficiency
     * 
     * @param x
     * 
     * @return
     */
    private static double fn(double x) {
	if (x > 0.8f) {
	    return 0.8f;
	}
	return x;
    }

    /**
     * Compute the weight that this Tag Window donates to the main idea of the
     * current HTML documents.
     * 
     * @param totalT
     *            Total number of chars in current HTML document
     * @param totalA
     *            total number of chars in all anchors of current HTML document
     * @param totalNumInfoNodes
     *            total number of info nodes in current HTML document
     * @return weight that this Tag Window donates to the main idea of curret
     *         HTML document
     */
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
	return weight;
    }

    /**
     * Definition for anchorDensity is, the ratio between # chars of anchor text
     * and # chars of total text.
     * 
     * @note if there's no anchor text or total text, returns 0.
     * @return anchor density
     */
    private double anchorDensity() {
	int anchorLen = anchorText.length();
	int textLen = text.length();
	if (anchorLen == 0 || textLen == 0) {
	    return 0;
	}
	return anchorLen / (double) textLen;
    }

    /**
     * compute the share of current Tag alpha * text ratio - beta * anchor ratio
     * 
     * @param totalA
     *            total number of chars in anchor text
     * @param totalT
     *            total number of chars in text
     * @return the share of this tag
     */
    private double share(int totalA, int totalT) {
	if (totalA == 0) {
	    return 1.6 * fn((double) text.length() / totalT);
	}
	return 1.6 * fn((double) text.length() / totalT) - .8
		* anchorText.length() / totalA;
    }

    /**
     * get inner text of this tag
     * 
     * @param visualMode
     *            whether adjust for visual tags? (add extra space or line
     *            breaker)
     * @return inner text
     */
    public String getInnerText(boolean visualMode) {
	if (visualMode) {
	    return getInnerText(node, visualMode);
	} else {
	    return text;
	}
    }

    /**
     * number of info nodes in this Tag
     * 
     * @return number of info nodes inthis tag
     */
    public int getNumInfoNodes() {
	return numInfoNodes;
    }

    /**
     * Anchor text in this element
     * 
     * @return anchor text
     */
    private String getAnchorText() {
	if (node.getNodeType() == Node.ELEMENT_NODE) {
	    return getAnchorText((Element) node);
	}
	return "";
    }

    /**
     * get w3c DOM object for this tag
     * 
     * @return
     */
    public Node getNode() {
	return node;
    }

    /**
     * get inner text for given Node
     * 
     * @param node
     * 
     * @param visualMode
     *            add extra space and line breaker
     * @return get inner text for given node
     */
    public static String getInnerText(Node node, boolean visualMode) {
	return getInnerText(node, visualMode, 0);
    }

    private static String getInnerText(Node node, boolean viewMode, int level) {
	if (node.getNodeType() == Node.TEXT_NODE) {
	    return Utility.filter(((Text) node).getData());
	}

	if (node.getNodeType() == Node.ELEMENT_NODE) {
	    Element element = (Element) node;
	    if (Utility.isInvalidElement(element)) {
		return "";
	    }
	    StringBuilder nodeText = new StringBuilder();
	    // replace the line break with space,
	    // beacause inappropriate line break may cause the paragraph
	    // corrupt.
	    if (viewMode && element.getTagName().equals("BR")) {
		nodeText.append(" ");
	    }
	    // let the appearance tags stay
	    if (viewMode && Utility.isHeading(element)) {
		nodeText.append("<" + element.getTagName() + ">");
	    }
	    NodeList list = element.getChildNodes();
	    for (int i = 0; i < list.getLength(); i++) {
		String t = getInnerText(list.item(i), viewMode, level + 1);
		// whether we need to add extra space?
		if (viewMode && Utility.needSpace(element)) {
		    t += " ";
		}
		nodeText.append(t);
	    }
	    if (viewMode && Utility.isHeading(element)) {
		nodeText.append("</" + element.getTagName() + ">");
	    }
	    // break the line, if the element is a REAL BIG tag, such as
	    // DIV,TABLE
	    if (viewMode && Utility.needWarp(element)
		    && nodeText.toString().trim().length() != 0) {
		nodeText.append("\r\n");
	    }
	    return nodeText.toString().replaceAll("[\r\n]+", "\r\n");
	}

	return "";
    }

    /**
     * get anchor text for given element
     * 
     * @param e
     *            w3c DOM element
     * @return anchor text in given element
     */
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
     * get number of info node in element
     * 
     * @param e
     *            w3c element
     * @return number of info node in element
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
