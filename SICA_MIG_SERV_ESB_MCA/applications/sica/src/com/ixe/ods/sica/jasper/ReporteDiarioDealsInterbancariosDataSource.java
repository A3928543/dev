/*
 * $Id: ReporteDiarioDealsInterbancariosDataSource.java,v 1.10 2008/02/22 18:25:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSourceProvider;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Data Source de prueba para el Reporte Diario de Deals Interbancarios.
 * 
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:04 $
 */
public class ReporteDiarioDealsInterbancariosDataSource extends JRAbstractBeanDataSourceProvider {

	/**
	 * Constructor de la clase Haciendo referencia al Bean a Utilizar.
	 */
	public ReporteDiarioDealsInterbancariosDataSource() {
		super(ReporteDiarioDealsInterbancariosBean.class);
	}
	
	/**
	 * Data source de prueba para el reporte de Conciliacion Diaria
	 * 
	 * @param arg0 arg0 Reporte de Prueba 
	 * @return JRDataSource
	 */
	public JRDataSource create(JasperReport jasperReport) throws JRException {
		List l = new ArrayList();

		ReporteDiarioDealsInterbancariosBean bean1 = new ReporteDiarioDealsInterbancariosBean("USD", new Date(), new Integer(1525), new Integer(1), "SPOT", new Date(), "Compra", "Grupo ODEM", "Transferencia", new Double(12.05), new Double(15000), new Double(15000*12.05));
		ReporteDiarioDealsInterbancariosBean bean2 = new ReporteDiarioDealsInterbancariosBean("USD", new Date(), new Integer(1526), new Integer(1), "SPOT", new Date(), "Compra", "Grupo ODEM", "Transferencia", new Double(12.05), new Double(15000), new Double(15000*12.05));
		ReporteDiarioDealsInterbancariosBean bean3 = new ReporteDiarioDealsInterbancariosBean("EUR", new Date(), new Integer(1527), new Integer(1), "SPOT", new Date(), "Compra", "Grupo ODEM", "Transferencia", new Double(12.05), new Double(15000), new Double(15000*12.05));
		ReporteDiarioDealsInterbancariosBean bean4 = new ReporteDiarioDealsInterbancariosBean("EUR", new Date(), new Integer(1527), new Integer(2), "SPOT", new Date(), "Compra", "Grupo ODEM", "Transferencia", new Double(12.05), new Double(15000), new Double(15000*12.05));
		ReporteDiarioDealsInterbancariosBean bean5 = new ReporteDiarioDealsInterbancariosBean("EUR", new Date(), new Integer(1528), new Integer(1), "SPOT", new Date(), "Compra", "Grupo ODEM", "Transferencia", new Double(12.05), new Double(15000), new Double(15000*12.05));
		ReporteDiarioDealsInterbancariosBean bean6 = new ReporteDiarioDealsInterbancariosBean("GBP", new Date(), new Integer(1529), new Integer(1), "SPOT", new Date(), "Compra", "Grupo ODEM", "Transferencia", new Double(12.05), new Double(15000), new Double(15000*12.05));

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
