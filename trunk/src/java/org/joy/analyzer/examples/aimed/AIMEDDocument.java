package org.joy.analyzer.examples.aimed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.xerces.parsers.DOMParser;
import org.joy.analyzer.Analyzer;
import org.joy.analyzer.Document;
import org.joy.analyzer.DocumentCreationException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.Pair;

public class AIMEDDocument extends Document {
	public final static String SENTENTCE_TAG = "sentence";
	public final static String ENTITIES_TAG = "entity";

	public static void main(String[] args) throws Exception, IOException,
			DocumentCreationException {

		File f = new File(args[0]);
		AIMEDDocument doc = new AIMEDDocument();
		doc.createFromInputStream(new FileInputStream(f), f.toURI().toString());

		LexicalizedParser parser = new LexicalizedParser(
				"./dicts/englishPCFG.ser.gz");
		SubTreeAnalyzer subAna = new SubTreeAnalyzer();
		subAna.setTreePrint(parser.getTreePrint());

		ParagraphPipelineAnalyzer<LexicalizedParser, Pair<Boolean, Tree>> p = new ParagraphPipelineAnalyzer<LexicalizedParser, Pair<Boolean, Tree>>(
				new Analyzer[] { new ParseTreeAnalyzer(), subAna,
						new FilterAnalyzer() });
		p.setDoc(doc);
		p.input(parser);
		p.doAnalyze();

		// 输出到文件
		FileWriter fw = new FileWriter("res.txt");
		FileWriter fw2 = new FileWriter("res2.txt");
		for (Pair<Boolean, Tree> pair : p.output()) {
			if (pair == null)
				continue;
			StringWriter sw = new StringWriter();
			parser.getTreePrint().printTree(pair.second, new PrintWriter(sw));
			String penn = sw.toString().replaceAll("\\r\\n", "").replaceAll(
					"\\s+", " ").replaceAll("\\(\\d+\\)", "(2 )");

			// 按照TK SVM-LIGHT的格式输出
			if (!penn.matches("\\(.+\\)")) {
				fw2.write((pair.first ? 1 : -1) + "\t|BT| (" + penn
						+ " ) |ET|\n");
			} else {
				fw2.write((pair.first ? 1 : -1) + "\t|BT| " + penn + " |ET|\n");
			}
			//按照钱龙华老师的程序格式输出
			if(pair.first){
				if (!penn.matches("\\(.+\\)")) {
					fw.write("1 1 (" + penn
							+ " ) \r\n");
				} else {
					fw.write("1 1 " + penn + " \r\n");
				}
			}
		}
		fw2.close();
		fw.close();
		System.out.println(p.output());
	}

	static class EntityPosition {

		public EntityPosition(int start, int end) {
			super();
			this.start = start;
			this.end = end;
		}

		int start, end;
	}

	private ArrayList<Integer[]> getEmbededList(NodeList entities,
			ArrayList<EntityPosition> rangeList) {
		ArrayList<Integer[]> embedmentList = new ArrayList<Integer[]>();
		// 读取到rangeList
		for (int i = 0; i < entities.getLength(); i++) {
			Element e = (Element) entities.item(i);
			String[] s = e.getAttribute("charOffset").split("-");
			int start = Integer.parseInt(s[0]);
			int end = Integer.parseInt(s[1]);
			rangeList.add(new EntityPosition(start, end));
		}
		// 分析嵌套情况
		for (int i = 0; i < rangeList.size(); i++) {
			ArrayList<Integer> vector = new ArrayList<Integer>();
			for (int j = 0; j < rangeList.size(); j++) {
				if (rangeList.get(i).start <= rangeList.get(j).start
						&& rangeList.get(i).end >= rangeList.get(j).end
						&& rangeList.get(i).end - rangeList.get(i).start > rangeList
								.get(j).end
								- rangeList.get(j).start) {
					// 找找看这个entity之前有没有被嵌套过,如果有，删除之，让更大的来嵌套
					for (Integer[] l : embedmentList) {
						if (new HashSet<Integer>(Arrays.asList(l)).contains(j)) {
							embedmentList.remove(l);
							break;
						}
					}
					vector.add(j);
				}
			}
			if (vector.size() != 0) {
				embedmentList.add(vector.toArray(new Integer[0]));
			}
		}
		// 更新rangelist
		int i = 0;
		for (Integer[] e : embedmentList) {
			for (Integer j : e) {
				rangeList.remove((int) (j - i));
				i++;
			}
		}
		return embedmentList;
	}

	@Override
	public void createFromInputStream(InputStream in, String URL)
			throws IOException, DocumentCreationException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(br));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		org.w3c.dom.Document doc = parser.getDocument();
		NodeList senNodes = doc.getElementsByTagName(SENTENTCE_TAG);
		int offset = 0, numpairs = 0;
		for (int i = 0; i < senNodes.getLength(); i++) {
			Element e = (Element) senNodes.item(i);
			// 定位所有entities
			String sen = e.getAttribute("text");
			NodeList entities = e.getElementsByTagName(ENTITIES_TAG);
			ArrayList<EntityPosition> rangeList = new ArrayList<EntityPosition>();
			ArrayList<Integer[]> res = getEmbededList(entities, rangeList);
			// 标定Entities
			int j = 0;
			for (EntityPosition pos : rangeList) {
				String sen1 = sen.substring(0, sen.indexOf("_")) + "\\PROTEIN";
				String sen2 = sen.substring(sen.indexOf("_")
						+ (pos.end - pos.start) + 1);
				// 如果有嵌套实体
				if (res.size() != 0) {
					for (Integer[] m : res) {
						for (Integer k : m) {
							if (k == j) {
								j++;
							}
						}
					}
				}
				sen1 += j;
				sen = sen1 + sen2;
				j++;
			}
			sen = sen.replaceFirst("[a-zA-Z]+:\\s+", "");
			// sen = sen.replaceAll("\\(.+=.+\\)", "");
			System.out.println(sen);
			paragraphs.add(new AimedParagraph(sen, 1.0, offset, e, res));
			numpairs += e.getElementsByTagName("pair").getLength();
			offset += sen.length();
		}
		System.out.println(numpairs);
		System.out.println(AimedParagraph.numpairs);
	}

	@Override
	public String getMineType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSuffix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}
}
