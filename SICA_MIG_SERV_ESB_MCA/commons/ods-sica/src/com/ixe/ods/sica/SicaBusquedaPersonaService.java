/*
 * $Id: SicaBusquedaPersonaService.java,v 1.12.88.1 2017/07/29 03:17:55 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import java.util.ArrayList;

import com.ixe.bean.Usuario;
import com.ixe.bean.bup.CriterioBusqueda;
import com.ixe.bean.bup.Persona;


/**
 * Business interface para el ejb de Contratacion Servicio 
 * <code>SicaBusquedaPersonaService</code>.
 * 
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.12.88.1 $ $Date: 2017/07/29 03:17:55 $
 */
public interface SicaBusquedaPersonaService {
    
    /**
     * Permite obtener el Objeto Usuario que realiza alguna operacion, por su ID.
     * 
     * @param idPersona El ID del Objeto Usuario a obtener.
     * @return Usuario.
     */
    Usuario obtenEmpleado(int idPersona);
    
    /**
     * Permite obtener el Objeto Persona por su ID.
     * 
     * @param idPersona El ID del Objeto Persona a obtener.
     * @return Persona.
     */
    Persona buscaPersonaPorId(int idPersona);
    
    /**
     * Busca persona.
     *
     * @param criterioBusqueda the criterio busqueda
     * @return the array list
     */
    ArrayList buscaPersona(CriterioBusqueda criterioBusqueda);
}