package org.joy.analyzer.examples.aimed;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.joy.analyzer.Analyzer;

import edu.stanford.nlp.io.EncodingPrintWriter.out;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;

/**
 * 子树提取分析器
 * 
 * @author Andy
 * 
 */
public class SubTreeAnalyzer extends Analyzer<Tree, Tree[]> {
	private TreePrint treePrint;

	public void setTreePrint(TreePrint treePrint) {
		this.treePrint = treePrint;
	}

	private boolean contains(Tree root, String protein) {
		if (root.label().value().contains(protein)) {
			return true;
		} else {
			Tree[] children = root.children();
			for (int i = 0; i < children.length; i++) {
				if (contains(children[i], protein)) {
					return true;
				}
			}
			return false;
		}
	}

	private Tree findSmallestCommonTree(Tree root, String protein1,
			String protein2) {
		if (!contains(root, protein1) || !contains(root, protein2)) {
			return null;
		} else {
			Tree[] children = root.children();
			for (int i = 0; i < children.length; i++) {
				Tree t = findSmallestCommonTree(children[i], protein1, protein2);
				if (t != null) {
					return t;
				}
			}
			return root;
		}
	}

	private Tree convertToPathTree(Tree root, String protein1, String protein2) {
		Tree[] children = root.children();
		int left = 0, right = children.length - 1;
		for (int i = 0; i < children.length; i++) {
			if (contains(children[i], protein1)) {
				left = i;
			}
			if (contains(children[i], protein2)) {
				right = i;
			}
		}
		for (int i = 0; i < left; i++) {
			root.removeChild(i);
			i--;
			left--;
		}
		children = root.children();
		for (int i = children.length - 1; i > right; i--) {
			root.removeChild(i);
		}
		children = root.children();
		for (int i = 0; i < children.length; i++) {
			convertToPathTree(children[i], protein1, protein2);
		}
		return root;
	}



	@Override
	public void doAnalyze() {
		AimedParagraph para = (AimedParagraph) this.para;
		if (para.getRelation().length < 2) {
			output = null;
			return;
		}

		ArrayList<Tree> res = new ArrayList<Tree>();
		for (int i = 0; i < para.getRelation().length; i++) {
			for (int j = 0; j < para.getRelation()[i].length; j++) {
				if (i == j || i > j )
					continue;
				Tree common = findSmallestCommonTree(input.deeperCopy(),
						"PROTEIN" + i, "PROTEIN" + j);
				Tree pt = convertToPathTree(common, "PROTEIN" + i, "PROTEIN"
						+ j);

				// print the tree, only for debug
				StringWriter sw = new StringWriter();
				treePrint.printTree(pt, new PrintWriter(sw, true));
				System.out.println(para.getRelation()[i][j]
						+ "\t"
						+ sw.toString().replaceAll("\\r\\n", "").replaceAll(
								"\\s+", " "));
				res.add(pt);
			}
		}
		output = res.toArray(new Tree[0]);
		System.out.println(para.getNumInterations());
		System.out.println(res.size());
		System.out.println();
	}

}
