package com.banorte.ods.sica.batch.transfint.domain;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import static org.apache.commons.lang.StringUtils.defaultIfEmpty;

/**
 * The Class ScDetalleRepTransfint.
 * 
 * @author i.hernandez.chavez
 */
@Entity
@Table(name = "SC_DETALLE_REP_TRANSFINT")
@SequenceGenerator(name = "ScDetalleRepTransfintSeq", 
					sequenceName = "SC_DETALLE_REP_TRANSFINT_SEQ", allocationSize = 1)
public class ScDetalleRepTransfint implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id detalle rep transfint. */
	@Id
	@Basic(optional = false)
	@Column(name = "ID_DETALLE_REP_TRANSFINT")
	@GeneratedValue(strategy = SEQUENCE, generator = "ScDetalleRepTransfintSeq")
	private Long idDetalleRepTransfint;

	/** The id detalle liquidacion. */
	@Basic(optional = false)
	@Column(name = "ID_DETALLE_LIQUIDACION")
	private Long idDetalleLiquidacion;

	/** The fecha operacion. */
	@Basic(optional = false)
	@Column(name = "FECHA_OPERACION")
	private String fechaOperacion;

	/** The hora operacion. */
	@Basic(optional = false)
	@Column(name = "HORA_OPERACION")
	private String horaOperacion;

	/** The fecha instruccion. */
	@Basic(optional = false)
	@Column(name = "FECHA_INSTRUCCION")
	private String fechaInstruccion;

	/** The hora instruccion. */
	@Basic(optional = false)
	@Column(name = "HORA_INSTRUCCION")
	private String horaInstruccion;

	/** The tipo operacion. */
	@Basic(optional = false)
	@Column(name = "TIPO_OPERACION")
	private short tipoOperacion;

	/** The tipo transferencia. */
	@Basic(optional = false)
	@Column(name = "TIPO_TRANSFERENCIA")
	private short tipoTransferencia;

	/** The id operacion. */
	@Basic(optional = false)
	@Column(name = "ID_OPERACION")
	private String idOperacion;

	/** The medio transfer. */
	@Basic(optional = false)
	@Column(name = "MEDIO_TRANSFER")
	private short medioTransfer;

	/** The otro medio transfer. */
	@Column(name = "OTRO_MEDIO_TRANSFER")
	private String otroMedioTransfer;

	/** The folio medio. */
	@Basic(optional = false)
	@Column(name = "FOLIO_MEDIO")
	private String folioMedio;

	/** The id inst reporta. */
	@Basic(optional = false)
	@Column(name = "ID_INST_REPORTA")
	private String idInstReporta;

	/** The id bco usuario cliente. */
	@Column(name = "ID_BCO_USUARIO_CLIENTE")
	private String idBcoUsuarioCliente;

	/** The id cliente. */
	@Basic(optional = false)
	@Column(name = "ID_CLIENTE")
	private String idCliente;

	/** The tipo cliente. */
	@Basic(optional = false)
	@Column(name = "TIPO_CLIENTE")
	private short tipoCliente;

	/** The razon social cliente. */
	@Column(name = "RAZON_SOCIAL_CLIENTE")
	private String razonSocialCliente;

	/** The ap paterno cliente. */
	@Column(name = "AP_PATERNO_CLIENTE")
	private String apPaternoCliente;

	/** The ap materno cliente. */
	@Column(name = "AP_MATERNO_CLIENTE")
	private String apMaternoCliente;

	/** The nombre cliente. */
	@Column(name = "NOMBRE_CLIENTE")
	private String nombreCliente;

	/** The fecha nac const cliente. */
	@Basic(optional = false)
	@Column(name = "FECHA_NAC_CONST_CLIENTE")
	private String fechaNacConstCliente;

	/** The nacionalidad cliente. */
	@Basic(optional = false)
	@Column(name = "NACIONALIDAD_CLIENTE")
	private String nacionalidadCliente;

	/** The tipo id cliente ext. */
	@Column(name = "TIPO_ID_CLIENTE_EXT")
	private Short tipoIdClienteExt;

	/** The id cliente ext. */
	@Column(name = "ID_CLIENTE_EXT")
	private String idClienteExt;

	/** The sexo cliente. */
	@Column(name = "SEXO_CLIENTE")
	private String sexoCliente;

	/** The estado nac cliente. */
	@Column(name = "ESTADO_NAC_CLIENTE")
	private String estadoNacCliente;

	/** The tipo cuenta cliente. */
	@Basic(optional = false)
	@Column(name = "TIPO_CUENTA_CLIENTE")
	private short tipoCuentaCliente;

	/** The cuenta cliente. */
	@Column(name = "CUENTA_CLIENTE")
	private String cuentaCliente;

	/** The tipo id inst rec ord. */
	@Basic(optional = false)
	@Column(name = "TIPO_ID_INST_REC_ORD")
	private short tipoIdInstRecOrd;

	/** The id inst rec ord. */
	@Basic(optional = false)
	@Column(name = "ID_INST_REC_ORD")
	private String idInstRecOrd;

	/** The tipo id inst ben ord. */
	@Basic(optional = false)
	@Column(name = "TIPO_ID_INST_BEN_ORD")
	private short tipoIdInstBenOrd;

	/** The id inst ben ord. */
	@Basic(optional = false)
	@Column(name = "ID_INST_BEN_ORD")
	private String idInstBenOrd;

	/** The tipo ben ord. */
	@Basic(optional = false)
	@Column(name = "TIPO_BEN_ORD")
	private short tipoBenOrd;

	/** The razon social nom ben ord. */
	@Basic(optional = false)
	@Column(name = "RAZON_SOCIAL_NOM_BEN_ORD")
	private String razonSocialNomBenOrd;

	/** The cuenta ben ord. */
	@Basic(optional = false)
	@Column(name = "CUENTA_BEN_ORD")
	private String cuentaBenOrd;

	/** The pais bco ben ord. */
	@Basic(optional = false)
	@Column(name = "PAIS_BCO_BEN_ORD")
	private String paisBcoBenOrd;

	/** The monto operacion. */
	@Basic(optional = false)
	@Column(name = "MONTO_OPERACION")
	private BigDecimal montoOperacion;

	/** The moneda operacion. */
	@Basic(optional = false)
	@Column(name = "MONEDA_OPERACION")
	private String monedaOperacion;

	/** The proposito transfer. */
	@Column(name = "PROPOSITO_TRANSFER")
	private String propositoTransfer;

	/** The folio transfer nac. */
	@Column(name = "FOLIO_TRANSFER_NAC")
	private String folioTransferNac;

	/** The tipo fondeo transfer. */
	@Column(name = "TIPO_FONDEO_TRANSFER")
	private Short tipoFondeoTransfer;

	/** The fondeo transfer. */
	@Column(name = "FONDEO_TRANSFER")
	private String fondeoTransfer;

	/** The fecha creacion. */
	@Basic(optional = false)
	@Column(name = "FECHA_CREACION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCreacion;

	/** The fecha ult mod. */
	@Column(name = "FECHA_ULT_MOD")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaUltMod;

	/** The id reporte transfint. */
	@JoinColumn(name = "ID_REPORTE_TRANSFINT", referencedColumnName = "ID_REPORTE_TRANSFINT")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private ScReporteTransfint idReporteTransfint;

	/**
	 * Instantiates a new sc detalle rep transfint.
	 */
	public ScDetalleRepTransfint() {
	}

	/**
	 * Instantiates a new sc detalle rep transfint.
	 * 
	 * @param idDetalleRepTransfint
	 *            the id detalle rep transfint
	 */
	public ScDetalleRepTransfint(Long idDetalleRepTransfint) {
		this.idDetalleRepTransfint = idDetalleRepTransfint;
	}

	/**
	 * Instantiates a new sc detalle rep transfint.
	 * 
	 * @param idDetalleRepTransfint
	 *            the id detalle rep transfint
	 * @param idDetalleLiquidacion
	 *            the id detalle liquidacion
	 * @param fechaOperacion
	 *            the fecha operacion
	 * @param horaOperacion
	 *            the hora operacion
	 * @param fechaInstruccion
	 *            the fecha instruccion
	 * @param horaInstruccion
	 *            the hora instruccion
	 * @param tipoOperacion
	 *            the tipo operacion
	 * @param tipoTransferencia
	 *            the tipo transferencia
	 * @param idOperacion
	 *            the id operacion
	 * @param medioTransfer
	 *            the medio transfer
	 * @param folioMedio
	 *            the folio medio
	 * @param idInstReporta
	 *            the id inst reporta
	 * @param idCliente
	 *            the id cliente
	 * @param tipoCliente
	 *            the tipo cliente
	 * @param fechaNacConstCliente
	 *            the fecha nac const cliente
	 * @param nacionalidadCliente
	 *            the nacionalidad cliente
	 * @param tipoCuentaCliente
	 *            the tipo cuenta cliente
	 * @param tipoIdInstRecOrd
	 *            the tipo id inst rec ord
	 * @param idInstRecOrd
	 *            the id inst rec ord
	 * @param tipoIdInstBenOrd
	 *            the tipo id inst ben ord
	 * @param idInstBenOrd
	 *            the id inst ben ord
	 * @param tipoBenOrd
	 *            the tipo ben ord
	 * @param razonSocialNomBenOrd
	 *            the razon social nom ben ord
	 * @param cuentaBenOrd
	 *            the cuenta ben ord
	 * @param paisBcoBenOrd
	 *            the pais bco ben ord
	 * @param montoOperacion
	 *            the monto operacion
	 * @param monedaOperacion
	 *            the moneda operacion
	 * @param fechaCreacion
	 *            the fecha creacion
	 */
	public ScDetalleRepTransfint(Long idDetalleRepTransfint,
			Long idDetalleLiquidacion, String fechaOperacion,
			String horaOperacion, String fechaInstruccion,
			String horaInstruccion, short tipoOperacion,
			short tipoTransferencia, String idOperacion, short medioTransfer,
			String folioMedio, String idInstReporta, String idCliente,
			short tipoCliente, String fechaNacConstCliente,
			String nacionalidadCliente, short tipoCuentaCliente,
			short tipoIdInstRecOrd, String idInstRecOrd,
			short tipoIdInstBenOrd, String idInstBenOrd, short tipoBenOrd,
			String razonSocialNomBenOrd, String cuentaBenOrd,
			String paisBcoBenOrd, BigDecimal montoOperacion,
			String monedaOperacion, Date fechaCreacion) {
		this.idDetalleRepTransfint = idDetalleRepTransfint;
		this.idDetalleLiquidacion = idDetalleLiquidacion;
		this.fechaOperacion = fechaOperacion;
		this.horaOperacion = horaOperacion;
		this.fechaInstruccion = fechaInstruccion;
		this.horaInstruccion = horaInstruccion;
		this.tipoOperacion = tipoOperacion;
		this.tipoTransferencia = tipoTransferencia;
		this.idOperacion = idOperacion;
		this.medioTransfer = medioTransfer;
		this.folioMedio = folioMedio;
		this.idInstReporta = idInstReporta;
		this.idCliente = idCliente;
		this.tipoCliente = tipoCliente;
		this.fechaNacConstCliente = fechaNacConstCliente;
		this.nacionalidadCliente = nacionalidadCliente;
		this.tipoCuentaCliente = tipoCuentaCliente;
		this.tipoIdInstRecOrd = tipoIdInstRecOrd;
		this.idInstRecOrd = idInstRecOrd;
		this.tipoIdInstBenOrd = tipoIdInstBenOrd;
		this.idInstBenOrd = idInstBenOrd;
		this.tipoBenOrd = tipoBenOrd;
		this.razonSocialNomBenOrd = razonSocialNomBenOrd;
		this.cuentaBenOrd = cuentaBenOrd;
		this.paisBcoBenOrd = paisBcoBenOrd;
		this.montoOperacion = montoOperacion;
		this.monedaOperacion = monedaOperacion;
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * Gets the id detalle rep transfint.
	 * 
	 * @return the id detalle rep transfint
	 */
	public Long getIdDetalleRepTransfint() {
		return idDetalleRepTransfint;
	}

	/**
	 * Sets the id detalle rep transfint.
	 * 
	 * @param idDetalleRepTransfint
	 *            the new id detalle rep transfint
	 */
	public void setIdDetalleRepTransfint(Long idDetalleRepTransfint) {
		this.idDetalleRepTransfint = idDetalleRepTransfint;
	}

	/**
	 * Gets the id detalle liquidacion.
	 * 
	 * @return the id detalle liquidacion
	 */
	public Long getIdDetalleLiquidacion() {
		return idDetalleLiquidacion;
	}

	/**
	 * Sets the id detalle liquidacion.
	 * 
	 * @param idDetalleLiquidacion
	 *            the new id detalle liquidacion
	 */
	public void setIdDetalleLiquidacion(Long idDetalleLiquidacion) {
		this.idDetalleLiquidacion = idDetalleLiquidacion;
	}

	/**
	 * Gets the fecha operacion.
	 * 
	 * @return the fecha operacion
	 */
	public String getFechaOperacion() {
		return fechaOperacion;
	}

	/**
	 * Sets the fecha operacion.
	 * 
	 * @param fechaOperacion
	 *            the new fecha operacion
	 */
	public void setFechaOperacion(String fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}

	/**
	 * Gets the hora operacion.
	 * 
	 * @return the hora operacion
	 */
	public String getHoraOperacion() {
		return horaOperacion;
	}

	/**
	 * Sets the hora operacion.
	 * 
	 * @param horaOperacion
	 *            the new hora operacion
	 */
	public void setHoraOperacion(String horaOperacion) {
		this.horaOperacion = horaOperacion;
	}

	/**
	 * Gets the fecha instruccion.
	 * 
	 * @return the fecha instruccion
	 */
	public String getFechaInstruccion() {
		return fechaInstruccion;
	}

	/**
	 * Sets the fecha instruccion.
	 * 
	 * @param fechaInstruccion
	 *            the new fecha instruccion
	 */
	public void setFechaInstruccion(String fechaInstruccion) {
		this.fechaInstruccion = fechaInstruccion;
	}

	/**
	 * Gets the hora instruccion.
	 * 
	 * @return the hora instruccion
	 */
	public String getHoraInstruccion() {
		return horaInstruccion;
	}

	/**
	 * Sets the hora instruccion.
	 * 
	 * @param horaInstruccion
	 *            the new hora instruccion
	 */
	public void setHoraInstruccion(String horaInstruccion) {
		this.horaInstruccion = horaInstruccion;
	}

	/**
	 * Gets the tipo operacion.
	 * 
	 * @return the tipo operacion
	 */
	public short getTipoOperacion() {
		return tipoOperacion;
	}

	/**
	 * Sets the tipo operacion.
	 * 
	 * @param tipoOperacion
	 *            the new tipo operacion
	 */
	public void setTipoOperacion(short tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	/**
	 * Gets the tipo transferencia.
	 * 
	 * @return the tipo transferencia
	 */
	public short getTipoTransferencia() {
		return tipoTransferencia;
	}

	/**
	 * Sets the tipo transferencia.
	 * 
	 * @param tipoTransferencia
	 *            the new tipo transferencia
	 */
	public void setTipoTransferencia(short tipoTransferencia) {
		this.tipoTransferencia = tipoTransferencia;
	}

	/**
	 * Gets the id operacion.
	 * 
	 * @return the id operacion
	 */
	public String getIdOperacion() {
		return idOperacion;
	}

	/**
	 * Sets the id operacion.
	 * 
	 * @param idOperacion
	 *            the new id operacion
	 */
	public void setIdOperacion(String idOperacion) {
		this.idOperacion = idOperacion;
	}

	/**
	 * Gets the medio transfer.
	 * 
	 * @return the medio transfer
	 */
	public short getMedioTransfer() {
		return medioTransfer;
	}

	/**
	 * Sets the medio transfer.
	 * 
	 * @param medioTransfer
	 *            the new medio transfer
	 */
	public void setMedioTransfer(short medioTransfer) {
		this.medioTransfer = medioTransfer;
	}

	/**
	 * Gets the otro medio transfer.
	 * 
	 * @return the otro medio transfer
	 */
	public String getOtroMedioTransfer() {
		return otroMedioTransfer;
	}

	/**
	 * Sets the otro medio transfer.
	 * 
	 * @param otroMedioTransfer
	 *            the new otro medio transfer
	 */
	public void setOtroMedioTransfer(String otroMedioTransfer) {
		this.otroMedioTransfer = otroMedioTransfer;
	}

	/**
	 * Gets the folio medio.
	 * 
	 * @return the folio medio
	 */
	public String getFolioMedio() {
		return folioMedio;
	}

	/**
	 * Sets the folio medio.
	 * 
	 * @param folioMedio
	 *            the new folio medio
	 */
	public void setFolioMedio(String folioMedio) {
		this.folioMedio = folioMedio;
	}

	/**
	 * Gets the id inst reporta.
	 * 
	 * @return the id inst reporta
	 */
	public String getIdInstReporta() {
		return idInstReporta;
	}

	/**
	 * Sets the id inst reporta.
	 * 
	 * @param idInstReporta
	 *            the new id inst reporta
	 */
	public void setIdInstReporta(String idInstReporta) {
		this.idInstReporta = idInstReporta;
	}

	/**
	 * Gets the id bco usuario cliente.
	 * 
	 * @return the id bco usuario cliente
	 */
	public String getIdBcoUsuarioCliente() {
		return idBcoUsuarioCliente;
	}

	/**
	 * Sets the id bco usuario cliente.
	 * 
	 * @param idBcoUsuarioCliente
	 *            the new id bco usuario cliente
	 */
	public void setIdBcoUsuarioCliente(String idBcoUsuarioCliente) {
		this.idBcoUsuarioCliente = idBcoUsuarioCliente;
	}

	/**
	 * Gets the id cliente.
	 * 
	 * @return the id cliente
	 */
	public String getIdCliente() {
		return idCliente;
	}

	/**
	 * Sets the id cliente.
	 * 
	 * @param idCliente
	 *            the new id cliente
	 */
	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	/**
	 * Gets the tipo cliente.
	 * 
	 * @return the tipo cliente
	 */
	public short getTipoCliente() {
		return tipoCliente;
	}

	/**
	 * Sets the tipo cliente.
	 * 
	 * @param tipoCliente
	 *            the new tipo cliente
	 */
	public void setTipoCliente(short tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Gets the razon social cliente.
	 * 
	 * @return the razon social cliente
	 */
	public String getRazonSocialCliente() {
		return razonSocialCliente;
	}

	/**
	 * Sets the razon social cliente.
	 * 
	 * @param razonSocialCliente
	 *            the new razon social cliente
	 */
	public void setRazonSocialCliente(String razonSocialCliente) {
		this.razonSocialCliente = razonSocialCliente;
	}

	/**
	 * Gets the ap paterno cliente.
	 * 
	 * @return the ap paterno cliente
	 */
	public String getApPaternoCliente() {
		return apPaternoCliente;
	}

	/**
	 * Sets the ap paterno cliente.
	 * 
	 * @param apPaternoCliente
	 *            the new ap paterno cliente
	 */
	public void setApPaternoCliente(String apPaternoCliente) {
		this.apPaternoCliente = apPaternoCliente;
	}

	/**
	 * Gets the ap materno cliente.
	 * 
	 * @return the ap materno cliente
	 */
	public String getApMaternoCliente() {
		return apMaternoCliente;
	}

	/**
	 * Sets the ap materno cliente.
	 * 
	 * @param apMaternoCliente
	 *            the new ap materno cliente
	 */
	public void setApMaternoCliente(String apMaternoCliente) {
		this.apMaternoCliente = apMaternoCliente;
	}

	/**
	 * Gets the nombre cliente.
	 * 
	 * @return the nombre cliente
	 */
	public String getNombreCliente() {
		return nombreCliente;
	}

	/**
	 * Sets the nombre cliente.
	 * 
	 * @param nombreCliente
	 *            the new nombre cliente
	 */
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	/**
	 * Gets the fecha nac const cliente.
	 * 
	 * @return the fecha nac const cliente
	 */
	public String getFechaNacConstCliente() {
		return fechaNacConstCliente;
	}

	/**
	 * Sets the fecha nac const cliente.
	 * 
	 * @param fechaNacConstCliente
	 *            the new fecha nac const cliente
	 */
	public void setFechaNacConstCliente(String fechaNacConstCliente) {
		this.fechaNacConstCliente = fechaNacConstCliente;
	}

	/**
	 * Gets the nacionalidad cliente.
	 * 
	 * @return the nacionalidad cliente
	 */
	public String getNacionalidadCliente() {
		return nacionalidadCliente;
	}

	/**
	 * Sets the nacionalidad cliente.
	 * 
	 * @param nacionalidadCliente
	 *            the new nacionalidad cliente
	 */
	public void setNacionalidadCliente(String nacionalidadCliente) {
		this.nacionalidadCliente = nacionalidadCliente;
	}

	/**
	 * Gets the tipo id cliente ext.
	 * 
	 * @return the tipo id cliente ext
	 */
	public Short getTipoIdClienteExt() {
		return tipoIdClienteExt;
	}

	/**
	 * Sets the tipo id cliente ext.
	 * 
	 * @param tipoIdClienteExt
	 *            the new tipo id cliente ext
	 */
	public void setTipoIdClienteExt(Short tipoIdClienteExt) {
		this.tipoIdClienteExt = tipoIdClienteExt;
	}

	/**
	 * Gets the id cliente ext.
	 * 
	 * @return the id cliente ext
	 */
	public String getIdClienteExt() {
		return idClienteExt;
	}

	/**
	 * Sets the id cliente ext.
	 * 
	 * @param idClienteExt
	 *            the new id cliente ext
	 */
	public void setIdClienteExt(String idClienteExt) {
		this.idClienteExt = idClienteExt;
	}

	/**
	 * Gets the sexo cliente.
	 * 
	 * @return the sexo cliente
	 */
	public String getSexoCliente() {
		return sexoCliente;
	}

	/**
	 * Sets the sexo cliente.
	 * 
	 * @param sexoCliente
	 *            the new sexo cliente
	 */
	public void setSexoCliente(String sexoCliente) {
		this.sexoCliente = sexoCliente;
	}

	/**
	 * Gets the estado nac cliente.
	 * 
	 * @return the estado nac cliente
	 */
	public String getEstadoNacCliente() {
		return estadoNacCliente;
	}

	/**
	 * Sets the estado nac cliente.
	 * 
	 * @param estadoNacCliente
	 *            the new estado nac cliente
	 */
	public void setEstadoNacCliente(String estadoNacCliente) {
		this.estadoNacCliente = estadoNacCliente;
	}

	/**
	 * Gets the tipo cuenta cliente.
	 * 
	 * @return the tipo cuenta cliente
	 */
	public short getTipoCuentaCliente() {
		return tipoCuentaCliente;
	}

	/**
	 * Sets the tipo cuenta cliente.
	 * 
	 * @param tipoCuentaCliente
	 *            the new tipo cuenta cliente
	 */
	public void setTipoCuentaCliente(short tipoCuentaCliente) {
		this.tipoCuentaCliente = tipoCuentaCliente;
	}

	/**
	 * Gets the cuenta cliente.
	 * 
	 * @return the cuenta cliente
	 */
	public String getCuentaCliente() {
		return cuentaCliente;
	}

	/**
	 * Sets the cuenta cliente.
	 * 
	 * @param cuentaCliente
	 *            the new cuenta cliente
	 */
	public void setCuentaCliente(String cuentaCliente) {
		this.cuentaCliente = cuentaCliente;
	}

	/**
	 * Gets the tipo id inst rec ord.
	 * 
	 * @return the tipo id inst rec ord
	 */
	public short getTipoIdInstRecOrd() {
		return tipoIdInstRecOrd;
	}

	/**
	 * Sets the tipo id inst rec ord.
	 * 
	 * @param tipoIdInstRecOrd
	 *            the new tipo id inst rec ord
	 */
	public void setTipoIdInstRecOrd(short tipoIdInstRecOrd) {
		this.tipoIdInstRecOrd = tipoIdInstRecOrd;
	}

	/**
	 * Gets the id inst rec ord.
	 * 
	 * @return the id inst rec ord
	 */
	public String getIdInstRecOrd() {
		return idInstRecOrd;
	}

	/**
	 * Sets the id inst rec ord.
	 * 
	 * @param idInstRecOrd
	 *            the new id inst rec ord
	 */
	public void setIdInstRecOrd(String idInstRecOrd) {
		this.idInstRecOrd = idInstRecOrd;
	}

	/**
	 * Gets the tipo id inst ben ord.
	 * 
	 * @return the tipo id inst ben ord
	 */
	public short getTipoIdInstBenOrd() {
		return tipoIdInstBenOrd;
	}

	/**
	 * Sets the tipo id inst ben ord.
	 * 
	 * @param tipoIdInstBenOrd
	 *            the new tipo id inst ben ord
	 */
	public void setTipoIdInstBenOrd(short tipoIdInstBenOrd) {
		this.tipoIdInstBenOrd = tipoIdInstBenOrd;
	}

	/**
	 * Gets the id inst ben ord.
	 * 
	 * @return the id inst ben ord
	 */
	public String getIdInstBenOrd() {
		return idInstBenOrd;
	}

	/**
	 * Sets the id inst ben ord.
	 * 
	 * @param idInstBenOrd
	 *            the new id inst ben ord
	 */
	public void setIdInstBenOrd(String idInstBenOrd) {
		this.idInstBenOrd = idInstBenOrd;
	}

	/**
	 * Gets the tipo ben ord.
	 * 
	 * @return the tipo ben ord
	 */
	public short getTipoBenOrd() {
		return tipoBenOrd;
	}

	/**
	 * Sets the tipo ben ord.
	 * 
	 * @param tipoBenOrd
	 *            the new tipo ben ord
	 */
	public void setTipoBenOrd(short tipoBenOrd) {
		this.tipoBenOrd = tipoBenOrd;
	}

	/**
	 * Gets the razon social nom ben ord.
	 * 
	 * @return the razon social nom ben ord
	 */
	public String getRazonSocialNomBenOrd() {
		return razonSocialNomBenOrd;
	}

	/**
	 * Sets the razon social nom ben ord.
	 * 
	 * @param razonSocialNomBenOrd
	 *            the new razon social nom ben ord
	 */
	public void setRazonSocialNomBenOrd(String razonSocialNomBenOrd) {
		this.razonSocialNomBenOrd = razonSocialNomBenOrd;
	}

	/**
	 * Gets the cuenta ben ord.
	 * 
	 * @return the cuenta ben ord
	 */
	public String getCuentaBenOrd() {
		return cuentaBenOrd;
	}

	/**
	 * Sets the cuenta ben ord.
	 * 
	 * @param cuentaBenOrd
	 *            the new cuenta ben ord
	 */
	public void setCuentaBenOrd(String cuentaBenOrd) {
		this.cuentaBenOrd = cuentaBenOrd;
	}

	/**
	 * Gets the pais bco ben ord.
	 * 
	 * @return the pais bco ben ord
	 */
	public String getPaisBcoBenOrd() {
		return paisBcoBenOrd;
	}

	/**
	 * Sets the pais bco ben ord.
	 * 
	 * @param paisBcoBenOrd
	 *            the new pais bco ben ord
	 */
	public void setPaisBcoBenOrd(String paisBcoBenOrd) {
		this.paisBcoBenOrd = paisBcoBenOrd;
	}

	/**
	 * Gets the monto operacion.
	 * 
	 * @return the monto operacion
	 */
	public BigDecimal getMontoOperacion() {
		return montoOperacion;
	}

	/**
	 * Sets the monto operacion.
	 * 
	 * @param montoOperacion
	 *            the new monto operacion
	 */
	public void setMontoOperacion(BigDecimal montoOperacion) {
		this.montoOperacion = montoOperacion;
	}

	/**
	 * Gets the moneda operacion.
	 * 
	 * @return the moneda operacion
	 */
	public String getMonedaOperacion() {
		return monedaOperacion;
	}

	/**
	 * Sets the moneda operacion.
	 * 
	 * @param monedaOperacion
	 *            the new moneda operacion
	 */
	public void setMonedaOperacion(String monedaOperacion) {
		this.monedaOperacion = monedaOperacion;
	}

	/**
	 * Gets the proposito transfer.
	 * 
	 * @return the proposito transfer
	 */
	public String getPropositoTransfer() {
		return propositoTransfer;
	}

	/**
	 * Sets the proposito transfer.
	 * 
	 * @param propositoTransfer
	 *            the new proposito transfer
	 */
	public void setPropositoTransfer(String propositoTransfer) {
		this.propositoTransfer = propositoTransfer;
	}

	/**
	 * Gets the folio transfer nac.
	 * 
	 * @return the folio transfer nac
	 */
	public String getFolioTransferNac() {
		return folioTransferNac;
	}

	/**
	 * Sets the folio transfer nac.
	 * 
	 * @param folioTransferNac
	 *            the new folio transfer nac
	 */
	public void setFolioTransferNac(String folioTransferNac) {
		this.folioTransferNac = folioTransferNac;
	}

	/**
	 * Gets the tipo fondeo transfer.
	 * 
	 * @return the tipo fondeo transfer
	 */
	public Short getTipoFondeoTransfer() {
		return tipoFondeoTransfer;
	}

	/**
	 * Sets the tipo fondeo transfer.
	 * 
	 * @param tipoFondeoTransfer
	 *            the new tipo fondeo transfer
	 */
	public void setTipoFondeoTransfer(Short tipoFondeoTransfer) {
		this.tipoFondeoTransfer = tipoFondeoTransfer;
	}

	/**
	 * Gets the fondeo transfer.
	 * 
	 * @return the fondeo transfer
	 */
	public String getFondeoTransfer() {
		return fondeoTransfer;
	}

	/**
	 * Sets the fondeo transfer.
	 * 
	 * @param fondeoTransfer
	 *            the new fondeo transfer
	 */
	public void setFondeoTransfer(String fondeoTransfer) {
		this.fondeoTransfer = fondeoTransfer;
	}

	/**
	 * Gets the fecha creacion.
	 * 
	 * @return the fecha creacion
	 */
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * Sets the fecha creacion.
	 * 
	 * @param fechaCreacion
	 *            the new fecha creacion
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * Gets the fecha ult mod.
	 * 
	 * @return the fecha ult mod
	 */
	public Date getFechaUltMod() {
		return fechaUltMod;
	}

	/**
	 * Sets the fecha ult mod.
	 * 
	 * @param fechaUltMod
	 *            the new fecha ult mod
	 */
	public void setFechaUltMod(Date fechaUltMod) {
		this.fechaUltMod = fechaUltMod;
	}

	/**
	 * Gets the id reporte transfint.
	 * 
	 * @return the id reporte transfint
	 */
	public ScReporteTransfint getIdReporteTransfint() {
		return idReporteTransfint;
	}

	/**
	 * Sets the id reporte transfint.
	 * 
	 * @param idReporteTransfint
	 *            the new id reporte transfint
	 */
	public void setIdReporteTransfint(ScReporteTransfint idReporteTransfint) {
		this.idReporteTransfint = idReporteTransfint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idDetalleRepTransfint != null ? idDetalleRepTransfint
				.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ScDetalleRepTransfint)) {
			return false;
		}
		ScDetalleRepTransfint other = (ScDetalleRepTransfint) object;
		if ((this.idDetalleRepTransfint == null && other.idDetalleRepTransfint != null)
				|| (this.idDetalleRepTransfint != null && !this.idDetalleRepTransfint
						.equals(other.idDetalleRepTransfint))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return defaultIfEmpty(this.fechaOperacion, "") + "|" +
				defaultIfEmpty(this.horaOperacion, "") + "|" +
				defaultIfEmpty(this.fechaInstruccion, "") + "|" +
				defaultIfEmpty(this.horaInstruccion, "") + "|" +
				(this.tipoOperacion > Short.parseShort("0") ? 
						String.valueOf(this.tipoOperacion) : "") + "|" +
				(this.tipoTransferencia > Short.parseShort("0") ? 
						String.valueOf(this.tipoTransferencia) : "") + "|" +
				defaultIfEmpty(this.idOperacion, "") + "|" +
				(this.medioTransfer > Short.parseShort("0") ? 
						String.valueOf(this.medioTransfer) : "") + "|" +
				defaultIfEmpty(this.otroMedioTransfer, "") + "|" +
				defaultIfEmpty(this.folioMedio, "") + "|" +
				defaultIfEmpty(this.idInstReporta, "") + "|" +
				defaultIfEmpty(this.idBcoUsuarioCliente, "") + "|" +
				defaultIfEmpty(this.idCliente, "") + "|" +
				(this.tipoCliente > Short.parseShort("0") ? 
						String.valueOf(this.tipoCliente) : "") + "|" +
				defaultIfEmpty(this.razonSocialCliente, "") + "|" +
				defaultIfEmpty(this.apPaternoCliente, "") + "|" +
				defaultIfEmpty(this.apMaternoCliente, "") + "|" +
				defaultIfEmpty(this.nombreCliente, "") + "|" +
				defaultIfEmpty(this.fechaNacConstCliente, "") + "|" +				
				defaultIfEmpty(this.nacionalidadCliente, "") + "|" +
				(this.tipoIdClienteExt != null && (this.tipoIdClienteExt > Short.parseShort("0")) ? 
						String.valueOf(this.tipoIdClienteExt) : "") + "|" +
				defaultIfEmpty(this.idClienteExt, "") + "|" +
				defaultIfEmpty(this.sexoCliente, "") + "|" +
				defaultIfEmpty(this.estadoNacCliente, "") + "|" +
				(this.tipoCuentaCliente > Short.parseShort("0") ? 
						String.valueOf(this.tipoCuentaCliente) : "") + "|" +
				defaultIfEmpty(this.cuentaCliente, "") + "|" +
				(this.tipoIdInstRecOrd > Short.parseShort("0") ? 
						String.valueOf(this.tipoIdInstRecOrd) : "") + "|" +
				defaultIfEmpty(this.idInstRecOrd, "") + "|" +
				(this.tipoIdInstBenOrd > Short.parseShort("0") ? 
						String.valueOf(this.tipoIdInstBenOrd) : "") + "|" +
				defaultIfEmpty(this.idInstBenOrd, "") + "|" +
				(this.tipoBenOrd > Short.parseShort("0") ? 
						String.valueOf(this.tipoBenOrd) : "") + "|" +
				defaultIfEmpty(this.razonSocialNomBenOrd, "") + "|" +
				defaultIfEmpty(this.cuentaBenOrd, "") + "|" +
				defaultIfEmpty(this.paisBcoBenOrd, "") + "|" +
				String.valueOf(montoOperacion) + "|" +
				defaultIfEmpty(this.monedaOperacion, "") + "|" +
				defaultIfEmpty(this.propositoTransfer, "") + "|" +
				defaultIfEmpty(this.folioTransferNac, "") + "|" +
				(this.tipoFondeoTransfer != null && (this.tipoFondeoTransfer > Short.parseShort("0")) ? 
						String.valueOf(this.tipoFondeoTransfer) : "") + "|" +
				defaultIfEmpty(this.fondeoTransfer, "") + "|";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	//@Override
	public String toStringCopia() {
		return defaultIfEmpty(this.fechaOperacion, "") + "|" +
				defaultIfEmpty(this.horaOperacion, "") + "|" +
				defaultIfEmpty(this.fechaInstruccion, "") + "|" +
				defaultIfEmpty(this.horaInstruccion, "") + "|" +
				(this.tipoOperacion > Short.parseShort("0") ? 
						String.valueOf(this.tipoOperacion) : "") + "|" +
				(this.tipoTransferencia > Short.parseShort("0") ? 
						String.valueOf(this.tipoTransferencia) : "") + "|" +
				defaultIfEmpty(this.idOperacion, "") + "|" +
				(this.medioTransfer > Short.parseShort("0") ? 
						String.valueOf(this.medioTransfer) : "") + "|" +
				defaultIfEmpty(this.otroMedioTransfer, "") + "|" +
				defaultIfEmpty(this.folioMedio, "") + "|" +
				defaultIfEmpty(this.idInstReporta, "") + "|" +
				defaultIfEmpty(this.idBcoUsuarioCliente, "") + "|" +
				defaultIfEmpty(this.idCliente, "") + "|" +
				(this.tipoCliente > Short.parseShort("0") ? 
						String.valueOf(this.tipoCliente) : "") + "|" +
				defaultIfEmpty(this.razonSocialCliente, "") + "|" +
				defaultIfEmpty(this.apPaternoCliente, "") + "|" +
				defaultIfEmpty(this.apMaternoCliente, "") + "|" +
				defaultIfEmpty(this.nombreCliente, "") + "|" +
				defaultIfEmpty(this.fechaNacConstCliente, "") + "|" +				
				defaultIfEmpty(this.nacionalidadCliente, "") + "|" +
				(this.tipoIdClienteExt != null && (this.tipoIdClienteExt > Short.parseShort("0")) ? 
						String.valueOf(this.tipoIdClienteExt) : "") + "|" +
				defaultIfEmpty(this.idClienteExt, "") + "|" +
				defaultIfEmpty(this.sexoCliente, "") + "|" +
				defaultIfEmpty(this.estadoNacCliente, "") + "|" +
				(this.tipoCuentaCliente > Short.parseShort("0") ? 
						String.valueOf(this.tipoCuentaCliente) : "") + "|" +
				defaultIfEmpty(this.cuentaCliente, "") + "|" +
				(this.tipoIdInstRecOrd > Short.parseShort("0") ? 
						String.valueOf(this.tipoIdInstRecOrd) : "") + "|" +
				defaultIfEmpty(this.idInstRecOrd, "") + "|" +
				(this.tipoIdInstBenOrd > Short.parseShort("0") ? 
						String.valueOf(this.tipoIdInstBenOrd) : "") + "|" +
				defaultIfEmpty(this.idInstBenOrd, "") + "|" +
				(this.tipoBenOrd > Short.parseShort("0") ? 
						String.valueOf(this.tipoBenOrd) : "") + "|" +
				defaultIfEmpty(this.razonSocialNomBenOrd, "") + "|" +
				defaultIfEmpty(this.cuentaBenOrd, "") + "|" +
				defaultIfEmpty(this.paisBcoBenOrd, "") + "|" +
				String.valueOf(montoOperacion) + "|" +
				defaultIfEmpty(this.monedaOperacion, "") + "|" +
				defaultIfEmpty(this.propositoTransfer, "") + "|" +
				defaultIfEmpty(this.folioTransferNac, "") + "|" +
				(this.tipoFondeoTransfer != null && (this.tipoFondeoTransfer > Short.parseShort("0")) ? 
						String.valueOf(this.tipoFondeoTransfer) : "") + "|" +
				defaultIfEmpty(this.fondeoTransfer, "") + "|" +
				(this.idDetalleLiquidacion) + "|";
	}


}
