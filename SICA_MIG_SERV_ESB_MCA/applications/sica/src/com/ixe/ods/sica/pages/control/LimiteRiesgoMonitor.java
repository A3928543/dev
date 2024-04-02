/*
 * $Id: LimiteRiesgoMonitor.java,v 1.18 2008/12/26 23:17:31 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.dao.LimiteDao;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.dto.PosicionMesaDto;
import com.ixe.ods.sica.services.RiesgosService;
import com.ixe.ods.sica.services.model.ModeloXls;
import com.ixe.ods.sica.services.impl.XlsServiceImpl;

/**
 * P&aacute;gina que permite al usuario monitorear los l&iacute;mites de riesgo.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.18 $ $Date: 2008/12/26 23:17:31 $
 */
public abstract class LimiteRiesgoMonitor extends SicaPage {

	/**
	 * M&eacut;etodo que carga todas los par&acute;metros de limite de riesgo
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
		setValorFuturo(getPizarronServiceData().isValorFuturoHabilitado());
        refrescarMonitor(cycle);
	}

    /**
	 * M&eacut;etodo que muestra el monitor de la posici&oacute;n
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void refrescarMonitor(IRequestCycle cycle) {
        Estado e = getSicaServiceData().findEstadoSistemaActual();
        setEstadoSica(e.getNombreEstado());
        RiesgosService riesgosService = (RiesgosService) getApplicationContext().
                getBean("riesgosService");
        Map resultados = riesgosService.obtenerMonitorRiesgos();
        setMidSpot(((Double) resultados.get("midSpot")).doubleValue());
        setPorcentajeAvisoGlobal(((Integer) resultados.get("porcentajeAvisoGlobal")).intValue());
        setPorcentajeAlarmaGlobal(((Integer) resultados.get("porcentajeAlarmaGlobal")).intValue());
        setCapitalDelGrupo(((Double) resultados.get("capitalDelGrupo")).doubleValue());
        setPorcentajeCapital(((Double) resultados.get("porcentajeCapital")).doubleValue());
        setLimiteRegulatorio((getCapitalDelGrupo()) * (getPorcentajeCapital() / CIEN));
        setPosicionesMesas((List) resultados.get("posicionesMesas"));
        setNivel(((Double) resultados.get("nivel")).doubleValue());
	}

    /**
     * Recorre el arreglo de objetos PosicionMesaDto y para cada uno de ellos, asigna los
     * l&iacute;mites de riesgo, utilizando el servicio LimiteDao.getLimitesParaMesaDivisa().
     *
     * @param posicionesMesas La lista de objetos PosicionMesaDto.
     * @see LimiteDao#getLimitesParaMesaDivisa(Integer, String, Integer).
     */
    private void calcularLimites(List posicionesMesas) {
        LimiteDao limiteDao = (LimiteDao) getApplicationContext().getBean("limiteDao");
        for (Iterator it = posicionesMesas.iterator(); it.hasNext();) {
            PosicionMesaDto pmDto = (PosicionMesaDto) it.next();
            Integer idMesaCambio = pmDto.getMesaCambio().getIdMesaCambio() == 0 ? null :
                    new Integer(pmDto.getMesaCambio().getIdMesaCambio());
            String idDivisa = "*todas*".equals(pmDto.getDivisaSeleccionada().getIdDivisa()) ? null :
                    pmDto.getDivisaSeleccionada().getIdDivisa();
            pmDto.setLimites(limiteDao.getLimitesParaMesaDivisa(idMesaCambio, idDivisa, null));
        }
        setPosicionesMesas(posicionesMesas);
    }

    /**
     * Recalcula los l&iacute;mites para la informaci&oacute;n desplegada.
     *
     * @param cycle El IRequestCycle.
     * @see #calcularLimites(java.util.List).
     */
    public void cambiarDivisaLimites(IRequestCycle cycle) {
        calcularLimites(getPosicionesMesas());
    }

    /**
	 * Imprime el Reporte de Limite de Fiego en formato de Excel.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void imprimirReporteLimiteRiesgoXls(IRequestCycle cycle) {
        List modelosXls = new ArrayList();
        List encabezados = new ArrayList();
        HashMap encabezado = new HashMap();
        encabezado.put(LIMITE_REGULATORIO_EXPR, new BigDecimal("" + getLimiteRegulatorio()));
        encabezado.put(MID_SPOT_EXPR, new BigDecimal("" + getMidSpot()));
        encabezado.put(NIVEL_EXPR, new BigDecimal("" + getNivel()));
        encabezado.put(CONSUMO_EXPR,
                new BigDecimal("" + (getNivel() * CIEN / getLimiteRegulatorio())));
        encabezados.add(encabezado);
        modelosXls.add(new ModeloXls("Datos Generales:", new String[]{"L\u00edmite Regulatorio",
                "MidSpot", NIVEL_STR, CONSUMO_STR}, new String[] { LIMITE_REGULATORIO_EXPR,
                MID_SPOT_EXPR, NIVEL_EXPR, CONSUMO_EXPR}, encabezados));
        for (Iterator it = getPosicionesMesas().iterator(); it.hasNext();) {
            PosicionMesaDto posicionMesaDto = (PosicionMesaDto) it.next();
            // Posiciones:
            modelosXls.add(new ModeloXls(posicionMesaDto.getMesaCambio().getNombre(),
                    SUBTITULOS_POSICION, EXPRESIONES_POSICION, posicionMesaDto.getPosiciones()));
            // Limites:
            modelosXls.add(new ModeloXls("L\u00edmites para la Divisa: " +
                    posicionMesaDto.getDivisaSeleccionada().getDescripcion(), SUBTITULOS_LIMITES,
                    EXPRESIONES_LIMITES, posicionMesaDto.getLimites()));
        }
        XlsServiceImpl.generarEscribir(cycle.getRequestContext().getResponse(),
                "MonitorRiesgo" + DATE_FORMAT.format(new Date()) + ".xls", modelosXls);
    }

	/**
     * Regresa el arreglo con los estados de operaci&oacute;n normal y operaci&oacute;n restringida.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[] {Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA,
        		Estado.ESTADO_OPERACION_VESPERTINA, Estado.ESTADO_OPERACION_NOCTURNA,
        		Estado.ESTADO_GENERACION_CONTABLE, Estado.ESTADO_FIN_LIQUIDACIONES};
    }

    /**
	 * Fija el valor de estadoSica.
	 *
	 * @param estadoSica El valor a asignar.
	 */
	public abstract void setEstadoSica(String estadoSica);

	/**
	 * Regresa el valor de capitalDelGrupo.
	 *
	 * @return double.
	 */
	public abstract double getCapitalDelGrupo();

	/**
	 * Fija el valor de capitalDelGrupo.
	 *
	 * @param capitalDelGrupo El valor a asignar.
	 */
	public abstract void setCapitalDelGrupo(double capitalDelGrupo);

	/**
	 * Regresa el valor de porcentajeCapital.
	 *
	 * @return double.
	 */
	public abstract double getPorcentajeCapital();

	/**
	 * Fija el valor de porcentajeCapital.
	 *
	 * @param porcentajeCapital El valor a asignar.
	 */
	public abstract void setPorcentajeCapital(double porcentajeCapital);

	/**
	 * Fija el valor de porcentajeAvisoGlobal.
	 *
	 * @param porcentajeAvisoGlobal El valor a asignar.
	 */
	public abstract void setPorcentajeAvisoGlobal(double porcentajeAvisoGlobal);

	/**
	 * Fija el valor de porcentajeAlarmaGlobal.
	 *
	 * @param porcentajeAlarmaGlobal El valor a asignar.
	 */
	public abstract void setPorcentajeAlarmaGlobal(double porcentajeAlarmaGlobal);

	/**
	 * Fija el valor de limiteRegulatorio.
	 *
	 * @param limiteRegulatorio El valor a asignar.
	 */
	public abstract void setLimiteRegulatorio(double limiteRegulatorio);

	/**
	 * Obtiene el valor de limiteRegulatorio
	 *
	 * @return double
	 */
	public abstract double getLimiteRegulatorio();

    /**
     * Regresa el valor de midSpot.
     *
     * @return double.
     */
    public abstract double getMidSpot();

    /**
	 * Fija el valor de midSpot.
	 *
	 * @param midSpot El valor a asignar.
	 */
	public abstract void setMidSpot(double midSpot);

    /**
     * Regresa el valor de nivel.
     *
     * @return double.
     */
    public abstract double getNivel();

    /**
	 * Fija el valor de nivel.
	 *
	 * @param nivel El valor a asignar.
	 */
	public abstract void setNivel(double nivel);

	/**
	 * Fija el valor de mostrarVar.
	 *
	 * @param mostrarVar El valor a asignar.
	 */
	public abstract void setMostrarVar(boolean mostrarVar);

	/**
	 * Regresa el valor de mostrarVar.
	 *
	 * @return boolean.
	 */
	public abstract boolean getValorFuturo();

	/**
	 * Fija el valor de valorFuturo.
	 *
	 * @param valorFuturo El valor a asignar.
	 */
	public abstract void setValorFuturo(boolean valorFuturo);

    /**
     * Regresa el valor de posicionesMesas.
     *
     * @return List.
     */
    public abstract List getPosicionesMesas();

    /**
     * Establece el valor de posicionesMesas.
     *
     * @param posicionesMesas El valor a asignar.
     */
    public abstract void setPosicionesMesas(List posicionesMesas);

    /**
     * Constante para la expresi&oacute;n ognl "limiteRegulatorio".
     */
    private static final String LIMITE_REGULATORIO_EXPR = "limiteRegulatorio";

    /**
     * Constante para la expresi&oacute;n ognl "midSpot".
     */
    private static final String MID_SPOT_EXPR = "midSpot";

    /**
     * Constante para la expresi&oacute;n ognl "nivel".
     */
    private static final String NIVEL_EXPR = "nivel";

    /**
     * Constante para la cadena "Nivel".
     */
    private static final String NIVEL_STR = "Nivel";

    /**
     * Constante para la expresi&oacute;n ognl "nivel".
     */
    private static final String CONSUMO_EXPR = "consumo";

    /**
     * Constante para la cadena "Consumo".
     */
    private static final String CONSUMO_STR = "Consumo";

    /**
     * Constante para la cadena "Total".
     */
    private static final String TOTAL_STR = "Total:";

    /**
     * Constante para el valor 100.0.
     */
    private static final double CIEN = 100.0;

    /**
     * Constantes para los ti&iacute;tulos del excel (posici&oacute;n).
     */
    private static final String[] SUBTITULOS_POSICION = new String[]{"Divisa", "Factor Divisa",
            "Posici\u00f3n Inicial", "Cash", "Tom", "Spot", "72Hr", "96Hr", "Posici\u00f3n Final",
            "Utilidad M. N."
    };

    /**
     * Constantes para las expresiones ognl del excel (posici&oacute;n).
     */
    private static final String[] EXPRESIONES_POSICION = new String[]{"divisa.idDivisa",
            "factorDivisa", "posicionInicial", "cash", "tom", "spot", "hr72", "vFut",
            "posicionFinal", "utilidadMn"
    };

    /**
     * Constantes para los ti&iacute;tulos del excel (l&iacute;mites).
     */
    private static final String[] SUBTITULOS_LIMITES = new String[]{"Concepto", "L\u00edmite",
            NIVEL_STR, CONSUMO_STR, "Estado"
    };

    /**
     * Constantes para las expresiones ognl del excel (l&iacute;mites).
     */
    private static final String[] EXPRESIONES_LIMITES = new String[]{"tipoLimite.nombre", "limite",
            NIVEL_EXPR, "porcentajeConsumo", "estado"
    };
}
