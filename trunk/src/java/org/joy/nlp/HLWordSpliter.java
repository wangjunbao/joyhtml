package org.joy.nlp;

class HLS {
    static {
	System.loadLibrary("HLS");
    }

    public native boolean HLSplitInit(String path);

    public native void HLFreeSplit();

    public native String HLS_ParagraphProcess(String content, int bPOSTagged);

}

/**
 * NOTE: HLWordSpliter is not available for linux
 * 
 * @author Administrator
 */
public class HLWordSpliter extends WordSpliter {

    private static HLS h = null;
    private final static Object waiter = new Object();

    public HLWordSpliter() {
	synchronized (waiter) {
	    if (h == null) {
		System.out.println("Initialising dicts...");
		h = new HLS();
		if (System.getenv("DIC_HOME") != null) {
		    h.HLSplitInit(System.getenv("DIC_HOME"));
		} else {
		    h.HLSplitInit("./dicts/");
		}
	    } else {
		System.err.println("尝试多次初始化分词词典！");
	    }
	}
    }

    public void close() {
	synchronized (waiter) {
	    if (h != null) {
		h.HLFreeSplit();
		h = null;
	    }
	}

    }

    public String[] split(String text) {
	return split(text, false).split(" ");
    }

    public String split(String text, boolean isTagged) {
	synchronized (waiter) {
	    if (h == null) {
		System.out.println("分词系统已经关闭！");
		return null;
	    } else {
		return h.HLS_ParagraphProcess(text, isTagged ? 1 : 0);
	    }
	}

    }

    public Word[] splitToWords(String text) {
	String[] s = split(text, true).replaceAll("\\s+/W", "").split(" ");
	Word[] words = new Word[s.length];
	for (int i = 0; i < s.length; i++) {
	    String wordText = s[i].split("/")[0];
	    String wordTag = s[i].split("/")[1];

	    words[i] = new Word(wordText, wordTag);
	}

	return words;
    }

    public static void main(String[] args) {
	HLWordSpliter h = new HLWordSpliter();
	System.out.println(h.split("圆明园兔首将被拍卖。", true));

	for (Word w : h.splitToWords("他从马上摔下来。")) {
	    System.out.print(w.getText() + "/" + w.getTag() + " ");
	}
	System.out.println("");
	new HLWordSpliter();

	System.out.println(h.split("我们在领导的帮助下，实现了这个功能。", true));
	h.close();
	System.out.println(h.split("刘挺拔出宝剑。", false));
    }
}
