/*
 * $Id: ISicaKondorSessionBean.java,v 1.1 2010/04/13 21:57:00 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.kondor.bean;

import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.ixe.ods.sica.model.RespAltaKondor;

/**
 * The Interface ISicaKondorSessionBean.
 *
 * @author Israel Hernandez
 * @version $Revision: 1.1 $ $Date: 2010/04/13 21:57:00 $
 */
public interface ISicaKondorSessionBean {

	/**
	 * Crear swap kondor.
	 *
	 * @param ticket the ticket
	 * @param bes the bes
	 *
	 * @return the resp alta kondor[]
	 *
	 * @throws NamingException the naming exception
	 * @throws CreateException the create exception
	 */
	public RespAltaKondor[] crearSwapKondor(String ticket, ArrayList bes)
    throws NamingException, CreateException;
}
