/*
 * Obtiene todos los tipos de pizarron disponibles
 * Clase: HibernatePizarronServiceData
 * Método: void refrescarPizarrones(String ticket)
 */
 
SELECT *
FROM SC_TIPO_PIZARRON

/*
 * Elimina los renglones del pizarron para un tipo de pizarron dado
 * Clase: HibernatePizarronServiceData
 * Método: void refrescarPizarron(String ticket, TipoPizarron tipoPizaron)
 */

DELETE FROM SC_RENGLON_PIZARRON WHERE ID_TIPO_PIZARRON = :idtippiz

/*
 * Obtiene la variación actual de los tipos de cambio
 * Clase: HibernatePizarronServiceData
 * Método: Variacion findVariacionActual()
 */

SELECT *
FROM SICA_VARIACION V
WHERE V.ID_VARIACION = (
	SELECT MAX(V2.ID_VARIACION)
	FROM SICA_VARIACION V2
)
 
/*
 * @hibernate.query name="findSpreadsActualesByTipoPizarron"
 * query="FROM Spread AS s WHERE s.idSpread in (SELECT sa.idSpread FROM SpreadActual AS sa WHERE
 * sa.id.tipoPizarron.idTipoPizarron = ?)"
 * Clase: AbstractHibernateSicaServiceData
 * Método: List findSpreadsActualesByTipoPizarron(Integer idTipoPizarron)
 */

SELECT S.*
FROM SC_SPREAD S
WHERE S.ID_SPREAD IN (
	SELECT SA.ID_SPREAD
	FROM SC_SPREAD_ACTUAL SA
	WHERE SA.ID_TIPO_PIZARRON = :idtippiz
)

/* 
 * findPrecioReferenciaActual
 * "FROM PrecioReferencia AS pr WHERE pr.idPrecioReferencia = (SELECT pra.idPrecioReferencia FROM PrecioReferenciaActual pra)"
 * Clase: AbstractHibernateSicaServiceData
 * Método: PrecioReferencia findPrecioReferenciaActual()
 */
 
SELECT PR.*
FROM SC_PRECIO_REFERENCIA PR
WHERE PR.ID_PRECIO_REFERENCIA = (
	SELECT PRA.ID_PRECIO_REFERENCIA
	FROM SC_PRECIO_REFERENCIA_ACTUAL PRA
)

/*
 * proximosDiasFestivos
 * Retorna una lista de dias festivos para una semana dada (7 dias a partir de la FECHA ACTUAL)
 * "FROM FechaNoLaboral AS df WHERE df.fecha BETWEEN ? AND ? ORDER BY df.fecha"
 * TODO1: Establecer intervalo de fechas a una semana (contar 7 dias a partir de una fecha recibida como parámetro)
 * Clase: HibernatePizarronServiceData
 * Método: List proximosDiasFestivos(Date fechaCash)
 */

SELECT *
FROM BUP_FECHA_INHABIL DF
WHERE DF.FECHA BETWEEN 
	TO_DATE(:inidate || ' 00:00:00','DD/MM/YYYY HH24:MI:SS') 
	AND TO_DATE(:enddate || ' 23:59:59','DD/MM/YYYY HH24:MI:SS')
ORDER BY DF.FECHA

/*
 * "SELECT fd 
 *   FROM FactorDivisa AS fd 
 *		INNER JOIN FETCH fd.facDiv.fromDivisa 
 *		INNER JOIN FETCH fd.facDiv.toDivisa, FactorDivisaActual AS fda 
 *	WHERE fd.idFactorDivisa = fda.idFactorDivisa"
 * Clase: AbstractHibernateSicaServiceData
 * Método: findFactoresDivisaActuales
 */
	
-- alternativa 
SELECT FD.*
FROM SC_FACTOR_DIVISA FD
	INNER JOIN SC_FACTOR_DIVISA_ACTUAL FDA
		ON FDA.ID_FACTOR_DIVISA = FD.ID_FACTOR_DIVISA

/*
 * Busca el valor del parámetro FACTOR_AUTOMATICO el cual contiene 'S' o 'N'
 * Clase: HibernatePizarronServiceData
 * Método: isFactoresAutomaticos
 */

SELECT * -- Campo VALOR
FROM SC_PARAMETRO
WHERE ID_PARAMETRO = 'FACTOR_AUTOMATICO'
	
	
	
	