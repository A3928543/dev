/*
 * $Id: SeleccionPlantillasInterbancario.java,v 1.22.42.1.8.2.18.1 2016/10/06 00:40:48 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.interbancario;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.components.FormasPagoLiqSelectionModel;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.IPlantilla;
import com.ixe.ods.sica.model.IPlantillaCuentaIxe;
import com.ixe.ods.sica.model.IPlantillaIntl;
import com.ixe.ods.sica.model.IPlantillaNacional;
import com.ixe.ods.sica.model.IPlantillaTranNacional;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.pages.deals.AbstractSeleccionFormaLiquidacion;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.treasury.dom.common.Banco;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.ixe.treasury.dom.common.Pais;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * P&aacute;gina que permite al usuario seleccionar una plantilla para ser asignada a un detalle de
 * deal interbancario.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.22.42.1.8.2.18.1 $ $Date: 2016/10/06 00:40:48 $
 */
public abstract class SeleccionPlantillasInterbancario extends AbstractSeleccionFormaLiquidacion {

	/**
	 * Asigna los valores necesarios al activarse la p&aacute;gina.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        String ticket = getTicket();
        DealDetalle det = getDealDetalle();
        // Revisa si hay un solo mnemonico posible y lo asigna:
        if (det.getMnemonico() == null) {
            List fpls = getFormasPagoLiqService().getProductosPizarron(ticket,
                    det.isRecibimos(), det.getDivisa().getIdDivisa(),
                    det.getClaveFormaLiquidacion());
            if (fpls.size() == 1) {
                det.setMnemonico(((FormaPagoLiq) fpls.get(0)).getMnemonico());
                
                try{
                	
                	if( getDealDetalle().getSustituyeA() == null){
                		consumirLineaCredito();
            		}else{
            			getLineaCambioServiceData().aplicarUsoLCDetalleSplit(getDealDetalle());
            		}
                	getSicaServiceData().update(det);
                }catch(SicaException sicaException){
            		sicaException.printStackTrace();
            		getDealDetalle().setMnemonico(null);
            		getDelegate().record(sicaException.getMessage(), null);
            		return;
            	}
            }
        }
        if (det.getMnemonico() == null) {
            setModoMnemonico(true);
        }
        else if (!getFormaPagoLiq(getDealDetalle().getMnemonico()).getUsaPlantilla().
                booleanValue()) {
            redirigir(cycle);
        }
        else {
            setModoMnemonico(false);
            setFormasPagoLiq(getFormasPagoLiqService().
                    getProductosPizarron(ticket, det.isRecibimos(),
                    det.getDivisa().getIdDivisa(), det.getClaveFormaLiquidacion()));
        }
        listarPlantillas();
    }

    /**
	 * Obtiene los Tipos de Liquidaci&oacute;n.
	 *
	 * @return String[].
	 */
	private String[] getTiposLiquidacion() {
		DealDetalle det = getDealDetalle();
        List formasLiquidacionTmp = getFormasPagoLiqService().getProductosNoPizarron(getTicket(),
                det.isRecibimos(), det.getDivisa().getIdDivisa(), null);
		return getTiposLiquidacion(formasLiquidacionTmp);
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
	 * Modelo del combo de Mnemm&oacute;nicos.
	 *
	 * @return IPropertySelecctionModel
	 */
    public IPropertySelectionModel getMnemonicosModel() {
        List fpls = new ArrayList();
        DealDetalle det = getDealDetalle();
        if (Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
            List formasLiquidacionTmp = getFormasPagoLiqService().
                    getProductosNoPizarron(getTicket(), det.isRecibimos(),
                            det.getDivisa().getIdDivisa(), null);
            for (Iterator it = formasLiquidacionTmp.iterator(); it.hasNext();) {
                FormaPagoLiq fpl = (FormaPagoLiq) it.next();
                if (fpl.getNombreTipoLiquidacion().trim().equals(getSelectedTipoLiquidacion()) &&
                        fpl.getDesplegableSica().booleanValue() &&
                        Constantes.MNEMONICO_ACTIVO.equals(fpl.getStatus())) {
                    fpls.add(fpl);
                }
            }
        }
        else {
            List formasLiquidacionTmp = getFormasPagoLiqService().
                    getProductosPizarron(getTicket(), det.isRecibimos(),
                            det.getDivisa().getIdDivisa(), det.getClaveFormaLiquidacion());
            for (Iterator it = formasLiquidacionTmp.iterator(); it.hasNext();) {
                FormaPagoLiq fpl = (FormaPagoLiq) it.next();
                if (fpl.getDesplegableSica().booleanValue() &&
                        Constantes.MNEMONICO_ACTIVO.equals(fpl.getStatus())) {
                    fpls.add(fpl);
                }
            }
        }
        return new FormasPagoLiqSelectionModel(fpls);
    }

    /**
     * Asigna el valor del mnem&oacute;nico seleccionado.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void seleccionarMnemonico(IRequestCycle cycle) {
        if (getModoSubmit() == 1) {
            if (Divisa.PESO.equals(getDealDetalle().getDivisa().getIdDivisa())) {
                if (StringUtils.isEmpty(getSelectedTipoLiquidacion())) {
                    getDelegate().record("Debe seleccionar Tipo de Liquidaci\u00f3n para poder " +
                            "escoger Mnem\u00f3nico.", null);
                    return;
                }
                if (StringUtils.isEmpty(getMnemonico())) {
                    getDelegate().record("Debe seleccionar Mnem\u00f3nico.", null);
                    return;
                }
            }
            if (getMnemonico().indexOf("BALNETEO") >= 0) {
                FormaPagoLiq fpl = ((SicaSiteCache) getApplicationContext().
                        getBean("sicaSiteCache")).getFormaPagoLiq(getTicket(), getMnemonico());
                if (fpl.getMontoMaximo() != null &&
                        getDealDetalle().getMonto() > fpl.getMontoMaximo().doubleValue()) {
                    getDelegate().record("El valor m\u00e1ximo para el mnem\u00f3nico " +
                            "seleccionado es " + fpl.getMontoMaximo(), null);
                    return;
                }
            }
            
            setDealDetalle(getSicaServiceData().findDealDetalle(getDealDetalle().getIdDealPosicion()));
            
            getDealDetalle().setMnemonico(getMnemonico());
            
            try{
            	
            	if( getDealDetalle().getSustituyeA() == null){
            		consumirLineaCredito();
        		}else{
        			getLineaCambioServiceData().aplicarUsoLCDetalleSplit(getDealDetalle());
        		}
            	getSicaServiceData().update(getDealDetalle());
            }catch(SicaException sicaException){
        		sicaException.printStackTrace();
        		getDealDetalle().setMnemonico(null);
        		getDelegate().record(sicaException.getMessage(), null);
        		return;
        	}
            
            if (! getFormaPagoLiq(getDealDetalle().getMnemonico()).getUsaPlantilla().
                    booleanValue()) {
                redirigir(cycle);
            }
            else {
                setModoMnemonico(false);
            }
        }
        listarPlantillas();
    }

    /**
     *
     */
    private void listarPlantillas() {
        DealDetalle det = getDealDetalle();
        List plantillasCuentaIxe = new ArrayList();
        List plantillasIntl = new ArrayList();
        List plantillasNal = new ArrayList();
        List plantillasTranNal = new ArrayList();
        List todas = getSicaServiceData().findPlantillasByContratoMnemonicos(getTicket(),
                det.getDeal().getContratoSica().getNoCuenta(), det.getMnemonico(),
                det.getClaveFormaLiquidacion(), det.getDivisa().getIdDivisa(),
                det.isRecibimos());
        Collections.sort(todas, new Comparator() {
            private String getCriterio1(IPlantilla p) {
                return p instanceof IPlantillaCuentaIxe ? p.getBeneficiario().getNombreCompleto() :
                        p instanceof IPlantillaIntl ? p.getBeneficiario().getNombreCompleto() :
                        p instanceof IPlantillaNacional ? p.getBeneficiario().getNombreCompleto() :
                        p instanceof IPlantillaTranNacional ?
                                p.getBeneficiario().getNombreCompleto() : null;
            }
            private String getCriterio2(IPlantilla p) {
                return p instanceof IPlantillaCuentaIxe ?
                        ((IPlantillaCuentaIxe) p).getNoCuentaIxe() :
                        p instanceof IPlantillaIntl ?
                                ((IPlantillaIntl) p).getNoCuentaBeneficiario() :
                        p instanceof IPlantillaNacional ? "" :
                        p instanceof IPlantillaTranNacional ?
                                ((IPlantillaTranNacional) p).getClabe() : null;
            }
            public int compare(Object p1, Object p2) {
                if (p1 instanceof IPlantilla && p2 instanceof IPlantilla) {
                    int res = getCriterio1((IPlantilla) p1).
                            compareTo(getCriterio1((IPlantilla) p2));
                    if (res != 0) {
                        return res;
                    }
                    return getCriterio2((IPlantilla) p1).compareTo(getCriterio2((IPlantilla) p2));
                }
                return -1;
            }
        });
        for (Iterator it = todas.iterator(); it.hasNext(); ) {
            IPlantilla plantilla = (IPlantilla) it.next();
            if (plantilla instanceof IPlantillaCuentaIxe) {
                plantillasCuentaIxe.add(plantilla);
            }
            if (plantilla instanceof IPlantillaIntl) {
                plantillasIntl.add(plantilla);
            }
            else if (plantilla instanceof IPlantillaNacional) {
                plantillasNal.add(plantilla);
            }
            else  if (plantilla instanceof IPlantillaTranNacional) {
                plantillasTranNal.add(plantilla);
            }
        }
        setPlantillasCuentaIxe(plantillasCuentaIxe);
        setPlantillasIntl(plantillasIntl);
        setPlantillasNal(plantillasNal);
        setPlantillasTranNal(plantillasTranNal);

    }

    /**
     * Asigna la plantilla para el deal.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void seleccionarPlantilla(IRequestCycle cycle) {
        SicaServiceData sd = getSicaServiceData();
        setDealDetalle(getSicaServiceData().findDealDetalle(getDealDetalle().getIdDealPosicion()));
        DealDetalle det = getDealDetalle();
        IPlantilla plantilla = (IPlantilla) sd.find(Plantilla.class,
                (Integer) cycle.getServiceParameters()[0]);
        //Validando, de tratarse de una Plantilla Cuenta Ixe, que la Cuenta Ixe aun este Activa.
        //Se conserva validación solo para el modo BUP
        if (plantilla instanceof IPlantillaCuentaIxe &&
        		getSicaServiceData().findParametro(ParametroSica.MODO_BUP_ALTAMIRA).getValor().equals("BUP")) {
            String[] ctasEje = getSicaServiceData().findCuentasEje(plantilla.
                    getBeneficiario().getIdPersona());
            boolean valida = false;
            for (int i = 0; i < ctasEje.length && !valida; i++) {
                String cta = ctasEje[i];
                if (cta.trim().equals(((IPlantillaCuentaIxe) plantilla).getNoCuentaIxe().trim())) {
                    valida = true;
                }
            }
            if (!valida) {
                getDelegate().record("La Cuenta Eje de la Plantilla seleccionada no esta Activa " +
                        "y/o Vigente.", null);
                return;
            }
        }
        else if (plantilla instanceof IPlantillaIntl) {
        	//JDCH
	        	if(((IPlantillaIntl)plantilla).getClaveBancoInterm()== null){
	        		
	        		SicaSiteCache ssc = (SicaSiteCache) getApplicationContext().getBean("sicaSiteCache");
	        		
	        		Banco banco = ssc.encontrarBanco(getTicket(), (IPlantillaIntl)plantilla,false);//regresa el banco Beneficiario
	        		Pais tesPais = ssc.getPaisXClaveISO(getTicket(), banco.getIdPais());
	        		
	        		if ("S".equals(tesPais.getSancionadoOFAC())){
	      			  	System.out.println("El pais del banco Beneficiario esta dentro de los sancionados por la OFAC:   "+ plantilla.getIdPlantilla());
	      			  getDelegate().record("El país al que pertenece el Banco Beneficiario se encuentra en la lista de países sancionados por OFAC, " +
	      			  	"no puede continuar con el registro de la transferencia.",null);
	      			return;
	            		
	            	}
	        		if ("S".equals(tesPais.getAltoRiesgoGAFI())){
	      			  	System.out.println("El pais del banco Beneficiario es considerado de alto riesgo por GAFI:   "+ plantilla.getIdPlantilla());
	      			  getDelegate().record("El país al que pertenece el Banco Beneficiario se encuentra en la lista de países de alto riesgo de GAFI, " +
	      			  	"no puede continuar con el registro de la transferencia.",null);
	      			return;
	            		
	            	}
	        	}
	        	else{
	        		SicaSiteCache ssc = (SicaSiteCache) getApplicationContext().getBean("sicaSiteCache");
	        		
	        		Banco banco = ssc.encontrarBanco(getTicket(), (IPlantillaIntl)plantilla,false); //regresa el banco Beneficiario
	        		Pais tesPais = ssc.getPaisXClaveISO(getTicket(), banco.getIdPais());
	        		
	        		if ("S".equals(tesPais.getSancionadoOFAC())){
	      			  	System.out.println("El pais del banco beneficiario esta dentro de los sancionados por la OFAC:   "+ plantilla.getIdPlantilla());
	      			  getDelegate().record("El país al que pertenece el Banco Beneficiario se encuentra en la lista de países sancionados por OFAC, " +
	      			  	"no puede continuar con el registro de la transferencia.",null);
	      			return;
	            		
	            	}
	        		if ("S".equals(tesPais.getAltoRiesgoGAFI())){
	      			  	System.out.println("El pais del banco beneficiario esta considerado de alto riesgo por GAFI:   "+ plantilla.getIdPlantilla());
	      			  getDelegate().record("El país al que pertenece el Banco Beneficiario se encuentra en la lista de países de alto riesgo de GAFI, " +
	      			  	"no puede continuar con el registro de la transferencia.",null);
	      			return;
	            		
	            	}
	        		
	        		Banco bancoInterm = ssc.encontrarBanco(getTicket(), (IPlantillaIntl)plantilla,true);//regresa el banco Intermediario
	        		Pais tesPaisBcoInterm = ssc.getPaisXClaveISO(getTicket(), bancoInterm.getIdPais());
	        		
	        		if ("S".equals(tesPaisBcoInterm.getSancionadoOFAC())){
	      			  	System.out.println("El pais del banco intermediario esta dentro de los sancionados por la OFAC:   "+ plantilla.getIdPlantilla());
	      			  getDelegate().record("El país al que pertenece el Banco Intermediario se encuentra en la lista de países sancionados por OFAC, " +
	      			  	"no puede continuar con el registro de la transferencia.",null);
	      			return;
	            		
	            	}
	        		if ("S".equals(tesPaisBcoInterm.getAltoRiesgoGAFI())){
	      			  	System.out.println("El pais del banco intermediario esta considerado de alto riesgo por GAFI:   "+ plantilla.getIdPlantilla());
	      			  getDelegate().record("El país al que pertenece el Banco Intermediario se encuentra en la lista de países de alto riesgo de GAFI, " +
	      			  	"no puede continuar con el registro de la transferencia.",null);
	      			return;
	            		
	            	}
	        		
	        	}
        	//JDCH
            det.setInstruccionesPagador(((IPlantillaIntl) plantilla).getInstrBcoBeneficiario());
            det.setInstruccionesIntermediario(((IPlantillaIntl) plantilla).
                    getInstrBcoIntermediario());
        }
        det.setPlantilla(plantilla);
        
        boolean isMnemonicoAsignado=false;
        if (det.getMnemonico() != null){
        	isMnemonicoAsignado = true;
        }
        
        
        det.setMnemonico(plantilla.getMnemonico());
        getDealService().calcularComisionDealDetalle(getTicket(), det, false);
        
        if (!isMnemonicoAsignado){
        	try{
        		if( det.getSustituyeA() == null){
        			consumirLineaCredito();
        		}else{
        			getLineaCambioServiceData().aplicarUsoLCDetalleSplit(det);
        			
        		}
        		
        	}catch(SicaException sicaException){
        		sicaException.printStackTrace();
        		det.setMnemonico(null);        		
        		throw new SicaException(sicaException.getMessage());
        	}
        }
        
        sd.update(det);
        redirigir(cycle);
    }

    
    /**
     * Metodo para consumir Linea de Credito en base al detalle seleccionado del Deal
     */
    private void consumirLineaCredito() {

		boolean bMnemonico = getLineaCambioServiceData().validarAplicablesTfPagAnt(getTicket(), getDealDetalle().getMnemonico(),
		getDealDetalle().getClaveFormaLiquidacion(), getDealDetalle().isRecibimos());
		if(bMnemonico && getDealDetalle().getMnemonico()!= null){
		
		    if((getDealDetalle().getDeal().isPagoAnticipado() || getDealDetalle().getDeal().isTomaEnFirme()) && !getDealDetalle().getDeal().isDealBalanceo()){
				getLineaCambioServiceData().consumirLineaCreditoPA(getDealDetalle().getDeal(), getDealDetalle());
			}else if(!getDealDetalle().getDeal().isDealBalanceo()){
				getLineaCambioServiceData().consumirLineaCredito(getDealDetalle().getDeal(),  getDealDetalle());
			}
		}
	}
    
    
    
    /**
     * Envia a la p&aacute;gina de Captura de Deal Interbancario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void redirigir(IRequestCycle cycle) {
        try {
            CapturaDealInterbancario nextPage = (CapturaDealInterbancario) cycle.
                    getPage("CapturaDealInterbancario");
            nextPage.setDeal(getSicaServiceData().findDeal(getDealDetalle().getDeal().getIdDeal()));
            cycle.activate(nextPage);
        }
        catch (SicaException e) {
            IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
            delegate.record(e.getMessage(), null);
            debug(e);
        }
    }

    /**
     * Establece el valor de modoMnemonico.
     *
     * @param modoMnemonico El valor a asignar.
     */
    public abstract void setModoMnemonico(boolean modoMnemonico);

    /**
     * Regresa el valor de mnemonico.
     *
     * @return String.
     */
    public abstract String getMnemonico();

    /**
     * Regresa el Tipo de Liquidaci&oacute;n seleccionado.
     *
     * @return String.
     */
    public abstract String getSelectedTipoLiquidacion();

    /**
     * Fija el valor del Tipo de Liquidaci&oacute;n seleccionado.
     *
     * @param selectedTipoLiquidacion El valor a asignar.
     */
    public abstract void setSelectedTipoLiquidacion(String selectedTipoLiquidacion);

    /**
	 * Regresa el modo de submit de la pagina. Si es por boton o por combo.
	 * @return int
	 */
	public abstract int getModoSubmit();
}
