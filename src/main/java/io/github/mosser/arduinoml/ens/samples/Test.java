package io.github.mosser.arduinoml.ens.samples;

import io.github.mosser.arduinoml.ens.generator.ToC;
import io.github.mosser.arduinoml.ens.generator.Visitor;
import io.github.mosser.arduinoml.ens.model.Action;
import io.github.mosser.arduinoml.ens.model.Actuator;
import io.github.mosser.arduinoml.ens.model.App;
import io.github.mosser.arduinoml.ens.model.Condition;
import io.github.mosser.arduinoml.ens.model.SIGNAL;
import io.github.mosser.arduinoml.ens.model.Sensor;
import io.github.mosser.arduinoml.ens.model.State;
import io.github.mosser.arduinoml.ens.model.Transition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        freshlyOn.setActions(Arrays.asList(switchTheLightOn));
        freshlyOff.setActions(Arrays.asList(switchTheLightOff));

        // Binding transitions to states
        Sensor buttonSensor = new Sensor(10, "button");
        Condition buttonPressed = new Condition(buttonSensor, SIGNAL.HIGH);
        Condition buttonNotPressed = new Condition(buttonSensor, SIGNAL.LOW);


        Transition onTrans = new Transition("onTrans", List.of(buttonPressed), off, freshlyOn);
        Transition offTrans = new Transition("offTrans", List.of(buttonPressed), on, freshlyOff);
        Transition buttonUpOnTrans = new Transition("buttonUpOnTrans", List.of(buttonNotPressed), freshlyOff, off);
        Transition buttonUpOffTrans = new Transition("buttonUpOffTrans", List.of(buttonNotPressed), freshlyOn, on);

        // Building the App
        App theSwitch = new App();
        theSwitch.setName("Led!");
        theSwitch.setBricks(Arrays.asList(led));
        theSwitch.setStates(Arrays.asList(on, off, freshlyOff, freshlyOn));
        theSwitch.setInitial(on);
        theSwitch.addTransition(onTrans);
        theSwitch.addTransition(offTrans);
        theSwitch.addTransition(buttonUpOffTrans);
        theSwitch.addTransition(buttonUpOnTrans);
        theSwitch.addSensor(buttonSensor);

        // Generating Code
        Visitor codeGenerator = new ToC();
        theSwitch.accept(codeGenerator);

        // Writing C files
        try {
            System.out.println("Generating C code: ./output/fsm.h");
            Files.write(Paths.get("./output/fsm.h"), codeGenerator.getHeaders().toString().getBytes());
            System.out.println("Generating C code: ./output/main.c");
            Files.write(Paths.get("./output/main.c"), codeGenerator.getCode().toString().getBytes());
            System.out.println("Code generation: done");
            System.out.println("Board upload : cd output && make upload && cd ..;");
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
