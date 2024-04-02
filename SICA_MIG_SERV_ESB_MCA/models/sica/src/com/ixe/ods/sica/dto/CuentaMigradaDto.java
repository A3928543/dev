package com.ixe.ods.sica.dto;

import java.io.Serializable;


/**
 * DTO para cuentas migradas.
 *
 * @author Diego Pazaran
 *  Banorte
 *
 * @version $Revision: 1.1.2.1 $
 */
public class CuentaMigradaDto implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * La cuenta Ixe
     */
    private String cuentaIxe;

    /**
     * La cuenta Altamira
     */
    private String cuentaAltamira;

    /**
     * El numero de cliente en Altamira
     */
    private String noClienteAltamiraIxe;

    /**
     * El estatus de la cuenta migrada
     * 'S' : Si
     * 'N' : No
     */
    private String estatus;

    /**
     * Getter Cuenta Ixe
     *
     * @return cuentaIxe con
     * 	el valor de la cuenta. 
     */
    public String getCuentaIxe() {
        return cuentaIxe;
    }

    /**
     * Setter Cuenta Ixe
     *
     * @param cuentaIxe con el 
     * 	numero de cuenta. 
     */
    public void setCuentaIxe(String cuentaIxe) {
        this.cuentaIxe = cuentaIxe;
    }

    /**
     * getter cuenta Altamira
     *
     * @return el numero de
     * 	cuenta Altamira. 
     */
    public String getCuentaAltamira() {
        return cuentaAltamira;
    }

    /**
     * Setter cuenta Altamira.
     *
     * @param cuentaAltamira con el 
     * 	numero de cuenta. 
     */
    public void setCuentaAltamira(String cuentaAltamira) {
        this.cuentaAltamira = cuentaAltamira;
    }

    /**
     * getter no cliente Altamira
     *
     * @return El numero de cliente
     * 	en Altamira. 
     */
    public String getNoClienteAltamiraIxe() {
        return noClienteAltamiraIxe;
    }

    /**
     * Setter nomero cliente Altamira
     *
     * @param noClienteAltamiraIxe el 
     * 	numero de cliente en altamira.
     */
    public void setNoClienteAltamiraIxe(String noClienteAltamiraIxe) {
        this.noClienteAltamiraIxe = noClienteAltamiraIxe;
    }

    /**
     * getter estatus
     *
     * @return String
     * 	con el estatus de la cuenta. 
     */
    public String getEstatus() {
        return estatus;
    }

    /**
     * Setter Estatus
     *
     * @param estatus el estatus
     * 	de la cuenta. 
     */
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    /**
     * Define el meto toString.
     *
     * @return String
     * 	con los campos del dto. 
     */
    public String toString() {
        return new StringBuffer(" cuentaIxe: " + cuentaIxe).append(" cuentaAltamira: " +
            cuentaAltamira).append(" noClienteAltamiraIxe: " + noClienteAltamiraIxe)
                                                           .append(" estatus: " + estatus).toString();
    }
}
