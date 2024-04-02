/*
 * $Id: MailVespertinoService.java,v 1.3.32.1.2.1 2010/12/20 21:13:10 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

import java.net.URL;

/**
 * Interfaz para el bean que env&iacute;a el correo del Cierre Vespertino.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.3.32.1.2.1 $ $Date: 2010/12/20 21:13:10 $
 */
public interface MailVespertinoService {

    /**
     * Env&iacute; un correo a las direcciones del Par&aacute;metro del sistema
     * 'EMAIL_CIERRE_VESPERTINO' para notificar que el SICA ha entrado en horario vespertino.
     *
     * @param logo El URL para el logo en el reporte (opcional).
     */
    void enviarCorreoCierreVespertino(URL logo);
    
    /**
     * Env&iacute; un correo a las direcciones del Par&aacute;metro del sistema
     * 'EMAIL_CIERRE_VESPERTINO' para notificar que el SICA ha entrado en horario de fin de 
     * liquidaci\u00f3n.
     */
    void enviarCorreoFinLiquidacion();
}
