/*
 * $Id: NumReferencia.java,v 1.3 2008/02/22 18:25:50 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

/**
 * Stored Procedure en Java que sirve para calcular el n&uacute;mero de referencia de acuerdo.
 *
 * @author Israel Hernandez
 * @version $Revision: 1.3 $ $Date: 2008/02/22 18:25:50 $
 */
 public class NumReferencia {

    /**
     * Define el tipo de operacion suma.
     */
    private static final int SUMA = 1;

    /**
     * Define el tipo de operacion izquierda.
     */
    private static final int IZQUIERDA = 2;

    /**
     * Define el tipo de operacion derecha.
     */
    private static final int DERECHA = 3;

    /**
     * Define el modulo 10 para calculo del digito verificador.
     */
    private static final int MODULO_10 = 10;

    /**
     * Define el modulo 11 para calculo del digito verificador.
     */
    private static final int MODULO_11 = 11;

    /**
     * Define el modulo 97 para calculo del digito verificador.
     */
    private static final int MODULO_97 = 97;

    /**
     * Constructor por default, no hace nada.
     */
    public NumReferencia() {
        super();
    }

    /**
     * Genera referencia persona.
     *
     * @param idPersona the id persona
     * @return the string
     */
    public static String generaReferenciaPersona(Integer idPersona) {
        String cveReferencia = null;

        try {
            String peso = "9";
            Integer alineacion = new Integer(1);
            Integer operIni = new Integer(1);
            Integer operFin = new Integer(1);
            Integer modulo = new Integer(10);
            Integer digitoResta = new Integer(5);
            String strIdPersona = idPersona.toString();
            String digito = calculaDigitoVerificador(strIdPersona, peso, alineacion, operIni,
                    operFin, modulo, digitoResta);
            if (digito != null) {
                cveReferencia = strIdPersona + digito;
                if (cveReferencia.length() < 10) {
                    while (cveReferencia.length() < 10) {
                        cveReferencia = "0" + cveReferencia; //Rellenar de ceros.
                    }
                }
            }
        }
        catch (NumberFormatException ne) {
            ne.printStackTrace();
        }
        return cveReferencia;
    }

    /**
     * Calcula digito verificador.
     *
     * @param idPersona   the id persona
     * @param peso        the peso
     * @param alineacion  the alineacion
     * @param operInicial the oper inicial
     * @param operFinal   the oper final
     * @param moduloSelec the modulo selec
     * @param digitoResta the digito resta
     * @return the string
     */
    private static String calculaDigitoVerificador(String idPersona, String peso, Integer alineacion,
                                                   Integer operInicial, Integer operFinal,
                                                   Integer moduloSelec, Integer digitoResta) {
        String digito = null;
        int longPeso = peso.length();
        int longContrato = idPersona.length();
        int j = 0;
        String pesoRep = "";
        int sumatoria = 0;
        int sumaPesoCont = 0;
        int pesoIndex = 0;
        int contratoIndex = 0;
        int modulo = 0;

        String numAux = "";

        try {
            pesoRep = llenaPesoLogitudContrato(idPersona, peso, alineacion);
            if (longPeso <= longContrato) {
                for (int i = 0; i < longContrato; i++) {
                    numAux = idPersona.charAt(i) + ""; //contrato
                    contratoIndex = Integer.parseInt(numAux);
                    numAux = pesoRep.charAt(i) + ""; // peso
                    pesoIndex = Integer.parseInt(numAux);
                    if (operInicial.intValue() == 1) {//Suma
                        sumaPesoCont = contratoIndex + pesoIndex;
                    }
                    else {//Multiplicacion
                        sumaPesoCont = contratoIndex * pesoIndex;
                    }

                    if (sumaPesoCont > 9) {
                        sumaPesoCont = recursivoDigitos(sumaPesoCont + "", operFinal);
                    }
                    sumatoria += sumaPesoCont;
                }
                switch (moduloSelec.intValue()) {
                    case MODULO_10:
                        modulo = sumatoria % MODULO_10;
                        break;
                    case MODULO_11:
                        modulo = sumatoria % MODULO_11;
                        break;
                    case MODULO_97:
                        modulo = sumatoria % MODULO_97;
                        break;
                }

                if (modulo > 9) {
                    modulo = recursivoDigitos(modulo + "", operFinal);
                }
                modulo = modulo - digitoResta.intValue();
                //si en la resta se obtine numero negativo se le aplica el valor absoluto.
                modulo = Math.abs(modulo);
                //para tomar siempre el digito de la derecha como digito verificador.
                modulo = modulo % 10;
                digito = modulo + "";
            }

        }
        catch (NumberFormatException ne) {
            ne.printStackTrace();
        }
        return digito;
    }

    /**
     * Llena peso logitud contrato.
     *
     * @param contrato   the contrato
     * @param peso       the peso
     * @param alineacion the alineacion
     * @return the string
     */
    private static String llenaPesoLogitudContrato(String contrato, String peso, Integer alineacion) {
        int longPeso = peso.length();
        int longContrato = contrato.length();
        int j = 0;
        String cadenaPeso = "";
        for (int i = 0; i < longContrato; i++) {
            if (j >= longPeso) {
                j = 0;
            }
            if (alineacion.intValue() == 1) {
                cadenaPeso = cadenaPeso + peso.charAt(j);
            }
            else {
                cadenaPeso = peso.charAt(j) + cadenaPeso;
            }
            j = j + 1;
        }
        return cadenaPeso;
    }

    /**
     * Recursivo digitos.
     *
     * @param valor     the valor
     * @param operFinal the oper final
     * @return the int
     */
    private static int recursivoDigitos(String valor, Integer operFinal) {
        if (valor.length() == 1) {
            return Integer.parseInt(valor);
        }
        else {
            int dig1 = Integer.parseInt(valor.charAt(0) + "");
            int dig2 = Integer.parseInt(valor.charAt(1) + "");
            int suma = 0;
            switch (operFinal.intValue()) {
                case SUMA://Se suman
                    suma = dig1 + dig2;
                    break;
                case IZQUIERDA://Se toma el valor de la izquierda
                    suma = dig1;
                    break;
                case DERECHA://Se toma el valor de la derecha
                    suma = dig2;
                    break;
            }
            return recursivoDigitos(suma + "", operFinal);
        }
    }

    public static void main(String[] args) {
        System.out.println(NumReferencia.generaReferenciaPersona(new Integer(0)));
    }
}