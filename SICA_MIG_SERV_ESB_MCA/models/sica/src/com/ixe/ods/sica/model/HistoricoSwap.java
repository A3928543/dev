package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Clase persistente para la tabla SC_SWAP donde se registran las operaciones de swaps generadas por el SICA o bien
 * por TAS.</p>
 * <p/>
 * <p>Se refiere al histórico de Swap. Su relación con un conjunto de Deal hace necesario que exista una tabla 
 * para mantener la consistencia de datos al mover los Deals a Históricos.
 * 
 * @author Edgar I. Leija
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:24 $
 * @hibernate.class table="SC_H_SWAP"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.HistoricoSwap"
 * dynamic-update="true"
 */
public class HistoricoSwap implements Serializable {

    /**
     * Constructor vac&iacute;o.
     */
    public HistoricoSwap() {
        super();
    }
    
    /**
     * Constructor que recibe un swap e inicializa todas sus variables.
     * 
     * @param s El swap que inicializa el hist&oacute;rico.
     */
    public HistoricoSwap(Swap s) {
        super();
        setIdFolioSwap(s.getIdFolioSwap());
        setCompra(s.isCompra());
        setFechaOperacion(s.getFechaOperacion());
        if(s.getIdFolioSwap() >0) {
        	setFolioSwapInicio(s.getFolioSwapInicio());
        }
        else {
        	setFolioSwapInicio(null);
        }
        setMonto(s.getMonto());
        setMontoAsignado(s.getMontoAsignado());
        setStatus(s.getStatus());
        setTipoSwap(s.getTipoSwap());
        setContratoSica(s.getContratoSica().getNoCuenta());
        setDivisa(s.getDivisa().getIdDivisa());
    }

    /**
     * Regresa el valor de idFolioSwap.
     *
     * @return int.
     * @hibernate.id generator-class="assigned"
     * column="ID_FOLIO_SWAP"
     * unsaved-value="null"
     */
    public int getIdFolioSwap() {
        return idFolioSwap;
    }

    /**
     * Establece el valor de idFolioSwap.
     *
     * @param idFolioSwap El valor a asignar.
     */
    public void setIdFolioSwap(int idFolioSwap) {
        this.idFolioSwap = idFolioSwap;
    }

    /**
     * Regresa el valor de compra.
     *
     * @return boolean.
     * @hibernate.property column="COMPRA"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     */
    public boolean isCompra() {
        return compra;
    }

    /**
     * Establece el valor de compra.
     *
     * @param compra El valor a asignar.
     */
    public void setCompra(boolean compra) {
        this.compra = compra;
    }

    /**
     * Regresa el valor de fechaOperacion.
     *
     * @return Date.
     * @hibernate.property column="FECHA_OPERACION"
     * not-null="true"
     * unique="false"
     */
    public Date getFechaOperacion() {
        return fechaOperacion;
    }

    /**
     * Establece el valor de fechaOperacion.
     *
     * @param fechaOperacion El valor a asignar.
     */
    public void setFechaOperacion(Date fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    /**
     * Regresa el valor de folioSwapInicio.
     *
     * @return Integer.
     * @hibernate.property column="FOLIO_SWAP_INICIO"
     * not-null="false"
     * unique="false"
     */
    public Integer getFolioSwapInicio() {
        return folioSwapInicio;
    }

    /**
     * Establece el valor de folioSwapInicio.
     *
     * @param folioSwapInicio El valor a asignar.
     */
    public void setFolioSwapInicio(Integer folioSwapInicio) {
        this.folioSwapInicio = folioSwapInicio;
    }

    /**
     * Regresa el valor de monto.
     *
     * @return double.
     * @hibernate.property column="MONTO"
     * not-null="true"
     * unique="false"
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Establece el valor de monto.
     *
     * @param monto El valor a asignar.
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }

    /**
     * Regresa el valor de montoAsignado.
     *
     * @return double.
     * @hibernate.property column="MONTO_ASIGNADO"
     * not-null="true"
     * unique="false"
     */
    public double getMontoAsignado() {
        return montoAsignado;
    }

    /**
     * Establece el valor de montoAsignado.
     *
     * @param montoAsignado El valor a asignar.
     */
    public void setMontoAsignado(double montoAsignado) {
        this.montoAsignado = montoAsignado;
    }

    /**
     * Regresa el valor de status.
     *
     * @return String.
     * @hibernate.property column="STATUS"
     * not-null="true"
     * unique="false"
     */
    public String getStatus() {
        return status;
    }

    /**
     * Establece el valor de status.
     *
     * @param status El valor a asignar.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Regresa el valor de tipoSwap.
     *
     * @return String.
     * @hibernate.property column="TIPO_SWAP"
     * not-null="true"
     * unique="false"
     */
    public String getTipoSwap() {
        return tipoSwap;
    }

    /**
     * Establece el valor de tipoSwap.
     *
     * @param tipoSwap
     */
    public void setTipoSwap(String tipoSwap) {
        this.tipoSwap = tipoSwap;
    }

    /**
     * Regresa el no. de cuenta del ContratoSica.
     *
     * @return String.
     * @hibernate.property column="NO_CUENTA"
     * not-null="true"
     * unique="false"
     */
    public String getContratoSica() {
        return _contratoSica;
    }

    /**
     * Establece el valor de contratoSica.
     *
     * @param contratoSica El valor a asignar.
     */
    public void setContratoSica(String contratoSica) {
        _contratoSica = contratoSica;
    }

    /**
     * Regresa el valor de tipoSwap.
     *
     * @return String.
     * @hibernate.property column="ID_DIVISA"
     * not-null="true"
     * unique="false"
     */
    public String getDivisa() {
        return _divisa;
    }

    /**
     * Establece el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(String divisa) {
        _divisa = divisa;
    }

    /**
     * El identificador del registro.
     */
    private int idFolioSwap;

    /**
     * Si el inicio del SWAP es compra o venta.
     */
    private boolean compra;

    /**
     * La fecha en que se registr&oacute; el swap.
     */
    private Date fechaOperacion = new Date();

    /**
     * Si se gener&oacute; en el sica, es el n&uacute;mero de deal interbancario.
     */
    private Integer folioSwapInicio;

    /**
     * El monto completo del SWAP.
     */
    private double monto;

    /**
     * Monto que ya tiene una contraparte asignada en el SWAP.
     */
    private double montoAsignado;

    /**
     * IN)icio, PA)rcialmente asignado, CA)ncelado
     */
    private String status = STATUS_INICIO;

    /**
     * S)ICA o E)xterno o de estrategia.
     */
    private String tipoSwap;

    /**
     * Relaci&oacute;n muchos-a-uno con ContratoSica.
     */
    private String _contratoSica;

    /**
     * Relaci&ooacute;n muchos-a-uno con Divisa.
     */
    private String _divisa;

    /**
     * Relaci&oacute;n uno-a-muchos con Deal.
     */
    private List _deals = new ArrayList();

    /**
     * Constante para indicar el tipo de swap cambiario.
     */
    public final static String TIPO_SWAP_SICA = "S";

    /**
     * Constante para indicar el tipo de swap que viene de TAS.
     */
    public final static String TIPO_SWAP_ESTRATEGIA = "E";

    /**
     * Constante para indicar el status del swap en inicio.
     */
    public final static String STATUS_INICIO = "IN";

    /**
     * Constante para indicar el status del swap parcialmente asignado.
     */
    public final static String STATUS_PARCIALMENTE_ASIGNADO = "PA";

    /**
     * Constante para indicar el status del swap completo.
     */
    public final static String STATUS_COMPLETAMENTE_ASIGNADO = "CO";

    /**
     * Constante para indicar el status del swap en proceso de cancelaci&oacute;n.
     */
    public final static String STATUS_PROCESO_CANCELACION = "PC";

    /**
     * Constante para indicar el status del swap cancelado.
     */
    public final static String STATUS_CANCELADO = "CA";
}
