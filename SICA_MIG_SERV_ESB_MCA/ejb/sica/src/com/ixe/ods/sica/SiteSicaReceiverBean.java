/*
 * $Id: SiteSicaReceiverBean.java,v 1.21.70.1 2014/08/20 18:01:27 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import java.io.Serializable;

import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import com.ixe.ods.sica.sdo.WorkFlowServiceData;
import com.ixe.treasury.dom.common.BloqueoLiquidacionMessage;
import com.ixe.treasury.dom.common.CambioFormaLiquidacionMessage;
import com.ixe.treasury.dom.common.CancelacionDetalleMessage;
import com.ixe.treasury.dom.common.CancelacionLiquidacionMessage;
import com.ixe.treasury.dom.common.DetalleLiquidadoMessage;
import com.ixe.treasury.dom.common.NotificacionCambioMessage;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.support.AbstractJmsMessageDrivenBean;

/**
 * MessageDrivenBean que se conecta al t&oacute;pico del SITE para reaccionar ante notificaciones
 * enviadas a este.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.21.70.1 $ $Date: 2014/08/20 18:01:27 $
 *
 * @ejbgen:message-driven destination-jndi-name = jms/SiteTopic
 * ejb-name = SiteSicaReceiverMDB
 * destination-type = javax.jms.Topic
 * durable = True
 * initial-beans-in-free-pool = 2
 * max-beans-in-free-pool = 10
 * default-transaction = Required
 * trans-timeout-seconds= 30
 * transaction-type = Container
 */
public class SiteSicaReceiverBean extends AbstractJmsMessageDrivenBean {

    /**
     * Constructor por default.
     */
    public SiteSicaReceiverBean() {
        super();
    }

    /**
     * Recibe el mensaje del t&oacute;pico del SITE y realiza una acci&oacute;n al respecto.
     *
     * @param msg El mensaje.
     */
    public void onMessage(Message msg) {
        warn("Recibiendo mensaje " + msg, null);
        try {
            if (msg instanceof ObjectMessage) {
                Serializable obj = ((ObjectMessage) msg).getObject();
                if (obj instanceof NotificacionCambioMessage && "SICA".
                        equals(((NotificacionCambioMessage) obj).getSistemaRegistra())) {
                    if (obj instanceof DetalleLiquidadoMessage) {
                        DetalleLiquidadoMessage m = (DetalleLiquidadoMessage) obj;
                        debug("Nueva liquidacion del detalle " +
                                    m.getIdDetalleLiquidacion() + "  " + m.getIdDetalleExt(), null);
                        getWorkFlowServiceData().recibirEntregarCompleto(m.
                                getIdDetalleLiquidacion(), m.getIdDetalleExt(), m.getFed(),
                                m.getMensajeMT());
                    }
                    else if (obj instanceof CancelacionLiquidacionMessage) {
                        CancelacionLiquidacionMessage m = (CancelacionLiquidacionMessage) obj;
                        debug("Cancelaci\u00f3n global del deal " + m.getNumeroOrden(), null);
                    }
                    else if (obj instanceof CancelacionDetalleMessage) {
                        CancelacionDetalleMessage m = (CancelacionDetalleMessage) obj;
                        debug("Cancelaci\u00f3n del detalle de liquidaci\u00f3n" +
                                    m.getIdDetalleLiquidacion() + " " + m.getIdDetalleExt(), null);
                        getWorkFlowServiceData().siteAutorizoONegoCancelacionDetalle(
                                Integer.valueOf(m.getNumeroOrden()).intValue(), m.isAutorizada(),
                                m.getIdDetalleLiquidacion());
                    }
                    else if (obj instanceof CambioFormaLiquidacionMessage) {
                        CambioFormaLiquidacionMessage m = (CambioFormaLiquidacionMessage) obj;
                        getWorkFlowServiceData().cambiarFormaLiquidacion(m.
                                getIdDetalleLiquidacion(), m.getIdDetalleExt(), m.getMnemonico());
                        debug("CambioFormaLiquidacionMessage", null);
                    }
                    else if (obj instanceof BloqueoLiquidacionMessage) {
                    	BloqueoLiquidacionMessage m = (BloqueoLiquidacionMessage) obj;
                    	getWorkFlowServiceData().asignarStatusReverso(m.isAutorizada(),
                    			new Integer(m.getNumeroOrden()));
                    	debug("BloqueoLiquidacionMessage", null);
                    }
                    else {
                        warn("Tipo de mensaje desconocido " + msg, null);
                    }
                }
            }
            debug("Mensaje procesado.", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            warn(e.getMessage(), e);
            getMessageDrivenContext().setRollbackOnly();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Escribe en el log un warning.
     *
     * @param object El objeto a escribir.
     * @param t La excepci&oacute;n.
     */
    private void warn(Object object, Throwable t) {
        if (logger.isWarnEnabled()) {
            logger.warn(object, t);
        }
    }

    /**
     * Escribe en el log un debug.
     *
     * @param object El objeto a escribir.
     * @param t La excepci&oacute;n.
     */
    private void debug(Object object, Throwable t) {
        if (logger.isDebugEnabled()) {
            logger.debug(object, t);
        }
    }

    /**
     * Regresa el valor de workFlowServiceData.
     *
     * @return WorkFlowServiceData.
     */
    private WorkFlowServiceData getWorkFlowServiceData() {
        return (WorkFlowServiceData) getBeanFactory().getBean("sicaWorkFlowServiceData");
    }

    /**
     * No hace nada.
     */
    protected void onEjbCreate() {
    }

    /**
     * Establece el beanFactoryLocatorKey como 'sicaApplicationContext' para poder cargar el
     * applicationContext adecuado.
     *
     * @param messageDrivenContext El MessageDrivenContext del ejb.
     */
    public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext) {
        super.setMessageDrivenContext(messageDrivenContext);
        setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
        setBeanFactoryLocatorKey("sicaApplicationContext");
    }

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -3011299482246075212L;
}
