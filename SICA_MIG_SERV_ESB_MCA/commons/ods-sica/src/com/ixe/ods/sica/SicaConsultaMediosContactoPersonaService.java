/*
 * $Id: SicaConsultaMediosContactoPersonaService.java,v 1.2 2009/08/03 21:54:09 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica;

import java.util.ArrayList;

import com.ixe.contratacion.ContratacionException;

/**
 * Business interface para el Session Bean del M&oacute;dulo de Contrataci&oacute;n
 * ConsultaMediosContactoPersonaSession que se encuentra en el JNDI:
 * ejb.ConsultaMediosContactoPersonaSessionRemoteHome.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2009/08/03 21:54:09 $
 */
public interface SicaConsultaMediosContactoPersonaService {

    /**
     * Regresa la lista de Medios de contacto del la persona con el n&uacute;mero especificado.
     *
     * @param idPersona El n&uacute;mero de persona, seg&uacute;n la BUP.
     * @return ArrayList de MedioContacto.
     * @throws ContratacionException Si ocurre alg&uacute;n problema.
     */
    ArrayList obtenMediosContacto(int idPersona) throws ContratacionException;
}
