/*
 * $Id: SicaConsultaCatalogosService.java,v 1.12.30.2.54.1.2.1 2017/07/29 03:17:56 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica;

import java.util.LinkedHashMap;
import java.util.Vector;



/**
 * Business interface para el ejb de Contratacion Servicio 
 * <code>SicaConsultaCatalogosService</code>.
 * 
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.12.30.2.54.1.2.1 $ $Date: 2017/07/29 03:17:56 $
 */
public interface SicaConsultaCatalogosService {
    
    /**
     * Obtiene los Sectores Economicos de una Persona Moral y Fisica
     * que tienen Sector Banxico.
     * 
     * @param tipoPersona Persona Fisica o Moral.
     * @return Vector 	  Los Sectores Economicos.
     */
    Vector obtenCatSectorEconomicoBanxico(String tipoPersona);
    
    /**
     * Obtiene las nacionalidades existentes.
     * 
     * @return Vector 	  Nacionalidades
     */
    Vector obtenCatNacionalidad();
    
    /**
     * Obtiene el catálogo de paises existentes en BUP
     * 
     * @return Vector 	  Paises disponibles
     */
    Vector obtenCatpais();
    
    /**
     * Obtiene el catálogo de actividades Económicas
     * 
     * @return Vector 	  Actividades Económicas
     */
    Vector obtenCatActividadEconomica();
    
    /**
     * Obtiene el cat&aacute;logo de giros econ&oacute;micos.
     * @return
     */
    LinkedHashMap obtenCatGiroEconomico();
    
    /**
     * Obtiene el cat&aacute;logo de actividades econ&oacute;micas
     * para el giro proporcionado.
     * @param idGiro
     * @return
     */
    LinkedHashMap obtenCatActividadGiro(String idGiro);
    
    /**
	* Este método obtendrá el catálogo de estado civil (bup_estado_civil).
	*/
    public Vector obtenCatEstadoCivil();
    
    /**
     * Servicio que obtiene los tipos de comprobante para las direcciones del cliente
     * @param tipo_persona
     * @return LinkedHashMap de objetos TipoComprobante
     */
    public LinkedHashMap obtenCatTipoComprobante(String tipo_persona);
    
    public LinkedHashMap obtenCatTipoIdentificacion(String tipo);
}