/*
 * $Id: CapturaDatosComprobante.java,v 1.1.2.2.30.1 2020/12/01 04:53:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2013 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Actividad;
import com.ixe.ods.sica.model.Contrato;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.pages.SicaPage;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;

/**
 * <p>P&aacute;gina que permite al promotor capturar o modificar los correos para el env&iacute;o de
 * comprobantes de operaci&oacute;n a los clientes.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1.2.2.30.1 $ $Date: 2020/12/01 04:53:01 $
 */
public abstract class CapturaDatosComprobante extends SicaPage {

    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        if (getDeal().getEmailsComprobantes() == null && getDeal().getContratoSica() != null) {
            Contrato contrato = getSicaServiceData().findContratoByNoCuenta(getDeal().
                    getContratoSica().getNoCuenta());

            if (contrato != null) {
                getDeal().setEmailsComprobantes(contrato.getEmailsComprobantes());
            }
        }
    }

    public void submit(IRequestCycle cycle) {
    	auditar(new Integer(getDeal().getIdDeal()), LogAuditoria.CAMBIO_DATOS_COMP,
    			"em: " + getDeal().getEmailsComprobantes(), "INFO", "E");
    }

    /**
     * Si no hay errores de validaci&oacute;n, asigna el nombre del cliente al deal, de acuerdo con
     * la selecci&oacute;n del cliente, y activa la p&aacute;gina de CapturaDeal.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void aceptar(IRequestCycle cycle) {
        try {
            if (getDelegate().getHasErrors()) {
                return;
            }
            if (getDeal().getEmailsComprobantes() == null || StringUtils.isEmpty(getDeal().getEmailsComprobantes())) {
                throw new SicaException("Por favor proporcione al menos una direcci\u00f3n de " +
                        "correo electr\u00f3nico.");
            }
            if (!getDeal().getEmailsComprobantes().matches(getPatronEmails())) {
                throw new SicaException("Una o m\u00e1s direcciones de correo electr\u00f3nico " +
                        "no son v\u00e1lidos");
            }
            if (getDeal().getContratoSica() != null) {
                Contrato contrato = getSicaServiceData().findContratoByNoCuenta(getDeal().
                        getContratoSica().getNoCuenta());

                if (contrato != null) {
                    if (contrato.getEmailsComprobantes() != null &&
                            !contrato.getEmailsComprobantes().equals(getDeal().getEmailsComprobantes())) {
                        auditar(new Integer(getDeal().getIdDeal()), LogAuditoria.CAMBIO_DATOS_COMP,
                        		"em: " + getDeal().getEmailsComprobantes(), "INFO", "E");
                    }
                    contrato.setEmailsComprobantes(getDeal().getEmailsComprobantes());
                    getSicaServiceData().update(contrato);
                }
            }
            if (getDeal().getIdDeal() > 0) {
                getWorkFlowServiceData().actualizarDatosDeal(getTicket(), getDeal());
                getWorkFlowServiceData().terminarActividadesParaDeal(getDeal(),
                        Actividad.RES_SOL_MODIF, ((Visit) getVisit()).getUser());
            }
            cycle.activate("CapturaDeal");
        }
        catch (SicaException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     * Regresa a la p&aacute;gina anterior.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void regresar(IRequestCycle cycle) {
        if (isModoPrimeraVez()) {
            getDeal().setComprobante(Deal.SIN_COMPROBANTE);
            getDeal().setEmailsComprobantes(null);
            if (getDeal().getIdDeal() > 0) {
                getSicaServiceData().update(getDeal());
            }
        }
        cycle.activate("CapturaDeal");
    }

    /**
     * Regresa el patr&oacute;n de la validaci&oacute;n para los correos adicionales.
     *
     * @return String.
     */
    public String getPatronEmails() {
        return "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*([;]\\s*\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)*";
    }

    /**
     * Regresa el valor de deal.
     *
     * @return Deal.
     */
    public abstract Deal getDeal();

    /**
     * Fija el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public abstract void setDeal(Deal deal);

    /**
     * Regresa el valor de modoPrimeraVez.
     *
     * @return boolean.
     */
    public abstract boolean isModoPrimeraVez();

    /**
     * Establece el valor de modoPrimeraVez.
     *
     * @param modoPrimeraVez El valor a asignar.
     */
    public abstract void setModoPrimeraVez(boolean modoPrimeraVez);
}
