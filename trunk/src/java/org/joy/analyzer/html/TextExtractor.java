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
 * 
 * Extract main body from HTML document. Main Body is a block of text that is
 * close to the main idea of current HTML document. Extractor will also generate
 * a weight for each paragraph, which indicates how this paragraph are close to
 * document's main idea.
 * 
 */
public class TextExtractor {

    /**
     * Max anchor density allowed
     */
    public static final double MAX_ANCHOR_DEN = 0.5;
    /**
     * How many chars in current document?
     */
    private int totalTextLen = 0;
    /**
     * How many chars in anchors of current document?
     */
    private int totalAnchorTextLen = 0;
    /**
     * How many info nodes do we have in current document?
     */
    private int totalNumInfoNodes = 0;
    /**
     * Tag list
     */
    private List<TagWindow> windowsList = new ArrayList<TagWindow>();
    /**
     * paragraph list
     */
    private List<Paragraph> paragraphList = new ArrayList<Paragraph>();
    /**
     * w3c HTML DOM tree
     */
    private Document doc;

    /**
     * construct a Extractor using DOM model
     * 
     * @param doc
     *            DOM model
     */
    public TextExtractor(Document doc) {
	super();
	this.doc = doc;

    }

    /**
     * Remove some tags that will obviously not contains the main body.
     * 
     * @param e
     *            w3c element
     * @throws ParseException
     * 
     */
    private void cleanup(Element e, int level) throws ParseException {
	NodeList c = e.getChildNodes();
	for (int i = 0; i < c.getLength(); i++) {
	    if (c.item(i).getNodeType() == Node.ELEMENT_NODE) {
		Element t = (Element) c.item(i);
		if (Utility.isInvalidElement(t)) {
		    e.removeChild(c.item(i));
		} else {
		    cleanup(t, level + 1);
		}
	    }
	}
    }

    /**
     * Extract main body in current HTML document, and generate the paragraphs
     * list
     * 
     * @return main body of current HTML docuemnt
     * @throws ParseException
     */
    public String extract() throws ParseException {
	Node body = doc.getElementsByTagName("BODY").item(0);
	if (body == null)
	    return "";
	// cleanup, remove the invalid tags,
	cleanup((Element) body, 0);

	totalTextLen = TagWindow.getInnerText(body, false).length();
	// get anchor text length
	totalAnchorTextLen = TagWindow.getAnchorText((Element) body).length();

	totalNumInfoNodes = TagWindow.getNumInfoNode((Element) body);

	buildTagWindows(body);

	String bodyText = "";
	if (windowsList.size() == 0) {
	    bodyText = "";
	} else {
	    // get the max score
	    Collections.sort(windowsList, new Comparator<TagWindow>() {

		public int compare(TagWindow t1, TagWindow t2) {
		    if (t1.weight(totalTextLen, totalAnchorTextLen,
			    totalNumInfoNodes) > t2.weight(totalTextLen,
			    totalAnchorTextLen, totalNumInfoNodes)) {
			return 1;
		    } else {
			return -1;
		    }
		}
	    });
	    TagWindow max = windowsList.get(windowsList.size() - 1);
	    bodyText = max.getInnerText(true);

	}
	// dont forget Title, if it has
	String whole = TagWindow.getInnerText(body, true);
	// TODO:NEKO HTML bug walking around
	if (!whole.trim().startsWith("<TITLE>")
		&& doc.getElementsByTagName("TITLE").getLength() != 0) {
	    whole = "<TITLE>"
		    + TagWindow.getInnerText(doc.getElementsByTagName("TITLE")
			    .item(0), false) + "</TITLE>\r\n" + whole;
	}

	if (!bodyText.trim().equals("")) {
	    // extract all the paragraphs, add them to the paragraph list
	    paragraphList = new ParagraphSplitter(bodyText, whole).split();
	}
	return bodyText;
    }

    /**
     * build Tag Windows for each W3C node
     * 
     * @param node
     *            w3c Node that you want to build from
     */
    private void buildTagWindows(Node node) {
	if (node.getNodeType() == Node.TEXT_NODE) {
	    return;
	}

	if (node.getNodeType() == Node.ELEMENT_NODE) {
	    Element element = (Element) node;
	    if (Utility.isInvalidElement(element)) {
		return;
	    }
	    if (!TagWindow.getInnerText(node, false).trim().equals("")) // add
	    // the
	    // tags
	    {
		windowsList.add(new TagWindow(node));
	    }
	    NodeList list = element.getChildNodes();
	    for (int i = 0; i < list.getLength(); i++) {
		buildTagWindows(list.item(i));
	    }
	}
    }


    public List<Paragraph> getParagraphList() {
	return paragraphList;
    }

    /**
     * Test method
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
	File folder = new File("d:/res2/");
	for (File f : folder.listFiles()) {
	    DOMParser parser = new DOMParser();
	    BufferedReader reader = new BufferedReader(new FileReader(
		    "d://res2/" + f.getName()));
	    try {
		parser.parse(new InputSource(reader));
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    Document doc = parser.getDocument();
	    System.out.println(doc.getElementsByTagName("BODY").item(0)
		    .getTextContent());
	    System.out.print(f.getName() + "....");

	    TextExtractor extractor = new TextExtractor(doc);
	    String str = extractor.extract();
	    StringBuffer sb = new StringBuffer();
	    for (Paragraph p : extractor.getParagraphList()) {
		sb.append(p.getText() + "\r\n" + p.getWeight() + "\r\n");
		// System.out.print(p.getWeight()+"...\n");
	    }
	    System.out.println(extractor.totalAnchorTextLen + ":  "
		    + extractor.totalTextLen + "\t"
		    + extractor.totalAnchorTextLen
		    / (float) extractor.totalTextLen);

	    // System.out.println(str);
	    FileWriter fw = new FileWriter("d:/res3/" + f.getName() + ".txt");
	    fw.write(sb.toString());
	    fw.close();
	}
    }
}
