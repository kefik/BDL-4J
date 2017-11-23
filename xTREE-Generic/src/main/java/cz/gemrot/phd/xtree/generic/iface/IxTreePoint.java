package cz.gemrot.phd.xtree.generic.iface;

import cz.gemrot.phd.xtree.generic.agent.IAgent;

/**
 * Interface to the xTreePoint; it contains single method for its execution.
 * 
 * In contrast to the thesis, xTreePoint executes body and mind actions directly, it is not returning them
 * as the division between Reasoning and Decision-Making is rather theoretical way of thinking about agent
 * action-selection.
 * 
 * @author Jakub Gemrot
 *
 * @param <AGENT>
 */
public interface IxTreePoint<AGENT extends IAgent> {

	/**
	 * xTreePoint get called by 'caller' that is sending 'signal' to it.
	 * @param caller
	 * @param signal
	 * @param agent
	 * @return result of the call containing chosen xTreeOption and signal
	 */
	public SelectResult<AGENT> select(IxTreePoint<AGENT> caller, IxSignal signal, AGENT agent);
	
	public static class SelectResult<AGENT extends IAgent> {
		
		private IxTreeLink<AGENT> link;
		
		private IxSignal signal;

		public SelectResult() {
			
		}
		
		public SelectResult(IxTreeLink<AGENT> link, IxSignal signal) {
			super();
			this.link = link;
			this.signal = signal;
		}

		public IxTreeLink<AGENT> getLink() {
			return link;
		}

		public void setLink(IxTreeLink<AGENT> link) {
			this.link = link;
		}

		public IxSignal getSignal() {
			return signal;
		}

		public void setSignal(IxSignal signal) {
			this.signal = signal;
		}
		
		@Override
		public String toString() {
			return "SelectResult[" + signal + " > " + link + "]";
		}

	}
	
}
