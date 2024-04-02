/*
 * $Id: ActividadDao.java,v 1.1 2009/09/23 16:50:00 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

import java.util.List;

/**
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1 $ $Date: 2009/09/23 16:50:00 $
 */
public interface ActividadDao {

    /**
     * Regresa la lista de objetos DealWorkitemVO que representan actividades pendientes de
     * completar del WorkFlow.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param nombreActividad El nombre de la actividad a buscar (puede ser null).
     * @return List de DealWorkitemVO y/o DealDetWorkitemVO.
     */
    List getWorkitems(String ticket, String nombreActividad);
}
