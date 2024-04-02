/*
 * $Id: ReporteDealDataSource.java,v 1.11 2008/02/22 18:25:03 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2007 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSourceProvider;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Bean para generar el reporte de Conciliacion Diaria
 *
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.11 $ $Date: 2008/02/22 18:25:03 $
 */
public class ReporteDealDataSource extends JRAbstractBeanDataSourceProvider {
    /**
     * Constructor de la clase Haciendo referencia al
     * Bean a Utilizar
     */
    public ReporteDealDataSource() {
        super(ReporteDealBean.class);
    }

    /**
     * @param jasperReport La referencia al Reporte.
     * @return JRDataSource.
     * @throws JRException Si no se puede crear el data source.
     * @see net.sf.jasperreports.engine.JRDataSourceProvider#create(
            net.sf.jasperreports.engine.JasperReport)
     */
    public JRDataSource create(JasperReport jasperReport) throws JRException {
        ArrayList list = new ArrayList();
        ArrayList lista = new ArrayList();
        HashMap map = new HashMap();
        map.put("Cuenta", new Integer(1000));
        map.put("Descripcion", "Banamex");
        map.put("cliente", "Gerardo Corzo");
        String cuenta = map.get("Cuenta").toString() + "\n"
                + map.get("Descripcion").toString() + "\n"
                + map.get("cliente").toString() + "\n";
        map.put("infoAdicional", cuenta);
        lista.add(map);
        InputStream image = null;
        ReporteDealBean bean1 = new ReporteDealBean("Peso", Boolean.FALSE,
                new Integer(666), "X", new Date(), new Date(), "SPOT",
                new Integer(900), "++++ Esto es una prueba", new Double(1000),
                new Integer(1), "Traveler", new Double(138919),
                new Double(8999), new Double(65656), new Double(88888),
                new Double(11000), "Promotor",
                "Cliente que vive en la calle tal de tal", new Integer(1), "X",
                "COHG-103073asd",
                "Retorno Violetas # 78 Izcalli Ecatepec de Morelos aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                        + "aaaaaaaaaaaaaaRetorno Violetas # 78 Izcalli Ecatepec de Morelos aaaaaaa"
                        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaasdfffffffffffffffffffffffffffffff"
                        + "fffffffffffffffffffffffffffffffffff"
                        + "", "X", "X", image, Boolean.FALSE, "Horario", null, null,
                null, null, null, null, null, "Linea Credito",
                "Comprobante de las condiciones de la operación. Sin validez Fiscal como recibo de"
                        + " cantidades recibidas ó entregadas", "descMnemonico", cuenta,
                "Total Recibimos", "Total Entregamos", new Double(12.90),
                "Retorno Violetas # 567 Las Flores, Delegación GAM, frente al Colegio de las Rosas",
                new Double(1200), "TT", "7", "1254.00", "Lic, Gustavo Gonzalez", "GOAG810812");
        ReporteDealBean bean2 = new ReporteDealBean("Peso", Boolean.FALSE,
                new Integer(666), "X", new Date(), new Date(),
                "TOM", new Integer(900), "++++ Esto es una prueba",
                new Double(1000), new Integer(1), "Traveler",
                new Double(138919), new Double(8999), new Double(65656),
                new Double(88888), new Double(11000), "Promotor",
                "Cliente que vive en la calle tal de tal", new Integer(1),
                "", "COHG-103073asd",
                "Retorno Violetas # 78 Izcalli Ecatepec de Morelos aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                        + "aaaaaaaaaaaaaaaRetorno Violetas # 78 Izcalli Ecatepec de Morelos aaaaa"
                        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaasdffffffffffffffffffffffffffff"
                        + "ffffffffffffffffffffffffffffffffffffff" + "", "X", "X", image,
                Boolean.FALSE, "Horario", null, null, null, null, null, null, null,
                "Linea Credito",
                "Comprobante de las condiciones de la operación. Sin validez Fiscal como recibo "
                        + "de cantidades recibidas ó entregadas", "descMnemonico", cuenta,
                "Total Recibimos", "Total Entregamos", new Double(13.90),
                "Retorno Violetas # 567 Las Flores, Delegación GAM, frente al Colegio de las Rosas",
                new Double(1200), "CA", "7", "1256884.00",
                "Directamente oficina Ing. Edgar Leija Juarez piso 50", "GOAG810812");

        /*
        ReporteDealBean bean3 = new ReporteDealBean("TRAEXT", new Boolean (true),new Integer(666),
            image,new Date(),new Date(),"CASH", new Integer (900),"++++ Esto es una prueba",
            new Double (1000),new Integer(1),"Traveler",new Double(138919),new Double(8999),
            new Double(65656),new Double(88888), new Double(11000),"Promotor","Cliente que vive
            en la calle tal de tal",new Integer(1), image, "COHG-103073asd", "Retorno Violetas #
            78 Izcalli Ecatepec de Morelos aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaRetorno
            Violetas # 78 Izcalli Ecatepec de Morelos aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
            aaasdffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "",image, image, image, Boolean.FALSE, "Horario", null, null, null, null,
                        null, null, null,
                        "Linea Credito", "Comprobante de las condiciones de la operación.
                        Sin validez Fiscal como recibo de cantidades recibidas ó entregadas",
                        "descMnemonico",
                        cuenta, "Total Recibimos", "Total Entregamos", new Double (15.90),
                        "Retorno Violetas # 567 Las Flores, Delegación GAM, frente al Colegio de
                        las Rosas", new Double(1200),"TT","7");
        ReporteDealBean bean4 = new ReporteDealBean("DOCEXT", new Boolean (true),new Integer(666),
            image,new Date(),new Date(),"HOY", new Integer (900),"++++ Esto es una prueba",
            new Double(1000),new Integer(1),"Traveler",new Double(138919),new Double(8999),
            new Double(65656),new Double(88888), new Double(11000),"Promotor",
            "Cliente que vive en la calle tal de tal",new Integer(1), image, "COHG-103073asd",
            "Retorno Violetas # 78 Izcalli Ecatepec de Morelos aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
            aaaaaaaaaaaasdffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
            "",image,image, image, Boolean.FALSE, "Horario", null, null, null, null, null, null,
            null, "Linea Credito", "Comprobante de las condiciones de la operación. Sin validez
            Fiscal como recibo de cantidades recibidas ó entregadas",  "descMnemonico", cuenta,
            "Total Recibimos", "Total Entregamos", new Double (18.00), "Retorno Violetas # 567 Las
            Flores, Delegación GAM, frente al Colegio de las Rosas", new Double(1200),"TT","7");
        */
        list.add(bean1);
        list.add(bean2);

        //list.add(bean3);
        //list.add(bean4);
        return new JRBeanCollectionDataSource(list);
    }

    /**
     * @param dataSource El dataSource para construir el reporte.
     * @throws JRException Si ocurre alg&uacute;n error.
     * @see net.sf.jasperreports.engine.JRDataSourceProvider#dispose(
            net.sf.jasperreports.engine.JRDataSource)
     */
    public void dispose(JRDataSource dataSource) throws JRException {
    }
}
