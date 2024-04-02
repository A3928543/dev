/*
 * $Id: CuentasSelectionModel.java,v 1.10 2008/02/22 18:25:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.mesa;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.CuentaEjecutivo;
import com.ixe.ods.sica.model.ContratoCliente;
import com.ixe.ods.sica.sdo.SicaServiceData;
import org.apache.tapestry.form.IPropertySelectionModel;

import java.util.List;

/**
 * Clase para la creaci&oacute;n espec&iacute;fica de un modelo de 
 * Cuentas de ejecutivo
 *
 * @author Gerardo Corzo
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:32 $
 */
public class CuentasSelectionModel implements IPropertySelectionModel {
	
	/**
	 * Constructor de la clase, inicializa el objeto con el dos parametros:
	 * una lista de Cuentas y un Servicio (SicaServiceData).
	 * 
	 * @param cuentas La lista de cuentas.
	 * @param sicaServiceData El servicio sicaServiceData.
	 */
    public CuentasSelectionModel(List cuentas, SicaServiceData sicaServiceData) {
        super();
        _cuentas = cuentas;
        _sicaServiceData = sicaServiceData;
    }

    /**
     * @see org.apache.tapestry.form.IPropertySelectionModel#getOptionCount()
     */
    public int getOptionCount() {
        return _cuentas.size();
    }

    /**
     * @see org.apache.tapestry.form.IPropertySelectionModel#getOption(int)
     */
    public Object getOption(int index) {
        return _cuentas.get(index);
    }

    /**
     * @see org.apache.tapestry.form.IPropertySelectionModel#getLabel(int)
     */
    public String getLabel(int index) {
        CuentaEjecutivo ce = (CuentaEjecutivo) getOption(index);
        ContratoSica cs = _sicaServiceData.findContrato(ce.getId().getNoCuenta());
        return ce.getId().getNoCuenta() + "     " + (cs != null ? ContratoCliente.clienteParaContratoSica(cs).getNombreCompleto() : "");
    }

    /**
     * @see org.apache.tapestry.form.IPropertySelectionModel#getValue(int)
     */
    public String getValue(int index) {
        return String.valueOf(index);
    }

    /**
     * @see org.apache.tapestry.form.IPropertySelectionModel#translateValue(java.lang.String)
     */
    public Object translateValue(String value) {
        return getOption(Integer.valueOf(value).intValue());
    }

    /**
     *  La Lista de las cuentas ejecutivo
     */
    private List _cuentas;

    /**
     * El SicaServiceData
     */
    private SicaServiceData _sicaServiceData;
}