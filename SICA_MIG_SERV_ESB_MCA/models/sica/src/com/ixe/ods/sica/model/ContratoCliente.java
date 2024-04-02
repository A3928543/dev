/*
 * $Id: ContratoCliente.java,v 1.14 2009/02/28 00:38:09 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.bup.model.PersonaCuentaRol;
import java.util.Iterator;

/**
 * Componente para obtener la persona titular de un Contrato SICA.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.14 $ $Date: 2009/02/28 00:38:09 $
 */
public class ContratoCliente {

    /**
     * Regresa la persona ligada a los rol 'TIT' del contratoSica, que est&aacute; vigente.
     *
     * @param contratoSica El objeto del contratoSica.
     * @return Persona.
     */
    public static Persona clienteParaContratoSica(ContratoSica contratoSica) {
        if (contratoSica != null) {
            for (Iterator it = contratoSica.getRoles().iterator(); it.hasNext(); ) {
                PersonaCuentaRol pcr = (PersonaCuentaRol) it.next();
                if ("TIT".equals(pcr.getId().getRol().getIdRol())) {
                    return pcr.getId().getPersona();
                }
            }
        }
        return null;

    }
}
