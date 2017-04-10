package ee.ttu.idu0080.raamatupood.types;


import java.io.Serializable;
import java.util.List;

public class Tellimus implements Serializable {
	private static final long serialVersionUID = 1L;
	public List<Toode> tooted;
	

	public Tellimus(List<Toode> tooted) {
		this.tooted = tooted;
	}

	public String toString() {
		String stringiks = "";
		for(Toode toode : tooted){
			stringiks = stringiks + toode.toString() + "\n";
		}
		return stringiks;
	}
}