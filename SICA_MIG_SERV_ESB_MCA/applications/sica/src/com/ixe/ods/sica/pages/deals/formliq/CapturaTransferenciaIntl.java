/*
 * $Id: CapturaTransferenciaIntl.java,v 1.23.10.2.12.1.28.3.2.1.2.1.12.3 2017/10/20 17:17:17 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.formliq;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.ValidationConstraint;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.Persona;
//import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.components.TablaPlantillaInternacional;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.IPlantillaIntl;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaIntl;
import com.ixe.ods.sica.pages.CapturaBeneficiarios;
import com.ixe.ods.sica.pages.deals.CapturaBanco;
import com.ixe.ods.sica.pages.deals.CapturaDeal;
import com.ixe.ods.sica.pages.deals.interbancario.CapturaDealInterbancario;
import com.ixe.ods.sica.pages.deals.plantillas.IAsignacionBeneficiario;
import com.ixe.treasury.dom.common.Banco;
//import com.ixe.treasury.dom.common.Pais;
import com.ixe.treasury.dom.common.Pais;


/**
 * Clase para Captura de Plantillas de Transferencias Internacionales.

 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.23.10.2.12.1.28.3.2.1.2.1.12.3 $ $Date: 2017/10/20 17:17:17 $
 */
public abstract class CapturaTransferenciaIntl extends AbstractCapturaLiq
        implements IAsignacionBeneficiario {

	/**
     * Se ejecuta cada que se activa la p&aacute;gina. Popula los datos de las Instrucciones al
     * Intermediario, Pagador y Beneficiario, si es que estas existen.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        DealDetalle det = getDealDetalle();
        
        // Limpiando las variables persistentes
        setInstruccionesBeneficiario(null);
        if (det.getInstruccionesBeneficiario() != null && !"".equals(det.getInstruccionesBeneficiario().trim())) {
        	setInstruccionesBeneficiario(det.getInstruccionesBeneficiario().trim());	
        }
        // Populando y preseleccionando Combo Intermediario
        if (((PlantillaIntl) det.getPlantilla()).getClaveBancoInterm() != null) {
        	setSinConIntermediario(TablaPlantillaInternacional.CON_INTERMEDIARIO);
        }
        else {
        	setSinConIntermediario(TablaPlantillaInternacional.SIN_INTERMEDIARIO);
        }
        if (getTienePlantilla()) {
//        	//Populando las Instrucciones al Intermediario
//            if (det.getInstruccionesIntermediario() != null) {
//            	setInstruccionesIntermediario(det.getInstruccionesIntermediario().
//                        replaceAll(PIPE, ""));
//            }
//            //Populando las Instrucciones al Pagador
//            if (det.getInstruccionesPagador() != null) {
//                setInstruccionesPagador(det.getInstruccionesPagador().replaceAll(PIPE, ""));
//            }
            //Populando las Instrucciones al Beneficiario
            if (det.getInstruccionesBeneficiario() != null) {
            	setInstruccionesBeneficiario(det.getInstruccionesBeneficiario().
                        replaceAll(PIPE, ""));
            }
        }
//        else if (!(det.getPlantilla().getIdPlantilla() < 1)) {
//        	//Populando las Instrucciones al Intermediario
//            if (((PlantillaIntl) det.getPlantilla()).getInstrBcoIntermediario() != null) {
//                setInstruccionesIntermediario(((PlantillaIntl) det.getPlantilla()).
//                        getInstrBcoIntermediario().replaceAll(PIPE, ""));
//            }
//            //Populando las Instrucciones al Pagador
//            if (((PlantillaIntl) det.getPlantilla()).getInstrBcoBeneficiario() != null) {
//                setInstruccionesPagador(((PlantillaIntl) det.getPlantilla()).
//                        getInstrBcoBeneficiario().replaceAll(PIPE, ""));
//            }
//        }
        
	//F65211 Complemento - Obligatorio propósito de transferencia Ley 115 LIC
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
        faltaPropositoDeLaTransferencia();
        if (getFaltaPropositoTransferencia()) {
            if (delegate != null) {
                delegate.record("No se ha definido el Prop\u00f3sito de la Transferencia.", ValidationConstraint.REQUIRED);
            }
		}
    }
	
	/**
	 * Crea la Plantilla de Transferencia Internacional
	 */
	public void crearPlantilla() {
        PlantillaIntl plantilla = new PlantillaIntl();

        DealDetalle det = getDealDetalle();
        plantilla.setMnemonico(det.getMnemonico());
        plantilla.setDivisa(det.getDivisa());
        plantilla.setContratoSica(det.getDeal().getContratoSica());
        det.setPlantilla(plantilla);
    }
	
	/**
     * Asigna el Beneficiario a la Plantilla.
     *
     * @param beneficiario El destinatario de la transferencia.
     */
	public void creaBeneficiario(Persona beneficiario) {
		getDealDetalle().getPlantilla().setBeneficiario(beneficiario);
	}

	/**
     * Guarda y actualiza los datos de la Plantilla y del Detalle del Deal respectivamente en la
     * Base de Datos.
     *
     * @param plantilla La Plantilla a guardar.
     * @param cycle El ciclo de la p&aacute;gina.
     */
    private void actualizar(IPlantillaIntl plantilla, IRequestCycle cycle) {
        if (plantilla.getBeneficiario() == null) {
            throw new SicaException("Por favor seleccione un beneficiario.");
        }
        if (plantilla.getBeneficiario().getIdPersona().intValue() < 0) {
    		throw new SicaException("No se ha definido el nombre del beneficiario para " +
                    "la plantilla.");
    	}
        if (!PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(plantilla.getTipoCuentaBanco().trim())) {
            if (StringUtils.isEmpty(plantilla.getClaveBanco())) {
                throw new SicaException("No se ha definido la clave banco beneficiario.");
            }
        	if (StringUtils.isEmpty(plantilla.getNombreBanco())) {
	            throw new SicaException("No se ha definido el nombre banco beneficiario.");
        	}
        }
        else {
        	if (StringUtils.isEmpty(plantilla.getNombreBanco())) {
	            throw new SicaException("No se ha definido el nombre del banco beneficiario.");
        	}
        }
        // Validando, de tratarse de una Plantilla con Intermediario, que esten llenos los campos
        // cuenta_banco_interm y nombre_banco_interm
        if (isConIntermediario()) {
        	if (!PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(plantilla.
                    getTipoCuentaBancoInterm().trim())) {
        		if (StringUtils.isEmpty(plantilla.getClaveBancoInterm())) {
            		throw new SicaException("No se ha definido la clave banco intermediario.");
                }
            	if (StringUtils.isEmpty(plantilla.getNombreBancoInterm())) {
            		throw new SicaException("No se ha definido el nombre banco intermediario.");
                }
            }
        }
        
        // Validaciones Instrucciones Beneficiario:
		//F65211 Complemento - Obligatorio propósito de transferencia Ley 115 LIC
        //se cambia el titulo del campo de Instrucciones Beneficiario a "Proposito de la Transferencia" y se hace obligatorio 
        String propositoTransferencia = getInstruccionesBeneficiario();
        Pattern pattern = Pattern.compile("(([?)(\\.,\\x20])*([a-zA-Z0-9])+([?)(\\.,\\x20])*)+"); //("[a-zA-Z0-9?)(\\.,\\x20]{1,130}");
        if (propositoTransferencia == null || "".equals(propositoTransferencia.trim())) {
        	throw new SicaException("No se ha definido el Prop\u00f3sito de la Transferencia.");
        } else if (propositoTransferencia.length() > 130) {
        	throw new SicaException("El Prop\u00f3sito de la Transferencia no puede exceder de 130 posiciones.");
        } else if (!pattern.matcher(propositoTransferencia).matches() ) {
        	throw new SicaException("El Prop\u00f3sito de la Transferencia no permite caracteres especiales, acentos o ñ.");
        } else {
        	propositoTransferencia = propositoTransferencia.toUpperCase();
        	getDealDetalle().setInstruccionesBeneficiario(propositoTransferencia);
        }

        // Guardando y/o actualizando registros:
        boolean nuevaPlantilla = false;
        if (plantilla.getIdPlantilla() < 1) {
            nuevaPlantilla = true;
            if (!isConIntermediario()) {
                if (!PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(
                        plantilla.getTipoCuentaBanco().trim())) {
                    plantilla.setCuentaBancoInterm(null);
                    plantilla.setNombreBancoInterm(null);
                }
                else {
                    plantilla.setNombreBancoInterm(null);
            	}
            }
        }
       
       
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
        if (nuevaPlantilla) {
            if (getDealDetalle().getDeal().getCliente().getNombreCompleto().
                    equals(plantilla.getBeneficiario().getNombreCompleto())) {
                plantilla.setStatusPlantilla(Plantilla.STATUS_PLANTILLA_ACTIVA);
            }
            plantilla.setInstrBcoIntermediario(getDealDetalle().getInstruccionesIntermediario());
            plantilla.setInstrBcoBeneficiario(getDealDetalle().getInstruccionesPagador());
        }
        // Se validan los caracteres no validos:
        plantilla.validarCaracteresCuentaBeneficiario();
        plantilla.validarCaracteresCuentaIntermediario();
        plantilla.validarCaracteresInstrucciones(getDealDetalle().getInstruccionesPagador() +
                " " + getDealDetalle().getInstruccionesIntermediario() + " " +
                getDealDetalle().getInstruccionesBeneficiario());
        if (nuevaPlantilla) {
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
            getSicaServiceData().storePlantillaParaDealDetalle(getDealDetalle(), plantilla);
        }
        if (!Plantilla.STATUS_PLANTILLA_ACTIVA.equals(plantilla.getStatusPlantilla())) {
            marcarSolicitudAutorizacionPorPlantilla(getDealDetalle());
        }
        else {
            getSicaServiceData().update(getDealDetalle());
        }
        if (getDealDetalle().getDeal().isInterbancario()) {
            CapturaDealInterbancario nextPage = (CapturaDealInterbancario) cycle.
                    getPage("CapturaDealInterbancario");

            nextPage.setDeal(getSicaServiceData().findDeal(getDealDetalle().getDeal().getIdDeal()));
            nextPage.activate(cycle);
        }
        else {
            CapturaDeal nextPage = (CapturaDeal) cycle.getPage("CapturaDeal");

            if (nuevaPlantilla) {
                nextPage.setDeal(getSicaServiceData().findDeal(getDealDetalle().getDeal().
                        getIdDeal()));
            }
            cycle.activate(nextPage);
        }
    }

    /**
     * Realiza algunas validaciones y manda a llamar al Metodo <code>actualizar</code> 
     * para que Guarde la Plantilla y Actualice el Deal Detalle.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void submit(IRequestCycle cycle) {
        //Validando que los Datos a guardar o actualizar sean los correctos.
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        if (delegate != null && delegate.getHasErrors()) {
            return;
        }
        try {
            IPlantillaIntl plantilla = (IPlantillaIntl) getDealDetalle().getPlantilla();
            SicaSiteCache ssc = (SicaSiteCache) getApplicationContext().getBean("sicaSiteCache");
            if (plantilla.getBeneficiario().getIdPersona().intValue() < 0) {
        		throw new SicaException("No se ha definido el nombre del beneficiario para la " +
                        "plantilla.");
        	}
            if (TablaPlantillaInternacional.MODO_SUBMIT_CON_SIN_INTERMEDIARIO.
                    equals(getModoSubmit())) {
                TablaPlantillaInternacional.definirSinConIntermediario(plantilla,
                        isConIntermediario());
            }
            else if (TablaPlantillaInternacional.MODO_SUBMIT_TPO_CTA_BCO.equals(getModoSubmit())) {
            	if (PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(plantilla.getTipoCuentaBanco().trim())) {
            		
            		if (plantilla.getDivisa() == null ) {
                        throw new SicaException("No se ha definido la divisa de la plantilla");
            		} 	
            		if (plantilla.getNombreBanco() == null) {
            			throw new SicaException("No se ha definido el nombre del Banco " +
                                "Beneficiario");
            		}
            		
            		plantilla.setCuentaBancoInterm(plantilla.getCuentaBancoInterm());
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
            		throw new SicaException("No se encontr\u00f3 el banco beneficiario "
            				+ "especificado.");
            	}
            	//Si el sistema de pago es Swift y la clave del banco introducida es correcta, 
            	//se valida que la cuenta del beneficiario cumpla con el codigo IBAN
            	
            	if( plantilla.getTipoCuentaBanco().equals(PlantillaIntl.TIPO_CUENTA_BANCO_SWIFT)){
            		TablaPlantillaInternacional.validarCodigoIBAN(ssc, plantilla, getTicket());
            	}
            	
            	/*Se comenta para liberacion posterior
					else{
            		Pais tesPais = ssc.getPaisXClaveISO(getTicket(), banco.getIdPais());
            		if (Constantes.SI.equals( getSicaServiceData().findBupPaisXId(tesPais.getIdBupPais())
            				.getParaisoFiscal())){
            			throw new SicaException("El banco proporcionado para la transferecia al Beneficiario " +
            					"se encuentra en un pa\u00eds " +
            					"considerado un para\u00edso fiscal. Considere el riesgo que representa " +
            					"la operaci\u00f3n.");
            		}
            	}*/
            }
            else if (TablaPlantillaInternacional.MODO_SUBMIT_TPO_CTA_BCO_INTERM.
                    equals(getModoSubmit())) {
            	if (PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(plantilla.
                        getTipoCuentaBancoInterm().trim())) {
                    if (plantilla.getDivisa() == null ||
                            !Divisa.DOLAR.equals(plantilla.getDivisa().getIdDivisa())) {
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
            else if (TablaPlantillaInternacional.MODO_SUBMIT_CLAVE_BANCO_INTERM.
                    equals(getModoSubmit())) {
            	
                Banco banco = ssc.encontrarBanco(getTicket(), plantilla, true);
                if (banco == null) {
                    throw new SicaException("No se encontr\u00f3 el banco intermediario "
                            + "especificado.");
                }
                /* Se comenta para liberacion posterior.
				else{
            		Pais tesPais = ssc.getPaisXClaveISO(getTicket(), banco.getIdPais());
            		if (Constantes.SI.equals( getSicaServiceData().findBupPaisXId(tesPais.getIdBupPais())
            				.getParaisoFiscal())){
            			throw new SicaException("El banco proporcionado para la transferecia al Intermediario " +
            					"se encuentra en un pa\u00eds " +
            					"considerado un para\u00edso fiscal. Considere el riesgo que representa " +
            					"la operaci\u00f3n.");
            		}
                }*/
            }
            else if (MODO_SUBMIT_ACTUALIZAR.equals(getModoSubmit())) {
            	
            	//Si el sistema de pago es Swift y la clave del banco introducida es correcta, 
            	//se valida que la cuenta del beneficiario cumpla con el codigo IBAN
            	
            	if( plantilla.getTipoCuentaBanco().equals(PlantillaIntl.TIPO_CUENTA_BANCO_SWIFT)){
            		TablaPlantillaInternacional.validarCodigoIBAN(ssc, plantilla, getTicket());
 
            	}
                
            	if (plantilla instanceof IPlantillaIntl) {
                    ((IPlantillaIntl) plantilla).validarCaracteresCuentaBeneficiario();
                    ((IPlantillaIntl) plantilla).validarCaracteresCuentaBeneficiario();
                    
                    //Si el sistema de pago es Swift y la clave del banco introducida es correcta, 
                	//se valida que la cuenta del beneficiario cumpla con el codigo IBAN
                    //SicaSiteCache ssc = (SicaSiteCache) getApplicationContext().getBean("sicaSiteCache");
                	if( ((IPlantillaIntl) plantilla).getTipoCuentaBanco().equals(PlantillaIntl.TIPO_CUENTA_BANCO_SWIFT)){
                		TablaPlantillaInternacional.validarCodigoIBAN(ssc, ((IPlantillaIntl) plantilla), getTicket());
                	}
                	
                	Banco banco = null;
                	Pais tesPais = null;
                	
                	//JDCH 02/06/2014 se agrega esta validacion para las plantillas internacionales ya uqe no debe asignar la plantilla si el país es sancionado por la OCDE
                	if(((IPlantillaIntl)plantilla).getClaveBancoInterm()== null){

                        if (StringUtils.isEmpty(plantilla.getClaveBanco())) {
                            throw new SicaException("No se ha definido la clave banco beneficiario.");
                        }
                        
                        if (StringUtils.isEmpty(plantilla.getNombreBanco())) {
                    		throw new SicaException("No se ha definido el nombre banco beneficiario.");
                        }
                        
                        if (!PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(plantilla.getTipoCuentaBanco()) &&
                                plantilla.getClaveBanco() != null &&
                                !plantilla.getClaveBanco().equals(plantilla.getClaveBancoTecleado())) {
                            throw new SicaException("La clave del banco deber\u00eda ser " +
                                    plantilla.getClaveBancoTecleado());

                        }
                		
                		banco = ssc.encontrarBanco(getTicket(), (IPlantillaIntl)plantilla,false);//regresa el banco Beneficiario
                		tesPais = ssc.getPaisXClaveISO(getTicket(), banco.getIdPais());
                		
                		if ("S".equals(tesPais.getSancionadoOFAC())){
              			  	System.out.println("El pais del banco Beneficiario esta dentro de los sancionados por la OFAC:   "+ plantilla.getIdPlantilla());
                            throw new SicaException("El país al que pertenece el Banco Beneficiario se encuentra en la lista de países sancionados por OFAC, " +
              			  	"no puede continuar con el registro de la transferencia.");
                    		
                    	}
                		if ("S".equals(tesPais.getAltoRiesgoGAFI())){
              			  	System.out.println("El pais del banco Beneficiario es considerado de alto riesgo por GAFI:   "+ plantilla.getIdPlantilla());
                            throw new SicaException("El país al que pertenece el Banco Beneficiario se encuentra en la lista de países de alto riesgo de GAFI, " +
              			  	"no puede continuar con el registro de la transferencia.");
                    		
                    	}
                		
                	}
                	else{
                        if (StringUtils.isEmpty(plantilla.getClaveBancoInterm())) {
                            throw new SicaException("No se ha definido la clave banco intermediario.");
                        }
                		
                        if (StringUtils.isEmpty(plantilla.getNombreBancoInterm())) {
                    		throw new SicaException("No se ha definido el nombre banco intermediario.");
                        }
                        
                        if (!PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(plantilla.getTipoCuentaBancoInterm()) &&
                                plantilla.getClaveBancoInterm() != null &&
                                !plantilla.getClaveBancoInterm().equals(
                                        plantilla.getClaveBancoIntermTecleado())) {
                            throw new SicaException("La clave del banco intermediario deber\u00eda ser " +
                                    plantilla.getClaveBancoIntermTecleado());                
                        }
                        
//                        if (StringUtils.isEmpty(plantilla.getClaveBanco())) {
//                            throw new SicaException("No se ha definido la clave banco beneficiario.");
//                        }
//                        
//                        if (StringUtils.isEmpty(plantilla.getNombreBanco())) {
//                    		throw new SicaException("No se ha definido el nombre banco beneficiario.");
//                        }
                        
                        if (!PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(plantilla.getTipoCuentaBanco()) &&
                                plantilla.getClaveBanco() != null &&
                                !plantilla.getClaveBanco().equals(plantilla.getClaveBancoTecleado())) {
                            throw new SicaException("La clave del banco deber\u00eda ser " +
                                    plantilla.getClaveBancoTecleado());

                        }
                        
                        if(!PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(plantilla.getTipoCuentaBanco().trim())){
                		banco = ssc.encontrarBanco(getTicket(), (IPlantillaIntl)plantilla,false); //regresa el banco Beneficiario
                		tesPais = ssc.getPaisXClaveISO(getTicket(), banco.getIdPais());
                		
                		if ("S".equals(tesPais.getSancionadoOFAC())){
              			  	System.out.println("El pais del banco beneficiario esta dentro de los sancionados por la OFAC:   "+ plantilla.getIdPlantilla());
                            throw new SicaException("El país al que pertenece el Banco Beneficiario se encuentra en la lista de países sancionados por OFAC, " +
              			  	"no puede continuar con el registro de la transferencia.");
                    		
                    	}
                		if ("S".equals(tesPais.getAltoRiesgoGAFI())){
              			  	System.out.println("El pais del banco beneficiario esta considerado de alto riesgo por GAFI:   "+ plantilla.getIdPlantilla());
                            throw new SicaException("El país al que pertenece el Banco Beneficiario se encuentra en la lista de países de alto riesgo de GAFI, " +
              			  	"no puede continuar con el registro de la transferencia.");
                    		
                    	}
                        }
                		

                		
                		Banco bancoInterm = ssc.encontrarBanco(getTicket(), (IPlantillaIntl)plantilla,true);//regresa el banco Intermediario
                		Pais tesPaisBcoInterm = ssc.getPaisXClaveISO(getTicket(), bancoInterm.getIdPais());
                		
                		if ("S".equals(tesPaisBcoInterm.getSancionadoOFAC())){
              			  	System.out.println("El pais del banco intermediario esta dentro de los sancionados por la OFAC:   "+ plantilla.getIdPlantilla());
                            throw new SicaException("El país al que pertenece el Banco Intermediario se encuentra en la lista de países sancionados por OFAC, " +
              			  	"no puede continuar con el registro de la transferencia.");
                    		
                    	}
                		if ("S".equals(tesPaisBcoInterm.getAltoRiesgoGAFI())){
              			  	System.out.println("El pais del banco intermediario esta considerado de alto riesgo por GAFI:   "+ plantilla.getIdPlantilla());
                            throw new SicaException("El país al que pertenece el Banco Intermediario se encuentra en la lista de países de alto riesgo de GAFI, " +
              			  	"no puede continuar con el registro de la transferencia.");
                    		
                    	}
                		
                	}
                	
                	//576449 - SPID MXN a USD y SPID obligatorio PM transfer domésticas USD
                	DealDetalle det = getDealDetalle();
            		Deal deal = det.getDeal();
            		Persona p = deal.getCliente();
            		String ordenanteTipoPersona = p.getTipoPersona();
            		String beneficiarioTipoPersona = plantilla.getBeneficiario().getTipoPersona();
            		String divisa = det.getDivisa().getIdDivisa();
                	if( ((IPlantillaIntl) plantilla).getTipoCuentaBanco().equals(PlantillaIntl.TIPO_CUENTA_BANCO_SWIFT)){
		        		if ( det.getMnemonico() != null && det.getMnemonico().startsWith("E") && "USD".equals(divisa) ) {
		        			if ( "MX".equals(tesPais.getClaveISO()) ) {
		        				if ( "PM".equals(ordenanteTipoPersona) && "PM".equals(beneficiarioTipoPersona) ) {
		              			  	System.out.println("Operación no válida.  Las liquidaciones en USD en México se deben registrar vía SPID");
		                            throw new SicaException("Operación no válida.  Las liquidaciones en USD en México se deben registrar vía SPID");
		        				}
		        			}
		        		}
                	}
                }

            	
                actualizar(plantilla, cycle);
            }
        }
        catch (SicaException e) {
            debug(e.getMessage(), e);
            if (delegate != null) {
                delegate.record(e.getMessage(), ValidationConstraint.REQUIRED);
            }
        }
    }

    /**
     * Abre la opcion de Isertar Banco.
     * 
     * @param cycle El el Modo de Submit de la Pagina.
     */
    public void insertarBanco(IRequestCycle cycle) {
        CapturaBanco nextPage = (CapturaBanco) cycle.getPage("CapturaBanco");
        nextPage.setPlantilla((IPlantillaIntl) getDealDetalle().getPlantilla());
        nextPage.setPaginaRetorno(getPageName());
        nextPage.activate(cycle);
    }

    /**
     * Indica si la Plantilla es Con Intermediario.
     * 
     * @return True o False.
     */
    public boolean isConIntermediario() {
        return TablaPlantillaInternacional.CON_INTERMEDIARIO.equals(getSinConIntermediario());
    }

    /**
     * Agrega un beneficiario.
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void agregaBeneficiario(IRequestCycle cycle) {
        CapturaBeneficiarios captura = (CapturaBeneficiarios) cycle.getPage("CapturaBeneficiarios");
        Deal deal = getDealDetalle().getDeal();
        captura.setContratoSica(deal.getContratoSica());
        captura.setIdPersonaCliente(deal.getCliente().getIdPersona().intValue());
        captura.setNombreCliente(deal.getCliente().getNombreCorto());
        captura.setPaginaAnterior(getPageName());
        captura.activate(cycle);
    }
    
    /**
     * Obtiene el Modo de Submit de la Pagina.
     * 
     * @return Si el submit fue por boton o por combo.
     */
    public abstract String getModoSubmit();

    /**
     * Indica si la Plantilla a Capturar es Con o Sin Intermediario.
     * 
     * @return Si es Con o Sin Intermediario.
     */
    public abstract String getSinConIntermediario();

   
    /**
     * Instrucciones al Beneficiario.
     *
     * @return Rengl&oacute;n 1 de la Instrucciones.
     */
    public abstract String getInstruccionesBeneficiario();
    
    /**
     * Establece si la operaci&oacute;n es con o sin intermediario.
     * 
     * @param sinConIntermediario El valor para sinConIntermediario.
     */
    public abstract void setSinConIntermediario(String sinConIntermediario);

    
    /**
     * Instrucciones al Beneficiario.
     *
     * @param instruccionesBeneficiario Las Instrucciones.
     */
    public abstract void setInstruccionesBeneficiario(String instruccionesBeneficiario);

    //F65211 Complemento - Obligatorio propósito de transferencia Ley 115 LIC
    /**
     * Establece si a la transferencia le falta que se capture el propósito de la transferencia.
     * 
     * @return void.
     */
    public void faltaPropositoDeLaTransferencia() {
    	DealDetalle det = getDealDetalle();
    	if(det.faltaPropositoDeTransferencia()) {
    		setFaltaPropositoTransferencia(true);
		} else {
			setFaltaPropositoTransferencia(false);
    	}            	
    }

	//F65211 Complemento - Obligatorio propósito de transferencia Ley 115 LIC
    /**
     * Indica si la Plantilla a Capturar Requiere Proposito Transferencia.
     * 
     * @return Si Falta Proposito Transferencia.
     */
    public abstract boolean getFaltaPropositoTransferencia();
    
	//F65211 Complemento - Obligatorio propósito de transferencia Ley 115 LIC
    /**
     * Establece si a la operaci&oacute;n le Falta Proposito Transferencia.
     * 
     * @param faltaPropositoTransferencia El valor para faltaPropositoTransferencia.
     */
    public abstract void setFaltaPropositoTransferencia(boolean faltaPropositoTransferencia);

    
    /**
     * La constante para indicar el modo de submit para actualizar el registro.
     */
    public static final String MODO_SUBMIT_ACTUALIZAR = "actualizar";

    /**
     * Constante para la cadena '\\|'.
     */
    private static final String PIPE = "\\|";
}
