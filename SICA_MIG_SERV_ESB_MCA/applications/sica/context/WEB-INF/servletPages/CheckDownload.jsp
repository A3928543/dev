<%-- 
    Document   : CheckDownload
    Created on : 1/08/2008, 11:05:25 AM
    Author     : TCS00L4
--%>
<%@page contentType="multipart/form-data" pageEncoding="UTF-8"%>
<%@ page import="java.math.*" %><%
  String result="1";
  String downLoadResource=request.getParameter("resource");  
  String status=(String)session.getAttribute(downLoadResource.substring(1,downLoadResource.length()));    
  
  if("ok".equals(status)){
      result="0";
      session.setAttribute(downLoadResource, "1");
  }
%><%=result%>

