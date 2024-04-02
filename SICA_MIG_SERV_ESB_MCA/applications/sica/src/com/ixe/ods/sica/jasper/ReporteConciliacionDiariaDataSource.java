/*
 * $Id: ReporteConciliacionDiariaDataSource.java,v 1.10 2008/02/22 18:25:04 ccovian Exp $
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
import java.util.*;

/**
 * Data Source de prueba para el Reporte de Conciliacion Diaria
 * 
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:04 $
 */
public class ReporteConciliacionDiariaDataSource extends JRAbstractBeanDataSourceProvider{
	
	/**
	 * Constructor de la clase Haciendo referencia al Bean a Utilizar.
	 */
	public ReporteConciliacionDiariaDataSource() {
		super(ReporteConciliacionDiariaBean.class);		
	}
	
	/**
	 * Data source de prueba para el reporte de Conciliacion Diaria
	 * 
	 * @param arg0 arg0 Reporte de Prueba 
	 * @return JRDataSource
	 */
	public JRDataSource create(JasperReport arg0) throws JRException {
		List l = new ArrayList();
		Date d = new Date();
		Calendar gc = new GregorianCalendar();
		gc.setTime(d);
		gc.add(Calendar.DAY_OF_YEAR, -1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.MILLISECOND, 0);
		Calendar gca = new GregorianCalendar();
		gca.setTime(d);
		gca.add(Calendar.DAY_OF_YEAR, -2);
		gca.set(Calendar.HOUR_OF_DAY, 0);
		gca.set(Calendar.SECOND, 0);
		gca.set(Calendar.MINUTE, 0);
		gca.set(Calendar.MILLISECOND, 0);
		InputStream image = null;
		ReporteConciliacionDiariaBean b1 = new ReporteConciliacionDiariaBean(new Date(), "Dolar Americano", "OPERACION ", "MXN", "HOY", new Double(25000.50), new Double(30000.00), new Double(5000000.00),
				"TOM", new Double(25000.00), new Double(30000.00), new Double(2000000.00),
				"SPOT", new Double(20000.00), new Double(40000.00), new Double(5000000.00),
				"72HR", new Double(30000.00), new Double(10000.00), new Double(6000000.00),
				"VFUT", new Double(20000.00), new Double(20000.00), new Double(7000000.00),
				gc.getTime(), new Double (1000000000),
				"TOM", gc.getTime(), new Double (100000.00), new Double (200000.00), new Double (500000.00),  
				"SPOT", gca.getTime(), new Double (300000.00), new Double (400000.00), new Double (200000.00),
				"72HR", gca.getTime(), new Double (200000.00), new Double (100000.00), new Double (300000.00),
				"VFUT", gca.getTime(), new Double (100000.00), new Double (200000.00), new Double (500000.00),
				"HOY", new Double(400000.00), new Double(500000.00),  new Double(700000.00), new Double(250000000.00), image);
		ReporteConciliacionDiariaBean b2 = new ReporteConciliacionDiariaBean(new Date(), "Euro", "OPERACION ", "MXN", "HOY", new Double(35000.50), new Double(40000.00), new Double(7000000.00),
				"TOM", new Double(25000.00), new Double(30000.00), new Double(2000000.00),
				"SPOT", new Double(20000.00), new Double(40000.00), new Double(5000000.00),
				null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, new Double(0.00), new Double(0.00), new Double(0.00),
				gc.getTime(), new Double (1000000000),
				"TOM", gc.getTime(), new Double (100000.00), new Double (200000.00), new Double (500000.00),  
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				"HOY", new Double(400000.00), new Double(500000.00),  new Double(700000.00), new Double(250000000.00), image);
		ReporteConciliacionDiariaBean b3 = new ReporteConciliacionDiariaBean(new Date(), "Euro", "OPERACION ", "MXN", "HOY", new Double(35000.50), new Double(40000.00), new Double(7000000.00),
				"TOM", new Double(25000.00), new Double(30000.00), new Double(2000000.00),
				"SPOT", new Double(20000.00), new Double(40000.00), new Double(5000000.00),
				null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, new Double(0.00), new Double(0.00), new Double(0.00),
				gc.getTime(), new Double (1000000000),
				"TOM", gc.getTime(), new Double (100000.00), new Double (200000.00), new Double (500000.00),  
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				"HOY", new Double(400000.00), new Double(500000.00),  new Double(700000.00), new Double(250000000.00), image);
		ReporteConciliacionDiariaBean b5 = new ReporteConciliacionDiariaBean(new Date(), "Dolar Americano", "TRADER USD", "USD", "HOY", new Double(35000.50), new Double(40000.00), new Double(7000000.00),
				"TOM", new Double(25000.00), new Double(30000.00), new Double(2000000.00),
				"SPOT", new Double(20000.00), new Double(40000.00), new Double(5000000.00),
				null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, new Double(0.00), new Double(0.00), new Double(0.00),
				gc.getTime(), new Double (1000000000),
				"TOM", gc.getTime(), new Double (100000.00), new Double (200000.00), new Double (500000.00),  
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				"HOY", new Double(400000.00), new Double(500000.00),  new Double(700000.00), new Double(250000000.00), image);
		ReporteConciliacionDiariaBean b6 = new ReporteConciliacionDiariaBean(new Date(), "Dolar Americano", "TRADER USD", "USD", "HOY", new Double(35000.50), new Double(40000.00), new Double(7000000.00),
				"TOM", new Double(25000.00), new Double(30000.00), new Double(2000000.00),
				"SPOT", new Double(20000.00), new Double(40000.00), new Double(5000000.00),
				null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, new Double(0.00), new Double(0.00), new Double(0.00),
				gc.getTime(), new Double (1000000000),
				"TOM", gc.getTime(), new Double (100000.00), new Double (200000.00), new Double (500000.00),  
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				"HOY", new Double(400000.00), new Double(500000.00),  new Double(700000.00), new Double(250000000.00), image);
		ReporteConciliacionDiariaBean b7 = new ReporteConciliacionDiariaBean(new Date(), "Dolar Americano", "TRADER MXN", "MXN", "HOY", new Double(35000.50), new Double(40000.00), new Double(7000000.00),
				"TOM", new Double(25000.00), new Double(30000.00), new Double(2000000.00),
				"SPOT", new Double(20000.00), new Double(40000.00), new Double(5000000.00),
				null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, new Double(0.00), new Double(0.00), new Double(0.00),
				gc.getTime(), new Double (1000000000),
				"TOM", gc.getTime(), new Double (100000.00), new Double (200000.00), new Double (500000.00),  
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				"HOY", new Double(400000.00), new Double(500000.00),  new Double(700000.00), new Double(250000000.00), image);
		ReporteConciliacionDiariaBean b9 = new ReporteConciliacionDiariaBean(new Date(), "Libra Esterlina", "TRADER MXN", "MXN", "HOY", new Double(35000.50), new Double(40000.00), new Double(7000000.00),
				"TOM", new Double(25000.00), new Double(30000.00), new Double(2000000.00),
				"SPOT", new Double(20000.00), new Double(40000.00), new Double(5000000.00),
				null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, new Double(0.00), new Double(0.00), new Double(0.00),
				gc.getTime(), new Double (1000000000),
				"TOM", gc.getTime(), new Double (100000.00), new Double (200000.00), new Double (500000.00),  
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				"HOY", new Double(400000.00), new Double(500000.00),  new Double(700000.00), new Double(250000000.00), image);
		ReporteConciliacionDiariaBean b8 = new ReporteConciliacionDiariaBean(new Date(), "Euro", "FORWARDS", "MXN", "HOY", new Double(35000.50), new Double(40000.00), new Double(7000000.00),
				"TOM", new Double(25000.00), new Double(30000.00), new Double(2000000.00),
				"SPOT", new Double(20000.00), new Double(40000.00), new Double(5000000.00),
				null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, new Double(0.00), new Double(0.00), new Double(0.00),
				gc.getTime(), new Double (1000000000),
				"TOM", gc.getTime(), new Double (100000.00), new Double (200000.00), new Double (500000.00),  
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				null, null, new Double(0.00), new Double(0.00), new Double(0.00),
				"HOY", new Double(400000.00), new Double(500000.00),  new Double(700000.00), new Double(250000000.00), image);
		l.add(b1);
		l.add(b2);
		l.add(b3);
		l.add(b5);
		l.add(b6);
		l.add(b7);
		l.add(b8);
		l.add(b9);
		return new JRBeanCollectionDataSource(l);
	}

	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#dispose(net.sf.jasperreports.engine.JRDataSource)
	 */
	public void dispose(JRDataSource arg0) throws JRException {
	}
}
