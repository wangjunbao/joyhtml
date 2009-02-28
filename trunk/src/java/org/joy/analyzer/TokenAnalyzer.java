package org.joy.analyzer;

import java.util.Arrays;
import java.util.List;

import org.joy.nlp.Word;
import org.joy.nlp.WordSpliter;

public class TokenAnalyzer extends Analyzer<WordSpliter, List<Word>> {
	@Override
	public void doAnalyze() {
		//分词器直接输出分词结果。
		output = Arrays.asList(input.splitToWords(doc.getContent()));
	}
}
