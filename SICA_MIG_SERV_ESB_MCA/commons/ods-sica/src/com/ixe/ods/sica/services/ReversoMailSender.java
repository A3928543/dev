/*
 * $Id: ReversoMailSender.java,v 1.12.34.1 2010/09/14 23:57:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Reverso;

/**
 * Componente que env&iacute;a por correo electr&oacute;nico la informaci&oacute;n de un reverso las
 * personas configuradas en el bean reversoMailSender del Application Context.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.12.34.1 $ $Date: 2010/09/14 23:57:23 $
 */
public interface ReversoMailSender {

    /**
     * Env&iacute;a al promotor que captur&oacute; el reverso un correo electr&oacute;nico
     * informando que est&aacute; pendiente de autorizaci&oacute;n por parte de la mesa de cambios.
     *
     * @param reverso El reverso que captur&oacute; el promotor.
     * @param cc El arreglo de direcciones de correo al que ser&aacute; copiado el email (puede ser
     *      null).
     */
    void enviarMailReversoPendienteDeAutorizar(Reverso reverso, String[] cc);

    /**
     * Env&iacute; la informaci&oacute;n del reverso al promotor, operador de la mesa de cambios,
     * tesorer&iacute;a y riesgos.
     *
     * @param reverso El objeto reverso.
     * @param cc El arreglo de direcciones de correo al que ser&aacute; copiado el email (puede ser
     *      null).
     * @throws SicaException Si no se puede enviar el correo:
     */
    void enviarMailReverso(Reverso reverso, String[] cc) throws SicaException;
}
