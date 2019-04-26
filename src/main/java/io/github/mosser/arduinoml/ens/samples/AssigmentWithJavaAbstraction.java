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

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AssigmentWithJavaAbstraction {


    public static void main(String[] args) {

        App app = new App("Assignment 1");

        Segment firstSeg = new Segment("seg1", 1);
        Segment secondSeg = new Segment("seg2", 2);
        Segment thirdSeg = new Segment("seg3", 3);
        Segment fourthSeg = new Segment("seg4", 4);
        Segment fifthSeg = new Segment("seg5", 5);
        Segment sixthSeg = new Segment("seg6", 6);
        Segment seventhSeg = new Segment("seg7", 7);

        Actuator firstDigit = new Actuator("digit1", 8);

        Actuator led = new Actuator("led", 13);

        Action firstDigitOn = new Action(firstDigit, SIGNAL.LOW);

        Action ledOn = new Action(led, SIGNAL.HIGH);
        Action ledOff = new Action(led, SIGNAL.LOW);

        Set<Segment> allSegments = Set.of(firstSeg, secondSeg, thirdSeg, fourthSeg, fifthSeg, sixthSeg, seventhSeg);

        Digit zero = new Digit("zero", allSegments, firstSeg, secondSeg, thirdSeg, fourthSeg, fifthSeg, sixthSeg);
        Digit one = new Digit("one", allSegments, secondSeg, thirdSeg);
        Digit two = new Digit("two", allSegments, firstSeg, secondSeg, fourthSeg, fifthSeg, seventhSeg);
        Digit three = new Digit("three", allSegments, firstSeg, secondSeg, thirdSeg, fourthSeg, seventhSeg);
        Digit four = new Digit("four", allSegments, secondSeg, thirdSeg, sixthSeg, seventhSeg);
        Digit five = new Digit("five", allSegments, firstSeg, thirdSeg, fourthSeg, sixthSeg, seventhSeg);
        Digit six = new Digit("six", allSegments, firstSeg, thirdSeg, fourthSeg, fifthSeg, sixthSeg, seventhSeg);
        Digit seven = new Digit("seven", allSegments, firstSeg, secondSeg, thirdSeg);
        Digit eight = new Digit("eight", allSegments, firstSeg, secondSeg, thirdSeg, fourthSeg, fifthSeg, sixthSeg, seventhSeg);
        Digit nine = new Digit("nine", allSegments, firstSeg, secondSeg, thirdSeg, fourthSeg, sixthSeg, seventhSeg);
        zero.next = one;
        one.next = two;
        two.next = three;
        three.next = four;
        four.next = five;
        five.next = six;
        six.next = seven;
        seven.next = eight;
        eight.next = nine;
        nine.next = zero;

        State reset = new State("reset")
                .withActions(firstSeg.on, secondSeg.on, thirdSeg.on, fourthSeg.on, fifthSeg.on, sixthSeg.on, seventhSeg.off, ledOn);


        Sensor buttonSensor = new Sensor("button", 10);
        Condition buttonPressed = new SensorCondition(buttonSensor, SIGNAL.HIGH);
        Condition buttonNotPressed = new SensorCondition(buttonSensor, SIGNAL.LOW);


        Timer timer = new Timer("timer");
        Condition oneSecond = new TimerCondition(timer, 1000);

        for (Digit digit : List.of(zero, one, two, three, four, five, six, seven, eight, nine)) {
            String nextTransitionName = digit.state.getName() + "2" + digit.next.state.getName();
            Transition nextTrans = new Transition(nextTransitionName, oneSecond, digit.state, digit.next.state);
            Transition resetTrans = new Transition("reset" + digit.state.getName(), buttonPressed, digit.state, reset);
            app.addState(digit.state);
            app.addTransitions(nextTrans, resetTrans);
        }

        zero.state.addActions(ledOff, firstDigitOn);

        Transition resetToZero = new Transition("resetToZero", List.of(buttonNotPressed, oneSecond), reset, zero.state);

        allSegments.stream().map(s -> s.actuator).forEach(app::addActuator);
        app.addActuators(led, firstDigit);
        app.addState(reset);
        app.setInitial(zero.state);
        app.addTransition(resetToZero);
        app.addTimer(timer);
        app.addSensor(buttonSensor);

        // Generating Code
        Visitor codeGenerator = new ToC();
        app.accept(codeGenerator);

        // Writing C files
        FileWriter.outputCode(codeGenerator);
    }


    static class Segment {

        final Actuator actuator;
        final Action on;
        final Action off;

        public Segment(String name, int pin) {
            actuator = new Actuator(name, pin);
            on = new Action(actuator, SIGNAL.HIGH);
            off = new Action(actuator, SIGNAL.LOW);
        }
    }

    static class Digit {
        State state;
        Digit next;

        public Digit(String name, Set<Segment> allSegments, Segment... onSegmentsVarArg) {
            state = new State(name);
            Set<Segment> onSegments = Arrays.stream(onSegmentsVarArg).collect(Collectors.toSet());
            onSegments.forEach(segment -> state.addAction(segment.on));
            allSegments.stream().filter(seg -> !onSegments.contains(seg)).forEach(seg -> state.addAction(seg.off));
        }
    }
}
