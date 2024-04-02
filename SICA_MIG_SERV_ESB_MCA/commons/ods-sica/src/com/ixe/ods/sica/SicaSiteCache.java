/*
 * $Id: SicaSiteCache.java,v 1.18 2009/06/09 19:47:30 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.IPlantillaIntl;
import com.ixe.ods.sica.model.PlantillaIntl;
import com.ixe.treasury.dom.common.Banco;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.ixe.treasury.dom.common.Pais;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Bean que mantiene un 'cache de las formas de liquidaci&oacute;n' v&aacute;lidas para el sica, con
 * el fin de evitar el viaje constante a los servicios del site. El cach&eacute; se refresca cada
 * 5 minutos.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.18 $ $Date: 2009/06/09 19:47:30 $
 */
public class SicaSiteCache implements Serializable {

    /**
     * Constructor por default.
     */
    public SicaSiteCache() {
        super();
    }

    /**
     * Regresa el objeto FormaPagoLiq que corresponde al idMatriz especificado.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idMatriz La llave primaria del objeto que se desea encontrar.
     * @return FormaPagoLiq.
     * @throws SicaException Si no hay FormaPagoLiq con el idMatriz especificado.
     */
    public FormaPagoLiq getFormaPagoLiq(String ticket, Long idMatriz) throws SicaException {
        List formasPagoLiq = obtenerFormasPagoLiq(ticket);
        for (Iterator it = formasPagoLiq.iterator(); it.hasNext();) {
            FormaPagoLiq formaPagoLiq = (FormaPagoLiq) it.next();
            if (formaPagoLiq.getIdMatriz().equals(idMatriz)) {
                return formaPagoLiq;
            }
        }
        throw new SicaException("No se encontr\u00f3 la forma de pago y liquidaci\u00f3n "
                + "n\u00famero " + idMatriz);
    }

    /**
     * Regresa todas las formasPagoLiq para el sistema SICA en el canal de Promoci&oacute;n Mayoreo.
     * El resultado se guarda en la variable de instancia <code>formasPagoLiq</code> para que la
     * siguiente vez que se invoca este m\u00e9todo, se regresa el valor de esta variable sin tener
     * que viajar al SITE.
     *
     * @param ticket El ticket para comunicarse con el SITE.
     * @return List.
     */
    public synchronized List obtenerFormasPagoLiq(String ticket) {
        if (formasPagoLiq == null) {
            refrescar(ticket);
        }
        return formasPagoLiq;
    }

    /**
     * Itera la lista formasPagoLiq hasta encontrar la que corresponde al mnem&oacute;nico recibido
     * como par&aacute;metro, y regresa esta FormaPagoLiq.
     *
     * @param ticket El ticket para comunicarse con el SITE.
     * @param mnemonico El mnem&uacute;nico a buscar entre las formas de liquidaci&oacute;n.
     * @return FormaPagoLiq.
     */
    public FormaPagoLiq getFormaPagoLiq(String ticket, String mnemonico) {
        for (Iterator it = obtenerFormasPagoLiq(ticket).iterator(); it.hasNext(); ) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            if (mnemonico.equals(fpl.getMnemonico())) {
                return fpl;
            }
        }
        return null;
    }

    /**
     * Establece el valor de siteService.
     *
     * @param siteService El valor a asignar.
     */
    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    /**
     * Utiliza los servicios del SITE para localizar y encontrar un banco internacional por su tipo
     * de clave y su clave de banco beneficiario o intermediario, y asigna a la plantilla el nombre
     * del banco y el identificador del pa&iacute;s.
     *
     * @see #encontrarBanco(String, String, String, String).
     * @param ticket El ticket que se pasa al servicio del SITE.
     * @param plantilla La plantilla a la que se asignar&aacute; el banco beneficiario o
     *          intermediario.
     * @param intermediario Si es para banco intermediario o beneficiario.
     * @return Banco.
     * @throws SicaException Si no se encuentra el banco, o la clave de banco no es v&aacute;lida.
     */
    public Banco encontrarBanco(String ticket, IPlantillaIntl plantilla, boolean intermediario)
            throws SicaException {
        String claveBanco = intermediario
                ? plantilla.getClaveBancoInterm() : plantilla.getClaveBanco();
        String tipoCuentaBanco = (intermediario
                ? plantilla.getTipoCuentaBancoInterm() : plantilla.getTipoCuentaBanco()).trim();
        Banco banco = encontrarBanco(ticket, tipoCuentaBanco, claveBanco,
                plantilla.getDivisa().getIdDivisa().trim());
        if (banco == null) {
            return null;
        }
        if (intermediario) {
            plantilla.setNombreBancoInterm(banco.getNombreCorto());
            plantilla.setIdPaisInterm(banco.getIdPais());
            plantilla.setClaveBancoIntermTecleado(claveBanco);
        }
        else {
            plantilla.setNombreBanco(banco.getNombreCorto());
            plantilla.setIdPais(banco.getIdPais());
            plantilla.setClaveBancoTecleado(claveBanco);
        }
        return banco;
    }

    /**
     * Utiliza los servicios del SITE para localizar y encontrar un banco internacional por su tipo
     * de clave y su clave de banco beneficiario o intermediario.
     *
     * @param ticket El ticket que se pasa al servicio del SITE.
     * @param tipoCuentaBanco El tipo de cuenta (ABA, SWIFT, CHIPS).
     * @param claveBanco El n&uacute;mero ABA, SWIFT o CHIPS.
     * @param idDivisa La clave de la divisa.
     * @return Banco.
     * @throws SicaException Si no se encuentra el banco, o la clave de banco no es v&aacute;lida.
     */
    public Banco encontrarBanco(String ticket, String tipoCuentaBanco, String claveBanco,
                                String idDivisa) throws SicaException {
    	if (PlantillaIntl.TIPO_CUENTA_BANCO_ABA.equals(tipoCuentaBanco)
                && claveBanco.length() != 9) {
            throw new SicaException("La Clave Banco ABA debe tener 9 caracteres.");
        }
        if (PlantillaIntl.TIPO_CUENTA_BANCO_SWIFT.equals(tipoCuentaBanco)
                && claveBanco.length() != 8 && claveBanco.length() != 11) {
            throw new SicaException("La Clave Banco SWIFT debe tener 8 u 11 caracteres.");
        }
        if (PlantillaIntl.TIPO_CUENTA_BANCO_ABA.equals(tipoCuentaBanco)
                || PlantillaIntl.TIPO_CUENTA_BANCO_CHIPS.equals(tipoCuentaBanco)) {
            if (! Divisa.DOLAR.equals(idDivisa)) {
                throw new SicaException("Si el \"Tipo Cuenta Banco\" es \"ABA\" o "
                        + "\"CHIP\" la Divisa debe ser D\u00f3lar Americano.");
            }
        }
        Banco banco = null;
        if (PlantillaIntl.TIPO_CUENTA_BANCO_ABA.equals(tipoCuentaBanco)) {
            banco = siteService.findBancoInternacionalByABA(ticket, claveBanco.toUpperCase());
        }
        else if (PlantillaIntl.TIPO_CUENTA_BANCO_SWIFT.equals(tipoCuentaBanco)) {
            banco = siteService.findBancoInternacionalBySwift(ticket, claveBanco.toUpperCase());
        }
        else if (PlantillaIntl.TIPO_CUENTA_BANCO_CHIPS.equals(tipoCuentaBanco)) {
            banco = siteService.findBancoInternacionalByChips(ticket, claveBanco.toUpperCase());
        }
        else if (PlantillaIntl.TIPO_CUENTA_BANCO_ALRA.equals(tipoCuentaBanco)) {
            banco = siteService.findBancoInternacionalById(ticket, new Long(claveBanco));
        }
        return banco;
    }

    /**
     * Vuelve null la variable de instancia <code>formasPagoLiq</code> y la variable
     * <code>paises</code>, de modo que la siguiente petici&oacute;n refresque la lista.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     */
    public void refrescar(String ticket) {
        if (logger.isDebugEnabled()) {
            logger.debug("Actualizando mnem\u00f3nicos...");
        }
        formasPagoLiq = siteService.findFormaPagoLiq(ticket, "SICA", "PMY");
        paises = new ArrayList();
        if (logger.isDebugEnabled()) {
            logger.debug("Los mnem\u00f3nicos han sido actualizados.");
        }
    }

    /**
     * Regresa el pais que tiene la clave ISO especificada y lo mantiene en cach&eacute; para
     * futuras referencias.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param claveISO La clave ISO a buscar.
     * @return Pais.
     * @see com.ixe.ods.sica.SiteService#getPaisXClaveISO(String, String).
     */
    public Pais getPaisXClaveISO(String ticket, String claveISO) {
        for (Iterator it = paises.iterator(); it.hasNext();) {
            Pais pais = (Pais) it.next();
            if (claveISO.equals(pais.getClaveISO())) {
                return pais;
            }
        }
        Pais pais = siteService.getPaisXClaveISO(ticket, claveISO);
        paises.add(pais);
        return pais;        
    }
    
    /**
     * La interfaz de comunicaci&oacute;n con el Sistema de Tesorer&iacute;a (SITE).
     */
    private SiteService siteService;

    /**
     * La lista de formas de liquidaci&oacute;n aplicables al SICA.
     */
    private List formasPagoLiq;

    /**
     * La lista de paises.
     */
    private List paises = new ArrayList();

    /**
     * El objeto para logging.
     */
    private transient Log logger = LogFactory.getLog(getClass());    

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 239618325683742310L;
}
