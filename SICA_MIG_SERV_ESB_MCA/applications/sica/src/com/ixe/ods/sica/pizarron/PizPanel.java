/*
 * $Id: PizPanel.java,v 1.21.10.4.4.1.8.3.24.1 2014/02/19 19:33:48 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pizarron;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Esta clase permite obtener desde una p&aacute;gina de Tapestry, los tipos de cambio para los
 * distintos productos con las que operan las divisas del pizarr&oacute;n; los datos se obtienen por
 * medio de una conexi&oacute;n http a esta p&aacute;gina.
 *
 * Configura las propiedades de la interfaz grafica del pizarron. regristra el evento del doble
 * click para la captura de deal y genera el applet para la definici&oacute;n del tipo de cambio del
 * deal y el applet de confirmaci&oacute;n de la operaci&iacute;n, valida los limites de
 * desviaci&oacute;n del tipo de cambio; define cuando los tipos de cambio estan 'a la alza' o
 * 'a la baja' cambiando de color las celdas del pizarron.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.21.10.4.4.1.8.3.24.1 $ $Date: 2014/02/19 19:33:48 $
 */
public class PizPanel extends JPanel implements ActionListener {

	/**
	 * Constructor de la clase.
	 */
    public PizPanel() {
        super();
        try {
            _bRef = new JButton();
            _bRef.setIcon(new ImageIcon(getClass().getResource("refresh.gif")));
        }
        catch (NullPointerException e) {
            if (_logger.isLoggable(Level.WARNING)) {
                _logger.log(Level.WARNING, "No se pudo cargar la imagen refresh.gif");
            }
        }
    }

    /**
     * Constructor que invoca a inicializar(), que asigna los valores necesarios a las variables de
     * instancia.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param ctrl La interfaz del tipo de operaci&oacute;n.
     * @param prot El protocolo de comunicaci&oacute;n (http | https).
     * @param serv El nombre del host.
     * @param idTp El id del Tipo de Pizarron para el canal.
     * @param divs La lista de divisas.
     * @param fpls La lista de productos.
     * @param reglasNeteos La lista de reglas de neteos.
     * @param restrs La lista de restricciones.
     * @param desvMontoLim El valor del l&iacute;mite de desviaci&oacute;n del monto.
     * @param desvPorcLim El valor del l&iacute;mite de desviaci&oacute;n del porcentaje.
     * @param desvFact1 El factor 1 de desviaci&oacute;n.
     * @param desvFact2 El factor 2 de desviaci&oacute;n.
     * @param desvPorcMax El valor maximo de desviaci&oacute;n del porcentaje.
     * @param vFut Define si hay valor futuro.
     * @param segsRefr El n&uacute;mero de segundos para refrescar el pizarr&oacute;n.
     * @see #inicializar(String, ICtrlPizarra, String, String, int, java.util.List, java.util.List,
     *      java.util.List, java.util.List, boolean, double, double, double, double, double,
     *      boolean, int, String)
     */ 
    public PizPanel(String ticket, ICtrlPizarra ctrl, String prot, String serv, int idTp,
                    List divs, List fpls, List reglasNeteos, List restrs, double desvMontoLim,
                    double desvPorcLim, double desvFact1, double desvFact2, double desvPorcMax,
                    boolean vFut, int segsRefr, String psLimHr) {
        this();
        inicializar(ticket, ctrl, prot, serv, idTp, divs, fpls, reglasNeteos, restrs, false,
                desvMontoLim, desvPorcLim, desvFact1, desvFact2, desvPorcMax, vFut, segsRefr, 
                psLimHr);
    }

    /**
     * Asigna los valores a las variables de instancia de la clase.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param ctrl La interfaz del tipo de operaci&oacute;n.
     * @param prot El protocolo de comunicaci&oacute;n (http | https).
     * @param serv El nombre del host.
     * @param idTp El id del Tipo de Pizarron para el canal.
     * @param divs La lista de divisas.
     * @param fpls La lista de productos.
     * @param reglasNeteos La lista de reglas de neteos.
     * @param restrs La lista de restricciones.
     * @param soloCash Define si el pizarr&oacute;n solo muestra las celdas de los tipos de cambio
     * para cash.
     * @param desvMontoLim El valor del l&iacute;mite de desviaci&oacute;n del monto.
     * @param desvPorcLim El valor del l&iacute;mite de desviaci&oacute;n del porcentaje.
     * @param desvFact1 El factor 1 de desviaci&oacute;n.
     * @param desvFact2 El factor 2 de desviaci&oacute;n.
     * @param desvPorcMax El valor maximo de desviaci&oacute;n del porcentaje.
     * @param vFut Define si hay valor futuro.
     * @param segsRefr El n&uacute;mero de segundos para refrescar el pizarr&oacute;n.
     */
    public void inicializar(String ticket, ICtrlPizarra ctrl, String prot, String serv,
                            int idTp, List divs, List fpls, List reglasNeteos, List restrs,
                            boolean soloCash, double desvMontoLim, double desvPorcLim,
                            double desvFact1, double desvFact2, double desvPorcMax, boolean vFut,
                            int segsRefr, String psLimHr) {
        _ticket = ticket;
        this.prot = prot;
        _serv = serv;
        _vFut = vFut;
        _restrs = restrs;
        _ctrl = ctrl;
        _idTipoPizarron = idTp;
        _segsRefr = segsRefr;
        _fpls = fpls;
        _reglasNeteos = reglasNeteos;
        _soloCash = soloCash;
        _desvMontoLim = desvMontoLim;
        _desvPorcLim = desvPorcLim;
        _desvFact1 = desvFact1;
        _desvFact2 = desvFact2;
        _desvPorcMax = desvPorcMax;
        _psLimHr = psLimHr;
        confGui(divs);
        _rows = new ArrayList();
        actualizar();
        timer.start();
    }

    /**
     * Sustituye los acentos codificados para XML por unicode.
     *
     * @param str La cadena a convertir.
     * @return String.
     */
    private String convAcentos(String str) {
        str = str.replaceAll("&#225;", "\u00e1");
        str = str.replaceAll("&#233;", "\u00e9");
        str = str.replaceAll("&#237;", "\u00ed");
        str = str.replaceAll("&#241;", "\u00f1");
        str = str.replaceAll("&#243;", "\u00f3");
        return str.replaceAll("&#250;", "\u00fa");
    }

    /**
     * Regresa el mapa de valores, seg&uacute;n la opci&oacute;n que se solicit&oacute; al servidor.
     *
     * @param opcion OP_PIZ | OP_MET | OP_NFREC.
     * @param i El contador del rengl&oacute;n.
     * @param values El arreglo de valores de la l&iacute;nea devuelta por el servidor.
     * @return Map.
     */
    private Map getValores(int opcion, int i, String[] values) {
        Map valores = new HashMap();
        if (opcion == OP_PIZ) {
            if (i > 0) {
                valores.put("idSpread", new Integer(values[Consts.NUM_0]));
                valores.put(Consts.ID_FACTOR_DIVISA_KEY,
                        new Integer(values[Consts.NUM_1]));
                valores.put(Consts.CLAVE_FORMA_LIQUIDACION_KEY,
                        values[Consts.NUM_2]);
                valores.put("nombreFormaLiquidacion",
                        convAcentos(values[Consts.NUM_3]));
                valores.put(Consts.ID_DIVISA_KEY, values[Consts.NUM_4]);
                valores.put(Consts.FACTOR_DIVISA_KEY, new Double(values[Consts.NUM_5]));
                valores.put(Consts.C_CASH_KEY, new Double(values[Consts.NUM_6]));
                valores.put(Consts.V_CASH_KEY, new Double(values[Consts.NUM_7]));
                valores.put(Consts.C_TOM_KEY, new Double(values[Consts.NUM_8]));
                valores.put(Consts.V_TOM_KEY, new Double(values[Consts.NUM_9]));
                valores.put(Consts.C_SPOT_KEY, new Double(values[Consts.NUM_10]));
                valores.put(Consts.V_SPOT_KEY, new Double(values[Consts.NUM_11]));
                valores.put(Consts.C_72HR_KEY, new Double(values[Consts.NUM_12]));
                valores.put(Consts.V_72HR_KEY, new Double(values[Consts.NUM_13]));
                valores.put(Consts.C_VFUT_KEY, new Double(values[Consts.NUM_14]));
                valores.put(Consts.V_VFUT_KEY, new Double(values[Consts.NUM_15]));
                valores.put(Consts.PR_MID_SPOT_KEY, new Double(values[Consts.NUM_16]));
                valores.put(Consts.PR_SPOT_KEY, new Double(values[Consts.NUM_17]));
                valores.put("cMaxCash", new Double(values[Consts.NUM_18]));
                valores.put("vMinCash", new Double(values[Consts.NUM_19]));
                valores.put("vMinCash", new Double(values[Consts.NUM_19]));
                valores.put(Consts.ID_PR_KEY, new Integer(values[Consts.NUM_20]));
            }
        }
        else if (opcion == OP_NFREC) {
            valores.put("id", values[Consts.NUM_0]);
            valores.put("descripcion", convAcentos(values[Consts.NUM_1]));
            valores.put(Consts.COMPRA_KEY, new Double(values[Consts.NUM_2]));
            valores.put(Consts.VENTA_KEY, new Double(values[Consts.NUM_3]));
            valores.put(Consts.FACTOR_DIVISA_KEY, new Double(values[Consts.NUM_4]));
            valores.put(Consts.PR_MID_SPOT_KEY, new Double(values[Consts.NUM_5]));
            valores.put(Consts.PR_SPOT_KEY, new Double(values[Consts.NUM_6]));
            valores.put(Consts.ID_FACTOR_DIVISA_KEY, new Integer(values[Consts.NUM_7]));
            valores.put(Consts.ID_PR_KEY, new Integer(values[Consts.NUM_8]));
        }
        else if (opcion == OP_MET) {
            valores.put("id", values[Consts.NUM_0]);
            valores.put("descripcion", convAcentos(values[Consts.NUM_1]));
            valores.put(Consts.C_CASH_KEY, new Double(values[Consts.NUM_2]));
            valores.put(Consts.V_CASH_KEY, new Double(values[Consts.NUM_3]));
            valores.put(Consts.C_TOM_KEY, new Double(values[Consts.NUM_4]));
            valores.put(Consts.V_TOM_KEY, new Double(values[Consts.NUM_5]));
            valores.put(Consts.C_SPOT_KEY, new Double(values[Consts.NUM_6]));
            valores.put(Consts.V_SPOT_KEY, new Double(values[Consts.NUM_7]));
            valores.put(Consts.FACTOR_DIVISA_KEY, new Double(values[Consts.NUM_8]));
            valores.put(Consts.PR_MID_SPOT_KEY, new Double(values[Consts.NUM_9]));
            valores.put(Consts.PR_SPOT_KEY, new Double(values[Consts.NUM_10]));
            valores.put(Consts.ID_FACTOR_DIVISA_KEY, new Integer(values[Consts.NUM_11]));
            valores.put(Consts.ID_PR_KEY, new Integer(values[Consts.NUM_12]));
        }
        return valores;
    }

    /**
     * Obtiene de la p&aacute;gia de Tapestry los valores, los datos de l tipo de
     * cambio con base en la opcion que define el usuario (divisa o metal).
     *
     * @param opcion La define si se selecciono una divisa o metales.
     * @return List Una lista de objetos Map, cada mapa representa un rengl&oacuet;n del pizarr&oaite;n
     */
    public List getDatos(int opcion) {
        List resultados = null;
        try {
            // Esta condicion se necesita cuando se quiere correr el applet desde el IDE:
            if (SERV_IDE.equals(_serv)) {
                _serv = Consts.LOCALHOST_STR;
            }
            String servletURL = prot + "://" + _serv +
                    "/sica/app?service=external/DatosPizarron&sp=" + opcion +
                    (opcion == 0 ? "&sp" + "=" + _idTipoPizarron : "") + "&sp=" + "S" +
                    new Date().getTime() + "&s" + "p=S" + _ticket + "&sp=S" + _soloCash;
            _logger.info(servletURL);
            URL url = new URL(servletURL);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty(Consts.PRAGMA_STR, Consts.NO_CACHE_STR);
            conn.connect();
            BufferedReader buf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            try {
                String str;
                resultados = new ArrayList();
                int i = 0;
                while ((str = buf.readLine()) != null) {
                    try {
                        String values[] = str.split(Consts.PIPE_REGEXP_STR);
                        resultados.add(getValores(opcion, i, values));
                    }
                    catch (Exception e) {
                        if (_logger.isLoggable(Level.WARNING)) {
                            _logger.log(Level.WARNING, "No se pudo parsear la linea  " + str, e);
                        }
                    }
                    i++;
                }
            }
            catch (IOException e) {
                if (_logger.isLoggable(Level.SEVERE)) {
                    _logger.log(Level.SEVERE, Consts.ERROR_STR, e);
                }
            }
            finally {
                try {
                    buf.close();
                }
                catch (IOException e) {
                    _logger.log(Level.WARNING, "Error al cerrar el buffer", e);
                }
            }
        }
        catch (IOException e) {
            if (_logger.isLoggable(Level.WARNING)) {
                _logger.log(Level.WARNING, Consts.ERROR_STR, e);
            }
        }
        return resultados;
    }

    /**
     * Configura la tabla principal del pizarr&oacute;n.
     */
    private void confTab() {
        _tab.setModel(createTableModel());
        _tab.setColumnSelectionAllowed(false);
        _tab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _tab.setCellSelectionEnabled(true);
        _tab.setDefaultRenderer(Object.class, new CRenderer());
        _tab.getColumnModel().getColumn(0).setMinWidth(Consts.NUM_125);
        _tab.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    dobleClick(_tab, 0);
                }
            }
        });
    }

    /**
     * Configura la tabla de divisas no frecuentes.
     */
    private void confTabNF() {
        _tabNF = new JTable(new TabModNF(new ArrayList()));
        _tabNF.setPreferredScrollableViewportSize(new Dimension(Consts.NUM_500, Consts.NUM_240));
        _tabNF.setColumnSelectionAllowed(false);
        _tabNF.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _tabNF.setCellSelectionEnabled(true);
        ((DefaultTableCellRenderer) _tabNF.getDefaultRenderer(Object.class)).
                setHorizontalAlignment(JLabel.CENTER);
        _tabNF.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    dobleClick(_tabNF, 1);
                }
            }
        });
    }

    /**
     * Configura la tabla de metales amonedados.
     */
    private void confTabMA() {
        _tabMA = new JTable(new TabModNF(new ArrayList()));
        _tabMA.setPreferredScrollableViewportSize(new Dimension(Consts.NUM_500, Consts.NUM_240));
        _tabMA.setColumnSelectionAllowed(false);
        _tabMA.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _tabMA.setCellSelectionEnabled(true);
        ((DefaultTableCellRenderer) _tabMA.getDefaultRenderer(Object.class)).
                setHorizontalAlignment(JLabel.CENTER);
        _tabMA.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    dobleClick(_tabMA, 2);
                }
            }
        });
    }

    /**
     * Inicializa los objetos de la interfaz del usuario.
     *
     * @param divs La lista de idDivisas.
     */
    private void confGui(List divs) {
        setLayout(new BorderLayout(2, 2));
        JPanel controles = new JPanel(new BorderLayout());
        JPanel botonera = new JPanel(new GridBagLayout());
        add(controles, BorderLayout.WEST);
        confTab();
        confTabNF();
        confTabMA();
        add(new JScrollPane(_tab, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        JButton bNF = new JButton();
        JButton bMA = new JButton();
        jChBoxMN = new JCheckBox();
        jChBoxMN.setText("Reglas de Neteo");
        try {
            bNF.setIcon(new ImageIcon(getClass().getResource("world.gif")));
            bMA.setIcon(new ImageIcon(getClass().getResource("metales_amonedados.gif")));
        }
        catch (NullPointerException e) {
            _logger.warning(e.getMessage());
        }
        bNF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showNF();
            }
        });
        bMA.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMA();
            }
        });
        cb = new JComboBox(divs.toArray());
        cb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _idDivisa = (String) ((JComboBox) e.getSource()).getSelectedItem();
                _tab.setModel(createTableModel());
                _tab.getColumnModel().getColumn(0).setMinWidth(Consts.NUM_200);
            }
        });
        cb.setRenderer(new BanderaListCellRenderer());
        JToolBar tb = new JToolBar();
        _bRef.setToolTipText("Refrescar Pizarr\u00f3n");
        tb.add(_bRef);
        bNF.setToolTipText("Ver divisas no frecuentes");
        tb.add(bNF);
        bMA.setToolTipText("Ver metales amonedados");
        tb.add(bMA);
        controles.add(tb, BorderLayout.NORTH);
        Insets ins = new Insets(1, 1, 1, 1);
        botonera.add(new JLabel("Divisa: "), new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, ins, 1, 1));
        botonera.add(cb, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, ins, 1, 1));
        botonera.add(jChBoxMN, new GridBagConstraints(1, 1, 0, 1, 0, 1,
                GridBagConstraints.SOUTH, GridBagConstraints.SOUTH, ins, 0, 1));
        controles.add(botonera, BorderLayout.CENTER);
         _lUltAct.setBorder(BorderFactory.createLoweredBevelBorder());
        controles.add(_lUltAct, BorderLayout.SOUTH);
        _bRef.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizar();
            }
        });
        actualizarFecha(new Date());
    }

    /**
     * Muestra el panel de divisas no frecuentes.
     */
    private void showNF() {
        _dNFMA = new JDialog((Frame) null, "Divisas no frecuentes", true);
        Dimension scrn = getToolkit().getScreenSize();
        try {
            _tabNF.setModel(new TabModNF(getDatos(OP_NFREC)));
        }
        catch (Exception e1) {
            _tabNF.setModel(new TabModNF(new ArrayList()));
        }
        _dNFMA.getContentPane().setLayout(new BorderLayout(Consts.NUM_5, Consts.NUM_5));
        _dNFMA.getContentPane().add(new JScrollPane(_tabNF,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Producto:"));
        panel.add(_cbProds);
        _dNFMA.getContentPane().add(panel, BorderLayout.NORTH);
        _dNFMA.pack();
        _dNFMA.setLocation((scrn.width - _dNFMA.getWidth()) / 2,
                (scrn.height - _dNFMA.getHeight()) / 2);
        _dNFMA.show();
    }

    /**
     * Muestra el panel mec&aacute;nica de neteos.
     */
    private void showMN() {
        _pMN = new JDialog((Frame) null, "Mec\u00e1nica de Neteos " + _idDivisa, true);
        Dimension scrn = getToolkit().getScreenSize();
        panelReglas = new JPanel(new GridLayout(0, 1));
        inicializaPanelMN();
        if (!capturaDeal) {
        	btnAceptar.setEnabled(false);
        	tfCpra.setEditable(false);
        	tfVta.setEditable(false);
            tfMtoCpra.setEditable(false);
            tfMtoVta.setEditable(false);
        }
        anadirListeners();
        boolean existenReglas = false;
    	for (Iterator it = _reglasNeteos.iterator(); it.hasNext();) {
    		valoresDeLasReglas = Consts.STR_VACIO;
    		Map reglasMap = (HashMap) it.next();
			String idDivisa = (String) reglasMap.get(Consts.ID_DIVISA_KEY);
			if (_idDivisa.equals(idDivisa)) {
                String regla = Consts.STR_VACIO;
                if (isVenta) {
                    if ((reglasMap.get(Consts.CLAVE_FORMA_LIQUIDACION_VENTA_KEY)).
                            equals(idProdSel)) {
                        existenReglas = true;
                        regla = "Compra: " + reglasMap.get(Consts.CLAVE_FORMA_LIQUIDACION_COMPRA_KEY
                        ) + ", Venta: " + reglasMap.get(Consts.
                                CLAVE_FORMA_LIQUIDACION_VENTA_KEY) + ", Base: ";
                        if (reglasMap.get(Consts.BASE_REGLA).equals(Consts.TCM)) {
                            regla = regla + Consts.TIPO_DE_CAMBIO;
                            valoresDeLasReglas = ((BigDecimal) reglasMap.get(Consts.
                            		DIF_ANTES_DEL_HORARIO)).toString() + ";" +
                            		((BigDecimal) reglasMap.get(Consts.DIF_DESPUES_DEL_HORARIO)).
                            		toString();
                        }
                        else {
                            regla = regla + Consts.IMPORTE_O_MONTO;
                            valoresDeLasReglas = reglasMap.get(Consts.MONTO_ANTES_DEL_HORARIO) +
                                    "-" + reglasMap.get(Consts.MONTO_DESPUES_DEL_HORARIO);
                        }
						llenarReglas(regla);
                    }
                }
                else {
                    if ((reglasMap.get(Consts.CLAVE_FORMA_LIQUIDACION_COMPRA_KEY)).
                            equals(idProdSel)) {
                        existenReglas = true;
                        regla = "Compra: " + reglasMap.get(Consts.CLAVE_FORMA_LIQUIDACION_COMPRA_KEY
                        ) + ", Venta: " + reglasMap.get(Consts.
                                CLAVE_FORMA_LIQUIDACION_VENTA_KEY) + ", Base: ";
                        if (reglasMap.get(Consts.BASE_REGLA).equals(Consts.TCM)) {
                            regla = regla + Consts.TIPO_DE_CAMBIO;
                            valoresDeLasReglas = ((BigDecimal) reglasMap.get(Consts.
                            		DIF_ANTES_DEL_HORARIO)).toString() + ";" +
                            		((BigDecimal) reglasMap.get(Consts.DIF_DESPUES_DEL_HORARIO)).
                            		toString();
                        }
                        else {
                            regla = regla + Consts.IMPORTE_O_MONTO;
                            valoresDeLasReglas = (String) reglasMap.get(Consts.
                                    MONTO_ANTES_DEL_HORARIO) + "-" + (String)
                                    reglasMap.get(Consts.MONTO_DESPUES_DEL_HORARIO);
                        }
                        llenarReglas(regla);
					}
				}
			}
        }
    	if (!existenReglas) {
    		avisar("No existen reglas de neteo para la divisa seleccionada.");
    	}
    	else {
    		btnAceptar.setEnabled(false);
    		_pMN.getContentPane().setLayout(new BorderLayout());
            Insets ins = new Insets(2, 2, 2, 2);
            JPanel panelNorte = new JPanel();
            JPanel panelCentro = new JPanel();
            JPanel panelSur = new JPanel();
            panelSur.setLayout(new FlowLayout());
            panelSur.add(btnAceptar);
            panelSur.add(btnCancelar);
            panelCentro.setLayout(new GridBagLayout());
            panelCentro.add(new JLabel(""), new GridBagConstraints(0, 0, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 100, 0));
            panelCentro.add(lTCM, new GridBagConstraints(1, 0, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 100, 0));
            panelCentro.add(lTCC, new GridBagConstraints(2, 0, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 100, 0));
            panelCentro.add(lMto, new GridBagConstraints(3, 0, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 100, 0));
            panelCentro.add(lImpte, new GridBagConstraints(4, 0, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 100, 0));
            panelCentro.add(lCpra, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
            panelCentro.add(lCpraTcm, new GridBagConstraints(1, 1, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
            panelCentro.add(tfCpra, new GridBagConstraints(2, 1, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
            panelCentro.add(tfMtoCpra, new GridBagConstraints(3, 1, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
            panelCentro.add(lImpteTotCpra, new GridBagConstraints(4, 1, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
            panelCentro.add(lVta, new GridBagConstraints(0, 2, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
            panelCentro.add(lVtaTcm, new GridBagConstraints(1, 2, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
            panelCentro.add(tfVta, new GridBagConstraints(2, 2, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
            panelCentro.add(tfMtoVta, new GridBagConstraints(3, 2, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
            panelCentro.add(lImpteTotVta, new GridBagConstraints(4, 2, 1, 1, 0, 0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, ins, 0, 0));
            panelNorte.setLayout(new GridLayout());
            panelNorte.add(panelReglas);
            panelNorte.setBorder(tituloMN);
            _pMN.getContentPane().add(panelNorte, BorderLayout.NORTH);
            _pMN.getContentPane().add(panelCentro, BorderLayout.CENTER);
            _pMN.getContentPane().add(panelSur, BorderLayout.SOUTH);
            _pMN.pack();
            _pMN.setLocation((scrn.width - _pMN.getWidth()) / 2,
                    (scrn.height - _pMN.getHeight()) / 2);
            iniOp = getFechaServ();
            _pMN.setResizable(false);
            _pMN.show();
        }
    }

    /**
     * Inicializa todos los JLabels, JTextFields, GroupRadios y Botones necesarios para mostrar el
     * panel de mec&aacute;nica de neteos.
     * 
     */
    private void inicializaPanelMN() {
    	tituloMN = new TitledBorder("Divisa: " + _idDivisa);
    	tituloMN.setTitleFont(new Font("Tahoma", Font.BOLD, 11));
    	lTCM = new JLabel("T.C. Mesa");
        lTCM.setFont(new Font("Tahoma", Font.BOLD, 11));
        lTCC = new JLabel("T.C. Cliente");
        lTCC.setFont(new Font("Tahoma", Font.BOLD, 11));
        lMto = new JLabel("Cantidad");
        lMto.setFont(new Font("Tahoma", Font.BOLD, 11));
        lImpte = new JLabel("Importe M.N.");
        lImpte.setFont(new Font("Tahoma", Font.BOLD, 11));
        lCpra = new JLabel("Compra:");
        lCpra.setFont(new Font("Tahoma", Font.BOLD, 11));
        lVta = new JLabel("Venta:");
        lVta.setFont(new Font("Tahoma", Font.BOLD, 11));
        lCpraTcm = new JLabel();
        lVtaTcm = new JLabel();
        valorLCpra = Consts.NUMD_0;
        valorLVta = Consts.NUMD_0;
        lCpraTcm.setText(FMT_TC.format(valorLCpra));
        lVtaTcm.setText(FMT_TC.format(valorLVta));
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        tfCpra = new JTextField();
        tfCpra.setText(FMT_TC.format(valorLCpra));
        tfVta = new JTextField();
        tfVta.setText(FMT_TC.format(valorLVta));
        tfMtoCpra = new JTextField();
        tfMtoCpra.setText(String.valueOf(Consts.NUMD_0));
        tfMtoCpra.setEnabled(false);
        lImpteTotCpra = new JLabel();
        lImpteTotCpra.setText(String.valueOf(Consts.NUMD_0));
        lImpteTotVta = new JLabel();
        lImpteTotVta.setText(String.valueOf(Consts.NUMD_0));
        tfMtoVta = new JTextField();
        tfMtoVta.setText(String.valueOf(Consts.NUMD_0));
        tfMtoVta.setEnabled(false);
        reglaRadioSel = Consts.STR_VACIO;
        groupRadios = new ButtonGroup();
    }
    
    /**
     * Agrega listeners de FocusListener, KeyListener y ActionListener a los campos de texto donde 
     * ingresan montos e importes para mec&aacute;nica de neteos y el bot&oacute;n de aceptar 
     * respectivamente.
     */
    private void anadirListeners() {
    	tfMtoCpra.addKeyListener(new Comas());
    	tfMtoVta.addKeyListener(new Comas());
    	tfMtoCpra.addKeyListener(new MyKeyListener(Consts.TF_MTO_CPRA));
        tfMtoVta.addKeyListener(new MyKeyListener(Consts.TF_MTO_VTA));
        tfCpra.addKeyListener(new MyKeyListener(Consts.TF_CPRA));
        tfVta.addKeyListener(new MyKeyListener(Consts.TF_VTA));
        tfMtoCpra.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent evt) {
                if (tfMtoCpra.getText().trim().equals(Consts.STR_VACIO) || tfMtoCpra.getText().
                        trim() == null) {
                    tfMtoCpra.setText(String.valueOf(Consts.NUMD_0));
                }
            }

            public void focusGained(FocusEvent evt) {
                ((JTextField) evt.getSource()).selectAll();
            }
        });
        tfMtoVta.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent evt) {
                if (tfMtoVta.getText().trim().equals(Consts.STR_VACIO) || tfMtoVta.getText().
                        trim() == null) {
                    tfMtoVta.setText(String.valueOf(Consts.NUMD_0));
                }
            }

            public void focusGained(FocusEvent evt) {
                ((JTextField) evt.getSource()).selectAll();
            }
        });
        tfCpra.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent evt) {
                if (tfCpra.getText().trim().equals(Consts.STR_VACIO) || tfCpra.getText().
                        trim() == null) {
                    tfCpra.setText(String.valueOf(Consts.NUMD_0));
                }
            }
        	public void focusGained(FocusEvent evt) {
        		((JTextField) evt.getSource()).selectAll();
        	}
        });
        tfVta.addFocusListener(new FocusListener() {
        	public void focusLost(FocusEvent evt) {
        		if (tfVta.getText().trim().equals(Consts.STR_VACIO) || tfVta.getText().
        				trim() == null) {
        			tfVta.setText(String.valueOf(Consts.NUMD_0));
				}
        	}
        	public void focusGained(FocusEvent evt) {
        		((JTextField) evt.getSource()).selectAll();
        	}
        });
        btnAceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviarOperarNeteos();
            }
        });
        btnCancelar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		_pMN.hide();
        	}
        });
    }
    
    /**
     * Verifica que toda la informaci&oacute;n capturada en neteo sea correctam y de ser as&iacute;
     * ser&aacute; enviada para su captura.
     */
    private void enviarOperarNeteos() {
    	String[] claveProductos = reglaRadioSel.split("-");
        String idProdCpra = claveProductos[Consts.NUM_0];
        String idProdVta = claveProductos[Consts.NUM_1];
        double cantCpr;
        double cantVta;
        try {
            cantCpr = validarNum(tfMtoCpra.getText(), "cantidad");
            cantVta = validarNum(tfMtoVta.getText(), "cantidad");
            validarNum(tfCpra.getText(), "tipo de cambio cliente");
            validarNum(tfVta.getText(), "tipo de cambio cliente");
            if (baseReglaSel.equals(Consts.IMPORTE_O_MONTO)) {
            	if ((!validarMinTccNeteo(lCpraTcm.getText(), tfCpra.getText(), false)) ||
            			(!validarMinTccNeteo(lVtaTcm.getText(), tfVta.getText(), true))) {
            		return;
            	}
            	if (validarPorcentajeMontos(cantCpr, cantVta)) {
            		return;
            	}
            	if (isRangoLimHrNeteoPermitido() && ((cantVta + montoAntesDelHorario) > cantCpr)) {
            		avisar("El importe de la regla ha sido excedido, verifique que exista una " +
            				"diferencia m\u00ednima en el monto por: " +
            				FMT_MON.format(montoAntesDelHorario) + ".");
            		return;
            	}
            	else if (!isRangoLimHrNeteoPermitido() &&
            			((cantVta + montoDespuesDelHorario) > cantCpr)){
            		avisar("El importe de la regla ha sido excedido, verifique que exista una " +
            				"diferencia m\u00ednima en el monto por: " +
            				FMT_MON.format(montoDespuesDelHorario) + ".");
            		return;
            	}
            }
            double fd = 1.0;
            if (validarCan(lCpraTcm.getText(), tfCpra.getText(), false) &&
                    vMinMult(idProdCpra, true, _idDivisa, cantCpr) &&
                    validarTCLimDesv(cantCpr * fd, tfCpra.getText(), lCpraTcm.getText()) &&
                    validarCan(lVtaTcm.getText(), tfVta.getText(), true) &&
                    vMinMult(idProdVta, false, _idDivisa, cantVta) &&
                    validarTCLimDesv(cantVta * fd, tfVta.getText(), lVtaTcm.getText())) {
            	_pMN.hide();
            	int res = confOpMN(true, fechaValSel, cantCpr, Double.valueOf(lCpraTcm.
            		   getText()).doubleValue(), Double.valueOf(tfCpra.getText()).
            		   doubleValue(), idProdCpra, _idDivisa, false, false, fechaValSel,
            		   cantVta, Double.valueOf(lVtaTcm.getText()).doubleValue(), Double.
            		   valueOf(tfVta.getText()).doubleValue(), idProdVta, _idDivisa, false);
            	if (res == JOptionPane.NO_OPTION) {
            	        _pMN.show();
            	 }
            	 else if (res == JOptionPane.YES_OPTION) {
            		_logger.info("Tiempo: " + (getFechaServ().getTime() - iniOp.getTime()) /
                             Consts.NUM_1000);
                     if ((getFechaServ().getTime() - iniOp.getTime()) /
                             Consts.NUM_1000 > _tout) {
                    	avisar("Han transcurrido los " + _tout + " segs. para aceptar la " +
                             "operaci\u00f3n. " + "Por favor seleccione de nuevo el tipo " +
                             "de cambio del pizarr\u00f3n.");
                         _pMN.hide();
                     }
                     else {
                         _pMN.hide();
                         _ctrl.operarNeteos(true, fechaValSel, idProdCpra, _idDivisa,
                               Double.valueOf(lCpraTcm.getText()).doubleValue(), cantCpr,
                               Double.valueOf(tfCpra.getText()).doubleValue(),
                               prMidSpotCpra.doubleValue(),
                               prSpotCpra.doubleValue(),
                               factorDivisaCpra.doubleValue(),
                               Integer.valueOf(idPrCpra).intValue(),
                     		   Integer.valueOf(idFactorDivisaCpra).intValue(),
                               Integer.valueOf(idSpreadCpra).intValue(), false, fechaValSel,
                               idProdVta, _idDivisa, Double.valueOf(lVtaTcm.getText()).doubleValue(),
                               cantVta,
                               Double.valueOf(tfVta.getText()).doubleValue(), 
                               prMidSpotVta.doubleValue(),
                               prSpotVta.doubleValue(),
                               factorDivisaVta.doubleValue(),
                               Integer.valueOf(idPrVta).intValue(),
                               Integer.valueOf(idFactorDivisaVta).intValue(),
                               Integer.valueOf(idSpreadVta).intValue());
                     }
            	 }
            	 else {
            		 _pMN.hide();
            		 return;
            	 }
            }
        }
        catch (ParseException ex) {
            avisar("El monto no es v\u00e1lido.");
        }
        catch (Exception ex) {
            avisar(ex.getMessage());
        }
    }
    
    /**
     * Inicializa los RadioButtons con las reglas asociadas a la divisa seleccionada en ese momento.
     * 
     * @param regla La regla.
     */
    private void llenarReglas(String regla) {
        radioBtnsReglas = new JRadioButton(regla, false);
        radioBtnsReglas.setFont(new Font("Tahoma", Font.BOLD, 11));
        radioBtnsReglas.setActionCommand(valoresDeLasReglas);
        radioBtnsReglas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String valMtosYDifs = ((JRadioButton) e.getSource()).getActionCommand().trim();
                tfMtoVta.setEnabled(true);
                tfMtoCpra.setEnabled(true);
                inicializarMtosEImptes(Consts.STR_VACIO);
                if (capturaDeal) {
                    btnAceptar.setEnabled(true);
                }
                String reglaSel = ((JRadioButton) e.getSource()).getText().trim();
                String claveFormaLiquidacionCompraSel = reglaSel.substring(8, 14).trim();
                String claveFormaLiquidacionVentaSel = reglaSel.substring(23, 29).trim();
                baseReglaSel = reglaSel.substring(37).trim();
                reglaRadioSel = claveFormaLiquidacionCompraSel + "-" +
                        claveFormaLiquidacionVentaSel;
                diferencial = new BigDecimal(Consts.NUMD_0);
                montoAntesDelHorario = Consts.NUMD_0;
                montoDespuesDelHorario = Consts.NUMD_0;
                //OBTIENE EL TIPO DE CAMBIO DEL PRODUCTO CONTRARIO AL ANCLA
                if (baseReglaSel.trim().equals(Consts.IMPORTE_O_MONTO)) {
                	obtenerTipoCambioContrarioAncla(claveFormaLiquidacionCompraSel,
                		claveFormaLiquidacionVentaSel);
                }
                //METODO SUMAR O RESTAR DIFERENCIAL
                sumarRestarDiferencial(valMtosYDifs);
                //OBTIENE LOS MONTOS ANTES Y DESPUES DEL HORARIO
                obtenerMontoAntesYDespuesDelHorario(valMtosYDifs);
                //IDS
                findIdPrIdFactorDivisaIdSpread(claveFormaLiquidacionCompraSel,
                        claveFormaLiquidacionVentaSel);
                tfCpra.setText(FMT_TC.format(valorLCpra));
                tfVta.setText(FMT_TC.format(valorLVta));
                if (baseReglaSel.trim().equals(Consts.TIPO_DE_CAMBIO)) {
                	lCpraTcm.setText(FMT_TC.format(valorLCpra));
                	lVtaTcm.setText(FMT_TC.format(valorLVta));
                }
            }
        });
        groupRadios.add(radioBtnsReglas);
        panelReglas.add(radioBtnsReglas);
    }

    /**
     * Calcula el importe total en moneda nacional y el tipo de cambio del cliente si la regla
	 * es por tipo de cambio, para mostrar en la captura de mec&aacute;nica 
     * de neteos.
     * 
     * @param	mtoCpra   Monto de la Compra
     * @param	mtoVta    Monto de la Venta
     * @param	tccCpra   Tipo de cambio del cliente a la Compra
     * @param	tccVta    Tipo de cambio del cliente a la Venta
     * @param   cajaTexto Identifica el textField del KeyListener que se esta tecleando.
     */
    private void calcularImporteMn(String mtoCpra, String mtoVta, String tccCpra, String tccVta,
    		String cajaTexto) {
    	double impteTotCpra;
    	double impteTotVta;
    	double cantCpra;
    	double cantVta;
    	double tcCpra;
    	double tcVta;
    	try {
    		cantCpra = validarNum(mtoCpra, "importe de la compra");
    		tcCpra = validarNum(tccCpra, "tipo de cambio cliente");
    		cantVta = validarNum(mtoVta, "importe de la venta");
    		tcVta = validarNum(tccVta, "tipo de cambio cliente");
    		if (baseReglaSel.equals(Consts.IMPORTE_O_MONTO)) {
    			if (cajaTexto.equals(Consts.TF_MTO_CPRA)) {
    				if (!isVenta) {
    					impteTotCpra = tcCpra * cantCpra;
    					if (!(cantVta <= Consts.NUMD_0)) {
    						tcVta = impteTotCpra / cantVta;
    					}
    					tfVta.setText(FMT_TC.format(tcVta));
    					impteTotVta = tcVta * cantVta;
    					lImpteTotCpra.setText(FMT_MON.format(impteTotCpra));
    					lImpteTotVta.setText(FMT_MON.format(impteTotVta));
    				}
    				else {
    					impteTotVta = tcVta * cantVta;
    					if (!(cantCpra <= Consts.NUMD_0)) {
    						tcCpra = impteTotVta / cantCpra;
    					}
    					tfCpra.setText(FMT_TC.format(tcCpra));
    					impteTotCpra = tcCpra * cantCpra;
    					lImpteTotCpra.setText(FMT_MON.format(impteTotCpra));
    					lImpteTotVta.setText(FMT_MON.format(impteTotVta));
    				}
    			}
    			else {
    				impteTotVta = tcVta * cantVta;
    				if (isVenta) {
    					if (!(cantCpra <= Consts.NUMD_0)) {
    						tcCpra = impteTotVta / cantCpra;
    					}
    					tfCpra.setText(FMT_TC.format(tcCpra));
    					impteTotCpra = tcCpra * cantCpra;
    					lImpteTotCpra.setText(FMT_MON.format(impteTotCpra));
    					lImpteTotVta.setText(FMT_MON.format(impteTotVta));
    				}
    				else {
    					impteTotCpra = tcCpra * cantCpra;
    					if (!(cantVta <= Consts.NUMD_0)) {
    						tcVta = impteTotCpra / cantVta;
    					}
    					tfVta.setText(FMT_TC.format(tcVta));
    					impteTotVta = tcVta * cantVta;
    					lImpteTotCpra.setText(FMT_MON.format(impteTotCpra));
    					lImpteTotVta.setText(FMT_MON.format(impteTotVta));
    				}
    			}
    		}
    		else {
    			impteTotCpra = tcCpra * cantCpra;
    			impteTotVta = tcVta * cantVta;
    			lImpteTotCpra.setText(FMT_MON.format(impteTotCpra));
    			lImpteTotVta.setText(FMT_MON.format(impteTotVta));
    		}
    	}
    	catch (Exception ex) {
    		avisar("El monto no es v\u00e1lido.");
    	}
    }
    
    /**
     * Valida que los montos entre si no excedan el cinco porciento.
     * 
     * @param cantCpra La cantidad ingresada en la compra.
     * @param cantVta La cantidad ingresada en la venta.
     * @return boolean
     */
    private boolean validarPorcentajeMontos(double cantCpra, double cantVta) {
    	boolean excedeLimite = false;
    	double porcentaje = 0.05;
    	double montoMax = 0.0;
    	if (isVenta) {
    		montoMax = cantVta * porcentaje;
    		if ((cantCpra > (cantVta + montoMax))) {
    			avisar("La cantidad del monto en la compra excede el l\u00edmite " +
				"permitido de " + (FMT_MON.format(cantVta + montoMax)) + ".");
    			excedeLimite = true;
    		}
    	}
    	else {
    		montoMax = cantCpra * porcentaje;
    		if (cantVta < (cantCpra- montoMax)) {
    			avisar("La cantidad del monto en la venta excede el l\u00edmite " +
				"permitido de " + (FMT_MON.format(cantCpra - montoMax)) + "."); 
    			excedeLimite = true;
    		}
    	}
    	return excedeLimite;
    }
    
    /**
     * Se encarga de verificar que los campos de texto en mec&aacute;nica de neteos, no sean mayor a
     * 999,999,999.99 y que la informaci&oacute;n capturada sea num&eacute;rica.
     * 
     * @param campoTexto    Nombre del Campo de Texto a verificar.
     * @param venta         Nos indica si es venta o compra.
     * @param reglaBase		Nos indica cual es la regla base seleccionada de Mec. Net.
     */
    private void verificadorCantidadMaximaMonto(String campoTexto, boolean venta, 
    		String reglaBase) {
    	if ((!venta && reglaBase.equals(Consts.IMPORTE_O_MONTO)) || (!venta && reglaBase.
    			equals(Consts.TIPO_DE_CAMBIO))) {
    		try {
    			  if (Double.parseDouble(campoTexto.replaceAll("\\,", "")) >
    						Consts.NUMD_999999999P99) {
    				  avisar("El monto de la compra debe ser menor a 999,999,999.99");
    				  if (reglaBase.equals(Consts.IMPORTE_O_MONTO)) {
    					inicializarMtosEImptes(Consts.STR_VACIO);
    				  }
    				  else {
    					tfMtoCpra.setText(String.valueOf(Consts.NUMD_0));
    					lImpteTotCpra.setText(String.valueOf(Consts.NUMD_0));
    				  }
    				  return;
    			  }
    			}
    		  catch (Exception ex) {
    			  avisar("El monto " + campoTexto + " de la compra no es " +
    					  "v\u00e1lido.");
    			  if (reglaBase.equals(Consts.IMPORTE_O_MONTO)) {
    				inicializarMtosEImptes(Consts.STR_VACIO);
    			  }
    			  else {
    				tfMtoCpra.setText(String.valueOf(Consts.NUMD_0));
    			    lImpteTotCpra.setText(String.valueOf(Consts.NUMD_0));
    			  }
    			  return;
    		  }
    	}
        else if ((venta && reglaBase.equals(Consts.IMPORTE_O_MONTO)) || (venta && reglaBase.
                equals(Consts.TIPO_DE_CAMBIO))) {
            try {
                if (Double.parseDouble(campoTexto.replaceAll("\\,", "")) >
                        Consts.NUMD_999999999P99) {
                    avisar("El monto de la venta debe ser menor a 999,999,999.99");
                    if (reglaBase.equals(Consts.IMPORTE_O_MONTO)) {
                        inicializarMtosEImptes(Consts.STR_VACIO);
                    }
                    else {
                        tfMtoVta.setText(String.valueOf(Consts.NUMD_0));
                        lImpteTotVta.setText(String.valueOf(Consts.NUMD_0));
                    }
                    return;
                }
            }
            catch (Exception ex) {
                avisar("El monto " + campoTexto + " de la venta no es " +
                        "v\u00e1lido.");
                if (reglaBase.equals(Consts.IMPORTE_O_MONTO)) {
                    inicializarMtosEImptes(Consts.STR_VACIO);
                }
                else {
                    tfMtoVta.setText(String.valueOf(Consts.NUMD_0));
                    lImpteTotVta.setText(String.valueOf(Consts.NUMD_0));
                }
                return;
            }
        }
    }
    
    /**
     * Se encarga de verificar que los campos de tipo de cambio en
     * mec&aacute;nica de neteos, no contengan doble punto o alg&uacute;n caracter no v&aacute;lido.
     *
     * @param tipoDeCambio
     * @param venta
     */
    private void verificadorTiposDeCambio(String tipoDeCambio, boolean venta) {
        if (!venta) {
            try {
                Double.parseDouble(tipoDeCambio);
            }
            catch (Exception ex) {
                avisar("El tipo de cambio " + tipoDeCambio + " de la compra no es " +
                        "v\u00e1lido.");
                tfCpra.setText(String.valueOf(Consts.NUMD_0));
            }
        }
        else {
            try {
                Double.parseDouble(tipoDeCambio);
            }
            catch (Exception ex) {
                avisar("El tipo de cambio " + tipoDeCambio + " de la venta no es " +
                        "v\u00e1lido.");
                tfVta.setText(String.valueOf(Consts.NUMD_0));
            }
        }
    }
    
    /**
     * Inicializa en 0.0 los campos de monto y los labels con el importe total en moneda nacional
     * calculados anteriormente.
     * 
     * @param  campoTextField Indica el campo en el cual se encuentra situado el usuario.
     */
    private void inicializarMtosEImptes(String campoTextField) {
        if (campoTextField.equals(Consts.TF_MTO_CPRA)) {
            tfMtoVta.setText(String.valueOf(Consts.NUMD_0));
        }
        else if (campoTextField.equals(Consts.TF_MTO_VTA)) {
            tfMtoCpra.setText(String.valueOf(Consts.NUMD_0));
        }
        else {
            tfMtoVta.setText(String.valueOf(Consts.NUMD_0));
            tfMtoCpra.setText(String.valueOf(Consts.NUMD_0));
        }
        lImpteTotVta.setText(String.valueOf(Consts.NUMD_0));
        lImpteTotCpra.setText(String.valueOf(Consts.NUMD_0));
    }

    /**
     * Sumar o restar los montos definidos antes o despu&eacute;s del horario permitido.
     *
     * @param venta  Indica si viene del textField monto de la venta o monto de la compra.
     */
    private void sumarRestarMontosPorHorario(boolean venta) {
        if (venta) {
            if (isRangoLimHrNeteoPermitido()) {
                double cantVta = Consts.NUMD_0;
                try {
                    cantVta = validarNum(tfMtoVta.getText(), "importe de la venta");
                    cantVta = cantVta + montoAntesDelHorario;
                }
                catch (Exception ex) {
                    avisar("El campo importe de la venta debe ser num\u00e9rico.");
                }
                tfMtoCpra.setText(FMT_MON.format(cantVta));
            }
            else {
                double cantVta = Consts.NUMD_0;
                try {
                    cantVta = validarNum(tfMtoVta.getText(), "importe de la venta");
                    cantVta = cantVta + montoDespuesDelHorario;
                }
                catch (Exception ex) {
                    avisar("El campo importe de la venta debe ser num\u00e9rico.");
                }
                tfMtoCpra.setText(FMT_MON.format(cantVta));
            }
        }
        else {
            if (isRangoLimHrNeteoPermitido()) {
                double cantCpra = Consts.NUMD_0;
                try {
                    cantCpra = validarNum(tfMtoCpra.getText(), "importe de la compra");
                    cantCpra = cantCpra - montoAntesDelHorario;
                }
                catch (Exception ex) {
                    avisar("El campo importe de la compra debe ser num\u00e9rico.");
                }
                tfMtoVta.setText(FMT_MON.format(cantCpra));
            }
            else {
                double cantCpra = Consts.NUMD_0;
                try {
                    cantCpra = validarNum(tfMtoCpra.getText(), "importe de la compra");
                    cantCpra = cantCpra - montoDespuesDelHorario;
                }
                catch (Exception ex) {
                    avisar("El campo importe de la compra debe ser num\u00e9rico.");
                }
                tfMtoVta.setText(FMT_MON.format(cantCpra));
            }
        }
    }
    
    /**
     * Obtiene el tipo de cambio del producto contrario al ancla seleccionada en
     * una regla de neteo.
     *
     * @param claveFormaLiqCpra		La clave del producto en la compra.
     * @param claveFormaLiqVta		La clave del producto en la venta.
     */
    private void obtenerTipoCambioContrarioAncla(String claveFormaLiqCpra,
    		String claveFormaLiqVta) {
    	for (Iterator it = rowFiltrado.iterator(); it.hasNext();) {
    		Map pizarron = (HashMap) it.next();
    		String idDivisaPizarron = (String) pizarron.get(Consts.ID_DIVISA_KEY);
    		String claveFormaLiquidacionPizarron = (String) pizarron.get(Consts.
    				CLAVE_FORMA_LIQUIDACION_KEY);
    		if (isVenta) {
                if (idDivisaPizarron.equals(_idDivisa) && claveFormaLiquidacionPizarron.
                        equals(claveFormaLiqCpra)) {
                    if (fechaValSel.equals(Consts.CASH)) {
                    	lCpraTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(pizarron.
                    			get(Consts.C_CASH_KEY))).doubleValue()));
                    	lVtaTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(valorSel))));
                    }
                    else if (fechaValSel.equals(Consts.TOM)) {
                    	lCpraTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(pizarron.
                    			get(Consts.C_TOM_KEY))).doubleValue()));
                    	lVtaTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(valorSel))));
                    }
                    else if (fechaValSel.equals(Consts.SPOT)) {
                    	lCpraTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(pizarron.
                    			get(Consts.C_SPOT_KEY))).doubleValue()));
                    	lVtaTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(valorSel))));
                    }
                    else if (fechaValSel.equals(Consts.HR72)) {
                    	lCpraTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(pizarron.
                    			get(Consts.C_72HR_KEY))).doubleValue()));
                    	lVtaTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(valorSel))));
                    }
                    else if (fechaValSel.equals(Consts.VFUT)) {
                    	lCpraTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(pizarron.
                    			get(Consts.C_VFUT_KEY))).doubleValue()));
                    	lVtaTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(valorSel))));
                    }
                    break;
                }
            }
            else if (!isVenta) {
                if (idDivisaPizarron.equals(_idDivisa) && claveFormaLiquidacionPizarron.
                        equals(claveFormaLiqVta)) {
                    if (fechaValSel.equals(Consts.CASH)) {
                    	lVtaTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(pizarron.
                    			get(Consts.V_CASH_KEY))).doubleValue()));
                    	lCpraTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(valorSel))));
                    }
                    else if (fechaValSel.equals(Consts.TOM)) {
                    	lVtaTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(pizarron.
                    			get(Consts.V_TOM_KEY))).doubleValue()));
                    	lCpraTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(valorSel))));
                    }
                    else if (fechaValSel.equals(Consts.SPOT)) {
                    	lVtaTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(pizarron.
                    			get(Consts.V_SPOT_KEY))).doubleValue()));
                    	lCpraTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(valorSel))));
                    }
                    else if (fechaValSel.equals(Consts.HR72)) {
                    	lVtaTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(pizarron.
                    			get(Consts.V_72HR_KEY))).doubleValue()));
                    	lCpraTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(valorSel))));
                    }
                    else if (fechaValSel.equals(Consts.VFUT)) {
                    	lVtaTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(pizarron.
                    			get(Consts.V_VFUT_KEY))).doubleValue()));
                    	lCpraTcm.setText(FMT_TC.format(Double.valueOf(String.valueOf(valorSel))));
                    }
                    break;
                }
            }
    	}
    }

    /**
     * Suma o resta el diferencial de la regla de neteo seleccionada, al tipo de cambio ya sea
     * a la compra o a la venta dependiendo de la regla base seleccionada.
     *
     * @param valorDiferencial Contiene el valor del diferencial de la regla seleccionada.
     */
    private void sumarRestarDiferencial(String valorDiferencial) {
        if (baseReglaSel.trim().equals(Consts.TIPO_DE_CAMBIO)) {
        	String[] valoresDiferencial = valorDiferencial.split(";");
        	if (isRangoLimHrNeteoPermitido()) {
        		diferencial = new BigDecimal(valoresDiferencial[0]);
        	}
        	else {
        		diferencial = new BigDecimal(valoresDiferencial[1]);
        	}
            verificaCarry(diferencial.doubleValue());
            if (isVenta) {
                valorLVta = valorSel;
                valorLCpra = valorSel - diferencial.doubleValue();
            }
            else {
                valorLCpra = valorSel;
                valorLVta = valorSel + diferencial.doubleValue();
            }
        }
    }

    /**
     * Multiplica el valor del diferencial por el valor de la fecha valor correspondiente.
     *
     * @param difAux Valor del diferencial a ser aplicado para la regla de neteo seleccionada.
     */
    private void verificaCarry(double difAux) {
        if (fechaValSel.equals(Consts.CASH)) {
            diferencial = new BigDecimal(difAux * Consts.NUM_1);
        }
        else if (fechaValSel.equals(Consts.TOM)) {
            diferencial = new BigDecimal(difAux * Consts.NUM_2);
        }
        else if (fechaValSel.equals(Consts.SPOT)) {
            diferencial = new BigDecimal(difAux * Consts.NUM_3);
        }
        else if (fechaValSel.equals(Consts.HR72)) {
            diferencial = new BigDecimal(difAux * Consts.NUM_4);
        }
        else if (fechaValSel.equals(Consts.VFUT)) {
            diferencial = new BigDecimal(difAux * Consts.NUM_5);
    	}
    }
    
    /**
     * Obtiene el monto antes y despu&eacute;s del horario en dado caso de que la regla seleccionada
     * sea IMPORTE O MONTO.
     *
     * @param valores Contiene los montos antes y despu&eacute;s del horario respectivamente.
     */
    private void obtenerMontoAntesYDespuesDelHorario(String valores) {
        String[] valoresMonto = valores.split("-");
        if (baseReglaSel.equals(Consts.IMPORTE_O_MONTO)) {
            montoAntesDelHorario = Double.valueOf(valoresMonto[0]).doubleValue();
            montoDespuesDelHorario = Double.valueOf(valoresMonto[1]).doubleValue();
            tfMtoVta.setEnabled(true);
            tfMtoCpra.setEnabled(true);
            valorLVta = Double.valueOf(lVtaTcm.getText()).doubleValue();
            valorLCpra = Double.valueOf(lCpraTcm.getText()).doubleValue();
            tfCpra.setEnabled(false);
            tfVta.setEnabled(false);
        }
        else {
        	tfCpra.setEnabled(true);
            tfVta.setEnabled(true);
        }
    }

    /**
     * Regresa true si el horario del sistema es menor al m&aacute;ximo permitido para sumar el 
     * monto antes del horario y regresa false si el horario del sistema es mayor al m&aacute;ximo 
     * permitido para entonces sumar el monto despues del horario.
     *
     * @return boolean.
     */
    private boolean isRangoLimHrNeteoPermitido() {
        String horaActual = HOUR_FORMAT.format(new Date());
        return horaActual.compareTo(_psLimHr) < 0;
    }

    /**
     * M&eacute;todo que sirve para buscar el IdPr, IdFactorDivisa y el IdSpread de la regla 
     * seleccionada en el panel de mec&aacute;nica de neteos, tanto para la compra como para la
     * venta.
     *
     * @param claveFormaLiquidacionCompraSel La clave de la compra de la regla seleccionada.
     * @param claveFormaLiquidacionVentaSel  La clave de la venta de la regla seleccionada.
     */
    private void findIdPrIdFactorDivisaIdSpread(String claveFormaLiquidacionCompraSel,
                                                String claveFormaLiquidacionVentaSel) {
        for (Iterator it = rowFiltrado.iterator(); it.hasNext();) {
            Map pizarron = (HashMap) it.next();
            String idDivisaPizarron = (String) pizarron.get(Consts.ID_DIVISA_KEY);
            String claveFormaLiquidacionPizarron = (String) pizarron.get(Consts.
                    CLAVE_FORMA_LIQUIDACION_KEY);
            if (idDivisaPizarron.equals(_idDivisa) && claveFormaLiquidacionPizarron.
                    equals(claveFormaLiquidacionCompraSel)) {
                prMidSpotCpra = (Double)pizarron.get(Consts.PR_MID_SPOT_KEY);
                prSpotCpra = (Double)pizarron.get(Consts.PR_SPOT_KEY);
                factorDivisaCpra = (Double)pizarron.get(Consts.FACTOR_DIVISA_KEY);
                idPrCpra = String.valueOf(pizarron.get(Consts.ID_PR_KEY));
                idFactorDivisaCpra = String.valueOf(pizarron.get(Consts.ID_FACTOR_DIVISA_KEY));
                idSpreadCpra = String.valueOf(pizarron.get(Consts.ID_SPREAD));
            }
            if (idDivisaPizarron.equals(_idDivisa) && claveFormaLiquidacionPizarron.
                    equals(claveFormaLiquidacionVentaSel)) {
            	prMidSpotVta = (Double)pizarron.get(Consts.PR_MID_SPOT_KEY);
                prSpotVta = (Double)pizarron.get(Consts.PR_SPOT_KEY);
                factorDivisaVta = (Double)pizarron.get(Consts.FACTOR_DIVISA_KEY);
                idPrVta = String.valueOf(pizarron.get(Consts.ID_PR_KEY));
                idFactorDivisaVta = String.valueOf(pizarron.get(Consts.ID_FACTOR_DIVISA_KEY));
                idSpreadVta = String.valueOf(pizarron.get(Consts.ID_SPREAD));
            }
        }
    }

    /**
     * Muestra el panel de metales amonedados.
     */
    private void showMA() {
        _dNFMA = new JDialog((Frame) null, "Metales amonedados", true);
        Dimension scrn = getToolkit().getScreenSize();
        try {
            _tabMA.setModel(new TabMetalesAmonedados(getDatos(OP_MET)));
        }
        catch (Exception e1) {
            _tabMA.setModel(new TabMetalesAmonedados(new ArrayList()));
        }
        _dNFMA.getContentPane().add(new JScrollPane(_tabMA,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        _dNFMA.setSize(940, 260);
        _dNFMA.setLocation((scrn.width - _dNFMA.getWidth()) / 2,
                (scrn.height - _dNFMA.getHeight()) / 2);
        _dNFMA.show();
    }

    /**
     * Crea y regresa un nuevo table model para desplegar el pizarr&oacute;n de divisas frecuentes.
     *
     * @return TabMod.
     */
    private com.ixe.ods.sica.pizarron.TabMod createTableModel() {
        rowFiltrado = new ArrayList();
        rowTempFiltrado = new ArrayList();
        Iterator itTemp = null;
        if (_rowsTemp != null) {
            itTemp = _rowsTemp.iterator();
        }
        for (Iterator it = _rows.iterator(); it.hasNext();) {
            Map r = (Map) it.next();
            Map rTemp = null;
            if (_rowsTemp != null) {
                if (itTemp != null && itTemp.hasNext()) {
                    rTemp = (Map) itTemp.next();
                }
            }
            if (_idDivisa.equals(r.get(Consts.ID_DIVISA_KEY))) {
                rowFiltrado.add(r);
                if (_rowsTemp != null) {
                    rowTempFiltrado.add(rTemp);
                }
            }
        }
        return new TabMod(_idDivisa, rowFiltrado, _restrs, _soloCash, _vFut);
    }

    /**
     * Establece el valor de tout.
     *
     * @param tout El valor a asignar.
     */
    public void setTout(int tout) {
        _tout = tout;
    }
    
    /**
     * Establece el valor de tout para Divisas Frecuentes.
     *
     * @param tout El valor a asignar.
     */
    public void setToutDF(int _toutdf) {
		toutDF = _toutdf;
	}

	/**
     * Establece el valor de tout para Metales amonedados y Divisas No Frecuentes.
     *
     * @param tout El valor a asignar.
     */
    public void setToutMA_DNF(int _toutma_dnf) {
		toutMA_DNF = _toutma_dnf;
	}
    
    /**
     * Muestra en el textField tfSb2 la &uacute;ltima fecha de actualizaci&oacute;n de la pizarra.
     *
     * @param d La fecha a desplegar.
     */
    private void actualizarFecha(Date d) {
        _lUltAct.setText("\u00dalt. modif.:" + new SimpleDateFormat("HH:mm:ss").format(d));
    }

    /**
     * Confirma si se lleva a cabo o no la captura del deal.
     *
     * @param rec Define si es compra o venta.
     * @param tv El tipo valor de la operaci&oacute;n.
     * @param cant El monto de la operaci&oacute;n
     * @param tcm El tipo de cambio para la mesa.
     * @param tcc El tipo de cambio para el cliente.
     * @param idProd El id del producto.
     * @param idDiv El id de la divisa.
     * @param sdec Define si el redondeo es a 6 decimales.
     * @return int
     */
    public int confOp(boolean rec, String tv, double cant, double tcm, double tcc, 
    		String idProd, String idDiv, boolean sdec) {
        JDialog dialog;
        JOptionPane op = new JOptionPane(new DatPan(rec, tv, cant, tcm, tcc, idProd, idDiv, sdec));
        op.setOptionType(JOptionPane.YES_NO_CANCEL_OPTION);
        dialog = op.createDialog(null, "Confirme por favor:");
        dialog.show();
        return ((Integer) op.getValue()).intValue();
    }
    
    /**
     * Confirma si se lleva a cabo o no la captura del deal con mec&aacute;nica de neteos.
     *
     * @param recCpra Define si es compra o venta.
     * @param tvCpra El tipo valor de la operaci&oacute;n en la compra.
     * @param cantCpra El monto de la operaci&oacute;n en la compra.
     * @param tcmCpra El tipo de cambio para la mesa en la compra.
     * @param tccCpra El tipo de cambio para el cliente en la compra.
     * @param idProdCpra El id del producto en la compra.
     * @param idDivCpra El id de la divisa en la compra.
     * @param sdecCpra Define si el redondeo es a 6 decimales en la compra.
     * @param recVta Define si es compra o venta.
     * @param tvVta El tipo valor de la operaci&oacute;n en la venta.
     * @param cantVta El monto de la operaci&oacute;n en la venta.
     * @param tcmVta El tipo de cambio para la mesa en la venta.
     * @param tccVta El tipo de cambio para el cliente en la venta.
     * @param idProdVta El id del producto en la venta.
     * @param idDivVta El id de la divisa en la venta.
     * @param sdecVta Define si el redondeo es a 6 decimales en la venta.
     * @return int
     */
    public int confOpMN(boolean recCpra, String tvCpra, double cantCpra, double tcmCpra,
                        double tccCpra, String idProdCpra, String idDivCpra, boolean sdecCpra,
                        boolean recVta, String tvVta, double cantVta, double tcmVta, double tccVta,
                        String idProdVta, String idDivVta, boolean sdecVta) {
        JDialog dialog;
        JOptionPane op = new JOptionPane(new DatPan(recCpra, tvCpra, cantCpra, tcmCpra, tccCpra,
                idProdCpra, idDivCpra, sdecCpra, recVta, tvVta, cantVta, tcmVta, tccVta,
                idProdVta, idDivVta, sdecVta));
        op.setOptionType(JOptionPane.YES_NO_CANCEL_OPTION);
        dialog = op.createDialog(null, "Confirme por favor:");
        dialog.show();
        int respuesta = JOptionPane.CANCEL_OPTION;
        if ((op.getValue()) != null) {
            respuesta = ((Integer) op.getValue()).intValue();
        }
        return respuesta;
    }

    /**
     * Pregunta al usuario el monto a comprar o vender y el tipo de cambio que se dar&aacute; al
     * cliente y env&iacute;a esta informaci&oacute;n a la p&aacute;gina de captura de deal para
     * insertar un nuevo detalle.
     *
     * @param table La tabla en la que se origin&oacute; el doble-click.
     * @param tipo 0 Frec, 1 No Frec, 2 Met Amon.
     */
    public void dobleClick(JTable table, int tipo) {
    	if(tipo==0)
    		_tout = toutDF;
    	else
    		_tout = toutMA_DNF;
        
    	int selCol = table.getSelectedColumn();
        int selRow = table.getSelectedRow();
        boolean isFrec = tipo == 0;
        boolean isMet = tipo == 2;
    	if ((isFrec && selCol < 1) || (!isFrec && selCol < 2) || !capturaDeal ||
                table.getValueAt(selRow, selCol).toString().trim().length() == 0) {
    		return;
    	}
    	actualizar();
        String valor = table.getValueAt(selRow, selCol).toString().trim();
        
        if ("".equals(valor) || "0.0000".equals(valor)) {
            return;
        }
        boolean vta = isFrec ? selCol % 2 == 0 : selCol % 2 != 0;
        
        if ((vta && _pivote.equals(PIVOTE_VTA_FIN)) || (!vta && _pivote.equals(PIVOTE_CPA_FIN))) {
            avisar("La " + (vta ? "venta" : "compra") +
                    " ya hab\u00eda sido cerrada para este deal.");
            return;
        }
        if ((vta && _pivote.equals(PIVOTE_CPA)) || (!vta && _pivote.equals(PIVOTE_VTA))) {
            if (preguntar("\u00bfTermin\u00f3 de capturar TODOS los detalles de " +
                    (_pivote.equals(PIVOTE_CPA) ? "RECIBIMOS?" : "ENTREGAMOS?")) != 0) {
                return;
            }
            setPivote(!vta ? PIVOTE_VTA_FIN : PIVOTE_CPA_FIN);
        }
        String idProd = isFrec ? ((AbstractTabMod) table.getModel()).getIdProd(selRow) : isMet ?
                "METALS" : _cbProds.getSelectedIndex() == 0 ? "TRAEXT" : _cbProds.getSelectedIndex() == 1 ? "DOCEXT" : "EFECTI";
        
        String idDiv = isFrec ? _idDivisa : (String) table.getModel().getValueAt(selRow, 0);
        
        DatPan panel = new DatPan(table.getModel(), selRow, selCol);
        JOptionPane op = new JOptionPane(panel);
        op.setOptionType(JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog;
        String tv = isFrec ? (selCol > Consts.NUM_8 ? Consts.VFUT : selCol > Consts.NUM_6 ?
                Consts.HR72 : selCol > Consts.NUM_4 ? Consts.SPOT : selCol > 2 ? Consts.TOM :
                Consts.HOY) : isMet ? (selCol > Consts.NUM_5 ? Consts.SPOT : selCol > Consts.NUM_3 ?
                Consts.TOM : Consts.HOY) : Consts.HOY;
                
        if (jChBoxMN.isSelected() && capturaDeal) {
        	valorSel = Double.parseDouble(valor);
        	isVenta = vta;
        	idProdSel = idProd;
        	if (tv.equals(Consts.HOY)) {
        		fechaValSel = Consts.CASH;
        	}
        	else {
        		fechaValSel = tv;
        	}
        	showMN();
        	return;
        }
        dialog = isFrec ? op.createDialog(null, (vta ? Consts.VENTA_STR : Consts.COMPRA_STR) + tv +
                Consts.NBSP + idProd + Consts.NBSP + idDiv) :
                op.createDialog(null, (vta ? Consts.VENTA_STR : Consts.COMPRA_STR) + idProd +
                        Consts.NBSP + idDiv);
        double cant = 0.0;
        // Empieza el timer:
        iniOp = getFechaServ();
        int res = JOptionPane.CANCEL_OPTION;
        while (true) {
            if (res == JOptionPane.CANCEL_OPTION) {
                while (true) {
                    dialog.show();
                    if (((Integer) op.getValue()).intValue() == 0) {
                        try {
                            cant = validarNum(panel.getTfCant().getText(), "cantidad");
                            validarNum(panel.getTfTc().getText(), "tipo de cambio cliente");
                            
                            double fd = isFrec ? ((TabMod) table.getModel()).getFactorDivisa(selRow) : 1;
                            if (validarCan(panel.getTfTm().getText(),
                                    panel.getTfTc().getText(), vta) &&
                                    vMinMult(idProd, !vta, idDiv, cant) &&
                                    validarTCLimDesv(cant * fd, panel.getTfTc().getText(),
                                            panel.getTfTm().getText())) {
                                break;
                            }
                        }
                        catch (ParseException e) {
                            avisar("El monto no es v\u00e1lido.");
                        }
                        catch (Exception e) {
                            avisar(e.getMessage());
                        }
                    }
                    else {
                        return;
                    }
                }
                if (! isFrec) {
                    _dNFMA.setVisible(false);
                }
                res = confOp(!vta, tv, cant,
                        Double.valueOf(panel.getTfTm().getText()).doubleValue(),
                        Double.valueOf(panel.getTfTc().getText()).doubleValue(), idProd, idDiv,
                        false);
                if (res == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            else if (res == JOptionPane.YES_OPTION) {
                break;
            }
            else {
                return;
            }
        }
        _logger.info("Tiempo: " + (getFechaServ().getTime() - iniOp.getTime()) / Consts.NUM_1000);
        if ((getFechaServ().getTime() - iniOp.getTime()) / Consts.NUM_1000 > _tout) {
            avisar("Han transcurrido los " + _tout  + " segs. para aceptar la operaci\u00f3n. " +
                    "Por favor seleccione de nuevo el tipo de cambio del pizarr\u00f3n.");
            return;
        }
        actualizar();
        String tcm2 = table.getValueAt(selRow, selCol).toString().trim();
        String tcm = panel.getTfTm().getText().trim();
        if (!tcm.equals(tcm2)) {
            if (JOptionPane.NO_OPTION == preguntar("El tipo de cambio de la mesa ha\nvariado de " +
                    tcm + " a " + tcm2 + ".\n" + "\u00bfDesea continuar?")) {
                return;
            }
        }
        tcm = tcm2;
        _ctrl.operar(!vta, isFrec ? (selCol < Consts.NUM_3 ? Consts.CASH : selCol < Consts.NUM_5 ?
                Consts.TOM : selCol < Consts.NUM_7 ? Consts.SPOT : selCol < Consts.NUM_9 ?
                Consts.HR72 : Consts.VFUT) : isMet ? (selCol > Consts.NUM_5 ? Consts.SPOT :
                selCol > Consts.NUM_3 ? Consts.TOM : Consts.CASH) : Consts.CASH,
                idProd, idDiv, Double.valueOf(tcm).doubleValue(), cant,
                Double.valueOf(panel.getTfTc().getText()).doubleValue(),
                ((AbstractTabMod) table.getModel()).getPrMidSpot(selRow),
                ((AbstractTabMod) table.getModel()).getPrSpot(selRow),
                ((AbstractTabMod) table.getModel()).getFactorDivisa(selRow),
                isFrec ? ((TabMod) table.getModel()).getIdSpr(selRow) : 0,
                ((AbstractTabMod) table.getModel()).getIdPr(selRow),
                ((AbstractTabMod) table.getModel()).getIdFD(selRow));
        if (_pivote.equals(SIN_PIVOTE)) {
            setPivote(_pivote = vta ? PIVOTE_VTA : PIVOTE_CPA);
        }
    }

    /**
     * Valida que el valor v sea num&eacute;rico.
     *
     * @param v El valor a validar.
     * @param campo El nombre del campo.
     * @return double.
     * @throws Exception Si el valor no es v&aacute;lido.
     */
    private double validarNum(String v, String campo) throws Exception {
        try {
            return DatPan.FMT_MON.parse(v).doubleValue();
        }
        catch (NumberFormatException e) {
            throw new Exception("El campo " + campo + " debe ser num\u00e9rico.");
        }
    }
    
    /**
     * Obtiene la fecha actual al confirmar el monto y el tipo de cambio
     * de la operacion para calcular el tiempo transcurrido.
     * 
     * @return Date
     */
    private Date getFechaServ() {
    	 _logger.info(new Date() + " Entrando a getFechaServ()...");
         List resultados = null;
         try {
             // Esta condicion se necesita cuando se quiere correr el applet desde el IDE:
             if (SERV_IDE.equals(_serv)) {
                 _serv = Consts.LOCALHOST_STR;
             }
             String servletURL = prot + ":/" + "/" + _serv + "/sica/fecha.jsp?" +
                     new Date().getTime();
             _logger.info(servletURL);
             URL url = new URL(servletURL);
             URLConnection conn = url.openConnection();
             conn.setDoOutput(true);
             conn.setRequestProperty(Consts.PRAGMA_STR, Consts.NO_CACHE_STR);
             conn.connect();
             BufferedReader buf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
             try {
                 String str;
                 resultados = new ArrayList();
                 while ((str = buf.readLine()) != null) {
                     try {
                         String values[] = str.split(Consts.PIPE_REGEXP_STR);
                         Long l = new Long(values[0]);
                         Date fechaActual = new Date();
                         fechaActual.setTime(l.longValue());
                         resultados.add(fechaActual);
                     }
                     catch (NullPointerException e) {
                         e.printStackTrace();
                         if (_logger.isLoggable(Level.WARNING)) {
                             _logger.log(Level.WARNING, "No se pudo parsear la linea " + str);
                         }
                     }
                 }
             }
             catch (IOException e) {
                 if (_logger.isLoggable(Level.SEVERE)) {
                     _logger.log(Level.SEVERE, Consts.ERROR_STR, e);
                 }
             }
             finally {
                 try {
                     buf.close();
                 }
                 catch (IOException e) {
                     if (_logger.isLoggable(Level.WARNING)) {
                         _logger.log(Level.WARNING, "Error al cerrar el buffer.", e);
                     }
                 }
             }
         }
         catch (IOException e) {
             if (_logger.isLoggable(Level.WARNING)) {
                 _logger.log(Level.WARNING, Consts.ERROR_STR, e);
             }
         }
         _logger.info(new Date() + " Saliendo de getFechaServ().\n\n");
        return (Date) resultados.get(0);
    }

    /**
     * Regresa true si el monto y tipo de cambio al cliente capturados son v&aacute;lidos.
     *
     * @param m El monto.
     * @param v El tipo de cambio al cliente.
     * @param vta Si fue venta o no.
     * @return boolean.
     */
    private boolean validarCan(String m, String v, boolean vta) {
        return !((vta && (Double.parseDouble(m) > Double.parseDouble(v))) ||
                (!vta && (Double.parseDouble(m) < Double.parseDouble(v)))) ||
                preguntar("El tipo de cambio para el Cliente es " + (vta ? "menor " : "mayor ") +
                        "que el de la Mesa.\n\u00bfDesea continuar?") == 0;
    }

    /**
     * Valida el monto m&iacute;nimo y m&iacute;nimo com&uacute;n m&uacute;ltiplo, de acuerdo a la
     * informaci&oacute;n de la lista de formasPagoLiquidaci&oacute;n (fpls).
     *
     * @param cfl La clave forma liquidaci&oacute;n (producto).
     * @param cpra true para compra y false para venta.
     * @param idDiv El identificador de la divisa.
     * @param mto El monto a validar.
     * @return boolean.
     * @throws Exception Si hay un error en la validaci&oacute;n.
     */
    private boolean vMinMult(String cfl, boolean cpra, String idDiv, double mto)
            throws Exception {
        StringBuffer sb = new StringBuffer();
        DecimalFormat df = new DecimalFormat("0.00");
        for (Iterator it = _fpls.iterator(); it.hasNext();) {
            Map map = (Map) it.next();
            String mnem = (String) map.get("mnemonico");
            if (cfl.equals(map.get("claveFormaLiquidacion")) && idDiv.equals(map.get("idDivisa")) &&
                    ((cpra && mnem.startsWith("R")) || (!cpra && mnem.startsWith("E")))) {
                double min = ((BigDecimal) map.get("minimo")).doubleValue();
                double mult = ((Double) map.get("multiplo")).doubleValue();
                double cosciente = df.parse(df.format(mto / mult)).doubleValue();
                if (mto >= min && (cosciente - (long) cosciente == 0)) {
                    return true;
                }
                StringBuffer sb2 = new StringBuffer("(M\u00ednimo: ").append(min).
                        append(", M\u00faltiplo de ").append(mult).append(")\n");
                if (sb.toString().indexOf(sb2.toString()) < 0) {
                    sb.append(sb2.toString());
                }
            }
        }
        if (sb.toString().trim().length() > 0) {
            throw new Exception("El monto no es v\u00e1lido. Debe seguir las siguientes reglas:\n" +
                    sb.toString());
        }
        else {
            throw new Exception("Error de datos.\nNo se tienen configuradas correctamente las " +
                    "formas de liquidaci\u00f3n para la Divisa " + idDiv + " \nen la " +
                    (cpra ? "COMPRA " : "VENTA") + " de " + cfl +
                    ".\nFavor de llamar a Sistemas.");
        }
    }

    /**
     * Regresa true si el tipo de cambio al cliente capturado est&aacute; dentro del l&iacute;mite
     * de desviaci&oacute;n (+,-).
     *
     * @param mto El monto.
     * @param tcc El tipo de cambio al cliente.
     * @param tcm El tipo de cambio de la mesa.
     * @return boolean.
     */
    private boolean validarTCLimDesv(double mto, String tcc, String tcm) {
        _logger.info("Monto: " + mto + " tcc " + tcm);
        double tcCliente = redondear8Dec(Double.parseDouble(tcc));
        _logger.info("tcCliente: " + tcCliente);
        double tcMesa = redondear8Dec(Double.parseDouble(tcm));
        _logger.info("tcMesa: " + tcMesa);
        double limiteDesviacion;
        boolean resultado = true;
        if (mto >= _desvMontoLim) {
            limiteDesviacion = _desvPorcLim;
        }
        else {
            limiteDesviacion = _desvPorcMax * Math.exp(_desvFact1 * log(mto)) * _desvFact2 /
                    Consts.NUMD_100;
        }
        _logger.info("limiteDesviacion: " + limiteDesviacion);
        limiteDesviacion = redondear8Dec(limiteDesviacion);
        _logger.info("2. limiteDesviacion: " + limiteDesviacion);
        _logger.info("a. " + redondear8Dec(tcMesa * (1.0 - limiteDesviacion)));
        _logger.info("b. " + redondear8Dec(tcMesa * (1.0 + limiteDesviacion)));
        if (tcCliente < redondear8Dec(tcMesa * (1.0 - limiteDesviacion))) {
            avisar("El tipo cambio es menor al l\u00edmite de desviaci\u00f3n permitido.");
            resultado = false;
        }
        if (tcCliente > redondear8Dec(tcMesa * (1.0 + limiteDesviacion))) {
            avisar("El tipo cambio es mayor al l\u00edmite de desviaci\u00f3n permitido.");
            resultado = false;
        }
        _logger.info("Resultado: " + resultado);
        return resultado;
    }
    
    /**
     * Regresa false si el tipo de cambio del cliente en el neteo, es menor al tipo de cambio
     * de la mesa.
     * 
     * @param tcmNeteo El tipo de cambio de la mesa en la captura por neteo.
     * @param tccNeteo El tipo de cambio del client en la captura por neteo.
     * @param vta      Si fue venta o no.
     * @return
     */
    private boolean validarMinTccNeteo(String tcmNeteo, String tccNeteo, boolean vta) {
        boolean resultado = true;
        if (vta && (Double.parseDouble(tccNeteo) < Double.parseDouble(tcmNeteo))) {
            avisar("El tipo de cambio del cliente en la venta no puede ser menor al tipo " +
                    "de cambio de la mesa.");
            resultado = false;
        }
        else if (!vta && (Double.parseDouble(tccNeteo) > Double.parseDouble(tcmNeteo))) {
            avisar("El tipo de cambio del cliente en la compra no puede ser mayor al tipo " +
                    "de cambio de la mesa.");
            resultado = false;
        }
        return resultado;
    }

    /**
     * Regresa la cantidad redondeada a 8 cifras decimales.
     *
     * @param cantidad La cantidad a redondear.
     * @return double.
     */
    public double redondear8Dec(double cantidad) {
        double res = cantidad > 0 ? cantidad + 0.000000005 : cantidad - 0.000000005;
        return ((long) (res * 100000000.0)) / 100000000.0;
    }

    /**
     * Regresa el logaritmo base 10 de x.
     *
     * @param x El valor a calcular.
     * @return double.
     */
    private double log(double x) {
        return Math.log(x) / Math.log(Consts.NUMD_10);
    }

    /**
     * Muestra un mensaje en una ventana de di&aacute;logo.
     *
     * @param m El mensaje a mostrar.
     */
    public void avisar(String m) {
        JOptionPane.showMessageDialog(null, m, Consts.ATENCION_STR, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Muestra un panel de confirmaci&oacute;n en modo YES_NO_OPTION. Regresa 0 si el usuario
     * contesta S&iacute;, o 1 si el usuario contesta No.
     *
     * @param m El mensaje a mostrar.
     * @return int.
     */
    public int preguntar(String m) {
        return JOptionPane.showConfirmDialog(null, m, Consts.ATENCION_STR,
                JOptionPane.YES_NO_OPTION);
    }

    /**
     * Refresca el pizarr&oacute;n con los datos.
     */
    public void actualizar() {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS");
    	
    	_logger.log(Level.SEVERE, "Iniciando la actualizacion del applet pizarron" + sdf.format(new Date()));
    	
        List rs = getDatos(OP_PIZ);
        _rowsTemp = _rows;
        _rows = new ArrayList();
        for (Iterator it = rs.iterator(); it.hasNext();) {
            _rows.add(it.next());
        }
        _tab.setModel(createTableModel());
        _tab.getColumnModel().getColumn(0).setMinWidth(Consts.NUM_200);
        establecerColor();
        actualizarFecha(new Date());
        _segsAct = 0;
        
        _logger.log(Level.SEVERE, "Terminando la actualizacion del applet pizarron" + sdf.format(new Date()));
    }

    /**
     * Cambia los colores de fondo de las celdas para indicar si el precio vari&oacute; a la alza o
     * a la baja.
     */
    private void establecerColor() {
        try {
            Iterator it = rowFiltrado.iterator();
            int i = 0;
            modificados = new int[_rows.size()][Consts.NUM_10];
            for (Iterator itTemp = rowTempFiltrado.iterator(); itTemp.hasNext(); i++) {
                Map rpTemp = (Map) itTemp.next();
                Map rp = (Map) it.next();
                Object[][] valores = new Object[][]{
                        {rpTemp.get(Consts.C_CASH_KEY), rp.get(Consts.C_CASH_KEY)},
                        {rpTemp.get(Consts.V_CASH_KEY), rp.get(Consts.V_CASH_KEY)},
                        {rpTemp.get(Consts.C_TOM_KEY), rp.get(Consts.C_TOM_KEY)},
                        {rpTemp.get(Consts.V_TOM_KEY), rp.get(Consts.V_TOM_KEY)},
                        {rpTemp.get(Consts.C_SPOT_KEY), rp.get(Consts.C_SPOT_KEY)},
                        {rpTemp.get(Consts.V_SPOT_KEY), rp.get(Consts.V_SPOT_KEY)},
                        {rpTemp.get(Consts.C_72HR_KEY), rp.get(Consts.C_72HR_KEY)},
                        {rpTemp.get(Consts.V_72HR_KEY), rp.get(Consts.V_72HR_KEY)},
                        {rpTemp.get(Consts.C_VFUT_KEY), rp.get(Consts.C_VFUT_KEY)},
                        {rpTemp.get(Consts.V_VFUT_KEY), rp.get(Consts.V_VFUT_KEY)}
                };
                for (int fv = 0; fv < Consts.NUM_10; fv++) {
                    if (((Double) valores[fv][0]).doubleValue() < ((Double) valores[fv][1]).
                            doubleValue()) {
                        modificados[i][fv] = 2;
                    }
                    else if (((Double) valores[fv][0]).doubleValue() > ((Double) valores[fv][1]).
                            doubleValue()) {
                        modificados[i][fv] = 1;
                    }
                    else {
                        modificados[i][fv] = 0;
                    }
                }
            }
        }
        catch (NullPointerException e) {
            if (_logger.isLoggable(Level.WARNING)) {
                _logger.log(Level.WARNING, "Error.", e);
            }
        }
        _tab.repaint();
    }

    /**
     * Establece el valor de capturaDeal.
     *
     * @param capturaDeal El valor a asignar.
     */
    public void setCapturaDeal(boolean capturaDeal) {
        this.capturaDeal = capturaDeal;
    }

    /**
     * Fija el valor de pivote.
     *
     * @param p El valor a asignar.
     */
    public void setPivote(String p) {
        _pivote = p;
    }

    /**
     * Fija el valor de tipo de deal. Usado desde javascript.
     *
     * @param tipoDeal El valor a asignar.
     */
    public void setTipoDeal(int tipoDeal) {
        setPivote(SIN_PIVOTE);
    }

    /**
     * Detiene el timer del registro el pizarr&oacute;n.
     */
    public void desregistrar() {
        if (timer != null) {
            timer.stop();
        }
    }

    /**
     * Invoca a <code>desregistrar()<code/>.
     *
     * @throws Throwable Si ocurre alg&uacute;n error.
     */
    protected void finalize() throws Throwable {
        desregistrar();
        super.finalize();
    }

    /**
     * Registra el evento para acutalizar los datos del pizarr&oacute;n.
     *
     * @param e El ActionEvent.
     */
    public void actionPerformed(ActionEvent e) {
        _segsAct++;
        if (_segsAct > _segsRefr) {
            actualizar();
        }
    }

    /**
     * Si el frame superior de la ventana del browser est&aacute; en modo de captura de deal o no.
     */
    private boolean capturaDeal;

    /**
     * La interfaz para obtener la operaci&oacute;n realizada.
     */
    private ICtrlPizarra _ctrl;

    /**
     * Combo box para los productos de divisas no frecuentes.
     */
    private JComboBox _cbProds = new JComboBox(new String[]{
            "TRANSFERENCIA EXTRANJERA", "DOCUMENTO EXTRANJERO", "EFECTIVO"
    });

    /**
     * El tipo de pizarron para el canal de opearacion
     */
    private int _idTipoPizarron;

    /**
     * El Panel para desplegar los precios de las divisas no frecuentes y metales amonedados.
     */
    private JDialog _dNFMA;

    /**
     * El Panel para desplegar las reglas de mec&aacute;nica de neteos.
     */
    private JDialog _pMN;

    /**
     * La clave de la divisa seleccionada para la pizarra.
     */
    private String _idDivisa = "USD";

    /**
     * El protocolo de comunicaci&oacute;n: http o https.
     */
    private String prot;

    /**
     * La ip y puerto del WebLogic.
     */
    private String _serv;

    /**
     * El objeto para realizar logs a la consola.
     */
    private transient Logger _logger = Logger.global;

    /**
     * Matriz que indica cuales celdas cambiaron.
     */
    private int modificados[][] = new int[Consts.NUM_100][Consts.NUM_8];

    /**
     * Informaci&oacute;n del pivote.
     */
    private String _pivote;

    /**
     * La lista de restricciones.
     */
    private List _restrs;

    /**
     * Lista de objetos Map que representa los renglones de la pizarra.
     */
    private java.util.List _rows = new ArrayList();

    /**
     * Lista de renglones de la pizarra, utilizada para el cambio de color (verde, blanco o rojo)
     * de las celdas.
     */
    private java.util.List _rowsTemp;

    /**
     * Almacena el modelo de la tabla filtrado.
     */
    private List rowFiltrado;

    /**
     * Almacena el respaldo del modelo de la tabla filtrado.
     */
    private List rowTempFiltrado = new ArrayList();

    /**
     * La fecha en que el promotor inicia el doble click en el pizarr&oacute;n.
     */
    private Date iniOp;

    /**
     * Los segundos transcurridos desde que se hizo doble click sobre la pizarra.
     */
    //private int _segs;

    /**
     * La lista de productos.
     */
    private List _fpls;
    
    /**
     * La lista de reglas.
     */
    private List _reglasNeteos;

    /**
     * Variable para el TELLER que define si el pizarr&oacute;n solamente muestra las celdas para
     * los tipos de cambio CASH.
     */
    private boolean _soloCash;

    /**
     * Los segundos de timeout para proteger el pizarron.
     */
    private int _tout = Consts.NUM_22;

    /**
     * Los segundos de timeout para Operaciones de Metales Amonedados y Divisas No Frecuentes.
     */
    private int toutMA_DNF = Consts.NUM_22;
    
    /**
     * Los segundos de timeout para Operaciones de Divisas Frecuentes.
     */
    private int toutDF = Consts.NUM_180;
    
    /**
     * La tabla para mostrar la pizarra.
     */
    private JTable _tab = new JTable();

    /**
     * La tabla para mostrar las divisas no frecuentes.
     */
    private JTable _tabNF = new JTable();

    /**
     * La tabla para mostrar las divisas de metales amonedados.
     */
    private JTable _tabMA = new JTable();
    
    /**
     * El status bar derecho.
     */
    private JButton _bRef;
    
    /**
     * El combo box de divisas.
     */
    private JComboBox cb;

    /**
     * Etiqueta que muestra la &uacute;ltima actualizaci&oacute;n del pizarr&oacute;n.
     */
    private JLabel _lUltAct = new JLabel("", JLabel.CENTER);

    /**
     * Par&aacute;metro Desviaci&oacute;n Monto L&iacute;mite.
     */
    private double _desvMontoLim;

    /**
     * Par&aacute;metro Desviaci&oacute;n Porcentaje L&iacute;mite.
     */
    private double _desvPorcLim;

    /**
     * Par&aacute;metro Desviaci&oacute;n Factor 1.
     */
    private double _desvFact1;

    /**
     * Par&aacute;metro Desviaci&oacute;n Factor 2.
     */
    private double _desvFact2;

    /**
     * Par&aacute;metro Desviaci&oacute;n Porcentaje M&aacute;ximo.
     */
    private double _desvPorcMax;

    /**
     * Define si hay valor fututo en el pizarr&oacute;n
     */
    private boolean _vFut;

    /**
     * Los segundos de la &uacute;ltima acutalizaci&oacute;n de los datos del pizarr&oacute;n
     */
    private int _segsAct;

    /**
     * Radio Button para todas las reglas de mec&aacute;nica de neteos.
     */
    private JRadioButton radioBtnsReglas;

    /**
     * Valor del tipo de cambio de la venta para el panel mec&aacute;nica de neteos.
     */
    private double valorLVta;

    /**
     * Valor del tipo de cambio de la compra para el panel mec&aacute;nica de neteos.
     */
    private double valorLCpra;

    /**
     * Campo de texto para modificar el tipo de cambio del cliente para la compra en el panel de
     * mec&aacute;nica de neteos.
     */
    private JTextField tfCpra;

    /**
     * Campo de texto para modificar el tipo de cambio del cliente para la venta en el panel de
     * mec&aacute;nica de neteos.
     */
    private JTextField tfVta;

    /**
     * Etiqueta que muestra en la compra, el tipo de cambio de la mesa para el panel mec&aacute;nica
     * de neteos.
     */
    private JLabel lCpraTcm;

    /**
     * Etiqueta que muestra en la venta, el tipo de cambio de la mesa para el panel mec&aacute;nica
     * de neteos.
     */
	private JLabel lVtaTcm;

    /**
     * Campo de texto para modificar la cantidad a capturar para la compra, en el panel de
     * mec&aacute;nica de neteos.
     */
	private JLabel lImpte;
	
	/**
	 * Campo de texto para modificar la cantidad a capturar para la compra, en el panel de 
	 * mec&aacute;nica de neteos.
	 */
    private JTextField tfMtoCpra;

    /**
     * Campo de texto deshabilitado usado para mostrar el importe total en moneda nacional de la
     * compra.
     */
    private JLabel lImpteTotCpra;

    /**
     * Campo de texto deshabilitado usado para mostrar el importe total en moneda nacional de la
     * venta.
     */
    private JLabel lImpteTotVta;

    /**
     * Campo de texto para modificar la cantidad a capturar para la venta, en el panel de
     * mec&aacute;nica de neteos.
     */
    private JTextField tfMtoVta;

    /**
     * Valor de la regla seleccionada por alguno de los Radio Buttons en el panel de
     * mec&aacute;nica de neteos.
     */
    private String reglaRadioSel;
    
    /**
     * Valor del diferencial de la regla seleccionada por alguno de los Radio Buttons, en el panel
     * de mec&aacute;nica de neteos.
     */
    private BigDecimal diferencial;

    /**
     * El valor del monto a aplicarse en caso de que la regla caiga antes del horario establecido.
     */
    private double montoAntesDelHorario;

    /**
     * El valor del monto a aplicarse en caso de que la regla caiga despu&eacute;s del horario
     * establecido.
     */
    private double montoDespuesDelHorario;

    /**
     * Valor que indica si el promotor desea capturar una regla de neteo en base en compra o venta.
     */
    private boolean isVenta;

    /**
     * Valor que indica que tipo de producto desea capturar el promotor en una regla de neteo.
     */
    private String idProdSel;

    /**
     * Valor que indica cual es el tipo de cambio en el pizarr&oacute;n del producto que se desea
     * capturar en una regla de neteo.
     */
    private double valorSel;

    /**
     * Valor que indica que fecha valor contiene el producto que se desea capturar en una regla de
     * neteo.
     */
    private String fechaValSel;

    /**
     * Valor que indica cual es la base de la regla seleccionada en los RadioButtons, en la captura
     * de mec&aacute;nica de neteos.
     */
    private String baseReglaSel;
	
	/**
	 * Valor del idPr para la compra, de la regla seleccionada por alguno de los Radio Buttons, 
	 * en el panel de mec&aacute;nica de neteos.
	 * 
	 * @deprecated Se debe utilizar el valor directo del Precio Referencia para la Compra.
	 */
    private String idPrCpra;

    /**
	 * Valor del Precio Referencia Mid Spot para la compra, de la regla seleccionada por alguno de los Radio Buttons, 
	 * en el panel de mec&aacute;nica de neteos.
	 */
    private Double prMidSpotCpra;
    
    /**
	 * Valor del Precio Referencia Spot para la compra, de la regla seleccionada por alguno de los Radio Buttons, 
	 * en el panel de mec&aacute;nica de neteos.
	 */
    private Double prSpotCpra;
    
    /**
     * Valor del idFactorDivisa para la compra, de la regla seleccionada por alguno de los Radio
     * Buttons en el panel de mec&aacute;nica de neteos.
     * 
     * @deprecated Se debe utilizar el valor directo del Factor Divisa para la Compra.
     */
    private String idFactorDivisaCpra;
    
    /**
     * Valor del Factor Divisa para la compra, de la regla seleccionada por alguno de los Radio
     * Buttons en el panel de mec&aacute;nica de neteos.
     */
    private Double factorDivisaCpra;

    /**
     * Valor del idSpread para la compra, de la regla seleccionada por alguno de los Radio Buttons,
     * en el panel de mec&aacute;nica de neteos.
     */
    private String idSpreadCpra;

    /**
     * Valor del idPr para la venta, de la regla seleccionada por alguno de los Radio Buttons,
     * en el panel de mec&aacute;nica de neteos.
     * 
     * @deprecated Se debe utilizar el valor directo del Precio Referencia para la Venta.
     */
    private String idPrVta;
    
    /**
     * Valor del Precio Referencia Mid Spot para la venta, de la regla seleccionada por alguno de los Radio Buttons,
     * en el panel de mec&aacute;nica de neteos.
     */
    private Double prMidSpotVta;
    
    /**
     * Valor del Precio Referencia Spot para la venta, de la regla seleccionada por alguno de los Radio Buttons,
     * en el panel de mec&aacute;nica de neteos.
     */
    private Double prSpotVta;

    /**
     * Valor del idFactorDivisa para la venta, de la regla seleccionada por alguno de los Radio
	 * Buttons, en el panel de mec&aacute;nica de neteos.
	 * 
	 * @deprecated Se debe utlizar el valor directo del Factor Divisa para la Venta.
	 */
	private String idFactorDivisaVta;
	
	/**
     * Valor del Factor Divisa para la venta, de la regla seleccionada por alguno de los Radio
	 * Buttons, en el panel de mec&aacute;nica de neteos.
	 */
	private Double factorDivisaVta;

    /**
     * Valor del idSpread para la venta, de la regla seleccionada por alguno de los Radio Buttons,
     * en el panel de mec&aacute;nica de neteos.
     */
    private String idSpreadVta;


    /**
     * Panel que contiene los RadioButtons de las reglas para el panel de mec&aacute;nica de neteos.
     */
    private JPanel panelReglas;

    /**
     * Etiqueta que muestra el t&iacute;tutlo del panel de mec&aacute;nica de neteos.
     */
    private TitledBorder tituloMN;

    /**
     * Etiqueta que muestra la leyenda TCM para el panel de mec&aacute;nica de neteos.
     */
    private JLabel lTCM;
	
	/**
     * Etiqueta que muestra la leyenda TCC para el panel de mec&aacute;nica de neteos.
     */
	private JLabel lTCC;
	
	/**
     * Etiqueta que muestra la leyenda MONTO para el panel de mec&aacute;nica de neteos.
     */
	private JLabel lMto;
	
	/**
     * Etiqueta que muestra la leyenda COMPRA para el panel de mec&aacute;nica de neteos.
     */
	private JLabel lCpra;
	
	/**
     * Etiqueta que muestra la leyenda VENTA para el panel de mec&aacute;nica de neteos.
     */
	private JLabel lVta;
	
	/**
     * Bot&oacute;n de ACEPTAR para el panel de mec&aacute;nica de neteos.
     */
	private JButton btnAceptar;
	
	/**
     * Bot&oacute;n de CANCELAR para el panel de mec&aacute;nica de neteos.
     */
	private JButton btnCancelar;
	
	/**
	 * Bot&oacute;n de tipo JCheckBox para saber si el pizarr&oacute;n se encuentra habilitado
	 * para la captura de deals por mec&aacute;nica de neteos.
	 */
	private JCheckBox jChBoxMN;
	
	/**
     * Grupo de Botones al cual est&aacute;n asociados los RadioButtons para el panel de 
     * mec&aacute;nica de neteos.
     */
	private ButtonGroup groupRadios;

    /**
     * El ticket de la sesi&oacute;n del usuario.
     */
    private String _ticket;
    
    /**
     * El formato monetario para el monto.
     */
    public static final DecimalFormat FMT_MON = new DecimalFormat("#,##0.00");

    /**
     * El formato para los tipos de cambio.
     */
    private static final DecimalFormat FMT_TC = new DecimalFormat("0.00000000");

    /**
     * A timer object used to refresh the call duration.
     */
    private Timer timer = new Timer(Consts.NUM_1000, this);

    /**
     * Constante para indicar los segundos para refrescar los datos del pizarr&oacute;n
     */
    private static int _segsRefr = Consts.NUM_30;
    
    /**
     * Constante del horario l&iacute;mite para indicar el importe a sumar o restar en divisa a la 
     * captura de deals por mec&aacute;nica de neteos.
     */
    private static String _psLimHr = "";
    
    /**
     * Cadena a la cual se le asociaran los diferentes valores de las reglas de neteo existentes por
     * divisa.
     */
    private static String valoresDeLasReglas;

    /**
     * Constante para indicar la operaci&oacute;n del pizarr&oacute;n.
     */
    private static final int OP_PIZ = 0;

    /**
     * Constante para indicar la operaci&oacute;n de mostar divisas no frecuentes.
     */
    private static final int OP_NFREC = 1;

    /**
     * Constante para indicar la operaci&oacute;n de mostar metales.
     */
    private static final int OP_MET = 2;

    /**
     * Constante para la direcci&oacute;n del servidor inexistente.
     */
    private static final String SERV_IDE = ":-1";

    /**
     * Constante para indicar que no se ha definido pivote.
     */
    public static final String SIN_PIVOTE = "No definido";

    /**
     * Constante para indicar que el pivote es Compra.
     */
    private static final String PIVOTE_CPA = "Cpa";

    /**
     * Constante para indicar que el pivote es Compra y se ha cerrado su captura.
     */
    private static final String PIVOTE_CPA_FIN = "Cpa OK";

    /**
     * Constante para indicar que el pivote es Venta.
     */
    private static final String PIVOTE_VTA = "Vta";

    /**
     * Constante para indicar que el pivote es Venta y se ha cerrado su captura.
     */
    private static final String PIVOTE_VTA_FIN = "Vta OK";

    /**
     * Constante para el tono de rojo.
     */
    public static final Color ROJO = new Color(125, 0, 17);

    /**
     * Constante para el tono de verde.
     */
    public static final Color VERDE = new Color(204, 204, 153);
    
    /**
     * Constante con el formato de horas y minutos.
     */
    public static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm:ss");

    /**
     * Clase interna para controlar el cambio de las celdas.
     *
     * @author Jean C. Favila.
     */
    private class CRenderer extends DefaultTableCellRenderer {

        /**
         * Constructor por default. Inicializa el alineamiento de las celdas a centrado.
         *
         */
        public CRenderer() {
            super();
            setHorizontalAlignment(JLabel.CENTER);
        }

        /**
         * Asigna el color de fondo de la celda, de acuerdo a la variaci&oacute;n con respecto al
         * valor anterior. Verde a la alza, rojo a la baja o blanco en caso de no haber cambios.
         *
         * @param table La tabla.
         * @param value El valor de la celda.
         * @param isSelected Si est&aacute; o no seleccionada la celda.
         * @param hasFocus Si la celda tiene o no el focus.
         * @param row El n&uacute;mero de rengl&oacute;n.
         * @param column El n&uacute;mero de columna.
         * @return Component.
         */
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);
            if (column == 0) {
                cell.setBackground(Color.WHITE);
                cell.setForeground(Color.BLACK);
            }
            else if (modificados[row][column - 1] == 2) {
                cell.setBackground(VERDE);
                cell.setForeground(Color.BLACK);
            }
            else if (modificados[row][column - 1] == 1) {
                cell.setBackground(ROJO);
                cell.setForeground(Color.WHITE);
            }
            else {
                cell.setBackground(Color.WHITE);
                cell.setForeground(Color.BLACK);
            }
            return cell;
        }
    }
    
    /**
     * Clase interna para capturar el evento Key y calcula los importes totales en moneda nacional, 
     * cada vez que el usuario escriba un monto o tipo de cambio diferente.
     *
     * @author Abraham L&oacute;pez A.
     */
    class MyKeyListener extends KeyAdapter {
        private String textField = Consts.STR_VACIO;

        public MyKeyListener(String textField) {
            this.textField = textField;
        }

        public void keyTyped(KeyEvent ke) {
            char caracter = ke.getKeyChar();
            if (caracter == KeyEvent.VK_ENTER) {
                enviarOperarNeteos();
            }
            if (((caracter < '0') || (caracter > '9')) && (caracter != KeyEvent.VK_BACK_SPACE) &&
                    (caracter != '.') && (caracter != KeyEvent.VK_ENTER)) {
                ke.consume();
            }
        }

        public void keyReleased(KeyEvent ke) {
            if (textField.equals(Consts.TF_MTO_CPRA)) {
                if (!tfMtoCpra.getText().trim().equals(Consts.STR_VACIO) && tfMtoCpra.getText().
                        trim() != null) {
                    if (baseReglaSel.equals(Consts.IMPORTE_O_MONTO)) {
                        verificadorCantidadMaximaMonto(tfMtoCpra.getText(), false,
                                Consts.IMPORTE_O_MONTO);
                        if (!isVenta) {
                        	sumarRestarMontosPorHorario(false);
                        }
                    }
                    else {
                        verificadorCantidadMaximaMonto(tfMtoCpra.getText(), false,
                                Consts.TIPO_DE_CAMBIO);
                    }
                    calcularImporteMn(tfMtoCpra.getText(), tfMtoVta.getText(), tfCpra.getText(),
                            tfVta.getText(), Consts.TF_MTO_CPRA);
                }
                else {
                    if (baseReglaSel.equals(Consts.IMPORTE_O_MONTO)) {
                        inicializarMtosEImptes(Consts.TF_MTO_CPRA);
                    }
                    else {
                        lImpteTotCpra.setText(String.valueOf(Consts.NUMD_0));
                    }
                }
            }
            else if (textField.equals(Consts.TF_MTO_VTA)) {
                if (!tfMtoVta.getText().trim().equals(Consts.STR_VACIO) && tfMtoVta.getText().
                        trim() != null) {
                    if (baseReglaSel.equals(Consts.IMPORTE_O_MONTO)) {
                        verificadorCantidadMaximaMonto(tfMtoVta.getText(), true,
                                Consts.IMPORTE_O_MONTO);
                        if (isVenta) {
                        	sumarRestarMontosPorHorario(true);
                        }
                    }
                    else {
                        verificadorCantidadMaximaMonto(tfMtoVta.getText(), true,
                                Consts.TIPO_DE_CAMBIO);
                    }
                    calcularImporteMn(tfMtoCpra.getText(), tfMtoVta.getText(), tfCpra.getText(),
                            tfVta.getText(), Consts.TF_MTO_VTA);
                }
                else {
                    if (baseReglaSel.equals(Consts.IMPORTE_O_MONTO)) {
                        inicializarMtosEImptes(Consts.TF_MTO_VTA);
                    }
                    else {
                        lImpteTotVta.setText(String.valueOf(Consts.NUMD_0));
                    }
                }
            }
            else if (textField.equals(Consts.TF_CPRA)) {
                if (!tfCpra.getText().trim().equals(Consts.STR_VACIO) && tfCpra.getText().
                        trim() != null) {
                    verificadorTiposDeCambio(tfCpra.getText(), false);
                    calcularImporteMn(tfMtoCpra.getText(), tfMtoVta.getText(), tfCpra.getText(),
                            tfVta.getText(), null);
                }
                else {
                    calcularImporteMn(tfMtoCpra.getText(), tfMtoVta.getText(),
                            String.valueOf(Consts.NUMD_0), tfVta.getText(), null);
                }
            }
            else if (textField.equals(Consts.TF_VTA)) {
                if (!tfVta.getText().trim().equals(Consts.STR_VACIO) && tfVta.getText().
                        trim() != null) {
                    verificadorTiposDeCambio(tfVta.getText(), true);
                    calcularImporteMn(tfMtoCpra.getText(), tfMtoVta.getText(), tfCpra.getText(),
                            tfVta.getText(), null);
                }
                else {
                    calcularImporteMn(tfMtoCpra.getText(), tfMtoVta.getText(), tfCpra.getText(),
                            String.valueOf(Consts.NUMD_0), null);
                }
            }
        }
    }

    /**
     * Clase interna para controlar el cambio de las imagenes de las divisas.
     *
     * @author Jean C. Favila
     */
    class BanderaListCellRenderer extends DefaultListCellRenderer {

        /**
         * Constructor vac&iacute;o.
         */
        public BanderaListCellRenderer() {
            super();
        }

        /**
         * Regresa el componente incluyendo la imagen de la bandera.
         *
         * @param list La lista de elementos.
         * @param value El valor de la celda.
         * @param index El &iacute;ndice.
         * @param isSelected Si est&aacute; o no seleccionado.
         * @param cellHasFocus Si est&aacute; o no activo.
         * @return Coponent.
         */
        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            Component retValue = super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus
            );
            try {
                setIcon(new ImageIcon(getClass().getResource(value + ".jpg")));
            }
            catch (NullPointerException e) {
                _logger.warning(e.getMessage());
            }
            return retValue;
        }
     }
}
