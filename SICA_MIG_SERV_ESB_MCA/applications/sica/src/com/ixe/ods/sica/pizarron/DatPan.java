/*
 * $Id: DatPan.java,v 1.12.30.1 2010/06/17 21:35:57 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pizarron;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

/**
 * Panel para mostrar los datos de una operaci&oacute;n de cambios.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.12.30.1 $ $Date: 2010/06/17 21:35:57 $
 */
public class DatPan extends JPanel {

    /**
     * Construye un JPanel para permitir al usuario capturar una operaci&oacute;n de cambios.
     *
     * @param tModel El JTable para obtener la celda seleccionada y determinar el tipo de
     *  operaci&oacute;n.
     * @param selRow El rengl&oacute;n seleccionado en la tabla.
     * @param selCol La columna seleccionada en la tabla.
     */
    public DatPan(TableModel tModel, int selRow, int selCol) {
        super();
        setLayout(new GridBagLayout());
        Insets ins = new Insets(2, 2, 2, 2);
        _tfCant.addKeyListener(new Comas());
        _tfCant.setText("");
        _tfTm = new JLabel(tModel.getValueAt(selRow, selCol).toString());
        _tfTc.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent evt) {
                ((JTextField) evt.getSource()).selectAll();
            }
            public void focusLost(FocusEvent evt) {
            }
        });
        _tfTc.setText(tModel.getValueAt(selRow, selCol).toString());
        add(new JLabel("Cantidad:"), new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(_tfCant, new GridBagConstraints(1, 0, 1, 1, 1.0, 0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel("T.C. Mesa:"), new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(_tfTm, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel("T.C. Cliente:"), new GridBagConstraints(0, 2, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(_tfTc, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, ins, 0, 0));
    }

    /**
     * Construye un JPanel para desplegar los datos de una operaci&oacute;n de cambios.
     *
     * @param compra true compra, false venta.
     * @param tv La fecha valor.
     * @param cant El monto de la operaci&ocute;n.
     * @param tm El tipo de cambio de la mesa.
     * @param tc El tipo de cambio del cliente.
     * @param idProd La clave del producto.
     * @param idDiv La clave de la divisa.
     * @param sdec Si es a seis decimales el formateo o a cuatro.
     */
    public DatPan(boolean compra, String tv, double cant, double tm, double tc, String idProd,
                  String idDiv, boolean sdec) {
        super();
        setLayout(new GridBagLayout());
        Insets ins = new Insets(2, 2, 2, 2);
        add(new JLabel("Cantidad:"), new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel(FMT_MON.format(cant)), new GridBagConstraints(1, 0, 1, 1, 1.0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        if (!sdec) {
        	add(new JLabel("T.C. Mesa:"), new GridBagConstraints(0, 1, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        	add(new JLabel(FMT_TC.format(tm)), new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        	add(new JLabel("T.C. Cliente:"), new GridBagConstraints(0, 2, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        	add(new JLabel(FMT_TC.format(tc)), new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        }
        else {
        	add(new JLabel("T.C. Mesa:"), new GridBagConstraints(0, 1, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        	add(new JLabel(FMT_TC.format(tm)), new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        	add(new JLabel("T.C. Cliente:"), new GridBagConstraints(0, 2, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        	add(new JLabel(FMT_TC.format(tc)), new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        }
        add(new JLabel("Operaci\u00f3n:"), new GridBagConstraints(0, 3, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel((compra ? "Compra " : "Venta ") + tv),
                new GridBagConstraints(1, 3, 3, 1, 1.0, 1.0, GridBagConstraints.EAST,
                        GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel("Instrumento:"), new GridBagConstraints(0, 4, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel(idProd), new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel("Divisa:"), new GridBagConstraints(0, 5, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel(idDiv), new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        }
    
    /**
     * Construye un JPanel para desplegar los datos de una operaci&oacute;n de cambios con 
     * mec&aacute;nica de neteos.
     *
     * @param compra true compra, false venta.
     * @param tvCpra La fecha valor de la compra.
     * @param cantCpra El monto de la operaci&ocute;n en la compra.
     * @param tmCpra El tipo de cambio de la mesa en la compra.
     * @param tcCpra El tipo de cambio del cliente en la compra.
     * @param idProdCpra La clave del producto en la compra.
     * @param idDivCpra La clave de la divisa en la compra.
     * @param sdecCpra Si es a seis decimales el formateo o a cuatro en la compra.
     * @param venta true compra, false venta.
     * @param tvVta La fecha valor de la venta.
     * @param cantVta El monto de la operaci&ocute;n en la venta.
     * @param tmVta El tipo de cambio de la mesa en la venta.
     * @param tcVta El tipo de cambio del cliente en la venta.
     * @param idProdVta La clave del producto en la venta.
     * @param idDivVta La clave de la divisa en la venta.
     * @param sdecVta Si es a seis decimales el formateo o a cuatro en la venta.
     */
    public DatPan(boolean compra, String tvCpra, double cantCpra, double tmCpra, double tcCpra, 
    			  String idProdCpra, String idDivCpra, boolean sdecCpra, boolean venta, 
    			  String tvVta, double cantVta, double tmVta, double tcVta, String idProdVta, 
    			  String idDivVta, boolean sdecVta) {
        super();
        setLayout(new GridBagLayout());
        Insets ins = new Insets(2, 2, 2, 2);
        add(new JLabel("COMPRA"), new GridBagConstraints(1, 0, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel("Cantidad:"), new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel(FMT_MON.format(cantCpra)), new GridBagConstraints(1, 1, 1, 1, 1.0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        if (!sdecCpra) {
        	add(new JLabel("T.C. Mesa:"), new GridBagConstraints(0, 2, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        	add(new JLabel(FMT_TC.format(tmCpra)), new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        	add(new JLabel("T.C. Cliente:"), new GridBagConstraints(0, 3, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        	add(new JLabel(FMT_TC.format(tcCpra)), new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        }
        else {
        	add(new JLabel("T.C. Mesa:"), new GridBagConstraints(0, 2, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        	add(new JLabel(FMT_TC.format(tmCpra)), new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        	add(new JLabel("T.C. Cliente:"), new GridBagConstraints(0, 3, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        	add(new JLabel(FMT_TC.format(tcCpra)), new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        }
        add(new JLabel("Operaci\u00f3n:"), new GridBagConstraints(0, 4, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel((compra ? "Compra " : "Venta ") + tvCpra),
                new GridBagConstraints(1, 4, 3, 1, 1.0, 1.0, GridBagConstraints.EAST,
                        GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel("Instrumento:"), new GridBagConstraints(0, 5, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel(idProdCpra), new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel("Divisa:"), new GridBagConstraints(0, 6, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel(idDivCpra), new GridBagConstraints(1, 6, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel("VENTA"), new GridBagConstraints(7, 0, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel(FMT_MON.format(cantVta)), new GridBagConstraints(7, 1, 1, 1, 1.0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        if (!sdecVta) {
        	add(new JLabel(FMT_TC.format(tmVta)), new GridBagConstraints(7, 2, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        	add(new JLabel(FMT_TC.format(tcVta)), new GridBagConstraints(7, 3, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        }
        else {
        	add(new JLabel(FMT_TC.format(tmVta)), new GridBagConstraints(7, 2, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        	add(new JLabel(FMT_TC.format(tcVta)), new GridBagConstraints(7, 3, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        }
        add(new JLabel((venta ? "Compra " : "Venta ") + tvVta),
                new GridBagConstraints(7, 4, 3, 1, 1.0, 1.0, GridBagConstraints.EAST,
                        GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel(idProdVta), new GridBagConstraints(7, 5, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        add(new JLabel(idDivVta), new GridBagConstraints(7, 6, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
    }

    /**
     * Regresa el valor de tfCant.
     *
     * @return JTextField.
     */
    public JTextField getTfCant() {
        return _tfCant;
    }

    /**
     * Regresa el valor de tfTc.
     *
     * @return JTextField.
     */
    public JTextField getTfTc() {
        return _tfTc;
    }

    /**
     * Regresa el valor de tfTm.
     *
     * @return JLabel.
     */
    public JLabel getTfTm() {
        return _tfTm;
    }

    /**
     * El textField para que el usuario capture el monto de la operaci&oacute;n.
     */
    private JTextField _tfCant = new JTextField();

    /**
     * El textField para que el usuario capture el tipo de cambio al cliente.
     */
    private JTextField _tfTc = new JTextField();

    /**
     * El label para mostrar el tipo de cambio de la mesa.
     */
    private JLabel _tfTm;

    /**
     * El formato monetario para el monto.
     */
    public static final DecimalFormat FMT_MON = new DecimalFormat("#,##0.00");

    /**
     * El formato para los tipos de cambio.
     */
    private static final DecimalFormat FMT_TC = new DecimalFormat("0.000000");
}
