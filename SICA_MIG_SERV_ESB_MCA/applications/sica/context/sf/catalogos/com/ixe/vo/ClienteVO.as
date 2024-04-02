class com.ixe.vo.ClienteVO
{
    var clave : String;
    var nombre : String;
    var paterno : String;
    var materno : String;

    function ClienteVO()
    {
        clave = '';
        nombre = '';
        paterno = '';
        materno = '';
    }

    static function crear(clave : String, nombre : String, paterno : String, materno : String) : ClienteVO
    {
        var cte : ClienteVO = new ClienteVO();
        cte.clave = clave;
        cte.nombre = nombre;
        cte.paterno = paterno;
        cte.materno = materno;
        return cte;
    }
}
