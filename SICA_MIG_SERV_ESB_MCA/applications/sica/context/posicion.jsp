<%@ taglib uri="FlexTagLib" prefix="mm" %>
<html>
   <head>
      <title>Posici&oacute;n</title>
      <script type="text/javascript" language="JavaScript">
      function openNewWindow(URLtoOpen, windowName, windowFeatures) {
         newWindow=window.open(URLtoOpen, windowName, windowFeatures);
         newWindow.focus();
         void(0);
      }
      </script>
   </head>
   <body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0">
      <%
          String ticket = (String) session.getAttribute("ticket");
          if (ticket != null) {
      %>
      <mm:mxml source="posicion/posicion.mxml">
         <mm:flashvar name="ticket" value="<%=ticket%>" />
      </mm:mxml>
      <%
          } else {
      %>
        Acceso denegado.
      <%
          }
      %>
   </body>
</html>