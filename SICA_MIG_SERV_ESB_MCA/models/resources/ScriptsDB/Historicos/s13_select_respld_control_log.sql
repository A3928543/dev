--
-- Selecciona los registros de la tabla de control SC_RESPALDO_TABLA_LOG, para mostrar los mensajes de log generados por 
-- los procesos de respaldo para cada tabla
--

set serveroutput on size 1000000

select ctr.nom_tbl_orig, rtl.*
from sc_respaldo_tabla_log rtl
    left outer join sc_cat_tabla_respaldo ctr
        on rtl.id_tabla = ctr.id_tabla
order by rtl.id_respaldo_tabla_log

