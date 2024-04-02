--
-- Inserts para dar de alta los parametros necesarios de Tipo de Cambio Especial, en la tabla SC_PARAMETRO
--

-- Monto maximo para promotores en dolares
INSERT INTO SICA_ADMIN.SC_PARAMETRO (ID_PARAMETRO, TIPO_VALOR, VALOR) VALUES ('TCE_MONTO_MAXIMO_PROMOTORES', 'n', '3000');

-- Porcentaje de variacion maximo respecto al TC pizarron, al cual queda sujeto el TCM
INSERT INTO SICA_ADMIN.SC_PARAMETRO (ID_PARAMETRO, TIPO_VALOR, VALOR) VALUES ('TCE_PORCENT_VAR_TCM_VS_TCR', 'n', '5');


