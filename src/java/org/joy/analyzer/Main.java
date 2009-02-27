/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.joy.analyzer.html.HTMLDocument;
import org.joy.analyzer.html.ParseException;
import org.joy.nlp.ACWordSpliter;
import org.joy.nlp.WordSpliter;

/**
 * 
 * @author Lamfeeling
 */
public class Main {

	/**
	 * @param args
	 *            the command line arguments
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException,
			IOException, ParseException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		// TODO 在这里添加测试代码
		String data = org.joy.analyzer.html.Utility
				.getWebContent("http://news.sina.com.cn/c/2008-12-11/155516828944.shtml");
		//分析web资源
		HTMLDocument doc = HTMLDocument
				.createHTMLDocument(
						"http://news.sina.com.cn/c/2008-12-11/155516828944.shtml",
						data);

		//初始化分词器
		WordSpliter spliter = new ACWordSpliter();
		//初始化一个管道分析器，把分词和关键词分析结合起来。
		PipelineAnalyzer<WordSpliter, List<Hit>> analyzer = new PipelineAnalyzer<WordSpliter, List<Hit>>(
				new Analyzer[] {
						new TokenAnalyzer(),
						// 分词分析器
						(Analyzer) Class.forName("PluginTest").newInstance() 
						// 关键词分析器,您可以在管道里加入任何你自己的分析器
				});
		//设置分析文档
		analyzer.setDoc(doc);
		//输入
		analyzer.input(spliter);
		//执行分析操作
		analyzer.doAnalyze();
		//打印输出
		List<Hit> hits = analyzer.output();
		System.out.println(hits);

	}
}
