package org.joy.analyzer.examples.aimed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joy.analyzer.Document;
import org.joy.analyzer.DocumentCreationException;
import org.joy.analyzer.Paragraph;

public class AIMEDDocument extends Document {
	private static final String TITLE_ANNOTATION = "TI -";
	private static final String ABSTRACT_ANNOTATION = "AB -",
			PARAGRAPH_ANNOTATION = "PG-";
	private static final String ADDRESS_ANNOTATION = "AD -";
	private static final double TITLE_WEIGHT = 1.0, ABSTRACT_WEIGHT = .8;
	private static final String PROT_TAG = "<prot>", PROTEND_TAG = "</prot>";
	private HashMap<String, Integer[]> entities = new HashMap<String, Integer[]>();

	private void parseEntities(String fullText) {
		Pattern p = Pattern.compile(PROT_TAG + "[^<]+" + PROTEND_TAG);
		fullText = fullText.toLowerCase();
		Matcher m = p.matcher(fullText);
		while (m.find()) {
			String e = m.group().replaceAll(
					"(" + PROT_TAG + ")|(" + PROTEND_TAG + ")", "").trim()
					.toLowerCase();
			if (entities.containsKey(e))
				continue;
			int index = fullText.indexOf(e);
			ArrayList<Integer> pos = new ArrayList<Integer>();
			while (index != -1) {
				pos.add(index);
				index = fullText.indexOf(e, index + 1);
			}
			entities.put(e, pos.toArray(new Integer[0]));
		}
		for (String e : entities.keySet()) {
			System.out.println(e + " " + Arrays.toString(entities.get(e)));
		}
	}

	@Override
	public void createFromInputStream(InputStream in, String URL)
			throws IOException, DocumentCreationException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = br.readLine();
		StringBuffer sb = new StringBuffer();
		int offset = 0;
		while (line != null) {
			// append the origin data
			sb.append(line);
			line = line.replaceAll(ADDRESS_ANNOTATION+" -\\s+.+", "");
			if (line.startsWith(TITLE_ANNOTATION)) {
				// add as title
				line = line.replaceAll(TITLE_ANNOTATION + "\\s+", "");
			} else if (line.startsWith(ABSTRACT_ANNOTATION)
					|| line.startsWith(PARAGRAPH_ANNOTATION)) {
				// mark the inAbs flag
				line = line.replaceAll(ABSTRACT_ANNOTATION + "|"
						+ PARAGRAPH_ANNOTATION + "\\s+", "");
			}
			line = line.replaceAll("<[^<]*>\\s+", "");
			// add to paragraph only if we are in abstract or title
			paragraphs.add(new Paragraph(line, 1.0 , offset));
			offset += line.length();
			line = br.readLine();
		}
		System.out.println(getContent());
		setData(sb.toString());
		setUrl(URL);
		parseEntities(sb.toString());
		br.close();
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
		return paragraphs.get(0).getText();
	}

	public static void main(String[] args) throws Exception, IOException,
			DocumentCreationException {
		File f = new File(args[0]);
		for (File t : f.listFiles()) {
			AIMEDDocument doc = new AIMEDDocument();
			doc.createFromInputStream(new FileInputStream(t), t.toURI()
					.toString());
			System.out.println();
		}
	}
}
