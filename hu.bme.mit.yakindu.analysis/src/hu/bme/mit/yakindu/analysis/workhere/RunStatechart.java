package hu.bme.mit.yakindu.analysis.workhere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;



public class RunStatechart {
	
	public static void readUserEvents(ExampleStatemachine s){
		Scanner reader = new Scanner(System.in);
		String command = reader.nextLine();
	
		if(command.equals("exit"))
			System.exit(0);
		else {
			switch(command) {
			case "white":
				s.raiseWhite();
				s.runCycle();
				break;
			case "black": 
				s.raiseBlack();
				s.runCycle();
				break;
			default:
				System.out.println("That's not a valid event.");
				break;
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();
		while (true) {
			readUserEvents(s);
			print(s);
		}
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
