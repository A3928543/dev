/*
 * $Id: SicaKondorSessionBean.java,v 1.3 2010/05/12 20:36:53 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.ods.sica.model.BitacoraEnviadas;
import com.ixe.ods.sica.model.RespAltaKondor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Stateless Session Bean para comunicar a Kondor con el SICA. Recibe los mensajes del proceso
 * externo SicaKondor para generar los swaps de derivados correspondientes.
 *
 * @author Jean C. Favila.
 * @version  $Revision: 1.3 $ $Date: 2010/05/12 20:36:53 $
 * @ejbgen:session ejb-name = SicaKondorSessionEJB
 * type = Stateless
 * initial-beans-in-free-pool = 5
 * max-beans-in-free-pool = 25
 * default-transaction = Required
 * trans-timeout-seconds = 60
 * enable-call-by-reference = true
 * @ejbgen:jndi-name remote = ejb/sica/SicaKondorHome
 * local = ejb/sica/SicaKondorLocalHome
 */
public class SicaKondorSessionBean implements SessionBean {

    /**
     * Constructor por default.
     */
    public SicaKondorSessionBean() {
        super();
    }

    public void ejbCreate() {
        
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
     * @return Arreglo de RespAltaKondor.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @throws javax.ejb.CreateException Si ocurre alg&uacute;n error.
     * @throws javax.naming.NamingException Si ocurre alg&uacute;n error.
     */
    public RespAltaKondor[] crearSwapKondor(String ticket, ArrayList bes)
            throws NamingException, CreateException {
        Context ctx;
        com.ixe.ods.sica.SicaInternalKondorSession ejb = null;
        try {
            ctx = new InitialContext();
            com.ixe.ods.sica.SicaInternalKondorSessionHome home =
                    (SicaInternalKondorSessionHome) ctx.lookup("ejb/sica/SicaInternalKondorHome");
            ejb = home.create();
            System.out.println("\n\nRecibiendo arreglo de BitacoraEnviadas:");
            int i = 0;
            for (Iterator it = bes.iterator(); it.hasNext(); i++) {
                BitacoraEnviadas be = (BitacoraEnviadas) it.next();

                System.out.println(i + " " + be.getId().getIdConf() + " " + be.getId().getDivisa());
            }
            return ejb.crearSwapKondor(ticket, bes);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e.getMessage());
        }
        finally {
            if (ejb != null) {
                try {
                    ejb.remove();
                }
                catch (Exception e) {
                    e.printStackTrace(); 
                }
            }
        }
    }

    /**
     * No hace nada.
     *
     * @param sessionContext El sessionContext para el EJB.
     * @throws EJBException Si ocurre alg&uacute;n error.
     * @throws RemoteException Si ocurre alg&uacute;n error.
     */
    public void setSessionContext(SessionContext sessionContext)
            throws EJBException, RemoteException {

    }

    /**
     * No hace nada.
     *
     * @throws EJBException    Si ocurre alg&uacute;n error.
     * @throws RemoteException Si ocurre alg&uacute;n error.
     */
    public void ejbRemove() throws EJBException, RemoteException {

    }

    /**
     * No hace nada.
     *
     * @throws EJBException    Si ocurre alg&uacute;n error.
     * @throws RemoteException Si ocurre alg&uacute;n error.
     */
    public void ejbActivate() throws EJBException, RemoteException {

    }

    /**
     * No hace nada.
     *
     * @throws EJBException    Si ocurre alg&uacute;n error.
     * @throws RemoteException Si ocurre alg&uacute;n error.
     */
    public void ejbPassivate() throws EJBException, RemoteException {
        
    }
}
