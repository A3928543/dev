/*
 * $Id: AbstractSeleccionFormaLiquidacion.java,v 1.10 2008/02/22 18:25:43 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.*;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.services.FormasPagoLiqService;

import java.util.*;

/**
 * @author Jean C. Favila
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:43 $
 */
public abstract class AbstractSeleccionFormaLiquidacion extends SicaPage {
	
    /**
     * Obtiene las plantillas que puede seleccionar el usuario. Si el detalle tiene un mnem&oacute;nico asignado,
     * presenta s&oacute;lo las que corresponden a &eacute;ste.
     *
     * @see com.ixe.ods.sica.services.FormasPagoLiqService#getMnemonicosPosiblesParaDealDetalle(String, com.ixe.ods.sica.model.DealDetalle).
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findPlantillasByContratoMnemonicos(String, java.util.List).
     * @throws SicaException Si algo sale mal.
     */
    protected void buscarPlantillas() throws SicaException {
        FormasPagoLiqService fpls = getFormasPagoLiqService();
        List todas = getSicaServiceData().findPlantillasByContratoMnemonicos(
                getDealDetalle().getDeal().getContratoSica().getNoCuenta(),
                fpls.getMnemonicosPosiblesParaDealDetalle(getTicket(), getDealDetalle()));
        List plantillasCuentaIxe = new ArrayList();
        List plantillasIntl = new ArrayList();
        List plantillasNal = new ArrayList();
        List plantillasTranNal = new ArrayList();
        Collections.sort(todas, new Comparator() {
            private String getCriterio1(IPlantilla p) {
                return p instanceof IPlantillaCuentaIxe ? p.getBeneficiario().getNombreCompleto() :
                        p instanceof IPlantillaIntl ? p.getBeneficiario().getNombreCompleto() :
                        p instanceof IPlantillaNacional ? p.getBeneficiario().getNombreCompleto() :
                        p instanceof IPlantillaTranNacional ?
                                p.getBeneficiario().getNombreCompleto() : null;
            }
            private String getCriterio2(IPlantilla p) {
                return p instanceof IPlantillaCuentaIxe ? ((IPlantillaCuentaIxe) p).getNoCuentaIxe() :
                        p instanceof IPlantillaIntl ? ((IPlantillaIntl) p).getNoCuentaBeneficiario() :
                        p instanceof IPlantillaNacional ? "" :
                        p instanceof IPlantillaTranNacional ? ((IPlantillaTranNacional) p).getClabe() : null;
            }
            public int compare(Object p1, Object p2) {
                if (p1 instanceof IPlantilla && p2 instanceof IPlantilla) {
                    int res = getCriterio1((IPlantilla) p1).compareTo(getCriterio1((IPlantilla) p2));
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
     * Regresa el valor de dealDetalle.
     *
     * @return DealDetalle.
     */
    public abstract DealDetalle getDealDetalle();

    /**
     * Fija el valor de dealDetalle.
     *
     * @param detalle El valor a asignar.
     */
    public abstract void setDealDetalle(DealDetalle detalle);

    /**
     * Fija el valor de formasPagoLiq.
     *
     * @param formasPagoLiq El valor a asignar.
     */
    public abstract void setFormasPagoLiq(List formasPagoLiq);    

    /**
     * Regresa el valor de plantillasCuentaIxe.
     *
     * @return List.
     */
    public abstract List getPlantillasCuentaIxe();

    /**
     * Fija el valor de plantillasCuentaIxe.
     *
     * @param plantillasCuentaIxe El valor a asignar.
     */
    public abstract void setPlantillasCuentaIxe(List plantillasCuentaIxe);

    /**
     * Regresa el valor de plantillasIntl.
     *
     * @return List.
     */
    public abstract List getPlantillasIntl();

    /**
     * Fija el valor de plantillasIntl.
     *
     * @param plantillasIntl El valor a asignar.
     */
    public abstract void setPlantillasIntl(List plantillasIntl);

    /**
     * Regresa el valor de plantillasNal.
     *
     * @return List.
     */
    public abstract List getPlantillasNal();

    /**
     * Fija el valor de plantillasNal.
     *
     * @param plantillasNal El valor a asignar.
     */
    public abstract void setPlantillasNal(List plantillasNal);

    /**
     *  Regresa el valor de plantillasTranNal.
     *
     * @return List.
     */
    public abstract List getPlantillasTranNal();

    /**
     * Fija el valor de plantillasTranNal.
     *
     * @param plantillasTranNal El valor a asignar.
     */
    public abstract void setPlantillasTranNal(List plantillasTranNal);
}
