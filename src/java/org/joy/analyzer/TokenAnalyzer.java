package org.joy.analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joy.nlp.Word;
import org.joy.nlp.WordSpliter;

public class TokenAnalyzer extends Analyzer<WordSpliter, List<Word>> {
	@Override
	public void doAnalyze() {
		try {
			output = new ArrayList<Word>();
			BufferedReader br = new BufferedReader(new StringReader(doc
					.getContent()));
			String line = br.readLine();
			while (line != null) {
				output.addAll(Arrays.asList(input.splitToWords(line)));
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
