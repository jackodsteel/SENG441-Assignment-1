package io.github.mosser.arduinoml.ens.samples;

import io.github.mosser.arduinoml.ens.generator.ToC;
import io.github.mosser.arduinoml.ens.generator.Visitor;
import io.github.mosser.arduinoml.ens.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) {


        Actuator led = new Actuator();
        led.setName("LED");
        led.setPin(13);

        // Declaring states
        State on = new State("on");
        State off = new State("off");

        // Creating actions
        Action switchTheLightOn = new Action();
        switchTheLightOn.setActuator(led);
        switchTheLightOn.setValue(SIGNAL.HIGH);

        Action switchTheLightOff = new Action();
        switchTheLightOff.setActuator(led);
        switchTheLightOff.setValue(SIGNAL.LOW);

        // Binding actions to states
        on.setActions(Arrays.asList(switchTheLightOn));
        off.setActions(Arrays.asList(switchTheLightOff));

        // Binding transitions to states
        Sensor buttonSensor = new Sensor(10, "button");
        Condition buttonPressed = new Condition(buttonSensor, SIGNAL.HIGH);
        Condition buttonNotPressed = new Condition(buttonSensor, SIGNAL.LOW);


        Transition onTrans = new Transition("onTrans", List.of(buttonPressed), off, on);
        Transition offTrans = new Transition("offTrans", List.of(buttonNotPressed), on, off);

        // Building the App
        App theSwitch = new App();
        theSwitch.setName("Led!");
        theSwitch.setBricks(Arrays.asList(led));
        theSwitch.setStates(Arrays.asList(on, off));
        theSwitch.setInitial(on);
        theSwitch.addTransition(onTrans);
        theSwitch.addTransition(offTrans);

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
