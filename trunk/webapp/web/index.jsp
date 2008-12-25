<%-- 
    Document   : index
    Created on : 2008-12-25, 15:29:44
    Author     : Lamfeeling
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.joy.analyzer.*" %>
<%@page import="org.joy.analyzer.html.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Web信息抽取示例</title>
    </head>
    <body>
        <center>
        <h2>请您输入需要抽取的Web页面URL:)</h2>
        <form action="index.jsp">
            <input type="text" size="40" name="url" value="http://news.sina.com.cn/c/2008-12-11/155516828944.shtml" />
            <input type="submit" value="提取" />
        </form>
        <%
        if (request.getParameter("url") != null) {
            String text = Utility.getWebContent(request.getParameter("url"));
            HTMLDocument doc = HTMLDocument.createHTMLDocument(request.getParameter("url"), text);
            for (Paragraph p : doc.getParagraphs()) {
        %>
        <p align="left" style=" font-size: <%=p.getWeight() * 20 + 10 + "pt"%>">
            <%
                if (p.getWeight() >= 0.3) {
                    out.print("<b>");
                }
            %>
            <%=p.getText()+"    <font color=red>权重："+p.getWeight()+"</font>"%>
            <%
                if (p.getWeight() >= 0.3) {
                    out.print("</b>");
                }
            %>
        </p>
        <%
            }
        }
        %>
        </center>
    </body>
</html>
