/*
 * $Id: RepBanxicoInformeDataSource.java,v 1.12 2008/02/22 18:25:03 ccovian Exp $
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
 * Permite generar un template del Informe Analitico de Compras y Ventas para Banxico.
 *
 * @author Gustavo Gonzalez
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:03 $
 */
public class RepBanxicoInformeDataSource extends JRAbstractBeanDataSourceProvider {
    
    /**
	 * Constructor sin parametros de la clase, haciendo referencia al
	 * Bean a Utilizar
	 */
	public RepBanxicoInformeDataSource() {
		super(RepBanxicoInformeBean.class);
	}

	/**
	 * Crea un reporte de prueba con los datos especificados.
	 *
	 * @param arg0 Reporte de prueba.
	 * @throws JRException Si ocurre alg&uacute;n error.
	 * @return JRDataSource.
	 */
	public JRDataSource create(JasperReport arg0) throws JRException {
		List l = new ArrayList();
		InputStream image = null;
		Calendar fecha = new GregorianCalendar();
		fecha.setTime(new Date());
		fecha.add(Calendar.DAY_OF_MONTH, -2);
		Calendar fecha1 = new GregorianCalendar();
		fecha1.setTime(new Date());
		fecha1.add(Calendar.DAY_OF_MONTH, -4);
		Calendar fecha2 = new GregorianCalendar();
		fecha2.setTime(new Date());
		fecha2.add(Calendar.DAY_OF_MONTH, -5);
		RepBanxicoInformeBean bean28 = new RepBanxicoInformeBean("IXE", fecha1.getTime(),
                new Date(), new Double(0), "IXE FORWARD", new Double(0), new Double(0),
                new Double(0), new Double(0), new Double(0), new Double(0), new Double(0), image,
                Boolean.FALSE, "USD", "1", new Double(0), "600.00      0.000", "2000.00    0.000",
                new Double(0), new Double(0), new Double(0), new Double(0), new Double(0),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),
                new Double(11.2400), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(1000), new Double(10000), Boolean.FALSE, new Double(20000),
                Boolean.TRUE, new Double(12000), new Double(12000), new String("N/A"), new String("N/A"),
                new String("120,400"), new String("120,400"));
        RepBanxicoInformeBean bean15 = new RepBanxicoInformeBean("BANCA AFIRME GGGGGG",
                fecha1.getTime(), new Date(), new Double(23000000), "BANCOS", new Double(100),
                new Double(200), new Double(300), new Double(350), new Double(340),
                new Double(1000), new Double(2000), image, Boolean.TRUE, "USD", "1",
                new Double(2000), "600.00      0.000", "2000.00    0.000", new Double(125000.50),
                new Double(70000), new Double(50000), new Double(20000), new Double(5000),
                new Double(11.2400), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(1000), Boolean.TRUE);
        RepBanxicoInformeBean bean16 = new RepBanxicoInformeBean("BANCA AFIRME GGGGGG",
                fecha1.getTime(), new Date(), new Double(23000000), "BANCOS", new Double(100),
                new Double(200), new Double(300), new Double(350), new Double(340),
                new Double(1000), new Double(2000), image, Boolean.TRUE, "USD", "1",
                new Double(2000), "600.00      0.000", "2000.00    0.000", new Double(125000.50),
                new Double(70000), new Double(50000), new Double(20000), new Double(5000),
                new Double(11.2400), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(1000), Boolean.TRUE);
        RepBanxicoInformeBean bean17 = new RepBanxicoInformeBean("BANCA AFIRME GGGGGG",
                fecha1.getTime(), new Date(), new Double(23000000), "BANCOS", new Double(100),
                new Double(200), new Double(300), new Double(350), new Double(340),
                new Double(1000), new Double(2000), image, Boolean.TRUE, "USD", "1",
                new Double(2000), "600.00      0.000", "2000.00    0.000", new Double(125000.50),
                new Double(70000), new Double(50000), new Double(20000), new Double(5000),
                new Double(11.2400), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(1000), Boolean.TRUE);
        RepBanxicoInformeBean bean18 = new RepBanxicoInformeBean("INTERCAM", fecha1.getTime(),
                new Date(), new Double(23000000), "CASAS DE BOLSA", new Double(100),
                new Double(200), new Double(300), new Double(250), new Double(360),
                new Double(1000), new Double(2000), image, Boolean.TRUE, "USD", "1",
                new Double(2000), "600.00      0.000", "2000.00    0.000", new Double(125000.50),
                new Double(70000), new Double(50000), new Double(20000), new Double(5000),
                new Double(11.2400), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(1000), Boolean.TRUE);
        RepBanxicoInformeBean bean19 = new RepBanxicoInformeBean("MONEX", fecha1.getTime(),
                new Date(), new Double(23000000), "PERSONAS MORALES", new Double(100),
                new Double(200), new Double(300), new Double(450), new Double(350),
                new Double(1000), new Double(2000), image, Boolean.TRUE, "USD", "1",
                new Double(2000), "600.00      0.000", "2000.00    0.000", new Double(125000.50),
                new Double(70000), new Double(50000), new Double(20000), new Double(5000),
                new Double(11.2400), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(1000), Boolean.TRUE);
        RepBanxicoInformeBean bean20 = new RepBanxicoInformeBean("MONEX", fecha1.getTime(),
                new Date(), new Double(23000000), "PERSONAS MORALES", new Double(100),
                new Double(200), new Double(300), new Double(450), new Double(350),
                new Double(1000), new Double(2000), image, Boolean.TRUE, "USD", "1",
                new Double(2000), "10000.00      0.000", "2000.00    0.000", new Double(125000.50),
                new Double(70000), new Double(50000), new Double(20000), new Double(5000),
                new Double(11.2400), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(1000), Boolean.TRUE);
        RepBanxicoInformeBean bean27 = new RepBanxicoInformeBean("IXE", fecha1.getTime(),
                new Date(), new Double(0), "IXE FORWARD", new Double(0), new Double(0),
                new Double(0), new Double(0), new Double(0), new Double(0), new Double(0), image,
                Boolean.FALSE, "USD", "1", new Double(0), "600.00      0.000", "2000.00    0.000",
                new Double(0), new Double(0), new Double(0), new Double(0), new Double(0),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),
                new Double(11.2400), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(1000), new Double(10000), Boolean.FALSE, new Double(20000),
                Boolean.TRUE, new Double(12000), new Double(12000), new String("N/A"), new String("N/A"),
                new String("120,400"), new String("120,400"));
        RepBanxicoInformeBean bean21 = new RepBanxicoInformeBean("BANCA AFIRME GGGGGG",
                fecha1.getTime(), new Date(), new Double(23000000), "BANCOS", new Double(100),
                new Double(200), new Double(300), new Double(250), new Double(360),
                new Double(1000), new Double(2000), image, Boolean.FALSE, "USD", "1",
                new Double(2000), "600.00      0.000", "2000.00    0.000", new Double(125000.50),
                new Double(70000), new Double(50000), new Double(20000), new Double(5000),
                new Double(11.2400), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(1000), Boolean.TRUE);
        RepBanxicoInformeBean bean22 = new RepBanxicoInformeBean("MONEX", fecha1.getTime(),
                new Date(), new Double(23000000), "CASAS DE BOLSA", new Double(100),
                new Double(200), new Double(300), new Double(450), new Double(350),
                new Double(1000), new Double(2000), image, Boolean.FALSE, "USD", "1",
                new Double(2000), "600.00      0.000", "2000.00    0.000", new Double(125000.50),
                new Double(70000), new Double(50000), new Double(20000), new Double(5000),
                new Double(11.2400), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(1000), Boolean.TRUE);
        RepBanxicoInformeBean bean23 = new RepBanxicoInformeBean("INTERCAM", fecha1.getTime(),
                new Date(), new Double(23000000), "PERSONAS MORALES", new Double(100),
                new Double(200), new Double(300), new Double(350), new Double(340),
                new Double(1000), new Double(2000), image, Boolean.FALSE, "USD", "1",
                new Double(2000), "600.00      0.000", "2000.00    0.000", new Double(125000.50),
                new Double(70000), new Double(50000), new Double(20000), new Double(5000),
                new Double(11.2400), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(1000), Boolean.TRUE);
        RepBanxicoInformeBean bean24 = new RepBanxicoInformeBean("INTERCAM", fecha1.getTime(),
                new Date(), new Double(23000000), "PERSONAS MORALES", new Double(100),
                new Double(200), new Double(300), new Double(350), new Double(340),
                new Double(1000), new Double(2000), image, Boolean.FALSE, "USD", "1",
                new Double(2000), "600.00      0.000", "2000.00    0.000", new Double(125000.50),
                new Double(70000), new Double(50000), new Double(20000), new Double(5000),
                new Double(11.2400), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(1000), Boolean.TRUE);
        RepBanxicoInformeBean bean25 = new RepBanxicoInformeBean("IXE", fecha1.getTime(),
                new Date(), new Double(0), "IXE FORWARD", new Double(0), new Double(0),
                new Double(0), new Double(0), new Double(0), new Double(0), new Double(0), image,
                Boolean.FALSE, "USD", "1", new Double(0), "600.00      0.000", "2000.00    0.000",
                new Double(0), new Double(0), new Double(0), new Double(0), new Double(0),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),
                new Double(11.2400), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(1000), new Double(10000), Boolean.FALSE, new Double(20000),
                Boolean.TRUE, new Double(12000), new Double(12000),new String("N/A"), new String("N/A"),
                new String("120,400"), new String("120,400"));
        RepBanxicoInformeBean bean26 = new RepBanxicoInformeBean("IXE", fecha1.getTime(),
                new Date(), new Double(0), "IXE FORWARD", new Double(0), new Double(0),
                new Double(0), new Double(0), new Double(0), new Double(0), new Double(0), image,
                Boolean.FALSE, "USD", "1", new Double(0), "600.00      0.000", "2000.00    0.000",
                new Double(0), new Double(0), new Double(0), new Double(0), new Double(0),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),new Double(123500.40),new Double(123500.40),
                new Double(123500.40),new Double(123500.40),
                new Double(11.2400), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(11.3500), new Double(11.3500), new Double(11.3500), new Double(11.3500),
                new Double(1000), new Double(10000), Boolean.FALSE, new Double(20000),
                Boolean.TRUE, new Double(12000), new Double(12000),new String("N/A"), new String("N/A"),
                new String("120,400"), new String("120,400"));
        l.add(bean27);
        l.add(bean28);
        l.add(bean15);
        l.add(bean16);
        l.add(bean17);
        l.add(bean18);
        l.add(bean19);
        l.add(bean20);
        l.add(bean21);
        l.add(bean22);
        l.add(bean23);
        l.add(bean24);
        l.add(bean25);
        l.add(bean26);
		return new JRBeanCollectionDataSource(l);
	}

	/**
     * @param arg0 El dataSource.
     * @throws JRException Si ocurre alg&uacute;n error.
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#dispose(
            net.sf.jasperreports.engine.JRDataSource)
	 */
	public void dispose(JRDataSource arg0) throws JRException {
	}
}
