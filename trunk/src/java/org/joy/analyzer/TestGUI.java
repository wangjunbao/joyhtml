/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.joy.analyzer.html.Anchor;
import org.joy.analyzer.html.HTMLDocument;
import org.joy.analyzer.html.ParseException;

/**
 *
 * @author hd
 */
public class TestGUI extends Frame {

    TextField urlText;
    JTextPane textArea;
    TextArea urlArea;

    public static void main(String[] args) {
        new TestGUI().launchFrame();
    }

    public void launchFrame() {
        this.setLocation(200, 100);
        this.setTitle("Test");
        this.setPreferredSize(new Dimension(800, 600));
        urlText = new TextField();

        textArea = new JTextPane();
        urlArea = new TextArea();
        JScrollPane jsp = new JScrollPane(textArea);

        this.add(urlText, BorderLayout.NORTH);
        this.add(jsp, BorderLayout.CENTER);
        this.add(urlArea, BorderLayout.SOUTH);
        //textArea.setLineWrap(true);
        urlText.addActionListener(new URLMonitor());
        this.pack();
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    }

    public void insert(String str, AttributeSet attrSet) {
        javax.swing.text.Document doc = textArea.getDocument();
        try {
            doc.insertString(doc.getLength(), str, attrSet);
        } catch (BadLocationException e) {
            System.out.println("BadLocationException: " + e);
        }
    }

    public void setDocs(String str, Color col, boolean bold, int fontSize) {
        SimpleAttributeSet attrSet = new SimpleAttributeSet();
        StyleConstants.setForeground(attrSet, col);
        //颜色
        if (bold == true) {
            StyleConstants.setBold(attrSet, true);
        }//字体类型
        StyleConstants.setFontSize(attrSet, fontSize);
        //字体大小
        insert(str, attrSet);
    }

    private String getContentText() {
        String s = "";
        try {
            InputStream in = null;
            HttpURLConnection conn = (HttpURLConnection) new URL(urlText.getText()).openConnection();
            String contentType = conn.getHeaderField("Content-Type").toLowerCase();

            String encoding = "utf-8";
            boolean charsetFound = false;
            if (contentType.contains("charset")) {
                encoding = contentType.split("charset=")[1];
                charsetFound = true;
            }
            if (!charsetFound) {
                in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
                String line = br.readLine();
                StringBuffer sb = new StringBuffer();
                while (line != null) {
                    sb.append(line);
                    line = br.readLine();
                }
                br = new BufferedReader(new StringReader(sb.toString().replaceAll(">\n*", ">\n")));
                line = br.readLine();
                while (line != null && !line.toLowerCase().matches(".*<meta.*content=.*")) {
                    line = br.readLine();
                }
                encoding = line.toLowerCase().split("charset=")[1].replaceAll("[^a-z|1-9]", " ").split("\\s+")[0];
                charsetFound = true;
                br.close();
            }
            conn = (HttpURLConnection) new URL(urlText.getText()).openConnection();
            in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, encoding));
            String line = br.readLine();
            StringBuffer sb = new StringBuffer();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            br.close();
            s = sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(TestGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    class URLMonitor implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            launch();
        }

        private void launch() {
            urlArea.setText("");
            textArea.setText("");
            try {
                HTMLDocument doc = HTMLDocument.createHTMLDocument(urlText.getText(), getContentText());
                for (Anchor a : doc.getAnchors()) {
                    if (a != null) {
                        urlArea.setText(urlArea.getText() + a.getText() + "   =>   " + a.getURL() + "\n");
                    }

                }
                for (Paragraph p : doc.getParagraphs()) {
                    if (p != null) {
                        setDocs(p.getText() + "     -----" + p.getWeight() + "\n", Color.red, false, (int) (p.getWeight() * 30 + 10));
                    }
                }
            } catch (ParseException ex) {
                Logger.getLogger(TestGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
