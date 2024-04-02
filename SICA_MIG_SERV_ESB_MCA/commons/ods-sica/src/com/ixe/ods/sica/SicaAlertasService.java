/*
 * $Id: SicaAlertasService.java,v 1.12 2008/02/22 18:25:02 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import java.util.HashMap;

/**
 * Business interface para el ejb de alarmas.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:02 $
 */
public interface SicaAlertasService {

    /**
     * Env&iacute;a la alerta del tipo y contexto proporcionados a la persona especificada.
     *
     * @param tipoAlerta El tipo de alerta.
     * @param idPersona La persona que recibir&aacute; la alerta.
     * @param contexto El contexto de valores para generar el mensaje.
     */
    void generaAlerta(String tipoAlerta, Integer idPersona, HashMap contexto);
}
