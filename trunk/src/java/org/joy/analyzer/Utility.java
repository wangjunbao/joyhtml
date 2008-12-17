/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Lamfeeling
 */
public class Utility {

    private static final String[] LARGE_NODES = {"DIV", "TABLE"};
    private static final String[] IMPORTANT_NODES = {"TR", "TD"};
    private static final String[] INFO_NODE = {"P", "SPAN", "BR"};
    public static final String[] HEADING_TAGS = {"TITLE","H1", "H2", "H3"};
    private static final String[] INVALID_TAGS = {"STYLE", "COMMENT", "SCRIPT", "OPTION", "LI"};
    private static final String LINK_NODE = "A";

    public static String filter(String text) {
        text = text.replaceAll("[^\u4e00-\u9fa5|a-z|A-Z|0-9|０-９,.，。:；?!|\\s|\\@]", " ");
        text = text.replaceAll("[【】]", " ");
        text = text.replaceAll("\n+", "\n");
        text = text.replaceAll("\\|", "");
        text = text.replaceAll("\\s+", " ");
        text = text.trim();
        return text;
    }

    public static boolean isImportantNode(Element e) {
        for (String s : IMPORTANT_NODES) {
            if (e.getTagName().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLargeNode(Element e) {
        for (String s : LARGE_NODES) {
            if (e.getTagName().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInfoNode(Element e) {
        for (String s : INFO_NODE) {
            if (e.getTagName().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isHeading(Element e) {
        for (String s : HEADING_TAGS) {
            if (e.getTagName().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInvalidNode(Element e) {
        for (String s : INVALID_TAGS) {
            if (e.getTagName().equals(s)) {
                return true;
            }
        }
        return false;
    }
        public static  int numInfoNode(Element e) {
        int num = isInfoNode(e) ? 1 : 0;
        NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                num += numInfoNode((Element) children.item(i));
            }
        }
        return num;
    }
        
    public static  boolean isLinkNode(Element e) {
        if (e.getTagName().equals(LINK_NODE)) {
            return true;
        }
        return false;
    }
    public static  int numLinkNode(Element e) {
        int num = isLinkNode(e) ? 1 : 0;
        NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                num += numLinkNode((Element) children.item(i));
            }
        }
        return num;
    }
}
