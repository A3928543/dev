/*
 * $Id: SicaValidaClienteMontoTresMilUSDService.java,v 1.11.30.1 2010/08/10 19:29:25 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import java.rmi.RemoteException;
import java.util.List;



import com.ixe.contratacion.ContratacionException;

/**
 * Business interface para el ejb de Contratacion del Servicio <code>SicaValidaClienteMontoTresMilUSDService</code>, para
 * la validaci&oacute;n de operaci&oacute;n de montos mayores a $3,000.00 USD.
 * 
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.11.30.1 $ $Date: 2010/08/10 19:29:25 $
 */
public interface SicaValidaClienteMontoTresMilUSDService {

	/**
     * Permite Validar si el Cliente tiene Completa su Informaci&oacute;n en el
     * M&oacute;dulo de Contrataci&oacute;n para Permitirle o No Operaciones con
     * Montos Mayores a $ 3,000.00
     * 
     * @param idPersona Integer - Identificador del cliente.
     * @param isPersonaFisica boolean - Si es true el cliente es una persona fisica, false indica persona moral.
     * @return String - La informacion erronea o faltante que tiene
     * el cliente en la BUP
     * @throws ContratacionException en caso de que al cliente le falte tener registrada alguna informacion.
     * @throws RemoteException -
     */
	String validaClienteSica(Integer IdPersona, boolean isPersonaFisica) 
	throws ContratacionException, RemoteException;
	
	/**
	 * 
	 * @param idPersona -
	 * @param noCuenta -
	 * @return -
	 * @throws ContratacionException - 
	 * @throws RemoteException -
	 */
	String obtenDocumentacionFaltanteCliente(int idPersona, String noCuenta)  
	throws ContratacionException, RemoteException;
    
    /**
       * Busca los clientes por los id que 
       *que vienen en la Lista ( Objetos Integer)
       * para validar si tienen los datos completos e
       * la BUP, ademas de tener la documentacion 
       * completa. Devuelve un Map donde la llave
       * es el idPersona (Integer) y el valor es
       * la causa por la que es invalido el cliente.
       * Solo se devuelven los clientes invalidos
     * 
     * @param mapaPersonasList List  de objetos 
     * Integer con los idPersona
     * @return Map donde la llave
      * es el idPersona (Integer) y el valor es
      * la causa por la que es invalido el cliente.
      * Solo se devuelven los clientes invalidos
     * @throws ContratacionException -
     * @throws RemoteException -
     */
    List validarDocumentacionClientes(List mapaPersonasList) throws ContratacionException,
    RemoteException;
    
    
     
}