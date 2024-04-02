/*
 * $Id: CatGrupoTrabajoPromotor.java,v 1.3 2010/05/14 21:37:31 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.catalogos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.GrupoTrabajo;
import com.ixe.ods.sica.model.GrupoTrabajoPromotor;
import com.ixe.ods.sica.pages.SicaPage;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * @author Israel Rebollar
 * @version $Revision: 1.3 $ $Date: 2010/05/14 21:37:31 $
 */
public abstract class CatGrupoTrabajoPromotor extends SicaPage {
	
	
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
		setGruposTrabajo(getSicaServiceData().findAll(GrupoTrabajo.class));
		if (getGruposTrabajo().isEmpty()) {
			getDelegate().record("No se han capturado Grupos de Trabajo, favor de capturar " +
					"grupos de trabajo previo a ingresar a esta pantalla.", null);
			return;
		}
		setGruposTrabajoPromotores(consultaGruposTrabajoPromotores());
		setPromotores(obtienePromotoresSinAsignar());
	}

	
	public List consultaGruposTrabajoPromotores(){
		
		List gruposList = getSicaServiceData().findAll(GrupoTrabajoPromotor.class);
		Map grupos = new HashMap();
		List tempList;
		for (Iterator iter = gruposList.iterator(); iter.hasNext();){
			GrupoTrabajoPromotor gpoTrabajoPromotor = (GrupoTrabajoPromotor) iter.next();
			if (!grupos.containsKey(gpoTrabajoPromotor.getId().getGrupoTrabajo().getIdGrupoTrabajo())){
				tempList = new ArrayList();
				tempList.add(gpoTrabajoPromotor);
			}else{
				tempList = (List) grupos.get(gpoTrabajoPromotor.getId().getGrupoTrabajo().getIdGrupoTrabajo());
				tempList.add(gpoTrabajoPromotor);
			}
			grupos.put(gpoTrabajoPromotor.getId().getGrupoTrabajo().getIdGrupoTrabajo(), tempList);
		}
		List returnedList = new ArrayList();
		for (Iterator iter = grupos.values().iterator(); iter.hasNext();) {
			List tmp = (List)iter.next();
			Collections.sort(tmp, new Comparator() {
				public int compare(Object o1, Object o2) {
					GrupoTrabajoPromotor gt1 = (GrupoTrabajoPromotor) o1;
					GrupoTrabajoPromotor gt2 = (GrupoTrabajoPromotor) o2;
					return gt1.getId().getPromotor().getNombre().compareTo(
							gt2.getId().getPromotor().getNombre());
				}
			});
			returnedList.add(tmp);
		}
		return returnedList;
	}
	
	/**
	 * 
	 * @return
	 */
	public List obtienePromotoresSinAsignar(){
		List grupoTrabajoList = getSicaServiceData().findAll(GrupoTrabajoPromotor.class);
		List promotores = getSicaServiceData().findAllPromotoresSICA(((Visit)getVisit()).getTicketInfo().getNombreSistema());
		for (Iterator iter = grupoTrabajoList.iterator(); iter.hasNext();){
			GrupoTrabajoPromotor gpoTrabajoPromotor = (GrupoTrabajoPromotor) iter.next();
			if (promotores.contains(gpoTrabajoPromotor.getId().getPromotor())){
				promotores.remove(gpoTrabajoPromotor.getId().getPromotor());
			}
		}
		return promotores;
	}
	
	/**
	 * Eliminar un registro de GrupoTrabajo.
	 * 
	 * @param cycle
	 */
	public void eliminarCombinacion(IRequestCycle cycle){
		GrupoTrabajoPromotor combinacionEliminar = (getSicaServiceData().findCombinacionGrupoTrabajo(
				cycle.getServiceParameters()[0].toString(), cycle.getServiceParameters()[1].toString()));
		if (combinacionEliminar != null){
			getSicaServiceData().delete(combinacionEliminar);
		}
		setGruposTrabajo(getSicaServiceData().findAll(GrupoTrabajo.class));
		setGruposTrabajoPromotores(consultaGruposTrabajoPromotores());
		setPromotores(obtienePromotoresSinAsignar());
	}
	
	/**
	 * 
	 */
	public void agregarCombinacion(IRequestCycle cycle){
		getSicaServiceData().store(getNuevaCombinacionGrupoPromotor());
		setGruposTrabajo(consultaGruposTrabajoPromotores());
		setGruposTrabajo(getSicaServiceData().findAll(GrupoTrabajo.class));
		setGruposTrabajoPromotores(consultaGruposTrabajoPromotores());
		setPromotores(obtienePromotoresSinAsignar());
	}
	
	/**
	 * 
	 * @return IPropertySelectionModel
	 */
	public IPropertySelectionModel getPromotoresModel(){
		return new RecordSelectionModel(getPromotores(), "idPersona", "nombreCompleto");
	}
	
	
	public IPropertySelectionModel getGrupoTrabajoModel(){
		return new RecordSelectionModel(getGruposTrabajo(), "idGrupoTrabajo", "nombre");
	}
	
	/**
	 * 
	 * @return List.
	 */
	public abstract List getGruposTrabajoPromotores();
	
	/**
	 * 
	 * @param grupos
	 */
	public abstract void setGruposTrabajoPromotores(List grupos);
	
	/**
	 * 
	 * @return
	 */
	public abstract List getPromotores();
	
	/**
	 * 
	 * @param promotores
	 */
	public abstract void setPromotores(List promotores);
	
	/**
	 * 
	 * @return
	 */
	public abstract List getGruposTrabajo();
	
	/**
	 * 
	 */
	public abstract void setGruposTrabajo(List grupo);
	
	/**
	 * 
	 * @return GrupoTrabajoPromotor.
	 */
	public abstract GrupoTrabajoPromotor getNuevaCombinacionGrupoPromotor();
	
	/**
	 * 
	 * @param grupoTrabajo
	 */
	public abstract void setNuevaCombinacionGrupoPromotor(GrupoTrabajoPromotor grupoTrabajo);
	
}
