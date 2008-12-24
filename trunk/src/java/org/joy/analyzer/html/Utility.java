/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joy.analyzer.TestGUI;
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
    private static final String[] INFO_NODE = {"P", "SPAN", "H1", "H2", "B", "I"};
    public static final String[] HEADING_TAGS = {"TITLE", "H1", "H2", "H3", "H4", "H5", "H6", "H7"};
    private static final String[] INVALID_TAGS = {"STYLE", "COMMENT", "SCRIPT", "OPTION", "LI"};
    private static final String[] SPACING_TAGS = {"BR", "SPAN"};
    private static final String LINK_NODE = "A";

    public static String getWebContent(String URL) {
        String s = "";
        try {
            InputStream in = null;
            HttpURLConnection conn = (HttpURLConnection) new URL(URL).openConnection();
            String contentType = conn.getHeaderField("Content-Type").toLowerCase();

            String encoding = "utf-8";
            boolean charsetFound = false;
            if (contentType.contains("charset")) {
                encoding = contentType.split("charset=")[1];
                charsetFound = true;
            }
            if (!charsetFound) {
                in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
                char[] buf = new char[1024];
                int len = br.read(buf);
                String header = new String(buf, 0, len);
                Pattern p = Pattern.compile(".*<meta.*content=.*charset=.*", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(header);
                if (m.matches()) {
                    encoding = header.toLowerCase().split("charset=")[1].replaceAll("[^a-z|1-9|\\-]", " ").split("\\s+")[0];
                } else {
                    encoding = "utf-8";
                }
                charsetFound = true;
                br.close();
            }
            
            conn = (HttpURLConnection) new URL(URL).openConnection();
            in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, encoding));
            String line = br.readLine();
            StringBuffer sb = new StringBuffer();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            br.close();
            s = sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(TestGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

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

    public static boolean containsNoise(String text) {
        if (text.toLowerCase().contains("copyright") ||
                text.toLowerCase().contains("all rights reserved") ||
                text.toLowerCase().contains("版权所有") ||
                text.toLowerCase().contains("©") ||
                text.toLowerCase().contains("上一页") ||
                text.toLowerCase().contains("下一页") ||
                text.toLowerCase().contains("ICP备")) {
            return true;
        }
        return false;
    }
}
