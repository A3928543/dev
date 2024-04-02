/*
 * $Id: DatosPizarron.java,v 1.15 2008/11/12 05:53:13 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;

/**
 * P&aacute;gina que genera renglones de texto que utiliza PizPanel (El applet del pizarr&oacute;n)
 * para presentar la informaci&oacute;n de las divisas frecuentes, no frecuentes y metales
 * amonedados. El applet se comunica por HTTP con esta p&aacute;gina para obtener los resultados.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.15 $ $Date: 2008/11/12 05:53:13 $
 * @see com.ixe.ods.sica.pizarron.PizPanel
 * @see com.ixe.ods.sica.sdo.PizarronServiceData
 */
public abstract class DatosPizarron extends SicaPage implements IExternalPage {

    /**
     * Arma las cadenas que corresponden a los precios de divisas frecuentes, no frecuentes o
     * metales amonedados, de acuerdo al par&aacute;metro de opci&oacute;n que se reciba.
     *
     * @param parametros 0.- La opci&oacute;n, 1.- idTipoPizarron, 2.- timestamp, 3.- ticket.
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activateExternalPage(Object[] parametros, IRequestCycle cycle) {
        try {
        	int opcion = ((Integer) parametros[0]).intValue();
        	setOpcion(opcion);
        	if (opcion == PIZARRON) {
        		int idTipoPizarron = ((Integer) parametros[1]).intValue();
                boolean sucursales = "true".equals(parametros[IND_SUCURSALES]);
                String ticket = (String) (parametros.length > IND_TICKET &&
                        parametros[IND_TICKET] != null ? parametros[IND_TICKET] :
                        ((Visit) getVisit()).getTicket());
                setRenglones(getPizarronServiceData().getRenglonesPizarron(ticket,
            		  new Integer(idTipoPizarron), sucursales));
        	}
        	else {
        		setRenglones(getPizarronServiceData().
                        obtenerPizarronOtrasDivisas(opcion == METALES));
            }
        }
        catch (SicaException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
    }

    /**
     * Establece el valor de renglones.
     *
     * @param renglones El valor a asignar.
     */
    public abstract void setRenglones(java.util.List renglones);

    /**
     * Establece el valor de opcion.
     *
     * @param opcion El valor a asignar.
     */
    public abstract void setOpcion(int opcion);

    /**
     * La constante para la opci&oacute;n de generar los renglones del pizarr&oacute;n de divisas
     * frecuentes.
     */
    public static final int PIZARRON = 0;

    /**
     * La constante para la opci&oacute;n de generar los renglones de divisas no frecuentes.
     */
    public static final int NO_FRECUENTES = 1;

    /**
     * La constante para la opci&oacute;n de generar los renglones de metales amonedados.
     */
    public static final int METALES = 2;

    /**
     * La constante para el &iacute;ndice de par&aacute;metros que indica el ticket de la
     * sesi&oacute;n del usuario.
     */
    public static final int IND_TICKET = 3;

    /**
     * La constante para el &iacute;ndice de par&aacute;metros que indica que el pizarr&oacute;n es
     * para sucursales.
     */
    public static final int IND_SUCURSALES = 4;
}
