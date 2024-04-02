/*
 * $Id: SecurityService.java,v 1.1 2008/09/05 21:03:07 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.ods.seguridad.model.IRespuesta;
import com.ixe.ods.seguridad.model.SeguridadException;

/**
 * Business Interface para el EJB SecuritySessionBean.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.1 $ $Date: 2008/09/05 21:03:07 $
 */
public interface SecurityService {

    /**
     * Autoriza y graba en el log de facultades.
     *
     * @param ticket El ticket de la sesi&oacute;n del Usuario.
     * @param ip La direcci&oacute;n IP de la petici&oacute;n.
     * @param facultad La facultad a autorizar.
     * @return IRespuesta.
     * @throws SeguridadException Si el usuario no tiene la facultad asociada.
     */
    IRespuesta autorizaFacultad(String ticket, String ip, String facultad)
            throws SeguridadException;
}
