package com.ixe.ods.sica.model;

import java.io.Serializable;

import com.ixe.ods.bup.model.Sucursal;


/**
 * Clase persistente para la tabla DealReinicioPosicion.
 *
 * @hibernate.class table="SC_DEAL_REINICIO_POSICION"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.DealReinicioPosicion"
 * dynamic-update="true"
 *
 * @hibernate.query name="findDealReinicioById" query="FROM DealReinicioPosicion as drp where drp.idDealReinicioPosicion = ?"
 * @hibernate.query name="findDealReinicioByCorteId" query="FROM DealReinicioPosicion as drp where drp.idCorte = ?"
 * @hibernate.query name="findDealReinicio" query="FROM DealReinicioPosicion as drp where drp.idCorte = ? and drp.idDivisa=? and operacion = ?"
 * @hibernate.query name="findDealReiniciobyfechaAndDivisa" query="FROM DealReinicioPosicion as drp where to_char(drp.corte.fechaAlta, 'dd/MM/yyyy') = ? and  drp.idDivisa = ?"
 * @hibernate.query name="findDealReiniciobyfecha" query="FROM DealReinicioPosicion as drp where to_char(drp.corte.fechaAlta, 'dd/MM/yyyy') = ? "
 * 
 * 
 * @author GMS
 * 			
 * @version $Revision: 1.1.2.1.12.1 $
 */
public class DealReinicioPosicion implements Serializable{


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Identificador de la tabla
	 */
	private int idDealReinicioPosicion;
	
	/**
	 * Identificador del DEAL
	 */
	private Integer idDeal;
	
	/**
	 * Identificador del corte enviado a MUREX
	 */
	private int idCorte;
	
	/**
	 * Identificador de la Divisa
	 */
	private String idDivisa;
	
	/**
	 * Tipo de operacion Compra o Venta
	 */
	private String operacion;
	
	/**
	 * Monto del DEAL Interbancario
	 */
	private double monto;
	
	/**
	 * Tipo de cambio del DEAL Interbancario
	 */
	private double tipoCambio;
	
	/**
	 * Corte
	 */
	private Corte corte;
	
	
	/**
	 * Constructor por defecto
	 * @return
	 */
	public DealReinicioPosicion() {}
	
	
	/**
	 * Constructor con todos los campos de la clase
	 * @param idDealReinicioPosicion
	 * @param idDeal
	 * @param idCorte
	 * @param idDivisa
	 * @param operacion
	 * @param monto
	 * @param tipoCambio
	 */
	public DealReinicioPosicion(int idDealReinicioPosicion, Integer idDeal,
			int idCorte, String idDivisa, String operacion, double monto,
			double tipoCambio) {
		super();
		this.idDealReinicioPosicion = idDealReinicioPosicion;
		this.idDeal = idDeal;
		this.idCorte = idCorte;
		this.idDivisa = idDivisa;
		this.operacion = operacion;
		this.monto = monto;
		this.tipoCambio = tipoCambio;
	}

	//Property accesors
	
    /**
     * Regresa el valor de idCorteDetalle
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_DEAL_REINICIO_POSICION"
     * unsaved-value="null"
     *
     * @hibernate.generator-param name="sequence"
     * value="SC_DEAL_REINICIO_POSICION_SEQ"
     * @return int.
     */
	public int getIdDealReinicioPosicion() {
		return idDealReinicioPosicion;
	}
	
	/** 
	 * 
	 *@param idDealReinicioPosicion
	 */
	public void setIdDealReinicioPosicion(int idDealReinicioPosicion) {
		this.idDealReinicioPosicion = idDealReinicioPosicion;
	}
	
	/**
	 *@hibernate.property column="ID_DEAL"
	 * not-null="false"
     * unique="false" 
	 * @return int
	 */
	public Integer getIdDeal() {
		return idDeal;
	}
	
	/**
	 * 
	 * @param idDeal
	 */
	public void setIdDeal(Integer idDeal) {
		this.idDeal = idDeal;
	}
	
	/**
	 * *@hibernate.property column="ID_CORTE"
	 * not-null="true"
     * unique="false" 
	 * @return int 
	 */
	public int getIdCorte() {
		return idCorte;
	}
	
	/**
	 * 
	 * @param idCorte
	 */
	public void setIdCorte(int idCorte) {
		this.idCorte = idCorte;
	}
	
	/**
	 * 
	 * *@hibernate.property column="ID_DIVISA"
	 * not-null="true"
     * unique="false" 
	 * @return String idDivisa
	 */
	public String getIdDivisa() {
		return idDivisa;
	}
	
	/**
	 * 
	 * @param idDivisa
	 */
	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}
	
	/**
	 * *@hibernate.property column="OPERACION"
	 * not-null="true"
     * unique="false" 
	 * @return String Compra o Venta
	 */
	public String getOperacion() {
		return operacion;
	}
	
	/**
	 * 
	 * @param operacion
	 */
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	
	/**
	 * *@hibernate.property column="MONTO"
	 * not-null="true"
     * unique="false" 
	 * @return double monto
	 */
	public double getMonto() {
		return monto;
	}
	
	/**
	 * 
	 * @param monto
	 */
	public void setMonto(double monto) {
		this.monto = monto;
	}
	
	/**
	 * *@hibernate.property column="TIPO_CAMBIO"
	 * not-null="true"
     * unique="false"
	 * @return double tipoCambio
	 */
	public double getTipoCambio() {
		return tipoCambio;
	}
	
	/**
	 * 
	 * @param tipoCambio
	 */
	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	
    /**
     * Regresa el valor de Corte.
     *
     * @hibernate.many-to-one column="ID_CORTE"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Corte"
     * outer-join="auto"
     * unique="false"
     * update="false"
     * insert="false"
     * @return Corte.
     */
    public Corte getCorte() {
        return corte;
    }
    
	/**
	 * 
	 * @param corte
	 */
	public void setCorte(Corte corte) {
		this.corte = corte;
	}
}