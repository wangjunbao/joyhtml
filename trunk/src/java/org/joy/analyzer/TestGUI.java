/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joy.analyzer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joy.analyzer.html.Anchor;
import org.joy.analyzer.html.HTMLDocument;
import org.joy.analyzer.html.ParseException;


/**
 *
 * @author hd
 */

public class TestGUI extends Frame{

     TextField urlText;

     TextArea pargArea;
     TextArea urlArea;
    public static void main(String[] args)
    {
        new TestGUI().lunchFrame();
    }

    public void lunchFrame()
    {
        this.setLocation(200, 100);
        this.setTitle("Test");
       this.setPreferredSize(new Dimension(800,600));
        urlText=new TextField();
        
        pargArea=new TextArea();
        urlArea=new TextArea();
        this.add(urlText , BorderLayout.NORTH);
        this.add(pargArea , BorderLayout.CENTER);
        this.add(urlArea , BorderLayout.SOUTH);

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


    private String getContentText()
    {
         String s = "";
        try {

            InputStream in = null;
            in = new URL(urlText.getText()).openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
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

    class URLMonitor implements ActionListener
    {

        public void actionPerformed(ActionEvent e) {

        lunch();
    }

        private void lunch() {
            urlArea.setText("");
            pargArea.setText("");
             try {
                HTMLDocument doc = HTMLDocument.createHTMLDocument(urlText.getText(), getContentText());
                for (Anchor a : doc.getAnchors()) {
                    if (a != null) {
                        urlArea.setText(urlArea.getText()+a.getText() + "   =>   " + a.getURL()+"\n");
                    }

                }

                for (Paragraph p : doc.getParagraphs()) {
                        if (p != null) {
                            pargArea.setText(pargArea.getText()+p.getText() + "     -----" + p.getWeight()+"\n");
                        }
                    }
            } catch (ParseException ex) {
                Logger.getLogger(TestGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
