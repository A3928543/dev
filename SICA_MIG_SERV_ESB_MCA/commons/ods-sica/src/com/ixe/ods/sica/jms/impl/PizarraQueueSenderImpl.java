/*
 * $Id: PizarraQueueSenderImpl.java,v 1.14 2008/12/26 23:17:35 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.jms.impl;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.NamingException;

import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.JmsTemplate102;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jndi.JndiLocatorSupport;

import com.ixe.ods.sica.jms.PizarraQueueSender;

/**
 * Clase para la notificaci&oacute;n de cambios en la pizarra v&iacute;a JMS.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.14 $ $Date: 2008/12/26 23:17:35 $
 */
public class PizarraQueueSenderImpl extends JndiLocatorSupport implements PizarraQueueSender {

    /**
     * Constructor por default.
     */
    public PizarraQueueSenderImpl() {
        super();
    }

    /**
	 * Envia el mensaje JMS con precio de referencia actual.
     *
     * @param idPrecioReferencia El &uacute;ltimo valor del precio de referencia con el cual se
     *          construir&aacute;n los pizarrones.
	 */
    public void send(final int idPrecioReferencia) {
        JmsTemplate jmsTemplate = new JmsTemplate102(_connectionFactory, false);
        try {
            jmsTemplate.setPubSubDomain(false);
            jmsTemplate.setTimeToLive(20000);
            jmsTemplate.send(getQueue(), new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    MapMessage msg = session.createMapMessage();
                    msg.setInt("idPrecioReferencia", idPrecioReferencia);
                    return msg;
                }
            });
        }
        catch (JmsException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e);
            }
        }
        catch (NamingException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e);
            }
        }
    }
    
    /**
     * Asigna el valor para connectionFactory.
     * 
     * @param connectionFactory El valor para connectionFactory.
     */
    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        _connectionFactory = connectionFactory;
    }
    
    /**
     * Asigna el valor para queueJndiName.
     * 
     * @param queueJndiName El valor para queueJndiName.
     */
    public void setQueueJndiName(String queueJndiName) {
        _queueJndiName = queueJndiName;
    }
    
    /**
     * Obtiene la cola de mensajes.
     * 
     * @return Queue
     * @throws NamingException Si no se puede hacer el lookup.
     */
    public Queue getQueue() throws NamingException {
        return (Queue) lookup(_queueJndiName);
    }
    
    /**
     * El nombre de la cola jndi.
     */
    private String _queueJndiName;
    
    /**
     * La conecxi&oacute;n jndi.
     */
    private ConnectionFactory _connectionFactory;
}