/*
 * $Id: CapSwap.as,v 1.13 2010/04/13 15:26:18 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010  LegoSoft S.C.
 */

import mx.controls.*;
import mx.formatters.*;
import mx.managers.*;
import mx.validators.*;
import com.ixe.ods.sica.posicion.vo.*;
import com.ixe.ods.sica.vo.*;

/**
 * Pantalla de captura de datos para los deals que conforman un Swap.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.13 $ $Date: 2010/04/13 15:26:18 $
 */
class views.CapSwap extends mx.containers.VBox
{
    /**
     * Llama a crearFechasValor() para inicializar el contenido del combo de fechas valor. En modo
     * contado, manda llamar a definirDeals() para sincronizar el dataGrid con los modelos de deals.
     */
    private function inicializar() : Void
    {
        numberBase = new NumberBase('.', ',', '.', ',');
        definirDeals();
        swapsService.getFechasValor();
        fechas = new Array();
    }

    /**
     * Inicializa de nuevo los campos para permitir al usuario capturar otro swap.
     */
    public function capturarOtro() : Void
    {
        contraparte = null;
        this['folioSwapTextField'].text = "0";
        this['montoTextField'].text = "0.0";
        this['tipoCambioTextField'].text = "0.0";
        this['puntosTextField'].text = "0.0";
        this['tipoCambioFinTextField'].text = "0.0";
        this['multipleCheckBox'].selected = false;
        definirDeals();
        sincronizar();
    }

    /**
     * Regresa la diferencia en d&#237;as entre la fecha anterior y la fecha posterior.
     */
    private function getDiasDiferencia(posterior : Date, anterior : Date) : Number
    {
        var fecha : Date = new Date();
        var i : Number;

        anterior.setHours(0);
        anterior.setMinutes(0);
        anterior.setSeconds(0);
        anterior.setMilliseconds(0);
        posterior.setHours(0);
        posterior.setMinutes(0);
        posterior.setSeconds(0);
        posterior.setMilliseconds(0);
        fecha.setTime(anterior.getTime());
        for (i = 0; fecha < posterior; i++) {
            fecha.setDate(fecha.getDate() + 1);
        }
        return i;
    }

    /**
     * Recibe la respuesta de la llamada al servicio getFechasValor() de swapsService. Llena en la
     * propiedad diasDiferencia, en el primer elemento de la lista, la diferencia en d&iacute;as de
     * CASH a TOM y en el segundo la diferencia entre TOM y SPOT.
     *
     * @param lista El arreglo de fechas correspondiente a CASH, TOM y SPOT.
     */
    public function fechasValorRecibidas(lista : Array) : Void
    {
        fechas = lista;
        diasDiferencia = new Array();
        diasDiferencia[0] = getDiasDiferencia(lista[1], lista[0]);
        diasDiferencia[1] = getDiasDiferencia(lista[2], lista[1]);
        diasDiferencia[2] = getDiasDiferencia(lista[3], lista[2]);
        if (lista.length == 5) {
            diasDiferencia[3] = getDiasDiferencia(lista[4], lista[3]);
        }
    }

    /**
     * Inicializa el arreglo de deals. Asigna el primer elemento al modelo llamado dealInicio.
     */
    private function definirDeals() : Void
    {
        deals = new Array();
        deals[0] = this['dealInicio'];
        if (! this['multipleCheckBox'].selected) {
            deals[1] = this['dealFin'];
        }
    }

    /**
     * Regresa un arreglo de fechas valor, de acuerdo al modo m&#250;ltiple o derivados o simple.
     * Este arreglo se asigna al combo box de fechas valor, si el Broker puede operar Valor Futuro,
     * el arreglo agrega 96HR al arreglo, si no, solo toma en cuenta hasta 72HR.
     *
     * @return Array.
     */
    private function crearFechasValor() : Array
    {
        if (this['multipleCheckBox'].selected) {
            if (valFut && operaVFut) {
                return [{inicio:CASH, fin:'', descripcion:CASH},
                    {inicio:TOM, fin:'', descripcion:TOM},
                    {inicio:SPOT, fin:'', descripcion:SPOT},
                    {inicio:HR72, fin:'', descripcion:HR72},
                    {inicio:HR96, fin:'', descripcion:HR96}];
            }
            else {
                return [{inicio:CASH, fin:'', descripcion:CASH},
                    {inicio:TOM, fin:'', descripcion:TOM},
                    {inicio:SPOT, fin:'', descripcion:SPOT},
                    {inicio:HR72, fin:'', descripcion:HR72}];
            }
        }
        else {
            if (valFut && operaVFut) {
                return [{inicio:CASH, fin:TOM, descripcion:'CASH / TOM'},
                    {inicio:CASH, fin:SPOT, descripcion:'CASH / SPOT'},
                    {inicio:CASH, fin:HR72, descripcion:'CASH / 72HR'},
                    {inicio:CASH, fin:HR96, descripcion:'CASH / 96HR'},
                    {inicio:TOM, fin:SPOT, descripcion:'TOM / SPOT'},
                    {inicio:TOM, fin:HR72, descripcion:'TOM / 72HR'},
                    {inicio:TOM, fin:HR96, descripcion:'TOM / 96HR'},
                    {inicio:SPOT, fin:HR72, descripcion:'SPOT / 72HR'},
                    {inicio:SPOT, fin:HR96, descripcion:'SPOT / 96HR'},
                    {inicio:HR72, fin:HR96, descripcion:'72HR / 96HR'}
                ];
            }
            else {
                return [{inicio:CASH, fin:TOM, descripcion:'CASH / TOM'},
                    {inicio:CASH, fin:SPOT, descripcion:'CASH / SPOT'},
                    {inicio:CASH, fin:HR72, descripcion:'CASH / 72HR'},
                    {inicio:TOM, fin:SPOT, descripcion:'TOM / SPOT'},
                    {inicio:TOM, fin:HR72, descripcion:'TOM / 72HR'},
                    {inicio:SPOT, fin:HR72, descripcion:'SPOT / 72HR'}
                ];
            }
        }
    }

    /**
     * El checkbox de modo m&#250;ltiple llama a este m&#233;todo para hacer visible o no la
     * secci&#243;n de datosContraparte y de botonesMultiple. Llama a definirDeals().
     */
    private function multipleSimple() : Void
    {
        fechasValor = crearFechasValor();
        this['datosContraparte'].visible = ! this['multipleCheckBox'].selected;
        this['botonesMultiple'].visible = this['multipleCheckBox'].selected;
        definirDeals();
    }

    /**
     * Llamado cuando el usuario define el tipo de operaci&#243;n (compra o venta), para asignar el
     * tipo de operaci&#243;n de los deals listados en el dataGrid. Llama a sincronizar().
     */
    private function cambiarTipoOperacion() : Void
    {
        if (this['multipleCheckBox'].selected) {
            var to : String = this['operacionRadio'].selectedData;
            for (var i : Number = 1; i < deals.length; i++) {
                deals[i].tipoOperacion = to == COMPRA ? VENTA : to == VENTA ? COMPRA : '';
            }
        }
        sincronizar();
    }

    /**
     * Llamado cuando el usuario cambia el estado del checkbox de puntos swap, para calcular el tipo
     * de cambio fin.
     */
    private function cambiarPuntosSwap() : Void
    {
        try {
            var sonPuntos : Boolean = this['puntosCheckBox'].selected;
            this['puntosTextField'].enabled = sonPuntos;
            this['tipoCambioFinLabel'].enabled = !sonPuntos;
            this['tipoCambioFinTextField'].enabled = !sonPuntos;
            if (! sonPuntos) {
                this['puntosTextField'].text = '0';
            }
        }
        catch (err : Error) {
            mx.controls.Alert.show(String(err), 'Atenci\u00f3n:');
        }
    }

    /**
     * Llama al servicio findProductos() de swapsService.
     */
    private function findProductos() : Void
    {
        swapsService.findProductos(idMesaCambio, idCanal,
                this['divisasComboBox'].selectedItem.idDivisa);
    }

    /**
     * Asigna los tipos de cambio fin de acuerdo a las fechas valor seleccionadas.
     */
    private function calcularTipoCambioFin() : Void
    {
        try {
            if (deals.length > 0) {
                if (this['puntosCheckBox'].selected) {
                    var pts : Number = Number(this['puntosTextField'].text) != Number.NaN ?
                                       Number(this['puntosTextField'].text) : 0;
                    if (this['multipleCheckBox'].selected) {
                        for (var i : Number = 1; i < deals.length; i++) {
                            deals[i].tipoCambio = Number(deals[0].tipoCambio) +
                                                  (getDiferencia(deals[0].fechaValor,
                                                          deals[i].fechaValor) * pts);
                        }
                    }
                    else {
                        var ini : String = this['fechasValorComboBox'].selectedItem.inicio;
                        var fin : String = this['fechasValorComboBox'].selectedItem.fin;
                        this['tipoCambioFinTextField'].text = Number(deals[0].tipoCambio) +
                                                              (pts * getDiferencia(ini, fin));
                    }
                }
            }
        }
        catch (err : Error) {
            mx.controls.Alert.show(String(err), 'Atenci\u00f3n:');
        }
    }

    /**
     * Regresa la fecha para una fechaValor determinada.
     *
     * @param fechaValor CASH, TOM, SPOT o 72HR.
     * @return Date.
     */
    private function getFecha(fechaValor : String) : Date
    {
        if (fechas != null) {
            if (fechaValor == CASH) {
                return fechas[0];
            }
            else if (fechaValor == TOM) {
                return fechas[1];
            }
            else if (fechaValor == SPOT) {
                return fechas[2];
            }
            else if (fechaValor == HR72) {
                return fechas[3];
            }
            else {
                return fechas[4];
            }
        }
        return null;
    }

    /**
     * Sincroniza los datos de los modelos con el dataGrid.
     */
    private function sincronizar() : Void
    {
        calcularTipoCambioFin();
        var balance : Number = deals[0].balance;
        for (var i : Number = 0; i < deals.length; i++) {
            if (i > 0) {
                balance -= deals[i].monto;
                deals[i].balance = balance;
            }
            deals[i].fechaLiquidacion = getFecha(deals[i].fechaValor);
        }
        this['dataGrid'].dataProvider = deals;
    }

    /**
     * Abre el popup ComplementoWindow para permitir al usuario capturar el deal final en un swap de
     * tipo m&#250;ltiple.
     */
    private function agregarDetalle() : Void
    {
        var popup = PopUpManager.createPopUp(_root, ComplementoWindow, true,
        {deferred:true, capSwap:this, productos:productos, puntos:this['puntosCheckBox'].selected});
        popup.centerPopUp(this);
    }

    /**
     * En modo m&#250;ltiple, permite eliminar un deal de fin de swap que se encuentre seleccionado
     * en el dataGrid.
     */
    private function eliminarDetalle() : Void
    {
        try {
            var ind : Number = this['dataGrid'].selectedIndex;
            if (ind == 0) {
                throw new Error('El deal de inicio no puede eliminarse.');
            }
            else if (ind > 0) {
                deals.splice(ind, 1);
                sincronizar();
            }
            else {
                throw new Error('Seleccione por favor el deal de fin que desea eliminar.');
            }
        }
        catch (err : Error) {
            mx.controls.Alert.show(String(err), 'Atenci\u00f3n:');
        }
    }

    /**
     * Llamado desde el Panel de ComplementoWindow para insertar el deal fin especificado en el
     * arreglo de deals.
     *
     * @param fechaValor CASH | TOM | SPOT | 72HR.
     * @param claveFormaLiquidacion El identificador del producto.
     * @param monto El monto de la operaci&#243;n del deal de fin.
     * @param tipoCambio El tipo de cambio de la operaci&#243;n.
     */
    public function insertarComplemento(fechaValor : String, claveFormaLiquidacion : String,
                                        monto : Number, tipoCambio : Number) : Void
    {
        var df : Object = new Object();
        df.tipo = 'Fin';
        df.tipoOperacion = '';
        df.fechaValor = fechaValor;
        df.claveFormaLiquidacion = claveFormaLiquidacion;
        df.monto = monto;
        df.tipoCambio = tipoCambio;
        deals[deals.length] = df;
        sincronizar();
    }

    /**
     * Regresa la diferencia en d&#237;as entre dos fechas valor, utilizando el arreglo
     * diasDiferencia.
     *
     * @param fv1 La primera fecha valor a comparar.
     * @param fv2 La segunda fecha valor a comparar.
     * @return Number.
     */
    public function getDiferencia(fv1 : String, fv2 : String) : Number
    {
        if (!valFut) {
            if (fv1 == fv2) {
                return 0;
            }
            else if (fv1 == CASH && fv2 == TOM) {
                return diasDiferencia[0];
            }
            else if (fv1 == CASH && fv2 == SPOT) {
                return diasDiferencia[0] + diasDiferencia[1];
            }
            else if (fv1 == CASH && fv2 == HR72) {
                return diasDiferencia[0] + diasDiferencia[1] + diasDiferencia[2];
            }
            else if (fv1 == TOM && fv2 == SPOT) {
                return diasDiferencia[1];
            }
            else if (fv1 == TOM && fv2 == HR72) {
                return diasDiferencia[1] + diasDiferencia[2];
            }
            else if (fv1 == SPOT && fv2 == HR72) {
                return diasDiferencia[2];
            }
        }
        else {
            if (fv1 == fv2) {
                return 0;
            }
            else if (fv1 == CASH && fv2 == TOM) {
                return diasDiferencia[0];
            }
            else if (fv1 == CASH && fv2 == SPOT) {
                return diasDiferencia[0] + diasDiferencia[1];
            }
            else if (fv1 == CASH && fv2 == HR72) {
                return diasDiferencia[0] + diasDiferencia[1] + diasDiferencia[2];
            }
            else if (fv1 == CASH && fv2 == HR96) {
                return diasDiferencia[0] + diasDiferencia[1] + diasDiferencia[2] +
                       diasDiferencia[3];
            }
            else if (fv1 == TOM && fv2 == SPOT) {
                return diasDiferencia[1];
            }
            else if (fv1 == TOM && fv2 == HR72) {
                return diasDiferencia[1] + diasDiferencia[2];
            }
            else if (fv1 == TOM && fv2 == HR96) {
                return diasDiferencia[1] + diasDiferencia[2] + diasDiferencia[3];
            }
            else if (fv1 == SPOT && fv2 == HR72) {
                return diasDiferencia[2];
            }
            else if (fv1 == SPOT && fv2 == HR96) {
                return diasDiferencia[2] + diasDiferencia[3];
            }
            else if (fv1 == HR72 && fv2 == HR96) {
                return diasDiferencia[3];
            }
        }
        return 0;
    }

    /**
     * Realiza las validaciones necesarias. Arroja errores en caso de que alguna condici&#243;n no
     * sea v&#225;lida.
     */
    private function validarSwap() : Void
    {
        var op = this['operacionRadio'].selectedData;
        var sonPuntos : Boolean = this['puntosCheckBox'].selected;
        var modoMultiple : Boolean = this['multipleCheckBox'].selected;
        if (! (op == COMPRA || op == VENTA)) {
            throw new Error('No se ha seleccionado el tipo de operaci\u00f3n.');
        }
        if (! Validator.isValid(this, 'dealInicio.monto')) {
            throw new Error('El monto no es v\u00e1lido.');
        }
        if (! Validator.isValid(this, 'dealInicio.tipoCambio')) {
            throw new Error('El tipo de cambio Inicio no es v\u00e1lido.');
        }
        if (sonPuntos && !Validator.isValid(this, 'dealInicio.puntosSwap')) {
            throw new Error('Los puntos swap no son v\u00e1lidos.');
        }
        if (!modoMultiple && !Validator.isValid(this, 'dealFin.tipoCambio')) {
            throw new Error('El tipo de cambio Fin no es v\u00e1lido.');
        }
        if (! (deals.length >= 2)) {
            throw new Error('Debe haber por lo menos dos deals.');
        }
        if (deals[deals.length - 1].balance != 0.0) {
            throw new Error('El swap no est\u00e1 balanceado.');
        }
    }

    /**
     * Llama a validarSwap() para validar la operaci&#243;n. Crea una instancia de SwapVO con los
     * datos capturados e invoca el servicio insertarSwap() de swapsService. El resultado de la
     * operaci&#243;n es procesada en swapInsertado().
     */
    private function guardarSwap() : Void
    {
        try {
            validarSwap();
            var dts = new Array();
            for (var i : Number = 0; i < deals.length; i++) {
                dts[i] = new DetalleSwapVO(deals[i].tipo, deals[i].tipoOperacion == COMPRA,
                        deals[i].fechaValor, deals[i].fechaLiquidacion,
                        deals[i].claveFormaLiquidacion, deals[i].monto, deals[i].tipoCambio);
            }
            var idSwap : Number = 0;
            var sw : SwapVO = new SwapVO(idSwap, contraparte, idCanal,
                    this['divisasComboBox'].selectedItem.idDivisa, idMesaCambio,
                    deals[0].tipoOperacion == COMPRA, idUsuario, dts);
            swapsService.insertarSwap(sw);
        }
        catch (err : Error) {
            mx.controls.Alert.show(String(err), 'Atenci\u00f3n.');
        }
    }

    /**
     * Recibe el resultado del servicio insertarSwap().
     */
    public function swapInsertado(resultado) : Void
    {
        this['folioSwapTextField'].text = String(resultado.idFolioSwap);
        for (var i : Number = 0; i < deals.length; i++) {
            deals[i].idDeal = resultado.idsDeals[i];
        }
        sincronizar();
        var msg : String = 'El swap con folio ' + resultado.idFolioSwap +
                           ' fue insertado existosamente.';
        var popup = PopUpManager.createPopUp(_root, Confirmacion, true,
        {deferred:true, mensaje:msg});
        popup.centerPopUp(this);
    }

    public var numberBase : NumberBase;

    /**
     * Arreglo de fechas para cada fecha valor.
     */
    private var fechas : Array;

    /**
     * El identificador del canal.
     */
    public var idCanal : String;

    /**
     * El identificador de la mesa de cambios.
     */
    public var idMesaCambio : Number;

    /**
     * El identificador del usuario.
     */
    public var idUsuario : Number;

    /**
     * 'True' si hay valor futuro, 'false' en otro caso.
     */
    public var valFut : Boolean;

    /**
     * Define si la contraparte opera en Valor Futuro.
     */
    public var operaVFut : Boolean;

    /**
     * La referencia al remote object de Java.
     */
    public var swapsService : Object;

    /**
     * La contraparte para los deals interbancarios.
     */
    public var contraparte : BrokerVO;

    /**
     * La lista de divisas disponibles.
     */
    public var divisas : Array;

    /**
     * La lista de productos disponibles para la divisa seleccionada.
     */
    public var productos : Array;

    /**
     * La lista de deals que componen al swap.
     */
    private var deals : Array;

    /**
     * La lista que contiene las opciones para fechas valor.
     */
    private var fechasValor : Array;

    /**
     * El arreglo con la informaci&#243;n de la diferencia en d&#237;as entre las fechas valor.
     */
    private var diasDiferencia : Array;

    /**
     * Constante para identificar la fecha valor CASH.
     */
    private static var CASH : String = 'CASH';

    /**
     * Constante para identificar la fecha valor TOM.
     */
    private static var TOM : String = 'TOM';

    /**
     * Constante para identificar la fecha valor SPOT.
     */
    private static var SPOT : String = 'SPOT';

    /**
     * Constante para identificar la fecha valor 72HR.
     */
    private static var HR72 : String = '72HR';

    /**
     * Constante para identificar la fecha valor 96HR (VFut).
     */
    private static var HR96 : String = '96HR';

    /**
     * Constante para identificar el tipo de operaci&#243;n compra.
     */
    private static var COMPRA : String = 'Compra';

    /**
     * Constante para identificar el tipo de operaci&#243;n venta.
     */
    private static var VENTA : String = 'Venta';

    /**
     * Constante para identificar si hay valor futuro.
     */
    private static var VAL_FUT : String = 'true';
}