/*******************************************************************************
 * Copyright (c) 2019 University of Southampton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     University of Southampton - initial API and implementation
 *******************************************************************************/
package ac.soton.eventb.documentor;

import org.eventb.emf.core.EventBElement;

import ac.soton.eventb.classdiagrams.Association;
import ac.soton.eventb.classdiagrams.Class;
import ac.soton.eventb.classdiagrams.ClassAttribute;
import ac.soton.eventb.classdiagrams.ClassConstraint;
import ac.soton.eventb.classdiagrams.ClassMethod;
import ac.soton.eventb.classdiagrams.Classdiagram;
import fr.systerel.internal.explorer.statistics.Statistics;
import fr.systerel.internal.explorer.statistics.StatisticsUtil;
import fr.systerel.explorer.ExplorerPlugin;
import fr.systerel.internal.explorer.model.IModelElement;
import fr.systerel.internal.explorer.statistics.IStatistics;

import java.util.List;

import org.eventb.core.IPOSequent;
import org.eventb.core.IPSStatus;

/**
 * <p>
 * Generate latex Proof Statistics table for a project
 * </p>
 * @author dd4g12
 * @version 1.0.0
 * @since 1.0.0
 */

public class ProofStatisticsExporter {
	
	public static String generateStatistics(List <IModelElement> elements, String projName) {
		String str = "";
	    String proj = DocumentGenerator.replaceUnderscore(projName);
		str += beginProofTable(proj);
				
		for(IModelElement element : elements) {
			Statistics st = new Statistics(element);
			String name = element.getInternalElement().getElementName();
			int total = st.getAuto();
			int auto = st.getAuto();
			int manual = st.getManual();
			int rev = st.getReviewed();
			int und = st.getUndischarged();
			str += buildProofStatisticsRow(name, total, auto, manual, rev, und);
		}
		
		str += endProofTable(proj);
		
		return str;
	}

	public static String buildProofStatisticsRow(String elemName, int total, int auto, int manual, int rev, int und){
		String str = "";

		str += DocumentGenerator.replaceUnderscore(elemName) + "&" + total + "&" + auto + "&" + manual + "&" + rev + "&" + und + "\\\\ \n";
		str += "\\hline \n";
		return str;
	}

	public static String beginProofTable(String projName){
		String str = "";
		str+= DiagramProperties.addComment("Proof Statistics Table for " + projName);
		str += "\\begin{table}[!htb]  \n";
		str += "\\centering \n";
		str += "\\begin{tabular}{|l|l|l|l|l|l|} \n";
		str += "\\hline \n";
		str += "\\textbf{Element Name} & \\textbf{Total} & \\textbf{Auto} & \\textbf{Manual} & \\textbf{Rev.} & \\textbf{Und.}\\\\ \n";
		str += "\\hline \n";
		
		return str;
	
	}
	
	public static String endProofTable(String projName){
		String str = "";
		str += "\\end{tabular} \n";
		str += "\\caption{Proof Statistics for \\emph{" + projName + "} Project} \n";
		str += "\\label{tab:ProofStatistics} \n";
		str += "\\end{table} \n";
		return str;
	
	}

}
