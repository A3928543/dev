--
-- Selecciona los registros de la tabla de control SC_RESPALDO_TABLA para mostrar las cifras de control de los respaldos
-- realizados por tabla
--

set serveroutput on size 1000000

select ctr.nom_tbl_orig, r.tipo_respaldo, rt.*
from sc_respaldo_tabla rt
    inner join sc_cat_tabla_respaldo ctr
        on rt.id_tabla = ctr.id_tabla
	inner join sc_respaldo r
		on r.id_respaldo = rt.id_respaldo
order by rt.id_respaldo, rt.id_respaldo_tabla

