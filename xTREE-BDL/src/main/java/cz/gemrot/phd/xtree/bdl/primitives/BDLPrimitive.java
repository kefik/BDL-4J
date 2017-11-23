package cz.gemrot.phd.xtree.bdl.primitives;

import cz.gemrot.phd.utils.sockets.AutoConfig;
import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.points.BDLPoint;
import cz.gemrot.phd.xtree.bdl.points.BDLPointCore;
import cz.gemrot.phd.xtree.bdl.points.BDLPointEnvelope;
import cz.gemrot.phd.xtree.bdl.points.BDLPointNut;
import cz.gemrot.phd.xtree.bdl.points.BDLPointShell;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignal;

@AutoConfig
public class BDLPrimitive<AGENT extends IBDLAgent> extends BDLPoint<AGENT> {

	BDLPoint<AGENT> point;
	
	public BDLPrimitive(BDLPointCore core) {
		BDLPointNut<AGENT> nut = new BDLPointNut<AGENT>(core);
		BDLPointShell<AGENT> shell = new BDLPointShell<AGENT>(nut);
		BDLPointEnvelope<AGENT> envelope = new BDLPointEnvelope<AGENT>(shell);
		
		this.point = envelope;
	}

	@Override
	public SelectResult<AGENT> select(BDLPoint<AGENT> caller, BDLxSignal signal, AGENT agent) {
		return point.select(caller, signal, agent);
	}
	
	@Override
	public String toString() {
		return "Primitive[point=" + point + "]";
	}
	
}
