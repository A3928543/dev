/*
 * $Id: CantLet.java,v 1.10 2008/02/22 18:25:40 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.util;

/**
 * Convertidor de cantidades num&eacute;ricas a letra.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:40 $
 */
public class CantLet {

    /**
     * Regresa la cantidad n expresada en letra en espa&ntilde;ol.
     *
     * @param n La cantidad a convertir.
     * @return String.
     */
    private static String convLt1000(long n) {
        String soFar;
        boolean special = false;

        if ((100 < n) && (n < 200)) {
            special = true;
        }
        if (n % 100 < 20) {
            soFar = nums[(int) (n % 100)];
            n /= 100;
        }
        else if ((20 < n % 100) && (n % 100 < 30)) {
            soFar = "VEINTI" + nums[(int) (n % 10)];
            n /= 100;
        }
        else {
            soFar = nums[(int) (n % 10)];
            if (n % 10 == 0) {
                n /= 10;
                soFar = nom10[(int) (n % 10)] + soFar;
            }
            else {
                n /= 10;
                soFar = nom10[(int) (n % 10)] + "Y " + soFar;
            }
            n /= 10;
        }
        if (n == 0) {
            return soFar;
        }
        if (special) {
            return "CIENTO " + soFar;
        }
        return nom100[(int) n] + soFar;
    }

    /**
     * Regresa la cantidad n expresada en letra en espa&ntilde;ol, agregando al final la clave de la divisa que se
     * recibe como par&aacute;metro.
     *
     * @param num La cantidad a convertir.
     * @param curr Las iniciales de la divisa.
     * @return String.
     */
    public static String conv(double num, String curr) {
        /* special case */
        if (num == 0) {
            return "CERO";
        }
        String prefix = "";
        if (num < 0) {
            num = -num;
            prefix = "MENOS ";
        }
        String soFar = "";
        int place = 0;
        do {
            long n = (long) (num % 1000);
            if (n != 0) {
                boolean singular = false;
                if (n % 100 == 1 && place != 0) {
                    singular = true;
                }
                if (singular) {
                    String s = convLt1000(n - 1);
                    soFar = s + sMajorNames[place] + soFar;
                }
                else {
                    String s = convLt1000(n);
                    soFar = s + pMajorNames[place] + soFar;
                }
            }
            place++;
            num /= 1000;
        }
        while (num > 0);
        return (prefix + soFar).trim() + " " + curr;
    }
    
    /**
     * Constante con los valores en letra de cantidades en miles y millones en singular.
     */
    private static final String[] sMajorNames = {"", "UN MIL ", "UN MILLON ", "UN BILLON ", "UN TRILLON ", "UN CUATRILLON ", "UN QUINTILLON "};
    
    /**
     * Constante con los valores en letra de cantidades en miles y millones en plural.
     */
    private static final String[] pMajorNames = {"", "MIL ", "MILLONES ", "BILLONES ", "TRILLONES ", "CUATRILLONES ", "QUINTILLONES "};
    
    /**
     * Constante con los valores en letra de cantidades en decenas.
     */
    private static final String[] nom10 = {"", "DIEZ ", "VEINTE ", "TREINTA ", "CUARENTA ", "CINCUENTA ", "SESENTA ", "SETENTA ", "OCHENTA ", "NOVENTA "};
    
    /**
     * Constante con los valores en letra de cantidades en centenas.
     */
    private static final String[] nom100 = {"", "CIEN ", "DOSCIENTOS ", "TRESCIENTOS ", "CUATROCIENTOS ", "QUINIENTOS ", "SEISCIENTOS ", "SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS "};
    
    /**
     * Constante con los valores en letra de cantidades en unidades y dos decenas.
     */
    private static final String[] nums = {"", "UN ", "DOS ", "TRES ", "CUATRO ", "CINCO ", "SEIS ", "SIETE ", "OCHO ", "NUEVE ", "DIEZ ", "ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS ", "DIECISIETE ", "DIECIOCHO ", "DIECINUEVE "};
}
