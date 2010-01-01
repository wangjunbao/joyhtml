package org.joy.analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public class DocumentFactory {
	private static Hashtable<String, Class<? extends Document>> mineTable = new Hashtable<String, Class<? extends Document>>();
	private static Hashtable<String, Class<? extends Document>> suffixTable = new Hashtable<String, Class<? extends Document>>();
	private static final String PLUGINS_PATH = "./plugins";

	/**
	 * 在指定的目录中搜寻文档模型插件并且导入到相应的table中
	 * @param path 要搜索的路径
	 */
	private static void searchPlugins(File path) {
		try {
			if (path.isDirectory()) {
				URI u = path.toURI();
				URLClassLoader loader = URLClassLoader
						.newInstance(new URL[] { u.toURL() });
				Enumeration<URL> urls = loader.findResources("feature.xml");
				while (urls.hasMoreElements()) {
					// TODO 分析xml文件，读取类
					Properties xmlProperties = new Properties();
					URLConnection conn = urls.nextElement().openConnection();
					xmlProperties.load(conn.getInputStream());
					String type = (String) xmlProperties.get("Type");
					if (type.equals("Doc")) {
						// 读取文档模型
						Class<? extends Document> c = (Class<? extends Document>) loader
								.loadClass((String) xmlProperties
										.get("ClassName"));
						// 注册相应的mine类型和suffix
						for(String s:c.newInstance().getSuffix().split(";")){
							suffixTable.put(s, c);							
						}
						mineTable.put(c.newInstance().getMineType(), c);
					}
				}
				//搜索子目录
				for(File f:path.listFiles()){
					searchPlugins(f);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static {
		searchPlugins(new File(PLUGINS_PATH));
	}

	/**
	 * 通过给定的URL创建文档
	 * 
	 * @param u
	 * @return
	 * @throws IOException
	 * @throws DocumentCreationException
	 */
	public static Document createDocumentFromURL(URL u) throws IOException,
			DocumentCreationException {
		try {
			if (u.getProtocol().toLowerCase().equals("http")) {
				// TODO: 尝试获取MIME 类型。
				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
				String mine = conn.getContentType().split(";")[0];
				// 获取Mime指定的文档对象模型
				if (mine == null ||!mineTable.containsKey(mine))
					throw new UnsupportedOperationException("不支持的文件格式！");
				Document doc = mineTable.get(mine).newInstance();
				// 创建文档
				doc.createFromInputStream(conn.getInputStream(), u.toString());
				return doc;
			} else if (u.getProtocol().toLowerCase().equals("file")) {
				int i = u.getFile().indexOf(".");
				if (i == -1) {
					throw new UnsupportedOperationException("不支持的文件格式！");
				}
				String suffix = u.getFile().substring(i);
				if (!suffixTable.containsKey(suffix))
					throw new UnsupportedOperationException("不支持的文件格式！");
				Document doc = suffixTable.get(suffix).newInstance();
				FileInputStream in = new FileInputStream(u.getFile());
				doc.createFromInputStream(in, u.toString());
				in.close();
				return doc;
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
