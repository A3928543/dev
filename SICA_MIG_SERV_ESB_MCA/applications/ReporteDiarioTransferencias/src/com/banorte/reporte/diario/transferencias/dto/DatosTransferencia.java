package com.banorte.reporte.diario.transferencias.dto;

import java.math.BigDecimal;
import java.util.Date;

public class DatosTransferencia 
{	
	private Integer idScTraextEnv; // ID_SC_TRAEXT_ENV NUMBER,
	private String fechaOperacion; // FECHA_OPERACION DATE,
    private String tipoOperacion; // TIPO_OPERACION VARCHAR(2),
    private String tipoTransferencia; // TIPO_TRANSFERENCIA VARCHAR(1),
    private String idOperacion; // ID_OPERACION VARCHAR(20),
    private String medio; // MEDIO VARCHAR(1),
    private String folio; // FOLIO VARCHAR(20),
    private String idInstReporta; // ID_INST_REPORTA VARCHAR(6),
    private String idBcoCliOriginador; // ID_BCO_CLI_ORIGINADOR VARCHAR(6),
    private String idCliOriginador; // ID_CLI_ORIGINADOR VARCHAR(20),
    private String tipoCliOriginador; // TIPO_CLI_ORIGINADOR VARCHAR(20),
    private String razonSocialCli; // RAZON_SOCIAL_CLI VARCHAR(150),
    private String apPaternoCli; // AP_PATERNO_CLI VARCHAR(50),
    private String apMaternoCli; // AP_MATERNO_CLI VARCHAR(50),
    private String apNombreCli; // AP_NOMBRE_CLI VARCHAR(50),
    private String fechaNac; // FECHA_NAC DATE,
    private String tipoCuentaOrd; // TIPO_CUENTA_ORD VARCHAR(1),
    private String cuentaOrd; // CUENTA_ORD VARCHAR(18),
    private String tipoIdInstRecept; // TIPO_ID_INST_RECEPT VARCHAR(1),
    private String idInstRecept; // ID_INST_RECEPT VARCHAR(20),
    private String tipoIdInstBenef; // TIPO_ID_INST_BENEF VARCHAR(1),
    private String idInstBenef; // ID_INST_BENEF VARCHAR(11),
    private String tipoBenef; // TIPO_BENEF VARCHAR(1),
    private String nombreRazonSocBenef; // NOMBRE_RAZON_SOC_BENEF VARCHAR(150),
    private String cuentaBenef; // CUENTA_BENEF VARCHAR(35),
    private String montoOp;//BigDecimal montoOp; // MONTO_OP NUMBER(16,2),
    private String idDivisa; // ID_DIVISA VARCHAR(3),
    private String observaciones; // OBSERVACIONES VARCHAR(100),
    private String folioTransNacional; // FOLIO_TRANS_NACIONAL VARCHAR(20),
    private String tipoFondeoTrans; // TIPO_FONDEO_TRANS VARCHAR(1),
    private String fondeoTrans; // FONDEO_TRANS VARCHAR(100),
    //private String reportadaBanxico; // REPORTADA_BANXICO CHAR(1)
    private String otroMedio; // OTRO_MEDIO
	private String nacionCliOriginador; //NACION_CLI_ORIGINADOR
    private String tipoIdOriginadorExt; //TIPO_ID_ORIGINADOR_EXT
    private String IdOriginadorExt; //ID_ORIGINADOR_EXT
    private String sexoCliOriginador; //SEXO_CLI_ORIGINADOR
    private String edoNacCliOriginador; //EDO_NAC_CLI_ORIGINADOR
    private String paisBcoBenef; // PAIS_BCO_BENEF

	
	public DatosTransferencia(){}


	public Integer getIdScTraextEnv() {
		return idScTraextEnv;
	}


	public void setIdScTraextEnv(Integer idScTraextEnv) {
		this.idScTraextEnv = idScTraextEnv;
	}


	public String getFechaOperacion() {
		return fechaOperacion;
	}


	public void setFechaOperacion(String fechaOperacion) {//(Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}


	public String getTipoOperacion() {
		return tipoOperacion;
	}


	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}


	public String getTipoTransferencia() {
		return tipoTransferencia;
	}


	public void setTipoTransferencia(String tipoTransferencia) {
		this.tipoTransferencia = tipoTransferencia;
	}


	public String getIdOperacion() {
		return idOperacion;
	}


	public void setIdOperacion(String idOperacion) {
		this.idOperacion = idOperacion;
	}


	public String getMedio() {
		return medio;
	}


	public void setMedio(String medio) {
		this.medio = medio;
	}


	public String getFolio() {
		return folio;
	}


	public void setFolio(String folio) {
		this.folio = folio;
	}


	public String getIdInstReporta() {
		return idInstReporta;
	}


	public void setIdInstReporta(String idInstReporta) {
		this.idInstReporta = idInstReporta;
	}


	public String getIdBcoCliOriginador() {
		return idBcoCliOriginador;
	}


	public void setIdBcoCliOriginador(String idBcoCliOriginador) {
		this.idBcoCliOriginador = idBcoCliOriginador;
	}


	public String getIdCliOriginador() {
		return idCliOriginador;
	}


	public void setIdCliOriginador(String idCliOriginador) {
		this.idCliOriginador = idCliOriginador;
	}


	public String getTipoCliOriginador() {
		return tipoCliOriginador;
	}


	public void setTipoCliOriginador(String tipoCliOriginador) {
		this.tipoCliOriginador = tipoCliOriginador;
	}


	public String getRazonSocialCli() {
		return razonSocialCli;
	}


	public void setRazonSocialCli(String razonSocialCli) {
		this.razonSocialCli = razonSocialCli;
	}


	public String getApPaternoCli() {
		return apPaternoCli;
	}


	public void setApPaternoCli(String apPaternoCli) {
		this.apPaternoCli = apPaternoCli;
	}


	public String getApMaternoCli() {
		return apMaternoCli;
	}


	public void setApMaternoCli(String apMaternoCli) {
		this.apMaternoCli = apMaternoCli;
	}


	public String getApNombreCli() {
		return apNombreCli;
	}


	public void setApNombreCli(String apNombreCli) {
		this.apNombreCli = apNombreCli;
	}


	public String getFechaNac() {
		return fechaNac;
	}


	public void setFechaNac(String fechaNac) {
		this.fechaNac = fechaNac;
	}


	public String getTipoCuentaOrd() {
		return tipoCuentaOrd;
	}


	public void setTipoCuentaOrd(String tipoCuentaOrd) {
		this.tipoCuentaOrd = tipoCuentaOrd;
	}


	public String getCuentaOrd() {
		return cuentaOrd;
	}


	public void setCuentaOrd(String cuentaOrd) {
		this.cuentaOrd = cuentaOrd;
	}


	public String getTipoIdInstRecept() {
		return tipoIdInstRecept;
	}


	public void setTipoIdInstRecept(String tipoIdInstRecept) {
		this.tipoIdInstRecept = tipoIdInstRecept;
	}


	public String getIdInstRecept() {
		return idInstRecept;
	}


	public void setIdInstRecept(String idInstRecept) {
		this.idInstRecept = idInstRecept;
	}


	public String getTipoIdInstBenef() {
		return tipoIdInstBenef;
	}


	public void setTipoIdInstBenef(String tipoIdInstBenef) {
		this.tipoIdInstBenef = tipoIdInstBenef;
	}


	public String getIdInstBenef() {
		return idInstBenef;
	}


	public void setIdInstBenef(String idInstBenef) {
		this.idInstBenef = idInstBenef;
	}


	public String getTipoBenef() {
		return tipoBenef;
	}


	public void setTipoBenef(String tipoBenef) {
		this.tipoBenef = tipoBenef;
	}


	public String getNombreRazonSocBenef() {
		return nombreRazonSocBenef;
	}


	public void setNombreRazonSocBenef(String nombreRazonSocBenef) {
		this.nombreRazonSocBenef = nombreRazonSocBenef;
	}


	public String getCuentaBenef() {
		return cuentaBenef;
	}


	public void setCuentaBenef(String cuentaBenef) {
		this.cuentaBenef = cuentaBenef;
	}


	public String getMontoOp() {
		return montoOp;
	}


	public void setMontoOp(String montoOp) {//(BigDecimal montoOp) {
		this.montoOp = montoOp;
	}


	public String getIdDivisa() {
		return idDivisa;
	}


	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}


	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public String getFolioTransNacional() {
		return folioTransNacional;
	}


	public void setFolioTransNacional(String folioTransNacional) {
		this.folioTransNacional = folioTransNacional;
	}


	public String getTipoFondeoTrans() {
		return tipoFondeoTrans;
	}


	public void setTipoFondeoTrans(String tipoFondeoTrans) {
		this.tipoFondeoTrans = tipoFondeoTrans;
	}


	public String getFondeoTrans() {
		return fondeoTrans;
	}


	public void setFondeoTrans(String fondeoTrans) {
		this.fondeoTrans = fondeoTrans;
	}

    public String getOtroMedio() {
		return otroMedio;
	}


	public void setOtroMedio(String otroMedio) {
		this.otroMedio = otroMedio;
	}


	public String getNacionCliOriginador() {
		return nacionCliOriginador;
	}


	public void setNacionCliOriginador(String nacionCliOriginador) {
		this.nacionCliOriginador = nacionCliOriginador;
	}


	public String getTipoIdOriginadorExt() {
		return tipoIdOriginadorExt;
	}


	public void setTipoIdOriginadorExt(String tipoIdOriginadorExt) {
		this.tipoIdOriginadorExt = tipoIdOriginadorExt;
	}


	public String getIdOriginadorExt() {
		return IdOriginadorExt;
	}


	public void setIdOriginadorExt(String idOriginadorExt) {
		IdOriginadorExt = idOriginadorExt;
	}


	public String getSexoCliOriginador() {
		return sexoCliOriginador;
	}


	public void setSexoCliOriginador(String sexoCliOriginador) {
		this.sexoCliOriginador = sexoCliOriginador;
	}


	public String getEdoNacCliOriginador() {
		return edoNacCliOriginador;
	}


	public void setEdoNacCliOriginador(String edoNacCliOriginador) {
		this.edoNacCliOriginador = edoNacCliOriginador;
	}


	public String getPaisBcoBenef() {
		return paisBcoBenef;
	}


	public void setPaisBcoBenef(String paisBcoBenef) {
		this.paisBcoBenef = paisBcoBenef;
	}

}
