/*
 * $Id: ReporteLinCredAutDataSource.java,v 1.10 2008/02/22 18:25:04 ccovian Exp $
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
 * que permite probar el reporte de las l&iacute;neas de Cr&eacute;dito
 * autorizadas
 *
 * @author Gerardo Corzo Herrera
 */
public class ReporteLinCredAutDataSource extends JRAbstractBeanDataSourceProvider{

	/**
	 * Constructor de la clase Haciendo referencia al
	 * Bean a Utilizar
	 * 
	 * @param ReporteLinCredAutBean
	 */
	public ReporteLinCredAutDataSource() {
		super(ReporteLinCredAutBean.class);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#create(net.sf.jasperreports.engine.JasperReport)
	 */
	public JRDataSource create(JasperReport arg0) throws JRException {
		InputStream image = null;
		ArrayList l = new ArrayList();
		ReporteLinCredAutBean bean1 = new ReporteLinCredAutBean("Alta", "Transferencia", new Date(), new Date(), new Integer(658), "USD", new Double(1000000),
				new Double(1000900), "ALESTRA S.A de C.V.",	"ACTIVA", new Date(), new Date(), new Double(10000), new Double(23000), new Integer(2), new Integer(0),
				"Transferencia", image, "JOSE SALDANA", new Double(120202), new Double(1000000), new Double(120000));
		ReporteLinCredAutBean bean2 = new ReporteLinCredAutBean("Alta", "Transferencia", new Date(), new Date(), new Integer(658), "USD", new Double(1000000),
				new Double(1000900), "ALESTRA S.A de C.V.",	"ACTIVA", new Date(), new Date(), new Double(10000), new Double(23000), new Integer(2), new Integer(0), 
				"Transferencia", image, "JOSE SALDANA", new Double(120202), new Double(1000000), new Double(120000));
		ReporteLinCredAutBean bean3 = new ReporteLinCredAutBean("Alta", "Transferencia", new Date(), new Date(), new Integer(658), "USD", new Double(1000000),
				new Double(1000900), "ALESTRA S.A de C.V.",	"ACTIVA", new Date(), new Date(), new Double(10000), new Double(23000), new Integer(2), new Integer(0),
				"Transferencia", image, "JOSE SALDANA", new Double(120202), new Double(1000000), new Double(120000));
		ReporteLinCredAutBean bean4 = new ReporteLinCredAutBean("Alta", "Transferencia", new Date(), new Date(), new Integer(658), "USD", new Double(1000000),
				new Double(1000900) , "ALESTRA S.A de C.V.",	"ACTIVA", new Date(), new Date(), new Double(10000), new Double(23000),new Integer(2), new Integer(0),
				"Transferencia", image, "JOSE SALDANA", new Double(120202), new Double(1000000), new Double(120000));
		ReporteLinCredAutBean bean5 = new ReporteLinCredAutBean("Alta", "Transferencia", new Date(), new Date(), new Integer(658), "USD", new Double(1000000),
				new Double(1000900), "ALESTRA S.A de C.V.",	"ACTIVA", new Date(), new Date(), new Double(10000), new Double(23000), new Integer(2), new Integer(0),
				"Transferencia", image, "JOSE SALDANA", new Double(120202), new Double(1000000), new Double(120000));
		ReporteLinCredAutBean bean6 = new ReporteLinCredAutBean("Alta", "Transferencia", new Date(), new Date(), new Integer(658), "USD", new Double(1000000),
				new Double(1000900), "ALESTRA S.A de C.V.",	"ACTIVA", new Date(), new Date(), new Double(10000), new Double(23000), new Integer(2), new Integer(0),
				"Transferencia", image, "JOSE SALDANA", new Double(120202), new Double(1000000), new Double(120000));
		ReporteLinCredAutBean bean7 = new ReporteLinCredAutBean("Alta", "Remesas en Camino", new Date(), new Date(), new Integer(658), "USD", new Double(1000000),
				new Double(1000900), "ALESTRA S.A de C.V.",	"ACTIVA", new Date(), new Date(), new Double(10000), new Double(23000), new Integer(2), new Integer(0),
				"Remesas en Camino", image, "JOSE SALDANA", new Double(120202), new Double(1000000), new Double(120000));
		ReporteLinCredAutBean bean8 = new ReporteLinCredAutBean("Alta", "Remesas en Camino", new Date(), new Date(), new Integer(658), "USD", new Double(1000000),
				new Double(1000900), "ALESTRA S.A de C.V.",	"ACTIVA", new Date(), new Date(), new Double(10000), new Double(23000), new Integer(2), new Integer(0), 
				"Remesas en Camino", image, "JOSE SALDANA", new Double(120202), new Double(1000000), new Double(120000));
		ReporteLinCredAutBean bean9 = new ReporteLinCredAutBean("Alta", "Remesas en Camino", new Date(), new Date(), new Integer(658), "USD", new Double(1000000),
				new Double(1000900), "ALESTRA S.A de C.V.",	"ACTIVA", new Date(), new Date(), new Double(10000), new Double(23000), new Integer(2), new Integer(0),
				"Remesas en Camino", image, "JOSE SALDANA", new Double(120202), new Double(1000000), new Double(120000));
		ReporteLinCredAutBean bean10 = new ReporteLinCredAutBean("Alta", "Remesas en Camino", new Date(), new Date(), new Integer(658), "USD", new Double(1000000),
				new Double(1000900) , "ALESTRA S.A de C.V.",	"ACTIVA", new Date(), new Date(), new Double(10000), new Double(23000),new Integer(2), new Integer(0),
				"Remesas en Camino", image, "JOSE SALDANA", new Double(120202), new Double(1000000), new Double(120000));
		ReporteLinCredAutBean bean11 = new ReporteLinCredAutBean("Alta", "Otro Tipo", new Date(), new Date(), new Integer(658), "USD", new Double(1000000),
				new Double(1000900), "ALESTRA S.A de C.V.",	"ACTIVA", new Date(), new Date(), new Double(10000), new Double(23000), new Integer(2), new Integer(0),
				"Otro Tipo", image, "JOSE SALDANA", new Double(120202), new Double(1000000), new Double(120000));
		ReporteLinCredAutBean bean12 = new ReporteLinCredAutBean("Alta", "Otro Tipo", new Date(), new Date(), new Integer(658), "USD", new Double(1000000),
				new Double(1000900), "ALESTRA S.A de C.V.",	"ACTIVA", new Date(), new Date(), new Double(10000), new Double(23000), new Integer(2), new Integer(0),
				"Otro Tipo", image, "JOSE SALDANA", new Double(120202), new Double(1000000), new Double(120000));
		l.add(bean1);
		l.add(bean2);
		l.add(bean3);
		l.add(bean4);
		l.add(bean5);
		l.add(bean6);
		l.add(bean7);
		l.add(bean8);
		l.add(bean9);
		l.add(bean10);
		l.add(bean11);
		l.add(bean12);
		return new JRBeanCollectionDataSource(l);
	}

	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#dispose(net.sf.jasperreports.engine.JRDataSource)
	 */
	public void dispose(JRDataSource arg0) throws JRException {
	}

}
