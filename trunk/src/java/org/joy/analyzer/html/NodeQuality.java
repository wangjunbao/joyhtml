package org.joy.analyzer.html;

import org.w3c.dom.Element;

public class NodeQuality {
	private static final String[] LARGE_NODES = {"DIV", "TABLE"};
    private static final String[] IMPORTANT_NODES = {"TR", "TD"};
    private static final String[] INFO_NODE = {"P", "SPAN", "BR"};
    private static final String LINK_NODE = "A";
    private static final String[] TITLE = {"H1", "H2", "H3"};
    private static final String[] INVALID_TAGS = {"STYLE", "COMMENT", "SCRIPT", "OPTION", "LI"};
    public static final int TITLE_WEIGHT = 200;
    public static final int PLAIN_TEXT_WEIGHT = 30;
    public static final int ANCHOR_TEXT_WEIGHT = 10;
    public static final int REF_WEIGHT = 70;
    
    
    
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

    public static boolean isTitleNode(Element e) {
        for (String s : TITLE) {
            if (e.getTagName().equals(s)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isLinkNode(Element e) {
        if (e.getTagName().equals(LINK_NODE)) {
            return true;
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

}
