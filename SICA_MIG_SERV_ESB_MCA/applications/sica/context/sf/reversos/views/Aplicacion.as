/*
 * $Id: Aplicacion.as,v 1.8 2008/05/31 01:10:13 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2008 LegoSoft S.C.
 */

import com.ixe.ods.sica.reversos.vo.*;
import mx.controls.*;
import mx.core.*;
import mx.managers.*;

/**
 * Controlador principal de la aplicaci&oacute;n de reversos del SICA.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.8 $ $Date: 2008/05/31 01:10:13 $
 */
class views.Aplicacion extends Application
{
    /**
     * El ticket de la sesi&oacute;n del usuario.
     */
   	var ticket : String ;

    /**
     * El n&uacute;mero de persona del promotor que captura el reverso.
     */
    var idPromotor : Number;

    /**
     * El n&uacute;mero de usuario que captura el reverso.
     */
    var idUsuario : Number;

    /**
     * True si el usuario est&aacute; conectado al canal de ixe directo.
     */
    var ixeDirecto : Boolean;

    /**
     * True si el usuario es el promotor de guardia.
     */
    var guardia : Boolean;

    /**
     * La facultad con la que se conecta el usuario al m&oacute;dulo de captura de reversos.
     */
    var facultad : String;

    var desvMontoLim : Number;

    var desvPorcLim : Number;

    var desvPorcMax : Number;

    var desvFact1 : Number;

    var desvFact2 : Number;

    /**
     * El deal que va a ser reversado.
     */
    private var deal : Object;

    /**
     * La lista de fechas valor seleccionables.
     */
    private var fechasValor : Array;

    /**
     * El ContratoVO cuando el deal es por cliente.
     */
    private var contratoNuevo : Object;

    /**
     * La fecha valor que debi&oacute; tener el deal originalmente.
     */
    private var fechaValorNueva : String;

    /**
     * La lista de detalles modificados en el reverso.
     */
    private var detallesModificados : Array;

	/**
	 * Llamado en el evento <code>creationComplete</code> para inicializa los arreglos
	 * detallesRecibimos y detallesEntregamos como arreglos vac&iacute;os.
	 */
	private function inicializar(ticket : String, idPromotor : Number, idUsuario : Number,
                                 ixeDirecto : Boolean, guardia : Boolean, facultad : String,
                                 desvMontoLim : Number, desvPorcLim : Number, desvPorcMax : Number,
                                 desvFact1 : Number, desvFact2 : Number) : Void
	{
		this.ticket = ticket;
		this.idPromotor = idPromotor;
		this.idUsuario = idUsuario;
		this.ixeDirecto = ixeDirecto;
		this.guardia = guardia;
		this.facultad = facultad;
        this.desvMontoLim = desvMontoLim;
        this.desvPorcLim = desvPorcLim;
        this.desvPorcMax = desvPorcMax;
        this.desvFact1 = desvFact1;
        this.desvFact2 = desvFact2;
        fechasValor = ['CASH', 'TOM ', 'SPOT', '72HR', 'VFUT'];
	}

	private function mostrarOcultarPanelDeal(mostrar) : Void
	{
		this['panelDatosDeal'].visible = mostrar;
		this['idDealLabel'].enabled = !mostrar;
		this['idDealTextField'].enabled = !mostrar;
		this['buscarButton'].enabled = 	!mostrar;
		if (!mostrar) {
			this['idDealTextField'].text = '';
		}
	}

    /**
     * Ejecuta el servicio findDeal() del Remote Object reversosService. La respuesta es recibida
     * en respFindDeal().
     */
    private function findDeal() : Void
    {
        var idDeal : String = this['idDealTextField'].text;
        if (isNaN(idDeal) || Number(idDeal) < 1) {
            mx.controls.Alert.show('El n\u00famero de deal debe ser un n\u00famero mayor que cero.',
                    'Atenci\u00f3n');
		}
		else {
			this['reversosService'].findDeal(ticket, Number(idDeal), ixeDirecto, guardia, facultad,
                    idUsuario);
		}
	}

	/**
	 * Asigna el deal recibido como par&aacute;metro y limpia las condiciones del reverso.
	 *
	 * @param deal El deal encontrado.
	 */
	private function respFindDeal(deal : Object) : Void
	{
		this.deal = deal;
		contratoNuevo = null;
		this['clienteCheckBox'].selected = false;
		this['fechaValorCheckBox'].selected = false;
		this['cancelacionCheckBox'].selected = false;
		detallesModificados = new Array();
		mostrarOcultarPanelDeal(true);
		this['motivosDataGrid'].dataProvider = new Array();
	}

    /**
     * Inicia el evento de drag and drop para la etiqueta de cliente y la etiqueta de fecha valor.
     *
     * @param event El evento de drag and drop.
     * @param text El label de cliente o fecha valor.
     * @param format clienteLabelFormat o fechaValorLabelFormat.
     */
    private function dragIt(event, text, format):Void
	{
		var ds:DragSource = new DragSource();
		ds.addData(text,format);
		DragManager.doDrag(event.target,ds,null);
	}

    /**
     * Acepta el manejo del evento de drag and drop cuando el usuario arrastra el cliente, fecha
     * valor o detalle que desea reversar, sobre la secci&oacute;n de motivos del reverso.
     *
     * @param event El evento de drag and drop.
     * @param format El tipo de evento (clienteLabelFormat, fechaValorClienteFormat o items.
     */
    private function doDragEnter(event, format) : Void
	{
		if (event.dragSource.formats[0] == 'items') {
			var detalleDeal = event.dragSource.dataForFormat(event.dragSource.formats[0])[0];
			if (detalleDeal.idDivisa == 'MXN') {
				event.handled = false;
				return;
			}
        }
		event.handled = true;
	}

    /**
     * Llamado cuando el usuario hace el 'drop' de la operaci&oacute;n de drag and drop.
     * Llama a <code>reversarDetalleDeal()</code>, <code>reversarCliente()</code>,
     * seg&uacute;n corresponda.
     *
     * @param event El evento de drag and drop.
     */
    private function doDragDrop(event)
	{
		if (event.dragSource.formats[0] == 'items') {
			var detalleDeal = event.dragSource.dataForFormat(event.dragSource.formats[0])[0];
			reversarDetalleDeal(detalleDeal);
        }
		else if ('clienteLabelFormat' == event.dragSource.formats[0]) {
			this['clienteCheckBox'].selected = true;
			reversarCliente();
		}
		else if ('fechaValorLabelFormat' == event.dragSource.formats[0]) {
			this['fechaValorCheckBox'].selected = true;
			this['cancelacionCheckBox'].selected = false;
		}
	}

	/**
	 * Recibido cuando ocurre el evento <code>change</code> en
	 * <code>recibimosDataGrid</code> o <code>entregamosDataGrid</code>, para limpiar la
	 * selecci&oacute;n del dataGrid contrario de detalles de deal.
	 *
	 * @param evento El evento change.
	 */
	private function seleccionarDetalle(evento) : Void
	{
		if (evento.target.selectedItem != null) {
			if (evento.target == this['recibimosDataGrid']) {
				this['entregamosDataGrid'].selectedIndex = -1;
			}
			else {
				this['recibimosDataGrid'].selectedIndex = -1;
			}
		}
	}

    /**
     * Si el checkbox de cliente est&aacute; marcado, muestra el popup para seleccionar el cliente
     * correcto.
     */
    private function reversarCliente() : Void
	{
		if (this['clienteCheckBox'].selected) {
            var popup;
            if (deal.interbancario) {
				popup = PopUpManager.createPopUp(_root, ContrapartesWindow, true,
				    {deferred:true});
			}
			else {
				popup = PopUpManager.createPopUp(_root, ClienteWindow, true,
				    {deferred:true});
			}
            popup.centerPopUp(this);
			this['cancelacionCheckBox'].selected = false;
		}
		else {
			contratoNuevo = null;
		}
	}

    /**
     * Si el checkBox de fecha valor est&aacute; marcado, entonces desmarca el checkBox de
     * cancelaci&oacute;n.
     */
    private function marcarFechaValor() : Void
	{
		if (this['fechaValorCheckBox'].selected) {
			this['cancelacionCheckBox'].selected = false;
			mostrarFechaLiquidacion();
		}
		else {
			this['fechaLiquidacionLabel'].text = '';
		}
	}

    /**
     * Llama al servicio fechaLiquidacionParaFechaValor() de reversosService.
     */
    private function mostrarFechaLiquidacion() : Void
	{
		this['reversosService'].fechaLiquidacionParaFechaValor(deal.idDeal,
			this['fechaValorComboBox'].selectedItem);
	}

    /**
     * Muestra en la interfaz la fecha de liquidaci&#243;n para el deal de correcci&#243;n.
     *
     * @param fecha La fecha de liquidaci&#243;n para el deal de correcci&#243;n.
     */
    private function respFechaLiquidacionParaFechaValor(fecha) : Void
	{
		this['fechaLiquidacionLabel'].text = 'Liquidaci\u00f3n: ' + fecha;
	}

    /**
     * Desmarca el checkbox de cancelaci&oacute;n y muestra el popup para capturar un nuevo detalle
     * de reverso.
     *
     * @param detalle El detalle de deal que se desea reversar.
     */
    private function reversarDetalleDeal(detalle) : Void
	{
		this['cancelacionCheckBox'].selected = false;
		var popup = PopUpManager.createPopUp(_root, DetalleWindow, false,
			{deferred:true, detalleDeal:detalle, interbancario:deal.interbancario});
		popup.centerPopUp(this);
	}

    /**
     * Agrega al dataGrid <code>motivosDataGrid</code> el detalle de reverso. Si &eacute;ste ya
     * exist&iacute;a con anterioridad reemplaza el original con el detalle que llega como
     * par&aacute;metro.
     *
     * @param detalle El detalle de reverso que desea agregarse a la lista.
     */
    private function agregarDetalleReversado(detalle : DetalleReversoVO) : Void
	{
		var indiceDetalleExistente : Number = -1;

		// Se revisa si el detalle ya existe en la lista de detalles modificados:
		for (var i : Number = 0; i < detallesModificados.length; i++) {
			if (detalle.folioDetalle == detallesModificados[i].folioDetalle) {
				indiceDetalleExistente = i;
			}
		}
		if (indiceDetalleExistente >= 0) {
			detallesModificados.splice(indiceDetalleExistente, 1, detalle);
		}
		else {
			detallesModificados.push(detalle);
		}
		this['motivosDataGrid'].dataProvider = detallesModificados;
	}

    /**
     * Elimina el detalle de reverso seleccionado en el datagrid <code>motivosDataGrid</code>
     *
     * @param indiceDetalleReversado El &iacute;ndice del detalle que desea eliminarse (el
     *  seleccionado).
     */
    private function eliminarDetalleReversado(indiceDetalleReversado : Number) : Void
	{
		detallesModificados.splice(indiceDetalleReversado, 1);
		this['motivosDataGrid'].dataProvider = detallesModificados;
	}

    /**
     * Llamado cuando el checkbox de cancelaci&oacute;n es seleccionado por el usuario. Si
     * est&aacute; marcado con una paloma, limpia el arreglo de detallesModificados, contratoNuevo,
     * fechaValorNueva y desmarca los checkbox de 'Por Cliente' y 'Por Fecha Valor', para que el
     * &uacute;nico motivo de reverso sea por Cancelaci&oacute;n.
     */
    private function marcarPorCancelacion() : Void
	{
		if (this['cancelacionCheckBox'].selected) {
			detallesModificados = new Array();
			contratoNuevo = null;
			fechaValorNueva = null;
			this['clienteCheckBox'].selected = false;
			this['fechaValorCheckBox'].selected = false;
			this['motivosDataGrid'].dataProvider = detallesModificados;
		}
	}

    /**
     * Graba el registro de reverso en la base de datos, el reverso deber&aacute; ser autorizado
     * por la mesa de cambios.
     */
    private function generarReverso() : Void
	{
        if (validarMotivosReverso()) {
            var noCuenta : String = contratoNuevo != null ? contratoNuevo.noCuenta : null;
            var fechaValor : String = this['fechaValorCheckBox'].selected
                ? this['fechaValorComboBox'].selectedItem : null;
            var observaciones : String = this['observacionesTextArea'].text != ''
                ? this['observacionesTextArea'].text : null;

                this['reversosService'].generarReverso(ticket, Number(this['idDealTextField'].text),
                        noCuenta, fechaValor, this['cancelacionCheckBox'].selected,
                        detallesModificados, observaciones, idUsuario);
        }
	}

    /**
     * Limpia el texto de observaciones y oculta el panel principal.
     *
     * @param resultado Ignorado.
     */
    private function respGenerarReverso(resultado : Object) : Void
	{
        this['observacionesTextArea'].text = '';
		this['fechaLiquidacionLabel'].text = '';
		mostrarOcultarPanelDeal(false);
        Alert.show('El reverso fue registrado, se encuentra ahora en proceso de autorizaci\u00f3n.',
                'Atenci\u00f3n:');
	}

	/**
	 * Muestra un Alert panel con el mensaje de error.
	 */
	private function falla(causa : Object) : Void
	{
		Alert.show(causa.description, 'Atenci\u00f3n:');
	}

    /**
     * Revisa que los motivos del reverso sean consistentes.
     */
    private function validarMotivosReverso() : Boolean
	{
		if (!this['clienteCheckBox'].selected && !this['fechaValorCheckBox'].selected &&
			!this['cancelacionCheckBox'].selected && detallesModificados.length < 1) {
			Alert.show('Debe definir el motivo de generar el reverso.', 'Atenci\u00f3n:');
			return false;
		}
		if (this['observacionesTextArea'].text == '') {
			Alert.show('Es necesario definir las observaciones para el reverso.', 'Atenci\u00f3n:');
			return false;
		}
		return true;
	}
}