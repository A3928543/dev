/*
 *  $Id: PerfilUsuarioSicaDao.java,v 1.11 2008/02/22 18:25:31 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.dao;

import com.ixe.ods.seguridad.model.IPerfil;
import com.ixe.ods.seguridad.sdo.SeguridadServiceData;
import com.legosoft.hibernate.HibernateTemplate;
import net.sf.hibernate.Session;

import java.util.Set;

/**
 * Dao de Servicios de acuerdo al Perfil del Usuario del SICA.
 *
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:31 $
 */
public class PerfilUsuarioSicaDao extends HibernateTemplate {

    /**
     * Constructor por Default.
     *
     * @param session La Sesi&oacute;n del sistema
     */
	public PerfilUsuarioSicaDao(Session session) {
        super(session);
    }

    /**
     * Obtiene la Lista Negra de Cuentas del Promotor Logeado en el Sistema.
     *
     * @param seguridadServiceData La referencia al SDO de Servicios del SICA.
     * @param idUsuario EL ID del Promotor Logeado en el Sistema.
     * @param applicationName El nombre del Sistema (SICA).
     * @return Set La lista negra de Cuentas del Promotor.
     */
	public static Set findListaNegraCuentasPorUsuarioPorSistema(SeguridadServiceData seguridadServiceData, int idUsuario, String applicationName) {
    	IPerfil perfil = seguridadServiceData.findPerfilByUserAndSystem(idUsuario, applicationName);
    	return perfil.getListaNegraCuentas();
    }

}