/**
 * ConsultarClabesSaldosRespuestaDatosPersonales.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.banorte.ws.esb.consultaclabessaldos;

public class ConsultarClabesSaldosRespuestaDatosPersonales  implements java.io.Serializable {
    private java.util.Date fechaProceso;

    /* NUMERO DE CUENTA */
    private java.lang.String tran_NumeroCuenta;

    /* CUENTA ASOCIADA */
    private java.lang.String cuentaAsociada;

    /* NUMERO DE CLIENTE */
    private java.lang.String tran_NumeroCliente;

    /* NOMBRE DEL CLIENTE */
    private java.lang.String tran_NombrePersona;

    /* REGISTRO FED. CAUS. */
    private java.lang.String rfc;

    /* NOMBRE DE CALLE */
    private java.lang.String nombreCalle;

    /* NUMERO DE CALLE */
    private java.lang.String numeroCalle;

    /* NOMBRE COLONIA */
    private java.lang.String nombreColonia;

    /* ENTIDAD FEDERATIVA */
    private java.lang.String entidadFederativa;

    /* NOMBRE DE CIUDAD */
    private java.lang.String nombreCiudad;

    /* CODIGO POSTAL */
    private java.lang.String codigoPostal;

    /* TELEFONO */
    private java.lang.String telefono;

    /* TIPO DE PRODUCTO */
    private java.lang.String tran_TipoProducto;

    /* SUBPRODUCTO */
    private java.lang.String tran_SubProducto;

    /* DESCRIPCION SUBPRODU */
    private java.lang.String descripcionSubprodu;

    /* ESTATUS DE LA CTA. */
    private java.lang.String estatusCta;

    /* CR ORIGEN */
    private java.lang.String crOrigen;

    /* DESCRIPCION SUCURSAL */
    private java.lang.String descripcionSucursal;

    private java.util.Date fechaApertura;

    private java.util.Date tran_FechaVencimiento;

    /* TIPO DE DIVISA */
    private java.lang.String tran_TipoDivisa;

    /* CLABE CUENTA */
    private java.lang.String clabeCuenta;

    /* SALDO PROMEDIO . */
    private java.math.BigDecimal saldoPromedio;

    /* CODIGO DE RETENCION */
    private java.lang.String codigoRetencion;

    /* REGIMEN */
    private java.lang.String regimen;

    /* SUCEPTIBILIDAD PLAST */
    private java.lang.String suceptibilidadPlast;

    /* TITULARIDAD */
    private java.lang.String titularidad;

    /* CVE DE REGIMEN */
    private java.lang.String cveRegimen;

    /* CODIGO DE BLOQUEO */
    private java.lang.String codigoBloqueo;

    /* SITUACION DE CUENTA */
    private java.lang.String situacionCuenta;

    /* BLOQUEO JUDICIAL */
    private java.lang.String bloqueoJudicial;

    /* BLOQUEO ADMON */
    private java.lang.String bloqueoAdmon;

    /* LIQUIDACION INTE. */
    private java.lang.String liquidacionInte;

    /* LINEA DE PRIVILEGIOS */
    private java.lang.String lineaPrivilegios;

    /* MONTO DE SERVICIO */
    private java.math.BigDecimal montoServicio;

    /* ESTATUS DE LIN.PRIV. */
    private java.lang.String estatusLinPriv;

    /* IND DE SERVICIO */
    private java.lang.String indServicio;

    /* IND DE COMISION */
    private java.lang.String indComision;

    /* REGIMEN DE FIRMAS */
    private java.lang.String regimenFirmas;

    /* NOMBRE PROMOTOR */
    private java.lang.String nombrePromotor;

    /* SEGMENTO CLIENTE */
    private java.lang.String segmentoCliente;

    /* BANCO GESTOR DE CTA */
    private java.lang.String banco;

    public ConsultarClabesSaldosRespuestaDatosPersonales() {
    }

    public ConsultarClabesSaldosRespuestaDatosPersonales(
           java.util.Date fechaProceso,
           java.lang.String tran_NumeroCuenta,
           java.lang.String cuentaAsociada,
           java.lang.String tran_NumeroCliente,
           java.lang.String tran_NombrePersona,
           java.lang.String rfc,
           java.lang.String nombreCalle,
           java.lang.String numeroCalle,
           java.lang.String nombreColonia,
           java.lang.String entidadFederativa,
           java.lang.String nombreCiudad,
           java.lang.String codigoPostal,
           java.lang.String telefono,
           java.lang.String tran_TipoProducto,
           java.lang.String tran_SubProducto,
           java.lang.String descripcionSubprodu,
           java.lang.String estatusCta,
           java.lang.String crOrigen,
           java.lang.String descripcionSucursal,
           java.util.Date fechaApertura,
           java.util.Date tran_FechaVencimiento,
           java.lang.String tran_TipoDivisa,
           java.lang.String clabeCuenta,
           java.math.BigDecimal saldoPromedio,
           java.lang.String codigoRetencion,
           java.lang.String regimen,
           java.lang.String suceptibilidadPlast,
           java.lang.String titularidad,
           java.lang.String cveRegimen,
           java.lang.String codigoBloqueo,
           java.lang.String situacionCuenta,
           java.lang.String bloqueoJudicial,
           java.lang.String bloqueoAdmon,
           java.lang.String liquidacionInte,
           java.lang.String lineaPrivilegios,
           java.math.BigDecimal montoServicio,
           java.lang.String estatusLinPriv,
           java.lang.String indServicio,
           java.lang.String indComision,
           java.lang.String regimenFirmas,
           java.lang.String nombrePromotor,
           java.lang.String segmentoCliente,
           java.lang.String banco) {
           this.fechaProceso = fechaProceso;
           this.tran_NumeroCuenta = tran_NumeroCuenta;
           this.cuentaAsociada = cuentaAsociada;
           this.tran_NumeroCliente = tran_NumeroCliente;
           this.tran_NombrePersona = tran_NombrePersona;
           this.rfc = rfc;
           this.nombreCalle = nombreCalle;
           this.numeroCalle = numeroCalle;
           this.nombreColonia = nombreColonia;
           this.entidadFederativa = entidadFederativa;
           this.nombreCiudad = nombreCiudad;
           this.codigoPostal = codigoPostal;
           this.telefono = telefono;
           this.tran_TipoProducto = tran_TipoProducto;
           this.tran_SubProducto = tran_SubProducto;
           this.descripcionSubprodu = descripcionSubprodu;
           this.estatusCta = estatusCta;
           this.crOrigen = crOrigen;
           this.descripcionSucursal = descripcionSucursal;
           this.fechaApertura = fechaApertura;
           this.tran_FechaVencimiento = tran_FechaVencimiento;
           this.tran_TipoDivisa = tran_TipoDivisa;
           this.clabeCuenta = clabeCuenta;
           this.saldoPromedio = saldoPromedio;
           this.codigoRetencion = codigoRetencion;
           this.regimen = regimen;
           this.suceptibilidadPlast = suceptibilidadPlast;
           this.titularidad = titularidad;
           this.cveRegimen = cveRegimen;
           this.codigoBloqueo = codigoBloqueo;
           this.situacionCuenta = situacionCuenta;
           this.bloqueoJudicial = bloqueoJudicial;
           this.bloqueoAdmon = bloqueoAdmon;
           this.liquidacionInte = liquidacionInte;
           this.lineaPrivilegios = lineaPrivilegios;
           this.montoServicio = montoServicio;
           this.estatusLinPriv = estatusLinPriv;
           this.indServicio = indServicio;
           this.indComision = indComision;
           this.regimenFirmas = regimenFirmas;
           this.nombrePromotor = nombrePromotor;
           this.segmentoCliente = segmentoCliente;
           this.banco = banco;
    }


    /**
     * Gets the fechaProceso value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return fechaProceso
     */
    public java.util.Date getFechaProceso() {
        return fechaProceso;
    }


    /**
     * Sets the fechaProceso value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param fechaProceso
     */
    public void setFechaProceso(java.util.Date fechaProceso) {
        this.fechaProceso = fechaProceso;
    }


    /**
     * Gets the tran_NumeroCuenta value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return tran_NumeroCuenta   * NUMERO DE CUENTA
     */
    public java.lang.String getTran_NumeroCuenta() {
        return tran_NumeroCuenta;
    }


    /**
     * Sets the tran_NumeroCuenta value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param tran_NumeroCuenta   * NUMERO DE CUENTA
     */
    public void setTran_NumeroCuenta(java.lang.String tran_NumeroCuenta) {
        this.tran_NumeroCuenta = tran_NumeroCuenta;
    }


    /**
     * Gets the cuentaAsociada value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return cuentaAsociada   * CUENTA ASOCIADA
     */
    public java.lang.String getCuentaAsociada() {
        return cuentaAsociada;
    }


    /**
     * Sets the cuentaAsociada value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param cuentaAsociada   * CUENTA ASOCIADA
     */
    public void setCuentaAsociada(java.lang.String cuentaAsociada) {
        this.cuentaAsociada = cuentaAsociada;
    }


    /**
     * Gets the tran_NumeroCliente value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return tran_NumeroCliente   * NUMERO DE CLIENTE
     */
    public java.lang.String getTran_NumeroCliente() {
        return tran_NumeroCliente;
    }


    /**
     * Sets the tran_NumeroCliente value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param tran_NumeroCliente   * NUMERO DE CLIENTE
     */
    public void setTran_NumeroCliente(java.lang.String tran_NumeroCliente) {
        this.tran_NumeroCliente = tran_NumeroCliente;
    }


    /**
     * Gets the tran_NombrePersona value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return tran_NombrePersona   * NOMBRE DEL CLIENTE
     */
    public java.lang.String getTran_NombrePersona() {
        return tran_NombrePersona;
    }


    /**
     * Sets the tran_NombrePersona value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param tran_NombrePersona   * NOMBRE DEL CLIENTE
     */
    public void setTran_NombrePersona(java.lang.String tran_NombrePersona) {
        this.tran_NombrePersona = tran_NombrePersona;
    }


    /**
     * Gets the rfc value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return rfc   * REGISTRO FED. CAUS.
     */
    public java.lang.String getRfc() {
        return rfc;
    }


    /**
     * Sets the rfc value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param rfc   * REGISTRO FED. CAUS.
     */
    public void setRfc(java.lang.String rfc) {
        this.rfc = rfc;
    }


    /**
     * Gets the nombreCalle value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return nombreCalle   * NOMBRE DE CALLE
     */
    public java.lang.String getNombreCalle() {
        return nombreCalle;
    }


    /**
     * Sets the nombreCalle value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param nombreCalle   * NOMBRE DE CALLE
     */
    public void setNombreCalle(java.lang.String nombreCalle) {
        this.nombreCalle = nombreCalle;
    }


    /**
     * Gets the numeroCalle value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return numeroCalle   * NUMERO DE CALLE
     */
    public java.lang.String getNumeroCalle() {
        return numeroCalle;
    }


    /**
     * Sets the numeroCalle value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param numeroCalle   * NUMERO DE CALLE
     */
    public void setNumeroCalle(java.lang.String numeroCalle) {
        this.numeroCalle = numeroCalle;
    }


    /**
     * Gets the nombreColonia value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return nombreColonia   * NOMBRE COLONIA
     */
    public java.lang.String getNombreColonia() {
        return nombreColonia;
    }


    /**
     * Sets the nombreColonia value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param nombreColonia   * NOMBRE COLONIA
     */
    public void setNombreColonia(java.lang.String nombreColonia) {
        this.nombreColonia = nombreColonia;
    }


    /**
     * Gets the entidadFederativa value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return entidadFederativa   * ENTIDAD FEDERATIVA
     */
    public java.lang.String getEntidadFederativa() {
        return entidadFederativa;
    }


    /**
     * Sets the entidadFederativa value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param entidadFederativa   * ENTIDAD FEDERATIVA
     */
    public void setEntidadFederativa(java.lang.String entidadFederativa) {
        this.entidadFederativa = entidadFederativa;
    }


    /**
     * Gets the nombreCiudad value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return nombreCiudad   * NOMBRE DE CIUDAD
     */
    public java.lang.String getNombreCiudad() {
        return nombreCiudad;
    }


    /**
     * Sets the nombreCiudad value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param nombreCiudad   * NOMBRE DE CIUDAD
     */
    public void setNombreCiudad(java.lang.String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }


    /**
     * Gets the codigoPostal value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return codigoPostal   * CODIGO POSTAL
     */
    public java.lang.String getCodigoPostal() {
        return codigoPostal;
    }


    /**
     * Sets the codigoPostal value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param codigoPostal   * CODIGO POSTAL
     */
    public void setCodigoPostal(java.lang.String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }


    /**
     * Gets the telefono value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return telefono   * TELEFONO
     */
    public java.lang.String getTelefono() {
        return telefono;
    }


    /**
     * Sets the telefono value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param telefono   * TELEFONO
     */
    public void setTelefono(java.lang.String telefono) {
        this.telefono = telefono;
    }


    /**
     * Gets the tran_TipoProducto value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return tran_TipoProducto   * TIPO DE PRODUCTO
     */
    public java.lang.String getTran_TipoProducto() {
        return tran_TipoProducto;
    }


    /**
     * Sets the tran_TipoProducto value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param tran_TipoProducto   * TIPO DE PRODUCTO
     */
    public void setTran_TipoProducto(java.lang.String tran_TipoProducto) {
        this.tran_TipoProducto = tran_TipoProducto;
    }


    /**
     * Gets the tran_SubProducto value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return tran_SubProducto   * SUBPRODUCTO
     */
    public java.lang.String getTran_SubProducto() {
        return tran_SubProducto;
    }


    /**
     * Sets the tran_SubProducto value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param tran_SubProducto   * SUBPRODUCTO
     */
    public void setTran_SubProducto(java.lang.String tran_SubProducto) {
        this.tran_SubProducto = tran_SubProducto;
    }


    /**
     * Gets the descripcionSubprodu value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return descripcionSubprodu   * DESCRIPCION SUBPRODU
     */
    public java.lang.String getDescripcionSubprodu() {
        return descripcionSubprodu;
    }


    /**
     * Sets the descripcionSubprodu value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param descripcionSubprodu   * DESCRIPCION SUBPRODU
     */
    public void setDescripcionSubprodu(java.lang.String descripcionSubprodu) {
        this.descripcionSubprodu = descripcionSubprodu;
    }


    /**
     * Gets the estatusCta value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return estatusCta   * ESTATUS DE LA CTA.
     */
    public java.lang.String getEstatusCta() {
        return estatusCta;
    }


    /**
     * Sets the estatusCta value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param estatusCta   * ESTATUS DE LA CTA.
     */
    public void setEstatusCta(java.lang.String estatusCta) {
        this.estatusCta = estatusCta;
    }


    /**
     * Gets the crOrigen value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return crOrigen   * CR ORIGEN
     */
    public java.lang.String getCrOrigen() {
        return crOrigen;
    }


    /**
     * Sets the crOrigen value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param crOrigen   * CR ORIGEN
     */
    public void setCrOrigen(java.lang.String crOrigen) {
        this.crOrigen = crOrigen;
    }


    /**
     * Gets the descripcionSucursal value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return descripcionSucursal   * DESCRIPCION SUCURSAL
     */
    public java.lang.String getDescripcionSucursal() {
        return descripcionSucursal;
    }


    /**
     * Sets the descripcionSucursal value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param descripcionSucursal   * DESCRIPCION SUCURSAL
     */
    public void setDescripcionSucursal(java.lang.String descripcionSucursal) {
        this.descripcionSucursal = descripcionSucursal;
    }


    /**
     * Gets the fechaApertura value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return fechaApertura
     */
    public java.util.Date getFechaApertura() {
        return fechaApertura;
    }


    /**
     * Sets the fechaApertura value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param fechaApertura
     */
    public void setFechaApertura(java.util.Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }


    /**
     * Gets the tran_FechaVencimiento value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return tran_FechaVencimiento
     */
    public java.util.Date getTran_FechaVencimiento() {
        return tran_FechaVencimiento;
    }


    /**
     * Sets the tran_FechaVencimiento value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param tran_FechaVencimiento
     */
    public void setTran_FechaVencimiento(java.util.Date tran_FechaVencimiento) {
        this.tran_FechaVencimiento = tran_FechaVencimiento;
    }


    /**
     * Gets the tran_TipoDivisa value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return tran_TipoDivisa   * TIPO DE DIVISA
     */
    public java.lang.String getTran_TipoDivisa() {
        return tran_TipoDivisa;
    }


    /**
     * Sets the tran_TipoDivisa value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param tran_TipoDivisa   * TIPO DE DIVISA
     */
    public void setTran_TipoDivisa(java.lang.String tran_TipoDivisa) {
        this.tran_TipoDivisa = tran_TipoDivisa;
    }


    /**
     * Gets the clabeCuenta value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return clabeCuenta   * CLABE CUENTA
     */
    public java.lang.String getClabeCuenta() {
        return clabeCuenta;
    }


    /**
     * Sets the clabeCuenta value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param clabeCuenta   * CLABE CUENTA
     */
    public void setClabeCuenta(java.lang.String clabeCuenta) {
        this.clabeCuenta = clabeCuenta;
    }


    /**
     * Gets the saldoPromedio value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return saldoPromedio   * SALDO PROMEDIO .
     */
    public java.math.BigDecimal getSaldoPromedio() {
        return saldoPromedio;
    }


    /**
     * Sets the saldoPromedio value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param saldoPromedio   * SALDO PROMEDIO .
     */
    public void setSaldoPromedio(java.math.BigDecimal saldoPromedio) {
        this.saldoPromedio = saldoPromedio;
    }


    /**
     * Gets the codigoRetencion value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return codigoRetencion   * CODIGO DE RETENCION
     */
    public java.lang.String getCodigoRetencion() {
        return codigoRetencion;
    }


    /**
     * Sets the codigoRetencion value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param codigoRetencion   * CODIGO DE RETENCION
     */
    public void setCodigoRetencion(java.lang.String codigoRetencion) {
        this.codigoRetencion = codigoRetencion;
    }


    /**
     * Gets the regimen value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return regimen   * REGIMEN
     */
    public java.lang.String getRegimen() {
        return regimen;
    }


    /**
     * Sets the regimen value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param regimen   * REGIMEN
     */
    public void setRegimen(java.lang.String regimen) {
        this.regimen = regimen;
    }


    /**
     * Gets the suceptibilidadPlast value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return suceptibilidadPlast   * SUCEPTIBILIDAD PLAST
     */
    public java.lang.String getSuceptibilidadPlast() {
        return suceptibilidadPlast;
    }


    /**
     * Sets the suceptibilidadPlast value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param suceptibilidadPlast   * SUCEPTIBILIDAD PLAST
     */
    public void setSuceptibilidadPlast(java.lang.String suceptibilidadPlast) {
        this.suceptibilidadPlast = suceptibilidadPlast;
    }


    /**
     * Gets the titularidad value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return titularidad   * TITULARIDAD
     */
    public java.lang.String getTitularidad() {
        return titularidad;
    }


    /**
     * Sets the titularidad value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param titularidad   * TITULARIDAD
     */
    public void setTitularidad(java.lang.String titularidad) {
        this.titularidad = titularidad;
    }


    /**
     * Gets the cveRegimen value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return cveRegimen   * CVE DE REGIMEN
     */
    public java.lang.String getCveRegimen() {
        return cveRegimen;
    }


    /**
     * Sets the cveRegimen value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param cveRegimen   * CVE DE REGIMEN
     */
    public void setCveRegimen(java.lang.String cveRegimen) {
        this.cveRegimen = cveRegimen;
    }


    /**
     * Gets the codigoBloqueo value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return codigoBloqueo   * CODIGO DE BLOQUEO
     */
    public java.lang.String getCodigoBloqueo() {
        return codigoBloqueo;
    }


    /**
     * Sets the codigoBloqueo value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param codigoBloqueo   * CODIGO DE BLOQUEO
     */
    public void setCodigoBloqueo(java.lang.String codigoBloqueo) {
        this.codigoBloqueo = codigoBloqueo;
    }


    /**
     * Gets the situacionCuenta value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return situacionCuenta   * SITUACION DE CUENTA
     */
    public java.lang.String getSituacionCuenta() {
        return situacionCuenta;
    }


    /**
     * Sets the situacionCuenta value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param situacionCuenta   * SITUACION DE CUENTA
     */
    public void setSituacionCuenta(java.lang.String situacionCuenta) {
        this.situacionCuenta = situacionCuenta;
    }


    /**
     * Gets the bloqueoJudicial value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return bloqueoJudicial   * BLOQUEO JUDICIAL
     */
    public java.lang.String getBloqueoJudicial() {
        return bloqueoJudicial;
    }


    /**
     * Sets the bloqueoJudicial value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param bloqueoJudicial   * BLOQUEO JUDICIAL
     */
    public void setBloqueoJudicial(java.lang.String bloqueoJudicial) {
        this.bloqueoJudicial = bloqueoJudicial;
    }


    /**
     * Gets the bloqueoAdmon value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return bloqueoAdmon   * BLOQUEO ADMON
     */
    public java.lang.String getBloqueoAdmon() {
        return bloqueoAdmon;
    }


    /**
     * Sets the bloqueoAdmon value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param bloqueoAdmon   * BLOQUEO ADMON
     */
    public void setBloqueoAdmon(java.lang.String bloqueoAdmon) {
        this.bloqueoAdmon = bloqueoAdmon;
    }


    /**
     * Gets the liquidacionInte value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return liquidacionInte   * LIQUIDACION INTE.
     */
    public java.lang.String getLiquidacionInte() {
        return liquidacionInte;
    }


    /**
     * Sets the liquidacionInte value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param liquidacionInte   * LIQUIDACION INTE.
     */
    public void setLiquidacionInte(java.lang.String liquidacionInte) {
        this.liquidacionInte = liquidacionInte;
    }


    /**
     * Gets the lineaPrivilegios value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return lineaPrivilegios   * LINEA DE PRIVILEGIOS
     */
    public java.lang.String getLineaPrivilegios() {
        return lineaPrivilegios;
    }


    /**
     * Sets the lineaPrivilegios value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param lineaPrivilegios   * LINEA DE PRIVILEGIOS
     */
    public void setLineaPrivilegios(java.lang.String lineaPrivilegios) {
        this.lineaPrivilegios = lineaPrivilegios;
    }


    /**
     * Gets the montoServicio value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return montoServicio   * MONTO DE SERVICIO
     */
    public java.math.BigDecimal getMontoServicio() {
        return montoServicio;
    }


    /**
     * Sets the montoServicio value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param montoServicio   * MONTO DE SERVICIO
     */
    public void setMontoServicio(java.math.BigDecimal montoServicio) {
        this.montoServicio = montoServicio;
    }


    /**
     * Gets the estatusLinPriv value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return estatusLinPriv   * ESTATUS DE LIN.PRIV.
     */
    public java.lang.String getEstatusLinPriv() {
        return estatusLinPriv;
    }


    /**
     * Sets the estatusLinPriv value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param estatusLinPriv   * ESTATUS DE LIN.PRIV.
     */
    public void setEstatusLinPriv(java.lang.String estatusLinPriv) {
        this.estatusLinPriv = estatusLinPriv;
    }


    /**
     * Gets the indServicio value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return indServicio   * IND DE SERVICIO
     */
    public java.lang.String getIndServicio() {
        return indServicio;
    }


    /**
     * Sets the indServicio value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param indServicio   * IND DE SERVICIO
     */
    public void setIndServicio(java.lang.String indServicio) {
        this.indServicio = indServicio;
    }


    /**
     * Gets the indComision value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return indComision   * IND DE COMISION
     */
    public java.lang.String getIndComision() {
        return indComision;
    }


    /**
     * Sets the indComision value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param indComision   * IND DE COMISION
     */
    public void setIndComision(java.lang.String indComision) {
        this.indComision = indComision;
    }


    /**
     * Gets the regimenFirmas value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return regimenFirmas   * REGIMEN DE FIRMAS
     */
    public java.lang.String getRegimenFirmas() {
        return regimenFirmas;
    }


    /**
     * Sets the regimenFirmas value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param regimenFirmas   * REGIMEN DE FIRMAS
     */
    public void setRegimenFirmas(java.lang.String regimenFirmas) {
        this.regimenFirmas = regimenFirmas;
    }


    /**
     * Gets the nombrePromotor value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return nombrePromotor   * NOMBRE PROMOTOR
     */
    public java.lang.String getNombrePromotor() {
        return nombrePromotor;
    }


    /**
     * Sets the nombrePromotor value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param nombrePromotor   * NOMBRE PROMOTOR
     */
    public void setNombrePromotor(java.lang.String nombrePromotor) {
        this.nombrePromotor = nombrePromotor;
    }


    /**
     * Gets the segmentoCliente value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return segmentoCliente   * SEGMENTO CLIENTE
     */
    public java.lang.String getSegmentoCliente() {
        return segmentoCliente;
    }


    /**
     * Sets the segmentoCliente value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param segmentoCliente   * SEGMENTO CLIENTE
     */
    public void setSegmentoCliente(java.lang.String segmentoCliente) {
        this.segmentoCliente = segmentoCliente;
    }


    /**
     * Gets the banco value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @return banco   * BANCO GESTOR DE CTA
     */
    public java.lang.String getBanco() {
        return banco;
    }


    /**
     * Sets the banco value for this ConsultarClabesSaldosRespuestaDatosPersonales.
     * 
     * @param banco   * BANCO GESTOR DE CTA
     */
    public void setBanco(java.lang.String banco) {
        this.banco = banco;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultarClabesSaldosRespuestaDatosPersonales)) return false;
        ConsultarClabesSaldosRespuestaDatosPersonales other = (ConsultarClabesSaldosRespuestaDatosPersonales) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fechaProceso==null && other.getFechaProceso()==null) || 
             (this.fechaProceso!=null &&
              this.fechaProceso.equals(other.getFechaProceso()))) &&
            ((this.tran_NumeroCuenta==null && other.getTran_NumeroCuenta()==null) || 
             (this.tran_NumeroCuenta!=null &&
              this.tran_NumeroCuenta.equals(other.getTran_NumeroCuenta()))) &&
            ((this.cuentaAsociada==null && other.getCuentaAsociada()==null) || 
             (this.cuentaAsociada!=null &&
              this.cuentaAsociada.equals(other.getCuentaAsociada()))) &&
            ((this.tran_NumeroCliente==null && other.getTran_NumeroCliente()==null) || 
             (this.tran_NumeroCliente!=null &&
              this.tran_NumeroCliente.equals(other.getTran_NumeroCliente()))) &&
            ((this.tran_NombrePersona==null && other.getTran_NombrePersona()==null) || 
             (this.tran_NombrePersona!=null &&
              this.tran_NombrePersona.equals(other.getTran_NombrePersona()))) &&
            ((this.rfc==null && other.getRfc()==null) || 
             (this.rfc!=null &&
              this.rfc.equals(other.getRfc()))) &&
            ((this.nombreCalle==null && other.getNombreCalle()==null) || 
             (this.nombreCalle!=null &&
              this.nombreCalle.equals(other.getNombreCalle()))) &&
            ((this.numeroCalle==null && other.getNumeroCalle()==null) || 
             (this.numeroCalle!=null &&
              this.numeroCalle.equals(other.getNumeroCalle()))) &&
            ((this.nombreColonia==null && other.getNombreColonia()==null) || 
             (this.nombreColonia!=null &&
              this.nombreColonia.equals(other.getNombreColonia()))) &&
            ((this.entidadFederativa==null && other.getEntidadFederativa()==null) || 
             (this.entidadFederativa!=null &&
              this.entidadFederativa.equals(other.getEntidadFederativa()))) &&
            ((this.nombreCiudad==null && other.getNombreCiudad()==null) || 
             (this.nombreCiudad!=null &&
              this.nombreCiudad.equals(other.getNombreCiudad()))) &&
            ((this.codigoPostal==null && other.getCodigoPostal()==null) || 
             (this.codigoPostal!=null &&
              this.codigoPostal.equals(other.getCodigoPostal()))) &&
            ((this.telefono==null && other.getTelefono()==null) || 
             (this.telefono!=null &&
              this.telefono.equals(other.getTelefono()))) &&
            ((this.tran_TipoProducto==null && other.getTran_TipoProducto()==null) || 
             (this.tran_TipoProducto!=null &&
              this.tran_TipoProducto.equals(other.getTran_TipoProducto()))) &&
            ((this.tran_SubProducto==null && other.getTran_SubProducto()==null) || 
             (this.tran_SubProducto!=null &&
              this.tran_SubProducto.equals(other.getTran_SubProducto()))) &&
            ((this.descripcionSubprodu==null && other.getDescripcionSubprodu()==null) || 
             (this.descripcionSubprodu!=null &&
              this.descripcionSubprodu.equals(other.getDescripcionSubprodu()))) &&
            ((this.estatusCta==null && other.getEstatusCta()==null) || 
             (this.estatusCta!=null &&
              this.estatusCta.equals(other.getEstatusCta()))) &&
            ((this.crOrigen==null && other.getCrOrigen()==null) || 
             (this.crOrigen!=null &&
              this.crOrigen.equals(other.getCrOrigen()))) &&
            ((this.descripcionSucursal==null && other.getDescripcionSucursal()==null) || 
             (this.descripcionSucursal!=null &&
              this.descripcionSucursal.equals(other.getDescripcionSucursal()))) &&
            ((this.fechaApertura==null && other.getFechaApertura()==null) || 
             (this.fechaApertura!=null &&
              this.fechaApertura.equals(other.getFechaApertura()))) &&
            ((this.tran_FechaVencimiento==null && other.getTran_FechaVencimiento()==null) || 
             (this.tran_FechaVencimiento!=null &&
              this.tran_FechaVencimiento.equals(other.getTran_FechaVencimiento()))) &&
            ((this.tran_TipoDivisa==null && other.getTran_TipoDivisa()==null) || 
             (this.tran_TipoDivisa!=null &&
              this.tran_TipoDivisa.equals(other.getTran_TipoDivisa()))) &&
            ((this.clabeCuenta==null && other.getClabeCuenta()==null) || 
             (this.clabeCuenta!=null &&
              this.clabeCuenta.equals(other.getClabeCuenta()))) &&
            ((this.saldoPromedio==null && other.getSaldoPromedio()==null) || 
             (this.saldoPromedio!=null &&
              this.saldoPromedio.equals(other.getSaldoPromedio()))) &&
            ((this.codigoRetencion==null && other.getCodigoRetencion()==null) || 
             (this.codigoRetencion!=null &&
              this.codigoRetencion.equals(other.getCodigoRetencion()))) &&
            ((this.regimen==null && other.getRegimen()==null) || 
             (this.regimen!=null &&
              this.regimen.equals(other.getRegimen()))) &&
            ((this.suceptibilidadPlast==null && other.getSuceptibilidadPlast()==null) || 
             (this.suceptibilidadPlast!=null &&
              this.suceptibilidadPlast.equals(other.getSuceptibilidadPlast()))) &&
            ((this.titularidad==null && other.getTitularidad()==null) || 
             (this.titularidad!=null &&
              this.titularidad.equals(other.getTitularidad()))) &&
            ((this.cveRegimen==null && other.getCveRegimen()==null) || 
             (this.cveRegimen!=null &&
              this.cveRegimen.equals(other.getCveRegimen()))) &&
            ((this.codigoBloqueo==null && other.getCodigoBloqueo()==null) || 
             (this.codigoBloqueo!=null &&
              this.codigoBloqueo.equals(other.getCodigoBloqueo()))) &&
            ((this.situacionCuenta==null && other.getSituacionCuenta()==null) || 
             (this.situacionCuenta!=null &&
              this.situacionCuenta.equals(other.getSituacionCuenta()))) &&
            ((this.bloqueoJudicial==null && other.getBloqueoJudicial()==null) || 
             (this.bloqueoJudicial!=null &&
              this.bloqueoJudicial.equals(other.getBloqueoJudicial()))) &&
            ((this.bloqueoAdmon==null && other.getBloqueoAdmon()==null) || 
             (this.bloqueoAdmon!=null &&
              this.bloqueoAdmon.equals(other.getBloqueoAdmon()))) &&
            ((this.liquidacionInte==null && other.getLiquidacionInte()==null) || 
             (this.liquidacionInte!=null &&
              this.liquidacionInte.equals(other.getLiquidacionInte()))) &&
            ((this.lineaPrivilegios==null && other.getLineaPrivilegios()==null) || 
             (this.lineaPrivilegios!=null &&
              this.lineaPrivilegios.equals(other.getLineaPrivilegios()))) &&
            ((this.montoServicio==null && other.getMontoServicio()==null) || 
             (this.montoServicio!=null &&
              this.montoServicio.equals(other.getMontoServicio()))) &&
            ((this.estatusLinPriv==null && other.getEstatusLinPriv()==null) || 
             (this.estatusLinPriv!=null &&
              this.estatusLinPriv.equals(other.getEstatusLinPriv()))) &&
            ((this.indServicio==null && other.getIndServicio()==null) || 
             (this.indServicio!=null &&
              this.indServicio.equals(other.getIndServicio()))) &&
            ((this.indComision==null && other.getIndComision()==null) || 
             (this.indComision!=null &&
              this.indComision.equals(other.getIndComision()))) &&
            ((this.regimenFirmas==null && other.getRegimenFirmas()==null) || 
             (this.regimenFirmas!=null &&
              this.regimenFirmas.equals(other.getRegimenFirmas()))) &&
            ((this.nombrePromotor==null && other.getNombrePromotor()==null) || 
             (this.nombrePromotor!=null &&
              this.nombrePromotor.equals(other.getNombrePromotor()))) &&
            ((this.segmentoCliente==null && other.getSegmentoCliente()==null) || 
             (this.segmentoCliente!=null &&
              this.segmentoCliente.equals(other.getSegmentoCliente()))) &&
            ((this.banco==null && other.getBanco()==null) || 
             (this.banco!=null &&
              this.banco.equals(other.getBanco())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getFechaProceso() != null) {
            _hashCode += getFechaProceso().hashCode();
        }
        if (getTran_NumeroCuenta() != null) {
            _hashCode += getTran_NumeroCuenta().hashCode();
        }
        if (getCuentaAsociada() != null) {
            _hashCode += getCuentaAsociada().hashCode();
        }
        if (getTran_NumeroCliente() != null) {
            _hashCode += getTran_NumeroCliente().hashCode();
        }
        if (getTran_NombrePersona() != null) {
            _hashCode += getTran_NombrePersona().hashCode();
        }
        if (getRfc() != null) {
            _hashCode += getRfc().hashCode();
        }
        if (getNombreCalle() != null) {
            _hashCode += getNombreCalle().hashCode();
        }
        if (getNumeroCalle() != null) {
            _hashCode += getNumeroCalle().hashCode();
        }
        if (getNombreColonia() != null) {
            _hashCode += getNombreColonia().hashCode();
        }
        if (getEntidadFederativa() != null) {
            _hashCode += getEntidadFederativa().hashCode();
        }
        if (getNombreCiudad() != null) {
            _hashCode += getNombreCiudad().hashCode();
        }
        if (getCodigoPostal() != null) {
            _hashCode += getCodigoPostal().hashCode();
        }
        if (getTelefono() != null) {
            _hashCode += getTelefono().hashCode();
        }
        if (getTran_TipoProducto() != null) {
            _hashCode += getTran_TipoProducto().hashCode();
        }
        if (getTran_SubProducto() != null) {
            _hashCode += getTran_SubProducto().hashCode();
        }
        if (getDescripcionSubprodu() != null) {
            _hashCode += getDescripcionSubprodu().hashCode();
        }
        if (getEstatusCta() != null) {
            _hashCode += getEstatusCta().hashCode();
        }
        if (getCrOrigen() != null) {
            _hashCode += getCrOrigen().hashCode();
        }
        if (getDescripcionSucursal() != null) {
            _hashCode += getDescripcionSucursal().hashCode();
        }
        if (getFechaApertura() != null) {
            _hashCode += getFechaApertura().hashCode();
        }
        if (getTran_FechaVencimiento() != null) {
            _hashCode += getTran_FechaVencimiento().hashCode();
        }
        if (getTran_TipoDivisa() != null) {
            _hashCode += getTran_TipoDivisa().hashCode();
        }
        if (getClabeCuenta() != null) {
            _hashCode += getClabeCuenta().hashCode();
        }
        if (getSaldoPromedio() != null) {
            _hashCode += getSaldoPromedio().hashCode();
        }
        if (getCodigoRetencion() != null) {
            _hashCode += getCodigoRetencion().hashCode();
        }
        if (getRegimen() != null) {
            _hashCode += getRegimen().hashCode();
        }
        if (getSuceptibilidadPlast() != null) {
            _hashCode += getSuceptibilidadPlast().hashCode();
        }
        if (getTitularidad() != null) {
            _hashCode += getTitularidad().hashCode();
        }
        if (getCveRegimen() != null) {
            _hashCode += getCveRegimen().hashCode();
        }
        if (getCodigoBloqueo() != null) {
            _hashCode += getCodigoBloqueo().hashCode();
        }
        if (getSituacionCuenta() != null) {
            _hashCode += getSituacionCuenta().hashCode();
        }
        if (getBloqueoJudicial() != null) {
            _hashCode += getBloqueoJudicial().hashCode();
        }
        if (getBloqueoAdmon() != null) {
            _hashCode += getBloqueoAdmon().hashCode();
        }
        if (getLiquidacionInte() != null) {
            _hashCode += getLiquidacionInte().hashCode();
        }
        if (getLineaPrivilegios() != null) {
            _hashCode += getLineaPrivilegios().hashCode();
        }
        if (getMontoServicio() != null) {
            _hashCode += getMontoServicio().hashCode();
        }
        if (getEstatusLinPriv() != null) {
            _hashCode += getEstatusLinPriv().hashCode();
        }
        if (getIndServicio() != null) {
            _hashCode += getIndServicio().hashCode();
        }
        if (getIndComision() != null) {
            _hashCode += getIndComision().hashCode();
        }
        if (getRegimenFirmas() != null) {
            _hashCode += getRegimenFirmas().hashCode();
        }
        if (getNombrePromotor() != null) {
            _hashCode += getNombrePromotor().hashCode();
        }
        if (getSegmentoCliente() != null) {
            _hashCode += getSegmentoCliente().hashCode();
        }
        if (getBanco() != null) {
            _hashCode += getBanco().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultarClabesSaldosRespuestaDatosPersonales.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.banorte.com/ws/esb/ConsultaClabesSaldos", ">consultarClabesSaldosRespuesta>DatosPersonales"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaProceso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FechaProceso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tran_NumeroCuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Tran_NumeroCuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cuentaAsociada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CuentaAsociada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tran_NumeroCliente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Tran_NumeroCliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tran_NombrePersona");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Tran_NombrePersona"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rfc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Rfc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreCalle");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NombreCalle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroCalle");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NumeroCalle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreColonia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NombreColonia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidadFederativa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EntidadFederativa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreCiudad");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NombreCiudad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoPostal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodigoPostal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telefono");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Telefono"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tran_TipoProducto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Tran_TipoProducto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tran_SubProducto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Tran_SubProducto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionSubprodu");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DescripcionSubprodu"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estatusCta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EstatusCta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("crOrigen");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CrOrigen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionSucursal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DescripcionSucursal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaApertura");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FechaApertura"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tran_FechaVencimiento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Tran_FechaVencimiento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tran_TipoDivisa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Tran_TipoDivisa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clabeCuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ClabeCuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saldoPromedio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SaldoPromedio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoRetencion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodigoRetencion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regimen");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Regimen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("suceptibilidadPlast");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SuceptibilidadPlast"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("titularidad");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Titularidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cveRegimen");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CveRegimen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoBloqueo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodigoBloqueo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("situacionCuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SituacionCuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bloqueoJudicial");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BloqueoJudicial"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bloqueoAdmon");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BloqueoAdmon"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("liquidacionInte");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LiquidacionInte"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lineaPrivilegios");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LineaPrivilegios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("montoServicio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MontoServicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estatusLinPriv");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EstatusLinPriv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indServicio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IndServicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indComision");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IndComision"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regimenFirmas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RegimenFirmas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombrePromotor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NombrePromotor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("segmentoCliente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SegmentoCliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("banco");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Banco"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
