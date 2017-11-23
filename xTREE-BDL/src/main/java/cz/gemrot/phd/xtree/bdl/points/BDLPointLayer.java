package cz.gemrot.phd.xtree.bdl.points;

import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignal;

public abstract class BDLPointLayer<AGENT extends IBDLAgent> extends BDLPoint<AGENT> {

	/**
	 * Standard constructor.
	 * @param source
	 */
	protected BDLPointLayer() {		
	}
	
	/**
	 * Copy-constructor.
	 * @param source
	 */
	protected BDLPointLayer(BDLPointLayer source) {		
	}
	
	@Override
	public abstract SelectResult<AGENT> select(BDLPoint<AGENT> caller, BDLxSignal signal, AGENT agent);
	
	@Override
	public BDLPointLayer<AGENT> clone() {
		try {
			return this.getClass().getConstructor(this.getClass()).newInstance(this);
		} catch (Exception e) {
			throw new RuntimeException("BDL point layer of class " + this.getClass() + " does not declare COPY-CONSTRUCTOR, i.e., " + this.getClass().getSimpleName() + "(" + this.getClass().getSimpleName() + " source).", e);
		}
	}
	
	public void reset() {
	}
	
}
