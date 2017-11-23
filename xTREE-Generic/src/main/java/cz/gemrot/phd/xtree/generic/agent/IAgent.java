package cz.gemrot.phd.xtree.generic.agent;

import java.util.logging.Logger;

/**
 * Interface to the agent; agent is thought to have following components:
 * <ol>
 *   <li>PERCEPTS      - that provides access to the current sensory information</li>
 *   <li>MEMORY        - that provides access to agent's memory that stores information between xTree evaluation</li>
 *   <li>DETERMINATION - that provides access to the state of xTree decision-making
 *   <li>BODY          - an interface for execution of body actions</li>
 *   <li>REASONING     - an interface for execution of mind actions and computations</li>
 * </ol>
 * 
 * @author Jakub Gemrot
 *
 * @param <PERCEPTS>
 * @param <MEMORY>
 * @param <BODY>
 * @param <REASONING>
 */
public interface IAgent<PERCEPTS extends IPercepts, MEMORY extends IMemory, DETERMINATION extends IDetermination, BODY extends IBodyActions, REASONING extends IReasoningActions> {
	
	public PERCEPTS getPercepts();
	public MEMORY getMemory();
	public DETERMINATION getDetermination();
	public BODY getBody();
	public REASONING getReasoning();
	
	public Logger getLog();
	
}
