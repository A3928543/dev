/*
 * $Id: SicaRegistroMedioContactoPersonaService.java,v 1.2 2009/08/03 21:54:09 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.bean.Usuario;
import com.ixe.bean.bup.MedioContacto;
import com.ixe.bean.bup.Persona;

/**
 * Business interface para el ejb de Contratacion Servicio
 * <code>SicaRegistroMedioContactoPersonaService</code>.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2009/08/03 21:54:09 $
 */
public interface SicaRegistroMedioContactoPersonaService {

    /**
     * Registra un medio de contacto para la persona especificada, a nombre del usuario
     * proporcionado.
     *
     * @param persona La persona a la que se le agrega el medio de contacto.
     * @param medioContacto El medio de contacto a agregar.
     * @param usuario El usuario que realiza la operaci&oacute;n.
     * @throws com.ixe.contratacion.ContratacionException Si no se puede registrar.
     */
    void registraMedioContacto(Persona persona, MedioContacto medioContacto, Usuario usuario)
            throws com.ixe.contratacion.ContratacionException;
}
