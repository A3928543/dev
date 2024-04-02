import mx.core.UIComponent
import mx.controls.CheckBox
import mx.controls.Alert

/**
 * Clase que define el tipo de celda para la selecci&oacute;n
 * de clientes favoritos de un promotor
 */
class com.ixe.ods.sica.informacionnegociacion.views.CheckBoxCellRenderer extends CheckBox
{
	private var listOwner : MovieClip;
	private var getCellIndex : Function;
	private var	getDataLabel : Function;
	
	/**
	 * Constructor.
	 */
	public function CheckBoxCellRenderer()
	{
	}
	
	/**
	 * Crea el componente.
	 */
	public function createChildren(Void) : Void
	{
		super.createChildren();
		addEventListener("click", this);
	}

	/**
	 * Define el valor para el componente.
	 */
	public function setValue(str:String, item:Object, sel:Boolean) : Void
	{
		this._visible = (item!=undefined);
		
		//var obj : Object = listOwner.dataProvider.getItemAt(getCellIndex().itemIndex);
		
		//Alert.show("Seleccionado: " + item[getDataLabel()] + "---" + item["favorito"]);
		
		if(item["favorito"] == 1){
			this.selected = true;
		}else{
			this.selected = false;
		}
		
	}
	
	/**
	 * Maneja el evento click en check.
	 */
	public function click()
	{
		var obj : Object = listOwner.dataProvider.getItemAt(getCellIndex().itemIndex);
		
		if(obj.contratoCorto == 0){
			Alert.show("Debe primero asignar un contrato corto al cliente.", 'Atenci\u00f3n:');
		}else{
		
			listOwner.dataProvider.editField(getCellIndex().itemIndex, getDataLabel(), this.selected);
			this.parentApplication.marcarFavorito(obj.contratoSica, this.selected);
		}
	}
}
