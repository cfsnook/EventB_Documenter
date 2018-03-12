package ac.soton.eventb.documenter;

import org.eventb.emf.core.AbstractExtension;
import org.eventb.emf.core.EventBElement;
import org.eventb.emf.core.EventBNamed;
import org.eventb.emf.core.context.Context;
import org.eventb.emf.core.machine.Action;
import org.eventb.emf.core.machine.Guard;
import org.eventb.emf.core.machine.Invariant;
import org.eventb.emf.core.machine.Machine;
import org.eventb.emf.core.machine.Witness;

import ac.soton.eventb.classdiagrams.Association;
import ac.soton.eventb.classdiagrams.Class;
import ac.soton.eventb.classdiagrams.ClassAttribute;
import ac.soton.eventb.classdiagrams.ClassConstraint;
import ac.soton.eventb.classdiagrams.ClassMethod;
import ac.soton.eventb.classdiagrams.Classdiagram;
import ac.soton.eventb.emf.core.extension.coreextension.TypedParameter;
import ac.soton.eventb.statemachines.AbstractNode;
import ac.soton.eventb.statemachines.State;
import ac.soton.eventb.statemachines.Statemachine;
import ac.soton.eventb.statemachines.Transition;

public class DiagramProperties {
	public static String exportProperties (EventBElement element){
		String str = "";
		if(element instanceof Machine || element instanceof Context){
			for (AbstractExtension ext: element.getExtensions()){
				if(ext instanceof Classdiagram){
					
					str += addDiagram(((EventBNamed)element).getName(), ((Classdiagram) ext).getName());
					//call a method to get the properties or maybe write directly to the tex file
					
					 str += buildClassdiagramTable((Classdiagram) ext);
										
				}
				
				else if(ext instanceof Statemachine){
					//call a method to get the properties or maybe write directly to the tex file
					str += addDiagram(((EventBNamed)element).getName(), ((Statemachine) ext).getName());
					//call a method to get the properties or maybe write directly to the tex file
					 str += buildStatemachineTable((Statemachine) ext);
				}
			}
		}
		return str;
	}
	
	/**
	 * @param cd
	 * @return
	 */
	public static String buildClassdiagramTable(Classdiagram cd){
		 
		
		String str = "";
		str+= addComment("Class Diagram Property table for: " + cd.getName());
		
		str += "\\begin{table}[!htb]  \n";
		str += "\\centering \n";
		str += "\\begin{tabular}{|p{2cm}||p{5cm}|p{3cm}|p{2cm}|} \n";
		str += "\\hline \n";
		str += "\\textbf{Class} & \\textbf{Constraints} & \\textbf{Methods} & \\textbf{Attributes}\\\\ \n";
		str += "\\hline \n";
		
		// Class Properties
		// used eventbcode and nort verb for table contents in case greater than column width
		for (Class cdClass : cd.getClasses()){
			str += DocumentGenerator.replaceUnderscore(cdClass.getName()) + "&";
			
			str += "\\begin{tabular}{@{}p{5cm}@{}}\n";
			for(ClassConstraint constraint : cdClass.getConstraints()){
				
				str += beginEventBCode(constraint.getPredicate()) + "\\\\ \n";
			}
			str += "\\end{tabular} \n";
			str += "&";	
			
			str += "\\begin{tabular}{@{}p{3cm}@{}l} \n";
			for(ClassMethod method : cdClass.getMethods()){
				str += DocumentGenerator.replaceUnderscore(method.getLabel())+ "\\\\ \n";
			}
			str += "\\end{tabular} \n";
			str += "&";
			
			str += "\\begin{tabular}{@{}p{2cm}@{}} \n";
			for(ClassAttribute attribute : cdClass.getClassAttributes()){
				str += DocumentGenerator.replaceUnderscore(attribute.getName())+ "\\\\ \n";
			}
			str += "\\end{tabular} \n";
			
			str += "\\\\ \\hline \n";
		}
		
		str += "\\end{tabular} \n";
		str += "\\caption{Class properties of " + DocumentGenerator.replaceUnderscore(cd.getName()) + " classdiagram} \n";
		str += "\\label{tab:Class_" + cd.getName() + "} \n";
		str += "\\end{table} \n";
		
		
		// assosciation properties
		str+= addComment("Class Diagram Assosciation Property table for: " + cd.getName());
		
		str += "\\begin{table}[!htb] \n";
		str += "\\centering \n";
		str += "\\begin{tabular}{|p{4cm}||p{4cm}|p{4cm}|} \n";
		str += "\\hline \n";
		str += "\\textbf{Assosciation} & \\textbf{Source} & \\textbf{Target}\\\\ \n";
		str += "\\hline \n";
		for (Association cdAssosciation : cd.getAssociations()){
			str += DocumentGenerator.replaceUnderscore(cdAssosciation.getName()) + "&" + DocumentGenerator.replaceUnderscore(cdAssosciation.getSource().getName()) + "&" + DocumentGenerator.replaceUnderscore(cdAssosciation.getTarget().getName()) + "\\\\ \n";
			
		}
		str += "\\hline \n";
		str += "\\end{tabular} \n";
		str += "\\caption{Association properties of " + DocumentGenerator.replaceUnderscore(cd.getName()) + " classdiagram} \n";
		str += "\\label{tab:Assosciation_" + cd.getName() + "} \n";
		str += "\\end{table} \n";
		
		return str;
	}
	
	public static String addDiagram(String elementName, String diagramName){
		String figName = elementName +"_" + diagramName;
		String figPath = "Figures/" + figName;
		String str = "";
		str+= addComment("Figure: " + diagramName);
		str+= "\\begin{figure}[!htb] \n";
		str+= "\\centering \n";
		str+= "\\includegraphics[width=0.8\\linewidth,keepaspectratio]{" + figPath + "} \n"; 
		str+= "\\caption{" + DocumentGenerator.replaceUnderscore(diagramName) +  " in " +  DocumentGenerator.replaceUnderscore(elementName) + "} \n";
		str+= "\\label{fig:" + figName + "} \n";
		str+= "\\end{figure} \n";
		return str;
	}
	
	public static String beginEventBCode(String code){
		String str = "";
		str += "\\begin{EventBcode} \n" + code +  "\\end{EventBcode} \n";
		return str;
		
	}
	
	public static String addVerb(String content){
		String str = "";
		str += "\\verb|"+ content + "|";
		return str;
	}
	
	public static String buildStatemachineTable(Statemachine sm){
		
		String smName = sm.getName();
		String str = "";
		
		//Nodes Properties table
		str+= addComment("State Property table for statemachine: " + sm.getName());
		
		str += "\\begin{table}[!htb]  \n";
		str += "\\centering \n";
		str += "\\begin{tabular}{|p{2cm}||p{3cm}|p{2cm}|p{2cm}|p{3cm}|} \n";
		str += "\\hline \n";
		str += "\\textbf{State} & \\textbf{Invariants} & \\textbf{Entry Actions} & \\textbf{Exit Actions} & \\textbf{Statemachines}\\\\ \n";
		str += "\\hline \n";
		
		for (AbstractNode node : sm.getNodes()){
		    if (node instanceof State){
		    	State state = (State) node;
		    	str += DocumentGenerator.replaceUnderscore(state.getName()) + "&";
					
				str += "\\begin{tabular}{@{}p{3cm}@{}}\n";
				for(Invariant inv : state.getInvariants()){
						
					str += beginEventBCode(inv.getPredicate()) + "\\\\ \n";
				}
				str += "\\end{tabular} \n";
				str += "&";	
				
				str += "\\begin{tabular}{@{}p{2cm}@{}}\n";
				for(Action entryAct : state.getEntryActions()){
						
					str += beginEventBCode(entryAct.getAction()) + "\\\\ \n";
				}
				str += "\\end{tabular} \n";
				str += "&";
				
				str += "\\begin{tabular}{@{}p{5cm}@{}}\n";
				for(Action exitAct : state.getEntryActions()){
						
					str += beginEventBCode(exitAct.getAction()) + "\\\\ \n";
				}
				str += "\\end{tabular} \n";
				str += "&";	
				
				str += "\\begin{tabular}{@{}p{3cm}@{}}\n";
				for(Statemachine stateM: state.getStatemachines()){
						
					str += DocumentGenerator.replaceUnderscore(stateM.getName()) + "\\\\ \n";
				}
				str += "\\end{tabular} \n";
				str += "\\\\ \\hline \n";
					
		    }// end if state
          
		}// end for node
		
		str += "\\end{tabular} \n";
		str += "\\caption{State properties of " + DocumentGenerator.replaceUnderscore(smName)+ " statemachine} \n";
		str += "\\label{tab:State_" + smName + "} \n";
		str += "\\end{table} \n";
		
		//transion source target
		str+= addComment("Transision source-target table for statemachine: " + sm.getName());
		
		str += "\\begin{table}[!htb] \n";
		str += "\\centering \n";
		str += "\\begin{tabular}{|p{4cm}||p{4cm}|p{4cm}|} \n";
		str += "\\hline \n";
		str += "\\textbf{Transition} & \\textbf{Source} & \\textbf{Target}\\\\ \n";
		str += "\\hline \n";
		for (Transition transition : sm.getTransitions()){
			str += DocumentGenerator.replaceUnderscore(transition.getLabel()) + "&" + DocumentGenerator.replaceUnderscore(transition.getSource().getName()) + "&" + DocumentGenerator.replaceUnderscore(transition.getTarget().getName()) + "\\\\ \n";
			
		}
		str += "\\hline \n";
		str += "\\end{tabular} \n";
		str += "\\caption{Transition properties of " + DocumentGenerator.replaceUnderscore(smName) + " statemachine} \n";
		str += "\\label{tab:Assosciation_" + smName + "} \n";
		str += "\\end{table} \n";
		
		// transition properties
		str+= addComment("Transition Property table for statemachine: " + sm.getName());
		str += "\\begin{table}[!htb]  \n";
		str += "\\centering \n";
		str += "\\begin{tabular}{|p{2cm}||p{2cm}|p{2cm}|p{3cm}|p{3cm}|} \n";
		str += "\\hline \n";
		str += "\\textbf{Transition} & \\textbf{Parameters} & \\textbf{Witnesses} & \\textbf{Guards} & \\textbf{Actions}\\\\ \n";
		str += "\\hline \n";
		for (Transition transition: sm.getTransitions()){
		    
		    str += DocumentGenerator.replaceUnderscore(transition.getLabel()) + "&";
					
			str += "\\begin{tabular}{@{}p{2cm}@{}}\n";
			
			for(TypedParameter par : transition.getParameters()){
						
				str += DocumentGenerator.replaceUnderscore(par.getName()) + "\\\\ \n";
			}
			str += "\\end{tabular} \n";
			str += "&";	
			
			//witnesses
			str += "\\begin{tabular}{@{}p{2cm}@{}}\n";
			
			for(Witness wit : transition.getWitnesses()){
						
				str += beginEventBCode(wit.getPredicate()) + "\\\\ \n";
			}
			str += "\\end{tabular} \n";
			str += "&";	
			//gurds
			str += "\\begin{tabular}{@{}p{3cm}@{}}\n";
			
			for(Guard grd : transition.getGuards()){
						
				str += beginEventBCode(grd.getPredicate()) + "\\\\ \n";
			}
			str += "\\end{tabular} \n";
			str += "&";	
			//actions
            str += "\\begin{tabular}{@{}p{3cm}@{}}\n";
			
			for(Action act : transition.getActions()){
						
				str += beginEventBCode(act.getAction()) + "\\\\ \n";
			}
			str += "\\end{tabular} \n";
			str += "\\\\ \\hline \n";
		}
		str += "\\end{tabular} \n";
		str += "\\caption{Transition properties of " + DocumentGenerator.replaceUnderscore(smName)+ " statemachine} \n";
		str += "\\label{tab:tran_" + smName + "} \n";
		str += "\\end{table} \n";
		//-----------------------------------------
		return str;
	}
	
	public static String addComment(String description){
		String str = "";
		
		str += "\n";
		str += "%----------------------------------------------------------\n";
		str += "%" + description + "\n";
		str += "%----------------------------------------------------------\n";
		
		return str;
	}
}
