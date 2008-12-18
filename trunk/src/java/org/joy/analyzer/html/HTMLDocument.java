/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.html;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import org.cyberneko.html.parsers.DOMParser;
import org.joy.analyzer.Document;
import org.joy.analyzer.Paragraph;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * HTML鏂囨湰绫汇��
 * @author Lamfeeling
 */
public class HTMLDocument extends Document {

    private org.w3c.dom.Document doc;
    private List<Anchor> anchors;
    private List<Paragraph> paragraphs;
    private String url;
    private String bodyText="";
    /**
     * 浠庢寚瀹氱殑瀛楃涓蹭腑鏋勯�犱竴涓狧TMLDocument
     * @param str 鎵�鍒跺畾鐨勫瓧绗︿覆
     * @return 鐢辨寚瀹氱殑瀛楃涓插鏃╃殑鏂囨。绫�
     */
    public static HTMLDocument createHTMLDocument(String URL, String str) throws ParseException {
        DOMParser parser = new DOMParser();
        try {
             parser.parse(new InputSource(new StringReader(str)));
        } catch (SAXException ex) {
            //濡傛灉瑙ｆ瀽閿欒锛岃鎶涘嚭寮傚父
            Logger.getLogger(HTMLDocument.class.getName()).log(Level.SEVERE, null, ex);
            throw new ParseException(ex.getMessage());
        } catch (IOException ex) {
            // never reach here
            Logger.getLogger(HTMLDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new HTMLDocument(URL,str,  parser.getDocument());
    }

    /**
     * 鍙椾繚鎶ょ殑鏋勯�犳柟娉曪紝涓嶅彲浠ョ洿鎺ユ瀯閫�
     * @param URL 缃戦〉鐨刄RL鍦板潃
     * @param content 鐢ㄤ簬鏋勯�犳枃妗ｇ殑瀛楃涓�
     * @param doc 鐢变笂鏂囧垎鏋愬櫒鍒嗘瀽鍑虹殑Document绫�
     */
    protected HTMLDocument(String url,String content, org.w3c.dom.Document doc) {
        super(content);
         this.doc = doc;
        this.url = url;
        parse();
       
    }

    private void parse(){
        //TODO: 鍒╃敤姝ょ被涓殑Document鍙橀噺鍒嗘瀽HTML锛屽垎鏋愪唬鐮佸啓杩欓噷銆傛柟娉曚箣寰岋紝鎵�鏈夌殑绉佹湁鍙橀噺閮借璧嬩簣鍚堥�傜殑鍒濆鍊笺��
        Parser p = new Parser(url, doc);
        p.parse();
        anchors = p.getAnchors();
        
        //     提取正文
        Extractor ext=new Extractor(doc,getContent());
        ext.extract();
        bodyText=ext.getTextBody();
    }
    /**
     * 鑾峰彇鏂囨湰涓殑姝ｆ枃娈佃惤
     * @return 杩斿洖鏂囨。涓殑姝ｆ枃娈佃惤鐨勯泦鍚�
     */
    @Override
    public List<Paragraph> getParagraphs() {
        //return paragraphs;
        throw new UnsupportedOperationException();
    }

    /**
     * 杩斿洖HTML鏂囨。鐨勮繛鎺ラ泦鍚�
     * @return HTML鏂囨。鐨勮繛鎺ラ泦鍚�
     */
    public List<Anchor> getAnchors() {
        return anchors;
    }

    /**
     * 杩斿洖HTML鏂囨湰鐨勬爣棰�
     * @return HTML鏂囨湰鐨勬爣棰�
     */
    @Override
    public String getTitle() {
        return doc.getElementsByTagName("TITLE").item(0).getTextContent();
    }

    /**
     * 杩斿洖鏂囨湰鏋勫缓鐨勭殑DOM鏍�
     * @return Dom鏍戞枃妗ｅ璞�
     */
    public org.w3c.dom.Document getDoc() {
        return doc;
    }

	public String getBodyText() {
		return bodyText;
	}
}
