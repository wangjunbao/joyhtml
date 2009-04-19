package org.joy.analyzer.examples.aimed;

import java.util.ArrayList;

import org.joy.analyzer.Paragraph;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AimedParagraph extends Paragraph {
	private static final String PAIR_TAG = "pair";
	private static final String ENTITY_TAG = "entity";
	private boolean[][] relation;
	private ArrayList<Integer[]> nestList;
	public static int numpairs = 0;
	private int numInterations = 0;

	public ArrayList<Integer[]> getNestList() {
		return nestList;
	}
	
	public boolean isNested(int i) {
		for (Integer[] n : getNestList()) {
			for (Integer j : n) {
				if (i == j)
					return true;
			}
		}
		return false;
	}
	public AimedParagraph(String text, double weight, int offset,
			Element senTag, ArrayList<Integer[]> embededList) {
		super(text, weight, offset);
		this.nestList = embededList;
		NodeList children = senTag.getElementsByTagName(PAIR_TAG);
		int numEntities = senTag.getElementsByTagName(ENTITY_TAG).getLength();
		relation = new boolean[numEntities][numEntities];
		for (int i = 0; i < children.getLength(); i++) {
			Element e = (Element) children.item(i);
			if (e.getAttribute("interaction").equals("True")) {
				String e1 = e.getAttribute("e1");
				e1 = e1.substring(e1.length() - 1);

				String e2 = e.getAttribute("e2");
				e2 = e2.substring(e2.length() - 1);
				relation[Integer.parseInt(e1)][Integer.parseInt(e2)] = true;
				System.out.println(e1 + "-" + e2);
			}
		}
		numInterations = children.getLength();
		numpairs += children.getLength();
	}

	public boolean[][] getRelation() {
		return relation;
	}

	public int getNumInterations() {
		return numInterations;
	}
}
