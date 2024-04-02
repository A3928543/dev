package com.ixe.ods.sica.model;


// default package
import java.util.Date;


/**
 * Clase persistente para la tabla SC_CORTE_DETALLE.
 *
 * @hibernate.class table="SC_CORTE_DETALLE"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.CorteDetalle"
 * dynamic-update="true"
 *
 * @hibernate.query name="findCorteDetalleById" query="FROM CorteDetalle as c where c.idCorteDetalle = ?"
 * @hibernate.query name="findDetallesCortebyIdCorte" query="FROM CorteDetalle as d where d.idCorte = ?"
 * @hibernate.query name="findDetallesCortebyfechaAndDivisa" query="FROM CorteDetalle as d where to_char(trade_date, 'dd/MM/yyyy') = ? and  d.ccy = ?"
 * @hibernate.query name="findDetallesCortebyfecha" query="FROM CorteDetalle as d where to_char(trade_date, 'dd/MM/yyyy') = ? "
 * @author Banorte
 *         Diego Pazarán
 *
 * @version $Revision: 1.1.2.2.2.1.12.1 $
 */
public class CorteDetalle implements java.io.Serializable {
    // Fields    

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Identificador del Corte * */
    private int idCorteDetalle;

    /** Corte * */
    private int idCorte;

    /** Tipo de operación en MUREX. * */
    private String type1;

    /** DAcción a realizar en MUREX. * */
    private String action;

    /** Identificador unico de la operación, SC + ID_CORTE_DETALLE. * */
    //private String tradeGlobalId;

    /** Sistema que envía información.  * */
    private String sourceModule;

    /** Fecha de pactación de la operación. * */
    private Date tradeDate;

    /** Entidad en MUREX.* */
    private String ourName;

    /** Contraparte.  * */
    private String theirName;

    /** Portafolio origen a afectar, siempre será FX-SICA* */
    private String ourPortfolio;

    /** Portafolio secundario a afectar* */
    private String theirPortfolio;

    /** Usuario con la que se registra la operación en MUREX * */
    private String user1;

    /** Grupo al que pertenece el usuario * */
    private String group1;

    /**Indica si es compra o venta * */
    private String buySell;

    /** Par de divisas en formato XXX/YYY * */
    private String contract;

    /** Etiqueta Fecha Valor * */
    private Date deliveryDateLabel;

    /** Fecha valor de la operación.* */
    private Date deliveryDate;

    /** Monto * */
    private double amount;

    /** Tipo de cambio cliente  * */
    private double price;

    /** FUNDING_COST * */
    private double fundingCost;

    /** Divisa * */
    private String ccy;

    /** Estrategia contable de la operación  * */
    private String acountingSectionSource;

    /** Portafolio destino a afectar.  * */
    private String backToBackPortfolioName;

    /** comentarios * */
    private String sourceComments;

    // Constructors

    /** default constructor */
    public CorteDetalle() {
    }

    /** full constructor */
    public CorteDetalle(int idCorteDetalle, int idCorte, String type1, String action,
        String sourceModule, Date tradeDate, String ourName,
        String theirName, String ourPortfolio, String theirPortfolio, String user1, String group1,
        String buySell, String contract, Date deliveryDateLabel, Date deliveryDate, double amount,
        double price, String ccy, String acountingSectionSource, String backToBackPortfolioName,
        String sourceComments) {
        this.idCorteDetalle = idCorteDetalle;
        this.idCorte = idCorte;
        this.type1 = type1;
        this.action = action;
        this.sourceModule = sourceModule;
        this.tradeDate = tradeDate;
        this.ourName = ourName;
        this.theirName = theirName;
        this.ourPortfolio = ourPortfolio;
        this.theirPortfolio = theirPortfolio;
        this.user1 = user1;
        this.group1 = group1;
        this.buySell = buySell;
        this.contract = contract;
        this.deliveryDateLabel = deliveryDateLabel;
        this.deliveryDate = deliveryDate;
        this.amount = amount;
        this.price = price;
        this.ccy = ccy;
        this.acountingSectionSource = acountingSectionSource;
        this.backToBackPortfolioName = backToBackPortfolioName;
        this.sourceComments = sourceComments;
    }

    // Property accessors

    /**
     * Regresa el valor de idCorteDetalle
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_CORTE_DETALLE"
     * unsaved-value="null"
     *
     * @hibernate.generator-param name="sequence"
     * value="SC_CORTE_DETALLE_SEQ"
     * @return int.
     */
    public int getIdCorteDetalle() {
        return this.idCorteDetalle;
    }

    /**
     * Asigna idCorteDetalle
     *
     * @param idCorteDetalle con el valor
     * 		a asignar.
     */
    public void setIdCorteDetalle(int idCorteDetalle) {
        this.idCorteDetalle = idCorteDetalle;
    }

    /**
     * Regresa el idCorte
     *
     * @return Corte.
     * @hibernate.property column="ID_CORTE"
     */
    public int getIdCorte() {
        return this.idCorte;
    }

    /**
     * Asigna idCorte
     *
     * @param scCorte con el valor
     * 		a asignar.
     */
    public void setIdCorte(int idCorte) {
        this.idCorte = idCorte;
    }

    /**
     * Regresa el valor de type.
     *
     * @return String.
     *
     * @hibernate.property column="TYPE_1"
     * not-null="true"
     * unique="false"
     */
    public String getType1() {
        return this.type1;
    }

    /**
     * Asigna type1
     *
     * @param type1 con el valor 
     * 		a asignar.
     */
    public void setType1(String type1) {
        this.type1 = type1;
    }

    /**
     * Regresa el valor de action.
     *
     * @return String.
     *
     * @hibernate.property column="ACTION"
     * not-null="true"
     * unique="false"
     */
    public String getAction() {
        return this.action;
    }

    /**
     * Asigna action
     *
     * @param action con el valor a asignar. 
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Regresa el valor de tradeGlobalId.
     *
     * @return String con SC + ID_CORTE_DETALLE.
     */
    public String getTradeGlobalId() {
    	
    	if(getIdCorteDetalle() > 0) {
    		
    		return "SICA" + getIdCorteDetalle();
    	}
    	else return null;
    }

    

    /**
     * Regresa el valor de sourceModule.
     *
     * @return String.
     *
     * @hibernate.property column="SOURCE_MODULE"
     * not-null="true"
     * unique="false"
     */
    public String getSourceModule() {
        return this.sourceModule;
    }

    /**
     * Asigna sourceModule
     *
     * @param sourceModule con el valor a asignar
     */
    public void setSourceModule(String sourceModule) {
        this.sourceModule = sourceModule;
    }

    /**
     * Regresa el valor de tradeDate.
     *
     * @return Date.
     *
     * @hibernate.property column="TRADE_DATE"
     * not-null="true"
     * unique="false"
     */
    public Date getTradeDate() {
        return this.tradeDate;
    }

    /**
     * Asigna tradeDate
     *
     * @param tradeDate con el valor a asignar.
     */
    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    /**
     * Regresa el valor de ourName.
     *
     * @return String.
     *
     * @hibernate.property column="OUR_NAME"
     * not-null="true"
     * unique="false"
     */
    public String getOurName() {
        return this.ourName;
    }

    /**
     * Asigna ourName
     *
     * @param ourName con el valor a Asignar.
     */
    public void setOurName(String ourName) {
        this.ourName = ourName;
    }

    /**
     * Regresa el valor de theirName.
     *
     * @return String.
     *
     * @hibernate.property column="THEIR_NAME"
     * not-null="true"
     * unique="false"
     */
    public String getTheirName() {
        return this.theirName;
    }

    /**
     * Asignra theirName
     *
     * @param theirName con el valor a Asignar.
     */
    public void setTheirName(String theirName) {
        this.theirName = theirName;
    }

    /**
     * Regresa el valor de ourPortfolio.
     *
     * @return String.
     *
     * @hibernate.property column="OUR_PORTFOLIO"
     * not-null="true"
     * unique="false"
     */
    public String getOurPortfolio() {
        return this.ourPortfolio;
    }

    /**
     * Asigna ourPortfolio
     *
     * @param ourPortfolio con el valor a asignar.
     */
    public void setOurPortfolio(String ourPortfolio) {
        this.ourPortfolio = ourPortfolio;
    }

    /**
     * Regresa el valor de theirPortfolio.
     *
     * @return String.
     *
     * @hibernate.property column="THEIR_PORTFOLIO"
     * not-null="false"
     * unique="false"
     */
    public String getTheirPortfolio() {
        return this.theirPortfolio;
    }

    /**
     * Regresa theirPortfolio
     *
     * @param theirPortfolio con el valor
     * 		a asignar.
     */
    public void setTheirPortfolio(String theirPortfolio) {
        this.theirPortfolio = theirPortfolio;
    }

    /**
     * Regresa el valor de user1.
     *
     * @return String.
     *
     * @hibernate.property column="USER_1"
     * not-null="true"
     * unique="false"
     */
    public String getUser1() {
        return this.user1;
    }

    /**
     * Asigna user1
     *
     * @param user1 con el valor
     * 		a asignar.
     */
    public void setUser1(String user1) {
        this.user1 = user1;
    }

    /**
     * Regresa el valor de group1.
     *
     * @return String.
     *
     * @hibernate.property column="GROUP_1"
     * not-null="true"
     * unique="false"
     */
    public String getGroup1() {
        return this.group1;
    }

    /**
     * Asigna group1
     *
     * @param group1 con el valor a asignar.
     */
    public void setGroup1(String group1) {
        this.group1 = group1;
    }

    /**
     * Regresa el valor de buySell.
     *
     * @return String.
     *
     * @hibernate.property column="BUY_SELL"
     * not-null="true"
     * unique="false"
     */
    public String getBuySell() {
        return this.buySell;
    }

    /**
     * Asigna buySell
     *
     * @param buySell con el valor a asignar.
     */
    public void setBuySell(String buySell) {
        this.buySell = buySell;
    }

    /**
     * Regresa el valor de contract.
     *
     * @return String.
     *
     * @hibernate.property column="CONTRACT"
     * not-null="true"
     * unique="false"
     */
    public String getContract() {
        return this.contract;
    }

    /**
     * Asigna contract
     *
     * @param contract con el valor a asignar.
     */
    public void setContract(String contract) {
        this.contract = contract;
    }

    /**
     * Regresa el valor de deliveryDateLabel.
     *
     * @return Date.
     *
     * @hibernate.property column="DELIVERY_DATE_LABEL"
     * not-null="false"
     * unique="false"
     */
    public Date getDeliveryDateLabel() {
        return this.deliveryDateLabel;
    }

    /**
     * Asigna deliveryDateLabel
     *
     * @param deliveryDateLabel con el valor
     * 		a asignar.
     */
    public void setDeliveryDateLabel(Date deliveryDateLabel) {
        this.deliveryDateLabel = deliveryDateLabel;
    }

    /**
     * Regresa el valor de deliveryDate.
     *
     * @return Date.
     *
     * @hibernate.property column="DELIVERY_DATE"
     * not-null="true"
     * unique="false"
     */
    public Date getDeliveryDate() {
        return this.deliveryDate;
    }

    /**
     * Asigna deliveryDate
     *
     * @param deliveryDate con el valor
     * 		a asignar.
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * Regresa el valor de amount.
     *
     * @return double.
     *
     * @hibernate.property column="AMOUNT"
     * not-null="true"
     * unique="false"
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * Asigna amount
     *
     * @param amount con el valor
     * 		a asignar.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Regresa el valor de price.
     *
     * @return double.
     *
     * @hibernate.property column="PRICE"
     * not-null="true"
     * unique="false"
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * Asigna price
     *
     * @param price con el valor
     * 		a asignar.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Regresa el valor de fundingCost.
     *
     * @return double.
     *
     * @hibernate.property column="FUNDING_COST"
     * not-null="true"
     * unique="false"
     */
    public double getFundingCost() {
        return fundingCost;
    }

    /**
     * Asigna fundingCost
     *
     * @param fundingCost con el valor 
     * 		a asignar.
     */
    public void setFundingCost(double fundingCost) {
        this.fundingCost = fundingCost;
    }

    /**
     * Regresa la Divisa.
     *
     * @return String.
     *
     * @hibernate.property column="CCY"
     * not-null="true"
     * unique="false"
     */
    public String getCcy() {
        return this.ccy;
    }

    /**
     * Asigna ccy
     *
     * @param ccy DOCUMENT ME!
     */
    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    /**
     * Regresa el valor de acountingSectionSource.
     *
     * @return String.
     *
     * @hibernate.property column="ACOUNTING_SECTION_SOURCE"
     * not-null="true"
     * unique="false"
     */
    public String getAcountingSectionSource() {
        return this.acountingSectionSource;
    }

    /**
     * Asigna acountingSectionSource
     *
     * @param acountingSectionSource con el valor
     * 		a asignar.
     */
    public void setAcountingSectionSource(String acountingSectionSource) {
        this.acountingSectionSource = acountingSectionSource;
    }

    /**
     * Regresa el valor de backToBackPortfolioName.
     *
     * @return String.
     *
     * @hibernate.property column="BACK_TO_BACK_PORTFOLIO_NAME"
     * not-null="true"
     * unique="false"
     */
    public String getBackToBackPortfolioName() {
        return this.backToBackPortfolioName;
    }

    /**
     * Asigna backToBackPortfolioName
     *
     * @param backToBackPortfolioName con el valor
     * 		a asignar.
     */
    public void setBackToBackPortfolioName(String backToBackPortfolioName) {
        this.backToBackPortfolioName = backToBackPortfolioName;
    }

    /**
     * Regresa el valor de sourceComments.
     *
     * @return String.
     *
     * @hibernate.property column="SOURCE_COMMENTS"
     * not-null="true"
     * unique="false"
     */
    public String getSourceComments() {
        return this.sourceComments;
    }

    /**
     * Asigna sourceComments
     *
     * @param sourceComments con el valor a asignar.
     */
    public void setSourceComments(String sourceComments) {
        this.sourceComments = sourceComments;
    }
}
