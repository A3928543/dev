/*
 * $Id: AltaRapidaDeClientes.java,v 1.17.14.6.4.7.2.7.2.8.4.13.14.4.6.1.10.3.8.5.2.3.6.1 2020/11/05 07:45:25 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages;

import java.lang.reflect.UndeclaredThrowableException;
import java.text.DateFormat;
//import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import com.banorte.www.ws.exception.SicaAltamiraException;
import com.ixe.bean.Constantes;
import com.ixe.bean.Usuario;
import com.ixe.bean.bup.Catalogo;
import com.ixe.bean.bup.Direccion;
import com.ixe.bean.bup.Persona;
import com.ixe.bean.bup.PersonaFisica;
import com.ixe.bean.bup.PersonaMoral;
import com.ixe.bean.bup.RelacionPersonas;
import com.ixe.bean.bup.TipoComprobante;
import com.ixe.contratacion.ContratacionException;
import com.ixe.contratacion.excepcion.FaltaDireccionException;
import com.ixe.contratacion.excepcion.FaltaInfGeneralException;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.seguridad.model.IPerfil;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.SicaBusquedaPersonaService;
import com.ixe.ods.sica.SicaConsultaCatalogosService;
import com.ixe.ods.sica.SicaConsultaDireccionesPersonaService;
import com.ixe.ods.sica.SicaConsultaRelacionContratacionService;
import com.ixe.ods.sica.SicaContratacionProductoBancoService;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaRegistroDireccionPersonaService;
import com.ixe.ods.sica.SicaRegistroPersonaService;
import com.ixe.ods.sica.SicaRegistroRelacionContratacionService;
import com.ixe.ods.sica.SicaValidationException;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
//import com.ixe.ods.sica.model.Divisa;
//import com.ixe.ods.sica.model.Cliente;
import com.ixe.ods.sica.dto.InfoCuentaAltamiraDto;
import com.ixe.ods.sica.model.Cliente;
import com.ixe.ods.sica.model.Contrato;
//import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.EmpresaCambios;
import com.ixe.ods.sica.model.NitPersona;
import com.ixe.ods.sica.model.RegulatorioActividadEconomica;
import com.ixe.ods.sica.model.RegulatorioDatosPM;
import com.ixe.ods.sica.model.RegulatorioEventoRelacion;
import com.ixe.ods.sica.model.RegulatorioInstitucion;
import com.ixe.ods.sica.model.RegulatorioPais;
import com.ixe.ods.sica.model.RegulatorioPerfil;
import com.ixe.ods.sica.model.RegulatorioSectorIndustrial;
import com.ixe.ods.sica.model.RegulatorioSociedadMercantil;
import com.ixe.ods.sica.model.RegulatorioTipoIdentificador;
import com.ixe.ods.sica.model.RegulatorioTipoRelacion;
import com.ixe.ods.sica.model.TipoBloqueo;
import com.ixe.ods.sica.sdo.dto.PersonaDto;
import com.ixe.ods.sica.sdo.dto.RespuestaDto;
import com.ixe.ods.sica.services.InformacionCuentaAltamiraService;
import com.ixe.ods.sica.services.PersonaService;
import com.ixe.ods.sica.utils.DateUtils;
//import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.util.Utilerias;
import com.legosoft.tapestry.model.RecordSelectionModel;
//import org.springframework.orm.hibernate.HibernateTemplate;

/**
 * Clase que contendra la Logica de Negocio para dar de Alta de manera rapida un Cliente en la BUP
 * y posteriormente crearle un Contrato SICA.
 *
 * @author Javier Cordova (jcordova)
 * @version  $Revision: 1.17.14.6.4.7.2.7.2.8.4.13.14.4.6.1.10.3.8.5.2.3.6.1 $ $Date: 2020/11/05 07:45:25 $
 */
public abstract class AltaRapidaDeClientes extends SicaPage {
	
	private static final String OPCION_DEFAULT = "SELECCIONE LA OPCION DESEADA";
	
	private static final Integer ID_DEFAULT = new Integer(-100); 

    /**
     * Limpia variables y precarga informacion cada que se activa la P&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
    	
    	super.activate(cycle);
    	setPersonasBUP(new ArrayList());
        limpiarCampos();
        setFisicaMoral("MORAL");
        setModoSubmit(0);
        setAlta(false);
        setNombreEmpresa("");
        
//        setShowIfe(false);
//        setShowPasaporte(false);
//        setShowInsertDatComp(false);
//        setShowCbNacionalidad(false);
        
        Map b = new HashMap();
        b.put(VALUE,"IXE");
        b.put(LABEL,"IXE");
        setEmpresa(b);
    }

    /**
	 * Limpia las variables de la pagina.
	 */
	public void limpiarCampos() {
	    setPaterno("");
        setMaterno("");
        setNombre("");
        setNacionalidad(null);
        setRazonSocial("");
        setRfc("");
        setSectorBanxico(null);
        setFechaNacimiento(null);
        setFechaNacimientoString("");
        setCalle("");
        setNoExterior("");
        setNoInterior("");
        setPrimaria(false);
        setColonia("");
        setCiudad("");
        setCodigoPostal("");
        setDelegacion("");
        setEstado("");
        setPais(null);
        setNoCuenta("");
        setNoCuentaContratoSica("");
        setHashmapMunicipio(null);
        setHashmapColonia(null);
        setHashmapEntidad(null);
        setNoSic("");
        setCuentaCheques("");
        setSucursalOpera("");
        setSucursalOrigen("");
        setIdPersonaCr(null);
        setIdClienteCr(new Integer(-1));
        setNombreClienteCr(null);
        setActualizarCr(false);
        setErrorFaltaSic(false);
		setErrorFaltaNumCuenta(false);
		setErrorFaltaSucOpera(false);
		
		setVerCamposRegulatorios(false);
		setSignosNoValidos(false);
		
        setClaveBanxico("");
	    setContraparte("");
	    setTipoSociedadMercantil(null);
	    setTipoIdentificador(null);
	    setClaveIdentificador("");
	    setTipoContraparte(null);
	    setClaveLei("");
	    setSectorIndustrial(null);
	    setPaisControlContraparte(null);
	    setPaisContraparte(null);
	    setClaveLeiMatrizDirecta("");
	    setClaveLeiMatrizUltima("");
	    setFechaContraparte(null);
	    setTipoRelacion(null);
	    setGrupoFinanciero(new Integer(3));
	    setTipoEventoRelacion(null);
	    setFechaEventoRelacion(new Date());
	    
	    setApoderadoNombre("");
	    setApoderadoPaterno("");
	    setApoderadoMaterno("");
	    
	    setCurp("");
	    setNumComprobante("");
	    setTipoComprobante(null);
	    setNacionalidadMexicana(false);
	    setPaisNacimiento(null);
	    setFlujoAsignarContrato(false);
	    setSexo("");
	    setIdExtranjero("");
	    setIdPersona(null);
	    //60057-CFDI 3.3
        setNit("");

	}
	
	public void limpiarCampos2() {
	    setPaterno("");
        setMaterno("");
        setNombre("");
        setNacionalidad(null);
        setRazonSocial("");
        setRfc("");
        setSectorBanxico(null);
        setFechaNacimiento(null);
        setFechaNacimientoString("");
        setCalle("");
        setNoExterior("");
        setNoInterior("");
        setPrimaria(false);
        setColonia("");
        setCiudad("");
        setCodigoPostal("");
        setDelegacion("");
        setEstado("");
        setPais(null);
        setHashmapMunicipio(null);
        setHashmapColonia(null);
        setHashmapEntidad(null);
        setNoSic("");
        setCuentaCheques("");
        setSucursalOpera("");
        setSucursalOrigen("");
        setIdPersonaCr(null);
        setIdClienteCr(new Integer(-1));
        setNombreClienteCr("");
        setActualizarCr(false);
        setErrorFaltaSic(false);
		setErrorFaltaNumCuenta(false);
		setErrorFaltaSucOpera(false);
		setVerCamposRegulatorios(false);
		setSignosNoValidos(false);
		
		//setVerCamposRegulatorios(false);
        setClaveBanxico("");
	    setContraparte("");
	    setTipoSociedadMercantil(null);
	    setTipoIdentificador(null);
	    setClaveIdentificador("");
	    setTipoContraparte(null);
	    setClaveLei("");
	    setSectorIndustrial(null);
	    setPaisControlContraparte(null);
	    setPaisContraparte(null);
	    setClaveLeiMatrizDirecta("");
	    setClaveLeiMatrizUltima("");
	    setFechaContraparte(null);
	    setTipoRelacion(null);
	    setGrupoFinanciero(new Integer(3));
	    setTipoEventoRelacion(null);
	    setFechaEventoRelacion(new Date());
	    
	    setApoderadoNombre("");
	    setApoderadoPaterno("");
	    setApoderadoMaterno("");
	    setCurp("");
	    setNumComprobante("");
	    setTipoComprobante(null);
	    setNacionalidadMexicana(false);
	    setPaisNacimiento(null);
	    setFlujoAsignarContrato(false);
	    setSexo("");
	    setIdExtranjero("");
	    //60057-CFDI 3.3
        setNit("");

	}

    /**
     * Llama a <code>limpiarCampos()</code>.
     *
     * @param cycle El Ciclo de la P&aacute;gina.
     */
    public void cambiarTipoPersona(IRequestCycle cycle) {
        limpiarCampos();
        setAlta(false);
        setPersonasBUP(new ArrayList());
    }

    /**
     * Verifica la existencia de un Cliente especificado por el usuario en la BUP antes de agregarlo
     * como nuevo.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void agregar(IRequestCycle cycle) throws ContratacionException {
    	debug("Ejecutar metodo agregar con modo submit: " + getModoSubmit());
        setLevel(0);
        if (getEmpresa() != null) {
        	setNombreEmpresa((String)getEmpresa().get("value"));
        }
        else {
        	setNombreEmpresa("");
        }
        
        if (!isFlujoAsignarContrato()) {//Para regresar con el listado de personas.
        	setPersonasBUP(new ArrayList());
        }
        convertirAUpperCase();
        if (cycle.getServiceParameters().length > 1) {
        	debug("Parametro recibido: " + (cycle.getServiceParameters()[0]).toString());
        	setModoSubmit(new Integer((cycle.getServiceParameters()[0]).toString()).intValue());
        }
        _logger.debug("Valor en modo submit: " + getModoSubmit());
        if (!verificaPais(cycle)) {
        	switch(getModoSubmit()) {
	            /* Método buscar cliente */
	        	case 1:
	        		buscarCliente(cycle);
	        		break;
	        	/* Guardar nuevo cliente */
	        	case 3:
	        		agregarNuevoCliente(cycle);
	        		break;
	        	/* Método buscar codigo postal */
	        	case 4:
	        		direccionCP(getCodigoPostal());
	        		break;
	        	case 6:
	        		buscarCR(cycle);
	        		break;
	        	default:
	        		debug("Opcion default");
	        		setModoSubmit(5);
	        		break;
        	}
        }
            
        return;
    }
    
    /**
     * Funci\u00F3n que indica si el usuario ha seleccionado un nuevo pa\u00EDs de residencia
     * del cliente
     * @param  cyle      Ciclo de la p\u00E1gina
     * @return boolean   True o False, dependiendo si el usuario modific\u00F3 el pa\u00EDs
     * de residencia
     */
    public boolean verificaPais(IRequestCycle cyle) {
    	if (getModoSubmit() == 0) {
    		setCodigoPostal("");
    		setColonia("");
    		setDelegacion("");
    		setEstado("");
    		setHashmapColonia(null);
    		setHashmapMunicipio(null);
    		setHashmapEntidad(null);
    		setModoSubmit(5);
    		//60057-CFDI 3.3
            setNit("");
    		cambiaNacionalidad();
    		return true;
    	}
    	return false;
    }
    
    /**
     * @param cycle                    Ciclo de la P\u00E1gina
     * @throws ContratacionException   Excepci\u00F3n lanzada por el m\u00F3dulo de
     * Contrataci\u00F3n
     */
    public void agregarNuevoCliente(IRequestCycle cycle) 
    		throws ContratacionException {
    	debug("Metodo agregarNuevoCliente()");
    	String msjError = "";
    	if(getEmpresa().get(VALUE).equals(EmpresaCambios.ID_EMPRESA_BANORTE)){
        	if(getNoSic()==null||getSucursalOrigen()==null ||getSucursalOpera()==null||getCuentaCheques()==null)
        	{
        		msjError = "Cada campo del cliente Banorte solo debe contener d\u00EDgitos. ";
        	}
        	else
        	{
        		if(getNoSic().trim().length()==0||getSucursalOrigen().trim().length()==0||getSucursalOpera().trim().length()==0||
        				getCuentaCheques().trim().length()==0)
	        	{
        			msjError = "Cada campo del cliente Banorte debe contener al menos un d\u00EDgito. ";
	        	}
        	}
    	}
    	else{
    		setSucursalOpera("");
    		setSucursalOrigen("");
    		setNoSic("");
    		setCuentaCheques("");
    	}
        
    	if (!verificaCampos() || !verificaCodigoPostal()) {
    		msjError += "Verifique que los campos marcados se compongan de al menos 3 caracteres. ";	
    	}
    	if (!validarCaracteresCampos()){
    		msjError += "Verifique que los campos marcados no contengan caracteres especiales. ";
    		if(getEmpresa().get(VALUE).equals(EmpresaCambios.ID_EMPRESA_BANORTE))
    			msjError += "Cada campo del cliente Banorte solo debe contener d\u00EDgitos. ";
    	}
    	if (getNacionalidad() == null || 
    			getNacionalidad().getId().equals(String.valueOf(ID_DEFAULT))){
			msjError += "Debe seleccionar un valor para Nacionalidad. ";
		}
    	else {
    		if (isNacionalidadMexicana()) {
    			String msjRFC = Utilerias.validaRFC(getRfc(), "FISICA".equals(getFisicaMoral()) ? 
    					Constantes.ID_TIPO_PERSONA_FISICA : Constantes.ID_TIPO_PERSONA_MORAL);
        		
        		if(msjRFC.length() > 0) {
        			msjError += msjRFC.charAt(0) + msjRFC.substring(1).toLowerCase().
        			replaceAll("rfc", "RFC") + ". ";
        			setRfc("");
        			
        			if ("MORAL".equals(getFisicaMoral())) {
        				if (msjError.indexOf("El RFC proporcionado es inv") != -1){
        					msjError =  msjError.replace('5', '4');
        					msjError = msjError.replaceAll("10", "9");
        				}
        			}
        		}
    		}
    		if ("FISICA".equals(getFisicaMoral())) {
    			if (getPaisNacimiento() == null || 
    					getPaisNacimiento().get(VALUE).equals(String.valueOf(ID_DEFAULT))) {
    				msjError += "Debe seleccionar un valor para Pa\u00EDs de nacimiento. ";
    			}
    			if (getTipoComprobante() == null || 
    					getTipoComprobante().get(VALUE).equals(String.valueOf(ID_DEFAULT))) {
    				msjError += "Debe seleccionar un valor para Tipo Identificaci\u00f3n. ";
    			}
    			if (StringUtils.isEmpty(getSexo())) {
    				msjError += "Debe seleccionar un valor para Sexo.";
    			}
    		}
    	}
    	if("SELECCIONE".equals(getSectorBanxico().get(VALUE).toString())){
			msjError += "Debe seleccionar un valor para Sector econ\u00f3mico. ";
		}
    	try {
    		SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");        
            setFechaNacimientoString(format1.format(getFechaNacimiento()));
            if (!isValidDate(getFechaNacimiento())) {
            	msjError += "La fecha de " + ("FISICA".equals(getFisicaMoral()) ? "nacimiento" :
            		"constituci\u00f3n") 
            		+  " no debe ser mayor a la fecha Actual. ";
            }
    	}
    	catch (Exception e) {
    		msjError += "La fecha de " + ("FISICA".equals(getFisicaMoral()) ? "nacimiento" :
    			"constituci\u00f3n") 
    			+ " debe tener formato 'dd-MM-yyyy'. ";
    	}
    	
    	if("MORAL".equals(getFisicaMoral()))
    	{
    		if(StringUtils.isEmpty(getApoderadoNombre()) || "".equals(getApoderadoNombre().trim()))
        		msjError += " El nombre del apoderado legal no debe estar en blanco.";

        	if(StringUtils.isEmpty(getApoderadoPaterno()) || "".equals(getApoderadoPaterno().trim()))
        		msjError += " El apellido paterno del apoderado legal no debe estar en blanco.";
        	
        	/*if(StringUtils.isEmpty(getApoderadoMaterno()) || "".equals(getApoderadoMaterno().trim()))
        		msjError += " El apellido materno del apoderado legal no debe estar en blanco.";*/
    	}
    	
    	//60057-CFDI 3.3
    	String nit = getNit();
    	if (getPais() != null && ("CANADÁ".equalsIgnoreCase(getPais().getDescripcion()) || "ESTADOS UNIDOS".equalsIgnoreCase(getPais().getDescripcion()) ) ) {
    		Pattern pattern = Pattern.compile("\\d{9}");
    	    if (!pattern.matcher(nit).matches()) {
    	    	msjError += " Para direcciones cuyo pa\u00EDs sea Canad\u00E1 o Estados Unidos es necesario se proporcione el campo NIT con formato de 9 d\u00EDgitos.";
    	    }
    	} else if(getPais() != null && (!"MEXICO".equalsIgnoreCase(getPais().getDescripcion()) && !"MÉXICO".equalsIgnoreCase(getPais().getDescripcion()) ) ) {
    		if (nit != null && !"".equals(nit.trim()) && !nit.matches("[A-Za-z0-9]*")) {
    			msjError += "El campo NIT no debe tener caracteres inv\u00E1lidos. ";
    		}
    	}
    	//************************************************************************************
    	// Comienza validacion de campos regulatorios
    	//************************************************************************************
    	List camposEnBlanco = new ArrayList();
    	List camposDefault = new ArrayList(); 
    	StringBuffer notas = new StringBuffer();
    	notas.append("");
    	
    	if(isVerCamposRegulatorios())
    	{
    		if(StringUtils.isEmpty(getContraparte()))
        	{
    			camposEnBlanco.add("'Contraparte'");
    			notas.append("El campo 'Contraparte' no debe contener la informaci\u00f3n de la sociedad mercantil. ");
        	}
        	else
        	{
        		setSignosNoValidos(false);
        		Pattern patron = Pattern.compile("\\p{Punct}"); // se valida !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
        		Matcher matcher = patron.matcher(getContraparte());
        		if(matcher.find())
        		{
        			notas.append("El campo 'Contraparte' no debe contener signos de puntuaci\u00f3n o abreviaciones. ");
        			setSignosNoValidos(true);
        		}
        	}
        	
        	if(getTipoSociedadMercantil() == null || getTipoSociedadMercantil().getDescripcionCorta() == null ||
        	   "SELECCIONE LA OPCION DESEADA".equals(getTipoSociedadMercantil().getDescripcionCorta()))
        	{
        		camposDefault.add("'Tipo de Sociedad Mercantil'");
        	}
        	
        	if(getTipoIdentificador() == null || getTipoIdentificador().getDescripcion() == null ||
        	   "SELECCIONE LA OPCION DESEADA".equals(getTipoIdentificador().getDescripcion()))
        	{
        		camposDefault.add("'Tipo identificador de la contraparte'");;
        	}
        	
        	if(StringUtils.isEmpty(getClaveIdentificador()))
        	{
        		camposEnBlanco.add("'Clave de identificador de la contraparte'");
        	}
        	else if(getTipoIdentificador() != null && getTipoIdentificador().getId() != null && 
        			getFisicaMoral().equals("MORAL") && getTipoIdentificador().getId().intValue() == 1 && // 1:RFC
        			(getClaveIdentificador().length() != 9 && getClaveIdentificador().length() != 12)) 
        	{
        		camposEnBlanco.add("'Clave de identificador de la contraparte'");
        		notas.append(" El campo 'Clave de identificador de la contraparte' con opci\u00f3n de RFC " +
        				     "debe tener una longitud de 12 posiciones con homoclave o 9 posiciones sin homoclave para Personas Morales.");
        	}
        	
        	if(getTipoContraparte() == null || getTipoContraparte().getDescripcion() == null ||
        	   "SELECCIONE LA OPCION DESEADA".equals(getTipoContraparte().getDescripcion()))
        	{
        		camposDefault.add("'Tipo contraparte'");
        	}
        	
        	if(StringUtils.isEmpty(getClaveLei()))
        		camposEnBlanco.add("'Clave LEI (Legal Entity Identifier)' -- Capture ND para indicar que no se encuentra disponible");
        	
        	if(getSectorIndustrial() == null || getSectorIndustrial().getDescripcion() == null ||
        	   "SELECCIONE LA OPCION DESEADA".equals(getSectorIndustrial().getDescripcion()))
        	{
        		camposDefault.add("'Sector Industrial (SCIAN)'");
        	}
        	
        	if(getPaisControlContraparte() == null || getPaisControlContraparte().getNombrePais() == null ||
        	   "SELECCIONE LA OPCION DESEADA".equals(getPaisControlContraparte().getNombrePais()))
        	{
        		camposDefault.add("'Pa\u00EDs de residencia de quien tiene el control de la contraparte'");
        	}
        	
        	if(getPaisContraparte() == null || getPaisContraparte().getNombrePais() == null ||
        	   "SELECCIONE LA OPCION DESEADA".equals(getPaisContraparte().getNombrePais()))
        	{
        		camposDefault.add("'Pa\u00EDs de residencia de la contraparte'"); 
        	}
        	
        	if(StringUtils.isEmpty(getClaveLeiMatrizDirecta()))
        		camposEnBlanco.add("'Clave LEI de la matriz directa' -- Capture NA si no se tiene la clave LEI de la matriz directa"); 
        	
        	if(StringUtils.isEmpty(getClaveLeiMatrizUltima()))
        		camposEnBlanco.add("'Clave LEI de la matriz en \u00FAltima instancia de la contraparte' -- " +
        					       " Capture NA si no se tiene disponible esta clave");
        	
        	if(getFechaContraparte() == null)
        		camposEnBlanco.add("'Fecha de alta o modificaci\u00f3n de la contraparte'");
        	else
        	{
        		if(getFechaContraparte().after(new Date()))
        			camposEnBlanco.add(" El campo 'Fecha de alta o modificaci\u00f3n de la contraparte' " +
        						       "no debe ser mayor al d\u00EDa en curso.");
        	}
        	
        	if(getTipoRelacion() == null || getTipoRelacion().getDescripcion() == null || getTipoRelacion().getId() == null ||
        	   "SELECCIONE LA OPCION DESEADA".equals(getTipoRelacion().getDescripcion()))
        	{
        		camposDefault.add("'Tipo de relaci\u00f3n'");
        	}
        	
        	if(getTipoRelacion() != null && getTipoRelacion().getId() != null && getTipoRelacion().getId().intValue() != 10) // la contraparte si está relacionada con la institucion  -- 10:no hay relación
        	{// getTipoRelacion() != null && getTipoRelacion().getId().intValue() == 10
        		if(getGrupoFinanciero() == null || getGrupoFinanciero().intValue() == 3) // 3:no se ha seleccionado un radion button
        			notas.append(" El campo '¿Pertenece al Grupo Financiero?' debe tener seleccionada una opci\u00f3n v\u00E1lida.");
        		
        		if(getTipoEventoRelacion() == null || getTipoEventoRelacion().getDescripcion() == null ||
        		   "SELECCIONE LA OPCION DESEADA".equals(getTipoEventoRelacion().getDescripcion()))
        			camposDefault.add("'Tipo de evento en la realci\u00f3n'");
        		
        		if(getFechaEventoRelacion() == null)
        			camposEnBlanco.add("'Fecha del evento de la relaci\u00f3n'");
            	else
            	{
            		if(getFechaEventoRelacion().after(new Date()))
            			notas.append(" El campo 'Fecha del evento de la relaci\u00f3n' " +
            						"no debe ser mayor al d\u00EDa en curso.");
            	}
        	}
        	
        	int indice = 0;
        	
        	if(camposDefault.size() > 0)
        	{
        		msjError += " Los siguientes campos deben tener seleccionada una opci\u00f3n diferente" +
        			        " a la proporcionada por default: " + camposDefault.get(0);
        		for(indice = 1; indice < camposDefault.size(); indice++)
        			msjError += ", " + camposDefault.get(indice);
        		msjError += ". ";
        	}
        	if(camposEnBlanco.size() > 0)
        	{
        		msjError += " Los siguientes campos no deben estar en blanco: " + camposEnBlanco.get(0);
        		for(indice = 1; indice < camposEnBlanco.size(); indice++)
        			msjError += ", " + camposEnBlanco.get(indice);
        		msjError += ". ";
        	}
        	if(notas.length() > 0)
        	{
        		msjError += notas.toString();
        	}
    	}
    	//************************************************************************************
    	// Fin de validacion de campos regulatorios
    	//************************************************************************************
    	
    	if (!msjError.equals("")) {
    		getDelegate().record(msjError, null);
            return;
        }
    	
    	List personasBUPTmp = null;
    	if(getNacionalidad().getId().equals(String.valueOf(Num.I_48))){
    	personasBUPTmp = getBupServiceData().findPersons("", "",
                "", "", "", "FISICA".equals(getFisicaMoral()) ? 
                		getRfc().substring(0, 10) : getRfc(),
                		"", "", MAX_RESULTS);
    	}
    	
        if (personasBUPTmp != null && !personasBUPTmp.isEmpty()) {
        	boolean rfcExistente = false;
        	if (getIdPersona() != null) {//Persona existente en la BD
        		debug("getIdPersona(): " + getIdPersona());
        		boolean encontrado = false;
        		for (Iterator it = personasBUPTmp.iterator(); it.hasNext();) {
	        		com.ixe.ods.bup.model.Persona personaBUP = 
							(com.ixe.ods.bup.model.Persona) it.next();
	        		if (personaBUP.getIdPersona().equals(getIdPersona())) {
	        			encontrado = true;
	        			break;
	        		}
        		}
        		rfcExistente = !encontrado;
        	}
        	else {
        		 rfcExistente = true;
        	}
        	if (rfcExistente) {
	        	getDelegate().record("No es posible guardar el Cliente, ya se ha almacenado un"
										+ " cliente con el mismo RFC y/o nombre completo. "
									    + " Favor de verificar la"
										+ " informaci\u00f3n ingresada.", null);
                return;
        	}
        	else {
        		nuevoCliente(cycle);
        	}
        }
        else {
        	nuevoCliente(cycle);
        }
    }
    
    public void actualizarCR(IRequestCycle cycle)
    {
    	//Persona persona = getSicaBusquedaPersonaService().buscaPersonaPorId(idPersona.intValue());
    	//ContratoSica contratoSICA = getSicaServiceData().findContratoSicaByIdPersona(new Integer(persona.getIdPersona()));
    	//Contrato contratoCorto = getSicaServiceData().getContratoCortoXCuenta(contratoSICA.getNoCuenta());
    	//Usuario empleado = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().getIdPersona().intValue());
    	
    	String msj = "", nombre = "";
    	List clientes = null;
    	Cliente cliente = null;
    	Visit visit = null;
    	InformacionCuentaAltamiraService service = null;
    	InfoCuentaAltamiraDto dto = null;
    	
    	nombre = getNombreClienteCr();
    	visit = (Visit) getVisit();
    	
    	if(getIdPersonaCr() == null)
    		msj += "No es posible actualizar el registro del cliente, no se encuentra su identificador.";
    	
    	//clientes = getSicaServiceData().findClienteByIdPersona(getIdPersonaCr());
    	clientes = getSicaServiceData().findClienteByIdPersonaAndIdCliente(getIdPersonaCr(), getIdClienteCr());
    	if(clientes == null || clientes.isEmpty())
    		msj += " No es posible actualizar el registro del cliente, no se encuentra registrado en el sistema.";
    	
    	msj += validarDatosCR(getNoSic(), getCuentaCheques(), getSucursalOpera());
    	
    	if(StringUtils.isEmpty(msj))
    	{
    		try
        	{
        		service = getInformacionCuentaAltamiraService();
        		dto = service.consultaInformacionCuenta(getCuentaCheques(), visit.getTicket());
        		
        		msj = validarDatosAltamiraCR(dto, getNoSic());
        		if(StringUtils.isNotEmpty(msj))
        			setSucursalOrigen("");	
        	}
        	catch(SicaAltamiraException exception)
        	{
        		exception.printStackTrace();
        		msj = exception.getMessage();
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        		msj = " Ha ocurrido un error al realizar la consulta de la sucursal de origen o CR. " + e.getMessage();
        	}
    	}

    	if(StringUtils.isEmpty(getSucursalOrigen()))
    		msj += " No es posible actualizar los datos sin una sucursal de origen v\u00e1lida.";

    	if(msj.length() > 0)
    	{
    		if(getIdPersonaCr() != null)
        		buscarApoderadoDatosRegulatorios(getIdPersonaCr());
    		setLevel(0); // indicamos que son warnings
    		getDelegate().record(msj, null);
    		return;
    	}
    	
    	cliente = (Cliente)clientes.get(0);
    	_logger.debug("--> actualizarCR -- idCliente: " + cliente.getIdCliente() + 
		           		   ", idPersona: " + cliente.getIdPersona() + ", idEmpresa: " + cliente.getIdEmpresa());
    	
		cliente.setCuentaCheques(getCuentaCheques());
		cliente.setSic(getNoSic());
		cliente.setSucursalOperacion(getSucursalOpera());
		cliente.setSucursalOrigen(getSucursalOrigen());
		cliente.setFechaUltMod(new Date());
		cliente.setUsuarioUltMod(visit.getUser().getIdPersona());
		
		//cliente.setIdEmpresa(EmpresaCambios.ID_EMPRESA_BANORTE);
		
		getSicaServiceData().update(cliente);
		
		msj = "Se han actualizado los datos del cliente " + nombre + 
		      " con la siguiente informaci\u00f3n, cuenta de cheques: " + getCuentaCheques() +
		      ", SIC: " + getNoSic() + ", sucursal donde opera: " + getSucursalOpera() +
		      " y sucursal origen: " + getSucursalOrigen();
		
		limpiarCampos();
		setAlta(false);
		setFisicaMoral("");
		setNombreEmpresa("IXE");
		setActualizarCr(false);
		setIdPersonaCr(null);
		setIdClienteCr(new Integer(-1));
		setNombreClienteCr("");
		setModoSubmit(0);
		setLevel(1);
		//getDelegate().record(msj, null);
		cycle.setServiceParameters(new Object[]{"0"});
		Mensaje nextPage = (Mensaje) getRequestCycle().getPage("Mensaje");
        nextPage.setMensaje(msj);
        nextPage.setTipo(Mensaje.TIPO_AVISO);
        getRequestCycle().activate(nextPage);
		
		//return;
		
		// Tapestry.PAGE_SERVICE : new String[]{"Home"}, con HOME_SERVICE los parametros son null
		/*throw new RedirectException(((SupportEngine)cycle.getEngine()).
				getService(Tapestry.HOME_SERVICE).getLink(cycle, cycle.getPage("Home"), 
						null).getURL());*/
		
       //((SupportEngine) getEngine()).
		//cycle.commitPageChanges();
		//cycle.activate(cycle.getPage("AltaRapidaDeClientes").getEngine().);
		//activate(cycle);
    }
    
    public void mostrarCamposCR(IRequestCycle cycle)
    {
    	String msj = "";
    	Cliente cliente = null;
    	
    	_logger.debug(" --> mostrarCamposCR, getModoSubmit(): " + getModoSubmit());
    	_logger.debug(" --> mostrarCamposCR, IdPersona: " + (cycle.getServiceParameters()[0]).toString());
    	_logger.debug(" --> mostrarCamposCR, nombreCliente: " + (cycle.getServiceParameters()[1]).toString());
    	_logger.debug(" --> mostrarCamposCR, contratoCortoIdCliente: " + (cycle.getServiceParameters()[2]).toString());
    	
    	Integer idPersona = new Integer((cycle.getServiceParameters()[0]).toString());
    	String nombreCliente = (cycle.getServiceParameters()[1]).toString();
    	Integer idCliente = (Integer)cycle.getServiceParameters()[2];
    	
    	//List clientes = getSicaServiceData().findClienteByIdPersona(idPersona);
    	List clientes = getSicaServiceData().findClienteByIdPersonaAndIdCliente(idPersona, idCliente);
    	_logger.debug(" --> mostrarCamposCR, lista Clientes: " + clientes + ", es vacio: " + (clientes != null ? clientes.isEmpty() + "" : "null"));
    	if(clientes == null || clientes.isEmpty())
    	{
    		msj = "No se encontr\u00f3 el registro cliente (" + nombreCliente + 
    		      ") para actualizar los datos de la sucursal de origen.";
    		setLevel(-1); // errores
    		getDelegate().record(msj, null);
    		setIdPersonaCr(null);
    		setIdClienteCr(new Integer(-1));
    		setNombreClienteCr("");
    		setActualizarCr(false);
    		return;
    	}
    	else
    	{
    		setPersonasBUP(new ArrayList());
    		setIdPersonaCr(idPersona);
    		setIdClienteCr(idCliente);
    		setNombreClienteCr(nombreCliente);
    		setActualizarCr(true);
    		setModoSubmit(5);
    		
    		//setNombreEmpresa("BNTE");
    		
    		//com.ixe.ods.bup.model.Persona personaBUP = null;
    		Vector nacionalidadTmp = getSicaConsultaCatalogosService().obtenCatNacionalidad();
    		
    		//if(cycle.getServiceParameters()[2] != null)
    		//	setRfc((cycle.getServiceParameters()[2]).toString());
    		
    		cliente = (Cliente)clientes.get(0);
    		
    		Map empresaCliente = null;
    		EmpresaCambios empresaGrupoFinanciero = null;
    		_logger.debug(" --> mostrarCamposCR, cliente.getIdEmpresa(): " + cliente.getIdEmpresa());
    		if(!StringUtils.isEmpty(cliente.getIdEmpresa()))
    		{
    			List empresasList = getSicaServiceData().findAllEmpresasByOrdenAlfabetico();
        		for (Iterator i = empresasList.iterator(); i.hasNext();) 
        		{
        			empresaGrupoFinanciero = (EmpresaCambios) i.next();
        	    	if(empresaGrupoFinanciero.getIdEmpresa().equals(cliente.getIdEmpresa()))
        	    	{
	        	    	empresaCliente = new HashMap();
	        	    	empresaCliente.put(VALUE, empresaGrupoFinanciero.getIdEmpresa());
	        	    	empresaCliente.put(LABEL, empresaGrupoFinanciero.getDescripcion());
	        	    	setEmpresa(empresaCliente);
	        	    	setNombreEmpresa(empresaGrupoFinanciero.getIdEmpresa());
	        	    	_logger.debug(" --> mostrarCamposCR, asignar datos de empresa: " + empresaGrupoFinanciero.getDescripcion());
	        	    	_logger.debug(" --> mostrarCamposCR, setNombreEmpresa(): " + empresaGrupoFinanciero.getIdEmpresa());
	        	    	break;
        	    	}
        	    }
    		}
    		
    		_logger.debug("--> mostrarCamposCR -- idCliente: " + cliente.getIdCliente() + 
    				           ", idPersona: " + cliente.getIdPersona() + ", idEmpresa: " + cliente.getIdEmpresa());
    		if(cliente.getCuentaCheques() != null)
    			setCuentaCheques(cliente.getCuentaCheques());
    		if(cliente.getSic() != null)
    			setNoSic(cliente.getSic());
    		if(cliente.getSucursalOrigen() != null)
    			setSucursalOrigen(cliente.getSucursalOrigen());
    		if(cliente.getSucursalOperacion() != null)
    			setSucursalOpera(cliente.getSucursalOperacion());
    		
    		//60057-CFDI 3.3
        	NitPersona nitPersona = getSicaServiceData().findNitByIdPersona(idPersona);
        	if(nitPersona != null && nitPersona.getNit() != null) {
    			setNit(nitPersona.getNit());
        	}

    		///////////////////////////////////////////////////////////////////////////////////////////////////////
    		_logger.debug("El tipo de pesersona seleccionada es, getFisicaMoral(): " + getFisicaMoral());
			if(getFisicaMoral().equals("MORAL"))
			{
				com.ixe.ods.bup.model.PersonaMoral pm = (com.ixe.ods.bup.model.PersonaMoral)getSicaServiceData().
				                                        findPersonaMoralByIdPersona(idPersona);
				_logger.debug("pm: " + pm);
				if(pm != null)
				{
					_logger.debug("pm.getRazonSocial(): " + pm.getRazonSocial());
					setRazonSocial(pm.getRazonSocial());
					
					_logger.debug("pm.getSectorEconomico: " + pm.getSectorEconomico());
					if(pm.getSectorEconomico() != null)
					{
						Map sectorMap = new HashMap();
						sectorMap.put(VALUE, pm.getSectorEconomico().getIdSector().toString());
					    sectorMap.put(LABEL, pm.getSectorEconomico().getDescripcion());
						setSectorBanxico(sectorMap);
						_logger.debug("pm.getSectorEconomico().getIdSector(): " + pm.getSectorEconomico().getIdSector());
						_logger.debug("pm.getSectorEconomico().getDescripcion(): " + pm.getSectorEconomico().getDescripcion());
					}
					
					_logger.debug("pm.getNacionalidad(): " + pm.getNacionalidad());
					if(pm.getNacionalidad() != null)
					{
						for (Iterator it = nacionalidadTmp.iterator(); it.hasNext(); ) 
						{
        			        Catalogo sector = (Catalogo) it.next();
        			        if (sector.getDescripcion().equals(pm.getNacionalidad())) 
        			        {
        			        	_logger.debug("Se encontro la nacionalidad (sector): " + sector.getDescripcion() + ", id: " + sector.getId());
        			        	setNacionalidad(sector);
        			        	break;
        			        }
        			    }
					}
					
					_logger.debug("pm.getRfc(): " + pm.getRfc());
					if(pm.getRfc() != null)
						setRfc(pm.getRfc());
					
					_logger.debug("pm.getFechaConsitucion(): " + pm.getFechaConsitucion());
					if(pm.getFechaConsitucion() != null)
						setFechaNacimiento(pm.getFechaConsitucion());
				}
				
				buscarApoderadoDatosRegulatorios(idPersona);
			}
			else
			{
				com.ixe.ods.bup.model.PersonaFisica pf = (com.ixe.ods.bup.model.PersonaFisica)getSicaServiceData().
				                                          findPersonaFisicaByIdPersona(idPersona);
				_logger.debug("pf: " + pf);
				if(pf != null)
				{
					_logger.debug("pf.getPaterno(): " + pf.getPaterno() + ", pf.getMaterno(): " + pf.getMaterno() + 
							           ", pf.getNombre(): " + pf.getNombre());
					setPaterno(pf.getPaterno());
					setMaterno(pf.getMaterno());
					setNombre(pf.getNombre());
					_logger.debug("pf.getFechaNacimiento(): " + pf.getFechaNacimiento() + ", pf.getRfc(): " + pf.getRfc());
					setFechaNacimiento(pf.getFechaNacimiento());
					if(pf.getRfc() != null)
						setRfc(pf.getRfc());
					_logger.debug("pf.getSectorEconomico: " + pf.getSectorEconomico());
					if(pf.getSectorEconomico() != null)
					{
						Map sectorMap = new HashMap();
						sectorMap.put(VALUE, pf.getSectorEconomico().getIdSector().toString());
					    sectorMap.put(LABEL, pf.getSectorEconomico().getDescripcion());
						setSectorBanxico(sectorMap);
						_logger.debug("pf.getSectorEconomico().getIdSector(): " + pf.getSectorEconomico().getIdSector());
						_logger.debug("pf.getSectorEconomico().getDescripcion(): " + pf.getSectorEconomico().getDescripcion());
					}
					_logger.debug("pf.getNacionalidad(): " + pf.getNacionalidad());
    				if(pf.getNacionalidad() != null)
    				{
    					for (Iterator it = nacionalidadTmp.iterator(); it.hasNext(); ) {
        			        Catalogo sector = (Catalogo) it.next();
        			        if (sector.getDescripcion().equals(pf.getNacionalidad())) 
        			        {
        			        	_logger.debug("Se encontro la nacionalidad (sector): " + sector.getDescripcion() + ", id: " + sector.getId());
        			        	setNacionalidad(sector);
        			        	break;
        			        }
        			    }
    				}
				}
			}
    	}
    	return;
    }
    
    /**
     * Cambia nacionalidad.
     */
    public void cambiaNacionalidad() {
    	String idPais = getNacionalidad() != null ? getNacionalidad().getId() : "";
    	debug("IdPais : " + idPais);
    	if (StringUtils.isNotEmpty(idPais) && 
    			!idPais.equals(String.valueOf(ID_DEFAULT))) {
    		if (Constantes.ID_PAIS_MEXICO.equals(idPais)) {
    			setNacionalidadMexicana(true);
    		}
    		else {
    			setNacionalidadMexicana(false);
    		}
    	}	    
    	_logger.debug("NacionalidadMexicana: " + isNacionalidadMexicana());
    }
    
    private void buscarApoderadoDatosRegulatorios(Integer idPersona)
    {
    	//*********** Inicia búsqueda de los datos del apoderado legal e información regulatoria ************
		try
		{
			RelacionPersonas relacion = null;
			ArrayList relaciones = null;
			int idInstanciaRelacionAnterior = -1000;
			relaciones = (ArrayList)getSicaConsultaRelacionContratacionService().
								obtenRelaciones(idPersona.intValue(), Constantes.ID_RES_ARRAY_LIST);
			if(relaciones != null && relaciones.size() > 0)
			{
				for(int indice = 0; indice < relaciones.size(); indice++)
				{
					relacion = (RelacionPersonas)relaciones.get(indice);
					// 6:relacion de cliente y apoderado
					if("6".equals(relacion.getIdTipoRelacion()) && Constantes.STATUS_RELACION_VIGENTE.equals(relacion.getStatus()))
					{
						if(relacion.getIdInstanciaRelacion() > idInstanciaRelacionAnterior)
						{
							idInstanciaRelacionAnterior = relacion.getIdInstanciaRelacion();
							com.ixe.ods.bup.model.PersonaFisica pf = getSicaServiceData().findPersonaFisicaByIdPersona(new Integer(relacion.getIdPersona2()));
							if(pf != null)
							{
								setApoderadoNombre(pf.getNombre());
								setApoderadoPaterno(pf.getPaterno());
								setApoderadoMaterno(pf.getMaterno());
							}
						}
					}
				}
			}
		}
		catch(ContratacionException ce)
    	{
    		warn(ce.getMessage(), ce);
	        getDelegate().record("Error al consultar al apoderado legal del cliente: " + ce.getMessage(), null);
	        return;
    	} 
		
		RegulatorioDatosPM datosPM = null;
		List datos = getSicaServiceData().findRegulatorioDatosPM(idPersona);
		if(datos != null && datos.size() > 0)
		{
			datosPM = (RegulatorioDatosPM)datos.get(0);
			
			if(datosPM.getPerfilMdd() != null)
			{
				if("S".equals(datosPM.getPerfilMdd().trim()))
				{
					setVerCamposRegulatorios(true);
				}
				else
				{
					setVerCamposRegulatorios(false); // no se marca el check box porque no está perfilado para operar 1MDD
				}
			}
			
			setClaveBanxico(datosPM.getClaveBanxico());
			setContraparte(datosPM.getNombreContraparte());
			setTipoSociedadMercantil(datosPM.getSociedadMercantil());
			setTipoIdentificador(datosPM.getTipoIdentificador());
			setClaveIdentificador(datosPM.getClaveIdentificadorContraparte());
			setTipoContraparte(datosPM.getActividadEconomica());
			setClaveLei(datosPM.getClaveLei());
			setSectorIndustrial(datosPM.getSectorIndustrial());
			setPaisControlContraparte(datosPM.getPaisControlContraparte());
			setPaisContraparte(datosPM.getPaisResidenciaContraparte());
			setClaveLeiMatrizDirecta(datosPM.getClaveLeiMatrizDirecta());
			setClaveLeiMatrizUltima(datosPM.getClaveLeiMatrizUltimaInstancia());
			setFechaContraparte(datosPM.getFechaContraparte());
			
			RegulatorioInstitucion institucion = null;
			datos = getSicaServiceData().findRegulatorioInstitucion(idPersona);
			if(datos != null && datos.size() > 0)
			{
				institucion = (RegulatorioInstitucion)datos.get(0);
				setTipoRelacion(institucion.getTipoRelacion());
				setGrupoFinanciero(institucion.getPerteneceGrupoFinanciero());
				if(institucion.getEventoRelacion() != null)
					setTipoEventoRelacion(institucion.getEventoRelacion());
				if(institucion.getFechaEvento() != null)
					setFechaEventoRelacion(institucion.getFechaEvento());
			}
		}
		else
		{
			setVerCamposRegulatorios(false);
		}
		//***************************************************************************************************
    }
    
    private String validarDatosCR(String SIC, String ctaCheques, String sucOpera)
    {
    	String msjError = "";
    	
    	setErrorFaltaSic(true);
    	if(StringUtils.isEmpty(SIC))
    		msjError += " Debe propocionar el n\u00FAmero SIC.";
    	else if(!SIC.matches("[0-9]*"))
    		msjError += " El n\u00FAmero de SIC debe contener solo d\u00EDgitos.";
    	else
    		setErrorFaltaSic(false);
    	
    	setErrorFaltaNumCuenta(true);
    	if(StringUtils.isEmpty(ctaCheques))
    		msjError += " Debe propocionar el n\u00FAmero de Cuenta.";
    	else if(!ctaCheques.matches("[0-9]*"))
    		msjError += " El n\u00FAmero de cuenta debe contener solo d\u00EDgitos.";
    	else
    		setErrorFaltaNumCuenta(false);
    	
    	setErrorFaltaSucOpera(true);
    	if(StringUtils.isEmpty(sucOpera))
    		msjError += " Debe propocionar el n\u00FAmero de sucursal donde opera el cliente.";
    	else if(!sucOpera.matches("[0-9]*"))
    		msjError += " El n\u00FAmero de sucursal donde opera el cliente debe contener solo d\u00EDgitos.";
    	else
    		setErrorFaltaSucOpera(false);
    	
    	return msjError;
    }
    
    private String validarDatosAltamiraCR(InfoCuentaAltamiraDto dto, String SIC)
    {
    	String msjError = "";
    	int sicLocal = -1, sicService = -1;
    	
    	_logger.debug("dto.getNumeroCliente(): " + dto.getNumeroCliente() + ", dto.getCr(): " + dto.getCr());
		if(StringUtils.isEmpty(dto.getNumeroCliente()))
			msjError += " No es posible verificar el n\u00FAmero de Cliente, el servicio de devolvi\u00f3 un dato err\u00f3neo.";
		else if(StringUtils.isEmpty(dto.getCr()))
			msjError += " El servicio de consulta de la sucursal origen devolvi\u00f3 un CR inv\u00e1lido o vac\u00EDo.";
		else if(!InformacionCuentaAltamiraService.STATUS_ACTIVA.equals(dto.getStatusCuenta()))
			msjError += " La cuenta proporcionada no est\u00e1 activa, estatus: " + dto.getStatusCuenta();
		
		if(StringUtils.isNotEmpty(dto.getNumeroCliente()))
		{
			try
			{
				sicLocal = Integer.parseInt(SIC);
				sicService = Integer.parseInt(dto.getNumeroCliente());
				if(sicLocal != sicService)
					msjError += " El n\u00FAmero de SIC proporcionado no coincide con el n\u00FAmero de cliente registrado en Altamira.";
			}
			catch(Exception e)
			{
				e.printStackTrace();
				msjError += " Verifique los datos del campo SIC, valor capturado: " + SIC + 
				            " -- valor devuelto por Altamira: " + dto.getNumeroCliente();
			} 
		}
		
		//if(!dto.getNumeroCliente().equals(SIC))
		//	msjError += " El n\u00FAmero de SIC proporcionado no coincide con el n\u00FAmero de cliente registrado en Altamira.";
    	
    	return msjError;
    }
    
    public void buscarCR(IRequestCycle cycle)
    {
    	debug("Metodo buscarCR()");
    	String msjError = "";
    	
    	Visit visit = (Visit) getVisit();
    	
    	InformacionCuentaAltamiraService service = null;
    	InfoCuentaAltamiraDto dto = null;

    	_logger.debug("getModoSubmit(): " + getModoSubmit() + ", getNoSic(): " + getNoSic() + ", getCuentaCheques(): " + getCuentaCheques());
    	//setModoSubmit(2);
    	//setNombreEmpresa("BNTE");
    	
    	if(getActualizarCr()) 
    		setModoSubmit(5);
		else
			setModoSubmit(3); // se encuentra en la opcion de agregar cliente, se setea a 3 para continuar con el flujo de agregar cliente
    	
    	if(getIdPersonaCr() != null)
    		buscarApoderadoDatosRegulatorios(getIdPersonaCr());
    	
    	msjError = validarDatosCR(getNoSic(), getCuentaCheques(), getSucursalOpera());
    	convertirAUpperCase();
    	if(msjError.length() > 0)
    	{
    		setLevel(0); // warning modoSubmit
    		setSucursalOrigen("");
    		getDelegate().record(msjError, null);
    		return;
    	}
    	else
    	{
    		try
        	{
        		service = getInformacionCuentaAltamiraService();
        		dto = service.consultaInformacionCuenta(getCuentaCheques(), visit.getTicket());
        		
        		msjError = validarDatosAltamiraCR(dto, getNoSic());
        		if(StringUtils.isEmpty(msjError))
        			setSucursalOrigen(dto.getCr());	
        	}
        	catch(SicaAltamiraException exception)
        	{
        		exception.printStackTrace();
        		msjError = exception.getMessage();
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        		msjError = " Ha ocurrido un error al realizar la consulta de la sucursal de origen o CR. " + e.getMessage();
        	}
        	
    		if(msjError.length() > 0)
    		{
    			setLevel(0); // errores
    			setSucursalOrigen("");
    			//System.out.println("--> buscarCR(), submit: " + (cycle.getServiceParameters()[0]).toString());
        		//setModoSubmit(Integer.parseInt((cycle.getServiceParameters()[0]).toString()));
        		getDelegate().record(msjError, null);
        		return;
    		}
    	}
    }
    
    /**
     * Funci\u00f3n que busca un cliente en Contrataci\u00f3n
     * @param cycle Ciclo de la P\u00E1gina
     */
    public void buscarCliente(IRequestCycle cycle) {
    	debug("Metodo buscarCliente()");
    	if (!verificaCampos()) {
    		getDelegate().record("Debe ingresar alg\u00FAn campo, cuya longitud sea " +
    				"de al menos 3 caracteres", null);
    		setModoSubmit(0);
                return;
            }
    	else if(!validarCaracteresCampos()){
    		getDelegate().record("Verifique que los campos no contengan caracteres especiales", null);
    		setModoSubmit(0);
    		return;
    	}
    	else {

    		    setAlta(true);
    			List personasBUPTmp = getBupServiceData().findPersons(getRazonSocial(), getNombre(),
    					getPaterno(), getMaterno(), "", getRfc(), "", getNoCuenta(), MAX_RESULTS);
    			if (personasBUPTmp != null && !personasBUPTmp.isEmpty()) {
    				String mensajeMaxResults = "";
    				if (personasBUPTmp.size() >= MAX_RESULTS) {
    					mensajeMaxResults = MENSAJE_MAX_RESULTS ;
    					MessageFormat formatter = new MessageFormat( mensajeMaxResults );
    					Object [] messageArguments = {""+ MAX_RESULTS };           		
    					mensajeMaxResults = formatter.format(messageArguments);
    				}
    				List personasBUP = new ArrayList();
    				for (Iterator it = personasBUPTmp.iterator(); it.hasNext(); ) {
    					boolean ejecutivo=false;
    					com.ixe.ods.bup.model.Persona personaBUP = (com.ixe.ods.bup.model.Persona)
    					it.next();
    					Integer idPersona = personaBUP.getIdPersona();
    					String nombreCompleto = personaBUP.getNombreCompleto();
    					String rfc = personaBUP.getRfc();
    					
    					ContratoSica contratoSica = getSicaServiceData().
    					findContratoSicaByIdPersona(idPersona);
    					
    					Visit visit = (Visit) getVisit();
    					HashMap row = new HashMap();
    					if (contratoSica != null) {
    						
    						IPerfil perfil = getSeguridadServiceData().
    						findPerfilByUserAndSystem(visit.getUser().getIdUsuario(),
    								((SupportEngine) getEngine()).getApplicationName());
    						if (getSeguridadServiceData().isCuentaAsignadaEjecutivo(perfil,
    								contratoSica.getNoCuenta())) {
    							row.put("statusContrato", CON_CONTRATO);
    							if (isIxeDirecto()) {
    								row.put("ejecutivo", visit.getUser().getPersona().
    										getNombreCorto());
    							}
    						}
    						else {
    							row.put("statusContrato", CON_CONTRATO_OTRO_EJECUTIVO);
    							row.put("statusContratoCorto", CON_CONTRATO_OTRO_EJECUTIVO);
    							ejecutivo=true;
    							if (isIxeDirecto()) {
    								try {
    									EmpleadoIxe promotor = getSicaServiceData().
    									findPromotorByContratoSica(contratoSica.getNoCuenta());
    									row.put("ejecutivo", promotor.getNombreCorto());
    								}
    								catch (SicaException e) {
    									getDelegate().record(e.getMessage(), null);
    									return;
    								}
    							}
    						}
    						Contrato contrato=getContratoCorto(contratoSica.getNoCuenta());
    						if(!ejecutivo){
	    						if(contrato!=null)
	    						{
	    							contratoCorto=""+contrato.getIdContrato();
	    							row.put("statusContratoCorto", CON_CONTRATO_CORTO);
	    							row.put("contratoCortoIdCliente", contrato.getIdCliente());
	    						}
	    						else
	    						{
	    							contratoCorto="NO TIENE";
	    							row.put("statusContratoCorto", SIN_CONTRATO_CORTO);
	    							row.put("contratoCortoIdCliente", new Integer(-1));
	    						}
    						}
    					}
    					else {
    						row.put("statusContrato", SIN_CONTRATO);
    						row.put("statusContratoCorto", SIN_NINGUN_CONTRATO);
    						if (isIxeDirecto()) {
    							row.put("ejecutivo", "");
    						}
    					}
    					row.put("idPersona", idPersona);
    					row.put("nombreCompleto", nombreCompleto);
    					row.put("rfc", rfc);
    					personasBUP.add(row);
    				}
    				setLevel(1);
    				setPersonasBUP(personasBUP);
							
                    getDelegate().record(mensajeMaxResults
							+ " "+ getRazonSocial() +" "+ getNombre() +" "+ getPaterno() 
							+" "+ getMaterno()+ " Si el cliente que busca no est\u00e1 en la lista, puede darlo de alta oprimiendo la liga "
							+ "'Formulario de Alta de Cliente'.",	null);
    			}
    			else {
    				setPersonasBUP(new ArrayList());
    				getDelegate().record("No se encontr\u00f3 el Cliente en la BUP. Si desea " +
    						"crear un nuevo cliente, utilice la liga 'Formulario de Alta de " +
    						"Cliente'. Para seguir buscando introduzca nuevos criterios y " +
    						"oprima el bot\u00f3n 'BUSCAR'.", null);

    			}
    			
    			setModoSubmit(0);
    		
        }
	}

    /**
	 * Convierte lo escrito en los Criterios de B&uacute;squeda a Uppercase para evitar fallas en
     * las b&uacute;squedas de la Base de Datos.
	 */
	public void convertirAUpperCase() {
        if (StringUtils.isNotEmpty(getPaterno())) {
            setPaterno(getPaterno().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getMaterno())) {
            setMaterno(getMaterno().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getNombre())) {
            setNombre(getNombre().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getRazonSocial())) {
            setRazonSocial(getRazonSocial().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getRfc())) {
            setRfc(getRfc().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getCalle())) {
            setCalle(getCalle().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getNoExterior())) {
            setNoExterior(getNoExterior().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getNoInterior())) {
        	setNoInterior(getNoInterior().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getColonia())) {
            setColonia(getColonia().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getCiudad())) {
            setCiudad(getCiudad().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getDelegacion())) {
            setDelegacion(getDelegacion().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getEstado())) {
            setEstado(getEstado().toUpperCase());
        }
        
    }

    /**
     * Agrega en la BUP al Cliente especificado por el usuario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void nuevoCliente(IRequestCycle cycle) throws ContratacionException {
    	Visit visit = (Visit) getVisit();
    	Integer idPersonaUser = visit.getUser().getIdPersona();
    	Persona personaRegistrada = null;
    	try {
    		PersonaDto perDto = new PersonaDto();
	        Direccion direccion = new Direccion();
	        direccion.setIdTipoDireccion(isPrimaria() ? "1": "3");
	        direccion.setCalle(getCalle());
	        direccion.setNumeroExterior(getNoExterior());
	        direccion.setNumeroInterior(getNoInterior());
	        direccion.setColonia(getColonia());
	        direccion.setCiudad(getCiudad());
	        direccion.setCodigoPostal(getCodigoPostal());
	        direccion.setDelegacion(getDelegacion());
	        direccion.setEntFederativa(getEstado());
	        direccion.setIdPais(getPais().getId());
	        perDto.setDireccion(direccion);
	        perDto.setIdExtranjero(this.getIdExtranjero());
	        String strFecha = "";
	        if (getFechaNacimiento() != null) {
        		DateFormat df = new SimpleDateFormat(DateUtils.FORMATO_FECHA_CON_DIAGONAL);
        		strFecha = df.format(getFechaNacimiento());
        	}
	        if ("FISICA".equals(getFisicaMoral())) {
	            PersonaFisica personaFisica = new PersonaFisica();
	            
	            personaFisica.setNombre(getNombre());
	            personaFisica.setPaterno(getPaterno());
	            personaFisica.setMaterno(
						(!getMaterno().equals("")) ? getMaterno() : ".");
	        	personaFisica.setTipoPersona("PF");
	        	personaFisica.setIdSector(Integer.parseInt(getSectorBanxico().get(VALUE) == "SELECCIONE" ?
	        			"0" : getSectorBanxico().get(VALUE).toString()));
                personaFisica.setRfc(
						(!getRfc().equals("")) ? getRfc() : "XAXX010101000");
                personaFisica.setIdPais(getNacionalidad().getId());
	        	personaFisica.setNacionalidad(getNacionalidad().getDescripcion());
	        	personaFisica.setFechaNacimiento(strFecha);
	        	personaFisica.setDireccion(direccion);
	        	personaFisica.setIdActividad("237");
	        	personaFisica.setActividadGiro("17");
	        	personaFisica.setCurp(this.getCurp());
	        	String tipoComprobante = (String) this.getTipoComprobante().get(VALUE);
	        	personaFisica.setIdTipoIdentifica(tipoComprobante);
	        	personaFisica.setNoIdentificacion(this.getNumComprobante());
	        	personaFisica.setSexo(this.getSexo());
	        	String idPais = getPaisNacimiento() != null ? 
	        			getPaisNacimiento().get(VALUE).toString() : "";
	        	if (StringUtils.isNotEmpty(idPais) && 
	        			!idPais.equals(String.valueOf(ID_DEFAULT))) {
	        		Catalogo pais = new Catalogo();
	        		pais.setId(idPais);
	        		pais.setDescripcion(getPaisNacimiento().get(LABEL).toString());
	        		perDto.setPaisNacimiento(pais);
	        	}
	        	perDto.setPersona(personaFisica);
	    	}
	    	else {
	    	    PersonaMoral personaMoral = new PersonaMoral();
	    	    personaMoral.setRazonSocial(getRazonSocial());
	    	    personaMoral.setTipoPersona("PM");
	    	    personaMoral.setIdSector(Integer.parseInt(getSectorBanxico().get(VALUE) == "SELECCIONE" ?
	    	    		"0" : getSectorBanxico().get(VALUE).toString()));
                personaMoral.setRfc(getRfc());
	        	personaMoral.setNacionalidad(getNacionalidad().getDescripcion());
	        	personaMoral.setFechaConstitucion(strFecha);
	        	personaMoral.setFechaConstitucion(getFechaNacimientoString());
	        	personaMoral.setDireccion(direccion);
	        	personaMoral.setIdPais(getNacionalidad().getId());
	        	personaMoral.setIdActividad("237");
	        	personaMoral.setActividadGiro("17");
	        	perDto.setPersona(personaMoral);
	        	//**************************** Inicia almacenamiento del apoderado legal ******************************
	        	//Persona personaApoderadoLegal = null;
	        	PersonaFisica pfApoderadoLegal = new PersonaFisica();
	        	pfApoderadoLegal.setNombre(getApoderadoNombre().trim());
	        	pfApoderadoLegal.setPaterno(getApoderadoPaterno().trim());
	        	pfApoderadoLegal.setMaterno(StringUtils.isNotEmpty(getApoderadoMaterno()) ? getApoderadoMaterno().trim() : "");
	        	pfApoderadoLegal.setTipoPersona("PF");
	        	pfApoderadoLegal.setRfc("XAXX010101000");
	        	perDto.setApoderado(pfApoderadoLegal);
	        	
	        	//******************************************************************************************************
	        	//********************* Inicia almacenamiento de los datos regulatorios del cliente ********************
	        	if(isVerCamposRegulatorios())
	        	{
	        		RegulatorioPerfil perfil = new RegulatorioPerfil();
	        		//perfil.setIdPersona(new Integer(personaRegistrada.getIdPersona()));
		        	perfil.setContratoSica(null);
		        	perfil.setDatosRegulatoriosActualizados("S");
		        	perfil.setFechaAlta(new Date());
		        	perfil.setFechaUltimaModificacion(perfil.getFechaAlta());
		        	perfil.setIdPersonaAlta(visit.getUser().getIdPersona());
		        	perfil.setIdPersonaUltimaMod(perfil.getIdPersonaAlta());
		        	//perfil.setPerfilMdd("S");
		        	perfil.setRegInstitucionActualizados("S");
		        	perDto.setRegulatorioPerfil(perfil);
		        	//getSicaServiceData().store(perfil);
		        	
		        	RegulatorioDatosPM datosPM = new RegulatorioDatosPM();
		        	//datosPM.setIdPersona(new Integer(personaRegistrada.getIdPersona()));
		        	datosPM.setClaveBanxico(getClaveBanxico());
		        	datosPM.setNombreContraparte(getContraparte());
		        	datosPM.setSociedadMercantil(getTipoSociedadMercantil());
		        	datosPM.setTipoIdentificador(getTipoIdentificador());
		        	datosPM.setClaveIdentificadorContraparte(getClaveIdentificador());
		        	datosPM.setActividadEconomica(getTipoContraparte());
		        	datosPM.setClaveLei(getClaveLei());
		        	datosPM.setSectorIndustrial(getSectorIndustrial());
		        	datosPM.setPaisResidenciaContraparte(getPaisContraparte());
		        	datosPM.setPaisControlContraparte(getPaisControlContraparte());
		        	datosPM.setClaveLeiMatrizDirecta(getClaveLeiMatrizDirecta());
		        	datosPM.setClaveLeiMatrizUltimaInstancia(getClaveLeiMatrizUltima());
		        	datosPM.setFechaContraparte(getFechaContraparte());
		        	datosPM.setFechaSistema(new Date());
		        	datosPM.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
		        	datosPM.setContratoSica(null);
		        	datosPM.setPerfilMdd("S");
		        	perDto.setRegulatorioDatosPM(datosPM);
		        	//getSicaServiceData().store(datosPM);
		        	
		        	RegulatorioInstitucion institucion = new RegulatorioInstitucion();
		        	//institucion.setIdPersona(new Integer(personaRegistrada.getIdPersona()));
		        	institucion.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
		        	institucion.setFechaSistema(new Date());
		        	institucion.setContratoSica(null);
		        	institucion.setTipoRelacion(getTipoRelacion());
		        	perDto.setRegulatorioInstitucion(institucion);
		        	
		        	if(getGrupoFinanciero() != null)
		        	{
		        		if(getGrupoFinanciero().intValue() != 2 && getGrupoFinanciero().intValue() != 1)
		        			institucion.setPerteneceGrupoFinanciero(new Integer(0));
		        		else
		        			institucion.setPerteneceGrupoFinanciero(getGrupoFinanciero());
		        	}
		        	else
		        		institucion.setPerteneceGrupoFinanciero(new Integer(0));
		        	
		        	// Si la contraparte si está relacionada con la institucion  
		        	if(getTipoRelacion() != null && getTipoRelacion().getId() != null && getTipoRelacion().getId().intValue() != 10) 
		        	{
		        		institucion.setEventoRelacion(getTipoEventoRelacion());
		        		institucion.setFechaEvento(getFechaEventoRelacion());
		        	}
		        	else //TipoRelacion = 10, no hay relación
		        	{
		        		institucion.setEventoRelacion(null); // en el reporte se pone 0
		        		institucion.setFechaEvento(null); // en el reporte se va en blanco 
		        	}
		        	
		        	//getSicaServiceData().store(institucion);
	        	}
	        	//******************************************************************************************************
	    	}
	        if (getIdPersona() != null) {//Para solo actualizar la informacion.
	        	perDto.getPersona().setIdPersona(getIdPersona().intValue());
	        }
	        personaRegistrada = this.getPersonaService().guardarPersona(perDto, idPersonaUser);
	        setLevel(1);
	        setNoCuenta("");
	        
	        //Si el cliente pertenece a banorte, se almacenan los datos adicionales
	        if(getEmpresa().get(VALUE).equals(EmpresaCambios.ID_EMPRESA_BANORTE)){
	        	getSicaServiceData().storeDatosClienteBanorte(new Integer(personaRegistrada.getIdPersona()), EmpresaCambios.ID_EMPRESA_BANORTE,getNoSic(),getSucursalOrigen(),
	        	getSucursalOpera(),getCuentaCheques(),visit.getUser().getIdUsuario());
	        } 
	        
	        //60057-CFDI 3.3
    	    if (getNit() != null && !"".equals(getNit().trim()) && !"null".equals(getNit().trim()) ) {
	        	getSicaServiceData().storeNit(new Integer(personaRegistrada.getIdPersona()), getNit());
	        }
	        
	       	setSucursalOpera("");
	       	setSucursalOrigen("");
	       	setNoSic("");
	       	setCuentaCheques("");
	        
	    }
	    catch (Exception e) {
	        warn(e.getMessage(), e);
	        getDelegate().record(e.getMessage(), null);
	        return;
	    }
    	setIdPersona(null);
		setModoSubmit(1);
		agregar(cycle);
    }
    
    /**
     * Agrega en la BUP al Cliente especificado por el usuario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void nuevoClienteAnt(IRequestCycle cycle) throws ContratacionException {
    	Visit visit = (Visit) getVisit();
    	try {
    		//Servicios de Contratacion
	        Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().
                    getIdPersona().intValue());
	        Persona personaRegistrada = null;
	        
	        Direccion direccion = new Direccion();
	        direccion.setIdTipoDireccion(isPrimaria()? "1": "3");
	        direccion.setCalle(getCalle());
	        direccion.setNumeroExterior(getNoExterior());
	        direccion.setNumeroInterior(getNoInterior());
	        direccion.setColonia(getColonia());
	        direccion.setCiudad(getCiudad());
	        direccion.setCodigoPostal(getCodigoPostal());
	        direccion.setDelegacion(getDelegacion());
	        direccion.setEntFederativa(getEstado());
	        direccion.setIdPais(getPais().getId());
	        
	      
	        
	        if ("FISICA".equals(getFisicaMoral())) {
	            PersonaFisica personaFisica = new PersonaFisica();
	            personaFisica.setNombre(getNombre());
	            personaFisica.setPaterno(getPaterno());
	            personaFisica.setMaterno(
						(!getMaterno().equals("")) ? getMaterno() : ".");
	        	personaFisica.setTipoPersona("PF");
	        	personaFisica.setIdSector(Integer.parseInt(getSectorBanxico().get(VALUE) == "SELECCIONE" ?
	        			"0" : getSectorBanxico().get(VALUE).toString()));
                personaFisica.setRfc(
						(!getRfc().equals("")) ? getRfc() : "XAXX010101000");
                personaFisica.setIdPais(getNacionalidad().getId());
                
	        	personaFisica.setNacionalidad(getNacionalidad().getDescripcion());
	        	personaFisica.setFechaNacimiento(getFechaNacimientoString());
	        	personaFisica.setDireccion(direccion);
	        	
	        	personaFisica.setIdActividad("237");
	        	personaFisica.setActividadGiro("17");
	        	
	        	try {
	        		 personaRegistrada = getSicaRegistroPersonaService().
	        		 			registraPersona(personaFisica, usuario);
	        		 
	        	}
	        	catch(ContratacionException ce) {
	        		warn(ce.getMessage(), ce);
	    	        getDelegate().record("Error al registrar Persona F\u00EDsica: " +
	    	        		ce.getMessage(),null);
	    	        return;
	        	}
	        	try {
	        		getSicaRegistroDireccionPersonaService().registraDireccion(personaRegistrada,
	        				direccion, usuario);
	        	}
	        	catch(ContratacionException ce) {
	        		warn(ce.getMessage(), ce);
	    	        getDelegate().record("Error al registrar la Direcci\u00f3n: " +
	    	        		ce.getMessage(), null);
	    	        return;
	        	}
	    	}
	    	else {
	    	    PersonaMoral personaMoral = new PersonaMoral();
	    	    personaMoral.setRazonSocial(getRazonSocial());
	    	    personaMoral.setTipoPersona("PM");
	    	    personaMoral.setIdSector(Integer.parseInt(getSectorBanxico().get(VALUE) == "SELECCIONE" ?
	    	    		"0" : getSectorBanxico().get(VALUE).toString()));
                personaMoral.setRfc(getRfc());
	        	personaMoral.setNacionalidad(getNacionalidad().getDescripcion());
	        	personaMoral.setFechaConstitucion(getFechaNacimientoString());
	        	personaMoral.setDireccion(direccion);
	        	personaMoral.setIdPais(getNacionalidad().getId());
	        	
	        	personaMoral.setIdActividad("237");
	        	personaMoral.setActividadGiro("17");
	        	
	        	try {
	        		personaRegistrada = getSicaRegistroPersonaService().
	        				registraPersona(personaMoral, usuario);
	        	}
	        	catch(ContratacionException ce) {
	        		warn(ce.getMessage(), ce);
	    	        getDelegate().record("Error al registrar Persona Moral: " +
	    	        		ce.getMessage(), null);
	    	        return;
	        	}
	        	try {
	        		getSicaRegistroDireccionPersonaService().registraDireccion(personaRegistrada,
	        				direccion, usuario);
	        	}
	        	catch(ContratacionException ce) {
	        		warn(ce.getMessage(), ce);
	    	        getDelegate().record("Error al registrar la Direcci\u00f3n: " +
	    	        		ce.getMessage(), null);
	    	        return;
	        	}
	        	//**************************** Inicia almacenamiento del apoderado legal ******************************
	        	Persona personaApoderadoLegal = null;
	        	PersonaFisica pfApoderadoLegal = new PersonaFisica();
	        	pfApoderadoLegal.setNombre(getApoderadoNombre().trim());
	        	pfApoderadoLegal.setPaterno(getApoderadoPaterno().trim());
	        	pfApoderadoLegal.setMaterno(StringUtils.isNotEmpty(getApoderadoMaterno()) ? getApoderadoMaterno().trim() : "");
	        	pfApoderadoLegal.setTipoPersona("PF");
	        	pfApoderadoLegal.setRfc("XAXX010101000");
	        	try 
	        	{
	        		personaApoderadoLegal = getSicaRegistroPersonaService().registraPersona(pfApoderadoLegal, usuario);
	        	}
	        	catch(ContratacionException ce) 
	        	{
	        		warn(ce.getMessage(), ce);
	    	        getDelegate().record("Error al registrar al apoderado legal: " + ce.getMessage(),null);
	    	        return;
	        	}
	        	//******************************************************************************************************
	        	//******** Inicia almacenamiento de la relacion entre el cliente y su apoderado legal ******************
	        	RelacionPersonas relacion = new RelacionPersonas();
	        	relacion.setIdPersona1(personaRegistrada.getIdPersona());
	        	relacion.setIdPersona2(personaApoderadoLegal.getIdPersona());  //DESCRIPCION_ATRIBUTO_APODERADO
	        	relacion.setIdTipoRelacion("6"); 
	        	relacion.setStatus(Constantes.STATUS_RELACION_VIGENTE);
	        	
	        	try
	        	{
	        		getSicaRegistroRelacionContratacionService().registraRelacion(relacion, usuario);
	        		System.out.println("ID relacion: " + relacion.getIdInstanciaRelacion());
	        	}
	        	catch(ContratacionException ce)
	        	{
	        		warn(ce.getMessage(), ce);
	    	        getDelegate().record("Error al registrar la relaci\u00f3n entre el cliente y su apoderado legal: " +
	    	        		ce.getMessage(), null);
	    	        return;
	        	} 
	        	//******************************************************************************************************
	        	//********************* Inicia almacenamiento de los datos regulatorios del cliente ********************
	        	if(isVerCamposRegulatorios())
	        	{
	        		RegulatorioPerfil perfil = new RegulatorioPerfil();
	        		perfil.setIdPersona(new Integer(personaRegistrada.getIdPersona()));
		        	perfil.setContratoSica(null);
		        	perfil.setDatosRegulatoriosActualizados("S");
		        	perfil.setFechaAlta(new Date());
		        	perfil.setFechaUltimaModificacion(perfil.getFechaAlta());
		        	perfil.setIdPersonaAlta(visit.getUser().getIdPersona());
		        	perfil.setIdPersonaUltimaMod(perfil.getIdPersonaAlta());
		        	//perfil.setPerfilMdd("S");
		        	perfil.setRegInstitucionActualizados("S");
		        	
		        	getSicaServiceData().store(perfil);
		        	
		        	RegulatorioDatosPM datosPM = new RegulatorioDatosPM();
		        	datosPM.setIdPersona(new Integer(personaRegistrada.getIdPersona()));
		        	datosPM.setClaveBanxico(getClaveBanxico());
		        	datosPM.setNombreContraparte(getContraparte());
		        	datosPM.setSociedadMercantil(getTipoSociedadMercantil());
		        	datosPM.setTipoIdentificador(getTipoIdentificador());
		        	datosPM.setClaveIdentificadorContraparte(getClaveIdentificador());
		        	datosPM.setActividadEconomica(getTipoContraparte());
		        	datosPM.setClaveLei(getClaveLei());
		        	datosPM.setSectorIndustrial(getSectorIndustrial());
		        	datosPM.setPaisResidenciaContraparte(getPaisContraparte());
		        	datosPM.setPaisControlContraparte(getPaisControlContraparte());
		        	datosPM.setClaveLeiMatrizDirecta(getClaveLeiMatrizDirecta());
		        	datosPM.setClaveLeiMatrizUltimaInstancia(getClaveLeiMatrizUltima());
		        	datosPM.setFechaContraparte(getFechaContraparte());
		        	datosPM.setFechaSistema(new Date());
		        	datosPM.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
		        	datosPM.setContratoSica(null);
		        	datosPM.setPerfilMdd("S");
		        	
		        	getSicaServiceData().store(datosPM);
		        	
		        	RegulatorioInstitucion institucion = new RegulatorioInstitucion();
		        	institucion.setIdPersona(new Integer(personaRegistrada.getIdPersona()));
		        	institucion.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
		        	institucion.setFechaSistema(new Date());
		        	institucion.setContratoSica(null);
		        	institucion.setTipoRelacion(getTipoRelacion());
		        	
		        	if(getGrupoFinanciero() != null)
		        	{
		        		if(getGrupoFinanciero().intValue() != 2 && getGrupoFinanciero().intValue() != 1)
		        			institucion.setPerteneceGrupoFinanciero(new Integer(0));
		        		else
		        			institucion.setPerteneceGrupoFinanciero(getGrupoFinanciero());
		        	}
		        	else
		        		institucion.setPerteneceGrupoFinanciero(new Integer(0));
		        	
		        	// Si la contraparte si está relacionada con la institucion  
		        	if(getTipoRelacion() != null && getTipoRelacion().getId() != null && getTipoRelacion().getId().intValue() != 10) 
		        	{
		        		institucion.setEventoRelacion(getTipoEventoRelacion());
		        		institucion.setFechaEvento(getFechaEventoRelacion());
		        	}
		        	else //TipoRelacion = 10, no hay relación
		        	{
		        		institucion.setEventoRelacion(null); // en el reporte se pone 0
		        		institucion.setFechaEvento(null); // en el reporte se va en blanco 
		        	}
		        	
		        	getSicaServiceData().store(institucion);
	        	}
	        	//******************************************************************************************************
	    	}
	        setLevel(1);
	        setNoCuenta("");
	        
	        //Si el cliente pertenece a banorte, se almacenan los datos adicionales
	        if(getEmpresa().get(VALUE).equals(EmpresaCambios.ID_EMPRESA_BANORTE)){
	        	getSicaServiceData().storeDatosClienteBanorte(new Integer(personaRegistrada.getIdPersona()), EmpresaCambios.ID_EMPRESA_BANORTE,getNoSic(),getSucursalOrigen(),
	        	getSucursalOpera(),getCuentaCheques(),visit.getUser().getIdUsuario());
	        }
	        
	       	setSucursalOpera("");
	       	setSucursalOrigen("");
	       	setNoSic("");
	       	setCuentaCheques("");
	        
	    }
	    catch (Exception e) {
	        warn(e.getMessage(), e);
	        getDelegate().record(e.getMessage(),null);
	        return;
	    }
		setModoSubmit(1);
		agregar(cycle);
    }

    /**
     * Asigna un Contrato SICA a un Cliente especificado por el usuario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void asignarContratoSICA(IRequestCycle cycle) {
        setLevel(0);
        Visit visit = (Visit) getVisit();
        Integer idPersona = new Integer((cycle.getServiceParameters()[0]).toString());
        String nombreCompleto = (cycle.getServiceParameters()[1]).toString();
    	try {
    		debug("Asignar contrato a la persona: " + idPersona);
    		RespuestaDto resDto = 
    				getPersonaService().isValidaInformacionGeneralPersona(idPersona);
    		this.debug("Respuesta de la validación de datos: " + resDto.getCodigo());
    		if (resDto.getCodigo() == RespuestaDto.EXITO) {
    			//Servicios de Contratacion
	            Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().
	                    getIdPersona().intValue());
	            usuario.setTicketInfo(visit.getTicketInfo());
	            usuario.setTicket(visit.getTicket());
	            Persona persona = ((PersonaDto) resDto.getObjecto()).getPersona();
	            int flujo = this.modificaDatosPersona((PersonaDto) resDto.getObjecto());
	            //try{jdch30
	            if (flujo == 0) {
		            super.debug("Mensage de *** CHUY *** entro al servicio del Moc registraCuentaCambios:  ");
		            String noContratoSICA = getSicaContratacionProductoBancoService().registraCuentaCambios(persona, usuario);
		            setNoCuentaContratoSica(noContratoSICA);
		            limpiarCampos2();
		            setAlta(false);
		            setModoSubmit(0);
		            setLevel(1);
		            setPersonasBUP(new ArrayList());
		            //nuevo jdch 07052014
		            ContratoSica contratoSICA = getSicaServiceData().findContratoSicaByIdPersona(new Integer(persona.getIdPersona()));
		            Contrato contratoCorto = getSicaServiceData().getContratoCortoXCuenta(contratoSICA.getNoCuenta());
		
		            
		            if (contratoCorto == null){
			            super.debug("Mensage de *** CHUY *** entro a registrar contrato corto ya que no tenia uno");
			            asignarContratoCorto(cycle);
		            }
		            else{
		            	super.debug("Mensage de *** CHUY *** entro a actualizar contrato corto a restringido ya que es una reapertura");
		            	getSicaServiceData().cambiarEstadoContrato(contratoSICA.getNoCuenta(),TipoBloqueo.RESTRINGIDO_POR_APERTURA,113890);
		                getDelegate().record("Se reasign\u00f3 contrato corto No.: "+contratoCorto.getIdContrato()+ " y el Contrato SICA No.: " +
		                        getNoCuentaContratoSica() + " para el Cliente: " + nombreCompleto +
		                        ", con clave de referencia: " + getClaveReferencia(idPersona) +
		                        ". Le recordamos que es necesario dar a conocer al cliente su clave de referencia.",
		                        null);
		            }            
		            
		            getSicaServiceData().actualizarInfoRegulatoria(new Integer(persona.getIdPersona()), contratoSICA.getNoCuenta());
	            }
    		}
    		else {
    			this.validaErrorAsignarContrato(resDto);
    		}
        }
    	/* *
    	 * Éste catch es agregado para atrapar las excepciones checked que hayan sido encapsuladas en UndeclaredThrowableException
    	 * para ser manejados y poder mostrar un mensaje dependiendo del error que ocurrió
    	 */
    	catch(UndeclaredThrowableException e){
    		Throwable excepcionUndeclared = e.getUndeclaredThrowable();
    		error(excepcionUndeclared.getMessage(), excepcionUndeclared);
    		if( excepcionUndeclared.getCause() instanceof FaltaDireccionException ){
    			getDelegate().record("Ocurrió un error, favor de contactar a Mesa de Control: "+excepcionUndeclared.getMessage(),
                        null);
    		} else{
    			getDelegate().record("Ocurrió un error, favor de contactar a sistemas: "+excepcionUndeclared.getMessage(),
                        null);
    		}
    	}
	    catch (ContratacionException ce) {//jdch30 se agrego
	    	if (ce.getCause() instanceof FaltaDireccionException) {
	    		debug("FaltaDireccionException...");
	    		setLevel(0);
	    		getDelegate().record(((FaltaDireccionException) ce.getCause()).getMessage(), null);
	    	}
	    	else if (ce.getCause() instanceof FaltaInfGeneralException ) {
	    		debug("FaltaInfGeneralException...");
	    		setLevel(0);
	    		getDelegate().record(((FaltaInfGeneralException) ce.getCause()).getMessage(), null);
	    	}
	    	else {
		    	Persona persona = getSicaBusquedaPersonaService().buscaPersonaPorId(idPersona.intValue());
		    	ContratoSica CS = getSicaServiceData().findContratoSicaByIdPersona(new Integer(persona.getIdPersona()));
		    	if (CS != null){
		    		super.debug("Lanzo una excepcion del MOC y forza la generacion del contrato corto:  ");
		    		//nuevo jdch 07052014
		            ContratoSica contratoSICA = getSicaServiceData().findContratoSicaByIdPersona(new Integer(persona.getIdPersona()));
		            Contrato contratoCorto = getSicaServiceData().getContratoCortoXCuenta(contratoSICA.getNoCuenta());
		            
		            if (contratoCorto == null){
			            super.debug("Mensage de *** CHUY *** entro a registrar contrato corto ya que no tenia uno");
			            asignarContratoCorto(cycle);
		            }
		            else{
		            	super.debug("Mensage de *** CHUY *** entro a actualizar contrato corto a restringido ya que es una reapertura");
		            	getSicaServiceData().cambiarEstadoContrato(contratoSICA.getNoCuenta(),TipoBloqueo.RESTRINGIDO_POR_APERTURA,113890/*Se asigna 113890 ya que pertenece al usuario de la aplicacion*/);
		                getDelegate().record("Se reasign\u00f3 contrato corto No.: "+contratoCorto.getIdContrato()+ " y el Contrato SICA No.: " +
		                        getNoCuentaContratoSica() + " para el Cliente: " + nombreCompleto +
		                        ", con clave de referencia: " + getClaveReferencia(idPersona) +
		                        ". Le recordamos que es necesario dar a conocer al cliente su clave de referencia.",
		                        null);
		            }	    	
				}
		    	else{
		    		error(ce.getMessage(), ce);
		    		getDelegate().record("Ocurri\u00f3 un error, favor de contactar a sistemas: "+ce.getMessage(),null);
		    	}
	    	}
	    }//jdch30 se agrego
    	catch (SicaValidationException ex) {
    		String msg = "Error al asignar contrato: " + ex.getMessage();
    		setLevel(0);
    		getDelegate().record(msg, null);
    	}
        catch (Exception e) {
            error(e.getMessage(), e);
            String msg = "Ocurri\u00f3 un error, favor de contactar a sistemas: " + e.getMessage();
            getDelegate().record(msg, null);
        }
    }
    
    public void asignarContratoCorto(IRequestCycle cycle){
    	Visit visit = (Visit) getVisit();
    	String nombreCompleto = (cycle.getServiceParameters()[1]).toString();
    	Integer idPersona = new Integer((cycle.getServiceParameters()[0]).toString());
    	Persona persona = getSicaBusquedaPersonaService().
        buscaPersonaPorId(idPersona.intValue());
    	Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().
                getIdPersona().intValue());
    	ContratoSica contratoSICA = getSicaServiceData().findContratoSicaByIdPersona(new Integer(persona.getIdPersona()));
        
        setNoCuentaContratoSica(contratoSICA.getNoCuenta());
        
    	Contrato contrato =new Contrato();
        
        setNoCuentaContratoSica(getSicaServiceData().findContratoSicaByIdPersona(new Integer(persona.getIdPersona())).getNoCuenta());
        Integer contratoCorto = getSicaServiceData().storeDatosContrato(new Integer(persona.getIdPersona()), 
        		getNoCuentaContratoSica(), visit.getUser().getIdUsuario());
        
        getSicaServiceData().actualizarInfoRegulatoria(new Integer(persona.getIdPersona()), contratoSICA.getNoCuenta());
        
        limpiarCampos2();
        setAlta(false);
        setModoSubmit(0);
        setLevel(1);
        setPersonasBUP(new ArrayList());
        debug("Se ejecuto metodo correctamente: asignarContratoCorto()");
        getDelegate().record("Se registr\u00f3 contrato corto No.: "+contratoCorto+ " y el Contrato SICA No.: " +
                getNoCuentaContratoSica() + " para el Cliente: " + nombreCompleto +
                ", con clave de referencia: " + getClaveReferencia(idPersona) +
                ". Le recordamos que es necesario dar a conocer al cliente su clave de referencia.",
                null);
//        return contratoCorto;
    }
    /**
     * Valida los datos del IFE por medio del ESB.
     *
     * @param folioIfe	El n&uacte;mero de folio del IFE.
     * @param anioRegistro El anio de registro del IFE.
     * @param numeroEmisionIfe El n&uacte;mero de emisi&oacute;n del IFE.
     * @param ocrIfe El n&uacute;mero de OCR del IFE.
     */
    private void validarCredencialIfe(String folioIfe, String anioRegistro,
    		String numeroEmisionIfe, String ocrIfe) {
    	try {
    		getSicaEsbService().validaCredenciaIfe(((Visit) getVisit()).getTicketInfo().
    				getNombreSistema(), ((Visit) getVisit()).getUser().getClave(),
    				((Visit) getVisit()).getTicket(), folioIfe, anioRegistro, numeroEmisionIfe,
    				ocrIfe);
    	}
        catch (SicaException e) {
        	warn(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
        catch (Exception e) {
        	warn(e.getMessage(), e);
        	getDelegate().record("No se pudo validar el IFE. Favor de contactar al \u00e1rea de" +
            		" sistemas.", null);
        }
    }

    /**
     * Verifica que los campos sean correctos
     * @return boolean  True o False, dependiendo si la informacion ingresada es correcta
     */
    public boolean verificaCampos() {
    	if (getModoSubmit() == 1) { // Es busqueda
    		if (getFisicaMoral().equals("FISICA")) {
    			if ((getNombre().trim().length() < 2) &&
        				(getMaterno().trim().length() < 2)  && (getPaterno().trim().length() < 2) &&
        				(getRfc().trim().length() < 3) && (getNoCuenta().trim().length() < 3)) {
        			return false;
        		}
    		}
    		else {
    			if ((getRazonSocial().trim().length() < 3) &&
        				(getRfc().trim().length() < 3) && (getNoCuenta().trim().length() < 3)) {
        			return false;
        		}	
    		}    		
    	} // Es alta
    	else {
    		if (getNacionalidad() == null || 
    				getNacionalidad().getId().equals(String.valueOf(ID_DEFAULT))) {
    			return false;
    		}
    		else {
    			if (getSectorBanxico() == null || 
    					getSectorBanxico().get(VALUE).toString().equals("SELECCIONE")) {
        			return false;
        		}
    			if (getFisicaMoral().equals("FISICA")) {
    				if (StringUtils.isEmpty(getNombre()) || getNombre().trim().length() < 2) {
    					return false;
    				}
    				if (StringUtils.isEmpty(getPaterno()) || getPaterno().trim().length() < 2) {
    					return false;
    				}
    				if (StringUtils.isNotEmpty(getMaterno()) && getMaterno().trim().length() < 2) {
    					return false;
    				}
    				if (getTipoComprobante() == null || 
    						getTipoComprobante().get(VALUE).equals(String.valueOf(ID_DEFAULT))) {
    	    			return false;
    	    		}
    				if (StringUtils.isEmpty(getNumComprobante())) {
    					return false;
    				}
    				if (StringUtils.isEmpty(getSexo())) {
    					return false;
    				}
    				if (getPaisNacimiento() == null || 
    						getPaisNacimiento().get(VALUE).equals(String.valueOf(ID_DEFAULT))) {
    	    			return false;
    	    		}
					if (isNacionalidadMexicana()) {
						if (StringUtils.isEmpty(getCurp())) {
	    					return false;
	    				}
						if (StringUtils.isEmpty(getRfc()) || getRfc().trim().length() < 3) {
							return false;
						}
	    			}
	    			else {
	    				if (StringUtils.isEmpty(getIdExtranjero())) {
	    					return false;
	    				}
	    			}
				}
				else {
					if (StringUtils.isEmpty(getRazonSocial()) || getRazonSocial().trim().length() < 3) {
						return false;
					}
					if (isNacionalidadMexicana()) {
						if (StringUtils.isEmpty(getRfc()) || getRfc().trim().length() < 3) {
							return false;
						}
	    			}
	    			else {
	    				if (StringUtils.isEmpty(getIdExtranjero())) {
	    					return false;
	    				}
	    			}
				}
    		}
        	if (getModoSubmit() == 3 && (StringUtils.isEmpty(getCalle()) || getCalle().trim().
        			length() < 3 || StringUtils.isEmpty(getNoExterior()) || getNoExterior().trim().
        			length() == 0 || StringUtils.isEmpty(getColonia()) || getColonia().trim().
        			length() < 3 || getColonia().equals("-- SELECCIONE COLONIA --") ||
        			StringUtils.isEmpty(getCiudad()) || getCiudad().trim().length() < 3 ||
        			StringUtils.isEmpty(getDelegacion()) || getDelegacion().trim().length() < 3 ||
        			getDelegacion().equals("-- SELECCIONE MUNICIPIO --") ||
        			StringUtils.isEmpty(getEstado()) || getEstado().trim().length() < 3 ||
        			getEstado().equals("-- SELECCIONE ENTIDAD --"))
        			|| getSectorBanxico().get(VALUE).toString().equals("SELECCIONE")) {
        		return false;
        	}
    	}
    	return true;
    }
    public boolean validarCaracteresCamposBanorte(){
    	
    	if(getNoSic().trim().length()!=0 && getSucursalOrigen().trim().length()!=0
    			&& getSucursalOpera().trim().length()!=0 && getCuentaCheques().trim().length()!=0){
    		if(!getNoSic().trim().matches("[0-9]*") // [A-Za-z0-9- ]*
    				||!getSucursalOrigen().trim().matches("[0-9]*")
    				||!getSucursalOpera().trim().matches("[0-9]*")
    				||!getCuentaCheques().trim().matches("[0-9]*")){
    			return false;
    		}
            	
    	}
    	return true;
    }
    public boolean validarCaracteresCampos(){
    	if(getModoSubmit() == 1) { // Es busqueda
    		if (getFisicaMoral().equals("FISICA")) {
    			if(!getNombre().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
    					|| !getMaterno().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
    					|| !getPaterno().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
    					|| !getRfc().matches("[a-zA-Z0-9]*")
    					|| !getNoCuenta().trim().matches("[-0-9]*")) {
    				return false;
        		}
    		}
    		else {
    			if (!getRazonSocial().trim().matches("[A-Za-z0-9.,& \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
    					|| !getRfc().trim().matches("[a-zA-Z0-9]*")
    					|| !getNoCuenta().trim().matches("[-0-9]*")){
        			return false;
        		}
    		}
    		if(!validarCaracteresCamposBanorte())
    			return false;
    	} // Es alta
    	else {
    		String pattern = "[a-zA-Z0-9]*";
    		if (getFisicaMoral().equals("FISICA")) {
        		if(!getNombre().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
        				|| !getPaterno().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
        				|| !getMaterno().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")){
        			return false;
        		}
        		if (getNacionalidad().getId().equals(String.valueOf(Num.I_48))) {
        			if (!getRfc().trim().matches(pattern)) {
        				return false;
        			}
        			if (!getCurp().trim().matches(pattern)) {
        				return false;
        			}
        		}
        		else if (!getNacionalidad().getId().equals(String.valueOf(ID_DEFAULT))){
        			if (StringUtils.isNotEmpty(getRfc()) && !getRfc().trim().matches(pattern)) {
        				return false;
        			}
        			if (!getIdExtranjero().trim().matches(pattern)) {
        				return false;
        			}
        		}
        		if(!validarCaracteresCamposBanorte())
        			return false;
        	}
        	else {
        		if (!getRazonSocial().trim().matches("[A-Za-z0-9.,& \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")) {
        			return false;
        		}
        		if (!getNacionalidad().getId().equals(String.valueOf(Num.I_48)) && 
        				!getNacionalidad().getId().equals(String.valueOf(ID_DEFAULT))) {
        			if (!getIdExtranjero().trim().matches(pattern)) {
        				return false;
        			}
        		}
        		if (StringUtils.isNotEmpty(getRfc()) && 
        				!getRfc().trim().matches("[a-zA-Z0-9]*")) {
        			return false;
        		}
        		if(!validarCaracteresCamposBanorte())
        			return false;
        	}
        	if (getModoSubmit() == 3 && 
        			(!getCalle().trim().matches("[A-Za-z0-9., \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
        				|| !getNoExterior().trim().matches("[0-9]*")
        				|| !getNoInterior().trim().matches("[0-9a-zA-Z]*")
        				|| !getCodigoPostal().trim().matches("[0-9]*")
        				|| !getColonia().trim().matches("[A-Za-z0-9. \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*") 
        				|| !getCiudad().trim().matches("[A-Za-z. \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
        				|| !getDelegacion().trim().matches("[A-Za-z. \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
        				|| !getEstado().trim().matches("[A-Za-z. \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*"))) {
        		return false;
        	}
        	if(getModoSubmit() == 3 && !validarCaracteresCamposBanorte())
    			return false;
    	}
    	
    	return true;
    }
    
    /**
     * Validar caracteres campos modificar.
     *
     * @return true, if successful
     */
    public boolean validarCaracteresCamposModificar(){
		String pattern = "[a-zA-Z0-9]*";
		if (getFisicaMoral().equals("FISICA")) {
    		if(!getNombre().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
    				|| !getPaterno().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
    				|| !getMaterno().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")){
    			return false;
    		}
    		if (getNacionalidad().getId().equals(String.valueOf(Num.I_48))) {
    			if (!getRfc().trim().matches(pattern)) {
    				return false;
    			}
    			if (!getCurp().trim().matches(pattern)) {
    				return false;
    			}
    		}
    		else if (!getNacionalidad().getId().equals(String.valueOf(ID_DEFAULT))){
    			if (StringUtils.isNotEmpty(getRfc()) && !getRfc().trim().matches(pattern)) {
    				return false;
    			}
    			if (!getIdExtranjero().trim().matches(pattern)) {
    				return false;
    			}
    		}
    		if(!validarCaracteresCamposBanorte())
    			return false;
    	}
    	else {
    		if (!getRazonSocial().trim().matches("[A-Za-z0-9.,& \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")) {
    			return false;
    		}
    		if (!getNacionalidad().getId().equals(String.valueOf(Num.I_48)) && 
    				!getNacionalidad().getId().equals(String.valueOf(ID_DEFAULT))) {
    			if (!getIdExtranjero().trim().matches(pattern)) {
    				return false;
    			}
    		}
    		if (StringUtils.isNotEmpty(getRfc()) && 
    				!getRfc().trim().matches("[a-zA-Z0-9]*")) {
    			return false;
    		}
    		if(!validarCaracteresCamposBanorte())
    			return false;
    	}
    	
    	return true;
    }
    
    /**
     * Verifica que el Codigo Postal sea correcto
     * @return boolean   True o False, dependiendo si el Codigo Postal es correcto o no
     */
    private boolean verificaCodigoPostal() {
    	if (getCodigoPostal().equals("") || getCodigoPostal() == null ||
    			getCodigoPostal().length() != 5) {
    		setCodigoPostal("");
    		return false;
    	}
    	String digitos = "0123456789";
    	for (int i=0; i < getCodigoPostal().length(); i++) {
    		if (digitos.indexOf(String.valueOf(getCodigoPostal().charAt(i))) < 0) {
    	        return false;
    		}
    	}
    	return true;
    }
  
    /**
     * Método que busca las colonias, municipios y estados asociados a un Código Postal
     * @param codigoPostal             Codigo Postal
     * @throws ContratacionException   Excepción lanzada por el sistema de Contratacion
     */
    public void direccionCP(String codigoPostal) throws ContratacionException{
    	debug("Metodo direccionCP()");
    	if (verificaCodigoPostal()) {
	    	Visit visit = (Visit) getVisit();
	    	Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().
	                getIdPersona().intValue());
	    	Direccion direccionAux = new Direccion();
			direccionAux.setCodigoPostal(codigoPostal);
			try {
				direccionAux = getSicaConsultaDireccionesPersonaService().obtenDireccion(usuario,
						direccionAux);
				if (direccionAux != null) {
					setColonia("");
					setDelegacion("");
					setEstado("");
					setHashmapColonia(direccionAux.getColoniasParaCodigo());
					setHashmapMunicipio(direccionAux.getMunicipiosParaColonia());
					setHashmapEntidad(direccionAux.getEntidadesFederativasParaMunicipio());
				}
				else {
					setHashmapColonia(null);
					setHashmapMunicipio(null);
					setHashmapEntidad(null);
					setColonia("");
					setDelegacion("");
					setEstado("");
					setCodigoPostal("");
					getDelegate().record("No se ha encontrado informaci\u00f3n para el" +
							" C\u00f3digo Postal, intente con uno diferente.", null);
					return;
				}
			}
			catch (ContratacionException e) {
				setHashmapColonia(null);
				setHashmapMunicipio(null);
				setHashmapEntidad(null);
				setColonia("");
				setDelegacion("");
				setEstado("");
				setCodigoPostal("");
				getDelegate().record("No se ha encontrado informaci\u00f3n para el" +
						" C\u00f3digo Postal, intente con uno diferente.", null);
		        return;
			}
    	}
    	else {
    		getDelegate().record("El  C\u00f3digo Postal ingresado es incorrecto.", null);
	        return;
    	}
    }
    
    /**
     * Popula el combo de Tipos de Persona.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getComboTiposPersona() {
		return new StringPropertySelectionModel(new String[] {"MORAL", "FISICA"});
	}

	/**
     * Obtiene el Modelo a mostrar en el Combo de Sectores.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getSectorBanxicoModel() {
	    String tipoPersona = "FISICA".equals(getFisicaMoral()) ? "PF" : "PM";
	    Vector sectoresTmp = getSicaConsultaCatalogosService().
                obtenCatSectorEconomicoBanxico(tipoPersona);
	    List sectores = new ArrayList();
	    Map sectorMap = new HashMap();
	    
	    sectorMap.put(VALUE, "SELECCIONE");
	    sectorMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
	    sectores.add(sectorMap);
	    
	    for (Iterator i = sectoresTmp.iterator(); i.hasNext();) {
	        Catalogo sector = (Catalogo) i.next();
	        sectorMap = new HashMap();
	        sectorMap.put(VALUE, sector.getId());
	        sectorMap.put(LABEL, sector.getDescripcion());
	        
	        sectores.add(sectorMap);
	    }
	    return new RecordSelectionModel(sectores, VALUE, LABEL);
    }

/**
     * Obtiene el Modelo a mostrar en el Combo de Colonias.
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoMunicipios() throws ContratacionException {
		if (getHashmapMunicipio() == null) {
			direccionCP(getCodigoPostal());
		}
		Set keys = getHashmapMunicipio().keySet();
		List municipios = new ArrayList();
		if (getDelegacion() == null || getDelegacion().equals("") || 
				getDelegacion().equals("-- SELECCIONE MUNICIPIO --")) {
			municipios.add("-- SELECCIONE MUNICIPIO --");
		}
		for (Iterator i = keys.iterator(); i.hasNext(); ) {
	        municipios.add(getHashmapMunicipio().get(i.next().toString()));
	    }
	    return new StringPropertySelectionModel(listToArray(municipios));
    }

/**
     * Obtiene el Modelo a mostrar en el Combo de Colonias.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoEntidades() throws ContratacionException{
		if (getHashmapEntidad() == null) {
			direccionCP(getCodigoPostal());
		}
		Set keys = getHashmapEntidad().keySet();
		List entidades = new ArrayList();
		if (getEstado() == null || getEstado().equals("") ||
				getEstado().equals("-- SELECCIONE ENTIDAD --")) {
			entidades.add("-- SELECCIONE ENTIDAD --");
		}
	    
		for (Iterator i = keys.iterator(); i.hasNext();) {
	        entidades.add(getHashmapEntidad().get(i.next().toString()));
	    }
	    return new StringPropertySelectionModel(listToArray(entidades));
    }

/**
     * Obtiene el Modelo a mostrar en el Combo de Colonias.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoColonias() throws ContratacionException{
		if(getHashmapColonia() == null){
			direccionCP(getCodigoPostal());
		}
		Set keys = getHashmapColonia().keySet();
		List colonias = new ArrayList();
		if (getColonia() == null || getColonia().equals("") ||
				getColonia().equals("-- SELECCIONE COLONIA --")) {
			colonias.add("-- SELECCIONE COLONIA --");
		}
		for (Iterator i = keys.iterator(); i.hasNext();) {
	        colonias.add(getHashmapColonia().get(i.next().toString()));
	    }
	    return new StringPropertySelectionModel(listToArray(colonias));
    }

    /**
	 * @param list       Lista que se va a convertir en Arreglo
	 * @return String[]  Arreglo de String
	 */
	public String[] listToArray(List list){
		String[] array = new String[list.size()];
		for (int i=0; i<list.size(); i++) {
			array[i] = (String) list.get(i);
		}
		return array;
	}
	
	/**
	 * Verifica que la Fecha de Nacimiento sea menor que la Fecha actual
	 * @param fecha       Fecha de Nacimiento/Constituci\u00f3n
	 * @return boolean    True o False, si la fecha es correcta
	 */
	public boolean isValidDate(Date fecha) {
        try {
            Calendar fechaNac = Calendar.getInstance();
            fechaNac.setTime(fecha);
            Calendar diaActual = Calendar.getInstance();
            diaActual.setTime(new Date());
            if (diaActual.before(fechaNac)) {
            	setFechaNacimientoString("");
            	setFechaNacimiento(null);
            	return false;
            }
        }
        catch (Exception e) {
        	setFechaNacimientoString("");
        	setFechaNacimiento(null);
            return false;
        }
        return true;
    }
	
	//************************************************************************************************************
	//*************************  Carga de catálogos para datos regulatorios **************************************

	/**
     * Obtiene el Modelo a mostrar en el Combo de Tipo de sociedad mercantil.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoSociedadMercantil() {
		List catalogo = new ArrayList();
		RegulatorioSociedadMercantil opcionDefault = new RegulatorioSociedadMercantil();
		opcionDefault.setId(ID_DEFAULT);
		opcionDefault.setDescripcionCorta(OPCION_DEFAULT);
		catalogo.add(opcionDefault);
		catalogo.addAll(getSicaServiceData().findCatalogoSociedadMercantil());

	    return new RecordSelectionModel(catalogo, "id", "descripcionCorta"); 
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo de Tipo de identificador de la contraparte.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoTipoIdentificador() {
		List catalogo = new ArrayList();
		RegulatorioTipoIdentificador opcionDefault = new RegulatorioTipoIdentificador();
		opcionDefault.setId(new Integer(-100));
		opcionDefault.setDescripcion("SELECCIONE LA OPCION DESEADA");
		catalogo.add(opcionDefault);
		catalogo.addAll(getSicaServiceData().findCatalogoIdentificadorContraparte());

	    return new RecordSelectionModel(catalogo, "id", "descripcion");
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo de Tipo de contraparte (Actividad economica).
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoTipoContraparte() {
		List catalogo = new ArrayList();
		RegulatorioActividadEconomica opcionDefault = new RegulatorioActividadEconomica();
		opcionDefault.setId(new Integer(-100));
		opcionDefault.setDescripcion("SELECCIONE LA OPCION DESEADA");
		catalogo.add(opcionDefault);
		catalogo.addAll(getSicaServiceData().findCatalogoActividadEconomica());

	    return new RecordSelectionModel(catalogo, "id", "descripcion");
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo de Tipo de contraparte (Actividad economica).
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoSectorIndustrial() {
		List catalogo = new ArrayList();
		RegulatorioSectorIndustrial opcionDefault = new RegulatorioSectorIndustrial();
		opcionDefault.setId(new Integer(-100));
		opcionDefault.setDescripcion("SELECCIONE LA OPCION DESEADA");
		catalogo.add(opcionDefault);
		catalogo.addAll(getSicaServiceData().findCatalogoSectorIndustrial());

	    return new RecordSelectionModel(catalogo, "id", "descripcion");
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo pais de datos regulatorios.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoPaisRegulatorio() {
		List catalogo = new ArrayList();
		RegulatorioPais opcionDefault = new RegulatorioPais();
		opcionDefault.setClaveIso("-100");
		opcionDefault.setNombrePais("SELECCIONE LA OPCION DESEADA");
		catalogo.add(opcionDefault);
		catalogo.addAll(getSicaServiceData().findCatalogoPais());

	    return new RecordSelectionModel(catalogo, "claveIso", "nombrePais");
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo Tipo Relacion.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoTipoRelacion() {
		List catalogo = new ArrayList();
		RegulatorioTipoRelacion opcionDefault = new RegulatorioTipoRelacion();
		opcionDefault.setId(new Integer(-100));
		opcionDefault.setDescripcion("SELECCIONE LA OPCION DESEADA");
		catalogo.add(opcionDefault);
		catalogo.addAll(getSicaServiceData().findCatalogoTipoRelacion());

	    return new RecordSelectionModel(catalogo, "id", "descripcion");
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo Tipo Relacion.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoEventoRelacion() {
		List catalogo = new ArrayList();
		RegulatorioEventoRelacion opcionDefault = new RegulatorioEventoRelacion();
		opcionDefault.setId(new Integer(-100));
		opcionDefault.setDescripcion("SELECCIONE LA OPCION DESEADA");
		catalogo.add(opcionDefault);
		catalogo.addAll(getSicaServiceData().findCatalogoEventoRelacion());

	    return new RecordSelectionModel(catalogo, "id", "descripcion");
    }
	
    //************************************************************************************************************
	
	/**
     * Obtiene el Modelo a mostrar en el Combo de Nacionalidad.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoNacionalidad() {
	    Vector nacionalidadTmp = getSicaConsultaCatalogosService().obtenCatNacionalidad();
	    List nacionalidades = new ArrayList();
	    Catalogo nacDefault = new Catalogo();
	    nacDefault.setId(String.valueOf(ID_DEFAULT));
	    nacDefault.setDescripcion(OPCION_DEFAULT);
	    nacionalidadTmp.add(0, nacDefault);
	    for (Iterator i = nacionalidadTmp.iterator(); i.hasNext(); ) {
	        Catalogo sector = (Catalogo) i.next();
	        if (getNacionalidad() != null && getNacionalidad().getDescripcion().
	        		equals(sector.getDescripcion())) {
	        	nacionalidades.add(sector);
	        }
	    }
	    for (Iterator i = nacionalidadTmp.iterator(); i.hasNext(); ) {
	        Catalogo sector = (Catalogo) i.next();
	        if (getNacionalidad() == null || !getNacionalidad().getDescripcion().
	        		equals(sector.getDescripcion())) {
	        	nacionalidades.add(sector);
	        }
	    }
	    _logger.debug("Cantidad de nacionalidades: " + nacionalidades.size());
	    
	    return new RecordSelectionModel(nacionalidades, "id", "descripcion");
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo de Paises.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoPaisNacimiento() {
		Vector sectoresTmp = (getVectorPais() == null) ?
				getSicaConsultaCatalogosService().obtenCatpais() : getVectorPais();		
	    List sectores = new ArrayList();
	    Map pais = new HashMap();
	    pais.put(VALUE, String.valueOf(ID_DEFAULT));
	    pais.put(LABEL, OPCION_DEFAULT);
	    sectores.add(pais);
	    for (Iterator i = sectoresTmp.iterator(); i.hasNext();) {
	        Catalogo sector = (Catalogo) i.next();
	        pais = new HashMap();
	        pais.put(VALUE, sector.getId());
		    pais.put(LABEL, sector.getDescripcion());
		    sectores.add(pais);
		}
	    
	    return new RecordSelectionModel(sectores, VALUE, LABEL);
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo de Paises.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoPais() {
		Vector sectoresTmp = (getVectorPais() == null) ?
				getSicaConsultaCatalogosService().obtenCatpais() : getVectorPais();		
	    List sectores = new ArrayList();
	    for (Iterator i = sectoresTmp.iterator(); i.hasNext();) {
	        Catalogo sector = (Catalogo) i.next();
		    if (getPais() != null && getPais().getDescripcion().equals(sector.getDescripcion())) {
		    	sectores.add(sector);
		    	i.remove();
		    }
		}
	    for (Iterator i = sectoresTmp.iterator(); i.hasNext();) {
	        Catalogo sector = (Catalogo) i.next();
	        sectores.add(sector);
	        
	    }
	    return new RecordSelectionModel(sectores, "id", "descripcion");
    }
	
	/**
	 * Gets the catalogo tipo comprobante.
	 *
	 * @return the catalogo tipo comprobante
	 */
	public IPropertySelectionModel getCatalogoTipoComprobante() {
		List tipoDocs = new ArrayList();
		Map item = new HashMap();
		item.put(VALUE, String.valueOf(ID_DEFAULT));
		item.put(LABEL, OPCION_DEFAULT);
		tipoDocs.add(item);
		String idPais = getNacionalidad() != null ? getNacionalidad().getId() : "";
		if (StringUtils.isNotEmpty(idPais) && 
				!idPais.equals(String.valueOf(ID_DEFAULT))) {
			String filtro = "NAC";
			if (!isNacionalidadMexicana()) {
				filtro = "EXT";
			}
			LinkedHashMap tipos = 
					this.getSicaConsultaCatalogosService().obtenCatTipoIdentificacion(filtro);
			for (Iterator i = tipos.keySet().iterator(); i.hasNext();) {
				String idDocumento = (String) i.next();
				TipoComprobante tipo = (TipoComprobante) tipos.get(idDocumento);
				item = new HashMap();
				item.put(VALUE, tipo.getIdTipoComprobante());
				item.put(LABEL, tipo.getDescripcion());
				tipoDocs.add(item);
			}
		}
		
		return new RecordSelectionModel(tipoDocs, VALUE, LABEL);
	}
	
	/**
     * Devuelve el catalago de 
     * divisas para el combo de divisas
     * @return
     */
    public IPropertySelectionModel getCatalogoEmpresa() {
	    
	    List empresasList = getSicaServiceData().findAllEmpresasByOrdenAlfabetico();
//	    Vector sectoresTmp = getSicaConsultaCatalogosService().
//                obtenCatSectorEconomicoBanxico(tipoPersona);
	    List empresas = new ArrayList();
	    Map sectorMap = null;
	    
	    for (Iterator i = empresasList.iterator(); i.hasNext();) {
	    	EmpresaCambios empresa = (EmpresaCambios) i.next();
	        sectorMap = new HashMap();
	        sectorMap.put(VALUE, empresa.getIdEmpresa());
	        sectorMap.put(LABEL, empresa.getDescripcion());
	        
	        empresas.add(sectorMap);
	    }
	    
		return new RecordSelectionModel(empresas, VALUE, LABEL);
	}
    /**
     * Busca un contrato por medio del numero de cuenta sica
     * @param NO_CUENTA
     * @return
     */
    public Contrato getContratoCorto(String NO_CUENTA){
    	List contratos=getSicaServiceData().findContratosByCuenta(NO_CUENTA);
    	Contrato contrato=null;
    	for (Iterator i = contratos.iterator(); i.hasNext();) {
    		contrato = (Contrato) i.next();
    	}
    	return contrato;
	    	
    }
	
    /**
     * Obtiene los nombres de las columnas a mostrar en el Portlet de los clientes encontrados
     * en la BUP, dependiendo si se trata de Personas Morales o F&iacute;sicas y de si el usuario
     * que opera el sistema pertenece o no al Canal IXE Directo. Se establece el número de columnas
     * de dicho Portlet.
     *
     * @return List Los nombres de las columnas.
     */
    public List getNombresColumnasClientes() {
		List nombres = new ArrayList();
		if ("MORAL".equals(getFisicaMoral())) {
			if (isIxeDirecto()) {
				nombres.add("Raz&oacute;n Social");
				nombres.add("RFC");
				nombres.add("Cuentas Eje");
				nombres.add("Ejecutivo");
				nombres.add("Alta R&aacute;pida de Contratos SICA");
				nombres.add("Alta Contrato Corto");
				nombres.add("Modificaci\u00f3n de datos");
				setNoColumnasClientes(Num.I_7);
				//setNoColumnasClientes(Num.I_6);
			}
			else {
				nombres.add("Raz&oacute;n Social");
				nombres.add("RFC");
				nombres.add("Cuentas Eje");
				nombres.add("Alta R&aacute;pida de Contratos SICA");
				nombres.add("Alta Contrato Corto");
				nombres.add("Modificaci\u00f3n de datos");
				setNoColumnasClientes(Num.I_6);
				//setNoColumnasClientes(Num.I_5);
			}
		}
		else {
			if (isIxeDirecto()) {
				nombres.add("Nombre");
				nombres.add("RFC");
				nombres.add("Cuentas Eje");
				nombres.add("Ejecutivo");
				nombres.add("Alta R&aacute;pida de Contratos SICA");
				nombres.add("Alta Contrato Corto");
				nombres.add("Modificaci\u00f3n de datos");
				setNoColumnasClientes(Num.I_7);
				//setNoColumnasClientes(Num.I_6);
			}
			else {
				nombres.add("Nombre");
				nombres.add("RFC");
				nombres.add("Cuentas Eje");
				nombres.add("Alta R&aacute;pida de Contratos SICA");
				nombres.add("Alta Contrato Corto");
				nombres.add("Modificaci\u00f3n de datos");
				setNoColumnasClientes(Num.I_6);
				//setNoColumnasClientes(Num.I_5);
			}
		}
		return nombres;
	}

	/**
	 * Permite saber si el usuario que est&aacute; logeado al sistema pertenece o no al 
	 * Canal Ixe Directo.
	 *
	 * @return boolean.
	 */
	public boolean isIxeDirecto() {
		Visit visit = (Visit) getVisit();
		return visit.isIxeDirecto();
	}

	/**
     * Obtiene el Modelo de las Cuentas Eje que tiene una Persona F&iacute;sica o Moral en la BUP.
     *
     * @param idPersona El identificador de la persona.
     * @return IPropertySelectionModel.
     */
    public String mostrarCuentasEjeByIdPersona(Integer idPersona) {
    	String [] cuentas = getSicaServiceData().findCuentasEje(idPersona);
    	String arregloCompleto = "";
    	for (int i = 0; i < cuentas.length; i++) {
    		if (cuentas[i].length() > 0) {
    			arregloCompleto += cuentas[i] + " | ";
    		}
    	}
    	return arregloCompleto;
    }
    
    private int modificaDatosPersona(PersonaDto persona) {
    	//getDelegate().record("Error al asignar el contrato. Datos faltantes del cliente: " +
    	//					 "RFC.", null);
    	setModoSubmit(5);
    	initDatosPersona(persona);
    	if (persona.isPersonaMoral()) {
    		this.buscarApoderadoDatosRegulatorios(getIdPersona());
    	}
    	setPersonasBUP(new ArrayList());
    	_logger.debug("this.getFisicaMoral(): " + this.getFisicaMoral());
    	_logger.debug("this.getModoSubmit(): " + this.getModoSubmit());
    	
    	return 0;
    }

	/**
	 * Obtiene La lista de Personas en la BUP que concuerdan con los criterios de Alta.
	 * Permite saber si el Cliente que desea darse de Alta ya existe o no en la BUP.
	 *
	 * @return List La lista de personas que concuerdan con los criterios de Alta.
	 */
	public abstract List getPersonasBUP();

	/**
	 * Fija La lista de Personas en la BUP que concuerdan con los criterios de Alta.
	 * Permite saber si el Cliente que desea darse de Alta ya existe o no en la BUP.
	 *
	 * @param personasBUP La lista a fijar.
	 */
	public abstract void setPersonasBUP(List personasBUP);

	/**
	 * Regresa la opcion seleccionada en el combo de Tipos de Persona.
	 *
	 * @return String
	 */
	public abstract String getFisicaMoral();

	/**
	 * Fija la opcion seleccionada en el combo de Tipos de Persona.
	 *
	 * @param fisicaOMoral El valor a asignar.
	 */
	public abstract void setFisicaMoral(String fisicaOMoral);

	/**
	 * Regresa el modo de submit de la pagina. Si es por boton o por combo.
	 *
	 * @return int
	 */
	public abstract int getModoSubmit();

    /**
     * Fija el modo de Sumbit de la pagina. Si ya sea por boton o por combo.
     *
     * @param modoSubmit El valor a asignar.
     */
	public abstract void setModoSubmit(int modoSubmit);

    /**
     * Regresa el valor del campo Apellido Paterno.
     *
     * @return String    Apellido paterno
     */
	public abstract String getPaterno();

    /**
     * Fija el valor del campo Apellido Paterno.
     *
     * @param paterno El valor a asignar.
     */
	public abstract void setPaterno(String paterno);

	/**
     * Regresa el valor del campo Apellido Materno.
     *
     * @return String    Apellido Materno
     */
	public abstract String getMaterno();

    /**
     * Fija el valor del campo Apellido Materno.
     *
     * @param materno El valor a asignar.
     */
	public abstract void setMaterno(String materno);

	/**
     * Regresa el valor del campo Nombre.
     *
     * @return String   Nombre del Cliente
     */
	public abstract String getNombre();

    /**
     * Fija el valor del campo Nombre.
     *
     * @param nombre El valor a asignar.
     */
	public abstract void setNombre(String nombre);

    /**
     * Regresa el valor del campo Razon Social.
     *
     * @return String   Razon Social
     */
	public abstract String getRazonSocial();

    /**
     * Fija el valor del campo Razon Social.
     *
     * @param razonSocial El valor a asignar.
     */
	public abstract void setRazonSocial(String razonSocial);

    /**
     * Obtiene el valor del RFC
     * @return String    RFC del cliente
     */
    public abstract String getRfc();

    /**
     * Fija el valor del campo rfc.
     *
     * @param rfc El valor a asignar.
     */
    public abstract void setRfc(String rfc);
    

    /**
     * Regresa el valor de la propiedad <code>nacionalidad</code>
     * @return String    Nacionalidad
     */
    public abstract Catalogo getNacionalidad();

    /**
     * Asigna el valor de la propiedad <code>nacionalidad</code>
     * @param nacionalidad  Nacionalidad del cliente
     */
    public abstract void setNacionalidad(Catalogo nacionalidad);
    
    /**
     * Regresa el valor de la propiedad <code>fechaNacimiento</code>
     * @return Date  Fecha de Nacimiento
     */
    public abstract Date getFechaNacimiento();
    
    /**
     * Asigna el valor de la propiedad <code>fechaNacimiento</code>
     * @param fechaNacimiento Fecha de Nacimiento como Date
     */
    public abstract void setFechaNacimiento(Date fechaNacimiento);
    
    /**
     * Regresa el valor de la propiedad <code>fechaNacimiento</code>
     * @return Date  Fecha de Nacimiento como String
     */
    public abstract String getFechaNacimientoString();
    
    /**
     * Asigna el valor de la propiedad <code>fechaNacimiento</code>
     * @param fechaNacimientoString Fecha de Nacimiento como String
     */
    public abstract void setFechaNacimientoString(String fechaNacimientoString);
    
    /**
     * @return Boolean    True si la direcc\u00f3 es primaria, False en caso contrario
     */
    public abstract boolean isPrimaria();
    
    /**
     * @param primaria   Propiedad que indica si la dirección es primaria o no.
     */
    public abstract void setPrimaria(boolean primaria);
    
    /**
     * Regresa el valor de la propiedad Calle
     * @return String    Calle del cliente
     */
    public abstract String getCalle();
    
    /**
     * Asigna el valor a la propiedad Calle
     * @param calle    Calle
     */
    public abstract void setCalle(String calle);
    
    /**
     * @return String
     */
    public abstract String getNoInterior();
    
    /**
     * @param noInterior
     */
    public abstract void setNoInterior(String noInterior);
    
    /**
     * @return
     */
    public abstract String getNoExterior();
    
    /**
     * @param noExterior
     */
    public abstract void setNoExterior(String noExterior);
    
    /**
     * @return
     */
    public abstract String getColonia();
    
    /**
     * @param colonia
     */
    public abstract void setColonia(String colonia);
    
    /**
     * @return
     */
    public abstract String getCiudad();
    
    /**
     * @param ciudad
     */
    public abstract void setCiudad(String ciudad);
    
    /**
     * @return
     */
    public abstract String getCodigoPostal();
    
    /**
     * @param codigoPostal
     */
    public abstract void setCodigoPostal(String codigoPostal);
    
    /**
     * @return
     */
    public abstract String getDelegacion();
    
    /**
     * @param delegacion
     */
    public abstract void setDelegacion(String delegacion);
    
    /**
     * @return
     */
    public abstract String getEstado();
    
    /**
     * @param estado
     */
    public abstract void setEstado(String estado);
    
    /**
     * @return
     */
    public abstract Catalogo getPais();
    
//    /**
//     * @return
//     */
//    public abstract Catalogo getDivisa();
    
    /**
     * @return
     */
    public abstract Map getEmpresa();
    
    /**
     * @return
     */
    public abstract void setEmpresa(Map empresa);
    
    /**
     * @return
     */
    public abstract String getNombreEmpresa();
    
    /**
     * @param nombreEmpresa
     */
    public abstract void setNombreEmpresa(String empresa);
    
    /**
     * @param pais
     */
    public abstract void setPais(Catalogo pais);
    
//    /**
//     * @param pais
//     */
//    public abstract void setDivisa(Catalogo divisa);
    
    /**
     * @return
     */
    public abstract HashMap getHashmapColonia();
    
    /**
     * @param colonia
     */
    public abstract void setHashmapColonia(HashMap colonia);
    
    /**
     * @return
     */
    public abstract HashMap getHashmapMunicipio();
    
    /**
     * @param municipio
     */
    public abstract void setHashmapMunicipio(HashMap municipio);
    
    /**
     * @return
     */
    public abstract HashMap getHashmapEntidad();
    
    /**
     * @param entidad
     */
    public abstract void setHashmapEntidad(HashMap entidad);

/**
     * @return
     */
    public abstract Vector getVectorPais();
    
    /**
     * @param pais
     */
    public abstract void setVectorPais(Vector pais);

    /**
     * Regresa el valor de noCuenta.
     *
     * @return String.
     */
    public abstract String getNoCuenta();

    /**
     * Fija el valor del campo noCuenta.
     *
     * @param noCuenta El valor a asignar.
     */
    public abstract void setNoCuenta(String noCuenta);
    
    /**
     * Regresa el objeto <code>Catalogo</code> seleccionado del combo de Sectores Banxico.
     *
     * @return Catalogo
     */
	public abstract Map getSectorBanxico();

    /**
     * Fija el objeto <code>Catalogo</code> con el Sector Banxico seleccionado
     * del combo de Sectores Banxico.
     *
     * @param sectorBanxico El valor a asignar.
     */
	public abstract void setSectorBanxico(Map sectorBanxico);

    /**
     * Nivel de los mensajes al usuario: -1 Error, 0 Warning, 1 Exito.
     *
     * @param level El valor a asignar.
     */
	public abstract void setLevel(int level);

	/**
     * Regresa el valor de <code>noCuentaContratoSica</code>.
     *
     * @return String
     */
    public abstract String getNoCuentaContratoSica();

    /**
     * Fija el valor de <code>noCuentaContratoSica</code>.
     *
     * @param noCuentaContratoSica El valor a asignar.
     */
    public abstract void setNoCuentaContratoSica(String noCuentaContratoSica);

    /**
     * Regresa el valor de <code>noSic</code>.
     *
     * @return String
     */
    public abstract String getNoSic();

    /**
     * Fija el valor de <code>noSic</code>.
     *
     * @param noCuentaContratoSica El valor a asignar.
     */
    public abstract void setNoSic(String noSic);

    /**
     * Regresa el valor de <code>sucursalOrigen</code>.
     *
     * @return String
     */
    public abstract String getSucursalOrigen();

    /**
     * Fija el valor de <code>sucursalOrigen</code>.
     *
     * @param sucursalOpera El valor a asignar.
     */
    public abstract void setSucursalOrigen(String sucursalOrigen);

    /**
     * Regresa el valor de <code>sucursalOpera</code>.
     *
     * @return String
     */
    public abstract String getSucursalOpera();

    /**
     * Fija el valor de <code>sucursalOpera</code>.
     *
     * @param sucursalOpera El valor a asignar.
     */
    public abstract void setSucursalOpera(String sucursalOpera);

    /**
     * Regresa el valor de <code>cuentaCheques</code>.
     *
     * @return String
     */
    public abstract String getCuentaCheques();

    /**
     * Fija el valor de <code>cuentaCheques</code>.
     *
     * @param cuentaCheques El valor a asignar.
     */
    public abstract void setCuentaCheques(String cuentaCheques);
    
    public abstract String getCurp();

	public abstract void setCurp(String curp);

	public abstract String getNumComprobante();

	public abstract void setNumComprobante(String numComprobante);
	
	public abstract Map getTipoComprobante();
	
	public abstract void setTipoComprobante(Map tipoComprobante);
	
	public abstract boolean isNacionalidadMexicana();
	
	public abstract void setNacionalidadMexicana(boolean nacionalidadMexicana);
	
	public abstract String getSexo();
	
	public abstract void setSexo(String sexo);
	
	public abstract Map getPaisNacimiento();
	
	public abstract void setPaisNacimiento(Map paisNacimiento);
	
    /**
     * Regresa el valor de <code>nit</code>.
     * 
     * @return
     */
    public abstract String getNit();
    
    /**
     * Fija el valor de <code>nit</code>.
     * 
     * @param nit
     */
    public abstract void setNit(String nit);


	/**
     * Regresa el valor de <code>cuentaCheques</code>.
     *
     * @return String
     */
    public String contratoCorto;
    
    /**
     * Valida error asignar contrato.
     *
     * @param respuesta the respuesta
     */
    private void validaErrorAsignarContrato(RespuestaDto respuesta) {
    	int codigo = respuesta.getCodigo();
    	SicaException error = null;
    	switch(codigo) {
	    	case RespuestaDto.ERROR_VALIDACION:
	    		setPersonasBUP(new ArrayList());
	    		setActualizarCr(true);
	    		setFlujoAsignarContrato(true);
	    		setModoSubmit(5);
	    		this.initDatosPersona((PersonaDto) respuesta.getObjecto());
	    		error = new SicaValidationException(respuesta.getMensaje());
	    		break;
	    	case RespuestaDto.ERROR_NEGOCIO:
	    		error = new SicaException(respuesta.getMensaje());
	    		break;
	    	case RespuestaDto.ERROR_SISTEMA:
	    		error = new SicaException(respuesta.getMensaje());
	    		break;
    	}
    	
    	throw error;
    }
    
    /**
     * Inits the datos persona.
     *
     * @param perDto the per dto
     */
    private void initDatosPersona(PersonaDto perDto) {
    	Persona persona = perDto.getPersona();
    	setPersona(persona);
    	setRfc(persona.getRfc());
    	setIdPersona(new Integer(persona.getIdPersona()));
    	debug("Se inicializan datos de la persona: " + getIdPersona());
    	if (StringUtils.isNotEmpty(persona.getIdPais())) {
    		Catalogo nacionalidad = new Catalogo();
    		nacionalidad.setId(persona.getIdPais());
    		nacionalidad.setDescripcion(persona.getNacionalidad());
    		setNacionalidadMexicana(perDto.isNacionalidadMexicana());
	    	setNacionalidad(nacionalidad);
	    	cambiaNacionalidad();
    	}
    	if (perDto.getPaisNacimiento() != null) {
    		Map paisNac = new HashMap();
    		paisNac.put(VALUE, perDto.getPaisNacimiento().getId());
    		paisNac.put(LABEL, perDto.getPaisNacimiento().getDescripcion());
    		setPaisNacimiento(paisNac);
    		debug("Pais de nacimiento: " + getPaisNacimiento());
    	}
    	debug("perDto.getFecha(): " + perDto.getFecha());
    	setFechaNacimiento(perDto.getFecha());
    	if (StringUtils.isNotEmpty(perDto.getIdExtranjero())) {
    		setIdExtranjero(perDto.getIdExtranjero());
    	}
    	debug("Id Sector: " + persona.getIdSector());
    	if (persona.getIdSector() > 0) {
    		Map sector = new HashMap();
    		sector.put(VALUE, String.valueOf(persona.getIdSector()));
    		sector.put(LABEL, persona.getDescSector());
    		setSectorBanxico(sector);
    		debug("SectorBanxico: " + getSectorBanxico());
    	}
    	if (persona instanceof PersonaFisica) {
    		PersonaFisica fisica = (PersonaFisica) persona;
    		setFisicaMoral("FISICA");
    		setNombre(fisica.getNombre());
    		setPaterno(fisica.getPaterno());
    		setMaterno(fisica.getMaterno());
    		if (StringUtils.isNotEmpty(fisica.getCurp())) {
    			setCurp(fisica.getCurp());
    		}
    		if (StringUtils.isNotEmpty(fisica.getIdTipoIdentifica())) {
    			Map tipo = new HashMap();
    			tipo.put(VALUE, fisica.getIdTipoIdentifica());
    			tipo.put(LABEL, fisica.getDescTipoIdentifica());
    			setTipoComprobante(tipo);
    		}
    		if (StringUtils.isNotEmpty(fisica.getNoIdentificacion())) {
    			setNumComprobante(fisica.getNoIdentificacion());
    		}
    		if (StringUtils.isNotEmpty(fisica.getSexo())) {
    			setSexo(fisica.getSexo());
    		}
    	}
    	else {
    		PersonaMoral moral = (PersonaMoral) persona;
    		setFisicaMoral("MORAL");
    		setRazonSocial(moral.getRazonSocial());
    	}
    }
    
    /**
     * Actualizar datos persona.
     *
     * @param cycle the cycle
     */
    public void actualizarDatosPersona(IRequestCycle cycle) {
    	String msg = "";
    	PersonaDto perDto = new PersonaDto();
    	Visit visit = (Visit) getVisit();
    	Integer idPersonaUser = visit.getUser().getIdPersona();
    	debug("Modo submit en actualizarDatosPersona(): " + getModoSubmit());
    	try {
    		if (getPersona() != null) {
    			perDto.setIdExtranjero(this.getIdExtranjero());
    			getPersona().setRfc(getRfc());
    			if (getSectorBanxico() != null) {
    				int idSector = Integer.parseInt(getSectorBanxico().get(VALUE) == "SELECCIONE" ?
    	        			"0" : getSectorBanxico().get(VALUE).toString());
    				getPersona().setIdSector(idSector);
    			}
    			getPersona().setIdPais(getNacionalidad().getId());
    			getPersona().setNacionalidad(getNacionalidad().getDescripcion());
    			if (getFechaNacimiento() != null) {
    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");        
    				setFechaNacimientoString(df.format(getFechaNacimiento()));
    			}
		        if ("FISICA".equals(getFisicaMoral())) {
		        	((PersonaFisica) getPersona()).setNombre(getNombre());
		        	((PersonaFisica) getPersona()).setPaterno(getPaterno());
		        	((PersonaFisica) getPersona()).setMaterno(getMaterno());
		        	((PersonaFisica) getPersona()).setFechaNacimiento(getFechaNacimientoString());
		        	((PersonaFisica) getPersona()).setCurp(getCurp());
		        	String tipoComprobante = (String) getTipoComprobante().get(VALUE);
		        	if (!String.valueOf(ID_DEFAULT).equals(tipoComprobante)) {
		        		((PersonaFisica) getPersona()).setIdTipoIdentifica(tipoComprobante);
		        	}
		        	((PersonaFisica) getPersona()).setNoIdentificacion(getNumComprobante());
		        	((PersonaFisica) getPersona()).setSexo(getSexo());
		    	}
		    	else {
		    	    ((PersonaMoral) getPersona()).setRazonSocial(getRazonSocial());
		    	    ((PersonaMoral) getPersona()).setFechaConstitucion(getFechaNacimientoString());
		    	}
		        perDto.setPersona(getPersona());
		        boolean updateOk = this.getPersonaService().actualizarPersona(perDto, idPersonaUser);
		        if (updateOk) {
		        	setFlujoAsignarContrato(false);
		        	setActualizarCr(false);
		        	setPersona(null);
		        	setIdPersona(null);
		        	setModoSubmit(1);//Para buscar.
		        }
    		}
    	}
    	catch (SicaValidationException ex) {
    		msg = "Error al actualizar los datos del cliente: " + ex.getMessage();
    		setLevel(-1);
    		getDelegate().record(msg, null);
    	}
        catch (Exception ex) {
        	msg = "Ocurrió un error, favor de contactar a sistemas: " + ex.getMessage();
            error(ex.getMessage(), ex);
            setLevel(-1);
            getDelegate().record(msg, null);
        }
    }

    /**
     * Fija el valor de <code>noColumnasClientes</code>.
     *
     * @param noColumnasClientes El valor a asignar.
     */
    public abstract void setNoColumnasClientes(int noColumnasClientes);
    
    
    /**
     * Obtiene una referencia al EJB del servicio de Contratacion
     * <code>SicaBusquedaPersonaService</code>.
     *
     * @return SicaBusquedaPersonaService
     */
    private SicaBusquedaPersonaService getSicaBusquedaPersonaService() {
        return (SicaBusquedaPersonaService) getApplicationContext().
                getBean("sicaBusquedaPersonaService");
    }

    /**
     * Obtiene una referencia al EJB del servicio de Contratacion
     * <code>SicaRegistroPersonaService</code>.
     *
     * @return SicaRegistroPersonaService
     */
    private SicaRegistroPersonaService getSicaRegistroPersonaService() {
        return (SicaRegistroPersonaService) getApplicationContext().
                getBean("sicaRegistroPersonaService");
    }

    /**
     * Obtiene una referencia al EJB del servicio de Contratacion
     * <code>SicaRegistroPersonaService</code>.
     *
     * @return SicaRegistroDireccionPersonaService
     */
    private SicaRegistroDireccionPersonaService getSicaRegistroDireccionPersonaService() {
        return (SicaRegistroDireccionPersonaService) getApplicationContext().
                getBean("sicaRegistroDireccionPersonaService");
    }
    
    /**
     * Obtiene una referencia al EJB del servicio de direcciones del sistema de Contratacion
     * <code>SicaConsultaDireccionesPersonaService</code>.
     *
     * @return SicaConsultaDireccionesPersonaService
     */
    private SicaConsultaDireccionesPersonaService getSicaConsultaDireccionesPersonaService() {
        return (SicaConsultaDireccionesPersonaService) getApplicationContext().
                getBean("sicaConsultaDireccionesPersonaService");
    }

    /**
     * Obtiene una referencia al EJB del servicio de Contratacion
     * <code>SicaConsultaCatalogosService</code>.
     *
     * @return SicaConsultaCatalogosService
     */
    private SicaConsultaCatalogosService getSicaConsultaCatalogosService() {
        return (SicaConsultaCatalogosService) getApplicationContext().
                getBean("sicaConsultaCatalogosService");
    }

    /**
     * Obtiene una referencia al EJB del servicio de Contratacion
     * <code>SicaContratacionProductoBancoService</code>.
     *
     * @return SicaContratacionProductoBancoService
     */
    private SicaContratacionProductoBancoService getSicaContratacionProductoBancoService() {
        return (SicaContratacionProductoBancoService) getApplicationContext().
                getBean("sicaContratacionProductoBancoService");
    }
    
    /**
     * Obtiene una referencia al EJB del servicio de Contratacion
     * <code>SicaRegistroRelacionContratacionService</code>.
     *
     * @return SicaRegistroRelacionContratacionService
     */
    private SicaRegistroRelacionContratacionService getSicaRegistroRelacionContratacionService() {
        return (SicaRegistroRelacionContratacionService) getApplicationContext().
                getBean("sicaRegistroRelacionContratacionService");
    }
    
    /**
     * Obtiene una referencia al EJB del servicio de Contratacion
     * <code>SicaConsultaRelacionContratacionService</code>.
     *
     * @return SicaConsultaRelacionContratacionService
     */
    private SicaConsultaRelacionContratacionService getSicaConsultaRelacionContratacionService() {
        return (SicaConsultaRelacionContratacionService) getApplicationContext().
                getBean("sicaConsultaRelacionesPersonaContratacionService");
    }
    
    private InformacionCuentaAltamiraService getInformacionCuentaAltamiraService() {
        return (InformacionCuentaAltamiraService) getApplicationContext().getBean("informacionCuentaAltamiraService");
    }
    
    /**
     * Gets the persona service.
     *
     * @return the persona service
     */
    private PersonaService getPersonaService() {
    	return (PersonaService) getApplicationContext().getBean("personaService");
    }
    
    /**
     * Regresa el valor de showIfe.
     *
     * @return boolean.
     */
    //public abstract boolean getShowIfe();
    
    /**
     * Fija el valor del campo showIfe.
     *
     * @param showIfe El valor a asignar.
     */
    //public abstract void setShowIfe(boolean showIfe);
    
    /**
     * Regresa el valor de showPasaporte.
     *
     * @return boolean.
     */
    //public abstract boolean getShowPasaporte();
    
    /**
     * Fija el valor del campo showPasaporte.
     *
     * @param pasaporte El valor a asignar.
     */
    //public abstract void setShowPasaporte(boolean pasaporte);
    
    /**
     * Regresa el valor de pasaporte.
     *
     * @return String.
     */
    //public abstract String getPasaporte();
    
    /**
     * Fija el valor del campo pasaporte.
     *
     * @param pasaporte El valor a asignar.
     */
    //public abstract void setPasaporte(String pasaporte);
    
    /**
     * Regresa el valor de folioIfe.
     *
     * @return String.
     */
    //public abstract String getFolioIfe();
    
    /**
     * Fija el valor del campo folioIfe.
     *
     * @param folioIfe El valor a asignar.
     */
    //public abstract void setFolioIfe(String folioIfe);
    
    /**
     * Regresa el valor de anioRegistroIfe.
     *
     * @return String.
     */
    //public abstract String getAnioRegistroIfe();
    
    /**
     * Fija el valor del campo anioRegistroIfe.
     *
     * @param anioRegistroIfe El valor a asignar.
     */
    //public abstract void setAnioRegistroIfe(String anioRegistroIfe);
    
    /**
     * Regresa el valor de numeroEmisionIfe.
     *
     * @return String.
     */
    //public abstract String getNumeroEmisionIfe();
    
    /**
     * Fija el valor del campo numeroEmisionIfe.
     *
     * @param numeroEmisionIfe El valor a asignar.
     */
    //public abstract void setNumeroEmisionIfe(String numeroEmisionIfe);
    
    /**
     * Regresa el valor de numeroOcrIfe.
     *
     * @return String.
     */
    //public abstract String getNumeroOcrIfe();
    
    /**
     * Fija el valor del campo numeroEmisionIfe.
     *
     * @param numeroEmisionIfe El valor a asignar.
     */
    //public abstract void setNumeroOcrIfe(String numeroEmisionIfe);
    
    /**
     * Regresa el valor de showInsertDatComp.
     *
     * @return boolean.
     */
    //public abstract boolean getShowInsertDatComp();
    
    /**
     * Fija el valor del campo showInsertDatComp.
     *
     * @param showInsertDatComp El valor a asignar.
     */
    //public abstract void setShowInsertDatComp(boolean showInsertDatComp);
    
    /**
     * Regresa el valor de showCbNacionalidad.
     *
     * @return boolean.
     */
    //public abstract boolean getShowCbNacionalidad();
    
    /**
     * Fija el valor del campo showCbNacionalidad().
     *
     * @param showCbNacionalidad El valor a asignar.
     */
    //public abstract void setShowCbNacionalidad(boolean showCbNacionalidad);
    
    /**
     * Regresa el valor de idPersona.
     *
     * @return Integer.
     */
    public abstract Integer getIdPersona();
    
    /**
     * Fija el valor del campo idPersona.
     *
     * @param idPersona El valor a asignar.
     */
    public abstract void setIdPersona(Integer idPersona);
    
    /**
     * Regresa el valor de nombreCompleto.
     *
     * @return String.
     */
    public abstract String getNombreCompleto();
    
    /**
     * Fija el valor del campo nombreCompleto.
     *
     * @param nombreCompleto El valor a asignar.
     */
    public abstract void setNombreCompleto(String nombreCompleto);

    /**
     * Constante que define el N&uacute;mero M&aacute;ximo de registros que puede regresar un Query.
     */
    public static final int MAX_RESULTS = 51;

    /**
	 * Mensaje a mostrar cuando se exece el maximo de registros en la busque de
	 * clientes
	 */
	public static final String MENSAJE_MAX_RESULTS = "Se encontraron m\u00e1s de {0} " +
			"coincidencias en su b\u00fasqueda del Cliente: ";

    /**
     * Constante que identifica a las Personas BUP sin Contrato SICA.
     */
    public static final String SIN_CONTRATO = "0";

    /**
     * Constante que identifica a las Personas BUP con Contrato SICA y que le corresponden al
     * Usuario (Ejecutivo) que opera el Sistema.
     */
    public static final String CON_CONTRATO = "1";

    /**
     * Constante que identifica a las Personas BUP con Contrato SICA pero que le corresponden a otro
     * Ejecutivo. No al que opera el Sistema.
     */
    public static final String CON_CONTRATO_OTRO_EJECUTIVO = "2";
    
    /**
     * Constante que identifica a las Personas BUP sin Contrato Corto.
     */
    public static final String SIN_CONTRATO_CORTO="3";
    
    /**
     * Constante que identifica a las Personas BUP con Contrato Corto.
     */
    public static final String CON_CONTRATO_CORTO="4";
    
    
    /**
     * Constante que identifica a las Personas BUP con Contrato Corto.
     */
    public static final String SIN_NINGUN_CONTRATO="5";
    
    
    /**
     * Bandera para identificar si el usuario ha buscado al menos en una ocasi&oacute;n al cliente
     * que desea registrar.
     * 
     * @return boolean
     */
    public abstract boolean getAlta();
    
    /**
     * Fija el valor del campo alta.
     *
     * @param alta El valor a asignar.
     */
    public abstract void setAlta(boolean alta);
    
    /**
     * Obtiene el IdPersona del cliente que va a actualizar sus datos de CR
     * @return Integer
     */
    public abstract Integer getIdPersonaCr();
    /**
     * Fija el idPersona del cliente que va a actualizar sus datos de CR
     * @param idPersonaCr
     */
    public abstract void setIdPersonaCr(Integer idPersonaCr);
    
    /**
     * Obtiene el idCliente al que pertenece el contrato corto 
     * @return Integer
     */
    public abstract Integer getIdClienteCr();
    
    /**
     * Fija el valor del idCliente al que pertenece el contrato corto
     * @param idClienteCr
     */
    public abstract void setIdClienteCr(Integer idClienteCr);
    
    /**
     * Obtiene el nombre del cliente que va a actualizar sus datos de CR
     * @return String
     */
    public abstract String getNombreClienteCr();
    
    /**
     * Fija el nombre del cliente que va a actualizar sus datos de CR
     * @param nombreClienteCr
     */
    public abstract void setNombreClienteCr(String nombreClienteCr);
    
    /**
     * Obtiene el valor del campo actualizarCr para indicar que se deben mostrar los campos del bloque CLIENTE BANORTE
     * @return boolean
     */
    public abstract boolean getActualizarCr();
    
    /**
     * Fija el valor del campo actualizarCr para indicar que se deben mostrar los campos del bloque CLIENTE BANORTE
     * @param actualizarCr
     */
    public abstract void setActualizarCr(boolean actualizarCr);
    
    /**
     * Obtiene el valor de la variable errorFaltanDatosCr cuando no se ha capturado el sic
     * @return boolean
     */
    public abstract boolean getErrorFaltaSic();
    
    /**
     * Fija el valor de la variable errorFaltanDatosCr cuando no se ha capturado el sic
     * @param boolean
     */
    public abstract void setErrorFaltaSic(boolean errorFaltaSic);
    
    /**
     * Obtiene el valor de la variable errorFaltaNumCuenta cuando no se ha capturado el numero de cuenta
     * @return boolean
     */
    public abstract boolean getErrorFaltaNumCuenta();
    
    /**
     * Fija el valor de la variable errorFaltaNumCuenta cuando no se ha capturado el numero de cuenta
     * @param boolean
     */
    public abstract void setErrorFaltaNumCuenta(boolean errorFaltaNumCuenta);
    
    /**
     * Obtiene el valor de la variable errorFaltaSucOpera cuando no se ha capturado la sucursal donde opera
     * @return boolean
     */
    public abstract boolean getErrorFaltaSucOpera();
    
    /**
     * Fija el valor de la variable errorFaltaSucOpera cuando no se ha capturado la sucursal donde opera
     * @param boolean
     */
    public abstract void setErrorFaltaSucOpera(boolean errorFaltaSucOpera);
    
    // Campos para el apoderado legal
    
    /**
     * @param apoderadoPaterno
     */
    public abstract void setApoderadoPaterno(String apoderadoPaterno);
    public abstract String getApoderadoPaterno();
    
    /**
     * @param apoderadoMaterno
     */
    public abstract void setApoderadoMaterno(String apoderadoMaterno);
    public abstract String getApoderadoMaterno();
    
   
    /**
     * @param apoderadoNombre
     */
    public abstract void setApoderadoNombre(String apoderadoNombre);
    public abstract String getApoderadoNombre();
    
    // Campo para mostrar u ocultar los campos regulatorios
    
    /**
     * @param verCamposRegulatorios
     */
    public abstract void setVerCamposRegulatorios(boolean verCamposRegulatorios);
    public abstract boolean isVerCamposRegulatorios();
    
    /**
     * @param signosNoValidos
     */
    public abstract void setSignosNoValidos(boolean signosNoValidos);
    public abstract boolean isSignosNoValidos();
    
    // Campos regulatorios
    
    /**
     * @param claveBanxico
     */
    public abstract void setClaveBanxico(String claveBanxico);
    public abstract String getClaveBanxico();
    
    
    /**
     * @param contraparte
     */
    public abstract void setContraparte(String contraparte);
    public abstract String getContraparte();
    
    /**
     * @param tipoSociedadMercantil
     */
    public abstract void setTipoSociedadMercantil(RegulatorioSociedadMercantil tipoSociedadMercantil);
    public abstract RegulatorioSociedadMercantil getTipoSociedadMercantil();
    
    
    /**
     * @param tipoIdentificador
     */
    public abstract void setTipoIdentificador(RegulatorioTipoIdentificador tipoIdentificador);
    public abstract RegulatorioTipoIdentificador getTipoIdentificador();
    
    
    /**
     * @param claveIdentificador
     */
    public abstract void setClaveIdentificador(String claveIdentificador);
    public abstract String getClaveIdentificador();
    
    
    /**
     * @param tipoContraparte
     */
    public abstract void setTipoContraparte(RegulatorioActividadEconomica tipoContraparte);
    public abstract RegulatorioActividadEconomica getTipoContraparte();
    
    
    /**
     * @param claveLei
     */
    public abstract void setClaveLei(String claveLei);
    public abstract String getClaveLei();
    
    
    /**
     * @param sectorIndustrial
     */
    public abstract void setSectorIndustrial(RegulatorioSectorIndustrial sectorIndustrial);
    public abstract RegulatorioSectorIndustrial getSectorIndustrial();
    
    /**
     * @param paisControlContraparte
     */
    public abstract void setPaisControlContraparte(RegulatorioPais paisControlContraparte);
    public abstract RegulatorioPais getPaisControlContraparte();
    
    /**
     * @param paisContraparte
     */
    public abstract void setPaisContraparte(RegulatorioPais paisContraparte);
    public abstract RegulatorioPais getPaisContraparte();
    
    /**
     * @param claveLeiMatrizDirecta
     */
    public abstract void setClaveLeiMatrizDirecta(String claveLeiMatrizDirecta);
    public abstract String getClaveLeiMatrizDirecta();
    
    /**
     * @param public abstract String getClaveLeiMatrizUltima();
     */
    public abstract void setClaveLeiMatrizUltima(String claveLeiMatrizUltima);
    public abstract String getClaveLeiMatrizUltima();
    
    /**
     * @param fechaContraparte
     */
    public abstract void setFechaContraparte(Date fechaContraparte);
    public abstract Date getFechaContraparte();
    
    /**
     * @param tipoRelacion
     */
    public abstract void setTipoRelacion(RegulatorioTipoRelacion tipoRelacion);
    public abstract RegulatorioTipoRelacion getTipoRelacion();
    
    
    /**
     * @param public abstract Integer getGrupoFinanciero();
     */
    public abstract void setGrupoFinanciero(Integer grupoFinanciero);
    public abstract Integer getGrupoFinanciero();
    
    /**
     * @param tipoEventoRelacion
     */
    public abstract void setTipoEventoRelacion(RegulatorioEventoRelacion tipoEventoRelacion);
    public abstract RegulatorioEventoRelacion getTipoEventoRelacion();
    
    /**
     * @param fechaEventoRelacion
     */
    public abstract void setFechaEventoRelacion(Date fechaEventoRelacion);
    public abstract Date getFechaEventoRelacion();
    
    private static final String LABEL = "label";
    private static final String VALUE = "value";
    
	/**
	 * Gets the id extranjero.
	 *
	 * @return the id extranjero
	 */
	public abstract String getIdExtranjero();

	/**
	 * Sets the id extranjero.
	 *
	 * @param idExtranjero the new id extranjero
	 */
	public abstract void setIdExtranjero(String idExtranjero);
	
	/**
	 * Checks if is flujo asignar contrato.
	 *
	 * @return true, if is flujo asignar contrato
	 */
	public abstract boolean isFlujoAsignarContrato();

	/**
	 * Sets the flujo asignar contrato.
	 *
	 * @param flujoAsignarContrato the new flujo asignar contrato
	 */
	public abstract void setFlujoAsignarContrato(boolean flujoAsignarContrato);

	/**
	 * Gets the persona.
	 *
	 * @return the persona
	 */
	public abstract Persona getPersona();

	/**
	 * Sets the persona.
	 *
	 * @param persona the new persona
	 */
	public abstract void setPersona(Persona persona);
	
}
