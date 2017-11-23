package cz.cuni.amis.pogamut.ut2004.examples.bdlbot;

import java.io.InputStream;

import cz.cuni.amis.pogamut.base.utils.guice.AgentScoped;
import cz.cuni.amis.pogamut.ut2004.bdl.UT2004BDLBotController;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Initialize;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004BotRunner;
import cz.cuni.amis.utils.exception.PogamutException;

@AgentScoped
public class BDLBot2_HunterBot extends UT2004BDLBotController<UT2004Bot> {

	@Override
	public Initialize getInitializeCommand() {
		return super.getInitializeCommand().setName("HunterBot");
	}
	
	@Override
	public InputStream getBDLPlanXMLStream() {
		return getResourceStream("cz/cuni/amis/pogamut/ut2004/examples/bdlbot/02-HunterBot.xml");
	}
    
    @SuppressWarnings("unchecked")
	public static void main(String args[]) throws PogamutException {
        new UT2004BotRunner(     // class that wraps logic for bots executions, suitable to run single bot in single JVM
        		BDLBot2_HunterBot.class,   // which UT2004BotController it should instantiate
                "BDLBot"         // what name the runner should be using
        ).setMain(true)          // tells runner that is is executed inside MAIN method, thus it may block the thread and watch whether agent/s are correctly executed
         .startAgents(1);        // tells the runner to start 1 agent
    }

	
}
