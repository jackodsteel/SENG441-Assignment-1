package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitable;
import io.github.mosser.arduinoml.ens.generator.Visitor;

import java.util.ArrayList;
import java.util.List;

public class Transition implements Visitable, NamedElement {

    private String name;
    private List<Condition> conditions;
    private State currentState;
    private State nextState;

    public Transition(String name) {
        this.name = name;
        this.conditions = new ArrayList<>();
    }

    public Transition(String name, List<Condition> conditions, State currentState, State nextState) {
        this.name = name;
        this.conditions = conditions;
        this.currentState = currentState;
        this.nextState = nextState;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public void addCondition(Condition condition) {
        this.conditions.add(condition);
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public State getNextState() {
        return nextState;
    }

    public void setNextState(State nextState) {
        this.nextState = nextState;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
