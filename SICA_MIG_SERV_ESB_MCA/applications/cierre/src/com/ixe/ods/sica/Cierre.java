/*
 * $Id: Cierre.java,v 1.2 2008/12/26 23:17:30 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica;

import java.util.Date;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Category;

/**
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2008/12/26 23:17:30 $
 */
public class Cierre {

    private void hacerCierre(String args[]) {
        Context ctx = null;
        SicaSession ejb = null;
        if (args == null || args.length != 3) {
            logger.info("Uso: java -cp cierre-sica.jar com.ixe.ods.sica.Cierre " +
                    "t3://IP:PUERTO 0|1|2 emails \n\nDonde:\n0.- Cierre Contable y del d\u00eda\n1.-" +
                    " Cierre Contable.\n2.- Cierre del d\u00eda.\n\nEjemplo:\njava -cp " +
                    "cierre-sica.jar com.ixe.ods.sica.Cierre t3://200.23.32.12:7001 0 ccovian@ixe.com.mx,fibarra@ixe.com.mx");
            System.exit(-1);
        }
        try {
            Properties ps = new Properties();
            ps.put(InitialContext.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
            ps.put(InitialContext.PROVIDER_URL, args[0]);
            ctx = new InitialContext(ps);
            SicaSessionHome home = (SicaSessionHome) ctx.lookup("ejb/sica/SicaHome");
            ejb = home.create();
            String[] para = args[2].split("\\,");
            logger.info("Correos Electronicos: " + args[2]);
            logger.info("Inicia proceso " + args[1] + ": " + new Date());
            if ("0".equals(args[1])) {
                // Cierre completo:
                logger.info(ejb.iniciarCierreContableYCierreFinal(para));
            }
            else if ("1".equals(args[1])) {
                // Cierre contable:
                ejb.iniciarCierreContable(para);
            }
            else if("2".equals(args[1])) {
                // Cierre del dia:
                logger.info(ejb.iniciarCierreFinal(para));
            }
            logger.info("Fin del proceso: " + new Date());
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.getMessage(), e);
        }
        finally {
            if (ejb != null) {
                try {
                    ejb.remove();
                }
                catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                }
                catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
    }

    public static void main(String[] args) {
        new Cierre().hacerCierre(args);
        System.exit(0);
    }

    /**
     * El objeto para logging.
     */
    static Category logger = Category.getInstance(Cierre.class.getName());
}
