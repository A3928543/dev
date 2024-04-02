/*
 * $Id: EdicionPlantillaIntl.java,v 1.21.22.1 2012/02/22 01:35:22 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.plantillas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.components.TablaPlantillaInternacional;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.IPlantillaIntl;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaIntl;
import com.ixe.ods.sica.model.PlantillaPantalla;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.deals.CapturaBanco;
import com.ixe.ods.sica.pages.mesa.MigracionPlantillas;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.treasury.dom.common.Banco;
import com.ixe.treasury.dom.common.FormaPagoLiq;

/**
 * Clase para el Alta de Plantillas de Transferencias Internacionales.
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.21.22.1 $ $Date: 2012/02/22 01:35:22 $
 */
public abstract class EdicionPlantillaIntl extends SicaPage {

    /**
     * Cada vez que se tiene acceso a la pagina carga sus parametros iniciales.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        SicaServiceData sd = getSicaServiceData();
        List plantillasPantalla = sd.findMnemonicosByPlantillaPantalla(NOM_PANT_PLANT_INTL);
        List fpls = new ArrayList();
        for (Iterator it = plantillasPantalla.iterator(); it.hasNext();) {
            PlantillaPantalla pp = (PlantillaPantalla) it.next();
            FormaPagoLiq fpl = getFormaPagoLiq(pp.getMnemonico());
            if (fpl != null && fpl.getDesplegableSica().booleanValue() &&
                    Constantes.MNEMONICO_ACTIVO.equals(fpl.getStatus())) {
                fpls.add(fpl);
            }
            else if (_logger.isWarnEnabled()) {
                _logger.warn("No se encontr\u00f3 la formaPagoLiq con el mnem\u00f3nico " +
                        pp.getMnemonico() + ". Probablemente se encuentre inactivo.");
            }
        }
        setFormasPagoLiq(fpls);
        // Limpiando las variables persistentes
        setInstruccionesPagador(null);
        setInstruccionesIntermediario(null);
    }

    /**
     * Regresa las formas de pago como una matriz de tres columnas.
     *
     * @return FormaPagoLiq[][].
     */
    public FormaPagoLiq[][] getMatrizFpls() {
        int renglones = (getFormasPagoLiq().size() / Num.I_3) +
                (getFormasPagoLiq().size() % Num.I_3);
        FormaPagoLiq[][] matriz = new FormaPagoLiq[renglones][Num.I_3];
        int y = 0;
        int x = 0;
        for (Iterator it = getFormasPagoLiq().iterator(); it.hasNext();) {
            matriz[y][x] = (FormaPagoLiq) it.next();
            x++;
            if (x > 2) {
                x = 0;
                y++;
            }
        }
        return matriz;
    }

    /**
     * Inserta en la Base de Datos una Plantilla de Transferencia Internacional.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {
        IPlantillaIntl plantilla = getPlantilla();
        if (MODO_SUBMIT_MNEMONICO.equals(getModoSubmit())) {
            plantilla.setDivisa((Divisa) getSicaServiceData().find(Divisa.class, getFormaPagoLiq(
                    plantilla.getMnemonico()).getIdDivisa()));
            TablaPlantillaInternacional.limpiarValores(plantilla);
            return;
        }
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        if (delegate != null && delegate.getHasErrors()) {
            return;
        }
        if (StringUtils.isEmpty(plantilla.getMnemonico())) {
        	delegate.record("Debe seleccionar un Mnem\u00f3nico.", null);
        	return;
        }
        try {
            if (StringUtils.isNotEmpty(plantilla.getClavePlantilla())) {
                plantilla.setClavePlantilla(plantilla.getClavePlantilla().toUpperCase());
            }
            plantilla.setNoCuentaBeneficiario(plantilla.getNoCuentaBeneficiario().toUpperCase());
            if (StringUtils.isNotEmpty(plantilla.getClaveBancoInterm())) {
                plantilla.setClaveBancoInterm(plantilla.getClaveBancoInterm().toUpperCase());
            }
            plantilla.setClaveBanco(plantilla.getClaveBanco() != null ?
                    plantilla.getClaveBanco().toUpperCase() : null);
            if (StringUtils.isNotEmpty(plantilla.getCuentaBancoInterm())) {
                plantilla.setCuentaBancoInterm(plantilla.getCuentaBancoInterm().toUpperCase());
            }
            SicaSiteCache ssc = (SicaSiteCache) getApplicationContext().getBean("sicaSiteCache");
            if (TablaPlantillaInternacional.MODO_SUBMIT_CON_SIN_INTERMEDIARIO.
                    equals(getModoSubmit())) {
                TablaPlantillaInternacional.definirSinConIntermediario(plantilla,
                        isConIntermediario());
            }
            else if (TablaPlantillaInternacional.MODO_SUBMIT_TPO_CTA_BCO.equals(getModoSubmit())) {
                if (PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(plantilla.
                        getTipoCuentaBanco().trim())) {
                    if (plantilla.getDivisa() == null || !Divisa.DOLAR.equals(plantilla.
                            getDivisa().getIdDivisa())) {
                        throw new SicaException("Para poder utilizar Direct Deposit la " +
                                "plantilla debe tener la divisa USD.");
                    }
                    if (plantilla.getTipoCuentaBanco() == null) {
                    	delegate.record("No se ha definido el nombre del banco beneficiario.",
                                null);
                    	return;
                    }
                	plantilla.setTipoCuentaBanco(plantilla.getTipoCuentaBanco());
                    plantilla.setClaveBanco(null);
                    plantilla.setNombreBanco(plantilla.getNombreBanco());
                    plantilla.setIdPais(null);
            	}
            	else {
            		TablaPlantillaInternacional.procesarTipoCuentaBanco(plantilla, false);
            	}
            }
            else if (TablaPlantillaInternacional.MODO_SUBMIT_CLAVE_BANCO.equals(getModoSubmit())) {
                Banco banco = ssc.encontrarBanco(getTicket(), plantilla, false);
                if (banco == null) {
                    throw new SicaException("No se encontr\u00f3 el " +
                            "banco beneficiario especificado.");
                }
                
                //Si el sistema de pago es Swift y la clave del banco introducida es correcta, 
            	//se valida que la cuenta del beneficiario cumpla con el codigo IBAN
            	
            	if( plantilla.getTipoCuentaBanco().equals(PlantillaIntl.TIPO_CUENTA_BANCO_SWIFT)){
            		TablaPlantillaInternacional.validarCodigoIBAN(ssc, plantilla, getTicket());
            	}
                
            }
            else if (TablaPlantillaInternacional.MODO_SUBMIT_TPO_CTA_BCO_INTERM.
                    equals(getModoSubmit())) {
            	if (PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(plantilla.
                        getTipoCuentaBancoInterm().trim())) {
                    if (plantilla.getDivisa() == null || !Divisa.DOLAR.equals(plantilla.
                            getDivisa().getIdDivisa())) {
                        plantilla.setTipoCuentaBancoInterm(PlantillaIntl.TIPO_CUENTA_BANCO_ABA);
                    	throw new SicaException("Para poder utilizar Direct Deposit la plantilla " +
                                "debe tener la divisa USD.");
                    }
            		plantilla.setClaveBancoInterm(getSicaServiceData().getABABOFA().trim());
            		Banco banco = ssc.encontrarBanco(getTicket(),
                            PlantillaIntl.TIPO_CUENTA_BANCO_ABA, plantilla.getClaveBancoInterm(),
                            plantilla.getDivisa().getIdDivisa().trim());
            		if (banco == null) {
                    	throw new SicaException("No se encontr\u00f3 el banco " +
                                "intermediario default para DD.");
                    }
                    else {
                    	plantilla.setNombreBancoInterm(banco.getNombreCorto());
                    	plantilla.setIdPaisInterm(banco.getIdPais());
                    }
                }
            	else {
            		TablaPlantillaInternacional.procesarTipoCuentaBanco(plantilla, true);
            	}
            }
            else if (TablaPlantillaInternacional.MODO_SUBMIT_CLAVE_BANCO_INTERM.equals(
                    getModoSubmit())) {
                Banco banco = ssc.encontrarBanco(getTicket(), plantilla, true);
                if (banco == null) {
                	throw new SicaException("No se encontr\u00f3 el banco intermediario " +
                            "especificado.");
                }
            }
            else if (MODO_SUBMIT_ACTUALIZAR.equals(getModoSubmit())) {
            	
            	//Si el sistema de pago es Swift y la clave del banco introducida es correcta, 
            	//se valida que la cuenta del beneficiario cumpla con el codigo IBAN
            	
            	if( plantilla.getTipoCuentaBanco().equals(PlantillaIntl.TIPO_CUENTA_BANCO_SWIFT)){
            		TablaPlantillaInternacional.validarCodigoIBAN(ssc, plantilla, getTicket());
            	}
            	
            	if (plantilla.getBeneficiario() == null ||
                        plantilla.getBeneficiario().getIdPersona().intValue() < 1) {
                    delegate.record("Por favor seleccione un beneficiario.",
                            ValidationConstraint.REQUIRED);
                    return;
                }
            	if (!PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(plantilla.
                        getTipoCuentaBanco().trim())) {
            		if (StringUtils.isEmpty(plantilla.getClaveBanco())) {
        	            delegate.record("No se ha definido la clave banco beneficiario.",
                                ValidationConstraint.REQUIRED);
        	            return;
                	}
                	if (StringUtils.isEmpty(plantilla.getNombreBanco())) {
        	            delegate.record("No se ha definido " +
                                "el nombre banco beneficiario.",
                                ValidationConstraint.REQUIRED);
        	            return;
                	}
                }
            	if (PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(plantilla.
                        getTipoCuentaBanco().trim())) {
            		if (plantilla.getNombreBanco() == null ||
                            StringUtils.isEmpty(plantilla.getNombreBanco())) {
        	            delegate.record("No se ha definido el nombre banco beneficiario.",
                                ValidationConstraint.REQUIRED);
        	            return;
                	}
                }
                
            	// Validando, de tratarse de una Plantilla con Intermediario, que esten llenos los
                // campos cuenta_banco_interm y nombre_banco_interm
                if (isConIntermediario()) {
                	if (!PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(plantilla.
                            getTipoCuentaBancoInterm().trim())) {
                		if (StringUtils.isEmpty(plantilla.getClaveBancoInterm())) {
                    		delegate.record("No se ha definido la clave banco intermediario.",
                                    null);
                    		return;
                        }
                    	if (StringUtils.isEmpty(plantilla.getNombreBancoInterm())) {
                    		delegate.record("No se ha definido el nombre banco intermediario.",
                                    null);
                    		return;
                        }
                    }
                }
                if (getNombreCliente().equals(plantilla.getBeneficiario().getNombreCompleto())) {
                    plantilla.setStatusPlantilla(Plantilla.STATUS_PLANTILLA_ACTIVA);
                }

                if (getInstruccionesIntermediario() != null &&
                        getInstruccionesIntermediario().length() > 63) {
                    delegate.record("Las instrucciones al Banco Intermediario no deben exceder " +
                            "66 caracteres.", null);
        			return;
                }
                
                //Validaciones Instrucciones Banco Pagador:
                if (getInstruccionesPagador() != null && getInstruccionesPagador().length() > 99) {
                    delegate.record("Las instrucciones al Banco Pagador no deben exceder 99 " +
                            "caracteres.", null);
                    return;
                }
                //Actualizando Instrucciones Intermediario
                if (StringUtils.isEmpty(getInstruccionesIntermediario()) ||
                        (getInstruccionesIntermediario() == null)) {
                    plantilla.setInstrBcoIntermediario(null);
                }
                else {
                    plantilla.setInstrBcoIntermediario(getInstruccionesIntermediario().
                            toUpperCase());
                }
                //Actualizando Instrucciones Pagador
                plantilla.setInstrBcoBeneficiario(getInstruccionesPagador() != null ?
                    getInstruccionesPagador().toUpperCase() : "");
                // Validamos que el usuario no haya cambiado las claves de banco:
                if (!PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(plantilla.getTipoCuentaBanco()) &&
                        plantilla.getClaveBanco() != null &&
                        !plantilla.getClaveBanco().equals(plantilla.getClaveBancoTecleado())) {
                    throw new SicaException("La clave del banco deber\u00eda ser " +
                            plantilla.getClaveBancoTecleado());
                }
                if (isConIntermediario() && !PlantillaIntl.TIPO_CUENTA_BANCO_DD.
                        equals(plantilla.getTipoCuentaBancoInterm()) &&
                        plantilla.getClaveBancoInterm() != null &&
                        !plantilla.getClaveBancoInterm().equals(
                                plantilla.getClaveBancoIntermTecleado())) {
                    throw new SicaException("La clave del banco intermediario deber\u00eda ser " +
                            plantilla.getClaveBancoIntermTecleado());
                }
                // Se validan los caracteres no validos:
                plantilla.validarCaracteresCuentaBeneficiario();
                plantilla.validarCaracteresCuentaIntermediario();
                plantilla.validarCaracteresInstrucciones(plantilla.getInstrBcoBeneficiario() + " " +
                        plantilla.getInstrBcoIntermediario());
                // Se guarda la plantilla en la base de datos:
                getSicaServiceData().store(plantilla);
                if (PAGINA_MIGRACION_PLANTILLAS.equals(getPaginaAnterior())) {
                	MigracionPlantillas nextPage = (MigracionPlantillas) cycle.
                            getPage(PAGINA_MIGRACION_PLANTILLAS);
                	nextPage.setMapaEdicionMnemonico(new HashMap());
                	nextPage.setPaginaAnterior(getPageName());
                	nextPage.setNuevoBeneficiario(new Persona());
                	nextPage.buscarCuentasPlantillas(cycle);
                	nextPage.activarDelegate(true);
                	cycle.activate(PAGINA_MIGRACION_PLANTILLAS);
                }
                else {
                	cycle.activate(PAGINA_CONSULTA_PLANTILLAS_INTL);
                }
            }
        }
        catch (SicaException e) {
            if (delegate != null) {
                delegate.record(e.getMessage(), ValidationConstraint.REQUIRED);
            }
        }
    }

    /**
     * Activa la opcion de Captura de Bancos.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void insertarBanco(IRequestCycle cycle) {
        CapturaBanco nextPage = (CapturaBanco) cycle.getPage("CapturaBanco");
        nextPage.setPlantilla(getPlantilla());
        nextPage.setPaginaRetorno(getPageName());
        nextPage.activate(cycle);
    }
    
    /**
     * Cancela la edici&oacute;n de la plantilla. 
     * Envia a MigracionPlantillas.  
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cancelarEdicion(IRequestCycle cycle) {
    	MigracionPlantillas nextPage = (MigracionPlantillas) cycle.
                getPage(PAGINA_MIGRACION_PLANTILLAS);
    	nextPage.setMapaEdicionMnemonico(new HashMap());
    	nextPage.setNuevoBeneficiario(new Persona());
    	nextPage.setPaginaAnterior(getPageName());
    	nextPage.buscarCuentasPlantillas(cycle);
    	cycle.activate(PAGINA_MIGRACION_PLANTILLAS);
    }

    /**
     * Si la Plantilla tiene Intermediario.
     * @return True o False.
     */
    public boolean isConIntermediario() {
        return TablaPlantillaInternacional.CON_INTERMEDIARIO.equals(getSinConIntermediario());
    }

    /**
	 * Obtiene el Modelo de los Status de las Plantillas sin la parte de Activacion.
	 * @return IPropertySelectionModel.
	 */
    public IPropertySelectionModel getStatusPlantillasSinActivar() {
        return new StringPropertySelectionModel(new String[] {"PE", "SU"});
    }

    /**
	 * Obtiene el Modelo de los Status de las Plantillas solo con a parte de Activacion.
	 * @return IPropertySelectionModel.
	 */
    public IPropertySelectionModel getStatusPlantillasActivar() {
        return new StringPropertySelectionModel(new String[] {"AC"});
    }

    /**
     * Obtiene el Modo de Submit de la pagina. Si es de el boton submit o de un combo.
     *
     * @return El modo de submit de la pagina.
     */
    public abstract String getModoSubmit();

    /**
     * Regresa el valor de nombreCliente.
     *
     * @return String.
     */
    public abstract String getNombreCliente();

    /**
     * Fija el Modo de Submit de la pagina. Si es de el boton submit o de un combo.
     * @param modoSubmit El modo de submit de la pagina.
     */
    public abstract void setModoSubmit(String modoSubmit);

    /**
     * Regresa el valor de idPersona.
     *
     * @return Integer.
     */
    public abstract Integer getIdPersona();

    /**
     * Establece el valor de idPersona.
     *
     * @param idPersona El valor a asignar.
     */
    public abstract void setIdPersona(Integer idPersona);
    
    /**
     * Fija el Nombre del Cliente.
     * @param mombreCliente El valor a asignar.
     */
    public abstract void setNombreCliente(String mombreCliente);

    /**
     * Obtiene si la Plantilal es Sin o Con Intermediario.
     * @return Si tiene o no Intermediario.
     */
    public abstract String getSinConIntermediario();

    /**
     * Fija si la Plantilal es Sin o Con Intermediario.
     * @param sinConIntermediario Si tiene o no Intermediario.
     */
    public abstract void setSinConIntermediario(String sinConIntermediario);

    /**
     * Obtiene las Formas de Pago Liquidacion (Productos).
     * @return La Lista de Formas de Pago.
     */
    public abstract List getFormasPagoLiq();

    /**
     * Fija las Formas de Pago Liquidacion (Productos).
     * @param formasPagoLiq La Lista de Formas de Pago.
     */
    public abstract void setFormasPagoLiq(List formasPagoLiq);

    /**
     * Obtiene la Plantilla a dar de Alta.
     * @return La Plantilla.
     */
    public abstract IPlantillaIntl getPlantilla();

    /**
     * Fija la Plantilla a dar de Alta.
     * @param plantilla La Plantilla.
     */
    public abstract void setPlantilla(IPlantillaIntl plantilla);

    /**
     * Instrucciones al Banco Intermediario.
     *
     * @return Rengl&oacute;n 1 de la Instrucciones.
     */
    public abstract String getInstruccionesIntermediario();

    /**
     * Instrucciones al Banco Beneficiario.
     *
     * @return Rengl&oacute;n 1 de la Instrucciones.
     */
    public abstract String getInstruccionesPagador();

    /**
     * Instrucciones al Banco Intermediario.
     *
     * @param instruccionesIntermediarioR1 Las Instrucciones.
     */
    public abstract void setInstruccionesIntermediario(String instruccionesIntermediarioR1);

    /**
     * Establece el Rengl&oacute;n 1 de las Instrucciones al Banco Beneficiario.
     *
     * @param instruccionesPagador Las Instrucciones.
     */
    public abstract void setInstruccionesPagador(String instruccionesPagador);

    /**
     * Obtiene el valor de la variable global paginaAnterior
     *
     * @return String
     */
    public abstract String getPaginaAnterior();

    /**
     * Asigna el valor para la variable global paginaAnterior
     *
     * @param paginaAnterior Contiene el nombre de la p&aacute;gina anterior
     */
    public abstract void setPaginaAnterior(String paginaAnterior);
   
    /**
     * Obtiene el valor para MapaEdicioMnemonico que contiene
     * los datos de la plantilla seleccionada
     * 
     * @return HashMap
     */
    public abstract HashMap getMapaEdicionPlantilla();
    
    /**
     * Asigna el valor para MapaEdicioMnemonico que contiene
     * los datos de la plantilla seleccionada
     * 
     * @param mapaEdicionPlantilla Mapa con los datos de la plantilla seleccionada
     */
    public abstract void setMapaEdicionPlantilla(HashMap mapaEdicionPlantilla);
    
    /**
	 * Constante Captura Plantilla Transferencia Internacional.
	 */
    public static final String NOM_PANT_PLANT_INTL = "CapturaTransferenciaIntl";

    /**
	 * Constante Modo Submit Mnemonico.
	 */
    public static final String MODO_SUBMIT_MNEMONICO = "mnemonico";

    /**
	 * Constante Modo Submit Actualizar.
	 */
    public static final String MODO_SUBMIT_ACTUALIZAR = "actualizar";

    /**
     * Constante Pagina MigracionPlantillas
     */
    public static final String PAGINA_MIGRACION_PLANTILLAS = "MigracionPlantillas";

    /**
     * Constante Pagina ConsultaPlantillasIntl
     */
    public static final String PAGINA_CONSULTA_PLANTILLAS_INTL = "ConsultaPlantillasIntl";
}
