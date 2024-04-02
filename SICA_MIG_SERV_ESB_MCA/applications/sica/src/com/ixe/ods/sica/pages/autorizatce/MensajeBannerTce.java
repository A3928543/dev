package com.ixe.ods.sica.pages.autorizatce;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.springframework.dao.DataAccessException;

import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.MensajeTce;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * Pantalla de captura de mensajes para su publicacion en la interfaz de captura de tc especial 
 * 
 * @author Cesar Jeronimo Gomez
 */
public abstract class MensajeBannerTce extends SicaPage implements IExternalPage {
	//Defino longitud máxima del mensaje.
	private final int longMax = 1000;
	/**
	 * Se llama al cargar la pagina
	 * 
	 * @param params
	 * @param cycle
	 */
	public void activateExternalPage(Object[] params, IRequestCycle cycle) {
		try {
			if(isPromocion()) {
				throw new SicaException("Un usuario de promoci\u00F3n no tiene acceso a la publicaci\u00F3n de mensajes");
			}
			initFields();
		} catch (SicaException e) {
			AutorizaTceHelper.redirectToMessageWithoutMenu(cycle, e.getMessage());
		}
	}
	
	/**
	 * Determina si el usuario actual es de promocion
	 * 
	 * @return
	 */
	protected boolean isPromocion() {
		return AutorizaTceHelper.isPromocion((Visit)getVisit());
	}
	
	/**
	 * Se llama al guardar un mensaje
	 * 
	 * @param cycle
	 */
	public void submit(IRequestCycle cycle) {
		try {
			saveMensaje();
		} catch (SicaException e) {
			getDelegate().record(e.getMessage(), null);
		}
	}
	
	/**
	 * Se llama al cancelar la captura de un mensaje
	 * 
	 * @param cycle
	 */
	public void cancelar(IRequestCycle cycle) {
		try {
			initFields();
		} catch (SicaException e) {
			getDelegate().record(e.getMessage(), null);
		}
	}
	
	/**
	 * Listener para refrescar los mensajes del banner
	 * 
	 * @param cycle
	 */
	public void refreshBanner(IRequestCycle cycle) {
		try {
			debug("Refrescando mensajes en banner");
			setListaMensajes(getSicaServiceData().findMensajesTceHoy());
		} catch(SicaException e) {
			getDelegate().record(e.getMessage(), null);
		}
	}
	
	/**
	 * Inicializa los valores de los campos
	 */
	private void initFields() {
		setMensaje(new MensajeTce());
		setListaMensajes(getSicaServiceData().findMensajesTceHoy());
	}
	
	/**
	 * Guarda el mensaje solo si es valido
	 * 
	 * @return
	 */
	private void saveMensaje() {
		if(sonCamposValidos(getMensaje())) {
			EmpleadoIxe user = getCurrentEmpleadoIxe();

			// Seteamos los demas datos del objeto mensaje
			getMensaje().setUsuarioCreacion(user);
			getMensaje().setUsuarioUltModif(getMensaje().getUsuarioCreacion());
			getMensaje().setFechaCreacion(new Date());
			getMensaje().setFechaUltModif(getMensaje().getFechaCreacion());
			
			getSicaServiceData().saveMensajeTce(getMensaje());
			initFields();
		}
	}
	
	/**
	 * Obtiene el empleado ixe en sesion
	 * 
	 * @return
	 */
	private EmpleadoIxe getCurrentEmpleadoIxe() {
		try {
			IUsuario usuarioActual = AutorizaTceHelper.getUsuarioActualConPersona((Visit) getVisit());
			if(StringUtils.isEmpty(usuarioActual.getClave())) {
				throw new SicaException("El usuario actual no tiene clave");
			}
			EmpleadoIxe user = getSicaServiceData().findPromotorSicaByClave(usuarioActual.getClave());
			if(user == null) {
				throw new SicaException("Clave de usuario no encontrada");
			}
			return user;
		} catch (DataAccessException e) {
			error("Error al consultar usuario", e);
			throw new SicaException("Error al consultar usuario actual");
		}
	}
	
	/**
	 * Valida los campos del mensaje
	 * 
	 * @param mensaje
	 * @return
	 */
	private boolean sonCamposValidos(MensajeTce mensaje) {
		if(StringUtils.isEmpty(mensaje.getMensaje())) {
			getDelegate().record("El mensaje es requerido", null);
			return false;
		}
		if(StringUtils.isEmpty(mensaje.getPrioridad())) {
			getDelegate().record("La prioridad es requerida", null);
			return false;
		}
		//Valido que la longitud del mensaje sea menor a 1000 caracteres
		if(mensaje.getMensaje().length() > longMax) {
			getDelegate().record("Longitud del mensaje excedida.\nMáximo " + longMax + " caracteres\n Mensaje actual: " + mensaje.getMensaje().length(), null);
			return false;
		}
		
		return true;
	}
	
	/** Obtiene el mensaje que se esta capturando */
	public abstract MensajeTce getMensaje();
	
	/** Establece el mensaje que se esta capturando */
	public abstract void setMensaje(MensajeTce mensaje);
	
	/** Obtiene la lista de mensajes que se presenta en pantalla */
	public abstract List getListaMensajes();
	
	/** Establece la lista de mensajes que se presentan */
	public abstract void setListaMensajes(List listaMensajes);
	
}
