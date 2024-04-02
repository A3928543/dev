/*
 * $Id: CapturarFormaLiquidacion.java,v 1.13 2008/02/22 18:25:43 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * P&aacute;gina que permite al promotor capturar un detalle de deal sin producto y en moneda
 * nacional, que sirve para balancear una operaci&oacute;n de compra / venta.
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.13 $ $Date: 2008/02/22 18:25:43 $
 */
public abstract class CapturarFormaLiquidacion extends SicaPage {

	/**
     * Activa la p&aacute;gina <code>CapturaDeal</code> y llama a <code>insertar()</code> para
     * agregar el nuevo detalle de liquidaci&oacute;n.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see com.ixe.ods.sica.sdo.SicaServiceData#insertarMxn(String, com.ixe.ods.sica.model.Deal,
            boolean, String, double).
     */
    public void aceptar(IRequestCycle cycle) {
        IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
        if (getModoSubmit() == 1) {
            try {
                if (delegate.getHasErrors()) {
                    return;
                }
                if (StringUtils.isEmpty(getSelectedTipoLiquidacion())) {
                    throw new SicaException("Debe seleccionar un tipo de liquidaci\u00f3n y una forma de " +
                            "liquidaci\u00f3n.");
                }
                if (StringUtils.isEmpty(getMnemonico())) {
                    throw new SicaException("Debe seleccionar una forma de liquidaci\u00f3n.");
                }
                if (getCantidad() <= 0.0) {
                    throw new SicaException("La cantidad debe ser mayor que cero.");
                }
                if (getCantidad() > getMontoOriginal()) {
                    throw new SicaException("La cantidad debe ser menor o igual a " +
                            getMoneyFormat().format(getMontoOriginal()));
                }
                CapturaDeal nextPage = (CapturaDeal) cycle.getPage("CapturaDeal");
                getSicaServiceData().insertarMxn(getTicket(), nextPage.getDeal(), isRecibimos(),
                        getMnemonico(), getCantidad());
                cycle.activate(nextPage);
	        }
	        catch (SicaException e) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug(e);
                }
                delegate.record(e.getMessage(),null);
	        }
        }
    }

    /**
     * Regresa la lista de formas de liquidaci&oacute;n para recibimos o para entregamos, ordenadas por
     * mnem&oacute;nico. Filtra las formas que no sean desplegables en el sica y las de SPEI para Deals interbancarios.
     *
     * @return List.
     */
    private List obtenerFormasLiquidacion() {
        List formasLiquidacion = getFormasPagoLiqService().getProductosNoPizarron(getTicket(), isRecibimos(), null, null);
        for (Iterator it = formasLiquidacion.iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            if (!fpl.getDesplegableSica().booleanValue()
                    || !Constantes.MNEMONICO_ACTIVO.equals(fpl.getStatus())
                    || ("SPEI".equals(fpl.getClaveTipoLiquidacion().trim())
                        && (!(fpl.getMnemonico().indexOf("MXNSPEIIXEB") >= 0)
                        && !(fpl.getMnemonico().indexOf("BCTC") >= 0)))) {
                it.remove();
            }
        }
        Collections.sort(formasLiquidacion, new Comparator() {
            public int compare(Object fp1, Object fp2) {
                if (((FormaPagoLiq) fp1).getMnemonico().indexOf("BALNETEO") >= 0) {
                    return 1;
                }
                if (((FormaPagoLiq) fp2).getMnemonico().indexOf("BALNETEO") >= 0) {
                    return -1;
                }
                return ((FormaPagoLiq) fp1).getDescripcion().toLowerCase().
                        compareTo(((FormaPagoLiq) fp2).getDescripcion().toLowerCase());
            }
        });
        return formasLiquidacion;
    }
    
    /**
	 * Obtiene los Tipos de Liquidaci&oacute;n.
	 *
	 * @return String[].
	 */
	private String[] getTiposLiquidacion() {
		return getTiposLiquidacion(obtenerFormasLiquidacion());
	}
    
    /**
	 * Modelo del combo de Tipos de Liquidaci&oacute;n.
	 *
	 * @return RecordSelectionModel
	 */
	public IPropertySelectionModel getTiposLiquidacionModel() {
		return new StringPropertySelectionModel(getTiposLiquidacion());
	}
	
	/**
     * Regresa la lista de formas de liquidaci&oacute;n para recibimos o para entregamos pero filtradas de acuerdo a 
     * lo seleccionado en el combo de Tipos Liquidaci&oacute;n.
     *
     * @return List.
     */
	public List getFormasLiquidacionFiltradas() {
		List formasLiq = new ArrayList();
		List formasLiquidacionTmp = obtenerFormasLiquidacion();
		for (Iterator it = formasLiquidacionTmp.iterator(); it.hasNext();) {
			FormaPagoLiq fpl = (FormaPagoLiq) it.next();
			if (fpl.getNombreTipoLiquidacion().trim().equals(getSelectedTipoLiquidacion())) {
				formasLiq.add(fpl);
			}
		}
		return formasLiq;
	}

    /**
     * Regresa el valor de cantidad.
     *
     * @return double.
     */
    public abstract double getCantidad();

    /**
     * Fija el valor de cantidad.
     *
     * @param cantidad El valor a asignar.
     */

    public abstract void setCantidad(double cantidad);

    /**
     * Regresa el valor de mnemonico.
     *
     * @return String.
     */
    public abstract String getMnemonico();

    /**
     * Regresa el valor de montoOriginal.
     *
     * @return double.
     */
    public abstract double getMontoOriginal();

    /**
     * Establece el valor de montoOriginal.
     *
     * @param montoOriginal El valor a asignar.
     */
    public abstract void setMontoOriginal(double montoOriginal);

    /**
     * Regresa el valor de recibimos.
     *
     * @return boolean.
     */
    public abstract boolean isRecibimos();

    /**
     * Fija el valor de recibimos.
     *
     * @param recibimos El valor a asignar.
     */
    public abstract void setRecibimos(boolean recibimos);
    
    /**
     * Regresa el Tipo de Liquidaci&oacute;n seleccionado.
     *
     * @return String.
     */
    public abstract String getSelectedTipoLiquidacion();

    /**
     * Fija el valor del Tipo de Liquidaci&oacute;n seleccionado.
     *
     * @param selectedTipoLiquidacion El valor a asignar.
     */
    public abstract void setSelectedTipoLiquidacion(String selectedTipoLiquidacion);
    
    /**
	 * Regresa el modo de submit de la pagina. Si es por boton o por combo.
	 * @return int
	 */
	public abstract int getModoSubmit();    
}
