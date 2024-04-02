/*
 * $Id: Comas.java,v 1.11.30.1 2010/06/17 21:34:21 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pizarron;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import javax.swing.JTextField;

/**
 * Clase que da formato a las cantidades ingresadas a los comboBox, colocando
 * las comas como separadores de miles.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.11.30.1 $ $Date: 2010/06/17 21:34:21 $
 */
public class Comas implements KeyListener  {

    /**
     * No hace nada.
     *
     * @param e El evento del teclado.
     */
    public void keyPressed(KeyEvent e) {
    }

    /**
     * Registra el evento cuando se presiona una tecla y da formato
     * a la cantidad ingresada.
     * 
     * @param KeyEvent El evento del teclado.
     */
    public void keyReleased(KeyEvent e) {
        try {
            JTextField tf = (JTextField) e.getSource();
            if (e.getKeyChar() != '.') {
                String montoStr = tf.getText().replaceAll("\\,", "");
                int ind = montoStr.indexOf(".");
                if (ind >= 0 && ind < montoStr.length() - 3) {
                    montoStr = montoStr.substring(0, ind + 3);
                }
                ind = montoStr.indexOf(".");
                if (ind > 0 && ind == montoStr.length() - 2) {
                    tf.setText(FMT_MON_1.format(Double.valueOf(montoStr)));
                }
                else if (ind > 0 && ind == montoStr.length() - 3) {
                    tf.setText(FMT_MON_2.format(Double.valueOf(montoStr)));
                }
                else {
                    tf.setText(FMT_MON_0.format(Double.valueOf(montoStr)));
                }
            }
        }
        catch (NumberFormatException e1) {
        }
    }

    /**
     * No hace nada.
     *
     * @param e El evento del teclado.
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Formato que se aplicar&aacute; #,##0.##.
     */
    public static final DecimalFormat FMT_MON_0 = new DecimalFormat("#,##0.##");

    /**
     * Formato que se aplicar&aacute; #,##0.0#.
     */
    public static final DecimalFormat FMT_MON_1 = new DecimalFormat("#,##0.0#");

    /**
     * Formato que se aplicar&aacute; #,##0.00.
     */
    public static final DecimalFormat FMT_MON_2 = new DecimalFormat("#,##0.00");
}
