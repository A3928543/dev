/*
 * $Id: MailVespertinoServiceImpl.java,v 1.7.12.1.6.1.2.1 2010/12/20 21:15:28 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ixe.ods.sica.dao.CondorProcCambiosDao;
import com.ixe.ods.sica.model.CpaVta;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PosIni;
import com.ixe.ods.sica.model.Posicion;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.GeneralMailSender;
import com.ixe.ods.sica.services.MailVespertinoService;
import com.ixe.ods.sica.services.PdfService;
import com.ixe.ods.sica.services.model.ModeloPdf;
import com.ixe.ods.sica.utils.BDUtils;
import com.ixe.ods.sica.utils.MapUtils;

/**
 * Implementaci&oacute;n para el bean que env&iacute;a el correo del Cierre Vespertino.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.7.12.1.6.1.2.1 $ $Date: 2010/12/20 21:15:28 $
 */
public class MailVespertinoServiceImpl implements MailVespertinoService {

    /**
     * Constructor por default.
     */
    public MailVespertinoServiceImpl() {
        super();
    }

    /**
     * Env&iacute; un correo a las direcciones del Par&aacute;metro del sistema
     * 'EMAIL_CIERRE_VESPERTINO' para notificar que el SICA ha entrado en horario vespertino.
     */
    public void enviarCorreoCierreVespertino(URL logo) {
        ByteArrayOutputStream contenido = null;
        try {
            ParametroSica param = getSicaServiceData().
                    findParametro(ParametroSica.EMAIL_CIERRE_VESPERTINO);
            String[] para = param.getValor().split("\\,");
            List mesas = getSicaServiceData().findAllMesas();
            List modelos = new ArrayList();
            for (Iterator it = mesas.iterator(); it.hasNext();) {
                MesaCambio mc = (MesaCambio) it.next();
                List posics = convertirPosicionesEnMapas(getSicaServiceData().
                        findPosicionByIdMesaCambio(mc.getIdMesaCambio()));
                ModeloPdf modelo = new ModeloPdf("Mesa: " + mc.getNombre(),
                        new String[]{"Divisa", "Pos Ini", "Cash", "Tom", "Spot", "72Hr", "96Hr",
                                "Pos Fin", "T.C.P.", "Pos Fin MN", "Utilidad Global",
                                "Utilidad Mesa"},
                        new String[]{"idDivisa", "posicionInicial",
                                "cash", "tom", "spot", "tm3", "vFut",
                                "posicionFinal", "tipoCambioPromedioPonderadoCliente",
                                "posicionFinalMnClientePond", "utilidadGlobal", "utilidadMesa"},
                        posics, new String[]{"cc0", "rn2", "rn2", "rn2", "rn2", "rn2", "rn2", "rn2",
                                "rn6", "rn2", "rn2", "rn2"});
                modelos.add(modelo);
            }
            contenido = new ByteArrayOutputStream();
            String titulo = "Cierre Vespertino SICA " + DATE_FORMAT.format(new Date());
            getPdfService().escribirPdf(logo, titulo, modelos, contenido);
            getGeneralMailSender().enviarMail("ixecambios@ixe.com.mx", para, null,
                    "Cierre Vespertino SICA", "El SICA ha entrado en horario vespertino.\n\nEste " +
                            "correo ha sido generado autom\u00e1ticamente por el sistema SICA. " +
                            "No responda ni env\u00ede correo a esta direcci\u00f3n por favor.",
                    "posicion_" + DATE_FORMAT.format(new Date()) + ".pdf", "application/pdf",
                    contenido.toByteArray());
        }
        catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn(e.getMessage(), e);
            }
        }
        finally {
            if (contenido != null) {
                try {
                    contenido.close();
                }
                catch (IOException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
    }
    
    /**
     * Env&iacute; un correo a las direcciones del Par&aacute;metro del sistema
     * 'EMAIL_CIERRE_VESPERTINO' para notificar que el SICA ha entrado en horario vespertino.
     */
    public void enviarCorreoFinLiquidacion() {
        ByteArrayOutputStream contenido = null;
        try {            
            StringBuffer contenido2 = new StringBuffer();
            GregorianCalendar fechaReporte = new GregorianCalendar();
            List renglones = getCondorProcCambiosDao().ejecutar(fechaReporte.getTime());
            for (Iterator it = renglones.iterator(); it.hasNext();) {
                contenido2 = contenido2.append((String) it.next()).append("\r\n");
            }
            ParametroSica paramSite = getSicaServiceData().
                    findParametro(ParametroSica.EMAIL_SITE_VESPERTINO);
            String[] para = paramSite.getValor().split("\\,");
            getGeneralMailSender().enviarMail("ixecambios@ixe.com.mx",para, null,
                    "Archivo tesorer\u00eda operaciones SICA para carga a MINDS Banco del " +
                            DATE_FORMAT.format(new Date()),
                    "Se env\u00eda el archivo de operaciones cambiarias (del SICA) que se carga " +
                            "al MINDS Banco para los reportes de tesorer\u00eda. Lo anterior con " +
                            "el objetivo de poder tener actualizado hasta el d\u00eda de hoy la " +
                            "informaci\u00f3n del lavado de dinero que consulta el usuario del " +
                            "\u00e1rea normativa.\n\nEste correo ha sido generado " +
                            "autom\u00e1ticamente por el sistema SICA. No responda ni env\u00ede " +
                            "correo a esta direcci\u00f3n por favor.", "trdtsr" +
                            DATE_FORMAT2.format(new Date()) + "_ix.txt", "plain/text",
                    contenido2.toString().getBytes());
        }
        catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn(e.getMessage(), e);
            }
        }
        finally {
            if (contenido != null) {
                try {
                    contenido.close();
                }
                catch (IOException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Regresa una lista de mapas que contienen la informaci&oacute;n necesaria para el correo de la
     * posici&oacute;n.
     *
     * @param posics Un arreglo de objetos de la clase Posicion.
     * @return List de HashMap.
     */
    private List convertirPosicionesEnMapas(List posics) {
        List mapas = new ArrayList();
        for (Iterator it = posics.iterator(); it.hasNext();) {
            Posicion p = (Posicion) it.next();
            PosIni pi = p.getPosIni();
            CpaVta cv = p.getCpaVta();
            mapas.add(MapUtils.generar(new String[]{"idDivisa", "posicionInicial", "cash", "tom",
                    "spot", "tm3", "vFut", "posicionFinal", "tipoCambioPromedioPonderadoCliente",
                    "posicionFinalMnClientePond", "utilidadGlobal", "utilidadMesa"},
                    new Object[]{p.getDivisa().getIdDivisa(),
                            BDUtils.generar6(pi.getPosicionInicial()),
                            BDUtils.generar6(pi.getPosicionInicialCash() + cv.getCash()),
                            BDUtils.generar6(pi.getPosicionInicialTom() + cv.getTom()),
                            BDUtils.generar6(pi.getPosicionInicialSpot() + cv.getSpot()),
                            BDUtils.generar6(pi.getPosicionInicial72Hr().doubleValue() +
                                    cv.get72Hr()),
                            BDUtils.generar6(pi.getPosicionInicialVFut().doubleValue() +
                                    cv.getVFut()),
                            BDUtils.generar2(p.getPosicionFinal()),
                            BDUtils.generar6(p.getTipoCambioPromedioPonderadoCliente()),
                            BDUtils.generar6(p.getPosicionFinalMnClientePond()),
                            BDUtils.generar6(p.getUtilidadGlobal()),
                            BDUtils.generar6(p.getUtilidadMesa())}));
        }
        return mapas;
    }

    /**
     * Regresa el valor de condorProcCambiosDao.
     *
     * @return CondorProcCambiosDao.
     */
    public CondorProcCambiosDao getCondorProcCambiosDao() {
        return condorProcCambiosDao;
    }

    /**
     * Establece el valor de condorProcCambiosDao.
     *
     * @param condorProcCambiosDao El valor a asignar.
     */
    public void setCondorProcCambiosDao(CondorProcCambiosDao condorProcCambiosDao) {
        this.condorProcCambiosDao = condorProcCambiosDao;
    }

    /**
     * Regresa el valor de generalMailSender.
     *
     * @return GeneralMailSender.
     */
    public GeneralMailSender getGeneralMailSender() {
        return generalMailSender;
    }

    /**
     * Establece el valor de generalMailSender.
     *
     * @param generalMailSender El valor a asignar.
     */
    public void setGeneralMailSender(GeneralMailSender generalMailSender) {
        this.generalMailSender = generalMailSender;
    }

    /**
     * Regresa el valor de pdfService.
     *
     * @return PdfService.
     */
    public PdfService getPdfService() {
        return pdfService;
    }

    /**
     * Establece el valor de pdfService.
     *
     * @param pdfService El valor a asignar.
     */
    public void setPdfService(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Regresa el valor de sicaServiceData.
     *
     * @return SicaServiceData.
     */
    public SicaServiceData getSicaServiceData() {
        return sicaServiceData;
    }

    /**
     * Establece el valor de sicaServiceData.
     *
     * @param sicaServiceData El valor a asignar.
     */
    public void setSicaServiceData(SicaServiceData sicaServiceData) {
        this.sicaServiceData = sicaServiceData;
    }

    /**
     * El DAO para obtener el reporte que se env&iacute;a al Site.
     */
    private CondorProcCambiosDao condorProcCambiosDao;

    /**
     * Referencia al bean generalMailSender.
     */
    private GeneralMailSender generalMailSender;

    /**
     * Referencia al bean pdfService.
     */
    private PdfService pdfService;

    /**
     * Referencia al bean sicaServiceData.
     */
    private SicaServiceData sicaServiceData;

    /**
     * El objeto para logging.
     */
    private transient Log logger = LogFactory.getLog(getClass());

    /**
     * Constante para el formato de fechas.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy",
            new Locale("es", "mx"));

    /**
     * Constante para el formato de fechas.
     */
    private static final SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("ddMMyy");
}
