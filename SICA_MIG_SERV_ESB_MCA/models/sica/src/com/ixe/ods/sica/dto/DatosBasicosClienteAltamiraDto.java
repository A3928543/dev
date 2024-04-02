package com.ixe.ods.sica.dto;

import java.util.HashMap;
import java.util.Map;


/**
 * DTO para los datos 
 * 	Basicos del cliente Altamira.
 *
 * @author Diego Pazaran
 *              Banorte.
 *
 * @version $Revision: 1.1.2.3 $
 */
public class DatosBasicosClienteAltamiraDto {
    /** Gobierno Federal */
    private static final String PJ_GOB_FEDERAL = "111";

    /** Gobierno del DF */
    private static final String PJ_GDF = "112";

    /** Gobiernos estatales y municipales  */
    private static final String PJ_GOB_EST_MUN = "113";

    /** Org desc. y emp. de part. est */
    private static final String PJ_ORG_DESC_EMP = "114";

    /** Banco de México */
    private static final String PJ_BANXICO = "121";

    /** Banca de desarrollo */
    private static final String PJ_BANCA_ESARROLLO = "122";

    /** Banca Multiple */
    private static final String PJ_BANCA_MULT = "123";

    //Otras entidades financieraws publicas */
    //private static final String PJ_ENT_FIN_PUB = "123";

    /** Otras entidades financieraws privadas */
    private static final String PJ_ENT_FIN_PRIV = "212";

    /**  Empresas */
    private static final String PJ_EMPRESAS = "220";

    /** Instituciones financieras del extranjero */
    private static final String PJ_INST_FIN_EXT = "230";

    /** Emp no financieras y part. ext */
    private static final String PJ_EMP_NO_FIN = "240";

    /** Persona Física sin actividad empresarial */
    private static final String PJ_PF_SIN_ACT_EMP = "251";

    /** Persona Física con actividad empresarial */
    private static final String PJ_PF_CON_ACT_EMP = "252";

    /** Persona Física no residente en el pais */
    private static final String PJ_PF_NO_RES_MEX = "260";

    /** Persona Fisica */
    public static final String TIPO_PF = "PF";

    /** Persona Fisica con Actividad Empresarial */
    public static final String TIPO_AE = "AE";

    /** Persona Moral */
    public static final String TIPO_PM = "PM";

    /** Apellido Paterno * */
    private String paterno;

    /** Apellido Materno* */
    private String materno;

    /** Nombre * */
    private String nombre;

    /** Razon Social* */
    private String razonSocial;

    /** RFC * */
    private String rfc;

    /** Tipo de Persona * */
    private String tipoPersona;

    /** Personalidad Juridica Altamira **/
    private String perJur;

    /** Mapa de tipos de persona * */
    private Map tipoPersonaMap;

    /**
     * Creates a new DatosBasicosClienteAltamiraDto object.
     */
    public DatosBasicosClienteAltamiraDto() {
        tipoPersonaMap = new HashMap();
        tipoPersonaMap.put(PJ_GOB_FEDERAL, TIPO_PM);
        tipoPersonaMap.put(PJ_GDF, TIPO_PM);
        tipoPersonaMap.put(PJ_GOB_EST_MUN, TIPO_PM);
        tipoPersonaMap.put(PJ_ORG_DESC_EMP, TIPO_PM);
        tipoPersonaMap.put(PJ_BANXICO, TIPO_PM);
        tipoPersonaMap.put(PJ_BANCA_ESARROLLO, TIPO_PM);
        tipoPersonaMap.put(PJ_BANCA_MULT, TIPO_PM);

        //tipoPersonaMap.put(PJ_ENT_FIN_PUB, TIPO_PM);
        tipoPersonaMap.put(PJ_ENT_FIN_PRIV, TIPO_PM);
        tipoPersonaMap.put(PJ_EMPRESAS, TIPO_PM);
        tipoPersonaMap.put(PJ_INST_FIN_EXT, TIPO_PM);
        tipoPersonaMap.put(PJ_EMP_NO_FIN, TIPO_PM);
        tipoPersonaMap.put(PJ_PF_SIN_ACT_EMP, TIPO_PF);
        tipoPersonaMap.put(PJ_PF_CON_ACT_EMP, TIPO_AE);
        tipoPersonaMap.put(PJ_PF_NO_RES_MEX, TIPO_PF);
    }

    /**
     * Regresa Apellido paterno
     *
     * @return <code>String</code> con
     *  Apellido Paterno.
     */
    public String getPaterno() {
        return paterno;
    }

    /**
     * Asigna Apellido Paterno
     *
     * @param paterno con el valor
     * 		a asignar.
     */
    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    /**
     * Regresa apellido materno
     *
     * @return <code>String</code> con
     * 	apellido Materno.
     */
    public String getMaterno() {
        return materno;
    }

    /**
     * Asigna Apellido Materno	
     *
     * @param materno con el valor
     * 	a asignar.
     */
    public void setMaterno(String materno) {
        this.materno = materno;
    }

    /**
     * Regresa el nombre
     *
     * @return <code>String</code>
     * 		con el nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre
     *
     * @param nombre con el valor
     * 	a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Recupera la razón social.
     *
     * @return <code>String</code>
     * 	con la razon social.
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * Asigna la razon social
     *
     * @param razonSocial con el valor
     * 		a asignar.
     */
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    /**
     * Regresa el RFC
     *
     * @return <code>String</code> con
     * 	 el rfc.
     */
    public String getRfc() {
        return rfc;
    }

    /**
     * Asigna el RFC
     *
     * @param rfc con el valor
     *  	 a asignar.
     */
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    /**
     * Regresa el tipo de persona PF, PM o AE
     * 	 de acuerdo al mapa de personas
     * 	 juridicas en altamira.
     *
     * @return <code>String</code> con el 
     * 	tipo de persona.
     */
    public String getTipoPersona() {
        this.tipoPersona = (String) tipoPersonaMap.get(getPerJur());

        return tipoPersona;
    }

    /**
     * Regresa la persona juridica
     * @return the perJur
     */
    public String getPerJur() {
        return perJur;
    }

    /**
     * @param perJur the perJur to set
     */
    public void setPerJur(String perJur) {
        this.perJur = perJur;
    }

    /**
     * Sobreescribe el metodo toString.
     *
     * @return <code>String</code> con el 
     * 	valor de las propiedades del DTO.
     * 	
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("\nNombre: ");
        sb.append(this.getNombre());
        sb.append("\n");

        sb.append("Razon Social: ");
        sb.append(this.getRazonSocial());
        sb.append("\n");

        sb.append("Paterno: ");
        sb.append(this.getPaterno());
        sb.append("\n");

        sb.append("Materno: ");
        sb.append(this.getMaterno());
        sb.append("\n");

        sb.append("RFC: ");
        sb.append(this.getRfc());
        sb.append("\n");

        sb.append("Tipo Persona: ");
        sb.append(this.getTipoPersona());
        sb.append("\n");
        
        sb.append("PerJur: ");
        sb.append(this.getPerJur());

        return sb.toString();
    }
}
