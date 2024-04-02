/*
 * $Id: CapturaDatosEnvio.java,v 1.19 2009/08/03 22:02:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

import com.ixe.bean.Constantes;
import com.ixe.bean.Usuario;
import com.ixe.bean.bup.Direccion;
import com.ixe.bean.bup.Persona;
import com.ixe.contratacion.ContratacionException;
import com.ixe.ods.sica.SicaBusquedaPersonaService;
import com.ixe.ods.sica.SicaConsultaDireccionesPersonaService;
import com.ixe.ods.sica.SicaRegistroDireccionPersonaService;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * P&aacute;gina para la Captura de Datos de Env&iacute;o del Deal.
 * Se utilizan los Servicios de Contrataci&oacute;n de Mensajeria.
 *
 * @author Jean C. Favila, Javier Cordova (jcordova)
 * @version $Revision: 1.19 $ $Date: 2009/08/03 22:02:36 $
 */
public abstract class CapturaDatosEnvio extends SicaPage {

    /**
     * Limpia variables y precarga informaci&oacute;n cada que esta se activa la P&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
    	super.activate(cycle);
        IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
        limpiarVariables();
        List direcciones = new ArrayList();
        if (getDeal().getDireccion() != null) {
            setIdDireccion(getDeal().getDireccion().getIdDireccion().intValue());
        }
        //Servicio de Contratacion
        try {
	        Persona persona = getSicaBusquedaPersonaService().buscaPersonaPorId(getDeal().
                    getCliente().getIdPersona().intValue());
            List list = (ArrayList) getSicaConsultaDireccionesPersonaService().
                    obtenDirecciones(persona, Constantes.ID_RES_ARRAY_LIST);
            for (Iterator it = list.iterator(); it.hasNext();) {
                Direccion dir = (Direccion) it.next();
                if (getDeal().getDireccion() == null) {
                    if ("S".equals(dir.getEsFiscal())
                            || Constantes.ID_TIPO_DIR_SICA.equals(dir.getIdTipoDireccion())) {
                        setIdDireccion(dir.getIdDireccion());
                    }
	            }
	            direcciones.add(dir);
	        }
            if (!direcciones.isEmpty()) {
                Collections.sort(direcciones, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        Direccion d1 = (Direccion) o1;
                        Direccion d2 = (Direccion) o2;
                        return d1.toString().compareTo(d2.toString());
                    }
                });
            }
	        setDirecciones(direcciones);
        }
        catch (Exception e) {
            warn(e.getMessage(), e);
	        delegate.record("Hubo un error al intentar obtener las Direcciones del Cliente.",
                    null);
        }
    }

    /**
     * Agrega una Direcci&oacute;n de Env&ioacute;o seleccionada o escrita por el Promotor.
     * Utiliza los Servicios de Contrataci&oacute;n de Mensajeria de Consulta de Direcciones y
     * Alta R&aacute;pida de Direcciones de Env&iacute;o de un Cliente.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void aceptar(IRequestCycle cycle) {
        IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
        Visit visit = (Visit) getVisit();
        if (getIdDireccion() < 0) {
            delegate.record("Debe seleccionar una Direcci\u00f3n existente. En caso contrario, " +
                    "debe seleccionar la opcion Otra y teclear los datos requeridos.", null);
            return;
        }
        if (getIdDireccion() == 0) {
        	if (delegate.getHasErrors()) {
                return;
            }
            try {
	            Persona persona = getSicaBusquedaPersonaService().buscaPersonaPorId(getDeal().
                        getCliente().getIdPersona().intValue());
	            Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(visit.getUser().
                        getIdPersona().intValue());
	            Direccion direccionTmp = new Direccion();
	            direccionTmp.setIdTipoDireccion(Constantes.ID_TIPO_DIR_SICA);
	            direccionTmp.setCalle(getCalleYNo());
                direccionTmp.setNumeroExterior(getNumeroExterno());
                direccionTmp.setNumeroInterior(getNumeroInterno());
	            direccionTmp.setCiudad(getCiudad());
	            direccionTmp.setCodigoPostal(getCodigoPostal());
	            direccionTmp.setColonia(getColonia());
	            direccionTmp.setDelegacion(getDelegacion());
	            direccionTmp.setIdPais(Constantes.ID_PAIS_MEXICO);
	            direccionTmp.setEntFederativa(getEstado());
                direccionTmp.setVerificadoFE(com.ixe.bean.Constantes.ID_TIPO_NUEVO_SICA_FE);
	            Direccion nuevaDireccion = getSicaRegistroDireccionPersonaService().
                        registraDireccionCambios(persona, direccionTmp, usuario);
	            getDeal().setDireccion(getSicaServiceData().findDireccion(nuevaDireccion.getIdDireccion()));
	            if (getDeal().getIdDeal() > 0) {
	            	getWorkFlowServiceData().actualizarDatosDeal(getTicket(), getDeal());
	            }
            }
            catch (ContratacionException e) {
                warn(e.getMessage(), e);
                delegate.record(e.getMessage(), null);
                return;
            }
            catch (Exception e) {
                warn(e.getMessage(), e);
    	        delegate.record("Hubo un error al intentar dar de Alta la Direcci\u00f3n tecleada "
                        + "y asignarla al Deal.", null);
    			return;
            }
        }
        else {
            try {
            	com.ixe.ods.bup.model.Direccion dire = getSicaServiceData().findDireccion(getIdDireccion());
                if (dire.getCalleYNumero() == null) {
                	dire.setCalleYNumero("");
                }
                if (dire.getCiudad() == null) {
                	dire.setCiudad(" ");
                }
                if (dire.getCodigoPostal() == null) {
                	dire.setCodigoPostal(" ");
                }
                if (dire.getColonia() == null) {
                	dire.setColonia(" ");
                }
                if (dire.getDelegacionMunicipio() == null) {
                	dire.setDelegacionMunicipio(" ");
                }
                if (dire.getIdEntFederativa() == null) {
                	dire.setIdEntFederativa(" ");
                }
                if (dire.getFiscal() == null) {
                	dire.setFiscal(Boolean.FALSE);
                }
            	getDeal().setDireccion(dire);
            	if (getDeal().getIdDeal() > 0) {
                    getWorkFlowServiceData().actualizarDatosDeal(getTicket(), getDeal());
	            }
            }
            catch (Exception e) {
                warn(e.getMessage(), e);
                delegate.record("Hubo un error al intentar asignar la Direcci\u00f3n seleccionada "
                        + "al Deal.", null);
    			return;
            }
        }
        cycle.activate(getPaginaAnterior());
    }

    /**
     * Regresa a la p&aacute;gina anterior.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void regresar(IRequestCycle cycle) {
        if (isModoPrimeraVez()) {
            getDeal().setMensajeria(false);
            getDeal().setDireccion(null);
            if (getDeal().getIdDeal() > 0) {
                getSicaServiceData().update(getDeal());
            }
        }
        cycle.activate(getPaginaAnterior());
    }

    /**
     * Limpia las Variables de la p&aacute;gina.
     */
    private void limpiarVariables() {
        setCalleYNo("");
        setCiudad("");
        setCodigoPostal("");
        setColonia("");
        setDelegacion("");
        setEstado("");
        setIdDireccion(-1);
        setNumeroExterno("");
        setNumeroInterno("");
        setDirecciones(null);
    }

    /**
     * Regresa el valor de deal.
     *
     * @return Deal.
     */
    public abstract Deal getDeal();

    /**
     * Fija el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public abstract void setDeal(Deal deal);

    /**
     * Regresa el valor de idDireccion.
     *
     * @return int.
     */
    public abstract int getIdDireccion();

    /**
     * Fija el valor de idDireccion.
     *
     * @param idDireccion El valor a asignar.
     */
    public abstract void setIdDireccion(int idDireccion);

    /**
     * Fija la Lista de Direcciones de Mensajeria del Cliente.
     *
     * @param direcciones Los valores a asignar.
     */
    public abstract void setDirecciones(List direcciones);

    /**
     * Regresa la Lista de Direcciones de Mensajeria del Cliente.
     *
     * @return List Las Direcciones.
     */
    public abstract List getDirecciones();

    /**
	 * Regresa el valor de <code>calleYNo</code>.
     *
	 * @return String
	 */
    public abstract String getCalleYNo();

    /**
	 * Regresa el valor de <code>ciudad</code>.
     *
	 * @return String
	 */
    public abstract String getCiudad();

    /**
	 * Regresa el valor de <code>codigoPostal</code>.
     *
	 * @return String
	 */
    public abstract String getCodigoPostal();

    /**
	 * Regresa el valor de <code>colonia</code>.
     *
	 * @return String
	 */
    public abstract String getColonia();

    /**
	 * Regresa el valor de <code>delegacion</code>.
     *
	 * @return String
	 */
    public abstract String getDelegacion();

    /**
	 * Regresa el valor de <code>estado</code>.
     *
	 * @return String
	 */
    public abstract String getEstado();

    /**
     * Fija el valor de <code>calleYNo<code>.
     *
     * @param calleYNo El valor a asignar.
     */
    public abstract void setCalleYNo(String calleYNo);

    /**
     * Regresa el valor de numeroExterno.
     *
     * @return String.
     */
    public abstract String getNumeroExterno();

    /**
     * Establece el valor de numeroExterno.
     *
     * @param numeroExterno El valor a asignar.
     */
    public abstract void setNumeroExterno(String numeroExterno);

    /**
     * Regresa el valor de numeroInterno.
     *
     * @return String.
     */
    public abstract String getNumeroInterno();

    /**
     * Establece el valor de numeroInterno.
     *
     * @param numeroInterno El valor a asignar.
     */
    public abstract void setNumeroInterno(String numeroInterno);

    /**
     * Fija el valor de <code>ciudad<code>.
     *
     * @param ciudad El valor a asignar.
     */
    public abstract void setCiudad(String ciudad);

    /**
     * Fija el valor de <code>codigoPostal<code>.
     *
     * @param codigoPostal El valor a asignar.
     */
    public abstract void setCodigoPostal(String codigoPostal);

    /**
     * Fija el valor de <code>colonia<code>.
     *
     * @param colonia El valor a asignar.
     */
    public abstract void setColonia(String colonia);

    /**
     * Fija el valor de <code>delegacion<code>.
     *
     * @param delegacion El valor a asignar.
     */
    public abstract void setDelegacion(String delegacion);

    /**
     * Fija el valor de <code>estado<code>.
     *
     * @param estado El valor a asignar.
     */
    public abstract void setEstado(String estado);

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
	 * Obtiene la P&aacute;gina a la que se debe de regresar en caso de Cancelaci&oacute;n.
	 *
	 * @return String El nombre de la P&aacute;gina.
	 */
	public abstract String getPaginaAnterior();

	/**
	 * Establece la P&aacute;gina a la que se debe de regresar en caso de Cancelaci&oacute;n.
	 *
	 * @param paginaAnterior La P&aacute;gina.
	 */
	public abstract void setPaginaAnterior(String paginaAnterior);

    /**
     * Obtiene una referencia al EJB del servicio de Contratacion
     * <code>SicaConsultaDireccionesPersonaService</code>.
     *
     * @return SicaConsultaDireccionesPersonaService.
     */
    private SicaConsultaDireccionesPersonaService getSicaConsultaDireccionesPersonaService() {
        return (SicaConsultaDireccionesPersonaService) getApplicationContext().
                getBean("sicaConsultaDireccionesPersonaService");
    }

    /**
     * Obtiene una referencia al EJB del servicio de Contratacion
     * <code>SicaBusquedaPersonaService</code>.
     *
     * @return SicaBusquedaPersonaService.
     */
    private SicaBusquedaPersonaService getSicaBusquedaPersonaService() {
        return (SicaBusquedaPersonaService) getApplicationContext().
                getBean("sicaBusquedaPersonaService");
    }

    /**
     * Obtiene una referencia al EJB del servicio de Contratacion
     * <code>SicaRegistroDireccionPersonaService</code>.
     * @return SicaRegistroDireccionPersonaService.
     */
    private SicaRegistroDireccionPersonaService getSicaRegistroDireccionPersonaService() {
        return (SicaRegistroDireccionPersonaService) getApplicationContext().
                getBean("sicaRegistroDireccionPersonaService");
    }

    /**
     * Constante para indicar que la direcci&oacute;n es la del textfield de otra direcci&oacute;n.
     */
    public static final int ID_DIRECCION_OTRO = 0;
}