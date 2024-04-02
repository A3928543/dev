/*
 * $Id: PosicionVO.java,v 1.11.38.1.36.1 2014/10/29 16:41:38 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */
package com.ixe.ods.sica.posicion.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Value Object para Posicion para la comunicaci&oacute;n del SICA con las applicaciones en Flex (Monitor de la Posici&oacute;n).
 * 
 * @author rchavez
 */
public class PosicionVO implements Serializable {

    /**
     * Constructor por Default.
     */
    public PosicionVO() {
    }

    /**
     * Constructor con todos los campos de la tabla SC_POSICION. Se utiliza en el query getPosicionMesa
     *
     * @param compraCash El valor del tipo del cambio para Compra Cash.
     * @param ventaCash El valor del tipo del cambio para Venta Cash.
     * @param compraTom El valor del tipo del cambio para Compra Tom.
     * @param ventaTom El valor del tipo del cambio para Venta Tom.
     * @param compraSpot El valor del tipo del cambio para Compra Spot.
     * @param ventaSpot El valor del tipo del cambio para Venta Spot.
     * @param compra72Hr El valor del tipo del cambio para Compra 72Hr.
     * @param venta72Hr El valor del tipo del cambio para Venta 72Hr.
     * @param compraVFut El valor del tipo del cambio para Compra VFut.
     * @param ventaVFut El valor del tipo del cambio para Venta VFut.
     * @param posicionInicialCash El monto de la Posici&oacute;n Inicial para Cash.
     * @param posicionInicialTom El monto de la Posici&oacute;n Inicial para Tom.
     * @param posicionInicialSpot El monto de la Posici&oacute;n Inicial para Spot.
     * @param posicionInicialMnCash El monto en moneda nacional de la Posici&oacute;n Inicial para Cash.
     * @param posicionInicialMnTom El monto en moneda nacional de la Posici&oacute;n Inicial para Tom.
     * @param posicionInicialMnSpot El monto en moneda nacional de la Posici&oacute;n Inicial para Spot.
     * @param posicionInicialMn72Hr El monto en moneda nacional de la Posici&oacute;n Inicial para 72Hr.
     * @param posicionInicialMnVFut El monto en moneda nacional de la Posici&oacute;n Inicial para VFut.
     * @param compraMnClienteCash El monto en moneda nacional del tipo de cambio para el cliente de Compra Cash.
     * @param ventaMnClienteCash El monto en moneda nacional del tipo de cambio para el cliente de Venta Cash.
     * @param compraMnClienteTom El monto en moneda nacional del tipo de cambio para el cliente de Compra Tom.
     * @param ventaMnClienteTom El monto en moneda nacional del tipo de cambio para el cliente de Venta Tom.
     * @param compraMnClienteSpot El monto en moneda nacional del tipo de cambio para el cliente de Compra Spot.
     * @param ventaMnClienteSpot El monto en moneda nacional del tipo de cambio para el cliente de Venta Spot.
     * @param compraMnCliente72Hr El monto en moneda nacional del tipo de cambio para el cliente de Compra 72Hr.
     * @param ventaMnCliente72Hr El monto en moneda nacional del tipo de cambio para el cliente de Venta 72Hr.
     * @param compraMnClienteVFut El monto en moneda nacional del tipo de cambio para el cliente de Compra VFut.
     * @param ventaMnClienteVFut El monto en moneda nacional del tipo de cambio para el cliente de Venta VFut.
     * @param compraMnPizarronCash El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Cash.
     * @param ventaMnPizarronCash El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Cash.
     * @param compraMnPizarronTom El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Tom.
     * @param ventaMnPizarronTom El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Tom.
     * @param compraMnPizarronSpot El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Spot.
     * @param ventaMnPizarronSpot El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Spot.
     * @param compraMnPizarron72Hr El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra 72Hr.
     * @param ventaMnPizarron72Hr El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta 72Hr.
     * @param compraMnPizarronVFut El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra VFut.
     * @param ventaMnPizarronVFut El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta VFut.
     * @param compraMnMesaCash El monto en moneda nacional del tipo de cambio para la mesa de Compra Cash.
     * @param ventaMnMesaCash El monto en moneda nacional del tipo de cambio para la mesa de Venta Cash.
     * @param compraMnMesaTom El monto en moneda nacional del tipo de cambio para la mesa de Compra Tom.
     * @param ventaMnMesaTom El monto en moneda nacional del tipo de cambio para la mesa de Venta Tom.
     * @param compraMnMesaSpot El monto en moneda nacional del tipo de cambio para la mesa de Compra Spot
     * @param ventaMnMesaSpot El monto en moneda nacional del tipo de cambio para la mesa de Venta Spot.
     * @param compraMnMesa72Hr El monto en moneda nacional del tipo de cambio para la mesa de Compra 72Hr.
     * @param ventaMnMesa72Hr El monto en moneda nacional del tipo de cambio para la mesa de Venta 72Hr.
     * @param compraMnMesaVFut El monto en moneda nacional del tipo de cambio para la mesa de Compra VFut.
     * @param ventaMnMesaVFut El monto en moneda nacional del tipo de cambio para la mesa de Venta VFut.
     * @param idMesaCambio El id de la mesa.
     * @param idDivisa El id de la divisa.
     * @param utilidadRealizadaGlobal El monto de la Utilidad Global.
     * @param utilidadRealizadaMesa El monto de la utilidad Tom.
     * @param compraInCash El monto en moneda nacional del tipo de cambio para Compra Interbancaria Cash. 
     * @param ventaInCash El monto en moneda nacional del tipo de cambio para Venta Interbancaria Cash.
     * @param compraInTom El monto en moneda nacional del tipo de cambio para Compra Interbancaria Tom.
     * @param ventaInTom El monto en moneda nacional del tipo de cambio para Venta Interbancaria Tom.
     * @param compraInSpot El monto en moneda nacional del tipo de cambio para Compra Interbancaria Spot.
     * @param ventaInSpot El monto en moneda nacional del tipo de cambio para Venta Interbancaria Spot.
     * @param compraIn72Hr El monto en moneda nacional del tipo de cambio para Compra Interbancaria 72Hr.
     * @param ventaIn72Hr El monto en moneda nacional del tipo de cambio para Venta Interbancaria 72Hr.
     * @param compraInVFut El monto en moneda nacional del tipo de cambio para Compra Interbancaria VFut.
     * @param ventaInVFut El monto en moneda nacional del tipo de cambio para Venta Interbancaria VFut.
     */
    public PosicionVO(double compraCash, double ventaCash, double compraTom, double ventaTom, double compraSpot, double ventaSpot,
    		double compra72Hr, double venta72Hr, double compraVFut, double ventaVFut,
    		double posicionInicialCash, double posicionInicialTom, double posicionInicialSpot, double posicionInicial72Hr, double posicionInicialVFut, 
    		double posicionInicialMnCash, double posicionInicialMnTom, double posicionInicialMnSpot, double posicionInicialMn72Hr, double posicionInicialMnVFut, 
    		double compraMnClienteCash, double ventaMnClienteCash, double compraMnClienteTom, double ventaMnClienteTom, double compraMnClienteSpot, double ventaMnClienteSpot, 
    		double compraMnCliente72Hr, double ventaMnCliente72Hr, double compraMnClienteVFut, double ventaMnClienteVFut,
    		double compraMnPizarronCash, double ventaMnPizarronCash, double compraMnPizarronTom,double ventaMnPizarronTom, double compraMnPizarronSpot, double ventaMnPizarronSpot,
    		double compraMnPizarron72Hr, double ventaMnPizarron72Hr, double compraMnPizarronVFut, double ventaMnPizarronVFut,
    		double compraMnMesaCash, double ventaMnMesaCash, double compraMnMesaTom, double ventaMnMesaTom, double compraMnMesaSpot, double ventaMnMesaSpot, 
    		double compraMnMesa72Hr, double ventaMnMesa72Hr, double compraMnMesaVFut, double ventaMnMesaVFut, 
    		Integer idMesaCambio, String idDivisa,
    		double utilidadRealizadaGlobal, double utilidadRealizadaMesa,
    		double compraInCash, double ventaInCash, double compraInTom, double ventaInTom, double compraInSpot, double ventaInSpot,
    		double compraIn72Hr, double ventaIn72Hr, double compraInVFut, double ventaInVFut) {
        this.compraCash = new BigDecimal(compraCash);
        this.ventaCash = new BigDecimal(ventaCash);
        this.compraTom = new BigDecimal(compraTom);
        this.ventaTom = new BigDecimal(ventaTom);
        this.compraSpot = new BigDecimal(compraSpot);
        this.ventaSpot = new BigDecimal(ventaSpot);
        this.compra72Hr = new BigDecimal(compra72Hr);
        this.venta72Hr = new BigDecimal(venta72Hr);
        this.compraVFut = new BigDecimal(compraVFut);
        this.ventaVFut = new BigDecimal(ventaVFut);
        this.posicionInicialCash = new BigDecimal(posicionInicialCash);
        this.posicionInicialTom = new BigDecimal(posicionInicialTom);
        this.posicionInicialSpot = new BigDecimal(posicionInicialSpot);
        this.posicionInicial72Hr = new BigDecimal(posicionInicial72Hr);
        this.posicionInicialVFut = new BigDecimal(posicionInicialVFut);
        this.posicionInicialMnCash = new BigDecimal(posicionInicialMnCash);
        this.posicionInicialMnTom = new BigDecimal(posicionInicialMnTom);
        this.posicionInicialMnSpot = new BigDecimal(posicionInicialMnSpot);
        this.posicionInicialMn72Hr = new BigDecimal(posicionInicialMn72Hr);
        this.posicionInicialMnVFut = new BigDecimal(posicionInicialMnVFut);
        this.compraMnClienteCash = new BigDecimal(compraMnClienteCash);
        this.ventaMnClienteCash = new BigDecimal(ventaMnClienteCash);
        this.compraMnClienteTom = new BigDecimal(compraMnClienteTom);
        this.ventaMnClienteTom = new BigDecimal(ventaMnClienteTom);
        this.compraMnClienteSpot = new BigDecimal(compraMnClienteSpot);
        this.ventaMnClienteSpot = new BigDecimal(ventaMnClienteSpot);
        this.compraMnCliente72Hr = new BigDecimal(compraMnCliente72Hr);
        this.ventaMnCliente72Hr = new BigDecimal(ventaMnCliente72Hr);
        this.compraMnClienteVFut = new BigDecimal(compraMnClienteVFut);
        this.ventaMnClienteVFut = new BigDecimal(ventaMnClienteVFut);
        this.compraMnPizarronCash = new BigDecimal(compraMnPizarronCash);
        this.ventaMnPizarronCash = new BigDecimal(ventaMnPizarronCash);
        this.compraMnPizarronTom = new BigDecimal(compraMnPizarronTom);
        this.ventaMnPizarronTom = new BigDecimal(ventaMnPizarronTom);
        this.compraMnPizarronSpot = new BigDecimal(compraMnPizarronSpot);
        this.ventaMnPizarronSpot = new BigDecimal(ventaMnPizarronSpot);
        this.compraMnPizarron72Hr = new BigDecimal(compraMnPizarron72Hr);
        this.ventaMnPizarron72Hr = new BigDecimal(ventaMnPizarron72Hr);
        this.compraMnPizarronVFut = new BigDecimal(compraMnPizarronVFut);
        this.ventaMnPizarronVFut = new BigDecimal(ventaMnPizarronVFut);
        this.compraMnMesaCash = new BigDecimal(compraMnMesaCash);
        this.ventaMnMesaCash = new BigDecimal(ventaMnMesaCash);
        this.compraMnMesaTom = new BigDecimal(compraMnMesaTom);
        this.ventaMnMesaTom = new BigDecimal(ventaMnMesaTom);
        this.compraMnMesaSpot = new BigDecimal(compraMnMesaSpot);
        this.ventaMnMesaSpot = new BigDecimal(ventaMnMesaSpot);
        this.compraMnMesa72Hr = new BigDecimal(compraMnMesa72Hr);
        this.ventaMnMesa72Hr = new BigDecimal(ventaMnMesa72Hr);
        this.compraMnMesaVFut = new BigDecimal(compraMnMesaVFut);
        this.ventaMnMesaVFut = new BigDecimal(ventaMnMesaVFut);
        this.idMesaCambio = idMesaCambio;
        this.idDivisa = idDivisa;
        this.utilidadRealizadaGlobal = new BigDecimal(utilidadRealizadaGlobal);
        this.utilidadRealizadaMesa = new BigDecimal(utilidadRealizadaMesa);
        this.compraInCash = new BigDecimal(compraInCash);
        this.ventaInCash = new BigDecimal(ventaInCash);
        this.compraInTom = new BigDecimal(compraInTom);
        this.ventaInTom = new BigDecimal(ventaInTom);
        this.compraInSpot = new BigDecimal(compraInSpot);
        this.ventaInSpot = new BigDecimal(ventaInSpot);
        this.compraIn72Hr = new BigDecimal(compraIn72Hr);
        this.ventaIn72Hr = new BigDecimal(ventaIn72Hr);
        this.compraInVFut = new BigDecimal(compraInVFut);
        this.ventaInVFut = new BigDecimal(ventaInVFut);
    }
    
    /**
     * Constructor utilizado para el armado de Cortes
     *
     * @param compraCash El valor del tipo del cambio para Compra Cash.
     * @param ventaCash El valor del tipo del cambio para Venta Cash.
     * @param compraTom El valor del tipo del cambio para Compra Tom.
     * @param ventaTom El valor del tipo del cambio para Venta Tom.
     * @param compraSpot El valor del tipo del cambio para Compra Spot.
     * @param ventaSpot El valor del tipo del cambio para Venta Spot.
     * @param compra72Hr El valor del tipo del cambio para Compra 72Hr.
     * @param venta72Hr El valor del tipo del cambio para Venta 72Hr.
     * @param compraVFut El valor del tipo del cambio para Compra VFut.
     * @param ventaVFut El valor del tipo del cambio para Venta VFut.
     * @param compraMnClienteCash El monto en moneda nacional del tipo de cambio para el cliente de Compra Cash.
     * @param ventaMnClienteCash El monto en moneda nacional del tipo de cambio para el cliente de Venta Cash.
     * @param compraMnClienteTom El monto en moneda nacional del tipo de cambio para el cliente de Compra Tom.
     * @param ventaMnClienteTom El monto en moneda nacional del tipo de cambio para el cliente de Venta Tom.
     * @param compraMnClienteSpot El monto en moneda nacional del tipo de cambio para el cliente de Compra Spot.
     * @param ventaMnClienteSpot El monto en moneda nacional del tipo de cambio para el cliente de Venta Spot.
     * @param compraMnCliente72Hr El monto en moneda nacional del tipo de cambio para el cliente de Compra 72Hr.
     * @param ventaMnCliente72Hr El monto en moneda nacional del tipo de cambio para el cliente de Venta 72Hr.
     * @param compraMnClienteVFut El monto en moneda nacional del tipo de cambio para el cliente de Compra VFut.
     * @param ventaMnClienteVFut El monto en moneda nacional del tipo de cambio para el cliente de Venta VFut.
     * @param compraMnPizarronCash El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Cash.
     * @param ventaMnPizarronCash El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Cash.
     * @param compraMnPizarronTom El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Tom.
     * @param ventaMnPizarronTom El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Tom.
     * @param compraMnPizarronSpot El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Spot.
     * @param ventaMnPizarronSpot El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Spot.
     * @param compraMnPizarron72Hr El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra 72Hr.
     * @param ventaMnPizarron72Hr El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta 72Hr.
     * @param compraMnPizarronVFut El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra VFut.
     * @param ventaMnPizarronVFut El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta VFut.
     * @param idDivisa El id de la divisa.
     */
    public PosicionVO(double compraCash, double ventaCash, double compraTom, double ventaTom, double compraSpot, double ventaSpot,
    		double compra72Hr, double venta72Hr, double compraVFut, double ventaVFut,
    		double compraMnClienteCash, double ventaMnClienteCash, double compraMnClienteTom, double ventaMnClienteTom, double compraMnClienteSpot, double ventaMnClienteSpot, 
    		double compraMnCliente72Hr, double ventaMnCliente72Hr, double compraMnClienteVFut, double ventaMnClienteVFut,
    		double compraMnPizarronCash, double ventaMnPizarronCash, double compraMnPizarronTom,double ventaMnPizarronTom, double compraMnPizarronSpot, double ventaMnPizarronSpot,
    		double compraMnPizarron72Hr, double ventaMnPizarron72Hr, double compraMnPizarronVFut, double ventaMnPizarronVFut,
    		String idDivisa) {
        this.compraCash = new BigDecimal(compraCash);
        this.ventaCash = new BigDecimal(ventaCash);
        this.compraTom = new BigDecimal(compraTom);
        this.ventaTom = new BigDecimal(ventaTom);
        this.compraSpot = new BigDecimal(compraSpot);
        this.ventaSpot = new BigDecimal(ventaSpot);
        this.compra72Hr = new BigDecimal(compra72Hr);
        this.venta72Hr = new BigDecimal(venta72Hr);
        this.compraVFut = new BigDecimal(compraVFut);
        this.ventaVFut = new BigDecimal(ventaVFut);
        this.compraMnClienteCash = new BigDecimal(compraMnClienteCash);
        this.ventaMnClienteCash = new BigDecimal(ventaMnClienteCash);
        this.compraMnClienteTom = new BigDecimal(compraMnClienteTom);
        this.ventaMnClienteTom = new BigDecimal(ventaMnClienteTom);
        this.compraMnClienteSpot = new BigDecimal(compraMnClienteSpot);
        this.ventaMnClienteSpot = new BigDecimal(ventaMnClienteSpot);
        this.compraMnCliente72Hr = new BigDecimal(compraMnCliente72Hr);
        this.ventaMnCliente72Hr = new BigDecimal(ventaMnCliente72Hr);
        this.compraMnClienteVFut = new BigDecimal(compraMnClienteVFut);
        this.compraMnPizarronCash = new BigDecimal(compraMnPizarronCash);
        this.ventaMnPizarronCash = new BigDecimal(ventaMnPizarronCash);
        this.compraMnPizarronTom = new BigDecimal(compraMnPizarronTom);
        this.ventaMnPizarronTom = new BigDecimal(ventaMnPizarronTom);
        this.compraMnPizarronSpot = new BigDecimal(compraMnPizarronSpot);
        this.ventaMnPizarronSpot = new BigDecimal(ventaMnPizarronSpot);
        this.compraMnPizarron72Hr = new BigDecimal(compraMnPizarron72Hr);
        this.ventaMnPizarron72Hr = new BigDecimal(ventaMnPizarron72Hr);
        this.compraMnPizarronVFut = new BigDecimal(compraMnPizarronVFut);
        this.ventaMnPizarronVFut = new BigDecimal(ventaMnPizarronVFut);
        this.idMesaCambio = idMesaCambio;
        this.idDivisa = idDivisa;
    }
    
    /**
     * Constructor utilizado para el armado de Cortes
     *
     * @param idDivisa El id de la divisa.
     * @param tipoValor La fecha Valor.
     * @param tipoOperacion El tipo de la operacion.
     * @param montoMnMesa El monto en moneda nacional.
     * @param montoMn El monto en moneda nacional.
     * @param monto El monto en divisa.
     * @param idCorte El id del Corte.
     */
    public PosicionVO(String idDivisa,String tipoValor,String tipoOperacion,double montoMnMesa,double montoMn, double monto, Integer idCorte
    		) {
        this.idDivisa = idDivisa;
        this.tipoValor = tipoValor;
        this.tipoOperacion = tipoOperacion;
        this.montoMnMesa = new BigDecimal(montoMnMesa);
        this.montoMn = new BigDecimal(montoMn);
        this.monto = new BigDecimal(monto);
        this.idCorte = idCorte;
    }

    /**
     * Se usa en el query getUtilidadGlobal
     *
     @param compraCash El valor del tipo del cambio para Compra Cash.
     * @param ventaCash El valor del tipo del cambio para Venta Cash.
     * @param compraTom El valor del tipo del cambio para Compra Tom.
     * @param ventaTom El valor del tipo del cambio para Venta Tom.
     * @param compraSpot El valor del tipo del cambio para Compra Spot.
     * @param ventaSpot El valor del tipo del cambio para Venta Spot.
     * @param compra72Hr El valor del tipo del cambio para Compra VFut.
     * @param venta72Hr El valor del tipo del cambio para Venta VFUt.
     * @param compraVFut El valor del tipo del cambio para Compra 72Hr.
     * @param ventaVFut El valor del tipo del cambio para Venta 72Hr.
     * @param posicionInicialCash El monto de la Posici&oacute;n Inicial para Cash.
     * @param posicionInicialTom El monto de la Posici&oacute;n Inicial para Tom.
     * @param posicionInicialSpot El monto de la Posici&oacute;n Inicial para Spot.
     * @param posicionInicialMnCash El monto en moneda nacional de la Posici&oacute;n Inicial para Cash.
     * @param posicionInicialMnTom El monto en moneda nacional de la Posici&oacute;n Inicial para Tom.
     * @param posicionInicialMnSpot El monto en moneda nacional de la Posici&oacute;n Inicial para Spot.
     * @param posicionInicialMn72Hr El monto en moneda nacional de la Posici&oacute;n Inicial para 72Hr.
     * @param posicionInicialMnVFut El monto en moneda nacional de la Posici&oacute;n Inicial para VFut.
     * @param compraMnClienteCash El monto en moneda nacional del tipo de cambio para el cliente de Compra Cash.
     * @param ventaMnClienteCash El monto en moneda nacional del tipo de cambio para el cliente de Venta Cash.
     * @param compraMnClienteTom El monto en moneda nacional del tipo de cambio para el cliente de Compra Tom.
     * @param ventaMnClienteTom El monto en moneda nacional del tipo de cambio para el cliente de Venta Tom.
     * @param compraMnClienteSpot El monto en moneda nacional del tipo de cambio para el cliente de Compra Spot.
     * @param ventaMnClienteSpot El monto en moneda nacional del tipo de cambio para el cliente de Venta Spot.
     * @param compraMnCliente72Hr El monto en moneda nacional del tipo de cambio para el cliente de Compra 72Hr.
     * @param ventaMnCliente72Hr El monto en moneda nacional del tipo de cambio para el cliente de Venta 72Hr.
     * @param compraMnClienteVFut El monto en moneda nacional del tipo de cambio para el cliente de Compra VFut.
     * @param ventaMnClienteVFut El monto en moneda nacional del tipo de cambio para el cliente de Venta VFut.
     * @param compraMnPizarronCash El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Cash.
     * @param ventaMnPizarronCash El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Cash.
     * @param compraMnPizarronTom El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Tom.
     * @param ventaMnPizarronTom El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Tom.
     * @param compraMnPizarronSpot El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Spot.
     * @param ventaMnPizarronSpot El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Spot.
     * @param compraMnPizarron72Hr El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra 72Hr.
     * @param ventaMnPizarron72Hr El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta 72Hr.
     * @param compraMnPizarronVFut El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra VFut.
     * @param ventaMnPizarronVFut El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta VFut.
     * @param compraMnMesaCash El monto en moneda nacional del tipo de cambio para la mesa de Compra Cash.
     * @param ventaMnMesaCash El monto en moneda nacional del tipo de cambio para la mesa de Venta Cash.
     * @param compraMnMesaTom El monto en moneda nacional del tipo de cambio para la mesa de Compra Tom.
     * @param ventaMnMesaTom El monto en moneda nacional del tipo de cambio para la mesa de Venta Tom.
     * @param compraMnMesaSpot El monto en moneda nacional del tipo de cambio para la mesa de Compra Spot
     * @param ventaMnMesaSpot El monto en moneda nacional del tipo de cambio para la mesa de Venta Spot.
     * @param compraMnMesa72Hr El monto en moneda nacional del tipo de cambio para la mesa de Compra 72Hr.
     * @param ventaMnMesa72Hr El monto en moneda nacional del tipo de cambio para la mesa de Venta 72Hr.
     * @param compraMnMesaVFut El monto en moneda nacional del tipo de cambio para la mesa de Compra VFut.
     * @param ventaMnMesaVFut El monto en moneda nacional del tipo de cambio para la mesa de Venta VFut.
     * @param compraInCash El monto en moneda nacional del tipo de cambio para Compra Interbancaria Cash. 
     * @param ventaInCash El monto en moneda nacional del tipo de cambio para Venta Interbancaria Cash.
     * @param compraInTom El monto en moneda nacional del tipo de cambio para Compra Interbancaria Tom.
     * @param ventaInTom El monto en moneda nacional del tipo de cambio para Venta Interbancaria Tom.
     * @param compraInSpot El monto en moneda nacional del tipo de cambio para Compra Interbancaria Spot.
     * @param ventaInSpot El monto en moneda nacional del tipo de cambio para Venta Interbancaria Spot.
     * @param compraIn72Hr El monto en moneda nacional del tipo de cambio para Compra Interbancaria 72Hr.
     * @param ventaIn72Hr El monto en moneda nacional del tipo de cambio para Venta Interbancaria 72Hr.
     * @param compraInVFut El monto en moneda nacional del tipo de cambio para Compra Interbancaria VFut.
     * @param ventaInVFut El monto en moneda nacional del tipo de cambio para Venta Interbancaria VFut.
     * @param idDivisa El id de la divisa.
     */
    public PosicionVO(double compraCash, double ventaCash, double compraTom, double ventaTom, double compraSpot, double ventaSpot,
    		double compra72Hr, double venta72Hr, double compraVFut, double ventaVFut,
    		double posicionInicialCash, double posicionInicialTom, double posicionInicialSpot, double posicionInicial72Hr, double posicionInicialVFut,
    		double posicionInicialMnCash, double posicionInicialMnTom, double posicionInicialMnSpot, double posicionInicialMn72Hr, double posicionInicialMnVFut,
    		double compraMnClienteCash, double ventaMnClienteCash, double compraMnClienteTom,
    		double ventaMnClienteTom, double compraMnClienteSpot, double ventaMnClienteSpot,
    		double compraMnCliente72Hr, double ventaMnCliente72Hr, double compraMnClienteVFut, double ventaMnClienteVFut,
    		double compraMnPizarronCash, double ventaMnPizarronCash, double compraMnPizarronTom, double ventaMnPizarronTom,
    		double compraMnPizarronSpot, double ventaMnPizarronSpot, double compraMnPizarron72Hr, double ventaMnPizarron72Hr, double compraMnPizarronVFut, double ventaMnPizarronVFut,
    		double compraMnMesaCash,double ventaMnMesaCash, double compraMnMesaTom, double ventaMnMesaTom, double compraMnMesaSpot,
    		double ventaMnMesaSpot, double ventaMnMesa72Hr, double compraMnMesa72Hr, double ventaMnMesaVFut, double compraMnMesaVFut,
    		double compraInCash, double ventaInCash, double compraInTom, double ventaInTom,
    		double compraInSpot, double ventaInSpot, double compraIn72Hr, double ventaIn72Hr, double compraInVFut, double ventaInVFut,
    		String idDivisa) {
    	this.compraCash = new BigDecimal(compraCash);
        this.ventaCash = new BigDecimal(ventaCash);
        this.compraTom = new BigDecimal(compraTom);
        this.ventaTom = new BigDecimal(ventaTom);
        this.compraSpot = new BigDecimal(compraSpot);
        this.ventaSpot = new BigDecimal(ventaSpot);
        this.compra72Hr = new BigDecimal(compra72Hr);
        this.venta72Hr = new BigDecimal(venta72Hr);
        this.compraVFut = new BigDecimal(compraVFut);
        this.ventaVFut = new BigDecimal(ventaVFut);
        this.posicionInicialCash = new BigDecimal(posicionInicialCash);
        this.posicionInicialTom = new BigDecimal(posicionInicialTom);
        this.posicionInicialSpot = new BigDecimal(posicionInicialSpot);
        this.posicionInicial72Hr = new BigDecimal(posicionInicial72Hr);
        this.posicionInicialVFut = new BigDecimal(posicionInicialVFut);
        this.posicionInicialMnCash = new BigDecimal(posicionInicialMnCash);
        this.posicionInicialMnTom = new BigDecimal(posicionInicialMnTom);
        this.posicionInicialMnSpot = new BigDecimal(posicionInicialMnSpot);
        this.posicionInicialMn72Hr = new BigDecimal(posicionInicialMn72Hr);
        this.posicionInicialMnVFut = new BigDecimal(posicionInicialMnVFut);
        this.compraMnClienteCash = new BigDecimal(compraMnClienteCash);
        this.ventaMnClienteCash = new BigDecimal(ventaMnClienteCash);
        this.compraMnClienteTom = new BigDecimal(compraMnClienteTom);
        this.ventaMnClienteTom = new BigDecimal(ventaMnClienteTom);
        this.compraMnClienteSpot = new BigDecimal(compraMnClienteSpot);
        this.ventaMnClienteSpot = new BigDecimal(ventaMnClienteSpot);
        this.compraMnCliente72Hr = new BigDecimal(compraMnCliente72Hr);
        this.ventaMnCliente72Hr = new BigDecimal(ventaMnCliente72Hr);
        this.compraMnClienteVFut = new BigDecimal(compraMnClienteVFut);
        this.ventaMnClienteVFut = new BigDecimal(ventaMnClienteVFut);
        this.compraMnPizarronCash = new BigDecimal(compraMnPizarronCash);
        this.ventaMnPizarronCash = new BigDecimal(ventaMnPizarronCash);
        this.compraMnPizarronTom = new BigDecimal(compraMnPizarronTom);
        this.ventaMnPizarronTom = new BigDecimal(ventaMnPizarronTom);
        this.compraMnPizarronSpot = new BigDecimal(compraMnPizarronSpot);
        this.ventaMnPizarronSpot = new BigDecimal(ventaMnPizarronSpot);
        this.compraMnPizarron72Hr = new BigDecimal(compraMnPizarron72Hr);
        this.ventaMnPizarron72Hr = new BigDecimal(ventaMnPizarron72Hr);
        this.compraMnPizarronVFut = new BigDecimal(compraMnPizarronVFut);
        this.ventaMnPizarronVFut = new BigDecimal(ventaMnPizarronVFut);
        this.compraMnMesaCash = new BigDecimal(compraMnMesaCash);
        this.ventaMnMesaCash = new BigDecimal(ventaMnMesaCash);
        this.compraMnMesaTom = new BigDecimal(compraMnMesaTom);
        this.ventaMnMesaTom = new BigDecimal(ventaMnMesaTom);
        this.compraMnMesaSpot = new BigDecimal(compraMnMesaSpot);
        this.ventaMnMesaSpot = new BigDecimal(ventaMnMesaSpot);
        this.compraMnMesa72Hr = new BigDecimal(compraMnMesa72Hr);
        this.ventaMnMesa72Hr = new BigDecimal(ventaMnMesa72Hr);
        this.compraMnMesaVFut = new BigDecimal(compraMnMesaVFut);
        this.ventaMnMesaVFut = new BigDecimal(ventaMnMesaVFut);
        this.compraInCash = new BigDecimal(compraInCash);
        this.ventaInCash = new BigDecimal(ventaInCash);
        this.compraInTom = new BigDecimal(compraInTom);
        this.ventaInTom = new BigDecimal(ventaInTom);
        this.compraInSpot = new BigDecimal(compraInSpot);
        this.ventaInSpot = new BigDecimal(ventaInSpot);
        this.compraIn72Hr = new BigDecimal(compraIn72Hr);
        this.ventaIn72Hr = new BigDecimal(ventaIn72Hr);
        this.compraInVFut = new BigDecimal(compraInVFut);
        this.ventaInVFut = new BigDecimal(ventaInVFut);
        this.idDivisa = idDivisa;
    }

    /**
     * Constructor usado en los metodos Query que no tiene los datos id_divisa, id_mesa, id_canal, id_producto, posicion_inicialm
     * posicion_inicial_cliente, posicion_inicial_mesa y posicion_inicial_pizarron.
     * Se utiliza en los queries getQueryPosicionCanales, getQueryPosicionProductos
     *
     * @param compraCash El valor del tipo del cambio para Compra Cash.
     * @param ventaCash El valor del tipo del cambio para Venta Cash.
     * @param compraTom El valor del tipo del cambio para Compra Tom.
     * @param ventaTom El valor del tipo del cambio para Venta Tom.
     * @param compraSpot El valor del tipo del cambio para Compra Spot.
     * @param ventaSpot El valor del tipo del cambio para Venta Spot.
     * @param compra72Hr El valor del tipo del cambio para Compra VFut.
     * @param venta72Hr El valor del tipo del cambio para Venta VFUt.
     * @param compraVFut El valor del tipo del cambio para Compra 72Hr.
     * @param ventaVFut El valor del tipo del cambio para Venta 72Hr.
     * @param compraMnClienteCash El monto en moneda nacional del tipo de cambio para el cliente de Compra Cash.
     * @param ventaMnClienteCash El monto en moneda nacional del tipo de cambio para el cliente de Venta Cash.
     * @param compraMnClienteTom El monto en moneda nacional del tipo de cambio para el cliente de Compra Tom.
     * @param ventaMnClienteTom El monto en moneda nacional del tipo de cambio para el cliente de Venta Tom.
     * @param compraMnClienteSpot El monto en moneda nacional del tipo de cambio para el cliente de Compra Spot.
     * @param ventaMnClienteSpot El monto en moneda nacional del tipo de cambio para el cliente de Venta Spot.
     * @param compraMnCliente72Hr El monto en moneda nacional del tipo de cambio para el cliente de Compra 72Hr.
     * @param ventaMnCliente72Hr El monto en moneda nacional del tipo de cambio para el cliente de Venta 72Hr.
     * @param compraMnClienteVFut El monto en moneda nacional del tipo de cambio para el cliente de Compra VFut.
     * @param ventaMnClienteVFut El monto en moneda nacional del tipo de cambio para el cliente de Venta VFut.
     * @param compraMnPizarronCash El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Cash.
     * @param ventaMnPizarronCash El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Cash.
     * @param compraMnPizarronTom El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Tom.
     * @param ventaMnPizarronTom El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Tom.
     * @param compraMnPizarronSpot El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Spot.
     * @param ventaMnPizarronSpot El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Spot.
     * @param compraMnPizarron72Hr El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra 72Hr.
     * @param ventaMnPizarron72Hr El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta 72Hr.
     * @param compraMnPizarronVFut El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra VFut.
     * @param ventaMnPizarronVFut El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta VFut.
     * @param compraMnMesaCash El monto en moneda nacional del tipo de cambio para la mesa de Compra Cash.
     * @param ventaMnMesaCash El monto en moneda nacional del tipo de cambio para la mesa de Venta Cash.
     * @param compraMnMesaTom El monto en moneda nacional del tipo de cambio para la mesa de Compra Tom.
     * @param ventaMnMesaTom El monto en moneda nacional del tipo de cambio para la mesa de Venta Tom.
     * @param compraMnMesaSpot El monto en moneda nacional del tipo de cambio para la mesa de Compra Spot
     * @param ventaMnMesaSpot El monto en moneda nacional del tipo de cambio para la mesa de Venta Spot.
     * @param compraMnMesa72Hr El monto en moneda nacional del tipo de cambio para la mesa de Compra 72Hr.
     * @param ventaMnMesa72Hr El monto en moneda nacional del tipo de cambio para la mesa de Venta 72Hr.
     * @param compraMnMesaVFut El monto en moneda nacional del tipo de cambio para la mesa de Compra VFut.
     * @param ventaMnMesaVFut El monto en moneda nacional del tipo de cambio para la mesa de Venta VFut.
     */
    public PosicionVO(double compraCash, double ventaCash, double compraTom, double ventaTom, double compraSpot,
    		double ventaSpot, double compra72Hr, double venta72Hr, double compraVFut, double ventaVFut,
    		double compraMnClienteCash,double ventaMnClienteCash, double compraMnClienteTom, double ventaMnClienteTom, double compraMnClienteSpot,
    		double ventaMnClienteSpot, double compraMnCliente72Hr,double ventaMnCliente72Hr, double compraMnClienteVFut, double ventaMnClienteVFut,
    		double compraMnPizarronCash, double ventaMnPizarronCash, double compraMnPizarronTom, double ventaMnPizarronTom, double compraMnPizarronSpot,
    		double ventaMnPizarronSpot, double compraMnPizarron72Hr, double ventaMnPizarron72Hr, double compraMnPizarronVFut, double ventaMnPizarronVFut,
    		double compraMnMesaCash, double ventaMnMesaCash, double compraMnMesaTom, double ventaMnMesaTom, double compraMnMesaSpot,
    		double ventaMnMesaSpot, double ventaMnMesa72Hr, double compraMnMesa72Hr, double ventaMnMesaVFut, double compraMnMesaVFut,
    		double compraInCash, double ventaInCash, double compraInTom, double ventaInTom, double compraInSpot,
    		double ventaInSpot, double ventaIn72Hr, double compraIn72Hr, double ventaInVFut, double compraInVFut) {
    	this.compraCash = new BigDecimal(compraCash);
        this.ventaCash = new BigDecimal(ventaCash);
        this.compraTom = new BigDecimal(compraTom);
        this.ventaTom = new BigDecimal(ventaTom);
        this.compraSpot = new BigDecimal(compraSpot);
        this.ventaSpot = new BigDecimal(ventaSpot);
        this.compra72Hr = new BigDecimal(compra72Hr);
        this.venta72Hr = new BigDecimal(venta72Hr);
        this.compraVFut = new BigDecimal(compraVFut);
        this.ventaVFut = new BigDecimal(ventaVFut);
        this.compraMnClienteCash = new BigDecimal(compraMnClienteCash);
        this.ventaMnClienteCash = new BigDecimal(ventaMnClienteCash);
        this.compraMnClienteTom = new BigDecimal(compraMnClienteTom);
        this.ventaMnClienteTom = new BigDecimal(ventaMnClienteTom);
        this.compraMnClienteSpot = new BigDecimal(compraMnClienteSpot);
        this.ventaMnClienteSpot = new BigDecimal(ventaMnClienteSpot);
        this.compraMnCliente72Hr = new BigDecimal(compraMnCliente72Hr);
        this.ventaMnCliente72Hr = new BigDecimal(ventaMnCliente72Hr);
        this.compraMnClienteVFut = new BigDecimal(compraMnClienteVFut);
        this.ventaMnClienteVFut = new BigDecimal(ventaMnClienteVFut);
        this.compraMnPizarronCash = new BigDecimal(compraMnPizarronCash);
        this.ventaMnPizarronCash = new BigDecimal(ventaMnPizarronCash);
        this.compraMnPizarronTom = new BigDecimal(compraMnPizarronTom);
        this.ventaMnPizarronTom = new BigDecimal(ventaMnPizarronTom);
        this.compraMnPizarronSpot = new BigDecimal(compraMnPizarronSpot);
        this.ventaMnPizarronSpot = new BigDecimal(ventaMnPizarronSpot);
        this.compraMnPizarron72Hr = new BigDecimal(compraMnPizarron72Hr);
        this.ventaMnPizarron72Hr = new BigDecimal(ventaMnPizarron72Hr);
        this.compraMnPizarronVFut = new BigDecimal(compraMnPizarronVFut);
        this.ventaMnPizarronVFut = new BigDecimal(ventaMnPizarronVFut);
        this.compraMnMesaCash = new BigDecimal(compraMnMesaCash);
        this.ventaMnMesaCash = new BigDecimal(ventaMnMesaCash);
        this.compraMnMesaTom = new BigDecimal(compraMnMesaTom);
        this.ventaMnMesaTom = new BigDecimal(ventaMnMesaTom);
        this.compraMnMesaSpot = new BigDecimal(compraMnMesaSpot);
        this.ventaMnMesaSpot = new BigDecimal(ventaMnMesaSpot);
        this.compraMnMesa72Hr = new BigDecimal(compraMnMesa72Hr);
        this.ventaMnMesa72Hr = new BigDecimal(ventaMnMesa72Hr);
        this.compraMnMesaVFut = new BigDecimal(compraMnMesaVFut);
        this.ventaMnMesaVFut = new BigDecimal(ventaMnMesaVFut);
        this.compraInCash = new BigDecimal(compraInCash);
        this.ventaInCash = new BigDecimal(ventaInCash);
        this.compraInTom = new BigDecimal(compraInTom);
        this.ventaInTom = new BigDecimal(ventaInTom);
        this.compraInSpot = new BigDecimal(compraInSpot);
        this.ventaInSpot = new BigDecimal(ventaInSpot);
        this.compraIn72Hr = new BigDecimal(compraIn72Hr);
        this.ventaIn72Hr = new BigDecimal(ventaIn72Hr);
        this.compraInVFut = new BigDecimal(compraInVFut);
        this.ventaInVFut = new BigDecimal(ventaInVFut);
    }

    /**
     * Constructor usado en los metodos Query que no tiene los datos id_divisa, id_mesa, id_canal y id_producto
     * Se utiliza en los queries getPosicionCanal, getPosicionProducto, getPosicionSucursales, getPosicionEfectivo
     *
     * @param compraCash El valor del tipo del cambio para Compra Cash.
     * @param ventaCash El valor del tipo del cambio para Venta Cash.
     * @param compraTom El valor del tipo del cambio para Compra Tom.
     * @param ventaTom El valor del tipo del cambio para Venta Tom.
     * @param compraSpot El valor del tipo del cambio para Compra Spot.
     * @param ventaSpot El valor del tipo del cambio para Venta Spot.
     * @param compra72Hr El valor del tipo del cambio para Compra VFut.
     * @param venta72Hr El valor del tipo del cambio para Venta VFUt.
     * @param compraVFut El valor del tipo del cambio para Compra 72Hr.
     * @param ventaVFut El valor del tipo del cambio para Venta 72Hr.
     * @param posicionInicialCash El monto de la Posici&oacute;n Inicial para Cash.
     * @param posicionInicialTom El monto de la Posici&oacute;n Inicial para Tom.
     * @param posicionInicialSpot El monto de la Posici&oacute;n Inicial para Spot.
     * @param posicionInicialMnCash El monto en moneda nacional de la Posici&oacute;n Inicial para Cash.
     * @param posicionInicialMnTom El monto en moneda nacional de la Posici&oacute;n Inicial para Tom.
     * @param posicionInicialMnSpot El monto en moneda nacional de la Posici&oacute;n Inicial para Spot.
     * @param posicionInicialMn72Hr El monto en moneda nacional de la Posici&oacute;n Inicial para 72Hr.
     * @param posicionInicialMnVFut El monto en moneda nacional de la Posici&oacute;n Inicial para VFut.
     * @param compraMnClienteCash El monto en moneda nacional del tipo de cambio para el cliente de Compra Cash.
     * @param ventaMnClienteCash El monto en moneda nacional del tipo de cambio para el cliente de Venta Cash.
     * @param compraMnClienteTom El monto en moneda nacional del tipo de cambio para el cliente de Compra Tom.
     * @param ventaMnClienteTom El monto en moneda nacional del tipo de cambio para el cliente de Venta Tom.
     * @param compraMnClienteSpot El monto en moneda nacional del tipo de cambio para el cliente de Compra Spot.
     * @param ventaMnClienteSpot El monto en moneda nacional del tipo de cambio para el cliente de Venta Spot.
     * @param compraMnCliente72Hr El monto en moneda nacional del tipo de cambio para el cliente de Compra 72Hr.
     * @param ventaMnCliente72Hr El monto en moneda nacional del tipo de cambio para el cliente de Venta 72Hr.
     * @param compraMnClienteVFut El monto en moneda nacional del tipo de cambio para el cliente de Compra VFut.
     * @param ventaMnClienteVFut El monto en moneda nacional del tipo de cambio para el cliente de Venta VFut.
     * @param compraMnPizarronCash El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Cash.
     * @param ventaMnPizarronCash El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Cash.
     * @param compraMnPizarronTom El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Tom.
     * @param ventaMnPizarronTom El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Tom.
     * @param compraMnPizarronSpot El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Spot.
     * @param ventaMnPizarronSpot El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Spot.
     * @param compraMnPizarron72Hr El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra 72Hr.
     * @param ventaMnPizarron72Hr El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta 72Hr.
     * @param compraMnPizarronVFut El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra VFut.
     * @param ventaMnPizarronVFut El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta VFut.
     * @param compraMnMesaCash El monto en moneda nacional del tipo de cambio para la mesa de Compra Cash.
     * @param ventaMnMesaCash El monto en moneda nacional del tipo de cambio para la mesa de Venta Cash.
     * @param compraMnMesaTom El monto en moneda nacional del tipo de cambio para la mesa de Compra Tom.
     * @param ventaMnMesaTom El monto en moneda nacional del tipo de cambio para la mesa de Venta Tom.
     * @param compraMnMesaSpot El monto en moneda nacional del tipo de cambio para la mesa de Compra Spot
     * @param ventaMnMesaSpot El monto en moneda nacional del tipo de cambio para la mesa de Venta Spot.
     * @param compraMnMesa72Hr El monto en moneda nacional del tipo de cambio para la mesa de Compra 72Hr.
     * @param ventaMnMesa72Hr El monto en moneda nacional del tipo de cambio para la mesa de Venta 72Hr.
     * @param compraMnMesaVFut El monto en moneda nacional del tipo de cambio para la mesa de Compra VFut.
     * @param ventaMnMesaVFut El monto en moneda nacional del tipo de cambio para la mesa de Venta VFut.
     * @param compraInCash El monto en moneda nacional del tipo de cambio para Compra Interbancaria Cash. 
     * @param ventaInCash El monto en moneda nacional del tipo de cambio para Venta Interbancaria Cash.
     * @param compraInTom El monto en moneda nacional del tipo de cambio para Compra Interbancaria Tom.
     * @param ventaInTom El monto en moneda nacional del tipo de cambio para Venta Interbancaria Tom.
     * @param compraInSpot El monto en moneda nacional del tipo de cambio para Compra Interbancaria Spot.
     * @param ventaInSpot El monto en moneda nacional del tipo de cambio para Venta Interbancaria Spot.
     * @param compraIn72Hr El monto en moneda nacional del tipo de cambio para Compra Interbancaria 72Hr.
     * @param ventaIn72Hr El monto en moneda nacional del tipo de cambio para Venta Interbancaria 72Hr.
     * @param compraInVFut El monto en moneda nacional del tipo de cambio para Compra Interbancaria VFut.
     * @param ventaInVFut El monto en moneda nacional del tipo de cambio para Venta Interbancaria VFut.
     */
    public PosicionVO(double compraCash, double ventaCash, double compraTom, double ventaTom, double compraSpot, double ventaSpot, double compra72Hr, double venta72Hr, double compraVFut, double ventaVFut,
                      double posicionInicialCash, double posicionInicialTom, double posicionInicialSpot, double posicionInicial72Hr, double posicionInicialVFut,
                      double posicionInicialMnCash, double posicionInicialMnTom, double posicionInicialMnSpot, double posicionInicialMn72Hr, double posicionInicialMnVFut,
                      double compraMnClienteCash, double ventaMnClienteCash, double compraMnClienteTom,
                      double ventaMnClienteTom, double compraMnClienteSpot, double ventaMnClienteSpot,
                      double compraMnCliente72Hr, double ventaMnCliente72Hr, double compraMnClienteVFut, double ventaMnClienteVFut,
                      double compraMnPizarronCash, double ventaMnPizarronCash, double compraMnPizarronTom,
                      double ventaMnPizarronTom, double compraMnPizarronSpot, double ventaMnPizarronSpot,
                      double compraMnPizarron72Hr, double ventaMnPizarron72Hr, double compraMnPizarronVFut, double ventaMnPizarronVFut,
                      double compraMnMesaCash, double ventaMnMesaCash, double compraMnMesaTom, double ventaMnMesaTom,
                      double compraMnMesaSpot, double ventaMnMesaSpot, double compraMnMesa72Hr, double ventaMnMesa72Hr,
                      double compraMnMesaVFut, double ventaMnMesaVFut, double compraInCash, double ventaInCash,
                      double compraInTom, double ventaInTom, double compraInSpot, double ventaInSpot, double compraIn72Hr,double ventaIn72Hr,
                      double compraInVFut, double ventaInVFut ) {
        this.compraCash = new BigDecimal(compraCash);
        this.ventaCash = new BigDecimal(ventaCash);
        this.compraTom = new BigDecimal(compraTom);
        this.ventaTom = new BigDecimal(ventaTom);
        this.compraSpot = new BigDecimal(compraSpot);
        this.ventaSpot = new BigDecimal(ventaSpot);
        this.compra72Hr = new BigDecimal(compra72Hr);
        this.venta72Hr = new BigDecimal(venta72Hr);
        this.compraVFut = new BigDecimal(compraVFut);
        this.ventaVFut = new BigDecimal(ventaVFut);
        this.posicionInicialCash = new BigDecimal(posicionInicialCash);
        this.posicionInicialTom = new BigDecimal(posicionInicialTom);
        this.posicionInicialSpot = new BigDecimal(posicionInicialSpot);
        this.posicionInicial72Hr = new BigDecimal(posicionInicial72Hr);
        this.posicionInicialVFut = new BigDecimal(posicionInicialVFut);
        this.posicionInicialMnCash = new BigDecimal(posicionInicialMnCash);
        this.posicionInicialMnTom = new BigDecimal(posicionInicialMnTom);
        this.posicionInicialMnSpot = new BigDecimal(posicionInicialMnSpot);
        this.posicionInicialMn72Hr = new BigDecimal(posicionInicialMn72Hr);
        this.posicionInicialMnVFut = new BigDecimal(posicionInicialMnVFut);
        this.compraMnClienteCash = new BigDecimal(compraMnClienteCash);
        this.ventaMnClienteCash = new BigDecimal(ventaMnClienteCash);
        this.compraMnClienteTom = new BigDecimal(compraMnClienteTom);
        this.ventaMnClienteTom = new BigDecimal(ventaMnClienteTom);
        this.compraMnClienteSpot = new BigDecimal(compraMnClienteSpot);
        this.ventaMnClienteSpot = new BigDecimal(ventaMnClienteSpot);
        this.compraMnCliente72Hr = new BigDecimal(compraMnCliente72Hr);
        this.ventaMnCliente72Hr = new BigDecimal(ventaMnCliente72Hr);
        this.compraMnClienteVFut = new BigDecimal(compraMnClienteVFut);
        this.ventaMnClienteVFut = new BigDecimal(ventaMnClienteVFut);
        this.compraMnPizarronCash = new BigDecimal(compraMnPizarronCash);
        this.ventaMnPizarronCash = new BigDecimal(ventaMnPizarronCash);
        this.compraMnPizarronTom = new BigDecimal(compraMnPizarronTom);
        this.ventaMnPizarronTom = new BigDecimal(ventaMnPizarronTom);
        this.compraMnPizarronSpot = new BigDecimal(compraMnPizarronSpot);
        this.ventaMnPizarronSpot = new BigDecimal(ventaMnPizarronSpot);
        this.compraMnPizarron72Hr = new BigDecimal(compraMnPizarron72Hr);
        this.ventaMnPizarron72Hr = new BigDecimal(ventaMnPizarron72Hr);
        this.compraMnPizarronVFut = new BigDecimal(compraMnPizarronVFut);
        this.ventaMnPizarronVFut = new BigDecimal(ventaMnPizarronVFut);
        this.compraMnMesaCash = new BigDecimal(compraMnMesaCash);
        this.ventaMnMesaCash = new BigDecimal(ventaMnMesaCash);
        this.compraMnMesaTom = new BigDecimal(compraMnMesaTom);
        this.ventaMnMesaTom = new BigDecimal(ventaMnMesaTom);
        this.compraMnMesaSpot = new BigDecimal(compraMnMesaSpot);
        this.ventaMnMesaSpot = new BigDecimal(ventaMnMesaSpot);
        this.compraMnMesa72Hr = new BigDecimal(compraMnMesa72Hr);
        this.ventaMnMesa72Hr = new BigDecimal(ventaMnMesa72Hr);
        this.compraMnMesaVFut = new BigDecimal(compraMnMesaVFut);
        this.ventaMnMesaVFut = new BigDecimal(ventaMnMesaVFut);
        this.compraInCash = new BigDecimal(compraInCash);
        this.ventaInCash = new BigDecimal(ventaInCash);
        this.compraInTom = new BigDecimal(compraInTom);
        this.ventaInTom = new BigDecimal(ventaInTom);
        this.compraInSpot = new BigDecimal(compraInSpot);
        this.ventaInSpot = new BigDecimal(ventaInSpot);
        this.compraIn72Hr = new BigDecimal(compraIn72Hr);
        this.ventaIn72Hr = new BigDecimal(ventaIn72Hr);
        this.compraInVFut = new BigDecimal(compraInVFut);
        this.ventaInVFut = new BigDecimal(ventaInVFut);
    }

    /**
     * Constructor para la posicion divisa
     *
     @param compraCash El valor del tipo del cambio para Compra Cash.
     * @param ventaCash El valor del tipo del cambio para Venta Cash.
     * @param compraTom El valor del tipo del cambio para Compra Tom.
     * @param ventaTom El valor del tipo del cambio para Venta Tom.
     * @param compraSpot El valor del tipo del cambio para Compra Spot.
     * @param ventaSpot El valor del tipo del cambio para Venta Spot.
     * @param compra72Hr El valor del tipo del cambio para Compra VFut.
     * @param venta72Hr El valor del tipo del cambio para Venta VFUt.
     * @param compraVFut El valor del tipo del cambio para Compra 72Hr.
     * @param ventaVFut El valor del tipo del cambio para Venta 72Hr.
     * @param posicionInicialCash El monto de la Posici&oacute;n Inicial para Cash.
     * @param posicionInicialTom El monto de la Posici&oacute;n Inicial para Tom.
     * @param posicionInicialSpot El monto de la Posici&oacute;n Inicial para Spot.
     * @param posicionInicialMnCash El monto en moneda nacional de la Posici&oacute;n Inicial para Cash.
     * @param posicionInicialMnTom El monto en moneda nacional de la Posici&oacute;n Inicial para Tom.
     * @param posicionInicialMnSpot El monto en moneda nacional de la Posici&oacute;n Inicial para Spot.
     * @param posicionInicialMn72Hr El monto en moneda nacional de la Posici&oacute;n Inicial para 72Hr.
     * @param posicionInicialMnVFut El monto en moneda nacional de la Posici&oacute;n Inicial para VFut.
     * @param compraMnPizarronCash El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Cash.
     * @param ventaMnPizarronCash El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Cash.
     * @param compraMnPizarronTom El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Tom.
     * @param ventaMnPizarronTom El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Tom.
     * @param compraMnPizarronSpot El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra Spot.
     * @param ventaMnPizarronSpot El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta Spot.
     * @param compraMnPizarron72Hr El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra 72Hr.
     * @param ventaMnPizarron72Hr El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta 72Hr.
     * @param compraMnPizarronVFut El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Compra VFut.
     * @param ventaMnPizarronVFut El monto en moneda nacional del tipo de cambio para del pizarr&oacute;n de Venta VFut.
     * @param compraInCash El monto en moneda nacional del tipo de cambio para Compra Interbancaria Cash. 
     * @param ventaInCash El monto en moneda nacional del tipo de cambio para Venta Interbancaria Cash.
     * @param compraInTom El monto en moneda nacional del tipo de cambio para Compra Interbancaria Tom.
     * @param ventaInTom El monto en moneda nacional del tipo de cambio para Venta Interbancaria Tom.
     * @param compraInSpot El monto en moneda nacional del tipo de cambio para Compra Interbancaria Spot.
     * @param ventaInSpot El monto en moneda nacional del tipo de cambio para Venta Interbancaria Spot.
     * @param compraIn72Hr El monto en moneda nacional del tipo de cambio para Compra Interbancaria 72Hr.
     * @param ventaIn72Hr El monto en moneda nacional del tipo de cambio para Venta Interbancaria 72Hr.
     * @param compraInVFut El monto en moneda nacional del tipo de cambio para Compra Interbancaria VFut.
     * @param ventaInVFut El monto en moneda nacional del tipo de cambio para Venta Interbancaria VFut.
     * @param idDivisa El id de la divisa.
     */
    public PosicionVO(double compraCash, double ventaCash, double compraTom, double ventaTom, double compraSpot, double ventaSpot, double compra72Hr, double venta72Hr, double compraVFut, double ventaVFut,
                      double posicionInicialCash, double posicionInicialTom, double posicionInicialSpot, double posicionInicial72Hr, double posicionInicialVFut,
                      double posicionInicialMnCash, double posicionInicialMnTom, double posicionInicialMnSpot, double posicionInicialMn72Hr, double posicionInicialMnVFut,
                      double compraMnPizarronCash, double ventaMnPizarronCash, double compraMnPizarronTom, double ventaMnPizarronTom, double compraMnPizarronSpot, double ventaMnPizarronSpot, 
                      double compraMnPizarron72Hr, double ventaMnPizarron72Hr, double compraMnPizarronVFut, double ventaMnPizarronVFut, 
                      double compraInCash, double ventaInCash, double compraInTom, double ventaInTom, double compraInSpot, double ventaInSpot, 
                      double compraIn72Hr, double ventaIn72Hr, double compraInVFut, double ventaInVFut, String idDivisa) {
        this.compraCash = new BigDecimal(compraCash);
        this.ventaCash = new BigDecimal(ventaCash);
        this.compraTom = new BigDecimal(compraTom);
        this.ventaTom = new BigDecimal(ventaTom);
        this.compraSpot = new BigDecimal(compraSpot);
        this.ventaSpot = new BigDecimal(ventaSpot);
        this.compra72Hr = new BigDecimal(compra72Hr);
        this.venta72Hr = new BigDecimal(venta72Hr);
        this.compraVFut = new BigDecimal(compraVFut);
        this.ventaVFut = new BigDecimal(ventaVFut);
        this.posicionInicialCash = new BigDecimal(posicionInicialCash);
        this.posicionInicialTom = new BigDecimal(posicionInicialTom);
        this.posicionInicialSpot = new BigDecimal(posicionInicialSpot);
        this.posicionInicial72Hr = new BigDecimal(posicionInicial72Hr);
        this.posicionInicialVFut = new BigDecimal(posicionInicialVFut);
        this.posicionInicialMnCash = new BigDecimal(posicionInicialMnCash);
        this.posicionInicialMnTom = new BigDecimal(posicionInicialMnTom);
        this.posicionInicialMnSpot = new BigDecimal(posicionInicialMnSpot);
        this.posicionInicialMn72Hr = new BigDecimal(posicionInicialMn72Hr);
        this.posicionInicialMnVFut = new BigDecimal(posicionInicialMnVFut);
        this.compraMnPizarronCash = new BigDecimal(compraMnPizarronCash);
        this.ventaMnPizarronCash = new BigDecimal(ventaMnPizarronCash);
        this.compraMnPizarronTom = new BigDecimal(compraMnPizarronTom);
        this.ventaMnPizarronTom = new BigDecimal(ventaMnPizarronTom);
        this.compraMnPizarronSpot = new BigDecimal(compraMnPizarronSpot);
        this.ventaMnPizarronSpot = new BigDecimal(ventaMnPizarronSpot);
        this.compraMnPizarron72Hr = new BigDecimal(compraMnPizarron72Hr);
        this.ventaMnPizarron72Hr = new BigDecimal(ventaMnPizarron72Hr);
        this.compraMnPizarronVFut = new BigDecimal(compraMnPizarronVFut);
        this.ventaMnPizarronVFut = new BigDecimal(ventaMnPizarronVFut);
        this.compraInCash = new BigDecimal(compraInCash);
        this.ventaInCash = new BigDecimal(ventaInCash);
        this.compraInTom = new BigDecimal(compraInTom);
        this.ventaInTom = new BigDecimal(ventaInTom);
        this.compraInSpot = new BigDecimal(compraInSpot);
        this.ventaInSpot = new BigDecimal(ventaInSpot);
        this.compraIn72Hr = new BigDecimal(compraIn72Hr);
        this.ventaIn72Hr = new BigDecimal(ventaIn72Hr);
        this.compraInVFut = new BigDecimal(compraInVFut);
        this.ventaInVFut = new BigDecimal(ventaInVFut);
        this.idDivisa = idDivisa;
    }
    
    /**
     * Obtiene el monto de la Posici&oacute;n Inicial, sumando los montos
     * de las fechas valor incluyendo 72Hr y VFut.
     * 
     * @return BigDecimal
     */
    public BigDecimal getPosicionInicial() {
        return getPosicionInicialCash().add(getPosicionInicialTom()).add(getPosicionInicialSpot()).add(getPosicionInicial72Hr()).add(getPosicionInicialVFut());
    }
    
    /**
     * Obtiene el monto en moneda nacional de la Posici&oacute;n Inicial, 
     * sumando los montos de las fechas valor incluyendo 72Hr y VFut.
     * 
     * @return
     */
    public BigDecimal getPosicionInicialMn() {
        return getPosicionInicialMnCash().add(getPosicionInicialMnTom()).add(getPosicionInicialMnSpot()).add(getPosicionInicialMn72Hr()).add(getPosicionInicialMnVFut());
    }
     
    /**
     * Regresa las compras en cash
     *
     * @return BigDecimal
     */
    public BigDecimal getCompraCash() {
        return compraCash;
    }

    /**
     * Asigna las compras en cash
     *
     * @param compraCash
     */
    public void setCompraCash(BigDecimal compraCash) {
        this.compraCash = compraCash;
    }

    /**
     * Regresa las ventas en cash
     *
     * @return BigDecimal
     */
    public BigDecimal getVentaCash() {
        return ventaCash;
    }

    /**
     * Asigna las ventas en cash
     *
     * @param ventaCash
     */
    public void setVentaCash(BigDecimal ventaCash) {
        this.ventaCash = ventaCash;
    }

    /**
     * Regresa las compras en Tom
     *
     * @return BigDecimal
     */
    public BigDecimal getCompraTom() {
        return compraTom;
    }

    /**
     * Asigna las compras en tom
     *
     * @param compraTom
     */
    public void setCompraTom(BigDecimal compraTom) {
        this.compraTom = compraTom;
    }

    /**
     * Regresa las ventas en Tom
     *
     * @return BigDecimal
     */
    public BigDecimal getVentaTom() {
        return ventaTom;
    }

    /**
     * Regresa las compras en spot
     *
     * @return BigDecimal
     */
    public BigDecimal getCompraSpot() {
        return compraSpot;
    }

    /**
     * Asigna las compras en spot
     *
     * @param compraSpot
     */
    public void setCompraSpot(BigDecimal compraSpot) {
        this.compraSpot = compraSpot;
    }

    /**
     * Asigna las ventas en tom
     *
     * @param ventaTom
     */
    public void setVentaTom(BigDecimal ventaTom) {
        this.ventaTom = ventaTom;
    }

    /**
     * Regresa las ventas en spot
     *
     * @return BigDecimal
     */
    public BigDecimal getVentaSpot() {
        return ventaSpot;
    }

    /**
     * Asigna las ventas en spot
     *
     * @param ventaSpot
     */
    public void setVentaSpot(BigDecimal ventaSpot) {
        this.ventaSpot = ventaSpot;
    }

    /**
     * Regresa el valor de posicionInicialCash.
     *
     * @return BigDecimal.
     */
    public BigDecimal getPosicionInicialCash() {
        return posicionInicialCash;
    }

    /**
     * Establece el valor de posicionInicialCash.
     *
     * @param posicionInicialCash El valor a asignar.
     */
    public void setPosicionInicialCash(BigDecimal posicionInicialCash) {
        this.posicionInicialCash = posicionInicialCash;
    }

    /**
     * Regresa el valor de posicionInicialTom.
     *
     * @return BigDecimal.
     */
    public BigDecimal getPosicionInicialTom() {
        return posicionInicialTom;
    }

    /**
     * Establece el valor de posicionInicialTom.
     *
     * @param posicionInicialTom El valor a asignar.
     */
    public void setPosicionInicialTom(BigDecimal posicionInicialTom) {
        this.posicionInicialTom = posicionInicialTom;
    }

    /**
     * Regresa el valor de posicionInicialSpot.
     *
     * @return posicionInicialSpot.
     */
    public BigDecimal getPosicionInicialSpot() {
        return posicionInicialSpot;
    }

    /**
     * Establece el valor de posicionInicialSpot.
     *
     * @param posicionInicialSpot El valor a asignar.
     */
    public void setPosicionInicialSpot(BigDecimal posicionInicialSpot) {
        this.posicionInicialSpot = posicionInicialSpot;
    }
    
    /**
     * Regresa el valor de posicionInicial72Hr
     * 
     * @return BigDecimal
     */
	public BigDecimal getPosicionInicial72Hr() {
		return posicionInicial72Hr;
	}
	
	/**
	 * Establece el valor de posicionInicial72Hr
	 * 
	 * @param posicionInicial72Hr El valor a asignar
	 */
	public void setPosicionInicial72Hr(BigDecimal posicionInicial72Hr) {
		this.posicionInicial72Hr = posicionInicial72Hr;
	}
	
	/**
	 * Establece el valor de posicionInicialVFut
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPosicionInicialVFut() {
		return posicionInicialVFut;
	}

	/**
	 * Regresa el valor de posicionInicialVFut
	 * 
	 * @param posicionInicialVFut El valor a asignar
	 */
	public void setPosicionInicialVFut(BigDecimal posicionInicialVFut) {
		this.posicionInicialVFut = posicionInicialVFut;
	}
	
    /**
     * Regresa el valor de posicionInicialMnCash.
     *
     * @return BigDecimal.
     */
    public BigDecimal getPosicionInicialMnCash() {
        return posicionInicialMnCash;
    }

    /**
     * Establece el valor de posicionInicialMnCash.
     *
     * @param posicionInicialMnCash El valor a asignar.
     */
    public void setPosicionInicialMnCash(BigDecimal posicionInicialMnCash) {
        this.posicionInicialMnCash = posicionInicialMnCash;
    }

    /**
     * Regresa el valor de posicionInicialMnTom.
     *
     * @return BigDecimal.
     */
    public BigDecimal getPosicionInicialMnTom() {
        return posicionInicialMnTom;
    }

    /**
     * Establece el valor de posicionInicialMnTom.
     *
     * @param posicionInicialMnTom El valor a asignar.
     */
    public void setPosicionInicialMnTom(BigDecimal posicionInicialMnTom) {
        this.posicionInicialMnTom = posicionInicialMnTom;
    }

    /**
     * Regresa el valor de posicionInicialMnSpot.
     *
     * @return BigDecimal.
     */
    public BigDecimal getPosicionInicialMnSpot() {
        return posicionInicialMnSpot;
    }

    /**
     * Establece el valor de posicionInicialMnSpot.
     *
     * @param posicionInicialMnSpot El valor a asignar.
     */
    public void setPosicionInicialMnSpot(BigDecimal posicionInicialMnSpot) {
        this.posicionInicialMnSpot = posicionInicialMnSpot;
    }
    
    /**
     * Regresa el valor de posicionInicialMn72Hr.
     * 
     * @return BigDecimal
     */
    public BigDecimal getPosicionInicialMn72Hr() {
		return posicionInicialMn72Hr;
	}

    /**
     * Establece el valor de posicionInicialMn72Hr.
     * 
     * @param posicionInicialMn72Hr El valor a asignar
     */
	public void setPosicionInicialMn72Hr(BigDecimal posicionInicialMn72Hr) {
		this.posicionInicialMn72Hr = posicionInicialMn72Hr;
	}
	
	/**
	 * Regresa el valor de posicionInicialMnVFut.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPosicionInicialMnVFut() {
		return posicionInicialMnVFut;
	}

	/**
	 * Establece el valor de posicionInicialMnVFut.
	 * 
	 * @param posicionInicialMnVFut El valor a asignar
	 */
	public void setPosicionInicialMnVFut(BigDecimal posicionInicialMnVFut) {
		this.posicionInicialMnVFut = posicionInicialMnVFut;
	}
    
    /**
     * Regresa las compras del cliente en moneda nacional cash
     *
     * @return BigDecimal
     */
    public BigDecimal getCompraMnClienteCash() {
        return compraMnClienteCash;
    }

    /**
     * Asigna las compras del cliente en moneda nacional cash
     *
     * @param compraMnClienteCash
     */
    public void setCompraMnClienteCash(BigDecimal compraMnClienteCash) {
        this.compraMnClienteCash = compraMnClienteCash;
    }

    /**
     * Regresa las ventas del cliente en moneda nacional cash
     *
     * @return BigDecimal
     */
    public BigDecimal getVentaMnClienteCash() {
        return ventaMnClienteCash;
    }

    /**
     * Asigna las ventas del cliente en moneda nacional cash
     *
     * @param ventaMnClienteCash
     */
    public void setVentaMnClienteCash(BigDecimal ventaMnClienteCash) {
        this.ventaMnClienteCash = ventaMnClienteCash;
    }

    /**
     * Regresa las compras del cliente en moneda nacional tom
     *
     * @return BigDecimal
     */
    public BigDecimal getCompraMnClienteTom() {
        return compraMnClienteTom;
    }

    /**
     * Asigna las compras del cliente en moneda nacional tom
     *
     * @param compraMnClienteTom
     */
    public void setCompraMnClienteTom(BigDecimal compraMnClienteTom) {
        this.compraMnClienteTom = compraMnClienteTom;
    }

    /**
     * Regresa las ventas del cliente en moneda nacional tom
     *
     * @return BigDecimal
     */
    public BigDecimal getVentaMnClienteTom() {
        return ventaMnClienteTom;
    }

    /**
     * Asigna las ventas del cliente en moneda nacional tom
     *
     * @param ventaMnClienteTom
     */
    public void setVentaMnClienteTom(BigDecimal ventaMnClienteTom) {
        this.ventaMnClienteTom = ventaMnClienteTom;
    }

    /**
     * Regresa las compras del cliente en moneda nacional spot
     *
     * @return BigDecimal
     */
    public BigDecimal getCompraMnClienteSpot() {
        return compraMnClienteSpot;
    }

    /**
     * Asigna las compras del cliente en moneda nacional spot
     *
     * @param compraMnClienteSpot
     */
    public void setCompraMnClienteSpot(BigDecimal compraMnClienteSpot) {
        this.compraMnClienteSpot = compraMnClienteSpot;
    }

    /**
     * Regresa las ventas del cliente en moneda nacional spot
     *
     * @return BigDecimal
     */
    public BigDecimal getVentaMnClienteSpot() {
        return ventaMnClienteSpot;
    }

    /**
     * Asigna las ventas del cliente en moneda nacional spot
     *
     * @param ventaMnClienteSpot
     */
    public void setVentaMnClienteSpot(BigDecimal ventaMnClienteSpot) {
        this.ventaMnClienteSpot = ventaMnClienteSpot;
    }

    /**
     * Regresa las compras del pizarron en moneda nacional cash
     *
     * @return BigDecimal
     */
    public BigDecimal getCompraMnPizarronCash() {
        return compraMnPizarronCash;
    }

    /**
     * Asigna las compras del pizarron en moneda nacional cash
     *
     * @param compraMnPizarronCash El valor a asignar.
     */
    public void setCompraMnPizarronCash(BigDecimal compraMnPizarronCash) {
        this.compraMnPizarronCash = compraMnPizarronCash;
    }

    /**
     * Regresa las ventas del pizarron en moneda nacional cash
     *
     * @return BigDecimal
     */
    public BigDecimal getVentaMnPizarronCash() {
        return ventaMnPizarronCash;
    }

    /**
     * Asigna las ventas del pizarron en moneda nacional cash
     *
     * @param ventaMnPizarronCash
     */
    public void setVentaMnPizarronCash(BigDecimal ventaMnPizarronCash) {
        this.ventaMnPizarronCash = ventaMnPizarronCash;
    }

    /**
     * Regresa las compras del pizarron en moneda nacional tom
     *
     * @return BigDecimal
     */
    public BigDecimal getCompraMnPizarronTom() {
        return compraMnPizarronTom;
    }

    /**
     * Asigna las compras del pizarron en moneda nacional tom
     *
     * @param compraMnPizarronTom
     */
    public void setCompraMnPizarronTom(BigDecimal compraMnPizarronTom) {
        this.compraMnPizarronTom = compraMnPizarronTom;
    }

    /**
     * Regresa las ventas del pizarron en moneda nacional tom
     *
     * @return BigDecimal
     */
    public BigDecimal getVentaMnPizarronTom() {
        return ventaMnPizarronTom;
    }

    /**
     * Asigna las ventas del pizarron en moneda nacional tom
     *
     * @param ventaMnPizarronTom
     */
    public void setVentaMnPizarronTom(BigDecimal ventaMnPizarronTom) {
        this.ventaMnPizarronTom = ventaMnPizarronTom;
    }

    /**
     * Regresa las compras del pizarron en moneda nacional spot
     *
     * @return BigDecimal
     */
    public BigDecimal getCompraMnPizarronSpot() {
        return compraMnPizarronSpot;
    }

    /**
     * Asigna las compras del pizarron en moneda nacional spot
     *
     * @param compraMnPizarronSpot
     */
    public void setCompraMnPizarronSpot(BigDecimal compraMnPizarronSpot) {
        this.compraMnPizarronSpot = compraMnPizarronSpot;
    }

    /**
     * Regresa las ventas del pizarron en moneda nacional spot
     *
     * @return BigDecimal
     */
    public BigDecimal getVentaMnPizarronSpot() {
        return ventaMnPizarronSpot;
    }

    /**
     * Asigna las ventas del pizarron en moneda nacional spot
     *
     * @param ventaMnPizarronSpot
     */
    public void setVentaMnPizarronSpot(BigDecimal ventaMnPizarronSpot) {
        this.ventaMnPizarronSpot = ventaMnPizarronSpot;
    }

    /**
     * Regresa las compras del mesa en moneda nacional cash
     *
     * @return BigDecimal
     */
    public BigDecimal getCompraMnMesaCash() {
        return compraMnMesaCash;
    }

    /**
     * Asigna las compras del mesa en moneda nacional cash
     *
     * @param compraMnMesaCash
     */
    public void setCompraMnMesaCash(BigDecimal compraMnMesaCash) {
        this.compraMnMesaCash = compraMnMesaCash;
    }

    /**
     * Regresa las ventas del mesa en moneda nacional cash
     *
     * @return BigDecimal
     */
    public BigDecimal getVentaMnMesaCash() {
        return ventaMnMesaCash;
    }

    /**
     * Asigna las ventas de la mesa en moneda nacional cash
     *
     * @param ventaMnMesaCash
     */
    public void setVentaMnMesaCash(BigDecimal ventaMnMesaCash) {
        this.ventaMnMesaCash = ventaMnMesaCash;
    }

    /**
     * Regresa las compras de la mesa en moneda nacional tom
     *
     * @return BigDecimal
     */
    public BigDecimal getCompraMnMesaTom() {
        return compraMnMesaTom;
    }

    /**
     * Asigna las compras de la mesa en moneda nacional tom
     *
     * @param compraMnMesaTom
     */
    public void setCompraMnMesaTom(BigDecimal compraMnMesaTom) {
        this.compraMnMesaTom = compraMnMesaTom;
    }

    /**
     * Regresa las ventas de la mesa en moneda nacional tom
     *
     * @return BigDecimal
     */
    public BigDecimal getVentaMnMesaTom() {
        return ventaMnMesaTom;
    }

    /**
     * Asigna las ventas de la mesa en moneda nacional tom
     *
     * @param ventaMnMesaTom
     */
    public void setVentaMnMesaTom(BigDecimal ventaMnMesaTom) {
        this.ventaMnMesaTom = ventaMnMesaTom;
    }

    /**
     * Regresa las compras de la mesa en moneda nacional spot
     *
     * @return BigDecimal
     */
    public BigDecimal getCompraMnMesaSpot() {
        return compraMnMesaSpot;
    }

    /**
     * Asigna las compras de la mesa en moneda nacional spot
     *
     * @param compraMnMesaSpot
     */
    public void setCompraMnMesaSpot(BigDecimal compraMnMesaSpot) {
        this.compraMnMesaSpot = compraMnMesaSpot;
    }

    /**
     * Regresa las ventas de la mesa en moneda nacional spot
     *
     * @return BigDecimal
     */
    public BigDecimal getVentaMnMesaSpot() {
        return ventaMnMesaSpot;
    }

    /**
     * Asigna las ventas de la mesa en moneda nacional spot
     *
     * @param ventaMnMesaSpot
     */
    public void setVentaMnMesaSpot(BigDecimal ventaMnMesaSpot) {
        this.ventaMnMesaSpot = ventaMnMesaSpot;
    }

    /**
     * Regresa el tipo de cambio del mercado
     *
     * @return BigDecimal
     */
    public BigDecimal getTipoCambioMercado() {
        return tipoCambioMercado;
    }

    /**
     * Asigna el tipo de cambio del mercado
     *
     * @param tipoCambioMercado
     */
    public void setTipoCambioMercado(BigDecimal tipoCambioMercado) {
        this.tipoCambioMercado = tipoCambioMercado;
    }

    /**
     * Regresa el identificador de la mesa de cambio
     *
     * @return Integer
     */
    public Integer getIdMesaCambio() {
        return idMesaCambio;
    }

    /**
     * Asigna el identificador de la mesa de cambio
     *
     * @param idMesaCambio
     */
    public void setIdMesaCambio(Integer idMesaCambio) {
        this.idMesaCambio = idMesaCambio;
    }

    /**
     * Regresa el identificador de la divisa
     *
     * @return String
     */
    public String getIdDivisa() {
        return idDivisa;
    }

    /**
     * Asigna el identificador de la divisa
     *
     * @param idDivisa
     */
    public void setIdDivisa(String idDivisa) {
        this.idDivisa = idDivisa;
    }

    /**
     * Regresa el identificador del canal
     *
     * @return String
     */
    public String getIdCanal() {
        return idCanal;
    }

    /**
     * Asigna el identificador del canal
     *
     * @param idCanal
     */
    public void setIdCanal(String idCanal) {
        this.idCanal = idCanal;
    }

    /**
     * Regresa el identificador del producto
     *
     * @return String
     */
    public String getIdProducto() {
        return idProducto;
    }

    /**
     * Asigna el identificador del producto
     *
     * @param idProducto
     */
    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Regresa la utilidad global
     *
     * @return BigDecimal
     */
    public BigDecimal getUtilidadRealizadaGlobal() {
        return utilidadRealizadaGlobal;
    }

    /**
     * Regresa la utilidad tom
     *
     * @return BigDecimal
     */
    public BigDecimal getUtilidadRealizadaMesa() {
        return utilidadRealizadaMesa;
    }

    /**
     * Regresa el valor de documentosTransferencias.
     * 
     * @return BigDecimal.
     */
    public BigDecimal getDocumentosTransferencias() {
        return documentosTransferencias;
    }

    /**
     * Regresa el valor de documentosTransferenciasMnCliente.
     * 
     * @return BigDecimal
     */
    public BigDecimal getDocumentosTransferenciasMnCliente() {
        return documentosTransferenciasMnCliente;
    }

    /**
     * Regresa el valor de documentosTransferenciasMnMesa.
     * 
     * @return BigDecimal
     */
    public BigDecimal getDocumentosTransferenciasMnMesa() {
        return documentosTransferenciasMnMesa;
    }

    /**
     * Regresa el valor de documentosTransferenciasMnPizarron.
     * 
     * @return BigDecimal
     */
    public BigDecimal getDocumentosTransferenciasMnPizarron() {
        return documentosTransferenciasMnPizarron;
    }

    /**
     * Regresa el valor de efectivo.
     * 
     * @return BigDecimal.
     */
    public BigDecimal getEfectivo() {
        return efectivo;
    }

    /**
     * Regresa el valor de efectivoMnCliente.
     * 
     * @return BigDecimal
     */
    public BigDecimal getEfectivoMnCliente() {
        return efectivoMnCliente;
    }

    /**
     * Regresa el valor de efectivoMnMesa.
     * 
     * @return BigDecimal
     */
    public BigDecimal getEfectivoMnMesa() {
        return efectivoMnMesa;
    }

    /**
     * Regresa el valor de efectivoMnPizarron.
     * 
     * @return BigDecimal
     */
    public BigDecimal getEfectivoMnPizarron() {
        return efectivoMnPizarron;
    }

    /**
     * Regresa el valor de compraInCash.
     * 
     * @return BigDecimal.
     */
    public BigDecimal getCompraInCash() {
        return compraInCash;
    }
    
    /**
     * Asigna el valor para compraInCash
     * 
     * @param compraInCash El valor para compraInCash
     */
    public void setCompraInCash(BigDecimal compraInCash) {
        this.compraInCash = compraInCash;
    }

    /**
     * Regresa el valor de ventaInCash.
     * 
     * @return BigDecimal.
     */
    public BigDecimal getVentaInCash() {
        return ventaInCash;
    }

    /**
     * Asigna el valor para ventaInCash.
     * 
     * @param ventaInCash El valor para ventaInCash. 
     */
    public void setVentaInCash(BigDecimal ventaInCash) {
        this.ventaInCash = ventaInCash;
    }

    /**
     * Regresa el valor de compraInSpot.
     * 
     * @return BigDecimal.
     */
    public BigDecimal getCompraInSpot() {
        return compraInSpot;
    }

    /**
     * Asigna el valor para compraInSpot
     * 
     * @param compraInSpot El valor para compraInSpot.
     */
    public void setCompraInSpot(BigDecimal compraInSpot) {
        this.compraInSpot = compraInSpot;
    }

    /**
     * Regresa el valor de ventaInSpot.
     * 
     * @return BigDecimal.
     */
    public BigDecimal getVentaInSpot() {
        return ventaInSpot;
    }

    /**
     * Asigna el valor para ventaInSpot.
     * 
     * @param ventaInSpot El valor para ventaInSpot.
     */
    public void setVentaInSpot(BigDecimal ventaInSpot) {
        this.ventaInSpot = ventaInSpot;
    }

    /**
     * Regresa el valor de CompraInTom
     * 
     * @return BigDecimal 
     */
    public BigDecimal getCompraInTom() {
        return compraInTom;
    }

    /**
     * Asigna el valor para compra compraInSpot.
     * 
     * @param compraInTom El valor para compraInSpot.
     */
    public void setCompraInTom(BigDecimal compraInTom) {
        this.compraInTom = compraInTom;
    }

    /**
     * Regresa el valor de ventaInTom.
     * 
     * @return BigDecimal.
     */
    public BigDecimal getVentaInTom() {
        return ventaInTom;
    }
    
    /**
     * Asigna el valor para ventaInTom.
     * 
     * @param ventaInTom El valor para ventaInTom.
     */
    public void setVentaInTom(BigDecimal ventaInTom) {
        this.ventaInTom = ventaInTom;
    }

    /**
     * Asigna los montos de efectivo y transferencias para cliente, pizarr&oacute;n y mesa
     * en divisa y moneda nacional.
     * 
     * @param efectivo El monto en efectivo de la divisa.
     * @param documentosTransferencias El monto de la transferencia en divisa.
     * @param documentosTransferenciasMnCliente El monto de la transferencia moneda nacional para el cliente.
     * @param documentosTransferenciasMnPizarron El monto de la transferencia moneda nacional del pizarr&oacute;n.
     * @param documentosTransferenciasMnMesa El monto de la transferencia moneda nacional para la mesa.
     * @param efectivoMnCliente El monto en efectivo para el cliente.
     * @param efectivoMnPizarron El monto en efectivo del pizarr&oacute;n.
     * @param efectivoMnMesa El monto en efectivo para la mesa.
     */ 
    public void completarInformacionDivisa(BigDecimal efectivo, BigDecimal documentosTransferencias,
                                           BigDecimal documentosTransferenciasMnCliente, BigDecimal documentosTransferenciasMnPizarron,
                                           BigDecimal documentosTransferenciasMnMesa, BigDecimal efectivoMnCliente, BigDecimal efectivoMnPizarron,
                                           BigDecimal efectivoMnMesa) {
        this.efectivo = efectivo;
        this.documentosTransferencias = documentosTransferencias;
        this.documentosTransferenciasMnCliente = documentosTransferenciasMnCliente;
        this.documentosTransferenciasMnPizarron = documentosTransferenciasMnPizarron;
        this.documentosTransferenciasMnMesa = documentosTransferenciasMnMesa;
        this.efectivoMnCliente = efectivoMnCliente;
        this.efectivoMnPizarron = efectivoMnPizarron;
        this.efectivoMnMesa = efectivoMnMesa;
    }
    
    /**
     * Regresa el valor de posicionInicialTeller.
     * 
     * @return BigDecimal.
     */
    public BigDecimal getPosicionInicialTeller() {
        return posicionInicialTeller;
    }

    /**
     * Asigna el valor para posicionInicialTeller.
     * 
     * @param posicionInicialTeller El valor para posicionInicialTeller.
     */
    public void setPosicionInicialTeller(BigDecimal posicionInicialTeller) {
        this.posicionInicialTeller = posicionInicialTeller;
    }

    /**
     * Regresa el valor de depositosEfectivoTeller.
     * 
     * @return BigDecimal
     */
    public BigDecimal getDepositosEfectivoTeller() {
        return depositosEfectivoTeller;
    }

    /**
     * Asigna el valor para depositosEfectivoTeller.
     * 
     * @param depositosEfectivoTeller El valor para depositosEfectivoTeller.
     */ 
    public void setDepositosEfectivoTeller(BigDecimal depositosEfectivoTeller) {
        this.depositosEfectivoTeller = depositosEfectivoTeller;
    }

    /**
     * Regresa el valor de depositosBovedaTeller.
     * 
     * @return BigDecimal.
     */
    public BigDecimal getDepositosBovedaTeller() {
        return depositosBovedaTeller;
    }
    
    /**
     * Asigna el valor para depositosBovedaTeller.
     * 
     * @param depositosBovedaTeller El valor para depositosBovedaTeller.
     */
    public void setDepositosBovedaTeller(BigDecimal depositosBovedaTeller) {
        this.depositosBovedaTeller = depositosBovedaTeller;
    }

    /**
     * Regresa el valor de retirosEfectivoTeller.
     * 
     * @return BigDecimal. 
     */
    public BigDecimal getRetirosEfectivoTeller() {
        return retirosEfectivoTeller;
    }

    /**
     * Asigna el valor para retirosEfectivosTeller.
     * 
     * @param retirosEfectivoTeller El valor de retirosEfectivoTeller.
     */
    public void setRetirosEfectivoTeller(BigDecimal retirosEfectivoTeller) {
        this.retirosEfectivoTeller = retirosEfectivoTeller;
    }

    /**
     * Regresa el valor de retirosBovedaTeller.
     * 
     * @return BigDecimal.
     */
    public BigDecimal getRetirosBovedaTeller() {
        return retirosBovedaTeller;
    }

    /**
     * Asigna el valor para retirosBovedaTeller.
     * 
     * @param retirosBovedaTeller El valor para retirosBovedaTeller. 
     */
    public void setRetirosBovedaTeller(BigDecimal retirosBovedaTeller) {
        this.retirosBovedaTeller = retirosBovedaTeller;
    }

    /**
     * Regresa el valor de compraMnCliente72Hr.
     * 
     * @return BigDecimal
     */
    public BigDecimal getCompraMnCliente72Hr() {
		return compraMnCliente72Hr;
	}
    
    /**
     * Asigna el valor para compraMnCliente72Hr.
     * 
     * @param compraMnCliente72Hr El valor para compraMnCliente72Hr.
     */
	public void setCompraMnCliente72Hr(BigDecimal compraMnCliente72Hr) {
		this.compraMnCliente72Hr = compraMnCliente72Hr;
	}
	
	/**
	 * Regresa el valor de comrpaMnClienteVFut.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCompraMnClienteVFut() {
		return compraMnClienteVFut;
	}

	/**
	 * Asigna el valor para compraMnClienteVFut.
	 * 
	 * @param compraMnClienteVFut El valor para compraMnClienteVFut.
	 */
	public void setCompraMnClienteVFut(BigDecimal compraMnClienteVFut) {
		this.compraMnClienteVFut = compraMnClienteVFut;
	}

	/**
	 * Regresa el valor de compraMnMesa72Hr.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCompraMnMesa72Hr() {
		return compraMnMesa72Hr;
	}

	/**
	 * Asigna el valor para compraMnMesa72Hr.
	 * 
	 * @param compraMnMesa72Hr El valor para comoraMnMesa72Hr.
	 */
	public void setCompraMnMesa72Hr(BigDecimal compraMnMesa72Hr) {
		this.compraMnMesa72Hr = compraMnMesa72Hr;
	}

	/**
	 * Regresa el valor de compraMnesaVFut.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCompraMnMesaVFut() {
		return compraMnMesaVFut;
	}

	/**
	 * Asigna el valor para compraMnMesaVFut.
	 * 
	 * @param compraMnMesaVFut El valor para compraMnMesaVFut.
	 */
	public void setCompraMnMesaVFut(BigDecimal compraMnMesaVFut) {
		this.compraMnMesaVFut = compraMnMesaVFut;
	}

	/**
	 * Regresa el valor de compraMnPizarron72Hr.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCompraMnPizarron72Hr() {
		return compraMnPizarron72Hr;
	}

	/**
	 * Asigna el valor para compraMnPizarron72Hr.
	 * 
	 * @param compraMnPizarron72Hr El valor para compraMnPizarron72Hr.
	 */
	public void setCompraMnPizarron72Hr(BigDecimal compraMnPizarron72Hr) {
		this.compraMnPizarron72Hr = compraMnPizarron72Hr;
	}

	/**
	 * Regresa el valor de compraMnPizarronVFut.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCompraMnPizarronVFut() {
		return compraMnPizarronVFut;
	}

	/**
	 * Asigna el valor para compraMnPizarronVFut.
	 * 
	 * @param compraMnPizarronVFut
	 */
	public void setCompraMnPizarronVFut(BigDecimal compraMnPizarronVFut) {
		this.compraMnPizarronVFut = compraMnPizarronVFut;
	}

	/**
	 * Regresa el valor de ventaMnCliente72Hr
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getVentaMnCliente72Hr() {
		return ventaMnCliente72Hr;
	}

	/**
	 * Asigna el valor para ventaMnCliente72Hr.
	 * 
	 * @param ventaMnCliente72Hr El valor para ventaMnCliente72Hr.
	 */
	public void setVentaMnCliente72Hr(BigDecimal ventaMnCliente72Hr) {
		this.ventaMnCliente72Hr = ventaMnCliente72Hr;
	}
	
	/**
	 * Regresa el valor de ventaMnclienteVFut.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getVentaMnClienteVFut() {
		return ventaMnClienteVFut;
	}

	/**
	 * Asigna el valor para ventaMnClienteVFut.
	 * 
	 * 
	 * @param ventaMnClienteVFut El valor para ventaMnClienteVFut.
	 */
	public void setVentaMnClienteVFut(BigDecimal ventaMnClienteVFut) {
		this.ventaMnClienteVFut = ventaMnClienteVFut;
	}
	
	/**
	 * Regresa el valor de ventaMnMesa72Hr.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getVentaMnMesa72Hr() {
		return ventaMnMesa72Hr;
	}

	/**
	 * Asigna el valor para ventaMnMesa72Hr.
	 * 
	 * @param ventaMnMesa72Hr El valor para ventaMnMesa72Hr.
	 */
	public void setVentaMnMesa72Hr(BigDecimal ventaMnMesa72Hr) {
		this.ventaMnMesa72Hr = ventaMnMesa72Hr;
	}

	/**
	 * Regresa el valor de ventaMnMesaVFut.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getVentaMnMesaVFut() {
		return ventaMnMesaVFut;
	}

	/**
	 * Asigna el valor para ventaMnMesaVFut.
	 * 
	 * @param ventaMnMesaVFut
	 */
	public void setVentaMnMesaVFut(BigDecimal ventaMnMesaVFut) {
		this.ventaMnMesaVFut = ventaMnMesaVFut;
	}

	/**
	 * Regresa el valor de ventaMnPizarron72Hr.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getVentaMnPizarron72Hr() {
		return ventaMnPizarron72Hr;
	}

	/**
	 * Asigna el valor para ventaMnPizarron72Hr. 
	 * 
	 * @param ventaMnPizarron72Hr El valor para ventaMnPizarron72Hr.
	 */
	public void setVentaMnPizarron72Hr(BigDecimal ventaMnPizarron72Hr) {
		this.ventaMnPizarron72Hr = ventaMnPizarron72Hr;
	}

	/**
	 * Regresa el valor de ventaMnPizarronVFut.
	 * 
	 * @return BigDecimal.
	 */
	public BigDecimal getVentaMnPizarronVFut() {
		return ventaMnPizarronVFut;
	}
	
	/**
	 * Asigna el valor para ventaMnPizarronVFut.
	 * 
	 * @param ventaMnPizarronVFut El valor para ventaMnPizarronVFut.
	 */
	public void setVentaMnPizarronVFut(BigDecimal ventaMnPizarronVFut) {
		this.ventaMnPizarronVFut = ventaMnPizarronVFut;
	}

	/**
	 * Regresa el valor de compra72Hr.
	 * 
	 * @return BigDecimal.
	 */
	public BigDecimal getCompra72Hr() {
		return compra72Hr;
	}

	/**
	 * Asigna el valor para compra72Hr.
	 * 
	 * @param compra72Hr El valor para compra72Hr.
	 */
	public void setCompra72Hr(BigDecimal compra72Hr) {
		this.compra72Hr = compra72Hr;
	}

	/**
	 * Regresa el valor de compraIn72Hr.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCompraIn72Hr() {
		return compraIn72Hr;
	}

	/**
	 * Asigna el valor para compraIn72Hr.
	 * 
	 * @param compraIn72Hr El valor para compraIn72Hr.
	 */
	public void setCompraIn72Hr(BigDecimal compraIn72Hr) {
		this.compraIn72Hr = compraIn72Hr;
	}

	/**
	 * Regresa el valor de compraInVFut.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCompraInVFut() {
		return compraInVFut;
	}

	/**
	 * Asigna el valor para compraInVFut.
	 * 
	 * @param compraInVFut El valor para compraInVfut.
	 */
	public void setCompraInVFut(BigDecimal compraInVFut) {
		this.compraInVFut = compraInVFut;
	}

	/**
	 * Regresa el valor de compraVFut.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCompraVFut() {
		return compraVFut;
	}

	/**
	 * Asigna el valor para compraVFut.
	 * 
	 * @param compraVFut El valor para compraVFut
	 */
	public void setCompraVFut(BigDecimal compraVFut) {
		this.compraVFut = compraVFut;
	}

	/**
	 * Regresa el valor de venta72H.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getVenta72Hr() {
		return venta72Hr;
	}
	
	/**
	 * Asigna el valor para venta72Hr.
	 * 
	 * @param venta72Hr El valor para venta72Hr.
	 */
	public void setVenta72Hr(BigDecimal venta72Hr) {
		this.venta72Hr = venta72Hr;
	}

	/**
	 * Regresa el valor de ventaIn72Hr.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getVentaIn72Hr() {
		return ventaIn72Hr;
	}

	/**
	 * Asigna el valor para ventaIn72Hr.
	 * 
	 * @param ventaIn72Hr El valor para ventaIn72Hro.
	 */
	public void setVentaIn72Hr(BigDecimal ventaIn72Hr) {
		this.ventaIn72Hr = ventaIn72Hr;
	}

	/**
	 * Regresa el valor de ventaInVFut
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getVentaInVFut() {
		return ventaInVFut;
	}

	/**
	 * Asigna el valor para ventaInVFut.
	 * 
	 * @param ventaInVFut El valor para ventaInVFut.
	 */
	public void setVentaInVFut(BigDecimal ventaInVFut) {
		this.ventaInVFut = ventaInVFut;
	}

	/**
	 * Regresa el valor de ventaVFut.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getVentaVFut() {
		return ventaVFut;
	}
	
	/**
	 * Asigna el valor para ventaVfut.
	 * 
	 * @param ventaVFut El valor para ventaVFut.
	 */
	public void setVentaVFut(BigDecimal ventaVFut) {
		this.ventaVFut = ventaVFut;
	}
    
    /**
	 * Devuelve true, si hay que dividir 1 / tasa_cambio
	 * de la divisa de este bean 
	 * @return
	 */
	public boolean isDivideDivisa() {
		return divideDivisa;
	}

	/**
	 * 
	 * @param isDivideDivisa - true 
	 * si hay que dividir 1 / tasa_cambio
	 * de la divisa de este bean  
	 */
	public void setDivideDivisa(boolean isDivideDivisa) {
		this.divideDivisa = isDivideDivisa;
	}
	
	
    /**
     * Compras en cash
     */
    private BigDecimal compraCash = new BigDecimal("0");

    /**
     * Ventas en cash
     */
    private BigDecimal ventaCash = new BigDecimal("0");

    /**
     * Compras en Tom
     */
    private BigDecimal compraTom = new BigDecimal("0");

    /**
     * Ventas en Tom
     */
    private BigDecimal ventaTom = new BigDecimal("0");

    /**
     * Compras en Spot
     */
    private BigDecimal compraSpot = new BigDecimal("0");

    /**
     * Ventas en Spot
     */
    private BigDecimal ventaSpot = new BigDecimal("0");
    
    /**
     * El valor o spread de compra a valor futuro.
     */
    private BigDecimal compraVFut = new BigDecimal("0");

    /**
     * El valor o spread de venta a valor futuro.
     */
    private BigDecimal ventaVFut = new BigDecimal("0");
    
    /**
     * El valor o spread de compra a 72 hrs.
     */
    private BigDecimal compra72Hr = new BigDecimal("0");
    
    /**
     * El valor o spread de venta a 72 hrs.
     */
    private BigDecimal venta72Hr = new BigDecimal("0");
    
    /**
     * Posici&oacute;n inicial cash.
     */
    private BigDecimal posicionInicialCash = new BigDecimal("0");

    /**
     * Posici&oacute;n inicial tom.
     */
    private BigDecimal posicionInicialTom = new BigDecimal("0");

    /**
     * Posici&oacute;n inicial spot.
     */
    private BigDecimal posicionInicialSpot = new BigDecimal("0");

    /**
     * Posicion inicial en moneda nacional cash
     */
    private BigDecimal posicionInicialMnCash = new BigDecimal("0");

    /**
     * Posicion inicial en moneda nacional tom
     */
    private BigDecimal posicionInicialMnTom = new BigDecimal("0");

    /**
     * Posicion inicial en moneda nacional spot
     */
    private BigDecimal posicionInicialMnSpot = new BigDecimal("0");
    
    /**
     * Posicion inicial en moneda nacional 72 horas
     */
    private BigDecimal posicionInicial72Hr = new BigDecimal("0");

    /**
     * Posicion inicial en moneda nacional valor futuro
     */
    private BigDecimal posicionInicialVFut = new BigDecimal("0");

    /**
     * Posicion inicial en moneda nacional 72 horas
     */
    private BigDecimal posicionInicialMn72Hr = new BigDecimal("0");

    /**
     * Posicion inicial en moneda nacional valor futuro
     */
    private BigDecimal posicionInicialMnVFut = new BigDecimal("0");

    /**
     * Compras en moneda nacional cliente cash
     */
    private BigDecimal compraMnClienteCash = new BigDecimal("0");

    /**
     * Ventas en moneda nacional cliente cash
     */
    private BigDecimal ventaMnClienteCash = new BigDecimal("0");

    /**
     * Compras en moneda nacional cliente tom
     */
    private BigDecimal compraMnClienteTom = new BigDecimal("0");

    /**
     * Ventas en moneda nacional cliente tom
     */
    private BigDecimal ventaMnClienteTom = new BigDecimal("0");

    /**
     * Compras en moneda nacional cliente spot
     */
    private BigDecimal compraMnClienteSpot = new BigDecimal("0");

    /**
     * Ventas en moneda nacional cliente spot
     */
    private BigDecimal ventaMnClienteSpot = new BigDecimal("0");
    
    /**
     * Variable Compra Moneda Nacional Cliente 72 Horas
     */
    private BigDecimal compraMnCliente72Hr = new BigDecimal("0");
    
    /**
     * Variable Venta Moneda Nacional Cliente 72 Horas
     */
    private BigDecimal ventaMnCliente72Hr = new BigDecimal("0");
    
    /**
     * Variable Compra Moneda Nacional Cliente Valor Futuro
     */
    private BigDecimal compraMnClienteVFut = new BigDecimal("0");
    
    /**
     * Variable Venta Moneda Nacional Cliente Valor Futuro
     */
    private BigDecimal ventaMnClienteVFut = new BigDecimal("0");
    
    /**
     * Compras en moneda nacional pizarron cash
     */
    private BigDecimal compraMnPizarronCash = new BigDecimal("0");

    /**
     * Ventas en moneda nacional pizarron cash
     */
    private BigDecimal ventaMnPizarronCash = new BigDecimal("0");

    /**
     * Compras en moneda nacional pizarron tom
     */
    private BigDecimal compraMnPizarronTom = new BigDecimal("0");

    /**
     * Ventas en moneda nacional pizarron tom
     */
    private BigDecimal ventaMnPizarronTom = new BigDecimal("0");

    /**
     * Compras en moneda nacional pizarron spot
     */
    private BigDecimal compraMnPizarronSpot = new BigDecimal("0");

    /**
     * Ventas en moneda nacional pizarron spot
     */
    private BigDecimal ventaMnPizarronSpot = new BigDecimal("0");

    /**
     * Variable Compra Moneda Nacional Pizarr&oacute;n 72 Horas
     */
    private BigDecimal compraMnPizarron72Hr = new BigDecimal("0");
    
    /**
     * Variable Venta Moneda Nacional Pizarr&oacute;n 72 Horas
     */
    private BigDecimal ventaMnPizarron72Hr = new BigDecimal("0");
    
    /**
     * Variable Compra Moneda Nacional Pizarr&oacute;n Valor Futuro
     */
    private BigDecimal compraMnPizarronVFut = new BigDecimal("0");
    
    /**
     * Variable venta Moneda Nacional Pizarr&oacute;n Valor Futuro
     */
    private BigDecimal ventaMnPizarronVFut = new BigDecimal("0");
    
    /**
     * Compras en moneda nacional mesa cash
     */
    private BigDecimal compraMnMesaCash = new BigDecimal("0");

    /**
     * Ventas en moneda nacional mesa cash
     */
    private BigDecimal ventaMnMesaCash = new BigDecimal("0");

    /**
     * Compras en moneda nacional mesa tom
     */
    private BigDecimal compraMnMesaTom = new BigDecimal("0");

    /**
     * Ventas en moneda nacional mesa tom
     */
    private BigDecimal ventaMnMesaTom = new BigDecimal("0");

    /**
     * Compras en moneda nacional mesa spot
     */
    private BigDecimal compraMnMesaSpot = new BigDecimal("0");

    /**
     * Ventas en moneda nacional mesa spot
     */
    private BigDecimal ventaMnMesaSpot = new BigDecimal("0");

    /**
     * Variable Compra Moneda Nacional Mesa 72 Horas
     */
    private BigDecimal compraMnMesa72Hr = new BigDecimal("0");
    
    /**
     * Variable Venta Moneda Nacional Mesa 72 Horas
     */
    private BigDecimal ventaMnMesa72Hr = new BigDecimal("0");
    
    /**
     * Variable Compra Moneda Nacional Mesa Valor Futuro
     */
    private BigDecimal compraMnMesaVFut = new BigDecimal("0");
    
    /**
     * Variable Venta Moneda Nacional Mesa Valor Futuro
     */
    private BigDecimal ventaMnMesaVFut = new BigDecimal("0");
    
    /**
     * Tipo de Cambio del Mercado
     */
    private BigDecimal tipoCambioMercado = new BigDecimal("0");

    /**
     * Identificador de la mesa de cambio
     */
    private Integer idMesaCambio;

    /**
     * Identificador de la divisa
     */
    private String idDivisa;

    /**
     * Indica si la Divisa 
     * tiene 'S' en la
     * tabla de sc_divisa
     */
    private boolean  divideDivisa;

    /**
     * Identificador del canal
     */
    private String idCanal;

    /**
     * Identificador del producto
     */
    private String idProducto;

    /**
     * Utilidad Cash
     */
    private BigDecimal utilidadRealizadaGlobal;

    /**
     * Utilidad Tom
     */
    private BigDecimal utilidadRealizadaMesa;

    /**
     * Efectivo
     */
    private BigDecimal efectivo = new BigDecimal("0");

    /**
     * Documentos Transferencias
     */
    private BigDecimal documentosTransferencias = new BigDecimal("0");

    /**
     * Documentos Transferencias Moneda Nacional Cliente
     */
    private BigDecimal documentosTransferenciasMnCliente = new BigDecimal("0");

    /**
     * Documentos Transferencias Moneda Nacional Pizarron
     */
    private BigDecimal documentosTransferenciasMnPizarron = new BigDecimal("0");

    /**
     * Documentos Transferencias Moneda Nacional Mesa
     */
    private BigDecimal documentosTransferenciasMnMesa = new BigDecimal("0");

    /**
     * Efectivo Moneda Nacional Cliente
     */
    private BigDecimal efectivoMnCliente = new BigDecimal("0");

    /**
     * Efectivo Moneda Nacional Pizarron
     */
    private BigDecimal efectivoMnPizarron = new BigDecimal("0");

    /**
     * Efectivo Moneda Nacional Pizarron
     */
    private BigDecimal efectivoMnMesa = new BigDecimal("0");

    /**
     * Compra Interbancario Cash
     */
    private BigDecimal compraInCash = new BigDecimal("0");

    /**
     * Compra Interbancario Tom
     */
    private BigDecimal compraInTom = new BigDecimal("0");

    /**
     * Compra Interbancario Spot
     */
    private BigDecimal compraInSpot = new BigDecimal("0");

    /**
     * Venta Interbancario Cash
     */
    private BigDecimal ventaInCash = new BigDecimal("0");

    /**
     * Venta Interbancario Tom
     */
    private BigDecimal ventaInTom = new BigDecimal("0");

    /**
     * Venta Interbancario Spot
     */
    private BigDecimal ventaInSpot = new BigDecimal("0");

    /**
     * El valor o spread de compra a 72 hrs.
     */
    private BigDecimal compraIn72Hr = new BigDecimal("0");
    
    /**
     * El valor o spread de venta a 72 hrs.
     */
    private BigDecimal ventaIn72Hr = new BigDecimal("0");
    
    /**
     * El valor o spread de compra a Valor futuro.
     */
    private BigDecimal compraInVFut = new BigDecimal("0");
    
    /**
     * El valor o spread de venta a Valor futuro.
     */
    private BigDecimal ventaInVFut = new BigDecimal("0");
    
    /**
     * Posicion Inicial del servicio de inventario de efectivo en sucursal
     */
    private BigDecimal posicionInicialTeller = new BigDecimal("0");

    /**
     * Depositos en efectivo del servicio de inventario de efectivo en
     * sucursal
     */
    private BigDecimal depositosEfectivoTeller = new BigDecimal("0");

    /**
     * Depositos a la boveda del servicio de inventario de efectivo en
     * sucursal
     */
    private BigDecimal depositosBovedaTeller = new BigDecimal("0");

    /**
     * Retiros de Efectivo del servicio de inventario de efectivo en
     * sucursal
     */
    private BigDecimal retirosEfectivoTeller = new BigDecimal("0");

    /**
     * Retiros de la Boveda del servicio de inventario de efectivo en
     * sucursal
     */
    private BigDecimal retirosBovedaTeller = new BigDecimal("0");
    
    public BigDecimal getMontoMnMesa() {
		return montoMnMesa;
	}

	public void setMontoMnMesa(BigDecimal montoMn) {
		this.montoMnMesa = montoMn;
	}
	
    public BigDecimal getMontoMn() {
		return montoMn;
	}

	public void setMontoMn(BigDecimal montoMn) {
		this.montoMn = montoMn;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getTipoValor() {
		return tipoValor;
	}

	public void setTipoValor(String tipoValor) {
		this.tipoValor = tipoValor;
	}

	public String getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public Integer getIdCorte() {
		return idCorte;
	}

	public void setIdCorte(Integer idCorte) {
		this.idCorte = idCorte;
	}

	/**
     * Sumatoria Mn del corte
     */
    private BigDecimal montoMnMesa = new BigDecimal("0");
    
	/**
     * Sumatoria Mn del corte
     */
    private BigDecimal montoMn = new BigDecimal("0");
    
    /**
     * Sumatoria Divisa del corte
     */
    private BigDecimal monto = new BigDecimal("0");
    
    /**
     * fecha valor
     */
    private String tipoValor;
    
    /**
     * Tipo de operacion
     */
    private String tipoOperacion;
 
    /**
     * Identificador de la mesa de cambio
     */
    private Integer idCorte;

	
}