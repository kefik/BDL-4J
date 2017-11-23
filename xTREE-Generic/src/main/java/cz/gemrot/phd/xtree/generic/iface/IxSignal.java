package cz.gemrot.phd.xtree.generic.iface;

import cz.gemrot.phd.xtree.generic.board.xBoard;

/**
 * Signal that is being propagated through xTree.
 * 
 * @author Jakub Gemrot
 */
public interface IxSignal {

	/**
	 * Name of the signal.
	 * @return
	 */
	public String getName();

	/**
	 * Arguments passed along with the signal.
	 * @return
	 */
	public xBoard getArgs();
	
	/**
	 * Whether the Signal finishes {@link xTree} evaluation.
	 * @return
	 */
	public boolean isDone();
	
}
