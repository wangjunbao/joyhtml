<%-- 
    Document   : index
    Created on : 2008-12-25, 15:29:44
    Author     : Lamfeeling
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.joy.analyzer.*" %>
<%@page import="org.joy.nlp.*" %>
<%@page import="java.util.*" %>
<%@page import="org.joy.analyzer.html.*" %>
<%@page import="java.text.*" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Web信息抽取示例</title>
    </head>
    <body>
        <a href="http://joyhtml.googlecode.com">看看我们的项目主页吧:)</a>
        <center>
            <h2>请您输入需要抽取的<a href="QA.jsp"><font color="red">主题页面</font></a>URL:)</h2>
            <h3><a href="mailto:lamfeeling@126.com">联系我们:lamfeeling@126.com</a></h3>
            <form action="index.jsp">
                <input type="text" size="40" name="url" value="" />
                <input type="submit" value="提取" />
            </form>
            <br/>
            <p>
                例子            <br/>
                <li>
                    <a href="index.jsp?url=http%3A%2F%2Fnews.sina.com.cn%2Fc%2F2008-12-11%2F155516828944.shtml">
                        马英九称与大陆交往未感受到政治压力
                    </a>
                </li>
                <br/>
                <li>
                    <a href="index.jsp?url=http%3A%2F%2Fnews.sina.com.cn%2Fc%2F2008-12-25%2F113816919047.shtml">
                        石家庄市政府通报三鹿集团破产案情况
                    </a>
                </li>
                <br/>
                <li>
                    <a href="samples.jsp">
                        更多例子...
                    </a>
                </li>
                <br/>
            </p>
            <%
        if (request.getParameter("url") != null) {
            try {
                if (request.getParameter("voted") == null) {
            %>
            <span style="text-align:left"><h3>主题关键字：</h3></span>


            <%                    }
                    String text = Utility.getWebContent(request.getParameter("url"));

                    HTMLDocument doc = HTMLDocument.createHTMLDocument(request.getParameter("url"), text);
            %>
            <P STYLE="background-color:#FFFFCC;color:red">
                <%
                    ACWordSpliter s = new ACWordSpliter();
                    HitAnalyzer a = new HitAnalyzer(doc, s);
                    a.doAnalyze();
                    List<Hit> hits = a.getHits();
                    int count = 0;
                    for (Hit h : hits) {
                        if (count > 10) {
                            break;
                        }
                %>
                <span><b><%=h.getTerm() + ":" + new DecimalFormat("0.00").format(h.getScore())%></b></span>
                <%
                        count++;
                    }
                    s.close();
                %>
            </P>
            <hr/>
            <p>
                <em style="color:red">您认为我们正确提取了正文内容吗？</em>
                <a href="commit.jsp?vote=true&url=<%=request.getParameter("url")%>">是</a>
                <a href="commit.jsp?vote=false&url=<%=request.getParameter("url")%>">否</a>
            </p>
            <p align="left">
                <a href="<%=request.getParameter("url")%>" target="blank">
                    查看原网页
                </a>
            </p>
            <%
                    for (Paragraph p : doc.getParagraphs()) {
            %>
            <p align="left" style=" font-size: <%=p.getWeight() * 20 + 10 + "pt"%>">
                <%
                if (p.getWeight() >= 0.3) {
                    out.print("<b>");
                }
                %>
                <%=p.getText().replaceAll("\r\n", "<br/><br/>") + "    <font color=red>权重：" + new DecimalFormat("0.00").format(p.getWeight()) + "</font>"%>
                <%
                if (p.getWeight() >= 0.3) {
                    out.print("</b>");
                }
                %>
            </p>
            <%
                    }
                } catch (Exception e) {
            %>
            <h2>您的请求出错了，请检查您输入的是否是完整的URL（需要包括Http://的开头）:)</h2>
            <%            }
        }
            %>
        </center>
    </body>
</html>
