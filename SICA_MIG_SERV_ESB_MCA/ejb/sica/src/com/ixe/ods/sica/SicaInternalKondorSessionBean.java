/*
 * $Id: SicaInternalKondorSessionBean.java,v 1.3 2010/06/01 16:21:27 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.ods.sica.model.BitacoraEnviadas;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.RespAltaKondor;
import com.ixe.ods.sica.sdo.SwapsServiceData;
import com.ixe.ods.sica.sdo.WorkFlowServiceData;
import com.ixe.ods.sica.services.GeneralMailSender;

import java.util.ArrayList;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;

import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ejb.support.AbstractStatelessSessionBean;

/**
 * Stateless Session Bean para comunicar a Kondor con el SICA. Recibe los mensajes del proceso
 * externo SicaKondor para generar los swaps de derivados correspondientes.
 *
 * @author Jean C. Favila.
 * @version  $Revision: 1.3 $ $Date: 2010/06/01 16:21:27 $
 * @ejbgen:session ejb-name = SicaInternalKondorSessionEJB
 * type = Stateless
 * initial-beans-in-free-pool = 5
 * max-beans-in-free-pool = 25
 * default-transaction = Required
 * trans-timeout-seconds = 60
 * enable-call-by-reference = true
 * @ejbgen:jndi-name remote = ejb/sica/SicaInternalKondorHome
 * local = ejb/sica/SicaInternalKondorLocalHome
 */
public class SicaInternalKondorSessionBean extends AbstractStatelessSessionBean {

    /**
     * Constructor por default.
     */
    public SicaInternalKondorSessionBean() {
        super();
    }

    /**
     * Cambia los valores por omisi&oacute;n de que archivo tomar como configuraci&oacute;n para
     * Spring. Tambien indica que el BeanFactory ser&aacute; compartido (singleton).
     *
     * @param sessionContext El SessionContext.
     */
    public void setSessionContext(SessionContext sessionContext) {
        super.setSessionContext(sessionContext);
        setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
        setBeanFactoryLocatorKey("sicaApplicationContext");
    }

    /**
     * Realiza el arranque necesario para proporcionar los servicios.
     *
     * @throws javax.ejb.CreateException Si no se puede crear el ejb.
     */
    protected void onEjbCreate() throws CreateException {
    }

    /**
     * Obtiene la lista de registros de Bit&aacute;cora enviada dado el folio en el TAS.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param bes El arreglo de objetos BitacoraEnviadas que componen el Swap.
     * @return List de HashMap.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     */
    public RespAltaKondor[] crearSwapKondor(String ticket, ArrayList bes) {
        String[] emails = new String[]{"ccovian@ixe.com.mx"};
        try {
            WorkFlowServiceData wfsd = (WorkFlowServiceData) getBeanFactory().
                    getBean("sicaWorkFlowServiceData");
            emails = wfsd.findParametro(ParametroSica.EMAIL_SWAPS_KONDOR).getValor().split("\\,");
            return getSwapsServiceData().crearSwapKondor(ticket, bes);
        }
        catch (SicaCodedException e) {
            enviarCorreoError(bes, e, emails);
            throw e;
        }
        catch (SicaException e) {
            SicaCodedException sce = new SicaCodedException(SicaCodedException.NO_CLASIFICADO_IA1, e);
            enviarCorreoError(bes, sce, emails);
            throw sce;
        }
        catch (DataIntegrityViolationException e) {
            SicaCodedException sce = new SicaCodedException(SicaCodedException.DATA_INTEGRITY_VIOLATION, e);
            enviarCorreoError(bes, sce, emails);
            throw sce; 
        }
        catch (DataAccessException e) {
            SicaCodedException sce = new SicaCodedException(SicaCodedException.DATA_INTEGRITY_VIOLATION, e);
            enviarCorreoError(bes, sce, emails);
            throw sce; 
        }
        catch (Exception e) {
            SicaCodedException sce = new SicaCodedException(SicaCodedException.KONDOR_ERROR_GENERAL, e);
            enviarCorreoError(bes, sce, emails);
            throw sce;
        }
    }

    /**
     * Pinta en el log de warnings la excepci&oacute;n y trata de enviar un correo con el mensaje de
     * error.
     *
     * @param bes La lista de objetos de BitacoraEnviadas.
     * @param e La excepci&oacute;n a pintar en el log.
     */
    private void enviarCorreoError(ArrayList bes, Throwable e, String[] emails) {
        if (logger.isWarnEnabled()) {
            logger.warn(e.getMessage(), e);
        }
        try {            
            GeneralMailSender gms = (GeneralMailSender) getBeanFactory().
                    getBean("generalMailSender");
            StringBuffer sb = new StringBuffer("Folios KONDOR:\n   ");

            for (Iterator it = bes.iterator(); it.hasNext();) {
                BitacoraEnviadas be = (BitacoraEnviadas) it.next();

                sb.append(be.getId().getIdConf()).append("\n   ");
            }
            sb.append("\n\nCAUSA: ").append(e.getMessage());
            gms.enviarMail("ixecambios@ixe.com.mx", emails, null,
                    "Error en operaci\u00f3n proveniente de KONDOR", sb.toString());
        }
        catch (Exception e1) {
            if (logger.isWarnEnabled()) {
                logger.warn(e1.getMessage(), e1);
            }
        }
    }

    /**
     * Regresa el valor del bean swapsServiceData.
     *
     * @return SwapsServiceData.
     */
    private SwapsServiceData getSwapsServiceData() {
        return (SwapsServiceData) getBeanFactory().getBean("jtaSwapsServiceData");
    }
}
