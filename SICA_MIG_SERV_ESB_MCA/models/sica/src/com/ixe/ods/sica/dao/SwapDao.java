/*
 * $Id: SwapDao.java,v 1.11 2008/02/22 18:25:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

/**
 * Interface de la clase JdbcSwapDaoImpl para obtener los siguientes numeros 
 * consecutivos de ciertas secuencias utilizadas para el Reporte de Operaciones 
 * a Banxico.
 * 
 * @author Jean C. Favila, Javier Cordova
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:32 $
 */
public interface SwapDao {
	
	/**
	 * Crea y asigna un n&uacte;mero de folio para el Swap.
	 * 
	 * @return int
	 */
    int crearFolioSwap();    
    
    /**
     * Crea y asigna un n&uacte;mero de folio para Bitacoras Enviadas.
     * 
     * @return int
     */
    int crearFolioProgressRecid();
}
