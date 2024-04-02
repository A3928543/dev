/*
 * $Id: IPlantillaIntl.java,v 1.17.22.1.2.1 2012/05/28 18:05:16 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import com.ixe.treasury.dom.common.Pais;

/**
 * Interface Proxy para las Plantillas de Transferencias Internacionales.
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.17.22.1.2.1 $ $Date: 2012/05/28 18:05:16 $
 */
public interface IPlantillaIntl extends IPlantilla {

    /**
     * Revisa que la cuenta del beneficiario solo contenga caracteres v&aacute;lidos. Levanta una
     * SicaException si no es as&iacute;.
     */
    void validarCaracteresCuentaBeneficiario();

    /**
     * Revisa que la cuenta del pagador en el banco intermediario solo contenga caracteres
     * v&aacute;lidos. Levanta una SicaException si no es as&iacute;.
     */
    void validarCaracteresCuentaIntermediario();
    
    /**
     * Verifica que la estructura sintactica del numero de cuenta del beneficiario
     * cumpla con el codigo IBAN de acuerdo con la clave del banco y pais destino
     *  
     * @param noCuentaBeneficiario
     * @param claveSwift
     * @param pais
     * @return
     */
    public int validarCodigoIBAN(String noCuentaBeneficiario, String claveSwift, Pais pais);

    /**
     * Revisa que las instrucciones solo contengan caracteres v&aacute;lidos. Levanta una
     * SicaException si no es as&iacute;.
     *
     * @param instrucciones Las instrucciones a validar.
     */
    void validarCaracteresInstrucciones(String instrucciones);

    /**
     * Regresa el valor de claveBancoTecleado.
     *
     * @return String.
     */
    String getClaveBancoTecleado();

    /**
     * Establece el valor de claveBancoTecleado.
     *
     * @param claveBancoTecleado El valor a asignar.
     */
    void setClaveBancoTecleado(String claveBancoTecleado);

    /**
     * Regresa el valor de claveBancoIntermTecleado.
     *
     * @return String.
     */
    String getClaveBancoIntermTecleado();

    /**
     * Establece el valor de claveBancoIntermTecleado.
     *
     * @param claveBancoIntermTecleado El valor a asignar.
     */
    void setClaveBancoIntermTecleado(String claveBancoIntermTecleado);

    /**
     * Regresa el valor de claveBanco.
     *
     * @return String.
     */
    String getClaveBanco();

    /**
     * Fija el valor de claveBanco.
     *
     * @param claveBanco El valor a asignar.
     */
    void setClaveBanco(String claveBanco);

    /**
     * Regresa el valor de claveBancoInterm.
     *
     * @return String.
     */
    String getClaveBancoInterm();

    /**
     * Fija el valor de claveBancoInterm.
     *
     * @param claveBancoInterm El valor a asignar.
     */
    void setClaveBancoInterm(String claveBancoInterm);

    /**
     * Regresa el valor de cuentaBancoInterm.
     *
     * @return String.
     */
    String getCuentaBancoInterm();

    /**
     * Fija el valor de cuentaBancoInterm.
     *
     * @param cuentaBancoInterm El valor a asignar.
     */
    void setCuentaBancoInterm(String cuentaBancoInterm);

    /**
     * Regresa el valor de noCuentaBeneficiario.
     *
     * @return String.
     */
    String getNoCuentaBeneficiario();

    /**
     * Fija el valor de noCuentaBeneficiario.
     *
     * @param noCuentaBeneficiario El valor a asignar.
     */
    void setNoCuentaBeneficiario(String noCuentaBeneficiario);

    /**
     * Regresa el valor de nombreBanco.
     *
     * @return String.
     */
    String getNombreBanco();

    /**
     * Fija el valor de nombreBanco.
     *
     * @param nombreBanco El valor a asignar.
     */
    void setNombreBanco(String nombreBanco);

    /**
     * Regresa el valor de nombreBancoInterm.
     *
     * @return String.
     */
    String getNombreBancoInterm();

    /**
     * Fija el valor de nombreBancoInterm.
     *
     * @param nombreBancoInterm El valor a asignar.
     */
    public void setNombreBancoInterm(String nombreBancoInterm);

    /**
     * Regresa el valor de tipoCuentaBanco.
     *
     * @return String.
     */
    String getTipoCuentaBanco();

    /**
     * Fija el valor de tipoCuentaBanco.
     *
     * @param tipoCuentaBanco El valor a asignar.
     */
    void setTipoCuentaBanco(String tipoCuentaBanco);

    /**
     * Regresa el valor de tipoCuentaBancoInterm.
     *
     * @hibernate.property column="TIPO_CUENTA_BANCO_INTERM"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    String getTipoCuentaBancoInterm();

    /**
     * Fija el valor de tipoCuentaBancoInterm.
     *
     * @param tipoCuentaBancoInterm El valor a asignar.
     */
    void setTipoCuentaBancoInterm(String tipoCuentaBancoInterm);

    /**
     * Regresa el valor de divisa.
     *
     * @return Divisa.
     */
    Divisa getDivisa();

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    void setDivisa(Divisa divisa);

    /**
     * Regresa el valor de claveBancoInterm.
     *
     * @return String.
     */
    String getIdPais();

    /**
     * Fija el valor de idPais.
     *
     * @param idPais El valor a asignar.
     */
    void setIdPais(String idPais);

    /**
     * Regresa el valor de idPaisInterm.
     *
     * @return String.
     */
    String getIdPaisInterm();

    /**
     * Fija el valor de idPaisInterm.
     *
     * @param idPaisInterm El valor a asignar.
     */
    void setIdPaisInterm(String idPaisInterm);

    /**
     * Regresa el valor de instrBcoBeneficiario.
     *
     * @return String.
     */
    String getInstrBcoBeneficiario();

    /**
     * Fija el valor de instrBcoBeneficiario.
     *
     * @param instrBcoBeneficiario El valor a asignar.
     */
    void setInstrBcoBeneficiario(String instrBcoBeneficiario);

    /**
     * Regresa el valor de instrBcoIntermediario.
     *
     * @return String.
     */
    String getInstrBcoIntermediario();

    /**
     * Fija el valor de instrBcoIntermediario.
     *
     * @param instrBcoIntermediario El valor a asignar.
     */
    void setInstrBcoIntermediario(String instrBcoIntermediario);

    /**
     * Regresa true si el tipoCuentaBanco es Direct Deposit.
     *
     * @return boolean.
     */
    boolean isTipoCuentaBancoDD();   
}
