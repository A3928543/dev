/*
 * $Id: CapturaDatosFacturacion.java,v 1.16.48.3.2.2.28.1 2020/12/01 04:53:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ixe.ods.sica.model.LogAuditoria;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import com.ixe.bean.Usuario;
import com.ixe.bean.bup.Direccion;
import com.ixe.bean.bup.MedioContacto;
import com.ixe.bean.bup.Persona;
import com.ixe.contratacion.ContratacionException;
import com.ixe.ods.bup.model.PersonaMoral;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaConsultaMediosContactoPersonaService;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Actividad;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.services.ContratoDireccionesService;
import com.ixe.ods.sica.utils.MapUtils;

/**
 * <p>P&aacute;gina que permite al promotor capturar o modificar los de facturaci&oacute;n de un
 * deal, como son el RFC, la raz&oacute;n social y la direcci&oacute;n fiscal del cliente.</p>
 * <p/>
 * <p> El usuario puede elegir el nombre de un combo box de nombres aplicables, o teclear otro
 * libremente.</p>
 *
 * @author Jean C. Favila
 * @version $Revision: 1.16.48.3.2.2.28.1 $ $Date: 2020/12/01 04:53:01 $
 */
public abstract class CapturaDatosFacturacion extends AbstractCapturaDirecciones {

    /**
     * Inicializa la lista de corporativos, la direcc&oacute;n fiscal y la raz&oacute;e social.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        Deal deal = getDeal();
        try {
            setCorporativos(getSicaServiceData().findCorporativosByPersonaMoral(deal.getCliente().
                    getIdPersona()));
            setOtroEmailSeleccionado(!StringUtils.isEmpty(deal.getEmailFacturaOtro()));
            inicializarEmails();
            //setTipoNombre(TIPO_NOMBRE_CORPORATIVO);
            if (! StringUtils.isEmpty(deal.getNombreFactura())) {
                boolean esCorporativo = false;
                for (int i = 0; i < getCorporativos().length && !esCorporativo; i++) {
                    if (deal.getNombreFactura().equals(getCorporativos()[i])) {
                        setNombreCorporativo(deal.getNombreFactura());
                        esCorporativo = true;
                    }
                }
            }
            //Servicio de Contratacion
            ContratoDireccionesService cds = (com.ixe.ods.sica.services.ContratoDireccionesService)
                    getApplicationContext().getBean("contratoDireccionesService");
            deal.setDirFactura(cds.getDireccionFiscalParaPersona(getDeal().getCliente().
                    getIdPersona().intValue()));
            setIdDireccionMensajeria(deal.getDirFacturaMensajeria() != null ?
                    deal.getDirFacturaMensajeria().getIdDireccion() : null);
        }
        catch (ContratacionException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
        //Poniendo el RFC del Cliente como el RFC Default para la Factura
        deal.setRfcFactura(deal.getCliente().getRfc());
    }

    /**
     * Inicializa las
     */
    private void inicializarEmails() throws ContratacionException {
        List mediosContacto = new ArrayList();
        // Se obtienen los medios de contacto (emails):
        if (getDeal().getCliente() != null) {
            Integer idPersona = getDeal().getCliente().getIdPersona();
            mediosContacto = getConsultaMediosContactoService().obtenMediosContacto(
                    idPersona.intValue());
            PersonaMoral corp = getSicaServiceData().findCorporativo(idPersona);
            if (corp != null && !corp.getIdPersona().equals(idPersona)) {
                mediosContacto.addAll(getConsultaMediosContactoService().obtenMediosContacto(
                    corp.getIdPersona().intValue()));
            }
        }
        List emails = new ArrayList();
        // Se eliminan los que no aplican para facturaci&oacute;n electr&oacute;nica:
        for (Iterator it = mediosContacto.iterator(); it.hasNext();) {
            MedioContacto mc = (MedioContacto) it.next();
            emails.add(MapUtils.generar(new String[] {"email", "seleccionado", "verificado"},
                    new Object[]{mc.getValor(), mc.getEsMedConValidoFE(),
                    mc.getEsMedConValidoFE()}));
        }
        setEmails(emails);
    }

    /**
     * Mensaje asociado al submit de la forma. No hace nada.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {
    }

    /**
     * Si no hay errores de validaci&oacute;n, asigna el nombre del cliente al deal, de acuerdo con
     * la selecci&oacute;n del cliente, y activa la p&aacute;gina de CapturaDeal.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void aceptar(IRequestCycle cycle) {
        try {
            if (getDelegate().getHasErrors()) {
                return;
            }
            if (!isOtroEmailSeleccionado()) {
                getDeal().setEmailFacturaOtro(null);
            }
            getDeal().setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
            if (MEDIO_DIST_MENSAJERIA.equals(getMedioDistribucion())) {
                getDeal().setEmailFactura(null);
                getDeal().setEmailFacturaOtro(null);
                getDeal().setCambioEmail(" ");
            }
            else {
                getDeal().setDirFacturaMensajeria(null);
            }
            if (getDeal().getCliente().getVerificaFe() == null ||
                    getDeal().getCliente().getVerificaFe().intValue() < 1) {
                throw new SicaException("Opci\u00f3n de Facturaci\u00f3n Electr\u00f3nica " +
                        "vac\u00edo, favor de comunicarse con la Mesa de Control.");

            }
            if (getDeal().getDirFactura() != null) {
            	
            	if((getDeal().getDirFactura().getVerificadoFE() == null ||
                            0 == getDeal().getDirFactura().getVerificadoFE().intValue())) {
            		throw new SicaException("Error en separaci\u00f3n de campos de n\u00famero en la " +
            			"direcci\u00f3n, favor de comunicarse con la Mesa de Control.");
            	}
            
            	//validaDireccionFacturacion
            	if(!StringUtils.isEmpty(getCamposFaltantesDireccionFactura())) {
            		throw new SicaException(
            			"El deal no puede facturarse porque hacen falta campos de direcci\u00f3n: " 
            			+ getCamposFaltantesDireccionFactura() + " favor de comunicarse con la Mesa de Control.");
            	}
            
            	//valida longitud de campos direccion factura
            	if(!StringUtils.isEmpty(getLongMaxCamposFactura())) {
            		throw new SicaException(
            			"El deal no puede facturarse porque existen campos con longitud inv\u00E1lida"
            			+ getLongMaxCamposFactura() + ", favor de comunicarse con la Mesa de Control.");
            	}
            
            	//valida entidad federativa.
            	if(StringUtils.isEmpty(getDeal().getDirFactura().getIdEntFederativa()) ||
            			!isEntidadFederativaValida()) {

            		throw new SicaException("El deal no puede facturarse porque la entidad federativa: " 
            				+  getDeal().getDirFactura().getIdEntFederativa() + " no es v\u00E1lida, "
            				+ "favor de comunicarse con la Mesa de Control.");
            	}
            }
            getDeal().setNombreFactura(getNombreCorporativo());
            getDeal().setCambioRfc(getDeal().getCliente() != null &&
                    getDeal().getRfcFactura() != null &&
                    !getDeal().getRfcFactura().equals(getDeal().getCliente().getRfc()) ?
                    Deal.EV_SOLICITUD : Deal.EV_NORMAL);
            getDeal().setCambioEmail(getDeal().getEmailFacturaOtro() != null &&
                    !StringUtils.isEmpty(getDeal().getEmailFacturaOtro().trim()) ?
                    Deal.EV_SOLICITUD : Deal.EV_NORMAL);
            if (MEDIO_DIST_EMAIL.equals(getMedioDistribucion())) {
                getDeal().setEmailFactura(generarEmailsParaActualizacion());
            }
            else {
                getDeal().setDirFacturaMensajeria(generarDireccionParaMensajeria());
            }
            if (getDeal().getIdDeal() > 0) {
                auditar(new Integer(getDeal().getIdDeal()), LogAuditoria.CAMBIO_DATOS_FACT,
                        (getDeal().getCliente() != null && getDeal().getRfcFactura() != null &&
                                !getDeal().getRfcFactura().equals(getDeal().getCliente().getRfc()) ?
                                "rfc: " + getDeal().getRfcFactura() + " " : " ") +
                                "rs: " + getDeal().getNombreFactura() + " " +
                                "em: " + getDeal().getEmailFacturaOtro(), "INFO", "E");
                getWorkFlowServiceData().actualizarDatosDeal(getTicket(), getDeal());
                getWorkFlowServiceData().terminarActividadesParaDeal(getDeal(),
                        Actividad.RES_SOL_MODIF, ((Visit) getVisit()).getUser());
            }
            cycle.activate("CapturaDeal");
        }
        catch (SicaException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     *  Obtiene campos faltantes en la direccion
     *  	de facturacion.
     *  
     *  @return <code>String</code> con los campos faltantes
     *  	para la factura.
     */
    private String getCamposFaltantesDireccionFactura() {
    	
    	StringBuffer sb = new StringBuffer("");
    	
    	if(StringUtils.isEmpty(getDeal().getDirFactura().getCalleYNumero())){
    		sb.append("Calle, ");
    	}
    	if(StringUtils.isEmpty(getDeal().getDirFactura().getNumExterior())) {
    		sb.append("N\u00famero exterior, ");
    	}
    	if(StringUtils.isEmpty(getDeal().getDirFactura().getColonia())) {
    		sb.append("Colonia, ");
    	}
    	if(StringUtils.isEmpty(getDeal().getDirFactura().getCiudad()) &&
    			StringUtils.isEmpty(getDeal().getDirFactura().getDelegacionMunicipio())){
    		sb.append("Ciudad o Municipio, ");
    	}
    	if(StringUtils.isEmpty(getDeal().getDirFactura().getCodigoPostal())) {
    		sb.append("C\u00f3digo Postal, ");
    	}
    	if(getDeal().getDirFactura().getPais() == null) {
    		sb.append("Pa\u00eds, ");
    	}
    	
    	return sb.toString();
    }
    
    /** 
     *  Permite obtener los campos en la direccion
     * 	de la factura que no cumplen con los critrerios de 
     *  longitud.
     * @return <code>String</code> con los campos que no
     * 		cumplen con los criterios de longitud.
     */
    private String getLongMaxCamposFactura() {
    	StringBuffer sb = new StringBuffer("");
    	
    	com.ixe.ods.bup.model.Direccion direccion = getDeal().getDirFactura();
    	
    	if(direccion.getCalleYNumero().length() > LONG_MAX_CALLE) {
    		sb.append(", Calle: actual " + direccion.getCalleYNumero().length());
    		sb.append(" m\u00E1ximo " + LONG_MAX_CALLE);
    	}
    	if(direccion.getNumExterior().length() > LONG_MAX_NUM_INT_EXT) {
    		sb.append(", N\u00FAmero Exterior: actual " + direccion.getNumExterior().length());
    		sb.append(" m\u00E1ximo " + LONG_MAX_NUM_INT_EXT);
    	}
    	if(!StringUtils.isEmpty(direccion.getNumInterior()) &&
    			  direccion.getNumInterior().length() > LONG_MAX_NUM_INT_EXT) {
    		sb.append(", N\u00FAmero Interior: actual " + direccion.getNumInterior().length());
    		sb.append(" m\u00E1ximo " + LONG_MAX_NUM_INT_EXT);
    	}
    	if(direccion.getColonia().length() > LONG_MAX_COLONIA) {
    		sb.append(", Colonia: actual " + direccion.getColonia().length());
    		sb.append(" m\u00E1ximo " + LONG_MAX_COLONIA);
    	}
    	if(!StringUtils.isEmpty(direccion.getDelegacionMunicipio()) &&
  			direccion.getDelegacionMunicipio().length() > LONG_MAX_POBLACION) {
    		sb.append(", Delegaci\u00F3n o Municipio: actual " + direccion.getDelegacionMunicipio().length());
    		sb.append(" m\u00E1ximo " + LONG_MAX_POBLACION);
    	}
    	
    	return sb.toString();
    }
    
    /**
     * Realiza la validación de la entidad federativa
     * 	de la dirección de la factura.
     * 
     * @return <code>true</code> si la entidad federativa es válida.
     * 		   <code>false</code> en cualquier otro caso. 
     */
    private boolean isEntidadFederativaValida() {
    	boolean entidadFederativaValida = false;
    	String entidad = null;
    	Integer idDireccion  = null;
    	String pais = null;
    	
    	entidad = getDeal().getDirFactura().getIdEntFederativa();
    	idDireccion = getDeal().getDirFactura().getIdDireccion();
    	pais = getSicaServiceData().findPaisDireccion(idDireccion);
    	
    	if(pais != null ) {
    		
    		if(PAIS_MEXICO.equals(pais)) {
    			entidadFederativaValida  = 
    			getSicaServiceData().isEntidadValida(entidad, "MX");
    		}
    		else {
    			entidadFederativaValida =
    			getSicaServiceData().isEntidadValida(entidad, null);
    		}
    	}
    	
    	return entidadFederativaValida;
    }
    
    /**
     * Regresa el patr&oacute;n de la validaci&oacute;n para el rfc que es el siguiente:
     * "[A-Z,a-z,\\&]{3}[A-Z,a-z\\&]?\\d\\d(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])[A-Z,a-z,0-9]{3}".
     *
     * @return String.
     */
    public String getPatronRfc() {
        return "[A-Z,a-z,\\&]{3}[A-Z,a-z,\\&]?\\d\\d(0[1-9]|1[012])"
                + "(0[1-9]|[12][0-9]|3[01])[A-Z,a-z,0-9]{3}";
    }

    /**
     * Regresa el patr&oacute;n de la validaci&oacute;n para los correos adicionales.
     *
     * @return String.
     */
    public String getPatronEmails() {
        return "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*([;]\\s*\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)*";
    }

    /**
     * Regresa true si el pa&iacute;s de la direcci&oacute;n fiscal seleccionada es M&eacute;xico.
     *
     * @return boolean.
     */
    public boolean isValidaRFC() {
        return getDeal().getCliente().getPais() == null
                || Constantes.ID_PAIS_MEXICO_BUP.equals(getDeal().getCliente().getPais().
                getIdPais());
    }

    public IPropertySelectionModel getMediosDistribucionModel() {
        return new StringPropertySelectionModel(new String[]{MEDIO_DIST_EMAIL,
                MEDIO_DIST_MENSAJERIA});
    }

    /**
     * Regresa a la p&aacute;gina anterior.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void regresar(IRequestCycle cycle) {
        if (isModoPrimeraVez()) {
            getDeal().setFactura(Deal.SIN_FACTURA);
            getDeal().setDirFactura(null);
            getDeal().setNombreFactura(null);
            getDeal().setRfcFactura(null);
            if (getDeal().getIdDeal() > 0) {
                getSicaServiceData().update(getDeal());
            }
        }
        cycle.activate("CapturaDeal");
    }

    /**
     * Regresa la lista de correos electr&oacute;nicos separados por comas.
     *
     * @return String.
     * @throws SicaException Si: a) El usuario no especific&oacute; correos electr&oacute;nicos. b)
     * Si el usuario seleccion&oacute; el checkbox de otro correo y no lo ingres&oacute;.
     */
    private String generarEmailsParaActualizacion() throws SicaException {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (Iterator it = getEmails().iterator(); it.hasNext(); i++) {
            Map registroEmail = (Map) it.next();
            if (Boolean.TRUE.equals(registroEmail.get("seleccionado"))) {
                String valor = registroEmail.get("email") != null ?
                        ((String) registroEmail.get("email")).replaceAll("\\,", "\\;") : "";
               sb = sb.append((sb.toString().length() > 0 ? ";" : "")).append(valor);
            }
        }
        if (!isOtroEmailSeleccionado()) {
            getDeal().setEmailFacturaOtro(null);
        }
        if (isOtroEmailSeleccionado()) {
            if (getDeal().getEmailFacturaOtro().trim().length() < 1) {
                throw new SicaException("No se especificaron otros correos electr\u00f3nicos.");
            }
            if (!getDeal().getEmailFacturaOtro().matches(getPatronEmails())) {
                throw new SicaException("Una o m\u00e1s direcciones de correo electr\u00f3nico " +
                        "no son v\u00e1lidos");
            }
            if ((sb.toString() + getDeal().getEmailFacturaOtro().trim()).length() > 100) {
                throw new SicaException("La longitud de las direcciones de correo electr\u00f3nico no deben exceder 100 caracteres");
            }
        }
        else if (sb.toString().length() < 1) {
            throw new SicaException("Por favor seleccione alg\u00fan correo electr\u00f3nico.");
        }
        else if (sb.toString().length() > 100) {
            throw new SicaException("La longitud de las direcciones de correo electr\u00f3nico no deben exceder 100 caracteres");
        }
        return sb.toString();
    }

    /**
     *
     * @return
     * @throws SicaException
     */
    private com.ixe.ods.bup.model.Direccion generarDireccionParaMensajeria() throws SicaException {
        try {
            if (getIdDireccionMensajeria() == null) {
                throw new SicaException("Por favor seleccione una direcci\u00f3n de la lista.");
            }
            if (getIdDireccionMensajeria().intValue() > 0) {
                return getSicaServiceData().findDireccion(getIdDireccionMensajeria().intValue());
            }
            else {
                validarNuevaDireccion();
                Visit visit = (Visit) getVisit();
                Persona persona = getSicaBusquedaPersonaService().buscaPersonaPorId(getDeal().
                        getCliente().getIdPersona().intValue());
                Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().
                        getIdPersona().intValue());
                Direccion direccionTmp = new Direccion();
                direccionTmp.setIdTipoDireccion(com.ixe.bean.Constantes.ID_TIPO_DIR_SICA);
                direccionTmp.setCalle(getNuevaDireccion().getCalleYNumero());
                direccionTmp.setNumeroExterior(getNuevaDireccion().getNumeroExterior());
                direccionTmp.setNumeroInterior(getNuevaDireccion().getNumeroInterior());
                direccionTmp.setCiudad(getNuevaDireccion().getCiudad());
                direccionTmp.setCodigoPostal(getNuevaDireccion().getCodigoPostal());
                direccionTmp.setColonia(getNuevaDireccion().getColonia());
                direccionTmp.setDelegacion(getNuevaDireccion().getDelegacion());
                direccionTmp.setIdPais(com.ixe.bean.Constantes.ID_PAIS_MEXICO);
                direccionTmp.setEntFederativa(getNuevaDireccion().getEntFederativa());
                direccionTmp.setVerificadoFE(com.ixe.bean.Constantes.ID_TIPO_NUEVO_SICA_FE);
                Direccion nuevaDireccion = getSicaRegistroDireccionPersonaService().
                        registraDireccionCambios(persona, direccionTmp, usuario);
                return getSicaServiceData().findDireccion(nuevaDireccion.getIdDireccion());
            }
        }
        catch (ContratacionException e) {
            throw new SicaException(e.getMessage());
        }
    }

    private void validarNuevaDireccion() throws SicaException {
        Direccion dir = getNuevaDireccion();
        if (StringUtils.isEmpty(getNuevaDireccion().getCalleYNumero()) ||
                StringUtils.isEmpty(getNuevaDireccion().getNumeroExterior()) ||
                StringUtils.isEmpty(getNuevaDireccion().getCiudad()) ||
                StringUtils.isEmpty(getNuevaDireccion().getCodigoPostal()) ||
                StringUtils.isEmpty(getNuevaDireccion().getColonia()) ||
                StringUtils.isEmpty(getNuevaDireccion().getDelegacion()) ||
                StringUtils.isEmpty(getNuevaDireccion().getEntFederativa())) {
            throw new SicaException("Por favor capture los campos requeridos de la nueva " +
                    "direcci\u00f3n. ");
        }
    }

    /**
     * Regresa la referencia al bean sicaConsultaMediosContactoPersonaService del application
     * context de spring.
     *
     * @return SicaConsultaMediosContactoPersonaService.
     */
    private SicaConsultaMediosContactoPersonaService getConsultaMediosContactoService() {
        return (SicaConsultaMediosContactoPersonaService) getApplicationContext().
                getBean("sicaConsultaMediosContactoPersonaService");
    }

    /**
     * Regresa el valor de corporativos.
     *
     * @return String[].
     */
    public abstract String[] getCorporativos();

    /**
     * Fija el valor de corporativos.
     *
     * @param corporativos El valor a asignar.
     */
    public abstract void setCorporativos(String[] corporativos);

    /**
     * Regresa el valor de emails.
     *
     * @return List.
     */
    public abstract List getEmails();

    /**
     * Establece el valor de emails.
     *
     * @param emails El balor a asignar.
     */
    public abstract void setEmails(List emails);

    /**
     * Regresa el valor de modoPrimeraVez.
     *
     * @return boolean.
     */
    public abstract boolean isModoPrimeraVez();

    /**
     * Establece el valor de modoPrimeraVez.
     *
     * @param modoPrimeraVez El valor a asignar.
     */
    public abstract void setModoPrimeraVez(boolean modoPrimeraVez);

    /**
     * Regresa el valor de nombreCorporativo.
     *
     * @return String.
     */
    public abstract String getNombreCorporativo();

    /**
     * Fija el valor de nombreCorporativo.
     *
     * @param nombreCorporativo El valor a asignar.
     */
    public abstract void setNombreCorporativo(String nombreCorporativo);

    /**
     * Regresa el valor de otroNombre.
     *
     * @return String.
     */
    public abstract String getOtroNombre();

    /**
     * Fija el valor de otroNombre.
     *
     * @param otroNombre El valor a asignar.
     */
    public abstract void setOtroNombre(String otroNombre);

    /**
     * Regresa el valor de otroMailSeleccionado.
     *
     * @return boolean.
     */
    public abstract boolean isOtroEmailSeleccionado();

    /**
     * Establece el valor de otroMailSeleccionado.
     *
     * @param otroEmailSeleccionado El valor a asignar.
     */
    public abstract void setOtroEmailSeleccionado(boolean otroEmailSeleccionado);

    /**
     * Fija el valor de tipoNombre.
     *
     * @return String.
     */
    public abstract String getTipoNombre();

    /**
     * Fija el valor de tipoNombre.
     *
     * @param tipoNombre El valor a asignar.
     */
    public abstract void setTipoNombre(String tipoNombre);

    /**
     * Regresa el valor de idDireccionMensajeria.
     *
     * @return Integer.
     */
    public abstract Integer getIdDireccionMensajeria();

    /**
     * Establece el valor de idDireccionMensajeria.
     *
     * @param idDireccionMensajeria El valor a asignar.
     */
    public abstract void setIdDireccionMensajeria(Integer idDireccionMensajeria);

    /**
     * Regresa el valor de medioDistribucion.
     *
     * @return String.
     */
    public abstract String getMedioDistribucion();

    /**
     * Regresa el valor de nuevaDireccion.
     *
     * @return El valor a asignar.
     */
    public abstract Direccion getNuevaDireccion();

    /**
     * Constante para indicar que el nombre en la factura es la selecci&oacute;n del combo de
     * corporativos.
     */
    public static final String TIPO_NOMBRE_CORPORATIVO = "C";

    /**
     * Constante para indicar que el nombre en la factura es el del textfield de otro nombre.
     */
    public static final String TIPO_NOMBRE_OTRO = "O";

    /**
     * Constante para el valor de medio de env&iacute;o por Correo Electr&oacute;nico.
     */
    public static final String MEDIO_DIST_EMAIL = "Correo Electr\u00f3nico";

    /**
     * Constante para el valor de medio de env&iacute;o por Mensajer&iacute;a.
     */
    public static final String MEDIO_DIST_MENSAJERIA = "Mensajer\u00eda";
    
    /**
     * Constante para el pais MEXICO
     */
    public static final String PAIS_MEXICO = "MEXICO";
    
    /**
     * Longitud de la calle en dirección de factura
     */
    public static final int LONG_MAX_CALLE = 60;

    /**
     * Longitud de el numero interior y exterior en direccion de la factura
     */
    public static final int LONG_MAX_NUM_INT_EXT = 10;
    
    /**
     * Longitud de la colonia en la direccion de la factura.
     */
    public static final int LONG_MAX_COLONIA  = 35;
    
    /**
     * Longitud máxima en la población de la factura.
     */
    public static final int LONG_MAX_POBLACION = 40;
}
