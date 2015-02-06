package ai;

import de.szut.pongsim.physics.PadMovement;
import de.szut.pongsim.physics.Point;

/**
 * Interface, die jede KI implementiert und Schnittstellenmethoden zwischen KI und Model enthält
 * @author Alexander
 *
 */
public interface User {
	
	/**
	 * Informiert die KI, dass sich diese auf der linken Seite des Spielfeldes befindet.
	 */
	public void setLeftSide();
	
	/**
	 * Informiert die KI, dass sich diese auf der rechten Seite des Spielfeldes befindet.
	 */
	public void setRightSide();
	
	/**
	 * Schnittstelle zwischen AILoader und Model
	 * @param ownPadBottomY unterste Koordinate des eigenen Pads
	 * @param enemyPadBottomY unterste Koordinate des gegnerischen Pads
	 * @return Die nächste Padbewegung (oben, unten oder stop)
	 */
	public PadMovement nextStep(int ownPadBottomY, int enemyPadBottomY);
	
	/**
	 * Aktualisiert die Ballposition.
	 * @param ballPos
	 */
	public void updateBallPos(Point ballPos);
	
	/**
	 * Signalisiert bei Aufruf, dass die Runde vorbei ist
	 */
	public void reset();
	
}
