/*
 * $Id: ReportePolizasContablesResumenDataSource.java,v 1.10 2008/02/22 18:25:04 ccovian Exp $
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
 * Clase que crea un Data Source,con un conjunto de beans,
 * que permite probar el reporte de Polizas Contables.
 *
 * @author Gustavo Gonzalez
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:04 $
 */
public class ReportePolizasContablesResumenDataSource  extends JRAbstractBeanDataSourceProvider{
	
	/**
	 * Constructor de la clase Haciendo referencia al Bean a Utilizar
	 */
	public ReportePolizasContablesResumenDataSource() {
		super(ReportePolizasContablesResumenBean.class);		
	}
	
	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#create(net.sf.jasperreports.engine.JasperReport)
	 */
	public JRDataSource create(JasperReport arg0) throws JRException {
		List l = new ArrayList();
		Date d = new Date();
		Calendar gc = new GregorianCalendar();
		gc.setTime(d);
		gc.add(Calendar.DAY_OF_YEAR,-1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.MILLISECOND, 0);
		InputStream image = null;
		ReportePolizasContablesResumenBean b1 = new ReportePolizasContablesResumenBean("Moneda Nacional", gc.getTime(), "1103-04-00-024-23", new Double(16000.00), new Double(20000.00), "ASDF");
		ReportePolizasContablesResumenBean b2 = new ReportePolizasContablesResumenBean("Moneda Nacional", gc.getTime(), "1103-04-00-024-23", new Double(4000.00), new Double(50000.00), "ZXCXX");
		ReportePolizasContablesResumenBean b3 = new ReportePolizasContablesResumenBean("Moneda Nacional", gc.getTime(), "126891GF225366000000000000000", new Double(10000.50), new Double(5000000.00), "ASDF");
		ReportePolizasContablesResumenBean b4 = new ReportePolizasContablesResumenBean("Moneda Nacional", gc.getTime(), "126891GF225366000000000000000", new Double(20000.50), new Double(4000000.00), "SDDD");
		ReportePolizasContablesResumenBean b5 = new ReportePolizasContablesResumenBean("Moneda Nacional", gc.getTime(), "126891GF898565000000000000000", new Double(16524.50), new Double(5000000.35), "WET");
		ReportePolizasContablesResumenBean b6 = new ReportePolizasContablesResumenBean("Moneda Nacional", gc.getTime(), "126891GF898565000000000000000", new Double(16524.50), new Double(5000000.35), "ASDF", image);
		l.add(b1);
		l.add(b2);
		l.add(b3);
		l.add(b4);
		l.add(b5);
		l.add(b6);
		return new JRBeanCollectionDataSource(l);
	}

	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#dispose(net.sf.jasperreports.engine.JRDataSource)
	 */
	public void dispose(JRDataSource arg0) throws JRException {
	}
}
