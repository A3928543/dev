/*
 * $Id: ContratoDireccionesServiceImpl.java,v 1.15 2009/08/19 20:51:45 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import com.ixe.bean.bup.Direccion;
import com.ixe.bean.bup.Expediente;
import com.ixe.bean.bup.Persona;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaBusquedaPersonaService;
import com.ixe.ods.sica.SicaConsultaDireccionesPersonaService;
import com.ixe.ods.sica.SicaConsultaProductosPersonaService;
import com.ixe.ods.sica.SicaConsultaRelacionesCuentaService;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.ValorFuturoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementa de la interfaz ContratoDireccionesService.  
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.15 $ $Date: 2009/08/19 20:51:45 $
 * @see com.ixe.ods.sica.services.ContratoDireccionesService
 */
public class ContratoDireccionesServiceImpl implements com.ixe.ods.sica.services.ContratoDireccionesService {

    /**
     * @see com.ixe.ods.sica.services.ContratoDireccionesService#asignarContrato(String, com.ixe.ods.sica.model.Deal, boolean).
     */
    public void asignarContrato(String noCuenta, Deal deal, boolean valorFuturo) throws SicaException {
        SicaServiceData sd = getSicaServiceData();
        ContratoSica cs = sd.findContratoSicaConSectorEconomico(noCuenta);
        deal.setContratoSica(cs);
        if (deal.getCliente().getSectorEconomico() == null) {
        	deal.setContratoSica(null);
        	throw new SicaException("No es posible asignar el contrato pues el cliente no tiene " +
        			"Sector Econ\u00f3mico.");
    	}
        try {
            _valorFuturoService.validarSectorEconomicoPorFechaValor(deal.getTipoValor(), deal, valorFuturo);
        }
        catch (SicaException e) {
            deal.setContratoSica(null);
            throw e;
        }
        try {
            deal.setRfcFactura(deal.getCliente().getRfc());
            deal.setNombreFactura(deal.getCliente().getNombreCompleto());
            deal.setDirFactura(getDireccionFiscalParaPersona(deal.getCliente().getIdPersona().intValue()));
            deal.setDireccion(deal.getDirFactura());
            deal.setNoCuentaIxe(getCuentaIxeCliente(deal));
        }
        catch (Exception e) {
            if (_logger.isWarnEnabled()) {
                _logger.warn("No se encontr\u00f3 la direcci\u00f3n fiscal para la persona " +
                        deal.getCliente().getIdPersona());
            }
            // throw new SicaException("No se encontr\u00f3 la direcci\u00f3n fiscal para la persona " + deal.getCliente().getIdPersona());
        }
        deal.setPromotor(sd.findPromotorByContratoSica(cs.getNoCuenta()));
        //Verificar Documentacion
        List relacionesCuenta = getSicaConsultaRelacionesCuentaService().
                obtenRelacionesCtaDocumentacion(getSicaConsultaProductosPersonaService().
                        obtenCuentaContrato(cs.getNoCuenta()));
        if (relacionesCuenta!= null && relacionesCuenta.size() > 0) {
            Map map;
            for (Iterator i = relacionesCuenta.iterator(); i.hasNext(); ) {
                map = (HashMap) i.next();
                Expediente expediente = (Expediente) map.get("expediente");  
                if (Constantes.EXP_DOC_STATUS.equals(expediente.getStatus())) {
                    deal.setEvento(Deal.EV_SOLICITUD,
                            deal.isInterbancario() ? Deal.EV_IND_INT_DOCUMENTACION : Deal.EV_IND_DOCUMENTACION);
                    break;
                }
            }
        }
//        else {
//            deal.setEvento(Deal.EV_SOLICITUD,
//                    deal.isInterbancario() ? Deal.EV_IND_INT_DOCUMENTACION : Deal.EV_IND_DOCUMENTACION);
//        }
        // Ver si es necesario incrementar el numero de excepciones de documentacion:
        if (Deal.EV_SOLICITUD.equals(deal.decodificarEvento(deal.isInterbancario() ? Deal.EV_IND_INT_DOCUMENTACION : Deal.EV_IND_DOCUMENTACION))) {
            cs.setNumeroExcepcionesDoc(cs.getNumeroExcepcionesDoc() + 1);
            sd.update(cs);
            ParametroSica par = sd.findParametro("MAX_EXCEDENTES_DOC");
            if (cs.getNumeroExcepcionesDoc() > Integer.valueOf(par.getValor()).intValue()) {
                throw new SicaException("El cliente seleccionado ha excedido el m\u00e1ximo de excepciones por " +
                        "documentaci\u00f3n permitidas.");
            }
        }
    }

    /**
     * Regresa el primer n&uacute;mero de cuenta de cheques del cliente, o null si no existe.
     *
     * @param deal El deal a revisar.
     * @return String.
     */
    private String getCuentaIxeCliente(Deal deal) {
        String[] cuentas = getSicaServiceData().findCuentasEje(deal.getCliente().getIdPersona());
        if ("".equals(cuentas[0])) {
            return null;
        }
        return cuentas[0];
    }  

    /**
     * @see com.ixe.ods.sica.services.ContratoDireccionesService#getDireccionFiscalParaPersona(int).
     */
    public com.ixe.ods.bup.model.Direccion getDireccionFiscalParaPersona(int idPersona) {
        try {
            SicaBusquedaPersonaService sbps = getSicaBusquedaPersonaService();
            Persona persona = sbps.buscaPersonaPorId(idPersona);
            SicaConsultaDireccionesPersonaService scdps = getSicaConsultaDireccionesPersonaService();
            List direcciones = (ArrayList) scdps.obtenDirecciones(persona, com.ixe.bean.Constantes.ID_RES_ARRAY_LIST);
            for (Iterator it = direcciones.iterator(); it.hasNext();) {
                Direccion dir = (Direccion) it.next();
                if ("S".equals(dir.getEsFiscal())) {
                    return getSicaServiceData().findDireccion(dir.getIdDireccion());
                }
            }
        }
        catch(Exception e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
        return null;
    }

    /**
     * Regresa el valor de sicaServiceData.
     *
     * @return SicaServiceData.
     */
    public SicaServiceData getSicaServiceData() {
        return _sicaServiceData;
    }

    /**
     * Establece el valor de sicaServiceData.
     *
     * @param sicaServiceData El valor a asignar.
     */
    public void setSicaServiceData(SicaServiceData sicaServiceData) {
        _sicaServiceData = sicaServiceData;
    }

    /**
     * Regresa el valor de sicaConsultaRelacionesCuentaService.
     *
     * @return SicaConsultaRelacionesCuentaService.
     */
    public SicaConsultaRelacionesCuentaService getSicaConsultaRelacionesCuentaService() {
        return _sicaConsultaRelacionesCuentaService;
    }

    /**
     * Establece el valor de sicaConsultaRelacionesCuentaService.
     *
     * @param sicaConsultaRelacionesCuentaService El valor a asignar.
     */
    public void setSicaConsultaRelacionesCuentaService(SicaConsultaRelacionesCuentaService sicaConsultaRelacionesCuentaService) {
        _sicaConsultaRelacionesCuentaService = sicaConsultaRelacionesCuentaService;
    }

    /**
     * Regresa el valor de sicaConsultaProductosPersonaService.
     *
     * @return SicaConsultaProductosPersonaService.
     */
    public SicaConsultaProductosPersonaService getSicaConsultaProductosPersonaService() {
        return _sicaConsultaProductosPersonaService;
    }

    /**
     * Establece el valor de sicaConsultaProductosPersonaService.
     *
     * @param sicaConsultaProductosPersonaService El valor a asignar.
     */
    public void setSicaConsultaProductosPersonaService(SicaConsultaProductosPersonaService sicaConsultaProductosPersonaService) {
        _sicaConsultaProductosPersonaService = sicaConsultaProductosPersonaService;
    }

    /**
     * Regresa el valor de sicaConsultaDireccionesPersonaService.
     *
     * @return SicaConsultaDireccionesPersonaService.
     */
    public SicaConsultaDireccionesPersonaService getSicaConsultaDireccionesPersonaService() {
        return _sicaConsultaDireccionesPersonaService;
    }

    /**
     * Establece el valor de sicaConsultaDireccionesPersonaService.
     *
     * @param sicaConsultaDireccionesPersonaService El valor a asignar.
     */
    public void setSicaConsultaDireccionesPersonaService(SicaConsultaDireccionesPersonaService sicaConsultaDireccionesPersonaService) {
        _sicaConsultaDireccionesPersonaService = sicaConsultaDireccionesPersonaService;
    }

    /**
     * Regresa el valor de sicaBusquedaPersonaService.
     *
     * @return SicaBusquedaPersonaService.
     */
    public SicaBusquedaPersonaService getSicaBusquedaPersonaService() {
        return _sicaBusquedaPersonaService;
    }

    /**
     * Establece el valor de sicaBusquedaPersonaService.
     *
     * @param sicaBusquedaPersonaService El valor a asignar.
     */
    public void setSicaBusquedaPersonaService(SicaBusquedaPersonaService sicaBusquedaPersonaService) {
        _sicaBusquedaPersonaService = sicaBusquedaPersonaService;
    }

    /**
     * Establece el valor de valorFuturoService.
     *
     * @param valorFuturoService El valor a asignar.
     */
    public void setValorFuturoService(ValorFuturoService valorFuturoService) {
        _valorFuturoService = valorFuturoService;
    }

    /**
     * La instancia de sicaServiceData, para realizar las operaciones a la base de datos.
     */
    private SicaServiceData _sicaServiceData;

    /**
     * El servicio de contrataci&oacute;n ConsultaRelacionesCuentaService.
     */
    private SicaConsultaRelacionesCuentaService _sicaConsultaRelacionesCuentaService;

    /**
     * El servicio de contrataci&oacute;n ConsultaProductosPersonaService.
     */
    private SicaConsultaProductosPersonaService _sicaConsultaProductosPersonaService;

    /**
     * El servicio de contrataci&oacute;n SicaConsultaDireccionesPersonaService.
     */
    private SicaConsultaDireccionesPersonaService _sicaConsultaDireccionesPersonaService;

    /**
     * El servicio de contrataci&oacute;n SicaBusquedaPersonaService.
     */
    private SicaBusquedaPersonaService _sicaBusquedaPersonaService;

    /**
     * El servicio de valor futuro.
     */
    private ValorFuturoService _valorFuturoService;
    
    /**
     * El objeto para logging.
     */
    private final transient Log _logger = LogFactory.getLog(getClass());
}
