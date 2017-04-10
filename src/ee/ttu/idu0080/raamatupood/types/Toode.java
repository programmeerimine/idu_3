package ee.ttu.idu0080.raamatupood.types;

import java.io.Serializable;

public class Toode implements Serializable {
	private static final long serialVersionUID = 1L;
	public String nimi;
	public int hind;

	public Toode(String nimi, int hind) {
		this.nimi = nimi;
		this.hind = hind;
	}

	public String toString() {
		return "Toode nimega: " + nimi + ", maksab: " + hind + ".";
	}
}