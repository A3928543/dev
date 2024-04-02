/*
 * $Id: RegistroClienteContratoServiceImpl.java,v 1.1.4.2 2010/10/08 01:15:27 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import org.apache.commons.lang.StringUtils;

import com.ixe.ods.sica.services.RegistroClienteContratoService;
import com.ixe.ods.sica.SicaBusquedaPersonaService;
import com.ixe.ods.sica.SicaContratacionProductoBancoService;
import com.ixe.ods.sica.SicaRegistroPersonaService;
import com.ixe.bean.Usuario;
import com.ixe.bean.bup.Persona;
import com.ixe.bean.bup.PersonaFisica;
import com.ixe.bean.bup.PersonaMoral;
import com.ixe.contratacion.ContratacionException;

/**
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1.4.2 $ $Date: 2010/10/08 01:15:27 $
 */
public class RegistroClienteContratoServiceImpl implements RegistroClienteContratoService {

    public int registrarPersona(String nombre, String paterno, String materno, String razonSocial,
                                 String rfc, int idPersonaUsuario, int idSectorBanxico)
            throws ContratacionException {
        Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(idPersonaUsuario);
        boolean isPersonaMoral = !StringUtils.isEmpty(razonSocial);
        
        if (!isPersonaMoral) {
            PersonaFisica personaFisica = new PersonaFisica();

            personaFisica.setNombre(nombre);
            personaFisica.setPaterno(paterno);
            personaFisica.setMaterno(materno);
            personaFisica.setRfc(rfc);
            personaFisica.setTipoPersona("PF");
            personaFisica.setIdSector(idSectorBanxico);
            return getSicaRegistroPersonaService().registraPersona(personaFisica, usuario).
                    getIdPersona();
        }
        else {
            PersonaMoral personaMoral = new PersonaMoral();

            personaMoral.setRazonSocial(razonSocial);
            personaMoral.setRfc(rfc);
            personaMoral.setTipoPersona("PM");
            personaMoral.setIdSector(idSectorBanxico);
            return getSicaRegistroPersonaService().registraPersona(personaMoral, usuario).
                    getIdPersona();
        }
    }

    public String registrarContratoSica(int idPersonaCliente, int idPersonaUsuario)
            throws ContratacionException {
        Usuario usuario = getSicaBusquedaPersonaService().obtenEmpleado(idPersonaUsuario);
        Persona persona = getSicaBusquedaPersonaService().buscaPersonaPorId(idPersonaCliente);

        return getSicaContratacionProductoBancoService().registraCuentaCambios(persona, usuario);
    }

    /**
     * Regresa el valor de sicaBusquedaPersonaService.
     *
     * @return SicaBusquedaPersonaService.
     */
    public SicaBusquedaPersonaService getSicaBusquedaPersonaService() {
        return sicaBusquedaPersonaService;
    }

    /**
     * Establece el valor de sicaBusquedaPersonaService.
     *
     * @param sicaBusquedaPersonaService El valor a asignar.
     */
    public void setSicaBusquedaPersonaService(SicaBusquedaPersonaService sicaBusquedaPersonaService) {
        this.sicaBusquedaPersonaService = sicaBusquedaPersonaService;
    }

    /**
     * Regresa el valor de sicaContratacionProductoBancoService.
     *
     * @return SicaContratacionProductoBancoService.
     */
    public SicaContratacionProductoBancoService getSicaContratacionProductoBancoService() {
        return sicaContratacionProductoBancoService;
    }

    /**
     * Establece el valor de sicaContratacionProductoBancoService .
     *
     * @param sicaContratacionProductoBancoService El valor a asignar.
     */
    public void setSicaContratacionProductoBancoService(
            SicaContratacionProductoBancoService sicaContratacionProductoBancoService) {
        this.sicaContratacionProductoBancoService = sicaContratacionProductoBancoService;
    }

    /**
     * Regresa el valor de sicaRegistroPersonaService.
     *
     * @return SicaRegistroPersonaService.
     */
    public SicaRegistroPersonaService getSicaRegistroPersonaService() {
        return sicaRegistroPersonaService;
    }

    /**
     * Establece el valor de sicaRegistroPersonaService.
     *
     * @param sicaRegistroPersonaService El valor a asignar.
     */
    public void setSicaRegistroPersonaService(SicaRegistroPersonaService sicaRegistroPersonaService) {
        this.sicaRegistroPersonaService = sicaRegistroPersonaService;
    }

    private SicaBusquedaPersonaService sicaBusquedaPersonaService;

    private SicaContratacionProductoBancoService sicaContratacionProductoBancoService;

    private SicaRegistroPersonaService sicaRegistroPersonaService;
}
