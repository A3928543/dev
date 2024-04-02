/*
 * $Id: EsbServiceImpl.java,v 1.1.2.5 2010/10/25 18:12:08 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.EsbService;

/**
 * Establece comunicaci&oacute;n con servicios de ESB.
 *
 * @author lvillegas
 * @version  $Revision: 1.1.2.5 $ $Date: 2010/10/25 18:12:08 $
 */
public class EsbServiceImpl implements EsbService {

    /**
     * Constructor por default.
     */
    public EsbServiceImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public void validaCredenciaIfe(String nombreAplicacion, String usuario, String ticket,
           String folio, String anioRegistro, String numeroEmision,
           String ocr) throws SicaException {
       com.ixe.wmb213.ConsultaIfe in = new com.ixe.wmb213.ConsultaIfe();
       
       com.ixe.wmi.wmbmessage.header.Header h = new com.ixe.wmi.wmbmessage.header.Header();
       h.setNombreAplic(nombreAplicacion);
       h.setUsuario(usuario);
       h.setTicket(ticket);
       h.setVersionHeader("1.1.0");
       h.setVersionServicio("1.0.0");
       h.setAreaUsuario("");
       
       com.ixe.wmb213.Request r = new com.ixe.wmb213.Request();
       r.setAnioRegistro(anioRegistro);
       r.setCanal("SICA");
       r.setFolio(folio);
       r.setNumEmision(numeroEmision);
       r.setOCR(ocr);
       
       in.setHeader(h);
       in.setRequest(r);
       
       com.ixe.wmb213.ConsultaIfe out = null;
       String ruta = "archivos/sica/";
       try {
           ruta = getSicaServiceData().findParametro("RUTA_CONFIGURACION_MQ").getValor();
       }
       catch (Exception e) {
           if (logger.isWarnEnabled()) {
               logger.warn("No se encontr\u00F3 el par\u00E1metro RUTA_CONFIGURACION_MQ.", e);
           }
       }
       try {
           out = new com.ixe.wmb213.ServConsultaIfe().ejecutaConsultaIfe(in, null, ruta);
       }
       catch (Exception e) {
           if (logger.isWarnEnabled()) {
               logger.warn("No se pudo realizar la validaci\u00F3n de la ife " + out, e);
           }
           throw new SicaException("Se ha producido un error en la ejecuci\u00F3n " +
               "de la solicitud para validar la credencial IFE.");
       }
       int codigoRespuesta = -1;
       if (out != null && out.getResponse() != null && out.getResponse().getCodigo() != null) {
           codigoRespuesta = out.getResponse().getCodigo().intValue();
       }
       switch(codigoRespuesta) {
           case Num.I_0:
               return;
           case Num.I_1:
               throw new SicaException("La credencial IFE no existe.");
           case Num.I_2:
               throw new SicaException("La credencial IFE fue dada de baja.");
           case Num.I_3:
               throw new SicaException("La credencial IFE fue sustraída.");
           default:
               throw new SicaException("Se ha producido un error en la ejecuci\u00F3n " +
                   "de la solicitud para validar la credencial IFE.");
       }
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
     * Referencia al bean sicaServiceData.
     */
    private SicaServiceData sicaServiceData;
   
    /**
     * El objeto para logging.
     */
    private final transient Log logger = LogFactory.getLog(getClass());
}
