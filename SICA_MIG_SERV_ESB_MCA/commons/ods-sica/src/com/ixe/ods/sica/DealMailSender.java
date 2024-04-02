/*
 * $Id: DealMailSender.java,v 1.12.64.6.10.1.14.1 2020/03/03 18:11:40 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2013 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.ixe.ods.bup.model.Direccion;
import com.ixe.ods.bup.model.MedioContacto;
import com.ixe.ods.bup.sdo.BupServiceData;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.PlantillaPantalla;
import com.ixe.ods.sica.sdo.WorkFlowServiceData;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * Clase utilizada para el envio del reporte deal por e-mail al cliente.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.12.64.6.10.1.14.1 $ $Date: 2020/03/03 18:11:40 $
 */
public class DealMailSender implements Serializable {

    /**
     * Constructor vac&iacute;o.
     */
    public DealMailSender() {
        super();
    }

    /**
	 * Envia el reporte de deal por e-mail.
	 * 
	 * @param ticket El ticket de la sesi&oacute;n,.
	 * @param deal El deal para enviar.
	 */
    public void enviarDealPorMail(String ticket, Deal deal) {
        try {
            String resource = "applications/sica/WEB-INF/jasper/ReporteDeal.jasper";
            List nombresArchivos = new ArrayList();
            byte[] bytes = null;

            new File("archivos/sica/comprobantes/").mkdirs();
            for (Iterator it = deal.getDetalles().iterator(); it.hasNext(); ) {
                DealDetalle det = (DealDetalle) it.next();

                if (!DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal())) {
                    Map reportOutParams = null;
                    JasperPrint jasperPrint;
                    JRDataSource dataSource = getDataSource(ticket, deal, det);
                    FileInputStream inputStream = new FileInputStream(new File(resource));
                    JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
                    jasperPrint = JasperFillManager.fillReport(jasperReport, reportOutParams, dataSource);
                    bytes = JasperExportManager.exportReportToPdf(jasperPrint);

                    String nombreArchivo = "archivos/sica/comprobantes/Operacion_" + deal.getIdDeal() + "_" + det.getIdDealPosicion() + ".pdf";

                    FileOutputStream fos = null;

                    try {
                        fos = new FileOutputStream(nombreArchivo);

                        fos.write(bytes);
                        fos.flush();
                        nombresArchivos.add(nombreArchivo);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        if (logger.isDebugEnabled()) {
                            logger.debug(e);
                        }
                    }
                    finally {
                        if (fos != null) {
                            fos.close();
                        }
                    }
                }
            }
            bytes = generarZip(nombresArchivos, "archivos/sica/comprobantes/Deal_" + deal.getIdDeal() + ".zip");
            construir(bytes, deal);
        }
        catch (SicaException e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.getMessage(), e);
            }
        }
        catch (JRException e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.getMessage(), e);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.getMessage(), e);
            }
        }
        catch (MessagingException e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.getMessage(), e);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.getMessage(), e);
            }
        }
    }

    /**
     * Construye y env&iacute;a por correo electr&oacute;nico al primer medio de contacto que se
     * encuentre para el cliente, el pdf del reporte de deal.
     *
     * @param bytes Los bytes del reporte.
     * @param deal El deal a enviar.
     * @throws IOException Si no se puede abrir el archivo.
     * @throws MessagingException Si no se puede enviar el correo electr&oacute;nico.
     * @throws SicaException Si no se encuentra el deal en la base de datos.
     */
    private void construir(byte[] bytes, Deal deal)
            throws IOException, MessagingException, SicaException {
        if (bytes != null && bytes.length > 0) {
            String name = "ReporteDeal_";
            FileOutputStream fstream;
            File myFile = new File(System.getProperty(TMP_DIR) + name + deal.getIdDeal() + ZIP_EXTENSION);
            fstream = new FileOutputStream(myFile);
            fstream.write(bytes, 0, bytes.length);
            fstream.flush();
            File myArchive = new File(System.getProperty(TMP_DIR) + name + deal.getIdDeal() + ZIP_EXTENSION);
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost(getHost());
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            String[] to = deal.getEmailsComprobantes().split("\\;");
            helper.setFrom(getFrom());
            helper.setTo(to);
            String conCopia = getSicaWorkFlowServiceData().findParametro(EMAIL_COPIA_OCULTA_COMPROBANTES).getValor();
            //helper.setCc(conCopia);
            helper.setBcc(conCopia);
            helper.setSubject("Aviso de Ixe banco");
            helper.setText("Reporte Deal de operaciones cambiarias");
            helper.addAttachment(name + deal.getIdDeal() + ZIP_EXTENSION, myArchive);
            sender.send(message);
            myFile.delete();
            myArchive.delete();
        }
    }
    
    /**
     * Obtiene el DataSource del reporte.
     * 
     * @param ticket El ticket de la sesi&oacute;n 
     * @param d El deal.
     * @param det El detalle de deal.
     * @return JRDataSource
     * @throws SicaException Si ocurre algun problema.
     */
    private JRDataSource getDataSource(String ticket, Deal d, DealDetalle det) throws SicaException {
        List dealMap = new ArrayList();
        List subTotalRecibimos = new ArrayList();
        List subTotalEntregamos = new ArrayList();
        List printBefore = d.isCompra() ? d.getRecibimos() : d.getEntregamos();
        List printAfter = d.isCompra() ? d.getEntregamos() : d.getRecibimos();
        for (Iterator it = printBefore.iterator(); it.hasNext(); ) {
            DealDetalle dealDetalle = (DealDetalle) it.next();
            if (!DealDetalle.STATUS_DET_CANCELADO.equals(dealDetalle.getStatusDetalleDeal())) {
                if (det.getIdDealPosicion() == dealDetalle.getIdDealPosicion()) {
                    HashMap map = llenarReporte(ticket, d, dealDetalle, subTotalEntregamos,
                            subTotalRecibimos);
                    if (map != null) {
                        dealMap.add(map);
                    }
                }
            }
        }
        for (Iterator it = printAfter.iterator(); it.hasNext();) {
            DealDetalle dealDetalle = (DealDetalle) it.next();
            if (! DealDetalle.STATUS_DET_CANCELADO.equals(dealDetalle.getStatusDetalleDeal())) {
                if (det.getIdDealPosicion() == dealDetalle.getIdDealPosicion()) {
                HashMap map = llenarReporte(ticket, d, dealDetalle, subTotalEntregamos,
                        subTotalRecibimos);
                if (map != null) {
                    dealMap.add(map);
                }
            }
        }
        }
        return new ListDataSource(dealMap);
    }
    
    /**
     * Asigna los valores de las variables del reporte.
     * 
     * @param ticket El ticket de la sesi&oacute;n.
     * @param d El deal.
     * @param dealDetalle El detalle del deal.
     * @param subTotalEntregamos El monto del subtotal para entregamos.
     * @param subTotalRecibimos El monto del subtotal para recibimos.
     * @return HashMap
     * @throws SicaException Si ocurre un problema.
     */
    private HashMap llenarReporte(String ticket, Deal d, DealDetalle dealDetalle,
                                  List subTotalEntregamos, List subTotalRecibimos)
            throws SicaException {
    	FormaPagoLiq fpl = getSicaSiteCache().getFormaPagoLiq(ticket, dealDetalle.getMnemonico());
        PlantillaPantalla pp = getSicaWorkFlowServiceData().
                findPlantillaPantallaByMnemonico(dealDetalle.getMnemonico());
        List datosAdicionales = dealDetalle.getInfoAdicional(fpl, pp);
        HashMap map1 = new HashMap();
        HashMap mapa;
        StringBuffer listTotalRecibimos = new StringBuffer();
        StringBuffer listTotalEntregamos = new StringBuffer();
        List sub = new ArrayList();
        List lista = new ArrayList();
        StringBuffer listMontoTotalRecibimos = new StringBuffer();
        StringBuffer listMontoTotalEntregamos = new StringBuffer();
        BupServiceData bsd = getBupServiceData();
        List l = bsd.findFiscalAddressByIdPerson(d.getCliente().getIdPersona());
        String direccion = "";
        for (Iterator iter = l.iterator(); iter.hasNext();) {
            Direccion element = (Direccion) iter.next();
            direccion += element.getCalleYNumero();

            direccion += element.getNumExterior() != null ? " " + element.getNumExterior() : "";
            direccion += element.getNumInterior() != null ? " " + element.getNumInterior() : "";
            direccion += " COL. "
                    + element.getColonia() + EMPTY_SPACE + element.getCiudad() + EMPTY_SPACE
                    + element.getDelegacionMunicipio() + " C.P. " + element
                    .getCodigoPostal();
        }
        try {
            FileInputStream inputStream = new FileInputStream(
                    new File("applications/sica/images/reportes/banorte.png"));
            map1.put("image", inputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e);
            }
        }
        map1.put("idDeal", new Integer(d.getIdDeal()));
        map1.put("idPersona", d.getCliente().getIdPersona());
        map1.put("cliente", d.getCliente().getNombreCompleto() + NEW_LINE + d.getCliente().getRfc()
                + NEW_LINE + direccion);
        //map1.put("rfc", d.getCliente().getRfc());
        //map1.put("direccion", direccion);
        if (d.getTipoValor().equals("CASH")) {
            map1.put("tipoValor", "HOY");
        }
        else {
            map1.put("tipoValor", d.getTipoValor());
        }
        map1.put("fechaCaptura", d.getFechaCaptura());
        map1.put("fechaAplicacion", d.getFechaLiquidacion());
        map1.put("idUsuario", d.getUsuario().getIdPersona());
        map1.put("promotor", d.getPromotor().getPaterno() + EMPTY_SPACE
                + d.getPromotor().getMaterno() + EMPTY_SPACE + d.getPromotor().getNombre());
        map1.put("compra", dealDetalle != null ?
                Boolean.valueOf(dealDetalle.isRecibimos()) :
                Boolean.valueOf(d.isCompra()));
        map1.put("descMnemonico", fpl.getDescripcion().toUpperCase());
        map1.put("idDivisa", dealDetalle.getDivisa().getDescripcion());
        map1.put("isRecibimos", Boolean.valueOf(dealDetalle.isRecibimos()));
        if (String.valueOf(dealDetalle.getTipoCambio()) != null) {
        	String tipoCambio = Constantes.CURRENCY4_FORMAT.format(dealDetalle.getTipoCambio());
        	map1.put("tipCambio", tipoCambio);
        }
        else {
        	map1.put("tipCambio", "");
        }
        map1.put("tipoCambio", new Double(dealDetalle.getTipoCambio()));
        map1.put("monto", new Double(dealDetalle.getMonto()));
        //map1.put("importe", new Double(dealDetalle.getMonto()* dealDetalle.getTipoCambio()));
        map1.put("importe", new Double(dealDetalle.getImporte()));
        map1.put("totalRecibimos", new Double(d.getTotalRecibimos()));
        map1.put("totalEntregamos", new Double(d.getTotalEntregamos()));
        if (dealDetalle.isRecibimos()) {
            HashMap map2 = new HashMap();
            map2.put("descRecibimos", dealDetalle.getDivisa().getDescripcion());
            map2.put("montoRecibimos", new Double(dealDetalle.getMonto()));
            subTotalRecibimos.add(map2);
            for (Iterator iterator = subTotalRecibimos.iterator(); iterator.hasNext();) {
                HashMap elemento = (HashMap) iterator.next();
                String element = elemento.get("descRecibimos").toString();
                if (!sub.contains(element)) {
                    sub.add(element);
                }
            }
            for (Iterator iter = sub.iterator(); iter.hasNext();) {
                double subtot = 0;
                String element = (String) iter.next();
                for (Iterator iterator = subTotalRecibimos.iterator(); iterator.hasNext();) {
                    HashMap elemento = (HashMap) iterator.next();
                    if (elemento.get("descRecibimos").toString().equals(element)) {
                        subtot += ((Double) elemento.get("montoRecibimos")).doubleValue();
                    }
                }
                mapa = new HashMap();
                mapa.put("descRecibimos", element);
                mapa.put("montoRecibimos", new Double(subtot));
                lista.add(mapa);
            }
            for (Iterator iter = lista.iterator(); iter.hasNext();) {
                HashMap element = (HashMap) iter.next();
                //listTotalRecibimos.append(element.get("descRecibimos")).append(EMPTY_SPACE).
                // append(new DecimalFormat("0.00").format(element.get("montoRecibimos"))).
                // append(NEW_LINE);
            	 listTotalRecibimos.append(element.get("descRecibimos")).append(NEW_LINE);
            	 listMontoTotalRecibimos.append(Constantes.MONEY_FORMAT.format(
                         element.get("montoRecibimos"))).append(NEW_LINE);
            }
            map1.put("subTotalRecibimos", listTotalRecibimos.toString());
            map1.put("subtotalDivisa", listMontoTotalRecibimos.toString());
        }
        else {
            HashMap map3 = new HashMap();
            map3.put("descEntregamos", dealDetalle.getDivisa().getDescripcion());
            map3.put("montoEntregamos", new Double(dealDetalle.getMonto()));
            subTotalEntregamos.add(map3);
            for (Iterator iterator = subTotalEntregamos.iterator(); iterator.hasNext();) {
                HashMap elemento = (HashMap) iterator.next();
                String element = elemento.get("descEntregamos").toString();
                if (!sub.contains(element)) {
                    sub.add(element);
                }
            }
            for (Iterator iter = sub.iterator(); iter.hasNext();) {
                double subtot = 0;
                String element = (String) iter.next();
                for (Iterator iterator = subTotalEntregamos.iterator(); iterator.hasNext();) {
                    HashMap elemento = (HashMap) iterator.next();
                    if (elemento.get("descEntregamos").toString().equals(element)) {
                        subtot += ((Double) elemento.get("montoEntregamos")).doubleValue();
                    }
                }
                mapa = new HashMap();
                mapa.put("descEntregamos", element);
                mapa.put("montoEntregamos", new Double(subtot));
                lista.add(mapa);
            }
            for (Iterator iter = lista.iterator(); iter.hasNext();) {
                HashMap element = (HashMap) iter.next();
                listTotalEntregamos.append(element.get("descEntregamos")).append(NEW_LINE);
                listMontoTotalEntregamos.append(Constantes.MONEY_FORMAT.format(
                        element.get("montoEntregamos"))).append(NEW_LINE);
            }
            map1.put("subTotalEntregamos", listTotalEntregamos.toString());
            map1.put("subtotalDivisa", listMontoTotalEntregamos.toString());
        }
        String infoAdicional = "";
        String infoAdicionalValor = "";
        String nombre;
        String valor;
        for (Iterator iter = datosAdicionales.iterator(); iter.hasNext();) {
            HashMap element = (HashMap) iter.next();
            if (element.get("nombre") != null && element.get("valor") != null) {
            	nombre = element.get("nombre").toString();
            	valor = element.get("valor").toString();
            	if (nombre.equals("BANCO PAGADOR")) {
            		nombre = "BANCO BENEFICIARIO";
            	}
            	if (nombre.indexOf("BANCO PAGADOR") > 0) {
            		int index = nombre.indexOf("BANCO PAGADOR");
            		String tmp = nombre.substring(0, index);
            		nombre = tmp + EMPTY_SPACE + "BANCO BENEFICIARIO";
            	}
            	int pipeIndex = valor.indexOf(PIPE);
            	if (pipeIndex > 0) {
            		StringTokenizer tokenValor = new StringTokenizer(valor, PIPE);
            		String newValor = "";
            		while (tokenValor.hasMoreTokens()) {
            			newValor += tokenValor.nextToken() + EMPTY_SPACE;
            		}
            		valor = newValor;
            	}
            	String clave = "ALRA 422397";
            	if (valor.startsWith(clave)) {
            		valor = valor.substring(12);
            	}
            	if (nombre.equals("INFORMACION PARA EL BENEFICIARIO")) {
            		nombre = "INF. PARA BENEFICIARIO";
            	}
            	if (nombre.equals("INSTRUCCIONES PARA EL  BANCO BENEFICIARIO")) {
            		nombre = "INST. A BANCO BENEFICIARIO";
            	}
            	if (nombre.equals("INSTRUCCIONES PARA EL BANCO INTERMEDIARIO")) {
            		nombre = "INST. A BANCO INTERMEDIARIO";
            	}
            	infoAdicional += nombre + NEW_LINE;
            	infoAdicionalValor += valor + NEW_LINE;
            	if (valor.length() > MAX_LENGTH) {
            		infoAdicional += NEW_LINE;
            	}
            }
        }
        map1.put("infoAdicional", infoAdicional);
        map1.put("infoAdicionalValor", infoAdicionalValor);
        return map1;
    }
    
    /**
     * Genera un zip con los archivos especificados y regresa los bytes del zip.
     *
     * @param archivos La lista de archivos excel a comprimir.
     * @param destino     El directorio temporal donde se guardar&aacute; el archivo zip.
     * @return byte[]
     * @throws IOException Si no se puede generar el archivo.
     */
    private byte[] generarZip(List archivos, String destino) throws IOException {
        byte[] resultado = null;
        byte[] buffer = new byte[1024];
        FileOutputStream fileOutputStream = null;
        ZipOutputStream zipOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(destino);
            zipOutputStream = new ZipOutputStream(fileOutputStream);
            for (Iterator it = archivos.iterator(); it.hasNext();) {
                String archivo = (String) it.next();
                File file = new File(archivo);
                FileInputStream fileInputStream = null;

                try {
                    fileInputStream = new FileInputStream(file);
                    String name = file.getName().startsWith("Errores_batch_") ? "Errores.txt" : file.getName();
                    zipOutputStream.putNextEntry(new ZipEntry(name));

                    int length = 0;

                    while ((length = fileInputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, length);
                    }

                }
                catch (IOException e) {
                    e.printStackTrace();

/*
                    if (logger.isDebugEnabled()) {
                        logger.debug(e.getMessage(), e);
                    }
*/
                    throw e;
                }
                finally {
                    if (zipOutputStream != null) {
                        try {
                            zipOutputStream.closeEntry();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
/*
                            if (logger.isDebugEnabled()) {
                                logger.debug(e.getMessage(), e);
                            }
*/
                        }
                    }
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
/*
                            if (logger.isDebugEnabled()) {
                                logger.debug(e.getMessage(), e);
                            }
*/
                        }
                    }
                }
                if (archivo != null) {
                    new File(archivo).delete();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
/*
            if (logger.isDebugEnabled()) {
                logger.debug(e.getMessage(), e);
            }
*/
            throw e;
        }
        finally {
            if (zipOutputStream != null) {
                zipOutputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(destino);
            resultado = new byte[fis.available()];
            fis.read(resultado);
        }
        catch (IOException e) {
            e.printStackTrace();
/*
            if (logger.isDebugEnabled()) {
                logger.debug(e.getMessage(), e);
            }
*/
            throw e;
        }
        finally {
            if (fis != null) {
                fis.close();
            }
        }
        return resultado;
    }

    /**
     * Obtiene el valor de host.
     * 
     * @return String
     */
    public String getHost() {
        return host;
    }

    /**
     * Asigna el valor para _host
     * @param host El valor para _host.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Obtiene el valor de from.
     * 
     * @return String
     */
    public String getFrom() {
        return from;
    }

    /**
     * Asigna el valor para from
     * 
     * @param from El valor para from.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Regresa el valor de bupServiceData.
     *
     * @return BupServiceData.
     */
    public BupServiceData getBupServiceData() {
        return bupServiceData;
    }

    /**
     * Establece el valor de bupServiceData.
     *
     * @param bupServiceData El valor a asignar.
     */
    public void setBupServiceData(BupServiceData bupServiceData) {
        this.bupServiceData = bupServiceData;
    }

    /**
     * Regresa el valor de sicaWorkFlowServiceData.
     *
     * @return WorkFlowServiceData.
     */
    public WorkFlowServiceData getSicaWorkFlowServiceData() {
        return sicaWorkFlowServiceData;
    }

    /**
     * Establece el valor de sicaWorkFlowServiceData.
     *
     * @param sicaWorkFlowServiceData El valor a asignar.
     */
    public void setSicaWorkFlowServiceData(WorkFlowServiceData sicaWorkFlowServiceData) {
        this.sicaWorkFlowServiceData = sicaWorkFlowServiceData;
    }

    /**
     * Regresa el valor de sicaSiteCache.
     *
     * @return SicaSiteCache.
     */
    public SicaSiteCache getSicaSiteCache() {
        return sicaSiteCache;
    }

    /**
     * Establece el valor de sicaSiteCache.
     *
     * @param sicaSiteCache El valor a asignar.
     */
    public void setSicaSiteCache(SicaSiteCache sicaSiteCache) {
        this.sicaSiteCache = sicaSiteCache;
    }
    
    /**
     * Instancia del servicio BupServiceData
     */
    private BupServiceData bupServiceData;
    
    /**
     * Instancia del servicio WorkFlowServiceData.
     */
    private WorkFlowServiceData sicaWorkFlowServiceData;
    
    /**
     * Instancia de SicaSiteCache.
     */
    private SicaSiteCache sicaSiteCache;
    
    /**
     * El host.
     */
    private String host;
    
    /**
     * La forma.
     */
    private String from;

    /**
     * El objeto para logging.
     */
    private final transient Log logger = LogFactory.getLog(getClass());

    /**
     * La constante para identificar el directorio temporal 'java.io.tmpdir'.
     */
    private static final String TMP_DIR = "java.io.tmpdir";

    /**
     * La constante para la extensi&oacute;n zip.
     */
    private static final String ZIP_EXTENSION = ".zip";

    /**
     * La constante para el largo m&aacute;ximo de una cadena.
     */
    private static final int MAX_LENGTH = 70;

    /**
     * La constante para un espacio en blanco.
     */
    private static final String EMPTY_SPACE = " ";

    /**
     * La constante para el salto de l&iacute;nea.
     */
    private static final String NEW_LINE = "\n";

    /**
     * La constante para el s&iacute;mbolo Pipe.
     */
    private static final String PIPE = "|";
    
    /**
     * La constante para la extensi&oacute;n zip.
     */
    private static final String EMAIL_COPIA_OCULTA_COMPROBANTES = "EMAIL_CC_COMPROBANTES";
    

    /**
     * El UID de serializaci&oacute;n.
     */
    private static final long serialVersionUID = 3028829686713305412L;
}
