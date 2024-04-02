<%@ taglib uri="FlexTagLib" prefix="mm" %>
<!-- $Id: index.jsp,v 1.29.2.5.8.1 2010/10/08 00:59:46 ccovian Exp $ -->
<html>
<head>
    <title>Autorizaciones.- Sistema de cambios</title>
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
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Boolean mc = (Boolean) session.getAttribute("SICA_AUT_MESA_CAMB");
        Boolean lc = (Boolean) session.getAttribute("SICA_AUT_LINCRED_EXC");
        Boolean dc = (Boolean) session.getAttribute("SICA_AREADOC");
        Boolean rv = (Boolean) session.getAttribute("SICA_AUT_REV");
        boolean porMonto = mc != null && mc.booleanValue();
        boolean porHorario = mc != null && mc.booleanValue();
        boolean porDesviacion = mc != null && mc.booleanValue();
        boolean porSobreprecio = mc != null && mc.booleanValue();
        boolean porCancelacion = mc != null && mc.booleanValue();
        boolean porCancelacionDet = mc != null && mc.booleanValue();
        boolean porModificacionMontoDet = mc != null && mc.booleanValue();
        boolean porModificacionProductoDet = mc != null && mc.booleanValue();
        boolean porReverso = mc != null && mc.booleanValue();
        boolean porDocumentacion = dc != null && dc.booleanValue();
        boolean porPlantilla = dc != null && dc.booleanValue();
        boolean porRfc = dc != null && dc.booleanValue();
        boolean porLimOper = dc != null && dc.booleanValue();
        boolean porEmail = dc !=null && dc.booleanValue();
        boolean porRechazoDoc = dc != null && dc.booleanValue();
        boolean porPagoAnticipado = lc != null && lc.booleanValue();
        boolean porDocumentacionInt = dc != null && dc.booleanValue();
        boolean porLineaOpInt = lc != null && lc.booleanValue();
        boolean porPagoAnticipadoInt = lc != null && lc.booleanValue();
        boolean porRechazoDocInt = dc != null && dc.booleanValue();
        boolean porConfirmarInt = dc != null && dc.booleanValue();
        boolean porAltaEmail = dc != null && dc.booleanValue();
        if (ticket != null) {
%>
<mm:mxml>
<views:Autorizaciones xmlns:mx="http://www.macromedia.com/2003/mxml" xmlns:views="views.*" xmlns:rev="*" width="100%" height="100%" creationComplete="inicializar()" marginTop="5" marginBottom="10" marginLeft="10" marginRight="10">

    <mx:Style source="resources/ixe.css"/>

    <mx:RemoteObject id="autorizacionesService" source="spring://autorizacionesDelegate" concurrency="last" showBusyCursor="true">
        <mx:method name="getWorkitems" result="llenarArreglos(event.result)" fault="falla(event.fault)" />
        <mx:method name="completarActividad" result="respCompletarActividad(event.result)" fault="falla(event.fault)" />
        <mx:method name="completarActividadParaModifProd" result="respCompletarActividad(event.result)" fault="falla(event.fault)" />
        <mx:method name="modificarTcmParaDealDetalle" result="respModificarTcm()" fault="falla(event.fault)" />
        <mx:method name="getTotales" result="respGetTotales(event.result)" fault="falla(event.fault)" />
        <mx:method name="confirmarDealsInterbancarios" result="respCompletarActividad(event.result)" fault="falla(event.fault)" />
        <mx:method name="completarAltaAntEmail" result="respCompletarActividad(event.result)" fault="falla(event.fault)" />
    </mx:RemoteObject>

    <mx:Script>
        <![CDATA[
        import mx.core.Application;
        import mx.controls.*;

        private function inicializar() : Void
        {
            ticket = '<%= ticket %>';
            idUsuario = <%= idUsuario %>;
            seleccionarCeja = true;
            findActividades(acordion.selectedChild.nombreActividad);
            reversos = new Array();
        }

        function respGetTotales(lista : Array) : Void
        {
            lista = lista;
<%          if (porHorario) { %>
            cnPorHorario.label = 'Deals detenidos por horario: (' + getTotalPara(cnPorHorario.nombreActividad, lista) + ')';
            cnPorHorario.enabled = getTotalPara(cnPorHorario.nombreActividad, lista) > 0;
<%          }
            if (porMonto) { %>
            cnPorMonto.label = 'Deals detenidos por monto: (' + getTotalPara(cnPorMonto.nombreActividad, lista) + ')';
            cnPorMonto.enabled = getTotalPara(cnPorMonto.nombreActividad, lista) > 0;
<%          }
            if (porDesviacion) { %>
            cnPorDesviacion.label = 'Deals de sucursales con desviaci\u00f3n al tipo de cambio al cliente: (' + getTotalPara(cnPorDesviacion.nombreActividad, lista) + ')';
            cnPorDesviacion.enabled = getTotalPara(cnPorDesviacion.nombreActividad, lista) > 0;
<%          }
            if (porSobreprecio) { %>
            cnPorSobreprecio.label = 'Deals con solicitud de autorizaci\u00f3n de sobreprecio: (' + getTotalPara(cnPorSobreprecio.nombreActividad, lista) + ')';
            cnPorSobreprecio.enabled = getTotalPara(cnPorSobreprecio.nombreActividad, lista) > 0;
<%          }
            if (porCancelacion) { %>
            cnPorCancelacion.label = 'Solicitudes de cancelaci\u00f3n de deals: (' + getTotalPara(cnPorCancelacion.nombreActividad, lista) + ')';
            cnPorCancelacion.enabled = getTotalPara(cnPorCancelacion.nombreActividad, lista) > 0;
<%          }
            if (porCancelacionDet) { %>
            cnPorCancelacionDet.label = 'Solicitudes de cancelaci\u00f3n de detalles de deals: (' + getTotalPara(cnPorCancelacionDet.nombreActividad, lista) + ')';
            cnPorCancelacionDet.enabled = getTotalPara(cnPorCancelacionDet.nombreActividad, lista) > 0;
<%          }
            if (porModificacionMontoDet) { %>
            cnPorModificacionMontoDet.label = 'Solicitudes de modificaci\u00f3n de monto de detalles de deals: (' + getTotalPara(cnPorModificacionMontoDet.nombreActividad, lista) + ')';
            cnPorModificacionMontoDet.enabled = getTotalPara(cnPorModificacionMontoDet.nombreActividad, lista) > 0;
<%          }
            if (porModificacionProductoDet) { %>
            cnPorModificacionProductoDet.label = 'Solicitudes de modificaci\u00f3n de producto de detalles de deals: (' + getTotalPara(cnPorModificacionProductoDet.nombreActividad, lista) + ')';
            cnPorModificacionProductoDet.enabled = getTotalPara(cnPorModificacionProductoDet.nombreActividad, lista) > 0;
<%          }
			if (porLimOper) { %>
			cnPorLimOper.label = 'Notificaciones de l\u00edmites de operaci\u00f3n: (' + getTotalPara(cnPorLimOper.nombreActividad, lista) + ')';
			cnPorLimOper.enabled = getTotalPara(cnPorLimOper.nombreActividad, lista) > 0;
<%			}
            if (porReverso) { %>
			cnPorReverso.label = 'Reversos: ('  + getTotalPara(cnPorReverso.nombreActividad, lista) + ')';
			cnPorReverso.enabled = getTotalPara(cnPorReverso.nombreActividad, lista) > 0;
<%			}
            if (porDocumentacion) { %>
            cnPorDocumentacion.label = 'Deals detenidos por falta de documentaci\u00f3n: (' + getTotalPara(cnPorDocumentacion.nombreActividad, lista) + ')';
            cnPorDocumentacion.enabled = getTotalPara(cnPorDocumentacion.nombreActividad, lista) > 0;
<%          }
            if (porPlantilla) { %>
        		cnPorPlantilla.label = 'Deals detenidos por plantilla pendiente de activaci\u00f3n: (' + getTotalPara(cnPorPlantilla.nombreActividad, lista) + ')';
        		cnPorPlantilla.enabled = getTotalPara(cnPorPlantilla.nombreActividad, lista) > 0;
<%          }
            if (porRfc) { %>
            cnPorRfc.label = 'Deals detenidos por cambio en R.F.C.: (' + getTotalPara(cnPorRfc.nombreActividad, lista) + ')';
            cnPorRfc.enabled = getTotalPara(cnPorRfc.nombreActividad, lista) > 0;
<%          }
            if (porEmail) { %>
            cnPorEmail.label = 'Deals detenidos por cambio en Correo Electr\u00f3nico: (' + getTotalPara(cnPorEmail.nombreActividad, lista) + ')';
            cnPorEmail.enabled = getTotalPara(cnPorEmail.nombreActividad, lista) > 0;
<%          }
            if (porRechazoDoc) { %>
            cnPorRechazoDoc.label = 'Deals rechazados por la mesa de control: (' + getTotalPara(cnPorRechazoDoc.nombreActividad, lista) + ')';
            cnPorRechazoDoc.enabled = getTotalPara(cnPorRechazoDoc.nombreActividad, lista) > 0;
<%          }
            if (porAltaEmail) { %>
            cnPorAltaEmail.label = 'Alta anticipada de correos electr\u00f3nicos: (' + getTotalPara(cnPorAltaEmail.nombreActividad, lista) + ')';
            cnPorAltaEmail.enabled = getTotalPara(cnPorAltaEmail.nombreActividad, lista) > 0;
<%          }
            if (porPagoAnticipado) { %>
            cnPorPagoAnticipado.label = 'Deals detenidos por pago anticipado: (' + getTotalPara(cnPorPagoAnticipado.nombreActividad, lista) + ')';
            cnPorPagoAnticipado.enabled = getTotalPara(cnPorPagoAnticipado.nombreActividad, lista) > 0;
<%          }
            if (porDocumentacionInt) { %>
            cnPorDocumentacionInt.label = 'Deals Interb. detenidos por falta de documentaci\u00f3n: (' + getTotalPara(cnPorDocumentacionInt.nombreActividad, lista) + ')';
            cnPorDocumentacionInt.enabled = getTotalPara(cnPorDocumentacionInt.nombreActividad, lista) > 0;
<%          }
            if (porLineaOpInt) { %>
            cnPorLineaOpInt.label = 'Deals Interb. detenidos por l\u00ednea de operaci\u00f3n: (' + getTotalPara(cnPorLineaOpInt.nombreActividad, lista) + ')';
            cnPorLineaOpInt.enabled = getTotalPara(cnPorLineaOpInt.nombreActividad, lista) > 0;
<%          }
            if (porPagoAnticipadoInt) { %>
            cnPorPagoAnticipadoInt.label = 'Deals Interb. detenidos por pago anticipado: (' + getTotalPara(cnPorPagoAnticipadoInt.nombreActividad, lista) + ')';
            cnPorPagoAnticipadoInt.enabled = getTotalPara(cnPorPagoAnticipadoInt.nombreActividad, lista) > 0;
<%          }
            if (porRechazoDocInt) { %>
            cnPorRechazoDocInt.label = 'Deals Interb. rechazados por la Mesa de Control: (' + getTotalPara(cnPorRechazoDocInt.nombreActividad, lista) + ')';
            cnPorRechazoDocInt.enabled = getTotalPara(cnPorRechazoDocInt.nombreActividad, lista) > 0;
<%          }
            if (porConfirmarInt) { %>
            cnPorConfirmarInt.label = 'Deals Interb. por confirmar: (' + getTotalPara(cnPorConfirmarInt.nombreActividad, lista) + ')';
            cnPorConfirmarInt.enabled = getTotalPara(cnPorConfirmarInt.nombreActividad, lista) > 0;
<%          } %>
        }

        private function llenarArreglos(lista : Array) : Void
        {
            var indice : Number = 0;
            porHorario = new Array();
            porMonto = new Array();
            porDesviacion = new Array();
            porSobreprecio = new Array();
            porCancelacion = new Array();
            porCancelacionDet = new Array();
            porModificacionMontoDet = new Array();
            porModificacionProductoDet = new Array();
            porLimOper = new Array();
            porReverso = new Array();
            porDocumentacion = new Array();
            porPlantilla = new Array();
            porRfc = new Array();
            porEmail = new Array();
            porRechazoDoc = new Array();
            porAltaEmail = new Array();
            porPagoAnticipado = new Array();
            porDocumentacionInt = new Array();
            porLineaOpInt = new Array();
            porPagoAnticipadoInt = new Array();
            porRechazoDocInt = new Array();
            porConfirmarInt = new Array();

            for (i = 0; i < acordion.numChildren; i++) {
                acordion.getHeaderAt(i).addEventListener('click', this);
            }

            for (var i : Number = 0; i < lista.length; i++) {
                var nomAct : String = lista[i].actividad;
                if (nomAct.indexOf('DN Horario') >= 0) {
                   porHorario[porHorario.length] = lista[i];
                }
                else if (nomAct.indexOf('DN Monto') >= 0) {
                   porMonto[porMonto.length] = lista[i];
                }
                else if (nomAct.indexOf('DN Desv. T.C.C. Suc.') >= 0) {
                    porDesviacion[porDesviacion.length] = lista[i];
                }
                else if (nomAct.indexOf('DN Sobreprecio') >= 0) {
                    porSobreprecio[porSobreprecio.length] = lista[i];
                }
                else if (nomAct.indexOf('DNCA Esp. Aut. Mesa.') >= 0) {
                    porCancelacion[porCancelacion.length] = lista[i];
                }
                else if (nomAct.indexOf('DN Canc. Det.') >= 0) {
                    porCancelacionDet[porCancelacionDet.length] = lista[i];
                }
                else if (nomAct.indexOf('DN Mod Monto') >= 0) {
                    porModificacionMontoDet[porModificacionMontoDet.length] = lista[i];
                }
                else if (nomAct.indexOf('DN Mod Producto') >= 0) {
                    porModificacionProductoDet[porModificacionProductoDet.length] = lista[i];
                }
                else if (nomAct.indexOf('DN Lim Oper.') >= 0) {
                	porLimOper[porLimOper.length] = lista[i];
                }
                else if (nomAct.indexOf('Reversos') >= 0) {
                    porReverso[porReverso.length] = lista[i];
                }
                else if (nomAct.indexOf('DN Documento') >= 0) {
                    porDocumentacion[porDocumentacion.length] = lista[i];
                }
                else if (nomAct.indexOf('DN Plantilla') >= 0) {
                    porPlantilla[porPlantilla.length] = lista[i];
                }
                else if (nomAct.indexOf('DN RFC') >= 0) {
                    porRfc[porRfc.length] = lista[i];
                }
                else if (nomAct.indexOf('DN EMAIL') >= 0) {
                    porEmail[porEmail.length] = lista[i];
                }
                else if (nomAct.indexOf('DN Rec. M. Ctrl') >= 0) {
                    porRechazoDoc[porRechazoDoc.length] = lista[i];
                }
                else if (nomAct.indexOf('Alta Email') >= 0) {
                    porAltaEmail[porAltaEmail.length] = lista[i];
                }
                else if (nomAct.indexOf('DN Pag. Antic.') >= 0) {
                    porPagoAnticipado[porPagoAnticipado.length] = lista[i];
                }
                else if (nomAct.indexOf('DInt Documento') >= 0) {
                    porDocumentacionInt[porDocumentacionInt.length] = lista[i];
                }
                else if (nomAct.indexOf('DInt Lin. Oper.') >= 0) {
                    porLineaOpInt[porLineaOpInt.length] = lista[i];
                }
                else if (nomAct.indexOf('DInt Lin. Cred.') >= 0) {
                    porPagoAnticipadoInt[porPagoAnticipadoInt.length] = lista[i];
                }
                else if (nomAct.indexOf('DInt Rec. M. Ctrl.') >= 0) {
                    porRechazoDocInt[porRechazoDocInt.length] = lista[i];
                }
                else if (nomAct.indexOf('Confirmaciones') >= 0) {
                    porConfirmarInt[porConfirmarInt.length] = lista[i];
                }
            }
            seleccionarCeja = acordion.selectedChild.tabla.dataProvider.length == 0;
            selectedCount = 0;
            
			<% if (porConfirmarInt) { %>
		    	cnPorConfirmarInt.contactoTextField.text = "";
		    <% } %>
    
            if (seleccionarCeja) {
                acordion.selectedChild =
<%          if (porHorario) { %>
                    porHorario.length > 0 ? cnPorHorario :
<%          }
            if (porMonto) { %>
                    porMonto.length > 0 ? cnPorMonto :
<%          }
            if (porDesviacion) { %>
                    porDesviacion.length > 0 ? cnPorDesviacion :
<%          }
            if (porSobreprecio) { %>
                    porSobreprecio.length > 0 ? cnPorSobreprecio :
<%          }
            if (porCancelacion) { %>
                    porCancelacion.length > 0 ? cnPorCancelacion :
<%          }
            if (porCancelacionDet) { %>
                    porCancelacionDet.length > 0 ? cnPorCancelacionDet :
<%          }
            if (porModificacionMontoDet) { %>
                    porModificacionMontoDet.length > 0 ? cnPorModificacionMontoDet :
<%          }
			if (porModificacionProductoDet) { %>
                    porModificacionProductoDet.length > 0 ? cnPorModificacionProductoDet :
<%          }
			if (porLimOper) { %>
					porLimOper.length > 0 ? cnPorLimOper :
<%			}
            if (porReverso) { %>
                    porReverso.length > 0 ? cnPorReverso :
<%          }
            if (porDocumentacion) { %>
                    porDocumentacion.length > 0 ? cnPorDocumentacion :
<%          }
            if (porPlantilla) { %>
            		  porPlantilla.length > 0 ? cnPorPlantilla :
<%          }
            if (porRfc) { %>
                    porRfc.length > 0 ? cnPorRfc :
<%          }
            if (porEmail) { %>
                    porEmail.length > 0 ? cnPorEmail :
<%          }
            if (porRechazoDoc) { %>
                    porRechazoDoc.length > 0 ? cnPorRechazoDoc :
<%          }
            if (porAltaEmail) { %>
                    porAltaEmail.length > 0 ? cnPorAltaEmail :
<%          }
            if (porPagoAnticipado) { %>
                    porPagoAnticipado.length > 0 ? cnPorPagoAnticipado :
<%          }
            if (porDocumentacionInt) { %>
                    porDocumentacionInt.length > 0 ? cnPorDocumentacionInt :
<%          }
            if (porRechazoDocInt) { %>
                    porRechazoDocInt.length > 0 ? cnPorRechazoDocInt :
<%          }
            if (porConfirmarInt) { %>
                    porConfirmarInt.length > 0 ? cnPorConfirmarInt :
<%          }
            if (porLineaOpInt) { %>
                    porLineaOpInt.length > 0 ? cnPorLineaOpInt :
<%          }
            if (porPagoAnticipadoInt) { %>
                    porPagoAnticipadoInt.length > 0 ? cnPorPagoAnticipadoInt :
<%          } %>
                    null;
            }
		    autorizacionesService.getTotales(<%=dc%>);
	}

        function respCompletarActividad(lista : Array) : Void
        {
            Application.application.enabled = true;
            llenarArreglos(lista);
        }

         ]]>
    </mx:Script>
    <mx:HBox width="100%">
        <mx:Image source="@Embed('resources/ixe.jpg')"/>
        <mx:Label text="SICA.- Autorizaciones" fontSize="16" fontWeight="bold" />
        <mx:Spacer width="100%" />
        <mx:Link label="Refrescar" click="findActividades(acordion.selectedChild.nombreActividad)" icon="@Embed('resources/sync.gif')" />
    </mx:HBox>

    <mx:Accordion id="acordion" width="100%" height="100%">
<%          if (porHorario) { %>
        <views:TablaHorarioMonto id="cnPorHorario" nombreActividad="DN Horario" width="100%" height="100%" workitems="{porHorario}" autorizacionesService="{autorizacionesService}" icono="resources/horario.jpg" />
<%          }
            if (porMonto) { %>
        <views:TablaHorarioMonto id="cnPorMonto" nombreActividad="DN Monto" width="100%" height="100%" workitems="{porMonto}" autorizacionesService="{autorizacionesService}" icono="resources/monto.jpg" />
<%          }
            if (porDesviacion) { %>
        <views:TablaNormal id="cnPorDesviacion" nombreActividad="DN Desv. T.C.C. Suc." width="100%" height="100%" workitems="{porDesviacion}" autorizacionesService="{autorizacionesService}" icono="resources/desviacion_tcc.jpg" />
<%          }
            if (porSobreprecio) { %>
        <views:TablaSobreprecio id="cnPorSobreprecio" nombreActividad="DN Sobreprecio" width="100%" height="100%" workitems="{porSobreprecio}" autorizacionesService="{autorizacionesService}" soloConfirmar="{true}" icono="resources/desviacion_tcc.jpg" />
<%          }
            if (porCancelacion) { %>
        <views:TablaNormal id="cnPorCancelacion" nombreActividad="DNCA Esp. Aut. Mesa." width="100%" height="100%" workitems="{porCancelacion}" autorizacionesService="{autorizacionesService}" icono="resources/cancelacion.jpg" />
<%          }
            if (porCancelacionDet) { %>
        <views:TablaCancelDetDeal id="cnPorCancelacionDet" nombreActividad="DN Canc. Det." width="100%" height="100%" workitems="{porCancelacionDet}" autorizacionesService="{autorizacionesService}" icono="resources/cancelacion.jpg" />
<%          }
            if (porModificacionMontoDet) { %>
        <views:TablaModMontoDetDeal id="cnPorModificacionMontoDet" nombreActividad="DN Mod Monto" width="100%" height="100%" workitems="{porModificacionMontoDet}" autorizacionesService="{autorizacionesService}" icono="resources/monto.jpg" />
<%          }
			if (porModificacionProductoDet) { %>
        <views:TablaModProductoDetDeal id="cnPorModificacionProductoDet" nombreActividad="DN Mod Producto" width="100%" height="100%" workitems="{porModificacionProductoDet}" autorizacionesService="{autorizacionesService}" icono="resources/desviacion_tcc.jpg" />
<%          }
			if (porLimOper) { %>
		<views:TablaNotifLimOper id="cnPorLimOper" nombreActividad="DN Lim Oper." width="100%" height="100%" workitems="{porLimOper}" autorizacionesService="{autorizacionesService}" icono="resources/desviacion_tcc.jpg" />
<%			}
            if (porDocumentacion) { %>
        <views:TablaNormal id="cnPorDocumentacion" nombreActividad="DN Documento" width="100%" height="100%" workitems="{porDocumentacion}" autorizacionesService="{autorizacionesService}" icono="resources/documentacion.jpg" />
<%          }
            if (porPlantilla) { %>
        <views:TablaPlantilla id="cnPorPlantilla" nombreActividad="DN Plantilla" width="100%" height="100%" workitems="{porPlantilla}" autorizacionesService="{autorizacionesService}" icono="resources/documentacion.jpg" />
<%          }
            if (porRfc) { %>
        <views:TablaRfc id="cnPorRfc" nombreActividad="DN RFC" width="100%" height="100%" workitems="{porRfc}" autorizacionesService="{autorizacionesService}" icono="resources/documentacion.jpg" />
<%          }
            if (porEmail) { %>
        <views:TablaEmail id="cnPorEmail" nombreActividad="DN EMAIL" width="100%" height="100%" workitems="{porEmail}" autorizacionesService="{autorizacionesService}" icono="resources/documentacion.jpg" />
<%          }
            if (porRechazoDoc) { %>
        <views:TablaNormal id="cnPorRechazoDoc" nombreActividad="DN Rec. M. Ctrl." width="100%" height="100%" workitems="{porRechazoDoc}" autorizacionesService="{autorizacionesService}" noRechazar="{true}" icono="resources/rechazado_doc.jpg" />
<%          }
            if (porPagoAnticipado) { %>
        <views:TablaNormal id="cnPorPagoAnticipado" nombreActividad="DN Pag. Antic." width="100%" height="100%" workitems="{porPagoAnticipado}" autorizacionesService="{autorizacionesService}" icono="resources/pago_anticipado.jpg" />
<%          }
            if (porDocumentacionInt) { %>
        <views:TablaNormal id="cnPorDocumentacionInt" nombreActividad="DInt Documento" width="100%" height="100%" workitems="{porDocumentacionInt}" autorizacionesService="{autorizacionesService}" icono="resources/documentacion_int.jpg" />
<%          }
            if (porRechazoDocInt) { %>
        <views:TablaNormal id="cnPorRechazoDocInt" nombreActividad="DInt Rec. M. Ctrl." width="100%" height="100%" workitems="{porRechazoDocInt}" autorizacionesService="{autorizacionesService}"  noRechazar="{true}" icono="resources/rechazado_doc_int.jpg" />
<%          }
            if (porConfirmarInt) { %>
                <views:TablaConfirmaciones id="cnPorConfirmarInt" nombreActividad="Confirmaciones" width="100%" height="100%" workitems="{porConfirmarInt}" autorizacionesService="{autorizacionesService}"  noRechazar="{true}" icono="resources/documentacion_int.jpg" />
<%          }
            if (porAltaEmail) { %>
        <views:TablaAltaEmail id="cnPorAltaEmail" nombreActividad="Alta Email" width="100%" height="100%" workitems="{porAltaEmail}" autorizacionesService="{autorizacionesService}" noRechazar="{true}" icono="resources/rechazado_doc.jpg" />
<%          }
            if (porLineaOpInt) { %>
        <views:TablaNormal id="cnPorLineaOpInt" nombreActividad="DInt Lin. Oper." width="100%" height="100%" workitems="{porLineaOpInt}" autorizacionesService="{autorizacionesService}" icono="resources/linea_operacion.jpg" />
<%          }
            if (porPagoAnticipadoInt) { %>
        <views:TablaNormal id="cnPorPagoAnticipadoInt" nombreActividad="DInt Lin. Cred." width="100%" height="100%" workitems="{porPagoAnticipadoInt}" autorizacionesService="{autorizacionesService}"  icono="resources/pago_anticipado.jpg" />
<%          }
            if (porReverso) { %>
        <rev:TablaReversos id="cnPorReverso" nombreActividad="Reversos" reversos="{porReverso}" width="100%" height="100%" />
<%          } %>
        </mx:Accordion>

</views:Autorizaciones>

</mm:mxml>
<%      }
        else { %>
            Acceso Denegado.
<%      }
    }
    catch (Exception e) {
%>
        Error al crear la p&aacute;gina.
<%
	} %>
</body>
</html>
