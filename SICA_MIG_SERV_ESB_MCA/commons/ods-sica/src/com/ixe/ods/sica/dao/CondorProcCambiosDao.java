/*
 * $Id: CondorProcCambiosDao.java,v 1.1 2009/01/07 17:26:09 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.dao;

import java.util.List;
import java.util.Date;

/**
 * DAO que ejecuta el stored procedure PS_CONDOR_CAMBIOS_TEST. Este DAO permite obtener el cuerpo
 * del correo que se env&iacute;a a la direcci&oacute;n siteSistemas@ixe.com.mx durante el cierre
 * vespertino del SICA.
 *  
 * @author Jean C. Favila
 * @version $Revision: 1.1 $ $Date: 2009/01/07 17:26:09 $
 */
public interface CondorProcCambiosDao {

    /**
     * Ejecuta el stored procedure y regresa un mapa con los resultados.
     *
     * @param fechaReporte La fecha que se quiere generar.
     * @return List.
     */    
    List ejecutar(Date fechaReporte);
}
