package com.ixe.ods.sica.dao;

import com.ixe.ods.sica.dto.CuentaMigradaDto;


/**
 * Interface dao para consulta de cuentas migradas
 *
 * @author $author$
 * @version $Revision: 1.1.2.2 $
 */
public interface PhoenixCuentaMigradaDao {
    /**
     * M�todo que b�sca en la tabla de cuentas migradas
     * una cuenta en particular.
     * @param noCuentaIxe la cuenta a buscar.
     * @return un objeto <code>CuentaMigradaDto</code>
     *          con la informaci�n de la cuenta.
     */
    public CuentaMigradaDto buscarCuenta(String noCuentaIxe);
}
