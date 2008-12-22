/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.html;

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
    public static final String[] HEADING_TAGS = {"TITLE", "H1", "H2", "H3", "H4", "H5", "H6", "H7"};
    private static final String[] INVALID_TAGS = {"STYLE", "COMMENT", "SCRIPT", "OPTION", "LI"};
    private static final String[] SPACING_TAGS = {"B","SPAN"};
    private static final String LINK_NODE = "A";

    public static String filter(String text) {
        text = text.replaceAll("[^\u4e00-\u9fa5|a-z|A-Z|0-9|０-９,.，。:；：><?》《!\\-©|\\s|\\@]", " ");
        text = text.replaceAll("[【】]", " ");
        text = text.replaceAll("[\r\n]+", "\r\n");
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

    public static int numInfoNode(Element e) {
        int num = isInfoNode(e) ? 1 : 0;
        NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                num += numInfoNode((Element) children.item(i));
            }
        }
        return num;
    }

    public static boolean isLinkNode(Element e) {
        if (e.getTagName().equals(LINK_NODE)) {
            return true;
        }
        return false;
    }

    public static int numLinkNode(Element e) {
        int num = isLinkNode(e) ? 1 : 0;
        NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                num += numLinkNode((Element) children.item(i));
            }
        }
        return num;
    }

    /**
     * Judge whether we need to warp the current Element after appended it to String Buffer.
     * @param e
     * @return
     */
    public static boolean isParagraph(Element e) {
        if (isHeading(e) || e.getTagName().equals("P") || isImportantNode(e) || isLargeNode(e)) {
            return true;
        }
        return false;
    }

    /**
     * Judge whehter we should add one space when facing the specific element
     * @param e
     * @return
     */
    public static boolean isSpace(Element e) {
        for (String s : SPACING_TAGS) {
            if (e.getTagName().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAppearanceTag(Element e) {
        //headings
        if (e.getTagName().matches("H[1-9]")) {
            return true;
        }
        //colored fonts
        if (e.getTagName().equals("FONT") &&
                !e.getAttribute("COLOR").equals("")) {
            return true;
        }
        //stronged texts
        if (e.getTagName().matches("[B|I|STRONG]")) {
            return true;
        }
        return false;
    }
}
