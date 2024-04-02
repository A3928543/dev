/*
 * $Id: TiposFormasLiquidacionServiceImpl.java,v 1.1.2.1 2010/10/08 01:10:59 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.services.FormasPagoLiqService;
import com.ixe.treasury.dom.common.FormaPagoLiq;

/**
 * 
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:10:59 $
 */
public class TiposFormasLiquidacionServiceImpl implements TiposFormasLiquidacionService {

    /**
	 * Obtiene los Tipos de Liquidaci&oacute;n.
	 *
	 * @return String[].
	 */
	public String[] getTiposLiquidacion(String ticket, DealDetalle det) {
        List formasLiquidacionTmp;

        if (Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
            formasLiquidacionTmp = getFormasPagoLiqService().getProductosNoPizarron(ticket,
                    det.isRecibimos(), det.getDivisa().getIdDivisa(), null);
        }
        else {
            formasLiquidacionTmp = getFormasPagoLiqService().getProductosPizarron(ticket,
                    det.isRecibimos(), det.getDivisa().getIdDivisa(), null);
        }
		return getTiposLiquidacion(formasLiquidacionTmp);
	}

    /**
     * Obtiene los Tipos de Liquidaci&oacute;n.
     *
     * @param formasPagoLiq La lista de formas de liquidaci&oacute;n.
     * @return String[].
     */
    private String[] getTiposLiquidacion(List formasPagoLiq) {
        List clavesTiposLiqTmp = new ArrayList();
        for (Iterator it = formasPagoLiq.iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            if (!clavesTiposLiqTmp.contains(fpl.getNombreTipoLiquidacion().trim())) {
                clavesTiposLiqTmp.add(fpl.getNombreTipoLiquidacion().trim());
            }
        }
        String[] clavesTiposLiq = new String[clavesTiposLiqTmp.size()];
        int j = 0;
        for (Iterator i = clavesTiposLiqTmp.iterator(); i.hasNext(); j++) {
            clavesTiposLiq[j] = (String) i.next();
        }
        return clavesTiposLiq;
    }

	/**
	 * Lista de Mnemm&oacute;nicos.
	 *
	 * @return IPropertySelecctionModel
	 */
    public List getMnemonicos(String ticket, DealDetalle det, String tipoLiquidacion) {
        List fpls = new ArrayList();
        if (Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
            List formasLiquidacionTmp = getFormasPagoLiqService().
                    getProductosNoPizarron(ticket, det.isRecibimos(),
                            det.getDivisa().getIdDivisa(), null);
            for (Iterator it = formasLiquidacionTmp.iterator(); it.hasNext();) {
                FormaPagoLiq fpl = (FormaPagoLiq) it.next();
                if (fpl.getNombreTipoLiquidacion().trim().equals(tipoLiquidacion) &&
                        fpl.getDesplegableSica().booleanValue() &&
                        Constantes.MNEMONICO_ACTIVO.equals(fpl.getStatus())) {
                    fpls.add(fpl);
                }
            }
        }
        else {
            List formasLiquidacionTmp = getFormasPagoLiqService().
                    getProductosPizarron(ticket, det.isRecibimos(),
                            det.getDivisa().getIdDivisa(), det.getClaveFormaLiquidacion());
            for (Iterator it = formasLiquidacionTmp.iterator(); it.hasNext();) {
                FormaPagoLiq fpl = (FormaPagoLiq) it.next();
                if (fpl.getDesplegableSica().booleanValue() &&
                        Constantes.MNEMONICO_ACTIVO.equals(fpl.getStatus())) {
                    fpls.add(fpl);
                }
            }
        }
        return fpls;
    }
    
    /**
     * Regresa el valor de formasPagoLiqService.
     *
     * @return formasPagoLiqService.
     */
    public FormasPagoLiqService getFormasPagoLiqService() {
        return formasPagoLiqService;
    }

    /**
     * Establece el valor de formasPagoLiqService.
     *
     * @param formasPagoLiqService El valor a asignar.
     */
    public void setFormasPagoLiqService(FormasPagoLiqService formasPagoLiqService) {
        this.formasPagoLiqService = formasPagoLiqService;
    }

    private FormasPagoLiqService formasPagoLiqService;
}
