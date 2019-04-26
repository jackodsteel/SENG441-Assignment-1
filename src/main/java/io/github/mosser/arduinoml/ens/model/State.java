package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitable;
import io.github.mosser.arduinoml.ens.generator.Visitor;

import java.util.ArrayList;
import java.util.List;

public class State implements NamedElement, Visitable {

    private String name;
    private List<Action> actions = new ArrayList<>();

    public State() {

    }

    public State(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void addActions(Action... actions) {
        for (Action action : actions) {
            addAction(action);
        }
    }

    public State withActions(Action... actions) {
        addActions(actions);
        return this;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
