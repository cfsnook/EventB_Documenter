/*******************************************************************************
 * Copyright (c) 2017-2018 University of Southampton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     University of Southampton - initial API and implementation
 *******************************************************************************/

package ac.soton.eventb.documentor;

import java.io.InputStream;

import org.apache.tools.ant.filters.StringInputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eventb.emf.core.EventBNamed;
import org.eventb.emf.core.EventBElement;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

/**
 * <p>
 * Latex document generator for iUML-B
 * include methods to generate latex files and their content
 * </p>
 * @author dd4g12
 * @version 1.0.0
 * @since 0.0.1
 */
public class DocumentGenerator {
	final static String documentsFolder = "EventB_Documents";

	public static String createElemFile(EventBElement element, int level){
		
		String name = "";
		
		if(element instanceof EventBNamed)
			name = ((EventBNamed)element).getName() ;
		
		// The different names to fix the problem if the report was generated from project or machine/context level
		String fileName = "";
		if (level < 1)
			fileName = name + "_section.tex";
		else
			fileName = name + ".tex";

		IProject proj= WorkspaceSynchronizer.getFile(element.eResource()).getProject();
	    String content = setContent(element, level);
	   
		generateFile(fileName,proj, content);
		
		return name;
	}
	
	// set document class, title, date, table of contents, page numbering
	public static String beginDocument(){
		
		String str = "";
		str += "\\documentclass[11pt]{report}\n" ;
		str += "\\usepackage{graphicx} \n";
		str += "\\usepackage{booktabs,array} \n";
		str += "\\usepackage{lstEventB} \n";
		str += "\\graphicspath{{./Figures/}} \n";
		str += "\\title{\\bf iUML-B Report}\n";
		str += "\\date{\\today}\n";
		str += "\\begin{document}\n";
		str += "\\maketitle\n";
		str += "\\pagenumbering{roman}\n" ;                
		str += "\\setcounter{page}{2}\n" ;                   
		str += "\\tableofcontents\n";
		
		str+= "\\addcontentsline{toc}{section}{\\listfigurename}\\listoffigures \n";
		str += "\\addcontentsline{toc}{section}{\\listtablename}\\listoftables \n";
		
		str += "\\newpage \n";
		str += "\\pagenumbering{arabic} \n";
		return str;
	}
	
	
	// end latex document
	public static String endDocument(){
		
		String str = "\\end{document}";	
		return str;
	}
	
	//begin chapter
	public static String beginChapter(String title){
		String str = "";
		str += "\\chapter{" + replaceUnderscore(title) + "} \n" ;        
		//str += "\\pagenumbering{arabic} \n";
		return str;
		
	}
	
	//begin section
	public static String beginSection(String title){
			String str = "";
			
			
			str += "\\section*{" + replaceUnderscore(title) + "} \n" ;        
			
			return str;
			
  }
  
	public static String includeFile(String name){
		String str = "";
		str += "\\include{" + name + "} \n" ;
		return str;
	}
		
  // set content for context machine level 0 or 1 (part of project)
  public static String setContent(EventBElement element, int level){
	  
	  String str = "";
	  String title = ((EventBNamed) element).getName();
	  if (level == 0){
		str += beginDocument();
		str += beginSection(title);
		str += DiagramProperties.exportProperties(element);
		str += endDocument();
	  }
	  
	  else
		  str += beginChapter(title);
	      str += DiagramProperties.exportProperties(element);
	    
	  return str;
	  
  }
 public static String replaceUnderscore(String str){
	 String strReplace = str.replace("_", "\\_");
	 return strReplace;
 }
 
 /**
  * <p>
  * generates a file in documentsFolder
  * fileName should  also include the extension
  * </p>
  * @author dd4g12
  * @version 1.0.0
  * @since 1.0.0
  */
 public static IFile generateFile(String fileName, IProject proj, String content) {
	 IFile file  = null;
	 IFolder folder =proj.getFolder(documentsFolder);
		
		if (folder.exists()){
			
		    //set document content
			file = folder.getFile(fileName);
			
			InputStream input = new StringInputStream(content);
			
			if (file.exists())
				try {
					
					file.setContents(input, IResource.FORCE, null);
				} catch (CoreException e1) {
					throw new RuntimeException("Could not update file: " + fileName, e1);
				}
				else{     
					try {
						file.create(input,true , null);
					} catch (CoreException e) {
						throw new RuntimeException("Could not create file: " + fileName, e);
					}
				}
		}
		return file;
	 
 }

}
