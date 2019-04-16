package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitor;

public class TimerCondition extends Condition {

    private Timer timer;
    private int triggerValue;

    public TimerCondition(Timer timer, int triggerValue) {
        this.timer = timer;
        this.triggerValue = triggerValue;
    }

    public Timer getTimer() {
        return timer;
    }

    public int getTriggerValue() {
        return triggerValue;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
