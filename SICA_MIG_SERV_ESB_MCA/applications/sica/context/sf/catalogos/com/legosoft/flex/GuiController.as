class com.legosoft.flex.GuiController extends mx.core.UIObject
{
    public static var LISTAR        : Number = 1 << 0;
    public static var INSERTAR      : Number = 1 << 1;
    public static var ELIMINAR      : Number = 1 << 2;
    public static var ACTUALIZAR    : Number = 1 << 3;
    public static var DESHACER      : Number = 1 << 4;
    public static var EDIT_FIELD    : Number = 1 << 5;
    public static var INSERT_FIELD  : Number = 1 << 6;
    public static var SELECCIONAR   : Number = 1 << 7;
    public static var DESELECCIONAR : Number = 1 << 8;

    private var currOp   : Number;
    public  var dataGrid : Object;

    public function inicializar() : Void
    {
        if (dataGrid != null) {
            dataGrid.addEventListener('change', this);
        }
        setCurrOp(LISTAR | INSERTAR);
    }

    private function change(event : Object) : Void
    {
        if (event.target.selectedItem != null) {
            setCurrOp(SELECCIONAR);
        }
        else {
            setCurrOp(DESELECCIONAR);
        }
    }

    public function getCurrOp() : Number
    {
        return currOp;
    }

    public function setCurrOp(co : Number)
    {
        if (co == LISTAR) {
            currOp = (LISTAR | INSERTAR);
        }
        else if (co == INSERTAR) {
            currOp = (INSERT_FIELD | EDIT_FIELD | ACTUALIZAR | DESHACER);
        }
        else if (co == ELIMINAR) {
            currOp = (ACTUALIZAR | DESHACER) & (~EDIT_FIELD) & (~INSERT_FIELD);
        }
        else if (co == EDIT_FIELD) {
            dataGrid.enabled = false;
            currOp = (currOp & ~(LISTAR | INSERTAR | ELIMINAR)) | ACTUALIZAR | DESHACER;
        }
        else if (co == ACTUALIZAR || co == DESHACER) {
            currOp = (LISTAR | INSERTAR);
        }
        else if (co == SELECCIONAR) {
            currOp = currOp | EDIT_FIELD | ELIMINAR;
        }
        else if (co == DESELECCIONAR) {
            currOp = currOp & (~EDIT_FIELD) & (~ELIMINAR);
        }
        dispatchEvent({type:'enableControls', currOp: currOp});
    }

    public function changeCurrOp(event : Object)
    {
        setCurrOp(event.op);
    }

    public function campoEditado() : Void
    {
        setCurrOp(EDIT_FIELD);
    }

    public function fetch() : Void
    {
        setCurrOp(LISTAR);
    }

    public function insert() : Void
    {
        setCurrOp(INSERTAR);
    }

    public function erase() : Void
    {
        setCurrOp(ELIMINAR);
        dataGrid.enabled = false;
    }

    public function save() : Void
    {
        setCurrOp(ACTUALIZAR);
        dataGrid.enabled = true;
    }

    public function revert() : Void
    {
        setCurrOp(DESHACER);
        dataGrid.enabled = true;
    }
}
