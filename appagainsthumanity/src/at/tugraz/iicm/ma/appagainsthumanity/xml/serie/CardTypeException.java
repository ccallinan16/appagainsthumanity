package at.tugraz.iicm.ma.appagainsthumanity.xml.serie;

public class CardTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CardTypeException(CardType type) {
		super("Unknown Card Type: " + type);
	}

}
