/*
 * $Id: EsbService.java,v 1.1.2.1 2010/10/08 01:15:27 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

import com.ixe.ods.sica.SicaException;

/**
 * Interfaz para la comunicaci&oacute;n con servicios de ESB.
 *
 * @author lvillegas
 * @version  $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:15:27 $
 */
public interface EsbService {

   /**
     * Realiza la validaci&oacute;n de las credenciales IFE.
     *
     * @param nombreAplicacion nombre de la aplicaci&oacute;n.
     * @param usuario clave usuario que est&aacute; realizando la validaci&oacute;n.
     * @param ticket contiene el ticket de la sesi&oacute;n del usuario.
     * @param folio de la credencial IFE.
     * @param anioRegistro a&ntilde;o de registro de la credencial IFE.
     * @param numeroEmision n&uacute;mero de emisi&oacute;n de la credencial IFE.
     * @param ocr OCR de la credencial IFE.
     * @throws Exception Si ocurre alg&uacute;n problema.
     */
    public void validaCredenciaIfe(String nombreAplicacion, String usuario, String ticket,
           String folio, String anioRegistro, String numeroEmision, String ocr)
           throws SicaException;
}
