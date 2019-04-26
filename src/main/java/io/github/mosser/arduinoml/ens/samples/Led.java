package io.github.mosser.arduinoml.ens.samples;

import io.github.mosser.arduinoml.ens.generator.ToC;
import io.github.mosser.arduinoml.ens.generator.Visitor;
import io.github.mosser.arduinoml.ens.model.Action;
import io.github.mosser.arduinoml.ens.model.Actuator;
import io.github.mosser.arduinoml.ens.model.App;
import io.github.mosser.arduinoml.ens.model.SIGNAL;
import io.github.mosser.arduinoml.ens.model.State;
import io.github.mosser.arduinoml.ens.util.FileWriter;

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
		on.addAction(switchTheLightOn);
		off.addAction(switchTheLightOff);

		// Binding transitions to states
//		on.setNext(off);
//		off.setNext(on);

		// Building the App
		App theSwitch = new App();
		theSwitch.setName("Led!");
		theSwitch.addActuator(led);
		theSwitch.addStates(on, off);
		theSwitch.setInitial(on);

		// Generating Code
		Visitor codeGenerator = new ToC();
		theSwitch.accept(codeGenerator);

		// Writing C files
        FileWriter.outputCode(codeGenerator);
	}

}
