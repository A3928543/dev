SET DEFINE OFF;

Insert into SICA_ADMIN.SICA_VARIACION (
	id_variacion, 
	fecha, 
	variacion_mid, 
	variacion_vta_spot, 
	variacion_vta_spot_ask, 
	mxn_eur_variacion_vta_spot, 
	mxn_eur_variacion_vta_spot_ask, 
	cad_variacion_vta_spot, 
	cad_variacion_vta_spot_ask, 
	chf_variacion_vta_spot, 
	chf_variacion_vta_spot_ask, 
	eur_variacion_vta_spot, 
	eur_variacion_vta_spot_ask, 
	gbp_variacion_vta_spot, 
	gbp_variacion_vta_spot_ask, 
	jpy_variacion_vta_spot, 
	jpy_variacion_vta_spot_ask
) Values (
	SICA_VARIACION_SEQ.NEXTVAL, 
	SYSDATE, 
	12.616, 12.582, 12.65, 
    15.551352, 15.64299, 1.0351, 1.0354, 1.1331, 
    1.1341, 1.236, 1.2366, 1.4537, 1.4542, 
    92.46, 92.51
);
COMMIT;
