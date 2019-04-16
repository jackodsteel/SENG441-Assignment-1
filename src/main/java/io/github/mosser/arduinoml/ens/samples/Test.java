package io.github.mosser.arduinoml.ens.samples;

import io.github.mosser.arduinoml.ens.generator.ToC;
import io.github.mosser.arduinoml.ens.generator.Visitor;
import io.github.mosser.arduinoml.ens.model.*;
import io.github.mosser.arduinoml.ens.util.FileWriter;

import java.util.Arrays;
import java.util.List;

public class
Test {

    public static void main(String[] args) {


        Actuator led = new Actuator();
        led.setName("LED");
        led.setPin(13);

        // Declaring states
        State on = new State("on");
        State off = new State("off");
        State freshlyOn = new State("freshly_on");
        State freshlyOff = new State("freshly_off");

        // Creating actions
        Action switchTheLightOn = new Action();
        switchTheLightOn.setActuator(led);
        switchTheLightOn.setValue(SIGNAL.HIGH);

        Action switchTheLightOff = new Action();
        switchTheLightOff.setActuator(led);
        switchTheLightOff.setValue(SIGNAL.LOW);

        // Binding actions to states
        freshlyOn.addAction(switchTheLightOn);
        freshlyOff.addAction(switchTheLightOff);

        // Binding transitions to states
        Sensor buttonSensor = new Sensor(10, "button");
        Condition buttonPressed = new SensorCondition(buttonSensor, SIGNAL.HIGH);
        Condition buttonNotPressed = new SensorCondition(buttonSensor, SIGNAL.LOW);


        Timer timer = new Timer("timer");
        Condition oneSecond = new TimerCondition(timer, 1000);


        Transition onTrans = new Transition("onTrans", List.of(buttonPressed), off, freshlyOn);
        Transition offTrans = new Transition("offTrans", List.of(buttonPressed), on, freshlyOff);
        Transition buttonUpOnTrans = new Transition("buttonUpOnTrans", List.of(buttonNotPressed), freshlyOff, off);
        Transition buttonUpOffTrans = new Transition("buttonUpOffTrans", List.of(buttonNotPressed), freshlyOn, on);
        Transition oneSecondOff = new Transition("oneSecondOff", List.of(oneSecond), on, freshlyOff);

        // Building the App
        App theSwitch = new App();
        theSwitch.setName("Led!");
        theSwitch.setBricks(Arrays.asList(led));
        theSwitch.addStates(on, off, freshlyOff, freshlyOn);
        theSwitch.setInitial(on);
        theSwitch.addTransitions(onTrans, offTrans, buttonUpOffTrans, buttonUpOnTrans, oneSecondOff);
        theSwitch.addTimer(timer);
        theSwitch.addSensor(buttonSensor);

        // Generating Code
        Visitor codeGenerator = new ToC();
        theSwitch.accept(codeGenerator);

        // Writing C files
        FileWriter.outputCode(codeGenerator);
    }

}
