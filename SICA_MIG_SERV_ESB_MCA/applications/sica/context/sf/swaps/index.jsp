<%@ taglib uri="FlexTagLib" prefix="mm" %>
<!-- $Id: index.jsp,v 1.10 2010/04/13 15:33:02 ccovian Exp $ -->
<html>
   <head>
      <title>Sistema de Cambios.- Swaps</title>
      <script type="text/javascript" language="JavaScript">
		<!--
		function closeWindow() {
			window.close();
			void(0);
		}
		//-->
      </script>
   </head>
   <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
      <%
          try {
              String ticket = (String) session.getAttribute("ticket");
              String idCanal = request.getParameter("ic");
              int idMesaCambio = Integer.valueOf(request.getParameter("imc")).intValue();
              int idUsuario = Integer.valueOf(request.getParameter("iu")).intValue();
              String valFut = request.getParameter("valFut");
              if (ticket != null && idMesaCambio > 0) {
      %>
<mm:mxml>
	<views:Swaps xmlns:mx="http://www.macromedia.com/2003/mxml" xmlns:views="views.*" width="100%" height="100%"
                 marginTop="3" marginBottom="3" marginLeft="3" marginRight="3"
                 creationComplete="inicializar('<%= idCanal %>', <%= idMesaCambio %>, <%= idUsuario %>)">

	    <mx:Style source="resources/ixe.css" />

		<mx:Script>
		<![CDATA[
			var ticket : String;
		]]>
		</mx:Script>

	    <mx:RemoteObject id="swapsService" source="spring://swapsDelegate" concurrency="single"
	                     showBusyCursor="true">
	        <mx:method name="findContrapartes" result="respFindContrapartes(event.result)" fault="falla(event.fault)" />
			<mx:method name="getFechasValor" result="capSwap.fechasValorRecibidas(event.result)"
                       fault="falla(event.fault)" />
	        <mx:method name="findAllDivisas" result="respFindAllDivisas(event.result)" fault="falla(event.fault)" />
	        <mx:method name="findProductos" result="respFindProductos(event.result)" fault="falla(event.fault)" />
	        <mx:method name="insertarSwap" result="capSwap.swapInsertado(event.result)" fault="falla(event.fault)" />
		    <mx:method name="findIdsSectoresUltimaFechaValor" result="respFindIdsSectoresUltimaFechaValor(event.result)" fault="falla(event.fault)" />
	    </mx:RemoteObject>

        <mx:HBox width="100%" >
            <mx:Image source="@Embed('resources/ixe.jpg')"/>
            <mx:Label text="SICA.- Captura de Swaps de Contado" fontSize="16" fontWeight="bold"/>
            <mx:Spacer width="100%"/>
        </mx:HBox>

	    <mx:HBox id="principal" width="100%"
	    		height="100%" marginBottom="10" marginLeft="10" marginRight="10">
	        <mx:Accordion id="acordion" width="100%" height="100%">
				<views:TablaContrapartes contrapartes="{contrapartesVisibles}"
	                                     contraparteSeleccionada="crearSwap(event.eventPropCont)"
	                                     letraSeleccionada="filtrarContrapartes(event.letra)" />
				<views:CapturaSwap id="capSwap" swapsService="{swapsService}"
                                   idCanal="{idCanal}" idMesaCambio="{idMesaCambio}" idUsuario="{idUsuario}"
                                   contraparte="{contraparte}" divisas="{divisas}" productos="{productos}" width="100%"
                                   height="100%" valFut="<%= valFut %>" />
	        </mx:Accordion>
		<!--mx:ControlBar>
			<mx:HBox width="100%" horizontalAlign="center">
				<mx:Button label="Limpiar todo" />
				<mx:Button label="Cerrar" click="getURL('javascript:closeWindow();')" />
			</mx:HBox>
		</mx:ControlBar-->
	    </mx:HBox>
	</views:Swaps>
</mm:mxml>
<%	    } else { %>
		    Acceso Denegado.
<%	    }
    }
    catch (Exception e) {
 %>
       Error al crear la p&aacute;gina.
<%
    }
%>
</body>
</html>
