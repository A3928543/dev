<%@ taglib uri="FlexTagLib" prefix="mm" %>
<!-- $Id: index.jsp,v 1.12 2008/04/21 23:42:30 favilaj Exp $ -->
<html>
   <head>
      <title>Sistema de Cambios.- Reversos</title>
      <script type="text/javascript" language="JavaScript">
		<!--
		function closeWindow() {
			window.close();
			void(0);
		}

        function verDeal(idDeal, interbancario) {
            var v = window.open('/sica/app?service=external/' + (interbancario ? 'DatosDealInterbancario' : 'DatosDeal') +
                        '&sp=' + idDeal, 'deal', 'width=800, height=600, resizable=yes, scrollbars=yes, status=yes');
            v.focus();
			void(0);
        }

		//-->
      </script>
   </head>
   <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
      <%
          try {
              String ticket = (String) session.getAttribute("ticket");
              int idPromotor = ((Integer) session.getAttribute("idPromotor")).intValue();
              int idUsuario = ((Integer) session.getAttribute("idUsuario")).intValue();
              boolean ixeDirecto = ((Boolean) session.getAttribute("ixeDirecto")).booleanValue();
              boolean guardia = ((Boolean) session.getAttribute("guardia")).booleanValue();
              double desvMontoLim = Double.valueOf((String) session.getAttribute("desvMontoLim")).
                      doubleValue();
              double desvPorcLim = Double.valueOf((String) session.getAttribute("desvPorcLim")).
                      doubleValue();
              double desvPorcMax = Double.valueOf((String) session.getAttribute("desvPorcMax")).
                      doubleValue();
              double desvFact1 = Double.valueOf((String) session.getAttribute("desvFact1")).
                      doubleValue();
              double desvFact2 = Double.valueOf((String) session.getAttribute("desvFact2")).
                      doubleValue();
              String facultad = (String) session.getAttribute("facultad");
              if (ticket != null) {
      %>

<mm:mxml>

	<views:Aplicacion xmlns:mx="http://www.macromedia.com/2003/mxml"
			xmlns:views="views.*"
			backgroundColor="#ffffff"
			marginTop="7" marginRight="7" marginLeft="7" marginBottom="7"
			creationComplete="inicializar('<%= ticket %>', <%= idPromotor %>, <%= idUsuario %>, <%= ixeDirecto %>, <%= guardia %>, '<%= facultad %>', <%= desvMontoLim %>, <%= desvPorcLim %>, <%= desvPorcMax %>, <%= desvFact1 %>, <%= desvFact2 %>)">

    <mx:Style source="resources/ixe.css"/>

    <mx:Script source="formatos.as" />

    <mx:RemoteObject id="reversosService" source="spring://reversosDelegate" concurrency="last" showBusyCursor="true">
        <mx:method name="findDeal" result="respFindDeal(event.result)" fault="mx.core.Application.application.falla(event.fault)" />
        <mx:method name="generarReverso" result="respGenerarReverso(event.result)" fault="mx.core.Application.application.falla(event.fault)" />
        <mx:method name="fechaLiquidacionParaFechaValor" result="respFechaLiquidacionParaFechaValor(event.result)" fault="mx.core.Application.application.falla(event.fault)" />
    </mx:RemoteObject>

    <mx:Effect>
        <mx:Fade name="fadeOut" alphaFrom="100" alphaTo="0" duration="500" />
        <mx:Fade name="fadeIn" alphaFrom="0" alphaTo="100" duration="500" />
    </mx:Effect>

	<mx:Effect>
        <mx:WipeLeft name="wipeLeft"/>
        <mx:WipeRight name="wipeRight"/>
    </mx:Effect>

	<mx:DateFormatter id="formateadorFecha" formatString="DD/MM/YYYY" />

	<mx:NumberFormatter id="formateadorMonto" thousandsSeparatorFrom="," thousandsSeparatorTo="," precision="2" />

    <mx:NumberFormatter id="formateadorTc" thousandsSeparatorFrom="," thousandsSeparatorTo="," precision="8" />

	<mx:Model id="observacionesModel">
        <obs>{observacionesTextArea.text}</obs>
    </mx:Model>

	<mx:StringValidator field="observacionesModel.obs" tooShortError="Las observaciones deben contener un minimo de 10 caracteres."
        tooLongError="El contenido de las observaciones debe ser m&#225;ximo de 512 caracteres." minLength="10" maxLength="512"
	 requiredFieldError="Debe introducir las observaciones para el reverso." />

    <mx:HBox width="100%">
        <mx:Image source="@Embed('resources/ixe.jpg')"/>
        <mx:Label text="SICA.- Reversos" fontSize="16" fontWeight="bold" />
        <mx:Spacer width="100%"/>
    	<mx:Label id="idDealLabel" text="N&#250;mero de deal que desea reversar:" fontWeight="bold" />
    	<mx:TextInput id="idDealTextField" width="80" textAlign="right" restrict="0-9" />
    	<mx:Button id="buscarButton" label="Buscar"
                click="findDeal()" />
    </mx:HBox>

    <mx:VBox id="panelDatosDeal" showEffect="fadeIn" hideEffect="fadeOut" width="100%" height="100%" visible="false">
	    <mx:Panel title="Datos del Deal:" width="100%" height="100%"
	    		marginTop="0" marginRight="0" marginLeft="0" marginBottom="0">
	    	<mx:VBox width="100%" height="100%" marginTop="5" marginRight="5" marginLeft="5" marginBottom="5">
				<mx:Grid width="100%">
					<mx:GridRow width="100%">
						<mx:GridItem horizontalAlign="right">
							<mx:Label text="Cliente:" fontWeight="bold" />
						</mx:GridItem>
						<mx:GridItem width="100%">
							<mx:Text id="clienteLabel" text="{deal.noCuenta + ' ' + deal.nombreCliente}" width="100%"
								className="dragLabel"
								mouseDown="dragIt(event, clienteLabel, 'clienteLabelFormat')"
								selectable="false"
								toolTip="Arrastre a la secci&#243;n de 'Motivos del Reverso' si desea modificar el cliente."/>
						</mx:GridItem>
						<mx:GridItem horizontalAlign="right">
							<mx:Label text="Utilidad:" fontWeight="bold" />
						</mx:GridItem>
						<mx:GridItem>
							<mx:Label text="{formateadorMonto.format(deal.utilidad)}" />
						</mx:GridItem>
						<mx:GridItem horizontalAlign="right">
							<mx:Label text="Fecha Valor:" fontWeight="bold" />
						</mx:GridItem>
						<mx:GridItem>
							<mx:Label id="fechaValorLabel" text="{deal.fechaValor}" className="dragLabel"
									toolTip="Arrastre a la secci&#243;n de 'Motivos del Reverso' si desea modificar la fecha valor."
									mouseDown="dragIt(event, clienteLabel, 'fechaValorLabelFormat')" />

						</mx:GridItem>
						<mx:GridItem horizontalAlign="right">
							<mx:Label text="Capturado:" fontWeight="bold" />
						</mx:GridItem>
						<mx:GridItem>
							<mx:Label text="{deal.fechaCaptura}" />
						</mx:GridItem>
			        </mx:GridRow>
					<mx:GridRow width="100%">
						<mx:GridItem horizontalAlign="right">
							<mx:Label text="Promotor:" fontWeight="bold" />
						</mx:GridItem>
						<mx:GridItem>
							<mx:Text text="{deal.nombrePromotor}" width="100%" />
						</mx:GridItem>
						<mx:GridItem horizontalAlign="right" width="60">
							<mx:Label text="Captur&#243;:" fontWeight="bold" />
						</mx:GridItem>
						<mx:GridItem>
							<mx:Text text="{deal.nombreUsuario}" width="100%" minWidth="100" />
						</mx:GridItem>
						<mx:GridItem horizontalAlign="right">
			                <mx:Label text="Status:" fontWeight="bold" />
			            </mx:GridItem>
			            <mx:GridItem>
			                <mx:Label text="{deal.descripcionStatus}" />
			            </mx:GridItem>
						<mx:GridItem horizontalAlign="right">
							<mx:Label text="Liquidaci&#243;n:" fontWeight="bold" />
						</mx:GridItem>
						<mx:GridItem>
							<mx:Label text="{deal.fechaLiquidacion}" />
						</mx:GridItem>
					</mx:GridRow>
				</mx:Grid>
		        <mx:DividedBox direction="horizontal" width="100%" height="100%">

	                <mx:DividedBox width="100%" height="100%">
	                    <mx:HBox width="100%" height="100%" minHeight="90">
	                        <mx:Label text="Rec.:" width="35" fontWeight="bold" />
	                        <mx:DataGrid id="recibimosDataGrid" width="100%" height="100%" dataProvider="{deal.detallesRecibimos}"
	                                    dragEnabled="true" change="seleccionarDetalle(event)" vScrollPolicy="on">
	                            <mx:columns>
	                                <mx:Array>
	                                    <mx:DataGridColumn columnName="folioDetalle" headerText="#" width="25" textAlign="right" />
	                                    <mx:DataGridColumn columnName="mnemonico" headerText="T. Liq." textAlign="center" />
	                                    <mx:DataGridColumn columnName="divisa.idDivisa" headerText="Div." width="45" textAlign="center" cellRenderer="BanderaCellRenderer" />
	                                    <mx:DataGridColumn columnName="descripcionStatus" headerText="Status" textAlign="center" />
	                                    <mx:DataGridColumn columnName="monto" headerText="Monto" labelFunction="formatearMonto"  textAlign="right" />
	                                <mx:DataGridColumn columnName="tipoCambioMesa" headerText="T.C.M." labelFunction="formatearTcm" width="100" textAlign="right" />
	                                <mx:DataGridColumn columnName="tipoCambio" headerText="T.C.C." labelFunction="formatearTcc" width="100" textAlign="right" />
	                                    <mx:DataGridColumn columnName="importe" headerText="Importe" labelFunction="formatearImporte" textAlign="right" />
	                                </mx:Array>
	                            </mx:columns>
	                        </mx:DataGrid>
	                    </mx:HBox>
	                    <mx:HBox width="100%" height="100%" minHeight="90">
	                        <mx:Label text="Ent.:" width="35" fontWeight="bold" />
	                        <mx:DataGrid id="entregamosDataGrid" width="100%" height="100%" dataProvider="{deal.detallesEntregamos}"
	                                    dragEnabled="true" change="seleccionarDetalle(event)" vScrollPolicy="on">
		                        <mx:columns>
		                            <mx:Array>
		                                <mx:DataGridColumn columnName="folioDetalle" headerText="#" width="25" textAlign="right" />
		                                <mx:DataGridColumn columnName="mnemonico" headerText="T. Liq." textAlign="center" />
		                                <mx:DataGridColumn columnName="divisa.idDivisa" headerText="Div." width="45" textAlign="center" cellRenderer="BanderaCellRenderer" />
		                                <mx:DataGridColumn columnName="descripcionStatus" headerText="Status" textAlign="center" />
		                                <mx:DataGridColumn columnName="monto" headerText="Monto" labelFunction="formatearMonto" textAlign="right" />
		                                <mx:DataGridColumn columnName="tipoCambioMesa" headerText="T.C.M." labelFunction="formatearTcm" width="100" textAlign="right" />
		                                <mx:DataGridColumn columnName="tipoCambio" headerText="T.C.C." labelFunction="formatearTcc" width="100" textAlign="right" />
		                                <mx:DataGridColumn columnName="importe" headerText="Importe" labelFunction="formatearImporte" textAlign="right" />
		                            </mx:Array>
		                        </mx:columns>
	                        </mx:DataGrid>
	                    </mx:HBox>
	                </mx:DividedBox>
	                <mx:VBox width="{recibimosDataGrid.selectedItem != null or entregamosDataGrid.selectedItem != null ? 200 : 0}"
                		height="100%" maxWidth="400" verticalGap="2" horizontalGap="2">
	                	<mx:Repeater id="infoAdicionalRepeater"
	                			dataProvider="{recibimosDataGrid.selectedItem != null ? recibimosDataGrid.selectedItem.datosAdicionales : entregamosDataGrid.selectedItem != null ? entregamosDataGrid.selectedItem.datosAdicionales : null}">
	                		<mx:Text text="{infoAdicionalRepeater.currentItem.nombre + ':'}" fontWeight="bold" width="100%" />
	                		<mx:Text text="{infoAdicionalRepeater.currentItem.valor}" width="100%" fontSize="10"  />
	                	</mx:Repeater>
	                </mx:VBox>
	            </mx:DividedBox>
	        </mx:VBox>
        </mx:Panel>
        <mx:Panel title="Motivos del reverso:" width="100%" height="200"
                status="Arrastre a esta secci&#243;n los elementos que desea modificar del deal original."
                marginTop="0" marginRight="0" marginLeft="0" marginBottom="0"
                dragEnter="doDragEnter(event,'clienteLabelFormat')"
                dragDrop="doDragDrop(event)">
            <mx:HBox width="100%" height="100%"
                            marginTop="5" marginRight="5" marginLeft="5" marginBottom="5">
                <mx:Grid width="100%">
                    <mx:GridRow width="100%">
                        <mx:GridItem>
                            <mx:CheckBox id="clienteCheckBox" label="Por Cliente:" click="reversarCliente()" />
                        </mx:GridItem>
                        <mx:GridItem>
                            <mx:Label text="{contratoNuevo != null ? contratoNuevo.noCuenta + ' ' + contratoNuevo.razonSocial : ''}" />
                        </mx:GridItem>
                    </mx:GridRow>
                    <mx:GridRow width="100%">
                        <mx:GridItem>
                            <mx:CheckBox id="fechaValorCheckBox" label="Por Fecha Valor:" click="marcarFechaValor();" />
                        </mx:GridItem>
                        <mx:GridItem>
                            <mx:ComboBox id="fechaValorComboBox" enabled="{fechaValorCheckBox.selected}" dataProvider="{fechasValor}" change="mostrarFechaLiquidacion()"/>
                            <mx:Label id="fechaLiquidacionLabel" />
                        </mx:GridItem>
                    </mx:GridRow>
                    <mx:GridRow width="100%">
                        <mx:GridItem>
                            <mx:CheckBox id="cancelacionCheckBox" click="marcarPorCancelacion()" label="Por Cancelaci&#243;n. " />
                        </mx:GridItem>
                    </mx:GridRow>
		    		<mx:GridRow width="100%">
                        <mx:GridItem>
                            <mx:Label text="Observaciones:" />
	                </mx:GridItem>
                        <mx:GridItem>
				<mx:TextArea id="observacionesTextArea" width="300"  height="40" maxChars="511" />
                        </mx:GridItem>
                    </mx:GridRow>
                </mx:Grid>
                <mx:VBox width="100%" height="100%">
                    <mx:Label text="Detalles a reversar:" fontWeight="bold" />
                    <mx:DataGrid id="motivosDataGrid" width="100%" height="100%" >
                        <mx:columns>
                            <mx:Array>
                                <mx:DataGridColumn columnName="folioDetalle" headerText="#" width="25" textAlign="right" />
                                <mx:DataGridColumn columnName="montoCambiado" headerText="Monto" textAlign="center" />
                                <mx:DataGridColumn columnName="tipoCambioCambiado" headerText="T.C.C." textAlign="center" />
                                <mx:DataGridColumn columnName="montoOriginal" headerText="De monto" textAlign="right" labelFunction="formatearMontoOriginal" />
                                <mx:DataGridColumn columnName="monto" headerText="A monto" textAlign="right" labelFunction="formatearMonto" />
                                <mx:DataGridColumn columnName="tipoCambioOriginal" headerText="De T.C.C." width="100" textAlign="right" labelFunction="formatearDeTcc"/>
                                <mx:DataGridColumn columnName="tipoCambio" headerText="A T.C.C." width="100" textAlign="right" labelFunction="formatearTcc" />
                            </mx:Array>
                        </mx:columns>
                    </mx:DataGrid>
                </mx:VBox>
            </mx:HBox>
            <mx:ControlBar>
                <mx:Button label="Ver deal completo" click="getURL('javascript:verDeal(' + deal.idDeal + ', ' + deal.interbancario + ')')" />
                <mx:Spacer width="100%" />
                <mx:Button label="Agregar Detalle" click="reversarDetalleDeal(recibimosDataGrid.selectedItem != null ? recibimosDataGrid.selectedItem : entregamosDataGrid.selectedItem)"
                           enabled="{(recibimosDataGrid.selectedItem != null and recibimosDataGrid.selectedItem.idDivisa != 'MXN') || (entregamosDataGrid.selectedItem != null and entregamosDataGrid.selectedItem.idDivisa != 'MXN')}" />
                <mx:Button label="Eliminar Motivo" click="eliminarDetalleReversado(motivosDataGrid.selectedIndex)"
                			enabled="{motivosDataGrid.selectedItem != null}" />
                <mx:Button label="Aceptar" click="generarReverso()" />
                <mx:Button label="Cancelar" click="mostrarOcultarPanelDeal(false); observacionesTextArea.text = ''; fechaLiquidacionLabel.text = '';" />
            </mx:ControlBar>
        </mx:Panel>
    </mx:VBox>
</views:Aplicacion>
</mm:mxml>
<%	    } else { %>
		    Acceso Denegado.
<%	    }
    }
    catch (Exception e) {
	e.printStackTrace();
 %>
       Error al crear la p&aacute;gina.
<%
    }
%>
</body>
</html>
