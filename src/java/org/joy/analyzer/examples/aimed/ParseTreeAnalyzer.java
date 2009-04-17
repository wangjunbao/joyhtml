package org.joy.analyzer.examples.aimed;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import org.joy.analyzer.Analyzer;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.Tree;

/**
 * 语法树分析器
 * 
 * @author Andy
 * 
 */
public class ParseTreeAnalyzer extends Analyzer<LexicalizedParser, Tree> {
	private void insertEmbededProtein(Tree t, String host, Integer[] children) {
		// for (Tree c : t.children()) {
		// if (c.value().contains(host)) {
		// for (Integer i : children) {
		// try {
		// c.addChild(Tree.valueOf("PROTEIN" + i));
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// return;
		// }
		// }
		Tree[] ts = t.children();
		for (int i = 0; i < ts.length; i++) {
			if (ts[i].value().contains(host)) {
				try {
					t.removeChild(i);
					t.insertDtr(Tree.valueOf("(" + ts[i].value() + ")"), i);
					ts = t.children();
					for (Integer j : children) {
						try {
							ts[i].addChild(Tree.valueOf("PROTEIN" + j));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		ts = t.children();
		for (int i = 0; i < ts.length; i++) {
			insertEmbededProtein(ts[i], host, children);
		}
	}

	@Override
	public void doAnalyze() {
		// 转换成AIMED PARA
		AimedParagraph aimedPara = (AimedParagraph) para;
		// 如果没有关系对，就不要分析了
		if (aimedPara.getRelation().length < 2) {
			output = null;
			return;
		}
		// 分句器
		DocumentPreprocessor dp = new DocumentPreprocessor();
		List<List<? extends HasWord>> sentences = dp
				.getSentencesFromText(new StringReader(aimedPara.getText()));
		Tree t = null;
		// 获取每句话的parse树
		for (int i = 0; i < sentences.size(); i++) {
			System.out.println(sentences.get(i));
			if (t == null)
				t = input.apply(sentences.get(i));
			else
				// 把第二句话加入到第一句话的后面，作为一个s节点
				t.addChild(input.apply(sentences.get(i)).getChild(0));
		}
		// 把语法树当中的嵌套结构表现出来
		for (Integer[] em : aimedPara.getEmbedmentList()) {
			String host = "PROTEIN" + (em[em.length - 1] + 1);
			insertEmbededProtein(t, host, em);
		}
		// 打印输出语法树，only for debug
		StringWriter sw = new StringWriter();
		input.getTreePrint().printTree(t, new PrintWriter(sw, true));
		System.out.println(sw.toString().replaceAll("\\r\\n", "").replaceAll(
				"\\s+", " "));
		output = t;
	}

}
