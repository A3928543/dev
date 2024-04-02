/*
 * $Id: CapturaCuentaIxe.java,v 1.17.46.4.12.1.16.1.12.1 2018/05/15 21:55:09 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.deals.formliq;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.valid.IValidationDelegate;

import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Cliente;
import com.ixe.ods.sica.model.Contrato;
import com.ixe.ods.sica.model.CuentaAltamira;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaCuentaIxe;
import com.ixe.ods.sica.pages.CapturaBeneficiarios;
import com.ixe.ods.sica.pages.CapturaBeneficiariosCuentaIxe;
import com.ixe.ods.sica.pages.deals.plantillas.IAsignacionBeneficiario;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Clase para la Captura de Plantillas Cuenta Ixe.
 *
 * @author Javier Cordova
 * @version  $Revision: 1.17.46.4.12.1.16.1.12.1 $ $Date: 2018/05/15 21:55:09 $
 */
public abstract class CapturaCuentaIxe extends AbstractCapturaLiq
		implements IAsignacionBeneficiario {
	
	public void activate(IRequestCycle cycle) {
		DealDetalle det = getDealDetalle();
		List cuentaAltamiraList = null;
        CuentaAltamira cuentaAltamira = null;
		String mnemonico = null;
		
		super.activate(cycle);
		if(getCr() == null)
			setCr("");
		
		_logger.debug(" --> CapturaCuentaIxebuscar.activate(), det.getPlantilla().getMnemonico(): " + det.getPlantilla().getMnemonico());
        if(det != null && det.getPlantilla() != null && det.getPlantilla().getMnemonico() != null && 
                          ("RUSDCARGIXEB".equals(det.getPlantilla().getMnemonico().toUpperCase()) || 
                           "RMXNCARGIXEB".equals(det.getPlantilla().getMnemonico().toUpperCase()))      )
        {
    		_logger.debug(" --> CapturaCuentaIxebuscar.activate(): buscar cr de la plantilla en sc_cuenta_altamira..");
    		_logger.debug(" --> CapturaCuentaIxebuscar.activate(), ((PlantillaCuentaIxe)det.getPlantilla()).getNoCuentaIxe(): " + 
    				       ((PlantillaCuentaIxe)det.getPlantilla()).getNoCuentaIxe());
    		
            cuentaAltamiraList = getSicaServiceData().findCuentaAltamiraByNoCuenta(((PlantillaCuentaIxe)det.getPlantilla()).getNoCuentaIxe());
            
    		if(cuentaAltamiraList != null && !cuentaAltamiraList.isEmpty())
    		{
    			cuentaAltamira = (CuentaAltamira)cuentaAltamiraList.get(0);
    			_logger.debug(" --> CapturaCuentaIxebuscar.activate(): cuentaAltamira.getCr() " + cuentaAltamira.getCr() + 
    					      ", cuentaAltamira.getIdPersona(): " + cuentaAltamira.getIdPersona());
    			setCr(cuentaAltamira.getCr());
    		}
        }
    }

    /**
     * Crea la Plantilla Cuenta Ixe.
     */
    public void crearPlantilla() {
        PlantillaCuentaIxe plantilla = new PlantillaCuentaIxe();
        DealDetalle det = getDealDetalle();
        plantilla.setMnemonico(det.getMnemonico());
        plantilla.setContratoSica(det.getDeal().getContratoSica());
        // Las plantillas cuenta Ixe deben estar activas desde el principio:
        plantilla.setStatusPlantilla(Plantilla.STATUS_PLANTILLA_ACTIVA);
        if (det.isRecibimos()) {
            plantilla.setBeneficiario(det.getDeal().getCliente());
        }
        det.setPlantilla(plantilla);
        setPaginaAnterior(null); //
    }

    /**
     * Guarda y actualiza los datos de la Plantilla y del Detalle del Deal respectivamente en la Base de Datos.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {
        IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
        try {
        	if ("5".equals(getModoSubmit())) {
        		String[] cuentas = getSicaServiceData().findCuentasEje(getDealDetalle().getPlantilla().getBeneficiario().getIdPersona());
                if (getDealDetalle().getPlantilla().getBeneficiario().getIdPersona().intValue() < 0) {
                	throw new SicaException("No se ha definido el nombre del beneficiario para la plantilla.");
                }
        		if ("".equals(cuentas[0])) {
        			throw new SicaException("No se encontraron cuentas eje para el beneficiario seleccionado.");
        		}
            	return;
            }
        	if (delegate.getHasErrors()) {
                return;
            }
            DealDetalle det = getDealDetalle();
            PlantillaCuentaIxe plantilla = (PlantillaCuentaIxe) det.getPlantilla();
            if (StringUtils.isNotEmpty(plantilla.getClavePlantilla())) {
                plantilla.setClavePlantilla(plantilla.getClavePlantilla().toUpperCase());
            }
            
            //Funcionalidad MODO bup
            if(getSicaServiceData().findParametro(ParametroSica.MODO_BUP_ALTAMIRA).getValor().equals("BUP")) {
            	
            	if (StringUtils.isEmpty(plantilla.getNoCuentaIxe())) {
                    throw new SicaException("No se puede dar de Alta la Plantilla porque el Cliente no tiene Cuentas Eje.");
                }
                if (!(plantilla.getNoCuentaIxe().length() == 9 || plantilla.getNoCuentaIxe().length() == 12)) {
                    throw new SicaException("El n\u00famero de cuenta debe tener 7 \u00F3 10 d\u00EDgitos num\u00E9ricos, " +
                            " seguidos de un gui\u00F3n y al finalizar un d\u00EDgito num\u00E9rico.");
                }
                plantilla.setNoCuentaIxe(plantilla.getNoCuentaIxe().toUpperCase());
                String[] ctasEje = getSicaServiceData().findCuentasEje(det.getPlantilla().getBeneficiario().getIdPersona());
                boolean valida = false;
                for (int i = 0; i < ctasEje.length && !valida; i++) {
                    String cta = ctasEje[i];
                    if (cta.trim().equals(plantilla.getNoCuentaIxe().trim())) {
                        String monedaCuenta = getSicaServiceData().findDivisaCuenta(cta.trim());
                        if (!plantilla.getMnemonico().trim().substring(1, 4).equals(monedaCuenta.trim())) {
                            throw new SicaException("El Mnem\u00F3nico no corresponde a la Divisa de la Cuenta Ixe.");
                        }
                        valida = true;
                    }
                }
                if (!valida) {
                    throw new SicaException("La cuenta tecleada no corresponde al beneficiario seleccionado.");
                }
            }//Termina Funcionalidad Modo BUP
            else { //Funcionalidad ALTAMIRA y ALTAMIRA_BUP
            	plantilla.setNoCuentaIxe(getNoCuentaIxe());
            }
            //Funcionalidad común.
            if (getDealDetalle().getPlantilla().getBeneficiario().getIdPersona().intValue() < 0) {
        		throw new SicaException("No se ha definido el nombre del beneficiario para la plantilla.");
        	}
            if (det.getDeal().getCliente().getNombreCompleto().equals(plantilla.getBeneficiario().getNombreCompleto())) {
                plantilla.setStatusPlantilla(Plantilla.STATUS_PLANTILLA_ACTIVA);
            }
            
			//F74380 - Validación de Liquidación de Cuentas Banorte en Panel de Autorización Plantillas
            //se obtiene el sic del cliente Altamira, el sic del cliente SICA y si son diferentes se coloca la Plantilla en Pendiente
            List cuentaAltamiraList = null;
            cuentaAltamiraList = getSicaServiceData().findCuentaAltamiraByNoCuenta(((PlantillaCuentaIxe)det.getPlantilla()).getNoCuentaIxe());
            CuentaAltamira cuentaAltamira = null;
            Integer sicAltamira = null;
            Integer sicClienteSica = null;
    		if(cuentaAltamiraList != null && !cuentaAltamiraList.isEmpty())
    		{
    			cuentaAltamira = (CuentaAltamira)cuentaAltamiraList.get(0);
    			sicAltamira = cuentaAltamira.getSic();
    			_logger.debug(" --> CapturaCuentaIxe.submit(): cuentaAltamira.getSic() " + sicAltamira);
    		}
    		Integer idPersona = det.getDeal().getCliente().getIdPersona();
    		Contrato contrato = getSicaServiceData().findContratoByNoCuenta(det.getDeal().getContratoSica().getNoCuenta());
    		if (contrato != null) {            
	    		Integer idCliente = contrato.getIdCliente();
	    		if (idPersona != null && idCliente != null) {
		    		List clientes = getSicaServiceData().findClienteByIdPersonaAndIdCliente(idPersona, idCliente);
		    		if (clientes != null && clientes.size() > 0) {
		    			Cliente clienteSica = (Cliente)clientes.get(0);
		    			try {
		    				sicClienteSica = Integer.valueOf(clienteSica.getSic());
		    			} catch (NumberFormatException nfe) {
							sicClienteSica = new Integer(0);
						}
		    			_logger.debug(" --> CapturaCuentaIxe.submit(): clienteSica.getSic() " + sicClienteSica);
		    		}
	    		}
    		}
    		if (sicAltamira != null && sicClienteSica != null &&  sicAltamira.equals(sicClienteSica)) {
    			plantilla.setStatusPlantilla(Plantilla.STATUS_PLANTILLA_ACTIVA);
    		} else {
    			plantilla.setStatusPlantilla(Plantilla.STATUS_PLANTILLA_PENDIENTE);
    		}
    		
            if (plantilla.getIdPlantilla() == 0) {
                getSicaServiceData().storePlantillaParaDealDetalle(det, plantilla);
            }
            else {
                getSicaServiceData().update(getDealDetalle());
            }
            if (!Plantilla.STATUS_PLANTILLA_ACTIVA.equals(plantilla.getStatusPlantilla())) {
                marcarSolicitudAutorizacionPorPlantilla(getDealDetalle());
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
     * Asigna el Beneficiario a la Plantilla.
     *
     * @param beneficiario El destinatario de la transferencia.
     */
	public void creaBeneficiario(Persona beneficiario) {
		getDealDetalle().getPlantilla().setBeneficiario(beneficiario);
	}

    /**
     * Agrega un beneficiario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void agregaBeneficiario(IRequestCycle cycle){
    	
    	//aqui cambiar direccionamiento si la pantalla anterior es CapturacuentaIXE (si si es, ya estás en ella babas)
    	//el switch bup está encendido.
    	if(getSicaServiceData().findParametro(ParametroSica.MODO_BUP_ALTAMIRA).getValor().equals("BUP")) {
    		CapturaBeneficiarios captura = (CapturaBeneficiarios)cycle.getPage("CapturaBeneficiarios");
    		
    		Deal deal = getDealDetalle().getDeal();
            captura.setContratoSica(deal.getContratoSica());
            captura.setIdPersonaCliente(deal.getCliente().getIdPersona().intValue());
            captura.setNombreCliente(deal.getCliente().getNombreCompleto());
            captura.setPaginaAnterior(getPageName());
            captura.activate(cycle);
    	}else{
    		CapturaBeneficiariosCuentaIxe captura = 
    			(CapturaBeneficiariosCuentaIxe)cycle.getPage("CapturaBeneficiariosCuentaIxe");
    		
    		Deal deal = getDealDetalle().getDeal();
            captura.setContratoSica(deal.getContratoSica());
            captura.setIdPersonaCliente(deal.getCliente().getIdPersona().intValue());
            captura.setNombreCliente(deal.getCliente().getNombreCompleto());
            captura.setPaginaAnterior(getPageName());
            captura.setMnemonico(getDealDetalle().getMnemonico());
            captura.activate(cycle);
    	}
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

    /**
     * Obtiene el Modelo a mostrar en el Combo de Cuentas Eje.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCuentasEjeModel() {
		return new StringPropertySelectionModel(getSicaServiceData().findCuentasEje(getDealDetalle().getPlantilla().getBeneficiario().getIdPersona()));
    }

	/**
     * Regresa el valor de modo_submit.
     *
     * @return String.
     */
    public abstract String getModoSubmit();
    
    
    /**
     * Setter noCuentaIxe
     */
    public abstract void setNoCuentaIxe(String noCuentaIxe);
    
    /**
     * Getter noCuentaIxe
     */
    public abstract String getNoCuentaIxe();
    
    /**
     * Establece la P&aacute;gina a la que se debe de regresar en caso de Cancelaci&oacute;n.
     *
     * @param paginaAnterior La P&aacute;gina.
     */
    public abstract void setPaginaAnterior(String paginaAnterior);

    /**
     * Obtiene la P&aacute;gina a la que se debe de regresar en caso de Cancelaci&oacute;n.
     *
     * @return String El nombre de la P&aacute;gina.
     */
    public abstract String getPaginaAnterior();
    
    /**
     * Obtiene el cr del cliente que esta asignado a la plantilla de RUSDCARGIXEB,RMXNCARGIXEB
     * @return String CR
     */
    public abstract String getCr();
    
    /**
     * Fija el valor del cr del cliente asignado a la plantilla de RUSDCARGIXEB,RMXNCARGIXEB
     * @param cr
     */
    public abstract void setCr(String cr);

    
    /** Constante paa identificar la captura de Cuenta IXE */
    public static final String CAPTURA_CUENTA_IXE = "CapturaCuentaIxe"; 
    
    /** Constante para redirigir a la pagina de captura beneficiarios cuenta ixe */
    public static final String PAGINA_CAPTURA_BENEFICIARIOS = "CapturaBeneficiariosCuentaIxe";
}
