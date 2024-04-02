/*
 * $Id: MailKondorService.java,v 1.2 2010/04/13 17:56:28 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

/**
 * Interfaz para el bean que env&iacute;a el correo a destinatarios de Folders de Kondor.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2010/04/13 17:56:28 $
 */
public interface MailKondorService {

    /**
     * Env&iacute;a un correo a las direcciones del par&aacute;metro 'EMAILS_FOLDERK_X' para
     * informar que se ha registrado o cancelado una operaci&oacute;n de derivados desde el SICA.
     * 
     * @param idConf El n&uacute;mero de confirmaci&oacute;n (de SC_BITACORA_ENVIADAS).
     * @param folio  El n&uacute;mero de operaci&oacute;n.
     * @param folderKondor El nombre del folder de Kondor.
     * @param tipoCorreo TIPO_ALTA_OPERACION | TIPO_CANCELA_OPERACION.
     */
    void enviarCorreo(String idConf, int folio, String folderKondor, int tipoCorreo);

    /**
     * Constante para el tipo de operaci&oacute;n de alta.
     */
    static final int TIPO_ALTA_OPERACION = 0;
    
    /**
     * Constante para el tipo de operaci&oacute;n de cancelaci&oacute;n.
     */
    static final int TIPO_CANCELA_OPERACION = 1;
}
