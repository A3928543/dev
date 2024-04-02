/*
 * $Id: EdicionPlantillaNacional.java,v 1.14 2008/02/22 18:25:01 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.deals.plantillas;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.components.FormasPagoLiqSelectionModel;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaCuentaIxe;
import com.ixe.ods.sica.model.PlantillaNacional;
import com.ixe.ods.sica.model.PlantillaPantalla;
import com.ixe.ods.sica.pages.catalogos.AbstractCatEdicion;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.legosoft.tapestry.model.RecordSelectionModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

/**
 * Clase para el Alta de Plantillas.
 * 
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.14 $ $Date: 2008/02/22 18:25:01 $
 */
public abstract class EdicionPlantillaNacional extends AbstractCatEdicion {

    /**
     * Inserta en la Base de Datos una Plantilla.
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void actualizar(IRequestCycle cycle) {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
    	if (getModoSubmit() == 1) {
	    	if (delegate != null && delegate.getHasErrors()) {
	    		return;
            }
	    	((Plantilla)getRegistro()).setUltimaModificacion(new Date());
	    	if (StringUtils.isNotEmpty(((Plantilla)getRegistro()).getClavePlantilla())) {
	    	    ((Plantilla)getRegistro()).setClavePlantilla(((Plantilla)getRegistro()).getClavePlantilla().toUpperCase());
	    	}
	    	if (isModoUpdate()) {
	            getSicaServiceData().update(getRegistro());
	        }
	        else {
	        	//((Plantilla)getRegistro()).setMnemonico(getSelectedMnemonico().get("mnemonico").toString());
	        	if (((Plantilla)getRegistro()) instanceof PlantillaCuentaIxe) {
	        		if (StringUtils.isEmpty(((PlantillaCuentaIxe)getRegistro()).getNoCuentaIxe()) && delegate != null) {
	        			delegate.record("No se puede dar de Alta la Plantilla porque el Cliente no tiene Cuentas Eje.", null);
		        		return;
	        		}
	        		
	        	}
	        	if (((Plantilla)getRegistro()) instanceof PlantillaNacional) {
		        	if (((PlantillaNacional)getRegistro()).getBeneficiario().getIdPersona().intValue() < 0) {
		        		delegate.record("Debe seleccionar un Beneficiario para dar de alta la Plantilla.", null);
		        		return;
		        	}
		        	if ("Seleccione un Mnemonico".equals(((PlantillaNacional)getRegistro()).getMnemonico())) {
		        		delegate.record("Debe seleccionar un Mnemonico para dar de alta la Plantilla.", null);
		        		return;
		        	}
	        		
	        		if (getNombreCliente().equals(((PlantillaNacional)getRegistro()).getBeneficiario().getNombreCompleto())) {
		        	    ((PlantillaNacional)getRegistro()).setStatusPlantilla(Plantilla.STATUS_PLANTILLA_ACTIVA);
		            }
	        	}
	        	getSicaServiceData().store(getRegistro());
	        }
	        cycle.activate(getNombrePaginaConsulta());
    	}
    }

    /**
     * Obtiene el Modelo de los Status de las Plantillas sin la Parte de Activacion.
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getStatusPlantillasSinActivar() {
		return new StringPropertySelectionModel(new String[] {"PE", "SU"});
	}

	/**
	 * Obtiene el Modelo del Status de las Plantillas unicamente con la Parte de Activacion.
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getStatusPlantillasActivar() {
		return new StringPropertySelectionModel(new String[] {"AC"});
	}

	/**
	 * Obtiene el Modelo del Combo CLABE o Cuenta.
	 * 
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getComboCLABECuenta() {
		return new StringPropertySelectionModel(new String[] {"CLABE", "Cuenta"});
	}
	
	/**
	 * Obtiene los mnemonicos para plantilla pantalla.
	 * 
	 * @param nombrePantalla
	 * @return List
	 */
    private List getFormasPagoLiqParaPantalla(String nombrePantalla) {
        List pps = getSicaServiceData().findMnemonicosByPlantillaPantalla(nombrePantalla);
        List mnems = new ArrayList();
        for (Iterator it = pps.iterator(); it.hasNext();) {
            PlantillaPantalla pp = (PlantillaPantalla) it.next();
            FormaPagoLiq fpl = getFormaPagoLiq(pp.getMnemonico());
            if (fpl != null && fpl.getDesplegableSica().booleanValue()
                    && Constantes.MNEMONICO_ACTIVO.equals(fpl.getStatus())) {
                mnems.add(getFormaPagoLiq(pp.getMnemonico()));
            }
        }
        return mnems;
    }

    /**
	 * Obtiene el Modelo de los Mnemonicos de las Plantillas Nacionales.
	 * 
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getMnemonicosPlantNacModel() {
		List fpl  = getFormasPagoLiqParaPantalla(PlantillaPantalla.CAPTURA_NACIONAL);
		FormaPagoLiq primerElemento = new  FormaPagoLiq();
		primerElemento.setIdMatriz(new Long("0"));
		primerElemento.setMnemonico("Seleccione un Mnemonico");
		fpl.add(0, primerElemento);
        return new FormasPagoLiqSelectionModel(fpl);
    }

	/**
	 * Obtiene el Modelo de los Mnemonicos de las Plantillas Cuenta Ixe.
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getMnemonicosPlantCtaIxeModel() {
        return new FormasPagoLiqSelectionModel(getFormasPagoLiqParaPantalla(PlantillaPantalla.CAPTURA_CUENTA_IXE));
    }

	/**
	 * Obtiene el Modelo de las Cuentas Eje.
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCuentasEjeModel() {
		Persona persona = getSicaServiceData().findPersonaContratoSica(((Plantilla)getRegistro()).getContratoSica().getNoCuenta());
		return new StringPropertySelectionModel(getSicaServiceData().findCuentasEje(persona.getIdPersona()));
    }

	/**
     * Modelo para la lista de beneficiarios del cliente.
     * 
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getBeneficiariosModel() {
    	List beneficiarios = getSicaServiceData().findBeneficiariosByNoCuentaByIdRol(((Plantilla)getRegistro()).getContratoSica().getNoCuenta(),
    			FacultySystem.BENEFICIARIO_SICA_ROL);
    	Persona primerElemento = new Persona();
    	primerElemento.setIdPersona(new Integer(-1));
    	primerElemento.setNombreCorto("Seleccione un Beneficiario");
    	beneficiarios.add(0, primerElemento);
    	return new RecordSelectionModel(beneficiarios, "idPersona", "nombreCorto");
    }
	
    /**
     * Regresa el valor de clabeOCuenta.
     *
	 * @return String.
	 */
	public abstract String getClabeOCuenta();

    /**
     * Regresa el valor de modoSubmit.
     *
	 * @return int.
	 */
	public abstract int getModoSubmit();

    /**
     * Regresa el valor de idPersona.
     *
     * @return Integer.
     */
    public abstract Integer getIdPersona();

    /**
     * Establece el valor de idPersona.
     *
     * @param idPersona El valor a asignar.
     */
    public abstract void setIdPersona(Integer idPersona);
    
    /**
     * Regresa el valor de nombreCliente.
     *
     * @return String.
     */
    public abstract String getNombreCliente();

	/**
     * Regresa el valor de nombreBanco.
     *
	 * @return String.
	 */
	public abstract String getNombreBanco();

    /**
     * Fija el valor de clabeOCuenta.
     *
	 * @param clabeOCuenta El valor a asignar.
	 */
	public abstract void setClabeOCuenta(String clabeOCuenta);

    /**
     * Fija el valor de modoSubmit.
     *
	 * @param modoSubmit El valor a asignar.
	 */
    public abstract void setModoSubmit(int modoSubmit);

    /**
     * Fija el valor de nombreCliente.
     *
     * @param mombreCliente El valor a asignar.
     */
    public abstract void setNombreCliente(String mombreCliente);

    /**
     * Fija el valor de nombreBanco.
     *
	 * @param nombreBanco El valor a asignar.
	 */
	public abstract void setNombreBanco(String nombreBanco);
	}