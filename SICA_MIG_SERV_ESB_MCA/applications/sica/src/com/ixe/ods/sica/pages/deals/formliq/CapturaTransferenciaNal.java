/*
 * $Id: CapturaTransferenciaNal.java,v 1.16.86.1 2017/05/12 21:41:51 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2007 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.deals.formliq;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.IPlantillaTranNacional;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaTranNacional;
import com.ixe.ods.sica.pages.CapturaBeneficiarios;
import com.ixe.ods.sica.pages.deals.plantillas.IAsignacionBeneficiario;
import com.ixe.treasury.dom.common.Banco;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.legosoft.tapestry.model.RecordSelectionModel;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

/**
 * Clase para la Captura de Plantillas de Transferencias Nacionales.
 *
 * @author Javier Cordova, Jean C. Favila
 * @version  $Revision: 1.16.86.1 $ $Date: 2017/05/12 21:41:51 $
 */
public abstract class CapturaTransferenciaNal extends AbstractCapturaLiq
        implements IAsignacionBeneficiario {

    /**
     * Crea la Plantilla de Transferencia Nacional.
     */
    public void crearPlantilla() {
    	PlantillaTranNacional plantilla = new PlantillaTranNacional();
        DealDetalle det = getDealDetalle();
        plantilla.setMnemonico(det.getMnemonico());
        plantilla.setDivisa(det.getDivisa());
        plantilla.setContratoSica(det.getDeal().getContratoSica());
        det.setPlantilla(plantilla);
        FormaPagoLiq fpl = getFormaPagoLiq(det.getMnemonico());
        plantilla.setIdBanco(fpl.getIdBanco());
        plantilla.setNombreBanco(fpl.getNombreBanco());
        if (plantilla.isBancoTerceroSpei()) {
            setClabeOCuenta("CLABE");
        }
    }

    /**
     * Asigna el Beneficiario a la Plantilla.
     *
     * @param beneficiario El beneficiario a asignar a la plantilla.
     */
    public void creaBeneficiario(Persona beneficiario) {
		getDealDetalle().getPlantilla().setBeneficiario(beneficiario);
	}

    /**
     * Permite saber si un Banco Opera Spei.
     *
     * @param idBanco El Banco a verificar.
     * @return Banco  Si opera Spei. De lo contrario null.
     */
    private Banco getBanco(Long idBanco) {
        try {
            List bancos = getSiteService().getBancosNacionalesXOperaSpei(getTicket());
            for (Iterator it = bancos.iterator(); it.hasNext();) {
                Banco banco = (Banco) it.next();
                if (banco.getIdBanco().equals(idBanco)) {
                    return banco;
                }
            }
            return null;
        }
        catch (Exception e) {
            throw new ApplicationRuntimeException("No se encontr&oacute; el banco con el"
                    + " n&uacute;mero " + idBanco, e);
        }
    }

    /**
     * Guarda y actualiza los datos de la Plantilla y del Detalle del Deal respectivamente en la
     * Base de Datos.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        IPlantillaTranNacional plantilla = (IPlantillaTranNacional) getDealDetalle().
                getPlantilla();
        Banco banco;
        if (plantilla.isSpei() && plantilla.getIdPlantilla() == 0) {
            banco = getBanco(plantilla.getIdBanco());
            plantilla.setNombreBanco(banco.getNombreCorto());
        }
        if (delegate != null && delegate.getHasErrors()) {
            return;
        }
        try {
            if (StringUtils.isEmpty(plantilla.getClabe()) && delegate != null) {
                throw new SicaException("El No. CLABE o No. Cuenta no puede estar vac\u00edo");
            }
            if ("CLABE".equals(getClabeOCuenta())) {
                if (plantilla.getClabe().length() != 18 && delegate != null) {
                    throw new SicaException("El No. CLABE debe tener 18 caracteres.");
                }
                banco = getBanco(plantilla.getIdBanco());
                com.ixe.ods.bup.model.Banco bupBanco = (com.ixe.ods.bup.model.Banco)
                        getSicaServiceData().find(
                                com.ixe.ods.bup.model.Banco.class,
                                new Integer(banco.getIdBanco().intValue()));
                if (!plantilla.getClabe().startsWith(bupBanco.getClaveBanxico())
                        && delegate != null) {
                    throw new SicaException("La CLABE debe iniciar con "
                            + bupBanco.getClaveBanxico());
                }
                if (plantilla.getBeneficiario().getIdPersona().intValue() < 0) {
                	plantilla.setBeneficiario(null);
            		throw new SicaException("No se ha definido el nombre del beneficiario para la "
                            + "plantilla.");
            	}
            }
            else {
                if (plantilla.getClabe().length() != 11 && delegate != null) {
                    throw new SicaException("El No. Cuenta debe tener 11 caracteres. Si el "
                            + "n\u00famero de cuenta tiene menos caracteres, rellene con ceros a "
                            + "la izquierda.");
                }
            }
            if (getModoSubmit() == MODO_SUBMIT_ACTUALIZAR) {
                if (StringUtils.isNotEmpty(plantilla.getClavePlantilla())) {
                    plantilla.setClavePlantilla(plantilla.getClavePlantilla().toUpperCase());
                }
                plantilla.setClabe(plantilla.getClabe().toUpperCase());
                if (plantilla.getIdPlantilla() < 1) {
                    if (getDealDetalle().getDeal().getCliente().getNombreCompleto().equals(
                            plantilla.getBeneficiario().getNombreCompleto())) {
                        plantilla.setStatusPlantilla(Plantilla.STATUS_PLANTILLA_ACTIVA);
                    }
                }
                if (plantilla.getBeneficiario().getIdPersona().intValue() < 0) {
            		throw new SicaException("No se ha definido el nombre del beneficiario para la "
                            + "plantilla.");
            	}

            	//576449 - SPID MXN a USD y SPID obligatorio PM transfer domésticas USD
                DealDetalle det = getDealDetalle();
            	Deal deal = det.getDeal();
        		Persona p = deal.getCliente();
        		String ordenanteTipoPersona = p.getTipoPersona();
        		String beneficiarioTipoPersona = plantilla.getBeneficiario().getTipoPersona();
        		String divisa = det.getDivisa().getIdDivisa();
        		if ( det.getMnemonico() != null && det.getMnemonico().startsWith("E") && "USD".equals(divisa) ) {
    				if ( "PM".equals(ordenanteTipoPersona) && "PM".equals(beneficiarioTipoPersona) ) {
          			  	System.out.println("Operación no válida.  Las liquidaciones en USD en México se deben registrar vía SPID");
                        throw new SicaException("Operación no válida.  Las liquidaciones en USD en México se deben registrar vía SPID");
    				}
        		}

                if (plantilla.getIdPlantilla() == 0) {
                	getSicaServiceData().storePlantillaParaDealDetalle(getDealDetalle(), plantilla);
                }
                else {
                    getSicaServiceData().update(getDealDetalle());
                }
                if (!Plantilla.STATUS_PLANTILLA_ACTIVA.equals(plantilla.getStatusPlantilla())) {
                    marcarSolicitudAutorizacionPorPlantilla(getDealDetalle());
                }
                
                cycle.activate(cycle.getPage("CapturaDeal"));
            }
        }
        catch (SicaException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
            delegate.record(e.getMessage(), null);

        }
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
        captura.setNombreCliente(deal.getCliente().getNombreCompleto());
        captura.setPaginaAnterior(getPageName());
        captura.activate(cycle);
    }

    /**
     * Modelo para la lista de beneficiarios del cliente.
     *
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getBeneficiariosModel() {
    	List beneficiarios = getSicaServiceData().findBeneficiariosByNoCuentaByIdRol(
                getDealDetalle().getDeal().getContratoSica().getNoCuenta(),
                FacultySystem.BENEFICIARIO_SICA_ROL);
    	Persona primerElemento = new Persona();
    	primerElemento.setIdPersona(new Integer(-1));
    	primerElemento.setNombreCorto("Seleccione un Beneficiario");
    	beneficiarios.add(0, primerElemento);
    	return new RecordSelectionModel(beneficiarios, "idPersona", "nombreCorto");
    }

    /**
     * Obtiene el Modelo a mostrar en el Combo CLABE o Cuenta.
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getComboCLABECuenta() {
        return new StringPropertySelectionModel(new String[] {"CLABE", "Cuenta"});
    }

    /**
     * Obtiene la seleccion del usuario de si la Plantilla lleva la CLABE o el Numero de Cuenta.
     *
     * @return La seleccion del usuario del Combo CLABE o Cuenta.
     */
    public abstract String getClabeOCuenta();

    /**
     * Fija la seleccion del usuario de si la Plantilla lleva la CLABE o el Numero de Cuenta.
     *
     * @param clabeOCuenta El valor a asignar.
     */
    public abstract void setClabeOCuenta(String clabeOCuenta);

    /**
     * Regresa el valor de modoSubmit.
     *
     * @return int Si se trata del boton submit o un submit de un combo.
     */
    public abstract int getModoSubmit();

    /**
     * Constante MODO_SUBMIT_ACTUALIZAR.
     */
    public static final int MODO_SUBMIT_ACTUALIZAR = 1;
}