package com.ixe.ods.sica.sdo.impl;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Iterator;

//import macromedia.swf.types.ArrayLists;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate.HibernateCallback;

//import sun.text.CompactShortArray.Iterator;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.BitacoraCorte;
import com.ixe.ods.sica.model.Corte;
import com.ixe.ods.sica.model.CorteDetalle;
import com.ixe.ods.sica.model.DealReinicioPosicion;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.EstatusCorte;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PosicionLog;
import com.ixe.ods.sica.model.PosicionLogCorte;
import com.ixe.ods.sica.model.SumatoriaCorte;
import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.ixe.ods.sica.sdo.CorteMurexServiceData;


/**
 * Clase que implementa los servicios de Acceso a Datos para
 *  los cortes de posición que envia SICA a Murex
 *
 * @author Diego Pazaran
 *  Banorte
 *
 * @version $Revision: 1.1.2.2.2.2.12.2 $
 */
public class HibernateCorteMurexServiceData extends AbstractHibernateSicaServiceData
    implements CorteMurexServiceData {
    
	/** El logger de la clase. * */
    Logger _logger = Logger.getLogger(HibernateCorteMurexServiceData.class);

    
    public boolean eliminarCorteDetalle(int idCorte) {
    	getHibernateTemplate().deleteAll(findDetallesCortebyIdCorte(idCorte));
		return true;
	}
    
    
    /**
     *@see com.ixe.ods.sica.sdo.CorteMurexServiceData#findCorteById(int idCorte).
     */
    public Corte findCorteById(int idCorte) {
        List cortes = getHibernateTemplate().findByNamedQuery("findCorteById",
                new Object[] { new Integer(idCorte) });

        if ((cortes != null) && (cortes.size() > 0)) {
            return (Corte) cortes.get(0);
        }
        else {
            throw new SicaException("No se encontró el corte: " + idCorte);
        }
    }

    /**
     *@see com.ixe.ods.sica.sdo.CorteMurexServiceData#findEstatusCorteByEstatus(String estatusCorte).
     */
    public EstatusCorte findEstatusCorteByEstatus(String estatusCorte) {
        List estatusCorteList = getHibernateTemplate().findByNamedQuery("findEstatusCorteByEstatus",
                new Object[] { estatusCorte });

        if ((estatusCorteList != null) && (estatusCorteList.size() > 0)) {
            return (EstatusCorte) estatusCorteList.get(0);
        }
        else {
            throw new SicaException("El estatus del corte no es valido");
        }
    }

    /**
     *@see com.ixe.ods.sica.sdo.CorteMurexServiceData#insertarCorteMurex(String usuario).
     */
    public Corte insertarCorteMurex(String usuario) {
        Corte corte = null;
        _logger.debug("Insertando Corte");
        
        if ((corte = findCorteByFechaHoy()) != null && !corte.getEstatusCorte().equals(CorteMurexServiceData.ERROR_TRANSFERENCIA)
        											&& !corte.getEstatusCorte().equals(CorteMurexServiceData.ERROR_DEALINTERBANCARIO)
        											&& !corte.getEstatusCorte().equals(CorteMurexServiceData.CORTE_PROCESADO)) {
        	_logger.debug("Estatus Corte " + corte.getEstatusCorte());
        	insertarBitacoraCorte(corte, "VISTA PREVIA",usuario);//se registra Bitacora por la generacion de una nueva VP
        	return corte;
            
        }
        _logger.debug("Insertando Corte");
        corte = new Corte();

        Calendar cal = Calendar.getInstance();

        corte.setEstatusCorte(findEstatusCorteByEstatus(CorteMurexServiceData.VISTA_PREVIA)
                                  .getEstatusCorte());

        corte.setUsuario(usuario);
        corte.setUsuarioUltMod(usuario);
        corte.setFechaAlta(cal.getTime());
        corte.setFechaUltMod(cal.getTime());

        getHibernateTemplate().save(corte);

        insertarBitacoraCorte(corte, "VISTA PREVIA",usuario);

        return corte;
    }

    /**
     *@see com.ixe.ods.sica.sdo.CorteMurexServiceData#actualizarEstatusCorte(int idCorte, String usuario, String estatusCorte,
              String comentarios)
     */
    public Corte actualizarEstatusCorte(int idCorte, String usuario, String estatusCorte,
        String comentarios) {
        String comentarioDefault = "Actualiza corte";
        Corte corte = findCorteById(idCorte);

        corte.setEstatusCorte(findEstatusCorteByEstatus(estatusCorte).getEstatusCorte());
        corte.setUsuarioUltMod(usuario);
        corte.setFechaUltMod(new Date());

        getHibernateTemplate().update(corte);

        insertarBitacoraCorte(corte, (comentarios != null) ? comentarios : comentarioDefault);

        return corte;
    }

    /**
     *@see com.ixe.ods.sica.sdo.CorteMurexServiceData#findCorteByFechaHoy().
     */
    public Corte findCorteByFechaHoy() {
        List cortes = null;
        Corte corte = null;
        _logger.debug("validando si existe corte hoy");
        Integer maxIdCorte = null;

        String queryMaxIdCorte = "select max(c.idCorte) from Corte c";

        maxIdCorte = (Integer) getHibernateTemplate().find(queryMaxIdCorte).get(0);

        if (maxIdCorte != null) {
            cortes = getHibernateTemplate().findByNamedQuery("findCorteByFechaHoy",
                    new Object[] { getFechaHoy(), maxIdCorte });

            if ((cortes != null) && (cortes.size() > 0)) {
                corte = (Corte) cortes.get(0);
            }
        }

        return corte;
    }
    
    /**
     *@see com.ixe.ods.sica.sdo.CorteMurexServiceData#findCorteByFechaHoy().
     */
    public BitacoraCorte findMaxBitacoraCorteByIdCorte(Corte corte) {
        List bitacoraCortes = null;
        BitacoraCorte bitacoraCorte = null;
        _logger.debug("obteniendo el ultimo bitacoraCorte para validar el envio a Murex");

        bitacoraCortes = getHibernateTemplate().findByNamedQuery("findMaxBitacoraCorteByIdCorte",new Object[] { new Integer(corte.getIdCorte()), new Integer(corte.getIdCorte())});
        _logger.debug("Elementos de la lista: " + bitacoraCortes.size());
     // Obtenemos un Iterador y recorremos la lista.
        Iterator iter = bitacoraCortes.iterator();
        while (iter.hasNext())
          //System.out.println(iter.next());
        _logger.debug("Elementos de la lista  " + ToStringBuilder.reflectionToString(iter.next()));
        
            if ((bitacoraCortes != null) && (bitacoraCortes.size() > 0)) {
            	bitacoraCorte = (BitacoraCorte) bitacoraCortes.get(0);

        }
            _logger.debug("Elementos que devolvio:  getIdBitacora " +bitacoraCorte.getIdBitacora());

        return bitacoraCorte;
    }
    
    

    /**
     *@see com.ixe.ods.sica.sdo.CorteMurexServiceData#registrarDetalleCorte(CorteDetalle corteDetalle, int idCorte).
     */
    public int registrarDetalleCorte(CorteDetalle corteDetalle, int idCorte) {
        if (corteDetalle == null) {
            throw new SicaException("El detalle a registrar no puede ser nulo");
        }
        
        corteDetalle = buildCorteDetalle(corteDetalle);
        _logger.debug("Generando corte Detalle"+ ToStringBuilder.reflectionToString(corteDetalle));
        Corte corte = findCorteById(idCorte);
        corteDetalle.setIdCorte(corte.getIdCorte());

        Integer idDetalle = (Integer) getHibernateTemplate().save(corteDetalle);

        return idDetalle.intValue();
    }

    /**
     *@see com.ixe.ods.sica.sdo.CorteMurexServiceData#findDetallesCortebyIdCorte(int idCorte).
     */
    public List findDetallesCortebyIdCorte(int idCorte) {
        List detallesCorte = null;
        Corte corte = findCorteById(idCorte);

        detallesCorte = getHibernateTemplate().findByNamedQuery("findDetallesCortebyIdCorte",
                new Object[] { new Integer(corte.getIdCorte()) });

        return detallesCorte;
    }

    /**
     * Registra un Evento en la bitacora.
     *
     * @param corte <code>Corte</code>
     *                 que se registra en la bitacora.
     * @param comentarios <code>String</code>
     *                 con los comentarios.
     */
    private void insertarBitacoraCorte(Corte corte, String comentarios) {
        BitacoraCorte bitacora = new BitacoraCorte(corte);
        bitacora.setComentarios(comentarios);

        getHibernateTemplate().save(bitacora);
    }
    
    /**
     * Registra un Evento en la bitacora.
     *
     * @param corte <code>Corte</code>
     *                 que se registra en la bitacora.
     * @param comentarios <code>String</code>
     *                 con los comentarios.
     */
    private void insertarBitacoraCorte(Corte corte, String comentarios,String usuario) {
        BitacoraCorte bitacora = new BitacoraCorte(corte,usuario);
        bitacora.setComentarios(comentarios);

        getHibernateTemplate().save(bitacora);
    }

    /**
     *@see com.ixe.ods.sica.sdo.ServiceData#find(Class, Serializable).
     */
    public Object find(Class clase, Serializable id) {
        return this.getHibernateTemplate().load(clase, id);
    }

    /**
     * Regresa la fecha del día.
     *
     * @return <code>String</code>
     *         con la fecha del día.
     */
    public String getFechaHoy() {
        String fechaHoy = null;
        SimpleDateFormat dateFormat = null;

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        ParametroSica p = (ParametroSica) find(ParametroSica.class, ParametroSica.FECHA_SISTEMA);

        try {
            fechaHoy = dateFormat.format(dateFormat.parse(p.getValor()));
            _logger.debug("Obteniendo fecha de hoy " + fechaHoy);
        }
        catch (ParseException pe) {
            fechaHoy = dateFormat.format(Calendar.getInstance().getTime());
            _logger.debug("PE...Obteniendo fecha de hoy " + fechaHoy);
        }

        return fechaHoy;
    }
    
    /**
     *@see com.ixe.ods.sica.sdo.CorteMurexServiceData#findNumCorteByFechaHoy().
     */
    public Integer findNumCorteByFechaHoy() {
        _logger.debug("validando el numero de cortes que se han hecho hoy");
        Integer numCortes = null;

        String queryNumCortes = "select count(c.idCorte) from Corte c where trunc(c.fechaAlta) = trunc(sysdate)";

        numCortes = (Integer) getHibernateTemplate().find(queryNumCortes).get(0);
        
        if (numCortes == null)
        	numCortes = new Integer(0);

        return numCortes;
    }
    
    /**
     * @see ServiceData#findDatosMonitor(String).
     */
    public List findDatosMonitor() throws SicaException {
        List drp = getHibernateTemplate().findByNamedQuery("findAllDealReinicio");
        if (drp.isEmpty()) {
            throw new SicaException("El dia de hoy no se han generado deals interbancarios. ");
        }
        return drp;
    }

    /**
     * Verifica y asigna valores constantes
     *  para las propiedades que no contenga un valor.
     *
     * @param cd , objeto <code>CorteDetalle</code>
     *                 a que se procesará.
     *
     * @return Objeto <code>CorteDetalle</code> formateado.
     */
    private CorteDetalle buildCorteDetalle(CorteDetalle cd) {
        if (StringUtils.isEmpty(cd.getType1())) {
            cd.setType1(CorteMurexServiceData.DET_CORTE_TYPE_DEFAULT);
        }

        if (StringUtils.isEmpty(cd.getAction())) {
            cd.setAction(CorteMurexServiceData.DET_CORTE_ACTION_DEFAULT);
        }

        if (StringUtils.isEmpty(cd.getSourceModule())) {
            cd.setSourceModule(CorteMurexServiceData.DET_CORTE_SOURCE_MODULE_DEFAULT);
        }

        if (StringUtils.isEmpty(cd.getOurName())) {
            cd.setOurName(CorteMurexServiceData.DET_CORTE_OURNAME_DEFAULT);
        }

        if (StringUtils.isEmpty(cd.getTheirName())) {
            cd.setTheirName(CorteMurexServiceData.DET_CORTE_THEIRNAME_DEFAULT);
        }

        if (StringUtils.isEmpty(cd.getOurPortfolio())) {
            cd.setOurPortfolio(CorteMurexServiceData.DET_CORTE_OURPORTFOLIO_DEFAULT);
        }

        if (StringUtils.isEmpty(cd.getUser1())) {
            cd.setUser1(CorteMurexServiceData.DET_CORTE_USER_DEFAULT);
        }

        if (StringUtils.isEmpty(cd.getGroup1())) {
            cd.setGroup1(CorteMurexServiceData.DET_CORTE_GROUP_DEFAULT);
        }

        if (StringUtils.isEmpty(cd.getAcountingSectionSource())) {
            cd.setAcountingSectionSource(CorteMurexServiceData.DET_CORTE_ACOUNTINGSECTIONSOURCE_DEFAULT);
        }

        if (StringUtils.isEmpty(cd.getBackToBackPortfolioName())) {
            cd.setBackToBackPortfolioName(CorteMurexServiceData.DET_CORTE_BACKTOBACKPORTFOLIONAME_DEFAULT);
        }

        return cd;
    }

	public Integer insertarDealReinicioPosicion(DealReinicioPosicion dealReinicioPosicion) {
		_logger.info("Insertando Deal Interbancario de reinicio de posicion");
		Integer idDRP = (Integer) getHibernateTemplate().save(dealReinicioPosicion);
		_logger.info("Deal Interbancario de reinicio de posicion insertado.." + dealReinicioPosicion!= null ? 
				ToStringBuilder.reflectionToString(dealReinicioPosicion): "Deal de reinicio Nulo");
		return idDRP;
	}

	public boolean eliminarDealsReinicioPosicion(int idCorte) {
		getHibernateTemplate().deleteAll(findDealsReinicioPosicionByIdCorte(idCorte));
		return true;
	}

	public List findDealsReinicioPosicionByIdCorte(int idCorte) {
		List deals = getHibernateTemplate().findByNamedQuery("findDealReinicioByCorteId", new Object[]{new Integer(idCorte)});
		return deals;
	}
	
	public List findDealReinicioPosicion(int idCorte,String idDivisa, String operacion) {
		List deals = getHibernateTemplate().findByNamedQuery("findDealReinicio", new Object[]{new Integer(idCorte), idDivisa, operacion});
		return deals;
	}
	
	public List findMaxIdPosicionLog() {
		List maxidPosicionLog = getHibernateTemplate().findByNamedQuery("findMaxIdPosicionLog");
		return maxidPosicionLog;
	}
	
	
	public DealReinicioPosicion actualizarReinicioPosicion(String usuario,int resultDeal,
			String idDivisa, String operacion) {
		DealReinicioPosicion drp = new DealReinicioPosicion();
		Corte corte              = findCorteByFechaHoy();
		List drpLista            = findDealReinicioPosicion(corte.getIdCorte(), idDivisa, operacion);
		if(drpLista.size() == 1){
			drp = (DealReinicioPosicion) findDealReinicioPosicion(corte.getIdCorte(), idDivisa, operacion).get(0);
			drp.setIdDeal(new Integer(resultDeal));
			getHibernateTemplate().update(drp);
		}else{
			logger.error("Se encontro mas de un DealReinicioPosicion con los mismos criterios");
		}
		return drp;
	}

	
	public List getCorteDetalleByFechaAndDivisa(Date fecha, Divisa divisa) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String strFecha = dateFormat.format(fecha);
		List cdList = getHibernateTemplate().findByNamedQuery("findDetallesCortebyfechaAndDivisa",new Object[]{strFecha, divisa.getIdDivisa()});
		logger.info("Detalles encontrados: " + cdList != null? cdList.size() + "" : "0");
		return cdList;
	}


	public List getCorteDetalleByFecha(Date fecha) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String strFecha = dateFormat.format(fecha);
		List cdList = getHibernateTemplate().findByNamedQuery("findDetallesCortebyfecha",new Object[]{strFecha});
		logger.info("Detalles encontrados: " + cdList != null? cdList.size() + "" : "0");
		return cdList;
	}

	

    /**
     * Inserta un nuevo registro en sc_posicion_log_corte.
     *
     * @param pl <code>PosicionLog</code>
     *                 que se registra en la posicion del corte.
     */
	public void insertLogCorte(PosicionLog pl){
		_logger.info("Insertando PosicionLogCorte ");
		
		PosicionLogCorte plc = new PosicionLogCorte();
		plc.setIdPosicionLog(pl.getIdPosicionLog());
		plc.setTipoValor(pl.getTipoValor());
		plc.setDeal(pl.getDeal().getIdDeal());
		plc.setDealPosicion(pl.getDealPosicion().getIdDealPosicion());
		plc.setDivisa(pl.getDivisa().getIdDivisa());
		plc.setMonto(pl.getMonto());
		plc.setMontoMn(pl.getMontoMn());
		
		plc.setPrecioReferencia(pl.getPrecioReferencia());
		plc.setTipoCambioCliente(pl.getTipoCambioCliente());
		plc.setTipoCambioMesa(pl.getTipoCambioMesa());
		plc.setTipoOperacion(pl.getTipoOperacion());
		plc.setFecha(pl.getFecha());
		
		
		if (pl.getTipoOperacion() == PosicionLog.CANCELACION_COMPRA){
			pl.setTipoOperacion(PosicionLog.VENTA);
			pl.setMonto(pl.getMonto() * -1);
			pl.setMontoMn(pl.getMontoMn() * -1);
		}
		
		else if (pl.getTipoOperacion() == PosicionLog.CANCELACION_VENTA){
			pl.setTipoOperacion(PosicionLog.COMPRA);
			pl.setMonto(pl.getMonto() * -1);
			pl.setMontoMn(pl.getMontoMn() * -1);
		}
		
		else if (pl.getTipoOperacion() == PosicionLog.CANCELACION_COMPRA_INTERBANCARIA){
			pl.setTipoOperacion(PosicionLog.VENTA);
			pl.setMonto(pl.getMonto() * -1);
			pl.setMontoMn(pl.getMontoMn() * -1);
		}
		
		else if (pl.getTipoOperacion() == PosicionLog.CANCELACION_VENTA_INTERBANCARIA){
			pl.setTipoOperacion(PosicionLog.COMPRA);
			pl.setMonto(pl.getMonto() * -1);
			pl.setMontoMn(pl.getMontoMn() * -1);
		}
		
		else if (pl.getTipoOperacion() == PosicionLog.VENTA_INTERBANCARIA){
			pl.setTipoOperacion(PosicionLog.VENTA);			
		}
		
		else if (pl.getTipoOperacion() == PosicionLog.COMPRA_INTERBANCARIA){
			pl.setTipoOperacion(PosicionLog.COMPRA);
		}
		
		getHibernateTemplate().save(plc);
		_logger.info("Registro insertado en SC_POSICION_LOG_CORTE.." + pl!= null ? 
				ToStringBuilder.reflectionToString(pl): "PosicionLogCorte Nulo");
		
	}
	
	public List realizaSumatoriaCV(String idDivisa){//AQUI OBTENER LA SUMATORIA Y CREAR EL POSICIONVO Y RETORNAR LISTA

//		List posicioLogCortes = getHibernateTemplate().find(
//							
//                         "SELECT new com.ixe.ods.sica.posicion.vo.PosicionVO(plc.divisa,plc.tipoValor,plc.tipoOperacion,SUM(round((plc.monto * plc.tipoCambioMesa),2)),SUM(plc.montoMn),SUM (plc.monto),plc.corte) "
//                                 + " FROM PosicionLogCorte AS plc WHERE plc.corte is null "
//                                 + " GROUP BY plc.divisa,plc.tipoValor,plc.tipoOperacion,plc.corte "
//                                 + " ORDER BY plc.divisa,plc.tipoValor,plc.tipoOperacion,plc.corte "
//						);
		_logger.info("dIVISA --->." + idDivisa);
		List sumatoriaCortes = getHibernateTemplate().findByNamedQuery("findAllSumatoriaCorte",new Object[] {idDivisa });
		
		List listPosicionVo = new ArrayList();
		
				for (Iterator it = sumatoriaCortes.iterator();it.hasNext();){
					
					SumatoriaCorte sumCorte = (SumatoriaCorte) it.next();
					PosicionVO pvo = new PosicionVO(sumCorte.getIdDivisa(),sumCorte.getTipoValor(),sumCorte.getTipoOperacion(),sumCorte.getMontoMxnMesa(),sumCorte.getMontoMxnCliente(),sumCorte.getMonto(),sumCorte.getIdCorte());
					listPosicionVo.add(pvo);
					
				}
				_logger.info("NUM --->." + listPosicionVo != null ? listPosicionVo.size()+"" : "0");
		return listPosicionVo;
		
		
	}
	
//	public void invocarSP(){
//		try {
//			_logger.debug("<<< ENTRO AL METODO QUE INVOCA EL SP >>>");
//			getHibernateTemplate().getSessionFactory().openSession().getNamedQuery("{CALL SC_INSERTA_ACUM_CORTES}").list();
//			
//		} catch (HibernateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
    /**
     * Llama al stored procedure encargado en actualizar los pizarrones SC_SP_GENERAR_PIZARRONES_SICA
     */
    public void invocarSP() {
    	try {
    		_logger.debug("<<< ENTRO AL METODO QUE INVOCA EL SP >>>");
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					
					CallableStatement cs = session.connection().prepareCall(
						"{ call SICA_ADMIN.SC_INSERTA_ACUM_CORTES }"
					);
					cs.executeUpdate();
					return null;
				}
			});
		} catch (DataAccessException e) {
			if (logger.isWarnEnabled()) {
                logger.warn(e);
            }
            throw new SicaException("No fue posible ejecutar el SP SC_INSERTA_ACUM_CORTES ");
		}
    }
    
    /**
     * Pone el numero de corte a la tabla sc_posicion_log_cortes y sc_sumatoria_cortes cuyo id_corte es nulo ya que el corte fue enviado a MX
     */
    public void setIdCortePorEnvioMX(){
    	
        Integer maxIdCorte = null;

        String queryMaxIdCorte = "select max(c.idCorte) from Corte c";

        maxIdCorte = (Integer) getHibernateTemplate().find(queryMaxIdCorte).get(0);
    	
    	_logger.debug("SELECT max(c.idCorte) FROM Corte AS c "+ maxIdCorte);
    	
    	List sumatoriaCorte = getHibernateTemplate().findByNamedQuery("findAllSumatoriaCorteSinEnviar");
    	
    	for(Iterator it = sumatoriaCorte.iterator();it.hasNext();){
    		SumatoriaCorte sumCorte = (SumatoriaCorte) it.next();
    		sumCorte.setIdCorte(maxIdCorte);
    		getHibernateTemplate().update(sumCorte);
    	}
    	
    	List  getAllPosicionLogCorte = getHibernateTemplate().find("FROM PosicionLogCorte as plc WHERE plc.corte is null");
    	
    	for(Iterator it = getAllPosicionLogCorte.iterator();it.hasNext();){
    		PosicionLogCorte plc = (PosicionLogCorte) it.next();
    		plc.setCorte(maxIdCorte);
    		getHibernateTemplate().update(plc);
    	}
    	
    }
	
	public List getDistintasDivisas(){
	
		List divisas = getHibernateTemplate().find("SELECT distinct p.divisa.idDivisa FROM Posicion AS p");
	return divisas;
	}
	
	public List getDivisas(List divisas){
		List divisa = getHibernateTemplate().findByNamedParam("FROM Divisa AS d WHERE d.idDivisa in (:divisas) ORDER BY d.orden", "divisas", divisas);
		return divisa;
	}
	
	public List findDealReiniciobyfechaAndDivisa(Date fecha,Divisa divisa){
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String strFecha = dateFormat.format(fecha);
		List cdList = getHibernateTemplate().findByNamedQuery("findDealReiniciobyfechaAndDivisa",new Object[]{strFecha, divisa.getIdDivisa()});
		logger.info("Detalles Reinicio encontrados: " + cdList != null? cdList.size() + "" : "0");
		return cdList;
	};
	
	
	
	public List findDealReiniciobyfecha(Date fecha){
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String strFecha = dateFormat.format(fecha);
		List cdList = getHibernateTemplate().findByNamedQuery("findDealReiniciobyfecha",new Object[]{strFecha});
		logger.info("Detalles Reinicio encontrados: " + cdList != null? cdList.size() + "" : "0");
		_logger.info("Registro de la lista cdList" + cdList!= null ? 
				ToStringBuilder.reflectionToString(cdList): "ReinicionPosicion Nulo");
		return cdList;
		
	};	

}
