package com.ixe.ods.sica.pages.mesa;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.CuentaEjecutivo;
import com.ixe.ods.bup.model.CuentaEjecutivoPK;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Status;
import com.ixe.ods.bup.model.TipoEjecutivo;
import com.ixe.ods.bup.model.UltimaModificacion;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaValidationException;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.ContratoCliente;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.services.PersonaService;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * P&aacute;gina que permite al usuario Asigar cuentas.
 *
 * @author Gerardo Corzo
 * @version  $Revision: 1.7.86.1 $ $Date: 2017/07/29 03:17:56 $
 */
public abstract class AsignacionMasivaCtas extends SicaPage {

    /**
     * M&eacut;etodo que carga todos los promotores de un usuario
     *
     * @param cycle El ciclo de la p&aacute;gina.
     *
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        Visit v = (Visit) getVisit();
        List promotores;
        setModo((String) cycle.getServiceParameters()[0]);

        cuentasSeleccionadasList = new ArrayList();
        this.cuentasAsignadasList=new ArrayList();
        this.cuentasString="";
        
        promotores=this.getSicaServiceData().findAllPromotoresSICA(
        		((SupportEngine) getEngine()).getApplicationName());
        
        Collections.sort(promotores, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((EmpleadoIxe) o1).getNombreCompleto().
                        compareTo(((EmpleadoIxe) o2).getNombreCompleto());
            }
        });
        EmpleadoIxe emp = new EmpleadoIxe();
        emp.setIdPersona(new Integer(-1));
        emp.setNombre("-- Seleccione un promotor--");
        promotores.add(0, emp);
        setPromotor(emp);
        setPromotoresList(promotores);
    }

    /**
     * Regresa el modo de la mesa de cambio.
     *
     * @return boolean.
     */
    public boolean isModoMesaCambio() {
        return FacultySystem.MODO_MESA_CAMBIOS.equals(getModo());
    }

    /**
     * Regresa un RecordSelectionModel con todos los ejecutivos.
     *
     * @return IPropertySelectionModel modelo de promotores.
     */
    public IPropertySelectionModel getPromotoresModel() {
        return new RecordSelectionModel(getPromotoresList(), "idPersona", "nombreCompleto");
    }

    /**
     * Regresa un CuentasSelectionModel con todas las cuentas.
     *
     * @return IPropertySelectionModel modelo de cuentas.
     */
    public IPropertySelectionModel getCuentasModel() {
        Visit v = (Visit) getVisit();
        List cuentas = getSicaServiceData().findCuentasEjecutivo(((SupportEngine) getEngine()).
                getIdTipoEjecutivo(), v.getUser().getIdPersona());
        getSicaServiceData().flush();

        for (Iterator it = cuentas.iterator(); it.hasNext();) {
            CuentaEjecutivo ce = (CuentaEjecutivo) it.next();
            ContratoSica cs = getSicaServiceData().findContrato(ce.getId().getNoCuenta());
            if (cs == null || ContratoCliente.clienteParaContratoSica(cs) == null) {
            	it.remove();
            }
        }
        return new CuentasSelectionModel(cuentas, getSicaServiceData());
    }

    /**
     * Regresa un CuentasSelectionModel con todas las cuentas asignadas a un ejecutivo.
     *
     * @return IPropertySelectionModel modelo de cuentas asignadas.
     */
    public IPropertySelectionModel getCuentasAsignadasModel() {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
        getBean("delegate");
        List cuentas = new ArrayList();
        ArrayList aux = new ArrayList();
        int i = 0, x = 0;
        boolean existente = false;

        if (getPromotor() != null && getPromotor().getIdPersona().intValue() > 0) {
            cuentas = getSicaServiceData().findCuentasEjecutivo(((SupportEngine) getEngine()).
                    getIdTipoEjecutivo(), getPromotor().getIdPersona());

            getSicaServiceData().flushEvict(cuentas);
            cuentasAsignadasList = (ArrayList)cuentas;

            if (cuentas.size() > 0) {
                for (Iterator it = cuentas.iterator(); it.hasNext();) {
                    CuentaEjecutivo ce = (CuentaEjecutivo) it.next();
                    ContratoSica cs = getSicaServiceData().findContrato(ce.getId().getNoCuenta());
                    getSicaServiceData().flush();
                    if (ce.getId() == null) {
                        it.remove();
                    }
                    else if (ContratoCliente.clienteParaContratoSica(cs) == null) {
                        it.remove();
                    }
                }

	          
            }
            else {
            	delegate.record("No se encontraron cuentas para el promotor seleccionado.", null);
                cuentas = new ArrayList();
            }
        }

        return new CuentasSelectionModel(cuentas, getSicaServiceData());
    }
    /**
     * Regresa un CuentasSelectionModel con todas las cuentas seleccionadas para transferir
     * a un promotor elegido.
     * @return IPropertySelectionModel cuentas seleccionadas a transferir.
     */
    public IPropertySelectionModel getCuentasSeleccionadasModel() {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
        getBean("delegate");

        if (cuentasSeleccionadasList.size() > 0) {

        	CuentaEjecutivo cuentaEjecutivo =  (CuentaEjecutivo) cuentasSeleccionadasList.get(0);
        }
        else {
        	cuentasSeleccionadasList = new ArrayList();
        }

        return new CuentasSelectionModel(cuentasSeleccionadasList, getSicaServiceData());
    }

    /**
     * M&eacut;etodo que define en que modo se va a trabajar, asignar o des-asignar una cuenta
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {

    	//¿Seleccionó algún promotor origen?
    	if(getPromotor() != null){
    		//¿Es la primera vez que selecciona un promotor origen?
    		if(promotorOnChange == null){
    			//Guardar al promotor elegido para compararlo en la siguiente elección
    			promotorOnChange = getPromotor();
    		}
    		else {
    			//Preguntar si el promotor seleccionado es diferente al promotor seleccionado anterior
    			if(promotorOnChange.getIdPersona().intValue() != getPromotor().getIdPersona().intValue()){
    				//Borrar lista de selección, ya que se eligió otro promotor
    				this.limpiarCtasSeleccionadas();
    				promotorOnChange = getPromotor();
    			}
    		}
    	}

        switch (getModoSubmit()) {
            case SUBMIT_TRANSFERIR:
                asignarCuentas();
                break;
           
            default:
                break;
        }
    }

    /**
     * M&eacut;etodo que des-asigna una cuenta
     * @param cuentaEjecutivoOrigen el objeto CuentaEjecutivo al cual se le desasigna el contrato
     */
    private void desAsignar(CuentaEjecutivo cuentaEjecutivoOrigen) {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");

    	try {
    		Status s = new Status();
    		s.setEstatus(STATUS_CTA_EJE_CANCE);
    		UltimaModificacion um = new UltimaModificacion();
            um.setUsuario(((Visit) getVisit()).getUser().getClave());
            um.setFecha(new Timestamp(new Date().getTime()));
            cuentaEjecutivoOrigen.setUltimaModificacion(um);
            cuentaEjecutivoOrigen.setStatus(s);
            getSicaServiceData().flushEvict(cuentaEjecutivoOrigen);
            getSicaServiceData().desAsignarCuentaEjecutivo(cuentaEjecutivoOrigen);
    	}
    	catch (Exception e) {
    		getSicaServiceData().update(cuentaEjecutivoOrigen);
		}
    }

    /**
     * Asigna una cuenta al promotor destino seleccionado
     * 
     * @param cuentaEjecutivo Objeto CuentaEjecutivo a guardar
     */
    private void asignar(CuentaEjecutivo cuentaEjecutivo) {

            CuentaEjecutivo nvaCta = new CuentaEjecutivo();
            CuentaEjecutivoPK nvaCtaPK = new CuentaEjecutivoPK();
            Status s = new Status();
    		s.setEstatus(STATUS_CTA_EJE_VIG);
            nvaCtaPK.setEjecutivo(getPromotorDest());
            nvaCtaPK.setNoCuenta(cuentaEjecutivo.getId().getNoCuenta());
            nvaCtaPK.setTipo(cuentaEjecutivo.getId().getTipo());
            nvaCta.setId(nvaCtaPK);
            nvaCta.setStatus(s);
            cuentaEjecutivo.setStatus(s);
            UltimaModificacion um = new UltimaModificacion();
            um.setUsuario(((Visit) getVisit()).getUser().getClave());
            um.setFecha(new Timestamp(new Date().getTime()));
            nvaCta.setUltimaModificacion(um);
            //getSicaServiceData().reasignarCuentaEjecutivo(cuentaEjecutivo, nvaCta);

            try{
            	getSicaServiceData().store(nvaCta);
            	getSicaServiceData().flush();
        	}catch (Exception e) {

        		cuentaEjecutivo.getId().setEjecutivo(getPromotorDest());
        		cuentaEjecutivo.setUltimaModificacion(um);
    	    	getSicaServiceData().update(cuentaEjecutivo);
    	    	getSicaServiceData().flush();
    		}
    }
    /**
     * Asignación masiva de cuentas de un promotor a otro.
     */
    private void asignarCuentas() {

    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
    	int i = 0;
    	CuentaEjecutivoPK nvaCtaPKOrigen = new CuentaEjecutivoPK();
    	if(cuentasString.equals("|")){
    		delegate.record("Seleccione almenos un elemento de la lista", null);
    		return;
    	}
    	
    	StringTokenizer strToken=new StringTokenizer(cuentasString,"|");
    	this.debug("Clientes as string: " + this.getClientesString());
    	StringTokenizer nombresTkn = new StringTokenizer(this.getClientesString(), "|");
    	List nombresList = new ArrayList();
    	
    	try{
    		while(nombresTkn.hasMoreElements()) {
    			String nombreCuenta = (String) nombresTkn.nextElement();
    			String[] elems = nombreCuenta.split(" ");
    			if (elems != null && elems.length >= 1) {
    				String cliente = elems[1];
    				if (elems.length >= 2) {
    					cliente = cliente + " " + elems[2];
    				}
    				if (elems.length >= 3) {
    					cliente = cliente + " " + elems[3];
    				}
    				nombresList.add(cliente);
    			}
    		}
    		while(strToken.hasMoreElements()){
    			try{
    				int index=Integer.parseInt((String) strToken.nextElement());
    				cuentasSeleccionadasList.add(this.cuentasAsignadasList.get(index));
    			}catch(Exception e)
    			{
    				continue;
    			}
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	

    	try{

	    	if(cuentasSeleccionadasList.size() > 0 && getPromotor() != null && getPromotor().getIdPersona().intValue() > 0
	    			&& getPromotorDest() != null && getPromotorDest().getIdPersona().intValue() > 0){
	    		String errores = "";
	    		if(getPromotor().getIdPersona().intValue() != getPromotorDest().getIdPersona().intValue()){
		        	for(i = 0; i < cuentasSeleccionadasList.size(); i++) {
		        		CuentaEjecutivo cuentaEjecutivo = (CuentaEjecutivo) cuentasSeleccionadasList.get(i);
	            		String noCuenta = cuentaEjecutivo.getId().getNoCuenta();
		            	try {
			        		CuentaEjecutivo cuentaEjecutivoOrigen = new CuentaEjecutivo();
			        		nvaCtaPKOrigen.setEjecutivo(getPromotor());
			        		nvaCtaPKOrigen.setNoCuenta(noCuenta);
			        		nvaCtaPKOrigen.setTipo(cuentaEjecutivo.getId().getTipo());
			        		cuentaEjecutivoOrigen.setId(nvaCtaPKOrigen);
		            		if (this.getPersonaService().isValidaInformacionGeneralPersona(noCuenta)) {
				        		this.desAsignar(cuentaEjecutivoOrigen);
				        		this.asignar(cuentaEjecutivo);
		            		}
	            		}
		            	catch (SicaValidationException ex) {
		            		debug(ex.getMessage());
		            		errores += noCuenta + "|" + ex.getMessage() + "\n";
		            	}
	            		catch (SicaException ex) {
	            			delegate.record("Error al efectuar la operación, favor de contactar a sistemas.", null);
	            			return;
	            		}
		        	}
		        	if (StringUtils.isNotEmpty(errores)) {
		        		//Envio de correo
		        		delegate.record("Error al efectuar la operaci\u00f3n, algunos de los " +
		        				"clientes procesados tienen su informaci\u00f3n general incompleta " +
		        				"y/o erronea. Se env\u00edo correo electronico indicando la lista de clientes. ", null);
		        		this.getPersonaService().enviaCorreo("AMC", errores);
		        	}
		        	else {
		        		delegate.record("Las cuentas han sido transferidas con éxito.", null);
		        	}
		        	cuentasSeleccionadasList.clear();
	    		}else{
	    			delegate.record("El promotor origen y detino deben ser diferentes.", null);
	    		}
	    	}else{
	    		delegate.record("Seleccione todos los elementos en pantalla.", null);
	    	}

    	}catch (Exception e) {
	    	e.printStackTrace();
	    	setLevel(-1);
	    	delegate.record("Hubo un error al intentar transferir las cuentas.", null);
		}
    	
    	this.setPromotor(null);
    	this.setPromotorDest(null);
    	this.cuentasAsignadasList=new ArrayList();
    }

    /**
     * M&eacut;etodo que se encarga de almacenar las cuentas seleccionadas
     */
    private void seleccionarCuentas() {

    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");

    	if(getCuentaAsignada() != null){
    		CuentaEjecutivo cuentaEjecutivo = getCuentaAsignada();
    		cuentasSeleccionadasList.add(cuentaEjecutivo);
    	}else{
    		delegate.record("Seleccione una cuenta.", null);
    	}
    }

    /**
     * M&eacut;etodo que se encarga de almacenar las cuentas seleccionadas
     */
    private void limpiarCtasSeleccionadas() {

    	cuentasSeleccionadasList.clear();
    }
    /**
     * M&eacut;etodo que se encarga de almacenar todos las cuentas asignadas
     * al propmotor seleccionado.
     */
    private void seleccionarTodasCuentas() {

    	if(cuentasAsignadasList.size() > 0){
    		cuentasSeleccionadasList = cuentasAsignadasList;
    	}
    	else{
    		cuentasSeleccionadasList = new ArrayList();
    }
    }
    /**
     * M&eacut;etodo que se encarga de eliminar de la lista de cuentas
     * seleccionadas, la elegida por el usuario.
     */
    private void eliminarCuentaSeleccionada() {

    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
    	ArrayList aux = new ArrayList();
    	int i = 0;

    	try{

    		if(getCuentaSeleccionada() != null){
		    	for(i = 0; i < cuentasSeleccionadasList.size(); i ++ ){

		    		CuentaEjecutivo cuentaEjecutivo = (CuentaEjecutivo) cuentasSeleccionadasList.get(i);
		    		if(!(cuentaEjecutivo.getId().getNoCuenta().compareTo(getCuentaSeleccionada().getId().getNoCuenta()) == 0)){
		    			aux.add(cuentaEjecutivo);
		    		}
		    	}
		    	cuentasSeleccionadasList = aux;
    		}else{
    			delegate.record("Seleccione la cuenta a cancelar.", null);
    		}
    	}catch (Exception e) {
	    	e.printStackTrace();
	    	setLevel(-1);
	    	delegate.record("Hubo un error al intentar eliminar de la lista la cuenta seleccionada.", null);
			}
    }

    /**
     * M&eacut;etodo que asigna una cuenta a un contrato sica.
     *
     * @param contratoSica El contrato sica al que se asigna la cuenta.
     */
    public void asignarCuenta(ContratoSica contratoSica) {
    	List l = getSicaServiceData().findCuentaEjecutivoByContratoSicaAndStatus(((SupportEngine) getEngine()).getIdTipoEjecutivo(),
    			STATUS_CTA_EJE_CANCE, contratoSica.getNoCuenta(), getPromotor().getIdPersona());
    	getSicaServiceData().flush();
    	if (l.isEmpty()) {
    		TipoEjecutivo te = (TipoEjecutivo) getSicaServiceData().find(TipoEjecutivo.class,
                    ((SupportEngine) getEngine()).getIdTipoEjecutivo());
            CuentaEjecutivo nvaCta = new CuentaEjecutivo();
            CuentaEjecutivoPK nvaCtaPK = new CuentaEjecutivoPK();
            Status status = new Status();
            status.setEstatus("VIG");
            nvaCtaPK.setEjecutivo(getPromotor());
            nvaCtaPK.setNoCuenta(contratoSica.getNoCuenta());
            nvaCtaPK.setTipo(te);
            nvaCta.setId(nvaCtaPK);
            nvaCta.setStatus(status);
            UltimaModificacion um = new UltimaModificacion();
            um.setUsuario(((Visit) getVisit()).getUser().getClave());
            um.setFecha(new Timestamp(new Date().getTime()));
            nvaCta.setUltimaModificacion(um);
            getSicaServiceData().store(nvaCta);
            getSicaServiceData().flush();
    	}
    	else {
    		Status status = new Status();
            status.setEstatus("VIG");
            UltimaModificacion um = new UltimaModificacion();
            um.setUsuario(((Visit) getVisit()).getUser().getClave());
            um.setFecha(new Timestamp(new Date().getTime()));
            CuentaEjecutivo cta = (CuentaEjecutivo)l.get(0);
            cta.setStatus(status);
            cta.setUltimaModificacion(um);
            getSicaServiceData().update(cta);
            getSicaServiceData().flush();
    	}
    }

    /**
     * M&eacut;etodo que pasa a la p&aacute;gina de Consulta de cuentas
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void abrirConsultaCuentas(IRequestCycle cycle) {
        ConsultaCuentas nextPage = (ConsultaCuentas) cycle.getPage("ConsultaCuentas");
        nextPage.activate(cycle);
    }

    /**
     * Regresa el valor de cuentaSeleccionada.
     *
     * @return CuentaEjecutivo.
     */
    public abstract CuentaEjecutivo getCuentaSeleccionada();

    /**
     * Fija el valor de cuentaSeleccionada.
     *
     * @param cuentaSeleccionada El valor a asignar.
     */
    public abstract void setCuentaSeleccionada(CuentaEjecutivo cuentaSeleccionada);

    /**
     * Regresa el valor de cuentaAsignada.
     *
     * @return CuentaEjecutivo.
     */
    public abstract CuentaEjecutivo getCuentaAsignada();

    /**
     * Fija el valor de cuentaAsignada.
     *
     * @param cuentaAsignada El valor a asignar.
     */
    public abstract void setCuentaAsignada(CuentaEjecutivo cuentaAsignada);


    /**
     * Regresa el valor de promotoresList.
     *
     * @return List.
     */
    public abstract List getPromotoresList();

    /**
     * Fija el valor de promotoresList.
     *
     * @param promotoresList El valor a asignar.
     */
    public abstract void setPromotoresList(List promotoresList);

    /**
     * Regresa el valor de promotor.
     *
     * @return EmpleadoIxe.
     */

    public abstract EmpleadoIxe getPromotor();

    /**
     * Fija el valor de promotor.
     *
     * @param promotor El valor a asignar.
     */
    public abstract void setPromotor(EmpleadoIxe promotor);
    /**
     * Regresa el valor de promotor.
     *
     * @return EmpleadoIxe.
     */
    public abstract EmpleadoIxe getPromotorDest();

    /**
     * Fija el valor de promotor.
     *
     * @param promotorDest El valor a asignar.
     */
    public abstract void setPromotorDest(EmpleadoIxe promotorDest);

    /**
     * Regresa el valor de modo.
     *
     * @return String.
     */
    public abstract String getModo();

    /**
     * Fija el valor de modo.
     *
     * @param modo El valor a asignar.
     */
    public abstract void setModo(String modo);

    /**
     * Regresa el valor de modoSubmit.
     *
     * @return int.
     */
    public abstract int getModoSubmit();

    /**
     * Nivel de los mensajes al usuario: -1 Error, 0 Warning, 1 Exito.
     *
     * @param level El valor a asignar.
     */
	public abstract void setLevel(int level);

	public abstract void setClientesString(String clientesString);
	
	public abstract String getClientesString(); 

    /**
     * Constante para indicar la opci&oacute; de asignar una cuenta.
     */
    public static final int SUBMIT_ASIGNAR = 1;

    /**
     * Constante para indicar la opci&oacute; de transferir varias cuentas.
     */
    public static final int SUBMIT_TRANSFERIR = 1;

    /**
     * Constante para indicar las cuentas seleccionadas a transferir
     */
    public static final int SUBMIT_SELECCIONAR = 2;
    /**
     * Constante para indicar que se eliminará de la lista una cuenta
     */
    public static final int SUBMIT_ELIMINAR_SELEC = 3;
    /**
     * Constante para indicar que se eliminarán todas las cuentas de la lista.
     */
    public static final int SUBMIT_ELIMINAR_TODAS = 4;
    /**
     * Constante para indicar que se seleccionarán todas las cuentas asignadas.
     */
    public static final int SUBMIT_SELECCIONAR_TODAS = 5;
    /**
     * Constante para indicar que se limpie la lista de cuentas destino.
     */
    public static final int SUBMIT_CLEAR_LIST = 6;

    /**
     * Constante para indicar la opci&oacute; de des-asignar una cuenta.
     */
    public static final int SUBMIT_DESASIGNAR = 2;

    /**
     * Constante para indicar la opci&oacute; de des-asignar una cuenta.
     */
    public static final String STATUS_CTA_EJE_CANCE = "CANCE";

    /**
     * Constante para indicar la opci&oacute; de des-asignar una cuenta.
     */
	public static final String STATUS_CTA_EJE_VIG = "VIG";

    /**
     * Lista que contiene las cuentas seleccionadas
     */
	private ArrayList cuentasSeleccionadasList = new ArrayList();

    /**
     * Lista que contiene las cuentas asignadas
     */
	private ArrayList cuentasAsignadasList = new ArrayList();

    /**
     * Objeto para saber si se cambió la selección del promotor
     */
	private EmpleadoIxe promotorOnChange = null;
	
	private String cuentasString="";

	/**
	 * @return the cuentasString
	 */
	public String getCuentasString() {
		return cuentasString;
	}

	/**
	 * @param cuentasString the cuentasString to set
	 */
	public void setCuentasString(String cuentasString) {
		this.cuentasString = cuentasString;
	}
	
	/**
     * Gets the persona service.
     *
     * @return the persona service
     */
    private PersonaService getPersonaService() {
    	return (PersonaService) getApplicationContext().getBean("personaService");
    }
}
