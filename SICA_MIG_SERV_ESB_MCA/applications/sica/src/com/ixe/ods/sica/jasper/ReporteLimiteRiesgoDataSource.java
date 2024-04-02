/*
 * $Id: ReporteLimiteRiesgoDataSource.java,v 1.11 2008/03/19 01:16:44 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSourceProvider;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Clase que crea un Data Source,con un conjunto de beans,
 * que permite probar el reporte de L&icaute;mite de Riesgo
 *
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.11 $ $Date: 2008/03/19 01:16:44 $
 */
public class ReporteLimiteRiesgoDataSource extends JRAbstractBeanDataSourceProvider {

    /**
     * Constructor de la clase Haciendo referencia al Bean a Utilizar
     */
    public ReporteLimiteRiesgoDataSource() {
		super(ReporteLimiteRiesgoBean.class);
	}

	/**
     * @param jasperReport El reporte a crear.
     * @return JRDataSource.
     * @throws JRException Si no se puede generar el reporte.
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider#create(
     *  net.sf.jasperreports.engine.JasperReport)
	 */
	public JRDataSource create(JasperReport jasperReport) throws JRException {
		List l = new ArrayList();
        ReporteLimiteRiesgoBean bean = new ReporteLimiteRiesgoBean(new Double(12258255.05),
                "Operacion", new Double(-12252256.25), "Operacion Restringida", new Double(-42.25),
                "USD", new Double(14.250), new Double(14.200), new Double(0.00),
                new Double(0.00), new Double(0.00), new Double(-1.185), new Double(-1.185),
                new Double(-77.41), new Double(100), new Double(200));
        ReporteLimiteRiesgoBean bean1 = new ReporteLimiteRiesgoBean(new Double(12258255.05),
                "Operacion", new Double(-12252256.25), "Operacion Restringida", new Double(-42.25),
                "EUR", new Double(14.250), new Double(14.200), new Double(0.00),
                new Double(0.00), new Double(0.00), new Double(-1.185), new Double(-1.185),
                new Double(-77.41), new Double(100), new Double(200));
        ReporteLimiteRiesgoBean bean2 = new ReporteLimiteRiesgoBean(new Double(12258255.05),
                "Operacion", new Double(-12252256.25), "Operacion Restringida", new Double(-42.25),
                "GBP", new Double(4.250), new Double(14.200), new Double(0.00),
                new Double(0.00), new Double(0.00), new Double(-1.185), new Double(-1.185),
                new Double(-77.41), new Double(100), new Double(200));
        ReporteLimiteRiesgoBean bean3 = new ReporteLimiteRiesgoBean(new Double(12258255.05),
                "Operacion", new Double(-12252256.25), "Operacion Restringida", new Double(-42.25),
                "TODAS", new Double(14.250), new Double(14.200), new Double(0.00),
                new Double(0.00), new Double(0.00), new Double(-1.185), new Double(-1.185),
                new Double(-77.41), new Double(100), new Double(200));
        l.add(bean);
        l.add(bean1);
        l.add(bean2);
        l.add(bean3);
        return new JRBeanCollectionDataSource(l);
    }

    /**
     * @param arg0 Ignorado.
     * @throws JRException Si no se puede generar el reporte.
     * @see net.sf.jasperreports.engine.JRDataSourceProvider#dispose(
     *      net.sf.jasperreports.engine.JRDataSource)
     */
    public void dispose(JRDataSource arg0) throws JRException {
    }
}