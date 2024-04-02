package com.ixe.ods.sica.pages;

import java.lang.reflect.UndeclaredThrowableException;
import java.text.MessageFormat;
import java.text.ParseException;
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
import java.util.TreeMap;
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
import com.ixe.bean.bup.MedioContacto;
import com.ixe.bean.bup.Persona;
import com.ixe.bean.bup.PersonaFisica;
import com.ixe.bean.bup.PersonaMoral;
import com.ixe.bean.bup.RelacionPersonas;
import com.ixe.bean.bup.TipoComprobante;
import com.ixe.contratacion.ContratacionException;
import com.ixe.contratacion.excepcion.FaltaDireccionException;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.SicaBusquedaPersonaService;
import com.ixe.ods.sica.SicaConsultaCatalogosService;
import com.ixe.ods.sica.SicaConsultaDireccionesPersonaService;
import com.ixe.ods.sica.SicaConsultaMediosContactoPersonaService;
import com.ixe.ods.sica.SicaConsultaRelacionContratacionService;
import com.ixe.ods.sica.SicaContratacionProductoBancoService;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaRegistroDireccionPersonaService;
import com.ixe.ods.sica.SicaRegistroMedioContactoContratacionService;
import com.ixe.ods.sica.SicaRegistroPersonaService;
import com.ixe.ods.sica.SicaRegistroRelacionContratacionService;
import com.ixe.ods.sica.SicaValidationException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.dto.InfoCuentaAltamiraDto;
import com.ixe.ods.sica.model.Cliente;
import com.ixe.ods.sica.model.Contrato;
import com.ixe.ods.sica.model.EmpresaCambios;
import com.ixe.ods.sica.model.NitPersona;
import com.ixe.ods.sica.model.ParametroSica;
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
import com.ixe.ods.sica.services.InformacionCuentaAltamiraService;
import com.ixe.ods.sica.services.PersonaService;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.util.PropiedadesMC;
import com.ixe.util.Utilerias;
import com.legosoft.tapestry.model.RecordSelectionModel;

public abstract class ClientesAreaContratos extends SicaPage 
{
	private static final String OPCION_DEFAULT = "SELECCIONE LA OPCION DESEADA";
	
	private static final Integer ID_DEFAULT = new Integer(-100); 
	
	/**
     * Limpia variables y precarga informacion cada que se activa la P&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
    	
    	super.activate(cycle);
    	
    	setSeccion(VER_SECCION_BUSQUEDA);
    	setSubSeccion("");
    	
    	setPersonasBUP(new ArrayList());
    	limpiarCamposBusqueda();
    	limpiarCampos();
        setFisicaMoral("MORAL");
        //setActividadEconomicaModel(new RecordSelectionModel(new ArrayList(), VALUE, LABEL));
        setIdPersonaApoderado(null);
        setIdPersona(null);
        setIdDireccionCliente(null);
        setModoActualizar(false);
        setModoBusqueda(false);
        limpiarCamposCliente();
    }

    public void limpiarCamposBusqueda()
    {
    	setRfc("");
    	setNoCuenta("");
    	setPaterno("");
    	setMaterno("");
    	setNombre("");
    	setRazonSocial("");
    	setSexo("");
    	setModoActualizar(false);
    }
    
    public void limpiarCamposCliente()
    {
    	setFaltaNombre(false);
        setFaltaPaterno(false);
        setFaltaMaterno(false);
        setFaltaSector(false);
        setFaltaGiro(false);
        setFaltaActividad(false);
        setFaltaNacionalidad(false);
        setFaltaRfc(false);
        setFaltaFechaNacimiento(false);
        setFaltaRazonSocial(false);
        setFaltaNombreApoderado(false);
        setFaltaPaternoApoderado(false);
        setFaltaMaternoApoderado(false);
        setFaltaNombreContraparte(false);
        setFaltaSociedadMercantil(false);
        setFaltaIdentificadorContraparte(false);
        setFaltaClaveContraparte(false);
        setFaltaTipoContraparte(false);
        setFaltaClaveLei(false);
        setFaltaSectorIndustrial(false);
        setFaltaPaisControl(false);
        setFaltaPaisContraparte(false);
        setFaltaClaveLeiDirecta(false);
        setFaltaClaveLeiUltima(false);
        setFaltaFechaContraparte(false);
        setFaltaTipoRelacion(false);
        setFaltaGrupoFinanciero(false);
        setFaltaEventoRelacion(false);
        setFaltaFechaRelacion(false);
        setFaltaCalle(false);
        setFaltaNoExterior(false);
        setFaltaNoInterior(false);
        setFaltaCodigoPostal(false);
        setFaltaColonia(false);
        setFaltaCiudad(false);
        setFaltaDelegacion(false);
        setFaltaEstado(false);
        setFaltaTipoComprobante(false);
        setFaltaNoComprobante(false);
        setFaltaFechaExpedicion(false);
        setFaltaEmail(false);
        setFaltaCurp(false);
        setFaltaIdExtranjero(false);
        setFaltaTipoDocumento(false);
        setFaltaNumDocumento(false);	
        //60057-CFDI 3.3
        setFaltaNit(false);
        
    }
    
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
	    setClavesReferencia(new HashMap());
	    setVerLupaBuscarCodigoPostal(true);
	    
	    setCurp("");
	    setNumDocumento("");
	    setNacionalidadMexicana(true);
	    setTipoDocumento(null);	
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
	    //60057-CFDI 3.3
	    setNit("");
	}

	/**
     * Llama a <code>limpiarCampos()</code>.
     *
     * @param cycle El Ciclo de la P&aacute;gina.
     */
    public void cambiarTipoPersona(IRequestCycle cycle) 
    {
        limpiarCamposBusqueda();
        setModoBusqueda(false);
        setModoActualizar(false);
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** Define la clave de la porpiedad peso para el calculo del digito verificador. */
    private static final String PESO       = "MC_PERSONA_PESO_DV";
    
    /** Define la clave de la propiedad alineacion(Izq-Der) 
     * para el calculo del digito verificador. 
     */
    private static final String ALINEACION = "MC_PERSONA_ALINEACION_DV";
    
    /** Define la clave de la propiedad operacion inicial para el calculo del digito verificador. */
    private static final String OPER_INI   = "MC_PERSONA_OPER_INI_DV";
    
    /** Define la clave de la propiedad operacion final para el calculo del digito verificador. */
    private static final String OPER_FIN   = "MC_PERSONA_OPER_FIN_DV";
    
    /** Define la clave de la propiedad modulo para el calculo del digito verificador. */
    private static final String MODULO     = "MC_PERSONA_MODULO_DV";
    
    /** Define la clave de la propiedad digito resta para el calculo del digito verificador. */
    private static final String DIGITO_RESTA = "MC_PERSONA_DIGITO_RESTA_DV";
    
    /** Define el tipo de operacion suma. */
    private static final int SUMA = 1;
    
    /** Define el tipo de operacion izquierda. */
    private static final int IZQUIERDA = 2;
    
    /** Define el tipo de operacion derecha. */
    private static final int DERECHA   = 3;
    
    /** Define el modulo 10 para calculo del digito verificador. */
    private static final int MODULO_10 = 10;
    
    /** Define el modulo 11 para calculo del digito verificador. */
    private static final int MODULO_11 = 11;
    
    /** Define el modulo 97 para calculo del digito verificador. */
    private static final int MODULO_97 = 97;
    
    public void consultarClaveReferencia(IRequestCycle cycle)
    {
    	Integer idPersona = new Integer((cycle.getServiceParameters()[0]).toString());
    	String claveReferencia = getClaveReferencia(idPersona);
    	Map datos = null;
    	Visit visit = (Visit) getVisit();
    	
    	setIdPersona(idPersona);
    	
    	if(getClavesReferencia() == null)
    		setClavesReferencia(new HashMap());
    	
    	if(StringUtils.isEmpty(claveReferencia) || "".equals(claveReferencia.trim()))
    	{
    		claveReferencia = generaReferenciaPersona(idPersona);
    		if(StringUtils.isEmpty(claveReferencia))
    		{
    			setLevel(-1);
    			getDelegate().record("Ocurri\u00F3 un error al generar la referencia del cliente. ", null);
    		}
    		else
    		{
    			datos = new HashMap();
    			datos.put("idPersona", idPersona.toString());
    			datos.put("claveReferencia", claveReferencia);
    			datos.put("status", Constantes.STATUS_CUENTA_ACTIVA);
    			datos.put("idUsuario", visit.getUser().getIdPersona().toString());
    			if(getSicaServiceData().insertarClaveReferenciaCliente(datos))
    			{
    				if(!getClavesReferencia().containsKey(idPersona))
    	    			getClavesReferencia().put(idPersona,claveReferencia);
    			}
    			else
    			{
    				setLevel(-1);
        			getDelegate().record("Ocurri\u00F3 un error al almacenar la referencia del cliente. ", null);
    			}
    		}
    	}
    	else
    	{
    		if(!getClavesReferencia().containsKey(idPersona))
    			getClavesReferencia().put(idPersona,claveReferencia);
    	}
    }
    
    /**
     * Genera referencia persona.
     * 
     * @param idPersona the id persona
     * 
     * @return the string
     */
    private String generaReferenciaPersona(Integer idPersona) {
        String cveReferencia = null;
        
        try {
            //Obtener las propiedades para calculo del digito verificador.
            PropiedadesMC props = new PropiedadesMC();
            String peso = props.obtenValorPropiedad(PESO);
            Integer alineacion = new Integer(props.obtenValorPropiedad(ALINEACION));
            Integer operIni = new Integer(props.obtenValorPropiedad(OPER_INI));
            Integer operFin = new Integer(props.obtenValorPropiedad(OPER_FIN));
            Integer  modulo = new Integer(props.obtenValorPropiedad(MODULO));
            Integer digitoResta = new Integer(props.obtenValorPropiedad(DIGITO_RESTA));
            String strIdPersona = idPersona.toString();
            String digito = calculaDigitoVerificador(strIdPersona, peso, alineacion, operIni,
                                                     operFin, modulo, digitoResta);
            if (digito != null) { 
                cveReferencia = strIdPersona + digito;
                if (cveReferencia.length() < 10) {
                    while (cveReferencia.length() < 10) {
                        cveReferencia = "0" + cveReferencia; //Rellenar de ceros.
                    }
                }
            }
        }
        catch (NumberFormatException ne) {
            //log.error("Error en el formato de las propiedades generaReferenciaPersona", ne);
        }
        
        return cveReferencia;
    }
    
    /**
     * Calcula digito verificador.
     * 
     * @param idPersona the id persona
     * @param peso the peso
     * @param alineacion the alineacion
     * @param operInicial the oper inicial
     * @param operFinal the oper final
     * @param moduloSelec the modulo selec
     * @param digitoResta the digito resta
     * 
     * @return the string
     */
    private String calculaDigitoVerificador(String idPersona, String peso, Integer alineacion,
                                            Integer operInicial, Integer operFinal, 
                                            Integer moduloSelec, Integer digitoResta) {
        String digito = null;
        int longPeso = peso.length();
        int longContrato = idPersona.length(); 
        int j = 0;
        String pesoRep = "";
        int sumatoria = 0;       
        int sumaPesoCont = 0;
        int pesoIndex = 0;
        int contratoIndex = 0;
        int modulo = 0;
        
        String numAux="";     
        
        try {
            pesoRep = llenaPesoLogitudContrato(idPersona, peso, alineacion);
            if (longPeso <= longContrato) {
                for (int i = 0 ; i < longContrato ; i++) {
                    numAux = idPersona.charAt(i) + ""; //contrato
                    contratoIndex = Integer.parseInt(numAux);
                    numAux = pesoRep.charAt(i) + ""; // peso
                    pesoIndex = Integer.parseInt(numAux);
                    if (operInicial.intValue() == 1) {//Suma
                        sumaPesoCont = contratoIndex + pesoIndex;
                    }   
                    else {//Multiplicacion
                        sumaPesoCont = contratoIndex * pesoIndex;
                    }
                        
                    if (sumaPesoCont > 9) {
                        sumaPesoCont = recursivoDigitos(sumaPesoCont + "", operFinal);
                    }
                        sumatoria+=sumaPesoCont;
                }
                switch (moduloSelec.intValue()) {
                    case MODULO_10:
                        modulo = sumatoria % MODULO_10;
                    break;
                    case MODULO_11:
                        modulo = sumatoria % MODULO_11;
                    break;
                    case MODULO_97:
                        modulo = sumatoria % MODULO_97;
                    break;
                }              
                
                if (modulo > 9) { 
                  modulo = recursivoDigitos(modulo + "", operFinal);
                }
                modulo = modulo - digitoResta.intValue();
                //si en la resta se obtine numero negativo se le aplica el valor absoluto.
                modulo = Math.abs(modulo);
                //para tomar siempre el digito de la derecha como digito verificador.
                modulo = modulo % 10;
                digito = modulo + "";
            }
           
        }
        catch(NumberFormatException ne){
            //log.error("Error en el formato de las propiedades calculaDigitoVerificador", ne);
        }
     
        return digito;
    }
    
    /**
     * Llena peso logitud contrato.
     * 
     * @param contrato the contrato
     * @param peso the peso
     * @param alineacion the alineacion
     * 
     * @return the string
     */
    private String llenaPesoLogitudContrato(String contrato, String peso, Integer alineacion) {
        int longPeso = peso.length();
        int longContrato = contrato.length();
        int j = 0;
        String cadenaPeso = "";
        for (int i = 0 ; i < longContrato ; i++) {
            if (j >= longPeso) {
                j = 0;
            }
            if (alineacion.intValue() == 1) {
                cadenaPeso = cadenaPeso + peso.charAt(j);
            }
            else {
                cadenaPeso = peso.charAt(j) + cadenaPeso;
            }
            j = j + 1;
            
        }
             
        return cadenaPeso;
    }
    
     /**
      * Recursivo digitos.
      * 
      * @param valor the valor
      * @param operFinal the oper final
      * 
      * @return the int
      */
     private int recursivoDigitos(String valor, Integer operFinal) {
        if (valor.length() == 1) {
            return Integer.parseInt(valor);
        }
        else{
            int dig1 = Integer.parseInt(valor.charAt(0) + "");
            int dig2 = Integer.parseInt(valor.charAt(1) + "");
            int suma = 0;
            switch (operFinal.intValue()) {
                case SUMA://Se suman
                    suma = dig1 + dig2;
                break;
                case IZQUIERDA://Se toma el valor de la izquierda
                    suma = dig1;
                break;
                case DERECHA://Se toma el valor de la derecha
                    suma = dig2;
                break;
            }
                
            return recursivoDigitos(suma + "", operFinal);            
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void consultarMediosContacto(IRequestCycle cycle)
    {
    	Integer idPersona = new Integer((cycle.getServiceParameters()[0]).toString());
    	String nombreCompleto = (cycle.getServiceParameters()[1]).toString();
    	String rfc = (cycle.getServiceParameters()[2]).toString();
    	
    	List mediosContacto = new ArrayList();
    	List mediosContactoTemp = new ArrayList();
    	Map medioMap = new HashMap();
    	MedioContacto medioContacto = null;
    	String factElectronica = null;
    	boolean agregar = false;
    	
    	try
    	{
    		setRfc(rfc);
        	setRazonSocial(nombreCompleto);
        	setMediosContacto(mediosContacto);
        	setIdPersona(idPersona);
        	
        	mediosContactoTemp = getSicaConsultaMediosContactoPersonaService().obtenMediosContacto(idPersona.intValue());
    		if(mediosContactoTemp != null && mediosContactoTemp.size() > 0)
    		{
    			for(int indice = 0; indice < mediosContactoTemp.size(); indice++)
    			{
    				agregar = false;
    				medioContacto = (MedioContacto)mediosContactoTemp.get(indice);
    				
    				if(StringUtils.isNotEmpty(medioContacto.getIdTipoMedioContacto()))
    				{
    					if("0".equals(medioContacto.getIdTipoMedioContacto()) || "1".equals(medioContacto.getIdTipoMedioContacto()) ||
    					   "3".equals(medioContacto.getIdTipoMedioContacto()) || "4".equals(medioContacto.getIdTipoMedioContacto())	||
    					   "5".equals(medioContacto.getIdTipoMedioContacto()) || "700".equals(medioContacto.getIdTipoMedioContacto()) ||
    					   "710".equals(medioContacto.getIdTipoMedioContacto()) || "800".equals(medioContacto.getIdTipoMedioContacto()) ||
    					   "810".equals(medioContacto.getIdTipoMedioContacto()))
    					{
    						agregar = true;
    					}
    				}
    				else if(StringUtils.isNotEmpty(medioContacto.getDescTipoMedioContacto()) && 
    				   (medioContacto.getDescTipoMedioContacto().toUpperCase().indexOf("EMAIL") != -1 || 
    				    medioContacto.getDescTipoMedioContacto().toUpperCase().indexOf("CORREO") != -1))
    				{
    					agregar = true;
    				}
    				
    				if(agregar)
    				{
    					medioMap = new HashMap(); // email   factElectronica
    					medioMap.put("email", StringUtils.isEmpty(medioContacto.getValor()) ? "" : medioContacto.getValor());
    					
    					if(medioContacto.getEsMedConValidoFE() != null)
    						factElectronica = medioContacto.getEsMedConValidoFE().booleanValue() ? "SI" : "NO";
    					else
    						factElectronica = "NO";
    					
    					medioMap.put("factElectronica", factElectronica);
    					medioMap.put("idMedioContacto", medioContacto.getIdMedioContacto() + "");
    					medioMap.put("idPersona",idPersona.toString());
    					medioMap.put("rfc",rfc);
    					medioMap.put("nombreCompleto",nombreCompleto);
    					
    					mediosContacto.add(medioMap);
    					
    				}
    				
    				/*  VALORES DE LA BUP
    				 *  0	CORREO INTERNO IXE
						1	CORREO REGISTRADO EN IXENET
						3	CORREO ELECTRONICO
						4	CORREO ELECTRONICO ALERTAS
						5	CORREO ELECTRONICO TRABAJO
						700	Correos Internos, NO IXE
						710	Correos Internos, NO IXE, Cliente
						800	Correos Internos, Inactivo
						810	Correos Internos, Cliente
    				 */
    			}
    			
    			setSeccion(VER_SECCION_MEDIOS_CONTACTO);
    			setSubSeccion(VER_SECCION_RESULTADO_MEDIOS);
    		}
    		else 
    		{
    			setMediosContacto(new ArrayList());
    			setLevel(0); // alerta warning
    			getDelegate().record("El cliente no tiene medios de contacto registrados en el sistema. ", null);
    			setSeccion(VER_SECCION_MEDIOS_CONTACTO);
    			setSubSeccion(VER_SECCION_RESULTADO_MEDIOS);
    		}
    	}
    	catch(ContratacionException e)
    	{
    		e.printStackTrace();
    		setRfc("");
        	setRazonSocial("");
			setMediosContacto(new ArrayList());
			setLevel(-1); // errer
			getDelegate().record("Ha ocurrido un error al consulta los medios de contacto del cliente. ", null);
    	}
    }
    
    public void mostrarMedioContacto(IRequestCycle cycle)
    {
    	int idMedioContacto = Integer.parseInt((cycle.getServiceParameters()[0]).toString());
    	int idPersona = Integer.parseInt((cycle.getServiceParameters()[1]).toString());
    	String nombreCompleto = (cycle.getServiceParameters()[2]).toString();
    	String rfc = cycle.getServiceParameters()[3].toString();
    	String email = cycle.getServiceParameters()[4].toString();
    	String esFacturacionElectonica = cycle.getServiceParameters()[5].toString();
    	
    	setRfc(rfc);
    	setRazonSocial(nombreCompleto);
    	setEmail(email);
    	setIdPersona(new Integer(idPersona));
    	setIdMedioContacto(new Integer(idMedioContacto));
    	
    	if("SI".equals(esFacturacionElectonica))
    		setFacturacionElectronica(true);
    	else
    		setFacturacionElectronica(false);
    	
    	setSeccion(VER_SECCION_MEDIOS_CONTACTO);
		setSubSeccion(VER_SECCION_MODIFICAR_AGREGAR_MEDIO);
    }
    
    public void guardarMedioContacto(IRequestCycle cycle)
    {
    	SicaRegistroMedioContactoContratacionService service = null;
    	MedioContacto medioContacto = null;
    	Visit visit = (Visit) getVisit();
    	Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().getIdPersona().intValue());
    	Persona cliente = getSicaBusquedaPersonaService().buscaPersonaPorId(getIdPersona().intValue());
    	StringBuffer errores = new StringBuffer();
    	
    	try
    	{
    		service = getSicaRegistroMedioContactoContratacionService();
    		
    		if(validarCamposMedioContacto(errores))
    		{
    			if(getIdMedioContacto() != null) // actualizacion
        		{
        			List mediosContactoTemp = getSicaConsultaMediosContactoPersonaService().obtenMediosContacto(getIdPersona().intValue());
            		if(mediosContactoTemp != null && mediosContactoTemp.size() > 0)
            		{
            			for(int indice = 0; indice < mediosContactoTemp.size(); indice++)
            			{
            				medioContacto = (MedioContacto)mediosContactoTemp.get(indice);
            				if(medioContacto.getIdMedioContacto() == getIdMedioContacto().intValue())
            					break;
            			}
            		}
            		
            		if(medioContacto != null)
            		{
            			medioContacto.setValor(getEmail());
            			medioContacto.setMedconFE(isFacturacionElectronica() ? "S" : "N");
            			service.modificaMedioContacto(medioContacto, usuario, null);
            		}
            		
            		setLevel(1);
        			getDelegate().record("Se ha actualizado exitosamente el medio de contacto del cliente. ", null);
        		}
        		else // alta
        		{
        			medioContacto = new MedioContacto();
        			medioContacto.setIdTipoMedioContacto(Constantes.TIPO_MEDIO_CONTACTO_EMAIL);
        			//medioContacto.setDescTipoMedioContacto("CORREO ELECTRONICO");
        			medioContacto.setValor(getEmail().trim());
        			medioContacto.setMedconFE(isFacturacionElectronica() ? "S" : "N");
        			service.registraMedioContacto(cliente, medioContacto, usuario);
        			
        			setLevel(1);
        			getDelegate().record("Se ha registrado exitosamente el medio de contacto del cliente. ", null);
        		}
    		}
    		else
    		{
    			setLevel(-1);
    			getDelegate().record(errores.toString(), null);
    		}
    	}
    	catch(ContratacionException e)
    	{
    		e.printStackTrace();
    		setLevel(-1);
			getDelegate().record("Ha ocurrido un error al modificar o registrar el medio de contacto del cliente. ", null);
    	}
    }
    
    public void prepararAltaMedioContacto(IRequestCycle cycle)
    {
    	setEmail("");
    	setFacturacionElectronica(false);
    	setIdMedioContacto(null);
    	
    	setSeccion(VER_SECCION_MEDIOS_CONTACTO);
		setSubSeccion(VER_SECCION_MODIFICAR_AGREGAR_MEDIO);
    }
    
    private boolean validarCamposMedioContacto(StringBuffer errores)
    {
    	boolean camposValidos = true;
    	String patronEmail = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*([;]\\s*\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)*";
    	
    	setFaltaEmail(true);
    	if(StringUtils.isEmpty(getEmail()) || "".equals(getEmail().trim()))
    		errores.append("El campo email no debe estar vac\u00EDo. ");
    	else if(!getEmail().matches(patronEmail))
    		errores.append("El email proporcionado no es v\u00E1lido. ");
    	else
    		setFaltaEmail(false); 
    	
    	if(errores.length() > 0)
    		camposValidos = false;
    	
    	return camposValidos;
    }
    
    public void eliminarMedioContacto(IRequestCycle cycle)
    {
    	int idMedioContacto = Integer.parseInt((cycle.getServiceParameters()[0]).toString());
    	int idPersona = Integer.parseInt((cycle.getServiceParameters()[1]).toString());
    	String nombreCompleto = (cycle.getServiceParameters()[2]).toString();
    	String rfc = cycle.getServiceParameters()[3].toString();
    	SicaRegistroMedioContactoContratacionService service = null;
    	MedioContacto medioContacto = null;
    	Visit visit = (Visit) getVisit();
    	Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().getIdPersona().intValue());
    	
    	Object parametros[] = new Object[]{idPersona + "", nombreCompleto, rfc};
    	
    	try
    	{
    		service = getSicaRegistroMedioContactoContratacionService();
			medioContacto = new MedioContacto();
			medioContacto.setIdMedioContacto(idMedioContacto);
			service.borraMedioContacto(medioContacto, usuario);
			setIdMedioContacto(null);
			cycle.setServiceParameters(parametros);
			setLevel(1);
			getDelegate().record("Se ha eliminado exitosamente el registro seleccionado. ", null);
			consultarMediosContacto(cycle);
    	}
    	catch(ContratacionException e)
    	{
    		e.printStackTrace();
    		setLevel(-1);
			getDelegate().record("Ha ocurrido un error al intentar eliminar el registro seleccionado. ", null);
    	}
    }
    
    
    public void consultarDireccionesCliente(IRequestCycle cycle)
    {
    	Integer idPersona = new Integer((cycle.getServiceParameters()[0]).toString());
    	String nombreCompleto = (cycle.getServiceParameters()[1]).toString();
    	String rfc = (cycle.getServiceParameters()[2]).toString();
    	
    	Map row = null;
    	List direcciones = null;
    	TreeMap idsNoRepetidos = new TreeMap();
    	Visit visit = (Visit) getVisit();
    	//Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().getIdPersona().intValue());
    	Direccion direccion = null;
    	Integer idDireccionConsultado = null;

    	//getBupServiceData().findPersonaByIdPersona(idPersona);
    	Persona cliente = getSicaBusquedaPersonaService().buscaPersonaPorId(idPersona.intValue());
    	direcciones = (List)getSicaConsultaDireccionesPersonaService().obtenDirecciones(cliente, Constantes.ID_RES_ARRAY_LIST);
    	setRfc(rfc);
    	setRazonSocial(nombreCompleto);
    	setIdPersona(idPersona);
    	
    	if(direcciones != null && direcciones.size() > 0)
    	{
        	setDireccionesCliente(new ArrayList());
        	
        	for(int indice = 0; indice < direcciones.size(); indice++)
        	{
        		direccion = (Direccion)direcciones.get(indice);
        		idDireccionConsultado = new Integer(direccion.getIdDireccion());
        		
        		if(!idsNoRepetidos.containsKey(idDireccionConsultado))
        		{
        			idsNoRepetidos.put(idDireccionConsultado, idDireccionConsultado);
        			row = new HashMap();
            		row.put("calle", direccion.getCalle());
            		row.put("noExt", direccion.getNumeroExterior());
            		row.put("noInt", direccion.getNumeroInterior());
            		row.put("colonia", direccion.getColonia());
            		row.put("delegacion", direccion.getDelegacion());
            		row.put("estado",direccion.getEntFederativa() );
            		row.put("codigoPostal", direccion.getCodigoPostal());
            		row.put("idDireccion", direccion.getIdDireccion() + "");
            		row.put("rfc", rfc);
            		row.put("nombreCompleto", nombreCompleto);
            		row.put("idPersona", idPersona.intValue() + "");
            		getDireccionesCliente().add(row);
        		}
        		else
        			idDireccionConsultado = null;
        	}
        	
    		setSeccion(VER_SECCION_DIRECCION);
			setSubSeccion(VER_SECCION_RESULTADOS_DIRECCION);
    	}
    	else 
		{
			setModoBusqueda(false);
			setDireccionesCliente(new ArrayList());
			getDelegate().record("El cliente no tiene registros de direcci\u00F3n en el sistema.", null);
			setSeccion(VER_SECCION_DIRECCION);
			setSubSeccion(VER_SECCION_RESULTADOS_DIRECCION);
		}
    	
    	idsNoRepetidos = null;
    	idDireccionConsultado = null;
    }
    
    public void cambiarPais(IRequestCycle cycle)
    {
    	Map paisMap = getPais();
    	if(paisMap != null && paisMap.get(VALUE) != null && "48".equals(paisMap.get((VALUE))))
    	{
    		/*try
    		{*/
    			setVerLupaBuscarCodigoPostal(true);
    			/*setFaltaCalle(false);
    	        setFaltaNoExterior(false);
    	        setFaltaNoInterior(false);
    	        setFaltaCodigoPostal(false);
    	        setFaltaColonia(false);
    	        setFaltaCiudad(false);
    	        setFaltaDelegacion(false);
    	        setFaltaEstado(false);
    	        setFaltaTipoComprobante(false);
    	        setFaltaNoComprobante(false);
    	        setFaltaFechaExpedicion(false);*/
    			/*if(StringUtils.isNotEmpty(getCodigoPostal()))
    				direccionCP(getCodigoPostal());*/
    		/*}
    		catch(ContratacionException e)
    		{
    			e.printStackTrace();
    		}*/
    	}
    	else
    	{
    		setColonia("");
    		setDelegacion("");
    		setEstado("");
    		setCiudad("");
    		setHashmapColonia(null);
			setHashmapMunicipio(null);
			setHashmapEntidad(null);
			setVerLupaBuscarCodigoPostal(false);
    	}
    }
    
    public void mostrarDireccionCliente(IRequestCycle cycle)
    {
    	int idDireccion = Integer.parseInt((cycle.getServiceParameters()[0]).toString());
    	int idPersona = Integer.parseInt((cycle.getServiceParameters()[1]).toString());
    	String nombreCompleto = (cycle.getServiceParameters()[2]).toString();
    	String rfc = (cycle.getServiceParameters()[3]).toString();
    	
    	Visit visit = (Visit) getVisit();
    	Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().getIdPersona().intValue());
    	Direccion direccion = null;
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	Map paisMap = null;
    	
    	try
    	{
    		setIdPersona(new Integer(idPersona));
    		direccion = getSicaConsultaDireccionesPersonaService().obtenDireccion(usuario, idDireccion);
    		setIdDireccionCliente(new Integer(idDireccion));
    		setCalle(StringUtils.isEmpty(direccion.getCalle()) ? "" : direccion.getCalle().trim());
    		setNoExterior(StringUtils.isEmpty(direccion.getNumeroExterior()) ? "" : direccion.getNumeroExterior().trim());
    		setNoInterior(StringUtils.isEmpty(direccion.getNumeroInterior()) ? "" : direccion.getNumeroInterior().trim());
    		setPrimaria(StringUtils.isEmpty(direccion.getIdTipoDireccion()) ? false : 
    					("1".equals(direccion.getIdTipoDireccion()) ? true : false));
    		setCodigoPostal(StringUtils.isEmpty(direccion.getCodigoPostal()) ? "" : direccion.getCodigoPostal().trim());
    		setColonia(StringUtils.isEmpty(direccion.getColonia()) ? "" : direccion.getColonia().trim());
    		setCiudad(StringUtils.isEmpty(direccion.getCiudad()) ? "" : direccion.getCiudad().trim());
    		setDelegacion(StringUtils.isEmpty(direccion.getDelegacion()) ? "" : direccion.getDelegacion().trim());
    		setEstado(StringUtils.isEmpty(direccion.getEntFederativa()) ? "" : direccion.getEntFederativa().trim());
    		setFacturacionElectronica(StringUtils.isEmpty(direccion.getEsFiscal()) ? false : 
    			("S".equals(direccion.getEsFiscal()) ? true : false));
    		
    		//getTipoComprobanteDireccion().get(VALUE)
    		if(direccion.getTipoComprobante() != null && StringUtils.isNotEmpty(direccion.getTipoComprobante().getIdTipoComprobante()))
    		{
    			TipoComprobante tc = null;
            	String tipoPersona = "MORAL".equals(getFisicaMoral()) ? Constantes.ID_TIPO_PERSONA_MORAL : Constantes.ID_TIPO_PERSONA_FISICA;
        	    LinkedHashMap comprobantesMap = getSicaConsultaCatalogosService().obtenCatTipoComprobante(tipoPersona);
        	    String idDocumento = null;
        	    
        	    for (Iterator i = comprobantesMap.keySet().iterator(); i.hasNext(); ) 
        	    {
        	    	idDocumento = (String) i.next();
        	    	tc = (TipoComprobante)comprobantesMap.get(idDocumento);
        	    	
        	    	if(idDocumento.equals(direccion.getTipoComprobante().getIdTipoComprobante()))
        	    	{
        	    		Map tcMap = new HashMap();
        	    		tcMap.put(VALUE, tc.getIdDocumento());
        	    		tcMap.put(LABEL, tc.getDescripcion());
        	    		
        	    		setTipoComprobanteDireccion(tcMap);
        	    		setNumeroComprobante(StringUtils.isNotEmpty(direccion.getNoComprobante()) ? direccion.getNoComprobante(): "");
        	    		
        	    		if(direccion.getFechaExpedicionComprobante() != null)
        	    		{
        	    			try
        	    			{
        	    				setFechaComprobante(sdf.parse(direccion.getFechaExpedicionComprobante()));
        	    			}
        	    			catch(ParseException e)
        	    			{
        	    				e.printStackTrace();
        	    			}
        	    		}
        	    		break;
        	    	}
        	    }
    		}
    		
    		//setTipoComprobanteDireccion(Map)
    		setNumeroComprobante(StringUtils.isEmpty(direccion.getNoComprobante()) ? "" : direccion.getNoComprobante().trim());
    		setFechaComprobante(StringUtils.isEmpty(direccion.getFechaExpedicionComprobante()) ? new Date() : 
    			(sdf.parse(direccion.getFechaExpedicionComprobante())));
    		
    		if(StringUtils.isNotEmpty(direccion.getIdPais())) 
    		{
    			paisMap = new HashMap();
    			paisMap.put(VALUE, direccion.getIdPais());
    			paisMap.put(LABEL, direccion.getDescPais());
    			setPais(paisMap);
    			if("48".equals(direccion.getIdPais()))
    				setVerLupaBuscarCodigoPostal(true);
    		}
    		else
    			setVerLupaBuscarCodigoPostal(true); // el idPais es null, talvez la dirección es de Mexico
    		
    		//System.out.println("dir comprobante: " + direccion.getFechaExpedicionComprobante() + 
    		//		"parse: " + sdf.parse(direccion.getFechaExpedicionComprobante()));
    		
    		setSeccion(VER_SECCION_DIRECCION);
    		setSubSeccion(VER_SECCION_MODIFICAR_AGREGAR_DIRECCION);
    	}
    	catch(ContratacionException e)
    	{
    		e.printStackTrace();
    		setLevel(-1);
    		getDelegate().record("Ocurri\u00F3 un error al consultar la direcci\u00F3n del cliente. ", null);
    	}
    	catch(ParseException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public void altaDireccionCliente(IRequestCycle cycle)
    {
    	setIdPersona(getIdPersona());
		
		setCalle("");
		setNoExterior("");
		setNoInterior("");
		setPrimaria(false);
		setCodigoPostal("");
		setColonia("");
		setCiudad("");
		setDelegacion("");
		setEstado("");
		setFacturacionElectronica(false);
		
		Map tipoMap = new HashMap();
		tipoMap.put(VALUE, "SELECCIONE");
		tipoMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
		setTipoComprobanteDireccion(tipoMap);
		
		setNumeroComprobante("");
        setFechaComprobante(new Date());
        
        setVerLupaBuscarCodigoPostal(true);
        setSeccion(VER_SECCION_DIRECCION);
		setSubSeccion(VER_SECCION_MODIFICAR_AGREGAR_DIRECCION);
    }
    
    public void agregarModificarDireccion(IRequestCycle cycle)
    {
    	StringBuffer errores = new StringBuffer();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	
    	if(validarCamposDireccion(errores))
    	{
    		if(getIdDireccionCliente() != null) // modificacion de direccion
        	{
    			Visit visit = (Visit) getVisit();
    	        Persona cliente = getSicaBusquedaPersonaService().buscaPersonaPorId(getIdPersona().intValue());
    	        Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().getIdPersona().intValue());
    	        
    			Map datos = new HashMap();
    			datos.put("pais",getPais().get(VALUE).toString());
    			datos.put("calle", getCalle().trim());
    			datos.put("noExterior", getNoExterior().trim());
    			datos.put("noInterior", getNoInterior() != null ? getNoInterior().trim() : null);
    			datos.put("esPrimaria", isPrimaria()? "1": "3");
    			datos.put("codigoPostal", getCodigoPostal().trim());
    			datos.put("colonia", getColonia().trim());
    			datos.put("ciudad", getCiudad().trim());
    			datos.put("delMunicipio", getDelegacion().trim());
    			datos.put("estado", getEstado().trim());
    			
    			if(isFacturacionElectronica())
    			{
    				datos.put("esFiscal", "S");
    				datos.put("facturacionElectronica", Constantes.ID_TIPO_MODIFICADO_CONTRATACION_FE + "");
    			}
    			else
    			{
    				datos.put("esFiscal", "N");
    				datos.put("facturacionElectronica", Constantes.ID_TIPO_NO_VALIDADA_FE + "");
    			}

    			if(!"SELECCIONE".equals(getTipoComprobanteDireccion().get(VALUE)))
    			{
	    			datos.put("tipoComprobante",getTipoComprobanteDireccion().get(VALUE).toString());
	    			datos.put("numComprobante",getNumeroComprobante().trim());
	    			datos.put("fechaComprobante",sdf.format(getFechaComprobante()));
    			}
    			datos.put("idDireccion", getIdDireccionCliente());
    			datos.put("usuario", visit.getUser().getIdPersona());
    			
    			getSicaServiceData().actualizarDireccion(datos);
    			
    			setLevel(1);
	    		getDelegate().record("Se ha actualizado la direcci\u00F3n del cliente exitosamente. ", null);
	    		activate(cycle);
        		
        	}
        	else // alta de direccion
        	{
        		Direccion direccion = new Direccion();
        		direccion.setIdPais(getPais().get(VALUE).toString());
        		direccion.setCalle(getCalle().trim());
        		direccion.setNumeroExterior(getNoExterior().trim());
    	        direccion.setNumeroInterior(getNoInterior() != null ? getNoInterior().trim() : null);
    	        direccion.setIdTipoDireccion(isPrimaria()? "1": "3");
    	        direccion.setCodigoPostal(getCodigoPostal().trim());
    	        direccion.setColonia(getColonia().trim());
    	        direccion.setCiudad(getCiudad().trim());
    	        direccion.setDelegacion(getDelegacion().trim());
    	        direccion.setEntFederativa(getEstado().trim());
    	        
    	        if(isFacturacionElectronica())
    			{
    				direccion.setEsFiscal("S");
        	        direccion.setVerificadoFE(Constantes.ID_TIPO_MODIFICADO_CONTRATACION_FE);
    			}
    			else
    			{
    				direccion.setEsFiscal("N");
        	        direccion.setVerificadoFE(Constantes.ID_TIPO_NO_VALIDADA_FE);
    			}
    	        
    	        if(!"SELECCIONE".equals(getTipoComprobanteDireccion().get(VALUE)))
    	    	{
    	        	TipoComprobante tc = null;
    	        	String tipoPersona = "MORAL".equals(getFisicaMoral()) ? Constantes.ID_TIPO_PERSONA_MORAL : Constantes.ID_TIPO_PERSONA_FISICA;
    	    	    LinkedHashMap comprobantesMap = getSicaConsultaCatalogosService().obtenCatTipoComprobante(tipoPersona);
    	    	    String idDocumento = null;
    	    	    
    	    	    for (Iterator i = comprobantesMap.keySet().iterator(); i.hasNext(); ) 
    	    	    {
    	    	    	idDocumento = (String) i.next();
    	    	    	tc = (TipoComprobante)comprobantesMap.get(idDocumento);
    	    	    	
    	    	    	if(idDocumento.equals(getTipoComprobanteDireccion().get(VALUE)))
    	    	    	{
    	    	    		direccion.setTipoComprobante(tc);
    	    	    		direccion.setNoComprobante(getNumeroComprobante());
    	    	    		direccion.setFechaExpedicionComprobante(sdf.format(getFechaComprobante()));
    	    	    		break;
    	    	    	}
    	    	    }
    	    	}
    	        
    	        Visit visit = (Visit) getVisit();
    	        Persona cliente = getSicaBusquedaPersonaService().buscaPersonaPorId(getIdPersona().intValue());
    	        Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().getIdPersona().intValue());
    	        try
    	        {
    	        	getSicaRegistroDireccionPersonaService().registraDireccion(cliente, direccion, usuario);
    	        	setLevel(1);
    	    		getDelegate().record("Se ha dado de alta la direcci\u00F3n del cliente exitosamente. ", null);
    	    		activate(cycle);
    	        }
    	        catch(ContratacionException e)
    	        {
    	        	e.printStackTrace();
    	        	setLevel(-1);
    	    		getDelegate().record("Ocurri\u00F3 un error al dar de alta la direcci\u00F3n del cliente. " + e.getLocalizedMessage(), null);
    	        }
        	}
    	}
    	else
    	{
    		setLevel(-1);
    		getDelegate().record(errores.toString(), null);
    	}
    }
    
    private boolean validarCamposDireccion(StringBuffer errores)
    {
    	boolean esValido = true;
    	
    	setFaltaCalle(true);
    	if(StringUtils.isEmpty(getCalle()) || "".equals(getCalle().trim()))
    		errores.append("El campo Calle no debe estar vac\u00EDo. ");
    	else if(!getCalle().trim().matches("[A-Za-z0-9., \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*"))
    		errores.append("El campo Calle no debe contener caracteres inv\u00E1lidos. ");
    	else
    		setFaltaCalle(false);
    	
    	setFaltaNoExterior(true);
    	if(StringUtils.isEmpty(getNoExterior()) || "".equals(getNoExterior().trim()))
    		errores.append("El campo No. Exterior no debe estar vac\u00EDo. ");
    	else if(!getNoExterior().trim().matches("[0-9a-zA-Z- \u00F1\u00D1]*"))
    		errores.append("El campo No. Exterior no debe contener caracteres inv\u00E1lidos. ");
    	else
    		setFaltaNoExterior(false);
    	
    	setFaltaNoInterior(true);
    	if(StringUtils.isNotEmpty(getNoInterior()) && !"".equals(getNoInterior().trim()) && !getNoInterior().trim().matches("[0-9a-zA-Z- \u00F1\u00D1]*"))
    		errores.append("El campo No. Interior no debe contener caracteres inv\u00E1lidos. ");
    	else
    		setFaltaNoInterior(false);
    	
    	setFaltaCodigoPostal(true);
    	if(StringUtils.isEmpty(getCodigoPostal()) || "".equals(getCodigoPostal().trim()))
    		errores.append("El campo C\u00F3digo Postal no debe estar vac\u00EDo. ");
    	else if(!getCodigoPostal().trim().matches("[0-9]*"))
    		errores.append("El campo C\u00F3digo Postal solo debe contener d\u00EDgitos. ");
    	else
    		setFaltaCodigoPostal(false);
    	
    	setFaltaColonia(true);
    	if(StringUtils.isEmpty(getColonia()) || "".equals(getColonia().trim()))
    		errores.append("El campo Colonia no debe estar vac\u00EDo. ");
    	else if(getColonia().equals("-- SELECCIONE COLONIA --"))
    		errores.append("El campo Colonia debe tener seleccionada una opci\u00F3n v\u00E1lida. ");
    	else if(!getColonia().trim().matches("[A-Za-z0-9. \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*"))
    		errores.append("El campo Colonia no debe contener caracteres inv\u00E1lidos. ");
    	else
    		setFaltaColonia(false);
    	
    	setFaltaCiudad(true);
    	if(StringUtils.isEmpty(getCiudad()) || "".equals(getCiudad().trim()))
    		errores.append("El campo Ciudad no debe estar vac\u00EDo. ");
    	else if(!getCiudad().trim().matches("[A-Za-z. \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*"))
    		errores.append("El campo Ciudad no debe contener caracteres inv\u00E1lidos. ");
    	else
    		setFaltaCiudad(false);
    	
    	setFaltaDelegacion(true);
    	if(StringUtils.isEmpty(getDelegacion()) || "".equals(getDelegacion().trim()))
    		errores.append("El campo Delegaci\u00F3n/Municipio no debe estar vac\u00EDo. ");
    	else if(getDelegacion().equals("-- SELECCIONE MUNICIPIO --"))
    		errores.append("El campo Delegaci\u00F3n/Municipio debe tener seleccionada una opci\u00F3n v\u00E1lida. ");
    	else if(!getDelegacion().trim().matches("[A-Za-z. \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*"))
    		errores.append("El campo Delegaci\u00F3n/Municipio no debe contener caracteres inv\u00E1lidos. ");
    	else
    		setFaltaDelegacion(false);
    	
    	setFaltaEstado(true);
    	if(StringUtils.isEmpty(getEstado()) || "".equals(getEstado().trim()))
    		errores.append("El campo Estado no debe estar vac\u00EDo. ");
    	else if(getEstado().equals("-- SELECCIONE ENTIDAD --"))
    		errores.append("El campo Estado debe tener seleccionada una opci\u00F3n v\u00E1lida. ");
    	else if(!getEstado().trim().matches("[A-Za-z. \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*"))
    		errores.append("El campo Estado no debe contener caracteres inv\u00E1lidos. ");
    	else
    		setFaltaEstado(false);
    	
    	setFaltaTipoComprobante(true);
    	if("SELECCIONE".equals(getTipoComprobanteDireccion().get(VALUE)) && 
    			(StringUtils.isNotEmpty(getNumeroComprobante()) /*|| getFechaComprobante() != null*/))
    		errores.append("El campo Tipo Comprobante de Domicilio debe tener seleccionada una opci\u00F3n v\u00E1lida. ");
    	else
    		setFaltaTipoComprobante(false);
    	
    	if(!"SELECCIONE".equals(getTipoComprobanteDireccion().get(VALUE)))
    	{
    		setFaltaNoComprobante(true);
    		if(StringUtils.isEmpty(getNumeroComprobante()) || "".equals(getNumeroComprobante().trim()))
    			errores.append("El campo N\u00FAmero de comprobante no debe estar vac\u00EDo. ");
    		else
    			setFaltaNoComprobante(false);
    		
    		setFaltaFechaContraparte(true);
    		if(getFechaComprobante() == null)
    			errores.append("El campo Fecha de expedici\u00F3n no debe estar vac\u00EDo. ");
    		else if(DateUtils.inicioDia(getFechaComprobante()).after(DateUtils.inicioDia(new Date())))
    			errores.append("El campo Fecha de expedici\u00F3n no debe mayor al d\u00EDa en curso. ");
    		else
    			setFaltaFechaContraparte(false);
    	}

    	//60057-CFDI 3.3
    	setFaltaNit(true);
    	//CANADÁ=10, ESTADOS UNIDOS=23
		if (("10".equals(getPais().get(VALUE).toString()) || "23".equals(getPais().get(VALUE).toString()))
				&& isFacturacionElectronica()) {
			Pattern pattern = Pattern.compile("\\d{9}");
			NitPersona nitPersona = getSicaServiceData().findNitByIdPersona(getIdPersona());
			if(nitPersona == null || nitPersona.getNit() == null || "".equals(nitPersona.getNit().trim()) || !pattern.matcher(nitPersona.getNit()).matches()) {
				errores.append("Si el pa\u00EDs de la direcci\u00f3n fiscal es Canad\u00E1 o Estados Unidos es necesario que modifique el campo NIT a un formato de 9 d\u00EDgitos "
						+ "en la secci\u00f3n de Datos generales y regulatorios del cliente.");
			} else {
				setFaltaNit(false);
			}
		} else {
			setFaltaNit(false);
		}
    
    	if(errores.length() > 0)
    		esValido = false;
    	
    	return esValido;
    }
    
    public void buscarDatosPorCodigoPostal(IRequestCycle cycle)
    {
    	try
    	{
    		setModoDireccion(false);
    		setFaltaCodigoPostal(false);
    		direccionCP(getCodigoPostal());
    	}
    	catch(ContratacionException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public void eliminarDireccionCliente(IRequestCycle cycle)
    {
    	int idDireccion = Integer.parseInt((cycle.getServiceParameters()[0]).toString());
    	int idPersona = Integer.parseInt((cycle.getServiceParameters()[1]).toString());
    	String nombreCompleto = (cycle.getServiceParameters()[2]).toString();
    	String rfc = (cycle.getServiceParameters()[3]).toString();
    	Map datos = new HashMap();
    	
    	Visit visit = (Visit) getVisit();
        
        datos.put("status", Constantes.STATUS_DIRECCION_CERRADO);
        datos.put("idDireccion", new Integer(idDireccion));
		datos.put("usuario", visit.getUser().getIdPersona());
    	
    	if(getSicaServiceData().eliminarDireccion(datos))
    	{
    		setIdDireccionCliente(null);
    		setLevel(1); // exito
			getDelegate().record("Se ha eliminado correctamente la direcci\u00F3n del cliente. ", null);
		}
		else
		{
			setLevel(-1); // indicamos que son errores
			getDelegate().record("Ha ocurrido un error al elimnar la direcci\u00F3n del cliente. ", null);
		}
    	
    	Object parametros[] = new Object[]{"" + idPersona, nombreCompleto, rfc};
    	cycle.setServiceParameters(parametros);
    	consultarDireccionesCliente(cycle);
    }
    
    
    public void ocultarMostrarSeccionRegulatoria(IRequestCycle cycle)
    {
    	Integer idPersona = getIdPersona();
    	List datos = null;
    	
    	if(idPersona != null)
    	{
    		datos = getSicaServiceData().findRegulatorioDatosPM(idPersona);
    		if(datos != null && datos.size() > 0)
    		{
    			if(!isVerCamposRegulatorios())
    			{
    				setLevel(0);
        			getDelegate().record("Nota: Al desmarcar el campo 'Datos regulatorios', el cliente no estar\u00E1 perfilado para " +
        					             "operar montos mayores que o iguales a 1 mill\u00F3n de dolares americanos.", null);
    			}
    			/*else
    				buscarApoderadoDatosRegulatorios(idPersona);*/
    		}
    	}
    	this.cambiaNacionalidad();
    }
    
    public void modificarDatosGralesRegulatoriosClienteAnt(IRequestCycle cycle)
    {
    	StringBuffer errores = new StringBuffer();
    	com.ixe.ods.bup.model.Persona persona = null; 
    	SimpleDateFormat dateFormatRegulatorio = new SimpleDateFormat("dd/MM/yyyy");
    	Visit visit = (Visit) getVisit();
    	//Usuario empleado = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().getIdPersona().intValue());
    	
    	setModoActualizar(true);
    	
    	if("FISICA".equals(getFisicaMoral()))
    	{
    		if(validarCamposPersona(errores))
    		{
    			Map datosPf = new HashMap();
    			datosPf.put("idPersona", getIdPersona());
    			datosPf.put("usuario", visit.getUser().getIdPersona()); 
    			datosPf.put("nombre", getNombre().trim());  
    			datosPf.put("paterno", getPaterno().trim()); 
    			datosPf.put("materno", StringUtils.isEmpty(getMaterno()) || "".equals(getMaterno().trim()) ? null :  getMaterno().trim()); 
    			datosPf.put("sectorEconomico", getSectorBanxico().get(VALUE).toString());
    			//datosPf.put("giroEconomico", getGiroEconomico().get(VALUE));
    			datosPf.put("actividadEconomica", getActividadEconomica().get(VALUE));
    			datosPf.put("nacionalidad", getNacionalidad().get(VALUE)); // *
    			datosPf.put("rfc", getRfc().trim()); 
    			datosPf.put("fechaNacimiento", getFechaNacimiento() != null ? dateFormatRegulatorio.format(getFechaNacimiento()) : ""); 
    			
    			if(getPaisNacimiento() != null && "SELECCIONE".equals(getPaisNacimiento().get(VALUE)))
    				datosPf.put("paisNacimiento", "");
    			else
    				datosPf.put("paisNacimiento", getPaisNacimiento().get(VALUE)); // *
    			
    			datosPf.put("lugarNacimiento", StringUtils.isEmpty(getLugarNacimiento()) || "".equals(getLugarNacimiento().trim()) ? "" : getLugarNacimiento().trim()); // *
    			
    			if(getEstadoCivil() == null || "SELECCIONE".equals(getEstadoCivil().get(VALUE).toString()))
    				datosPf.put("estadoCivil", "");
    			else 
    				datosPf.put("estadoCivil", getEstadoCivil().get(VALUE)); // *
    				 // *
    			
    			datosPf.put("sexo", StringUtils.isNotEmpty(getSexo()) || !"".equals(getSexo().trim()) ? getSexo() : "");
    			datosPf.put("facturacionElectronica", isFacturacionElectronica() ? "3" : "0");
    			
    			if(getSicaServiceData().actualizarPersonaFisica(datosPf))
    			{
        			setLevel(1); // exito
            		getDelegate().record("Se han actualizado correctamente los datos del cliente.", null);
        		}
    			else
    			{
    				setLevel(-1); // error
    				getDelegate().record("Ha ocurrido un error al actualizar los datos del cliente.", null);
    			}
    		}
    		else
    		{
    			setLevel(-1); // indicamos que son errores
        		getDelegate().record(errores.toString(), null);
    		}
    	}
    	else
    	{
    		Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().
                                                               getIdPersona().intValue());
    		// com.ixe.ods.bup.model.PersonaMoral;
    		//persona = getSicaServiceData().findPersonaMoralByIdPersona(idPersona);
    		//List personasBUPTmp = getBupServiceData().findPersonaByIdPersona(null);
    		if(validarCamposPersona(errores))
    		{
    			boolean altaApoderado = false;
    			Map datosPm = new HashMap();
    			com.ixe.ods.bup.model.PersonaFisica apoderado = null;
    			    			
    			datosPm.put("rfc", getRfc().trim());
    			datosPm.put("razonSocial", getRazonSocial().trim());
    			datosPm.put("nacionalidad", getNacionalidad().get(VALUE));
    			datosPm.put("sectorEconomico", getSectorBanxico().get(VALUE).toString());
    			datosPm.put("usuario", visit.getUser().getIdPersona()); 
    			datosPm.put("facturacionElectronica", isFacturacionElectronica() ? "3" : "0");
    			datosPm.put("idPersona", getIdPersona());
    			datosPm.put("fechaNacimiento", getFechaNacimiento() != null ? dateFormatRegulatorio.format(getFechaNacimiento()) : "");
    			getSicaServiceData().actualizarPersonaMoral(datosPm);
    			
    			// *******************  Inicia almacenamiento de apoderado legal *************************************************
    			if(StringUtils.isNotEmpty(getApoderadoNombre()) && StringUtils.isNotEmpty(getApoderadoPaterno()))
    			{
    				if(getIdPersonaApoderado() != null)
        				apoderado = getSicaServiceData().findPersonaFisicaByIdPersona(getIdPersonaApoderado());
        			
        			if(apoderado != null)
        			{
        				if(StringUtils.isEmpty(apoderado.getNombre()) || StringUtils.isEmpty(apoderado.getPaterno()) ||
        				   !getApoderadoNombre().equalsIgnoreCase(apoderado.getNombre()) || 
        				   !getApoderadoPaterno().equalsIgnoreCase(apoderado.getPaterno()) ||
        				   (StringUtils.isNotEmpty(getApoderadoMaterno()) && StringUtils.isNotEmpty(apoderado.getMaterno()) &&
        					 !getApoderadoMaterno().equalsIgnoreCase(apoderado.getMaterno())))
        				{
        					altaApoderado = true;
        				}
        				else // actualiza datos apoderado
        				{
        					com.ixe.ods.bup.model.PersonaFisica pf = getSicaServiceData().findPersonaFisicaByIdPersona(getIdPersonaApoderado());
        					if(pf != null)
        					{
        						pf.setNombre(getApoderadoNombre().trim());
        						pf.setPaterno(getApoderadoPaterno().trim());
        						pf.setMaterno(StringUtils.isEmpty(getApoderadoMaterno()) ? "": getApoderadoMaterno().trim());
        						getSicaServiceData().update(pf);
        					}
        				}
        			}
        			else
        				altaApoderado = true;
        			
        			if(altaApoderado)
    				{
    					PersonaFisica pfApoderadoLegal = new PersonaFisica();
    		        	pfApoderadoLegal.setNombre(getApoderadoNombre().trim());
    		        	pfApoderadoLegal.setPaterno(getApoderadoPaterno().trim());
    		        	pfApoderadoLegal.setMaterno(StringUtils.isEmpty(getApoderadoMaterno()) || "".equals(getApoderadoMaterno().trim()) ? "": getApoderadoMaterno().trim());
    		        	pfApoderadoLegal.setTipoPersona("PF");
    		        	//pfApoderadoLegal.setRfc("XAXX010101000");
    		        	try 
    		        	{
    		        		Persona personaApoderadoLegal = getSicaRegistroPersonaService().registraPersona(pfApoderadoLegal, usuario);
    		        		//******************************************************************************************************
        		        	//******** Inicia almacenamiento de la relacion entre el cliente y su apoderado legal ******************
        		        	RelacionPersonas relacion = new RelacionPersonas();
        		        	relacion.setIdPersona1(getIdPersona().intValue()); // cliente
        		        	relacion.setIdPersona2(personaApoderadoLegal.getIdPersona());  //apoderado
        		        	relacion.setIdTipoRelacion("6"); // Relacion de cliente y apoderado --> 6
        		        	relacion.setStatus(Constantes.STATUS_RELACION_VIGENTE);
        		        	
        		        	try
        		        	{
        		        		getSicaRegistroRelacionContratacionService().registraRelacion(relacion, usuario);
        		        	}
        		        	catch(ContratacionException ce)
        		        	{
        		        		warn(ce.getMessage(), ce);
        		    	        getDelegate().record("Error al registrar la relaci\u00f3n entre el cliente y su apoderado legal: " +
        		    	        		ce.getMessage(), null);
        		    	        return;
        		        	}
    		        	}
    		        	catch(ContratacionException ce) 
    		        	{
    		        		warn(ce.getMessage(), ce);
    		    	        getDelegate().record("Error al registrar al apoderado legal: " + ce.getMessage(),null);
    		    	        return;
    		        	}
    				}
    			}
    			
    			//******************************************************************************************************
	        	//********************* Inicia almacenamiento de los datos regulatorios del cliente ********************
    			List datos = null;
    	    	
	    		datos = getSicaServiceData().findRegulatorioDatosPM(getIdPersona());
	    		if(datos != null && datos.size() > 0) 
	    		{
	    			if(!isVerCamposRegulatorios()) // se desmarcó el check de info regulatoria, el cliente ya no va a estar perfilado
	    			{
	    				RegulatorioDatosPM pm = (RegulatorioDatosPM)datos.get(0);
	    				RegulatorioDatosPM pmNuevo = new RegulatorioDatosPM();  
	    				pmNuevo.setActividadEconomica(pm.getActividadEconomica());
	    				pmNuevo.setClaveBanxico(pm.getClaveBanxico());
	    				pmNuevo.setClaveIdentificadorContraparte(pm.getClaveIdentificadorContraparte());
	    				pmNuevo.setClaveLei(pm.getClaveLei().trim());
	    				pmNuevo.setClaveLeiMatrizDirecta(pm.getClaveLeiMatrizDirecta().trim());
	    				pmNuevo.setClaveLeiMatrizUltimaInstancia(pm.getClaveLeiMatrizUltimaInstancia().trim());
	    				pmNuevo.setContratoSica(pm.getContratoSica());
	    				pmNuevo.setFechaContraparte(pm.getFechaContraparte());
	    				pmNuevo.setFechaSistema(new Date());
	    				pmNuevo.setIdPersona(pm.getIdPersona());
	    				pmNuevo.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
	    				pmNuevo.setNombreContraparte(pm.getNombreContraparte().trim());
	    				pmNuevo.setPaisControlContraparte(pm.getPaisControlContraparte());
	    				pmNuevo.setPaisResidenciaContraparte(pm.getPaisResidenciaContraparte());
	    				pmNuevo.setPerfilMdd("N");
	    				pmNuevo.setSectorIndustrial(pm.getSectorIndustrial());
	    				pmNuevo.setSociedadMercantil(pm.getSociedadMercantil());
	    				pmNuevo.setTipoIdentificador(pm.getTipoIdentificador());
	    				
	    				getSicaServiceData().store(pmNuevo);
	    				
	    				RegulatorioPerfil perfil = null;
	    				datos = getSicaServiceData().findRegulatorioPerfil(getIdPersona());
	    				if(datos != null && datos.size() > 0)
	    				{
	    					perfil = (RegulatorioPerfil)datos.get(0);
	    					//perfil.setPerfilMdd("N"); // actualizar SC_REG_PERFIL o SC_REG_PM dependiendo en donde quede el campo perfil_mdd
	    					perfil.setFechaUltimaModificacion(new Date());
	    					perfil.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
	    					getSicaServiceData().update(perfil);
	    				}
	    			}
	    			else
	    			{
	    				// verificar si hubo cambios para insertar un nuevo registro 
	    				RegulatorioPerfil perfil = null;
	    				RegulatorioDatosPM regPmActual = null, regPmNuevo = null;
	    				RegulatorioInstitucion regInstActual = null, regInstNuevo = null;
	    				boolean huboCambiosPm = false;
	    				boolean huboCambiosInstitucion = false;
	    				boolean actualizaClaveBanxico = false;
	    				
	    				String noCuentaSica = null;
	    				
	    				ContratoSica contratoSica = getSicaServiceData().findContratoSicaByIdPersona(getIdPersona());
    	    			if(contratoSica != null && StringUtils.isNotEmpty(contratoSica.getNoCuenta()))
    	    				noCuentaSica = contratoSica.getNoCuenta();
	    				
    	    			regPmActual = (RegulatorioDatosPM)datos.get(0);
    	    			regPmNuevo = new RegulatorioDatosPM();
    	    			
    	    			regPmNuevo.setPerfilMdd("S");
    	    			regPmNuevo.setClaveBanxico(regPmActual.getClaveBanxico());
    	    			if((StringUtils.isEmpty(getClaveBanxico()) || "".equals(getClaveBanxico().trim())) && StringUtils.isNotEmpty(regPmActual.getClaveBanxico()))
    	    			{
    	    				huboCambiosPm = true;
    	    				regPmNuevo.setClaveBanxico(null); // el usuario eliminó la clave banxico
    	    			}
    	    			else if(StringUtils.isNotEmpty(getClaveBanxico()) && !"".equals(getClaveBanxico().trim())) // se actualiza la clave banxico, no es necesario reportarse
    	    			{                                                  // es decir, no se actualiza el campo REG_PM_ACTUALIZADO de sc_reg_perfil
    	    				actualizaClaveBanxico = true;
    	    				regPmNuevo.setClaveBanxico(getClaveBanxico().trim());
    	    			}
    	    			
    	    			regPmNuevo.setNombreContraparte(regPmActual.getNombreContraparte());
    	    			if(!StringUtils.equalsIgnoreCase(getContraparte(), regPmActual.getNombreContraparte()))
    	    			{
    	    				huboCambiosPm = true;
    	    				regPmNuevo.setNombreContraparte(getContraparte().trim());
    	    			}
    	    			
    	    			int valor = Integer.parseInt(getTipoSociedadMercantil().get(VALUE).toString());
    	    			regPmNuevo.setSociedadMercantil(regPmActual.getSociedadMercantil());
    	    			if(valor != regPmActual.getSociedadMercantil().getId().intValue())
    	    			{
    	    				regPmNuevo.setSociedadMercantil(new RegulatorioSociedadMercantil(new Integer(valor),
   								 getTipoSociedadMercantil().get(LABEL).toString(), 
   								 getTipoSociedadMercantil().get(LABEL + "2").toString()));
    	    				huboCambiosPm = true;
    	    			}
    	    			
    	    			valor = Integer.parseInt(getTipoIdentificador().get(VALUE).toString());
    	    			regPmNuevo.setTipoIdentificador(regPmActual.getTipoIdentificador());
    	    			if(valor != regPmActual.getTipoIdentificador().getId().intValue())
    	    			{
    	    				huboCambiosPm = true;
    	    				regPmNuevo.setTipoIdentificador(new RegulatorioTipoIdentificador(new Integer(valor),
    	    											 getTipoIdentificador().get(LABEL).toString()));
    	    			}
    	    			
    	    			regPmNuevo.setClaveIdentificadorContraparte(regPmActual.getClaveIdentificadorContraparte());
    	    			if(!StringUtils.equalsIgnoreCase(getClaveIdentificador(), regPmActual.getClaveIdentificadorContraparte()))
    	    			{
    	    				huboCambiosPm = true;
    	    				regPmNuevo.setClaveIdentificadorContraparte(getClaveIdentificador().trim());
    	    			}
					   	
    	    			valor = Integer.parseInt(getTipoContraparte().get(VALUE).toString());
    	    			regPmNuevo.setActividadEconomica(regPmActual.getActividadEconomica());
    	    			if(valor != regPmActual.getActividadEconomica().getId().intValue())
    	    			{
    	    				regPmNuevo.setActividadEconomica(new RegulatorioActividadEconomica(new Integer(valor),
	   			                               					getTipoContraparte().get(LABEL).toString()));
    	    				huboCambiosPm = true;
    	    			}
    	    			
    	    			regPmNuevo.setClaveLei(regPmActual.getClaveLei());
    	    			if(!StringUtils.equalsIgnoreCase(getClaveLei(), regPmActual.getClaveLei()))
    	    			{
    	    				regPmNuevo.setClaveLei(getClaveLei().trim());
    	    				huboCambiosPm = true;
    	    			}
					   	
    	    			valor = Integer.parseInt(getSectorIndustrial().get(VALUE).toString());
    	    			regPmNuevo.setSectorIndustrial(regPmActual.getSectorIndustrial());
    	    			if(valor != regPmActual.getSectorIndustrial().getId().intValue())
    	    			{
    	    				regPmNuevo.setSectorIndustrial(new RegulatorioSectorIndustrial(new Integer(valor),
					   			                    getSectorIndustrial().get(LABEL).toString()));
    	    				huboCambiosPm = true;
    	    			}
					   	
    	    			regPmNuevo.setPaisControlContraparte(regPmActual.getPaisControlContraparte());
    	    			if(!StringUtils.equalsIgnoreCase(getPaisControlContraparte().get(VALUE).toString(),
    	    					                         regPmActual.getPaisControlContraparte().getClaveIso()))
    	    			{
    	    				huboCambiosPm = true;
    	    				regPmNuevo.setPaisControlContraparte(new RegulatorioPais(getPaisControlContraparte().get(VALUE).toString(),
									getPaisControlContraparte().get(LABEL).toString()));
    	    			}
    	    			
    	    			regPmNuevo.setPaisResidenciaContraparte(regPmActual.getPaisResidenciaContraparte());
    	    			if(!StringUtils.equalsIgnoreCase(getPaisContraparte().get(VALUE).toString(),
    	    					                         regPmActual.getPaisResidenciaContraparte().getClaveIso()))
    	    			{
    	    				regPmNuevo.setPaisResidenciaContraparte(new RegulatorioPais(getPaisContraparte().get(VALUE).toString(),
					   											getPaisContraparte().get(LABEL).toString()));
    	    				huboCambiosPm = true;
    	    			}
					   	
    	    			regPmNuevo.setClaveLeiMatrizDirecta(regPmActual.getClaveLeiMatrizDirecta());
    	    			if(!StringUtils.equalsIgnoreCase(getClaveLeiMatrizDirecta(),regPmActual.getClaveLeiMatrizDirecta()))
    	    			{
    	    				regPmNuevo.setClaveLeiMatrizDirecta(getClaveLeiMatrizDirecta().trim());
    	    				huboCambiosPm = true;
    	    			}
    	    			
    	    			regPmNuevo.setClaveLeiMatrizUltimaInstancia(regPmActual.getClaveLeiMatrizUltimaInstancia());
    	    			if(!StringUtils.equalsIgnoreCase(getClaveLeiMatrizUltima(), regPmActual.getClaveLeiMatrizUltimaInstancia()))
    	    			{
    	    				regPmNuevo.setClaveLeiMatrizUltimaInstancia(getClaveLeiMatrizUltima().trim());
    	    				huboCambiosPm = true;
    	    			}
    	    			
    	    			regPmNuevo.setFechaContraparte(regPmActual.getFechaContraparte());
    	    			if(DateUtils.inicioDia(getFechaContraparte()).compareTo(DateUtils.inicioDia(regPmActual.getFechaContraparte())) != 0)
    	    			{
    	    				regPmNuevo.setFechaContraparte(DateUtils.inicioDia(getFechaContraparte()));
    	    				huboCambiosPm = true;
    	    			}
    	    			
    	    			regPmNuevo.setFechaSistema(new Date());
    	    			regPmNuevo.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
    	    			regPmNuevo.setIdPersona(getIdPersona());
    	    			regPmNuevo.setContratoSica(regPmActual.getContratoSica());
    	    			// regPmNuevo.setPerfilMdd(regPmActual.getPerfilMdd());
    	    			
    	    			//if(huboCambiosPm || actualizaClaveBanxico)
    	    				getSicaServiceData().store(regPmNuevo);
					   	
    	    			// ************** Verificacion de datos de la relacion del cliente con la institucion ******************
    	    			List datosInstitucion = getSicaServiceData().findRegulatorioInstitucion(getIdPersona());
    	    			if(datosInstitucion != null && datosInstitucion.size() > 0)
    	    			{
    	    				regInstActual = (RegulatorioInstitucion)datosInstitucion.get(0);
    	    				regInstNuevo = new RegulatorioInstitucion();
    	    				
    	    				regInstNuevo.setIdPersona(getIdPersona());
    	    				regInstNuevo.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
    	    				regInstNuevo.setFechaSistema(new Date());
    	    				regInstNuevo.setContratoSica(noCuentaSica);
    			        	
    	    				valor = Integer.parseInt(getTipoRelacion().get(VALUE).toString());
    	    				regInstNuevo.setTipoRelacion(regInstActual.getTipoRelacion());
    	    				
    	    				if(valor != regInstActual.getTipoRelacion().getId().intValue())
    	    				{
    	    					regInstNuevo.setTipoRelacion(new RegulatorioTipoRelacion(new Integer(valor),
    			        									 getTipoRelacion().get(LABEL).toString()) );
    	    					huboCambiosInstitucion = true;
    	    				}

	    					if(valor != 10) // el cliente esta relacionado con la institucion
    	    				{
    	    					int tipoEvento = Integer.parseInt(getTipoEventoRelacion().get(VALUE).toString());
    	    					if(regInstActual.getEventoRelacion() != null)
    	    					{
    	    						if(tipoEvento != regInstActual.getEventoRelacion().getId().intValue())
    	    						{
        	    						regInstNuevo.setEventoRelacion(new RegulatorioEventoRelacion(new Integer(tipoEvento),
      		                                  getTipoEventoRelacion().get(LABEL).toString()));
        	    						huboCambiosInstitucion = true;
        	    					}
    	    						else
    	    						{
    	    							regInstNuevo.setEventoRelacion(regInstActual.getEventoRelacion());
    	    						}
    	    					}
    	    					else
    	    					{
    	    						regInstNuevo.setEventoRelacion(new RegulatorioEventoRelacion(new Integer(tipoEvento),
    		                                  getTipoEventoRelacion().get(LABEL).toString()));
      	    						huboCambiosInstitucion = true;
    	    					}

    	    					if(regInstActual.getFechaEvento() == null || (regInstActual.getFechaEvento() != null && getFechaEventoRelacion() != null &&
    	    					   (DateUtils.inicioDia(regInstActual.getFechaEvento()).compareTo(DateUtils.inicioDia(getFechaEventoRelacion())) != 0)))
    	    					{
    	    						regInstNuevo.setFechaEvento(DateUtils.inicioDia(getFechaEventoRelacion()));
    	    						huboCambiosInstitucion = true;
    	    					}
    	    					else
	    						{
    	    						regInstNuevo.setFechaEvento(DateUtils.inicioDia(regInstActual.getFechaEvento()));
    	    						//huboCambiosInstitucion = true;
    	    					}
    	    					
    	    					if(getGrupoFinanciero() != null) // 2:no pertenece -- 1: si pertenece -- 3:no esta selecionado nada
    	    					{
    	    						if(regInstActual.getPerteneceGrupoFinanciero() != null && 
    	    						   (regInstActual.getPerteneceGrupoFinanciero().intValue() != getGrupoFinanciero().intValue()))
    	    						{
    	    							regInstNuevo.setPerteneceGrupoFinanciero(
    	    								getGrupoFinanciero().intValue() != 2 && getGrupoFinanciero().intValue() != 1 ? new Integer(0) : getGrupoFinanciero());
    	    							huboCambiosInstitucion = true;
    	    						}
    	    						else
    	    							regInstNuevo.setPerteneceGrupoFinanciero(regInstActual.getPerteneceGrupoFinanciero());
    	    					}
    	    					else
    	    					{
    	    						regInstNuevo.setPerteneceGrupoFinanciero(new Integer(0));
    	    						huboCambiosInstitucion = true;
    	    					}
    	    				}
	    					else // no hay relacion con la institucion
	    					{
    	    					regInstNuevo.setEventoRelacion(null); // en el reporte se pone 0
    	    					regInstNuevo.setFechaEvento(null); // en el reporte se va en blanco
    	    					regInstNuevo.setPerteneceGrupoFinanciero(new Integer(0));
    	    					
    	    					HashMap eventoMap = new HashMap();
    	    					eventoMap.put(VALUE, "SELECCIONE");
    	    					eventoMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
    	    					setTipoEventoRelacion(eventoMap);
    	    					setGrupoFinanciero(new Integer(3)); // no se ha seleccionado nada
    	    					setFechaEventoRelacion(DateUtils.inicioDia(new Date()));
	    					}
    	    				
    	    				if(huboCambiosInstitucion)
    	    					getSicaServiceData().store(regInstNuevo);
    	    				
    	    			}
    	    			// *****************************************************************************************************
    	    			
    	    			List perfiles = getSicaServiceData().findRegulatorioPerfil(getIdPersona());
    	    			perfiles = getSicaServiceData().findRegulatorioPerfil(getIdPersona());
    	    			if(perfiles != null && perfiles.size() > 0)
    	    			{
    	    				perfil = (RegulatorioPerfil)perfiles.get(0);
    	    				if(huboCambiosPm)
    	    					perfil.setDatosRegulatoriosActualizados("S");
    	    				
    	    				if(huboCambiosInstitucion)
    	    					perfil.setRegInstitucionActualizados("S");
    	    				
    	    				//perfil.setPerfilMdd("S"); // actualizar SC_REG_PERFIL o SC_REG_PM dependiendo en donde quede el campo perfil_mdd
	    					perfil.setFechaUltimaModificacion(new Date());
	    					perfil.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
    	    				
    	    				getSicaServiceData().update(perfil);
    	    			}
	    			}
	    		}
	    		else if(isVerCamposRegulatorios()) // el cliente no tiene info regulatoria y ya fue capturada para el alta
	    		{
	    			RegulatorioPerfil perfil = new RegulatorioPerfil();
	    			String noCuentaSica = null;
	    			
	    			ContratoSica contratoSica = getSicaServiceData().findContratoSicaByIdPersona(getIdPersona());
	    			if(contratoSica != null && StringUtils.isNotEmpty(contratoSica.getNoCuenta()))
	    				noCuentaSica = contratoSica.getNoCuenta();
	    			
	        		perfil.setIdPersona(getIdPersona());
		        	perfil.setContratoSica(noCuentaSica);
		        	perfil.setDatosRegulatoriosActualizados("S");
		        	perfil.setFechaAlta(new Date());
		        	perfil.setFechaUltimaModificacion(perfil.getFechaAlta());
		        	perfil.setIdPersonaAlta(visit.getUser().getIdPersona());
		        	perfil.setIdPersonaUltimaMod(perfil.getIdPersonaAlta());
		        	//perfil.setPerfilMdd("S");
		        	perfil.setRegInstitucionActualizados("S");
		        	
		        	getSicaServiceData().store(perfil);
		        	
		        	RegulatorioDatosPM datosPM = new RegulatorioDatosPM();
		        	datosPM.setIdPersona(getIdPersona());
		        	datosPM.setClaveBanxico(getClaveBanxico().trim());
		        	datosPM.setNombreContraparte(getContraparte().trim());
		        	
		        	datosPM.setSociedadMercantil(new RegulatorioSociedadMercantil(new Integer(getTipoSociedadMercantil().get(VALUE).toString()),
		        								 getTipoSociedadMercantil().get(LABEL).toString(), 
		        								 getTipoSociedadMercantil().get(LABEL + "2").toString()));
		        	
		        	datosPM.setTipoIdentificador(new RegulatorioTipoIdentificador(new Integer(getTipoIdentificador().get(VALUE).toString()),
		        			                     getTipoIdentificador().get(LABEL).toString()));
		        	
		        	datosPM.setClaveIdentificadorContraparte(getClaveIdentificador().trim());
		        	
		        	datosPM.setActividadEconomica(new RegulatorioActividadEconomica(new Integer(getTipoContraparte().get(VALUE).toString()),
		        			                      getTipoContraparte().get(LABEL).toString()));
		        	
		        	datosPM.setClaveLei(getClaveLei().trim());
		        	
		        	datosPM.setSectorIndustrial(new RegulatorioSectorIndustrial(new Integer(getSectorIndustrial().get(VALUE).toString()),
		        			                    getSectorIndustrial().get(LABEL).toString()));
		        	
		        	datosPM.setPaisResidenciaContraparte(new RegulatorioPais(getPaisContraparte().get(VALUE).toString(),
		        											getPaisContraparte().get(LABEL).toString()));
		        	
		        	datosPM.setPaisControlContraparte(new RegulatorioPais(getPaisControlContraparte().get(VALUE).toString(),
		        										getPaisControlContraparte().get(LABEL).toString()));
		        	
		        	datosPM.setClaveLeiMatrizDirecta(getClaveLeiMatrizDirecta().trim());
		        	datosPM.setClaveLeiMatrizUltimaInstancia(getClaveLeiMatrizUltima().trim());
		        	datosPM.setFechaContraparte(DateUtils.inicioDia(getFechaContraparte()));
		        	datosPM.setFechaSistema(new Date());
		        	datosPM.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
		        	datosPM.setContratoSica(noCuentaSica);
		        	datosPM.setPerfilMdd("S");
		        	
		        	getSicaServiceData().store(datosPM);
		        	
		        	RegulatorioInstitucion institucion = new RegulatorioInstitucion();
		        	institucion.setIdPersona(getIdPersona());
		        	institucion.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
		        	institucion.setFechaSistema(new Date());
		        	institucion.setContratoSica(noCuentaSica);
		        	
		        	institucion.setTipoRelacion(new RegulatorioTipoRelacion(new Integer(getTipoRelacion().get(VALUE).toString()),
		        									getTipoRelacion().get(LABEL).toString()) );
		        	
		        	// 3: el usuario no ha seleccionado nada,  1: si 2: no
		        	institucion.setPerteneceGrupoFinanciero(getGrupoFinanciero() == null || getGrupoFinanciero().intValue() == 3 ? 
		        											new Integer(0) : getGrupoFinanciero());
		        	
		        	// Si la contraparte si está relacionada con la institucion  
		        	if(getTipoRelacion() != null && getTipoRelacion().get(VALUE) != null && !getTipoRelacion().get(VALUE).toString().equals("10")) 
		        	{
		        		institucion.setEventoRelacion(new RegulatorioEventoRelacion(new Integer(getTipoEventoRelacion().get(VALUE).toString()),
		        				                                  getTipoEventoRelacion().get(LABEL).toString()));
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
    			
    			setLevel(1); // exito
        		getDelegate().record("Se han actualizado correctamente los datos del cliente.", null);
    		}
    		else
    		{
    			setLevel(-1); // indicamos que son errores
        		getDelegate().record(errores.toString(), null);
    		}
    	}
    }
    
    /**
     * Modificar datos grales regulatorios cliente.
     *
     * @param cycle the cycle
     */
    public void modificarDatosGralesRegulatoriosCliente(IRequestCycle cycle) {
    	StringBuffer errores = new StringBuffer();
    	SimpleDateFormat dateFormatRegulatorio = new SimpleDateFormat("dd/MM/yyyy");
    	Visit visit = (Visit) getVisit();
    	setModoActualizar(true);
    	try {
	    	PersonaDto perDto = new PersonaDto();
	    	Integer idPersonaUser = visit.getUser().getIdPersona();
	    	if("FISICA".equals(getFisicaMoral())) {
	    		if(validarCamposPersona(errores)) {
	    			PersonaFisica pf = new PersonaFisica();
	    			pf.setIdPersona(getIdPersona().intValue());
	    			pf.setNombre(getNombre().trim());
	    			pf.setPaterno(getPaterno().trim());
	    			pf.setMaterno(StringUtils.isEmpty(getMaterno()) || 
	    					"".equals(getMaterno().trim()) ? null :  getMaterno().trim());
	    			if (getSectorBanxico() != null && 
	    					StringUtils.isNumeric(getSectorBanxico().get(VALUE).toString())) {
	    				int idSector = 
	    						new Integer(getSectorBanxico().get(VALUE).toString()).intValue();
	    				pf.setIdSector(idSector);
	    			}
	    			pf.setIdActividad(String.valueOf(getActividadEconomica().get(VALUE)));
	    			pf.setActividadGiro(String.valueOf(getGiroEconomico().get(VALUE)));
	    			pf.setIdPais(String.valueOf(getNacionalidad().get(VALUE)));
	    			pf.setRfc(getRfc().trim());
	    			pf.setFechaNacimiento(getFechaNacimiento() != null ? 
	    					dateFormatRegulatorio.format(getFechaNacimiento()) : "");
	    			String paisNacimiento = "";
	    			if (getPaisNacimiento() != null && 
	    					!"SELECCIONE".equals(getPaisNacimiento().get(VALUE))) {
	    				paisNacimiento = String.valueOf(getPaisNacimiento().get(VALUE));
	    			}
	    			Catalogo cat = new Catalogo();
	    			cat.setId(paisNacimiento);
	    			perDto.setPaisNacimiento(cat);
	    			pf.setLugarNacimiento(StringUtils.isEmpty(getLugarNacimiento()) || 
	    					"".equals(getLugarNacimiento().trim()) ? "" : getLugarNacimiento().trim());
	    			String estadoCivil = "";
	    			if(getEstadoCivil() == null || 
	    					!"SELECCIONE".equals(getEstadoCivil().get(VALUE).toString())) {
	    				estadoCivil = String.valueOf(getEstadoCivil().get(VALUE));
	    			}
	    			pf.setIdEstadoCivil(estadoCivil);
	    			pf.setSexo(StringUtils.isNotEmpty(getSexo()) || 
	    					!"".equals(getSexo().trim()) ? getSexo() : "");
	    			pf.setVerificaFE(isFacturacionElectronica() ? 3 : 0);
	    			pf.setCurp(getCurp());
	    			if (getTipoDocumento() != null) {
	    				pf.setIdTipoIdentifica(String.valueOf(getTipoDocumento().get(VALUE)));
	    			}
	    			pf.setNoIdentificacion(getNumDocumento());
	    			perDto.setIdExtranjero(getIdExtranjero());
	    			perDto.setPersona(pf);
	    			if(this.getPersonaService().actualizarPersona(perDto, idPersonaUser)) {
	    				
	    				//60057-CFDI 3.3
	    				NitPersona nitPersona = getSicaServiceData().findNitByIdPersona(getIdPersona());
	    		    	if(nitPersona != null) {
	    		    		nitPersona.setNit(getNit() != null && !"".equals(getNit().trim()) ? getNit() : "");
	    		    		getSicaServiceData().update(nitPersona);
	    		    	} else {
	    		    		getSicaServiceData().storeNit(getIdPersona(), getNit() != null && !"".equals(getNit().trim()) ? getNit() : "");
	    		    	}
	    				
	        			setLevel(1); // exito
	            		getDelegate().record("Se han actualizado correctamente los datos del cliente.", null);
	        		}
	    			else {
	    				setLevel(-1); // error
	    				getDelegate().record("Ha ocurrido un error al actualizar los datos del cliente.", null);
	    			}
	    		}
	    		else {
	    			setLevel(-1); // indicamos que son errores
	        		getDelegate().record(errores.toString(), null);
	    		}
	    	}
	    	else {
	    		Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().
	                                                               getIdPersona().intValue());
	    		// com.ixe.ods.bup.model.PersonaMoral;
	    		//persona = getSicaServiceData().findPersonaMoralByIdPersona(idPersona);
	    		//List personasBUPTmp = getBupServiceData().findPersonaByIdPersona(null);
	    		if(validarCamposPersona(errores)) {
	    			PersonaMoral pMoral = new PersonaMoral();
	    			boolean altaApoderado = false;
	    			com.ixe.ods.bup.model.PersonaFisica apoderado = null;
	    			pMoral.setRfc(getRfc().trim());
	    			pMoral.setRazonSocial(getRazonSocial().trim());
	    			pMoral.setIdPais(String.valueOf(getNacionalidad().get(VALUE)));
	    			if (getSectorBanxico() != null && 
	    					StringUtils.isNumeric(getSectorBanxico().get(VALUE).toString())) {
	    				int idSector = 
	    						new Integer(getSectorBanxico().get(VALUE).toString()).intValue();
	    				pMoral.setIdSector(idSector);
	    			}
	    			pMoral.setVerificaFE(isFacturacionElectronica() ? 3 : 0);
	    			pMoral.setIdPersona(getIdPersona().intValue());
	    			pMoral.setFechaConstitucion(getFechaNacimiento() != null ? 
	    					dateFormatRegulatorio.format(getFechaNacimiento()) : "");
	    			perDto.setIdExtranjero(getIdExtranjero());
	    			perDto.setPersona(pMoral);
	    			this.getPersonaService().actualizarPersona(perDto, idPersonaUser);
	    			// *******************  Inicia almacenamiento de apoderado legal ****************
	    			if(StringUtils.isNotEmpty(getApoderadoNombre()) && 
	    					StringUtils.isNotEmpty(getApoderadoPaterno())) {
	    				if(getIdPersonaApoderado() != null)
	        				apoderado = getSicaServiceData().findPersonaFisicaByIdPersona(getIdPersonaApoderado());
	        			
	        			if(apoderado != null) {
	        				if(StringUtils.isEmpty(apoderado.getNombre()) || StringUtils.isEmpty(apoderado.getPaterno()) ||
	        				   !getApoderadoNombre().equalsIgnoreCase(apoderado.getNombre()) || 
	        				   !getApoderadoPaterno().equalsIgnoreCase(apoderado.getPaterno()) ||
	        				   (StringUtils.isNotEmpty(getApoderadoMaterno()) && StringUtils.isNotEmpty(apoderado.getMaterno()) &&
	        					 !getApoderadoMaterno().equalsIgnoreCase(apoderado.getMaterno())))
	        				{
	        					altaApoderado = true;
	        				}
	        				else // actualiza datos apoderado
	        				{
	        					com.ixe.ods.bup.model.PersonaFisica pf = getSicaServiceData().findPersonaFisicaByIdPersona(getIdPersonaApoderado());
	        					if(pf != null)
	        					{
	        						pf.setNombre(getApoderadoNombre().trim());
	        						pf.setPaterno(getApoderadoPaterno().trim());
	        						pf.setMaterno(StringUtils.isEmpty(getApoderadoMaterno()) ? "": getApoderadoMaterno().trim());
	        						getSicaServiceData().update(pf);
	        					}
	        				}
	        			}
	        			else
	        				altaApoderado = true;
	        			
	        			if(altaApoderado)
	    				{
	    					PersonaFisica pfApoderadoLegal = new PersonaFisica();
	    		        	pfApoderadoLegal.setNombre(getApoderadoNombre().trim());
	    		        	pfApoderadoLegal.setPaterno(getApoderadoPaterno().trim());
	    		        	pfApoderadoLegal.setMaterno(StringUtils.isEmpty(getApoderadoMaterno()) || "".equals(getApoderadoMaterno().trim()) ? "": getApoderadoMaterno().trim());
	    		        	pfApoderadoLegal.setTipoPersona("PF");
	    		        	//pfApoderadoLegal.setRfc("XAXX010101000");
	    		        	try 
	    		        	{
	    		        		Persona personaApoderadoLegal = getSicaRegistroPersonaService().registraPersona(pfApoderadoLegal, usuario);
	    		        		//******************************************************************************************************
	        		        	//******** Inicia almacenamiento de la relacion entre el cliente y su apoderado legal ******************
	        		        	RelacionPersonas relacion = new RelacionPersonas();
	        		        	relacion.setIdPersona1(getIdPersona().intValue()); // cliente
	        		        	relacion.setIdPersona2(personaApoderadoLegal.getIdPersona());  //apoderado
	        		        	relacion.setIdTipoRelacion("6"); // Relacion de cliente y apoderado --> 6
	        		        	relacion.setStatus(Constantes.STATUS_RELACION_VIGENTE);
	        		        	
	        		        	try
	        		        	{
	        		        		getSicaRegistroRelacionContratacionService().registraRelacion(relacion, usuario);
	        		        	}
	        		        	catch(ContratacionException ce)
	        		        	{
	        		        		warn(ce.getMessage(), ce);
	        		    	        getDelegate().record("Error al registrar la relaci\u00f3n entre el cliente y su apoderado legal: " +
	        		    	        		ce.getMessage(), null);
	        		    	        return;
	        		        	}
	    		        	}
	    		        	catch(ContratacionException ce) 
	    		        	{
	    		        		warn(ce.getMessage(), ce);
	    		    	        getDelegate().record("Error al registrar al apoderado legal: " + ce.getMessage(),null);
	    		    	        return;
	    		        	}
	    				}
	    			}
	    			
	    			//******************************************************************************************************
		        	//********************* Inicia almacenamiento de los datos regulatorios del cliente ********************
	    			List datos = null;
	    	    	
		    		datos = getSicaServiceData().findRegulatorioDatosPM(getIdPersona());
		    		if(datos != null && datos.size() > 0) 
		    		{
		    			if(!isVerCamposRegulatorios()) // se desmarcó el check de info regulatoria, el cliente ya no va a estar perfilado
		    			{
		    				RegulatorioDatosPM pm = (RegulatorioDatosPM)datos.get(0);
		    				RegulatorioDatosPM pmNuevo = new RegulatorioDatosPM();  
		    				pmNuevo.setActividadEconomica(pm.getActividadEconomica());
		    				pmNuevo.setClaveBanxico(pm.getClaveBanxico());
		    				pmNuevo.setClaveIdentificadorContraparte(pm.getClaveIdentificadorContraparte());
		    				pmNuevo.setClaveLei(pm.getClaveLei().trim());
		    				pmNuevo.setClaveLeiMatrizDirecta(pm.getClaveLeiMatrizDirecta().trim());
		    				pmNuevo.setClaveLeiMatrizUltimaInstancia(pm.getClaveLeiMatrizUltimaInstancia().trim());
		    				pmNuevo.setContratoSica(pm.getContratoSica());
		    				pmNuevo.setFechaContraparte(pm.getFechaContraparte());
		    				pmNuevo.setFechaSistema(new Date());
		    				pmNuevo.setIdPersona(pm.getIdPersona());
		    				pmNuevo.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
		    				pmNuevo.setNombreContraparte(pm.getNombreContraparte().trim());
		    				pmNuevo.setPaisControlContraparte(pm.getPaisControlContraparte());
		    				pmNuevo.setPaisResidenciaContraparte(pm.getPaisResidenciaContraparte());
		    				pmNuevo.setPerfilMdd("N");
		    				pmNuevo.setSectorIndustrial(pm.getSectorIndustrial());
		    				pmNuevo.setSociedadMercantil(pm.getSociedadMercantil());
		    				pmNuevo.setTipoIdentificador(pm.getTipoIdentificador());
		    				
		    				getSicaServiceData().store(pmNuevo);
		    				
		    				RegulatorioPerfil perfil = null;
		    				datos = getSicaServiceData().findRegulatorioPerfil(getIdPersona());
		    				if(datos != null && datos.size() > 0)
		    				{
		    					perfil = (RegulatorioPerfil)datos.get(0);
		    					//perfil.setPerfilMdd("N"); // actualizar SC_REG_PERFIL o SC_REG_PM dependiendo en donde quede el campo perfil_mdd
		    					perfil.setFechaUltimaModificacion(new Date());
		    					perfil.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
		    					getSicaServiceData().update(perfil);
		    				}
		    			}
		    			else
		    			{
		    				// verificar si hubo cambios para insertar un nuevo registro 
		    				RegulatorioPerfil perfil = null;
		    				RegulatorioDatosPM regPmActual = null, regPmNuevo = null;
		    				RegulatorioInstitucion regInstActual = null, regInstNuevo = null;
		    				boolean huboCambiosPm = false;
		    				boolean huboCambiosInstitucion = false;
		    				boolean actualizaClaveBanxico = false;
		    				
		    				String noCuentaSica = null;
		    				
		    				ContratoSica contratoSica = getSicaServiceData().findContratoSicaByIdPersona(getIdPersona());
	    	    			if(contratoSica != null && StringUtils.isNotEmpty(contratoSica.getNoCuenta()))
	    	    				noCuentaSica = contratoSica.getNoCuenta();
		    				
	    	    			regPmActual = (RegulatorioDatosPM)datos.get(0);
	    	    			regPmNuevo = new RegulatorioDatosPM();
	    	    			
	    	    			regPmNuevo.setPerfilMdd("S");
	    	    			regPmNuevo.setClaveBanxico(regPmActual.getClaveBanxico());
	    	    			if((StringUtils.isEmpty(getClaveBanxico()) || "".equals(getClaveBanxico().trim())) && StringUtils.isNotEmpty(regPmActual.getClaveBanxico()))
	    	    			{
	    	    				huboCambiosPm = true;
	    	    				regPmNuevo.setClaveBanxico(null); // el usuario eliminó la clave banxico
	    	    			}
	    	    			else if(StringUtils.isNotEmpty(getClaveBanxico()) && !"".equals(getClaveBanxico().trim())) // se actualiza la clave banxico, no es necesario reportarse
	    	    			{                                                  // es decir, no se actualiza el campo REG_PM_ACTUALIZADO de sc_reg_perfil
	    	    				actualizaClaveBanxico = true;
	    	    				regPmNuevo.setClaveBanxico(getClaveBanxico().trim());
	    	    			}
	    	    			
	    	    			regPmNuevo.setNombreContraparte(regPmActual.getNombreContraparte());
	    	    			if(!StringUtils.equalsIgnoreCase(getContraparte(), regPmActual.getNombreContraparte()))
	    	    			{
	    	    				huboCambiosPm = true;
	    	    				regPmNuevo.setNombreContraparte(getContraparte().trim());
	    	    			}
	    	    			
	    	    			int valor = Integer.parseInt(getTipoSociedadMercantil().get(VALUE).toString());
	    	    			regPmNuevo.setSociedadMercantil(regPmActual.getSociedadMercantil());
	    	    			if(valor != regPmActual.getSociedadMercantil().getId().intValue())
	    	    			{
	    	    				regPmNuevo.setSociedadMercantil(new RegulatorioSociedadMercantil(new Integer(valor),
	   								 getTipoSociedadMercantil().get(LABEL).toString(), 
	   								 getTipoSociedadMercantil().get(LABEL + "2").toString()));
	    	    				huboCambiosPm = true;
	    	    			}
	    	    			
	    	    			valor = Integer.parseInt(getTipoIdentificador().get(VALUE).toString());
	    	    			regPmNuevo.setTipoIdentificador(regPmActual.getTipoIdentificador());
	    	    			if(valor != regPmActual.getTipoIdentificador().getId().intValue())
	    	    			{
	    	    				huboCambiosPm = true;
	    	    				regPmNuevo.setTipoIdentificador(new RegulatorioTipoIdentificador(new Integer(valor),
	    	    											 getTipoIdentificador().get(LABEL).toString()));
	    	    			}
	    	    			
	    	    			regPmNuevo.setClaveIdentificadorContraparte(regPmActual.getClaveIdentificadorContraparte());
	    	    			if(!StringUtils.equalsIgnoreCase(getClaveIdentificador(), regPmActual.getClaveIdentificadorContraparte()))
	    	    			{
	    	    				huboCambiosPm = true;
	    	    				regPmNuevo.setClaveIdentificadorContraparte(getClaveIdentificador().trim());
	    	    			}
						   	
	    	    			valor = Integer.parseInt(getTipoContraparte().get(VALUE).toString());
	    	    			regPmNuevo.setActividadEconomica(regPmActual.getActividadEconomica());
	    	    			if(valor != regPmActual.getActividadEconomica().getId().intValue())
	    	    			{
	    	    				regPmNuevo.setActividadEconomica(new RegulatorioActividadEconomica(new Integer(valor),
		   			                               					getTipoContraparte().get(LABEL).toString()));
	    	    				huboCambiosPm = true;
	    	    			}
	    	    			
	    	    			regPmNuevo.setClaveLei(regPmActual.getClaveLei());
	    	    			if(!StringUtils.equalsIgnoreCase(getClaveLei(), regPmActual.getClaveLei()))
	    	    			{
	    	    				regPmNuevo.setClaveLei(getClaveLei().trim());
	    	    				huboCambiosPm = true;
	    	    			}
						   	
	    	    			valor = Integer.parseInt(getSectorIndustrial().get(VALUE).toString());
	    	    			regPmNuevo.setSectorIndustrial(regPmActual.getSectorIndustrial());
	    	    			if(valor != regPmActual.getSectorIndustrial().getId().intValue())
	    	    			{
	    	    				regPmNuevo.setSectorIndustrial(new RegulatorioSectorIndustrial(new Integer(valor),
						   			                    getSectorIndustrial().get(LABEL).toString()));
	    	    				huboCambiosPm = true;
	    	    			}
						   	
	    	    			regPmNuevo.setPaisControlContraparte(regPmActual.getPaisControlContraparte());
	    	    			if(!StringUtils.equalsIgnoreCase(getPaisControlContraparte().get(VALUE).toString(),
	    	    					                         regPmActual.getPaisControlContraparte().getClaveIso()))
	    	    			{
	    	    				huboCambiosPm = true;
	    	    				regPmNuevo.setPaisControlContraparte(new RegulatorioPais(getPaisControlContraparte().get(VALUE).toString(),
										getPaisControlContraparte().get(LABEL).toString()));
	    	    			}
	    	    			
	    	    			regPmNuevo.setPaisResidenciaContraparte(regPmActual.getPaisResidenciaContraparte());
	    	    			if(!StringUtils.equalsIgnoreCase(getPaisContraparte().get(VALUE).toString(),
	    	    					                         regPmActual.getPaisResidenciaContraparte().getClaveIso()))
	    	    			{
	    	    				regPmNuevo.setPaisResidenciaContraparte(new RegulatorioPais(getPaisContraparte().get(VALUE).toString(),
						   											getPaisContraparte().get(LABEL).toString()));
	    	    				huboCambiosPm = true;
	    	    			}
						   	
	    	    			regPmNuevo.setClaveLeiMatrizDirecta(regPmActual.getClaveLeiMatrizDirecta());
	    	    			if(!StringUtils.equalsIgnoreCase(getClaveLeiMatrizDirecta(),regPmActual.getClaveLeiMatrizDirecta()))
	    	    			{
	    	    				regPmNuevo.setClaveLeiMatrizDirecta(getClaveLeiMatrizDirecta().trim());
	    	    				huboCambiosPm = true;
	    	    			}
	    	    			
	    	    			regPmNuevo.setClaveLeiMatrizUltimaInstancia(regPmActual.getClaveLeiMatrizUltimaInstancia());
	    	    			if(!StringUtils.equalsIgnoreCase(getClaveLeiMatrizUltima(), regPmActual.getClaveLeiMatrizUltimaInstancia()))
	    	    			{
	    	    				regPmNuevo.setClaveLeiMatrizUltimaInstancia(getClaveLeiMatrizUltima().trim());
	    	    				huboCambiosPm = true;
	    	    			}
	    	    			
	    	    			regPmNuevo.setFechaContraparte(regPmActual.getFechaContraparte());
	    	    			if(DateUtils.inicioDia(getFechaContraparte()).compareTo(DateUtils.inicioDia(regPmActual.getFechaContraparte())) != 0)
	    	    			{
	    	    				regPmNuevo.setFechaContraparte(DateUtils.inicioDia(getFechaContraparte()));
	    	    				huboCambiosPm = true;
	    	    			}
	    	    			
	    	    			regPmNuevo.setFechaSistema(new Date());
	    	    			regPmNuevo.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
	    	    			regPmNuevo.setIdPersona(getIdPersona());
	    	    			regPmNuevo.setContratoSica(regPmActual.getContratoSica());
	    	    			// regPmNuevo.setPerfilMdd(regPmActual.getPerfilMdd());
	    	    			
	    	    			//if(huboCambiosPm || actualizaClaveBanxico)
	    	    				getSicaServiceData().store(regPmNuevo);
						   	
	    	    			// ************** Verificacion de datos de la relacion del cliente con la institucion ******************
	    	    			List datosInstitucion = getSicaServiceData().findRegulatorioInstitucion(getIdPersona());
	    	    			if(datosInstitucion != null && datosInstitucion.size() > 0)
	    	    			{
	    	    				regInstActual = (RegulatorioInstitucion)datosInstitucion.get(0);
	    	    				regInstNuevo = new RegulatorioInstitucion();
	    	    				
	    	    				regInstNuevo.setIdPersona(getIdPersona());
	    	    				regInstNuevo.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
	    	    				regInstNuevo.setFechaSistema(new Date());
	    	    				regInstNuevo.setContratoSica(noCuentaSica);
	    			        	
	    	    				valor = Integer.parseInt(getTipoRelacion().get(VALUE).toString());
	    	    				regInstNuevo.setTipoRelacion(regInstActual.getTipoRelacion());
	    	    				
	    	    				if(valor != regInstActual.getTipoRelacion().getId().intValue())
	    	    				{
	    	    					regInstNuevo.setTipoRelacion(new RegulatorioTipoRelacion(new Integer(valor),
	    			        									 getTipoRelacion().get(LABEL).toString()) );
	    	    					huboCambiosInstitucion = true;
	    	    				}
	
		    					if(valor != 10) // el cliente esta relacionado con la institucion
	    	    				{
	    	    					int tipoEvento = Integer.parseInt(getTipoEventoRelacion().get(VALUE).toString());
	    	    					if(regInstActual.getEventoRelacion() != null)
	    	    					{
	    	    						if(tipoEvento != regInstActual.getEventoRelacion().getId().intValue())
	    	    						{
	        	    						regInstNuevo.setEventoRelacion(new RegulatorioEventoRelacion(new Integer(tipoEvento),
	      		                                  getTipoEventoRelacion().get(LABEL).toString()));
	        	    						huboCambiosInstitucion = true;
	        	    					}
	    	    						else
	    	    						{
	    	    							regInstNuevo.setEventoRelacion(regInstActual.getEventoRelacion());
	    	    						}
	    	    					}
	    	    					else
	    	    					{
	    	    						regInstNuevo.setEventoRelacion(new RegulatorioEventoRelacion(new Integer(tipoEvento),
	    		                                  getTipoEventoRelacion().get(LABEL).toString()));
	      	    						huboCambiosInstitucion = true;
	    	    					}
	
	    	    					if(regInstActual.getFechaEvento() == null || (regInstActual.getFechaEvento() != null && getFechaEventoRelacion() != null &&
	    	    					   (DateUtils.inicioDia(regInstActual.getFechaEvento()).compareTo(DateUtils.inicioDia(getFechaEventoRelacion())) != 0)))
	    	    					{
	    	    						regInstNuevo.setFechaEvento(DateUtils.inicioDia(getFechaEventoRelacion()));
	    	    						huboCambiosInstitucion = true;
	    	    					}
	    	    					else
		    						{
	    	    						regInstNuevo.setFechaEvento(DateUtils.inicioDia(regInstActual.getFechaEvento()));
	    	    						//huboCambiosInstitucion = true;
	    	    					}
	    	    					
	    	    					if(getGrupoFinanciero() != null) // 2:no pertenece -- 1: si pertenece -- 3:no esta selecionado nada
	    	    					{
	    	    						if(regInstActual.getPerteneceGrupoFinanciero() != null && 
	    	    						   (regInstActual.getPerteneceGrupoFinanciero().intValue() != getGrupoFinanciero().intValue()))
	    	    						{
	    	    							regInstNuevo.setPerteneceGrupoFinanciero(
	    	    								getGrupoFinanciero().intValue() != 2 && getGrupoFinanciero().intValue() != 1 ? new Integer(0) : getGrupoFinanciero());
	    	    							huboCambiosInstitucion = true;
	    	    						}
	    	    						else
	    	    							regInstNuevo.setPerteneceGrupoFinanciero(regInstActual.getPerteneceGrupoFinanciero());
	    	    					}
	    	    					else
	    	    					{
	    	    						regInstNuevo.setPerteneceGrupoFinanciero(new Integer(0));
	    	    						huboCambiosInstitucion = true;
	    	    					}
	    	    				}
		    					else // no hay relacion con la institucion
		    					{
	    	    					regInstNuevo.setEventoRelacion(null); // en el reporte se pone 0
	    	    					regInstNuevo.setFechaEvento(null); // en el reporte se va en blanco
	    	    					regInstNuevo.setPerteneceGrupoFinanciero(new Integer(0));
	    	    					
	    	    					HashMap eventoMap = new HashMap();
	    	    					eventoMap.put(VALUE, "SELECCIONE");
	    	    					eventoMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
	    	    					setTipoEventoRelacion(eventoMap);
	    	    					setGrupoFinanciero(new Integer(3)); // no se ha seleccionado nada
	    	    					setFechaEventoRelacion(DateUtils.inicioDia(new Date()));
		    					}
	    	    				
	    	    				if(huboCambiosInstitucion)
	    	    					getSicaServiceData().store(regInstNuevo);
	    	    				
	    	    			}
	    	    			// *****************************************************************************************************
	    	    			
	    	    			List perfiles = getSicaServiceData().findRegulatorioPerfil(getIdPersona());
	    	    			perfiles = getSicaServiceData().findRegulatorioPerfil(getIdPersona());
	    	    			if(perfiles != null && perfiles.size() > 0)
	    	    			{
	    	    				perfil = (RegulatorioPerfil)perfiles.get(0);
	    	    				if(huboCambiosPm)
	    	    					perfil.setDatosRegulatoriosActualizados("S");
	    	    				
	    	    				if(huboCambiosInstitucion)
	    	    					perfil.setRegInstitucionActualizados("S");
	    	    				
	    	    				//perfil.setPerfilMdd("S"); // actualizar SC_REG_PERFIL o SC_REG_PM dependiendo en donde quede el campo perfil_mdd
		    					perfil.setFechaUltimaModificacion(new Date());
		    					perfil.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
	    	    				
	    	    				getSicaServiceData().update(perfil);
	    	    			}
		    			}
		    		}
		    		else if(isVerCamposRegulatorios()) // el cliente no tiene info regulatoria y ya fue capturada para el alta
		    		{
		    			RegulatorioPerfil perfil = new RegulatorioPerfil();
		    			String noCuentaSica = null;
		    			
		    			ContratoSica contratoSica = getSicaServiceData().findContratoSicaByIdPersona(getIdPersona());
		    			if(contratoSica != null && StringUtils.isNotEmpty(contratoSica.getNoCuenta()))
		    				noCuentaSica = contratoSica.getNoCuenta();
		    			
		        		perfil.setIdPersona(getIdPersona());
			        	perfil.setContratoSica(noCuentaSica);
			        	perfil.setDatosRegulatoriosActualizados("S");
			        	perfil.setFechaAlta(new Date());
			        	perfil.setFechaUltimaModificacion(perfil.getFechaAlta());
			        	perfil.setIdPersonaAlta(visit.getUser().getIdPersona());
			        	perfil.setIdPersonaUltimaMod(perfil.getIdPersonaAlta());
			        	//perfil.setPerfilMdd("S");
			        	perfil.setRegInstitucionActualizados("S");
			        	
			        	getSicaServiceData().store(perfil);
			        	
			        	RegulatorioDatosPM datosPM = new RegulatorioDatosPM();
			        	datosPM.setIdPersona(getIdPersona());
			        	datosPM.setClaveBanxico(getClaveBanxico().trim());
			        	datosPM.setNombreContraparte(getContraparte().trim());
			        	
			        	datosPM.setSociedadMercantil(new RegulatorioSociedadMercantil(new Integer(getTipoSociedadMercantil().get(VALUE).toString()),
			        								 getTipoSociedadMercantil().get(LABEL).toString(), 
			        								 getTipoSociedadMercantil().get(LABEL + "2").toString()));
			        	
			        	datosPM.setTipoIdentificador(new RegulatorioTipoIdentificador(new Integer(getTipoIdentificador().get(VALUE).toString()),
			        			                     getTipoIdentificador().get(LABEL).toString()));
			        	
			        	datosPM.setClaveIdentificadorContraparte(getClaveIdentificador().trim());
			        	
			        	datosPM.setActividadEconomica(new RegulatorioActividadEconomica(new Integer(getTipoContraparte().get(VALUE).toString()),
			        			                      getTipoContraparte().get(LABEL).toString()));
			        	
			        	datosPM.setClaveLei(getClaveLei().trim());
			        	
			        	datosPM.setSectorIndustrial(new RegulatorioSectorIndustrial(new Integer(getSectorIndustrial().get(VALUE).toString()),
			        			                    getSectorIndustrial().get(LABEL).toString()));
			        	
			        	datosPM.setPaisResidenciaContraparte(new RegulatorioPais(getPaisContraparte().get(VALUE).toString(),
			        											getPaisContraparte().get(LABEL).toString()));
			        	
			        	datosPM.setPaisControlContraparte(new RegulatorioPais(getPaisControlContraparte().get(VALUE).toString(),
			        										getPaisControlContraparte().get(LABEL).toString()));
			        	
			        	datosPM.setClaveLeiMatrizDirecta(getClaveLeiMatrizDirecta().trim());
			        	datosPM.setClaveLeiMatrizUltimaInstancia(getClaveLeiMatrizUltima().trim());
			        	datosPM.setFechaContraparte(DateUtils.inicioDia(getFechaContraparte()));
			        	datosPM.setFechaSistema(new Date());
			        	datosPM.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
			        	datosPM.setContratoSica(noCuentaSica);
			        	datosPM.setPerfilMdd("S");
			        	
			        	getSicaServiceData().store(datosPM);
			        	
			        	RegulatorioInstitucion institucion = new RegulatorioInstitucion();
			        	institucion.setIdPersona(getIdPersona());
			        	institucion.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
			        	institucion.setFechaSistema(new Date());
			        	institucion.setContratoSica(noCuentaSica);
			        	
			        	institucion.setTipoRelacion(new RegulatorioTipoRelacion(new Integer(getTipoRelacion().get(VALUE).toString()),
			        									getTipoRelacion().get(LABEL).toString()) );
			        	
			        	// 3: el usuario no ha seleccionado nada,  1: si 2: no
			        	institucion.setPerteneceGrupoFinanciero(getGrupoFinanciero() == null || getGrupoFinanciero().intValue() == 3 ? 
			        											new Integer(0) : getGrupoFinanciero());
			        	
			        	// Si la contraparte si está relacionada con la institucion  
			        	if(getTipoRelacion() != null && getTipoRelacion().get(VALUE) != null && !getTipoRelacion().get(VALUE).toString().equals("10")) 
			        	{
			        		institucion.setEventoRelacion(new RegulatorioEventoRelacion(new Integer(getTipoEventoRelacion().get(VALUE).toString()),
			        				                                  getTipoEventoRelacion().get(LABEL).toString()));
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
	    			
	    			//60057-CFDI 3.3
		    		NitPersona nitPersona = getSicaServiceData().findNitByIdPersona(getIdPersona());
    		    	if(nitPersona != null) {
    		    		nitPersona.setNit(getNit() != null && !"".equals(getNit().trim()) ? getNit() : "");
    		    		getSicaServiceData().update(nitPersona);
    		    	} else {
    		    		getSicaServiceData().storeNit(getIdPersona(), getNit() != null && !"".equals(getNit().trim()) ? getNit() : "");
    		    	}
    		    	
    		    	
	    			setLevel(1); // exito
	        		getDelegate().record("Se han actualizado correctamente los datos del cliente.", null);
	    		}
	    		else {
	    			setLevel(-1); // indicamos que son errores
	        		getDelegate().record(errores.toString(), null);
	    		}
	    	}
    	}
    	catch (SicaValidationException ex) {
    		setLevel(-1);
    		getDelegate().record(ex.getMessage(), null);
    	}
    	catch (SicaException ex) {
    		setLevel(-1);
    		getDelegate().record("Ocurri\u00f3 un error, favor de contactar a sistemas: " + 
    								ex.getMessage(), null);
    	}
    }
    
    public boolean validarCamposPersona(StringBuffer errores)
    {
    	boolean sonValidos = true;
    	boolean nombreApoderadoVacio = false, paternoApoderadoVacio = false;
    	StringBuffer apoderadoErrores = new StringBuffer();
    	
    	limpiarCamposCliente();
    	
    	if("FISICA".equals(getFisicaMoral())) {
    		setFaltaNombre(true);
        	if(StringUtils.isEmpty(getNombre()) || "".equals(getNombre().trim()))
        		errores.append("El campo nombre no debe estar vac\u00EDo. ");
        	else if(getNombre().length() < 2)
        		errores.append("El campo nombre debe tener como m\u00EDnimo 2 caracteres. ");
        	else if(!getNombre().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*"))
        		errores.append("El campo nombre no debe tener caracteres inv\u00E1lidos. ");
        	else
        		setFaltaNombre(false);
        	
        	setFaltaPaterno(true);
        	if(StringUtils.isEmpty(getPaterno()) || "".equals(getPaterno().trim()))
        		errores.append("El campo apellido paterno no debe estar vac\u00EDo. ");
        	else if(getPaterno().length() < 2)
        		errores.append("El campo apellido paterno debe tener como m\u00EDnimo 2 caracteres. ");
        	else if(!getPaterno().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*"))
        		errores.append("El campo apellido paterno no debe tener caracteres inv\u00E1lidoslidos. ");
        	else
        		setFaltaPaterno(false);
        	
        	if(StringUtils.isNotEmpty(getMaterno()) || !"".equals(getMaterno().trim())) {
        		setFaltaMaterno(true);
        		if(getMaterno().length() < 2)
            		errores.append("El campo apellido materno debe tener como m\u00EDnimo 2 caracteres o este puede ser  vac\u00EDo.");
            	else if(!getMaterno().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*"))
            		errores.append("El campo apellido materno no debe tener caracteres inv\u00E1lidos. ");
            	else
            		setFaltaMaterno(false);
        	}
        	
        	setFaltaSector(true);
        	if(getSectorBanxico() == null || getSectorBanxico().get("value").toString().equals("SELECCIONE"))
        		errores.append("El campo sector econ\u00F3mico debe tener seleccionado una opci\u00F3n v\u00E1lida. ");
        	else
        		setFaltaSector(false);
        	
        	setFaltaGiro(true);
        	if(getGiroEconomico() == null || getGiroEconomico().get("value").toString().equals("SELECCIONE"))
        		errores.append("El campo giro econ\u00F3mico debe tener seleccionado una opci\u00F3n v\u00E1lida. ");
        	else
        		setFaltaGiro(false);
        	
        	setFaltaActividad(true);
        	if(getActividadEconomica() == null || getActividadEconomica().get("value").toString().equals("SELECCIONE"))
        		errores.append("El campo actividad econ\u00F3mica debe tener seleccionado una opci\u00F3n v\u00E1lida. ");
        	else
        		setFaltaActividad(false);
        	
        	setFaltaNacionalidad(true);
        	if(getNacionalidad() == null || getNacionalidad().get(VALUE).equals("SELECCIONE"))
        		errores.append("El campo nacionalidad debe tener seleccionado una opci\u00F3n v\u00E1lida. ");
        	else
        		setFaltaNacionalidad(false);
        	
        	setFaltaRfc(true);
        	if(StringUtils.isEmpty(getRfc()) || "".equals(getRfc().trim()))
        		errores.append("El campo RFC no debe estar vac\u00EDo. ");
        	else
        	{
        		if(getNacionalidad() != null && getNacionalidad().get(VALUE).equals("48"))
        		{
        			String msjRFC = Utilerias.validaRFC(getRfc(), "FISICA".equals(getFisicaMoral()) ? Constantes.ID_TIPO_PERSONA_FISICA : Constantes.ID_TIPO_PERSONA_MORAL);
            		
            		if(msjRFC.length() > 0) 
            		{
            			msjRFC = msjRFC.charAt(0) + msjRFC.substring(1).toLowerCase().replaceAll("rfc", "RFC") + ". ";
            			
            			if ("MORAL".equals(getFisicaMoral())) 
            			{
            				if (msjRFC.indexOf("El RFC proporcionado es inv") != -1)
            				{
            					msjRFC =  msjRFC.replace('5', '4');
            					msjRFC = msjRFC.replaceAll("10", "9");
            				}
            			}
            			
            			errores.append(msjRFC + " ");
            		}
        			else
        				setFaltaRfc(false);
        		}
        		else
        			setFaltaRfc(false);
        	}
        	
        	Date fecha = new Date();
        	if(getFechaNacimiento() != null && fecha.before(getFechaNacimiento()))
        	{
        		errores.append("El campo fecha de nacimiento no debe ser mayor al d\u00EDa en curso.");
        		setFaltaFechaNacimiento(true);
        	}
        	else
        		setFaltaFechaNacimiento(false);
        	
        	if (isNacionalidadMexicana()) {
        		setFaltaIdExtranjero(false);
        		setFaltaCurp(true);
            	if(StringUtils.isEmpty(StringUtils.trim(getCurp())))
            		errores.append("El campo curp no debe estar vac\u00EDo. ");
            	else if(getCurp().length() != 18)
            		errores.append("El campo curp debe tener 18 caracteres. ");
            	else
            		setFaltaCurp(false);
        		
        	}
        	else {
        		setFaltaCurp(false);
        		setFaltaIdExtranjero(true);
        		if(StringUtils.isEmpty(StringUtils.trim(getIdExtranjero())))
            		errores.append("El campo identificador de extranjero no debe estar vac\u00EDo. ");
            	else if(getIdExtranjero().length() != 15)
            		errores.append("El campo identificador de extranjero debe tener 15 caracteres. ");
            	else
            		setFaltaIdExtranjero(false);
        	}
        	
        	setFaltaTipoDocumento(true);
        	if(getTipoDocumento() == null || getTipoDocumento().get(VALUE).equals(String.valueOf(ID_DEFAULT)))
        		errores.append("El campo tipo de comprobante debe tener seleccionado una opci\u00F3n v\u00E1lida. ");
        	else
        		setFaltaTipoDocumento(false);
        	
        	setFaltaNumDocumento(true);
        	if(StringUtils.isEmpty(StringUtils.trim(getNumDocumento())))
        		errores.append("El campo numero de documento no debe estar vac\u00EDo. ");
        	else if(getNumDocumento().length() < 2)
        		errores.append("El campo numero de documento debe tener como m\u00EDnimo 2 caracteres. ");
        	else
        		setFaltaNumDocumento(false);
    	}
    	else
    	{
    		setFaltaRazonSocial(true);
    		if(StringUtils.isEmpty(getRazonSocial()) || "".equals(getRazonSocial().trim()))
    			errores.append("El campo raz\u00F3n social no debe estar vac\u00EDo. ");
    		else if(getRazonSocial().trim().length() < 3)
    			errores.append("El campo raz\u00F3n social debe tener una longitud m\u00EDnima de 3 caracteres. ");
    		else if(!getRazonSocial().trim().matches("[A-Za-z0-9.,& \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*"))
    			errores.append("El campo raz\u00F3n social no debe tener caracteres inv\u00E1lidos. ");
    		else
    			setFaltaRazonSocial(false);
    		
    		setFaltaSector(true);
        	if(getSectorBanxico() == null || getSectorBanxico().get("value").toString().equals("SELECCIONE"))
        		errores.append("El campo sector econ\u00F3mico debe tener seleccionado una opci\u00F3n v\u00E1lida. ");
        	else
        		setFaltaSector(false);
        	
        	setFaltaNacionalidad(true);
        	if(getNacionalidad() == null || getNacionalidad().get(VALUE).equals("SELECCIONE"))
        		errores.append("El campo nacionalidad debe tener seleccionado una opci\u00F3n v\u00E1lida. ");
        	else
        		setFaltaNacionalidad(false);
        	
        	setFaltaRfc(true);
        	if(StringUtils.isEmpty(getRfc()) || "".equals(getRfc().trim()))
        		errores.append("El campo RFC no debe estar vac\u00EDo. ");
        	else
        	{
        		if(getNacionalidad() != null && getNacionalidad().get(VALUE).equals("48"))
        		{
        			String msjRFC = Utilerias.validaRFC(getRfc(), "FISICA".equals(getFisicaMoral()) ? Constantes.ID_TIPO_PERSONA_FISICA : Constantes.ID_TIPO_PERSONA_MORAL);
            		
            		if(msjRFC.length() > 0) 
            		{
            			msjRFC = msjRFC.charAt(0) + msjRFC.substring(1).toLowerCase().replaceAll("rfc", "RFC") + ". ";
            			
            			if ("MORAL".equals(getFisicaMoral())) 
            			{
            				if (msjRFC.indexOf("El RFC proporcionado es inv") != -1)
            				{
            					msjRFC =  msjRFC.replace('5', '4');
            					msjRFC = msjRFC.replaceAll("10", "9");
            				}
            			}
            			
            			errores.append(msjRFC + " ");
            		}
        			else
        				setFaltaRfc(false);
        		}
        		else
        			setFaltaRfc(false);
        	}
        	
        	if (!isNacionalidadMexicana()) {
        		setFaltaIdExtranjero(true);
        		if(StringUtils.isEmpty(StringUtils.trim(getIdExtranjero())))
            		errores.append("El campo identificador de extranjero no debe estar vac\u00EDo. ");
            	else if(getIdExtranjero().length() != 14)
            		errores.append("El campo identificador de extranjero debe tener 14 caracteres. ");
            	else
            		setFaltaIdExtranjero(false);
        	}
        	
        	Date fecha = DateUtils.inicioDia(new Date());
        	if(getFechaNacimiento() != null && fecha.before(DateUtils.inicioDia(getFechaNacimiento())))
        	{
        		errores.append("El campo fecha de constituci\u00F3n no debe ser mayor al d\u00EDa en curso.");
        		setFaltaFechaNacimiento(true);
        	}
        	else
        		setFaltaFechaNacimiento(false);
        	
        	// *********************************** Validaciondes del apoderado ********************************************
        	setFaltaNombreApoderado(true);
        	if(StringUtils.isEmpty(getApoderadoNombre()) || "".equals(getApoderadoNombre().trim()))
        	{
        		nombreApoderadoVacio = true;
        		apoderadoErrores.append("El campo nombre del apoderado no debe estar vac\u00EDo. ");
        	}
        	else if(getApoderadoNombre().length() < 2)
        		apoderadoErrores.append("El campo nombre del apoderado debe tener como m\u00EDnimo 2 caracteres. ");
        	else if(!getApoderadoNombre().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*"))
        		apoderadoErrores.append("El campo nombre del apoderado no debe tener caracteres inv\u00E1lidos. ");
        	else
        		setFaltaNombreApoderado(false);
        	
        	setFaltaPaternoApoderado(true);
        	if(StringUtils.isEmpty(getApoderadoPaterno()) || "".equals(getApoderadoPaterno().trim()))
        	{
        		paternoApoderadoVacio = true;
        		apoderadoErrores.append("El campo apellido paterno del apoderado no debe estar vac\u00EDo. ");
        	}
        	else if(getApoderadoPaterno().length() < 2)
        		apoderadoErrores.append("El campo apellido paterno del apoderado debe tener como m\u00EDnimo 2 caracteres. ");
        	else if(!getApoderadoPaterno().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*"))
        		apoderadoErrores.append("El campo apellido paterno del apoderado no debe tener caracteres inv\u00E1lidoslidos. ");
        	else
        		setFaltaPaternoApoderado(false);
        	
        	setFaltaMaternoApoderado(true);
        	if(StringUtils.isNotEmpty(getApoderadoMaterno()) && !"".equals(getApoderadoMaterno().trim()))
        	{
        		/*if(getApoderadoMaterno().length() < 2)
            		errores.append("El campo apellido materno del apoderado debe tener como m\u00EDnimo 2 caracteres. ");
            	else*/ if(!getApoderadoMaterno().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*"))
            		apoderadoErrores.append("El campo apellido materno del apoderado no debe tener caracteres inv\u00E1lidos. ");
            	else
            		setFaltaMaternoApoderado(false);
        	}
        	else
        		setFaltaMaternoApoderado(false);
        	
        	if(isObligatorioApoderadoLegal())
        	{	
        		if(isFaltaNombreApoderado() || isFaltaPaternoApoderado() || isFaltaMaternoApoderado())
        			errores.append(apoderadoErrores);
        	}
        	else 
        	{
        		if(apoderadoErrores.length() > 0)
        		{
        			// los errores se deben porque los campos estan vacíos, sigue el flujo...
        			if (nombreApoderadoVacio && paternoApoderadoVacio && (StringUtils.isEmpty(getApoderadoMaterno()) || "".equals(getApoderadoMaterno().trim()))) // no se capturaron, continua el flujo
            		{
            			setFaltaNombreApoderado(false);
                		setFaltaPaternoApoderado(false);
                		setFaltaMaternoApoderado(false);
            		}
        			else 
                		errores.append(apoderadoErrores);
        		}
        	}
        	// ********************************************************************************************************************
        	
        	if(isVerCamposRegulatorios())
        	{
        		setFaltaNombreContraparte(true);
        		if(StringUtils.isEmpty(getContraparte()) || "".equals(getContraparte().trim()))
        			errores.append("El campo Contraparte no debe estar vac\u00EDo. ");
        		else
        		{
        			Pattern patron = Pattern.compile("\\p{Punct}"); // se valida !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
            		Matcher matcher = patron.matcher(getContraparte());
            		if(matcher.find())
            			errores.append("El campo Contraparte no debe contener signos de puntuaci\u00f3n o abreviaciones. ");
            		else
            			setFaltaNombreContraparte(false);
        		}
        		
        		setFaltaSociedadMercantil(true);
        		if(getTipoSociedadMercantil() == null || "SELECCIONE".equals(getTipoSociedadMercantil().get(VALUE)))
        			errores.append("El campo Tipo de Sociedad Mercantil debe tener seleccionada una opci\u00F3n v\u00E1lida. ");
        		else
        			setFaltaSociedadMercantil(false);
        		
        		setFaltaIdentificadorContraparte(true);
        		if(getTipoIdentificador() == null || "SELECCIONE".equals(getTipoIdentificador().get(VALUE)))
        			errores.append("El campo Tipo de identificador de la contraparte debe tener seleccionada una opci\u00F3n v\u00E1lida. ");
        		else
        			setFaltaIdentificadorContraparte(false);
        		
        		setFaltaClaveContraparte(true);
        		if(StringUtils.isEmpty(getClaveIdentificador()) || "".equals(getClaveIdentificador().trim()))
        			errores.append("El campo Clave de identificador de la contraparte no debe estar vac\u00EDo. ");
        		else if(getTipoIdentificador() != null && getTipoIdentificador().get(VALUE) != null && 
            			getFisicaMoral().equals("MORAL") && "1".equals(getTipoIdentificador().get(VALUE)) && // 1:RFC
            			(getClaveIdentificador().length() != 9 && getClaveIdentificador().length() != 12)) 
            	{
        			errores.append(" El campo 'Clave de identificador de la contraparte' con opci\u00f3n de RFC " +
            				     "debe tener una longitud de 12 posiciones con homoclave o 9 posiciones sin homoclave para Personas Morales.");
            	}
        		else if(getTipoIdentificador() != null && getTipoIdentificador().get(VALUE) != null && 
            			getFisicaMoral().equals("MORAL") && "1".equals(getTipoIdentificador().get(VALUE)) && // 1:RFC
            			StringUtils.isNotEmpty(getRfc()) && !StringUtils.equalsIgnoreCase(getRfc(), getClaveIdentificador()))
        		{
        			errores.append("Si el campo 'Tipo de identificador de la contraparte' tiene seleccionada la opci\u00F3n de RFC los" +
        					"campos 'RFC' y 'Clave de identificador de la contraparte' no deben ser diferentes. ");
        			setFaltaRfc(true);
        		}
        		else
        			setFaltaClaveContraparte(false);
        		
        		
        		
        		setFaltaTipoContraparte(true);
        		if(getTipoContraparte() == null || getTipoContraparte().get(VALUE) == null || "SELECCIONE".equals(getTipoContraparte().get(VALUE)))
        			errores.append("El campo Tipo de contraparte debe tener seleccionada una opci\u00F3n v\u00E1lida. ");
        		else
        			setFaltaTipoContraparte(false);
        		
        		setFaltaClaveLei(true);
        		if(StringUtils.isEmpty(getClaveLei()) || "".equals(getClaveLei().trim()))
        			errores.append("El campo Clave LEI (Legal Entity Identifier) no debe estar vac\u00EDo. " +
        					       "Capture ND para indicar que no se encuentra disponible. ");
        		else
        			setFaltaClaveLei(false);
        		
        		setFaltaSectorIndustrial(true);
        		if(getSectorIndustrial() == null || "SELECCIONE".equals(getSectorIndustrial().get(VALUE)))
        			errores.append("El campo Sector Industrial (SCIAN) debe tener seleccionada una opci\u00F3n v\u00E1lida. ");
        		else
        			setFaltaSectorIndustrial(false);
        		
        		setFaltaPaisControl(true);
        		if(getPaisControlContraparte() == null || "SELECCIONE".equals(getPaisControlContraparte().get(VALUE)))
        			errores.append("El campo Pa\u00EDs de residencia de quien tiene el control de la contraparte debe " +
        					"tener seleccionada una opci\u00F3n v\u00E1lida. ");
        		else
        			setFaltaPaisControl(false);
        		
        		setFaltaPaisContraparte(true);
        		if(getPaisContraparte() == null || "SELECCIONE".equals(getPaisContraparte().get(VALUE)))
        			errores.append("El campo Pa\u00EDs de residencia de la contraparte debe tener seleccionada una opci\u00F3n v\u00E1lida. ");
        		else
        			setFaltaPaisContraparte(false);
        		
        		setFaltaClaveLeiDirecta(true);
        		if(StringUtils.isEmpty(getClaveLeiMatrizDirecta()) || "".equals(getClaveLeiMatrizDirecta().trim()))
        			errores.append("El campo Clave LEI de la matriz directa no debe estar vac\u00EDo -- " +
        					"Capture NA si no se tiene la clave LEI de la matriz directa ");
        		else
        			setFaltaClaveLeiDirecta(false);
        		
        		setFaltaClaveLeiUltima(true);
        		if(StringUtils.isEmpty(getClaveLeiMatrizUltima()) || "".equals(getClaveLeiMatrizUltima().trim()))
        				errores.append("El campo Clave LEI de la matriz en \u00FAltima instancia de la contraparte no debe estar vac\u00EDo." +
        					           " Capture NA si no se tiene disponible esta clave");
        		else
        			setFaltaClaveLeiUltima(false);
        		
        		setFaltaFechaContraparte(true);
        		if(getFechaContraparte() == null)
        			errores.append("El campo 'Fecha de alta o modificaci\u00f3n de la contraparte' no debe estar vac\u00EDo.");
            	else
            	{
            		if(getFechaContraparte().after(new Date()))
            			errores.append(" El campo 'Fecha de alta o modificaci\u00f3n de la contraparte' " +
            						       "no debe ser mayor al d\u00EDa en curso.");
            		else
            			setFaltaFechaContraparte(false);
            	}
        		
        		setFaltaTipoRelacion(true);
        		if(getTipoRelacion() == null || getTipoRelacion().get(LABEL) == null || getTipoRelacion().get(VALUE) == null ||
	        	   "SELECCIONE LA OPCION DESEADA".equals(getTipoRelacion().get(LABEL)))
	        	{
        			errores.append("El campo 'Tipo de relaci\u00f3n' debe tener seleccionada una opci\u00F3n v\u00E1lida. ");
	        	}
        		else
        			setFaltaTipoRelacion(false);
	        	
        		// la contraparte si está relacionada con la institucion  -- 10:no hay relación
	        	if(getTipoRelacion() != null && getTipoRelacion().get(VALUE) != null && 
	        		 !"10".equals(getTipoRelacion().get(VALUE)) && !"SELECCIONE".equals(getTipoRelacion().get(VALUE))) 
	        	{
	        		setFaltaGrupoFinanciero(true);
	        		if(getGrupoFinanciero() == null || getGrupoFinanciero().intValue() == 3 || getGrupoFinanciero().intValue() == 0) // 0 ó 3:no se ha seleccionado un radion button
	        			errores.append(" El campo '¿Pertenece al Grupo Financiero?' debe tener seleccionada una opci\u00f3n v\u00E1lida.");
	        		else
	        			setFaltaGrupoFinanciero(false);
	        		
	        		setFaltaEventoRelacion(true);
	        		if(getTipoEventoRelacion() == null || getTipoEventoRelacion().get(LABEL) == null ||
	        		   "SELECCIONE LA OPCION DESEADA".equals(getTipoEventoRelacion().get(LABEL)))
	        			errores.append("El campo 'Tipo de evento en la realci\u00f3n' debe tener seleccionada una opci\u00F3n v\u00E1lida. ");
	        		else
	        			setFaltaEventoRelacion(false);
	        		
	        		setFaltaFechaRelacion(true);
	        		if(getFechaEventoRelacion() == null)
	        			errores.append("El campo 'Fecha del evento de la relaci\u00f3n' no debe estar vac\u00EDo. ");
	            	else
	            	{
	            		if(getFechaEventoRelacion().after(new Date()))
	            			errores.append(" El campo 'Fecha del evento de la relaci\u00f3n' " +
	            						"no debe ser mayor al d\u00EDa en curso.");
	            		else
	            			setFaltaFechaRelacion(false);
	            	}
	        	}
        	}
    	}
    	
    	//60057-CFDI 3.3
    	setFaltaNit(true);
    	seRequiereNit(getIdPersona());
    	String nit = getNit();
    	Pattern pattern = Pattern.compile("\\d{9}");
    	if ( isRequiereNit() && (nit == null || "".equals(nit.trim()) || !pattern.matcher(nit).matches())){
    		errores.append(" Si cuenta con al menos una direcci\u00f3n fiscal cuyo pa\u00EDs sea Canad\u00E1 o Estados Unidos es necesario se proporcione el campo NIT con formato de 9 d\u00EDgitos.");
    	} else if(!isRequiereNit() && nit != null && !"".equals(nit.trim()) && !nit.matches("[A-Za-z0-9]*")) {
    		errores.append("El campo NIT no debe tener caracteres inv\u00E1lidos. ");
    	} else {
    		setFaltaNit(false);
    	}
    	
    	
    	if(errores.length() > 0)
    		sonValidos = false;
    		
    	return sonValidos;
    }
    
    public boolean isObligatorioApoderadoLegal()
    {
    	boolean result = true;
    	ParametroSica parametro = null;
    	final String OPCIONAL = "0";
    	
    	parametro = getSicaServiceData().findParametro(ParametroSica.CAPTURA_OBLIGATORIA_APODERADO);
    	if(parametro == null || StringUtils.isEmpty(parametro.getValor()) || OPCIONAL.equals(parametro.getValor()))
    		result = false;
    	
    	return result;
    }
    
    public void actualizarComboActividadEconomica(IRequestCycle cycle)
    {
    	//List actividades = null;
    	//Map actividadMap = null;
    	setModoActualizar(false);
    	
    	/*if(!getGiroEconomico().get(VALUE).equals("SELECCIONE"))
    	{
			LinkedHashMap map = getSicaConsultaCatalogosService().obtenCatActividadGiro(getGiroEconomico().get(VALUE).toString());
			actividades = new ArrayList();

		    String key = null;
		    actividadMap = new HashMap();
		    actividadMap.put(VALUE, "SELECCIONE");
		    actividadMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
		    actividades.add(actividadMap);
		    if(map != null)
		    {
		    	for (Iterator i = map.keySet().iterator(); i.hasNext();) 
			    {
			        key = (String) i.next();
			        actividadMap = new HashMap();
			        actividadMap.put(VALUE, key);
			        actividadMap.put(LABEL, (String)map.get(key));
			        
			        actividades.add(actividadMap);
			    }
		    	
		    	setActividadEconomicaModel(new RecordSelectionModel(actividades, VALUE, LABEL));
		    }
    	}
    	else
    	{
    		actividades = new ArrayList();
    		actividadMap = new HashMap();
    		actividadMap.put(VALUE, "SELECCIONE");
		    actividadMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
		    actividades.add(actividadMap);
		    setActividadEconomicaModel(new RecordSelectionModel(actividades, VALUE, LABEL));
    	}*/
    	cambiaNacionalidad();
    }
    
    /**
     * Cambia nacionalidad.
     */
    private void cambiaNacionalidad() {
    	if (getNacionalidad() != null) {
    		String idPais = (String) getNacionalidad().get(VALUE);
    		if (Constantes.ID_PAIS_MEXICO.equals(idPais)) {
    			setNacionalidadMexicana(true);
    		}
    		else {
    			setNacionalidadMexicana(false);
    		}
    	}
   
    	_logger.debug("NacionalidadMexicana: " + isNacionalidadMexicana());
    }
    
    /**
     * Mostrar datos cliente.
     *
     * @param cycle the cycle
     */
    public void mostrarDatosCliente(IRequestCycle cycle) {
    	Integer idPersona = new Integer((cycle.getServiceParameters()[0]).toString());
    	setModoActualizar(false);
    	PersonaDto dto = getPersonaService().buscarPersonaPorId(idPersona);
    	Persona persona = dto.getPersona();
    	setIdPersona(idPersona);
    	if (StringUtils.isNotEmpty(persona.getIdPais())) {
    		Map nacionalidad = new HashMap();
    		nacionalidad.put(VALUE, persona.getIdPais());
    		nacionalidad.put(LABEL, persona.getNacionalidad());
    		setNacionalidad(nacionalidad);
    	}
    	setNacionalidadMexicana(dto.isNacionalidadMexicana());
    	setRfc(persona.getRfc());
    	if (persona.getVerificaFE() > 0) {
    		setFacturacionElectronica(true);
    	}
    	else {
    		setFacturacionElectronica(false);
    	}
    	setFechaNacimiento(dto.getFecha());
    	setIdExtranjero(dto.getIdExtranjero());
    	if (persona.getIdSector() > 0) {
    		Map sector = new HashMap();
    		sector.put(VALUE, String.valueOf(persona.getIdSector()));
    		sector.put(LABEL, persona.getDescSector());
    		setSectorBanxico(sector);
    		debug("Sector: " + getSectorBanxico());
    	}
    	if (StringUtils.isNotEmpty(persona.getIdActividad())) {
    		Map actividad = new HashMap();
    		actividad.put(VALUE, persona.getIdActividad());
    		String desc = StringUtils.trim(persona.getDescActividad());
    		actividad.put(LABEL, desc);
    		setActividadEconomica(actividad);
    		debug("ActividadEconomica: " + getActividadEconomica());
    	}
    	if (dto.getActividadGiro() != null) {
    		Map giro = new HashMap();
    		giro.put(VALUE, dto.getActividadGiro().getId());
    		giro.put(LABEL, dto.getActividadGiro().getDescripcion());
    		setGiroEconomico(giro);
    		debug("GiroEconomico: " + getGiroEconomico());
    	}
    	if (persona instanceof PersonaFisica) {
    		PersonaFisica pf = (PersonaFisica) dto.getPersona();
    		setFisicaMoral("FISICA");	
    		setNombre(pf.getNombre());
    		setPaterno(pf.getPaterno());
    		setMaterno(pf.getMaterno());
    		if (dto.getPaisNacimiento() != null) {
    			Map pais = new HashMap();
    			pais.put(VALUE, dto.getPaisNacimiento().getId());
    			pais.put(LABEL, dto.getPaisNacimiento().getDescripcion());
    			setPaisNacimiento(pais);
    			debug("Pais de nacimiento: " + getPaisNacimiento());
    		}
    		setLugarNacimiento(pf.getLugarNacimiento());
    		if (StringUtils.isNotEmpty(pf.getLugarNacimiento())) {
    			setLugarNacimiento(pf.getLugarNacimiento());
    		}
    		if (StringUtils.isNotEmpty(pf.getIdEstadoCivil())) {
    			Map estado = new HashMap();
    			estado.put(VALUE, pf.getIdEstadoCivil());
    			estado.put(LABEL, pf.getDescEstadoCivil());
    			setEstadoCivil(estado);
    		}
    		if (StringUtils.isNotEmpty(pf.getSexo())) {
    			setSexo(pf.getSexo());
    		}
    		setCurp(pf.getCurp());
			if (StringUtils.isNotEmpty(pf.getIdTipoIdentifica())) {
				Map tipo = new HashMap();
				tipo.put(VALUE, pf.getIdTipoIdentifica());
				tipo.put(LABEL, pf.getDescTipoIdentifica());
				setTipoDocumento(tipo);
			}
			if (StringUtils.isNotEmpty(pf.getNoIdentificacion())) {
				setNumDocumento(pf.getNoIdentificacion());
			}
    	}
    	else  {
    		PersonaMoral pm = (PersonaMoral) dto.getPersona();
    		setFisicaMoral("MORAL");
    		setRazonSocial(pm.getRazonSocial());
    		buscarApoderadoDatosRegulatorios(idPersona);
    	}
    	setSeccion(VER_SECCION_MODIFICACION_CLIENTES);
    	
    	//60057-CFDI 3.3
    	NitPersona nitPersona = getSicaServiceData().findNitByIdPersona(getIdPersona());
    	if(nitPersona != null && nitPersona.getNit() != null) {
			setNit(nitPersona.getNit());
    	}
    	seRequiereNit(idPersona);
    	existeDireccionPaisExtranjero(idPersona);
			
    }
    
    /**
     * valida si se requiere la captura del NIT.
     */
    public void seRequiereNit(Integer idPersona) {
    	setRequiereNit(false);
    	PersonaDto dto = getPersonaService().buscarPersonaPorId(idPersona);
    	Persona persona = dto.getPersona();
    	List direcciones = (List)getSicaConsultaDireccionesPersonaService().obtenDirecciones(persona, Constantes.ID_RES_ARRAY_LIST);
    	for (Iterator it = direcciones.iterator(); it.hasNext();) {
			Direccion direccion = (Direccion) it.next();
			if (("CANADÁ".equals(direccion.getDescPais()) || "ESTADOS UNIDOS".equals(direccion.getDescPais()))
					&& direccion.getEsDirFiscal().booleanValue()) {
				setRequiereNit(true);
			}
		}
    }

    /**
     * valida si existe al menos 1 dirección de país extranjero
     */
    public void existeDireccionPaisExtranjero (Integer idPersona) {
    	setDireccionPaisExtranjero(false);
    	PersonaDto dto = getPersonaService().buscarPersonaPorId(idPersona);
    	Persona persona = dto.getPersona();
    	List direcciones = (List)getSicaConsultaDireccionesPersonaService().obtenDirecciones(persona, Constantes.ID_RES_ARRAY_LIST);
    	for (Iterator it = direcciones.iterator(); it.hasNext();) {
			Direccion direccion = (Direccion) it.next();
			if ((!"MEXICO".equals(direccion.getDescPais()) && !"MÉXICO".equals(direccion.getDescPais()))) {
				setDireccionPaisExtranjero(true);
			}
		}
    }

    public void mostrarDatosClienteAnt(IRequestCycle cycle)
    {
    	Integer idPersona = new Integer((cycle.getServiceParameters()[0]).toString());
    	//String nombreCompleto = (cycle.getServiceParameters()[1]).toString();
    	String rfc = (cycle.getServiceParameters()[2]).toString();
    	setFisicaMoral("PM".equals(cycle.getServiceParameters()[3]) ? "MORAL" : "FISICA");
    	com.ixe.ods.bup.model.Persona persona = null; 
    	com.ixe.ods.bup.model.PersonaFisica pf = null;
    	com.ixe.ods.bup.model.PersonaMoral pm = null;
    	
    	setModoActualizar(false);
    	
    	if("FISICA".equals(getFisicaMoral()))
    	{
    		setFisicaMoral("FISICA");
    		persona = getSicaServiceData().findPersonaFisicaByIdPersona(idPersona);
    		pf = (com.ixe.ods.bup.model.PersonaFisica)persona;
    		setNombre(pf.getNombre());
    		setPaterno(pf.getPaterno());
    		setMaterno(pf.getMaterno());
    		setRfc(rfc);
    		setFechaNacimiento(pf.getFechaNacimiento());
    		
    		if(pf.getSectorEconomico() != null)
			{
				Map sectorMap = new HashMap();
				sectorMap.put(VALUE, pf.getSectorEconomico().getIdSector().toString());
			    sectorMap.put(LABEL, pf.getSectorEconomico().getDescripcion());
				setSectorBanxico(sectorMap);
				//_logger.warn(arg0)("pf.getSectorEconomico().getIdSector(): " + pf.getSectorEconomico().getIdSector());
				//_logger.debug("pf.getSectorEconomico().getDescripcion(): " + pf.getSectorEconomico().getDescripcion());
			}
    		
    		if(pf.getActividadEconomica() != null && StringUtils.isNotEmpty(pf.getActividadEconomica().getIdActividad()))
			{
				Integer idGiroEconomico = getSicaServiceData().
				                          findGiroEconomicoAsociadoBupActividadEconomica(pf.getActividadEconomica().getIdActividad());
				
				if(idGiroEconomico != null)
				{
					LinkedHashMap giros = getSicaConsultaCatalogosService().obtenCatGiroEconomico();
					if(giros.containsKey(idGiroEconomico.toString()))
					{
						Map giroMap = new HashMap();
	    				giroMap.put(VALUE, idGiroEconomico.toString());
	    			    giroMap.put(LABEL, giros.get(idGiroEconomico.toString()));
	    				setGiroEconomico(giroMap);
					}
					
					LinkedHashMap map = getSicaConsultaCatalogosService().obtenCatActividadGiro(idGiroEconomico.toString());
					List actividades = new ArrayList();
					Map actividadMap = new HashMap();

				    String key = null;
				    
				    actividadMap.put(VALUE, "SELECCIONE");
				    actividadMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
				    actividades.add(actividadMap);
				    if(map != null)
				    {
				    	for (Iterator i = map.keySet().iterator(); i.hasNext();) 
					    {
					        key = (String) i.next();
					        actividadMap = new HashMap();
					        actividadMap.put(VALUE, key);
					        actividadMap.put(LABEL, (String)map.get(key));
					        
					        actividades.add(actividadMap);
					        
					        if(key.equals(pf.getActividadEconomica().getIdActividad()))
					        	setActividadEconomica(actividadMap);
					    }
				    	
				    	//setActividadEconomicaModel(new RecordSelectionModel(actividades, VALUE, LABEL));
				    }
				}
			}
    		
			_logger.debug("pf.getNacionalidad(): " + pf.getNacionalidad());
			if(pf.getNacionalidad() != null)
			{
				Vector nacionalidadTmp = getSicaConsultaCatalogosService().obtenCatNacionalidad();
			    Map nacionalidad = null;
			    
			    for (Iterator i = nacionalidadTmp.iterator(); i.hasNext(); ) 
			    {
			        Catalogo nac = (Catalogo) i.next();
			        if (nac.getDescripcion() .equals(pf.getNacionalidad())) 
			        {
			        	_logger.debug("Se encontro la nacionalidad (nac): " + nac.getDescripcion() + ", id: " + nac.getId());
			        	nacionalidad = new HashMap();
				        nacionalidad.put(VALUE, nac.getId());
				        nacionalidad.put(LABEL, nac.getDescripcion());
			        	setNacionalidad(nacionalidad);
			        	break;
			        }
			    }			
			}
			
			if(pf.getPaisNacimiento() != null)
			{
				Map pais = new HashMap();
				pais.put(VALUE, pf.getPaisNacimiento().getIdPais());
				pais.put(LABEL, pf.getPaisNacimiento().getDescripcion());

				setPaisNacimiento(pais);
			}
			
			if(StringUtils.isNotEmpty(pf.getLugarNacimiento()))
				setLugarNacimiento(pf.getLugarNacimiento());
			
			if(pf.getEstadoCivil() != null)
			{
				Map edoCivil = new HashMap();
				edoCivil.put(VALUE, pf.getEstadoCivil().getIdEstadoCivil());
				edoCivil.put(LABEL, pf.getEstadoCivil().getDescripcion());
				
				setEstadoCivil(edoCivil);
			}
			
			if(StringUtils.isNotEmpty(pf.getSexo()))
				setSexo(pf.getSexo());
			
			if(persona.getVerificaFe() != null && persona.getVerificaFe().intValue() > 0)
				setFacturacionElectronica(true);
			else
				setFacturacionElectronica(false);
			
			setIdPersona(idPersona);
			setSeccion(VER_SECCION_MODIFICACION_CLIENTES);
    	}
    	else
    	{
    		persona = getSicaServiceData().findPersonaMoralByIdPersona(idPersona);
    		pm = (com.ixe.ods.bup.model.PersonaMoral)persona;
    		
    		setRazonSocial(pm.getRazonSocial());
    		
    		if(pm.getSectorEconomico() != null)
			{
				Map sectorMap = new HashMap();
				sectorMap.put(VALUE, pm.getSectorEconomico().getIdSector().toString());
			    sectorMap.put(LABEL, pm.getSectorEconomico().getDescripcion());
				setSectorBanxico(sectorMap);
			}
    		
    		if(pm.getNacionalidad() != null)
			{
				Vector nacionalidadTmp = getSicaConsultaCatalogosService().obtenCatNacionalidad();
			    Map nacionalidad = null;
			    
			    for (Iterator i = nacionalidadTmp.iterator(); i.hasNext(); ) 
			    {
			        Catalogo nac = (Catalogo) i.next();
			        if (nac.getDescripcion() .equals(pm.getNacionalidad())) 
			        {
			        	_logger.debug("Se encontro la nacionalidad (nac): " + nac.getDescripcion() + ", id: " + nac.getId());
			        	nacionalidad = new HashMap();
				        nacionalidad.put(VALUE, nac.getId());
				        nacionalidad.put(LABEL, nac.getDescripcion());
			        	setNacionalidad(nacionalidad);
			        	break;
			        }
			    }			
			}
    		
    		setRfc(rfc);
    		setFechaNacimiento(pm.getFechaConsitucion());
    		if(persona.getVerificaFe() != null && persona.getVerificaFe().intValue() > 0)
				setFacturacionElectronica(true);
			else
				setFacturacionElectronica(false);
    		
    		buscarApoderadoDatosRegulatorios(idPersona);
    		
    		setIdPersona(idPersona);
			setSeccion(VER_SECCION_MODIFICACION_CLIENTES);
    	}
    }
    
    /**
     * Verifica la existencia de un Cliente especificado por el usuario en la BUP antes de agregarlo
     * como nuevo.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void agregar(IRequestCycle cycle) throws ContratacionException {
        setLevel(0);
        if (getEmpresa()!=null){
        	setNombreEmpresa((String)getEmpresa().get("value"));
        }
        else
        {
        	setNombreEmpresa("");
        }
        
        setPersonasBUP(new ArrayList());
        convertirAUpperCase();
        if (cycle.getServiceParameters().length > 1) {
        	setModoSubmit(new Integer((cycle.getServiceParameters()[0]).toString()).intValue());
        }

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
    		return true;
    	}
    	return false;
    }
    
    /**
     * @param cycle                    Ciclo de la P\u00E1gina
     * @throws ContratacionException   Excepci\u00F3n lanzada por el m\u00F3dulo de
     * Contrataci\u00F3n
     */
    public void agregarNuevoCliente(IRequestCycle cycle) throws ContratacionException {
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
    	if(!validarCaracteresCampos()){
    		msjError += "Verifique que los campos marcados no contengan caracteres especiales. ";
    		if(getEmpresa().get(VALUE).equals(EmpresaCambios.ID_EMPRESA_BANORTE))
    			msjError += "Cada campo del cliente Banorte solo debe contener d\u00EDgitos. ";
    	}
    	if(getNacionalidad().get(VALUE).equals(String.valueOf(Num.I_48))){
    		String msjRFC = Utilerias.validaRFC(getRfc(), "FISICA".equals(getFisicaMoral()) ? Constantes.ID_TIPO_PERSONA_FISICA : Constantes.ID_TIPO_PERSONA_MORAL);
    		
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
    		
    		if("SELECCIONE".equals(getSectorBanxico().get(VALUE).toString())){
    			msjError += "Debe seleccionar un valor para Sector econ\u00f3mico. ";
    		}
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
    	
    	if(StringUtils.isEmpty(getApoderadoNombre()) || "".equals(getApoderadoNombre().trim()))
    		msjError += " El nombre del apoderado legal no debe estar en blanco.";

    	if(StringUtils.isEmpty(getApoderadoPaterno()) || "".equals(getApoderadoPaterno().trim()))
    		msjError += " El apellido paterno del apoderado legal no debe estar en blanco.";
    	
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
        	
        	if(getTipoSociedadMercantil() == null || getTipoSociedadMercantil().get(LABEL) == null ||
        	   "SELECCIONE LA OPCION DESEADA".equals(getTipoSociedadMercantil().get(LABEL)))
        	{
        		camposDefault.add("'Tipo de Sociedad Mercantil'");
        	}
        	
        	if(getTipoIdentificador() == null || getTipoIdentificador().get(LABEL) == null ||
        	   "SELECCIONE LA OPCION DESEADA".equals(getTipoIdentificador().get(LABEL)))
        	{
        		camposDefault.add("'Tipo identificador de la contraparte'");;
        	}
        	
        	if(StringUtils.isEmpty(getClaveIdentificador()))
        	{
        		camposEnBlanco.add("'Clave de identificador de la contraparte'");
        	}
        	else if(getTipoIdentificador() != null && getTipoIdentificador().get(VALUE) != null && 
        			getFisicaMoral().equals("MORAL") && getTipoIdentificador().get(VALUE).toString().equals("1") && // 1:RFC
        			(getClaveIdentificador().length() != 9 && getClaveIdentificador().length() != 12)) 
        	{
        		camposEnBlanco.add("'Clave de identificador de la contraparte'");
        		notas.append(" El campo 'Clave de identificador de la contraparte' con opci\u00f3n de RFC " +
        				     "debe tener una longitud de 12 posiciones con homoclave o 9 posiciones sin homoclave para Personas Morales.");
        	}
        	
        	if(getTipoContraparte() == null || getTipoContraparte().get(LABEL) == null ||
        	   "SELECCIONE LA OPCION DESEADA".equals(getTipoContraparte().get(LABEL)))
        	{
        		camposDefault.add("'Tipo contraparte'");
        	}
        	
        	if(StringUtils.isEmpty(getClaveLei()))
        		camposEnBlanco.add("'Clave LEI (Legal Entity Identifier)' -- Capture ND para indicar que no se encuentra disponible");
        	
        	if(getSectorIndustrial() == null || getSectorIndustrial().get(LABEL) == null ||
        	   "SELECCIONE LA OPCION DESEADA".equals(getSectorIndustrial().get(LABEL)))
        	{
        		camposDefault.add("'Sector Industrial (SCIAN)'");
        	}
        	
        	if(getPaisControlContraparte() == null || getPaisControlContraparte().get(LABEL) == null ||
        	   "SELECCIONE LA OPCION DESEADA".equals(getPaisControlContraparte().get(LABEL)))
        	{
        		camposDefault.add("'Pa\u00EDs de residencia de quien tiene el control de la contraparte'");
        	}
        	
        	if(getPaisContraparte() == null || getPaisContraparte().get(LABEL) == null ||
        	   "SELECCIONE LA OPCION DESEADA".equals(getPaisContraparte().get(LABEL)))
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
        	
        	if(getTipoRelacion() == null || getTipoRelacion().get(LABEL) == null || getTipoRelacion().get(VALUE) == null ||
        	   "SELECCIONE LA OPCION DESEADA".equals(getTipoRelacion().get(LABEL)))
        	{
        		camposDefault.add("'Tipo de relaci\u00f3n'");
        	}
        	
        	if(getTipoRelacion() != null && getTipoRelacion().get(VALUE) != null && !getTipoRelacion().get(VALUE).toString().equals("10")) // la contraparte si está relacionada con la institucion  -- 10:no hay relación
        	{// getTipoRelacion() != null && getTipoRelacion().getId().intValue() == 10
        		if(getGrupoFinanciero() == null || getGrupoFinanciero().intValue() == 3) // 3:no se ha seleccionado un radion button
        			notas.append(" El campo '¿Pertenece al Grupo Financiero?' debe tener seleccionada una opci\u00f3n v\u00E1lida.");
        		
        		if(getTipoEventoRelacion() == null || getTipoEventoRelacion().get(LABEL) == null ||
        		   "SELECCIONE LA OPCION DESEADA".equals(getTipoEventoRelacion().get(LABEL)))
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
    	if(getNacionalidad().get(VALUE).equals(String.valueOf(Num.I_48))){
    	personasBUPTmp = getBupServiceData().findPersons("", "",
                "", "", "", "FISICA".equals(getFisicaMoral()) ? 
                		getRfc().substring(0, 10) : getRfc(),
                		"", "", MAX_RESULTS);
    	}
    	
        if (personasBUPTmp != null && !personasBUPTmp.isEmpty()) {
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
        			        	Map nac = new HashMap();
        			        	nac.put(sector.getId(), sector.getDescripcion());
        			        	setNacionalidad(nac);
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
        			        	Map nac = new HashMap();
        			        	nac.put(sector.getId(), sector.getDescripcion());
        			        	setNacionalidad(nac);
        			        	break;
        			        }
        			    }
    				}
				}
			}
    	}
    	return;
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
								setIdPersonaApoderado(pf.getIdPersona());
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
		Map dato = null;
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
			
			dato = new HashMap();
			dato.put(VALUE, datosPM.getSociedadMercantil().getId().toString());
			dato.put(LABEL, datosPM.getSociedadMercantil().getDescripcionCorta());
			dato.put(LABEL+"2", datosPM.getSociedadMercantil().getDescrpcionLarga());
			setTipoSociedadMercantil(dato);
			
			dato = new HashMap();
			dato.put(VALUE, datosPM.getTipoIdentificador().getId().toString());
			dato.put(LABEL, datosPM.getTipoIdentificador().getDescripcion());
			setTipoIdentificador(dato);
			
			setClaveIdentificador(datosPM.getClaveIdentificadorContraparte());
			
			dato = new HashMap();
			dato.put(VALUE, datosPM.getActividadEconomica().getId().toString());
			dato.put(LABEL, datosPM.getActividadEconomica().getDescripcion());
			setTipoContraparte(dato);
			
			setClaveLei(datosPM.getClaveLei());
			
			dato = new HashMap();
			dato.put(VALUE, datosPM.getSectorIndustrial().getId().toString());
			dato.put(LABEL, datosPM.getSectorIndustrial().getDescripcion());
			setSectorIndustrial(dato);
			
			dato = new HashMap();
			dato.put(VALUE, datosPM.getPaisControlContraparte().getClaveIso());
			dato.put(LABEL, datosPM.getPaisControlContraparte().getNombrePais());
			setPaisControlContraparte(dato);
			
			dato = new HashMap();
			dato.put(VALUE, datosPM.getPaisResidenciaContraparte().getClaveIso());
			dato.put(LABEL, datosPM.getPaisResidenciaContraparte().getNombrePais());
			setPaisContraparte(dato);
			
			setClaveLeiMatrizDirecta(datosPM.getClaveLeiMatrizDirecta());
			setClaveLeiMatrizUltima(datosPM.getClaveLeiMatrizUltimaInstancia());
			setFechaContraparte(datosPM.getFechaContraparte());
			
			RegulatorioInstitucion institucion = null;
			datos = getSicaServiceData().findRegulatorioInstitucion(idPersona);
			if(datos != null && datos.size() > 0)
			{
				institucion = (RegulatorioInstitucion)datos.get(0);
				
				dato = new HashMap();
				dato.put(VALUE, institucion.getTipoRelacion().getId().toString());
				dato.put(LABEL, institucion.getTipoRelacion().getDescripcion());
				setTipoRelacion(dato);
				
				setGrupoFinanciero(institucion.getPerteneceGrupoFinanciero());
				if(institucion.getEventoRelacion() != null)
				{
					dato = new HashMap();
					dato.put(VALUE, institucion.getEventoRelacion().getId().toString());
					dato.put(LABEL, institucion.getEventoRelacion().getDescripcion());
					setTipoEventoRelacion(dato);
				}
				if(institucion.getFechaEvento() != null)
					setFechaEventoRelacion(institucion.getFechaEvento());
			}
		}
		else
		{
			// No tiene info regulatoria
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
    	if (!camposBusquedaValidos()) 
    	{
    		setModoBusqueda(true);
    		getDelegate().record("Debe ingresar alg\u00FAn campo, cuya longitud sea " +
    				"de al menos 3 caracteres", null);
        }
    	else if(!validarCaracteresCamposBusqueda())
    	{
    		setModoBusqueda(true);
    		getDelegate().record("Verifique que los campos no contengan caracteres especiales", null);
    	}
    	else 
    	{
    			List personasBUPTmp = getBupServiceData().findPersons(getRazonSocial(), getNombre(),
    					getPaterno(), getMaterno(), "", getRfc(), "", getNoCuenta(), MAX_RESULTS);
    			if (personasBUPTmp != null && !personasBUPTmp.isEmpty()) 
    			{
    				String mensajeMaxResults = "";
    				if (personasBUPTmp.size() >= MAX_RESULTS) 
    				{
    					mensajeMaxResults = MENSAJE_MAX_RESULTS ;
    					MessageFormat formatter = new MessageFormat( mensajeMaxResults );
    					Object [] messageArguments = {""+ MAX_RESULTS };           		
    					mensajeMaxResults = formatter.format(messageArguments);
    				}
    				List personasBUP = new ArrayList();
    				for (Iterator it = personasBUPTmp.iterator(); it.hasNext(); ) 
    				{
    					com.ixe.ods.bup.model.Persona personaBUP = (com.ixe.ods.bup.model.Persona) it.next();
    					Integer idPersona = personaBUP.getIdPersona();
    					String nombreCompleto = personaBUP.getNombreCompleto();
    					String rfc = personaBUP.getRfc() != null ? personaBUP.getRfc().trim() : "";
    					HashMap row = new HashMap();
    					
    					/*ContratoSica contratoSica = getSicaServiceData().
    					findContratoSicaByIdPersona(idPersona);
    					
    					Visit visit = (Visit) getVisit();
    					
    					if (contratoSica != null) 
    					{
    						
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
    					}*/
    					
    					row.put("idPersona", idPersona);
    					row.put("nombreCompleto", nombreCompleto);
    					row.put("rfc", rfc);
    					
    					if(personaBUP.getTipoPersona() != null)
    					{
    						if("PM".equals(personaBUP.getTipoPersona()))
    							row.put("tipoPersona", personaBUP.getTipoPersona());
    						else
    							row.put("tipoPersona", "PF");
    					}
    					else
    						row.put("tipoPersona", personaBUP.getTipoPersona());
    					
    					personasBUP.add(row);
    				}
    				setLevel(1);
    				setPersonasBUP(personasBUP);
    				setClavesReferencia(new HashMap());
					
    				setSeccion(VER_SECCION_CLIENTES);
    				setModoBusqueda(false);
    				
    				if(StringUtils.isEmpty(mensajeMaxResults))
    					mensajeMaxResults = "Se encontraron " + personasBUP.size() + " registros que coinciden con los criterios proporcionados: ";
    				
                    getDelegate().record(mensajeMaxResults
							+ " "+ getRazonSocial() +" "+ getNombre() +" "+ getPaterno() 
							+ " "+ getMaterno(),	null);
    			}
    			else 
    			{
    				setSeccion(VER_SECCION_BUSQUEDA);
    				setModoBusqueda(false);
    				setPersonasBUP(new ArrayList());
    				getDelegate().record("No se encontr\u00f3 el Cliente con los datos proporcionados.", null);
    			}
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
	        direccion.setIdPais(getPais().get(VALUE).toString());
	        
	      
	        
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
                personaFisica.setIdPais(getNacionalidad().get(VALUE).toString());
                
	        	personaFisica.setNacionalidad(getNacionalidad().get(LABEL).toString());
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
	        	personaMoral.setNacionalidad(getNacionalidad().get(LABEL).toString());
	        	personaMoral.setFechaConstitucion(getFechaNacimientoString());
	        	personaMoral.setDireccion(direccion);
	        	personaMoral.setIdPais(getNacionalidad().get(VALUE).toString());
	        	
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
	        	pfApoderadoLegal.setNombre(getApoderadoNombre());
	        	pfApoderadoLegal.setPaterno(getApoderadoPaterno());
	        	pfApoderadoLegal.setMaterno((!getApoderadoMaterno().equals("")) ? getMaterno() : null);
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
	        		//System.out.println("ID relacion: " + relacion.getIdInstanciaRelacion());
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
		        	
		        	datosPM.setSociedadMercantil(new RegulatorioSociedadMercantil(new Integer(getTipoSociedadMercantil().get(VALUE).toString()),
		        								 getTipoSociedadMercantil().get(LABEL).toString(), 
		        								 getTipoSociedadMercantil().get(LABEL + "2").toString()));
		        	
		        	datosPM.setTipoIdentificador(new RegulatorioTipoIdentificador(new Integer(getTipoIdentificador().get(VALUE).toString()),
		        			                     getTipoIdentificador().get(LABEL).toString()));
		        	
		        	datosPM.setClaveIdentificadorContraparte(getClaveIdentificador());
		        	
		        	datosPM.setActividadEconomica(new RegulatorioActividadEconomica(new Integer(getTipoContraparte().get(VALUE).toString()),
		        			                      getTipoContraparte().get(LABEL).toString()));
		        	
		        	datosPM.setClaveLei(getClaveLei());
		        	
		        	datosPM.setSectorIndustrial(new RegulatorioSectorIndustrial(new Integer(getSectorIndustrial().get(VALUE).toString()),
		        			                    getSectorIndustrial().get(LABEL).toString()));
		        	
		        	datosPM.setPaisResidenciaContraparte(new RegulatorioPais(getPaisContraparte().get(VALUE).toString(),
		        											getPaisContraparte().get(LABEL).toString()));
		        	
		        	datosPM.setPaisControlContraparte(new RegulatorioPais(getPaisControlContraparte().get(VALUE).toString(),
		        										getPaisControlContraparte().get(LABEL).toString()));
		        	
		        	datosPM.setClaveLeiMatrizDirecta(getClaveLeiMatrizDirecta());
		        	datosPM.setClaveLeiMatrizUltimaInstancia(getClaveLeiMatrizUltima());
		        	datosPM.setFechaContraparte(getFechaContraparte());
		        	datosPM.setFechaSistema(new Date());
		        	datosPM.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
		        	datosPM.setContratoSica(null);
		        	
		        	getSicaServiceData().store(datosPM);
		        	
		        	RegulatorioInstitucion institucion = new RegulatorioInstitucion();
		        	institucion.setIdPersona(new Integer(personaRegistrada.getIdPersona()));
		        	institucion.setIdPersonaUltimaMod(visit.getUser().getIdPersona());
		        	institucion.setFechaSistema(new Date());
		        	institucion.setContratoSica(null);
		        	
		        	institucion.setTipoRelacion(new RegulatorioTipoRelacion(new Integer(getTipoRelacion().get(VALUE).toString()),
		        									getTipoRelacion().get(LABEL).toString()) );
		        	
		        	institucion.setPerteneceGrupoFinanciero(getGrupoFinanciero());
		        	
		        	// Si la contraparte si está relacionada con la institucion  
		        	if(getTipoRelacion() != null && getTipoRelacion().get(VALUE) != null && !getTipoRelacion().get(VALUE).toString().equals("10")) 
		        	{
		        		institucion.setEventoRelacion(new RegulatorioEventoRelacion(new Integer(getTipoEventoRelacion().get(VALUE).toString()),
		        				                                  getTipoEventoRelacion().get(LABEL).toString()));
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
 
    		//Servicios de Contratacion
            Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().
                    getIdPersona().intValue());
            usuario.setTicketInfo(visit.getTicketInfo());
            usuario.setTicket(visit.getTicket());
            
            Persona persona = getSicaBusquedaPersonaService().buscaPersonaPorId(idPersona.intValue());
            //try{jdch30
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
            	getSicaServiceData().cambiarEstadoContrato(contratoSICA.getNoCuenta(),TipoBloqueo.RESTRINGIDO_POR_APERTURA,113890/*Se asigna 113890 ya que pertenece al usuario de la aplicacion*/);
                getDelegate().record("Se reasign\u00f3 contrato corto No.: "+contratoCorto.getIdContrato()+ " y el Contrato SICA No.: " +
                        getNoCuentaContratoSica() + " para el Cliente: " + nombreCompleto +
                        ", con clave de referencia: " + getClaveReferencia(idPersona) +
                        ". Le recordamos que es necesario dar a conocer al cliente su clave de referencia.",
                        null);
            }            
            
            getSicaServiceData().actualizarInfoRegulatoria(new Integer(persona.getIdPersona()), contratoSICA.getNoCuenta());
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
	    		getDelegate().record("Ocurrió un error, favor de contactar a sistemas: "+ce.getMessage(),null);
	    	}
	    }//jdch30 se agrego
        catch (Exception e) {
            error(e.getMessage(), e);
            getDelegate().record("Ocurrió un error, favor de contactar a sistemas: "+e.getMessage(),
                    null);
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
    
    public boolean camposBusquedaValidos()
    {
    	boolean correcto = true;
    	
    	if (getFisicaMoral().equals("FISICA")) 
    	{
			if ((getNombre().trim().length() < 2) && (getPaterno().trim().length() < 2) &&
    			(getRfc().trim().length() < 3) && (getNoCuenta().trim().length() < 3)) {
				correcto = false;
    		}
		}
		else 
		{
			if ((getRazonSocial().trim().length() < 3) && (getRfc().trim().length() < 3) && (getNoCuenta().trim().length() < 3))
				correcto = false;
		} 
    	
    	return correcto;
    }
    /**
     * Verifica que los campos sean correctos
     * @return boolean  True o False, dependiendo si la informacion ingresada es correcta
     */
    public boolean verificaCampos() 
    {
    	if (getModoSubmit() == 1) 
    	{ // Es busqueda
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
    	else 
    	{
    		if (getFisicaMoral().equals("FISICA")) {
        		if(StringUtils.isEmpty(getNombre()) || getNombre().trim().length() < 2 ||
        				StringUtils.isEmpty(getPaterno()) || getPaterno().trim().length() < 2 ||
        				(getNacionalidad().get(VALUE).equals(String.
								valueOf(Num.I_48)) && (StringUtils.isEmpty(getRfc()) || 
										getRfc().trim().length() < 3 ||
								StringUtils.isEmpty(getMaterno()) || 
								getMaterno().trim().length() < 2))
								|| getSectorBanxico().get(VALUE).toString().equals("SELECCIONE")) {
        			
        			return false;
        		}
        	}
        	else {
        		if (StringUtils.isEmpty(getRazonSocial()) || getRazonSocial().trim().length() < 3
        				|| (getNacionalidad().get(VALUE).equals(String.
								valueOf(Num.I_48)) && (StringUtils.isEmpty(getRfc()) ||
										getRfc().trim().length() < 3))
										|| getSectorBanxico().get(VALUE).toString().equals("SELECCIONE")) {
        			return false;
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
    
    public boolean validarCaracteresCamposBusqueda()
    {
    	boolean valido = true;
    	
    	if (getFisicaMoral().equals("FISICA")) 
    	{
			if(!getNombre().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
					|| !getPaterno().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
					|| !getRfc().matches("[a-zA-Z0-9]*")
					|| !getNoCuenta().trim().matches("[-0-9]*")) {
				valido = false;
    		}
			else if(StringUtils.isNotEmpty(getMaterno()) &&
					!getMaterno().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*"))
				valido = false;
		}
		else {
			if (!getRazonSocial().trim().matches("[A-Za-z0-9.,& \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
					|| !getRfc().trim().matches("[a-zA-Z0-9]*")
					|| !getNoCuenta().trim().matches("[-0-9]*")){
				valido = false;
    		}
		}
    	
    	return valido;
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
    		if (getFisicaMoral().equals("FISICA")) {
        		if(!getNombre().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
        				|| !getPaterno().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
        				|| !getMaterno().trim().matches("[A-Za-z \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*")
        				|| (getNacionalidad().get(VALUE).equals(String.valueOf(Num.I_48)) && (!getRfc().trim().matches("[a-zA-Z0-9]*")))){
        			return false;
        		}
        		if(!validarCaracteresCamposBanorte())
        			return false;
        	}
        	else {
        		if (!getRazonSocial().trim().matches("[A-Za-z0-9.,& \u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1]*") 
        				|| (getNacionalidad().get(VALUE).equals(String.valueOf(Num.I_48)) && (!getRfc().trim().matches("[a-zA-Z0-9]*")))) {
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
					setVerLupaBuscarCodigoPostal(true);
				}
				else {
					setHashmapColonia(null);
					setHashmapMunicipio(null);
					setHashmapEntidad(null);
					setColonia("");
					setDelegacion("");
					setEstado("");
					setCodigoPostal("");
					setModoDireccion(false);
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
				setModoDireccion(false);
				getDelegate().record("No se ha encontrado informaci\u00f3n para el" +
						" C\u00f3digo Postal, intente con uno diferente.", null);
		        return;
			}
    	}
    	else {
    		setModoDireccion(true);
    		setLevel(-1);
    		getDelegate().record("El  C\u00f3digo Postal ingresado es incorrecto.", null);
    		setFaltaCodigoPostal(true);
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
	
	public IPropertySelectionModel getEstadoCivilModel() {
	    Vector edosTmp = getSicaConsultaCatalogosService().obtenCatEstadoCivil();
	    List edos = new ArrayList();
	    Map edoMap = new HashMap();
	    
	    edoMap.put(VALUE, "SELECCIONE");
	    edoMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
	    edos.add(edoMap);
	    
	    for (Iterator i = edosTmp.iterator(); i.hasNext();) {
	        Catalogo sector = (Catalogo) i.next();
	        edoMap = new HashMap();
	        edoMap.put(VALUE, sector.getId());
	        edoMap.put(LABEL, sector.getDescripcion());
	        
	        edos.add(edoMap);
	    }
	    return new RecordSelectionModel(edos, VALUE, LABEL);
    }
	
	/**
	 * Gets the actividad economica model.
	 *
	 * @return the actividad economica model
	 */
	public IPropertySelectionModel getActividadEconomicaModel() {
		List actividades = new ArrayList();
		Map actividadMap = new HashMap();

	    String key = null;
	    
	    actividadMap.put(VALUE, "SELECCIONE");
	    actividadMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
	    actividades.add(actividadMap);
	    if (getGiroEconomico() != null) {
	    	String idGiro = (String) getGiroEconomico().get(VALUE);
	    	if (!"SELECCIONE".equals(idGiro)) {
		    	LinkedHashMap map = getSicaConsultaCatalogosService().obtenCatActividadGiro(idGiro);
		    	for (Iterator i = map.keySet().iterator(); i.hasNext();) {
			        key = (String) i.next();
			        actividadMap = new HashMap();
			        actividadMap.put(VALUE, key);
			        String label = StringUtils.trim((String) map.get(key));
			        actividadMap.put(LABEL, label);
			        actividades.add(actividadMap);
			    }
	    	}
	    	
	    }
	    
	    return new RecordSelectionModel(actividades, VALUE, LABEL);
	}
	
	//public abstract void setActividadEconomicaModel(IPropertySelectionModel actividades);
	
	public IPropertySelectionModel getGiroEconomicoModel() 
	{
		LinkedHashMap map = getSicaConsultaCatalogosService().obtenCatGiroEconomico();
		List giros = new ArrayList();
	    Map giroMap = new HashMap();
	    String key = null;
	    
	    giroMap.put(VALUE, "SELECCIONE");
	    giroMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
	    giros.add(giroMap);
	    
	    for (Iterator i = map.keySet().iterator(); i.hasNext();) 
	    {
	        key = (String) i.next();
	        giroMap = new HashMap();
	        giroMap.put(VALUE, key);
	        giroMap.put(LABEL, (String)map.get(key));
	        
	        giros.add(giroMap);
	    }
	    return new RecordSelectionModel(giros, VALUE, LABEL);
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
	public IPropertySelectionModel getCatalogoSociedadMercantil() 
	{
		RegulatorioSociedadMercantil sm = null;
		List sociedades = getSicaServiceData().findCatalogoSociedadMercantil();
		List resultado = new ArrayList();
		Map sociedadMap = null;
		
		sociedadMap = new HashMap();
		sociedadMap.put(VALUE, "SELECCIONE");
		sociedadMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
		resultado.add(sociedadMap);
		
		if(sociedades != null && sociedades.size() > 0)
		{
			for(int i = 0; i < sociedades.size(); i++)
			{
				sm = (RegulatorioSociedadMercantil)sociedades.get(i);
				sociedadMap = new HashMap();
				sociedadMap.put(VALUE, sm.getId().toString());
				sociedadMap.put(LABEL, sm.getDescripcionCorta());
				sociedadMap.put(LABEL + "2", sm.getDescrpcionLarga());
				resultado.add(sociedadMap);
			}
		}

	    return new RecordSelectionModel(resultado, VALUE, LABEL);
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo de Tipo de identificador de la contraparte.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoTipoIdentificador() 
	{
		List tipos = getSicaServiceData().findCatalogoIdentificadorContraparte();
		List resultado = new ArrayList();
		Map tipoMap = null;
		RegulatorioTipoIdentificador tipo = null; 
		
		tipoMap = new HashMap();
		tipoMap.put(VALUE, "SELECCIONE");
		tipoMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
		resultado.add(tipoMap);
		
		if(tipos != null && tipos.size() > 0)
		{
			for(int i = 0; i < tipos.size(); i++)
			{
				tipo = (RegulatorioTipoIdentificador)tipos.get(i);
				tipoMap = new HashMap();
				tipoMap.put(VALUE, tipo.getId().toString());
				tipoMap.put(LABEL, tipo.getDescripcion());
				resultado.add(tipoMap);
			}
		}

	    return new RecordSelectionModel(resultado, VALUE, LABEL);
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo de Tipo de contraparte (Actividad economica).
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoTipoContraparte() {
		List resultado = new ArrayList();
		List tiposContrapartes = getSicaServiceData().findCatalogoActividadEconomica();
		Map tipoMap = null;
		RegulatorioActividadEconomica ae = null;
		
		tipoMap = new HashMap();
		tipoMap.put(VALUE, "SELECCIONE");
		tipoMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
		resultado.add(tipoMap);
		
		if(tiposContrapartes != null && tiposContrapartes.size() > 0)
		{
			for(int i = 0; i < tiposContrapartes.size(); i++)
			{
				ae = (RegulatorioActividadEconomica)tiposContrapartes.get(i);
				tipoMap = new HashMap();
				tipoMap.put(VALUE, ae.getId().toString());
				tipoMap.put(LABEL, ae.getDescripcion());
				resultado.add(tipoMap);
			}
		}

	    return new RecordSelectionModel(resultado, VALUE, LABEL);
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo de Tipo de contraparte (Actividad economica).
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoSectorIndustrial() {
		List resultado = new ArrayList();
		List sectores = getSicaServiceData().findCatalogoSectorIndustrial();
		Map sectorMap = null;
		RegulatorioSectorIndustrial si = null;
		
		sectorMap = new HashMap();
		sectorMap.put(VALUE, "SELECCIONE");
		sectorMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
		resultado.add(sectorMap);
		
		if(sectores != null && sectores.size() > 0)
		{
			for(int i = 0; i < sectores.size(); i++)
			{
				si = (RegulatorioSectorIndustrial)sectores.get(i);
				sectorMap = new HashMap();
				sectorMap.put(VALUE, si.getId().toString());
				sectorMap.put(LABEL, si.getDescripcion());
				resultado.add(sectorMap);
			}
		}
		
	    return new RecordSelectionModel(resultado, VALUE, LABEL);
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo pais de datos regulatorios.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoPaisRegulatorio() 
	{
		List resultado = new ArrayList();
		List paises = getSicaServiceData().findCatalogoPais();
		Map paisMap = null;
		RegulatorioPais paisreg = null;
		
		paisMap = new HashMap();
		paisMap.put(VALUE, "SELECCIONE");
		paisMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
		resultado.add(paisMap);
		
		if(paises != null && paises.size() > 0)
		{
			for(int i = 0; i < paises.size(); i++)
			{
				paisreg = (RegulatorioPais)paises.get(i);
				paisMap = new HashMap();
				paisMap.put(VALUE, paisreg.getClaveIso());
				paisMap.put(LABEL, paisreg.getNombrePais());
				resultado.add(paisMap);
			}
		}

	    return new RecordSelectionModel(resultado, VALUE, LABEL);
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo Tipo Relacion.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoTipoRelacion() 
	{
		List resultado = new ArrayList();
		List tiposRelacion = getSicaServiceData().findCatalogoTipoRelacion();
		Map tipoMap = null;
		RegulatorioTipoRelacion tr = null;
		
		tipoMap = new HashMap();
		tipoMap.put(VALUE, "SELECCIONE");
		tipoMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
		resultado.add(tipoMap);
		
		if(tiposRelacion != null && tiposRelacion.size() > 0)
		{
			for(int i = 0; i < tiposRelacion.size(); i++)
			{
				tr = (RegulatorioTipoRelacion)tiposRelacion.get(i);
				tipoMap = new HashMap();
				tipoMap.put(VALUE, tr.getId().toString());
				tipoMap.put(LABEL, tr.getDescripcion());
				resultado.add(tipoMap);
			}
		}

	    return new RecordSelectionModel(resultado, VALUE, LABEL);
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo Tipo Relacion.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoEventoRelacion() 
	{
		List resultado = new ArrayList();
		List eventos = getSicaServiceData().findCatalogoEventoRelacion();
		Map eventoMap = null;
		RegulatorioEventoRelacion e = null;
		
		eventoMap = new HashMap();
		eventoMap.put(VALUE, "SELECCIONE");
		eventoMap.put(LABEL, "SELECCIONE LA OPCION DESEADA");
		resultado.add(eventoMap);
		
		if(eventos != null && eventos.size() > 0)
		{
			for(int i = 0; i < eventos.size(); i++)
			{
				e = (RegulatorioEventoRelacion)eventos.get(i);
				eventoMap = new HashMap();
				eventoMap.put(VALUE, e.getId().toString());
				eventoMap.put(LABEL, e.getDescripcion());
				resultado.add(eventoMap);
			}
		}

	    return new RecordSelectionModel(resultado, VALUE, LABEL);
    }
	
    //************************************************************************************************************
	
	public IPropertySelectionModel getCatalogoTipoComprobante() {
		String tipoPersona = "MORAL".equals(getFisicaMoral()) ? Constantes.ID_TIPO_PERSONA_MORAL : Constantes.ID_TIPO_PERSONA_FISICA;
	    LinkedHashMap comprobantesMap = getSicaConsultaCatalogosService().obtenCatTipoComprobante(tipoPersona);
	    List comprobantes = new ArrayList();
	    Map comprobante = null;
	    String idDocumento = null;
	    
	    comprobante = new HashMap();
	    comprobante.put(VALUE, "SELECCIONE");
	    comprobante.put(LABEL, "SELECCIONE LA OPCION DESEADA");
	    comprobantes.add(comprobante);
	    
	    for (Iterator i = comprobantesMap.keySet().iterator(); i.hasNext(); ) 
	    {
	    	idDocumento = (String) i.next();
	    	TipoComprobante tc = (TipoComprobante)comprobantesMap.get(idDocumento);
	    	
	    	comprobante = new HashMap();
	    	comprobante.put(VALUE, tc.getIdDocumento());
	    	comprobante.put(LABEL, tc.getDescripcion());
	    	comprobantes.add(comprobante);
	    }
	    
	    return new RecordSelectionModel(comprobantes, VALUE, LABEL);
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo de Nacionalidad.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoNacionalidad() {
	    Vector nacionalidadTmp = getSicaConsultaCatalogosService().obtenCatNacionalidad();
	    List nacionalidades = new ArrayList();
	    Map nacionalidad = null;
	    
	    nacionalidad = new HashMap();
        nacionalidad.put(VALUE, "SELECCIONE");
        nacionalidad.put(LABEL, "SELECCIONE LA OPCION DESEADA");
        nacionalidades.add(nacionalidad);
	    
	    for (Iterator i = nacionalidadTmp.iterator(); i.hasNext(); ) 
	    {
	        Catalogo nac = (Catalogo) i.next();
	        nacionalidad = new HashMap();
	        nacionalidad.put(VALUE, nac.getId());
	        nacionalidad.put(LABEL, nac.getDescripcion());
	        nacionalidades.add(nacionalidad);
	    }
	    
	    return new RecordSelectionModel(nacionalidades, VALUE, LABEL);
    }
	
	/**
     * Obtiene el Modelo a mostrar en el Combo de Paises.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getCatalogoPais() {
		Vector sectoresTmp = getSicaConsultaCatalogosService().obtenCatpais();		
	    List paises = new ArrayList();
	    for (Iterator i = sectoresTmp.iterator(); i.hasNext();) {
	        Catalogo pais = (Catalogo) i.next();
	        Map paisMap = new HashMap();
	        paisMap.put(VALUE, pais.getId());
	        paisMap.put(LABEL, pais.getDescripcion());
	        paises.add(paisMap);
		}
	    
	    return new RecordSelectionModel(paises, VALUE, LABEL);
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
		if ("MORAL".equals(getFisicaMoral())) 
			nombres.add("Raz&oacute;n social");
		else
			nombres.add("Nombre completo");
		
		nombres.add("RFC");
		nombres.add("Modificaci&oacute;n de datos");
		nombres.add("Direcci&oacute;n");
		nombres.add("Medios de contacto");
		nombres.add("Referencia");
		setNoColumnasClientes(Num.I_6);
		
		return nombres;
	}
    
    public List getNombresColumnasDirecciones() {
		List nombres = new ArrayList();
		nombres.add("Calle");
		nombres.add("No. Ext.");
		nombres.add("No. Int.");
		nombres.add("Colonia");
		nombres.add("Delegaci&oacute;n/Municipio");
		nombres.add("Estado");
		nombres.add("C&oacute;digo Postal");
		nombres.add("");
		setNoColumnasDirecciones(Num.I_8);
		
		return nombres;
	}
    
    public List getNombresColumnasMediosContacto() {
		List nombres = new ArrayList();
		nombres.add("Tipo de medio de contacto");
		nombres.add("Valor");
		nombres.add("Facturaci\u00F3n electr\u00F3nica");
		nombres.add("");
		setNoColumnasMediosContacto(Num.I_4);
		
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
	
	public abstract void setDireccionesCliente(List direccionesCliente);
	
	public abstract List getDireccionesCliente();

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
     * @return Map    Nacionalidad
     */
    public abstract Map getNacionalidad();

    /**
     * Asigna el valor de la propiedad <code>nacionalidad</code>
     * @param nacionalidad  Nacionalidad del cliente
     */
    public abstract void setNacionalidad(Map nacionalidad);
    
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
    public abstract Map getPais();
    
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
    public abstract void setPais(Map pais);
    
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
     * Regresa el map seleccionado de combo giro economico.
     *
     * @return Map
     */
	public abstract Map getGiroEconomico();

    /**
     * Fija el map con el giro economico seleccionado
     *
     * @param giroEconomico El valor a asignar.
     */
	public abstract void setGiroEconomico(Map giroEconomico);
	
	/**
     * Regresa el map seleccionado de combo actividad economica.
     *
     * @return Map
     */
	public abstract Map getActividadEconomica();

    /**
     * Fija el map con la actividad economica seleccionado
     *
     * @param actividadEconomica El valor a asignar.
     */
	public abstract void setActividadEconomica(Map actividadEconomica);
	
	public abstract void setPaisNacimiento(Map paisNacimiento);
	public abstract Map getPaisNacimiento();
	
	public abstract void setLugarNacimiento(String lugarNacimiento);
	public abstract String getLugarNacimiento();
	
	public abstract void setEstadoCivil(Map estadoCivil);
	public abstract Map getEstadoCivil();
	
	public abstract void setSexo(String sexo);
	public abstract String getSexo();
	
	public abstract void setFacturacionElectronica(boolean facturacionElectronica);
	public abstract boolean isFacturacionElectronica();

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

    /**
     * Regresa el valor de <code>cuentaCheques</code>.
     *
     * @return String
     */
    public String contratoCorto;
    

    /**
     * Fija el valor de <code>noColumnasClientes</code>.
     *
     * @param noColumnasClientes El valor a asignar.
     */
    public abstract void setNoColumnasClientes(int noColumnasClientes);
    
    public abstract int getNoColumnasDirecciones();
    public abstract void setNoColumnasDirecciones(int noColumnasDirecciones);
    
    public abstract int getNoColumnasMediosContacto();
    public abstract void setNoColumnasMediosContacto(int noColumnasMediosContacto);
    
    public abstract void setEmail(String email);
    public abstract String getEmail();
    
    
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
    
    private SicaConsultaMediosContactoPersonaService getSicaConsultaMediosContactoPersonaService()
    {
    	return (SicaConsultaMediosContactoPersonaService) getApplicationContext().getBean("sicaConsultaMediosContactoPersonaService");
    }
    
    private SicaRegistroMedioContactoContratacionService getSicaRegistroMedioContactoContratacionService()
    {
    	return (SicaRegistroMedioContactoContratacionService) getApplicationContext().getBean("sicaRegistroMedioContactoContratacionService");
    }
    
    /**
     * Gets the persona service.
     *
     * @return the persona service
     */
    private PersonaService getPersonaService() {
    	return (PersonaService) getApplicationContext().getBean("personaService");
    }
    
    public IPropertySelectionModel getCatalogoTipoDocumento() {
    	List tipoDocs = new ArrayList();
		Map item = new HashMap();
		item.put(VALUE, String.valueOf(ID_DEFAULT));
		item.put(LABEL, OPCION_DEFAULT);
		tipoDocs.add(item);
		String filtro = "NAC";
		if (!isNacionalidadMexicana()) {
			filtro = "EXT";
		}
		LinkedHashMap tipos = 
				getSicaConsultaCatalogosService().obtenCatTipoIdentificacion(filtro);
		for (Iterator i = tipos.keySet().iterator(); i.hasNext();) {
			String idDocumento = (String) i.next();
			TipoComprobante tipo = (TipoComprobante) tipos.get(idDocumento);
			item = new HashMap();
			item.put(VALUE, tipo.getIdTipoComprobante());
			item.put(LABEL, tipo.getDescripcion());
			tipoDocs.add(item);
		}
		
		return new RecordSelectionModel(tipoDocs, VALUE, LABEL);
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
    
    public abstract void setClavesReferencia(Map clavesReferencia);
    public abstract Map getClavesReferencia();

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
    
    public abstract void setIdPersonaApoderado(Integer idPersonaApoderado);
    public abstract Integer getIdPersonaApoderado();
    
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
    
    public abstract void setModoBusqueda(boolean modoBusqueda);
    public abstract boolean isModoBusqueda();
    
    public abstract void setModoActualizar(boolean modoActualizar);
    public abstract boolean isModoActualizar();
    
    public abstract void setFaltaNombre(boolean faltaNombre);
    public abstract boolean isFaltaNombre();
    
    public abstract void setFaltaPaterno(boolean faltaPaterno);
    public abstract boolean isFaltaPaterno();
    
    public abstract void setFaltaMaterno(boolean faltaMaterno);
    public abstract boolean isFaltaMaterno();
    
    public abstract void setFaltaSector(boolean faltaSector);
    public abstract boolean isFaltaSector();
    
    public abstract void setFaltaGiro(boolean faltaGiro);
    public abstract boolean isFaltaGiro();
    
    public abstract void setFaltaActividad(boolean faltaActividad);
    public abstract boolean isFaltaActividad();
    
    public abstract void setFaltaNacionalidad(boolean faltaNacionalidad);
    public abstract boolean isFaltaNacionalidad();
    
    public abstract void setFaltaRfc(boolean faltaRfc);
    public abstract boolean isFaltaRfc();
    
    public abstract void setFaltaFechaNacimiento(boolean faltaFechaNacimiento);
    public abstract boolean isFaltaFechaNacimiento();
    
    public abstract void setFaltaRazonSocial(boolean faltaRazonSocial);
    public abstract boolean isFaltaRazonSocial();
    
    public abstract void setFaltaNombreApoderado(boolean faltaNombreApoderado);
    public abstract boolean isFaltaNombreApoderado();
    
    public abstract void setFaltaPaternoApoderado(boolean faltaPaternoApoderado);
    public abstract boolean isFaltaPaternoApoderado();
    
    public abstract void setFaltaMaternoApoderado(boolean faltaMaternoApoderado);
    public abstract boolean isFaltaMaternoApoderado();
    
    public abstract void setFaltaNombreContraparte(boolean faltaNombreContraparte);
    public abstract boolean isFaltaNombreContraparte();
    
    public abstract void setFaltaSociedadMercantil(boolean faltaSociedadMercantil);
    public abstract boolean isFaltaSociedadMercantil();
    
    public abstract void setFaltaIdentificadorContraparte(boolean faltaIdentificadorContraparte);
    public abstract boolean isFaltaIdentificadorContraparte();
    
    public abstract void setFaltaClaveContraparte(boolean faltaClaveContraparte);
    public abstract boolean isFaltaClaveContraparte();
    
    public abstract void setFaltaTipoContraparte(boolean faltaTipoContraparte);
    public abstract boolean isFaltaTipoContraparte();
    
    public abstract void setFaltaClaveLei(boolean faltaClaveLei);
    public abstract boolean isFaltaClaveLei();
    
    public abstract void setFaltaSectorIndustrial(boolean faltaSectorIndustrial);
    public abstract boolean isFaltaSectorIndustrial();
    
    public abstract void setFaltaPaisControl(boolean faltaPaisControl);
    public abstract boolean isFaltaPaisControl();
    
    public abstract void setFaltaPaisContraparte(boolean faltaPaisContraparte);
    public abstract boolean isFaltaPaisContraparte();
    
    public abstract void setFaltaClaveLeiDirecta(boolean faltaClaveLeiDirecta);
    public abstract boolean isFaltaClaveLeiDirecta();
    
    public abstract void setFaltaClaveLeiUltima(boolean faltaClaveLeiUltima);
    public abstract boolean isFaltaClaveLeiUltima();
    
    public abstract void setFaltaFechaContraparte(boolean faltaFechaContraparte);
    public abstract boolean isFaltaFechaContraparte();
    
    public abstract void setFaltaTipoRelacion(boolean faltaTipoRelacion);
    public abstract boolean isFaltaTipoRelacion();
    
    public abstract void setFaltaGrupoFinanciero(boolean faltaGrupoFinanciero);
    public abstract boolean isFaltaGrupoFinanciero();
    
    public abstract void setFaltaEventoRelacion(boolean faltaEventoRelacion);
    public abstract boolean isFaltaEventoRelacion();
    
    public abstract void setFaltaFechaRelacion(boolean faltaFechaRelacion);
    public abstract boolean isFaltaFechaRelacion();
    
    public abstract void setModoDireccion(boolean modoDireccion);
    public abstract boolean isModoDireccion();
    
    public abstract void setTipoComprobanteDireccion(Map tipoComprobanteDireccion);
    public abstract Map getTipoComprobanteDireccion();
    
    public abstract void setNumeroComprobante(String numeroComprobante);
    public abstract String getNumeroComprobante();
    
    public abstract void setFechaComprobante(Date fechaComprobante);
    public abstract Date getFechaComprobante();
    
    public abstract void setVerLupaBuscarCodigoPostal(boolean verLupaBuscarCodigoPostal);
    public abstract boolean isVerLupaBuscarCodigoPostal();
    
    public abstract void setIdDireccionCliente(Integer idDireccionCliente);
    public abstract Integer getIdDireccionCliente();
    
    public abstract void setFaltaCalle(boolean faltaCalle);
    public abstract boolean isFaltaCalle();
    
    public abstract void setFaltaNoExterior(boolean faltaNoExterior);
    public abstract boolean isFaltaNoExterior();
    
    public abstract void setFaltaNoInterior(boolean faltaNoInterior);
    public abstract boolean isFaltaNoInterior();
    
    public abstract void setFaltaCodigoPostal(boolean faltaCodigoPostal);
    public abstract boolean isFaltaCodigoPostal();
    
    public abstract void setFaltaColonia(boolean faltaColonia);
    public abstract boolean isFaltaColonia();
    
    public abstract void setFaltaCiudad(boolean faltaCiudad);
    public abstract boolean isFaltaCiudad();
    
    public abstract void setFaltaDelegacion(boolean faltaDelegacion);
    public abstract boolean isFaltaDelegacion();
    
    public abstract void setFaltaEstado(boolean faltaEstado);
    public abstract boolean isFaltaEstado();
    
    public abstract void setFaltaTipoComprobante(boolean faltaTipoComprobante);
    public abstract boolean isFaltaTipoComprobante();
    
    public abstract void setFaltaNoComprobante(boolean faltaNoComprobante);
    public abstract boolean isFaltaNoComprobante();
    
    public abstract void setFaltaFechaExpedicion(boolean faltaFechaExpedicion);
    public abstract boolean isFaltaFechaExpedicion();
    
    public abstract void setMediosContacto(List mediosContacto);
    public abstract List getMediosContacto();
    
    public abstract void setIdMedioContacto(Integer idMedioContacto);
    public abstract Integer getIdMedioContacto();
    
    public abstract void setFaltaEmail(boolean faltaEmail);
    public abstract boolean isFaltaEmail();
    
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
    public abstract void setTipoSociedadMercantil(Map tipoSociedadMercantil);
    public abstract Map getTipoSociedadMercantil();
    
    
    /**
     * @param tipoIdentificador
     */
    public abstract void setTipoIdentificador(Map tipoIdentificador);
    public abstract Map getTipoIdentificador();
    
    
    /**
     * @param claveIdentificador
     */
    public abstract void setClaveIdentificador(String claveIdentificador);
    public abstract String getClaveIdentificador();
    
    
    /**
     * @param tipoContraparte
     */
    public abstract void setTipoContraparte(Map tipoContraparte);
    public abstract Map getTipoContraparte();
    
    
    /**
     * @param claveLei
     */
    public abstract void setClaveLei(String claveLei);
    public abstract String getClaveLei();
    
    
    /**
     * @param sectorIndustrial
     */
    public abstract void setSectorIndustrial(Map sectorIndustrial);
    public abstract Map getSectorIndustrial();
    
    /**
     * @param paisControlContraparte
     */
    public abstract void setPaisControlContraparte(Map paisControlContraparte);
    public abstract Map getPaisControlContraparte();
    
    /**
     * @param paisContraparte
     */
    public abstract void setPaisContraparte(Map paisContraparte);
    public abstract Map getPaisContraparte();
    
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
    public abstract void setTipoRelacion(Map tipoRelacion);
    public abstract Map getTipoRelacion();
    
    
    /**
     * @param public abstract Integer getGrupoFinanciero();
     */
    public abstract void setGrupoFinanciero(Integer grupoFinanciero);
    public abstract Integer getGrupoFinanciero();
    
    /**
     * @param tipoEventoRelacion
     */
    public abstract void setTipoEventoRelacion(Map tipoEventoRelacion);
    public abstract Map getTipoEventoRelacion();
    
    /**
     * @param fechaEventoRelacion
     */
    public abstract void setFechaEventoRelacion(Date fechaEventoRelacion);
    public abstract Date getFechaEventoRelacion();
    
    public abstract void setSeccion(String seccion);
    public abstract String getSeccion();
    
    public abstract void setSubSeccion(String subSeccion);
    public abstract String getSubSeccion();
    
    /**
     * Gets the curp.
     *
     * @return the curp
     */
    public abstract String getCurp();

	/**
	 * Sets the curp.
	 *
	 * @param curp the new curp
	 */
	public abstract void setCurp(String curp);

	/**
	 * Gets the num documento.
	 *
	 * @return the num documento
	 */
	public abstract String getNumDocumento();

	/**
	 * Sets the num documento.
	 *
	 * @param numDocumento the new num documento
	 */
	public abstract void setNumDocumento(String numDocumento);
	
	/**
	 * Gets the tipo documento.
	 *
	 * @return the tipo documento
	 */
	public abstract Map getTipoDocumento();
	
	/**
	 * Sets the tipo documento.
	 *
	 * @param tipoDocumento the new tipo documento
	 */
	public abstract void setTipoDocumento(Map tipoDocumento);
	
	/**
	 * Checks if is nacionalidad mexicana.
	 *
	 * @return true, if is nacionalidad mexicana
	 */
	public abstract boolean isNacionalidadMexicana();
	
	/**
	 * Sets the nacionalidad mexicana.
	 *
	 * @param nacionalidadMexicana the new nacionalidad mexicana
	 */
	public abstract void setNacionalidadMexicana(boolean nacionalidadMexicana);
    
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
     * Checks if is falta curp.
     *
     * @return true, if is falta curp
     */
    public abstract boolean isFaltaCurp();
    
	/**
	 * Sets the falta curp.
	 *
	 * @param faltaCurp the new falta curp
	 */
	public abstract void setFaltaCurp(boolean faltaCurp);

	/**
	 * Checks if is falta id extranjero.
	 *
	 * @return true, if is falta id extranjero
	 */
	public abstract boolean isFaltaIdExtranjero();

	/**
	 * Sets the falta id extranjero.
	 *
	 * @param faltaIdExtranjero the new falta id extranjero
	 */
	public abstract void setFaltaIdExtranjero(boolean faltaIdExtranjero);
	
	/**
	 * Checks if is falta tipo documento.
	 *
	 * @return true, if is falta tipo documento
	 */
	public abstract boolean isFaltaTipoDocumento();

	/**
	 * Sets the falta tipo documento.
	 *
	 * @param faltaTipoDocumento the new falta tipo documento
	 */
	public abstract void setFaltaTipoDocumento(boolean faltaTipoDocumento);

	/**
	 * Checks if is falta num documento.
	 *
	 * @return true, if is falta num documento
	 */
	public abstract boolean isFaltaNumDocumento();

	/**
	 * Sets the falta num documento.
	 *
	 * @param faltaNumDocumento the new falta num documento
	 */
	public abstract void setFaltaNumDocumento(boolean faltaNumDocumento);
	
	/**
	 * Checks if is falta nit.
	 *
	 * @return true, if is falta nit
	 */
	public abstract boolean isFaltaNit();

	/**
	 * Sets the falta nit.
	 *
	 * @param faltaNit the new falta nit
	 */
	public abstract void setFaltaNit(boolean faltaNit);
	
    /**
     * Regresa el valor de <code>nit</code>.
     * 
     * @return nit
     */
    public abstract String getNit();
    
    /**
     * Fija el valor de <code>nit</code>.
     * 
     * @param nit
     */
    public abstract void setNit(String nit);

	/**
	 * Regresa el valor de <code>requiereNit.</code>.
	 *
	 * @return true, si requiere NIT
	 */
	public abstract boolean isRequiereNit();
	
	/**
	 * Fija el valor de <code>requiereNit.</code>.
	 *
	 * @param requiereNit
	 */
	public abstract void setRequiereNit(boolean requiereNit);
    
	/**
	 * Regresa el valor de <code>direccionPaisExtranjero.</code>.
	 *
	 * @return true, si existe al menos 1 dirección de pais Extranjero
	 */
	public abstract boolean isDireccionPaisExtranjero();
	
	/**
	 * Fija el valor de <code>direccionPaisExtranjero.</code>.
	 *
	 * @param direccionPaisExtranjero
	 */
	public abstract void setDireccionPaisExtranjero(boolean direccionPaisExtranjero);
    


	private static final String LABEL = "label";
    private static final String VALUE = "value";
    
    public static final String VER_SECCION_BUSQUEDA = "VER_SECCION_BUSQUEDA";
    public static final String VER_SECCION_CLIENTES = "VER_SECCION_CLIENTES";
    public static final String VER_SECCION_MODIFICACION_CLIENTES = "VER_SECCION_MODIFICACION_CLIENTES";
    public static final String VER_SECCION_DIRECCION = "VER_SECCION_DIRECCION";
    public static final String VER_SECCION_RESULTADOS_DIRECCION = "VER_SECCION_RESULTADOS_DIRECCION";
    public static final String VER_SECCION_MODIFICAR_AGREGAR_DIRECCION = "VER_SECCION_MODIFICAR_AGREGAR_DIRECCION";
    public static final String VER_SECCION_MEDIOS_CONTACTO = "VER_SECCION_MEDIOS_CONTACTO";
    public static final String VER_SECCION_RESULTADO_MEDIOS = "VER_SECCION_RESULTADO_MEDIOS";
    public static final String VER_SECCION_MODIFICAR_AGREGAR_MEDIO = "VER_SECCION_MODIFICAR_AGREGAR_MEDIO";
    
    public static final String SEXO_MASCULINO = "M";
    public static final String SEXO_FEMENINO = "F";
}
