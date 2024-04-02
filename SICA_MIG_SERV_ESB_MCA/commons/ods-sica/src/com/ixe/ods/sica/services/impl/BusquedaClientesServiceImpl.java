/*
 * $Id: BusquedaClientesServiceImpl.java,v 1.4.30.1.14.1.26.1 2017/07/29 03:17:56 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.seguridad.model.IPerfil;
import com.ixe.ods.seguridad.sdo.SeguridadServiceData;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaValidationException;
import com.ixe.ods.sica.dao.ClienteDao;
import com.ixe.ods.sica.dao.JerarquiaDao;
import com.ixe.ods.sica.dto.ClienteContratoDto;
import com.ixe.ods.sica.dto.ClienteContratoInfoPromotorDto;
import com.ixe.ods.sica.services.BusquedaClientesService;

/**
 * Implementaci&oacute;n de la interfaz BusquedaClientesService. Realiza las validaciones necesarias
 * para las consultas de clientes y utiliza el bean ClienteDao para llevarlas a cabo.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.4.30.1.14.1.26.1 $ $Date: 2017/07/29 03:17:56 $
 */
public class BusquedaClientesServiceImpl implements BusquedaClientesService {

    /**
     * @see com.ixe.ods.sica.services.BusquedaClientesService#findClientes(String, String, String,
            String, String, String, boolean, Integer, int, boolean).
     */
    public List findClientes(String razonSocial, String paterno, String materno, String nombre,
                             String noCuenta, String modo, boolean cuentasSubordinados,
                             Integer idPromotor, int idUsuario, boolean consultarTodos)
            throws SicaValidationException {
        List contratosSica = new ArrayList();
        razonSocial = razonSocial.toUpperCase().trim();
        paterno = paterno.toUpperCase().trim();
        materno = materno.toUpperCase().trim();
        nombre = nombre.toUpperCase().trim();
        noCuenta = noCuenta.trim();
        // Validaciones de Campos:
        if ((razonSocial + nombre + paterno + materno).indexOf("%") >= 0) {
            throw new SicaValidationException("No se permite utilizar el caracter '%' en los "
                    + "criterios de consulta.");
        }
        else if (StringUtils.isEmpty(nombre) && StringUtils.isEmpty(paterno) &&
                StringUtils.isEmpty(materno) && StringUtils.isEmpty(razonSocial) &&
                StringUtils.isEmpty(noCuenta)) {
            throw new SicaValidationException("Debe definir al menos un criterio de "
                    + "b\u00fasqueda.");
        }
        else if ((StringUtils.isNotEmpty(razonSocial) && razonSocial.length() < 3) ||
                (StringUtils.isNotEmpty(nombre) && nombre.length() < 3) ||
                (StringUtils.isNotEmpty(paterno) && paterno.length() < 3) ||
                (StringUtils.isNotEmpty(materno) && materno.length() < 3) ||
                (StringUtils.isNotEmpty(noCuenta) && noCuenta.length() < 3)) {
            throw new SicaValidationException("Los criterios de b\u00fasqueda deben tener m\u00e1s "
                    + "de 2 caracteres.");
        }
        else if (StringUtils.isNotEmpty(razonSocial) && (StringUtils.isNotEmpty(paterno)
                || StringUtils.isNotEmpty(materno) || StringUtils.isNotEmpty(nombre)
                || StringUtils.isNotEmpty(noCuenta))) {
            throw new SicaValidationException("La b\u00fasqueda es por Personas, por Empresas, o "
                    + "por Contrato.");
        }
        else if ((StringUtils.isNotEmpty(paterno) || StringUtils.isNotEmpty(materno)
                || StringUtils.isNotEmpty(nombre)) && (StringUtils.isNotEmpty(noCuenta)
                || StringUtils.isNotEmpty(razonSocial))) {
            throw new SicaValidationException("La b\u00fasqueda es por Personas, por Empresas, o "
                    + "por Contrato.");
        }
        else if (StringUtils.isNotEmpty(noCuenta) && (StringUtils.isNotEmpty(paterno) ||
                StringUtils.isNotEmpty(materno) || StringUtils.isNotEmpty(nombre) ||
                StringUtils.isNotEmpty(razonSocial))) {
            throw new SicaValidationException("La b\u00fasqueda es por Personas, por Empresas, o "
                    + "por Contrato.");
        }
        if (FacultySystem.MODO_MESA_CONTROL.equals(modo)) {
        	contratosSica = clienteDao.findClientes(razonSocial, paterno, materno,
                    nombre, noCuenta, false);
        }
        else {
            if (!cuentasSubordinados) {
                contratosSica = clienteDao.findClientes(consultarTodos ? null : idPromotor,
                        razonSocial, paterno, materno, nombre, noCuenta, true);
            }
            else {
                IPerfil perfil = getSeguridadServiceData().findPerfilByUserAndSystem(idUsuario,
                        FacultySystem.SICA);
                Integer idPersonaUsuario = perfil.getUsuario().getPersona().getIdPersona();
                List contratosSicaTemp = clienteDao.findClientes(razonSocial, paterno,
                        materno, nombre, noCuenta, true);
                List promotoresJerarquia = getJerarquiaDao().
                        findPromotoresJerarquia(idPersonaUsuario);
                for (Iterator iterator = contratosSicaTemp.iterator(); iterator.hasNext();) {
                    ClienteContratoDto dto = (ClienteContratoDto) iterator.next();
                    for (Iterator it2 = promotoresJerarquia.iterator(); it2.hasNext();) {
                        Persona promotorJerarquia = (Persona) it2.next();
                        if (promotorJerarquia.getIdPersona().equals(dto.getIdEjec())) {
                            contratosSica.add(dto);
                        }
                    }
                }
            }
        }
        return contratosSica;
    }
    
    /**
     * @param contratoCorto
     * @return {@link ClienteContratoInfoPromotorDto} 
     * @see BusquedaClientesService#findClientesByContratoCorto(Integer)
     */
    public ClienteContratoInfoPromotorDto findClienteByContratoCorto(Integer contratoCorto) {
    	if(contratoCorto == null) return null;
    	return clienteDao.findClienteByContratoCorto(contratoCorto);
	}
    
    /**
     * @param contratoSica
     * @return {@link ClienteContratoInfoPromotorDto}
     * @see BusquedaClientesService#findContratoCortoByContratoSica(String)
     */
    public ClienteContratoInfoPromotorDto findContratoCortoByContratoSica(String contratoSica) {
    	if(contratoSica == null) return null;
    	return clienteDao.findContratoCortoByContratoSica(contratoSica);
    }
    
    /**
     * @see com.ixe.ods.sica.services.BusquedaClientesService#findClientesparaBloqueo(String).
     */
    public List findClientesparaBloqueo(String noCuenta)
            throws SicaValidationException {
        List contratosSica = new ArrayList();
        noCuenta = noCuenta.trim();
        // Validaciones de Campos:
        if (StringUtils.isEmpty(noCuenta)) {
            throw new SicaValidationException("Debe ingresar el numero de cuenta o contrato corto");
        }

        contratosSica = clienteDao.findClientesparaBloqueo(noCuenta);

        return contratosSica;
    }
    
    /**
     * @see com.ixe.ods.sica.services.BusquedaClientesService#findClientesparaBloqueo(String).
     */
    public List findClientesparaBloqueo(int idContrato)
            throws SicaValidationException {
        List contratosSica = new ArrayList();
        //idContrato = idContrato.trim();
        // Validaciones de Campos:
//        if (StringUtils.isEmpty(idContrato)) {
//            throw new SicaValidationException("Debe ingresar el numero de cuenta o contrato corto");
//        }

        contratosSica = clienteDao.findClientesparaBloqueo(idContrato);

        return contratosSica;
    }
    
    /**
     * Regresa el valor de clienteDao.
     *
     * @return ClienteDao.
     */
    public ClienteDao getClienteDao() {
        return clienteDao;
    }

    /**
     * Establece el valor de clienteDao.
     *
     * @param clienteDao El valor a asignar.
     */
    public void setClienteDao(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    /**
     * Regresa el valor de jerarquiaDao.
     *
     * @return JerarquiaDao
     */
    public JerarquiaDao getJerarquiaDao() {
        return jerarquiaDao;
    }

    /**
     * Establece el valor de jerarquiaDao.
     *
     * @param jerarquiaDao El valor a asignar.
     */
    public void setJerarquiaDao(JerarquiaDao jerarquiaDao) {
        this.jerarquiaDao = jerarquiaDao;
    }

    /**
     * Regresa el valor de seguridadServiceData.
     *
     * @return SeguridadServiceData.
     */
    public SeguridadServiceData getSeguridadServiceData() {
        return seguridadServiceData;
    }

    /**
     * Establece el valor de seguridadServiceData.
     *
     * @param seguridadServiceData El valor a asignar.
     */
    public void setSeguridadServiceData(SeguridadServiceData seguridadServiceData) {
        this.seguridadServiceData = seguridadServiceData;
    }

    /**
     * El bean que realiza la consulta de clientes en la base de datos.
     */
    private ClienteDao clienteDao;

    /**
     * El bean que realiza la consulta a los nodos de la jerarqu&iacute;a.
     */
    private JerarquiaDao jerarquiaDao;

    /**
     * El bean de seguridad con el que se verifican perfiles y la jerarqu&iacute;a. 
     */
    private SeguridadServiceData seguridadServiceData;

}
