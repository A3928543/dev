/*
 * $Id: ReporteDealsPagoAntDataSource.java,v 1.10 2008/02/22 18:25:03 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSourceProvider;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Clase que crea un Data Source,con un conjunto de beans,
 * que permite probar el reporte de Deals con pago anticipado 
 *
 * @author Gerardo Corzo Herrera
 */
public class ReporteDealsPagoAntDataSource extends	JRAbstractBeanDataSourceProvider {

	/**
	 * Constructor de la clase Haciendo referencia al
	 * Bean a Utilizar
	 */
	public ReporteDealsPagoAntDataSource() {
		super(ReporteDealsPagoAntBean.class);
	}

	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#create(net.sf.jasperreports.engine.JasperReport)
	 */
	public JRDataSource create(JasperReport jasperReport) throws JRException {

		ArrayList l = new ArrayList();
		InputStream image = null;
		ReporteDealsPagoAntBean bean1 = new ReporteDealsPagoAntBean(new Date(),"MXD", "Gerardo Corzo Herrera", new Integer(604), new Double(10000), new Date(), "OK", new Integer (1), "123456789-0", "S", image);
		ReporteDealsPagoAntBean bean2 = new ReporteDealsPagoAntBean(new Date(),"TRF", "Gerardo Corzo Herrera", new Integer(605), new Double(10000.45), new Date(), "OK", new Integer (2), "123456789-0", "S", image);
		ReporteDealsPagoAntBean bean3 = new ReporteDealsPagoAntBean(new Date(),"TRF", "Gerardo Corzo Herrera", new Integer(606), new Double(10000.46), new Date(), "OK", new Integer (3), "123456789-0", "S", image);
		ReporteDealsPagoAntBean bean4 = new ReporteDealsPagoAntBean(new Date(),"MXD", "Gerardo Corzo Herrera", new Integer(607), new Double(10000.89), new Date(), "OK", new Integer (4), "123456789-1", "N", image);
		ReporteDealsPagoAntBean bean5 = new ReporteDealsPagoAntBean(new Date(),"MXD", "Gerardo Corzo Herrera", new Integer(608), new Double(10000.67), new Date(), "OK", new Integer (5), "123456789-1", "N" , image);
		ReporteDealsPagoAntBean bean6 = new ReporteDealsPagoAntBean(new Date(),"TRF", "Gerardo Corzo Herrera", new Integer(609), new Double(10000.12), new Date(), "OK", new Integer (6), "123456789-1s", "N", image);
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
