/*
 * $Id: RepUtMayoreoContableDataSource.java,v 1.11 2008/02/22 18:25:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
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
 * Permite modificar el Reporte de Utilidades de Mayoreo Contable.
 *
 * @author Edgar Ivan Leija
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:04 $
 */
public class RepUtMayoreoContableDataSource extends JRAbstractBeanDataSourceProvider {
	
	/**
	 * Constructor por default.
	 *
	 */
	public RepUtMayoreoContableDataSource() {
		super(RepUtMayoreoContableBean.class);
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
		RepUtMayoreoContableBean repBeanUSD = new RepUtMayoreoContableBean(image,"Mesa Operacion","Deal Sucursal","Peseta",new Double(100.3),new Double (99.7),new Double(100.3),new Double (99.7),new Double(98.7),
				new Double (20),new Double (64646),new Double (10),new Double (32.2),new Date());
		RepUtMayoreoContableBean repBeanPESOS = new RepUtMayoreoContableBean(image,"Mesa Operacion","Deal Normal","Pesos",new Double(100.3),new Double (99.7),new Double(10.3),new Double (89.7),new Double(98.7),
				new Double (1343242),new Double (64346),new Double (12.4),new Double (32.2),new Date());
		RepUtMayoreoContableBean repBeanUSD1 = new RepUtMayoreoContableBean(image,"Mesa Operacion","Deal Sucursal","Peseta",new Double(100.3),new Double (99.7),new Double(100.3),new Double (99.7),new Double(98.7),
				new Double (50),new Double (64646),new Double (10),new Double (32.2),new Date());
		RepUtMayoreoContableBean repBeanYEN1 = new RepUtMayoreoContableBean(image,"Mesa Operacion","Deal Normal","Yenes",new Double(100.3),new Double (99.7),new Double(10.3),new Double (89.7),new Double(98.7),
				new Double (1343242),new Double (64346),new Double (12.4),new Double (32.2),new Date());
		RepUtMayoreoContableBean repBeanUSD11 = new RepUtMayoreoContableBean(image,"Mesa Operacion","Deal Sucursal","Dolares",new Double(100.3),new Double (99.7),new Double(100.3),new Double (99.7),new Double(98.7),
				new Double (13242),new Double (64646),new Double (12.5),new Double (32.2),new Date());
		RepUtMayoreoContableBean repBeanPESO1 = new RepUtMayoreoContableBean(image,"Mesa Dolares","Deal Normal","Pesos",new Double(100.3),new Double (99.7),new Double(10.3),new Double (89.7),new Double(98.7),
				new Double (1343242),new Double (64346),new Double (12.4),new Double (32.2),new Date());
		RepUtMayoreoContableBean repBeanUSD12 = new RepUtMayoreoContableBean(image,"Mesa Operacion","Deal Sucursal","Dolares",new Double(100.3),new Double (99.7),new Double(100.3),new Double (99.7),new Double(98.7),
				new Double (13242),new Double (64646),new Double (12.5),new Double (32.2),new Date());
		RepUtMayoreoContableBean repBeanYEN12 = new RepUtMayoreoContableBean(image,"Mesa Operacion","Deal Normal","Yenes",new Double(100.3),new Double (99.7),new Double(10.3),new Double (89.7),new Double(98.7),
				new Double (1343242),new Double (64346),new Double (12.4),new Double (32.2),new Date());
		RepUtMayoreoContableBean repBeanUSD2 = new RepUtMayoreoContableBean(image,"Mesa Dolares","Deal Sucursal","Dolares",new Double(100.3),new Double (99.7),new Double(100.3),new Double (99.7),new Double(98.7),
				new Double (13242),new Double (64646),new Double (12.5),new Double (32.2),new Date());
		RepUtMayoreoContableBean repBeanPESOS2 = new RepUtMayoreoContableBean(image,"Mesa Dolares","Deal Normal","Pesos",new Double(100.3),new Double (99.7),new Double(10.3),new Double (89.7),new Double(98.7),
				new Double (1343242),new Double (64346),new Double (12.4),new Double (32.2),new Date());
		RepUtMayoreoContableBean repBeanUSD13 = new RepUtMayoreoContableBean(image,"Mesa Dolares","Deal Sucursal","Dolares",new Double(100.3),new Double (99.7),new Double(100.3),new Double (99.7),new Double(98.7),
				new Double (13242),new Double (64646),new Double (12.5),new Double (32.2),new Date());
		RepUtMayoreoContableBean repBeanYEN13 = new RepUtMayoreoContableBean(image,"Mesa Operacion","Deal Normal","Yenes",new Double(100.3),new Double (99.7),new Double(10.3),new Double (89.7),new Double(98.7),
				new Double (1343242),new Double (64346),new Double (12.4),new Double (32.2),new Date());
		RepUtMayoreoContableBean repBeanUSD3 = new RepUtMayoreoContableBean(image,"Mesa Dolares","Deal Sucursal","Dolares",new Double(100.3),new Double (99.7),new Double(100.3),new Double (99.7),new Double(98.7),
				new Double (13242),new Double (64646),new Double (12.5),new Double (32.2),new Date());
		RepUtMayoreoContableBean repBeanPESOS3 = new RepUtMayoreoContableBean(image,"Mesa Dolares","Deal Normal","Pesos",new Double(100.3),new Double (99.7),new Double(10.3),new Double (89.7),new Double(98.7),
				new Double (1343242),new Double (64346),new Double (12.4),new Double (32.2),new Date());
		RepUtMayoreoContableBean repBeanUSD14 = new RepUtMayoreoContableBean(image,"Mesa Dolares","Deal Sucursal","Dolares",new Double(100.3),new Double (99.7),new Double(100.3),new Double (99.7),new Double(98.7),
				new Double (13242),new Double (64646),new Double (12.5),new Double (32.2),new Date());
		RepUtMayoreoContableBean repBeanYEN14 = new RepUtMayoreoContableBean(image,"Mesa Dolares","Deal Normal","Yenes",new Double(100.3),new Double (99.7),new Double(10.3),new Double (89.7),new Double(98.7),
				new Double (1343242),new Double (64346),new Double (12.4),new Double (32.2),new Date());
		//mesa uno
		//sucursal
		elementos.add(repBeanUSD);
		elementos.add(repBeanUSD1);
		elementos.add(repBeanUSD11);
		elementos.add(repBeanUSD12);
		//normal
		elementos.add(repBeanPESOS);
		elementos.add(repBeanYEN1);
		elementos.add(repBeanYEN13);
		elementos.add(repBeanYEN12);
		//mesa dos
		//sucursal
		elementos.add(repBeanUSD2);
		elementos.add(repBeanUSD13);
		elementos.add(repBeanUSD3);
		elementos.add(repBeanUSD14);
		//normal
		elementos.add(repBeanPESO1);
		elementos.add(repBeanPESOS2);
		elementos.add(repBeanPESOS3);
		elementos.add(repBeanYEN14);
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
