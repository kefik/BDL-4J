package cz.gemrot.phd.xtree.bdl.primitives.sensors;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import cz.gemrot.phd.utils.Convert;
import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;

public class SpelCondition<AGENT extends IBDLAgent> implements ICondition<AGENT> {

	private static ExpressionParser parser;
	
	private static ExpressionParser getParser() {
		if (parser == null) {
			parser = new SpelExpressionParser();
		}
		return parser;		
	}

	private String condition;
	
	private Expression expression;
	
	public SpelCondition(String condition) {
		this.condition = condition;
		this.expression = getParser().parseExpression(condition);
	}
	
	@Override
	public boolean evaluate(AGENT agent) {		
		EvaluationContext context = new StandardEvaluationContext(agent.getReasoning());
		
		Object value = this.expression.getValue(context);
				
		if (agent.getLog() != null) agent.getLog().info("Sensor: " + condition + " => " + value);
		
		return Convert.toBoolean(value);
	}
	
	@Override
	public String toString() {
		return "SpelCondition[" + condition + "]";
	}

}
