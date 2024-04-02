/*
 * $Id: CapturaBanco.java,v 1.10 2008/02/22 18:25:42 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.sica.model.IPlantillaIntl;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.treasury.dom.common.Banco;
import com.ixe.treasury.dom.common.ExternalSiteException;
import com.ixe.treasury.dom.common.Pais;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * Permite al usuario capturar un nuevo banco internacional.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:42 $
 */
public abstract class CapturaBanco extends SicaPage {

    /**
     * Inicializa banco como una nueva instancia y pais en null.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        Banco banco = new Banco();
        banco.setEsBancoPequeno(Boolean.TRUE);
        setBanco(banco);
        try {
            List paises = getSiteService().getPaises(getTicket());
            // Itera los paises para sacar a Mexico de la lista:
            for (Iterator it = paises.iterator(); it.hasNext();) {
                Pais p = (Pais) it.next();
                if ("MX".equals(p.getClaveISO())) {
                    it.remove();
                    break;
                }
            }
            setPaises(paises);
            setPais(getPaisXClaveISO(getTicket(), "US"));
        }
        catch (ExternalSiteException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
            ((IValidationDelegate) getBeans().getBean("delegate")).record(e.getMessage(), null);
        }
        catch (SeguridadException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
            ((IValidationDelegate) getBeans().getBean("delegate")).record(e.getMessage(), null);
        }
    }

    /**
     * Regresa el tipo de clave de ruteo para el pa&iacute;s seleccionado.
     *
     * @return String.
     */
    public String getTipoClaveRuteo() {
        if (getPais() != null) {
            try {
                List cvesRuteo = getSiteService().getClavesRuteoXPais(getTicket(),
                        getPais().getClaveISO());
                if (! cvesRuteo.isEmpty()) {
                    return cvesRuteo.get(0).toString();
                }
            }
            catch (Exception e) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug(e);
                }
                ((IValidationDelegate) getBeans().getBean("delegate")).record(e.getMessage(), null);
            }
        }
        return "";
    }

    /**
     * Asigna la clave de ruteo del pa&iacute;s seleccionado.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {
        if (!isGuardando()) {
            String claveRuteoPais = "";
            try {
                List cvesRuteo = getSiteService().getClavesRuteoXPais(getTicket(), getPais().
                        getClaveISO());
                if (! cvesRuteo.isEmpty()) {
                    claveRuteoPais = cvesRuteo.get(0).toString();
                }
            }
            catch (Exception e) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug(e);
                }
                ((IValidationDelegate) getBeans().getBean("delegate")).record(e.getMessage(), null);
            }
            getBanco().setClaveRuteoPais(claveRuteoPais);
        }
    }

    /**
     * Inserta el nuevo Banco Internacional utilizando el servicio del Site y regresa a la captura
     * de la plantilla.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void guardar(IRequestCycle cycle) {
        setGuardando(true);
        try {
            IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
            if (delegate.getHasErrors()) {
                return;
            }
            Banco banco = getBanco();
            banco.setNombreCorto(banco.getNombreCorto().toUpperCase());
            banco.setIdPais(getPais().getClaveISO());
            if (StringUtils.isEmpty(banco.getPlaza())) {
                delegate.record("Debe ingresar un valor para Plaza.", null);
                return;
            }
            //Forzando captura de Clave ABA, SWIFT o CHIPS
            if (StringUtils.isEmpty(banco.getNumeroRuteoPais())
                    && StringUtils.isEmpty(banco.getClaveSWIFT())
                    && StringUtils.isEmpty(banco.getClaveCHIPS())) {
                delegate.record("Debe ingresar un valor para Clave ABA, SWIFT o CHIPS.", null);
                return;
            }
            banco.setPlaza(banco.getPlaza().toUpperCase());
            if (! StringUtils.isEmpty(banco.getClaveRuteoPais())) {
                banco.setClaveRuteoPais("ABA");
            }
            else {
                banco.setClaveRuteoPais(null);
            }
            if (! StringUtils.isEmpty(banco.getNumeroRuteoPais())) {
                banco.setNumeroRuteoPais(banco.getNumeroRuteoPais().toUpperCase());
            }
            if (! StringUtils.isEmpty(banco.getClaveSWIFT())) {
                banco.setClaveSWIFT(banco.getClaveSWIFT().toUpperCase());
            }
            if (! StringUtils.isEmpty(banco.getClaveCHIPS())) {
                banco.setClaveCHIPS(banco.getClaveCHIPS().toUpperCase());
            }
            banco.setIdBanco(getSiteService().agregaBancoPequeno(getTicket(), getBanco()));
            SicaPage nextPage = (SicaPage) cycle.getPage(getPaginaRetorno());
            cycle.activate(nextPage);
        }
        catch (Exception e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
            throw new ApplicationRuntimeException("Error al guardar el banco.", e);
        }
    }

    /**
     * Regresa el valor de banco.
     *
     * @return Banco.
     */
    public abstract Banco getBanco();

    /**
     * Fija el valor de banco.
     *
     * @param banco El valor a asignar.
     */
    public abstract void setBanco(Banco banco);

    /**
     * Regresa el valor de guardando.
     *
     * @return boolean.
     */
    public abstract boolean isGuardando();

    /**
     * Fija el valor de guardando.
     *
     * @param guardando El valor a asignar.
     */
    public abstract void setGuardando(boolean guardando);

    /**
     * Regresa el valor de plantilla.
     *
     * @return IPlantillaIntl.
     */
    public abstract IPlantillaIntl getPlantilla();

    /**
     * Establece el valor de plantilla.
     *
     * @param plantilla El valor a asignar.
     */
    public abstract void setPlantilla(IPlantillaIntl plantilla);

    /**
     * Regresa el valor de pais.
     *
     * @return Pais.
     */
    public abstract Pais getPais();

    /**
     * Fija el valor de pais.
     *
     * @param pais El valor a asignar.
     */
    public abstract void setPais(Pais pais);

    /**
     * Fija el valor de paises.
     *
     * @param paises El valor a asignar.
     */
    public abstract void setPaises(List paises);

    /**
     * Regresa el valor de paginaRetorno.
     *
     * @return String.
     */
    public abstract String getPaginaRetorno();

    /**
     * Establece el valor de paginaRetorno.
     *
     * @param paginaRetorno El valor a asignar.
     */
    public abstract void setPaginaRetorno(String paginaRetorno);
}
