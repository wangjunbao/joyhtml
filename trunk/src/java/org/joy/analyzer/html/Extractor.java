package org.joy.analyzer.html;

import java.util.TreeMap;

import org.joy.analyzer.Paragraph;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class Extractor {
	private Document doc;
	private int textSize,  numLinks = 1,  numInfoNodes;
	String content;
    private String title;
    private String body;
    private TreeMap<Float, Node> nodeList = new TreeMap<Float, Node>();
    boolean needWarp = false;
    private boolean isInAnchor;
    
    
    public Extractor(Document doc,String content) {
    	this.doc=doc;
    	this.content=content;
		
	}

    private void reset() {
        nodeList.clear();
        textSize = 0;
        numLinks = 1;
        numInfoNodes = 0;
    }
    public void extract()
    {
    	reset();
    	getTitle();
    	Node bodyNode=doc.getElementsByTagName("BODY").item(0);
    	statistics(bodyNode);
    	calWeight(bodyNode);
    	this.body=extractText(nodeList.lastEntry().getValue());
    	
    }
    private String extractText(Node n) {
    	
    	 if (n.getNodeType() == Node.TEXT_NODE) {
             String temp = filter(n.getTextContent()).trim();
             
             return temp;
         }
         if (n.getNodeType() == Node.ELEMENT_NODE) {
             if (NodeQuality.isInvalidNode((Element) n)) {
                 return "";
             }

             NodeList children = n.getChildNodes();
             StringBuilder sb = new StringBuilder();
             for (int i = 0; i < children.getLength(); i++) {
                 if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                     Element eChild = (Element) children.item(i);
                     if (NodeQuality.isInfoNode(eChild) || NodeQuality.isImportantNode(eChild) ||
                    		 NodeQuality.isLargeNode(eChild)) {
                         needWarp = true;
                     }
                 }

                 String line = "";
                 if (children.item(i).getNodeName().equals("A")) {
                     line = extractText(children.item(i));
                 } else {
                     int anchorLen = getAnchorTextLength(children.item(i));
                     int textLen = this.getNodeTextLength(children.item(i));
                     if ((float) anchorLen / textLen < 0.5) {
                         line = extractText(children.item(i));
                     } else {
                         if (textLen != 0) {
                             System.out.println();
                             System.out.println(anchorLen);
                             System.out.println(textLen);
                             System.out.println((float) anchorLen / textLen);
                             System.out.println();
                         }
                     }
                 }

                 if (!line.trim().equals("")) {
                     if (needWarp) {
                         sb.append(line + "\r\n    ");
                     } else {
                         sb.append(line);
                     }
                 }
                 needWarp = false;
             }
             return sb.toString();
         }
         return "";
    	}
	
	private void calWeight(Node node) {
    	if (node == null) {
            return;
        }
        if (node.getNodeType() == Node.TEXT_NODE) {
            return;
        }

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            float weight = 0;
            Element e = (Element) node;
            if (NodeQuality.isInvalidNode(e)) {
                return;
            }
            //����Ȩ��
            weight += NodeQuality.isImportantNode(e) ? 1 : 0;
            weight += NodeQuality.isLargeNode(e) ? 0.5 : 0;
            weight += NodeQuality.isTitleNode(e) ? 1 : 0;
            weight += fn(getInfoNodeNum(e) / (float) (numInfoNodes + 1));
            weight += fn(getNodeTextLength(e) / (float) textSize);
            weight -= getLinkNodeNum(e) / (float) (numLinks + 1);
            nodeList.put(weight, node);

            NodeList children = e.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                calWeight(children.item(i));
            }
        }
		
	}
    //===============?
    //�������鷳���������
    private float fn(float x) {
        if (x > 0.8f) {
            return 0.8f;
        }
        return x;
    }
    //===============
	private void statistics(Node node) {
		
		if(node==null)
		{
			return;
		}//����child����
		if(node.getNodeType()==Node.TEXT_NODE)
		{
			textSize+=this.filter(node.getTextContent()).length();
		}
		if(node.getNodeType()==node.ELEMENT_NODE)
		{
			Element e=(Element)node;
			
			if(NodeQuality.isInvalidNode(e))
			{
				return;
			}
			if(NodeQuality.isInfoNode(e))
			{
				numInfoNodes++;
			}
			if(NodeQuality.isLinkNode(e))
			{
				numLinks++;
			}
			NodeList children=e.getChildNodes();
			for(int i=0;i<children.getLength();i++)
			{
				statistics(children.item(i));
			}
		}
	}
	public int getAnchorTextLength(Node n) {
        if (n.getNodeType() == Node.TEXT_NODE) {
            if (isInAnchor) {
                return filter(n.getTextContent()).trim().length();
            } else {
                return 0;
            }
        }
        if (n.getNodeType() == Node.ELEMENT_NODE) {
            int length = 0;
            NodeList children = n.getChildNodes();
            if (NodeQuality.isLinkNode((Element) n)) {
                isInAnchor = true;
            }
            for (int i = 0; i < children.getLength(); i++) {
                length += getAnchorTextLength(children.item(i));
            }
            isInAnchor = false;
            return length;
        }
        return 0;
    }
	private void getTitle() {
		Node titleNode=doc.getElementsByTagName("TITLE").item(0);
		if(titleNode!=null)
		{
			this.title=titleNode.getTextContent();
			
			
		}
		
	}
	 private String filter(String text) {
		 text = text.replaceAll("[^\u2e80-\ufffdh ~@#$%&:,.';\"<>*+_-|\\w]", " ");
	        text = text.replaceAll("\n+", " ");
	        text = text.replaceAll("\\|", "");
	        text = text.replaceAll("\\s+", " ");
	        text = text.trim();
	        return text;
	 }
	 
	 private int getLinkNodeNum(Element e)
	 {
		 
		 int num=0;
		 if(NodeQuality.isLinkNode(e))
		 {
			 num=1;
		 }
		 
		 NodeList children=e.getChildNodes();
		 for(int i=0;i<children.getLength();i++)
		 {
			 if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
				 
				 num+=this.getLinkNodeNum((Element)children.item(i));
			 }
			 
		 }
		 return num;
	 }
	 private int getNodeTextLength(Node n)
	 {
		 if(n.getNodeType()==Node.TEXT_NODE)
		 {
			 return this.filter(n.getTextContent()).length();
		 }
		 if(n.getNodeType()==Node.ELEMENT_NODE)
		 {
			 int len=0;
			 Element e=(Element)n;
			 if(NodeQuality.isInvalidNode(e))
			 {
				 return 0;
			 }
			 NodeList children=e.getChildNodes();
			 for(int i=0;i<children.getLength();i++)
			 {
				 len+=this.getNodeTextLength(children.item(i));
			 }
			 
			 return len;
		 }
		 return 0;
	 }
	
	 private int getInfoNodeNum(Element e) {
	        int num =NodeQuality.isInfoNode(e) ? 1 : 0;
	        NodeList children = e.getChildNodes();
	        for (int i = 0; i < children.getLength(); i++) {
	            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
	                num += getInfoNodeNum((Element) children.item(i));
	            }
	        }
	        return num;
	    }
	 public String getTextBody()
    {
    	
    	return title + "\n" + body;
    }

}
