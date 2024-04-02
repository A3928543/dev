/*
 * $Id: ReporteBanxicoDataSource.java,v 1.10 2008/02/22 18:25:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.jasper;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSourceProvider;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * DataSource de prueba para generar una vista previa
 * de los reportes para Banxico.
 * 
 * @author Gustavo Gonzalez
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:04 $
 */
public class ReporteBanxicoDataSource extends JRAbstractBeanDataSourceProvider {
	
	/**
	 * Constructor de la clase Haciendo referencia al Bean a Utilizar
	 */
	public ReporteBanxicoDataSource() {
		super(ReporteBanxicoBean.class);
	}

	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#create(net.sf.jasperreports.engine.JasperReport)
	 */
	public JRDataSource create(JasperReport jasperReport) throws JRException {
		List l = new ArrayList();
		InputStream image = null;
        Calendar gc1 = new GregorianCalendar();	
        Calendar gc2 = new GregorianCalendar();	
	    gc1.add(Calendar.DAY_OF_YEAR,1);
	    gc2.add(Calendar.DAY_OF_YEAR,2);     
		
	    //ReporteBanxicoBean bean  = new ReporteBanxicoBean(new Date(), "EUR", "1", "0", "0", new Double(12444.98), new Double(50000.70), "MOIC", "LEGOSOFT", "LEGO", "PERSONAS MORALES");
	    //ReporteBanxicoBean bean1  = new ReporteBanxicoBean(new Date(),"EUR", "1", "1", "0", "6", new Double(23000.60), new Double(48000.50), "MOIC", "IXE", "90","PERSONAS MORALES MEXICANAS");
	    
		ReporteBanxicoBean bean = new ReporteBanxicoBean(new Double(500.56), new Date(),new Date(),"Empresas No Financieras", new Double(12.00), "Agosto", "ABENGOA MEXICO S.A. DE C.V"
			,"libra esterlina", new Double(2.7178), new Integer(1), image);
		ReporteBanxicoBean bean1 = new ReporteBanxicoBean(new Double(500.56), new Date(),new Date(),"Empresas No Financieras", new Double(12.00), "Agosto", "ABENGOA MEXICO S.A. DE C.V"
				,"Euro", new Double(11.78), new Integer(1), image);
		ReporteBanxicoBean bean2 = new ReporteBanxicoBean(new Double(500.56), new Date(), gc1.getTime(),"Empresas No Financieras", new Double(12.00), "Agosto", "Ganja MEXICO S.A. DE C.V"
				,"libra esterlina", new Double(2.7178), new Integer(1), image);
		ReporteBanxicoBean bean3 = new ReporteBanxicoBean(new Double(500.56), new Date(), gc1.getTime(),"Empresas No Financieras", new Double(12.00), "Agosto", "ABENGOA MEXICO S.A. DE C.V"
				,"Euro", new Double(2.7178), new Integer(1), image);
		ReporteBanxicoBean bean4 = new ReporteBanxicoBean(new Double(500.56), new Date(), gc2.getTime(),"Empresas No Financieras", new Double(12.00), "Agosto", "Ganja MEXICO S.A. DE C.V"
				,"Dólar Canadiense", new Double(2.7178), new Integer(1), image);
		ReporteBanxicoBean bean5 = new ReporteBanxicoBean(new Double(500.56), new Date(),new Date(),"Bancos", new Double(12.00), "Agosto", "ABENGOA MEXICO S.A. DE C.V"
				,"libra esterlina", new Double(2.7178), new Integer(2), image);
		ReporteBanxicoBean bean6 = new ReporteBanxicoBean(new Double(500.56), new Date(),new Date(),"Bancos", new Double(12.00), "Agosto", "ABENGOA MEXICO S.A. DE C.V"
				,"libra esterlina", new Double(2.7178), new Integer(2), image);
		ReporteBanxicoBean bean7 = new ReporteBanxicoBean(new Double(500.56), new Date(),gc1.getTime(),"Bancos", new Double(12.00), "Agosto", "Ganja MEXICO S.A. DE C.V"
				,"Euro", new Double(2.7178), new Integer(2), image);
		ReporteBanxicoBean bean8 = new ReporteBanxicoBean(new Double(500.56), new Date(),gc2.getTime(),"Bancos", new Double(12.00), "Agosto", "Ganja MEXICO S.A. DE C.V"
				,"libra esterlina", new Double(2.7178), new Integer(2), image);
		ReporteBanxicoBean bean9 = new ReporteBanxicoBean(new Double(500.56), new Date(),gc2.getTime(),"Bancos", new Double(12.00), "Agosto", "ABENGOA MEXICO S.A. DE C.V"
				,"Dólar Canadiense", new Double(2.7178), new Integer(2), image);
		ReporteBanxicoBean bean10 = new ReporteBanxicoBean(new Double(500.56), new Date(),new Date(),"Personas Físicas", new Double(12.00), "Agosto", "ABENGOA MEXICO S.A. DE C.V"
				,"Dólar Canadiense", new Double(2.7178), new Integer(3), image);
		ReporteBanxicoBean bean11 = new ReporteBanxicoBean(new Double(500.56), new Date(),gc1.getTime(),"Personas Físicas", new Double(12.00), "Agosto", "Ganja MEXICO S.A. DE C.V"
				,"libra esterlina", new Double(2.7178), new Integer(3), image);;
		ReporteBanxicoBean bean12 = new ReporteBanxicoBean(new Double(500.56), new Date(),gc2.getTime(),"Personas Físicas", new Double(12.00), "Agosto", "ABENGOA MEXICO S.A. DE C.V"
				,"Yen", new Double(2.7178), new Integer(3), image);
		ReporteBanxicoBean bean13 = new ReporteBanxicoBean(new Double(500.56), new Date(),gc2.getTime(),"Personas Físicas", new Double(12.00), "Agosto", "ABENGOA MEXICO S.A. DE C.V"
				,"libra esterlina", new Double(2.7178), new Integer(3), image);
		l.add(bean6);
		l.add(bean7);
		l.add(bean8);
		l.add(bean9);
		l.add(bean10);
		l.add(bean11);
		l.add(bean12);
		l.add(bean13);
	   /*
		ReporteBanxicoAclmeBean bean = new ReporteBanxicoAclmeBean("40032", "C", gc1.getTime(), gc2.getTime(),
				"50000.25", "USD", "1258-2", "123", "0", "", "MOPF", "Empresas No Financieras", 
				"Dolar Americano", new Double(1.25), "20025689-2", "Procesandoce", "Monex Casa de Bolsa");
		ReporteBanxicoAclmeBean bean1 = new ReporteBanxicoAclmeBean("40032", "V", gc1.getTime(), gc2.getTime(),
				"78900.25", "USD", "1258-2", "123", "0", "", "MOPF", "Empresas No Financieras", 
				"Dolar Americano", new Double(1.25), "20025689-2", "Procesandoce", "JP Morgan NY");
		ReporteBanxicoAclmeBean bean2 = new ReporteBanxicoAclmeBean("40032", "V", gc1.getTime(), gc2.getTime(),
				"560000.25", "EUR", "1258-2", "123", "0", "", "MOCC", "Casas de Cambio", 
				"Euro", new Double(1.25), "20025689-2", "Procesandoce", "INTERCAM");
		ReporteBanxicoAclmeBean bean3 = new ReporteBanxicoAclmeBean("40032", "C", gc1.getTime(), gc2.getTime(),
				"7800.25", "EUR", "1258-2", "123", "0", "", "MOPF", "Pesonas Fisicas", 
				"Euro", new Double(1.25), "20025689-2", "Procesandoce", "Monex Casa de Bolsa");
		ReporteBanxicoAclmeBean bean4 = new ReporteBanxicoAclmeBean("40032", "C", gc1.getTime(), gc2.getTime(),
				"450000.25", "USD", "1258-2", "123", "0", "", "MOPM", "Empresas No Financieras", 
				"Dolar Americano", new Double(1.25), "20025689-2", "Procesandoce", "Monex Casa de Bolsa");
		ReporteBanxicoAclmeBean bean5 = new ReporteBanxicoAclmeBean("40032", "V", gc1.getTime(), gc2.getTime(),
				"35200.52", "GBP", "1258-2", "123", "0", "", "MOPM", "Empresas No Financieras", 
				"Libra Esterlina", new Double(1.25), "20025689-2", "Procesandoce", "Bank of America");
		l.add(bean);
		l.add(bean1);
		l.add(bean2);
		l.add(bean3);
		l.add(bean4);
		l.add(bean5);
		l.add(bean1);*/
		
		return new JRBeanCollectionDataSource(l);
	}
	
	/**
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#dispose(net.sf.jasperreports.engine.JRDataSource)
	 */
	public void dispose(JRDataSource arg0) throws JRException {
	}
}
