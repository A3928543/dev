/*
 * $Id: PlantillaIntl.java,v 1.19.22.2.2.2 2012/06/18 23:05:03 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaException;
import com.ixe.treasury.dom.common.Pais;

/**
 * Subclase persistente de Plantilla, para la tabla SC_PLANTILLA_INTL.
 *
 * @hibernate.joined-subclass table="SC_PLANTILLA_INTL"
 *
 * @hibernate.joined-subclass-key column="ID_PLANTILLA"
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.19.22.2.2.2 $ $Date: 2012/06/18 23:05:03 $
 */
public class PlantillaIntl extends Plantilla implements IPlantillaIntl {

    /**
     * Constructor por default. Inicializa Parametros.
     */
    public PlantillaIntl() {
        super();
        setInternacional("I");
    }

    /**
     * @see com.ixe.ods.sica.model.IPlantillaIntl#validarCaracteresCuentaBeneficiario().
     */
    public void validarCaracteresCuentaBeneficiario() {
        String cta = getNoCuentaBeneficiario();

        if (cta != null) {
            int longitud = cta.length();

            cta = cta.replaceAll("[^a-zA-Z0-9]", "");
            if (longitud != cta.length()) {
                throw new SicaException("La cuenta del beneficiario contiene caracteres " +
                        "no v\u00e1lidos.");
            }
        }
    }

    /**
     * @see com.ixe.ods.sica.model.IPlantillaIntl#validarCaracteresCuentaIntermediario().
     */
    public void validarCaracteresCuentaIntermediario() {
        String cta = getCuentaBancoInterm();

        if (cta != null) {
            int longitud = cta.length();

            cta = cta.replaceAll("[^a-zA-Z0-9]", "");
            if (longitud != cta.length()) {
                throw new SicaException("La cuenta del beneficiario contiene caracteres no " +
                        "v\u00e1lidos.");
            }
        }
    }
    
    /**
     * Verifica que la estructura sintactica del numero de cuenta del beneficiario
     * cumpla con el codigo IBAN de acuerdo con la clave del banco y pais destino
     * 
     * @param noCuentaBeneficiario
     * @param claveSwift
     * @param pais
     * @return
     */
    public int validarCodigoIBAN(String noCuentaBeneficiario, String claveSwift, Pais pais){
    	
    	try{
	    	String claveIsoSwift = claveSwift.substring(4,6);
			String claveIsoCta = noCuentaBeneficiario.substring(0,2);
	    	
	    	if(noCuentaBeneficiario == null || noCuentaBeneficiario.equals("")
	    			|| claveSwift == null || claveSwift.equals("") || pais == null){
	    		return Constantes.COD_IBAN_LONG_CLAVE_ERR;
	    	}
	    	
	    	if( pais.getLongitudIban() == null ){
	    		//Si el campo es null, no hay nada que validars
	    		return Constantes.COD_IBAN_OK;
	    	}else if(pais.getLongitudIban().intValue() == -1){
	    		//Si no esta definida una longitud solo se valida la clave ISO
    			if( ! claveIsoSwift.toUpperCase().equals(claveIsoCta.toUpperCase())){
    				return Constantes.COD_IBAN_ISO_ERR;
    			}
	    	}
	    	//SE AGREGO ESTA CONDICION CUANDO LA CLAVE Y LONGITUD VIENEN MAL. JDCH 16/06/2012
	    	else if(noCuentaBeneficiario.length() != pais.getLongitudIban().intValue() 
	    			&&  ! claveIsoSwift.toUpperCase().equals(claveIsoCta.toUpperCase()))
	    	{
	    		return Constantes.COD_IBAN_LONG_ERR_ISO_ERR;
	    	}
	    	
	    	
	    	else{
	    		//Si hay una longitud definida, se valida la cuenta proporcionada
	    		if(noCuentaBeneficiario.length() != pais.getLongitudIban().intValue()){
	    			return Constantes.COD_IBAN_LONG_ERR;
	    	}
	    		//Si la longitud es correcta, se valida la clave ISO tambien
	    		else if( ! claveIsoSwift.toUpperCase().equals(claveIsoCta.toUpperCase())){
	    			return Constantes.COD_IBAN_ISO_ERR;
	    		}
	    	}
    	}catch(Exception er){
    		return Constantes.COD_IBAN_OK;
    	}
    	
    	return Constantes.COD_IBAN_OK;
    }

    /**
     * @see IPlantillaIntl#validarCaracteresInstrucciones(String).
     * 
     * @param instrucciones Las instrucciones a validar.
     */
    public void validarCaracteresInstrucciones(String instrucciones) {
        String instr = instrucciones;

        if (instr != null) {
            int longitud = instr.length();

            instr = instr.replaceAll("[^a-zA-Z0-9?)(\\.,\\x20]", "");
            if (longitud != instr.length()) {
                throw new SicaException("Las instrucciones contienen caracteres no v\u00e1lidos.");
            }
        }
    }

    /**
     * Regresa el valor de claveBancoTecleado.
     *
     * @return String.
     */
    public String getClaveBancoTecleado() {
        return claveBancoTecleado;
    }

    /**
     * Establece el valor de claveBancoTecleado.
     *
     * @param claveBancoTecleado El valor a asignar.
     */
    public void setClaveBancoTecleado(String claveBancoTecleado) {
        if (claveBancoTecleado != null) {
            claveBancoTecleado = claveBancoTecleado.toUpperCase();
        }
        this.claveBancoTecleado = claveBancoTecleado;
    }

    /**
     * Regresa el valor de claveBancoIntermTecleado.
     *
     * @return String.
     */
    public String getClaveBancoIntermTecleado() {
        return claveBancoIntermTecleado;
    }

    /**
     * Establece el valor de claveBancoIntermTecleado.
     *
     * @param claveBancoIntermTecleado El valor a asignar.
     */
    public void setClaveBancoIntermTecleado(String claveBancoIntermTecleado) {
        if (claveBancoIntermTecleado != null) {
            claveBancoIntermTecleado = claveBancoIntermTecleado.toUpperCase();
        }
        this.claveBancoIntermTecleado = claveBancoIntermTecleado;
    }

    /**
     * Indica si la Plantilla Puede Procesarse o no.
     * @return boolean.
     */
    public boolean puedeProcesarse() {
    	if (TIPO_CUENTA_BANCO_DD.equals(tipoCuentaBanco.trim())) {
            return _divisa != null && StringUtils.isNotEmpty(tipoCuentaBanco) &&
                    StringUtils.isNotEmpty(noCuentaBeneficiario) && getBeneficiario() != null &&
                    StringUtils.isNotEmpty(nombreBanco);
    	}
    	else {
    		return _divisa != null && StringUtils.isNotEmpty(tipoCuentaBanco) &&
                    StringUtils.isNotEmpty(tipoCuentaBanco) && StringUtils.isNotEmpty(claveBanco) &&
                    idPais != null && StringUtils.isNotEmpty(noCuentaBeneficiario) &&
                    getBeneficiario() != null && StringUtils.isNotEmpty(nombreBanco);
    	}
    }

    /**
     * Regresa Informaci&oacute;n Adicional de la Plantilla como HashMap.
     * @return List.
     */
    public List getInfoAdicional() {
        List list = new ArrayList();
        Map map = new HashMap();
        map.put("nombre", "BENEFICIARIO");
        String benef = getBeneficiario().getNombreCompleto();
        map.put("valor", benef != null ? benef.replaceAll("\\'", "\\\\'") : "");
        list.add(map);
        map = new HashMap();
        map.put("nombre", "CTA. BENEFICIARIO");
        map.put("valor", getNoCuentaBeneficiario());
        list.add(map);
        map = new HashMap();
        map.put("nombre", "BANCO PAGADOR");
        String bco = getNombreBanco() != null ? getNombreBanco().replaceAll("\\'", "\\\\'") : "";
        String tipoCuentaBanco = getTipoCuentaBanco() != null ? getTipoCuentaBanco() : "";
        String claveBanco = getClaveBanco() != null ? getClaveBanco() : "";
        map.put("valor", tipoCuentaBanco + " " + claveBanco + " " + bco);
        list.add(map);
        if(getClaveBancoInterm() != null && getNombreBanco() != null){
        	map = new HashMap();
            map.put("nombre", "BANCO INTERMEDIARIO");
            bco = getNombreBancoInterm() != null ? getNombreBancoInterm().
                    replaceAll("\\'", "\\\\'") : "";
            map.put("valor", getTipoCuentaBancoInterm() + " " + getClaveBancoInterm() + " " +
                    bco);
            list.add(map);
            map = new HashMap();
            map.put("nombre", "CTA. BANCO PAGADOR");
            map.put("valor", getCuentaBancoInterm());
            list.add(map);
        }
        return list;
    }

    /**
     * Regresa el valor de divisa.
     *
     * @hibernate.many-to-one column="ID_DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     * @return Divisa.
     */
    public Divisa getDivisa() {
        return _divisa;
    }

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
        _divisa = divisa;
    }

    /**
     * Regresa el valor de tipoCuentaBanco.
     *
     * @hibernate.property column="TIPO_CUENTA_BANCO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getTipoCuentaBanco() {
        return tipoCuentaBanco;
    }

    /**
     * Fija el valor de tipoCuentaBanco.
     *
     * @param tipoCuentaBanco El valor a asignar.
     */
    public void setTipoCuentaBanco(String tipoCuentaBanco) {
        this.tipoCuentaBanco = tipoCuentaBanco;
    }

    /**
     * Regresa el valor de tipoCuentaBancoInterm.
     *
     * @hibernate.property column="TIPO_CUENTA_BANCO_INTERM"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getTipoCuentaBancoInterm() {
        return tipoCuentaBancoInterm;
    }

    /**
     * Fija el valor de tipoCuentaBancoInterm.
     *
     * @param tipoCuentaBancoInterm El valor a asignar.
     */
    public void setTipoCuentaBancoInterm(String tipoCuentaBancoInterm) {
        this.tipoCuentaBancoInterm = tipoCuentaBancoInterm;
    }

    /**
     * Regresa el valor de claveBanco.
     *
     * @hibernate.property column="CLAVE_BANCO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getClaveBanco() {
        return claveBanco;
    }

    /**
     * Fija el valor de claveBanco.
     *
     * @param claveBanco El valor a asignar.
     */
    public void setClaveBanco(String claveBanco) {
        if (claveBanco != null) {
            claveBanco = claveBanco.toUpperCase();
        }
        this.claveBanco = claveBanco;
    }

    /**
     * Regresa el valor de claveBancoInterm.
     *
     * @hibernate.property column="CLAVE_BANCO_INTERM"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getClaveBancoInterm() {
        return claveBancoInterm;
    }

    /**
     * Fija el valor de claveBancoInterm.
     *
     * @param claveBancoInterm El valor a asignar.
     */
    public void setClaveBancoInterm(String claveBancoInterm) {
        if (claveBancoInterm != null) {
            claveBancoInterm = claveBancoInterm.toUpperCase();
        }
        this.claveBancoInterm = claveBancoInterm;
    }

    /**
     * Regresa el valor de claveBancoInterm.
     *
     * @hibernate.property column="ID_PAIS"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getIdPais() {
        return idPais;
    }

    /**
     * Fija el valor de idPais.
     *
     * @param idPais El valor a asignar.
     */
    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    /**
     * Regresa el valor de idPaisInterm.
     *
     * @hibernate.property column="ID_PAIS_INTERM"
     * not-null="false"
     * unique="false"
     * @return String.
     */

    public String getIdPaisInterm() {
        return idPaisInterm;
    }

    /**
     * Establece el valor de idPaisInterm.
     *
     * @param idPaisInterm El valor a asignar.
     */
    public void setIdPaisInterm(String idPaisInterm) {
        this.idPaisInterm = idPaisInterm;
    }

    /**
     * Regresa el valor de noCuentaBeneficiario.
     *
     * @hibernate.property column="NO_CUENTA_BENEFICIARIO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getNoCuentaBeneficiario() {
        return noCuentaBeneficiario;
    }

    /**
     * Fija el valor de noCuentaBeneficiario.
     *
     * @param noCuentaBeneficiario El valor a asignar.
     */
    public void setNoCuentaBeneficiario(String noCuentaBeneficiario) {
        this.noCuentaBeneficiario = noCuentaBeneficiario;
    }

    /**
     * Regresa el valor de nombreBanco.
     *
     * @hibernate.property column="NOMBRE_BANCO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getNombreBanco() {
        return nombreBanco;
    }

    /**
     * Fija el valor de nombreBanco.
     *
     * @param nombreBanco El valor a asignar.
     */
    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    /**
     * Regresa el valor de cuentaBancoInterm.
     *
     * @hibernate.property column="CUENTA_BANCO_INTERM"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getCuentaBancoInterm() {
        return cuentaBancoInterm;
    }

    /**
     * Fija el valor de cuentaBancoInterm.
     *
     * @param cuentaBancoInterm El valor a asignar.
     */
    public void setCuentaBancoInterm(String cuentaBancoInterm) {
        this.cuentaBancoInterm = cuentaBancoInterm;
    }

    /**
     * Regresa el valor de nombreBancoInterm.
     *
     * @hibernate.property column="NOMBRE_BANCO_INTERM"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getNombreBancoInterm() {
        return nombreBancoInterm;
    }

    /**
     * Fija el valor de nombreBancoInterm.
     *
     * @param nombreBancoInterm El valor a asignar.
     */
    public void setNombreBancoInterm(String nombreBancoInterm) {
        this.nombreBancoInterm = nombreBancoInterm;
    }

    /**
     * Regresa el valor de instrBcoBeneficiario.
     *
     * @hibernate.property column="INSTR_BCO_BENEFICIARIO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getInstrBcoBeneficiario(){
    	return instrBcoBeneficiario;
    }

    /**
     * Fija el valor de instrBcoBeneficiario.
     *
     * @param instrBcoBeneficiario El valor a asignar.
     */
    public void setInstrBcoBeneficiario(String instrBcoBeneficiario) {
        this.instrBcoBeneficiario = instrBcoBeneficiario;
    }

    /**
     * Regresa el valor de instrBcoIntermediario.
     *
     * @hibernate.property column="INSTR_BCO_INTERMEDIARIO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getInstrBcoIntermediario(){
    	return instrBcoIntermediario;
    }

    /**
     * Fija el valor de instrBcoIntermediario.
     *
     * @param instrBcoIntermediario El valor a asignar.
     */
    public void setInstrBcoIntermediario(String instrBcoIntermediario){
    	this.instrBcoIntermediario = instrBcoIntermediario;
    }

    /**
     * Regresa true si el tipoCuentaBanco es Direct Deposit.
     *
     * @return boolean.
     */
    public boolean isTipoCuentaBancoDD() {
        return getTipoCuentaBanco() != null &&
                TIPO_CUENTA_BANCO_DD.equals(getTipoCuentaBanco().trim());
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof IPlantillaIntl)) {
            return false;
        }
        IPlantillaIntl castOther = (IPlantillaIntl) other;
        return new EqualsBuilder().append(this.getIdPlantilla(),
                castOther.getIdPlantilla()).isEquals();
    }

    /**
     * Variable no persistente utilizada para validar que el usuario no cambie la clave del banco
     * beneficiario una vez seleccionado un banco v&aacute;lido.
     */
    private String claveBancoTecleado;

    /**
     * Variable no persistente utilizada para validar que el usuario no cambie la clave del banco
     * beneficiario una vez seleccionado un banco intermediario v&aacute;lido.
     */
    private String claveBancoIntermTecleado;
    
    /**
     * Relaci&oacute;n muchos-a-uno con Divisa.
     */
    private Divisa _divisa;

    /**
     * ABA | SWIFT | CHIPS | DD.
     */
    private String tipoCuentaBanco;

    /**
     * ABA | SWIFT | CHIPS | DD.
     */
    private String tipoCuentaBancoInterm;

    /**
     * Clave ABA, SWIFT, CHIPS o DD del banco que se trate. El formato depende de tipoCuentaBanco.
     */
    private String claveBanco;

    /**
     * Clave ABA, SWIFT, CHIPS o DD del banco intermediario que se trate. El formato depende de
     * tipoCuentaBancoInterm.
     */
    private String claveBancoInterm;

    /**
     * Clave iso del pa&iacute;s.
     */
    private String idPais;

    /**
     * Clave iso del pa&iacute; del banco intermediario.
     */
    private String idPaisInterm;

    /**
     * El n&uacute;mero de cuenta del beneficiario.
     */
    private String noCuentaBeneficiario;

    /**
     * El Nombre del Banco.
     */
    private String nombreBanco;

    /**
     * El n&uacute;mero de cuenta del banco intermediario. (No se encuentra en BancoIntl).
     */
    private String cuentaBancoInterm;

    /**
     * El nombre del banco intermediario. (No se encuentra en BancoIntl).
     */
    private String nombreBancoInterm;

    /**
     * Las instrucciones al banco receptor.
     */
    private String instrBcoBeneficiario;

    /**
     * Las instrucciones al banco intermediario.
     */
    private String instrBcoIntermediario;

    /**
     * Constante Tipo Cuenta Banco ABA.
     */
    public static final String TIPO_CUENTA_BANCO_ABA = "ABA";

    /**
     * Constante Tipo Cuenta Banco SWIFT.
     */
    public static final String TIPO_CUENTA_BANCO_SWIFT = "SWFT";

    /**
     * Constante Tipo Cuenta Banco CHIP.
     */
    public static final String TIPO_CUENTA_BANCO_CHIPS = "CHIP";

    /**
     * Constante Tipo Cuenta Banco DD.
     */
    public static final String TIPO_CUENTA_BANCO_DD = "DD";

    /**
     * Constante Tipo Cuenta Banco ALRA.
     */
    public static final String TIPO_CUENTA_BANCO_ALRA = "ALRA";

    /**
     * Constante Arreglo que contiene los Tipos Cuenta Banco.
     */
    public static final String[] TIPOS_CUENTA_BANCO = new String[] {
            TIPO_CUENTA_BANCO_ABA, TIPO_CUENTA_BANCO_SWIFT, TIPO_CUENTA_BANCO_CHIPS,
            TIPO_CUENTA_BANCO_DD
    };

    /**
     * Constante Arreglo que contiene los Tipos Cuenta Banco excepto el DD.
     */
    public static final String[] TIPOS_CUENTA_BANCO_SIN_DD = new String[] {
        TIPO_CUENTA_BANCO_ABA, TIPO_CUENTA_BANCO_SWIFT, TIPO_CUENTA_BANCO_CHIPS
    };

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -5537250416189064601L;
}