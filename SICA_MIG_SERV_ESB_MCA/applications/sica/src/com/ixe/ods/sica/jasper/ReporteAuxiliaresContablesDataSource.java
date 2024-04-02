/*
 * $Id: ReporteAuxiliaresContablesDataSource.java,v 1.10 2008/02/22 18:25:03 ccovian Exp $
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
 * Permite generar un template del reporte de Utilidades de Mayoreo Contable.
 *
 * @author Edgar Ivan Leija
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:03 $
 */
public class ReporteAuxiliaresContablesDataSource extends JRAbstractBeanDataSourceProvider{
	
	/**
	 * Constructor de la clase Haciendo referencia al
	 * Bean a Utilizar
	 */
	public ReporteAuxiliaresContablesDataSource() {
		super(ReporteAuxiliaresContablesBean.class);		
	}
	
	/**
	 * Crea un reporte de prueba con los datos especificados.
	 * 
	 * @param JasperReport Reporte de prueba.
	 * @throws JRException.
	 * @return JRDataSource.
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
		InputStream image = null;
		ReporteAuxiliaresContablesBean b1 = new ReporteAuxiliaresContablesBean("Moneda Nacional", gc.getTime(), "1103-04-00-024-23", "1931-1", new Double(0.00), new Double(20000.00), "L Compra Transferencia", "Intercam", "AMIN", "20000000-1");
		ReporteAuxiliaresContablesBean b2 = new ReporteAuxiliaresContablesBean("Moneda Nacional", gc.getTime(), "1103-04-00-024-23", "1931-2",new Double(0.00), new Double(50000.00), "L Compra Transferencia", "Intercam", "AMIN", "200002446-2");
		ReporteAuxiliaresContablesBean b3 = new ReporteAuxiliaresContablesBean("Dolar Americano", gc.getTime(), "126891GF225366000000000000000", "1932-1", new Double(10000.50), new Double(0.00), "L Venta Transferencia", "JP Morgan NY", "JPM", "200023545-2");
		ReporteAuxiliaresContablesBean b4 = new ReporteAuxiliaresContablesBean("Dolar Americano", gc.getTime(), "126891GF225366000000000000000", "1932-2", new Double(20000.50), new Double(0.00), "L Venta Efectivo", "JP Morgan NY", "JPM", "2006598987-1");
		ReporteAuxiliaresContablesBean b5 = new ReporteAuxiliaresContablesBean("Euros", gc.getTime(), "126891GF898565000000000000000", "1940-1", new Double(0.00), new Double(5000000.00), "L Compra Documento Extranjero", "Televisa", "TLV", "2001321654-7");
		ReporteAuxiliaresContablesBean b6 = new ReporteAuxiliaresContablesBean("Euros", gc.getTime(), "126891GF898565000000000000000", "1940-2", new Double(0.00), new Double(2000000.00), "L Compra Documento Extranjero", "Televisa", "TLV", "2002315648-8", image);
		ReporteAuxiliaresContablesBean b7 = new ReporteAuxiliaresContablesBean("Euros", gc.getTime(), "126891GF898565000000000000000", "1949-3", new Double(0.00), new Double(3000000.00), "L Compra Documento Extranjero", "Televisa", "TLV", "2001321654-7");
		ReporteAuxiliaresContablesBean b8 = new ReporteAuxiliaresContablesBean("Euros", gc.getTime(), "126891GF898565000000000000000", "1941-4", new Double(3000000.00), new Double(0.0), "L Venta Documento Extranjero", "Televisa", "TLV", "2001321654-7");
		ReporteAuxiliaresContablesBean b9 = new ReporteAuxiliaresContablesBean("Euros", gc.getTime(), "126891GF898565000000000000000", "1949-5", new Double(4000000.00), new Double(0.0), "L Venta Documento Extranjero", "Televisa", "TLV", "2001321654-7");
		ReporteAuxiliaresContablesBean b10 = new ReporteAuxiliaresContablesBean("Euros", gc.getTime(), "126891GF898565000000000000000", "1941-6", new Double(5000000.00), new Double(0.0), "L Venta Documento Extranjero", "Televisa", "TLV", "2001321654-7");
		l.add(b1);
		l.add(b2);
		l.add(b3);
		l.add(b4);
		l.add(b5);
		l.add(b6);
		l.add(b7);
		l.add(b8);
		l.add(b9);
		l.add(b10);
		return new JRBeanCollectionDataSource(l);
	}

	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#dispose(net.sf.jasperreports.engine.JRDataSource)
	 */
	public void dispose(JRDataSource arg0) throws JRException {
	}
}
