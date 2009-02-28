/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.terms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joy.nlp.Word;

/**
 * 简单的关键词提取，返回所有的分词结果，过滤掉停用词
 * 
 * @author Administrator
 */
public class SimpleTermExtractor extends TermExtractor {

	private final static HashSet<String> stopWords = new HashSet<String>();
	// 读取停用词表

	static {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"./dicts/stop.txt"));
			String line = br.readLine();
			while (line != null) {
				stopWords.add(line);
				line = br.readLine();
			}
			br.close();
		} catch (IOException ex) {
			Logger.getLogger(SimpleTermExtractor.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	@Override
	public HashSet<String> getTerms() {
		// 直接把分词结果作为term返回
		HashSet<String> termSet = new HashSet<String>();
		for (Word w : words) {
			if (!w.getText().trim().equals("")
					&& !stopWords.contains(w.getText())) {
				termSet.add(w.getText());
			}
		}
		return termSet;
	}
}
