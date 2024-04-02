package test.com.ixe.ods.sica;

public interface SicaSession {

	/**
	 *  
	 * @param para
	 * @return
	 * @throws Exception
	 */
	public abstract String iniciarCierreContableYCierreFinal(String[] para) throws Exception ;
	
	/**
	 * 
	 * @param para
	 * @return
	 * @throws Exception
	 */
	 public abstract String iniciarCierreFinal(String[] para) throws Exception ;

}
