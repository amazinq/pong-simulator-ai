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
	 * Schnittstelle zwischen AILoader und Model
	 * @param ownPadBottomY unterste Koordinate des eigenen Pads
	 * @param enemyPadBottomY unterste Koordinate des gegnerischen Pads
	 * @param BallPos momentane Ballposition
	 * @param BallSpeed momentane Ballgeschwindigkeit
	 * @param isDefender sagt der KI ob sie verteidigen muss
	 * @return Die nächste Padbewegung (oben, unten oder stop)
	 */
	public PadMovement nextStep(int ownPadBottomY, int enemyPadBottomY, Point ballPos, int ballSpeed, boolean isDefender);
	
	/**
	 * Signalisiert bei Aufruf, dass die Runde vorbei ist
	 */
	public void reset();
	
}
