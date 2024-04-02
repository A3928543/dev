package com.ixe.ods.sica.services.impl;

import java.math.BigDecimal;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.banorte.ws.esb.common.excepciongeneral.ExcepcionGenericaType;
import com.banorte.ws.esb.consultaclabessaldos.ConsultarClabesSaldosRespuesta;
import com.banorte.ws.esb.consultaclabessaldos.ConsultarClabesSaldosRespuesta.DatosPersonales;
import com.banorte.ws.esb.general.headers.EstadoRespuestaType;
import com.banorte.ws.esb.general.headers.HeaderResponseType;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.dto.InfoCuentaAltamiraDto;

public class ConsultaClabesSaldosResponseHandler extends DefaultHandler{
	
	ExcepcionGenericaType exceptionResponse;
	HeaderResponseType headerResponse;
	private EstadoRespuestaType estadoResponse;
	private ConsultarClabesSaldosRespuesta bodyResponse;
	private DatosPersonales datosPersonales;
	
	private StringBuffer sb;
    InfoCuentaAltamiraDto infoCuenta = new InfoCuentaAltamiraDto();

	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		
		String tagName;
		String keyValue[] = qName.split(":");
		
		if(keyValue.length == 1) {
			tagName = keyValue[0];
		}else {
			tagName = keyValue[1];
		}
		
		if(tagName.equalsIgnoreCase("Fault")) {
			throw new SicaException("Error en la respuesta del servicio ConsultaClabesSaldos");
		}else if(tagName.equalsIgnoreCase("Excepcion")) {
			exceptionResponse = new ExcepcionGenericaType();
		}else if(tagName.equalsIgnoreCase("HeaderRes")) {
			headerResponse = new HeaderResponseType();
		}else if(tagName.equalsIgnoreCase("EstadoRespuesta")) {
			estadoResponse = new EstadoRespuestaType();
		}else if(tagName.equalsIgnoreCase("consultarClabesSaldosRespuesta")) {
			bodyResponse = new ConsultarClabesSaldosRespuesta();
		}else if(tagName.equalsIgnoreCase("DatosPersonales")) {
			datosPersonales = new DatosPersonales();
		}else if(tagName.equalsIgnoreCase("DatosComplementariosCentroControl")) {
		}
		
        sb.setLength(0);
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException{
		sb.append(ch, start, length);
    }
	
	public void endElement(String uri, String localName, String qName) {
		
		/* DATOS DE EXCEPCION */
		if(exceptionResponse != null) {
			if(qName.equalsIgnoreCase("Codigo")) {
				exceptionResponse.setCodigo(sb.toString());
			}else if(qName.equalsIgnoreCase("Componente")) {
				exceptionResponse.setComponente(sb.toString());
			}else if(qName.equalsIgnoreCase("Metodo")) {
				exceptionResponse.setMetodo(sb.toString());
			}else if(qName.equalsIgnoreCase("Descripcion")) {
				exceptionResponse.setDescripcion(sb.toString());
			}
		}else {
			/* DATOS DE HEADER */
			if(headerResponse != null) {
				if(qName.equalsIgnoreCase("IdOperacion")) {
					headerResponse.setIdOperacion(sb.toString());
				}else if(qName.equalsIgnoreCase("NumReferencia")) {
					headerResponse.setNumReferencia(sb.toString());
				}else if(qName.equalsIgnoreCase("TokenOperacion")) {
					headerResponse.setTokenOperacion(sb.toString());
				}
			}
			
			if(estadoResponse != null) {
				if(qName.equalsIgnoreCase("Id")) {
					estadoResponse.setId(sb.toString());
				}else if(qName.equalsIgnoreCase("MensajeAUsuario")) {
					estadoResponse.setMensajeAUsuario(sb.toString());
				}else if(qName.equalsIgnoreCase("MensajeDetallado")) {
					estadoResponse.setMensajeDetallado(sb.toString());
				}
			}
			
			/* DATOS DE BODY */
			if(bodyResponse != null) {
				if(datosPersonales != null) {
					if(qName.equalsIgnoreCase("FechaProceso")) {
						datosPersonales.setFechaProceso(sb.toString());
					}else if(qName.equalsIgnoreCase("Tran_NumeroCuenta")) {
						datosPersonales.setTranNumeroCuenta(sb.toString());
					}else if(qName.equalsIgnoreCase("CuentaAsociada")) {
						datosPersonales.setCuentaAsociada(sb.toString());
					}else if(qName.equalsIgnoreCase("Tran_NumeroCliente")) {
						datosPersonales.setTranNumeroCliente(sb.toString());
					}else if(qName.equalsIgnoreCase("Tran_NombrePersona")) {
						datosPersonales.setTranNombrePersona(sb.toString());
					}else if(qName.equalsIgnoreCase("Rfc")) {
						datosPersonales.setRfc(sb.toString());
					}else if(qName.equalsIgnoreCase("NombreCalle")) {
						datosPersonales.setNombreCalle(sb.toString());
					}else if(qName.equalsIgnoreCase("NumeroCalle")) {
						datosPersonales.setNumeroCalle(sb.toString());
					}else if(qName.equalsIgnoreCase("NombreColonia")) {
						datosPersonales.setNombreColonia(sb.toString());
					}else if(qName.equalsIgnoreCase("EntidadFederativa")) {
						datosPersonales.setEntidadFederativa(sb.toString());
					}else if(qName.equalsIgnoreCase("NombreCiudad")) {
						datosPersonales.setNombreCiudad(sb.toString());
					}else if(qName.equalsIgnoreCase("CodigoPostal")) {
						datosPersonales.setCodigoPostal(sb.toString());
					}else if(qName.equalsIgnoreCase("Telefono")) {
						datosPersonales.setTelefono(sb.toString());
					}else if(qName.equalsIgnoreCase("Tran_TipoProducto")) {
						datosPersonales.setTranTipoProducto(sb.toString());
					}else if(qName.equalsIgnoreCase("Tran_SubProducto")) {
						datosPersonales.setTranSubProducto(sb.toString());
					}else if(qName.equalsIgnoreCase("DescripcionSubprodu")) {
						datosPersonales.setDescripcionSubprodu(sb.toString());
					}else if(qName.equalsIgnoreCase("EstatusCta")) {
						datosPersonales.setEstatusCta(sb.toString());
					}else if(qName.equalsIgnoreCase("CrOrigen")) {
						datosPersonales.setCrOrigen(sb.toString());
					}else if(qName.equalsIgnoreCase("DescripcionSucursal")) {
						datosPersonales.setDescripcionSucursal(sb.toString());
					}else if(qName.equalsIgnoreCase("FechaApertura")) {
						datosPersonales.setFechaApertura(sb.toString());
					}else if(qName.equalsIgnoreCase("Tran_FechaVencimiento")) {
						datosPersonales.setTranFechaVencimiento(sb.toString());
					}else if(qName.equalsIgnoreCase("Tran_TipoDivisa")) {
						datosPersonales.setTranTipoDivisa(sb.toString());
					}else if(qName.equalsIgnoreCase("ClabeCuenta")) {
						datosPersonales.setClabeCuenta(sb.toString());
					}else if(qName.equalsIgnoreCase("SaldoPromedio")) {
						datosPersonales.setSaldoPromedio(new BigDecimal(sb.toString()));
					}else if(qName.equalsIgnoreCase("CodigoRetencion")) {
						datosPersonales.setCodigoRetencion(sb.toString());
					}else if(qName.equalsIgnoreCase("Regimen")) {
						datosPersonales.setRegimen(sb.toString());
					}else if(qName.equalsIgnoreCase("SuceptibilidadPlast")) {
						datosPersonales.setSuceptibilidadPlast(sb.toString());
					}else if(qName.equalsIgnoreCase("Titularidad")) {
						datosPersonales.setTitularidad(sb.toString());
					}else if(qName.equalsIgnoreCase("CveRegimen")) {
						datosPersonales.setCveRegimen(sb.toString());
					}else if(qName.equalsIgnoreCase("CodigoBloqueo")) {
						datosPersonales.setCodigoBloqueo(sb.toString());
					}else if(qName.equalsIgnoreCase("SituacionCuenta")) {
						datosPersonales.setSituacionCuenta(sb.toString());
					}else if(qName.equalsIgnoreCase("BloqueoJudicial")) {
						datosPersonales.setRegimenFirmas(sb.toString());
					}else if(qName.equalsIgnoreCase("BloqueoAdmon")) {
						datosPersonales.setRegimenFirmas(sb.toString());
					}else if(qName.equalsIgnoreCase("LiquidacionInte")) {
						datosPersonales.setLiquidacionInte(sb.toString());
					}else if(qName.equalsIgnoreCase("LineaPrivilegios")) {
						datosPersonales.setLineaPrivilegios(sb.toString());
					}else if(qName.equalsIgnoreCase("MontoServicio")) {
						datosPersonales.setMontoServicio(new BigDecimal(sb.toString()));
					}else if(qName.equalsIgnoreCase("EstatusLinPriv")) {
						datosPersonales.setEstatusLinPriv(sb.toString());
					}else if(qName.equalsIgnoreCase("IndServicio")) {
						datosPersonales.setIndServicio(sb.toString());
					}else if(qName.equalsIgnoreCase("IndComision")) {
						datosPersonales.setIndServicio(sb.toString());
					}else if(qName.equalsIgnoreCase("RegimenFirmas")) {
						datosPersonales.setRegimenFirmas(sb.toString());
					}else if(qName.equalsIgnoreCase("NombrePromotor")) {
						datosPersonales.setNombrePromotor(sb.toString());
					}else if(qName.equalsIgnoreCase("SegmentoCliente")) {
						datosPersonales.setSegmentoCliente(sb.toString());
					}else if(qName.equalsIgnoreCase("Banco")) {
						datosPersonales.setBanco(sb.toString());
					}
				}
			}
		}
		
		sb.setLength(0);
	}
	
	public void startDocument() throws SAXException{
        sb = new StringBuffer();
	}
	
	public void endDocument() throws SAXException{
		if(exceptionResponse == null) {
			headerResponse.setEstadoRespuesta(estadoResponse);
			
			if(!headerResponse.getEstadoRespuesta().getId().equalsIgnoreCase("0")) {				
				this.infoCuenta.setNumeroCliente(datosPersonales.getTranNumeroCliente());
				this.infoCuenta.setCr(datosPersonales.getCrOrigen());
				this.infoCuenta.setDescSucursal(datosPersonales.getDescripcionSucursal());
				this.infoCuenta.setNombreCliente(datosPersonales.getTranNombrePersona());
				this.infoCuenta.setNumeroCuenta(datosPersonales.getTranNumeroCuenta());
				this.infoCuenta.setStatusCuenta(datosPersonales.getEstatusCta());
				this.infoCuenta.setTipoDivisa(datosPersonales.getTranTipoDivisa());
			}
		}
	}
}
