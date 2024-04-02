/*
 * $Id: ReporteDiariosDataSource.java,v 1.10 2008/02/22 18:25:03 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSourceProvider;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Clase que crea un Data Source,con un conjunto de beans,
 * que permite probar el reporte de Diarios 
 *
 * @author Gerardo Corzo Herrera
 */
public class ReporteDiariosDataSource extends JRAbstractBeanDataSourceProvider {

	/**
	 * Constructor de la clase Haciendo referencia al
	 * Bean a Utilizar
	 */
	public ReporteDiariosDataSource() {
		super(ReporteDiariosBean.class);
	}

	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#create(net.sf.jasperreports.engine.JasperReport)
	 */
	public JRDataSource create(JasperReport jasperReport) throws JRException {
		ArrayList l = new ArrayList();
		InputStream image = null;
		ReporteDiariosBean bean1 = new ReporteDiariosBean(new Date(), "Dólares", "MXD", "Gerry Bass Ganja", new Integer(658),new Double(125000000), new Double(11111), new Double(11.89), "Katia Alarcón Cruz", new Double(12838.00),new Date(), new Date(), "Totalmente Liq.",new Integer(001), new Boolean(true), image);
		ReporteDiariosBean bean2 = new ReporteDiariosBean(new Date(), "Dólares", "MXD", "Andres Manuel Obrador", new Integer(658),new Double(10000), new Double(11111), new Double(11.89), "Gloria Corzo Fuentes", new Double(12838.00),new Date(), new Date(), "Contabilizado",new Integer(002), new Boolean(true), image);
		ReporteDiariosBean bean3 = new ReporteDiariosBean(new Date(), "Dólares", "MXD", "Gloria", new Integer(658),new Double(10000), new Double(11111), new Double(11.89), "Chano Corzo Fuentes", new Double(12838.00),new Date(), new Date(), "AL",new Integer(003), new Boolean(true), image);
		ReporteDiariosBean bean4 = new ReporteDiariosBean(new Date(), "Dólares", "MXD", "Gloria", new Integer(658),new Double(10000), new Double(11111), new Double(11.89), "Gloria Coprzo Fuentes", new Double(12838.00),new Date(), new Date(), "AL",new Integer(001), new Boolean(false), image);
		ReporteDiariosBean bean5 = new ReporteDiariosBean(new Date(), "Dólares", "MXD", "Gloria", new Integer(658),new Double(10000), new Double(11111), new Double(11.89), "Edgar elGary Fuentes", new Double(12838.00),new Date(), new Date(), "AL",new Integer(002), new Boolean(false), image);
		ReporteDiariosBean bean6 = new ReporteDiariosBean(new Date(), "Dólares", "MXD", "Nadia", new Integer(658),new Double(10000), new Double(11111), new Double(11.89), "Gloria Corzo Herrera", new Double(12838.00),new Date(), new Date(), "AL",new Integer(003), new Boolean(false), image);
		l.add(bean1);
		l.add(bean2);
		l.add(bean3);
		l.add(bean4);
		l.add(bean5);
		l.add(bean6);
		return new JRBeanCollectionDataSource(l);
	}

	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#dispose(net.sf.jasperreports.engine.JRDataSource)
	 */
	public void dispose(JRDataSource arg0) throws JRException {
	}

}
