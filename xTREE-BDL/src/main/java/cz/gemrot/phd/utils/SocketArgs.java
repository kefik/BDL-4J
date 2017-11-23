package cz.gemrot.phd.utils;

import cz.gemrot.phd.utils.sockets.ConfigMap;
import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.points.BDLLink;
import cz.gemrot.phd.xtree.bdl.points.BDLPoint;
import cz.gemrot.phd.xtree.bdl.primitives.sensors.ICondition;

public class SocketArgs<AGENT extends IBDLAgent> extends ConfigMap {
	
	/**
	 * Auto-generated.
	 */
	private static final long serialVersionUID = 5602562894708845278L;
	
	private static ICondition CONDITION_FALSE = new ICondition() {
		@Override
		public boolean evaluate(IBDLAgent agent) {
			return false;
		}
	};
	
	public void set(String name, BDLPoint point) {
		put(name, new BDLLink(point));
	}
	
	public BDLLink<AGENT> getLink(String name) {
		return (BDLLink<AGENT>) get(name);
	}
	
	public ICondition<AGENT> getCondition(String name) {
		return getCondition(name, CONDITION_FALSE);
	}
	
	public ICondition<AGENT> getCondition(String name, ICondition<AGENT> defaultValue) {
		ICondition<AGENT> condition = (ICondition<AGENT>) get(name);
		if (condition == null) {
			return defaultValue;
		}
		return condition;
	}

}
