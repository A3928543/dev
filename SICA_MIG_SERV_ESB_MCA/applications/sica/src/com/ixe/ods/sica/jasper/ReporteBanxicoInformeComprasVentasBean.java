/*
 * $Id: ReporteBanxicoInformeComprasVentasBean.java,v 1.5 2008/02/22 18:25:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.jasper;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;

/**
 * Bean que permite probar los reportes para Banxico.
 *
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.5 $ $Date: 2008/02/22 18:25:04 $
 */
public class ReporteBanxicoInformeComprasVentasBean implements Serializable {


	public ReporteBanxicoInformeComprasVentasBean(Boolean isRecibimos, String idDivisa,
			String sector, Double sectorCash, Double sectorTom, Double sectorSpot, 
			Double sector72Hr, Double sectorVFut, Double swap,Double montoForward,
			Double montoResmerca, String resmercaString, Double posicionInicial,
			Double posicionInicialCash, Double posicionInicialTom,
			Double posicionInicialSpot, Double posicionInicial72Hr,
			Double posicionInicialVFut, Double posicionFinal,
			Double posicionFinalCash, Double posicionFinalTom,
			Double posicionFinalSpot, Double posicionFinal72Hr,
			Double posicionFinalVFut, Double tipoCambioCompra,
			Double tipoCambioCompraCash, Double tipoCambioCompraTom,
			Double tipoCambioCompraSpot, Double tipoCambioCompra72Hr,
			Double tipoCambioCompraVFut, Double tipoCambioVenta,
			Double tipoCambioVentaCash, Double tipoCambioVentaTom,
			Double tipoCambioVentaSpot, Double tipoCambioVenta72Hr,
			Double tipoCambioVentaVFut, Boolean isForward, Boolean isSectorResmerca,
			Double resmercaCompras, Double resmercaVentas,
			String resmercaComprasString, String resmercaVentasString,
			Double forwardEstra, Boolean isForwardEstra, Double forwardEstraCompras,
			Double forwardEstraVentas, Double posicionFinalResmerca, URL image,
			Double fwEstraSectores) {
		super();
		this.isRecibimos = isRecibimos;
		this.idDivisa = idDivisa;
		this.sector = sector;
		this.sectorCash = sectorCash;
		this.sectorSpot = sectorSpot;
		this.sectorTom = sectorTom;
		this.sector72Hr = sector72Hr;
		this.sectorVFut = sectorVFut;
		this.swap = swap;
		this.montoForward = montoForward;
		this.montoResmerca = montoResmerca;
		this.resmercaString = resmercaString;
		this.posicionInicial = posicionInicial;
		this.posicionInicialCash = posicionInicialCash;
		this.posicionInicialTom = posicionInicialTom;
		this.posicionInicialSpot = posicionInicialSpot;
		this.posicionInicial72Hr = posicionInicial72Hr;
		this.posicionInicialVFut = posicionInicialVFut;
		this.posicionFinal = posicionFinal;
		this.posicionFinalCash = posicionFinalCash;
		this.posicionFinalTom = posicionFinalTom;
		this.posicionFinalSpot = posicionFinalSpot;
		this.posicionFinal72Hr = posicionFinal72Hr;
		this.posicionFinalVFut = posicionFinalVFut;
		this.tipoCambioCompra = tipoCambioCompra;
		this.tipoCambioCompraCash = tipoCambioCompraCash;
		this.tipoCambioCompraTom = tipoCambioCompraTom;
		this.tipoCambioCompraSpot = tipoCambioCompraSpot;
		this.tipoCambioCompra72Hr = tipoCambioCompra72Hr;
		this.tipoCambioCompraVFut = tipoCambioCompraVFut;
		this.tipoCambioVenta = tipoCambioVenta;
		this.tipoCambioVentaCash = tipoCambioVentaCash;
		this.tipoCambioVentaTom = tipoCambioVentaTom;
		this.tipoCambioVentaSpot = tipoCambioVentaSpot;
		this.tipoCambioVenta72Hr = tipoCambioVenta72Hr;
		this.tipoCambioVentaVFut = tipoCambioVentaVFut;
		this.isForward = isForward;
		this.isSectorResmerca = isSectorResmerca;
		this.resmercaCompras = resmercaCompras;
		this.remercaVentas = resmercaVentas;
		this.resmercaComprasString = resmercaComprasString;
		this.remercaVentasString = resmercaVentasString;
		this.montoForwardEstra = forwardEstra;
		this.montoForwardEstraCompras = forwardEstraCompras;
		this.montoForwardEstraVentas = forwardEstraVentas;
		this.isForwardEstra = isForwardEstra;
		this.posicionFinalResmerca = posicionFinalResmerca;
		this.image = image;
		this.montoFwEstraSectores = fwEstraSectores;
	}

	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getIdDivisa() {
		return idDivisa;
	}
	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}
	public Boolean getIsForward() {
		return isForward;
	}
	public void setIsForward(Boolean isForward) {
		this.isForward = isForward;
	}
	public Boolean getIsRecibimos() {
		return isRecibimos;
	}
	public void setIsRecibimos(Boolean isRecibimos) {
		this.isRecibimos = isRecibimos;
	}
	public Boolean getIsSectorResmerca() {
		return isSectorResmerca;
	}
	public void setIsSectorResmerca(Boolean isSectorResmerca) {
		this.isSectorResmerca = isSectorResmerca;
	}
	public Double getMontoForward() {
		return montoForward;
	}
	public void setMontoForward(Double montoForward) {
		this.montoForward = montoForward;
	}
	public Double getMontoResmerca() {
		return montoResmerca;
	}
	public void setMontoResmerca(Double montoResmerca) {
		this.montoResmerca = montoResmerca;
	}
	public Double getPosicionFinal() {
		return posicionFinal;
	}
	public void setPosicionFinal(Double posicionFinal) {
		this.posicionFinal = posicionFinal;
	}
	public Double getPosicionFinal72Hr() {
		return posicionFinal72Hr;
	}
	public void setPosicionFinal72Hr(Double posicionFinal72Hr) {
		this.posicionFinal72Hr = posicionFinal72Hr;
	}
	public Double getPosicionFinalCash() {
		return posicionFinalCash;
	}
	public void setPosicionFinalCash(Double posicionFinalCash) {
		this.posicionFinalCash = posicionFinalCash;
	}
	public Double getPosicionFinalSpot() {
		return posicionFinalSpot;
	}
	public void setPosicionFinalSpot(Double posicionFinalSpot) {
		this.posicionFinalSpot = posicionFinalSpot;
	}
	public Double getPosicionFinalTom() {
		return posicionFinalTom;
	}
	public void setPosicionFinalTom(Double posicionFinalTom) {
		this.posicionFinalTom = posicionFinalTom;
	}
	public Double getPosicionFinalVFut() {
		return posicionFinalVFut;
	}
	public void setPosicionFinalVFut(Double posicionFinalVFut) {
		this.posicionFinalVFut = posicionFinalVFut;
	}
	public Double getPosicionInicial() {
		return posicionInicial;
	}
	public void setPosicionInicial(Double posicionInicial) {
		this.posicionInicial = posicionInicial;
	}
	public Double getPosicionInicial72Hr() {
		return posicionInicial72Hr;
	}
	public void setPosicionInicial72Hr(Double posicionInicial72Hr) {
		this.posicionInicial72Hr = posicionInicial72Hr;
	}
	public Double getPosicionInicialCash() {
		return posicionInicialCash;
	}
	public void setPosicionInicialCash(Double posicionInicialCash) {
		this.posicionInicialCash = posicionInicialCash;
	}
	public Double getPosicionInicialSpot() {
		return posicionInicialSpot;
	}
	public void setPosicionInicialSpot(Double posicionInicialSpot) {
		this.posicionInicialSpot = posicionInicialSpot;
	}
	public Double getPosicionInicialTom() {
		return posicionInicialTom;
	}
	public void setPosicionInicialTom(Double posicionInicialTom) {
		this.posicionInicialTom = posicionInicialTom;
	}
	public Double getPosicionInicialVFut() {
		return posicionInicialVFut;
	}
	public void setPosicionInicialVFut(Double posicionInicialVFut) {
		this.posicionInicialVFut = posicionInicialVFut;
	}
	public String getResmercaString() {
		return resmercaString;
	}
	public void setResmercaString(String resmercaString) {
		this.resmercaString = resmercaString;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public Double getSector72Hr() {
		return sector72Hr;
	}
	public void setSector72Hr(Double sector72Hr) {
		this.sector72Hr = sector72Hr;
	}
	public Double getSectorCash() {
		return sectorCash;
	}
	public void setSectorCash(Double sectorCash) {
		this.sectorCash = sectorCash;
	}
	public Double getSectorSpot() {
		return sectorSpot;
	}
	public void setSectorSpot(Double sectorSpot) {
		this.sectorSpot = sectorSpot;
	}
	public Double getSectorTom() {
		return sectorTom;
	}
	public void setSectorTom(Double sectorTom) {
		this.sectorTom = sectorTom;
	}
	public Double getSectorVFut() {
		return sectorVFut;
	}
	public void setSectorVFut(Double sectorVFut) {
		this.sectorVFut = sectorVFut;
	}
	public Double getSwap() {
		return swap;
	}
	public void setSwap(Double swap) {
		this.swap = swap;
	}
	public Double getSwapMn() {
		return swapMn;
	}
	public void setSwapMn(Double swapMn) {
		this.swapMn = swapMn;
	}
	public Double getTipoCambioCompra() {
		return tipoCambioCompra;
	}
	public void setTipoCambioCompra(Double tipoCambioCompra) {
		this.tipoCambioCompra = tipoCambioCompra;
	}
	public Double getTipoCambioCompra72Hr() {
		return tipoCambioCompra72Hr;
	}
	public void setTipoCambioCompra72Hr(Double tipoCambioCompra72Hr) {
		this.tipoCambioCompra72Hr = tipoCambioCompra72Hr;
	}
	public Double getTipoCambioCompraCash() {
		return tipoCambioCompraCash;
	}
	public void setTipoCambioCompraCash(Double tipoCambioCompraCash) {
		this.tipoCambioCompraCash = tipoCambioCompraCash;
	}
	public Double getTipoCambioCompraSpot() {
		return tipoCambioCompraSpot;
	}
	public void setTipoCambioCompraSpot(Double tipoCambioCompraSpot) {
		this.tipoCambioCompraSpot = tipoCambioCompraSpot;
	}
	public Double getTipoCambioCompraTom() {
		return tipoCambioCompraTom;
	}
	public void setTipoCambioCompraTom(Double tipoCambioCompraTom) {
		this.tipoCambioCompraTom = tipoCambioCompraTom;
	}
	public Double getTipoCambioCompraVFut() {
		return tipoCambioCompraVFut;
	}
	public void setTipoCambioCompraVFut(Double tipoCambioCompraVFut) {
		this.tipoCambioCompraVFut = tipoCambioCompraVFut;
	}
	public Double getTipoCambioVenta() {
		return tipoCambioVenta;
	}
	public void setTipoCambioVenta(Double tipoCambioVenta) {
		this.tipoCambioVenta = tipoCambioVenta;
	}
	public Double getTipoCambioVenta72Hr() {
		return tipoCambioVenta72Hr;
	}
	public void setTipoCambioVenta72Hr(Double tipoCambioVenta72Hr) {
		this.tipoCambioVenta72Hr = tipoCambioVenta72Hr;
	}
	public Double getTipoCambioVentaCash() {
		return tipoCambioVentaCash;
	}
	public void setTipoCambioVentaCash(Double tipoCambioVentaCash) {
		this.tipoCambioVentaCash = tipoCambioVentaCash;
	}
	public Double getTipoCambioVentaSpot() {
		return tipoCambioVentaSpot;
	}
	public void setTipoCambioVentaSpot(Double tipoCambioVentaSpot) {
		this.tipoCambioVentaSpot = tipoCambioVentaSpot;
	}
	public Double getTipoCambioVentaTom() {
		return tipoCambioVentaTom;
	}
	public void setTipoCambioVentaTom(Double tipoCambioVentaTom) {
		this.tipoCambioVentaTom = tipoCambioVentaTom;
	}
	public Double getTipoCambioVentaVFut() {
		return tipoCambioVentaVFut;
	}
	public void setTipoCambioVentaVFut(Double tipoCambioVentaVFut) {
		this.tipoCambioVentaVFut = tipoCambioVentaVFut;
	}
	public Double getRemercaVentas() {
		return remercaVentas;
	}

	public void setRemercaVentas(Double remercaVentas) {
		this.remercaVentas = remercaVentas;
	}

	public Double getResmercaCompras() {
		return resmercaCompras;
	}

	public void setResmercaCompras(Double resmercaCompras) {
		this.resmercaCompras = resmercaCompras;
	}
	
	public String getRemercaVentasString() {
		return remercaVentasString;
	}

	public void setRemercaVentasString(String remercaVentasString) {
		this.remercaVentasString = remercaVentasString;
	}

	public String getResmercaComprasString() {
		return resmercaComprasString;
	}

	public void setResmercaComprasString(String resmercaComprasString) {
		this.resmercaComprasString = resmercaComprasString;
	}
	
	public Boolean getIsForwardEstra() {
		return isForwardEstra;
	}

	public void setIsForwardEstra(Boolean isForwardEstra) {
		this.isForwardEstra = isForwardEstra;
	}

	public Double getMontoForwardEstra() {
		return montoForwardEstra;
	}

	public void setMontoForwardEstra(Double montoForwardEstra) {
		this.montoForwardEstra = montoForwardEstra;
	}
	
	public Double getMontoForwardEstraCompras() {
		return montoForwardEstraCompras;
	}

	public void setMontoForwardEstraCompras(Double montoForwardEstraCompras) {
		this.montoForwardEstraCompras = montoForwardEstraCompras;
	}

	public Double getMontoForwardEstraVentas() {
		return montoForwardEstraVentas;
	}

	public void setMontoForwardEstraVentas(Double montoForwardEstraVentas) {
		this.montoForwardEstraVentas = montoForwardEstraVentas;
	}
	
	public Double getPosicionFinalResmerca() {
		return posicionFinalResmerca;
	}

	public void setPosicionFinalResmerca(Double posicionFinalResmerca) {
		this.posicionFinalResmerca = posicionFinalResmerca;
	}
	
	public URL getImage() {
		return image;
	}

	public void setImage(URL image) {
		this.image = image;
	}
	
	public Double getMontoFwEstraSectores() {
		return montoFwEstraSectores;
	}

	public void setMontoFwEstraSectores(Double montoFwEstraSectores) {
		this.montoFwEstraSectores = montoFwEstraSectores;
	}
	
	private Boolean isRecibimos;
	private String idDivisa;
	private String sector;
	private Double sectorCash;
	private Double sectorTom;
	private Double sectorSpot; 
	private Double sector72Hr;
	private Double sectorVFut;
	private Double swap;
	private Double montoForward;
	private Double montoResmerca;
	private String resmercaString;
	private Double posicionInicial;
	private Double posicionInicialCash;
	private Double posicionInicialTom;
	private Double posicionInicialSpot;
	private Double posicionInicial72Hr;
	private Double posicionInicialVFut;
	private Double posicionFinal;
	private Double posicionFinalCash;
	private Double posicionFinalTom;
	private Double posicionFinalSpot;
	private Double posicionFinal72Hr;
	private Double posicionFinalVFut;
	private Double tipoCambioCompra;
	private Double tipoCambioCompraCash;
	private Double tipoCambioCompraTom;
	private Double tipoCambioCompraSpot;
	private Double tipoCambioCompra72Hr;
	private Double tipoCambioCompraVFut;
	private Double tipoCambioVenta;
	private Double tipoCambioVentaCash;
	private Double tipoCambioVentaTom;
	private Double tipoCambioVentaSpot;
	private Double tipoCambioVenta72Hr;
	private Double tipoCambioVentaVFut;
	private Double swapMn;  
	private Boolean isForward;
	private Boolean isSectorResmerca;
	private Date fecha;
	private Double resmercaCompras;
	private Double remercaVentas;
	private String  resmercaComprasString;
	private String remercaVentasString;
	private Double montoForwardEstra;
	private Boolean isForwardEstra;
	private Double montoForwardEstraCompras;
	private Double montoForwardEstraVentas;
	private Double posicionFinalResmerca;
	private URL image;
	private Double montoFwEstraSectores;
	
}	
