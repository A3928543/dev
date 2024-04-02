/*
 * $Id: PizarraApplet.java,v 1.13.10.1.6.1.8.2 2011/06/20 21:45:09 guzmanal Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.applet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ixe.ods.sica.pizarron.Comas;
import com.ixe.ods.sica.pizarron.DatPan;
import com.ixe.ods.sica.pizarron.ICtrlPizarra;
import com.ixe.ods.sica.pizarron.PizPanel;

/**
 * <p>Applet para mostrar la pizarra del SICA. Tiene 2 modos de funcionamiento:
 * </p>
 * <ul>
 *   <li>En l&iacute;nea.- los precios son actualizados en el momento en que es modificado
 *      alg&uacute;n spread, factor o el precio de referencia.</li>
 *   <li>Fuera de l&iacute;nea.- el usuario debe refrescar el pizarr&oacute;n para obtener los
 *       nuevos precios.</li>
 * </ul>
 * <p>Cuando el usuario est&aacute; parado sobre la p&aacute;gina de captura de deals, puede hacer
 *    doble-click sobre alguna de las celdas de la pizarra para escoger as&iacute; la fecha valor,
 *    el producto, el tipo de operaci&oacute; y el precio de la pizarra.
 * </p>
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.13.10.1.6.1.8.2 $ $Date: 2011/06/20 21:45:09 $
 */
public class PizarraApplet extends JApplet implements ICtrlPizarra, ActionListener {

	/**
	 * Constructor sin parametros del applet.
	 */
    public PizarraApplet() {
        super();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();  
        }
    }

    /**
     * Llama a <code>initParams()</code>, <code>initGui()</code> e
     * <code>initJms()</code>.
     */
    public void init() {
        super.init();
        initParams();
        setLocale(new Locale("es", "mx"));                                    
    }

    /**
     * Inicializa los par&aacute;metros de divisas frecuentes, no frecuentes y metales amonedados;
     * la lista de formas de pago y liquidaci&oacute;n y la lista de renglones iniciales de la
     * pizarra. Los parametros se obtienen de la p&aacute;gina DatosPizarron.html, por medio de una
     * conexi&oacute;n http al servicio SicaPizarronService.
     */
    private void initParams() {
        List divs = new ArrayList();
        for (int i = 0; getParameter(DIV_PARAM + i) != null; i++) {
            divs.add(getParameter(DIV_PARAM + i));
        }
        List fpls = new ArrayList();
        for (int i = 0; getParameter(FPL_PARAM + i) != null; i++) {
            String[] vs = getParameter(FPL_PARAM + i).split(PIPE_REGEXP);
            Map map = new HashMap();
            map.put("claveFormaLiquidacion", vs[0]);
            map.put(MNEMONICO_KEY, vs[1]);
            map.put("idDivisa", vs[2]);
            map.put("minimo", new BigDecimal(vs[3]));
            map.put("multiplo", Double.valueOf(vs[4]));
            map.put("desc", vs[5]);
            fpls.add(map);
        }
        List reglasNeteos = new ArrayList();
        Map mapReglas = null;
        for (int i = 0;getParameter("regla"+i)!=null;i++) {
        	String[] parametros = getParameter("regla" + i).split("\\|"); 
        	mapReglas = new HashMap();
        	mapReglas.put("idDivisa", parametros[0].trim());
        	mapReglas.put("claveFormaLiquidacionCompra", parametros[1].trim());
        	mapReglas.put("claveFormaLiquidacionVenta", parametros[2].trim());
        	mapReglas.put("baseRegla",parametros[3].trim());
        	mapReglas.put("difAntesDelHorario", !parametros[4].trim().equals("null") ?
        			new BigDecimal(parametros[4]) : null);
        	mapReglas.put("difDespuesDelHorario", !parametros[5].trim().equals("null") ?
        			new BigDecimal(parametros[5]) : null);
        	mapReglas.put("montoAntesDelHorario",parametros[6].trim());
        	mapReglas.put("montoDespuesDelHorario",parametros[7].trim());
        	reglasNeteos.add(mapReglas);
        }
        pizPanel = new PizPanel(getParameter("ticket"), this, getCodeBase().getProtocol(),
                getCodeBase().getHost() + ":" + getCodeBase().getPort(), 
                new Integer(getParameter("tp")).intValue(), divs, fpls,reglasNeteos, null, 
                Double.valueOf(getParameter("DESV_MONTO_LIM")).doubleValue(), 
                Double.valueOf(getParameter("DESV_PORC_LIM")).doubleValue(), 
                Double.valueOf(getParameter("DESV_FACT_1")).doubleValue(), 
                Double.valueOf(getParameter("DESV_FACT_2")).doubleValue(), 
                Double.valueOf(getParameter("DESV_PORC_MAX")).doubleValue(),
                "true".equals(getParameter("vFut")),
                Integer.valueOf(getParameter("segsRefr")).intValue(),
                String.valueOf(getParameter("psLimHr")).toString());
		pizPanel.setToutMA_DNF(Integer.valueOf(getParameter("tout")).intValue());
		pizPanel.setToutDF(Integer.valueOf(getParameter("toutDF")).intValue());
        getContentPane().setLayout(new BorderLayout(5, 5));
        getContentPane().add(pizPanel);
    }

    /**
     * Recibe los par&aacute;metros de la operaci&oacute;n capturada en el panel del
     * pizarr&oacute;n.
     *
     * Si quien implementa esta interfaz es el cliente del SICA en el Teller, &eacute;ste debe
     * utilizar SicaTellerSessionBean.agregarDetalleDeal(), pasando estos mismos par&aacute;metros
     * como referencia, para generar el detalle del deal con los datos de la operaci&oacute;n.
     *
     * @param cpra true para compra, false para venta.
     * @param tv Tipo Valor: CASH | TOM | SPOT.
     * @param idProd La clave del producto (TRAEXT, EFECTI, CHVIAJ, etc).
     * @param idDiv La clave de la divisa, seg&uacute;n la tabla SC_DIVISA.
     * @param tcm El tipo de cambio de la mesa.
     * @param monto El monto en la divisa de la operaci&oacute;n.
     * @param tcc El tipo de cambio otorgado al cliente.
     * @param prMidSpot El precio referencia Mid Spot utilizado.
     * @param prSpot El precio referencia Spot utilizado.
     * @param factorDivisa El Factor Divisa utilizado para la captura del detalle.
     * @param idSp El identificador del spread utilizado.
     * @param idPr El identificador del precio de referencia utilizado.
     * @param idFd El identificador del factor de divisa utilizado.
     */
    public void operar(boolean cpra, String tv, String idProd, String idDiv, double tcm, double monto,
                double tcc, double prMidSpot, double prSpot, double factorDivisa, int idSp, int idPr, int idFd) {
        try {
            URL url = getCodeBase();
            getAppletContext().showDocument(new URL(url.getProtocol(), url.getHost(), url.getPort(),
                    "/sica/app?service=external/CapturaDeal&sp=1" +
                            STR_PAR_PREFIX + (cpra ? "C" : "V") +
                            STR_PAR_PREFIX + tv +
                            STR_PAR_PREFIX + idProd +
                            STR_PAR_PREFIX + idDiv +
                            DBL_PAR_PREFIX + tcm +
                            DBL_PAR_PREFIX + monto +
                            DBL_PAR_PREFIX + tcc +
                            DBL_PAR_PREFIX + prMidSpot +
                            DBL_PAR_PREFIX + prSpot +
                            DBL_PAR_PREFIX + factorDivisa +
                            INT_PAR_PREFIX + idSp +
                            INT_PAR_PREFIX + idPr +
                            INT_PAR_PREFIX + idFd +
                            STR_PAR_PREFIX + new Date().getTime()), FRAME_SUP);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Recibe los par&aacute;metros de la operaci&oacute;n capturada en el panel de
     * mec&aacute;nica de neteos.
     *
     *
     * @param cpra Define que la operacion es una compra.
     * @param tvCpra Tipo valor del deal, del lado de la compra.
     * @param idProdCpra Id del producto de la compra.
     * @param idDivCpra Id de la divisa de la compra.
     * @param tcmCpra Tipo de cambio de la mesa para la compra.
     * @param montoCpra Monto del deal de la compra.
     * @param tccCpra Valor del tipo de cambio para el cliente, del lado de la compra.
     * @param prMidSpotCpra El precio referencia Mid Spot utilizado para la captura del detalle Compra.
     * @param prSpotCpra El precio referencia Spot utilizado para la captura del detalle Compra.
     * @param factorDivisaCpra El Factor Divisa utilizado para la captura del detalle Compra.
     * @param idPrCpra Id del precio de referencia actual para la compra.
     * @param idFdCpra Id del factor divisa para la compra.
     * @param idSpCpra Id del Spread para la compra.
     * @param vta Define que la operacion es una venta.
     * @param tvVta Tipo valor del deal, del lado de la venta.
     * @param idProdVta Id del producto de la venta.
     * @param idDivVta Id de la divisa de la venta.
     * @param tcmVta Tipo de cambio de la mesa para la venta.
     * @param montoVta Monto del deal de la venta.
     * @param tccVta Valor del tipo de cambio para el cliente, del lado de la venta.
     * @param prMidSpotVta El precio referencia Mid Spot utilizado para la captura del detalle Venta.
     * @param prSpotVta El precio referencia Spot utilizado para la captura del detalle Venta.
     * @param factorDivisaVta El Factor Divisa utilizado para la captura del detalle Venta.
     * @param idPrVta Id del precio de referencia actual para la venta.
     * @param idFdVta Id del factor divisa para la venta.
     * @param idSpVta Id del Spread para la venta.
     */
    public void operarNeteos(boolean cpra, String tvCpra, String idProdCpra, String idDivCpra,
                      double tcmCpra, double montoCpra, double tccCpra, double prMidSpotCpra, 
                      double prSpotCpra, double factorDivisaCpra, int idPrCpra, int idFdCpra, int idSpCpra, 
                      boolean vta, String tvVta, String idProdVta, String idDivVta,
                      double tcmVta, double montoVta, double tccVta, double prMidSpotVta, 
                      double prSpotVta, double factorDivisaVta, int idPrVta, int idFdVta, int idSpVta) {
        try {
            URL url = getCodeBase();
            getAppletContext().showDocument(new URL(url.getProtocol(), url.getHost(), url.getPort(),
                    "/sica/app?service=external/CapturaDeal&sp=7" +
                            STR_PAR_PREFIX + (cpra ? "C" : "V") +
                            STR_PAR_PREFIX + tvCpra +
                            STR_PAR_PREFIX + idProdCpra +
                            STR_PAR_PREFIX + idDivCpra +
                            DBL_PAR_PREFIX + tcmCpra +
                            DBL_PAR_PREFIX + montoCpra +
                            DBL_PAR_PREFIX + tccCpra +
                            DBL_PAR_PREFIX + prMidSpotCpra +
                            DBL_PAR_PREFIX + prSpotCpra +
                            DBL_PAR_PREFIX + factorDivisaCpra +
                            INT_PAR_PREFIX + idSpCpra +
                            INT_PAR_PREFIX + idPrCpra +
                            INT_PAR_PREFIX + idFdCpra +
                            STR_PAR_PREFIX + (vta ? "C" : "V") +
                            STR_PAR_PREFIX + tvVta +
                            STR_PAR_PREFIX + idProdVta +
                            STR_PAR_PREFIX + idDivVta +
                            DBL_PAR_PREFIX + tcmVta +
                            DBL_PAR_PREFIX + montoVta +
                            DBL_PAR_PREFIX + tccVta +
                            DBL_PAR_PREFIX + prMidSpotVta +
                            DBL_PAR_PREFIX + prSpotVta +
                            DBL_PAR_PREFIX + factorDivisaVta +
                            INT_PAR_PREFIX + idSpVta +
                            INT_PAR_PREFIX + idPrVta +
                            INT_PAR_PREFIX + idFdVta +
                            STR_PAR_PREFIX + new Date().getTime()), FRAME_SUP);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Define si se confirm&oacute; o no la operaci&oacute;n con los parametros establecidos.
     * 
     * @param rec Define si recibimos o entregamos.
     * @param tv Tipo valor del deal.
     * @param cant Monto del deal.
     * @param tcm Valor del tipo de cambio de la mesa.
     * @param tcc Valor del tipo de cambio para el cliente.
     * @param idProd Id del producto con el que se esta operando.
     * @param idDiv Id de la divisa.
     * @param sdec true si se desean 6 decimales o no.
     * @param adv true si se desea advertir que el tipo de cambio.
     * @param porcDesv El porcentaje de desviaci&oacute;n al tipo de cambio, s&oacute;lo se toma en
     *                  cuenta si adv est&aacute; en true.
     * @return int.
     */
    public int confOp(boolean rec, String tv, double cant, double tcm, double tcc, String idProd,
                      String idDiv, boolean sdec, boolean adv, String porcDesv) {
        if (adv && pizPanel.preguntar("El tipo de cambio tiene una desviaci\u00f3n mayor al " +
                porcDesv + "%. \u00bfDesea continuar?") == JOptionPane.NO_OPTION) {
            return JOptionPane.NO_OPTION;
        }
        return pizPanel.confOp(rec, tv, cant, tcm, tcc, idProd, idDiv, sdec);
    }

    /**
     * Fija el valor de capturaDeal.
     *
     * @param capturaDeal El valor a asignar.
     */
    public void setCapturaDeal(boolean capturaDeal) {
        pizPanel.setCapturaDeal(capturaDeal);
    }

    /**
     * Fija el valor de tipo de deal. Usado desde javascript.
     *
     * @param tipoDeal El valor a asignar.
     */
    public void setTipoDeal(int tipoDeal) {
        pizPanel.setPivote(PizPanel.SIN_PIVOTE);
    }

    /**
     * Hace forward del mensaje al pizPanel y regresa el resultado.
     *
     * @see PizPanel#preguntar(String).
     * @param m El mensaje a desplegar.
     * @return int.
     */
     public int preguntar(String m) {
         return pizPanel.preguntar(m);
     }

    /**
     * Muestra un panel modal para modificaciones a los detalles de deal (limpiar mnem&oacute;nicos
     * y split). Regresa un mapa con los valores tecleados por el usuario.
     *
     * @param titulo El t&iacute;tulo del panel.
     * @param conMonto Si debe preguntarse el nuevo monto o no.
     * @param max El monto m&aacute;ximo que puede teclear el usuario.
     * @param idDiv El identificador de la divisa.
     * @param mnem Los mnem&oacute;nicos a desplegar separados por pipes.
     * @param tpls Los Nombres de los Tipos Liquidaci&oacute;n a desplegar separados por pipes.
     * @param etiqueta La etiqueta para la forma de liquidaci&oacute;n.
     * @return Map.
     */
    private Map mostrarPanelDeal(String titulo, boolean conMonto, double max, String idDiv,
                                 String tpls, String mnem, String etiqueta) {
    	fpls = mnem.split(PIPE_REGEXP);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        Insets ins = new Insets(2, 2, 2, 2);
        JTextField tfMonto = new JTextField();
        tfMonto.addKeyListener(new Comas());
        panel.add(new JLabel(etiqueta), new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
        JComboBox cb = new JComboBox(tpls.split(PIPE_REGEXP));
        cb.setPreferredSize(new Dimension(400, 30));
        cb.addActionListener(this);
        panel.add(cb, new GridBagConstraints(1, 0, 2, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, ins, 1, 0));
        cb2.removeAllItems();
        panel.add(cb2, new GridBagConstraints(1, 3, 2, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, ins, 1, 3));
        if (conMonto) {
            panel.add(new JLabel("Monto:"), new GridBagConstraints(0, 6, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
            panel.add(tfMonto, new GridBagConstraints(1, 6, 1, 1, 1, 0, GridBagConstraints.EAST,
                    GridBagConstraints.HORIZONTAL, ins, 0, 0));
            panel.add(new JLabel("(M\u00e1ximo " + max + " " + idDiv + ")"),
                    new GridBagConstraints(2, 6, 1, 1, 0, 0, GridBagConstraints.EAST,
                            GridBagConstraints.HORIZONTAL, ins, 0, 0));
        }
        JOptionPane op = new JOptionPane(panel);
        op.setOptionType(JOptionPane.OK_CANCEL_OPTION);
        double monto = 0.0;
        Integer noOpc;
        do {
            JDialog dialog;
            dialog = op.createDialog(null, titulo);
            dialog.show();
            noOpc = (Integer) op.getValue();
            if (noOpc == null || noOpc.intValue() != 0) {
                break;
            }
            if (!conMonto) {
                break;
            }
            try {
                monto = DatPan.FMT_MON.parse(tfMonto.getText()).doubleValue();
                if (monto <= max) {
                    break;
                }
            }
            catch (ParseException e) {
                e.printStackTrace();  
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }

        } while(true);
        if (noOpc != null && (noOpc.intValue() == 0 && cb2.getSelectedItem() != null)) {
            Map res = new HashMap();
            if (conMonto) {
                res.put(MONTO_KEY, String.valueOf(monto));
            }
            res.put(MNEMONICO_KEY, cb2.getSelectedItem().toString().split("\\.\\-")[1]);
            return res;
        }
        else {
            return null;
        }
    }
    
    /**
     * Escucha los Eventos del Combo Box de Nombres de los Tipos Liquidaci&oacute;n
     * 
     * @param e	El evento de selecci&oacute;n de un elemento en el Combo Box.
     */
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        String tpl = cb.getSelectedItem().toString();
        popularFormasLiquidacionFiltradas(tpl);
    }
    
    /**
     * Regresa la lista de formas de liquidaci&oacute;n para recibimos o para entregamos pero
     * filtradas de acuerdo a lo seleccionado en el combo de Tipos Liquidaci&oacute;n.
     *
     * @param tpl El Tipos Liquidaci&oacute;n seleccionado.
     */
	public void popularFormasLiquidacionFiltradas(String tpl) {
		cb2.removeAllItems();
		for (int i = 0; i < fpls.length; i++) {
			String ntpl = fpls[i].split("\\@")[0];
			String fpl = fpls[i].split("\\@")[1];
			if (tpl.equals(ntpl)) {
				cb2.addItem(fpl);
			}
		}
	}

    /**
     * Muestra un panel modal para preguntar al promotor la forma de liquidaci&oacute;n a la que se
     * quiere cambiar un detalle de deal. Al confirmar los datos, se redirige a la p&aacute;gina de
     * CapturaDeal, para realizar los cambios al detalle.
     *
     * @see #mostrarPanelDeal(String, boolean, double, String, String, String, String).
     * @param idDealDet El n&uacute;mero de detalle del deal.
     * @param tpls La lista de Nombres de Tipos Liquidaci&oacute;n.
     * @param mnems La lista de mnem&oacute;nicos disponibles.
     * @param etiqueta La etiqueta para preguntar la forma de liquidaci&oacute;n.
     * @param inter Si se trata de un deal interbancario.
     */
    public void mostrarPanelModif(int idDealDet, String tpls, String mnems, String etiqueta,
                                  Boolean inter) {
        Map res = mostrarPanelDeal("Limpiar la forma de liquidaci\u00f3n:", false, 0.0, null, tpls,
                mnems, etiqueta);
        if (res != null) {
            try {
                URL url = getCodeBase();
                if (inter.booleanValue()) {
                	getAppletContext().showDocument(new URL(url.getProtocol(), url.getHost(),
                            url.getPort(),
                            "/sica/app?service=external/CapturaDealInterbancario&sp=5&sp=" +
                                    idDealDet + STR_PAR_PREFIX + res.get(MNEMONICO_KEY)),
                            FRAME_SUP);
                }
                else {
                	getAppletContext().showDocument(new URL(url.getProtocol(), url.getHost(),
                            url.getPort(),
                            "/sica/app?service=external/CapturaDeal&sp=5&sp=" + idDealDet +
                                    STR_PAR_PREFIX + res.get(MNEMONICO_KEY)), FRAME_SUP);
                }
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Muestra un panel modal para preguntar al promotor los datos necesarios para realizar el split
     * de un detalle de deal en dos. Al confirmar los datos, se redirige a la p&aacute;gina de
     * CapturaDeal, para realizar los cambios al detalle.
     *
     * @param idDealDet El n&uacute;mero de detalle original del deal.
     * @param folDet El folio del detalle del deal.
     * @param max El monto m&aacute;ximo que puede escribir el promotor.
     * @param idDiv El identificador de la divisa.
     * @param tpls La lista de Nombres de Tipos Liquidaci&oacute;n aplicables.
     * @param mnems La lista de mnem&oacute;nicos aplicables.
     * @param inter Si es interbancario o no.
     */
    public void mostrarPanelSplit(int idDealDet, int folDet, double max, String idDiv, 
    		String tpls, String mnems, Boolean inter) {
        Map res = mostrarPanelDeal("Dividir un detalle en dos", true, max, idDiv, tpls, mnems,
                "Forma de liquidaci\u00f3n:");
        if (res != null) {
            try {
                URL url = getCodeBase();
                if (inter.booleanValue()) {
                    getAppletContext().showDocument(new URL(url.getProtocol(), url.getHost(),
                            url.getPort(), "/sica/app?service=external/CapturaDealInterbancario" +
                                    "&sp=4&sp=" + idDealDet + DBL_PAR_PREFIX + res.get(MONTO_KEY) +
                                    STR_PAR_PREFIX + res.get(MNEMONICO_KEY)), FRAME_SUP);
                }
                else {
                    getAppletContext().showDocument(new URL(url.getProtocol(), url.getHost(),
                            url.getPort(), "/sica/app?service=external/CapturaDeal&sp=4&sp=" +
                                    idDealDet + DBL_PAR_PREFIX + res.get(MONTO_KEY) +
                                    STR_PAR_PREFIX + res.get(MNEMONICO_KEY)), FRAME_SUP);
                }
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Imprime el valor del log.
     * @param log Log generado.
     */
    public void log(String log) {
    	System.out.println(log);
    }

    /**
     * Cierra _jmsConn y _ctx. fija started en false.
     */
    public void destroy() {
        if (pizPanel != null) {
            pizPanel.desregistrar();
        }
    }
    
    /**
     * PizPanel de la pizarra.
     */
    private PizPanel pizPanel;
    
    /**
     * Arreglo de formas de liquidaci&oacute;n.
     */
    private String[] fpls;
    
    /**
     * ComboBox para los mnem&oacute;nicos.
     */
    private JComboBox cb2 = new JComboBox();

    /**
     * La constante para la expresi&oacute;n regular del caracter pipe.
     */
    private static final String PIPE_REGEXP = "\\|";

    /**
     * La constante para el par&aacute;tro 'div'.
     */
    private static final String DIV_PARAM = "div";

    /**
     * La constante para el par&aacute;metro 'fpl'.
     */
    private static final String FPL_PARAM = "fpl";

    /**
     * La constante para la llave 'mnemonico' dentro de Maps.
     */
    private static final String MNEMONICO_KEY = "mnemonico";

    /**
     * La constante para la llave 'monto' dentro de Maps.
     */
    private static final String MONTO_KEY = "monto";

    /**
     * Prefijo para par&aacute;metros String para p&aacute;ginas de Tapestry.
     */
    private static final String STR_PAR_PREFIX = "&sp=S";

    /**
     * Prefijo para par&aacute;metros Double para p&aacute;ginas de Tapestry.
     */
    private static final String DBL_PAR_PREFIX = "&sp=d";

    /**
     * Prefijo para par&aacute;metros Integer para p&aacute;ginas de Tapestry.
     */
    private static final String INT_PAR_PREFIX = "&sp=";

    /**
     * Contante para el nombre del frame superior de la p&aacute;gina.
     */
    private static final String FRAME_SUP = "superior";
}
