/*
 * $Id: ContratoDireccionesService.java,v 1.11 2008/02/22 18:25:50 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;

/**
 * Define los metodos mediante los cuales, utilizando los servicios de contrataci&oacute;n se realiza la
 * asignaci&oacute;n de contratos sica a los deals y la obtenci&oacute;n de la direcci&oacute;n fiscal del contrato.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:50 $
 */
public interface ContratoDireccionesService {
	
	/**
	 * Asigna un contrato Sica al cliente. Valida que si el deal se encuentra en la &uacute;ltima fecha valor disponible
     * (72HR o VFUT), pertenezca a alguno de los sectores econm&oacute;micos permitidos.
	 *
	 * @param noCuenta El n&uacute;mero de cuenta del cliente
	 * @param deal El id del deal.
     * @param valorFuturo Si se puede o no operar a 96Hrs.
	 * @throws SicaException Si el deal no cumple con la restricci&oacute;n sobre fechas valor, u ocurre alg&ucute;n error.
	 */
    void asignarContrato(String noCuenta, Deal deal, boolean valorFuturo) throws SicaException;

    /**
     * Obtiene la direccion fiscal para una persona.
     * 
     * @param idPersona El id de la persona.
     * @return Direccion
     */
    com.ixe.ods.bup.model.Direccion getDireccionFiscalParaPersona(int idPersona);
}
