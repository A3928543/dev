/*
 * $Id: SicaRegistroDireccionPersonaService.java,v 1.13.14.1 2010/08/10 19:29:07 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.bean.Usuario;
import com.ixe.bean.bup.Direccion;
import com.ixe.bean.bup.Persona;
import com.ixe.contratacion.ContratacionException;


/**
 * Business interface para el ejb de Contratacion Servicio 
 * <code>SicaContratacionProductoBancoService</code>.
 * 
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.13.14.1 $ $Date: 2010/08/10 19:29:07 $
 */
public interface SicaRegistroDireccionPersonaService {
    
    /**
     * Servicio de Alta Rapida de Direcciones para Mensajeria.
     * 
     * @param personaFisicaOMoral	El Cliente.
     * @param direccion				La Direccion a dar de Alta.
     * @param usuario				El Usuario que da de Alta la Direccion.
     * @return Direccion			La nueva Direccion.
     * @throws ContratacionException Si ocurre alg&uacute;n error.
     */
    Direccion registraDireccionCambios(Persona personaFisicaOMoral, Direccion direccion,
                                       Usuario usuario) throws ContratacionException;    
    
    /**
     * Servidio de Alta de Direcciones de Clientes
     * 
     * @param personaActual             Cliente al que se le almacenará la dirección
     * @param direccion                 Dirección del Cliente
     * @param usuario                   Usuario que da de alta la dirección
     * @throws ContratacionException    Excepción lanzada por el EJB de contratación.
     */
    void registraDireccion(Persona personaActual, Direccion direccion, Usuario usuario)
    throws ContratacionException;
}