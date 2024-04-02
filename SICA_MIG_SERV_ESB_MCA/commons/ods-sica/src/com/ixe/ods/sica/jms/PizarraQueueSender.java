/*
 * $Id: PizarraQueueSender.java,v 1.14 2008/12/26 23:17:34 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 LegoSoft S.C.
 */
package com.ixe.ods.sica.jms;

/**
 * Interfaz para la notificaci&oacute;n de cambios en la pizarra v&iacute;a JMS.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.14 $ $Date: 2008/12/26 23:17:34 $
 */
public interface PizarraQueueSender {
	
	/**
	 * @see com.ixe.ods.sica.jms.impl.PizarraQueueSenderImpl#send(int)
	 */
    public void send(int idPrecioReferencia);
}
