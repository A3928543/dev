/*
 * $Id: JerarquiaDao.java,v 1.1 2009/04/27 15:59:34 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

import java.util.List;

/**
 * Data Access Object para las tablas SEGU_JERARQUIA y SEGU_NODO_JERARQUIA.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1 $ $Date: 2009/04/27 15:59:34 $
 */
public interface JerarquiaDao {

    /**
     * Regresa una lista de personas que corresponden a la jerarqu&iacute; de la persona
     * especificada, junto con la jerarqu&iacute;a de su promotor alterno, si es que est&aacute;
     * definido.
     *
     * @param idPersona El n&uacute;mero de persona del promotor a revisar.
     * @return List de EmpleadoIxe.
     */
    List findPromotoresJerarquia(Integer idPersona);
}
