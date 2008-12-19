/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joy.analyzer;

import org.joy.analyzer.html.HTMLDocument;

/**
 *
 * @author Lamfeeling
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO 在这里添加测试代码
        String str="<td><a href=\"branch_setting/branch_setting.aspx\" target=\"_self\"><img src=\"images/index/zjl_wyhp_11.gif\" alt=\"部门设置\" width=\"62\" height=\"25\" \" ";
     HTMLDocument.createHTMLDocument("http://www.suda.edu.cn", str);
    }

}
