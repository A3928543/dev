/*
 * $Id: EdicionPlantillaTranNacional.java,v 1.14 2008/02/22 18:25:01 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.plantillas;

import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.components.BancosSelectionModel;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.IPlantillaTranNacional;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaPantalla;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.mesa.MigracionPlantillas;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.treasury.dom.common.Banco;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.legosoft.tapestry.model.RecordSelectionModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * Clase para el Alta de Plantillas de Transferencias Nacionales.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.14 $ $Date: 2008/02/22 18:25:01 $
 */
public abstract class EdicionPlantillaTranNacional extends SicaPage {

    /**
     * Arma la lista de formasPagoLiq aplicables (mnem&oacute;nicos).
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        SicaServiceData sd = getSicaServiceData();
        List plantillasPantalla = sd.findMnemonicosByPlantillaPantalla(NOM_PANT_PLANT_TRANAC);
        List fpls = new ArrayList();
        for (Iterator it = plantillasPantalla.iterator(); it.hasNext();) {
            PlantillaPantalla pp = (PlantillaPantalla) it.next();
            FormaPagoLiq fpl = getFormaPagoLiq(pp.getMnemonico());
            if (fpl != null && fpl.getDesplegableSica().booleanValue()
                    && Constantes.MNEMONICO_ACTIVO.equals(fpl.getStatus())) {
                fpls.add(fpl);
            }
            else if (_logger.isWarnEnabled()) {
                _logger.warn("No se encontr\u00f3 la formaPagoLiq con el mnem\u00f3nico "
                        + pp.getMnemonico() + ". Probablemente se encuentre inactivo.");
            }
        }
        setFormasPagoLiq(fpls);
        setMapaEdicionPlantilla(new HashMap());
        getPlantilla().setDivisa((Divisa) sd.find(Divisa.class,
                ((FormaPagoLiq) fpls.get(0)).getIdDivisa()));
    }

    /**
     * Regresa las formas de pago como una matriz de tres columnas.
     *
     * @return FormaPagoLiq[][].
     */
    public FormaPagoLiq[][] getMatrizFpls() {
        int renglones = (getFormasLiquidacionFiltradas().size() / 3)
                + (getFormasLiquidacionFiltradas().size() % 3);
        FormaPagoLiq[][] matriz = new FormaPagoLiq[renglones][3];
        int y = 0;
        int x = 0;
        for (Iterator it = getFormasLiquidacionFiltradas().iterator(); it.hasNext();) {
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
     * Regresa un IPropertySelection con las opciones 'CLABE' y 'Cuenta'.
     *
     * @return StringPropertySelectionModel.
     */
    public IPropertySelectionModel getComboCLABECuenta() {
        return new StringPropertySelectionModel(new String[] { "CLABE", "Cuenta" });
    }

    /**
     * Utiliza el servicio del Site getBancosNacionalesXOperaSpei para localizar el banco con el
     * n&uacute;mero especificado.
     *
     * @param idBanco el n&uacute;mero de banco.
     * @return Banco.
     */
    private Banco getBanco(Long idBanco) {
        try {
            for (Iterator it = getSiteService().
                    getBancosNacionalesXOperaSpei(getTicket()).iterator(); it.hasNext();) {
                Banco banco = (Banco) it.next();
                if (banco.getIdBanco().equals(idBanco)) {
                    return banco;
                }
            }
            return null;
        }
        catch (Exception e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
            throw new ApplicationRuntimeException("No se encontr\u00f3 el banco con n\u00famero "
                    + idBanco, e);
        }
    }

    /**
     * Valida los datos capturados por el usuario e inserta el registro de la plantilla de
     * transferencia nacional en la base de datos.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {
    	IPlantillaTranNacional plantilla = getPlantilla();
    	SicaServiceData sd = getSicaServiceData();
    	Banco banco;
    	if (MODO_SUBMIT_MNEMONICO.equals(getModoSubmit())) {
    		FormaPagoLiq fpl = getFormaPagoLiq(plantilla.getMnemonico());
    		plantilla.setDivisa((Divisa) sd.find(Divisa.class, fpl.getIdDivisa()));
    		if (! plantilla.isSpei()) {
    			plantilla.setIdBanco(fpl.getIdBanco());
    			plantilla.setNombreBanco(fpl.getNombreBanco());
    		}
    		if (plantilla.isBancoTerceroSpei()) {
    			setClabeOCuenta("CLABE");
    			setBancosList(getBancosList());
    		}
    	}
    	if  (MODO_SUBMIT_1.equals(getModoSubmit())) {
    		IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
    		if (delegate.getHasErrors()) {
    			return;
    		}
    		if (plantilla.getBeneficiario().getIdPersona().intValue() < 0) {
    			delegate.record("Debe seleccionar un beneficiario para la plantilla.", null);
				return;
    		}
    		if (StringUtils.isEmpty(getSelectedTipoLiquidacion())) {
    			delegate.record("Debe seleccionar un Tipo de Liquidaci\u00f3n.", null);
    			return;
    		}
    		if (plantilla.getMnemonico() == null) {
    			delegate.record("Debe seleccionar un Mnem\u00f3nnico para la Plantilla.", null);
    			return;
    		}
    		if (StringUtils.isEmpty(plantilla.getClabe()) && !plantilla.isBancoBancoSpei()
    				&& MODO_SUBMIT.equals(getModoSubmit())) {
    			delegate.record("El No. CLABE o No. Cuenta no puede ser nulo.", null);
    			return;
    		}
    		if (StringUtils.isNotEmpty(plantilla.getClavePlantilla())) {
    			plantilla.setClavePlantilla(plantilla.getClavePlantilla().toUpperCase());
    		}
    		plantilla.setClabe(plantilla.getClabe().toUpperCase());
    		if (plantilla.isBancoBancoSpei()) {
    			plantilla.setClabe("-");
    		}
    		else if (!plantilla.isBancoVostroSpei()) {
    			if (plantilla.isBancoTerceroSpei()) {
    				setClabeOCuenta("CLABE");
    			}
    			if ("CLABE".equals(getClabeOCuenta())) {
    				if (plantilla.getClabe().length() != 18) {
    					delegate.record("El No. CLABE debe tener 18 caracteres.", null);
    					return;
    				}
    				try {
    					banco = getBanco(plantilla.getIdBanco());
    					com.ixe.ods.bup.model.Banco bupBanco = (com.ixe.ods.bup.model.Banco)
    					getSicaServiceData().find(com.ixe.ods.bup.model.Banco.class,
    							new Integer(banco.getIdBanco().intValue()));
    					if (!plantilla.getClabe().startsWith(bupBanco.getClaveBanxico())) {
    						delegate.record("No coincide el Banco seleccionado con el c\u00f3digo "
    								+ "de Banco en la CLABE", null);
    						return;
    					}
    				}
    				catch (Exception e) {
    					delegate.record(e.getMessage(), null);
    				}
    			}
    			else {
    				if (plantilla.getClabe().length() != 11) {
    					delegate.record("El No. Cuenta debe tener 11 caracteres. Si el n\u00famero"
    							+ " de cuenta tiene menos caracteres, rellene con ceros a la "
    							+ "izquierda.", null);
    					return;
    				}
    			}
    		}
    		if (plantilla.isBancoTerceroSpei() || plantilla.isBancoVostroSpei()
    				|| plantilla.isBancoBancoSpei()) {
    			if (plantilla.getIdBanco().doubleValue() > 0) {
    				banco = getBanco(plantilla.getIdBanco());
    				plantilla.setNombreBanco(banco.getNombreCorto());
    			}
    			else {
    				delegate.record("Debe seleccionar un banco para la plantilla.", null);
    				return;
    			}
    		}
    		if (getNombreCliente().equals(plantilla.getBeneficiario().getNombreCompleto())) {
    			plantilla.setStatusPlantilla(Plantilla.STATUS_PLANTILLA_ACTIVA);
    		}
    		
    		sd.store(plantilla);
    		if (PAGINA_MIGRACION_PLANTILLAS.equals(getPaginaAnterior())) {
    			MigracionPlantillas nextPage = (MigracionPlantillas) cycle.
    			getPage(PAGINA_MIGRACION_PLANTILLAS);
    			nextPage.setMapaEdicionMnemonico(new HashMap());
    			nextPage.setNuevoBeneficiario(new Persona());
        	   	nextPage.buscarCuentasPlantillas(cycle);
        	   	nextPage.setPaginaAnterior(getPageName());
        	   	nextPage.activarDelegate(true);
        	   	cycle.activate(nextPage);
            }
            else {
                cycle.activate("ConsultaPlantillasTranNacionales");
            }
        }
    }

    /**
     * Cancela la edici&oacute;n de la plantilla. Envia
     * a MigracionPlantillas.  
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
     * Modelo para la lista de bancos que operan por medio de SPEI.
     * 
     * @return BancosSelectionModel
     */
    public IPropertySelectionModel getBancosModel() {
    	setBancosList(getSiteService().getBancosNacionalesXOperaSpei(getTicket()));
        Collections.sort(getBancosList(), new Comparator() {
            public int compare(Object o1, Object o2) {
                Banco b1 = (Banco) o1;
                Banco b2 = (Banco) o2;
                return b1.getNombreCorto().compareTo(b2.getNombreCorto());
            }
        });
        Banco primerElemento = new Banco();
    	primerElemento.setIdBanco(new Long("0"));
    	primerElemento.setNombreCorto("Seleccione Un Banco");
    	primerElemento.setClaveBanxico("-1");
    	getBancosList().add(0, primerElemento);
    	setBancosList(getBancosList());
    	return new BancosSelectionModel(getBancosList());
    }
    
    /**
     * Modelo para la lista de beneficiarios del cliente.
     * 
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getBeneficiariosModel() {
    	List beneficiarios = getSicaServiceData().findBeneficiariosByNoCuentaByIdRol(getPlantilla().getContratoSica().getNoCuenta(),
    			FacultySystem.BENEFICIARIO_SICA_ROL);
    	Persona primerElemento = new Persona();
    	primerElemento.setIdPersona(new Integer(-1));
    	primerElemento.setNombreCorto("Seleccione un Beneficiario");
    	beneficiarios.add(0, primerElemento);
    	return new RecordSelectionModel(beneficiarios, "idPersona", "nombreCorto");
    }
    
    /**
     * Regresa el valor de la lista de bancos para el ComboBox
     * 
     * @return List
     */
    public abstract List  getBancosList();
  
    /**
     * Asigna el valor para la lista de bancos del ComboBox
     * 
     * @param bancosList La lista de bancos.
     */
    public abstract void  setBancosList(List bancosList);
    
    /**
     * Regresa el valor de clabeOCuenta.
     *
     * @return String.
     */
    public abstract String getClabeOCuenta();
    
    /**
     * Fija el valor de clabeOCuenta.
     *
     * @param clabeOCuenta El valor a asignar.
     */
    public abstract void setClabeOCuenta(String clabeOCuenta);

    /**
     * Regresa el valor de formasPagoLiq.
     *
     * @return List.
     */
    public abstract List getFormasPagoLiq();

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
     * Regresa el valor de nombreCliente.
     *
     * @return String.
     */
    public abstract String getNombreCliente();

    /**
     * Fija el valor de formasPagoLiq.
     *
     * @param formasPagoLiq El valor a asignar.
     */
    public abstract void setFormasPagoLiq(List formasPagoLiq);

    /**
     * Fija el valor de nombreCliente.
     *
     * @param mombreCliente El valor a asignar.
     */
    public abstract void setNombreCliente(String mombreCliente);

    /**
     * Regresa el valor de plantilla.
     *
     * @return IPlantillaTranNacional.
     */
    public abstract IPlantillaTranNacional getPlantilla();

    /**
     * Fija el valor de plantilla.
     *
     * @param plantilla El valor a asignar.
     */
    public abstract void setPlantilla(IPlantillaTranNacional plantilla);

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
     * Regresa el valor de modo_submit.
     *
     * @return String.
     */
    public abstract String getModoSubmit();

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
     * Regresa la lista de formas de liquidaci&oacute;n para recibimos o para entregamos.
     *
     * @return List.
     */
    private List obtenerFormasLiquidacion() {
    	List formasLiquidacion = getFormasPagoLiq();
        for (Iterator it = formasLiquidacion.iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            if (!fpl.getDesplegableSica().booleanValue()
                    || !Constantes.MNEMONICO_ACTIVO.equals(fpl.getStatus())) {
                it.remove();
            }
        }
        Collections.sort(formasLiquidacion, new Comparator() {
            public int compare(Object fp1, Object fp2) {
                if (((FormaPagoLiq) fp1).getMnemonico().indexOf("BALNETEO") >= 0) {
                    return 1;
                }
                if (((FormaPagoLiq) fp2).getMnemonico().indexOf("BALNETEO") >= 0) {
                    return -1;
                }
                return ((FormaPagoLiq) fp1).getDescripcion().toLowerCase().
                        compareTo(((FormaPagoLiq) fp2).getDescripcion().toLowerCase());
            }
        });
        return formasLiquidacion;
    }
    
    /**
     * Regresa la lista de formas de liquidaci&oacute;n para recibimos o para entregamos pero
     * filtradas de acuerdo a lo seleccionado en el combo de Tipos Liquidaci&oacute;n.
     *
     * @return List.
     */
	public List getFormasLiquidacionFiltradas() {
        List formasLiq = new ArrayList();
        List formasLiquidacionTmp = obtenerFormasLiquidacion();
        for (Iterator it = formasLiquidacionTmp.iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            if (fpl.getNombreTipoLiquidacion().trim().equals(getSelectedTipoLiquidacion())) {
                formasLiq.add(fpl);
            }
        }
		return formasLiq;
	}
	
    /**
	 * Obtiene los Tipos de Liquidaci&oacute;n.
	 *
	 * @return String[].
	 */
	private String[] getTiposLiquidacion() {
		return getTiposLiquidacion(obtenerFormasLiquidacion());
	}
    
    /**
	 * Modelo del combo de Tipos de Liquidaci&oacute;n.
	 *
	 * @return RecordSelectionModel
	 */
	public IPropertySelectionModel getTiposLiquidacionModel() {
		return new StringPropertySelectionModel(getTiposLiquidacion());
	}
    
    /**
     * Regresa el Tipo de Liquidaci&oacute;n seleccionado.
     *
     * @return String.
     */
    public abstract String getSelectedTipoLiquidacion();

    /**
     * Regresa el valor de mnemonico.
     *
     * @return String.
     */
    public abstract String getMnemonico();

    /**
     * Fija el valor del Tipo de Liquidaci&oacute;n seleccionado.
     *
     * @param selectedTipoLiquidacion El valor a asignar.
     */
    public abstract void setSelectedTipoLiquidacion(String selectedTipoLiquidacion);
    
    /**
     * Constante para el tipo de nombre de plantilla pantalla.
     */
    public static final String NOM_PANT_PLANT_TRANAC = "CapturaTransferenciaNal";

    /**
     * Constante para el tipo de submit de cambio e mnem&oacute;nico.
     */
    public static final String MODO_SUBMIT_MNEMONICO = "mnemonico";
    	
    /**
     * Constante para el modo del submit
     */
    public static final String MODO_SUBMIT_1 = "1";
    
    /**
     * Constante para el tipo de submit de cambio e mnem&oacute;nico.
     */
    public static final String MODO_SUBMIT = "agregar";
    
    /**
     * Constante Pagina MigracionPlantillas
     */
    public static final String PAGINA_MIGRACION_PLANTILLAS = "MigracionPlantillas";
}
