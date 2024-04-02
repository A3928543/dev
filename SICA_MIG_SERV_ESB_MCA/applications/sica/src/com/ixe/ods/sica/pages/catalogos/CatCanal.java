/*
 * $Id: CatCanal.java,v 1.12.34.1 2010/10/08 01:08:29 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.catalogos;

import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.ixe.ods.bup.model.Sucursal;
import com.ixe.ods.bup.model.TipoIva;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.TipoPizarron;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Subclase de AbstractCatEdicion que permite al usuario editar o insertar un nuevo Canal.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.12.34.1 $ $Date: 2010/10/08 01:08:29 $
 */
public abstract class CatCanal extends AbstractCatEdicion {

    /**
     * Valida los datos capturados, asigna la relaci&oacute;n con Sucursal e inserta o actualiza el
     * registro.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void actualizar(IRequestCycle cycle) {
        if (getDelegate() != null && getDelegate().getHasErrors()) {
            return;
        }
        if (((Canal) getRegistro()).getSucursal().getIdSucursal().intValue() == 0) {
            ((Canal) getRegistro()).setSucursal(null);
        }
        ((Canal) getRegistro()).setIdCanal(((Canal) getRegistro()).getIdCanal().toUpperCase());
        ((Canal) getRegistro()).setNombre(((Canal) getRegistro()).getNombre().toUpperCase());
        ((Canal) getRegistro()).setTipoIva((TipoIva) getSicaServiceData().find(TipoIva.class,
                getClaveTipoIva()));
        if (((Canal) getRegistro()).getTipoPizarron().getIdTipoPizarron().intValue() < 1) {
            getDelegate().record("Debe seleccionar un Tipo de Pizarr\u00f3n para el Canal.", null);
            return;
        }
        else {
        	((Canal) getRegistro()).setTipoPizarron(((Canal) getRegistro()).getTipoPizarron());
        }
        if (!(((Canal) getRegistro()).getCodigoPostal() == null ||
                ((Canal) getRegistro()).getCodigoPostal().equals("")) &&
                !((Canal) getRegistro()).getCodigoPostal().matches("\\d\\d\\d\\d\\d")) {
            getDelegate().record("El c\u00f3digo postal debe ser de 5 d\u00edgitos.", null);
            return;
        }
        if (isModoUpdate()) {
            getSicaServiceData().update(getRegistro());
        }
        else {
            getSicaServiceData().store(getRegistro());
        }
        cycle.activate(getNombrePaginaConsulta());
    }

    /**
     * Regresa un arreglo con todas las Sucursales existentes, a&ntilde;adiendo una sucursal
     * vac&iacute;a al principio de la lista para indicar 'Sin sucursal'.
     *
     * @return List.
     */
    public List getSucursalesFiltradas() {
        List l = getSicaServiceData().findAllSucursales();
        Sucursal s = new Sucursal();
        s.setIdSucursal(new Integer(0));
        s.setNombre("");
        l.add(0, s);
        return l;
    }
    
    /**
     * Modelo para la lista de Tipos de Pizarron.
     * 
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getTiposPizarronModel() {
    	List tiposPizarron = getSicaServiceData().findAll(TipoPizarron.class);
    	TipoPizarron primerElemento = new TipoPizarron();
    	primerElemento.setIdTipoPizarron(new Integer(-1));
    	primerElemento.setDescripcion("Seleccione un Tipo de Pizarr\u00f3n");
    	tiposPizarron.add(0, primerElemento);
    	return new RecordSelectionModel(tiposPizarron, "idTipoPizarron", "descripcion");
    }
        
    /**
     * Regresa el valor de clave tipo IVA
     *
     * @return String.
     */
    public abstract String getClaveTipoIva();
	
    /**
     * Regresa el valor de TipoPizarron.
     * 
     * @return TipoPizarron.
     */
    public abstract TipoPizarron getTipoPizarron();
    
    /**
     * Asigna el valor para TipoPizarron.
     * 
     * @param tipoPizarron El valor para tipoPizarron
     */
    public abstract void setTipoPizarron(TipoPizarron tipoPizarron);
}
