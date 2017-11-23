package cz.gemrot.phd.xtree.bdl.agent;

import cz.gemrot.phd.xtree.generic.agent.IAgent;
import cz.gemrot.phd.xtree.generic.agent.IBodyActions;
import cz.gemrot.phd.xtree.generic.agent.IDetermination;
import cz.gemrot.phd.xtree.generic.agent.IMemory;
import cz.gemrot.phd.xtree.generic.agent.IPercepts;
import cz.gemrot.phd.xtree.generic.agent.IReasoningActions;

public interface IBDLAgent<PERCEPTS extends IPercepts, MEMORY extends IMemory, DETERMINATION extends IDetermination, BODY extends IBodyActions, REASONING extends IReasoningActions> extends IAgent<PERCEPTS, MEMORY, DETERMINATION, BODY, REASONING> {

}
