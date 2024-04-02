package com.ixe.ods.sica.pages;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.springframework.dao.DataAccessException;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.CuentaEjecutivo;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.Status;
import com.ixe.ods.bup.model.UltimaModificacion;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.Contrato;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.deals.plantillas.ConsultaPlantillasCuentaIxe;
import com.ixe.ods.sica.pages.deals.plantillas.ConsultaPlantillasIntl;
import com.ixe.ods.sica.pages.deals.plantillas.ConsultaPlantillasNacionales;
import com.ixe.ods.sica.pages.deals.plantillas.ConsultaPlantillasTranNacionales;
import com.ixe.ods.sica.pages.mesa.AsignacionCtasCliente;
import com.ixe.ods.sica.pizarron.Consts;
import com.ixe.ods.sica.services.BusquedaClientesService;
import com.ixe.contratacion.ContratacionException;
import com.ixe.ods.sica.model.TipoBloqueo;
import com.legosoft.tapestry.model.RecordSelectionModel;

public abstract class BloqueaContrato extends SicaPage {

	/**
	 * Activa el Modo de Operaci&oacute; as&iacute; como el T&iacute;tulo del
	 * Portlet de B&uacute;squedas de la P&aacute;gina cada que esta se activa.
	 * 
	 * @param cycle
	 *            El ciclo de la p&aacute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
		limpiarTodo();
		limpiar();
		setTituloActionPB("Bloqueo de Contrato");
		setNoCuenta("");

		List motivosBloqueo = getSicaServiceData().findAllMotivosBloqueo();
		Collections.sort(motivosBloqueo, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((TipoBloqueo) o1).getDescripcion().compareTo(
						((TipoBloqueo) o2).getDescripcion());
			}
		});

		for (Iterator iter = motivosBloqueo.iterator(); iter.hasNext();) {
			TipoBloqueo element = (TipoBloqueo) iter.next();
			super.debug("Valor de la lista:  " + element.getIdBloqueo() + " Descripcion: " + element.getDescripcion());

		}

		setMotivosBloqueoList(motivosBloqueo);
		super.debug("Valor setMotivosBloqueoList:  " + motivosBloqueo.size());
	}

	/**
	 * Vac&iacute;a la lista de clientes.
	 */
	public void limpiar() {
		setClientes(new ArrayList());
	}

	/**
	 * Regresa el mapa para asignar el focus al campo de texto
	 * empresasTextField.
	 * 
	 * @return Map.
	 */
	public Map getFirstTextFieldMap() {
		Map map = new HashMap();
		map.put("textField", "document.Form0.contratoTextField");
		return map;
	}

	/**
	 * Realiza las operaciones de b&uacute;squeda por Clientes, o por Empresas,
	 * o por Contratos Sica para los Canales de Sucursales y Mayoreo. Para este
	 * &uacute;ltimo Canal, hay dos casos, cuando el ejecutivo que opera el SICA
	 * quiere consultar sus Cuentas Propias Asignadas y el caso para cuando
	 * requiere consultar adem&aacute;s los Contratos Sica de sus Subordinados.
	 * 
	 * @param cycle
	 *            El ciclo de la p&aacute;gina.
	 */
	public void fetch(IRequestCycle cycle) {
		List contratosSica;
		Visit visit = (Visit) getVisit();
		quitarEspaciosEnBlanco();
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");// se agrego quitarla

		try {
			BusquedaClientesService busquedaService = (BusquedaClientesService) getApplicationContext().getBean("busquedaClientesService");
			contratosSica = busquedaService.findClientesparaBloqueo(getNoCuenta());
			if (contratosSica.isEmpty()) {
				try {
					Integer n = NumberUtils.createInteger(getNoCuenta());
					contratosSica = busquedaService.findClientesparaBloqueo(n.intValue());
				} catch (NumberFormatException e) {
					throw new SicaException("No se encontr\u00f3 el Contrato SICA");
				}

				if (contratosSica.isEmpty())
					throw new SicaException("No se encontr\u00f3 el Contrato SICA");
				// aqui empieza
				// validar que el contrato corto sea valido para poder aplicar un
				// bloqueo
				//ContratoSica cuenta = getSicaServiceData().findContrato(getNoCuenta());
				//Date fecha = (Date) cuenta.getFechaApertura();
				super.debug("******* Entra a realizar la busqueda por contrato corto ************");
				contratosSica = busquedaService.findClientesparaBloqueo(new Integer(getNoCuenta()).intValue());
				Contrato c = (Contrato) getSicaServiceData().findContratoByCorto(
						new Integer(Integer.parseInt(getNoCuenta())));

				super.debug("******* Valor que pasamos del contrato corto ************"	+ c.getIdContrato());
				super.debug("******* Valor que pasamos del contrato sica ************"	+ c.getNoCuenta());
				ContratoSica cuenta2 = (ContratoSica) getSicaServiceData().findContrato(c.getNoCuenta());
				Date fecha = (Date) cuenta2.getFechaApertura();

				super.debug("******************* VALOR DE FECHA_VALIDA_DOCUMENTACION --> :  " + getSicaServiceData().getFechaValidaDocumentacion());
				super.debug("******************* FECHA APERTURA DEL CONTRATO--> :  " + " >> " + fecha);
				
				if (contratosSica.isEmpty() || fecha.before(getSicaServiceData().getFechaValidaDocumentacion())) {
					setLevel(-1);
					super.debug("La fecha apertura es anterior a la del parametro o no tiene un contrato corto definido");
					delegate.record("Este contrato no cumple con los criterios de Bloqueo/Desbloqueo.",null);
				}
				setClientes(contratosSica);				
			}
			else{
			setClientes(contratosSica);

			// aqui empieza
			// validar que el contrato corto sea valido para poder aplicar un
			// bloqueo
			//ContratoSica cuenta = getSicaServiceData().findContrato(getNoCuenta());
			//Date fecha = (Date) cuenta.getFechaApertura();
			ContratoSica cuenta = getSicaServiceData().findContrato(getNoCuenta());
			Date fecha = (Date) cuenta.getFechaApertura();
			super.debug("******************* VALOR DE FECHA_VALIDA_DOCUMENTACION --> :  " + getSicaServiceData().getFechaValidaDocumentacion());
			super.debug("******************* FECHA APERTURA DEL CONTRATO--> :  " + " >> " + fecha);
			super.debug("******************* VALOR DEL CONTRATO CORTO--> :  " + " >> " + getSicaServiceData().findContratosByCuenta(getNoCuenta()));

			List contratocorto = getSicaServiceData().findContratosByCuenta(getNoCuenta());

			if (fecha.before(getSicaServiceData().getFechaValidaDocumentacion()) || contratocorto.size() == 0) {
				setLevel(-1);
				super.debug("La fecha apertura es anterior a la del parametro o no tiene un contrato corto definido");
				delegate.record("Este contrato no cumple con los criterios de Bloqueo/Desbloqueo.",null);
			}// aqui termina

		}
		}

		catch (SicaException e) {
			debug(e);
			limpiar();
			getDelegate().record(e.getMessage(), null);
		}
	}

	/**
	 * Quita los espacios en blanco en los Criterios de B&uacute;squeda para
	 * evitar fallas en las busquedas en la Base de Datos.
	 */
	public void quitarEspaciosEnBlanco() {
		if (StringUtils.isNotEmpty(getNoCuenta())) {
			setNoCuenta(getNoCuenta().trim());
		}
	}

	public void bloquear(IRequestCycle cycle) {
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans()
				.getBean("delegate");
		Visit visit = (Visit) getVisit();
		List tipoBloqueo = new ArrayList();

		tipoBloqueo = getSicaServiceData().findBloqueoByidBloqueo(
				new Integer(getTipoBloqueo().getIdBloqueo()));
		// validar que el contrato corto sea valido para poder aplicar un
		// bloqueo
		ContratoSica cuenta = getSicaServiceData().findContrato(getNoCuenta());
		// Si no encuentra el contrato por numero de cuenta entra a buscar por
		// contrato corto
		if (cuenta == null) {
			super.debug("******* Entra a realizar la busqueda por contrato corto ************");

			List contratosSica;
			BusquedaClientesService busquedaService = (BusquedaClientesService) getApplicationContext().getBean("busquedaClientesService");
			contratosSica = busquedaService.findClientesparaBloqueo(new Integer(getNoCuenta()).intValue());
			Contrato c = (Contrato) getSicaServiceData().findContratoByCorto(
					new Integer(Integer.parseInt(getNoCuenta())));

			super.debug("******* Valor que pasamos del contrato corto ************"	+ c.getIdContrato());
			super.debug("******* Valor que pasamos del contrato sica ************"	+ c.getNoCuenta());
			ContratoSica cuenta2 = (ContratoSica) getSicaServiceData().findContrato(c.getNoCuenta());
			Date fecha = (Date) cuenta2.getFechaApertura();

			super.debug("******************* VALOR DE FECHA_VALIDA_DOCUMENTACION --> :  " + getSicaServiceData().getFechaValidaDocumentacion());
			super.debug("******************* FECHA APERTURA DEL CONTRATO--> :  " + " >> " + fecha);

			if (!tipoBloqueo.isEmpty()
					&& tipoBloqueo != null
					&& fecha.after(getSicaServiceData().getFechaValidaDocumentacion()) && c != null) {
				TipoBloqueo tb = (TipoBloqueo) getTipoBloqueo();
				super.debug("Valor del status bloqueo nuevo:  "	+ tb.getIdBloqueo() + " Descripcion: "+ tb.getDescripcion() + " Cuenta: " + getNoCuenta());
				super.debug("Entrando a Bloquear Contrato");
				getSicaServiceData().cambiarEstadoContrato(getNoCuenta(),tb.getIdBloqueo(), visit.getUser().getIdUsuario());
				setLevel(1);
				delegate.record("El contrato ha sido bloqueado con exito!.",null);
				setNoCuenta(""); // limpia el campo de Busqueda de contrato
			} else {
				setLevel(-1);
				super.debug("No se pudo concretar el Bloqueo por que la fecha apertura es anterior a la del parametro o no tiene un contrato corto definido");
				delegate.record("Este contrato no cumple con los criterios de Bloqueo/Desbloqueo.",null);
			}
		} else {
			super.debug("******* Entra a realizar la busqueda por contrato SICA ************");
			Date fecha = (Date) cuenta.getFechaApertura();
			super.debug("******************* VALOR DE FECHA_VALIDA_DOCUMENTACION --> :  " + getSicaServiceData()
									.getFechaValidaDocumentacion());
			super.debug("******************* FECHA APERTURA DEL CONTRATO--> :  " + " >> " + fecha);
			super.debug("******************* VALOR DEL CONTRATO CORTO--> :  " + " >> " + getSicaServiceData().findContratosByCuenta(getNoCuenta()));

			List contratocorto = getSicaServiceData().findContratosByCuenta(getNoCuenta());

			if (!tipoBloqueo.isEmpty()
					&& tipoBloqueo != null
					&& fecha.after(getSicaServiceData().getFechaValidaDocumentacion())
					&& contratocorto.size() != 0) {
				TipoBloqueo tb = (TipoBloqueo) getTipoBloqueo();
				super.debug("Valor del status bloqueo nuevo:  "	+ tb.getIdBloqueo() + " Descripcion: " + tb.getDescripcion() + " Cuenta: " + getNoCuenta());
				super.debug("Entrando a Bloquear Contrato");
				getSicaServiceData().cambiarEstadoContrato(getNoCuenta(),tb.getIdBloqueo(), visit.getUser().getIdUsuario());
				setLevel(1);
				delegate.record("El contrato ha sido bloqueado con exito!.",null);
				setNoCuenta(""); // limpia el campo de Busqueda de contrato
			} else {
				setLevel(-1);
				super.debug("No se pudo concretar el Bloqueo por que la fecha apertura es anterior a la del parametro o no tiene un contrato corto definido");
				delegate.record(
								"Este contrato no cumple con los criterios de Bloqueo/Desbloqueo.",null);
			}
		}
	}

	public void desbloquear(IRequestCycle cycle) {

		IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans()
				.getBean("delegate");
		Visit visit = (Visit) getVisit();
		getSicaServiceData().cambiarEstadoContrato(getNoCuenta(),
				TipoBloqueo.SIN_BLOQUEO, visit.getUser().getIdUsuario());
		setLevel(1);
		delegate.record("El contrato ha sido desbloqueado con exito!.", null);
		setNoCuenta(""); // limpia el campo de Busqueda de contrato
	}

	/**
	 * Regresa el Nombre de la Facultad que tiene el Canal al que pertenece el
	 * Usuario que visita la p&aacute;gina.
	 * 
	 * @return String El Nombre de la Facultad.
	 */
	public String getNombreFacultad() {
		try {
			String nombreFacultad = (getSicaServiceData()
					.findCanal(((Visit) getVisit()).getIdCanal()))
					.getFacultad().getNombre();
			return nombreFacultad != null ? nombreFacultad : "";
		} catch (SicaException e) {
			warn(e);
			return null;
		}
	}

	/**
	 * Regresa un RecordSelectionModel con todos los motivos de bloqueo.
	 * 
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getMotivosBloqueoModel() {
		return new RecordSelectionModel(getMotivosBloqueoList(), "idBloqueo",
				"descripcion");
	}

	/**
	 * Obtiene la lista de descripciones de bloqueos.
	 * 
	 * @return List
	 */
	public abstract List getMotivosBloqueoList();

	/**
	 * Asigna el valor para la lista de descripciones de bloqueos.
	 * 
	 * @param motivosBloqueoList
	 *            El valor para la lista de descripciones de bloqueos.
	 */
	public abstract void setMotivosBloqueoList(List motivosBloqueoList);

	/**
	 * Establece el T&iacute;tulo del Portlet de B&uacute;squedas de la
	 * P&aacute;gina de acuerdo al modo de operaci&oacute;n de la misma.
	 * 
	 * @param tituloActionPB
	 *            El T&iacute;tulo del Portlet de B&uacute;squedas de la
	 *            P&aacute;gina.
	 */
	public abstract void setTituloActionPB(String tituloActionPB);

	/**
	 * Activa la lista de Clientes encontrados de acuerdo a los criterios de
	 * B&uacute;squeda preestablecidos.
	 * 
	 * @param clientes
	 *            La lista de Clientes encontrados.
	 */
	public abstract void setClientes(List clientes);

	// public abstract void getClientes();

	/**
	 * Establece el valor de noCuenta.
	 * 
	 * @param noCuenta
	 *            El valor a asignar.
	 */
	public abstract void setNoCuenta(String noCuenta);

	/**
	 * Obtiene lo establecido como criterio de b&uacute;squeda No. de Cuenta
	 * (Contrato).
	 * 
	 * @return String NoCuenta.
	 */
	public abstract String getNoCuenta();

	/**
	 * Asigna el valor para la lista de motivos de bloqueo.
	 * 
	 * @param tipoBloqueo
	 *            El valor para la lista de promotores.
	 */
	public abstract void setTipoBloqueo(TipoBloqueo tipoBloqueo);

	/**
	 * Obtiene las descripciones para la lista de motivos de bloqueo.
	 * 
	 * @return tipoBloqueo Las descripciones de bloqueos
	 */
	public abstract TipoBloqueo getTipoBloqueo();

	/**
	 * Obtiene lo establecido como criterio de b&uacute;squeda No. de Cuenta
	 * (Contrato Corto).
	 * 
	 * @return int idContrato.
	 */
	public abstract int getidContrato();

	/*
	 * 
	 * 
	 * 
	 * public abstract setnombreCliente(String ) public abstract setidBloqueo()
	 * public abstract setfechaUltMod() public abstract setusuarioUltMod()
	 */

	/**
	 * Obtiene el nivel del delegate.
	 * 
	 * @return int
	 */
	public abstract int getLevel();

	/**
	 * Nivel de los mensajes al usuario: -1 Error, 0 Warning, 1 Exito.
	 * 
	 * @param level
	 *            El valor a asignar.
	 */
	public abstract void setLevel(int level);

}
