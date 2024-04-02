/*
 * $Id: IAsignacionBeneficiario.java,v 1.9 2008/02/22 18:25:01 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.plantillas;

import com.ixe.ods.bup.model.Persona;
import org.apache.tapestry.IPage;

/**
 * Interfaz que deben implementar las p&aacute;ginas que permitea asignar el Beneficiario capturado a la plantilla
 *
 * @author Edgar Leija
 * @version $Revision: 1.9 $ $Date: 2008/02/22 18:25:01 $
 */
public interface IAsignacionBeneficiario extends IPage {

    /**
     * Crea el beneficiario y lo asigna a la plantilla.
     *
     * @param beneficiario el objeto Persona que corresponde al beneficario que se desea crear.
     */
    void creaBeneficiario(Persona beneficiario);
}
