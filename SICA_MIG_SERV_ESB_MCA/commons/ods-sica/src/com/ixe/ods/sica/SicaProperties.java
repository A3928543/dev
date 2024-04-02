/*
 * $Id: SicaProperties.java,v 1.2.60.1 2013/07/10 21:15:08 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2.60.1 $ $Date: 2013/07/10 21:15:08 $
 */
public class SicaProperties extends Properties {

    /**
     * Constructor singleton
     */
    protected SicaProperties() throws SicaException {
        if (propertiesPath == null) {
            System.err.println("Ruta de archivo de propiedades sica.properties no encontrada.");
        }
        else {
            this.loadProperties(propertiesPath);
        }

    }

    /**
     * getInstance() method to recover singleton instance
     *
     * @return The signleton instance
     */

    public static SicaProperties getInstance() throws SicaException {
        if (instance == null) {
            instance = new SicaProperties();
        }
        return instance;
    }

    /**
     * Loads the properties from the assigned file
     *
     * @param filePath The local/absolute path of the properties file
     */
    protected void loadProperties(String filePath) throws SicaException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            this.load(fis);
            for (java.util.Iterator t = this.keySet().iterator(); t.hasNext();) {
                String key = t.next().toString();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new SicaException("Error en tiempo de ejecucion@SicaProperties.loadProperties: "
                    + e);
        }
        finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getSicaUsr() throws SicaException {
        Object usr = get("SICA_USR");
        if (usr == null) {
            throw new SicaException("No est\u00e1 definida la propiedad SICA_USR");
        }
        return usr.toString();
    }

    public String getSicaPwd() throws SicaException {
        Object usr = get("SICA_PWD");
        if (usr == null) {
            throw new SicaException("No est\u00e1 definida la propiedad SICA_PWD");
        }
        return usr.toString();
    }

    public String getSicaSys() throws SicaException {
        Object usr = get("SICA_SYS");
        if (usr == null) {
            throw new SicaException("No est\u00e1 definida la propiedad SICA_SYS");
        }
        return usr.toString();
    }
    
    public String getSicaEsbUsr() throws SicaException {
        Object usr = get("SICA_ESB_USR");
        if (usr == null) {
            throw new SicaException("No est\u00e1 definida la propiedad SICA_ESB_USR");
        }
        return usr.toString();
    }

    public String getSicaEsbPwd() throws SicaException {
        Object usr = get("SICA_ESB_PWD");
        if (usr == null) {
            throw new SicaException("No est\u00e1 definida la propiedad SICA_ESB_PWD");
        }
        return usr.toString();
    }

    /**
     * Instancia singleton de esta clase
     */
    protected static SicaProperties instance;


    /**
     * Path con las propiedades
     */
    protected static String propertiesPath = "archivos/sica/sica.properties";
}
