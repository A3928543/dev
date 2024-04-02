/*
 * $Id: DescuadreDao.java,v 1.2 2008/02/22 18:25:31 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2007 LegoSoft S.C.
 */
package com.ixe.ods.sica.dao;

import java.util.List;

/**
 * Interfaz para el dao para el cierre del dia del SICA.
 *
 * @author Gustavo Gonzalez (ggonzalez)
 * @version $Revision: 1.2 $ $Date: 2008/02/22 18:25:31 $
 */
public interface DescuadreDao {

	/**
	 * Obtiene la lista de detalles de Deal cancelados en SC_POSICION_LOG
	 * que descuadren la posicion.
	 *
	 * @return boolean
	 */
	List findDescuadreDetallesDealsCancelados();

	/**
	 * Obtiene la lista de detalles de deal no cancelados en SC_POSICION_LOG tales
	 * que descuadren la posicion.
	 *
	 * @return boolean.
	 */
	 List findDescuadreDetallesDealsNoCancelados();
}
