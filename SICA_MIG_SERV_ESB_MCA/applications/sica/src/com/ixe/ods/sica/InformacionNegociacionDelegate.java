package com.ixe.ods.sica;

import java.util.List;

import org.apache.log4j.Logger;

import com.ixe.ods.sica.dao.InformacionNegociacionDao;

/**
 * Implementa m&eacute;todos delegados para operaciones de
 * la pantalla de informaci&oacute; de negociaci&oacute;n 
 * 
 * @author Efren Trinidad, Na-at Technologies
 *
 */
public class InformacionNegociacionDelegate {
	
	private static final Logger LOGGER = Logger.getLogger(InformacionNegociacionDelegate.class);
	
	/**
	 * Recupera la lista de clientes de un promotor.
	 * 
	 * @param idTipoEjecutivo El tipo de ejecutivo que hace la consulta
	 * @param idPersona El idPersona del ejecutivo
	 * @return Lista de 
	 */
	public List getClientesEjecutivo(Integer idPersona, boolean favoritos){  
		
		return getInformacionNegociacionDao().findClienteEjecutivoConInfoNegociacion(idPersona, favoritos);
	
	}
	/**
	 * Marca o desmarca un cliente como favorito de un promotor
	 * 
	 * @param noContrato
	 * @param favorito
	 * @return
	 */
	public boolean marcarClienteFavorito(String noCuenta, boolean favorito){
		return getInformacionNegociacionDao().updateClienteFavorito(noCuenta, favorito);
	}
	
	/**
	 * Obtiena las ultimas 5 operaciones para un cliente
	 * 
	 * @param idCliente Identificador del cliente seleccionado
	 * @return
	 */
	public List getLastFiveDeals(Integer idCliente) {
		List lastFiveDeals = getInformacionNegociacionDao().findLastFiveDeals(idCliente); 
		LOGGER.debug("Ultimos 5 deals encontrados: " + lastFiveDeals.size());
		return lastFiveDeals;
	}
	
	/**
	 * Actualiza la informacion del cliente
	 * 
	 * @param idCliente
	 * @param phoneNumber
	 * @param name
	 * @param email
	 * @return
	 */
	public boolean editContactInfo(Integer idCliente, String phoneNumber, String name, String email) {
		LOGGER.debug("editContactInfo - idCliente=" + idCliente + ", phoneNumber=" + phoneNumber + ", name=" + name + ", email=" + email);
		return informacionNegociacionDao.editContactInfo(idCliente, phoneNumber, name, email);
	}
	
	/**
	 * @return the informacionNegociacionDao
	 */
	public InformacionNegociacionDao getInformacionNegociacionDao() {
		return informacionNegociacionDao;
	}

	/**
	 * @param informacionNegociacionDao the informacionNegociacionDao to set
	 */
	public void setInformacionNegociacionDao(
			InformacionNegociacionDao informacionNegociacionDao) {
		this.informacionNegociacionDao = informacionNegociacionDao;
	}
	
	/**
     * La referencia al bean sicaServiceData
     */
    private InformacionNegociacionDao informacionNegociacionDao;

}
