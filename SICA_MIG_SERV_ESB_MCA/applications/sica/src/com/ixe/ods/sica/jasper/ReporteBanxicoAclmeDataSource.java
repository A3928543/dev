/*
 * $Id: ReporteBanxicoAclmeDataSource.java,v 1.10 2008/02/22 18:25:03 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSourceProvider;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Permite generar un template del Reporte del ACLME para Banxico.
 *
 * @author Edgar Ivan Leija
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:03 $
 */
public class ReporteBanxicoAclmeDataSource extends JRAbstractBeanDataSourceProvider {

	/**
	 * Constructor sin parametros de la clase, haciendo referencia al
	 * Bean a Utilizar
	 */
	public ReporteBanxicoAclmeDataSource() {
		super(ReporteBanxicoAclmeBean.class);
	}
	
	/**
	 * Crea un reporte de prueba con los datos especificados.
	 * 
	 * @param JasperReport Reporte de prueba.
	 * @throws JRException.
	 * @return JRDataSource.
	 */
	public JRDataSource create(JasperReport jasperReport) throws JRException {
		List l = new ArrayList();
		Calendar gc1 = new GregorianCalendar();	
        Calendar gc2 = new GregorianCalendar();	
        Calendar gc3 = new GregorianCalendar();
        Calendar gc = new GregorianCalendar();
        gc1.add(Calendar.DAY_OF_YEAR,1);
	    gc2.add(Calendar.DAY_OF_YEAR,2);
	    gc2.add(Calendar.DAY_OF_YEAR,3);
	    
//		ReporteBanxicoAclmeBean bean = new ReporteBanxicoAclmeBean("40032", "C", gc1.getTime(), gc2.getTime(),
//				new Double(50000.25), "USD", "1258-2", "123", "0", "", "MOPF", "Empresas No Financieras", 
//				"Dolar Americano", new Double(1.25), "20025689-2", "Procesandoce", "Monex Casa de Bolsa");
//		ReporteBanxicoAclmeBean bean1 = new ReporteBanxicoAclmeBean("40032", "V", gc1.getTime(), gc2.getTime(),
//				new Double(78900.25), "USD", "1258-2", "123", "0", "", "MOPF", "Empresas No Financieras", 
//				"Dolar Americano", new Double(1.25), "20025689-2", "Procesandoce", "JP Morgan NY");
//		ReporteBanxicoAclmeBean bean2 = new ReporteBanxicoAclmeBean("40032", "V", gc1.getTime(), gc2.getTime(),
//				new Double(560000.25), "EUR", "1258-2", "123", "0", "", "MOCC", "Casas de Cambio", 
//				"Euro", new Double(1.25), "20025689-2", "Procesandoce", "INTERCAM");
//		ReporteBanxicoAclmeBean bean3 = new ReporteBanxicoAclmeBean("40032", "C", gc1.getTime(), gc2.getTime(),
//				new Double(7800.25), "EUR", "1258-2", "123", "0", "", "MOPF", "Pesonas Fisicas", 
//				"Euro", new Double(1.25), "20025689-2", "Procesandoce", "Monex Casa de Bolsa");
//		ReporteBanxicoAclmeBean bean4 = new ReporteBanxicoAclmeBean("40032", "C", gc1.getTime(), gc2.getTime(),
//				new Double(450000.25), "USD", "1258-2", "123", "0", "", "MOPM", "Empresas No Financieras", 
//				"Dolar Americano", new Double(1.25), "20025689-2", "Procesandoce", "Monex Casa de Bolsa");
//		ReporteBanxicoAclmeBean bean5 = new ReporteBanxicoAclmeBean("40032", "V", gc1.getTime(), gc2.getTime(),
//				new Double(35200.52), "GBP", "1258-2", "123", "0", "", "MOPM", "Empresas No Financieras", 
//				"Libra Esterlina", new Double(1.25), "20025689-2", "Procesandoce", "Bank of America");
	    ReporteBanxicoAclmeBean bean3 = new ReporteBanxicoAclmeBean(new Date(), gc1.getTime(), gc2.getTime(), new Double(1000), "Euro", "103", "Grupo Odem", new Boolean(true), new Boolean(false), new Double(10.556789));
	    ReporteBanxicoAclmeBean bean4 = new ReporteBanxicoAclmeBean(new Date(), gc.getTime(), gc.getTime(), new Double(1000), "Euro", "104", "Grupo Odem", new Boolean(true), new Boolean(false), new Double(10.556789));
	    ReporteBanxicoAclmeBean bean7 = new ReporteBanxicoAclmeBean(new Date(), gc.getTime(), gc3.getTime(), new Double(1000), "Libra Esterlina", "122", "Grupo Odem", new Boolean(true), new Boolean(false), new Double(10.556789));
	    ReporteBanxicoAclmeBean bean10 = new ReporteBanxicoAclmeBean(new Date(), gc1.getTime(), gc3.getTime(), new Double(1000), "Libra Esterlina", "135", "Grupo Televisa", new Boolean(true), new Boolean(false), new Double(10.556789));
	    ReporteBanxicoAclmeBean bean = new ReporteBanxicoAclmeBean(new Date(), gc.getTime(), gc1.getTime(), new Double(1000), "Dolar Americano", "90", "Grupo Odem", new Boolean(true), new Boolean(false), new Double(10.556789));
	    ReporteBanxicoAclmeBean bean1 = new ReporteBanxicoAclmeBean(new Date(), gc.getTime(), gc1.getTime(), new Double(2000), "Dolar Americano", "101", "Grupo Odem", new Boolean(true), new Boolean(false), new Double(10.556789));
	    ReporteBanxicoAclmeBean bean2 = new ReporteBanxicoAclmeBean(new Date(), gc1.getTime(), gc2.getTime(), new Double(3500), "Dolar Americano", "102", "Grupo Televisa", new Boolean(false), new Boolean(false), new Double(10.556789));
	    ReporteBanxicoAclmeBean bean8 = new ReporteBanxicoAclmeBean(new Date(), gc.getTime(), gc2.getTime(), new Double(3000), "Libra Esterlina", "133", "Roberto Barreda Abascal", new Boolean(false), new Boolean(false), new Double(10.556789));
	    ReporteBanxicoAclmeBean bean9 = new ReporteBanxicoAclmeBean(new Date(), gc.getTime(), gc2.getTime(), new Double(2000), "Libra Esterlina", "134", "Roberto Barreda Abascal", new Boolean(false), new Boolean(false), new Double(10.556789));
	    
	    
	    
	    ReporteBanxicoAclmeBean bean11 = new ReporteBanxicoAclmeBean(new Date(), gc.getTime(), gc2.getTime(), new Double(2000), "Pesos Mexicanos", "122", "Grupo Odem", new Boolean(true), new Boolean(true), new Double(10.556789));
	    ReporteBanxicoAclmeBean bean14 = new ReporteBanxicoAclmeBean(new Date(), gc.getTime(), gc2.getTime(), new Double(2000), "Pesos Mexicanos", "125", "Grupo Odem", new Boolean(true), new Boolean(true), new Double(10.556789));
	    ReporteBanxicoAclmeBean bean15 = new ReporteBanxicoAclmeBean(new Date(), gc.getTime(), gc2.getTime(), new Double(2000), "Pesos Mexicanos", "126", "Grupo Bavaria", new Boolean(true), new Boolean(true), new Double(10.556789));
	    ReporteBanxicoAclmeBean bean6 = new ReporteBanxicoAclmeBean(new Date(), gc.getTime(), gc2.getTime(), new Double(2000), "Pesos Mexicanos", "121", "Roberto Barreda Abascal", new Boolean(true), new Boolean(true), new Double(10.556789));
	    
	    
	    
	    ReporteBanxicoAclmeBean bean5 = new ReporteBanxicoAclmeBean(new Date(), gc.getTime(), gc.getTime(), new Double(2000), "Pesos Mexicanos", "120", "Roberto Barreda Abascal", new Boolean(false), new Boolean(true), new Double(10.556789));
	    ReporteBanxicoAclmeBean bean12 = new ReporteBanxicoAclmeBean(new Date(), gc.getTime(), gc2.getTime(), new Double(2000), "Pesos Mexicanos", "123", "Grupo Odem", new Boolean(false), new Boolean(true), new Double(10.556789));
	    ReporteBanxicoAclmeBean bean13 = new ReporteBanxicoAclmeBean(new Date(), gc.getTime(), gc2.getTime(), new Double(2000), "Pesos Mexicanos", "124", "Grupo Odem", new Boolean(false), new Boolean(true), new Double(10.556789));
	    ReporteBanxicoAclmeBean bean16 = new ReporteBanxicoAclmeBean(new Date(), gc.getTime(), gc2.getTime(), new Double(2000), "Pesos Mexicanos", "127", "Grupo Bavaria", new Boolean(false), new Boolean(true), new Double(10.556789));
	    
		l.add(bean3);
		l.add(bean4);
		l.add(bean7);
		l.add(bean10);
		l.add(bean);
		l.add(bean1);
		l.add(bean2);
		l.add(bean8);
		l.add(bean9);
		l.add(bean11);
		l.add(bean14);
		l.add(bean15);
		l.add(bean6);
		l.add(bean5);
		l.add(bean12);
		l.add(bean13);
		l.add(bean16);
		return new JRBeanCollectionDataSource(l);
	}
	
	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#dispose(net.sf.jasperreports.engine.JRDataSource)
	 */
	public void dispose(JRDataSource arg0) throws JRException {
	}

}
