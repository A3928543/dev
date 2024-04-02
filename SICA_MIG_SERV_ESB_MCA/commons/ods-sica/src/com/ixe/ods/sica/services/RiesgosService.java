/*
 * $Id: RiesgosService.java,v 1.4 2008/12/26 23:17:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

import java.util.Map;

/**
 * Implementaci&oacute;n de la interfaz RiesgosService.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.4 $ $Date: 2008/12/26 23:17:33 $
 */
public interface RiesgosService {

    /**
	 * M&eacut;etodo que regresa la informaci&oacute;n que se despliega en el monitor de riesgos.
     *
     * @return Map.
	 */
    Map obtenerMonitorRiesgos();

    /**
	 * M&eacut;etodo que regresa la informaci&oacute;n que se despliega en el monitor de riesgos.
     *
     * @return Map con las siguientes llaves: midSpot, porcentajeAvisoGlobal,
     *      porcentajeAlarmaGlobal, capitalDelGrupo, porcentajeCapital, limiteRegulatorio,
     *      posicionesMesas, nivel.
	 */
	Map obtenerMonitorRiesgos(Integer idMesaCambio, String idDivisa, Integer horizonTiempo);
}
