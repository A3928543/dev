/*
 * $Id: EdicionPlantillaCuentaIxe.java,v 1.16 2008/11/12 05:53:13 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals.plantillas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.IPlantillaCuentaIxe;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaPantalla;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.mesa.MigracionPlantillas;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Clase para el Alta de Plantillas.
 *
 * @author Gerardo Corzo
 * @version  $Revision: 1.16 $ $Date: 2008/11/12 05:53:13 $
 */
public abstract class EdicionPlantillaCuentaIxe extends SicaPage {

	/**
	 * Asigna los valores necesarios al activarse la p&aacute;gina.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        SicaServiceData sd = getSicaServiceData();
        List plantillasPantalla = sd.findMnemonicosByPlantillaPantalla(PlantillaPantalla.CAPTURA_CUENTA_IXE);
        List fpls = new ArrayList();
        for (Iterator it = plantillasPantalla.iterator(); it.hasNext();) {
            PlantillaPantalla pp = (PlantillaPantalla) it.next();
            FormaPagoLiq fpl = getFormaPagoLiq(pp.getMnemonico());
            if (fpl != null && fpl.getDesplegableSica().booleanValue()
                    && Constantes.MNEMONICO_ACTIVO.equals(fpl.getStatus())) {
                fpls.add(fpl);
            }
            else if (_logger.isWarnEnabled()) {
                _logger.warn("No se encontr\u00f3 la formaPagoLiq con el mnem\u00f3nico " + pp.getMnemonico() +
                        ". Probablemente se encuentre inactivo.");
            }
        }
        setFormasPagoLiq(fpls);
        setMapaEdicionPlantilla(new HashMap());
    }

    /**
     * Regresa las formas de pago como una matriz de dos columnas.
     *
     * @return FormaPagoLiq[][].
     */
    public FormaPagoLiq[][] getMatrizFpls() {
        int renglones = (getFormasPagoLiq().size() / 2) + (getFormasPagoLiq().size() % 2);
        FormaPagoLiq[][] matriz = new FormaPagoLiq[renglones][2];
        int y = 0;
        int x = 0;
        for (Iterator it = getFormasPagoLiq().iterator(); it.hasNext();) {
            matriz[y][x] = (FormaPagoLiq) it.next();
            x++;
            if (x > 1) {
                x = 0;
                y++;
            }
        }
        return matriz;
    }

    /**
     * Activa el submit de la forma.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
        try {
            IPlantillaCuentaIxe plantilla = getPlantilla();
            SicaServiceData sd = getSicaServiceData();
            if ("5".equals(getModoSubmit())) {
            	if (plantilla.getBeneficiario().getIdPersona().intValue() < 0) {
        			delegate.record("Debe seleccionar un Beneficiario para la b\u00fasqueda.", null);
        			plantilla.setBeneficiario(null);
        			return;
        		}
            	String[] cuentas = getSicaServiceData().findCuentasEje(plantilla.getBeneficiario().getIdPersona());
        		if ("".equals(cuentas[0])) {
        			throw new SicaException("No se encontraron cuentas eje para el beneficiario seleccionado.");
        		}
            	return;
            }
            if (! MODO_SUBMIT_MNEMONICO.equals(getModoSubmit())) {
            	if (delegate.getHasErrors()) {
                    return;
                }
            	if (plantilla.getBeneficiario() == null) {
            		delegate.record("Debe seleccionar un Mnem\u00F3nico para dar de alta la Plantilla.", null);
        			return;
            	}
            	if (plantilla.getBeneficiario().getIdPersona().intValue() < 0) {
        			delegate.record("Debe seleccionar un beneficiario para la plantilla.", null);
        			plantilla.setBeneficiario(null);
    				return;
        		}
                if (StringUtils.isNotEmpty(plantilla.getClavePlantilla())) {
                    plantilla.setClavePlantilla(plantilla.getClavePlantilla().toUpperCase());
                }
                if (StringUtils.isEmpty(getPlantilla().getNoCuentaIxe())) {
                    throw new SicaException("No se puede dar de Alta la Plantilla porque el Cliente no tiene Cuentas Eje.");
                }
                if (plantilla.getNoCuentaIxe().length() > 12) {
        			delegate.record("Debe seleccionar una Cuenta para la Plantilla.", null);
    				return;
        		}
                if (!(getPlantilla().getNoCuentaIxe().length() == 9 || getPlantilla().getNoCuentaIxe().length() == 12)) {
                    throw new SicaException("El n\u00famero de cuenta debe tener 9 \u00F3 12 d\u00EDgitos num\u00E9ricos, " +
                            " seguidos de un gui\u00F3n y al finalizar un d\u00EDgito num\u00E9rico.");
                }
                plantilla.setNoCuentaIxe(plantilla.getNoCuentaIxe().toUpperCase());
                String[] ctasEje = getSicaServiceData().findCuentasEje(plantilla.getBeneficiario().getIdPersona());
                boolean valida = false;
                for (int i = 0; i < ctasEje.length && !valida; i++) {
                    String cta = ctasEje[i];
                    if (cta.trim().equals(plantilla.getNoCuentaIxe().trim())) {
                        String monedaCuenta = getSicaServiceData().findDivisaCuenta(cta.trim());
                        if (!plantilla.getMnemonico().trim().substring(1, 4).equals(monedaCuenta.trim())) {
                            throw new SicaException("El Mnem\u00F3nico no corresponde a la Divisa de la Cuenta Ixe.");
                        }
                        valida = true;
                    }
                }
                if (!valida) {
                    throw new SicaException("La cuenta tecleada no corresponde al beneficiario seleccionado.");
                }
                // Las plantillas cuenta Ixe deben estar activas desde el principio:
                plantilla.setStatusPlantilla(Plantilla.STATUS_PLANTILLA_ACTIVA);
                sd.store(plantilla);
                if (PAGINA_MIGRACION_PLANTILLAS.equals(getPaginaAnterior())) {
                    MigracionPlantillas prevPage = (MigracionPlantillas) cycle.getPage(PAGINA_MIGRACION_PLANTILLAS);
                    prevPage.setMapaEdicionMnemonico(new HashMap());
                    prevPage.setPaginaAnterior(getPageName());
                    prevPage.setNuevoBeneficiario(new Persona());
                    prevPage.buscarCuentasPlantillas(cycle);
                    prevPage.activarDelegate(true);
                    cycle.activate(prevPage);
                }
                else {
                    cycle.activate(PAGINA_CONSULTA_PLANTILLAS_IXE);
                }
            }
            else {
            	if (plantilla.getMnemonico() != null)  {
                    if (plantilla.getMnemonico().charAt(0) == 'R') {
                        plantilla.setBeneficiario(getSicaServiceData().findPersonaByContratoSica(plantilla.getContratoSica().getNoCuenta()));
                    }
                    else {
                    	plantilla.setBeneficiario(null);
                    }
            	}
            	else {
            		delegate.record("Debe seleccionar un Mnem\u00F3nico para dar de alta la Plantilla.", null);
        			plantilla.setBeneficiario(null);
            	}
            }
        }
        catch (SicaException e) {
            debug(e.getMessage(), e);
            delegate.record(e.getMessage(), null);
        }
    }

    /**
     * Obtiene el Modelo de las Cuentas Eje.
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getCuentasEjeModel() {
    	String primerElemento = "Seleccione Una Cuenta";
    	String [] cuentas = getSicaServiceData().findCuentasEje(getPlantilla().getBeneficiario().getIdPersona());
    	String [] arregloCompleto = new String[cuentas.length+1];
    	arregloCompleto[0] = primerElemento;
        for (int i = 0; i < cuentas.length; i++) {
            arregloCompleto[i + 1] = cuentas[i];
        }
    	return new StringPropertySelectionModel(arregloCompleto);
    }

    /**
     * Cancela la edici&oacute;n de la plantilla. Envia
     * a MigracionPlantillas.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cancelarEdicion(IRequestCycle cycle){
    	MigracionPlantillas nextPage = (MigracionPlantillas)cycle.getPage(PAGINA_MIGRACION_PLANTILLAS);
    	nextPage.setMapaEdicionMnemonico(new HashMap());
    	nextPage.setNuevoBeneficiario(new Persona());
    	nextPage.setPaginaAnterior(getPageName());
    	nextPage.buscarCuentasPlantillas(cycle);
    	cycle.activate(PAGINA_MIGRACION_PLANTILLAS);
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
     * Regresa el valor de formasPagoLiq.
     *
     * @return List.
     */
    public abstract List getFormasPagoLiq();


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
    public abstract IPlantillaCuentaIxe getPlantilla();

    /**
     * Fija el valor de plantilla.
     *
     * @param plantilla El valor a asignar.
     */
    public abstract void setPlantilla(IPlantillaCuentaIxe plantilla);

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
     * Constante mnemonico.
     */
    public static final String MODO_SUBMIT_MNEMONICO = "mnemonico";

    /**
     * Constante Pagina MigracionPlantillas
     */
    public static final String PAGINA_MIGRACION_PLANTILLAS = "MigracionPlantillas";

    /**
     * Constante Pagina ConsultaPlantillasCuentaIxe
     */
    public static final String PAGINA_CONSULTA_PLANTILLAS_IXE = "ConsultaPlantillasCuentaIxe";
}
