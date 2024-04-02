/*
 * $Id: AltaCorreosElectronicos.java,v 1.2 2009/08/03 21:53:54 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.tapestry.IRequestCycle;

import com.ixe.bean.bup.MedioContacto;
import com.ixe.contratacion.ContratacionException;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.SicaConsultaMediosContactoPersonaService;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.AutMedioContacto;

/**
 * P&aacute;gina que permite al usuario registrar un nuevo correo para facturaci&oacute;n
 * electr&oacute;nica, el cual deber&aacute; ser autorizado por la mesa de control.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.2 $ $Date: 2009/08/03 21:53:54 $
 */
public abstract class AltaCorreosElectronicos extends SicaPage {

    /**
     * Inicializa la persona, sus medios de contacto y las autorizaciones de alta de correos, de
     * acuerdo al valor del contrato sica especificado.
     *
     * @param noCuenta El n&uacute;mero de contrato sica.
     * @throws ContratacionException Si no se pueden cargar los medios de contacto.
     */
    public void inicializar(String noCuenta)
            throws ContratacionException {
        setPersona(getSicaServiceData().findPersonaByContratoSica(noCuenta));
        List mediosContacto = getConsultaMediosContactoService().obtenMediosContacto(
                getPersona().getIdPersona().intValue());
        Collections.sort(mediosContacto, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((MedioContacto) o1).getValor().compareTo(((MedioContacto) o2).getValor());
            }
        }
        );
        setMediosContacto(mediosContacto);
        inicializarAutMediosContacto();
    }

    /**
     * Carga las autorizaciones de alta de medios de contacto y los asigna a la propiedad
     * <code>mediosContactoAuts</code>.
     */
    private void inicializarAutMediosContacto() {
        setMediosContactoAuts(getSicaServiceData().findAutMediosContactoPersona(
                getPersona().getIdPersona().intValue()));
    }

    /**
     * Crea el registro del nuevo correo en la tabla SC_AUT_MEDIO_CONTACTO. 
     *
     * @param cycle El ciclo de la p&aacute;ina.
     */
    public void agregar(IRequestCycle cycle) {
        if (getDelegate().getHasErrors()) {
            return;
        }
            Visit visit = (Visit) getVisit();
            AutMedioContacto amc = getNuevoAutMedioContacto();
            amc.setPersona(getPersona());
            amc.setPromotor((EmpleadoIxe) getSicaServiceData().find(
                    com.ixe.ods.bup.model.EmpleadoIxe.class, visit.getUser().getIdPersona()));
            getSicaServiceData().store(amc);
            inicializarAutMediosContacto();
            setNuevoAutMedioContacto(new AutMedioContacto());
    }

    /**
     * Regresa la expresi&oacute;n regular para validar el nuevo correo electr&oacute;nico.
     *
     * @return String.
     */
    public String getPatronEmail() {
        return "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|" +
                "(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    }

    /**
     * Regresa true si el medio de contacto est&aacute; verificado para Facturaci&oacute;n
     * Electr&oacute;nica.
     *
     * @param mc El medio de contacto a verificar.
     * @return boolean.
     */
    public boolean isMedioContactoVerificadoFE(MedioContacto mc) {
        return "S".equals(mc.getMedconFE());
    }

    /**
     * Regresa la referencia al bean sicaConsultaMediosContactoPersonaService del application
     * context de spring.
     *
     * @return SicaConsultaMediosContactoPersonaService.
     * @see com.ixe.ods.sica.SicaConsultaMediosContactoPersonaService
     */
    private SicaConsultaMediosContactoPersonaService getConsultaMediosContactoService() {
        return (SicaConsultaMediosContactoPersonaService) getApplicationContext().
        getBean("sicaConsultaMediosContactoPersonaService");
    }

    /**
     * Regresa el valor de nuevoAutMedioContacto.
     *
     * @return AutMedioContacto.
     */
    public abstract AutMedioContacto getNuevoAutMedioContacto();

    /**
     * Establece el valor de nuevoAutMedioContacto.
     *
     * @param autmediocontacto El valor a asignar.
     */
    public abstract void setNuevoAutMedioContacto(AutMedioContacto autmediocontacto);

    /**
     * Regresa el valor de persona.
     *
     * @return Persona.
     */
    public abstract Persona getPersona();

    /**
     * Establece el valor de persona.
     *
     * @param persona El valor a asignar.
     */
    public abstract void setPersona(Persona persona);

    /**
     * Establece el valor de mediosContacto.
     *
     * @param list El valor a asignar.
     */
    public abstract void setMediosContacto(List list);

    /**
     * Establece el valor de mediosContactoAuts.
     *
     * @param list El valor a asignar.
     */
    public abstract void setMediosContactoAuts(List list);
}
