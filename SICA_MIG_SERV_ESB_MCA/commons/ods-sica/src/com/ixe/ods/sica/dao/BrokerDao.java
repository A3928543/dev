/*
 * $Id: BrokerDao.java,v 1.1 2009/09/23 16:50:12 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

import java.util.List;

/**
 * Data Access Object para extraer informaci&oacute;n de la tabla SC_BROKER.
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1 $ $Date: 2009/09/23 16:50:12 $
 */
public interface BrokerDao {

    /**
     * Construye y regresa la lista de BrokersVO (Contrapartes que son Instituciones Financieras),
     * que pueden operar swaps.
     *
     * @return List.
     */    
    List getBrokersVOParaOperarSwaps();
}
