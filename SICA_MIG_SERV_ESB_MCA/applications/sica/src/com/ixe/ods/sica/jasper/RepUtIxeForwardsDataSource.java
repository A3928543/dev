/*
 * $Id: RepUtIxeForwardsDataSource.java,v 1.1 2008/06/23 21:20:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSourceProvider;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Permite modificar el Reporte de Utilidades de Ixe Forwards
 *
 * @author Edgar Leija
 * @version $Revision: 1.1 $ $Date: 2008/06/23 21:20:23 $
 */
public class RepUtIxeForwardsDataSource extends JRAbstractBeanDataSourceProvider {
	
	/**
	 * Constructor por default.
	 *
	 */
	public RepUtIxeForwardsDataSource() {
		super(RepUtIxeForwardsBean.class);
	}
	
	/**
	 * Inicializa un objeto RepUtMayoreoContable para ejemplificar el reporte.  
	 * @param jasperReport el objeto que contiene el reporte
	 * @return  <code> JRDataSource </code>
	 * @throws JRException Excepci&oacute;n de jasper
	 */
	public JRDataSource create(JasperReport jasperReport) throws JRException {
        List elementos = new ArrayList();
        InputStream image = null;
        RepUtIxeForwardsBean bean1 = new RepUtIxeForwardsBean(image, new Integer(1), "Venta",
                "CASH", "USD", new Double(100.3), new Double(87100.3), new Double(9.7),
                new Double(94599.7), "Si", new Date(), "Mesa Operacion");
        RepUtIxeForwardsBean bean2 = new RepUtIxeForwardsBean(image, new Integer(2), "Compra",
                "TOM", "USD", new Double(1030.3), new Double(78100.3), new Double(2.7),
                new Double(4599.7), "Si", new Date(), "Mesa Operacion");
        RepUtIxeForwardsBean bean3 = new RepUtIxeForwardsBean(image, new Integer(3), "Compra",
                "SPOT", "USD", new Double(10120.3), new Double(78100.3), new Double(9.7),
                new Double(9000.7), "No", new Date(), "Mesa Operacion");
        RepUtIxeForwardsBean bean4 = new RepUtIxeForwardsBean(image, new Integer(4), "Venta",
                "CASH", "USD", new Double(10310.3), new Double(878100.3), new Double(7.7),
                new Double(1223.7), "Si", new Date(), "Mesa Operacion");
        RepUtIxeForwardsBean bean5 = new RepUtIxeForwardsBean(image, new Integer(5), "Compra",
                "CASH", "USD", new Double(1300.3), new Double(78100.3), new Double(9.7),
                new Double(2121.7), "No", new Date(), "Mesa Operacion");
        RepUtIxeForwardsBean bean6 = new RepUtIxeForwardsBean(image, new Integer(6), "Venta", "TOM",
                "EUR", new Double(1050.3), new Double(18700.3), new Double(1.7), new Double(4234.7),
                "Si", new Date(), "Mesa Operacion");
        RepUtIxeForwardsBean bean7 = new RepUtIxeForwardsBean(image, new Integer(7), "Compra",
                "SPOT", "CAD", new Double(3100.3), new Double(78100.3), new Double(9.7),
                new Double(6546.7), "Si", new Date(), "Mesa Trader");
        RepUtIxeForwardsBean bean8 = new RepUtIxeForwardsBean(image, new Integer(8), "Venta",
                "CASH", "USD", new Double(10420.3), new Double(18700.3), new Double(9.7),
                new Double(64564.7), "No", new Date(), "Mesa Trader");
        RepUtIxeForwardsBean bean9 = new RepUtIxeForwardsBean(image, new Integer(9), "Venta",
                "SPOT", "USD", new Double(103420.3), new Double(17800.3), new Double(8.7),
                new Double(987.7), "Si", new Date(), "Mesa Trader");
        RepUtIxeForwardsBean bean10 = new RepUtIxeForwardsBean(image, new Integer(10), "Compra",
                "TOM", "EUR", new Double(1002.3), new Double(18700.3), new Double(6.7),
                new Double(8766.7), "No", new Date(), "Mesa Trader");
        RepUtIxeForwardsBean bean11 = new RepUtIxeForwardsBean(image, new Integer(11), "Venta",
                "CASH", "EUR", new Double(104230.3), new Double(17800.3), new Double(1.7),
                new Double(6687.7), "Si", new Date(), "Mesa Trader");
        //Mesa Operacion
		elementos.add(bean1);
		elementos.add(bean2);
		elementos.add(bean3);
		elementos.add(bean4);
		elementos.add(bean5);
		elementos.add(bean6);
		elementos.add(bean7);
		elementos.add(bean8);
		elementos.add(bean9);
		elementos.add(bean10);
		elementos.add(bean11);		
		return new JRBeanCollectionDataSource(elementos);
	}
	
	/**
	 * @param arg0 el argumento del dataSource
	 * @throws JRException Excepci&oacute;n de jasper 
	 * 
	 */
	public void dispose(JRDataSource arg0) throws JRException {
	}	
}
