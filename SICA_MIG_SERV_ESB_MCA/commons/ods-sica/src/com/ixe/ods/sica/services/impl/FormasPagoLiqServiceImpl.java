/*
 * $Id: FormasPagoLiqServiceImpl.java,v 1.18 2010/04/30 17:20:06 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.services.FormasPagoLiqService;
import com.ixe.treasury.dom.common.FormaPagoLiq;

/**
 * Implementaci&oacute;n de la interfaz FormasPagoLiqService.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.18 $ $Date: 2010/04/30 17:20:06 $
 */
public class FormasPagoLiqServiceImpl implements FormasPagoLiqService {

    /**
     * Constructor vac&iacute;o.
     */
    public FormasPagoLiqServiceImpl() {
        super();
    }

    /**
     * @see com.ixe.ods.sica.services.FormasPagoLiqService#getFormasTiposLiq(String).
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @return List.
     */
    public List getFormasTiposLiq(String ticket) {
        return getSicaSiteCache().obtenerFormasPagoLiq(ticket);
    }

    /**
     * Regresa la formaPagoLiq que corresponde al idMatriz especificado.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idMatriz La clave de la forma de pago y liquidaci&oacute;n.
     * @return FormaPagoLiq.
     * @throws SicaException Si no existe la forma de pago y liquidaci&oacute;n.
     */
    public FormaPagoLiq getFormaPagoLiq(String ticket, Long idMatriz) throws SicaException {
        return getSicaSiteCache().getFormaPagoLiq(ticket, idMatriz);
    }

    /**
     * Regresa las formas de pago y liquidaci&oacute;n que corresponden o no al pizarr&oacute;n.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param pizarron true para los del pizarr&oacute;n, false para los que no son del
     *      pizarr&oacute;n (PESOS).
     * @return List.
     */
    private List getProductos(String ticket, boolean pizarron) {
        List resultados = new ArrayList();
        for (Iterator it = getFormasTiposLiq(ticket).iterator(); it.hasNext();) {
            FormaPagoLiq pfl = (FormaPagoLiq) it.next();
            if ((Divisa.PESO.equals(pfl.getIdDivisa()) && !pizarron) ||
                    (!Divisa.PESO.equals(pfl.getIdDivisa()) && pizarron)) {
                resultados.add(pfl);
            }
        }
        return resultados;
    }

    /**
     * @see com.ixe.ods.sica.services.FormasPagoLiqService#getProductosPizarron(String).
     * @see #getProductos(String, boolean).
     */
    public List getProductosPizarron(String ticket) {
        return getProductos(ticket, true);
    }

    /**
     * Filtra y regresa los productos por entregamos o recibimos, divisa y claveFormaLiquidacion
     * (producto).
     *
     * @param prods La lista de FormaPagoLiq.
     * @param recibimos true recibimos, false entregamos.
     * @param idDivisa El identificador de la divisa (opcional).
     * @param claveFormaLiquidacion El identificador del producto (opcional).
     * @return List.
     */
    private List getProductos(List prods, boolean recibimos, String idDivisa,
                              String claveFormaLiquidacion) {
        List productos = new ArrayList();
        for (Iterator it = prods.iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            if (recibimos == fpl.isRecibimos().booleanValue()) {
                boolean agregar = true;
                if (idDivisa != null && !fpl.getIdDivisa().equals(idDivisa)) {
                    agregar = false;
                }
                if (claveFormaLiquidacion != null && !fpl.getClaveFormaLiquidacion().
                        equals(claveFormaLiquidacion)) {
                    agregar = false;
                }
                if (agregar) {
                    productos.add(fpl);
                }
            }
        }
        if (productos.isEmpty()) {
            if (_logger.isDebugEnabled()) {
                StringBuffer sb = new StringBuffer("Se regresa una lista vac\u00eda de ").
                        append("FormaPagoLiq. La lista original").
                        append(" tiene ").append(prods.size()).append(" elems; recibimos = ").
                        append(recibimos).append("; divisa = ").append(idDivisa).
                        append(" claveFormaLiquidacion = ").append(claveFormaLiquidacion).
                        append(".\nLos mnemonicos originales son:\n");
                for (Iterator it = prods.iterator(); it.hasNext();) {
                    FormaPagoLiq fpl = (FormaPagoLiq) it.next();
                    sb.append(fpl.getMnemonico()).append(" ").append(fpl.getIdDivisa()).append(" ").
                            append(fpl.getRecibimos()).append(" ").
                            append(fpl.getClaveFormaLiquidacion()).append("\n");
                }
                _logger.debug(sb.toString());
            }
        }
        return productos;
    }

    /**
     * @see com.ixe.ods.sica.services.FormasPagoLiqService#getProductosPizarron(String, boolean,
     * String, String).
     */
    public List getProductosPizarron(String ticket, boolean recibimos, String idDivisa,
                                     String claveFormaLiquidacion) {
        return getProductos(getProductos(ticket, true), recibimos, idDivisa, claveFormaLiquidacion);
    }

    /**
     * @see com.ixe.ods.sica.services.FormasPagoLiqService#getProductosNoPizarron(String, boolean,
     * String, String).
     */
    public List getProductosNoPizarron(String ticket, boolean recibimos, String idDivisa,
                                       String claveFormaLiquidacion) {
        return getProductos(getProductos(ticket, false), recibimos, idDivisa,
                claveFormaLiquidacion);
    }

    /**
     * @see com.ixe.ods.sica.services.FormasPagoLiqService#getClavesFormasLiquidacion(String,
     * boolean).
     */
    public String[] getClavesFormasLiquidacion(String ticket, boolean pizarron) {
        List cfls = new ArrayList();
        for (Iterator it = getProductos(ticket, pizarron).iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            if (!cfls.contains(fpl.getClaveFormaLiquidacion())) {
                cfls.add(fpl.getClaveFormaLiquidacion());
            }
        }
        String[] resultados = new String[cfls.size()];
        int i = 0;
        for (Iterator it = cfls.iterator(); it.hasNext(); i++) {
            resultados[i] = (String) it.next();
        }
        return resultados;
    }

    /**
     * @see com.ixe.ods.sica.services.FormasPagoLiqService#getClavesFormasLiquidacionPorPizarron(
     *String, boolean, String).
     */
    public String[] getClavesFormasLiquidacionPorPizarron(String ticket, boolean recibimos,
                                                          String idDivisa) {
        List productos = getProductos(getProductos(ticket, true), recibimos, idDivisa, null);
        Map cflMap = new HashMap();
        for (Iterator iter = productos.iterator(); iter.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) iter.next();
            cflMap.put(fpl.getClaveFormaLiquidacion(), null);
        }
        Collection col = cflMap.keySet();
        String[] resultados = new String[col.size()];
        int i = 0;
        for (Iterator it = col.iterator(); it.hasNext(); i++) {
            resultados[i] = (String) it.next();
        }
        return resultados;
    }

    /**
     * @see com.ixe.ods.sica.services.FormasPagoLiqService#getNombreFormaLiquidacion(String,
     * String).
     */
    public String getNombreFormaLiquidacion(String ticket, String claveFormaLiquidacion) {
        for (Iterator it = getFormasTiposLiq(ticket).iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            if (claveFormaLiquidacion.equals(fpl.getClaveFormaLiquidacion())) {
                return fpl.getNombreFormaLiquidacion();
            }
        }
        return null;
    }

    /**
     * @see com.ixe.ods.sica.services.FormasPagoLiqService#getFormaPagoLiq(String, String).
     */
    public FormaPagoLiq getFormaPagoLiq(String ticket, String mnemonico) {
        for (Iterator it = getFormasTiposLiq(ticket).iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            if (fpl.getMnemonico().equals(mnemonico)) {
                return fpl;
            }
        }
        return null;
    }

    /**
     * @see com.ixe.ods.sica.services.FormasPagoLiqService#getMnemonicosPosiblesParaDealDetalle(
     * String, com.ixe.ods.sica.model.DealDetalle).
     * @see #getFormasPagoLiqPosibles(String, com.ixe.ods.sica.model.DealDetalle).
     */
    public List getMnemonicosPosiblesParaDealDetalle(String ticket, DealDetalle det) {
        List mnemonicos = new ArrayList();
        if (det.getMnemonico() != null) {
            mnemonicos.add(det.getMnemonico());
        }
        else {
            for (Iterator it = getFormasPagoLiqPosibles(ticket, det).iterator(); it.hasNext(); ) {
                FormaPagoLiq pfl = (FormaPagoLiq) it.next();
                mnemonicos.add(pfl.getMnemonico());
            }
        }
        return mnemonicos;
    }

    /**
     * Regresa la lista de formasPagoLiq aplicables, utilizando <code>aceptaFormaPagoLiq()</code>
     * del detalle del deal para filtrar los que no deben utilizarse.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param det El detalle del deal.
     * @see DealDetalle#aceptaFormaPagoLiq(FormaPagoLiq).
     * @return List.
     */
    private List getFormasPagoLiqPosibles(String ticket, DealDetalle det) {
        List fpls = Divisa.PESO.equals(det.getDivisa().getIdDivisa()) ?
                getProductosNoPizarron(ticket, det.isRecibimos(), det.getDivisa().getIdDivisa(),
                        det.getClaveFormaLiquidacion()) :
                getProductosPizarron(ticket, det.isRecibimos(), det.getDivisa().getIdDivisa(),
                        det.getClaveFormaLiquidacion());
        for (Iterator it = fpls.iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            if (!fpl.getDesplegableSica().booleanValue() || !det.aceptaFormaPagoLiq(fpl)
                    || !Constantes.MNEMONICO_ACTIVO.equals(fpl.getStatus())) {
                it.remove();
            }
        }
        return fpls;
    }

    /**
     * Regresa true si la clave de forma de liquidaci&oacute;n o el mnem&oacute;nico corresponden a
     * la linea de cambios 2.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param claveFormaLiquidacion La clave del producto.
     * @param mnemonico El mnem&oacute;nico (opcional).
     * @param recibimos True recibimos, False entregamos.
     * @return boolean.
     */
    public boolean isRemesa(String ticket, String claveFormaLiquidacion, String mnemonico,
                            boolean recibimos) {
        List fpls = getFormasTiposLiq(ticket);
        for (Iterator it = fpls.iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            if (mnemonico == null) {
                if (recibimos && fpl.getClaveFormaLiquidacion().equals(claveFormaLiquidacion) &&
                        fpl.getUsaLineaCredito() != null &&
                        fpl.getUsaLineaCredito().intValue() == 2) {
                    return true;
                }
            }
            else {
                if (fpl.getMnemonico().equals(mnemonico) && fpl.getUsaLineaCredito() != null &&
                        fpl.getUsaLineaCredito().intValue() == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Regresa el valor de sicaSiteCache.
     *
     * @return SicaSiteCache.
     */
    public SicaSiteCache getSicaSiteCache() {
        return _sicaSiteCache;
    }

    /**
     * Establece el valor de sicaSiteCache.
     *
     * @param sicaSiteCache El valor a asignar.
     */
    public void setSicaSiteCache(SicaSiteCache sicaSiteCache) {
        _sicaSiteCache = sicaSiteCache;
    }

    /**
     * La referencia al sicaSiteCache para obtener las formas de pago y liquidaci&oacute;n del
     * cach&eacute;.
     */
    private SicaSiteCache _sicaSiteCache;

    /**
     * El objeto para logging.
     */
    protected final transient Log _logger = LogFactory.getLog(getClass());
}