package ac.soton.eventb.documenter;

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

public class DocumentGenerator {
	final static String documentsFolder = "EventB_Documents";

	public static String createFile(EventBElement element, int level){
		
		String name = "";
		
		if(element instanceof EventBNamed)
			name = ((EventBNamed)element).getName() ;
		//---------------
		// The different names to fix the problem if the report was generated from project or machine/context level
		String fileName = "";
		if (level < 1)
			fileName = name + "_level0.tex";
		else
			fileName = name + ".tex";
		//--------------
		IFolder folder = WorkspaceSynchronizer.getFile(element.eResource()).getProject().getFolder(documentsFolder);
		
		//------------------set document content-----------
	    String content = setContent(element, level);
		InputStream input = new StringInputStream(content);
		
		//------------------set document content-----------
		
		if (folder.exists()){
			IFile file = folder.getFile(fileName);
			if (file.exists())
			try {
				//file.setContents(input, 0, null);
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
		return name;
	}
	
	public static IFile createProjectFile(IProject proj){
		IFile file  = null;
		String fileName = proj.getName() + ".tex";
			
		
		IFolder folder =proj.getFolder(documentsFolder);
		
		
			
		if (!folder.exists())
			try {
				folder.create(true, true, null);
			} catch (CoreException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		//------------------set document content-----------
		file = folder.getFile(fileName);
		String content = beginDocument();
		InputStream input = new StringInputStream(content);
				
		//------------------set document content-----------
		if (file.exists())
			try {
				//file.setContents(input, 0, null);
				file.setContents(input, IResource.FORCE, null);
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				throw new RuntimeException("Could not update file: " + fileName, e1);
			}
			else{     
				try {
					file.create(input,true , null);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					throw new RuntimeException("Could not create file: " + fileName, e);
				}
			}
		
		return file;
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
	// String strReplace = str.replaceAll("_", "-");
	 String strReplace = str.replace("_", "\\_");
	 return strReplace;
 }
}
