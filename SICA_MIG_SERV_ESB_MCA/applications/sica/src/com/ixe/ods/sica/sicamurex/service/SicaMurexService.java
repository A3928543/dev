package com.ixe.ods.sica.sicamurex.service;

import java.util.Date;
import java.util.List;

import net.sf.hibernate.HibernateException;

import com.ixe.ods.sica.model.Corte;
import com.ixe.ods.sica.model.BitacoraCorte;
import com.ixe.ods.sica.model.Divisa;

public interface SicaMurexService {
	
	/**
	 * Obtiene una lista de la posicion de todas las divisas 
	 * agrupadas por Divisa,Fecha valor.
	 * @param idMesaCambio
	 * @param usuario
	 * @param df
	 * @param dnf
	 * @param ma
	 * @return
	 */
	public List getPosicionDivisas( Integer idMesaCambio, String usuario, boolean df, boolean dnf, boolean ma );
	
	/**
	 * Obtiene una lista con los DEALS Interbancarios que 
	 * hacen la posicion de SICA a cero (Vista Previa).
	 * @return
	 */
	public List getVistaPreviaDealsReinicioPosicion();
	
	/**
	 * Obtiene una lista con el detalle del corte de hoy.
	 * @return
	 */
	public List getCorteDetalleByFechaHoy();

	/**
	 * Obtiene una lista con los DEALS Interbancarios que 
	 * hacen la posicion de SICA en base al corte de hoy.
	 * @return
	 */
	public List getDealsInterbancariosByFechaHoy();

	/**
	 * Obtener el corte de hoy.
	 * @return
	 */
	public Corte findCorteByFechaHoy();
	
	/**
	 * Obtener la ultima bitacora del corte.
	 * @return
	 */
	public BitacoraCorte findMaxBitacoraCorteByIdCorte(Corte corte);
	
	
	/**
	 * Obtener el numero de cortes de hoy.
	 * @return
	 */
	public Integer findNumCorteByFechaHoy();

	/**
	 * Este metodo se encarga de actualizar el Estado de un corte
	 * enviado a MUREX a Procesado
	 * @param usuario
	 */
	public void actualizarEstadoCorteProcesado(String usuario);

	/**
	 * Este metodo actualiza SC_DEAL_REINICIO_POSICION con el Id del DEAL INTERBANCARIO
	 * y si se trata de  COMPRA o VENTA .
	 * @param usuario
	 * @param idDeal
	 * @param divisa
	 * @param isCompra
	 */
	public void actualizarReinicioPosicion(String usuario, int idDeal, Divisa divisa,
			boolean isCompra);
	
	/**
	 * Metodo que obtiene los detalles del corte por fecha y divisa
	 * @param fecha
	 * @param divisa
	 * @return Lista de Detalles del Corte.
	 */
	public List getCorteDetalleByFechaAndDivisa(Date fecha, Divisa divisa);

	
	/**
	 * Este metodo consulta el acumulado de CV que hay en la tabla sc_posicion_log
	 * e interpreta las cancelaciones como operacion contraria, a efecto de armar corte y enviarlo a MUREX
	 */
	public void getAcumuladosCV();
	
	/**
	 * Invoca al SP que realiza la logica de sumatoria de cortes
	 */
	public void invocarSP() throws HibernateException;
	
	/**
	 * Actualiza los id_corte de la tabla sc_posicion_log_cortes del ultimo corte generado ya que fue enviado a MX
	 */
	public void setIdCortePorEnvioMX();
	
	/**
	 * Obtiene los datos para mostrar en el monitor de deals interbancarios
	 */
	public List findDatosMonitorDI(Date fecha, Divisa divisa);
	
	

	
}
