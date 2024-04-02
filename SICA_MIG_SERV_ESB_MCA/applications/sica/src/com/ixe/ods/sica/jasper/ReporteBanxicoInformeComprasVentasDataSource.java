package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.net.URL;
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

public class ReporteBanxicoInformeComprasVentasDataSource  extends JRAbstractBeanDataSourceProvider {
	
	/**
	 * Constructor de la clase Haciendo referencia al Bean a Utilizar
	 */
	public ReporteBanxicoInformeComprasVentasDataSource() {
		super(ReporteBanxicoInformeComprasVentasBean.class);
	}

	/**
     * @param jasperReport El reporte.
     * @return JRDataSource.
     * @throws JRException Si no se puede crear.
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#create(
            net.sf.jasperreports.engine.JasperReport)
	 */
	public JRDataSource create(JasperReport jasperReport) throws JRException {
		List l = new ArrayList();
		URL image = null;
		try {
		image =  new URL("http://localhost:7001/sica/logo_ixe.gif");
		}
		catch (Exception e) {
			e.getMessage();
		}
		ReporteBanxicoInformeComprasVentasBean bean = new ReporteBanxicoInformeComprasVentasBean(Boolean.TRUE, "EURO",
				"CASAS DE BOLSA", new Double(1000), new Double(1000), new Double(1000), 
				new Double(1000), new Double(1000), new Double(500), new Double(2000),
				new Double(10000), "N/A", new Double(54000),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(156700),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), Boolean.TRUE, Boolean.TRUE, 
				new Double(10000), new Double(15000),
				new String("10000"), new String("15000"), new Double(1000), Boolean.TRUE, 
				new Double(1000),  new Double(1000), new Double(23000), image, new Double(3200)); 
		ReporteBanxicoInformeComprasVentasBean bean2 = new ReporteBanxicoInformeComprasVentasBean(Boolean.TRUE, "EURO",
				"EMPRESAS", new Double(1000), new Double(1000), new Double(1000), 
				new Double(1000), new Double(1000), new Double(500), new Double(2000),
				new Double(5000), "15000", new Double(54000),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(156700),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), Boolean.TRUE, Boolean.TRUE, 
				new Double(10000), new Double(15000),
				new String("10000"), new String("15000"), new Double(1000), Boolean.TRUE, 
				new Double(1000),  new Double(1000), new Double(23000), image, new Double(3201));  
		ReporteBanxicoInformeComprasVentasBean bean9 = new ReporteBanxicoInformeComprasVentasBean(Boolean.TRUE, "EURO",
				"PERSONAS FISICAS", new Double(1000), new Double(1000), new Double(1000), 
				new Double(1000), new Double(1000), new Double(500), new Double(2000),
				new Double(5000), "15000", new Double(54000),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(156700),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), Boolean.FALSE, Boolean.FALSE, 
				new Double(10000), new Double(15000),
				new String("10000"), new String("15000"), new Double(1000), Boolean.TRUE, 
				new Double(1000),  new Double(1000), new Double(23000), image, new Double(3202)); 
		ReporteBanxicoInformeComprasVentasBean bean3 = new ReporteBanxicoInformeComprasVentasBean(Boolean.TRUE, "EURO",
				"OTROS INTERMEDIARIOS", new Double(1000), new Double(1000), new Double(1000), 
				new Double(1000), new Double(1000), new Double(500), new Double(2000),
				new Double(0), "0", new Double(54000),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(156700),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), Boolean.TRUE, Boolean.TRUE,
				new Double(10000), new Double(15000),
				new String("10000"), new String("15000"), new Double(0), Boolean.FALSE, 
				new Double(1000),  new Double(1000), new Double(23000), image, new Double(3203));   
		ReporteBanxicoInformeComprasVentasBean bean4 = new ReporteBanxicoInformeComprasVentasBean(Boolean.TRUE, "EURO",
				"OTROS INTERMEDIARIOS", new Double(1000), new Double(1000), new Double(1000), 
				new Double(1000), new Double(1000), new Double(0), new Double(0),
				new Double(0), "0", new Double(54000),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(156700),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), Boolean.FALSE, Boolean.FALSE, 
				new Double(10000), new Double(15000),
				new String("10000"), new String("15000"), new Double(0), Boolean.FALSE, 
				new Double(1000),  new Double(1000), new Double(23000), image, new Double(3204));   
		ReporteBanxicoInformeComprasVentasBean bean5 = new ReporteBanxicoInformeComprasVentasBean(Boolean.FALSE, "EURO",
				"CASAS DE BOLSA", new Double(1000), new Double(1000), new Double(1000), 
				new Double(1000), new Double(1000), new Double(500), new Double(2000),
				new Double(5000), "N/A", new Double(54000),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(156700),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), Boolean.TRUE, Boolean.TRUE, 
				new Double(10000), new Double(15000),
				new String("10000"), new String("15000"), new Double(1000), Boolean.TRUE, 
				new Double(1000),  new Double(1000), new Double(23000), image, new Double(3300));    
		ReporteBanxicoInformeComprasVentasBean bean6 = new ReporteBanxicoInformeComprasVentasBean(Boolean.FALSE, "EURO",
				"EMPRESAS", new Double(1000), new Double(1000), new Double(1000), 
				new Double(1000), new Double(1000), new Double(500), new Double(2000),
				new Double(5000), "10000", new Double(54000),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(156700),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), Boolean.TRUE, Boolean.TRUE, 
				new Double(10000), new Double(15000),
				new String("10000"), new String("15000"), new Double(1000), Boolean.TRUE, 
				new Double(1000),  new Double(1000), new Double(23000), image, new Double(3301));   
		ReporteBanxicoInformeComprasVentasBean bean7 = new ReporteBanxicoInformeComprasVentasBean(Boolean.FALSE, "EURO",
				"OTROS INTERMEDIARIOS", new Double(1000), new Double(1000), new Double(1000), 
				new Double(1000), new Double(1000), new Double(500), new Double(2000),
				new Double(0), "0", new Double(54000),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(156700),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), Boolean.TRUE, Boolean.TRUE, 
				new Double(10000), new Double(15000),
				new String("10000"), new String("15000"), new Double(0), Boolean.FALSE, 
				new Double(1000),  new Double(1000), new Double(23000), image, new Double(3302));   
		ReporteBanxicoInformeComprasVentasBean bean8 = new ReporteBanxicoInformeComprasVentasBean(Boolean.FALSE, "EURO",
				"OTROS INTERMEDIARIOS", new Double(1000), new Double(1000), new Double(1000), 
				new Double(1000), new Double(1000), new Double(0), new Double(0),
				new Double(0), "0", new Double(54000),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(2300),
				new Double(2300), new Double(156700),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(4580),
				new Double(4580), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), new Double(14.56789),
				new Double(14.56789), Boolean.FALSE, Boolean.FALSE, 
				new Double(10000), new Double(15000),
				new String("10000"), new String("15000"), new Double(0), Boolean.FALSE, 
				new Double(1000),  new Double(1000), new Double(23000), image, new Double(3302));   
          l.add(bean);
          l.add(bean2);
          l.add(bean3);
          l.add(bean4);
          l.add(bean9);
          l.add(bean5);
          l.add(bean6);
          l.add(bean7);
        return new JRBeanCollectionDataSource(l);
    }

    /**
     * @param arg0 No utilizado.
     * @throws JRException Si ocurre un problema.
     * @see net.sf.jasperreports.engine.JRDataSourceProvider#dispose(
            net.sf.jasperreports.engine.JRDataSource)
     */
	public void dispose(JRDataSource arg0) throws JRException {
	}
	
}
