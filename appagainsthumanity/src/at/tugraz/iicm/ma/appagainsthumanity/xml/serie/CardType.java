package at.tugraz.iicm.ma.appagainsthumanity.xml.serie;

public enum CardType {
	WHITE, BLACK;

	@Override
	public String toString() {
		
		if (this.equals(WHITE))
			return "white";
		if (this.equals(BLACK))
			return "black";
		return super.toString();
	}
}
