import mx.core.UIComponent
import mx.controls.CheckBox
import mx.controls.Button
import mx.controls.Alert
import mx.managers.PopUpManager;

/**
 * Clase que define el tipo de celda para la modificacion
 * de datos de clientes 
 */
class com.ixe.ods.sica.informacionnegociacion.views.EditCellRenderer extends Button {
	[Embed(source="resources/editar.gif")] 
    var editIcon:String; 

	private var listOwner : MovieClip;
	private var getCellIndex : Function;
	private var	getDataLabel : Function;
	
	/**
	 * Constructor.
	 */
	public function EditCellRenderer() {
	}
	
	/**
	 * Crea el componente.
	 */
	public function createChildren(Void) : Void {
		super.createChildren();
		addEventListener("click", this);
	}

	/**
	 * Define el valor para el componente.
	 */
	public function setValue(str:String, item:Object, sel:Boolean) : Void {
		this.icon = editIcon;
		if(item["editar"] == 1){
			this.enabled = true;
		} else {
			this.enabled = false;
		}
	}
	
	/**
	 * Maneja el evento click en el boton.
	 */
	public function click() {
		var objCliente : Object = listOwner.dataProvider.getItemAt(getCellIndex().itemIndex);
		
		if(objCliente.idCliente == 0) {
			return;
		}
		
		var popup = PopUpManager.createPopUp(_root, EditarInfoContacto, true,
			{deferred: true, cliente:objCliente, parentApp : this.parentApplication}
		);
		popup.lblSubtitle.text = "Datos de contacto de: " + objCliente.contratoSica + " " + objCliente.cliente;
		if (objCliente.telefono != null) {
			popup.tiPhoneNumber.text = objCliente.telefono;
		}
		if (objCliente.contacto != null) {
			popup.tiName.text = objCliente.contacto;
		}
		if (objCliente.email != null) {
			popup.tiEmail.text = objCliente.email;
		}
		
		popup.centerPopUp(listOwner);
	}
	
}
