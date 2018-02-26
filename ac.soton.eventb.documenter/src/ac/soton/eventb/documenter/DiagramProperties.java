package ac.soton.eventb.documenter;

import org.eventb.emf.core.AbstractExtension;
import org.eventb.emf.core.EventBElement;
import org.eventb.emf.core.EventBNamed;
import org.eventb.emf.core.context.Context;
import org.eventb.emf.core.machine.Machine;

import ac.soton.eventb.classdiagrams.Association;
import ac.soton.eventb.classdiagrams.Class;
import ac.soton.eventb.classdiagrams.ClassAttribute;
import ac.soton.eventb.classdiagrams.ClassConstraint;
import ac.soton.eventb.classdiagrams.ClassMethod;
import ac.soton.eventb.classdiagrams.Classdiagram;
import ac.soton.eventb.statemachines.Statemachine;

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
		str += "\\begin{table}[!htb]  \n";
		str += "\\centering \n";
		str += "\\begin{tabular}{|p{2cm}||p{5cm}|p{3cm}|p{2cm}|} \n";
		str += "\\hline \n";
		str += "\\textbf{Class} & \\textbf{Constraints} & \\textbf{Methods} & \\textbf{Attributes}\\\\ \n";
		str += "\\hline \n";
		
		// Class Properties
		// used eventbcode and nort verb for table contents in case greater than column width
		for (Class cdClass : cd.getClasses()){
			str += beginEventBCode(cdClass.getName()) + "&";
			
			str += "\\begin{tabular}{l}\n";
			for(ClassConstraint constraint : cdClass.getConstraints()){
				
				str += beginEventBCode(constraint.getPredicate()) + "\\\\ \n";
			}
			str += "\\end{tabular} \n";
			str += "&";	
			
			str += "\\begin{tabular}{l} \n";
			for(ClassMethod method : cdClass.getMethods()){
				str += beginEventBCode(method.getLabel())+ "\\\\ \n";
			}
			str += "\\end{tabular} \n";
			str += "&";
			
			str += "\\begin{tabular}{l} \n";
			for(ClassAttribute attribute : cdClass.getClassAttributes()){
				str += beginEventBCode(attribute.getName())+ "\\\\ \n";
			}
			str += "\\end{tabular} \n";
			
			str += "\\\\ \\hline \n";
		}
		
		str += "\\end{tabular} \n";
		str += "\\caption{Class properties of " + cd.getName() + " classdiagram} \n";
		str += "\\label{tab:Class_" + cd.getName() + "} \n";
		str += "\\end{table} \n";
		
		
		// assosciation properties
		str += "\\begin{table}[!htb] \n";
		str += "\\centering \n";
		str += "\\begin{tabular}{|p{4cm}||p{4cm}|p{4cm}|} \n";
		str += "\\hline \n";
		str += "\\textbf{Assosciation} & \\textbf{Source} & \\textbf{Target}\\\\ \n";
		str += "\\hline \n";
		for (Association cdAssosciation : cd.getAssociations()){
			str += beginEventBCode(cdAssosciation.getName()) + "&" + beginEventBCode(cdAssosciation.getSource().getName()) + "&" + beginEventBCode(cdAssosciation.getSource().getName()) + "\\\\ \n";
			
		}
		str += "\\hline \n";
		str += "\\end{tabular} \n";
		str += "\\caption{Association properties of " + cd.getName() + " classdiagram} \n";
		str += "\\label{tab:Assosciation_" + cd.getName() + "} \n";
		str += "\\end{table} \n";
		
		return str;
	}
	
	public static String addDiagram(String elementName, String diagramName){
		String figName = elementName +"_" + diagramName;
		String figPath = "Figures/" + figName;
		String str = "";
		str+= "\\begin{figure}[!htb] \n";
		str+= "\\centering \n";
		str+= "\\includegraphics[width=1\\linewidth]{" + figPath + "} \n";
		str+= "\\caption{" + diagramName +  " in " +  elementName + "} \n";
		str+= "\\label{fig:" + figName + "} \n";
		str+= "\\end{figure} \n";
		return str;
	}
	
	public static String beginEventBCode(String code){
		String str = "";
		str += "\\begin{EventBcode} \n" + code +  "\n \\end{EventBcode} \n";
		return str;
		
	}
	
	public static String addVerb(String content){
		String str = "";
		str += "\\verb|"+ content + "|";
		return str;
	}
}
