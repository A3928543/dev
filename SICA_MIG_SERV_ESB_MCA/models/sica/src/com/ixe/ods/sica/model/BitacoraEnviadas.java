/*
 * $Id: BitacoraEnviadas.java,v 1.18 2010/04/13 20:11:35 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.ixe.ods.sica.Constantes;

/**
 * Clase persistente para la tabla SC_BITACORA_ENVIADAS, donde se reportan Operaciones a Banxico.
 *
 * @hibernate.class table="SC_BITACORA_ENVIADAS"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.BitacoraEnviadas"
 * dynamic-update="true"
 *
 * @author Javier Cordova
 * @version  $Revision: 1.18 $ $Date: 2010/04/13 20:11:35 $
 */
public class BitacoraEnviadas implements Serializable {

    /**
     * Constructor por default.
     */
    public BitacoraEnviadas() {
        super();
    }

    /**
     * Regresa la descripci&oacute;n de la operaci&oacute;n, seg&uacute;n el valor de
     * <code>getClasifOperacion()</code>.
     *
     * @return String.
     */
    public String getDescripcionClasifOperacion() {
        if ("A".equals(getClasifOperacion()) || "B".equals(getClasifOperacion())) {
            return "Spot";
    }
         else if ("C".equals(getClasifOperacion()) || "D".equals(getClasifOperacion())) {
            return "FX-Swap";
        }
        else if ("E".equals(getClasifOperacion())) {
            return "FX-Swap (Spot vs. Outright Forward)";
        }
        return "";
    }

    /**
     * Regresa el valor entero que representa para Banxico las diferentes Fechas Valor.
     *
     * @param deal El deal a revisar.
     * @return el Tipo Valor para Banxico.
     */
    private int getPlazoBanxicoParaDeal(Deal deal) {
        int plazo = 3;
        if (Constantes.CASH.equals(deal.getTipoValor())) {
            plazo = 0;
        }
        else if (Constantes.TOM.equals(deal.getTipoValor())) {
            plazo = 1;
        }
        else if (Constantes.SPOT.equals(deal.getTipoValor())) {
            plazo = 2;
        }
        return plazo;
    }

    /**
     * Regresa el valor de id.
     *
	 * @return BitacoraEnviadasPK.
	 * @hibernate.id
	 */
	public BitacoraEnviadasPK getId() {
		return id;
	}

	/**
     * Establece el valor de id.
     *
	 * @param id El valor a asignar.
	 */
	public void setId(BitacoraEnviadasPK id) {
		this.id = id;
	}

    /**
     * Regresa el valor de nombre.
     *
     * @hibernate.property column="NOMBRE"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Fija el valor de nombre.
     *
     * @param nombre El valor a asignar.
    */
    public void setNombre(String nombre) {
        if (nombre != null && nombre.length() > 30) {
            nombre = nombre.substring(0, 29);
        }
        this.nombre = nombre;
    }

    /**
     * Regresa el valor de createDt.
     *
     * @hibernate.property column="CREATE_DT"
     * not-null="false"
     * unique="false"
     * @return Date.
     */
    public Date getCreateDt() {
        return createDt;
    }

    /**
     * Fija el valor de createDt.
     *
     * @param createDt El valor a asignar.
     */
    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    /**
     * Regresa el valor de status.
     *
     * @hibernate.property column="ESTATUS"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Fija el valor de status.
     *
     * @param status El valor a asignar.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Regresa el valor de fechaEnvio.
     *
     * @hibernate.property column="FECHA_ENVIO"
     * not-null="false"
     * unique="false"
     * @return Date.
     */
    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    /**
     * Fija el valor de fechaEnvio.
     *
     * @param fechaEnvio El valor a asignar.
     */
    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    /**
     * Regresa el valor de folioBanxico.
     *
     * @hibernate.property column="FOLIO_BANXICO"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getFolioBanxico() {
        return folioBanxico;
    }

    /**
     * Fija el valor de folioBanxico.
     *
     * @param folioBanxico El valor a asignar.
     */
    public void setFolioBanxico(Integer folioBanxico) {
        this.folioBanxico = folioBanxico;
    }

    /**
     * Regresa el valor de error.
     *
     * @hibernate.property column="ERROR"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getError() {
        return error;
    }

    /**
     * Fija el valor de error.
     *
     * @param error El valor a asignar.
     */
    public void setError(Integer error) {
        this.error = error;
    }

    /**
     * Regresa el valor de Accion.
     *
     * @hibernate.property column="ACCION"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getAccion() {
        return accion;
    }

    /**
     * Fija el valor de Accion.
     *
     * @param accion El valor a asignar.
     */
    public void setAccion(Integer accion) {
        this.accion = accion;
    }

    /**
     * Regresa el valor de plazo.
     *
     * @hibernate.property column="PLAZO"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getPlazo() {
        return plazo;
    }

    /**
     * Fija el valor de plazo.
     *
     * @param plazo El valor a asignar.
     */
    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }

    /**
     * Regresa el String Fecha Valor para el SICA, que representa el valor entero
     * <code>plazo</code> para Banxico.
     *
     * @return String La Fecha Valor para el SICA.
     */
    public String getPlazoSICA() {
        String plazo = null;
        if (getPlazo() != null) {
    	    if (0 == getPlazo().intValue()) {
    	        plazo = Constantes.CASH;
    	    }
	    	else if (1 == getPlazo().intValue()) {
	    		plazo = Constantes.TOM;
	    	}
	    	else if (2 == getPlazo().intValue()) {
	    		plazo = Constantes.SPOT;
	    	}
	    	else if (3 == getPlazo().intValue()) {
	    	    plazo = Constantes.MAS_48;
	    	}
    	}
    	return plazo;
    }

    /**
     * Regresa el valor de tipoOper.
     *
     * @hibernate.property column="TIPOOPER"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getTipoOper() {
        return tipoOper;
    }

    /**
     * Fija el valor de tipoOper.
     *
     * @param tipoOper El valor a asignar.
     */
    public void setTipoOper(Integer tipoOper) {
        this.tipoOper = tipoOper;
    }

    /**
     * Regresa el String Tipo de Operaci&oacute;n para el SICA que representa el valor entero
     * <code>tipoOper</code> para Banxico.
     *
     * @return String El Tipo de Operaci&oacute;n para el SICA.
     */
    public String getTipoOperSICA() {
        String tipoOper = null;
        if (TIPO_OPE_COMPRA.equals(getTipoOper())) {
    		tipoOper = "COMPRA";
    	}
    	else if (TIPO_OPE_VENTA.equals(getTipoOper())) {
    	    tipoOper = "VENTA";
    	}
        return tipoOper;
    }

    /**
     * Regresa el valor de monto.
     *
     * @hibernate.property column="MONTO"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getMonto() {
        return monto;
    }

    /**
     * Fija el valor de monto.
     *
     * @param monto El valor a asignar.
     */
    public void setMonto(Double monto) {
        this.monto = monto;
    }

    /**
     * Regresa el valor de tipo.
     *
     * @hibernate.property column="TIPO"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getTipo() {
        return tipo;
    }

    /**
     * Fija el valor de tipo.
     *
     * @param tipo El valor a asignar.
     */
    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    /**
     * Regresa el String Contado o Tipo de Derivado para el SICA que representa el valor entero
     * <code>tipo</code> para Banxico.
     *
     * @return String El Tipo para el SICA.
     */
    public String getTipoSICA() {
        String tipoSica = null;
        if (TIPO_CONTADO.equals(getTipo())) {
    		tipoSica = "CONTADO";
    	}
    	else if (TIPO_FORWARD.equals(getTipo())) {
    		tipoSica = "FORWARD";
    	}
    	else if (TIPO_FUTURO.equals(getTipo())) {
    		tipoSica = "FUTURO";
    	}
    	else if (TIPO_OPCION.equals(getTipo())) {
    	    tipoSica = "OPCION";
    	}
    	return tipoSica;
    }

    /**
     * Regresa el valor de folio.
     *
     * @hibernate.property column="FOLIO"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getFolio() {
        return folio;
    }

    /**
     * Fija el valor de folio.
     *
     * @param folio El valor a asignar.
     */
    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    /**
     * Regresa el valor de fechaConcertacion.
     *
     * @hibernate.property column="FECHA_CONCERTACION"
     * not-null="false"
     * unique="false"
     * @return Date.
     */
    public Date getFechaConcertacion() {
        return fechaConcertacion;
    }

    /**
     * Fija el valor de fechaConcertacion.
     *
     * @param fechaConcertacion El valor a asignar.
     */
    public void setFechaConcertacion(Date fechaConcertacion) {
        this.fechaConcertacion = fechaConcertacion;
    }

    /**
     * Regresa el valor de fechaLiquidacion.
     *
     * @hibernate.property column="FECHA_LIQUIDACION"
     * not-null="false"
     * unique="false"
     * @return Date.
     */
    public Date getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    /**
     * Fija el valor de fechaLiquidacion.
     *
     * @param fechaLiquidacion El valor a asignar.
     */
    public void setFechaLiquidacion(Date fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }

    /**
     * Regresa el valor de claveContraparte.
     *
     * @hibernate.property column="CLAVE_CONTRAPARTE"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getClaveContraparte() {
        return claveContraparte;
    }

    /**
     * Fija el valor de claveContraparte.
     *
     * @param claveContraparte El valor a asignar.
     */
    public void setClaveContraparte(String claveContraparte) {
        this.claveContraparte = claveContraparte;
    }

    /**
     * Regresa el valor de rfc.
     *
     * @hibernate.property column="RFC"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getRfc() {
        return rfc;
    }

    /**
     * Fija el valor de rfc.
     *
     * @param rfc El valor a asignar.
     */
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    /**
     * Regresa el valor de tipoContraparte.
     *
     * @hibernate.property column="TIPO_CONTRAPARTE"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getTipoContraparte() {
        return tipoContraparte;
    }

    /**
     * Fija el valor de tipoContraparte.
     *
     * @param tipoContraparte El valor a asignar.
     */
    public void setTipoContraparte(String tipoContraparte) {
        this.tipoContraparte = tipoContraparte;
    }

    /**
     * Regresa el valor de swap.
     *
     * @hibernate.property column="SWAP"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getSwap() {
        return swap;
    }

    /**
     * Fija el valor de swap.
     *
     * @param swap El valor a asignar.
     */
    public void setSwap(String swap) {
        this.swap = swap;
    }

    /**
     * Regresa el valor de statusEstrategia.
     *
     * @hibernate.property column="STATUS_ESTRATEGIA"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getStatusEstrategia() {
        return statusEstrategia;
    }

    /**
     * Fija el valor de statusEstrategia.
     *
     * @param statusEstrategia El valor a asignar.
     */
    public void setStatusEstrategia(String statusEstrategia) {
        this.statusEstrategia = statusEstrategia;
    }

    /**
     * Regresa el valor de noCuenta.
     *
     * @hibernate.property column="NO_CUENTA"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getNoCuenta() {
        return noCuenta;
    }

    /**
     * Fija el valor de noCuenta.
     *
     * @param noCuenta El valor a asignar.
     */
    public void setNoCuenta(String noCuenta) {
        this.noCuenta = noCuenta;
    }

    /**
     * Regresa el valor de progressRecid.
     *
     * @hibernate.property column="PROGRESS_RECID"
     * not-null="false"
     * unique="false"
     * @return Integer.
     */
    public Integer getProgressRecid() {
        return progressRecid;
    }

    /**
     * Fija el valor de progressRecid.
     *
     * @param progressRecid El valor a asignar.
     */
    public void setProgressRecid(Integer progressRecid) {
        this.progressRecid = progressRecid;
    }

    /**
     * De acuerdo a ciertos criterios, popula el campo claveContraparte o los campos nombre, rfc y
     * tipoContraparte.
     *
     * @param claveContraparte            Clave que tiene la Contraparte en el Catalogo Banxico (si
     *                                  es que existe).
     * @param nombre                    El nombre de la Contraparte (nombreCorto del Cliente).
     * @param rfc                        El RFC de la Contraparte.
     * @param tipoContraparte            Tipo (Sector) de la Contraparte.
     * @param isMenorMontoMaximoDeal    Si se trata de una Operaci&oacute;n cuyo Monto es menor Al
     *                                  Monto M&aacute;ximo Deal.
     */
    public void setContraparte(String claveContraparte, String nombre, String rfc, String
            tipoContraparte, boolean isMenorMontoMaximoDeal) {
        if (isMenorMontoMaximoDeal) {
            setClaveContraparte("");
            setNombre("");
            setRfc("");
            setTipoContraparte(tipoContraparte);
        }
        else {
	        if (claveContraparte != null && !"".equals(claveContraparte)) {
	            setClaveContraparte(claveContraparte);
	            setNombre("");
	            setRfc("");
	            setTipoContraparte("");
	        }
	        else {
	            setClaveContraparte("");
	            setNombre(nombre);
	            setRfc(rfc);
	            setTipoContraparte(tipoContraparte);
	        }
        }
    }

   /**
     * Regresa el valor de modoCreacionFolioSwap.
     *
     * @return String.
     */
    public String getModoCreacionFolioSwap() {
        return modoCreacionFolioSwap;
    }

    /**
     * Establece el valor de modoCreacionFolioSwap.
     *
     * @param modoCreacionFolioSwap El valor a asignar.
     */
    public void setModoCreacionFolioSwap(String modoCreacionFolioSwap) {
        this.modoCreacionFolioSwap = modoCreacionFolioSwap;
    }

    /**
     * Regresa el valor de montoDivisa.
     *
     * @hibernate.property column="MONTO_DIVISA"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getMontoDivisa() {
        return montoDivisa;
    }

    /**
     * Establece el valor de montoDivisa.
     *
     * @param montoDivisa El valor a asignar.
     */
    public void setMontoDivisa(Double montoDivisa) {
        this.montoDivisa = montoDivisa;
    }

    /**
     * Regresa el valor de tipoCambio.
     *
     * @hibernate.property column="TIPO_CAMBIO"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getTipoCambio() {
        return tipoCambio;
    }

    /**
     * Establece el valor de tipoCambio.
     *
     * @param tipoCambio El valor a asignar.
     */
    public void setTipoCambio(Double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    /**
     * Regresa el valor de tipoCambioInverso.
     *
     * @hibernate.property column="TIPO_CAMBIO_INVERSO"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
    public Double getTipoCambioInverso() {
        return tipoCambioInverso;
    }

    /**
     * Establece el valor de tipoCambioInverso.
     *
     * @param tipoCambioInverso El valor a asignar.
     */
    public void setTipoCambioInverso(Double tipoCambioInverso) {
        this.tipoCambioInverso = tipoCambioInverso;
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
     * Regresa el valor de contraDivisa.
     *
     * @hibernate.property column="CONTRA_DIVISA"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getContraDivisa() {
        return contraDivisa;
    }

    /**
     * Establece el valor de contraDivisa.
     *
     * @param contraDivisa El valor a asignar.
     */
    public void setContraDivisa(String contraDivisa) {
        this.contraDivisa = contraDivisa;
    }

    /**
     * Regresa el valor de clasifOperacion.
     *
     * @hibernate.property column="CLASIF_OPERACION"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getClasifOperacion() {
        return clasifOperacion;
    }

    /**
     * Establece el valor de clasifOperacion.
     *
     * @param clasifOperacion El valor a asignar.
     */
    public void setClasifOperacion(String clasifOperacion) {
        this.clasifOperacion = clasifOperacion;
    }

    /**
     * Regresa el valor de comentarios.
     *
     * @hibernate.property column="COMENTARIOS"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getComentarios() {
        return comentarios;
    }

    /**
     * Establece el valor de comentarios.
     *
     * @param comentarios El valor a asignar.
     */
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    /**
     * Regresa el valor de datosAdicional.
     *
     * @return Map.
     */
    public Map getDatosAdicionales() {
        return datosAdicionales;
    }

    /**
     * Establece el valor de datosAdicionales.
     *
     * @param datosAdicionales El valor a asignar.
     */
    public void setDatosAdicionales(Map datosAdicionales) {
        this.datosAdicionales = datosAdicionales;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof BitacoraEnviadas)) {
            return false;
        }
        BitacoraEnviadas castOther = (BitacoraEnviadas) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    /**
     * Regresa una cadena con los datos principales del registro.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).append("idConf", id.getIdConf()).
                append("claveContraparte", claveContraparte).append("contraDivisa", contraDivisa).
                append("montoDivisa", montoDivisa).append("tipoCambio", tipoCambio).
                toString();
    }
    
    /**
     * El identificador del registro.
     */
    private BitacoraEnviadasPK id = new BitacoraEnviadasPK();

    /**
     * El Folio interno de IXE para Reportar Operaciones a Banxico y que puede ser el
     * N&uacute;mero de Deal, el Folio Swap o el Folio Estrategia.
     */
    private Integer folio;

    /**
     * Clave de la Contraparte seg&uacute;n el Cat&aacute;logo Banxico.
     */
    private String claveContraparte;

    /**
     * El Nombre de la Contraparte del Deal.
     */
    private String nombre;

    /**
     * RFC de la Contraparte.
     */
    private String rfc;

    /**
     * Tipo de la Contraparte
     */
    private String tipoContraparte;

    /**
     * Numero que le indica a Banxico la Acci&oacute;n a Reportar.
     */
    private Integer accion;

    /**
     * Fecha valor del Deal: CASH, TOM, SPOT, +48.
     */
    private Integer plazo;

    /**
     * Si se trata de una compra o de una venta.
     */
    private Integer tipoOper;

    /**
     * El monto a reportar a Banxico.
     */
    private Double monto;

    /**
     * Campo Contado o Tipo de Derivado. Este valor actualmente para el SICA es fijo y es igual a 0.
     * Segun indicaciones de Ixe.
     */
    private Integer tipo;

    /**
     * Fecha en que se pact&oacute; el Deal.
     */
    private Date fechaConcertacion;

    /**
     * Fecha Liquidaci&oacute;n del Deal.
     */
    private Date fechaLiquidacion;

    /**
     * Campo para denotar si la operaci&oacute;n es parte de un swap (1) o no lo es (0).
     */
    private String swap;

    /**
     * Numero de error regresado por Banxico en caso de rechazar el Reporte del Deal.
     */
    private Integer error = new Integer(0);

    /**
     * Fecha de Envio del Reporte a Banxico.
     */
    private Date fechaEnvio;

    /**
     * Folio de Respuesta de Banxico en caso de aceptar el Reporte.
     */
    private Integer folioBanxico;

    /**
     * Fecha de Creacion del Registro.
     */
    private Date createDt;

    /**
     * Los Status con respecto al Reporte a Banxico son: Enviar, Enviada, Error, Cancelada
     */
    private String status;

    /**
     * Los Status con respecto a Swaps y Estrategias son: IN, PE
     */
    private String statusEstrategia;

    /**
     * El Numero de Contrato SICA del Reporte de Operaciones.
     */
    private String noCuenta;

    /**
     * Folio que utiliza TAS.
     */
    private Integer progressRecid;

    /**
     * La forma en que debe crearse el campo folio. No es una propiedad persistente.
     */
    private String modoCreacionFolioSwap;

    /**
     * El monto en la divisa de la operaci&oacute;n.
     */
    private Double montoDivisa = new Double(0);

    /**
     * El tipo de cambio.
     */
    private Double tipoCambio = new Double(0);

    /**
     * El inverso del tipo de cambio (1/tipoCambio).
     */
    private Double tipoCambioInverso = new Double(0);

    /**
     * El nombre del portafolios de Kondor.
     */
    private String folderk;

    /**
     * La divisa de referencia.
     */
    private String contraDivisa;

    /**
     * A, B, C, D, E.
     */
    private String clasifOperacion;

    /**
     * Los comentarios que se agregan como observaciones de los deals interbancarios que genera este
     * swap.
     */
    private String comentarios;

    /**
     * Repositorio para datos adicionales no persistentes.
     */
    private Map datosAdicionales = new HashMap();

    /**
     * Constante Status Enviar. Para reporte de Operaciones a Banxico.
     */
    public static final String STATUS_ENVIAR = "ENVIAR";

    /**
     * Constante Status Enviar1. Para Solicitud de Cancelaci&oacute;n de Operaciones
     * Reportadas previamente a Banxico.
     */
    public static final String STATUS_ENVIAR1 = "ENVIAR1";

    /**
     * Constante Status Enviada.
     */
    public static final String STATUS_ENVIADA = "ENVIADA";

    /**
     * Constante Status Cancelada.
     */
    public static final String STATUS_CANCELADA = "CANCELADA";

    /**
     * Constante Status Error.
     */
    public static final String STATUS_ERROR = "ERROR";

    /**
     * Constante para indicar que para este registro no ha sido capturado un deal interbancario.
     */
    public static final String ST_ESTRAT_PENDIENTE = "PE";

    /**
     * Constante para indicar que para este registro ya fue capturado un deal interbancario.
     */
    public static final String ST_ESTRAT_INICIO_SWAP = "IN";

    /**
     * Constante Mensaje Reporte Operaciones.
     */
    public static final Integer C_REPORTE = new Integer(71);

    /**
     * Constante Mensaje Posicion Inicial.
     */
    public static final Integer C_POSINI = new Integer(72);

    /**
     * Constante Mensaje Cancelacion de Reporte Operaciones.
     */
    public static final Integer C_CANCELA_OP = new Integer(73);

    /**
     * Constante Tipo Operacion Compra.
     */
    public static final Integer TIPO_OPE_COMPRA = new Integer(0);

    /**
     * Constante Tipo Operacion Venta.
     */
    public static final Integer TIPO_OPE_VENTA = new Integer(1);

    /**
     * Constante Campo Contado o Tipo de Derivado Contado.
     */
    public static final Integer TIPO_CONTADO = new Integer(0);

    /**
     * Constante Campo Contado o Tipo de Derivado Forward.
     */
    public static final Integer TIPO_FORWARD = new Integer(1);

    /**
     * Constante Campo Contado o Tipo de Derivado Futuro.
     */
    public static final Integer TIPO_FUTURO = new Integer(2);

    /**
     * Constante Campo Contado o Tipo de Derivado Opcion.
     */
    public static final Integer TIPO_OPCION = new Integer(3);

    /**
     * Ocurre cuando se intenta establecer una conexi&oacute;n con una versi&oacute;n de
     * protocolo no soportada.
     */
    public static final String ERR_VERSION = "ALBA_100";

    /**
     * Cuando el servidor de Operaciones rechaza la conexi&oacute;n.
     */
    public static final String ERR_CONEXION_RECHAZADA = "ALBA_101";

    /**
     * Ocurre cuando se env&iacute;an operaciones cuyo Plazo, Tipo de Operaci&oacute;n o Tipo
     * de Derivado no est&aacute; definido dentro del rango de constantes para cada grupo.
     */
    public static final String ERR_PARAMETRO = "ALBA_102";

    /**
     * Se produce cuando se env&iacute;a un n&uacute;mero de operaciones mayor al permitido.
     * El n&uacute;mero m&aacute;ximo de operaciones por mensaje es 24.
     */
    public static final String ERR_NUMERO_OP = "ALBA_103";

    /**
     * Se produce cuando la Contraparte no se encuentra en el Cat&aacute;logo del Sector
     * al que corresponde la operaci&oacute;n reportada. Este error no aplica a los
     * sectores empresas ni particulares.
     */
    public static final String ERR_CONTRAPARTE = "ALBA_104";

    /**
     * No se envi&oacute; un n&uacute;mero de folio v&aacute;lido para cancelar una
     * operaci&oacute;n. No se tiene registrada una operaci&oacute;n con el folio indicado.
     */
    public static final String ERR_OP_INEXISTENTE = "ALBA_105";

    /**
     * Ocurre cuando existe un error en la conexi&oacute;n.
     */
    public static final String ERR_FALLA_CONEXION = "ALBA_106";

    /**
     * Ocurre cuando la instituci&oacute;n que se intenta conectar no ha sido autorizada.
     * Ocurre cuando el Banco que se intenta conectar no est&aacute; autorizado.
     */
    public static final String ERR_BANCO_INEXISTENTE = "ALBA_107";

    /**
     * Se produce cuando el folio designado por la instituci&oacute;n para la operaci&oacute;n
     * (siempre y cuando esta no sea un swap) que se est&aacute; reportando ya se tiene registrado.
     */
    public static final String ERR_FOLIO_EXISTENTE = "ALBA_108";

    /**
     * Se produce cuando la nueva operaci&oacute;n (marcada como parte de un swap) no tiene
     * coincidencia en los campos (monto) con una operaci&oacute;n reportada previamente
     * (tambi&eacute;n marcada como swap) y cuyos folios de identificaci&oacute;n de swap
     * coinciden.
     */
    public static final String ERR_CONCIDENCIA_SWAP = "ALBA_110";

    /**
     * Se produce cuando la nueva operaci&oacute;n (marcada como parte de un swap) es del mismo tipo
     * (compra/venta) que una operaci&oacute;n reportada previamente (tambi&eacute;n marcada como
     * swap) y cuyos folios de identificaci&oacute;n de swap coinciden.
     */
    public static final String ERR_OP_DUPLICADA_SWAP = "ALBA_111";

    /**
     * Constante para el modo de creaci&oacute;n del folio con el swap al que est&aacute; ligado el
     * deal.
     */
    public static final String MODO_CREACION_FOLIO_SWAP = "SWAP";

    /**
     * Constante para el modo de creaci&oacute;n del folio sin generaci&oacute;n de folio.
     */
    public static final String MODO_CREACION_FOLIO_NULL = "NULL";

    /**
     * Constante para el modo de creaci&oacute;n del folio con sequence.
     */
    public static final String MODO_CREACION_FOLIO_SEQUENCE = "SEQUENCE";

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -6600415673900518347L;
}