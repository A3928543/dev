/*
 * $Id: CapturaBeneficiarios.java,v 1.11.60.1 2013/11/22 23:57:16 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import com.ixe.bean.Usuario;
import com.ixe.bean.bup.Persona;
import com.ixe.bean.bup.PersonaFisica;
import com.ixe.bean.bup.PersonaMoral;
import com.ixe.bean.bup.RelacionCuentaPersona;
import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.BupPersona;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaBusquedaPersonaService;
import com.ixe.ods.sica.SicaConsultaProductosPersonaService;
import com.ixe.ods.sica.SicaConsultaRelacionesCuentaService;
import com.ixe.ods.sica.SicaRegistroPersonaService;
import com.ixe.ods.sica.SicaRegistroRelacionCuentaPersonaService;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.pages.deals.plantillas.IAsignacionBeneficiario;
import com.ixe.ods.sica.pages.mesa.MigracionPlantillas;

/**
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.11.60.1 $ $Date: 2013/11/22 23:57:16 $
 */
public abstract class CapturaBeneficiarios extends SicaPage {

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
        setFisicaOMoral("FISICA");
        setPaterno("");
        setMaterno("");
        setNombre("");
        setRazonSocial("");
        setCuentaIxe("");
        setBeneficiarios(getSicaConsultaRelacionesCuentaService().obtenRelacionesCuentaParaRol(getSicaConsultaProductosPersonaService().obtenCuentaContrato(getContratoSica().getNoCuenta()), FacultySystem.BENEFICIARIO_SICA_ROL, null));
        setResultadoBusqueda(false);
        setMapaEdicionPlantilla(new HashMap());
    }

    /**
     * M&acute;todo que agrega un Beneficiario a un Cliente con Contrato SICA.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void agregar(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
        convertirAUpperCase();
        if (getModoSubmit() == 1) {
            if ((StringUtils.isNotEmpty(getRazonSocial()) && getRazonSocial().length() < 3) ||
                    (StringUtils.isNotEmpty(getNombre()) && getNombre().length() < 3) ||
                    (StringUtils.isNotEmpty(getPaterno()) && getPaterno().length() < 3) ||
                    (StringUtils.isNotEmpty(getMaterno()) && getMaterno().length() < 3)) {
                delegate.record("Los campos deben tener m\u00e1s de 2 caracteres.", null);
                return;
            }
            if ("FISICA".equals(getFisicaOMoral())) {
                if (StringUtils.isEmpty(getPaterno()) || StringUtils.isEmpty(getNombre())) {
                    delegate.record("Nombre y Apellido Paterno no deben ser campos vac\u00edos.", null);
                    return;
                }
            }
            else {
                if (StringUtils.isEmpty(getRazonSocial())) {
                    delegate.record("No debe haber campos vac\u00edos.", null);
                    return;
                }
            }
            if (delegate != null && delegate.getHasErrors()) {
                return;
            }
            RelacionCuentaPersona relacionCuentaPersona = new RelacionCuentaPersona();
            Visit visit = (Visit) getVisit();
            Persona persona;
            // Contratacion
            try {
                Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().
                        getIdPersona().intValue());
                if ("FISICA".equals(getFisicaOMoral())) {
                    PersonaFisica personaFisica = new PersonaFisica();
                    personaFisica.setNombre(getNombre());
                    personaFisica.setPaterno(getPaterno());
                    personaFisica.setMaterno(getMaterno());
                    personaFisica.setTipoPersona("PF");
                    persona = getSicaRegistroPersonaService().registraPersona(personaFisica, usuario);
                }
                else {
                    PersonaMoral personaMoral = new PersonaMoral();
                    personaMoral.setRazonSocial(getRazonSocial());
                    personaMoral.setTipoPersona("PM");
                    persona = getSicaRegistroPersonaService().registraPersona(personaMoral, usuario);
                }
                //Se actualiza el campo b_ilYtics a 1 para que no cambie el nombre, el proceso de Estandarizacion de la BUP
                
                getSicaServiceData().actualizarProcesoEstandarizacionBUP(PROCESO_ESTANDARIZACION_BUP_VALIDADO, new Integer(persona.getIdPersona()));
                
                relacionCuentaPersona.setIdPersona(persona.getIdPersona());
                relacionCuentaPersona.setCuenta(getSicaConsultaProductosPersonaService().
                        obtenCuentaContrato(getContratoSica().getNoCuenta()));
                relacionCuentaPersona.setIdRol(FacultySystem.BENEFICIARIO_SICA_ROL);
                getSicaRegistroRelacionCuentaPersonaService().registraBeneficiarioTrans(relacionCuentaPersona, getIdPersonaCliente(), usuario);
                setBeneficiarios(getSicaConsultaRelacionesCuentaService().obtenRelacionesCuentaParaRol(getSicaConsultaProductosPersonaService().obtenCuentaContrato(getContratoSica().getNoCuenta()), FacultySystem.BENEFICIARIO_SICA_ROL, null));
                //Asigna el nuevo beneficiario a la nueva plantilla que
                //sera editada en el sica
                if (PAGINA_MIGRACION_PLANTILLAS.equals(getPaginaAnterior())) {
                    MigracionPlantillas prevPage = (MigracionPlantillas) cycle.getPage(PAGINA_MIGRACION_PLANTILLAS);
                    HashMap mapaPlantilla = prevPage.getMapaEdicionMnemonico();
                    mapaPlantilla.remove("beneficiario");
                    Integer idPersona = new Integer(persona.getIdPersona());
                    com.ixe.ods.bup.model.Persona personaBeneficiario = (com.ixe.ods.bup.model.Persona) getSicaServiceData().find(Persona.class, idPersona);
                    prevPage.setNuevoBeneficiario(personaBeneficiario);
                    mapaPlantilla.put("beneficiario", personaBeneficiario.getNombreCompleto());
                    prevPage.setMapaEdicionMnemonico(mapaPlantilla);
                    prevPage.setPaginaAnterior(getPageName());
                    cycle.activate(prevPage.getPageName());
                    prevPage.setMostrarBusquedaYResultados(false);
                    prevPage.setEdicionMnemonico(true);
                }
                else if (CONSULTA_CLIENTES.equals(getPaginaAnterior())) {
                	cycle.activate(getPaginaAnterior());
                }
                else {
                	IAsignacionBeneficiario prevPage = (IAsignacionBeneficiario) cycle.getPage(getPaginaAnterior());
                	prevPage.creaBeneficiario((com.ixe.ods.bup.model.Persona) getSicaServiceData().find(com.ixe.ods.bup.model.Persona.class, new Integer(persona.getIdPersona())));
                	cycle.activate(getPaginaAnterior());
                }
            }
            catch (Exception e) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug(e);
                }
                if (delegate != null) {
                    delegate.record("Hubo un error al intentar dar de Alta el Beneficiario.", null);
                }
            }
        }
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
            Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().getIdPersona().intValue());
            RelacionCuentaPersona relacionCuentaPersona = new RelacionCuentaPersona();
            relacionCuentaPersona.setIdPersona(((Integer) cycle.getServiceParameters()[0]).intValue());
            relacionCuentaPersona.setCuenta(getSicaConsultaProductosPersonaService().obtenCuentaContrato(getContratoSica().getNoCuenta()));
            relacionCuentaPersona.setIdRol(FacultySystem.BENEFICIARIO_SICA_ROL);
            relacionCuentaPersonaMigPlantilla = relacionCuentaPersona;
            getSicaRegistroRelacionCuentaPersonaService().registraBeneficiarioTrans(relacionCuentaPersona, getIdPersonaCliente(), usuario);
            setBeneficiarios(getSicaConsultaRelacionesCuentaService().obtenRelacionesCuentaParaRol(getSicaConsultaProductosPersonaService().obtenCuentaContrato(getContratoSica().getNoCuenta()), FacultySystem.BENEFICIARIO_SICA_ROL, null));
        }
        catch (Exception e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
            if (delegate != null) {
                delegate.record("Hubo un error al intentar dar de Alta el Beneficiario.", null);
            }
        }
        if (PAGINA_MIGRACION_PLANTILLAS.equals(getPaginaAnterior())) {
            MigracionPlantillas prevPage = (MigracionPlantillas) cycle.getPage(PAGINA_MIGRACION_PLANTILLAS);
            HashMap mapaPlantilla = prevPage.getMapaEdicionMnemonico();
            Integer idPersona = new Integer(relacionCuentaPersonaMigPlantilla.getIdPersona());
            com.ixe.ods.bup.model.Persona personaBeneficiario = (com.ixe.ods.bup.model.Persona) getSicaServiceData().find(com.ixe.ods.bup.model.Persona.class, idPersona);
            prevPage.setNuevoBeneficiario(personaBeneficiario);
            prevPage.setMapaEdicionMnemonico(mapaPlantilla);
            cycle.activate(prevPage.getPageName());
            prevPage.setIdPersona(idPersona);
            prevPage.setMostrarBusquedaYResultados(false);
            prevPage.setEdicionMnemonico(true);
            prevPage.setPaginaAnterior(getPageName());
            prevPage.buscarCuentasPlantillas(cycle);
        }
        else if (CONSULTA_CLIENTES.equals(getPaginaAnterior())) {
        	cycle.activate(getPaginaAnterior());
        }
        else {
        	IAsignacionBeneficiario prevPage = (IAsignacionBeneficiario) cycle.getPage(getPaginaAnterior());
        	prevPage.creaBeneficiario((com.ixe.ods.bup.model.Persona) getSicaServiceData().find(com.ixe.ods.bup.model.Persona.class, (Integer) cycle.getServiceParameters()[0]));
        	cycle.activate(getPaginaAnterior());
        }
    }

    /**
     * Asigna as&iacute; mismo como beneficiario al cliente de la plantilla.
     *
     * @param cycle El ciclo de la p&aacute;gina
     */
    public void asignarBeneficiarioExistente(IRequestCycle cycle) {
        MigracionPlantillas prevPage = (MigracionPlantillas) cycle.getPage(PAGINA_MIGRACION_PLANTILLAS);
        HashMap mapaPlantilla = prevPage.getMapaEdicionMnemonico();
        Integer idPersona = new Integer(((Integer) cycle.getServiceParameters()[0]).intValue());
        com.ixe.ods.bup.model.Persona personaBeneficiario = (com.ixe.ods.bup.model.Persona) getSicaServiceData().find(com.ixe.ods.bup.model.Persona.class, idPersona);
        prevPage.setNuevoBeneficiario(personaBeneficiario);
        prevPage.setMapaEdicionMnemonico(mapaPlantilla);
        cycle.activate(prevPage.getPageName());
        prevPage.setIdPersona(idPersona);
        prevPage.setMostrarBusquedaYResultados(false);
        prevPage.setEdicionMnemonico(true);
        prevPage.setPaginaAnterior(getPageName());
        prevPage.buscarCuentasPlantillas(cycle);
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
     * Convierte lo escrito en los Criterios de Busqueda a Uppercase para evitar fallas en las busquedas
     * de la Base de Datos.
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
    }

    /**
     * Ejecuta la b&uacute;squeda de registros con los criterios ingresados por el usuario.
     *
     * @param cycle El ciclo de la p&acute;gina
     */
    public void busquedaBeneficiarios(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
        convertirAUpperCase();
        setListaResultados(null);
        setListaResultadosCuenta(null);
        if ((StringUtils.isNotEmpty(getRazonSocial()) && getRazonSocial().length() < 3) ||
                (StringUtils.isNotEmpty(getNombre()) && getNombre().length() < 3) ||
                (StringUtils.isNotEmpty(getPaterno()) && getPaterno().length() < 3) ||
                (StringUtils.isNotEmpty(getCuentaIxe()) && getCuentaIxe().length() < 3) ||
                (StringUtils.isNotEmpty(getMaterno()) && getMaterno().length() < 3)) {
            delegate.record("Los campos deben tener m\u00e1s de 2 caracteres.", null);
            return;
        }
        else if (StringUtils.isEmpty(getNombre().trim()) && StringUtils.isEmpty(getPaterno().trim()) &&
                StringUtils.isEmpty(getMaterno().trim()) && StringUtils.isEmpty(getRazonSocial().trim())
                && StringUtils.isEmpty(getCuentaIxe().trim())) {
            delegate.record("Debe definir al menos un criterio de b\u00fasqueda.", null);
            return;
        }
        else if (StringUtils.isNotEmpty(getCuentaIxe())) {
        	//Checar que Razon social este vacio
        	if(StringUtils.isNotEmpty(getRazonSocial().trim())) {
        		delegate.record("Para la b\u00fasqueda por Cuenta Ixe no es necesario el nombre de la Persona o de la Empresa.", null);
                return;
        	}
        	//Checar que Nombre, Paterno y Materno esten vacios
        	else if ((StringUtils.isNotEmpty(getPaterno().trim()) || StringUtils.isNotEmpty(getMaterno().trim()) ||
                    StringUtils.isNotEmpty(getNombre().trim()))){
        		delegate.record("Para la b\u00fasqueda por Cuenta Ixe no es necesario el Nombre de la Persona o de la Empresa.", null);
                return;
        	}
        }
        else if ((StringUtils.isNotEmpty(getPaterno().trim()) || StringUtils.isNotEmpty(getMaterno().trim()) ||
                StringUtils.isNotEmpty(getNombre().trim())) && StringUtils.isNotEmpty(getRazonSocial().trim()) && ! StringUtils.isNotEmpty(getCuentaIxe())) {
            delegate.record("La b\u00fasqueda es por Personas o por Empresas.", null);
            return;
        }
        else if (StringUtils.isNotEmpty(getRazonSocial().trim()) && (StringUtils.isNotEmpty(getPaterno().trim()) ||
                StringUtils.isNotEmpty(getMaterno().trim()) || StringUtils.isNotEmpty(getNombre().trim())) && ! StringUtils.isNotEmpty(getCuentaIxe())) {
            delegate.record("La b\u00fasqueda es por Personas o por Empresas.", null);
            return;
        }
        List personasBup = new ArrayList();
        List personasCuentaRolBup = new ArrayList();
        if (StringUtils.isNotEmpty(getCuentaIxe())) {
        	personasCuentaRolBup = getSicaServiceData().findPersonasByCuentaEje(getCuentaIxe());
        }
        else {
        	personasBup = getBupServiceData().findPersons(getRazonSocial(), getNombre(), getPaterno(), getMaterno(), "", "", "", "", MAX_RESULTS);
        }
        if (personasBup.isEmpty() && personasCuentaRolBup.isEmpty()) {
            delegate.record("No se encontr\u00f3 el Cliente o la Empresa con los datos ingresados.", null);
            setListaResultados(new ArrayList());
            setResultadoBusqueda(true);
        }
        else if (!personasBup.isEmpty()) {
        	setListaResultados(personasBup);
            setResultadoBusqueda(true);
        }
        else if (!personasCuentaRolBup.isEmpty()){
        	setListaResultadosCuenta(personasCuentaRolBup);
            setResultadoBusqueda(true);
        }
    }

    /**
     * Asigna el valor para personaMigPlant
     *
     * @param personaMigPlant Persona que se asigna como beneficiario de cuenta
     */
    public abstract void setPersonaMigPlant(com.ixe.ods.bup.model.Persona personaMigPlant);

    /**
     * Obtiene el valor de personaMigPlant
     *
     * @return com.ixe.ods.bup.model.Persona
     */
    public abstract com.ixe.ods.bup.model.Persona getPersonaMigPlant();

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
     * Regresa la lista de Personas encotradas en bup_persona con el
     * criterio de b&uacute;squeda ingresado
     *
     * @return List lista de Personas
     */
    public abstract List getListaResultados();

    /**
     * Fija el valor de <code>listaResultados</code>.
     *
	 * @param listaResultados El valor a asignar.
     */
    public abstract void setListaResultados(List listaResultados);
    
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
     * Popula el combo de Tipos de Persona.
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getComboTiposPersona() {
        return new StringPropertySelectionModel(new String[]{"FISICA", "MORAL"});
    }

    /**
     * Regresa la opcion seleccionada en el combo de Tipos de Persona.
     *
     * @return String
     */
    public abstract String getFisicaOMoral();

    /**
     * Fija la opcion seleccionada en el combo de Tipos de Persona.
     *
	 * @param fisicaOMoral El valor a asignar.
     */
    public abstract void setFisicaOMoral(String fisicaOMoral);

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
     * Regresa el valor de <code>paterno</code>.
     *
     * @return String
     */
    public abstract String getPaterno();

    /**
     * Fija el valor de <code>paterno</code>.
     *
	 * @param paterno El valor a asignar.
     */
    public abstract void setPaterno(String paterno);

    /**
     * Regresa el valor de <code>materno</code>.
     *
     * @return String
     */
    public abstract String getMaterno();

    /**
     * Fija el valor de <code>materno</code>.
     *
	 * @param materno El valor a asignar.
     */
    public abstract void setMaterno(String materno);

    /**
     * Regresa el valor de <code>nombre</code>.
     *
     * @return String
     */
    public abstract String getNombre();

    /**
     * Fija el valor de <code>nombre</code>.
     *
	 * @param nombre El valor a asignar.
     */
    public abstract void setNombre(String nombre);

    /**
     * Regresa el valor de <code>razonSocial</code>.
     *
     * @return String
     */
    public abstract String getRazonSocial();

    /**
     * Fija el valor de <code>razonSocial</code>.
     *
	 * @param razonSocial El valor a asignar.
     */
    public abstract void setRazonSocial(String razonSocial);
    
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
     * Obtiene el valor para MapaEdicioMnemonico que contiene
     * los datos de la plantilla seleccionada
     *
     * @return HashMap
     */
    public abstract HashMap getMapaEdicionPlantilla();

    /**
     * Asigna el valor para mapaEdicionPlantilla que contiene
     * los datos de la plantilla seleccionada
     *
     * @param mapaEdicionPlantilla Mapa con los datos de la plantilla seleccionada
     */
    public abstract void setMapaEdicionPlantilla(HashMap mapaEdicionPlantilla);

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

    /**
     * Obtiene una referencia al EJB del servicio de Contratacion <code>SicaRegistroPersonaService</code>.
     *
     * @return SicaRegistroPersonaService
     */
    private SicaRegistroPersonaService getSicaRegistroPersonaService() {
        return (SicaRegistroPersonaService) getApplicationContext().getBean("sicaRegistroPersonaService");
    }

    
    /**
     * Obtiene una referencia al EJB del servicio de Contratacion <code>SicaBusquedaPersonaService</code>.
     *
     * @return SicaBusquedaPersonaService
     */
//    private HibernateBupServiceData getBupServiceData() {
//        return (HibernateBupServiceData) getApplicationContext().getBean("sicaBusquedaPersonaService");
//    }
    
    
    /**
     * Constante para el N&uacute;mero M&aacute;ximo de Registros por Query.
     */
    private static final int MAX_RESULTS = 50;

	/**
     * Constante Pagina MigracionPlantillas
     */
    public final static String PAGINA_MIGRACION_PLANTILLAS = "MigracionPlantillas";
    
    /**
     * Constante Pagina ConsultaClientes
     */
    public final static String CONSULTA_CLIENTES = "ConsultaClientes";
    
    /**
     * Constante para proceso de estandarizacion y limpieza de la BUP
     */
    public static final Integer PROCESO_ESTANDARIZACION_BUP_VALIDADO = new Integer(1);
}
