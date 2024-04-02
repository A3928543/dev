/*
 * $Id: CancelacionMailSender.java,v 1.1 2008/07/08 04:16:48 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;

/**
 * Interfaz que debe implementar la Clase que env&iacute;a notificaciones v&iacute;a correo
 * electr&oacute;nico sobre deals cancelados que no fueron procesados antes del cierre vespertino.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.1 $ $Date: 2008/07/08 04:16:48 $
 */
public interface CancelacionMailSender {

    /**
     * Env&iacute;a el correo al promotor que captur&oacute; el deal para informarle que la Mesa de
     * Cambios cancel&oacute; el deal por no estar procesado al momento del cierre vespertino.
     *
     * @param deal El deal que se cancel&oacute;.
     * @throws SicaException Si no se puede enviar el correo al promotor.
     */
    void enviarMailPromotor(Deal deal) throws SicaException;
}
