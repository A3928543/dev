/*
 * $Id: BroadcastPizarron.java,v 1.13.42.1.52.2 2020/12/03 21:59:07 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.mesa;

import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.seguridad.services.LoginService;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaProperties;
import com.ixe.ods.sica.Visit;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.ApplicationRuntimeException;

/**
 * Recibe un precio de referencia (proveniente de un stored procedure en oracle), y escribe un
 * mensaje en la cola del pizarr&oacute;n para ser distribu&iacute;da a las pizarras de los
 * usuarios.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.13.42.1.52.2 $ $Date: 2020/12/03 21:59:07 $
 */
public class BroadcastPizarron extends SicaPage implements IExternalPage {

    /**
     * Constructor vac&iacute;o.
     */
    public BroadcastPizarron() {
        super();
    }

    /**
     * Llamado por el trigger de actualizaci&oacute;n de precio reuters. Refresca el cach&eacute;
     * de pizarrones.
     *
     * @param parametros El arreglo de par&aacute;metros (ignorado).
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activateExternalPage(Object[] parametros, IRequestCycle cycle) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Recibiendo precio referencia " + parametros[0]);
        }
        // Iteramos hasta encontrar el precio de referencia que llego como parametro para garantizar
        // que es el ultimo:
        int idPrecioReferencia = ((Integer) parametros[0]).intValue();
        PrecioReferenciaActual precioReferencia;
        long tiempoInicial = new java.util.Date().getTime();
        try {
            do {
                precioReferencia = getSicaServiceData().findPrecioReferenciaActual();
            }
            while (idPrecioReferencia > precioReferencia.getIdPrecioReferencia()
                    && (tiempoInicial + 15000 > new java.util.Date().getTime()));
        }
        catch (Exception e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
        // Se reconstruyen los pizarrones:
        try {
            getPizarronServiceData().refrescarPizarrones(obtenerTicket(), 
            		getRequestCycle().getRequestContext().getRequest().getRemoteAddr(), ((Visit) getVisit()).getUser());
        }
        catch (SicaException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Regresa un ticket que se obtiene a partir del usuario que se encuentra configurado en
     * sica.properties.
     *
     * @return String.
     * @throws SicaException Si no se pueden obtener las propiedades de conexi&oacute;n.
     */
    private String obtenerTicket() throws SicaException {
        try {
            String pUsr = SicaProperties.getInstance().getSicaUsr();
            String pPwd = SicaProperties.getInstance().getSicaPwd();
            String pSys = SicaProperties.getInstance().getSicaSys();
            LoginService loginService = (LoginService) getApplicationContext().
                    getBean("loginService");
            return loginService.obtieneTicket(pUsr, pSys, pPwd, pUsr, "SICA", "0.0.0.0");
        }
        catch (SeguridadException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
            throw new SicaException(e.getMessage());
        }
    }
}
