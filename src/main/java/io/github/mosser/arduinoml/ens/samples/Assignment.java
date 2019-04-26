package io.github.mosser.arduinoml.ens.samples;

import io.github.mosser.arduinoml.ens.generator.ToC;
import io.github.mosser.arduinoml.ens.generator.Visitor;
import io.github.mosser.arduinoml.ens.model.Action;
import io.github.mosser.arduinoml.ens.model.Actuator;
import io.github.mosser.arduinoml.ens.model.App;
import io.github.mosser.arduinoml.ens.model.Condition;
import io.github.mosser.arduinoml.ens.model.SIGNAL;
import io.github.mosser.arduinoml.ens.model.Sensor;
import io.github.mosser.arduinoml.ens.model.SensorCondition;
import io.github.mosser.arduinoml.ens.model.State;
import io.github.mosser.arduinoml.ens.model.Timer;
import io.github.mosser.arduinoml.ens.model.TimerCondition;
import io.github.mosser.arduinoml.ens.model.Transition;
import io.github.mosser.arduinoml.ens.util.FileWriter;

import java.util.List;

public class Assignment {

    public static void main(String[] args) {

        App app = new App("Assignment 1");


        Actuator firstSeg = new Actuator("seg1", 1);
        Actuator secondSeg = new Actuator("seg2", 2);
        Actuator thirdSeg = new Actuator("seg3", 3);
        Actuator fourthSeg = new Actuator("seg4", 4);
        Actuator fifthSeg = new Actuator("seg5", 5);
        Actuator sixthSeg = new Actuator("seg6", 6);
        Actuator seventhSeg = new Actuator("seg7", 7);

        Actuator firstDigit = new Actuator("digit1", 8);

        Actuator led = new Actuator("led", 13);

        Action firstSegOn = new Action(firstSeg, SIGNAL.HIGH);
        Action firstSegOff = new Action(firstSeg, SIGNAL.LOW);
        Action secondSegOn = new Action(secondSeg, SIGNAL.HIGH);
        Action secondSegOff = new Action(secondSeg, SIGNAL.LOW);
        Action thirdSegOn = new Action(thirdSeg, SIGNAL.HIGH);
        Action thirdSegOff = new Action(thirdSeg, SIGNAL.LOW);
        Action fourthSegOn = new Action(fourthSeg, SIGNAL.HIGH);
        Action fourthSegOff = new Action(fourthSeg, SIGNAL.LOW);
        Action fifthSegOn = new Action(fifthSeg, SIGNAL.HIGH);
        Action fifthSegOff = new Action(fifthSeg, SIGNAL.LOW);
        Action sixthSegOn = new Action(sixthSeg, SIGNAL.HIGH);
        Action sixthSegOff = new Action(sixthSeg, SIGNAL.LOW);
        Action seventhSegOn = new Action(seventhSeg, SIGNAL.HIGH);
        Action seventhSegOff = new Action(seventhSeg, SIGNAL.LOW);

        Action firstDigitOn = new Action(firstDigit, SIGNAL.LOW);

        Action ledOn = new Action(led, SIGNAL.HIGH);
        Action ledOff = new Action(led, SIGNAL.LOW);

        State zero = new State("zero")
                .withActions(firstSegOn, secondSegOn, thirdSegOn, fourthSegOn, fifthSegOn, sixthSegOn, seventhSegOff, ledOff, firstDigitOn);
        State one = new State("one")
                .withActions(firstSegOff, secondSegOn, thirdSegOn, fourthSegOff, fifthSegOff, sixthSegOff, seventhSegOff);
        State two = new State("two")
                .withActions(firstSegOn, secondSegOn, thirdSegOff, fourthSegOn, fifthSegOn, sixthSegOff, seventhSegOn);
        State three = new State("three")
                .withActions(firstSegOn, secondSegOn, thirdSegOn, fourthSegOn, fifthSegOff, sixthSegOff, seventhSegOn);
        State four = new State("four")
                .withActions(firstSegOff, secondSegOn, thirdSegOn, fourthSegOff, fifthSegOff, sixthSegOn, seventhSegOn);
        State five = new State("five")
                .withActions(firstSegOn, secondSegOff, thirdSegOn, fourthSegOn, fifthSegOff, sixthSegOn, seventhSegOn);
        State six = new State("six")
                .withActions(firstSegOn, secondSegOff, thirdSegOn, fourthSegOn, fifthSegOn, sixthSegOn, seventhSegOn);
        State seven = new State("seven")
                .withActions(firstSegOn, secondSegOn, thirdSegOn, fourthSegOff, fifthSegOff, sixthSegOff, seventhSegOff);
        State eight = new State("eight")
                .withActions(firstSegOn, secondSegOn, thirdSegOn, fourthSegOn, fifthSegOn, sixthSegOn, seventhSegOn);
        State nine = new State("nine")
                .withActions(firstSegOn, secondSegOn, thirdSegOn, fourthSegOn, fifthSegOff, sixthSegOn, seventhSegOn);

        State reset = new State("reset")
                .withActions(firstSegOn, secondSegOn, thirdSegOn, fourthSegOn, fifthSegOn, sixthSegOn, seventhSegOff, ledOn);


        Sensor buttonSensor = new Sensor("button", 10);
        Condition buttonPressed = new SensorCondition(buttonSensor, SIGNAL.HIGH);
        Condition buttonNotPressed = new SensorCondition(buttonSensor, SIGNAL.LOW);


        Timer timer = new Timer("timer");
        Condition oneSecond = new TimerCondition(timer, 1000);

        Transition zeroToOne = new Transition("zeroToOne", oneSecond, zero, one);
        Transition oneToTwo = new Transition("oneToTwo", oneSecond, one, two);
        Transition twoToThree = new Transition("twoToThree", oneSecond, two, three);
        Transition threeToFour = new Transition("threeToFour", oneSecond, three, four);
        Transition fourToFive = new Transition("fourToFive", oneSecond, four, five);
        Transition fiveToSix = new Transition("fiveToSix", oneSecond, five, six);
        Transition sixToSeven = new Transition("sixToSeven", oneSecond, six, seven);
        Transition sevenToEight = new Transition("sevenToEight", oneSecond, seven, eight);
        Transition eightToNine = new Transition("eightToNine", oneSecond, eight, nine);
        Transition nineToZero = new Transition("nineToZero", oneSecond, nine, zero);


        Transition resetZero = new Transition("resetZero", buttonPressed, zero, reset);
        Transition resetOne = new Transition("resetOne", buttonPressed, one, reset);
        Transition resetTwo = new Transition("resetTwo", buttonPressed, two, reset);
        Transition resetThree = new Transition("resetThree", buttonPressed, three, reset);
        Transition resetFour = new Transition("resetFour", buttonPressed, four, reset);
        Transition resetFive = new Transition("resetFive", buttonPressed, five, reset);
        Transition resetSix = new Transition("resetSix", buttonPressed, six, reset);
        Transition resetSeven = new Transition("resetSeven", buttonPressed, seven, reset);
        Transition resetEight = new Transition("resetEight", buttonPressed, eight, reset);
        Transition resetNine = new Transition("resetNine", buttonPressed, nine, reset);

        Transition resetToZero = new Transition("resetToZero", List.of(buttonNotPressed, oneSecond), reset, zero);


        app.addActuators(led, firstSeg, secondSeg, thirdSeg, fourthSeg, fifthSeg, sixthSeg, seventhSeg, firstDigit);
        app.addStates(reset, zero, one, two, three, four, five, six, seven, eight, nine);
        app.setInitial(zero);
        app.addTransitions(zeroToOne, oneToTwo, twoToThree, threeToFour, fourToFive, fiveToSix, sixToSeven, sevenToEight, eightToNine, nineToZero);
        app.addTransitions(resetZero, resetOne, resetTwo, resetThree, resetFour, resetFive, resetSix, resetSeven, resetEight, resetNine);
        app.addTransition(resetToZero);
        app.addTimer(timer);
        app.addSensor(buttonSensor);

        // Generating Code
        Visitor codeGenerator = new ToC();
        app.accept(codeGenerator);

        // Writing C files
        FileWriter.outputCode(codeGenerator);
    }

}
