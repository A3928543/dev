/*
 * $Id: RegistroClienteContratoService.java,v 1.1.4.2 2010/10/08 01:15:27 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

import com.ixe.contratacion.ContratacionException;

/**
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1.4.2 $ $Date: 2010/10/08 01:15:27 $
 */
public interface RegistroClienteContratoService {

    int registrarPersona(String nombre, String paterno, String materno, String razonSocial,
                                 String rfc, int idPersonaUsuario, int idSectorBanxico)
            throws ContratacionException;

    String registrarContratoSica(int idPersonaCliente, int idPersonaUsuario)
            throws ContratacionException;
}
