package org.joy.analyzer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Document describes an document analyzed in joydoc.
 * 
 * @author Song Liu(lamfeeling2@gmail.com)
 */
public abstract class Document {

    private String data;
    private String url;
    protected List<Paragraph> paragraphs;

    /**
     * create document from inputstream
     * 
     * @param in
     */
    public abstract void createFromInputStream(InputStream in, String URL)
	    throws IOException, DocumentCreationException;

    /**
     * construct a document using default constuctor
     */
    public Document() {
	paragraphs = new ArrayList<Paragraph>();
    }

    /**
     * Construct an document with given URL and document data
     * 
     * @param data
     *            raw data for document.
     */
    public Document(String URL, String data) {
	this.data = data;
	this.url = URL;
    }

    /**
     * get paragraphs for this document
     * 
     * @return paragraphs for this document
     */
    public List<Paragraph> getParagraphs() {
	return paragraphs;
    }

    /**
     * content(friendly representation) for current document
     * 
     * @return content for current document
     */
    public String getContent() {
	StringBuffer sb = new StringBuffer();
	for (Paragraph p : paragraphs) {
	    sb.append(p.getText());
	}
	return sb.toString();
    }

    /**
     * get document title
     * 
     * @return document title
     */
    public abstract String getTitle();

    /**
     * set the document raw data
     * 
     * @param data
     *            set document raw data
     */
    protected void setData(String data) {
	this.data = data;
    }

    /**
     * get raw data of this document
     * 
     * @return raw data of this document
     */
    public String getData() {
	return data;
    }

    /**
     * set URL for current document
     */
    protected void setUrl(String URL) {
	this.url = URL;
    }

    /**
     * get URL for current document
     * 
     * @return docuemnt URL
     */
    public String getUrl() {
	return url;
    }

    /**
     * get MIME type for current document.
     * 
     * @return  MIME type for current document.
     */
    public abstract String getMineType();

    /**
     * file suffix for current document.
     * 
     * @return file suffix for current document.
     */
    public abstract String getSuffix();
}
