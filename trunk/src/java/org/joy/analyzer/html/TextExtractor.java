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
import java.util.Comparator;
import java.util.List;
import org.xml.sax.InputSource;
import org.cyberneko.html.parsers.DOMParser;
import org.joy.analyzer.Paragraph;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
     * store the number of text not in anchor
     */
    private int totalTextLen = 0;
    /**
     * store the number of text not in anchor
     */
    private int totalAnchorTextLen = 0;
    private int totalNumInfoNodes = 0;
    /**
     * visit every node and count the factors that affect the priority
     * of this node
     */
    private List<Tag> tagList = new ArrayList<Tag>();
    private List<Paragraph> paragraphList = new ArrayList<Paragraph>();
    private Document doc;

    public TextExtractor(Document doc) {
        super();
        this.doc = doc;

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

    private void adjust(Element e) {
        NodeList c = e.getChildNodes();
        for (int i = 0; i < c.getLength(); i++) {
            if (c.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element t = (Element) c.item(i);
                if (new Tag(c.item(i)).isExtra()) {
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
        long s = System.currentTimeMillis();
        Node body = doc.getElementsByTagName("BODY").item(0);
        //cleanup, remove the invalid tags
        cleanup((Element) body);
        String whole = Tag.getInnerText(body, true);
        totalTextLen = Tag.getInnerText(body, false).length();
        // get anchor text length
        totalAnchorTextLen = Tag.getAnchorText((Element) body).length();

        totalNumInfoNodes = Tag.getNumInfoNode((Element) body);

        evaluateNodes(body);

        String bodyText = "";
        if (tagList.size() == 0) {
            bodyText = "";
        } else {
            //get the max score
            Collections.sort(tagList, new Comparator<Tag>() {

                public int compare(Tag t1, Tag t2) {
                    if (t1.weight(totalTextLen, totalAnchorTextLen, totalNumInfoNodes) >
                            t2.weight(totalTextLen, totalAnchorTextLen, totalNumInfoNodes)) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
            Tag max = tagList.get(tagList.size()-1);
            for (Tag t : tagList) {
                System.out.println(t.getInnerText(false) + "\t" + t.getWeight());
            }
            System.out.print("\t" + (System.currentTimeMillis() - s) + "\t");
            //cleanup2((Element) max.node);
            bodyText = max.getInnerText(true);

        }

        //extract all the paragraphs, add them to the paragraph list
        paragraphList = new ParagraphExtractor(bodyText, whole).extract();

        return bodyText;
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
                tagList.add(new Tag(node));
//                System.out.println(node.getTextContent());
//                System.out.println("");
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
