package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitable;
import io.github.mosser.arduinoml.ens.generator.Visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App implements NamedElement, Visitable {

	private String name;
    private List<Actuator> actuators = new ArrayList<>();
    private List<State> states = new ArrayList<>();
    private List<Transition> transitions = new ArrayList<>();
    private List<Sensor> sensors = new ArrayList<>();
    private List<Timer> timers = new ArrayList<>();
	private State initial;

    public App() {
    }

    public App(String name) {
        this.name = name;
    }

    public List<Timer> getTimers() {
        return timers;
    }

    public void addTimer(Timer timer) {
        timers.add(timer);
    }

    @Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public List<Actuator> getActuators() {
		return actuators;
	}

    public void addActuator(Actuator actuator) {
        actuators.add(actuator);
    }

    public void addActuators(Actuator... actuators) {
        Arrays.stream(actuators).forEach(this::addActuator);
    }

	public List<State> getStates() {
		return states;
	}

    public void addState(State state) {
        this.states.add(state);
    }

    public void addStates(State... states) {
        Arrays.stream(states).forEach(this::addState);
    }

	public State getInitial() {
		return initial;
	}

	public void setInitial(State initial) {
		this.initial = initial;
	}

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void addTransition(Transition transition) {
        transitions.add(transition);
    }

    public void addTransitions(Transition... transitions) {
        Arrays.stream(transitions).forEach(this::addTransition);
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
    }

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
