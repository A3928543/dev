/*
 * $Id: AsignaSubsidiarios.java,v 1.9 2009/08/03 21:59:29 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.bup.model.PersonaMoral;
import com.ixe.ods.sica.dao.ClienteDao;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.dto.ClienteContratoDto;

/**
 * Clase para la asignaci&oacute;n de Subsidiarios a Corporativos.
 *
 * @author Edgar Leija, Javier Cordova
 * @version $Revision: 1.9 $ $Date: 2009/08/03 21:59:29 $
 */
public abstract class AsignaSubsidiarios extends SicaPage {
	
	/**
     * Activa la p&aacute;gina y limpia variables persistentes.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
    	super.activate(cycle);
    	setOperacionExitosa(false);
    	limpiar();
    }

    /**
     * Convierte lo escrito en los Criterios de B&uacute;squeda a Uppercase.
     */
    public void convertirAUpperCase() {
        if (StringUtils.isNotEmpty(getRazonSocial())) {
            setRazonSocial(getRazonSocial().toUpperCase());
        }
    }
    
    /**
     * Realiza las operaciones de b&uacute;squeda por Clientes (Empresas) o por No. Cuenta Contrato
     * SICA.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void fetch(IRequestCycle cycle) {
    	if (getModoRefresh() == 1) {
    		limpiar();
    		return;
    	}
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        setOperacionExitosa(false);
        List contratosSica = new ArrayList();
        convertirAUpperCase();
        // Validaciones de Campos:
        if ((getRazonSocial() + getNoCuenta()).indexOf("%") >= 0) {
        	limpiar();
            delegate.record("No se permite utilizar el caracter '%' en los criterios de consulta.",
                    null);
            return;
        }
        else if (StringUtils.isEmpty(getRazonSocial().trim()) &&
                StringUtils.isEmpty(getNoCuenta().trim())) {
        	limpiar();
            delegate.record("Debe definir al menos un criterio de b\u00fasqueda.", null);
            return;
        }
        else if ((StringUtils.isNotEmpty(getRazonSocial()) && getRazonSocial().length() < 3)
                || (StringUtils.isNotEmpty(getNoCuenta()) && getNoCuenta().length() < 3)) {
        	limpiar();
            delegate.record("Los criterios de b\u00fasqueda deben tener m\u00e1s de 2 caracteres.",
                    null);
            return;
        }
        else if (StringUtils.isNotEmpty(getRazonSocial().trim()) &&
                StringUtils.isNotEmpty(getNoCuenta().trim())) {
        	limpiar();
            delegate.record("La b\u00fasqueda es por Empresas o por Contrato.", null);
            return;
        }
        ClienteDao clienteDao = (ClienteDao) getApplicationContext().getBean("clienteDao");
        contratosSica = clienteDao.findClientes(getRazonSocial(), getNoCuenta());
        List tmp = new ArrayList();
        for (Iterator it = contratosSica.iterator(); it.hasNext();) {
            ClienteContratoDto dto = (ClienteContratoDto) it.next();
            Object[] o = new Object[]{
                    dto.getNoCuenta(), dto.getNombreCorto(), dto.getTipoPersona(), dto.getEsGrupo(),
                    dto.getIdSector(), dto.getIdPersona(),
                    dto.getEsGrupo() != null && dto.getEsGrupo().booleanValue() ? "S" : null,
                    dto.getIdGrupoEmpresarial(), dto.getIdGpoEmpresarialContrato()
            };
            tmp.add(o);
        }
        contratosSica = tmp;
        if (contratosSica.isEmpty()) {
            setSubsidiarios(new ArrayList());
            delegate.record("No se encontr\u00f3 el Cliente o el Cliente no tiene Contrato SICA.",
                    null);
            return;
        }
        if (getClientes().size() < 1) {
            setClientes(contratosSica);
            setOperacionExitosa(true);
            delegate.record("Seleccione uno de los Corporativos encontrados o d\u00e9 Click en " +
                    "'CANCELAR TODO' para volver a buscar otro Corporativo.", null);
        }
        else {
            setSubsidiarios(contratosSica);
        }
    }

    /**
     * Encuentra los Subsidiarios de un Corporativo.
     *
     * @param corporativo El Corporativo.
     */
    private void findBeneficiariosAsignados(PersonaMoral corporativo) {
    	List tmp = getSicaServiceData().findSubsidiariosByPersonaMoral(corporativo.getIdPersona());
    	List tmpCuentas = new ArrayList();
    	for (Iterator iterator = tmp.iterator(); iterator.hasNext();) {
    		Persona personaTmp = (Persona) iterator.next();
            ContratoSica cs = getSicaServiceData().
                    findContratoSicaByIdPersona(personaTmp.getIdPersona());
            Object[] obj = new Object []{cs.getNoCuenta(), personaTmp.getNombreCorto()};
    		tmpCuentas.add(obj);
    	}
    	if (!tmpCuentas.isEmpty()) {
    		setAsignados(tmpCuentas);
        }
    }

    /**
     * Activa el registro del Corporativo seleccionado.
     *
     * @param cycle El IRequestCycle.
     */
    public void seleccionar(IRequestCycle cycle) {
        List tmp = new ArrayList();
        String noCuenta = (String) cycle.getServiceParameters()[0];
        setNombreCorto((String) cycle.getServiceParameters()[1]);
        PersonaMoral corporativo = (PersonaMoral) getSicaServiceData().
                findPersonaByContratoSica(noCuenta);
        Object[] obj = new Object[]{noCuenta, getNombreCorto(), null, null, null,
                corporativo.getIdPersona(), "S", null, corporativo.getIdPersona()};
        tmp.add(obj);
        setClientes(tmp);
        setIdCorporativo(corporativo.getIdPersona());
        findBeneficiariosAsignados(corporativo);
        setOperacionExitosa(true);
        getDelegate().record("Favor de buscar el cliente que quiere asignar como Subsidiario del " +
                "Corporativo.", null);
    }

    /**
     * Le agrega al Corporativo activo en pantalla en la base de datos, el registro del Subsidiario
     * seleccionado.
     *
     * @param cycle El IRequestCycle.
     */
    public void asignar(IRequestCycle cycle) {
        PersonaMoral subsidiario = (PersonaMoral) getSicaServiceData().
                findPersonaByContratoSica((String) cycle.getServiceParameters()[0]);
        Object[] obj = (Object[]) getClientes().get(0);
        String noCuenta = (String) obj[0];
        PersonaMoral corporativo = (PersonaMoral) getSicaServiceData().
                findPersonaByContratoSica(noCuenta);
        //PersonaMoral
        subsidiario.setEsGrupo(Boolean.FALSE);
        subsidiario.setGrupoEmpresarial(corporativo);
        getSicaServiceData().update(subsidiario);
        //ContratoSICA
        ContratoSica cs = getSicaServiceData().
                findContratoSicaByIdPersona(subsidiario.getIdPersona());
        cs.setIdGrupoEmpresarial(corporativo.getIdPersona());
        getSicaServiceData().update(cs);
        setOperacionExitosa(true);
        getDelegate().record("La asignaci\u00f3n fue completada con \u00e9xito", null);
        findBeneficiariosAsignados(corporativo);
        setSubsidiarios(new ArrayList());
        setRazonSocial(null);
        setNoCuenta(null);
    }

    /**
     * Le Borra al Corporativo activo en pantalla en la base de datos, el registro del Subsidiario
     * seleccionado.
     *
     * @param cycle El IRequestCycle.
     */
    public void desasignar(IRequestCycle cycle) {
        PersonaMoral subsidiario = (PersonaMoral) getSicaServiceData().
                findPersonaByContratoSica((String) cycle.getServiceParameters()[0]);
        //PersonaMoral
        subsidiario.setEsGrupo(null);
        subsidiario.setGrupoEmpresarial(null);
        getSicaServiceData().update(subsidiario);
        //ContratoSICA
        ContratoSica cs = getSicaServiceData().
                findContratoSicaByIdPersona(subsidiario.getIdPersona());
        cs.setIdGrupoEmpresarial(null);
        getSicaServiceData().update(cs);
        setOperacionExitosa(true);
        getDelegate().record("Se desasign\u00f3 el Subsidiario correctamente", null);
        Object[] obj = (Object[]) getClientes().get(0);
        String noCuenta = (String) obj[0];
        PersonaMoral corporativo = (PersonaMoral) getSicaServiceData().
                findPersonaByContratoSica(noCuenta);
        setAsignados(new ArrayList());
        findBeneficiariosAsignados(corporativo);
        setSubsidiarios(new ArrayList());
        setRazonSocial(null);
        setNoCuenta(null);
    }

    /**
     * Crear un Corporativo del registro seleccionado, siempre y cuando este no sea ya un
     * Corporativo.
     *
     * @param cycle El IRequestCycle.
     */
    public void crearCorporativo(IRequestCycle cycle) {
        PersonaMoral corporativo = (PersonaMoral) getSicaServiceData().
                findPersonaByContratoSica((String) cycle.getServiceParameters()[0]);
        //PersonaMoral
        corporativo.setEsGrupo(Boolean.TRUE);
        corporativo.setGrupoEmpresarial(null);
        getSicaServiceData().update(corporativo);
        //ContratoSICA
        ContratoSica cs = getSicaServiceData().
                findContratoSicaByIdPersona(corporativo.getIdPersona());
        cs.setIdGrupoEmpresarial(corporativo.getIdPersona());
        getSicaServiceData().update(cs);
        Object[] obj = new Object[]{cs.getNoCuenta(), corporativo.getRazonSocial(), null, null,
                null, corporativo.getIdPersona(), "S", null, corporativo.getIdPersona()};
        List tmp = new ArrayList();
        tmp.add(obj);
        setClientes(tmp);
        setOperacionExitosa(true);
        getDelegate().record("Seleccione el Corporativo creado o d\u00e9 Click en " +
                "'CANCELAR TODO' para volver a buscar/crear otro Corporativo.", null);
    }

    /**
     * Borra un Corporativo seleccionado. Deja los campos bup_persona_moral.id_grupo_empresarial,
     * bup_persona_moral.es_grupo y bup_contrato_sica.id_grupo_empresarial en null.
     *
     * @param cycle El IRequestCycle.
     */
    public void borrarCorporativo(IRequestCycle cycle) {
        PersonaMoral corporativo = (PersonaMoral) getSicaServiceData().
                findPersonaByContratoSica((String) cycle.getServiceParameters()[0]);
        //PersonaMoral
        corporativo.setEsGrupo(null);
        corporativo.setGrupoEmpresarial(null);
        getSicaServiceData().update(corporativo);
        //ContratoSICA
        ContratoSica cs = getSicaServiceData().
                findContratoSicaByIdPersona(corporativo.getIdPersona());
        cs.setIdGrupoEmpresarial(null);
        getSicaServiceData().update(cs);
        limpiar();
        setOperacionExitosa(true);
        getDelegate().record("El Corporativo se borr\u00f3 correctamente, d\u00e9 Click en " +
                "'BUSCAR' para volver a seleccionar/crear/borrar otro Corporativo.", null);
    }
    
    /**
     * Limpia variables persistentes.
     */
    public void limpiar() {
        setRazonSocial(null);
        setNoCuenta(null);
        setClientes(new ArrayList());
        setIdCorporativo(null);
        setAsignados(new ArrayList());
        setSubsidiarios(new ArrayList());
        setNombreCorto(null);
    }
    
    /**
     * Regresa los Subsidiarios Asignados a un Corporativo.
     *
     * @return List.
     */
    public abstract List getAsignados();
    
    /**
     * Fija la lista de Subsidiarios Asignados a un Corporativo encontrados, de acuerdo a los
     * criterios de B&uacute;squeda preestablecidos.
     *
     * @param asignados La lista de Subsidiarios Asignados encontrados.
     */
    public abstract void setAsignados(List asignados);
    
    /**
     * Regresa la lista de Corporativos.
     *
     * @return List.
     */
    public abstract List getClientes();
    
    /**
     * Activa la lista de Corporativos encontrados de acuerdo a los criterios de B&uacute;squeda
     * preestablecidos.
     *
     * @param clientes La lista de Corporativos encontrados.
     */
    public abstract void setClientes(List clientes);

    /**
     * Establece el valor de idCorporativo.
     *
     * @param idCorporativo El valor a asignar.
     */
    public abstract void setIdCorporativo(Integer idCorporativo);
    
    /**
     * Activa la lista de Subsidiarios encontrados de acuerdo a los criterios de B&uacute;squeda
     * preestablecidos.
     *
     * @param subsidiarios La lista de Subsidiarios encontrados.
     */
    public abstract void setSubsidiarios(List subsidiarios);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Raz&oacute;n Social.
     *
     * @return String RazonSocial.
     */
    public abstract String getRazonSocial();

    /**
     * Fija el valor de razonSocial.
     *
     * @param razonSocial El valor a asignar.
     */
    public abstract void setRazonSocial(String razonSocial);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda No. de Cuenta (Contrato).
     *
     * @return String NoCuenta.
     */
    public abstract String getNoCuenta();

    /**
     * Fija el valor de noCuenta.
     *
     * @param noCuenta El valor a asignar.
     */
    public abstract void setNoCuenta(String noCuenta);

    /**
     * Obtiene la Raz&oacute;n Social del Corporativo Seleccionado. 
     *
     * @return String NombreCorto.
     */
    public abstract String getNombreCorto();

    /**
     * Fija el valor de nombreCorto.
     *
     * @param nombreCorto El valor a asignar.
     */
    public abstract void setNombreCorto(String nombreCorto);

    /**
     * Regresa el modo de ejecuci&oacute;n de submit de la p&aacute;gina.
     *
     * @return int El modo de ejecuci&oacute;n
     */
    public abstract int getModoRefresh();

    /**
     * Bandera que indica si la operaci&oacute;n fue exitosa.
     *
     * @param operacionExitosa True o False.
     */
    public abstract void setOperacionExitosa(boolean operacionExitosa);

}	