package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import static org.apache.commons.lang.StringUtils.startsWithIgnoreCase;

/**
 *
 * @author IHernandez
 */
@Entity
@Table(name = "SAP_A_GENPOL_XS")
public class SapAGenpolXs implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "FECHADOC")
	@Temporal(TemporalType.DATE)
	private Date fechadoc;
	
	@Column(name = "FECHACONT")
	@Temporal(TemporalType.DATE)
	private Date fechacont;
	
	@Column(name = "CLASEDOC")
	private String clasedoc;
	
	@Column(name = "SOCIEDAD")
	private String sociedad;
	
	@Column(name = "MONEDA")
	private String moneda;
	
	@Column(name = "TIPOCAMBIO")
	private BigDecimal tipocambio;
	
	@Column(name = "PERIODO")
	private Short periodo;
	
	@Column(name = "TEXTOCAB")
	private String textocab;
	
	@Column(name = "CLAVECONTA")
	private Short claveconta;
	
	@Column(name = "CUENTACONTA")
	private Long cuentaconta;
	
	@Column(name = "IMPORTE")
	private BigDecimal importe;
	
	@Column(name = "CENTROCOSTOS")
	private String centrocostos;
	
	@Column(name = "CENTROBENEF")
	private String centrobenef;
	
	@Column(name = "INDIMPUESTO")
	private String indimpuesto;
	
	@Column(name = "IMPORTEBASEIMPUESTO")
	private BigDecimal importebaseimpuesto;
	
	@Column(name = "ASIGNACION")
	private String asignacion;
	
	@Column(name = "TEXTOPOS")
	private String textopos;
	
	@Column(name = "FECHAVALOR")
	@Temporal(TemporalType.DATE)
	private Date fechavalor;
	
	@Column(name = "SOCIEDADASOC")
	private String sociedadasoc;
	
	@Column(name = "SEGMENTO")
	private String segmento;
	
	@Column(name = "USUARIO")
	private String usuario;
	
	@EmbeddedId
    protected SapAGenpolXsPK sapAGenpolXsPK;
	
	@Column(name = "IMPMONLOCAL")
	private BigDecimal impmonlocal;
	
	@Column(name = "IND_ORIGEN")
	private String indOrigen;
	
	@Column(name = "FECHA_PROCESO")
	@Temporal(TemporalType.DATE)
	private Date fechaProceso;
	
	@Column(name = "STATUS")
	private Character status;
	
	@Column(name = "LOTE_CARGA")
	private Long loteCarga;
	
	@Column(name = "CIFRADO")
	private String cifrado;
	
	@Column(name = "PRODUCTO")
	private String producto;

	public SapAGenpolXs() {
	}

	public SapAGenpolXs(SapAGenpolXsPK sapAGenpolXsPK) {
        this.sapAGenpolXsPK = sapAGenpolXsPK;
    }

    public SapAGenpolXs(String referencia, BigDecimal idRegistro) {
        this.sapAGenpolXsPK = new SapAGenpolXsPK(referencia, idRegistro);
	}

	public Date getFechadoc() {
		return fechadoc;
	}

	public void setFechadoc(Date fechadoc) {
		this.fechadoc = fechadoc;
	}

	public Date getFechacont() {
		return fechacont;
	}

	public void setFechacont(Date fechacont) {
		this.fechacont = fechacont;
	}

	public String getClasedoc() {
		return clasedoc;
	}

	public void setClasedoc(String clasedoc) {
		this.clasedoc = clasedoc;
	}

	public String getSociedad() {
		return sociedad;
	}

	public void setSociedad(String sociedad) {
		this.sociedad = sociedad;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public BigDecimal getTipocambio() {
		return tipocambio;
	}

	public void setTipocambio(BigDecimal tipocambio) {
		this.tipocambio = tipocambio;
	}

	public Short getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Short periodo) {
		this.periodo = periodo;
	}

	public String getTextocab() {
		return textocab;
	}

	public void setTextocab(String textocab) {
		this.textocab = textocab;
	}

	public Short getClaveconta() {
		return claveconta;
	}

	public void setClaveconta(Short claveconta) {
		this.claveconta = claveconta;
	}

	public Long getCuentaconta() {
		return cuentaconta;
	}

	public void setCuentaconta(Long cuentaconta) {
		this.cuentaconta = cuentaconta;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public String getCentrocostos() {
		return centrocostos;
	}

	public void setCentrocostos(String centrocostos) {
		this.centrocostos = centrocostos;
	}

	public String getCentrobenef() {
		return centrobenef;
	}

	public void setCentrobenef(String centrobenef) {
		this.centrobenef = centrobenef;
	}

	public String getIndimpuesto() {
		return indimpuesto;
	}

	public void setIndimpuesto(String indimpuesto) {
		this.indimpuesto = indimpuesto;
	}

	public BigDecimal getImportebaseimpuesto() {
		return importebaseimpuesto;
	}

	public void setImportebaseimpuesto(BigDecimal importebaseimpuesto) {
		this.importebaseimpuesto = importebaseimpuesto;
	}

	public String getAsignacion() {
		return asignacion;
	}

	public void setAsignacion(String asignacion) {
		this.asignacion = asignacion;
	}

	public String getTextopos() {
		return textopos;
	}

	public void setTextopos(String textopos) {
		this.textopos = textopos;
	}

	public Date getFechavalor() {
		return fechavalor;
	}

	public void setFechavalor(Date fechavalor) {
		this.fechavalor = fechavalor;
	}

	public String getSociedadasoc() {
		return sociedadasoc;
	}

	public void setSociedadasoc(String sociedadasoc) {
		this.sociedadasoc = sociedadasoc;
	}

	public String getSegmento() {
		return segmento;
	}

	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public SapAGenpolXsPK getSapAGenpolXsPK() {
		return sapAGenpolXsPK;
	}

	public void setSapAGenpolXsPK(SapAGenpolXsPK sapAGenpolXsPK) {
		this.sapAGenpolXsPK = sapAGenpolXsPK;
	}

	public BigDecimal getImpmonlocal() {
		return impmonlocal;
	}

	public void setImpmonlocal(BigDecimal impmonlocal) {
		this.impmonlocal = impmonlocal;
	}

	public String getIndOrigen() {
		return indOrigen;
	}

	public void setIndOrigen(String indOrigen) {
		this.indOrigen = indOrigen;
	}

	public Date getFechaProceso() {
		return fechaProceso;
	}

	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Long getLoteCarga() {
		return loteCarga;
	}

	public void setLoteCarga(Long loteCarga) {
		this.loteCarga = loteCarga;
	}

	public String getCifrado() {
		return cifrado;
	}

	public void setCifrado(String cifrado) {
		this.cifrado = cifrado;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	@Override
	public int hashCode() {
		int hash = 0;
        hash += (sapAGenpolXsPK != null ? sapAGenpolXsPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof SapAGenpolXs)) {
			return false;
		}
		SapAGenpolXs other = (SapAGenpolXs) object;
        if ((this.sapAGenpolXsPK == null && other.sapAGenpolXsPK != null) || 
        		(this.sapAGenpolXsPK != null && !this.sapAGenpolXsPK.equals(
        				other.sapAGenpolXsPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
        return "com.ixe.ods.sica.batch.domain.SapAGenpolXs[ sapAGenpolXsPK=" + 
        								sapAGenpolXsPK + " ]";
	}
	
	public boolean isUtilidad() {
		return startsWithIgnoreCase(textopos, "UTILIDAD");
	}

}
