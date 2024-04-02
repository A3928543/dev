import mx.core.UIComponent
import mx.controls.CheckBox
import mx.controls.Alert

/**
 * Clase que define el tipo de celda para la selecci&oacute;n
 * de multiples elementos en el DataGrid de Conformaci&oacute;n de
 * operaciones interbancarias.
 */
class views.CheckBoxCellRenderer extends CheckBox
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
	 * Crea el compenente.
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
		this.selected = item[getDataLabel()];
	}
	
	/**
	 * Maneja el evento click en check.
	 */
	public function click()
	{
		listOwner.dataProvider.editField(getCellIndex().itemIndex, getDataLabel(), this.selected);
		if( this.selected ){
			this.parentApplication.selectedCount ++;
		}else{
			this.parentApplication.selectedCount --;
		}
		if(this.parentApplication.selectedCount == 0){
			this.parentDocument.contactoTextField.text = "";
		}
	}
}
