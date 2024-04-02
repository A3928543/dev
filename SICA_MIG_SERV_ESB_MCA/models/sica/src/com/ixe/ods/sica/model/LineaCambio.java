/*
 * $Id: LineaCambio.java,v 1.7.68.3 2016/08/03 01:08:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 - 2010 LegoSoft S.C.
 */
                                             
package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
                                                  
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.utils.BDUtils;

/**
 * Clase persistente para la tabla SC_LINEA_CAMBIO.
 *
 * @hibernate.class table="SC_LINEA_CAMBIO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.LineaCambio"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.7.68.3 $ $Date: 2016/08/03 01:08:01 $
 */
public class LineaCambio implements Serializable {


	/**
     * Constructor por default.
     */
    public LineaCambio() {
        super();
    }

    /**
     * Realiza el corrimiento de los usos por fecha valor, desde 96 horas hasta cash.
     */
    public void realizarCorrimiento() {
        BigDecimal usoNuevo = getUsoCash().add(getUsoTom());
        setUsoCash(usoNuevo);
        usoNuevo = getUsoSpot();
        setUsoTom(usoNuevo);
        usoNuevo = getUso72Hr();
        setUsoSpot(usoNuevo);
        usoNuevo = getUso96Hr();
        setUso72Hr(usoNuevo);
        setUso96Hr(BDUtils.generar2(0.0));
    }

    /**
     * Regresa el valor de importe - usoCash.
     *
     * @return BigDecimal.
     */
    public BigDecimal getSaldoCash() {
        return getImportePAyTF().subtract(getUsoCash());
    }

    
    /**
     * Regresa el valor de importe - usoTom.
     *
     * @return BigDecimal.
     */
    public BigDecimal getSaldoTom() {
        return getSaldoFV();
    }

  
    /**
     * Regresa el valor de importe - usoSpot.
     *
     * @return BigDecimal.
     */
    public BigDecimal getSaldoSpot() {
        return getSaldoFV();
    }

  

    /**
     * Regresa el valor de importe - uso72Hr.
     *
     * @return BigDecimal.
     */
    public BigDecimal getSaldo72Hr() {
        return getSaldoFV();
    }

   
    /**
     * Regresa el valor de importe - uso96Hr.
     *
     * @return BigDecimal.
     */
    public BigDecimal getSaldo96Hr() {
        return getSaldoFV();
    }
   

    /**
     * Regresa el valor  de idLineaCambio.
     *
     * @return Integer.
     * @hibernate.id generator-class="sequence"
     * column="ID_LINEA_CAMBIO"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_LINEA_CAMBIO_SEQ"
     */
    public Integer getIdLineaCambio() {
        return idLineaCambio;
    }

    /**
     * Establece el valor de idLineaCambio.
     *
     * @param idLineaCambio El valor a asignar.
     */
    public void setIdLineaCambio(Integer idLineaCambio) {
        this.idLineaCambio = idLineaCambio;
    }

    /**
     * Regresa el valor de casoExcepcion.
     *
     * @hibernate.property column="CASO_EXCEPCION"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isCasoExcepcion() {
        return casoExcepcion;
    }

    /**
     * Establece el valor de casoExcepcion.
     *
     * @param casoExcepcion El valor a asignar.
     */
    public void setCasoExcepcion(boolean casoExcepcion) {
        this.casoExcepcion = casoExcepcion;
    }

    /**
     * Regresa el valor del importe de la linea de credito para el monto de Pago Anticipado y Toma en Firme
     *
     * @return double.
     * @hibernate.property column="IMPORTE_PAYTF"
     * not-null="false"
     * unique="false"
     */
    public BigDecimal getImportePAyTF() {
		return importePAyTF;
	}

    /**
     * Establece el valor de importePAyTF. 
     *
     * @param importePAyTF El valor a asignar.
     */
	public void setImportePAyTF(BigDecimal importePAyTF) {
		this.importePAyTF = importePAyTF;
	}

	/**
     * Regresa el valor del importe de la linea de credito para el monto de Fecha Valor
     *
     * @return double.
     * @hibernate.property column="IMPORTE_FECHA_VALOR"
     * not-null="false"
     * unique="false"
     */
	public BigDecimal getImporteFV() {
		return importeFV;
	}

	/**
     * Establece el valor del monto de la linea de credito para el monto Fecha Valor importeFV 
     *
     * @param importeFV El valor a asignar.
     */
	public void setImporteFV(BigDecimal importeFV) {
		this.importeFV = importeFV;
	}
    
    
   /**
    * Regresa el valor del numero de excesos para Pago Anticipado y Toma en Firme
    *  
    * @return Integer.
    * @hibernate.property column="EXCESO_PAYTF"
    * @hibernate.cglib.use_reflection_optimizer=false
    * not-null=false
    * unique=false
    */
    public Integer getExcesoPAyTF() {
		return excesoPAyTF;
	}


    /**
     * Establece el valor del numero de excesos para Pago Anticipado y Toma en Firme.
     * 
     * @param excesoPAyTF El valor a asignar
     */
	public void setExcesoPAyTF(Integer excesoPAyTF) {
		this.excesoPAyTF = excesoPAyTF;
	}


	/**
	 * Regresa el valor del numero de excesos para Fecha Valor
	 * 
	 * @return Integer.
	 * @hibernate.property column="EXCESO_FV"
	 * not-null="false"
	 * unique="false" 
	 */
	public Integer getExcesoFV() {
		return excesoFV;
	}


	/**
	 * Establece el valor del numero de excesos para Fecha Valor
	 * 
	 * @param excesoFV El valor a asignar
	 */
	public void setExcesoFV(Integer excesoFV) {
		this.excesoFV = excesoFV;
	}


	/**
     * Regresa el valor de usoCash, ahora tambien uso Pago Anticipado y Toma en Firme . Si el uso se vuelve cero y la l&iacute;nea se encuentra
     * suspendida, se reactiva autom&aacute;ticamente.
     *
     * @return double.
     * @hibernate.property column="USO_CASH"
     * not-null="true"
     * unique="false"
     */
    public BigDecimal getUsoCash() {
        return usoCash;
    }

    /**
     * Establece el valor de usoCash. Si el uso se vuelve cero y la l&iacute;nea se encuentra
     * suspendida, se reactiva autom&aacute;ticamente.
     *
     * @param usoCash El valor a asignar.
     */
    public void setUsoCash(BigDecimal usoCash) {
        this.usoCash = usoCash;
    }

    /**
     * Regresa el valor de usoTom.
     *
     * @return BigDecimal.
     * @hibernate.property column="USO_TOM"
     * not-null="true"
     * unique="false"
     */
    public BigDecimal getUsoTom() {
        return usoTom;
    }

    /**
     * Establece el valor de usoTom.
     * 
     * @param usoTom El valor a asignar.
     */
    public void setUsoTom(BigDecimal usoTom) {
        this.usoTom = usoTom;
    }

    /**
     * Regresa el valor de usoCash.
     *
     * @return BigDecimal.
     * @hibernate.property column="USO_SPOT"
     * not-null="true"
     * unique="false"
     */
    public BigDecimal getUsoSpot() {
        return usoSpot;
    }

    /**
     * Establece el valor de usoSpot.
     *
     * @param usoSpot El valor a asignar.
     */
    public void setUsoSpot(BigDecimal usoSpot) {
        this.usoSpot = usoSpot;
    }

    /**
     * Regresa el valor de uso72Hr.
     *
     * @return BigDecimal.
     * @hibernate.property column="USO_72HR"
     * not-null="true"
     * unique="false"
     */
    public BigDecimal getUso72Hr() {
        return uso72Hr;
    }

    /**
     * Establece el valor de uso72Hr.
     *
     * @param uso72Hr El valor a asignar.
     */
    public void setUso72Hr(BigDecimal uso72Hr) {
        this.uso72Hr = uso72Hr;
    }

    /**
     * Regresa el valor de uso96Hr.
     *
     * @return BigDecimal.
     * @hibernate.property column="USO_96HR"
     * not-null="true"
     * unique="false"
     */
    public BigDecimal getUso96Hr() {
        return uso96Hr;
    }

    /**
     * Establece el valor de uso96Hr.
     *
     * @param uso96Hr El valor a asignar.
     */
    public void setUso96Hr(BigDecimal uso96Hr) {
        this.uso96Hr = uso96Hr;
    }
 
    /**
     * Regresa el valor de promedioLinea.
     *
     * @return BigDecimal.
     * @hibernate.property column="PROMEDIO_LINEA"
     * not-null="true"
     * unique="false"
     */
    public BigDecimal getPromedioLinea() {
        return promedioLinea;
    }

    /**
     * Establece el valor de promedioLinea.
     *
     * @param promedioLinea El valor a asignar.
     */
    public void setPromedioLinea(BigDecimal promedioLinea) {
        this.promedioLinea = promedioLinea;
    }

    /**
     * Regresa el valor de ultimaModificacion.
     *
     * @return Date.
     * @hibernate.property column="ULTIMA_MODIFICACION"
     * not-null="true"
     * unique="false"
     */
    public Date getUltimaModificacion() {
        return ultimaModificacion;
    }

    /**
     * Fija el valor de ultimaModificacion.
     *
     * @param ultimaModificacion El valor a asignar.
     */
    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    /**
     * Regresa el valor de numeroExcepciones
     *
     * @return int.
     * @hibernate.property column="NUMERO_EXCEPCIONES"
     * not-null="true"
     * unique="false"
     */
    public int getNumeroExcepciones() {
        return numeroExcepciones;
    }

    /**
     * Fija el valor de numeroExcepciones.
     *
     * @param numeroExcepciones El valor a asignar.
     */
    public void setNumeroExcepciones(int numeroExcepciones) {
        this.numeroExcepciones = numeroExcepciones;
    }

    /**
     * Regresa el valor de numeroExcepcionesMes.
     *
     * @return int.
     * @hibernate.property column="NUMERO_EXCEPCIONES_MES"
     * not-null="true"
     * unique="false"
     */
    public int getNumeroExcepcionesMes() {
        return numeroExcepcionesMes;
    }

    /**
     * Fija el valor de numeroExcepcionesMes.
     *
     * @param numeroExcepcionesMes El valor a asignar.
     */
    public void setNumeroExcepcionesMes(int numeroExcepcionesMes) {
        this.numeroExcepcionesMes = numeroExcepcionesMes;
    }

    /**
     * Regresa el valor de vencimiento.
     *
     * @return Date.
     * @hibernate.property column="VENCIMIENTO"
     * not-null="true"
     * unique="false"
     */
    public Date getVencimiento() {
        return vencimiento;
    }

    /**
     * Fija el valor de vencimiento.
     *
     * @param vencimiento El valor a asignar.
     */
    public void setVencimiento(Date vencimiento) {
        this.vencimiento = vencimiento;
    }

    /**
     * Regresa el valor de statusLinea.
     *
     * @return String.
     * @hibernate.property column="STATUS_LINEA"
     * not-null="true"
     * unique="false"
     */
    public String getStatusLinea() {
        return statusLinea;
    }

    /**
     * Fija el valor de statusLinea.
     *
     * @param statusLinea El valor a asignar.
     */
    public void setStatusLinea(String statusLinea) {
        this.statusLinea = statusLinea;
    }

    /**
     * Regresa el valor de corporativo.
     *
     * @return Persona.
     * @hibernate.many-to-one column="ID_CORPORATIVO"
     * cascade="none"
     * class="com.ixe.ods.bup.model.Persona"
     * outer-join="auto"
     * unique="false"
     */
    public Persona getCorporativo() {
        return corporativo;
    }
        
    /**
     * Fija el valor de corporativo.
     *
     * @param corporativo El valor a asignar.
     */
    public void setCorporativo(Persona corporativo) {
        this.corporativo = corporativo;
    }

    /**
     * Regresa el Tipo de Autorizacion asociado a la Linea de Credito
     * @return com.ixe.ods.sica.model.TipoAutorizacion
     * @hibernate.many-to-one column="ID_TIPO_AUTORIZACION"
     * cascade="none"
     * class ="com.ixe.ods.sica.model.TipoAutorizacion"
     * outer-join="auto"
     * unique="false"
     */
    public TipoAutorizacion getTipoAutorizacion(){
    	return tipoAutorizacion;
    }

    /**
     * Establece el valor del tipoAutorizacion
     * @param tipoAutorizacion El valor a asignar
     */
    public void setTipoAutorizacion(TipoAutorizacion tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}
    
    /**
     * Regresa la entidad Formalizacion asociada a la linea de credito
     * @return com.ixe.ods.sica.model.Formalizacion
     * @hibernate.many-to-one column="ID_FORMALIZACION"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Formalizacion"
     * outer-join="auto"
     * unique="false"
     */
	public Formalizacion getFormalizacion() {
		return formalizacion;
	}
	
	/**
	 * Establece el valor de la entidad Formalizacion asociada a la linea de credito
	 * @param formalizacion
	 */
	public void setFormalizacion(Formalizacion formalizacion) {
		this.formalizacion = formalizacion;
	}

	/**
	 * Regresa la Instancia Facultada asociada la linea de credito
	 * @return com.ixe.ods.sica.model.InstanciaFacultada
	 * @hibernate.many-to-one column="ID_INSTANCIA_FACULTADA"
	 * cascade="none"
	 * class="com.ixe.ods.sica.model.InstanciaFacultada"
	 * outer-join="auto"
	 * unique="false"
	 */
	public InstanciaFacultada getInstanciaFacultada() {
		return instanciaFacultada;
	}

	/**
	 * Establece el valor de la Instancia Facultada asociada a la linea de credito
	 * @param instanciaFacultada
	 */
	public void setInstanciaFacultada(InstanciaFacultada instanciaFacultada) {
		this.instanciaFacultada = instanciaFacultada;
	}

    /**
     * Regresa la Descripcion del Status de la Linea de Cr&eacute;dito.
     *
     * @return String.
     */
    public String getDescripcionStatus() {
        if (STATUS_SOLICITUD.equals(statusLinea)) {
            return "Solicitud";
        }
        else if (STATUS_APROBADA.equals(statusLinea)) {
            return "Aprobada";
        }
        else if (STATUS_ACTIVADA.equals(statusLinea)) {
            return "Activada";
        }
        else if (STATUS_VENCIDA.equals(statusLinea)) {
            return "Vencida";
        }
        else if (STATUS_SUSPENDIDA.equals(statusLinea)) {
            return "Suspendida";
        }
        else if (STATUS_CANCELADA.equals(statusLinea)) {
            return "Cancelada";
        }
        return "Desconocido";
    }

    /**
     * Obtiene el valor por "default" del Vencimiento, que es un anio.
     *
     * @return Date El valor por "default" del Vencimiento.
     */
    public Date getVencimientoDefault() {
        Calendar gc = new GregorianCalendar();
        gc.add(Calendar.YEAR, 1);
        return gc.getTime();
    }

    
    /**
     * Obtiene el uso Fecha Valor 
     * @return Bigdecimal 
     */
    public BigDecimal getUsoFechaValor(){
    	return getUsoTom().add(getUsoSpot()).add(getUso72Hr()).add(getUso96Hr());
    }
    
    
    /**
     * Obtiene el uso total de la linea de credito Total del uso de PAyTF + total del uso FV.
     * Uso Cash = Uso PAyTF
     * @return Bigdecimal el monto del uso total
     */
    public BigDecimal getUsoTotal() {
        return getUsoCash().add(getUsoFechaValor());
    }

    /**
     * Obtiene el total de la linea autorizada, Monto Pago Anticipado + Monto Fecha Valor
     * @return BigDecimal total de la linea Autorizada
     */
    public BigDecimal getTotalLineaAutorizada(){
    	return getImportePAyTF().add(getImporteFV());
    }
    
    /**
     * Obtiene el total de la linea de credito que se encuentra en Uso, Total del uso de PAyTF + Total del uso FV 
     * @return BigDecimal totalUsoLineaCredito
     */
    public BigDecimal getSaldoLineaAutorizada(){
    	return getTotalLineaAutorizada().subtract(getUsoTotal());
    }
    
    /**
     * Obtiene el saldo del monto PAyTF
     * @return
     */
    public BigDecimal getSaldoPAyTF(){
    	return getImportePAyTF().subtract(getUsoCash());
    }
    
    /**
     * Obtiene el saldo del monto FV
     * @return
     */
    public BigDecimal getSaldoFV(){
    	return getImporteFV().subtract(getUsoFechaValor());
    }
    
    
    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals(java.lang.Object).
     */
    public boolean equals(Object other) {
        if (!(other instanceof LineaCambio)) {
            return false;
        }
        LineaCambio castOther = (LineaCambio) other;
        return new EqualsBuilder().append(this.getIdLineaCambio(),
                castOther.getIdLineaCambio()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @return <code>int</code>
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdLineaCambio()).toHashCode();
    }
   


	/**
     * El identificador de la l&iacute;nea de cambios.
     */
    private Integer idLineaCambio;

    private boolean casoExcepcion = false;

    /**
     * El importe global de la l&iacute;nea de cambios.
     */
    private BigDecimal importePAyTF = new BigDecimal("0.00");

    /**
     * El importe autorizado para remesas.
     */
    private BigDecimal importeFV = new BigDecimal("0.00");

    /**
     * Tipo de autorizacion asociado a la Linea de Credito
     */
    private TipoAutorizacion tipoAutorizacion;
   
	/**
     * Formalizacion asociada a la linea de credito
     */
    private Formalizacion formalizacion;
    
    /**
     * Instancia Facultada asociada a la linea de credito 
     */
    private InstanciaFacultada instanciaFacultada;
    
    /**
     * El numero de excesos para Pago Anticipado y Toma en Firme
     */
    private Integer excesoPAyTF;
    
    /**
     * El numero de excesos para Fecha valor
     */
    private Integer excesoFV;
   
    /**
     * El uso global en CASH 
     */
    private BigDecimal usoCash = new BigDecimal("0.00");
  
    /**
     * El uso global en TOM 
     */
    private BigDecimal usoTom = new BigDecimal("0.00");

  
    /**
     * El uso global en SPOT 
     */
    private BigDecimal usoSpot = new BigDecimal("0.00");

   
    /**
     * El uso global en 72HR (incluye remesas).
     */
    private BigDecimal uso72Hr = new BigDecimal("0.00");

    
    /**
     * El uso global en 96HR (incluye remesas).
     */
    private BigDecimal uso96Hr = new BigDecimal("0.00");

    
    /**
     * No utilizado.
     */
    private BigDecimal promedioLinea = new BigDecimal("0.00");

    /**
     * La fecha de &uacute;ltima modificaci&oacute;n del registro.
     */
    private Date ultimaModificacion = new Date();

    /**
     * El n&uacute;mero de excepciones de la l&iacute;nea de cr&eacute;dito en toda la historia de
     * la l&iacute;nea de cr&eacute;dito.
     */
    private int numeroExcepciones;

    /**
     * El n&uacute;mero de excepciones de la l&iacute;nea en lo que va del mes actual.
     */
    private int numeroExcepcionesMes;

    /**
     * Fecha en que se vence la L&iacute;nea de Cr&eacute;dito.
     * El valor por default del vencimiento es a un anio.
     */
    private Date vencimiento = getVencimientoDefault();

    /**
     * SO (en solicitud), AP (aprobada), OK (activada), VE (Vencida), SU (suspendida).
     * El valor por default es STATUS_SOLICITUD.
     */
    private String statusLinea = STATUS_SOLICITUD;

    /**
     * Relaci&oacute; muchos-a-uno con Persona.
     */
    private Persona corporativo;

    /**
     * Constante Status Solicitud.
     */
    public static final String STATUS_SOLICITUD = "SO";

    /**
     * Constante Status Aprobada.
     */
    public static final String STATUS_APROBADA = "AP";

    /**
     * Constante Status Activada.
     */
    public static final String STATUS_ACTIVADA = "OK";

    /**
     * Constante Status Vencida.
     */
    public static final String STATUS_VENCIDA = "VE";

    /**
     * Constante Status Suspendida.
     */
    public static final String STATUS_SUSPENDIDA = "SU";

    /**
     * Constante Status Cancelada
     */
    public static final String STATUS_CANCELADA = "CA";

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -6831063938789839146L;
}
