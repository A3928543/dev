/*
 * $Id: MapUtils.java,v 1.2 2009/09/23 16:58:12 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import ognl.Ognl;
import ognl.OgnlException;

/**
 * Utiler&iacute;a para generar mapas en menos l&iacute;neas de c&oacute;digo.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2009/09/23 16:58:12 $
 */
public class MapUtils {

    /**
     * Crea un mapa con base a los arreglos de llaves y valores especificados.
     *
     * @param llaves El arreglo de llaves.
     * @param valores El arreglo de valores.
     * @return Map.
     */
    public static Map generar(String[] llaves, Object[] valores) {
        Map mapa = new HashMap();
        for (int i = 0; i < llaves.length; i++) {
            mapa.put(llaves[i], valores[i]);
        }
        return mapa;
    }


    /**
     * Convierte el objeto especificado en un mapa, respetando los nombres de las propiedades como
     * llaves del mapa.
     *
     * @param o El objeto a convertir.
     * @return HashMap.
     */
    public static Map generar(Object o) {
        Map mapa = new HashMap();
        Field[] fields = o.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                mapa.put(fields[i].getName(), Ognl.getValue(fields[i].getName(), o));
            }
            catch (OgnlException e) {
            }
        }
        return mapa;
    }

    /**
     * Regresa una copia del mapa especificado.
     *
     * @param otroMapa El mapa a copiar.
     * @return Map.
     */
    public static Map copiar(Map otroMapa) {
        Map resultado = new HashMap();
        for (Iterator it = otroMapa.keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();
            resultado.put(key, otroMapa.get(key));
        }
        return resultado;
    }

    public static void eliminarLlaves(Map mapa, String[] llaves) {
        for (int i = 0; i < llaves.length; i++) {
            mapa.remove(llaves[i]);
        }
    }
}