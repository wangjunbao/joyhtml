package org.joy.analyzer.examples.aimed;

import java.util.ArrayList;

import org.joy.analyzer.Analyzer;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.Pair;

/**
 * 过滤器分析器，把语法树优化
 * 
 * @author Andy
 * 
 */
public class FilterAnalyzer extends Analyzer<Tree[], Pair<Boolean, Tree>[]> {

	private void replaceProtein(Tree root, String name, String newName) {
		if (root.label().value().contains(name)) {
			root.label().setValue(newName);
		} else {
			Tree[] children = root.children();
			for (int i = 0; i < children.length; i++) {
				replaceProtein(children[i], name, newName);
			}
		}
	}

	private void filter(Tree t, String labelRegx) {
		boolean removed = false;
		do {
			Tree[] children = t.children();
			removed = false;
			for (int i = 0; i < children.length; i++) {
				if (children[i].value().matches(labelRegx)) {
					t.removeChild(i);
					removed = true;
					break;
				}
			}
		} while (removed);
		Tree[] children = t.children();
		for (int i = 0; i < children.length; i++) {
			filter(children[i], labelRegx);
		}
	}

	@Override
	public void doAnalyze() {
		AimedParagraph para = (AimedParagraph) this.para;
		if (para.getRelation().length < 2) {
			output = null;
			return;
		}

		ArrayList<Pair<Boolean, Tree>> res = new ArrayList<Pair<Boolean, Tree>>();
		int k = 0;
		for (int i = 0; i < para.getRelation().length; i++) {
			for (int j = 0; j < para.getRelation()[i].length; j++) {
				if (i == j || i > j )
					continue;
				// 把配对蛋白质名称替换成E1,E2
				replaceProtein(input[k], "PROTEIN" + i, "E1");
				replaceProtein(input[k], "PROTEIN" + j, "E2");
				// filter(input[k], ".*-.+-.*");
				res.add(new Pair<Boolean, Tree>(para.getRelation()[i][j],
						input[k]));
				k++;
			}
		}
		output = res.toArray(new Pair[0]);
	}

}
