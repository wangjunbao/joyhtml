/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joy.analyzer.html;

/**
 * 分析异常类
 * @author Lamfeeling
 */
public class ParseException extends Exception{

    /**
     * 构造一个解析异常
     * @param msg 异常的文本描述
     */
    public ParseException(String msg) {
        super(msg);
    }

    /**
     * 构造一个默认的解析异常
     */
    public ParseException() {
    }

}
