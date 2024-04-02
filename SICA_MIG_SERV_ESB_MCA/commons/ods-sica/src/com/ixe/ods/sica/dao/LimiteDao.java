/*
 * $Id: LimiteDao.java,v 1.4 2008/12/26 23:17:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

import java.util.List;

/**
 * Data Access Object que permite obtener los l&iacute;mites de riesgo para una mesa y divisa
 * determinadas.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.4 $ $Date: 2008/12/26 23:17:36 $
 */
public interface LimiteDao {

    /**
     * Encuentra el L&iacute;mite de Riesgo por Mesa de Cambio.
     *
     * @param idMesaCambio El Identificador de la Mesa de Cambio (puede ser null).
     * @param idDivisa El Identificador de la Divisa (puede ser null).
     * @return List El L&iacute;mite de Riesgo.
     */
    List findLimiteDeRiesgoByMesaDivisa(Integer idMesaCambio, String idDivisa);
    
    /**
     * Regresa una lista de objetos Limite con los niveles correspondientes.
     *
     * @param idMesaCambio La mesa de cambios o todas (null).
     * @param idDivisa La divisa o todas (null).
     * @param horizonteTiempo El horizonte de tiempo (opcional).
     * @return List de Limite.
     */    
    List getLimitesParaMesaDivisa(Integer idMesaCambio, String idDivisa, Integer horizonteTiempo);
}
