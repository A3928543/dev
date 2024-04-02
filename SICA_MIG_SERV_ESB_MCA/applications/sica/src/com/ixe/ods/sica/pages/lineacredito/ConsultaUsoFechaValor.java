/*
 * $Id: ConsultaUsoFechaValor.java,v 1.6 2009/08/03 22:06:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.lineacredito;

import java.util.List;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * P&aacute;gina que muestra los deals que est&aacute;n haciendo uso de una l&iacute;nea de cambios.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.6 $ $Date: 2009/08/03 22:06:23 $
 */
public abstract class ConsultaUsoFechaValor extends SicaPage implements IExternalPage {

    /**
     * Llama a configurar().
     *
     * @param objects El arreglo de par&aacute;metros. El primero es el id del corporativo.
     * @param iRequestCycle El ciclo de la p&aacute;gina.
     */
    public void activateExternalPage(Object[] objects, IRequestCycle iRequestCycle) {
        Integer idCorporativo = (Integer) objects[0];
        configurar(idCorporativo);
    }

    /**
     * Inicializa la l&iacute;nea del corporativo especificado, y la lista de deals que la utilizan.
     *
     * @param idCorporativo El id del corporativo a consultar.
     * @throws SicaException Si no existe l&iacute;nea de cambios para el corporativo especificado.
     */
    public void configurar(Integer idCorporativo) throws SicaException {
        setLinea(getLineaCambioServiceData().findLineaCambioParaCliente(idCorporativo));
        if (getLinea() == null) {
            throw new SicaException("No se encontr\u00f3 la l\u00ednea de cambio.");
        }
        setDeals(getLineaCambioServiceData().findDealsLineaCambioCliente(getTicket(),
                idCorporativo));
    }

    /**
     * Establece el valor de deals.
     *
     * @param deals El valor a asignar.
     */
    public abstract void setDeals(List deals);

    /**
     * Regresa el valor de lineaCambio.
     *
     * @return LineaCambio.
     */
    public abstract LineaCambio getLinea();

    /**
     * Establece el valor de linea.
     *
     * @param linea El valor a asignar.
     */
    public abstract void setLinea(LineaCambio linea);
}
