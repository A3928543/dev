/*
 * $Id: CapturaBeneficiariosCuentaIxe.java,v 1.1.2.2.6.1.6.4.6.1.10.1.16.1 2020/11/05 07:45:25 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;

import com.banorte.www.ws.exception.SicaAltamiraException;
import com.ixe.bean.Usuario;
import com.ixe.bean.bup.RelacionCuentaPersona;
import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaBusquedaPersonaService;
import com.ixe.ods.sica.SicaConsultaProductosPersonaService;
import com.ixe.ods.sica.SicaConsultaRelacionesCuentaService;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaRegistroRelacionCuentaPersonaService;
import com.ixe.ods.sica.Visit;
//import com.ixe.ods.sica.dao.PhoenixCuentaMigradaDao; //Apagado de Phoenix
import com.ixe.ods.sica.dto.CuentaMigradaDto;
import com.ixe.ods.sica.dto.InfoCuentaAltamiraDto;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.pages.deals.formliq.CapturaCuentaIxe;
//import com.ixe.ods.sica.services.IdIxeAltamiraService;  // El modo bup_altamira está en desuso
import com.ixe.ods.sica.services.InformacionCuentaAltamiraService;


/**
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.1.2.2.6.1.6.4.6.1.10.1.16.1 $ $Date: 2020/11/05 07:45:25 $
 */
public abstract class CapturaBeneficiariosCuentaIxe extends SicaPage {
    /**
     * Constante para el N&uacute;mero M&aacute;ximo de Registros por Query.
     */
    private static final int MAX_RESULTS = 50;

    /** Constante Pagina ConsultaClientes  */
    public final static String CONSULTA_CLIENTES = "ConsultaClientes";

    /** Pagina Captura Cuenta Ixe * */
    public final static String CAPTURA_CUENTA_IXE = "CapturaCuentaIxe";

    /** Modo de operacion BUP y ALTAMIRA */
    private final static String BUP_ALTAMIRA = "BUP_ALTAMIRA";

    /** Modo de operación ALTAMIRA  */
    private final static String ALTAMIRA = "ALTAMIRA";

    /** Error de negocio en la consulta idIxeAltamira */
    private static final String ID_ERROR = "0";

    /**
     * Limpia variables y precarga informacion cada que esta se activa la P&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        inicializarVariables();
    }

    /**
     * Asigna el valor inicial de las variables
     * de la pagina.
     */
    public void inicializarVariables() {
        setModoSubmit(0);
        setCuentaIxe("");
        setBeneficiarios(getSicaConsultaRelacionesCuentaService().obtenRelacionesCuentaParaRol(getSicaConsultaProductosPersonaService()
                                                                                                   .obtenCuentaContrato(getContratoSica()
                                                                                                                            .getNoCuenta()),
                FacultySystem.BENEFICIARIO_SICA_ROL, null));
        setResultadoBusqueda(false);
    }

    /**
     * Permite asignar una Persona existente como Beneficiario de una Cuenta.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void asignar(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
        RelacionCuentaPersona relacionCuentaPersonaMigPlantilla = new RelacionCuentaPersona();

        //Contratacion
        try {
            Visit visit = (Visit) getVisit();
            Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser()
                                                                                 .getIdPersona()
                                                                                 .intValue());
            RelacionCuentaPersona relacionCuentaPersona = new RelacionCuentaPersona();
            relacionCuentaPersona.setIdPersona(((Integer) cycle.getServiceParameters()[0]).intValue());
            relacionCuentaPersona.setCuenta(getSicaConsultaProductosPersonaService()
                                                .obtenCuentaContrato(getContratoSica().getNoCuenta()));
            relacionCuentaPersona.setIdRol(FacultySystem.BENEFICIARIO_SICA_ROL);
            relacionCuentaPersonaMigPlantilla = relacionCuentaPersona;
            getSicaRegistroRelacionCuentaPersonaService().registraBeneficiarioTrans(relacionCuentaPersona,
                getIdPersonaCliente(), usuario);
            setBeneficiarios(getSicaConsultaRelacionesCuentaService().obtenRelacionesCuentaParaRol(getSicaConsultaProductosPersonaService()
                                                                                                       .obtenCuentaContrato(getContratoSica()
                                                                                                                                .getNoCuenta()),
                    FacultySystem.BENEFICIARIO_SICA_ROL, null));
        }
        catch (Exception e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }

            if (delegate != null) {
                delegate.record("Hubo un error al intentar dar de Alta el Beneficiario.", null);
            }
        }

        if (CAPTURA_CUENTA_IXE.equals(getPaginaAnterior())) {
            //Flujo para cuentas IXE
            CapturaCuentaIxe prevPage = (CapturaCuentaIxe) cycle.getPage(getPaginaAnterior());
            prevPage.creaBeneficiario((com.ixe.ods.bup.model.Persona) getSicaServiceData().find(com.ixe.ods.bup.model.Persona.class,
                    (Integer) cycle.getServiceParameters()[0]));

            //si se obtuvo la cuenta de altamira es esta la que se registra en la plantilla.
            if ((getListaResultadosAltamira() != null) && !getListaResultadosAltamira().isEmpty()) {
                prevPage.setNoCuentaIxe(((InfoCuentaAltamiraDto) getListaResultadosAltamira().get(0)).getNumeroCuenta());
                prevPage.setCr(((InfoCuentaAltamiraDto) getListaResultadosAltamira().get(0)).getCr());
            }
            else {
                prevPage.setNoCuentaIxe(getCuentaIxe());
            }

            prevPage.setPaginaAnterior(getPageName());
            _logger.info("CapturaBeneficiario:::: Pagina Seteada: " + prevPage.getPaginaAnterior());
            cycle.activate(getPaginaAnterior());
        }
    }

    /**
     * Regresa a la pantalla de Aprobaci&oacute;n de L&iacute;neas de Cr&eacute;dito.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cancelar(IRequestCycle cycle) {
        cycle.activate(getPaginaAnterior());
    }

    /**
     * Ejecuta la b&uacute;squeda de registros con los criterios ingresados por el usuario.
     *
     * @param cycle El ciclo de la p&acute;gina
     */
    public void busquedaBeneficiarios(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");

        setListaResultadosCuenta(null);
        setListaResultadosAltamira(null);

        if ((StringUtils.isNotEmpty(getCuentaIxe()) && (getCuentaIxe().length() < 3))) {
            delegate.record("Los campos deben tener m\u00e1s de 2 caracteres.", null);

            return;
        }
        else if (StringUtils.isEmpty(getCuentaIxe().trim())) {
            delegate.record("Debe definir al menos un criterio de b\u00fasqueda.", null);

            return;
        }

        List personasCuentaRolBup = new ArrayList(); //cuentas encontradas en la bup
        List infoCuentaAltamiraList = new ArrayList(); // informacion de la cuenta en altamira
        String modoBupAltamira = null; //El modo de trabajo de sica.
        CuentaMigradaDto cuentaMigradaDto = null; //informacion de cuenta  migrada

        try {
            if (StringUtils.isNotEmpty(getCuentaIxe())) {
                //Se obtiene el modo de trabajo
                modoBupAltamira = getSicaServiceData().findParametro(ParametroSica.MODO_BUP_ALTAMIRA)
                                      .getValor().trim();

                _logger.debug("MODO_BUP_ALTAMIRA:  " + modoBupAltamira);

                if (modoBupAltamira.equals(BUP_ALTAMIRA)) {
                    //Lo primero que hay que hacer es revisar si la cuenta ya está migrada
                    /** Apagado de phoenix **/
//                    cuentaMigradaDto = getPhoenixCuentaMigradaDao().buscarCuenta(getCuentaIxe()
//                                                                                   .trim());

                    if (cuentaMigradaDto != null) { //la cuenta está migrada, se busca en Altamira
                        _logger.debug("Cuenta: " + getCuentaIxe() + " migrada: " +
                            cuentaMigradaDto.toString());

                        //Se consulta la cuenta en Altamira con noCuenta recuperada de la tabla de cuentas migradas
                        infoCuentaAltamiraList = buscarCuentaAltamira(cuentaMigradaDto.getCuentaAltamira(),
                                cuentaMigradaDto.getNoClienteAltamiraIxe(), modoBupAltamira);
                    }
                    else { //cuenta No migrada, se busca en la bup el titular (verifica si la cuenta esta espejeada)
                        _logger.debug("Cuenta: " + getCuentaIxe() +
                            " no migrada buscando en la BUP");

                        List personasList = getSicaServiceData().findPersonasByCuentaEje(getCuentaIxe());

                        for (Iterator iter = personasList.iterator(); iter.hasNext();) {
                            final com.ixe.ods.bup.model.PersonaCuentaRol pcr = (com.ixe.ods.bup.model.PersonaCuentaRol) iter.next();

                            if (pcr.getId().getRol().getIdRol().equals("TIT")) {
                                personasCuentaRolBup.add(pcr); //solo se muestra el titular
                                _logger.debug("Cuenta: " + getCuentaIxe() +
                                    " encontrada en la BUP: " + pcr.toString());

                                break;
                            }
                        }

                        //No se encontró en Bup, puede ser cuenta formato Altamira
                        if ((personasCuentaRolBup.size() < 1) && (getCuentaIxe().indexOf("-") < 0)) { //checar que no traiga guiones. 
                            _logger.debug("Cuenta: " + getCuentaIxe() +
                                " NO encontrada en la BUP, buscando en ALTAMIRA");
                            infoCuentaAltamiraList = buscarCuentaAltamira(getCuentaIxe(), null,
                                    modoBupAltamira); //se le manda noCuenta Capturada
                        }
                    }
                }
                else if (modoBupAltamira.equals(ALTAMIRA)) {
                    //se consulta la cuenta en altamira 
                    _logger.debug("Buscando Cuenta: " + getCuentaIxe() + " en Altamira");
                    /** Apagado de phoenix **/
                    //si trae guion, se obtiene primero formato Altamira
//                    if(StringUtils.isNotEmpty(getCuentaIxe()) && getCuentaIxe().trim().indexOf("-") > 0) {
//                    	 cuentaMigradaDto = getPhoenixCuentaMigradaDao().buscarCuenta(getCuentaIxe()
//                                 .trim());
//                    	 
//                    	 if(cuentaMigradaDto != null) { //se encontro en tabla de cuentas migradas, se busca en altamira.
//                    		 infoCuentaAltamiraList = buscarCuentaAltamira(cuentaMigradaDto.getCuentaAltamira(), 
//                        			 cuentaMigradaDto.getNoClienteAltamiraIxe(), modoBupAltamira);	 
//                    	 }
//                    	 else{ //En modo Altamira, ya no se busca en la BUP.
//                    		throw new  SicaAltamiraException("No se encontr\u00f3 la cuenta: " + getCuentaIxe());
//                    	 }
//                    }
//                    else {
                    	infoCuentaAltamiraList = buscarCuentaAltamira(getCuentaIxe().trim(), null, 
                        		modoBupAltamira); //se le manda noCuenta Capturada	
//                    }
                }
                else {
                    delegate.record("El par\u00E1metro SICA MODO_BUP_ALTAMIRA no tiene un valor v\u00E1lido",
                        null);

                    return;
                }
            }

            if ((infoCuentaAltamiraList != null) && !infoCuentaAltamiraList.isEmpty()) {
                setResultadoBusqueda(true);
                setListaResultadosAltamira(infoCuentaAltamiraList);
            }
            else if (personasCuentaRolBup.isEmpty() && infoCuentaAltamiraList.isEmpty()) {
                delegate.record("No se encontr\u00f3 el Cliente o la Empresa con los datos ingresados.",
                    null);

                setResultadoBusqueda(true);
            }
            else if (!personasCuentaRolBup.isEmpty()) {
                setListaResultadosCuenta(personasCuentaRolBup);
                setResultadoBusqueda(true);
            }
        }catch (SicaException se){
        	delegate.record(se.getMessage(), null);
    	}catch (SicaAltamiraException sae) {
            delegate.record("Error Servicio Altamira: " + sae.getLocalizedMessage(), null);
        }
    }

    /**
     * Metodo que permite
     * buscar en Altamira la información de una cuenta,
     * en caso de encontrarla también se recupera el
     * id_persona de la BUP a partir del número de cliente
     * en altamira.
     *
     * @param noCuenta El numero de cuenta que se
     *                         desea consultar.
     *
     * @param noClienteAltamira el número de cliente
     *                 asociado a la cuenta en Altamira, de no indicarse
     *                 se recupera del servicio que obtiene la informacion
     *                 de la cuenta.
     *
     * @return <code>List</code> de objetos <code>InfoCuentaAltamiraDto</code>
     *                         con la información encontrada.
     *
     * @throws SicaAltamiraException en caso de algún error
     *                         en la ejecucion del método.
     */
    private List buscarCuentaAltamira(String noCuenta, String noClienteAltamira,
        String modoBupAltamira) throws SicaAltamiraException {
        String idPersona = null;
        InfoCuentaAltamiraDto infoCuentaAltamiraDto = null;
        List infoCuentaAltamiraList = null;
        String mnemonicoOperacion = getMnemonico();
        //Se obtiene el usuario
        Visit visit = (Visit) getVisit();
        Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser()
                                                                             .getIdPersona()
                                                                             .intValue());
        
        //Se consulta la cuenta en Altamira
        infoCuentaAltamiraDto = getInformacionCuentaAltamiraService().consultaInformacionCuenta(noCuenta, visit.getTicket());
        validarDivisa(infoCuentaAltamiraDto, mnemonicoOperacion);
        
        //Si la cuenta existe en Altamira y se encuentra activa 
        if (infoCuentaAltamiraDto != null) {
            if ((noClienteAltamira != null) && (noClienteAltamira.trim().length() > 0)) {
                infoCuentaAltamiraDto.setNumeroCliente(noClienteAltamira);
            }

            //La cuenta proporcionada existe en Altamira y está activa 
            //se busca el id_persona (PEBN), solo si esta en el modo BUP_ALTAMIRA, 
            //ya hay interoperatividad y migración parcial de Altamira Ixe a Altamira Banorte.
            if (modoBupAltamira.equals(BUP_ALTAMIRA)) {
                if ((infoCuentaAltamiraDto.getNumeroCliente() != null) &&
                        (infoCuentaAltamiraDto.getNumeroCliente().trim().length() > 0)) {
                    //Es funcional solo para cuentas migradas, 
                    //no aplica para Nativas En Altamira Ixe ni Altamira Banorte.
                   /** La funcionalidad BUP_ALTAMIRA, está en desuso */
//                	 idPersona = getIdIxeAltamiraService().getIdClienteIxe(infoCuentaAltamiraDto.getNumeroCliente());

                    //De obtener un idPersona con valor 0 (cuenta nativa Altamira) se intenta
                    //buscar el id_persona en bup_persona_cuenta_rol a partir del número de cuenta
                    if ((idPersona != null) && idPersona.equals(ID_ERROR)) { //No hay relacion cliente Ixe/bnte
                        _logger.debug("Buscando la cuenta: " + noCuenta + " Espejeada a la BUP");
                        idPersona = getSicaServiceData().findIdPersonaCuentaAltamiraEspejeada(noCuenta);

                        if (idPersona == null) { //cuenta nativa no espejeada
                            _logger.debug("Cuenta " + noCuenta +
                                " no encontrada en BUP_PERSONA_CUENTA_ROL, " +
                                "se busca en SC_CUENTA_ALTAMIRA");

                            //se busca en SC_CUENTA_ALTAMIRA
                            idPersona = buscarIdPersonaPorCuentaAltamira(infoCuentaAltamiraDto,
                                    modoBupAltamira, usuario, visit.getTicket());
                        }
                    }
                }
                else { //no se recupero SIC de la B162
                    _logger.debug("No se puede obtener el cliente (SIC) para la cuenta:" +
                        noCuenta);
                    throw new SicaAltamiraException(
                        "Se requiere el n\u00famero de cliente para la cuenta " +
                        infoCuentaAltamiraDto.getNumeroCuenta());
                }
            } //end modo bup_altamira
            else if (modoBupAltamira.equals(ALTAMIRA)) {
                //Se verifica si la cuenta está espejeada a la BUP (BUP_PERSONA_CUENTA_ROL)
                _logger.debug("Buscando la cuenta: " + noCuenta + " Espejeada a la BUP");
                
                //se busca si está espejeada a la bup.
                if ((idPersona = getSicaServiceData().findIdPersonaCuentaAltamiraEspejeada(noCuenta)) == null) {
                    _logger.debug("Cuenta " + noCuenta +
                        " no encontrada en BUP_PERSONA_CUENTA_ROL, " +
                        " se busca en SC_CUENTA_ALTAMIRA");
                    idPersona = buscarIdPersonaPorCuentaAltamira(infoCuentaAltamiraDto,
                            modoBupAltamira, usuario, visit.getTicket());
                } //end if
                
            } //end modo Altamira
            

            _logger.debug("IdPersona: " + idPersona);

            //se asigna el idPersona de la bup
            infoCuentaAltamiraDto.setIdPersona(idPersona);

            //se agrega a la lista
            infoCuentaAltamiraList = new ArrayList();
            infoCuentaAltamiraList.add(infoCuentaAltamiraDto);
        } //end if Cuenta existe en Altamira y esta activa
        else {
            infoCuentaAltamiraList = new ArrayList(); //lista vacía
        }

        return infoCuentaAltamiraList;
    }

    /**
     * Valida si la cuenta de Altamira tiene la misma divisa que la pata que se esta operando 
     * 
     * @param infoCuentaAltamiraDto cuenta de Altamira.
     * @param mnemonicoOperacion mnemonico de la pata que se esta operando.
     * @return boolean false/true
     */
    private boolean validarDivisa(InfoCuentaAltamiraDto infoCuentaAltamiraDto,
			String mnemonicoOperacion) {
    	boolean resultado  = false;
    	String divisa      = infoCuentaAltamiraDto.getTipoDivisa();
    	String mapeoDivisa = divisa.equals(Divisa.DOLAR_ALTAMIRA)?Divisa.DOLAR:
    							divisa.equals(Divisa.MXN_ALTAMIRA)?Divisa.PESO:divisa;
    	int validarDivisa  = mnemonicoOperacion.indexOf(mapeoDivisa);
    	if(validarDivisa !=  -1 ){
    		resultado= true;
    	}else{
    		throw new SicaException("La moneda de la cuenta de cheques no corresponde a la moneda de la forma de pago/cobro que se quiere operar.");
    	}
    	return resultado;
	}

	/**
     *  Metodo que busca el idPersona (BUP)
     *          asociado a una cuenta Altamira [SC_CUENTA_ALTAMIRA].
     *
     * @param <code>InfoCuentaAltamiraDto</code>
     *                   con los datos de la cuenta en Altamira.
     * @param modoBupAltamira <code>String</code>
     *                   con el valor del parámetro MODO_BUP_ALTAMIRA
     *
     * @param <code>Usuario</code> el objeto del usuario de sistema.
     *
     * @return <code>String</code> con el identificador
     *                 de la persona en la BUP.
     *
     * @throws SicaAltamiraException en caso de algún error
     * 		al consultar el idPersona.
     */
    private String buscarIdPersonaPorCuentaAltamira(InfoCuentaAltamiraDto infoCuentaAltamiraDto,
        String modoBupAltamira, Usuario usuario, String ticket) throws SicaAltamiraException {
        String idPersona = null;

        if (modoBupAltamira.equals(BUP_ALTAMIRA)) {
            idPersona = getInformacionCuentaAltamiraService().
            		getIdPersonaModoBupAltamira(infoCuentaAltamiraDto, usuario, ticket);
        }
        else if (modoBupAltamira.equals(ALTAMIRA)) {
            idPersona = getInformacionCuentaAltamiraService().
            		getIdPersonaModoAltamira(infoCuentaAltamiraDto, usuario, ticket);
        }

        return idPersona;
    }

    /**
     * Regresa true si encontr&oacute; registros con el
     * criterio de b&uacute;squeda ingresado; false en
     * otro caso
     *
     * @return boolean si econtro o no registros
     */
    public abstract boolean getResultadoBusqueda();

    /**
     * Fija el valor de <code>resultadoBusqueda</code>.
     *
     * @param resultadoBusqueda El valor a asignar.
     */
    public abstract void setResultadoBusqueda(boolean resultadoBusqueda);

    /**
     * Regresa la lista de Personas encotradas en bup_persona_cuenta_rol con el
     * criterio de b&uacute;squeda ingresado
     *
     * @return List lista de Personas
     */
    public abstract List getListaResultadosCuenta();

    /**
     * Fija el valor de <code>listaResultadosCuenta</code>.
     *
     * @param listaResultadosCuenta El valor a asignar.
     */
    public abstract void setListaResultadosCuenta(List listaResultadosCuenta);

    /**
     * Fija el valor de <code>cuentaContratoSica</code>.
     *
     * @param contratoSica El valor a asignar.
     */
    public abstract void setContratoSica(ContratoSica contratoSica);

    /**
     * Regresa el valor de <code>cuentaContratoSica</code>.
     *
     * @return String
     */
    public abstract ContratoSica getContratoSica();

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
     * Regresa el valor de <code>modoSubmit</code>.
     *
     * @return String
     */
    public abstract int getModoSubmit();

    /**
     * Fija el valor de <code>modoSubmit</code>.
     *
     * @param modoSubmit El valor a asignar.
     */
    public abstract void setModoSubmit(int modoSubmit);

    /**
     * Setter de la lista de resultados
     *         de una cuenta buscada en altamira.
     * @param listaResultadosAltamira lista de objetos
     *                 <code>
     */
    public abstract void setListaResultadosAltamira(List listaResultadosAltamira);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract List getListaResultadosAltamira();

    /**
     * Regresa el valor de <code>cuentaIxe</code>.
     *
     * @return String
     */
    public abstract String getCuentaIxe();

    /**
     * Fija el valor de <code>cuentaIxe</code>.
     *
     * @param cuentaIxe El valor a asignar.
     */
    public abstract void setCuentaIxe(String cuentaIxe);

    /**
     * Fija el valor de <code>beneficiarios</code>.
     *
     * @param beneficiarios El valor a asignar.
     */
    public abstract void setBeneficiarios(List beneficiarios);

    /**
     * Regresa el valor de beneficiarios.
     *
     * @return List.
     */
    public abstract List getBeneficiarios();

    /**
     * Regresa el valor de <code>idPersonaCliente</code>.
     *
     * @return String.
     */
    public abstract int getIdPersonaCliente();

    /**
     * Fija el valor de <code>idPersonaCliente</code>.
     *
     * @param idPersonaCliente El valor a asignar.
     */
    public abstract void setIdPersonaCliente(int idPersonaCliente);

    /**
     * Fija el valor de <code>nombreCliente</code>.
     *
     * @param nombreCliente El valor a asignar.
     */
    public abstract void setNombreCliente(String nombreCliente);

    
    /**
     * Este dato se necesita para validar si la cuenta en Altamira
     * corresponde a la divisa que esta operando.
     * @param mnemonico
     */
    public abstract void setMnemonico(String mnemonico);
	
	public abstract String getMnemonico();
    
	
    /**
     * Obtiene una referencia al EJB del servicio de Contratacion <code>SicaConsultaRelacionesCuentaService</code>.
     *
     * @return SicaConsultaRelacionesCuentaService
     */
    private SicaConsultaRelacionesCuentaService getSicaConsultaRelacionesCuentaService() {
        return (SicaConsultaRelacionesCuentaService) getApplicationContext().getBean("sicaConsultaRelacionesCuentaService");
    }

    /**
     * Obtiene una referencia al EJB del servicio de Contratacion <code>SicaRegistroRelacionCuentaPersonaService</code>.
     *
     * @return SicaRegistroRelacionCuentaPersonaService
     */
    private SicaRegistroRelacionCuentaPersonaService getSicaRegistroRelacionCuentaPersonaService() {
        return (SicaRegistroRelacionCuentaPersonaService) getApplicationContext().getBean("sicaRegistroRelacionCuentaPersonaService");
    }

    /**
     * Obtiene una referencia al EJB del servicio de Contratacion <code>SicaConsultaProductosPersonaService</code>.
     *
     * @return SicaConsultaProductosPersonaService
     */
    private SicaConsultaProductosPersonaService getSicaConsultaProductosPersonaService() {
        return (SicaConsultaProductosPersonaService) getApplicationContext().getBean("sicaConsultaProductosPersonaService");
    }

    /**
     * Obtiene una referencia al EJB del servicio de Contratacion <code>SicaBusquedaPersonaService</code>.
     *
     * @return SicaBusquedaPersonaService
     */
    private SicaBusquedaPersonaService getSicaBusquedaPersonaService() {
        return (SicaBusquedaPersonaService) getApplicationContext().getBean("sicaBusquedaPersonaService");
    }
    /** La funcionalidad BUP_ALTAMIRA, esta en desuso */
//    /**
//     * Obtiene una referencia al WS para la consulta de Id de cliente Ixe - Altamira
//     * @return una objeto <code>IdIxeAltamiraService</code>
//     */
//    private IdIxeAltamiraService getIdIxeAltamiraService() {
//        return (IdIxeAltamiraService) getApplicationContext().getBean("consultaIdIxeAltamiraService");
//    }

    /**
     * Getter InformacionCuentaAltamiraService
     *
     * @return InformacionCuentaAltamiraService
     */
    private InformacionCuentaAltamiraService getInformacionCuentaAltamiraService() {
        return (InformacionCuentaAltamiraService) getApplicationContext().getBean("informacionCuentaAltamiraService");
    }
    /** Apagado de phoenix */
//    /**
//     * Getter PhoenixCuentaMigradaDao
//     *
//     * @return PhoenixCuentaMigradaDao
//     */
//    private PhoenixCuentaMigradaDao getPhoenixCuentaMigradaDao() {
//         return (PhoenixCuentaMigradaDao) getApplicationContext().getBean("phoenixCuentaMigradaDao");
//    }
}
