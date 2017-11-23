package cz.gemrot.phd.xtree.generic.signals;

import java.util.Map.Entry;

import cz.gemrot.phd.utils.sockets.ConfigMap;
import cz.gemrot.phd.xtree.generic.board.xBoard;
import cz.gemrot.phd.xtree.generic.iface.IxSignal;

/**
 * Base implementation of {@link IxSignal} that is propagated between xTreePoints.
 * 
 * @author Jakub Gemrot
 */
public class xSignal implements IxSignal {
	
	/**
	 * Name of the argument.
	 */
	private String name;
	
	/**
	 * Signal arguments; lazy-initialized within {@link #getArgs()}.
	 */
	private xBoard args;
	
	private boolean done;

	public xSignal(String name, ConfigMap userArgs) {
		this(name, false, userArgs);
	}
	
	public xSignal(String name, boolean isDoneSignal, ConfigMap userArgs) {
		this.name = name;
		if (userArgs != null && userArgs.size() > 0) {
			getArgs().set(args);			
		}
		this.done = isDoneSignal;
	}	
	
	public boolean isSignal(String name) {
		return this.name != null && this.name.equals(name);
	}

	public String getName() {
		return name;
	}

	public boolean hasArgs() {
		return args != null;
	}
	
	public xBoard getArgs() {
		if (args == null) this.args = new xBoard();
		return args;
	}
	
	public void setArgs(xBoard board) {
		args = board;
	}
	
	public boolean isDone() {
		return done;
	}
	
}
