package com.ixe.ods.sica.services.impl;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.banorte.ws.esb.common.excepciongeneral.ExcepcionGenericaType;
import com.banorte.ws.esb.general.headers.EstadoRespuestaType;
import com.banorte.ws.esb.general.headers.HeaderResponseType;
import com.banorte.ws.esb.mantenimientodatosbasicos.ConsultaDatosBasicosRespuesta;
import com.banorte.ws.esb.mantenimientodatosbasicos.ConsultaDatosBasicosRespuesta.RespDatBasicCliente;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.dto.DatosBasicosClienteAltamiraDto;

public class ConsultaDatosBasicosClienteAltamiraResponseHandler extends DefaultHandler{
	
	ExcepcionGenericaType exceptionResponse;
	HeaderResponseType headerResponse;
	private EstadoRespuestaType estadoResponse;
	private ConsultaDatosBasicosRespuesta bodyResponse;
	private RespDatBasicCliente datosBasicos;
	
	private StringBuffer sb;
	DatosBasicosClienteAltamiraDto infoCliente = new DatosBasicosClienteAltamiraDto();
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		
		String tagName;
		String keyValue[] = qName.split(":");
		
		if(keyValue.length == 1) {
			tagName = keyValue[0];
		}else {
			tagName = keyValue[1];
		}
		
		if(tagName.equalsIgnoreCase("Fault")) {
			throw new SicaException("Error en la respuesta del servicio MantenimientoDatosBasicosCliente");
		}else if(tagName.equalsIgnoreCase("Excepcion")) {
			exceptionResponse = new ExcepcionGenericaType();
		}else if(tagName.equalsIgnoreCase("HeaderRes")) {
			headerResponse = new HeaderResponseType();
		}else if(tagName.equalsIgnoreCase("EstadoRespuesta")) {
			estadoResponse = new EstadoRespuestaType();
		}else if(tagName.equalsIgnoreCase("mantenerDatosBasicosRespuesta")) {
			bodyResponse = new ConsultaDatosBasicosRespuesta();
		}else if(tagName.equalsIgnoreCase("RespDatBasicCliente")) {
			datosBasicos = new RespDatBasicCliente();
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
				if(datosBasicos != null) {
					if(qName.equalsIgnoreCase("NumeroCliente")) {
						datosBasicos.setNumeroCliente(Integer.valueOf(sb.toString()));
					}else if(qName.equalsIgnoreCase("RfcCliente")) {
						datosBasicos.setRfcCliente(sb.toString());
					}else if(qName.equalsIgnoreCase("Secursal")) {
						datosBasicos.setSecursal(sb.toString());;
					}else if(qName.equalsIgnoreCase("AreaNegocio")) {
						datosBasicos.setAreaNegocio(sb.toString());
					}else if(qName.equalsIgnoreCase("PersonalidadJuridica")) {
						datosBasicos.setPersonalidadJuridica(sb.toString());
					}else if(qName.equalsIgnoreCase("DescripcionCr")) {
						datosBasicos.setDescripcionCr(sb.toString());
					}else if(qName.equalsIgnoreCase("IndicadorBanco")) {
						datosBasicos.setIndicadorBanco(sb.toString());
					}else if(qName.equalsIgnoreCase("NombreCliente")) {
						datosBasicos.setNombreCliente(sb.toString());
					}else if(qName.equalsIgnoreCase("PrimerApellido")) {
						datosBasicos.setPrimerApellido(sb.toString());
					}else if(qName.equalsIgnoreCase("SegundoApellido")) {
						datosBasicos.setSegundoApellido(sb.toString());
					}else if(qName.equalsIgnoreCase("NombreCompleto")) {
						datosBasicos.setNombreCompleto(sb.toString());
					}else if(qName.equalsIgnoreCase("CodigoIdentifica")) {
						datosBasicos.setCodigoIdentifica(sb.toString());
					}else if(qName.equalsIgnoreCase("ClaveIdentifica")) {
						datosBasicos.setClaveIdentifica(sb.toString());
					}else if(qName.equalsIgnoreCase("Curp")) {
						datosBasicos.setCurp(sb.toString());
					}else if(qName.equalsIgnoreCase("Imss")) {
						datosBasicos.setImss(sb.toString());
					}else if(qName.equalsIgnoreCase("FielShcp")) {
						datosBasicos.setFielShcp(sb.toString());
					}else if(qName.equalsIgnoreCase("FechaNacimiento")) {
						datosBasicos.setFechaNacimiento(Integer.valueOf(sb.toString()));
					}else if(qName.equalsIgnoreCase("Sexo")) {
						datosBasicos.setSexo(sb.toString());
					}else if(qName.equalsIgnoreCase("EstadoCivil")) {
						datosBasicos.setEstadoCivil(sb.toString());
					}else if(qName.equalsIgnoreCase("Titulo")) {
						datosBasicos.setTitulo(sb.toString());
					}else if(qName.equalsIgnoreCase("DescripcionTitulo")) {
						datosBasicos.setDescripcionTitulo(sb.toString());
					}else if(qName.equalsIgnoreCase("Tratamiento")) {
						datosBasicos.setTratamiento(sb.toString());
					}else if(qName.equalsIgnoreCase("TipoDomicilio")) {
						datosBasicos.setTipoDomicilio(sb.toString());
					}else if(qName.equalsIgnoreCase("Via")) {
						datosBasicos.setVia(sb.toString());
					}else if(qName.equalsIgnoreCase("Calle")) {
						datosBasicos.setCalle(sb.toString());
					}else if(qName.equalsIgnoreCase("NombreCalle")) {
						datosBasicos.setNombreCalle(sb.toString());
					}else if(qName.equalsIgnoreCase("NumeroCalle")) {
						datosBasicos.setNumeroCalle(sb.toString());
					}else if(qName.equalsIgnoreCase("TipoVivienda")) {
						datosBasicos.setTipoVivienda(sb.toString());
					}else if(qName.equalsIgnoreCase("DescripcionVivienda")) {
						datosBasicos.setDescripcionVivienda(sb.toString());
					}else if(qName.equalsIgnoreCase("Entrada")) {
						datosBasicos.setEntrada(sb.toString());
					}else if(qName.equalsIgnoreCase("Piso")) {
						datosBasicos.setPiso(sb.toString());
					}else if(qName.equalsIgnoreCase("Departamento")) {
						datosBasicos.setDepartamento(sb.toString());
					}else if(qName.equalsIgnoreCase("Colonia")) {
						datosBasicos.setColonia(sb.toString());
					}else if(qName.equalsIgnoreCase("CodigoPostal")) {
						datosBasicos.setCodigoPostal(Integer.valueOf(sb.toString()));
					}else if(qName.equalsIgnoreCase("CodigoDelegMuni")) {
						datosBasicos.setCodigoDelegMuni(sb.toString());
					}else if(qName.equalsIgnoreCase("DescriDelegMuni")) {
						datosBasicos.setDescriDelegMuni(sb.toString());
					}else if(qName.equalsIgnoreCase("CodigoEstado")) {
						datosBasicos.setCodigoEstado(sb.toString());
					}else if(qName.equalsIgnoreCase("DescripcionEstado")) {
						datosBasicos.setDescripcionEstado(sb.toString());
					}else if(qName.equalsIgnoreCase("CodigoPais")) {
						datosBasicos.setCodigoPais(sb.toString());
					}else if(qName.equalsIgnoreCase("TipoTelefonoDos")) {
						datosBasicos.setTipoTelefonoDos(sb.toString());
					}else if(qName.equalsIgnoreCase("NumeroTelefonoDos")) {
						datosBasicos.setNumeroTelefonoDos(sb.toString());
					}else if(qName.equalsIgnoreCase("TipoTelefonoTres")) {
						datosBasicos.setTipoTelefonoTres(sb.toString());
					}else if(qName.equalsIgnoreCase("NumeroTelefonoTres")) {
						datosBasicos.setNumeroTelefonoTres(sb.toString());
					}else if(qName.equalsIgnoreCase("TipoTelefonoCuatro")) {
						datosBasicos.setTipoTelefonoCuatro(sb.toString());
					}else if(qName.equalsIgnoreCase("NumeroTelefonoCuatro")) {
						datosBasicos.setNumeroTelefonoCuatro(sb.toString());
					}else if(qName.equalsIgnoreCase("CodigoCno")) {
						datosBasicos.setCodigoCno(sb.toString());
					}else if(qName.equalsIgnoreCase("DescripCno")) {
						datosBasicos.setDescripCno(sb.toString());
					}else if(qName.equalsIgnoreCase("Cnaeg")) {
						datosBasicos.setCnaeg(sb.toString());
					}else if(qName.equalsIgnoreCase("IndGranCliente")) {
						datosBasicos.setIndGranCliente(sb.toString());
					}else if(qName.equalsIgnoreCase("Funcionario")) {
						datosBasicos.setFuncionario(sb.toString());
					}else if(qName.equalsIgnoreCase("UltimoNumeroAviso")) {
						datosBasicos.setUltimoNumeroAviso(Integer.valueOf(sb.toString()));
					}else if(qName.equalsIgnoreCase("NumDomicAlternativ")) {
						datosBasicos.setNumDomicAlternativ(Integer.valueOf(sb.toString()));
					}else if(qName.equalsIgnoreCase("NombreTarjeta")) {
						datosBasicos.setNombreTarjeta(sb.toString());
					}else if(qName.equalsIgnoreCase("Segmento")) {
						datosBasicos.setSegmento(sb.toString());
					}else if(qName.equalsIgnoreCase("GradoRiesgo")) {
						datosBasicos.setGradoRiesgo(sb.toString());
					}else if(qName.equalsIgnoreCase("CargoPolitico")) {
						datosBasicos.setCargoPolitico(sb.toString());
					}else if(qName.equalsIgnoreCase("DescCargoPolitico")) {
						datosBasicos.setDescCargoPolitico(sb.toString());
					}else if(qName.equalsIgnoreCase("ProgLealtadBanorte")) {
						datosBasicos.setProgLealtadBanorte(sb.toString());
					}else if(qName.equalsIgnoreCase("ProgLealtadBanorte")) {
						datosBasicos.setProgLealtadBanorte(sb.toString());
					}else if(qName.equalsIgnoreCase("Calireg")) {
						datosBasicos.setCalireg(sb.toString());
					}else if(qName.equalsIgnoreCase("IndPropagandaReus")) {
						datosBasicos.setIndPropagandaReus(sb.toString());
					}else if(qName.equalsIgnoreCase("IndLide")) {
						datosBasicos.setIndLide(sb.toString());
					}else if(qName.equalsIgnoreCase("IndicadorReferencia")) {
						datosBasicos.setIndicadorReferencia(sb.toString());
					}else if(qName.equalsIgnoreCase("Bxi")) {
						datosBasicos.setBxi(sb.toString());
					}else if(qName.equalsIgnoreCase("AltaShcp")) {
						datosBasicos.setAltaShcp(sb.toString());
					}else if(qName.equalsIgnoreCase("IndicadorMigrado")) {
						datosBasicos.setIndicadorMigrado(sb.toString());
					}else if(qName.equalsIgnoreCase("IndicadorRepecos")) {
						datosBasicos.setIndicadorRepecos(sb.toString());
					}else if(qName.equalsIgnoreCase("IndEncuestaWNueve")) {
						datosBasicos.setIndEncuestaWNueve(sb.toString());
					}else if(qName.equalsIgnoreCase("IndClienteFatca")) {
						datosBasicos.setIndClienteFatca(sb.toString());
					}else if(qName.equalsIgnoreCase("NacionalidadUno")) {
						datosBasicos.setNacionalidadUno(sb.toString());
					}else if(qName.equalsIgnoreCase("DescnacionalidadUno")) {
						datosBasicos.setDescnacionalidadUno(sb.toString());
					}else if(qName.equalsIgnoreCase("NacionalidadDos")) {
						datosBasicos.setNacionalidadDos(sb.toString());
					}else if(qName.equalsIgnoreCase("DescnacionalidadDos")) {
						datosBasicos.setDescnacionalidadDos(sb.toString());
					}else if(qName.equalsIgnoreCase("SocialSecurityNDos")) {
						datosBasicos.setSocialSecurityNDos(sb.toString());
					}else if(qName.equalsIgnoreCase("TaxpayerIdNDos")) {
						datosBasicos.setTaxpayerIdNDos(sb.toString());
					}else if(qName.equalsIgnoreCase("NacionalidadTres")) {
						datosBasicos.setNacionalidadTres(sb.toString());
					}else if(qName.equalsIgnoreCase("DescnacionalidadTres")) {
						datosBasicos.setDescnacionalidadTres(sb.toString());
					}else if(qName.equalsIgnoreCase("SocialSecurityNTres")) {
						datosBasicos.setSocialSecurityNTres(sb.toString());
					}else if(qName.equalsIgnoreCase("TaxpayerIdNTres")) {
						datosBasicos.setTaxpayerIdNTres(sb.toString());
					}else if(qName.equalsIgnoreCase("IndResidencia")) {
						datosBasicos.setIndResidencia(sb.toString());
					}else if(qName.equalsIgnoreCase("PaisResidenciaUno")) {
						datosBasicos.setPaisResidenciaUno(sb.toString());
					}else if(qName.equalsIgnoreCase("DescripPaisResidenciaUno")) {
						datosBasicos.setDescripPaisResidenciaUno(sb.toString());
					}else if(qName.equalsIgnoreCase("PaisResidenciaDos")) {
						datosBasicos.setPaisResidenciaDos(sb.toString());
					}else if(qName.equalsIgnoreCase("DescripPaisResidenciaDos")) {
						datosBasicos.setDescripPaisResidenciaDos(sb.toString());
					}else if(qName.equalsIgnoreCase("PaisResidenciaTres")) {
						datosBasicos.setPaisResidenciaTres(sb.toString());
					}else if(qName.equalsIgnoreCase("DescripPaisResidenciaTres")) {
						datosBasicos.setDescripPaisResidenciaTres(sb.toString());
					}
				}
			}
		}
		
		this.sb.setLength(0);
	}
	
	public void startDocument() throws SAXException{
		sb = new StringBuffer();
	}
	
	public void endDocument() throws SAXException{
		if(exceptionResponse == null) {
			headerResponse.setEstadoRespuesta(estadoResponse);
			
			if(!headerResponse.getEstadoRespuesta().getId().equalsIgnoreCase("0")) {
				this.infoCliente.setMaterno(datosBasicos.getSegundoApellido());
				this.infoCliente.setNombre(datosBasicos.getNombreCliente());
				this.infoCliente.setPaterno(datosBasicos.getPrimerApellido());
				this.infoCliente.setPerJur(datosBasicos.getPersonalidadJuridica());
				this.infoCliente.setRfc(datosBasicos.getRfcCliente());
				this.infoCliente.setRazonSocial(datosBasicos.getNombreCompleto());
			}
		}
	}
}