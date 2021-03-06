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
package ac.soton.eventb.documentor.handler;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eventb.emf.core.EventBElement;
import org.eventb.emf.core.machine.Machine;
import org.eventb.emf.core.context.Context;
import org.eventb.emf.persistence.EMFRodinDB;
import org.rodinp.core.IInternalElement;
import org.rodinp.core.IRodinElement;
import org.rodinp.core.IRodinFile;
import org.rodinp.core.IRodinProject;
import org.rodinp.core.RodinCore;
import org.rodinp.core.RodinDBException;
import ac.soton.eventb.documentor.DiagramExporter;
import ac.soton.eventb.documentor.DocumentGenerator;
import ac.soton.eventb.documentor.ProofStatisticsExporter;
import ac.soton.eventb.documentor.StyleFile;
import fr.systerel.internal.explorer.model.IModelElement;
import fr.systerel.internal.explorer.model.ModelController;

import org.eclipse.core.resources.IFolder;
/**
 * <p>
 * Handler for iUML-B document generator. Command can be executed from machine, context or project
 * </p>
 * @author dd4g12
 * @version 1.0.0
 * @since 0.0.1
 */
public class DocumentHandler extends AbstractHandler {
	
	final String documentsFolder = "EventB_Documents";
	final String figuresFolder = "Figures";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final EObject sourceElement;
			
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection){
			Object obj = ((IStructuredSelection)selection).getFirstElement();
			
			
			if (obj instanceof IInternalElement){
				EventBElement loadEventBComponent = new EMFRodinDB().loadEventBComponent((IInternalElement)obj);
				sourceElement = loadEventBComponent ;
			
			
				if(sourceElement instanceof Machine){
					Machine mch = (Machine) sourceElement;
					IFile sourceFile = WorkspaceSynchronizer.getFile(mch.eResource());
					
					String destination =  createFolders(sourceFile.getProject());
					DiagramExporter.exportDiagram(mch, destination);
					
					DocumentGenerator.createElemFile(mch, 0);
										
			   }
				else if (sourceElement instanceof Context){
					Context ctx = (Context) sourceElement;
					IFile sourceFile = WorkspaceSynchronizer.getFile(ctx.eResource());
					String destination =  createFolders(sourceFile.getProject());
					
					DiagramExporter.exportDiagram(ctx, destination);
					DocumentGenerator.createElemFile(ctx, 0);
					
				}
			
				
			}else if (obj instanceof IProject) {
				sourceElement = null;
				IProject proj = (IProject) obj;
				IRodinProject rodinProject = RodinCore.valueOf(proj);
				
				String destination = createFolders(rodinProject.getProject());
				String content = DocumentGenerator.beginDocument();

				//statistics
				List <IModelElement> elements = new  ArrayList<IModelElement>();
				elements.add(ModelController.getModelElement(rodinProject));

				try {
					    //begin document
						for(IRodinElement child : rodinProject.getChildren()){
				
							if(child instanceof IRodinFile ){
								EventBElement eventBComponent = new EMFRodinDB().loadEventBComponent(((IRodinFile) child).getRoot());
								if(eventBComponent instanceof Machine || eventBComponent instanceof Context){
									DiagramExporter.exportDiagram(eventBComponent, destination);	
									String name = DocumentGenerator.createElemFile(eventBComponent, 1);
								   
									
									content += DocumentGenerator.includeFile(name);	 
								    
								    //statistics
								    elements.add(ModelController.getModelElement(((IRodinFile) child).getRoot()));
									
								  //add created file to the report
								}//end if machine context
									
							}//end if RodinFile
							
							
						}//end for
						
						//statistics
						String stat = ProofStatisticsExporter.generateStatistics(elements, rodinProject.getElementName());
						//create proof statistics file
						String chContent = DocumentGenerator.beginChapter("Proof Statistics");
						chContent += stat;
						DocumentGenerator.generateFile("proofStatistics.tex", proj, chContent);
						//include proof statistics
						content += DocumentGenerator.includeFile("proofStatistics");
			
						//end document
						content += DocumentGenerator.endDocument();						
						//generate project file
						
						String fileName = proj.getName() + ".tex";
						DocumentGenerator.generateFile(fileName, proj , content);
					} catch (RodinDBException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
				
			}else return null;
	
		} else return null;
		return sourceElement;	
	}

	private  String createFolders(IProject proj){
		String destination = "";
		IFolder folderDocs = proj.getFolder(documentsFolder);
		IFolder folderFigures=null;
		
		if(folderDocs.exists()){
			folderFigures = folderDocs.getFolder(figuresFolder);
			if (!folderFigures.exists())
				try {
						folderFigures.create(false, true, null);
				} catch (CoreException e) {
					throw new RuntimeException("Could not create figures folder.", e);
			}
			IFile file = folderDocs.getFile("lstEventB.sty");
			if (!file.exists())
				StyleFile.createStyleFile(proj);
		}
		else{
			try {
    			folderDocs.create(true, true, null);
    			
    			StyleFile.createStyleFile(proj);
    			
				folderFigures = folderDocs.getFolder(figuresFolder);
				folderFigures.create(false, true, null);
			} catch (CoreException e) {
			throw new RuntimeException("Could not create documents folder.", e);
		}
		}
      
		IPath loc = folderFigures.getLocation().addTrailingSeparator();
		destination = loc.toOSString();
			
		
		return destination;
		   
	}

}
