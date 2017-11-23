package cz.gemrot.phd.xtree.bdl.primitives.actions;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;

public class SpelAction<AGENT extends IBDLAgent> implements IAction<AGENT> {

	private static ExpressionParser parser;
	
	private static ExpressionParser getParser() {
		if (parser == null) {
			parser = new SpelExpressionParser();
		}
		return parser;		
	}

	private String executeCommand;
	private Expression executeExpression;
	
	private String switchCommand;
	private Expression switchExpression;
	
	private String terminateCommand;
	private Expression terminateExpression;
	
	public SpelAction(String command) {
		String[] commands = command.split("\\|");
		if (commands.length >= 1) {
			this.executeCommand = commands[0];
			this.executeExpression = getParser().parseExpression(executeCommand);
		}
		if (commands.length == 2) {
			this.switchCommand = commands[1];
			this.switchExpression = getParser().parseExpression(switchCommand);
			this.terminateCommand = switchCommand;
			this.terminateExpression = switchExpression;
		} else
		if (commands.length >= 3) {
			this.switchCommand = commands[1];
			this.switchExpression = getParser().parseExpression(switchCommand);
			this.terminateCommand = commands[2];
			this.terminateExpression = getParser().parseExpression(terminateCommand);
		}		
	}
	
	public SpelAction(String executeCommand, String switchCommand, String terminateCommand) {
		this.executeCommand = executeCommand;
		this.executeExpression = getParser().parseExpression(executeCommand);
		if (switchCommand != null) {
			this.switchCommand = switchCommand;
			this.switchExpression = getParser().parseExpression(switchCommand);
		}
		if (terminateCommand != null) {
			this.terminateCommand = terminateCommand;
			this.terminateExpression = getParser().parseExpression(terminateCommand);
		}
	}
	
	@Override
	public ActionResult execute(AGENT agent) {
		if (agent.getLog() != null) agent.getLog().info("Action.Execute: " + executeCommand);
		
		EvaluationContext context = new StandardEvaluationContext(agent.getBody());
		executeExpression.getValue(context);
		return ActionResult.RUNNING;
	}

	@Override
	public ActionResult switchOut(AGENT agent) {
		if (switchExpression == null) {
			return ActionResult.SUCCESS;
		}
		
		if (agent.getLog() != null) agent.getLog().info("Action.Switch: " + switchCommand);
		
		EvaluationContext context = new StandardEvaluationContext(agent.getBody());
		switchExpression.getValue(context);
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult terminate(AGENT agent) {
		if (terminateExpression == null) {
			return ActionResult.TERMINATED;
		}
		
		if (agent.getLog() != null) agent.getLog().info("Action.Terminate: " + terminateCommand);
		
		EvaluationContext context = new StandardEvaluationContext(agent.getBody());
		terminateExpression.getValue(context);
		return ActionResult.TERMINATED;
	}
	
	@Override
	public String toString() {
		return "SpelAction[EXEC=" + executeCommand + (switchCommand != null ? "|SWITCH=" + switchCommand : "") + (terminateCommand != null ? "|TERM=" + terminateCommand : "") + "]";
	}

}
