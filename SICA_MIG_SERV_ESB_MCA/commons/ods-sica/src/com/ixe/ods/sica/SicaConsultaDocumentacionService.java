package com.ixe.ods.sica;

import java.rmi.RemoteException;
import java.util.Map;

import com.ixe.contratacion.ContratacionException;

/**
 * Bussines Interfaces para el ejb 
 * de Contratacion  ConsultaDocumentacionPersonaOpeSession
 * @author user2
 *
 */
public interface SicaConsultaDocumentacionService {
	
	/**
	 * Devuelve la fecha Compromiso que tiene el cliente
	 * para entregar toda la documentacion requerida para
	 * operar
	 * @return Date fecha Compromiso 
	 * @param noCuenta Numero de cuenta del cliente
	 * @throws ContratacionException -
	 * @throws RemoteException -
	 */
	Map fechaEntregaDocumentacionCAM10(String noCuenta)
			throws ContratacionException, RemoteException;
	/**
	 * 
    /**
     * Verifica si se tiene prorroga 
     * para la entrega de documentacion
     * referente al expediente de cuenta
     * para el producto 'cambios' (cuentas CAM10)
	 * @param noCuenta - Numero de cuenta (CAM10)
	 * del cliengte
	 * @return true - Si ha vencido la fecha compromiso
	 * de entrega de documentacion
	 * false - Si aun es vigente la fecha compromiso
	 * de entrega de documentacion
	 * @throws ContratacionException -
	 * @throws RemoteException -
	 */
	boolean isVencidaFechaEntregaDocumentacionCAM10(String noCuenta)
	throws ContratacionException, RemoteException;

}
