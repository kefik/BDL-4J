package cz.gemrot.phd.xtree.bdl.primitives.cores;

import java.util.ArrayList;
import java.util.List;

import cz.gemrot.phd.utils.SocketArgs;
import cz.gemrot.phd.utils.sockets.AutoConfig;
import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.points.BDLLink;
import cz.gemrot.phd.xtree.bdl.points.BDLPoint;
import cz.gemrot.phd.xtree.bdl.points.BDLPointCore;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignal;
import cz.gemrot.phd.xtree.generic.iface.IxTreePoint.SelectResult;

@AutoConfig
public class BDLCoreBody<AGENT extends IBDLAgent> extends BDLPointCore<AGENT> {

	private List<BDLLink<AGENT>> body = new ArrayList<BDLLink<AGENT>>();
	
	private int currIndex = 0;
	
	private BDLxSignal currSignal;
	
	public BDLCoreBody() {
	}
	
	/**
	 * Copy-constructor
	 * @param source
	 */
	public BDLCoreBody(BDLCoreBody source) {
		for (int i = 0; i < source.body.size(); ++i) {
			body.add(((BDLLink)source.body.get(i)).clone());
		}
		reset();
	}
	
	public BDLCoreBody(BDLPoint[] children) {
		for (BDLPoint child : children) {
			body.add(new BDLLink<AGENT>(child));
		}
	}
	
	public BDLCoreBody(List<BDLPoint> children) {
		for (BDLPoint child : children) {
			body.add(new BDLLink<AGENT>(child));
		}
	}
	
	@Override
	public void configure(SocketArgs config) {
		BDLLink<AGENT> link = config.getLink("p0");
		if (link != null) {
			body.add(link);
		}
		int i = 1;
		link = config.getLink("p" + i);
		while (link != null) {
			body.add(link);
			++i;
			link = config.getLink("p" + i); 
		}
	}
	
	@Override
	public void reset() {
		for (BDLLink<AGENT> child : body) {
			child.reset();
		}
	}

	@Override
	public SelectResult<AGENT> selectCore(BDLPoint<AGENT> caller, BDLxSignal callerSignal, BDLPoint<AGENT> currentCaller, BDLxSignal signal, AGENT agent) {
		if (signal.isCall()) {
			if (signal.isSwitch()) {
				currSignal = signal.asSignal(BDLxSignal.SIGNAL_TERMINATE);
			} else {
				currSignal = signal;
			}
			currIndex = 0;
		}
		
		if (signal.isResult()) {
			++currIndex;
		}
		
		if (currIndex >= body.size()) {
			currIndex = 0;
			if (callerSignal.isTerminate()) {
				return terminated(caller);
			} else {
				if (callerSignal.isSwitch()) {
					return success(caller);
				}
				return running(caller);
			}
		}
		
		return call(body.get(currIndex), currSignal);
	}

	@Override
	public SelectResult<AGENT> loopedCore(BDLPoint<AGENT> caller, BDLxSignal callerSignal, BDLPoint<AGENT> currentCaller, BDLxSignal signal, AGENT agent) {
		currIndex = 0;
		return selectCore(caller, callerSignal, currentCaller, signal, agent);
	}
	
	@Override
	public String toString() {
		return "Body[#children=" + body.size() + "]";
	}

}
