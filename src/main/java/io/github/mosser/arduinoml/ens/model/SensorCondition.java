package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitor;

public class SensorCondition extends Condition {


    private Sensor sensor;
    private SIGNAL expectedValue;

    public SensorCondition() {
    }

    public SensorCondition(Sensor sensor, SIGNAL expectedValue) {
        this.sensor = sensor;
        this.expectedValue = expectedValue;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public SIGNAL getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(SIGNAL expectedValue) {
        this.expectedValue = expectedValue;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
