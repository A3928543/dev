/*
 * $Id: InfoClaveReferencia.java,v 1.5 2008/02/22 18:25:10 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages;

import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;

/**
 * P&aacute;gina que despliega las claves de referencia posibles de un cliente en particular.
 * Dise&ntilde;ada para trabajar con el Componente <code>DatosClaveReferencia</code> mediante una
 * petici&oacute;n AJAX, utilizando el external service de esta p&aacute;gina.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.5 $ $Date: 2008/02/22 18:25:10 $
 */
public abstract class InfoClaveReferencia extends SicaPage implements IExternalPage {

    /**
     * Obtiene y asigna la lista de claves de referencia para el cliente especificado en el
     * par&aacute;metro cero; utiliza para esto el servicio findClavesReferenciaPersona del
     * bean BupServiceData.
     *
     * @param params El arreglo de par&aacute;metros el 0 debe ser un entero del id del cliente.
     * @param cycle El ciclo de la p&aacute;gina.
     * @see com.ixe.ods.bup.sdo.BupServiceData#findClavesReferenciaPersona(int).
     */
    public void activateExternalPage(Object[] params, IRequestCycle cycle) {
        Integer idPersona = (Integer) params[0];
        // S&oacute;lo interesa la primera:
        List clavesReferencia = getBupServiceData().
                findClavesReferenciaPersona(idPersona.intValue());
        List clavesMostradas = new ArrayList();
        if (clavesReferencia.isEmpty()) {
            clavesMostradas = new ArrayList();
        }
        else {
            clavesMostradas.add(clavesReferencia.get(0));
        }

        setClavesReferencia(clavesMostradas);
    }

    /**
     * Establece el valor de clavesReferencia, la lista de claves de referencia posibles para un
     * cliente en particular.
     *
     * @param clavesReferencia El valor a asignar.
     */
    public abstract void setClavesReferencia(List clavesReferencia);
}
