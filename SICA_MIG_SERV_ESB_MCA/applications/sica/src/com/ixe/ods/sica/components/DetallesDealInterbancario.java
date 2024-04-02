/*
 * $Id: DetallesDealInterbancario.java,v 1.15.54.1.12.1 2017/10/16 17:40:44 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import java.util.Map;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.IPlantillaIntl;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.deals.formliq.CapturaTransferenciaIntl;
import com.ixe.ods.sica.pages.deals.interbancario.CapturaDealInterbancario;
import com.ixe.ods.sica.pages.deals.interbancario.SeleccionPlantillasInterbancario;
import com.ixe.treasury.dom.common.FormaPagoLiq;

/**
 * Componente para desplegar los detalles de un deal interbancario.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.15.54.1.12.1 $ $Date: 2017/10/16 17:40:44 $
 */
public abstract class DetallesDealInterbancario extends BaseComponent {

    /**
     * Regresa true si el detalle es de 'Entregamos', el producto es una transferencia extranjera, y
     * el status es: 'AL' o 'CO'. Esto indica que es posible editar las instrucciones al
     * beneficiario, intermediario y pagador.
     *
     * @param det El detalle del deal.
     * @return boolean.
     */
    public boolean isPosibleCapturarInstrucciones(DealDetalle det) {
        if (det.getMnemonico() == null || det.getPlantilla() == null) {
        	return false;
        }
        SicaPage pagina = (SicaPage) getPage();
        FormaPagoLiq fpl = pagina.getFormaPagoLiq(det.getMnemonico());
        return !det.isRecibimos() &&
                Constantes.TRANSFERENCIA.equals(fpl.getClaveFormaLiquidacion()) &&
                (DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(det.getStatusDetalleDeal()) ||
                        DealDetalle.STATUS_DET_COMPLETO.equals(det.getStatusDetalleDeal()));
    }

    /**
     * Activa la p&aacute;gina <code>CapturaTransferenciaIntl</code>.
     *
     * @param cycle El IRequestCycle.
     */
    public void capturarInstrucciones(IRequestCycle cycle) {
        CapturaTransferenciaIntl nextPage = (CapturaTransferenciaIntl) cycle.
                getPage("CapturaTransferenciaIntl");
        DealDetalle det = ((SicaPage) getPage()).getSicaServiceData().
                findDealDetalle(((Integer) cycle.getServiceParameters()[0]).intValue());

        nextPage.setDealDetalle(det);
        nextPage.setSinConIntermediario(
                ((IPlantillaIntl) det.getPlantilla()).getClaveBancoInterm() != null ?
                TablaPlantillaInternacional.CON_INTERMEDIARIO :
                        TablaPlantillaInternacional.SIN_INTERMEDIARIO);
        nextPage.setModo(CapturaTransferenciaIntl.MODO_CAPTURA);
        nextPage.setInstruccionesBeneficiario(det.getInstruccionesBeneficiario());
        
        //F65211 Complemento - Obligatorio propósito de transferencia Ley 115 LIC
        cycle.activate(nextPage);
        nextPage.activate(cycle);

    }

    /**
     * Activa la p&aacute;gina <code>SeleccionPlantillasInterbancario</code> para seleccionar la
     * forma de liquidaci&oacute;n del deal.
     *
     * @param cycle El ciclo de la p&aacute;gina
     */
    public void capturarFormaLiquidacion(IRequestCycle cycle) {
        DealDetalle det = ((SicaPage) getPage()).getSicaServiceData().
                findDealDetalle(((Integer) cycle.getServiceParameters()[0]).intValue());
        SeleccionPlantillasInterbancario nextPage = (SeleccionPlantillasInterbancario) cycle.
                getPage("SeleccionPlantillasInterbancario");

        det.getDeal().getPromotor().getNombreCompleto();
        nextPage.setDealDetalle(det);
        nextPage.activate(cycle);
    }

    /**
     * Modifica los par&aacute;metros del detalle.
     *
     * @param cycle El ciclo de la p&aacute;gina.
	 */
    public void modificarDetalle(IRequestCycle cycle) {
        CapturaDealInterbancario nextPage = (CapturaDealInterbancario) getPage();

        nextPage.solicitarModificacionDetalle(cycle);
        setModifMap(nextPage.getModifMap());
    }

    /**
     * Llama a <code>partirDetalle de CapturaDealInterbancario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void partirDetalle(IRequestCycle cycle) {
        CapturaDealInterbancario nextPage = (CapturaDealInterbancario) getPage();
        nextPage.partirDetalle(cycle);        
    }
    
    /**
     * Fija el valor de modifMap.
     *
     * @param modifMap El valor a asignar.
     */
    public abstract void setModifMap(Map modifMap);
}
