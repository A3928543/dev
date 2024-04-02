/*
 * $Id: ReporteSiar.java,v 1.6 2008/12/26 23:17:28 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.reportes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.services.impl.XlsServiceImpl;
import com.ixe.ods.sica.services.model.ModeloXls;
import com.ixe.ods.sica.pizarron.Consts;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.HistoricoPosicion;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.Posicion;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sdo.PosicionServiceData;
import com.ixe.ods.sica.utils.DateUtils;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Clase para la Consulta del reporte SIAR para Riesgos.
 *
 * @author Gustavo Gonzalez
 * @version $Revision: 1.6 $ $Date: 2008/12/26 23:17:28 $
 */
public abstract class ReporteSiar extends SicaPage {

	/**
	 * Asigna los valores necesarios para la pagina.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
		setNombreArchivo("ReporteSIAR");
		_listaPosicionesMesas = new ArrayList();
		setDataSource(new ArrayList());
	}

	/**
	 * Realiza la busqueda de registros.
	 * 
	 * @param cycle El ciclo de la pagina.
	 */
	public void buscar(IRequestCycle cycle) {
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) 
		getBeans().getBean("delegate");
		Date fechaActual = DateUtils.inicioDia();
		Date fechaDesde = DateUtils.inicioDia(getFecha());
		_historico = fechaDesde.getTime() < fechaActual.getTime();
		if (fechaDesde.getTime() > fechaActual.getTime()) {
			setDataSource(new ArrayList());
			delegate.record("No se encontraron datos de Posicion con los criterios " +
					"de b\u00fasqueda especificados." , null);
			return;
        }
        List mesas = getSicaServiceData().findAllMesas();
        List posiciones = new ArrayList();
        for (Iterator it = mesas.iterator(); it.hasNext();) {
            MesaCambio mesa = (MesaCambio) it.next();
            List posicionParaMesa = getPosicionServiceData().findPosicionesParaSiar(
                    !"0".equals(getDivisa().getIdDivisa()) ? getDivisa().getIdDivisa() : null,
                    getFecha(), new Integer(mesa.getIdMesaCambio()), _historico);
            if (!posicionParaMesa.isEmpty()) {
                posiciones.add(definirPosicionActualOHistorico(posicionParaMesa));
            }
        }
        if (posiciones.isEmpty()) {
            setDataSource(new ArrayList());
            delegate.record("No se encontraron datos de Posicion con los criterios de " +
                    "b\u00fasqueda especificados.", null);
            return;
        }
        _listaPosicionesMesas = posiciones;
        setDataSource(_listaPosicionesMesas);
    }

    /**
	 * Define los registros correspondientes a la ultima modificacion de la posicion
	 * para historicos.
	 * 
	 * @param posiciones La lista de registros obtenidos de la consulta a la base de datos.
	 * @return List
	 */
	public List definirPosicionActualOHistorico(List posiciones) {
		List posicion = new ArrayList();
		List posicionDivisa = new ArrayList();
		if (_historico) {
			for (int i = posiciones.size() - 1; i > -1; i--) {
				HistoricoPosicion hp = (HistoricoPosicion) posiciones.get(i);
				if (!posicionDivisa.contains(hp.getDivisa().getIdDivisa())) {
					posicion.add(hp);
					posicionDivisa.add(hp.getDivisa().getIdDivisa());
				}
			}
		}
		else {
			posicion = posiciones;
		}
		return posicion;
	}

    /**
     * Listener que genera el archivo en excel de los registros obtenidos despues de la consulta.
     *
     * @param cycle El ciclo de la pagina.
     */
    public void generarListadoExcel(IRequestCycle cycle) {
        asignarTitulosExpresiones(TIT_REP_SIAR, EXP_REP_SIAR);
        XlsServiceImpl.generarEscribir(cycle.getRequestContext().getResponse(), getNombreArchivo(),
                generarListaSiar());
    }

    /**
	 * Itera los registros obtenidos por la consulta del DAO y los almacena
	 * en una coleccion de HashMaps para obtener el data Source del reporte.
	 * 
	 * @return List
	 */
	public List generarListaSiar() {
		List modelosXls = new ArrayList();
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
		int folio = 6;
		for (Iterator it = _listaPosicionesMesas.iterator(); it.hasNext(); ) {
			List posiciones = new ArrayList();
			List posicionMesa = (ArrayList) it.next();
			String nombreMesa = "";
			for (Iterator it2 = posicionMesa.iterator(); it2.hasNext(); ) {
				Map reg = new HashMap();
                if (_historico) {
                    HistoricoPosicion pos = (HistoricoPosicion) it2.next();
                    if (StringUtils.isEmpty(nombreMesa)) {
                        nombreMesa = pos.getMesaCambio().getNombre();
                    }
                    String[] clavesDivisa = obtenerClavesDivisa(pos.getDivisa(),
                            pos.getMesaCambio().getIdMesaCambio());
                    if (!pos.getDivisa().isFrecuente()) {
                        folio++;
                        reg.put("folio", new Integer(folio));
                    }
                    else {
                        reg.put("folio", new Integer(clavesDivisa[3]));
                    }
                    double importePosicionFinal = Math.abs(pos.getPosicionFinal());
                    reg.put("inicio", importePosicionFinal > 0 ? "AGREGA" : "*");
                    reg.put("portafolio", clavesDivisa[0]);
                    reg.put("contraparte", "X");
                    reg.put("signo", pos.getPosicionFinal() > 0 ? "Compra" : "Venta");
                    reg.put("actFinanc", clavesDivisa[1]);
                    reg.put("actBasico", clavesDivisa[2]);
                    reg.put("fPosOp", DATE_FORMAT.format(getFecha()));
                    Date fechaCash = getPizarronServiceData().getFechaOperacion(getFecha());
                    Date fechaSpot = getPizarronServiceData().getFechaSPOTSiar(fechaCash);
                    reg.put("fInicio", DATE_FORMAT.format(fechaCash));
                    reg.put("fVence", DATE_FORMAT.format(fechaSpot));
                    reg.put("titulosImpte", new Double(importePosicionFinal));
                    reg.put("tf1", "");
                    reg.put("tf2", "");
                    String tc = "";
                    if (pos.getTipoCambioPromedioPonderadoCliente() > 0 || 
                    		pos.getTipoCambioPromedioPonderadoCliente() < 0) {
                    	tc = getCurrencyFormat().
                    		format(pos.getTipoCambioPromedioPonderadoCliente());
                    }
                    else {
                    	tc = "0";
                    }
                    reg.put("tCambio", tc);
                    Calendar cash = new GregorianCalendar();
                    cash.setTime(fechaCash);
                    Calendar spot = new GregorianCalendar();
                    spot.setTime(fechaSpot);
                    reg.put("plazo", new Integer(diasDiferenciaCashSpot(cash, spot)));
                    reg.put("coCup", "");
                    reg.put("fEmis", "");
                    reg.put("factorCap", "");
                    reg.put("facAmor", "");
					reg.put("cupAmor", "");
					reg.put("numAmor", "");
					reg.put("tipoCredito", "");
				}
				else {
					Posicion pos = (Posicion) it2.next();
					if (StringUtils.isEmpty(nombreMesa)) {
                        nombreMesa = pos.getMesaCambio().getNombre();
                    }
                    String[] clavesDivisa = obtenerClavesDivisa(pos.getDivisa(),
                            pos.getMesaCambio().getIdMesaCambio());
                    if (!pos.getDivisa().isFrecuente()) {
                        folio++;
                        reg.put("folio", new Integer(folio));
                    }
                    else {
                        reg.put("folio", new Integer(clavesDivisa[3]));
                    }
                    double importePosicionFinal = Math.abs(pos.getPosicionFinal());
                    reg.put("inicio", importePosicionFinal > 0 ? "AGREGA" : "*");
                    reg.put("portafolio", clavesDivisa[0]);
                    reg.put("contraparte", "X");
                    reg.put("signo", pos.getPosicionFinal() > 0 ? "Compra" : "Venta");
                    reg.put("actFinanc", clavesDivisa[1]);
                    reg.put("actBasico", clavesDivisa[2]);
                    Date fechaCash = getPizarronServiceData().getFechaOperacion(getFecha());
                    Date fechaSpot = getPizarronServiceData().getFechaSPOTSiar(fechaCash);
                    reg.put("fPosOp", DATE_FORMAT.format(fechaCash));
                    reg.put("fInicio", DATE_FORMAT.format(fechaCash));
                    reg.put("fVence", DATE_FORMAT.format(fechaSpot));
                    reg.put("titulosImpte", new Double(importePosicionFinal));
                    reg.put("tf1", "");
                    reg.put("tf2", "");
                    String tc = "";
                    if (pos.getTipoCambioPromedioPonderadoCliente() > 0 || 
                    		pos.getTipoCambioPromedioPonderadoCliente() < 0) {
                    	tc = getCurrencyFormat().
                    		format(pos.getTipoCambioPromedioPonderadoCliente());
                    }
                    else {
                    	tc = "0";
                    }
                    reg.put("tCambio", tc);
                    Calendar cash = new GregorianCalendar();
                    cash.setTime(fechaCash);
                    Calendar spot = new GregorianCalendar();
                    spot.setTime(fechaSpot);
                    reg.put("plazo", new Integer(diasDiferenciaCashSpot(cash, spot)));
                    reg.put("coCup", "");
					reg.put("fEmis", "");
					reg.put("factorCap", "");
					reg.put("facAmor", "");
					reg.put("cupAmor", "");
					reg.put("numAmor", "");
					reg.put("tipoCredito", "");
				}
                posiciones.add(reg);
            }
            modelosXls.add(new ModeloXls("Posiciones y Operaciones Mesa: " + nombreMesa,
                    getTitulos(), getExpresiones(), posiciones));
        }
        return modelosXls;
    }

    /**
	 * Asigna los valores para las claves de divisa segun el documento de especificacion.
	 * 
	 * @param divisa La divisa de la posicion.
     * @param idMesaCambio La clave de la mesa de cambios.
     * @return double
     */
    public String[] obtenerClavesDivisa(Divisa divisa, int idMesaCambio) {
        String[] clavesDivisa = new String[Consts.NUM_4];
        String portafolio = idMesaCambio == 1 ? Constantes.CANAL_MESA_OPERACION :
                idMesaCambio == Consts.NUM_22 ? Constantes.CANAL_MESA_TRADER_USD :
                        idMesaCambio == 25 ? Constantes.CANAL_MESA_TRADER_MXN :
                                Constantes.CANAL_MESA_ESTRA;
        if (Divisa.DOLAR.equals(divisa.getIdDivisa())) {
            clavesDivisa[0] = "CAM_USD_MD_BANCO";
            clavesDivisa[1] = "CAM_USD_MD_" + portafolio;
            clavesDivisa[2] = "USD";
            clavesDivisa[Consts.NUM_3] = "1";
        }
        else if (Divisa.EURO.equals(divisa.getIdDivisa())) {
            clavesDivisa[0] = "CAM_EUR_MXP_BANCO";
            clavesDivisa[1] = "CAM_EUR_MXP_" + portafolio;
            clavesDivisa[2] = "EUR";
            clavesDivisa[Consts.NUM_3] = "2";
        }
        else if (Divisa.DOLAR_CANADIENSE.equals(divisa.getIdDivisa())) {
            clavesDivisa[0] = "CAM_CAD_BANCO";
            clavesDivisa[1] = "CAM_CAD_" + portafolio;
            clavesDivisa[2] = "CAD";
            clavesDivisa[Consts.NUM_3] = "3";
        }
        else if (Divisa.LIBRA_ESTERLINA.equals(divisa.getIdDivisa())) {
            clavesDivisa[0] = "CAM_GBP_BANCO";
            clavesDivisa[1] = "CAM_GBP_" + portafolio;
            clavesDivisa[2] = "GBP";
            clavesDivisa[Consts.NUM_3] = "4";
        }
        else if (Divisa.FRANCO_SUIZO.equals(divisa.getIdDivisa())) {
            clavesDivisa[0] = "CAM_CHF_BANCO";
            clavesDivisa[1] = "CAM_CHF_" + portafolio;
            clavesDivisa[2] = "CHF";
            clavesDivisa[Consts.NUM_3] = "5";
        }
        else if (Divisa.YEN.equals(divisa.getIdDivisa())) {
            clavesDivisa[0] = "CAM_JPY_BANCO";
            clavesDivisa[1] = "CAM_JPY_" + portafolio;
            clavesDivisa[2] = "JPY";
            clavesDivisa[Consts.NUM_3] = "6";
        }
        else {
            clavesDivisa[0] = "CAM_" + divisa.getIdDivisa() + "_BANCO";
            clavesDivisa[1] = "CAM_" + divisa.getIdDivisa() + "_" + portafolio;
            clavesDivisa[2] = divisa.getIdDivisa();
            clavesDivisa[Consts.NUM_3] = "0";
        }
        return clavesDivisa;
    }

    /**
     * Regresa un RecordSelectionModel con todas las divisas.
     *
     * @return IPropertySelectionModel
	 */
	public IPropertySelectionModel getDivisasModel() {
		List divisas = getSicaServiceData().findAllDivisasByOrdenAlfabetico();
		Divisa primerElemento = new Divisa();
		primerElemento.setIdDivisa("0");
		primerElemento.setDescripcion("TODAS");
		divisas.add(0, primerElemento);
		return new RecordSelectionModel(divisas, "idDivisa", "descripcion");
	}

	/**
	 * Asigna los titulos de las columas del reporte y las expresiones ognl
	 * para obtener el dato almacenado en la coleccion.
	 * 
	 * @param titulos Los titulos de las columnas del reportes.
	 * @param expresiones La lista de expresiones ognl para los datos.
	 */
	public void asignarTitulosExpresiones(String[] titulos, String[] expresiones) {
		List tits = new ArrayList();
		List exprs = new ArrayList();
		for (int i = 0; i < titulos.length; i++) {
			tits.add((String) titulos[i]);
			exprs.add((String) expresiones[i]);
		}
		setTitulos(tits);
		setExpresiones(exprs);
	}
	
	/**
	 * Obtiene el numero de dias naturales entre la fecha Cash y Spot. 
	 * 
	 * @param fechaCash La fecha para Cash.
	 * @param fechaSpot La fecha para Spot
	 * @return int
	 */
	public int diasDiferenciaCashSpot(Calendar fechaCash, Calendar fechaSpot) {
		Calendar fechaCashTmp = new GregorianCalendar();
		fechaCashTmp.setTime(fechaCash.getTime());
		int diasDiferencia = 0;
		while (fechaCashTmp.getTime().getTime() < fechaSpot.getTime().getTime()) {
			fechaCashTmp.add(Calendar.DAY_OF_MONTH, 1);
			diasDiferencia++;
		}
		return diasDiferencia;
	}

    /**
     * Regresa el bean de SicaServiceData que se encuentra declarado en el applicationContext.xml.
     *
     * @return SicaServiceData.
     */
    public PosicionServiceData getPosicionServiceData() {
        return (PosicionServiceData) getApplicationContext().getBean("posicionServiceData");
    }

    /**
     * Regresa por default el estado de operacion normal, restringida y vespertina. Las subclases
     * deben sobre escribir para indicar estados adicionales en los que &eacute;stas son
     * v&aacute;lidas.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[]{Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA,
                Estado.ESTADO_OPERACION_VESPERTINA, Estado.ESTADO_FIN_LIQUIDACIONES,
                Estado.ESTADO_OPERACION_NOCTURNA};
    }
	
	/**
	 * Lista con las mesas y sus posiciones.
	 */
	private List _listaPosicionesMesas = new ArrayList();

	/**
	 * Define si la consulta es sobre el historico de posicion 
	 * o sobre la posicion actual. 
	 */
	private boolean _historico;
	
	/**
	 * Obtiene el id de la divisa seleccionada.
	 * 
	 * @return Divisa
	 */
	public abstract Divisa getDivisa();

	/**
	 * Asinga el valor para idDivisa
	 * 
	 * @param divisa La divisa seleccionada
	 */
	public abstract void setDivisa(Divisa divisa);

	/**
	 * Obtiene el valor para la fecha de inicio de la consulta.
	 * 
	 * @return Date
	 */
	public abstract Date getFecha();

	/**
	 * Asigna el valor para fechaDesde
	 * 
	 * @param fecha La fecha de inicio de la consulta.
	 */
	public abstract void setFecha(Date fecha);

	/**
	 * Obtiene el valor para el dataSource
	 * 
	 * @return List
	 */
	public abstract List getDataSource();

	/**
	 * Asinga el valor para el DataSource.
	 * 
	 * @param dataSource El valor para el DataSource.
	 */
	public abstract void setDataSource(List dataSource);

	/**
	 * Obtiene el nombre del archivo.
	 * 
	 * @return String
	 */
	public abstract String getNombreArchivo();

	/**
	 * Asigna el valor para el nombre del archivo.
	 * 
	 * @param nombreArchivo El valor para el nombre del archivo
	 */
	public abstract void setNombreArchivo(String nombreArchivo);

	/**
	 * Obtiene la lista de titulos.
	 * 
	 * @return List
	 */
	public abstract List getTitulos();

	/**
	 * Asigna la lista para los titulos del reporte.
	 * 
	 * @param titulos La lista con los titulos del reporte
	 */
	public abstract void setTitulos(List titulos);

	/**
	 * Obtiene la lista de expresiones.
	 * 
	 * @return List
	 */
	public abstract List getExpresiones();

	/**
	 * Asigna la lista para las expresiones ognl del reporte.
	 *
     * @param expresiones La lista con las expresiones ognl del reporte.
     */
    public abstract void setExpresiones(List expresiones);

    /**
     * Arreglo estatico con los titulos para el repote de Utilidades.
     */
    private static final String[] TIT_REP_SIAR = new String[]{"*", "Portafolio", "Contraparte",
            "Signo", "Act. Financ", "Act. Basico", "F. Pos/Op.", "Folio", "F. Inicio", "F. Vence",
            "Titulos/Impte", "TF1", "TF2", "T. Cambio", "Plazo", "No. Cup.", "F. Emis",
            "Factor Cap.", "FacAmor", "Cup1erAmor", "NumAmor", "Tipo Credito"};

    /**
     * Arreglo estatico de expresiones ognl para el reporte de Utilidadew.
     */
    private static final String[] EXP_REP_SIAR = new String[]{"inicio", "portafolio",
            "contraparte", "signo", "actFinanc", "actBasico", "fPosOp", "folio", "fInicio",
            "fVence", "titulosImpte", "tf1", "tf2", "tCambio", "plazo", "coCup", "fEmis",
            "factorCap", "facAmor", "cupAmor", "numAmor", "tipoCredito"};

}
