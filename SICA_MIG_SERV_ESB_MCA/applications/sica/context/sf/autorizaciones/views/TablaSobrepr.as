/*
 * $Id: TablaSobrepr.as,v 1.12 2010/04/30 15:20:26 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010 LegoSoft S.C.
 */

import mx.managers.*;

/**
 * Subclase de views.Tabla que agrega la funcionalidad para que el autorizador modifique el tipo de cambio de mesa de un
 * detalle de deal seleccionado.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2010/04/30 15:20:26 $
 */
class views.TablaSobrepr extends views.Tabla
{

    /**
     * Abre el popup para editar el tipo de cambio de mesa.
     */
	private function editarTc() : Void
	{
		var popup = PopUpManager.createPopUp(_root, EditarTc, true,
				{deferred:true, tablaAut:this, detalle: this['tablaDetalles'].selectedItem});
		popup.centerPopUp(this);
	}

    /**
	*	Abre el popup para editar el tipo de cambio a multiples registros
	*/
	private function editarMultTc(): Void
	{
		var popup = PopUpManager.createPopUp(_root, DetalleSobreprecio, true,
				{deferred:true, parentRef: this, deal: this['tabla'].selectedItem});
		popup.centerPopUp(this);
	}

    /**
     * Actualiza el tipo de cambio de mesa del detalle de deal seleccionado.
     *
     * @param tcm El tipo de cambio de mesa a asignar al detalle de deal.
     */
	private function modificarTcm(tcm : Number) : Void
	{
        if (isNaN(tcm)) {
			throw new Error('El tipo de cambio no es un valor num\u00earico.');
        }
        this['tablaDetalles'].selectedItem.tcm = tcm;
		this['tablaDetalles'].dataProvider = this['tablaDetalles'].dataProvider;
	}
	
	/**
	* Actualiza el tipo de cambio de mesa de los detalles correspondientes.
	*
	* @param detalles La lista de detalles que se les cambio el tipo de cambio de mesa.
	*/
	private function modificarMultTcm(detalles : Array) : Void
	{
      for (var i: Number = 0; i < this['tablaDetalles'].dataProvider.length; i++) {
         for (var j: Number = 0; j < detalles.length; j++) {
            if (this['tablaDetalles'].dataProvider[i].idDealPosicion == detalles[j].idDealPosicion) {
               this['tablaDetalles'].dataProvider[i].tcm = detalles[j].tipoCambioMesa;
               this['tablaDetalles'].dataProvider[i].tipoCambioModificado = detalles[j].tipoCambioModificado;
            }
         }
      }
      this['tablaDetalles'].dataProvider = this['tablaDetalles'].dataProvider;
	}
   
}