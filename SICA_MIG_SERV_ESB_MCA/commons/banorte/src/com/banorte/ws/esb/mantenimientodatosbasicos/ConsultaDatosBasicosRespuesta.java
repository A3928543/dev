package com.banorte.ws.esb.mantenimientodatosbasicos;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Clase Java para ConsultaDatosBasicosRespuesta complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ConsultaDatosBasicosRespuesta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RespDatBasicCliente" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="NumeroCliente" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="99999999"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="RfcCliente" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="13"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Secursal" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="4"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="AreaNegocio" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="PersonalidadJuridica" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="3"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DescripcionCr" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="40"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="IndicadorBanco" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="NombreCliente" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="20"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="PrimerApellido" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="20"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="SegundoApellido" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="20"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="NombreCompleto" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="60"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="CodigoIdentifica" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="ClaveIdentifica" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="25"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Curp" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="18"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Imss" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="12"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="FielShcp" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="20"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="FechaNacimiento" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="99999999"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Sexo" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="EstadoCivil" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Titulo" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="3"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DescripcionTitulo" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="40"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Tratamiento" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="7"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TipoDomicilio" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Via" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Calle" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="6"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="NombreCalle" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="24"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="NumeroCalle" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="7"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TipoVivienda" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DescripcionVivienda" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="15"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Entrada" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Piso" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Departamento" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="4"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Colonia" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="40"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="CodigoPostal" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="99999"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="CodigoDelegMuni" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="3"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DescriDelegMuni" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="28"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="CodigoEstado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DescripcionEstado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="5"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="CodigoPais" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="3"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TipoTelefonoDos" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="NumeroTelefonoDos" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="17"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TipoTelefonoTres" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="NumeroTelefonoTres" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="17"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TipoTelefonoCuatro" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="NumeroTelefonoCuatro" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="17"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="CodigoCno" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DescripCno" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="36"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Cnaeg" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="6"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="IndGranCliente" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Funcionario" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="10"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="UltimoNumeroAviso" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="999"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="NumDomicAlternativ" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="999"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="NombreTarjeta" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="24"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Segmento" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="30"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="GradoRiesgo" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="CargoPolitico" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DescCargoPolitico" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="60"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="ProgLealtadBanorte" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Calireg" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="IndPropagandaReus" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="IndLide" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="IndicadorReferencia" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Bxi" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="AltaShcp" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="8"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="IndicadorMigrado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="IndicadorRepecos" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="IndEncuestaWNueve" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="IndClienteFatca" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="NacionalidadUno" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="3"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DescnacionalidadUno" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="20"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="NacionalidadDos" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="3"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DescnacionalidadDos" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="20"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="SocialSecurityNDos" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="13"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TaxpayerIdNDos" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="18"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="NacionalidadTres" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="3"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DescnacionalidadTres" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="20"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="SocialSecurityNTres" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="13"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TaxpayerIdNTres" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="18"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="IndResidencia" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="PaisResidenciaUno" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="3"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DescripPaisResidenciaUno" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="20"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="PaisResidenciaDos" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="3"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DescripPaisResidenciaDos" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="20"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="PaisResidenciaTres" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="3"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DescripPaisResidenciaTres" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="20"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Paginacion" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="NumeroRegistro" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="5"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="ClavePaginacion" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="120"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
public class ConsultaDatosBasicosRespuesta {

    /* SE CAMBIA DE List<ConsultaDatosBasicosRespuesta.RespDatBasicCliente> a ConsultaDatosBasicosRespuesta.RespDatBasicCliente */
	protected ConsultaDatosBasicosRespuesta.RespDatBasicCliente respDatBasicCliente;
    protected ConsultaDatosBasicosRespuesta.Paginacion paginacion;

    /**
     * Gets the value of the respDatBasicCliente property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the respDatBasicCliente property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRespDatBasicCliente().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConsultaDatosBasicosRespuesta.RespDatBasicCliente }
     * 
     * 
     */
    public ConsultaDatosBasicosRespuesta.RespDatBasicCliente getRespDatBasicCliente() {
        if (respDatBasicCliente == null) {
            respDatBasicCliente = new ConsultaDatosBasicosRespuesta.RespDatBasicCliente();
        }
        return this.respDatBasicCliente;
    }

    /**
     * Obtiene el valor de la propiedad paginacion.
     * 
     * @return
     *     possible object is
     *     {@link ConsultaDatosBasicosRespuesta.Paginacion }
     *     
     */
    public ConsultaDatosBasicosRespuesta.Paginacion getPaginacion() {
        return paginacion;
    }

    /**
     * Define el valor de la propiedad paginacion.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsultaDatosBasicosRespuesta.Paginacion }
     *     
     */
    public void setPaginacion(ConsultaDatosBasicosRespuesta.Paginacion value) {
        this.paginacion = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="NumeroRegistro" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="5"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="ClavePaginacion" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="120"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    public static class Paginacion {

        protected String numeroRegistro;
        protected String clavePaginacion;

        /**
         * Obtiene el valor de la propiedad numeroRegistro.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumeroRegistro() {
            return numeroRegistro;
        }

        /**
         * Define el valor de la propiedad numeroRegistro.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumeroRegistro(String value) {
            this.numeroRegistro = value;
        }

        /**
         * Obtiene el valor de la propiedad clavePaginacion.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getClavePaginacion() {
            return clavePaginacion;
        }

        /**
         * Define el valor de la propiedad clavePaginacion.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setClavePaginacion(String value) {
            this.clavePaginacion = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="NumeroCliente" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="99999999"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="RfcCliente" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="13"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Secursal" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="4"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="AreaNegocio" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="PersonalidadJuridica" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="3"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DescripcionCr" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="40"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="IndicadorBanco" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="NombreCliente" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="20"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="PrimerApellido" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="20"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="SegundoApellido" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="20"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="NombreCompleto" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="60"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="CodigoIdentifica" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="ClaveIdentifica" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="25"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Curp" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="18"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Imss" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="12"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="FielShcp" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="20"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="FechaNacimiento" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="99999999"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Sexo" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="EstadoCivil" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Titulo" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="3"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DescripcionTitulo" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="40"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Tratamiento" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="7"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TipoDomicilio" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Via" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Calle" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="6"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="NombreCalle" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="24"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="NumeroCalle" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="7"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TipoVivienda" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DescripcionVivienda" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="15"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Entrada" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Piso" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Departamento" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="4"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Colonia" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="40"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="CodigoPostal" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="99999"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="CodigoDelegMuni" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="3"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DescriDelegMuni" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="28"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="CodigoEstado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DescripcionEstado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="5"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="CodigoPais" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="3"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TipoTelefonoDos" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="NumeroTelefonoDos" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="17"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TipoTelefonoTres" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="NumeroTelefonoTres" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="17"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TipoTelefonoCuatro" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="NumeroTelefonoCuatro" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="17"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="CodigoCno" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DescripCno" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="36"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Cnaeg" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="6"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="IndGranCliente" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Funcionario" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="10"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="UltimoNumeroAviso" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="999"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="NumDomicAlternativ" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="999"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="NombreTarjeta" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="24"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Segmento" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="30"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="GradoRiesgo" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="CargoPolitico" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DescCargoPolitico" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="60"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="ProgLealtadBanorte" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Calireg" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="IndPropagandaReus" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="IndLide" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="IndicadorReferencia" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Bxi" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="AltaShcp" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="8"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="IndicadorMigrado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="IndicadorRepecos" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="IndEncuestaWNueve" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="IndClienteFatca" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="NacionalidadUno" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="3"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DescnacionalidadUno" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="20"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="NacionalidadDos" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="3"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DescnacionalidadDos" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="20"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="SocialSecurityNDos" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="13"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TaxpayerIdNDos" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="18"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="NacionalidadTres" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="3"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DescnacionalidadTres" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="20"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="SocialSecurityNTres" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="13"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TaxpayerIdNTres" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="18"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="IndResidencia" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="PaisResidenciaUno" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="3"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DescripPaisResidenciaUno" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="20"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="PaisResidenciaDos" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="3"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DescripPaisResidenciaDos" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="20"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="PaisResidenciaTres" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="3"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DescripPaisResidenciaTres" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="0"/>
     *               &lt;maxLength value="20"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    public static class RespDatBasicCliente {

        protected Integer numeroCliente;
        protected String rfcCliente;
        protected String secursal;
        protected String areaNegocio;
        protected String personalidadJuridica;
        protected String descripcionCr;
        protected String indicadorBanco;
        protected String nombreCliente;
        protected String primerApellido;
        protected String segundoApellido;
        protected String nombreCompleto;
        protected String codigoIdentifica;
        protected String claveIdentifica;
        protected String curp;
        protected String imss;
        protected String fielShcp;
        protected Integer fechaNacimiento;
        protected String sexo;
        protected String estadoCivil;
        protected String titulo;
        protected String descripcionTitulo;
        protected String tratamiento;
        protected String tipoDomicilio;
        protected String via;
        protected String calle;
        protected String nombreCalle;
        protected String numeroCalle;
        protected String tipoVivienda;
        protected String descripcionVivienda;
        protected String entrada;
        protected String piso;
        protected String departamento;
        protected String colonia;
        protected Integer codigoPostal;
        protected String codigoDelegMuni;
        protected String descriDelegMuni;
        protected String codigoEstado;
        protected String descripcionEstado;
        protected String codigoPais;
        protected String tipoTelefonoDos;
        protected String numeroTelefonoDos;
        protected String tipoTelefonoTres;
        protected String numeroTelefonoTres;
        protected String tipoTelefonoCuatro;
        protected String numeroTelefonoCuatro;
        protected String codigoCno;
        protected String descripCno;
        protected String cnaeg;
        protected String indGranCliente;
        protected String funcionario;
        protected Integer ultimoNumeroAviso;
        protected Integer numDomicAlternativ;
        protected String nombreTarjeta;
        protected String segmento;
        protected String gradoRiesgo;
        protected String cargoPolitico;
        protected String descCargoPolitico;
        protected String progLealtadBanorte;
        protected String calireg;
        protected String indPropagandaReus;
        protected String indLide;
        protected String indicadorReferencia;
        protected String bxi;
        protected String altaShcp;
        protected String indicadorMigrado;
        protected String indicadorRepecos;
        protected String indEncuestaWNueve;
        protected String indClienteFatca;
        protected String nacionalidadUno;
        protected String descnacionalidadUno;
        protected String nacionalidadDos;
        protected String descnacionalidadDos;
        protected String socialSecurityNDos;
        protected String taxpayerIdNDos;
        protected String nacionalidadTres;
        protected String descnacionalidadTres;
        protected String socialSecurityNTres;
        protected String taxpayerIdNTres;
        protected String indResidencia;
        protected String paisResidenciaUno;
        protected String descripPaisResidenciaUno;
        protected String paisResidenciaDos;
        protected String descripPaisResidenciaDos;
        protected String paisResidenciaTres;
        protected String descripPaisResidenciaTres;

        /**
         * Obtiene el valor de la propiedad numeroCliente.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getNumeroCliente() {
            return numeroCliente;
        }

        /**
         * Define el valor de la propiedad numeroCliente.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setNumeroCliente(Integer value) {
            this.numeroCliente = value;
        }

        /**
         * Obtiene el valor de la propiedad rfcCliente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRfcCliente() {
            return rfcCliente;
        }

        /**
         * Define el valor de la propiedad rfcCliente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRfcCliente(String value) {
            this.rfcCliente = value;
        }

        /**
         * Obtiene el valor de la propiedad secursal.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSecursal() {
            return secursal;
        }

        /**
         * Define el valor de la propiedad secursal.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSecursal(String value) {
            this.secursal = value;
        }

        /**
         * Obtiene el valor de la propiedad areaNegocio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAreaNegocio() {
            return areaNegocio;
        }

        /**
         * Define el valor de la propiedad areaNegocio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAreaNegocio(String value) {
            this.areaNegocio = value;
        }

        /**
         * Obtiene el valor de la propiedad personalidadJuridica.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPersonalidadJuridica() {
            return personalidadJuridica;
        }

        /**
         * Define el valor de la propiedad personalidadJuridica.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPersonalidadJuridica(String value) {
            this.personalidadJuridica = value;
        }

        /**
         * Obtiene el valor de la propiedad descripcionCr.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescripcionCr() {
            return descripcionCr;
        }

        /**
         * Define el valor de la propiedad descripcionCr.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescripcionCr(String value) {
            this.descripcionCr = value;
        }

        /**
         * Obtiene el valor de la propiedad indicadorBanco.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndicadorBanco() {
            return indicadorBanco;
        }

        /**
         * Define el valor de la propiedad indicadorBanco.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndicadorBanco(String value) {
            this.indicadorBanco = value;
        }

        /**
         * Obtiene el valor de la propiedad nombreCliente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNombreCliente() {
            return nombreCliente;
        }

        /**
         * Define el valor de la propiedad nombreCliente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNombreCliente(String value) {
            this.nombreCliente = value;
        }

        /**
         * Obtiene el valor de la propiedad primerApellido.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPrimerApellido() {
            return primerApellido;
        }

        /**
         * Define el valor de la propiedad primerApellido.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPrimerApellido(String value) {
            this.primerApellido = value;
        }

        /**
         * Obtiene el valor de la propiedad segundoApellido.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSegundoApellido() {
            return segundoApellido;
        }

        /**
         * Define el valor de la propiedad segundoApellido.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSegundoApellido(String value) {
            this.segundoApellido = value;
        }

        /**
         * Obtiene el valor de la propiedad nombreCompleto.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNombreCompleto() {
            return nombreCompleto;
        }

        /**
         * Define el valor de la propiedad nombreCompleto.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNombreCompleto(String value) {
            this.nombreCompleto = value;
        }

        /**
         * Obtiene el valor de la propiedad codigoIdentifica.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoIdentifica() {
            return codigoIdentifica;
        }

        /**
         * Define el valor de la propiedad codigoIdentifica.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoIdentifica(String value) {
            this.codigoIdentifica = value;
        }

        /**
         * Obtiene el valor de la propiedad claveIdentifica.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getClaveIdentifica() {
            return claveIdentifica;
        }

        /**
         * Define el valor de la propiedad claveIdentifica.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setClaveIdentifica(String value) {
            this.claveIdentifica = value;
        }

        /**
         * Obtiene el valor de la propiedad curp.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCurp() {
            return curp;
        }

        /**
         * Define el valor de la propiedad curp.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCurp(String value) {
            this.curp = value;
        }

        /**
         * Obtiene el valor de la propiedad imss.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getImss() {
            return imss;
        }

        /**
         * Define el valor de la propiedad imss.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setImss(String value) {
            this.imss = value;
        }

        /**
         * Obtiene el valor de la propiedad fielShcp.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFielShcp() {
            return fielShcp;
        }

        /**
         * Define el valor de la propiedad fielShcp.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFielShcp(String value) {
            this.fielShcp = value;
        }

        /**
         * Obtiene el valor de la propiedad fechaNacimiento.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getFechaNacimiento() {
            return fechaNacimiento;
        }

        /**
         * Define el valor de la propiedad fechaNacimiento.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setFechaNacimiento(Integer value) {
            this.fechaNacimiento = value;
        }

        /**
         * Obtiene el valor de la propiedad sexo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSexo() {
            return sexo;
        }

        /**
         * Define el valor de la propiedad sexo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSexo(String value) {
            this.sexo = value;
        }

        /**
         * Obtiene el valor de la propiedad estadoCivil.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEstadoCivil() {
            return estadoCivil;
        }

        /**
         * Define el valor de la propiedad estadoCivil.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEstadoCivil(String value) {
            this.estadoCivil = value;
        }

        /**
         * Obtiene el valor de la propiedad titulo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTitulo() {
            return titulo;
        }

        /**
         * Define el valor de la propiedad titulo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTitulo(String value) {
            this.titulo = value;
        }

        /**
         * Obtiene el valor de la propiedad descripcionTitulo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescripcionTitulo() {
            return descripcionTitulo;
        }

        /**
         * Define el valor de la propiedad descripcionTitulo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescripcionTitulo(String value) {
            this.descripcionTitulo = value;
        }

        /**
         * Obtiene el valor de la propiedad tratamiento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTratamiento() {
            return tratamiento;
        }

        /**
         * Define el valor de la propiedad tratamiento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTratamiento(String value) {
            this.tratamiento = value;
        }

        /**
         * Obtiene el valor de la propiedad tipoDomicilio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoDomicilio() {
            return tipoDomicilio;
        }

        /**
         * Define el valor de la propiedad tipoDomicilio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoDomicilio(String value) {
            this.tipoDomicilio = value;
        }

        /**
         * Obtiene el valor de la propiedad via.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVia() {
            return via;
        }

        /**
         * Define el valor de la propiedad via.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVia(String value) {
            this.via = value;
        }

        /**
         * Obtiene el valor de la propiedad calle.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCalle() {
            return calle;
        }

        /**
         * Define el valor de la propiedad calle.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCalle(String value) {
            this.calle = value;
        }

        /**
         * Obtiene el valor de la propiedad nombreCalle.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNombreCalle() {
            return nombreCalle;
        }

        /**
         * Define el valor de la propiedad nombreCalle.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNombreCalle(String value) {
            this.nombreCalle = value;
        }

        /**
         * Obtiene el valor de la propiedad numeroCalle.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumeroCalle() {
            return numeroCalle;
        }

        /**
         * Define el valor de la propiedad numeroCalle.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumeroCalle(String value) {
            this.numeroCalle = value;
        }

        /**
         * Obtiene el valor de la propiedad tipoVivienda.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoVivienda() {
            return tipoVivienda;
        }

        /**
         * Define el valor de la propiedad tipoVivienda.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoVivienda(String value) {
            this.tipoVivienda = value;
        }

        /**
         * Obtiene el valor de la propiedad descripcionVivienda.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescripcionVivienda() {
            return descripcionVivienda;
        }

        /**
         * Define el valor de la propiedad descripcionVivienda.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescripcionVivienda(String value) {
            this.descripcionVivienda = value;
        }

        /**
         * Obtiene el valor de la propiedad entrada.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEntrada() {
            return entrada;
        }

        /**
         * Define el valor de la propiedad entrada.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEntrada(String value) {
            this.entrada = value;
        }

        /**
         * Obtiene el valor de la propiedad piso.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPiso() {
            return piso;
        }

        /**
         * Define el valor de la propiedad piso.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPiso(String value) {
            this.piso = value;
        }

        /**
         * Obtiene el valor de la propiedad departamento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDepartamento() {
            return departamento;
        }

        /**
         * Define el valor de la propiedad departamento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDepartamento(String value) {
            this.departamento = value;
        }

        /**
         * Obtiene el valor de la propiedad colonia.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getColonia() {
            return colonia;
        }

        /**
         * Define el valor de la propiedad colonia.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setColonia(String value) {
            this.colonia = value;
        }

        /**
         * Obtiene el valor de la propiedad codigoPostal.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCodigoPostal() {
            return codigoPostal;
        }

        /**
         * Define el valor de la propiedad codigoPostal.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCodigoPostal(Integer value) {
            this.codigoPostal = value;
        }

        /**
         * Obtiene el valor de la propiedad codigoDelegMuni.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoDelegMuni() {
            return codigoDelegMuni;
        }

        /**
         * Define el valor de la propiedad codigoDelegMuni.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoDelegMuni(String value) {
            this.codigoDelegMuni = value;
        }

        /**
         * Obtiene el valor de la propiedad descriDelegMuni.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescriDelegMuni() {
            return descriDelegMuni;
        }

        /**
         * Define el valor de la propiedad descriDelegMuni.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescriDelegMuni(String value) {
            this.descriDelegMuni = value;
        }

        /**
         * Obtiene el valor de la propiedad codigoEstado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoEstado() {
            return codigoEstado;
        }

        /**
         * Define el valor de la propiedad codigoEstado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoEstado(String value) {
            this.codigoEstado = value;
        }

        /**
         * Obtiene el valor de la propiedad descripcionEstado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescripcionEstado() {
            return descripcionEstado;
        }

        /**
         * Define el valor de la propiedad descripcionEstado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescripcionEstado(String value) {
            this.descripcionEstado = value;
        }

        /**
         * Obtiene el valor de la propiedad codigoPais.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoPais() {
            return codigoPais;
        }

        /**
         * Define el valor de la propiedad codigoPais.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoPais(String value) {
            this.codigoPais = value;
        }

        /**
         * Obtiene el valor de la propiedad tipoTelefonoDos.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoTelefonoDos() {
            return tipoTelefonoDos;
        }

        /**
         * Define el valor de la propiedad tipoTelefonoDos.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoTelefonoDos(String value) {
            this.tipoTelefonoDos = value;
        }

        /**
         * Obtiene el valor de la propiedad numeroTelefonoDos.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumeroTelefonoDos() {
            return numeroTelefonoDos;
        }

        /**
         * Define el valor de la propiedad numeroTelefonoDos.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumeroTelefonoDos(String value) {
            this.numeroTelefonoDos = value;
        }

        /**
         * Obtiene el valor de la propiedad tipoTelefonoTres.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoTelefonoTres() {
            return tipoTelefonoTres;
        }

        /**
         * Define el valor de la propiedad tipoTelefonoTres.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoTelefonoTres(String value) {
            this.tipoTelefonoTres = value;
        }

        /**
         * Obtiene el valor de la propiedad numeroTelefonoTres.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumeroTelefonoTres() {
            return numeroTelefonoTres;
        }

        /**
         * Define el valor de la propiedad numeroTelefonoTres.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumeroTelefonoTres(String value) {
            this.numeroTelefonoTres = value;
        }

        /**
         * Obtiene el valor de la propiedad tipoTelefonoCuatro.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoTelefonoCuatro() {
            return tipoTelefonoCuatro;
        }

        /**
         * Define el valor de la propiedad tipoTelefonoCuatro.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoTelefonoCuatro(String value) {
            this.tipoTelefonoCuatro = value;
        }

        /**
         * Obtiene el valor de la propiedad numeroTelefonoCuatro.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumeroTelefonoCuatro() {
            return numeroTelefonoCuatro;
        }

        /**
         * Define el valor de la propiedad numeroTelefonoCuatro.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumeroTelefonoCuatro(String value) {
            this.numeroTelefonoCuatro = value;
        }

        /**
         * Obtiene el valor de la propiedad codigoCno.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoCno() {
            return codigoCno;
        }

        /**
         * Define el valor de la propiedad codigoCno.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoCno(String value) {
            this.codigoCno = value;
        }

        /**
         * Obtiene el valor de la propiedad descripCno.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescripCno() {
            return descripCno;
        }

        /**
         * Define el valor de la propiedad descripCno.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescripCno(String value) {
            this.descripCno = value;
        }

        /**
         * Obtiene el valor de la propiedad cnaeg.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCnaeg() {
            return cnaeg;
        }

        /**
         * Define el valor de la propiedad cnaeg.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCnaeg(String value) {
            this.cnaeg = value;
        }

        /**
         * Obtiene el valor de la propiedad indGranCliente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndGranCliente() {
            return indGranCliente;
        }

        /**
         * Define el valor de la propiedad indGranCliente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndGranCliente(String value) {
            this.indGranCliente = value;
        }

        /**
         * Obtiene el valor de la propiedad funcionario.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFuncionario() {
            return funcionario;
        }

        /**
         * Define el valor de la propiedad funcionario.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFuncionario(String value) {
            this.funcionario = value;
        }

        /**
         * Obtiene el valor de la propiedad ultimoNumeroAviso.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getUltimoNumeroAviso() {
            return ultimoNumeroAviso;
        }

        /**
         * Define el valor de la propiedad ultimoNumeroAviso.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setUltimoNumeroAviso(Integer value) {
            this.ultimoNumeroAviso = value;
        }

        /**
         * Obtiene el valor de la propiedad numDomicAlternativ.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getNumDomicAlternativ() {
            return numDomicAlternativ;
        }

        /**
         * Define el valor de la propiedad numDomicAlternativ.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setNumDomicAlternativ(Integer value) {
            this.numDomicAlternativ = value;
        }

        /**
         * Obtiene el valor de la propiedad nombreTarjeta.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNombreTarjeta() {
            return nombreTarjeta;
        }

        /**
         * Define el valor de la propiedad nombreTarjeta.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNombreTarjeta(String value) {
            this.nombreTarjeta = value;
        }

        /**
         * Obtiene el valor de la propiedad segmento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSegmento() {
            return segmento;
        }

        /**
         * Define el valor de la propiedad segmento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSegmento(String value) {
            this.segmento = value;
        }

        /**
         * Obtiene el valor de la propiedad gradoRiesgo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGradoRiesgo() {
            return gradoRiesgo;
        }

        /**
         * Define el valor de la propiedad gradoRiesgo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGradoRiesgo(String value) {
            this.gradoRiesgo = value;
        }

        /**
         * Obtiene el valor de la propiedad cargoPolitico.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCargoPolitico() {
            return cargoPolitico;
        }

        /**
         * Define el valor de la propiedad cargoPolitico.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCargoPolitico(String value) {
            this.cargoPolitico = value;
        }

        /**
         * Obtiene el valor de la propiedad descCargoPolitico.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescCargoPolitico() {
            return descCargoPolitico;
        }

        /**
         * Define el valor de la propiedad descCargoPolitico.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescCargoPolitico(String value) {
            this.descCargoPolitico = value;
        }

        /**
         * Obtiene el valor de la propiedad progLealtadBanorte.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProgLealtadBanorte() {
            return progLealtadBanorte;
        }

        /**
         * Define el valor de la propiedad progLealtadBanorte.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProgLealtadBanorte(String value) {
            this.progLealtadBanorte = value;
        }

        /**
         * Obtiene el valor de la propiedad calireg.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCalireg() {
            return calireg;
        }

        /**
         * Define el valor de la propiedad calireg.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCalireg(String value) {
            this.calireg = value;
        }

        /**
         * Obtiene el valor de la propiedad indPropagandaReus.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndPropagandaReus() {
            return indPropagandaReus;
        }

        /**
         * Define el valor de la propiedad indPropagandaReus.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndPropagandaReus(String value) {
            this.indPropagandaReus = value;
        }

        /**
         * Obtiene el valor de la propiedad indLide.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndLide() {
            return indLide;
        }

        /**
         * Define el valor de la propiedad indLide.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndLide(String value) {
            this.indLide = value;
        }

        /**
         * Obtiene el valor de la propiedad indicadorReferencia.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndicadorReferencia() {
            return indicadorReferencia;
        }

        /**
         * Define el valor de la propiedad indicadorReferencia.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndicadorReferencia(String value) {
            this.indicadorReferencia = value;
        }

        /**
         * Obtiene el valor de la propiedad bxi.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBxi() {
            return bxi;
        }

        /**
         * Define el valor de la propiedad bxi.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBxi(String value) {
            this.bxi = value;
        }

        /**
         * Obtiene el valor de la propiedad altaShcp.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAltaShcp() {
            return altaShcp;
        }

        /**
         * Define el valor de la propiedad altaShcp.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAltaShcp(String value) {
            this.altaShcp = value;
        }

        /**
         * Obtiene el valor de la propiedad indicadorMigrado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndicadorMigrado() {
            return indicadorMigrado;
        }

        /**
         * Define el valor de la propiedad indicadorMigrado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndicadorMigrado(String value) {
            this.indicadorMigrado = value;
        }

        /**
         * Obtiene el valor de la propiedad indicadorRepecos.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndicadorRepecos() {
            return indicadorRepecos;
        }

        /**
         * Define el valor de la propiedad indicadorRepecos.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndicadorRepecos(String value) {
            this.indicadorRepecos = value;
        }

        /**
         * Obtiene el valor de la propiedad indEncuestaWNueve.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndEncuestaWNueve() {
            return indEncuestaWNueve;
        }

        /**
         * Define el valor de la propiedad indEncuestaWNueve.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndEncuestaWNueve(String value) {
            this.indEncuestaWNueve = value;
        }

        /**
         * Obtiene el valor de la propiedad indClienteFatca.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndClienteFatca() {
            return indClienteFatca;
        }

        /**
         * Define el valor de la propiedad indClienteFatca.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndClienteFatca(String value) {
            this.indClienteFatca = value;
        }

        /**
         * Obtiene el valor de la propiedad nacionalidadUno.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNacionalidadUno() {
            return nacionalidadUno;
        }

        /**
         * Define el valor de la propiedad nacionalidadUno.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNacionalidadUno(String value) {
            this.nacionalidadUno = value;
        }

        /**
         * Obtiene el valor de la propiedad descnacionalidadUno.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescnacionalidadUno() {
            return descnacionalidadUno;
        }

        /**
         * Define el valor de la propiedad descnacionalidadUno.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescnacionalidadUno(String value) {
            this.descnacionalidadUno = value;
        }

        /**
         * Obtiene el valor de la propiedad nacionalidadDos.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNacionalidadDos() {
            return nacionalidadDos;
        }

        /**
         * Define el valor de la propiedad nacionalidadDos.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNacionalidadDos(String value) {
            this.nacionalidadDos = value;
        }

        /**
         * Obtiene el valor de la propiedad descnacionalidadDos.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescnacionalidadDos() {
            return descnacionalidadDos;
        }

        /**
         * Define el valor de la propiedad descnacionalidadDos.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescnacionalidadDos(String value) {
            this.descnacionalidadDos = value;
        }

        /**
         * Obtiene el valor de la propiedad socialSecurityNDos.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSocialSecurityNDos() {
            return socialSecurityNDos;
        }

        /**
         * Define el valor de la propiedad socialSecurityNDos.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSocialSecurityNDos(String value) {
            this.socialSecurityNDos = value;
        }

        /**
         * Obtiene el valor de la propiedad taxpayerIdNDos.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTaxpayerIdNDos() {
            return taxpayerIdNDos;
        }

        /**
         * Define el valor de la propiedad taxpayerIdNDos.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTaxpayerIdNDos(String value) {
            this.taxpayerIdNDos = value;
        }

        /**
         * Obtiene el valor de la propiedad nacionalidadTres.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNacionalidadTres() {
            return nacionalidadTres;
        }

        /**
         * Define el valor de la propiedad nacionalidadTres.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNacionalidadTres(String value) {
            this.nacionalidadTres = value;
        }

        /**
         * Obtiene el valor de la propiedad descnacionalidadTres.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescnacionalidadTres() {
            return descnacionalidadTres;
        }

        /**
         * Define el valor de la propiedad descnacionalidadTres.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescnacionalidadTres(String value) {
            this.descnacionalidadTres = value;
        }

        /**
         * Obtiene el valor de la propiedad socialSecurityNTres.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSocialSecurityNTres() {
            return socialSecurityNTres;
        }

        /**
         * Define el valor de la propiedad socialSecurityNTres.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSocialSecurityNTres(String value) {
            this.socialSecurityNTres = value;
        }

        /**
         * Obtiene el valor de la propiedad taxpayerIdNTres.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTaxpayerIdNTres() {
            return taxpayerIdNTres;
        }

        /**
         * Define el valor de la propiedad taxpayerIdNTres.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTaxpayerIdNTres(String value) {
            this.taxpayerIdNTres = value;
        }

        /**
         * Obtiene el valor de la propiedad indResidencia.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndResidencia() {
            return indResidencia;
        }

        /**
         * Define el valor de la propiedad indResidencia.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndResidencia(String value) {
            this.indResidencia = value;
        }

        /**
         * Obtiene el valor de la propiedad paisResidenciaUno.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPaisResidenciaUno() {
            return paisResidenciaUno;
        }

        /**
         * Define el valor de la propiedad paisResidenciaUno.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPaisResidenciaUno(String value) {
            this.paisResidenciaUno = value;
        }

        /**
         * Obtiene el valor de la propiedad descripPaisResidenciaUno.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescripPaisResidenciaUno() {
            return descripPaisResidenciaUno;
        }

        /**
         * Define el valor de la propiedad descripPaisResidenciaUno.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescripPaisResidenciaUno(String value) {
            this.descripPaisResidenciaUno = value;
        }

        /**
         * Obtiene el valor de la propiedad paisResidenciaDos.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPaisResidenciaDos() {
            return paisResidenciaDos;
        }

        /**
         * Define el valor de la propiedad paisResidenciaDos.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPaisResidenciaDos(String value) {
            this.paisResidenciaDos = value;
        }

        /**
         * Obtiene el valor de la propiedad descripPaisResidenciaDos.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescripPaisResidenciaDos() {
            return descripPaisResidenciaDos;
        }

        /**
         * Define el valor de la propiedad descripPaisResidenciaDos.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescripPaisResidenciaDos(String value) {
            this.descripPaisResidenciaDos = value;
        }

        /**
         * Obtiene el valor de la propiedad paisResidenciaTres.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPaisResidenciaTres() {
            return paisResidenciaTres;
        }

        /**
         * Define el valor de la propiedad paisResidenciaTres.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPaisResidenciaTres(String value) {
            this.paisResidenciaTres = value;
        }

        /**
         * Obtiene el valor de la propiedad descripPaisResidenciaTres.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescripPaisResidenciaTres() {
            return descripPaisResidenciaTres;
        }

        /**
         * Define el valor de la propiedad descripPaisResidenciaTres.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescripPaisResidenciaTres(String value) {
            this.descripPaisResidenciaTres = value;
        }

    }

}
