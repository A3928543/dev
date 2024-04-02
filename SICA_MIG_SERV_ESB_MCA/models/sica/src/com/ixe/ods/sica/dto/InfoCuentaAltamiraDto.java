package com.ixe.ods.sica.dto;

/**
 * Dto para informacion de cuenta
 *
 * @author Diego Pazaran
 *  Banorte
 *
 * @version $Revision: 1.1.2.1.12.3.2.1 $
 */
public class InfoCuentaAltamiraDto {
    /** El estatus de la cuenta * */
    private String statusCuenta;

    /** Numero de Cuenta * */
    private String numeroCuenta;

    /** Numero de Cliente * */
    private String numeroCliente;

    /** Nombre del Cliente * */
    private String nombreCliente;

    /** El idPersona en la BUP */
    private String idPersona;
    
    /** El centro de responsabilidad */
    private String cr;
    
    /**Descripcion de la sucursal**/
    private String descSucursal;
    

	/**Moneda en la que se encuentra dada de alta la cuenta**/
    private String tipoDivisa;
    

    /**
     * getter estatus cuenta.
     *
     * @return statusCuenta
     */
    public String getStatusCuenta() {
        return statusCuenta;
    }

    /**
     * Setter statusCuenta.
     *
     * @param statusCuenta con
     * 	el estatus de la cuenta.
     */
    public void setStatusCuenta(String statusCuenta) {
        this.statusCuenta = statusCuenta;
    }

    /**
     * Getter numero de cuenta.
     *
     * @return el numero de cuenta.
     */
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    /**
     * Setter Numero de cuennta.
     *
     * @param numeroCuenta con el 
     *  	numero de cuenta. 
     */
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    /**
     * Getter numero de cliente.
     *
     * @return el numero de cliente. 
     */
    public String getNumeroCliente() {
        return numeroCliente;
    }

    /**
     * Setter numero de cliente.
     *
     * @param numeroCliente con
     * 	el numero de cliente.  
     */
    public void setNumeroCliente(String numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    /**
     * Getter nombreCliente
     *
     * @return El nombre del cliente.
     */
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * Setter nombreCliente
     *
     * @param nombreCliente con
     * 	el nombre del cliente. 
     */
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    /**
     * getter IdPersona bup
     *
     * @return Integer con el 
     * 	idPersona bup.
     */
    public Integer getIdPersona() {
        try {
            return new Integer(idPersona);
        }
        catch (NumberFormatException nfe) {
            return new Integer(0);
        }
    }

    /**
     * Setter idPersona
     *
     * @param idPersona de la BUP
     */
    public void setIdPersona(String idPersona) {
        this.idPersona = idPersona;
    }
    
    /**
     * Regresa el Cr de la
     * 	cuenta
     * @return <code>String</code>
     * 	con el cr de la cuenta.
     */
    public String getCr() {
		return cr;
	}

    /**
     * Asigna el Cr de la cuenta
     * @param cr con el valor
     * 	a asignar.
     */
	public void setCr(String cr) {
		this.cr = cr;
	}
	
    /**
     * Regresa la descripcion de la
     * 	sucursal
     * @return <code>String</code>
     * 	con el cr de la cuenta.
     */
    public String getDescSucursal() {
		return descSucursal;
	}

    /**
     * Asigna la descripcion de la sucursal
     * @param descSucursal con el valor
     * 	a asignar.
     */
    public void setDescSucursal(String descSucursal) {
		this.descSucursal = descSucursal;
	}

	
	/**
	 * Regresa el Tipo Divisa de la cuenta.
	 * @return
	 */
    public String getTipoDivisa() {
		return tipoDivisa;
	}

    /**
     * Asigna el tipo Divisa de la cuenta
     * @param tipoDivisa
     */
	public void setTipoDivisa(String tipoDivisa) {
		this.tipoDivisa = tipoDivisa;
	}
	/**
     * define toString
     *
     * @return String con el valor
     * 	de las propiedades. 
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("\nInfoCuentaAltamiraDto: ");
        sb.append("\n numeroCuenta: ");
        sb.append(getNumeroCuenta());
        sb.append("\n numeroCliente: ");
        sb.append(getNumeroCliente());
        sb.append("\n nombreCliente: ");
        sb.append(getNombreCliente());
        sb.append("\n statusCuenta: ");
        sb.append(getStatusCuenta());
        sb.append("\n idPersona: ");
        sb.append(getIdPersona());
        sb.append("\n CR: ");
        sb.append(getCr());
        sb.append("\n descripcion: ");
        sb.append(getDescSucursal());
        sb.append("\n tipoDivisa: ");
        sb.append(getTipoDivisa());

        return sb.toString();
    }


}