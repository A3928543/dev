<%@ taglib uri="FlexTagLib" prefix="mm" %>
<html>
   <head>
      <title>Jerarquia</title>
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
          String ip = request.getRemoteAddr();
          String idUsuario = String.valueOf( (Integer) session.getAttribute("idUsuario") );
          String idPersona = String.valueOf( (Integer) session.getAttribute("idPersona") );          
          
          if (ticket != null) {
      %>
      <mm:mxml source="jerarquia.mxml">
         <mm:flashvar name="flTicket" value="<%=ticket%>" />
         <mm:flashvar name="flIp" value="<%=ip%>" />
         <mm:flashvar name="flIdUsuario" value="<%=idUsuario%>" />
         <mm:flashvar name="flIdPersonaSesion" value="<%=idPersona%>" />
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