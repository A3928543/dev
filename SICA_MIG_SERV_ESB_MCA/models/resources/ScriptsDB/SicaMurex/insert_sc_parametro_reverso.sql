
-- Monto maximo para promotores
DELETE FROM SICA_ADMIN.SC_PARAMETRO WHERE ID_PARAMETRO = 'TCE_MONTO_MAXIMO_PROMOTORES';

-- Porcentaje de variacion maximo respecto al TC pizarron, al cual queda sujeto el TCM
DELETE FROM SICA_ADMIN.SC_PARAMETRO WHERE ID_PARAMETRO = 'TCE_PORCENT_VAR_TCM_VS_TCR';