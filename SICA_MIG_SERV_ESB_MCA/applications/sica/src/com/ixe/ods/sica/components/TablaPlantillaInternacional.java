/*
 * $Id: TablaPlantillaInternacional.java,v 1.15.42.2.2.2 2012/06/18 23:02:42 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.IPlantillaIntl;
import com.ixe.ods.sica.model.PlantillaIntl;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.treasury.dom.common.Banco;
import com.ixe.treasury.dom.common.Pais;
import com.legosoft.tapestry.model.RecordSelectionModel;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

/**
 * Componente que permite al promotor capturar los datos de una plantilla de transferencia
 * internacional en dos modalidades: con y sin banco intermediario. Contiene la l&oacute;gica para
 * encontrar bancos internacionales de acuerdo a su clave ABA, CHIPS o SWIFT.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.15.42.2.2.2 $ $Date: 2012/06/18 23:02:42 $
 */
public class TablaPlantillaInternacional extends BaseComponent {

    /**
     * Constructor por default.
     */
    public TablaPlantillaInternacional() {
        super();
    }

    /**
     * Regresa la ciudad y el estado del banco de la plantilla.
     *
     * @param plantilla La plantilla a inspeccionar.
     * @param interm True si se desea con intermediario.
     * @return String.
     */
    public String plazaBanco(IPlantillaIntl plantilla, boolean interm) {
        try {
            if (plantilla != null) {
                SicaSiteCache ssc = (SicaSiteCache) ((SicaPage) getPage()).getApplicationContext().
                        getBean("sicaSiteCache");
                Banco banco = ssc.encontrarBanco(((Visit) getPage().getVisit()).getTicket(),
                        plantilla, interm);
                return banco.getPlaza() != null ? banco.getPlaza() + " " : "";
            }
        }
        catch (SicaException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
        catch (NullPointerException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
        return "";
    }

    /**
     * Regresa true si se est&aacute; en modo de con intermediario.
     *
     * @return boolean.
     */
    public boolean isConIntermediario() {
        return CON_INTERMEDIARIO.equals(getBinding("sinConIntermediario").getString());
    }
    
    /**
     * Regresa el valor del n&uacute;mero de cuenta del Cliente.
     * 
     * @return String
     */
    public String noCuenta() {
        return getBinding("noCuenta").getString();
    }
    
    /**
     * Obtiene la lista de beneficiarios del cliente.
     * 
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getBeneficiarios() {
    	SicaPage page = (SicaPage) getPage();
    	List beneficiarios = page.getSicaServiceData().
                findBeneficiariosByNoCuentaByIdRol(noCuenta(), FacultySystem.BENEFICIARIO_SICA_ROL);
    	Persona primerElemento = new Persona();
    	primerElemento.setIdPersona(new Integer(-1));
    	primerElemento.setNombreCorto("Seleccione un Beneficiario");
    	beneficiarios.add(0, primerElemento);
    	return new RecordSelectionModel(beneficiarios, "idPersona", "nombreCorto");
    }
    
    /**
     * Realiza la validacion del codigo IBAN en la plantilla que recibe como 
     * parametro
     * 
     * @param ssc
     * @param plantilla
     * @param ticket
     */
    public static void validarCodigoIBAN(SicaSiteCache ssc, IPlantillaIntl plantilla, String ticket){
    	
    	if(plantilla.getIdPais() != null){
	    	Pais tesPais = ssc.getPaisXClaveISO(ticket, plantilla.getIdPais());
				
	    	int resValidacionIBAN = plantilla.validarCodigoIBAN(plantilla.getNoCuentaBeneficiario(), 
					plantilla.getClaveBanco() , tesPais);
			if( resValidacionIBAN != Constantes.COD_IBAN_OK){
				String noCaracteres = (tesPais.getLongitudIban() == null || tesPais.getLongitudIban().intValue() == -1) ? "hasta 34 ": tesPais.getLongitudIban()+"";
				throw new SicaException("La cuenta del beneficiario capturada no " +
						"cumple con las validaciones establecidas para el pa\u00EDs por el " +
						"Comit\u00E9 Europeo de Est\u00E1ndares Bancarios. " +
						//Mensaje para cuando la clave y longitud del Iban vienen mal JDCH 18/06/2012
						(resValidacionIBAN == Constantes.COD_IBAN_LONG_ERR_ISO_ERR ? 						
						"Debe comenzar " +
						"con el c\u00F3digo ISO correspondiente al pa\u00EDs ("+plantilla.getIdPais()+") seguido de caracteres " +
						"sin espacios intermedios. Y debe tener una longitud de " + noCaracteres +" caracteres." : "") + 
						
						(resValidacionIBAN == Constantes.COD_IBAN_ISO_ERR ? 						
						"Debe comenzar " +
						"con el c\u00F3digo ISO correspondiente al pa\u00EDs ("+plantilla.getIdPais()+") seguido de caracteres " +
						"sin espacios intermedios." : "") + 
						
						(resValidacionIBAN == Constantes.COD_IBAN_LONG_ERR ? 
						"Debe tener una logitud de "+ noCaracteres +" caracteres." :"") +
						
						(resValidacionIBAN == Constantes.COD_IBAN_LONG_CLAVE_ERR ? "Debe comenzar " +
						"con el c\u00F3digo ISO correspondiente al pa\u00EDs ("+plantilla.getIdPais()+") seguido de caracteres " +
						"sin espacios intermedios, con una logitud de "+ noCaracteres +" caracteres." : ""));
			}
    	}
		
    }
     
    /**
     * Obtiene la lista de tipos de cuenta para bancos intermediario sin la
     * opcion de Direct Deposit.
     * 
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getTiposCuentaBancoIntermComboBox() {
    	return new StringPropertySelectionModel(PlantillaIntl.TIPOS_CUENTA_BANCO_SIN_DD);
    }
    
    /**
     * Obtiene la lista de tipos de cuenta para bancos intermediario con la
     * opcion de Direct Deposit.
     * 
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getTiposCuentaBancoIntermComboBoxConDD() {
    	return new StringPropertySelectionModel(PlantillaIntl.TIPOS_CUENTA_BANCO);
    }
    
    /**
     * Llamado por la p&aacute;gina que contiene a este componente para asignar los campos de
     * cuentaBancoInterm, tipoCuentaBancoInterm, nombreBancoInterm y tipoCuentaBanco, claveBanco,
     * nombreBanco y idPais, dependiendo si es con o sin intermediario.
     *
     * @param plantilla La plantilla a modificar.
     * @param isConIntermediario Si es con o sin intermediario.
     */
    public static void definirSinConIntermediario(IPlantillaIntl plantilla,
    		boolean isConIntermediario) {
    	if (isConIntermediario) {
    		plantilla.setClaveBanco("");
    		plantilla.setNombreBanco("");
    		plantilla.setIdPais(null);
    		plantilla.setIdPaisInterm(null);
    	}
    	else {
    		if (!PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(
    				plantilla.getTipoCuentaBancoInterm().trim())){
    			plantilla.setCuentaBancoInterm(null);
    		}
    		plantilla.setClaveBancoInterm(null);
    		plantilla.setNombreBancoInterm(null);
    		plantilla.setTipoCuentaBancoInterm(null);
    	}
    }

    /**
     * Llamado por la p&aacute;gina que contiene a este componente para limpiar los campos de la
     * plantilla claveBancoInterm y nombreBancoInterm si la plantilla es con intermediario; o
     * claveBanco, nombreBanco e idPais de la plantilla si es sin intermediario.
     *
     * @param plantilla La plantilla a modificar.
     * @param interm Si es con o sin intermediario.
     */
    public static void procesarTipoCuentaBanco(IPlantillaIntl plantilla, boolean interm) {
        if (interm) {
            plantilla.setClaveBancoInterm("");
            plantilla.setNombreBancoInterm("");
            plantilla.setIdPaisInterm(null);
        }
        else {
    	    plantilla.setClaveBanco("");
            plantilla.setNombreBanco("");
            plantilla.setIdPais(null);
        }
    }
    
    /**
     * Limpia los valores del banco beneficiario e intermediario.
     * 
     * @param plantilla La plantilla a modificar.
     */
    public static void limpiarValores(IPlantillaIntl plantilla) {
    	plantilla.setTipoCuentaBanco(PlantillaIntl.TIPO_CUENTA_BANCO_ABA);
    	plantilla.setClaveBanco("");
        plantilla.setNombreBanco("");
        plantilla.setIdPais(null);
        plantilla.setTipoCuentaBancoInterm(null);
        plantilla.setClaveBancoInterm(null);
        plantilla.setNombreBancoInterm(null);
    	plantilla.setCuentaBancoInterm(null);
    }

    /**
     * Regresa true si el nombre del banco de la plantilla no est&aacute; vac&iacute;o.
     *
     * @param plantilla La plantilla internacional a revisar.
     * @return boolean.
     */
    public boolean isEmptyNombreBanco(IPlantillaIntl plantilla) {
    	return StringUtils.isEmpty(plantilla.getNombreBanco());
    }

    /**
     * El objeto para logging.
     */
    private final transient Log _logger = LogFactory.getLog(getClass());

    /**
     * Constante para el modo de submit del combo de con o sin intermediario.
     */
    public static final String MODO_SUBMIT_CON_SIN_INTERMEDIARIO = "conSinIntermediario";

    /**
     * Constante para el modo de submit cuando el usuario tecle&oacute; la clave del banco
     * intermediario.
     */
    public static final String MODO_SUBMIT_CLAVE_BANCO_INTERM = "claveBancoInterm";

    /**
     * Constante para el modo de submit cuando el usuario tecle&oacute; la clave del banco
     * beneficiario.
     */
    public static final String MODO_SUBMIT_CLAVE_BANCO = "claveBanco";

    /**
     * Constante para el modo de submit cuando el usuario seleccion&oacute; el tipo de clave del
     * banco beneficiario.
     */
    public static final String MODO_SUBMIT_TPO_CTA_BCO = "tipoCuentaBanco";

    /**
     * Constante para el modo de submit cuando el usuario seleccion&oacute; el tipo de clave del
     * banco intermediario.
     */
    public static final String MODO_SUBMIT_TPO_CTA_BCO_INTERM = "tipoCuentaBancoInterm";

    /**
     * Constante para definir el modo Con Banco Intermediario.
     */
    public static final String CON_INTERMEDIARIO = "Con Intermediario";

    /**
     * Constante para definir el modo Sin Banco Intermediario. 
     */
    public static final String SIN_INTERMEDIARIO = "Sin Intermediario";
}
