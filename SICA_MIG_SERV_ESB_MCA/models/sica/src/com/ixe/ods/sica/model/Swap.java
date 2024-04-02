/*
 * $Id: Swap.java,v 1.13 2010/04/13 20:19:24 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.utils.BDUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Clase persistente para la tabla SC_SWAP donde se registran las operaciones de swaps generadas
 * por el SICA o bien por TAS.</p>
 * <p/>
 * <p>Se refiere al encabezado del swap y el monto que se tiene asignado. Es decir, puede darse el
 * caso donde SC_SWAP tenga una relaci&oacute;n a muchos con SC_DEAL que se refiere a la(s)
 * contraparte(s) del swap.</p>
 * <p/>
 * <p>El inicio del swap si es generado por el SICA se encuentra de el atributo ID_DEAL_INICIO. En
 * caso de ser generado por TAS se refiere al folio de la operaci&oacute;n de estrategia.</p>
 *
 * @author Jean C. Favila
 * @version $Revision: 1.13 $ $Date: 2010/04/13 20:19:24 $
 * @hibernate.class table="SC_SWAP"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Swap"
 * dynamic-update="true"
 * @hibernate.query name="findSwapsCierreDelDia"
 * query="FROM Swap AS sw WHERE sw.status != 'CO' AND sw.status != 'CA'"
 */
public class Swap implements Serializable {

    /**
     * Constructor vac&iacute;o.
     */
    public Swap() {
        super();
    }

    /**
     * Constructor preferido. Inicializa la mayor parte de las variables de instancia.
     *
     * @param idFolioSwap El folio del swap.
     * @param compra Si se trata de una compra o una venta.
     * @param monto  El monto en divisa del swap.
     * @param tipoSwap Sica o Estrategia.
     * @param folioSwapInicio El n&uacute;mero de deal interbancario que es el inicio del swap.
     * @param contratoSica El contrato sica relacionado.
     * @param divisa La divisa del swap.
     * @param folderk El portafolios de Kondor (Si aplica).
     * @param idConf El folio de la operaci&oacute;n en Kondor (Si aplica).
     */
    public Swap(int idFolioSwap, boolean compra, double monto, String tipoSwap,
                Integer folioSwapInicio, ContratoSica contratoSica, Divisa divisa,
                String folderk, String idConf) {
        this();
        this.idFolioSwap = idFolioSwap;
        this.compra = compra;
        this.monto = monto;
        this.tipoSwap = tipoSwap;
        this.folioSwapInicio = folioSwapInicio;
        this.folderk = folderk;
        this.idConf = idConf;
        _contratoSica = contratoSica;
        _divisa = divisa;
    }

    /**
     * Regresa la persona ligada a los rol 'TIT' del contratoSica.
     *
     * @return Persona.
     */
    public Persona getCliente() {
        return ContratoCliente.clienteParaContratoSica(getContratoSica());
    }

    /**
     * Regresa la diferencia entre el monto y el montoAsignado.
     *
     * @return double.
     */
    public double getMontoPorAsignar() {
        return monto - montoAsignado;
    }

    /**
     * Regresa una descripci&oacute;n m&aacute;s explicativa para el status del swap.
     *
     * @return String.
     */
    public String getDescripcionStatus() {
        String desc = "";
        if (STATUS_CANCELADO.equals(status)) {
            desc = "Cancelado";
        }
        else if (STATUS_INICIO.equals(status)) {
            desc = "Inicio";
        }
        else if (STATUS_PARCIALMENTE_ASIGNADO.equals(status)) {
            desc = "Parcialmente Asignado";
        }
        else if (STATUS_COMPLETAMENTE_ASIGNADO.equals(status)) {
            desc = "Completamente Asignado";
        }
        return desc;
    }

    /**
     * Regresa la descripcion del tipo de Swap.
     *
     * @return String.
     */
    public String getDescripcionTipoSwap() {
        String desc = null;
        if (TIPO_SWAP_SICA.equals(tipoSwap)) {
            desc = "Contado";
        }
        else if (TIPO_SWAP_ESTRATEGIA.equals(tipoSwap)) {
            desc = "Derivado";
        }
        return desc;
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
     * Regresa el valor de folderk.
     *
     * @hibernate.property column="FOLDERK"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getFolderk() {
        return folderk;
    }

    /**
     * Establece el valor de folderk.
     *
     * @param folderk El valor a asignar.
     */
    public void setFolderk(String folderk) {
        this.folderk = folderk;
    }

    /**
     * Regresa el valor de folioSwapInicio.
     *
     * @return int.
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
     * Regresa el valor de idConf.
     *
     * @hibernate.property column="ID_CONF"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getIdConf() {
        return idConf;
    }

    /**
     * Establece el valor de idConf.
     *
     * @param idConf El valor a asignar.
     */
    public void setIdConf(String idConf) {
        this.idConf = idConf;
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
     * Regresa el primero monto en la divisa del swap que encuentre entre los detalles del deal, o
     * el monto dolarizado si no se puede resolver &eacute;ste.
     *
     * @return BigDecimal.
     */
    public BigDecimal getMontoDivisa() {
        for (Iterator it = getDeals().iterator(); it.hasNext();) {
            Deal deal = (Deal) it.next();

            for (Iterator it2 = deal.getDetalles().iterator(); it2.hasNext();) {
                DealDetalle det = (DealDetalle) it2.next();

                if (!det.isCancelado() && getDivisa().equals(det.getDivisa())) {
                    return BDUtils.generar2(det.getMonto());
                }
            }
        }
        return BDUtils.generar2(monto);
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
     * @param tipoSwap El valor a asignar.
     */
    public void setTipoSwap(String tipoSwap) {
        this.tipoSwap = tipoSwap;
    }

    /**
     * Regresa el valor de contratoSica.
     *
     * @return ContratoSica.
     * @hibernate.many-to-one column="NO_CUENTA"
     * cascade="none"
     * class="com.ixe.ods.bup.model.ContratoSica"
     * outer-join="auto"
     * unique="false"
     */
    public ContratoSica getContratoSica() {
        return _contratoSica;
    }

    /**
     * Establece el valor de contratoSica.
     *
     * @param contratoSica El valor a asignar.
     */
    public void setContratoSica(ContratoSica contratoSica) {
        _contratoSica = contratoSica;
    }

    /**
     * Regresa el valor de divisa.
     *
     * @return Divisa.
     * @hibernate.many-to-one column="ID_DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     */
    public Divisa getDivisa() {
        return _divisa;
    }

    /**
     * Establece el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
        _divisa = divisa;
    }

    /**
     * Regresa el valor de deals.
     *
     * @return List.
     * @hibernate.bag inverse="true"
     * lazy="true"
     * cascade="none"
     * order-by="FECHA_CAPTURA"
     * @hibernate.collection-key column="ID_FOLIO_SWAP"
     * @hibernate.collection-one-to-many class="com.ixe.ods.sica.model.Deal"
     */
    public List getDeals() {
        return _deals;
    }

    /**
     * Establece el valor de deals.
     *
     * @param deals El valor a asignar.
     */
    public void setDeals(List deals) {
        _deals = deals;
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
     * El nombre del portafolios de KONDOR (opcional).
     */
    private String folderk;

    /**
     * Si se gener&oacute; en el sica, es el n&uacute;mero de deal interbancario.
     */
    private Integer folioSwapInicio;

    /**
     * El n&uacute;mero de operaci&oacute;n de KONDOR.
     */
    private String idConf;

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
    private ContratoSica _contratoSica;

    /**
     * Relaci&ooacute;n muchos-a-uno con Divisa.
     */
    private Divisa _divisa;

    /**
     * Relaci&oacute;n uno-a-muchos con Deal.
     */
    private List _deals = new ArrayList();

    /**
     * Constante para indicar el tipo de swap cambiario.
     */
    public static final String TIPO_SWAP_SICA = "S";

    /**
     * Constante para indicar el tipo de swap que viene de TAS.
     */
    public static final String TIPO_SWAP_ESTRATEGIA = "E";

    /**
     * Constante para indicar el status del swap en inicio.
     */
    public static final String STATUS_INICIO = "IN";

    /**
     * Constante para indicar el status del swap parcialmente asignado.
     */
    public static final String STATUS_PARCIALMENTE_ASIGNADO = "PA";

    /**
     * Constante para indicar el status del swap completo.
     */
    public static final String STATUS_COMPLETAMENTE_ASIGNADO = "CO";

    /**
     * Constante para indicar el status del swap en proceso de cancelaci&oacute;n.
     */
    public static final String STATUS_PROCESO_CANCELACION = "PC";

    /**
     * Constante para indicar el status del swap cancelado.
     */
    public static final String STATUS_CANCELADO = "CA";

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -4170906707213024680L;
}
