/*
 * $Id: ParametrosLineaCredito.java,v 1.13 2008/12/26 23:17:31 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2007 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.lineacredito;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.springframework.dao.DataAccessException;

/**
 * P&aacute;gina que permite modificar los Par&aacute;metros Generales del SICA.
 *
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.13 $ $Date: 2008/12/26 23:17:31 $
 */
public abstract class ParametrosLineaCredito extends SicaPage {

    /**
     * Se ejecuta cada que se activa la p&aacute;gina. Consulta los
     * Par&aacute;metros Generales del SICA y manda a llamar a
     * <code>asignacionParametros</code> para filtrar los respectivos de
     * L&iacute;neas de Cr&eacute;dito. El modo por default de la p&aacute;gina
     * es Update.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        limpiarTodo();
        List listParametros = getSicaServiceData().findAll(ParametroSica.class);
        asignacionParametros(listParametros);
        List listFormasCobro = findFormasPagoLiq();
        setFormasCobro(listFormasCobro);
    }

    /**
     * Sirve para colocar el focus de la p&aacute;gina cuando se carga
     * en el Campo de Texto Deseado.
     *
     * @return Map
     */
    public Map getFirstTextFieldMap() {
        Map map = new HashMap();
        map.put("textField", "document.Form0.lineaCreditoLlena");
        return map;
    }

    /**
     * Valida que no existan errores en los datos introducidos por el usuario y envia la lista de
     * Par&aacute;metros para que se actualicen.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void actualizaParametros(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        if (!delegate.getHasErrors()) {
            List parametros = new ArrayList();
            parametros.add(getLineaCreditoLlena());
            parametros.add(getDiasVencimientoLinea());
            parametros.add(getMaxExcedentes());
            getSicaServiceData().update(parametros);
        }
    }

    /**
     * Asigna los distintos Parametros a editar en sus correspondientes instancias.
     *
     * @param list La lista de Par&aacute;metros del SICA.
     */
    private void asignacionParametros(List list) {
        for (int c = 0; c < list.size(); c++) {
            ParametroSica parametro = (ParametroSica) list.get(c);
            String idParametro = parametro.getIdParametro();
            if (ParametroSica.LINEA_CREDITO_LLENA.equals(idParametro)) {
                setLineaCreditoLlena(parametro);
            }
            else if (ParametroSica.DIAS_VENCIMIENTO_LINEA.equals(idParametro)) {
                setDiasVencimientoLinea(parametro);
            }
            else if (ParametroSica.MAX_EXCEDENTES.equals(idParametro)) {
                setMaxExcedentes(parametro);
            }
        }
    }

    /**
     * Encuentra las Formas de Cobro que utilizan las L&iacute;neas de Cr&eacute;dito
     * y su Plazo de Liberaci&oacute;n.
     *
     * @return List Las Formas de Cobro.
     */
    public List findFormasPagoLiq() {
        List formasPagoLiqTmp = getFormasTiposLiq();
        List formasPagoLiq = new ArrayList();
        for (Iterator iterator = formasPagoLiqTmp.iterator(); iterator.hasNext();) {
            FormaPagoLiq tesFormaPagoLiq = (FormaPagoLiq) iterator.next();
            if (tesFormaPagoLiq.getUsaLineaCredito() != null
                    && tesFormaPagoLiq.getUsaLineaCredito().intValue() > 0) {
                formasPagoLiq.add(tesFormaPagoLiq);
            }
        }
        return formasPagoLiq;
    }

    /**
     * Regresa el Par&aacute;metro de la L&iacute;nea de Cr&eacute;dito: lineaCreditoLlena.
     *
     * @return ParametroSica lineaCreditoLlena
     */
    public abstract ParametroSica getLineaCreditoLlena();

    /**
     * Establece el Par&aacute;metro de la L&iacute;nea de Cr&eacute;dito: lineaCreditoLlena.
     *
     * @param parametro lineaCreditoLlena
     */
    public abstract void setLineaCreditoLlena(ParametroSica parametro);

    /**
     * Regresa el Par&aacute;metro de la L&iacute;nea de Cr&eacute;dito: diasVencimientoLinea.
     *
     * @return ParametroSica diasVencimientoLinea
     */
    public abstract ParametroSica getDiasVencimientoLinea();

    /**
     * Establece el Par&aacute;metro de la L&iacute;nea de Cr&eacute;dito: diasVencimientoLinea.
     *
     * @param parametro diasVencimientoLinea
     */
    public abstract void setDiasVencimientoLinea(ParametroSica parametro);

    /**
     * Regresa el Par&aacute;metro de la L&iacute;nea de Cr&eacute;dito: maxExcedentes.
     *
     * @return ParametroSica maxExcedentes
     */
    public abstract ParametroSica getMaxExcedentes();

    /**
     * Establece el Par&aacute;metro de la L&iacute;nea de Cr&eacute;dito: maxExcedentes.
     *
     * @param parametro maxExcedentes
     */
    public abstract void setMaxExcedentes(ParametroSica parametro);

    /**
     * Activa la Lista de Formas de Cobro que utilizan las L&iacute;neas de Cr&eacute;dito, cuyo
     * Plazo Liberaci&oacute;n es diferente de null.
     *
     * @param formasCobro La lista de Formas de Cobro a activar.
     */
    public abstract void setFormasCobro(List formasCobro);
}
