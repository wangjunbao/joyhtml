package org.joy.nlp;

/**
 * Word class, stores the word text and the POS tagger
 * 
 * @author Song Liu(lamfeeling2@gmail.com)
 */
public class Word {

    private String text;
    private String tag;

    public Word() {
    }

    public Word(String s) {
	int i = s.lastIndexOf("/");
	tag = s.substring(i + 1, s.length());
	// we store word ignoring its case
	text = s.substring(0, i).toLowerCase();
    }

    public Word(String text, String tag) {
	this.text = text;
	this.tag = tag;
    }

    /**
     * get the POS tagger
     * @return POS tagger
     */
    public String getTag() {
	return tag;
    }

    /**
     * get the word text 
     * @return word text
     */
    public String getText() {
	return text;
    }

}
