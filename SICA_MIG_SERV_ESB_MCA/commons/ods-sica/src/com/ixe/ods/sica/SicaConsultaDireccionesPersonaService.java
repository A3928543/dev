/*
 * $Id: SicaConsultaDireccionesPersonaService.java,v 1.12.30.1.56.1 2015/10/16 14:19:10 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.bean.Usuario;
import com.ixe.bean.bup.Direccion;
import com.ixe.bean.bup.Persona;
import com.ixe.contratacion.ContratacionException;


/**
 * Business interface para el ejb de Contratacion Servicio 
 * <code>SicaConsultaDireccionesPersonaService</code>.
 * 
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.12.30.1.56.1 $ $Date: 2015/10/16 14:19:10 $
 */
public interface SicaConsultaDireccionesPersonaService {
    
    /**
     * Obtiene las Direcciones de un Cliente.
     * 
     * @param persona	El cliente.
     * @param tipoRes	Si se regresan las Direcciones como HashMap o ArrayList.
     * @return Object	Las Direcciones.
     */
    Object obtenDirecciones(Persona persona, int tipoRes);
    
    
    /**
     * Obtiene las posibles colonias, municipios y estados a partir del Codigo Postal
     * 
     * @param usuario                 Usuario que realiza la consulta
     * @param direccion               Objeto de tipo Direccion
     * @return Direccion              Direccion con la información obtenida de la búsqueda                 
     * @throws ContratacionException  Excepción arrojada por el Sistema de Contratacion
     */
    Direccion obtenDireccion(Usuario usuario, Direccion direccion)  throws ContratacionException;
    
    /**
	 * Obtiene la información completa de una dirección, en base al identificador
	 * @ejbgen:remote-method
	 */
	public Direccion obtenDireccion(Usuario usuario, int idDir) throws ContratacionException;;
}