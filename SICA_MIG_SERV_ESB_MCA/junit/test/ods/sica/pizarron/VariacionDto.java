package test.ods.sica.pizarron;

import java.util.Date;

/**
 * DTO para los registros de SICA_VARIACION
 * 
 * @author Cesar Jeronimo Gomez
 *
 */
public class VariacionDto {
	
	private Integer idVariacion;
	private Date fecha;
	private Double variacionMid;
	private Double variacionVtaSpot;
	private Double variacionVtaSpotAsk;
	private Double mxnEurVariacionVtaSpot;
	private Double mxnEurVariacionVtaSpotAsk;
	private Double cadVariacionVtaSpot;
	private Double cadVariacionVtaSpotAsk;
	private Double chfVariacionVtaSpot;
	private Double chfVariacionVtaSpotAsk;
	private Double eurVariacionVtaSpot;
	private Double eurVariacionVtaSpotAsk;
	private Double gbpVariacionVtaSpot;
	private Double gbpVariacionVtaSpotAsk;
	private Double jpyVariacionVtaSpot;
	private Double jpyVariacionVtaSpotAsk;
	
	public VariacionDto() {
		super();
	}

	public VariacionDto(
		Integer idVariacion, 
		Date fecha, 
		Double variacionMid,
		Double variacionVtaSpot, 
		Double variacionVtaSpotAsk,
		Double mxnEurVariacionVtaSpot, 
		Double mxnEurVariacionVtaSpotAsk,
		Double cadVariacionVtaSpot, 
		Double cadVariacionVtaSpotAsk,
		Double chfVariacionVtaSpot, 
		Double chfVariacionVtaSpotAsk,
		Double eurVariacionVtaSpot, 
		Double eurVariacionVtaSpotAsk,
		Double gbpVariacionVtaSpot, 
		Double gbpVariacionVtaSpotAsk,
		Double jpyVariacionVtaSpot, 
		Double jpyVariacionVtaSpotAsk
	) {
		super();
		this.idVariacion = idVariacion;
		this.fecha = fecha;
		this.variacionMid = variacionMid;
		this.variacionVtaSpot = variacionVtaSpot;
		this.variacionVtaSpotAsk = variacionVtaSpotAsk;
		this.mxnEurVariacionVtaSpot = mxnEurVariacionVtaSpot;
		this.mxnEurVariacionVtaSpotAsk = mxnEurVariacionVtaSpotAsk;
		this.cadVariacionVtaSpot = cadVariacionVtaSpot;
		this.cadVariacionVtaSpotAsk = cadVariacionVtaSpotAsk;
		this.chfVariacionVtaSpot = chfVariacionVtaSpot;
		this.chfVariacionVtaSpotAsk = chfVariacionVtaSpotAsk;
		this.eurVariacionVtaSpot = eurVariacionVtaSpot;
		this.eurVariacionVtaSpotAsk = eurVariacionVtaSpotAsk;
		this.gbpVariacionVtaSpot = gbpVariacionVtaSpot;
		this.gbpVariacionVtaSpotAsk = gbpVariacionVtaSpotAsk;
		this.jpyVariacionVtaSpot = jpyVariacionVtaSpot;
		this.jpyVariacionVtaSpotAsk = jpyVariacionVtaSpotAsk;
	}

	public Integer getIdVariacion() {
		return idVariacion;
	}

	public Date getFecha() {
		return fecha;
	}

	public Double getVariacionMid() {
		return variacionMid;
	}

	public Double getVariacionVtaSpot() {
		return variacionVtaSpot;
	}

	public Double getVariacionVtaSpotAsk() {
		return variacionVtaSpotAsk;
	}

	public Double getMxnEurVariacionVtaSpot() {
		return mxnEurVariacionVtaSpot;
	}

	public Double getMxnEurVariacionVtaSpotAsk() {
		return mxnEurVariacionVtaSpotAsk;
	}

	public Double getCadVariacionVtaSpot() {
		return cadVariacionVtaSpot;
	}

	public Double getCadVariacionVtaSpotAsk() {
		return cadVariacionVtaSpotAsk;
	}

	public Double getChfVariacionVtaSpot() {
		return chfVariacionVtaSpot;
	}

	public Double getChfVariacionVtaSpotAsk() {
		return chfVariacionVtaSpotAsk;
	}

	public Double getEurVariacionVtaSpot() {
		return eurVariacionVtaSpot;
	}

	public Double getEurVariacionVtaSpotAsk() {
		return eurVariacionVtaSpotAsk;
	}

	public Double getGbpVariacionVtaSpot() {
		return gbpVariacionVtaSpot;
	}

	public Double getGbpVariacionVtaSpotAsk() {
		return gbpVariacionVtaSpotAsk;
	}

	public Double getJpyVariacionVtaSpot() {
		return jpyVariacionVtaSpot;
	}

	public Double getJpyVariacionVtaSpotAsk() {
		return jpyVariacionVtaSpotAsk;
	}

	public Object[] toObjectArray() {
		Object[] result = {
			this.fecha,
			this.variacionMid,
			this.variacionVtaSpot,
			this.variacionVtaSpotAsk,
			this.mxnEurVariacionVtaSpot,
			this.mxnEurVariacionVtaSpotAsk,
			this.cadVariacionVtaSpot,
			this.cadVariacionVtaSpotAsk,
			this.chfVariacionVtaSpot,
			this.chfVariacionVtaSpotAsk,
			this.eurVariacionVtaSpot,
			this.eurVariacionVtaSpotAsk,
			this.gbpVariacionVtaSpot,
			this.gbpVariacionVtaSpotAsk,
			this.jpyVariacionVtaSpot,
			this.jpyVariacionVtaSpotAsk
		};
		return result;
	}

}
