/*
 * $Id: CatBrokers.java,v 1.15.32.1 2010/09/09 00:30:56 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.catalogos;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidationConstraint;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.bup.model.Status;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Broker;
import com.ixe.ods.sica.pages.CapturaBeneficiarios;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.deals.plantillas.ConsultaPlantillasCuentaIxe;
import com.ixe.ods.sica.pages.deals.plantillas.ConsultaPlantillasIntl;
import com.ixe.ods.sica.pages.deals.plantillas.ConsultaPlantillasNacionales;
import com.ixe.ods.sica.pages.deals.plantillas.ConsultaPlantillasTranNacionales;
import com.ixe.ods.sica.pages.deals.plantillas.IAsignacionBeneficiario;

/**
 * Cat&aacute;logo de Brokers. Permite al usuario capturar nuevos brokers y definir sus plantillas.
 *
 * @author Gerardo Corzo, Jean C. Favila, Javier Cordova
 * @version $Revision: 1.15.32.1 $ $Date: 2010/09/09 00:30:56 $
 */
public abstract class CatBrokers extends SicaPage implements IAsignacionBeneficiario {

    /**
     * Redirige a la p&aacute;gina <code>CatBroker</code> para permitir al usuario modificar la
     * informaci&oacute;n del broker.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void editar(IRequestCycle cycle) {
         Integer idBroker = (Integer) cycle.getServiceParameters()[0];
         List l = getSicaServiceData().findBrokerByIdPersona(idBroker);
         Broker bk = (Broker) l.get(0);
         CatBroker nextPage = (CatBroker) cycle.getPage("CatBroker");
         if (Broker.NO_INSTITUCION_FINANCIERA.equals(bk.getTipoBroker())) {
             nextPage.setTipoBrokerSeleccionado("Inst. No Financiera");
         }
         else {
             nextPage.setTipoBrokerSeleccionado("Inst. Financiera");
         }
         nextPage.setPaginaAnterior("CatBrokers");
         nextPage.setClaveReuters(bk.getClaveReuters());
         nextPage.setPersonaMoral(bk.getId().getPersonaMoral());
         nextPage.setTipoBroker(bk.getTipoBroker());
         nextPage.setVisibleKondor(bk.isVisibleKondor());
         nextPage.setIdSaif(bk.getIdSaif());
         if (bk.isPagoAnticipado()) {
             nextPage.setPagoAnticipadoSeleccionado("S");
         }
         else {
        	 nextPage.setPagoAnticipadoSeleccionado("N");
         }
         nextPage.setTipoOperacion(1);
         nextPage.activate(cycle);
    }

    /**
     * Borra el broker seleccionado de la base de datos.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void eliminar(IRequestCycle cycle) {
    	Integer idBroker = (Integer) cycle.getServiceParameters()[0];
        List l = getSicaServiceData().findBrokerByIdPersona(idBroker);
        Broker bk = (Broker) l.get(0);
        bk.setStatus(Broker.STATUS_INACTIVO);
        this.getSicaServiceData().update(bk);
        ContratoSica cs=(ContratoSica)this.getSicaServiceData().findContratoSicaByIdPersona(idBroker);
        cs.setStatusOrigen("Closed");
        Status st=new Status();
        st.setEstatus(ContratoSica.CLOSED_STATE);        
        cs.setStatus(st);
        getSicaServiceData().update(cs);
    }

    /**
     * Redirige a la p&aacute;gina <code>CatBrokersConsulta</code> para permitir al usuario
     * insertar un nuevo broker.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void insertar(IRequestCycle cycle) {
        CatBrokersConsulta nextPage = (CatBrokersConsulta) cycle.getPage("CatBrokersConsulta");
        nextPage.setPaginaAnterior("CatBrokers");
        nextPage.setBrokers(new ArrayList());
        cycle.activate("CatBrokersConsulta");
    }

    /**
     * Redirige a la p&aacute;gina <code>ConsultaPlantillasIntl</code> para permitir al usuario
     * insertar una nueva plantilla.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void plantillasIntl(IRequestCycle cycle) {
    	ConsultaPlantillasIntl nextPage = (ConsultaPlantillasIntl) cycle.
                getPage("ConsultaPlantillasIntl");
        try {
            ContratoSica cs = getContratoParaPersona((Integer) cycle.getServiceParameters()[1]);
            nextPage.setContratoSica(cs);
            nextPage.setTipoPlantilla(cycle.getServiceParameters()[0].toString());
            nextPage.setNombreCliente(cycle.getServiceParameters()[2].toString());
            nextPage.setPaginaAnterior(cycle.getServiceParameters()[3].toString());
            nextPage.setIdPersona((Integer) cycle.getServiceParameters()[4]);
            cycle.activate("ConsultaPlantillasIntl");
        }
        catch (SicaException e) {
            IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
            delegate.record(e.getMessage(), ValidationConstraint.REQUIRED);
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
    }
    
    /**
     * Redirige a la p&aacute;gina <code>ConsultaPlantillasTranNacionales</code> para permitir al
     * usuario insertar una nueva plantilla.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void plantillasTranNal(IRequestCycle cycle) {
        try {
            ConsultaPlantillasTranNacionales nextPage = (ConsultaPlantillasTranNacionales) cycle.
                    getPage("ConsultaPlantillasTranNacionales");
            ContratoSica cs = getContratoParaPersona((Integer) cycle.getServiceParameters()[1]);
            nextPage.setContratoSica(cs);
            nextPage.setTipoPlantilla(cycle.getServiceParameters()[0].toString());
            nextPage.setNombreCliente(cycle.getServiceParameters()[2].toString());
            nextPage.setPaginaAnterior(cycle.getServiceParameters()[3].toString());
            nextPage.setIdPersona((Integer) cycle.getServiceParameters()[4]);
            cycle.activate("ConsultaPlantillasTranNacionales");
        }
        catch (SicaException e) {
            IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
            delegate.record(e.getMessage(), ValidationConstraint.REQUIRED);
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
    }

    /**
     * Redirige a la p&aacute;gina <code>ConsultaPlantillasCuentaIxe</code> para permitir al usuario
     * insertar una nueva plantilla.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void plantillasCtaIxe(IRequestCycle cycle) {
        try {
        	ConsultaPlantillasCuentaIxe nextPage = (ConsultaPlantillasCuentaIxe) cycle.
                    getPage("ConsultaPlantillasCuentaIxe");
            ContratoSica cs = getContratoParaPersona((Integer) cycle.getServiceParameters()[1]);
            nextPage.setContratoSica(cs);
            nextPage.setTipoPlantilla(cycle.getServiceParameters()[0].toString());
            nextPage.setNombreCliente(cycle.getServiceParameters()[2].toString());
            nextPage.setPaginaAnterior(cycle.getServiceParameters()[3].toString());
            nextPage.setIdPersona((Integer) cycle.getServiceParameters()[4]);
            cycle.activate("ConsultaPlantillasCuentaIxe");
        }
        catch (SicaException e) {
            IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
            delegate.record(e.getMessage(), ValidationConstraint.REQUIRED);
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
    }
    
    /**
     * Redirige a la p&aacute;gina <code>ConsultaPlantillasNacionales</code> 
     * para permitir al usuario insertar una nueva plantilla.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void plantillasCheques(IRequestCycle cycle) {
        try {
            ConsultaPlantillasNacionales nextPage = (ConsultaPlantillasNacionales) cycle.
                    getPage("ConsultaPlantillasNacionales");
            ContratoSica cs = getContratoParaPersona((Integer) cycle.getServiceParameters()[1]);
            nextPage.setContratoSica(cs);
            nextPage.setTipoPlantilla(cycle.getServiceParameters()[0].toString());
            nextPage.setNombreCliente(cycle.getServiceParameters()[2].toString());
            nextPage.setPaginaAnterior(cycle.getServiceParameters()[3].toString());
            nextPage.setIdPersona((Integer) cycle.getServiceParameters()[4]);
            cycle.activate("ConsultaPlantillasNacionales");
        }
        catch (SicaException e) {
            IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
            delegate.record(e.getMessage(), ValidationConstraint.REQUIRED);
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
    }

    /**
     * Regresa el contrato sica para la persona especificada.
     *
     * @param idPersona El n&uacute;mero de la persona en la bup.
     * @return ContratoSica.
     * @throws SicaException Si la persona no tiene un contrato sica asignado.
     */
    private ContratoSica getContratoParaPersona(Integer idPersona) throws SicaException {
        ContratoSica cs = getSicaServiceData().findContratoSicaByIdPersona(idPersona);
        if (cs == null) {
            throw new SicaException("No se encontr\u00f3 contrato para la contraparte "
                    + "seleccionada.");
        }
        return cs;
    }
    
    /**
     * Agrega un beneficiario.
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void agregaBeneficiario(IRequestCycle cycle) {
        try {
            CapturaBeneficiarios nextPage = (CapturaBeneficiarios) cycle.
                    getPage("CapturaBeneficiarios");
            Integer idPersona = (Integer) cycle.getServiceParameters()[0]; 
            ContratoSica cs = getContratoParaPersona(idPersona);
            nextPage.setContratoSica(cs);
            nextPage.setIdPersonaCliente(idPersona.intValue());
            nextPage.setNombreCliente(cycle.getServiceParameters()[1].toString());
            nextPage.setPaginaAnterior(cycle.getServiceParameters()[2].toString());
            nextPage.activate(cycle);
        }
        catch (SicaException e) {
            IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
            delegate.record(e.getMessage(), ValidationConstraint.REQUIRED);
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
    }
    
    /**
     * Asigna el Beneficiario a la Plantilla.
     *
     * @param beneficiario El destinatario de la transferencia.
     */
	public void creaBeneficiario(Persona beneficiario) {
	}
   
}
