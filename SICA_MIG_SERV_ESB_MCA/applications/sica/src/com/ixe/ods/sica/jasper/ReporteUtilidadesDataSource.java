/*
 * $Id: ReporteUtilidadesDataSource.java,v 1.11 2008/02/22 18:25:04 ccovian Exp $
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
 * Clase que crea un Data Source,con un conjunto de beans,
 * que permite probar el reporte de Utilidades 
 *
 * @author Gerardo Corzo Herrera
 */
public class ReporteUtilidadesDataSource extends JRAbstractBeanDataSourceProvider {

	/**
	 * Constructor de la clase Haciendo referencia al
	 * Bean a Utilizar
	 * 
	 * @param ReporteUtilidadesBean
	 */
	public ReporteUtilidadesDataSource() {
		super(ReporteUtilidadesBean.class);		
	}

	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#create(net.sf.jasperreports.engine.JasperReport)
	 */
	public JRDataSource create(JasperReport arg0) throws JRException {
	List l = new ArrayList();
	InputStream image = null;
     ReporteUtilidadesBean bean = new ReporteUtilidadesBean("Cadbury Aguas Minerales", new Double(0.00), new Double(0.00), new Date(),new Integer(105651), new Integer(0), new Double(33750), "Transferencia",
    		 new Double(0.023000), new Double (10.764500), "Venta", new Double (776.25),"Dólar", "DANIEL SALDANA SOSA", new Date(), new Date(), "<PMY>", "<981399>", 
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.TRUE);
     ReporteUtilidadesBean bean1 = new ReporteUtilidadesBean("Sherwin Williams", new Double(0.00), new Double(45), new Date(),new Integer(105882), new Integer(0), new Double(24750), "Documento",
    		 new Double(0.041760), new Double (10.693500), "Compra", new Double (1033.56),"Dólar", "DANIEL SALDANA SOSA", new Date(), new Date(), "<PMY>", "<981399>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.TRUE);
     
     ReporteUtilidadesBean bean2 = new ReporteUtilidadesBean("Epson México", new Double(0.00), new Double(45), new Date(),new Integer(105882), new Integer(1), new Double(15250), "Documento",
    		 new Double(0.041760), new Double (10.693500), "Compra", new Double (636.84),"Dólar", "DANIEL SALDANA SOSA", new Date(), new Date(), "<PMY>", "<981399>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.TRUE);
     ReporteUtilidadesBean bean3 = new ReporteUtilidadesBean("Promoción y Operación", new Double(0.00), new Double(0.00), new Date(),new Integer(105927), new Integer(0), new Double(27750), "Mexdolar",
    		 new Double(0.067580), new Double (10.735000), "Venta", new Double (1875.35),"Dólar", "DANIEL SALDANA SOSA", new Date(), new Date(), "<PMY>", "<981399>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.TRUE);
     ReporteUtilidadesBean bean4 = new ReporteUtilidadesBean("Promoción y Operación", new Double(0.00), new Double(0.00), new Date(),new Integer(106012), new Integer(0), new Double(6250), "Efectivo",
    		 new Double(0.002500), new Double (10.715000), "Compra", new Double (15.63),"Dólar", "DANIEL SALDANA SOSA", new Date(), new Date(), "<PMY>", "<981399>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.TRUE);
     ReporteUtilidadesBean bean5 = new ReporteUtilidadesBean("Siemens", new Double(0.00), new Double(0.00), new Date(),new Integer(106012), new Integer(1), new Double(6250), "Cheque Viajero",
    		 new Double(0.001500), new Double (10.725000), "Venta", new Double (9.38),"Dólar", "DANIEL SALDANA SOSA", new Date(), new Date(), "<PMY>", "<981399>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.TRUE);
     ReporteUtilidadesBean bean6 = new ReporteUtilidadesBean("Sanchez/Hugo", new Double(0.00), new Double(50), new Date(),new Integer(106109), new Integer(0), new Double(775000), "Transferencia",
    		 new Double(0.018760), new Double (10.741500), "Compra", new Double (14539),"Dólar", "DANIEL SALDANA SOSA", new Date(), new Date(), "<PMY>", "<981399>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.TRUE);
     ReporteUtilidadesBean bean7 = new ReporteUtilidadesBean("Corp Interamericana de E", new Double(0.00), new Double(0.00), new Date(),new Integer(106276), new Integer(0), new Double(3220), "Efectivo",
    		 new Double(0.055700), new Double (10.785000), "Venta", new Double (179.35),"Dólar", "DANIEL SALDANA SOSA", new Date(), new Date(), "<PMY>", "<981399>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.TRUE);
     ReporteUtilidadesBean bean8 = new ReporteUtilidadesBean("Gerardo Corzo Herrera", new Double(25), new Double(0.00), new Date(),new Integer(106412), new Integer(0), new Double(12275), "Mexdolar",
    		 new Double(0.052300), new Double (10.675000), "Compra", new Double (641.98),"Dólar", "DANIEL SALDANA SOSA", new Date(), new Date(), "<PMY>", "<981399>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.TRUE);    
     ReporteUtilidadesBean bean9 = new ReporteUtilidadesBean("Xerox Mexicana", new Double(0.00), new Double(0.00), new Date(),new Integer(105988), new Integer(0), new Double(3220), "Transferencia",
    		 new Double(0.055700), new Double (13.000000), "Venta", new Double (565.88),"Euro", "DANIEL SALDANA SOSA", new Date(), new Date(), "<PMY>", "<981399>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.TRUE);
     ReporteUtilidadesBean bean10 = new ReporteUtilidadesBean("Sanchez/Hugo", new Double(0.00), new Double(75), new Date(),new Integer(106276), new Integer(1), new Double(12275), "Transferencia",
    		 new Double(0.052300), new Double (12.965000), "Compra", new Double (22.27),"Euro", "DANIEL SALDANA SOSA", new Date(), new Date(), "<PMY>", "<981399>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.TRUE); 
     
     ReporteUtilidadesBean bean16 = new ReporteUtilidadesBean("Siemens", new Double(0.00), new Double(0.00), new Date(),new Integer(106012), new Integer(1), new Double(6250), "Cheque Viajero",
    		 new Double(0.001500), new Double (10.725000), "Venta", new Double (9.38),"Dólar", "PILAR GABRIELA VAZQUEZ", new Date(), new Date(), "<PMY>", "<881414>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.FALSE);
     ReporteUtilidadesBean bean17 = new ReporteUtilidadesBean("Sanchez/Hugo", new Double(0.00), new Double(50), new Date(),new Integer(106109), new Integer(0), new Double(775000), "Transferencia",
    		 new Double(0.018760), new Double (10.741500), "Compra", new Double (14539),"Dólar", "PILAR GABRIELA VAZQUEZ", new Date(), new Date(), "<PMY>", "<881414>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.FALSE);
     ReporteUtilidadesBean bean18 = new ReporteUtilidadesBean("Corp Interamericana de E", new Double(0.00), new Double(0.00), new Date(),new Integer(106276), new Integer(0), new Double(3220), "Efectivo",
    		 new Double(0.055700), new Double (10.785000), "Venta", new Double (179.35),"Dólar", "PILAR GABRIELA VAZQUEZ", new Date(), new Date(), "<PMY>", "<881414>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.FALSE);
     ReporteUtilidadesBean bean19 = new ReporteUtilidadesBean("Gerardo Corzo Herrera", new Double(25), new Double(0.00), new Date(),new Integer(106412), new Integer(0), new Double(12275), "Mexdolar",
    		 new Double(0.052300), new Double (10.675000), "Compra", new Double (641.98),"Dólar", "PILAR GABRIELA VAZQUEZ", new Date(), new Date(), "<PMY>", "<881414>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.FALSE);    
     ReporteUtilidadesBean bean20 = new ReporteUtilidadesBean("Xerox Mexicana", new Double(0.00), new Double(0.00), new Date(),new Integer(105988), new Integer(0), new Double(3220), "Transferencia",
    		 new Double(0.055700), new Double (13.000000), "Venta", new Double (565.88),"Euro", "PILAR GABRIELA VAZQUEZ", new Date(), new Date(), "<PMY>", "<881414>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.FALSE);
     ReporteUtilidadesBean bean21 = new ReporteUtilidadesBean("Sanchez/Hugo", new Double(0.00), new Double(75), new Date(),new Integer(106276), new Integer(1), new Double(12275), "Transferencia",
    		 new Double(0.052300), new Double (12.965000), "Compra", new Double (22.27),"Dólar Canadiense", "PILAR GABRIELA VAZQUEZ", new Date(), new Date(), "<PMY>", "<881414>",
    		 new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Integer(005), new Double(833525), new Double(45300), new Double(67372), new Double(23000), new Double(565433),
    		 new Double(5263724), new Double(122312), new Double(6327382), image, Boolean.FALSE);      
     l.add(bean);
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
     l.add(bean16);
     l.add(bean17);
     l.add(bean18);
     l.add(bean19);
     l.add(bean20);
     l.add(bean21);
     
     return new JRBeanCollectionDataSource(l);
	}
	
	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#dispose(net.sf.jasperreports.engine.JRDataSource)
	 */
	public void dispose(JRDataSource arg0) throws JRException {
	}

}
