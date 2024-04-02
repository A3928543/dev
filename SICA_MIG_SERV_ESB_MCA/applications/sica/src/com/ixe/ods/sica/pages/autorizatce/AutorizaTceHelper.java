package com.ixe.ods.sica.pages.autorizatce;

import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.pages.MensajeSinMenu;

/**
 * Funcionalidad comun para Tipo de Cambio especial
 * 
 * @author Cesar Jeronimo Gomez
 */
public class AutorizaTceHelper {

	/**
     * Evalua si el usuario es de la mesa de cambios
     * @return
     */
    public static boolean isMesaCambios(Visit visit) {
		if ( visit.hasPermission(FacultySystem.SICA_MESACAMBIOS) ) {
			return true;
		} else {
			return false;
		}
    }
    
    /**
     * Evalua si el usuario es un promotor
     * @return
     */
    public static boolean isPromocion(Visit visit) {
    	if(isMesaCambios(visit)) return false;
		if ( visit.hasPermission(FacultySystem.SICA_PROMOCION) ) {
			return true;
		} else {
			return false;
		}
    }
    
    /**
     * Obtiene el usuario actual
     * 
     * @return
     */
    public static IUsuario getUsuarioActualConPersona(Visit visit) {
		if(visit != null) {
			IUsuario user = visit.getUser();
			if (user != null) {
				Persona persona = user.getPersona();
				if(persona != null) {
	    			return user;
				}
			}
		}
		return null;
    }
    
    /**
	 * Redirecciona a una pagina de mensaje de error en caso de haber alguno
	 * 
	 * @param cycle
	 * @param message
	 */
	public static void redirectToMessageWithoutMenu(IRequestCycle cycle, String message) {
		MensajeSinMenu nextPage = (MensajeSinMenu) cycle.getPage("MensajeSinMenu");
        nextPage.setMensaje(message);
        cycle.activate(nextPage);
        return;
	}
	
}
