/*
 * $Id: CapturaNacional.java,v 1.15.86.1 2017/05/12 21:41:51 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.deals.formliq;

import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.IPlantilla;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaNacional;
import com.ixe.ods.sica.pages.CapturaBeneficiarios;
import com.ixe.ods.sica.pages.deals.plantillas.IAsignacionBeneficiario;
import com.legosoft.tapestry.model.RecordSelectionModel;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * Clase para la Captura de Plantillas Nacionales.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.15.86.1 $ $Date: 2017/05/12 21:41:51 $
 */
public abstract class CapturaNacional extends AbstractCapturaLiq
        implements IAsignacionBeneficiario {

	/**
     * Crea la Plantilla Nacional
     */
    public void crearPlantilla() {
        PlantillaNacional plantilla = new PlantillaNacional();
        DealDetalle det = getDealDetalle();
        plantilla.setMnemonico(det.getMnemonico());
        plantilla.setContratoSica(det.getDeal().getContratoSica());
        det.setPlantilla(plantilla);
    }

    /**
     * Asigna el Beneficiario a la Plantilla.
     *
     * @param beneficiario El destinatario a asignar.
     */
    public void creaBeneficiario(Persona beneficiario) {
		getDealDetalle().getPlantilla().setBeneficiario(beneficiario);
	}

    /**
     * Guarda y actualiza los datos de la Plantilla y del Detalle del Deal respectivamente en la
     * Base de Datos.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {
    	IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
        if (delegate.getHasErrors()) {
            return;
        }
        try {
        	DealDetalle det = getDealDetalle();
        	IPlantilla plantilla = det.getPlantilla();
        	if (StringUtils.isNotEmpty(plantilla.getClavePlantilla())) {
        		plantilla.setClavePlantilla(plantilla.getClavePlantilla().toUpperCase());
        	}
        	if (plantilla.getIdPlantilla() == 0) {
        		if (det.getDeal().getCliente().getIdPersona().equals(plantilla.getBeneficiario().
        				getIdPersona())) {
        			plantilla.setStatusPlantilla(Plantilla.STATUS_PLANTILLA_ACTIVA);
        		}
        	}
        	if (getDealDetalle().getPlantilla().getBeneficiario().getIdPersona().intValue() < 0) {
        		throw new SicaException("No se ha definido el nombre del beneficiario para la plantilla.");
        	}

        	//576449 - SPID MXN a USD y SPID obligatorio PM transfer domésticas USD
        	Deal deal = det.getDeal();
    		Persona p = deal.getCliente();
    		String ordenanteTipoPersona = p.getTipoPersona();
    		String beneficiarioTipoPersona = plantilla.getBeneficiario().getTipoPersona();
    		String divisa = det.getDivisa().getIdDivisa();
    		if ( det.getMnemonico() != null && det.getMnemonico().startsWith("E") && "USD".equals(divisa) ) {
				if ( "PM".equals(ordenanteTipoPersona) && "PM".equals(beneficiarioTipoPersona) ) {
      			  	System.out.println("Operación no válida.  Las liquidaciones en USD en México se deben registrar vía SPID");
                    throw new SicaException("Operación no válida.  Las liquidaciones en USD en México se deben registrar vía SPID");
				}
    		}

    		
    		if (getDealDetalle().getPlantilla().getIdPlantilla() == 0) {
        		getSicaServiceData().storePlantillaParaDealDetalle(getDealDetalle(), getDealDetalle().
        				getPlantilla());
        	}
            else {
                getSicaServiceData().update(getDealDetalle());
            }
        	if (!Plantilla.STATUS_PLANTILLA_ACTIVA.equals(plantilla.getStatusPlantilla())) {
        		marcarSolicitudAutorizacionPorPlantilla(det);
        	}
        	
        	cycle.activate(cycle.getPage("CapturaDeal"));
        }
        catch (SicaException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
            delegate.record(e.getMessage(), null);
        }
    }

    /**
     * Agrega un beneficiario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void agregaBeneficiario(IRequestCycle cycle) {
        CapturaBeneficiarios captura = (CapturaBeneficiarios) cycle.getPage("CapturaBeneficiarios");
        Deal deal = getDealDetalle().getDeal();
        captura.setContratoSica(deal.getContratoSica());
        captura.setIdPersonaCliente(deal.getCliente().getIdPersona().intValue());
        captura.setNombreCliente(deal.getCliente().getNombreCompleto());
        captura.setPaginaAnterior(getPageName());
        captura.activate(cycle);
    }

    /**
     * Modelo para la lista de beneficiarios del cliente.
     *
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getBeneficiariosModel() {
    	List beneficiarios = getSicaServiceData().findBeneficiariosByNoCuentaByIdRol(getDealDetalle().getDeal().getContratoSica().getNoCuenta(),
    			FacultySystem.BENEFICIARIO_SICA_ROL);
    	Persona primerElemento = new Persona();
    	primerElemento.setIdPersona(new Integer(-1));
    	primerElemento.setNombreCorto("Seleccione un Beneficiario");
    	beneficiarios.add(0, primerElemento);
    	return new RecordSelectionModel(beneficiarios, "idPersona", "nombreCorto");
    }
}
