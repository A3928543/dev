package com.ixe.ods.sica.sdo.dto;

import org.apache.commons.lang.StringUtils;

import com.ixe.ods.sica.model.Deal;

/**
 * Filtro para consultar deals con tipo de cambio especial
 * 
 * @author Cesar Jeronimo Gomez
 */
public class FiltroDealsTceDto {

	/** Constante para el filtro {@link #capturadoPor}, indica la inclusion solo de los deals de tce que fueron capturados por la mesa de cambios */
	public static final String CAPTURADO_POR_MESA_CAMBIOS = "0";
	
	/** Constante para el filtro {@link #capturadoPor}, indica la inclusion solo de los deals de tce que fueron capturados por un promotor */
	public static final String CAPTURADO_POR_PROMOTOR = "1";
	
	/** Constante para el filtro {@link #estatus}, indica la inclusion solo de los deals con ESTATUS_ESPECIAL = {@link Deal#STATUS_ESPECIAL_TC_AUTORIZADO} */
	public static final String ESTATUS_AUTORIZADA_TCE = "0";
	
	/** 
	 * Constante para el filtro {@link #estatus}, indica la inclusion solo de los deals con todos los status que maneja el sica (STATUS_DEAL)
	 * menos {@link Deal#STATUS_DEAL_TOTALMENTE_LIQUIDADO}
	 */
	public static final String ESTATUS_PENDIENTE = "1";
	
	/**
	 * Entero positivo nulo
	 */
	public static final int NULL_POSITIVE_INT = -1;
	
	/**
	 * Filtra por el perfil que capturo
	 */
	private String capturadoPor;
	
	/**
	 * Numero de deal
	 */
	private String idDeal;
	
	/**
	 * Estatus del deal
	 */
	private String estatus;
	
	/**
	 * Identificador de la persona para el promotor
	 */
	private int idPersonaPromotor;

	/**
	 * Constructor por default
	 */
	public FiltroDealsTceDto() {
		super();
		this.idPersonaPromotor = NULL_POSITIVE_INT;
	}

	/**
	 * Full constructor
	 * 
	 * @param capturadoPor
	 * @param idDeal
	 * @param estatus
	 */
	public FiltroDealsTceDto(String capturadoPor, String idDeal, String estatus) {
		super();
		this.capturadoPor = capturadoPor;
		this.idDeal = idDeal;
		this.estatus = estatus;
		this.idPersonaPromotor = NULL_POSITIVE_INT;
	}

	/**
	 * Retorna el valor de {@link #capturadoPor}
	 * @return
	 */
	public String getCapturadoPor() {
		return capturadoPor;
	}

	/**
	 * Retorn el valor de {@link #idDeal}
	 * @return
	 */
	public String getIdDeal() {
		return idDeal;
	}

	/**
	 * Retorna el valor de {@link #estatus}
	 * @return
	 */
	public String getEstatus() {
		return estatus;
	}
	
	/**
	 * Establece el valor de {@link #capturadoPor}
	 * @param capturadoPor
	 */
	public void setCapturadoPor(String capturadoPor) {
		this.capturadoPor = capturadoPor;
	}

	/**
	 * Establece el valor de {@link #idDeal}
	 * @param idDeal
	 */
	public void setIdDeal(String idDeal) {
		this.idDeal = idDeal;
	}

	/**
	 * Establece el valor de {@link #estatus}
	 * @param estatus
	 */
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public int getIdPersonaPromotor() {
		return idPersonaPromotor;
	}

	public void setIdPersonaPromotor(int idPersonaPromotor) {
		this.idPersonaPromotor = idPersonaPromotor;
	}

	/**
	 * Devuelve true si {@link #capturadoPor} no es especificado
	 * @return
	 */
	public boolean isCapturadoPorCualquiera() {
		return StringUtils.isEmpty(capturadoPor);
	}
	
	/**
	 * Devuelve true si {@link #capturadoPor} es mesa de cambios
	 * @return
	 */
	public boolean isCapturadoPorMesaCambios() {
		return CAPTURADO_POR_MESA_CAMBIOS.equals(capturadoPor);
	}
	
	/**
	 * Devuelve true si {@link #capturadoPor} es promocion
	 * @return
	 */
	public boolean isCapturadoPorPromotor() {
		return CAPTURADO_POR_PROMOTOR.equals(capturadoPor);
	}
	
	/**
	 * Devuelve true si {@link #estatus} no es especificado
	 * @return
	 */
	public boolean isEstatusCualquiera() {
		return StringUtils.isEmpty(estatus);
	}
	
	/**
	 * Devuelve true si {@link #estatus} es autorizada tc
	 * @return
	 */
	public boolean isEstatusAutorizadoTce() {
		return ESTATUS_AUTORIZADA_TCE.equals(estatus);
	}
	
	/**
	 * Devuelve true si {@link #estatus} es pendiente
	 * @return
	 */
	public boolean isEstatusPendiente() {
		return ESTATUS_PENDIENTE.equals(estatus);
	}
	
	/**
	 * Devuelve true si {@link #idDeal} no es especificado
	 * @return
	 */
	public boolean isNullDeal() {
		return StringUtils.isEmpty(idDeal);
	}
	
	/**
	 * Determina si el id del promotor es nulo
	 * @return
	 */
	public boolean isIdPersonaPromotorNull() {
		return idPersonaPromotor == NULL_POSITIVE_INT;
	}

	public String toString() {
		return "FiltroDealsTceDto [" 
			+ "capturadoPor=" + capturadoPor 
			+ ", idDeal=" + idDeal 
			+ ", estatus=" + estatus
			+ ", idPersonaPromotor=" + idPersonaPromotor
		+ "]";
	}
	
}
