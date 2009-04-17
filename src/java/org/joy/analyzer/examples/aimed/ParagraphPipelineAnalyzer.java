package org.joy.analyzer.examples.aimed;

import java.util.Arrays;
import java.util.Vector;

import org.joy.analyzer.Analyzer;
import org.joy.analyzer.Paragraph;
import org.joy.analyzer.PipelineAnalyzer;

/**
 * 分析段落的管道串联分析器
 * @author Andy
 *
 * @param <K> 输入类型
 * @param <E> 输出类型
 */
public class ParagraphPipelineAnalyzer<K, E> extends
		PipelineAnalyzer<K, Vector<E>> {

	public ParagraphPipelineAnalyzer(Analyzer<Object, Object>[] analyzers) {
		super(analyzers);
	}

	@Override
	public void doAnalyze() {
		output = null;
		Vector<E> res = new Vector<E>();
		for (Paragraph p : doc.getParagraphs()) {
			Object in = input;
			for (Analyzer<Object, Object> a : analyzers) {
				a.setPara(p);
				a.input(in);
				a.doAnalyze();
				in = a.output();
			}
			if (in != null)
				res.addAll(Arrays.asList((E[]) in));
		}
		output = res;
	}
}
