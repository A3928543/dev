/* 
 * $Id: CierreDao.java,v 1.5 2010/02/26 00:48:56 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

import java.util.List;

/**
 * Interfaz para el dao para el cierre del dia del SICA.
 * 
 * @author Gustavo Gonzalez (ggonzalez)
 * @version $Revision: 1.5 $ $Date: 2010/02/26 00:48:56 $
 */
public interface CierreDao {
	
	/**
	 * Encuentra la lista de ids de ejecutivos para las lineas de credito
	 * de determinado grupo empresarial. 
	 * 
	 * @param idGrupoEmpresarial El id del grupo empresarial.
	 * @return List
	 */
	List findPromotoresPorLineaDeCredito(Integer idGrupoEmpresarial);
	
	/**
	 * Inactiva los contratos SICA que no hayan operado en un tiempo determinado.
     *
     * @param diasAtras D&iacute;as atr&aacute; a partir de los cuales se debe tener actividad.
     * @return int El n&uacute;mero de registros actualizados. 
	 */
	int inactivaContratosSica(int diasAtras);
}
