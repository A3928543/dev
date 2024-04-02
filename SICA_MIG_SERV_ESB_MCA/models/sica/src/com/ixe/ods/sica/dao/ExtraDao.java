/* 
 * $Id: ExtraDao.java,v 1.11.60.1.4.1 2014/03/27 15:51:26 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.dao;

import java.util.List;

/**
 * Interfaz para el dao de b&uacute;squeda de personas y facultades.
 * 
 * @author David Solis (dsolis)
 * @version $Revision: 1.11.60.1.4.1 $ $Date: 2014/03/27 15:51:26 $
 */
public interface ExtraDao {
	
	/**
	 * Encuentra la lista de personas pertenecientes a una facultad y a un sistema.
	 * 
	 * @param facultad La facultad de la persona.
	 * @param sistema El nombre del sistema
	 * @return List
	 */
    List findPersonasWithFacultyAndSystem(String facultad, String sistema);
    
    
    /**
     * 
     * @param b_ilytics Valor que debe tomar el proceso de estandarizacion de la BUP.
     * @param idPersona Perona a actualizar.
     * @return boolean true si fue ejecutada la actualizacion.
     */
    public boolean updateProcesoEstandarizacionBupPersona(Integer b_ilytics, Integer idPersona);
    
    /**
     * Metodo que valida si existe una entidad federativa de M\u00E9xico y el extranjero
     * @param entidad la entidad que se desea validar
     * @param pais el pais al que pertenece la entidad.
     * @return <code>true</code> en caso de que la entidad sea v\u00E1lida,
     * 		   <code>false</code> en cualquier otro caso.
     */
    public boolean isEntidadValida(String entidad, String pais);
}
