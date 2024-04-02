/*
 * $Id: AbstractCapturaDealInterbancario.java,v 1.11 2010/03/18 17:05:49 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.interbancario;

import java.util.Iterator;
import java.util.List;

import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.IPlantilla;
import com.ixe.ods.sica.model.IPlantillaIntl;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.pages.deals.AbstractCapturaDeal;

/**
 * Superclase de las p&aacute;ginas de captura de deal interbancario. Provee los m&eacute;todos
 * comunes.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2010/03/18 17:05:49 $
 */
public abstract class AbstractCapturaDealInterbancario extends AbstractCapturaDeal {
	
	/**
	 * Compara si la divisa de referencia es Peso.
	 * 
	 * @return boolean.
	 */
    public boolean isDivisaReferenciaPeso() {
        return Divisa.PESO.equals(getDivisaReferencia().getIdDivisa());
    }    

    /**
     * Regresa la primera plantilla activada encontrada para el contrato sica especificado. Si la
     * plantilla es para transferencias internacionales, debe coincidir la divisa con el
     * par&aacute;metro idDivisa.
     *
     * @param tipoPlantilla El tipo de Plantilla a buscar.
     * @param noCuenta El n&uacute;mero de contrato sica.
     * @param idDivisa El id de la divisa, para el caso de transferencias internacionales.
     * @return IPlantilla.
     */
    protected IPlantilla getPlantillaConTipoParaContrato(String tipoPlantilla, String noCuenta,
                                                         String idDivisa) {
        List plantillas = getSicaServiceData().findPlantillas(tipoPlantilla, noCuenta);
        for (Iterator it = plantillas.iterator(); it.hasNext(); ) {
            IPlantilla p = (IPlantilla) it.next();
            if (Plantilla.STATUS_PLANTILLA_ACTIVA.equals(p.getStatusPlantilla())) {
                if ("TranNacionales".equals(tipoPlantilla)) {
                    return p;
                }
                else {
                    IPlantillaIntl pint = (IPlantillaIntl) p;
                    if (idDivisa.equals(pint.getDivisa().getIdDivisa())) {
                        return pint;
                    }
                }
            }
        }
        for (Iterator it = plantillas.iterator(); it.hasNext(); ) {
            IPlantilla p = (IPlantilla) it.next();
            if (Plantilla.STATUS_PLANTILLA_PENDIENTE.equals(p.getStatusPlantilla())) {
                if ("TranNacionales".equals(tipoPlantilla)) {
                    return p;
                }
                else {
                    IPlantillaIntl pint = (IPlantillaIntl) p;
                    if (idDivisa.equals(pint.getDivisa().getIdDivisa())) {
                        return pint;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Regresa la divisa de referencia de la mesa del deal.
     *
     * @return Divisa.
     */
    protected Divisa getDivisaReferencia() {
        return getDeal().getCanalMesa().getMesaCambio().getDivisaReferencia();
    }
}
