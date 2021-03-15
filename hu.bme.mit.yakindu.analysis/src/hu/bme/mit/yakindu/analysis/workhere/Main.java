package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.CompositeElement;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static String capitalize(String s) {
		char title = Character.toUpperCase(s.charAt(0));
		String rest = s.substring(1);
		
		return title + rest;
		
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		ArrayList<String> variables = new ArrayList<>();
		ArrayList<String> events = new ArrayList<>();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		EObject prev = null;
		// Collecting variables and events
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof VariableDefinition) {
				VariableDefinition vd = (VariableDefinition) content;
				variables.add(capitalize(vd.getName()));
			}
			if(content instanceof EventDefinition) {
				EventDefinition ed = (EventDefinition) content;
				events.add(ed.getName());
			}
			
		}
		String indent = "";
		// Generating code
		System.out.println(indent + "public class RunStatechart {");
		indent += "\t";
		System.out.println(indent + "public static void main(String[] args) throws IOException {");
		indent += "\t";
		System.out.println(indent + "ExampleStatemachine s = new ExampleStatemachine();");
		System.out.println(indent + "s.setTimer(new TimerService());");
		System.out.println(indent + "RuntimeService.getInstance().registerStatemachine(s, 200);");
		System.out.println(indent + "s.init();");
		System.out.println(indent + "s.enter();");
		System.out.println(indent + "s.runCycle();");
		System.out.println(indent + "while(true) {");
		indent += "\t";
		System.out.println(indent + "readcommand();");
		System.out.println(indent + "print();");
		indent = indent.substring(0, indent.length() - 1);
		//end of while
		System.out.println(indent + "}");
		System.out.println(indent + "System.exit(0);");
		indent = indent.substring(0, indent.length() - 1);
		//end of main
		System.out.println(indent + "}");
		System.out.println(indent + "public static void print(IExampleStatemachine s) {");
		indent += "\t";
		for(String var : variables) {
			System.out.print(indent + "System.out.println(\"");
			System.out.print(var.charAt(0));
			System.out.println("= \" + s.getSCInterface().get" + var +"());");
		}
		indent = indent.substring(0, indent.length() - 1);
		// end of print
		System.out.println(indent + "}");
		System.out.println(indent + "public static void readcommand(IExampleStatemachine s) throws IOException {");
		indent += "\t";
		System.out.println(indent + "Scanner reader = new Scanner(System.in);");
		System.out.println(indent + "String cmd = reader.nextLine();");
		System.out.println(indent + "if(cmd.equals(\"exit\"))");
		indent += "\t";
		System.out.println(indent + "System.exit(0);");
		indent = indent.substring(0, indent.length() - 1);
		System.out.println(indent + "else {");
		indent += "\t";
		System.out.println(indent + "switch(cmd) {");
		indent += "\t";
		for(String e : events) {
			System.out.println(indent + "case \""+ e +"\" :");
			indent += "\t";
			System.out.println(indent + "s.raise" + capitalize(e) + "();");
			System.out.println(indent + "s.runCycle();");
			System.out.println(indent + "break;");
			indent = indent.substring(0, indent.length() - 1);
		}
		System.out.println(indent + "default:");
		indent += "\t";
		System.out.println(indent + "System.out.println(\"That's not a valid event.\");");
		System.out.println(indent + "break;");
		indent = indent.substring(0, indent.length() - 1);
		// end of switch
		System.out.println(indent + "}");
		indent = indent.substring(0, indent.length() - 1);
		// end of else
		System.out.println(indent + "}");
		indent = indent.substring(0, indent.length() - 1);
		// end of readcommand
		System.out.println(indent + "}");
		indent = indent.substring(0, indent.length() - 1);
		//end of class
		System.out.println(indent + "}");
		System.out.println(indent + "");
			
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
