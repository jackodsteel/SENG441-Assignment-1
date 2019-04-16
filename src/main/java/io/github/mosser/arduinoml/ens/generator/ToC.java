package io.github.mosser.arduinoml.ens.generator;

import io.github.mosser.arduinoml.ens.model.Action;
import io.github.mosser.arduinoml.ens.model.Actuator;
import io.github.mosser.arduinoml.ens.model.App;
import io.github.mosser.arduinoml.ens.model.Condition;
import io.github.mosser.arduinoml.ens.model.Sensor;
import io.github.mosser.arduinoml.ens.model.State;
import io.github.mosser.arduinoml.ens.model.Transition;

public class ToC extends Visitor<StringBuffer> {

	public ToC() {
		this.code = new StringBuffer();
		this.headers = new StringBuffer();
	}

	private void c(String s) {
		code.append(String.format("%s\n",s));
	}

	private void h(String s) {
		headers.append(String.format("%s\n",s));
	}

	@Override
	public void visit(App app) {
		c("// C code generated from an object model");
		c(String.format("// Application name: %s\n", app.getName()));
		c("#include <avr/io.h>");
		c("#include <util/delay.h>");
		c("#include <Arduino.h>");
		c("#include <fsm.h>");
		c("");
		c("void setup(){");
		for(Actuator a: app.getActuators()){
			a.accept(this);
		}
        for (Sensor s : app.getSensors()) {
            s.accept(this);
        }
		c("}\n");

		for(State state: app.getStates()){
			h(String.format("void state_%s();", state.getName()));
			state.accept(this);
		}

		if (app.getInitial() != null) {
			c("int main(void) {");
			c("  setup();");
            c(String.format("  long curr_state = %s;", app.getInitial().getName().hashCode()));
			c(String.format("  state_%s();", app.getInitial().getName()));
            c("  while(true) {");
            for (Transition transition : app.getTransitions()) {
                transition.accept(this);
            }
            c("  }");
			c("  return 0;");
			c("}");
		}
	}

	@Override
	public void visit(Actuator actuator) {
	 	c(String.format("  pinMode(%d, OUTPUT); // %s [Actuator]", actuator.getPin(), actuator.getName()));
	}


	@Override
	public void visit(State state) {
		c(String.format("void state_%s() {",state.getName()));
		for(Action action: state.getActions()) {
			action.accept(this);
		}
		c("}");
	}


	@Override
	public void visit(Action action) {
		c(String.format("  digitalWrite(%d,%s);",action.getActuator().getPin(),action.getValue()));
	}

    @Override
    public void visit(Sensor sensor) {
        c(String.format("  pinMode(%d, INPUT); // %s [Sensor]", sensor.getPin(), sensor.getName()));
    }

    @Override
    public void visit(Condition condition) {
        c(String.format("(digitalRead(%s) == %s)", condition.getSensor().getPin(), condition.getExpectedValue()));
    }

    @Override
    public void visit(Transition transition) {
        c(String.format("if (%s == curr_state) {", transition.getCurrentState().getName().hashCode()));
        c("if ( ");
        for (Condition condition : transition.getConditions()) {
            condition.accept(this);
            c(" && ");
        }
        c(" true) { ");
        c(String.format("  curr_state = %s;", transition.getNextState().getName().hashCode()));
        c(String.format("  state_%s();", transition.getNextState().getName()));
        c("  }\n}");
    }

}
