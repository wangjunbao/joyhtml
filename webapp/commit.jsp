<%-- 
    Document   : commit
    Created on : 2008-12-25, 19:03:25
    Author     : Lamfeeling
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.io.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <%
        FileWriter w = new FileWriter(config.getServletContext().getRealPath("/webE/log.txt"), true);
        w.write(request.getParameter("url") + "\t" +
                request.getParameter("vote") + "\n");
        w.close();
        response.sendRedirect("index.jsp?url=" + request.getParameter("url")+"&voted=true");
        %>
    </body>
</html>
