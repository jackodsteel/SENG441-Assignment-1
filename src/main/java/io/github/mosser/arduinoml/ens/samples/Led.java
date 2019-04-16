package io.github.mosser.arduinoml.ens.samples;

import io.github.mosser.arduinoml.ens.generator.ToC;
import io.github.mosser.arduinoml.ens.generator.Visitor;
import io.github.mosser.arduinoml.ens.model.*;
import io.github.mosser.arduinoml.ens.util.FileWriter;

import java.util.Arrays;

public class Led {

	public static void main(String[] args) {


		Actuator led = new Actuator();
		led.setName("LED");
		led.setPin(13);

		// Declaring states
		State on = new State();
		on.setName("on");

		State off = new State();
		off.setName("off");

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
//		on.setNext(off);
//		off.setNext(on);

		// Building the App
		App theSwitch = new App();
		theSwitch.setName("Led!");
		theSwitch.setBricks(Arrays.asList(led));
		theSwitch.setStates(Arrays.asList(on, off));
		theSwitch.setInitial(on);

		// Generating Code
		Visitor codeGenerator = new ToC();
		theSwitch.accept(codeGenerator);

		// Writing C files
        FileWriter.outputCode(codeGenerator);
	}

}
