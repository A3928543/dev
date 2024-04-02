/*
 * $Id: AdminPizarronServiceData.java,v 1.12 2008/02/22 18:25:15 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo;

import java.util.List;

/**
 * Service Data Object para las operaciones a la base de datos que requiere el pizarr&oacute;n.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:15 $
 */
public interface AdminPizarronServiceData {
		
	/**
	 * Regresa una lista con todos los canales.
	 * 
	 * @return List.
	 */
    List findAllCanales();

    /**
     * Regresa una lista con todas las divisas.
     * 
     * @return List.
     */
    List findAllDivisas();

    /**
     * Regresa una lista con los valores actuales de todos los spreads
     * para un canal y una divisa.
     * 
     * @param idCanal El id del canal.
     * @param idDivisa El id de la divisa.
     * @return List.
     */
    List findSpreadsActuales(String idCanal, String idDivisa);
    
    /**
     * Regresa una lista con los factores de divisa actuales.
     * 
     * @return List.
     */
    List findFactoresDivisaActuales();
}
