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
package ac.soton.eventb.documenter;

import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.diagram.ui.render.util.DiagramRenderUtil;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eventb.emf.core.AbstractExtension;
import org.eventb.emf.core.EventBElement;
import org.eventb.emf.core.EventBNamed;
import org.eventb.emf.core.context.Context;
import org.eventb.emf.core.machine.Machine;
import ac.soton.eventb.emf.diagrams.navigator.DiagramsNavigatorExtensionPlugin;
import ac.soton.eventb.emf.diagrams.navigator.provider.IDiagramProvider;
/**
 * <p>
 * Export diagrams in Event-B projects  e.g. iUML-B class diagrams, statemachines...
 * </p>
 * @author dd4g12
 * @version 0.0.1
 * @since 0.0.1
 */
public class DiagramExporter {
	
	/**
	 * Export and save diagrams as PNG images
	 * 
	 * @param an EventBElement (context or machine) and a String representing the 
	 * location where images will be saved
	 * @return
	 */
	public static void exportDiagram (EventBElement element, String destination){
		if(element instanceof Machine || element instanceof Context){
			String diagramName = "";
			String failedDiag = "";

			for (AbstractExtension ext: element.getExtensions()){
				if(ext instanceof ac.soton.eventb.emf.diagrams.Diagram && ext instanceof EventBNamed){
					//----check-- possibly needed in case of inclusion for example
					//exportDiagram(ext, destination);
					//-----------------------------------------------------------
					EventBNamed e= (EventBNamed) ext;
					diagramName = ((EventBNamed)element).getName() +"_" + e.getName();
					ac.soton.eventb.emf.diagrams.Diagram extDiagram = (ac.soton.eventb.emf.diagrams.Diagram) ext;
				    org.eclipse.gmf.runtime.notation.Diagram diag = getDiagramToOpen(extDiagram);
					if(diag != null){
						Image image = DiagramRenderUtil.renderToSWTImage(diag);
						ImageLoader loader = new ImageLoader();
						loader.data = new ImageData[]{image.getImageData()};
					//	loader.save(destination + diagramName + ".PNG", SWT.IMAGE_PNG);
						// Need to catch the problem when location doesn't exist?
						loader.save(destination + diagramName+ ".png", SWT.IMAGE_PNG);
					//	setStatusMessage("successful exported " + diagramName,false);
					}else{
						// alert 
						System.err.println("There is no diagram layout available for diagram: " + diagramName);
						if (failedDiag  == "")
							failedDiag = failedDiag + diagramName;
						else
							failedDiag = failedDiag + ", "  + diagramName;
						}		    	
				}
				
			}// end of for
			if (failedDiag == "")
		    	setStatusMessage("All Diagrams are exported successfully", false);
		    
		    else{
		    	setStatusMessage("There is no diagram file available for diagrams: " + failedDiag, true);
		    }
		}				
	}
	
	/**
	 * Returns a diagram element to open.
	 * 
	 * @param resource a diagram resource
	 * @return
	 */
	private static org.eclipse.gmf.runtime.notation.Diagram getDiagramToOpen(EObject de1) {
		org.eclipse.gmf.runtime.notation.Diagram diagram = null;
		Resource resource = getDiagramResource(de1);
		if(resource != null){
			String ref1;
			if (de1 instanceof EventBElement && (ref1 = ((EventBElement)de1).getReference())!=null ){
				for (EObject e : resource.getContents()){
					EObject de2 = ((org.eclipse.gmf.runtime.notation.Diagram) e).getElement();			
					if (e instanceof org.eclipse.gmf.runtime.notation.Diagram && de2 instanceof EventBElement &&(ref1.equals(((EventBElement)de2).getReference()))){// 
						diagram = (org.eclipse.gmf.runtime.notation.Diagram) e;
						break;
					}
				}				
			}
		}
	
		return diagram;
	}
	
	// Return diagram resource
	private static Resource getDiagramResource(EObject element) {
		
		// find diagram provider
		Map<String, IDiagramProvider> registry = DiagramsNavigatorExtensionPlugin.getDefault().getDiagramProviderRegistry();
		String type = element.eClass().getName();
		IDiagramProvider provider = registry.get(type);
		
		String filename = provider.getDiagramFileName(element);
		
		IFile domainFile = WorkspaceSynchronizer.getFile(element.eResource());
		IProject project = domainFile.getProject();
		IFile diagramFile = project.getFile(filename);

		URI diagramURI = URI.createPlatformResourceURI(diagramFile.getFullPath().toOSString(), true);
	  
		
		if(diagramFile.exists()){
			Resource diagramRes = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain().getResourceSet().getResource(diagramURI, true);
			return diagramRes;
		}
		
	
		else{
		   return null;
		}
	
	}
	
	// Update status bar
	private static void setStatusMessage(String message, boolean error) {
        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow == null) {
            return;
        }
        IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
        if (page == null) {
            return;
        }
        IViewPart view = page.findView("fr.systerel.explorer.navigator.view");
        if (view == null) {
            return;
        }
        IViewSite site = view.getViewSite();
        IStatusLineManager slManager = site.getActionBars().getStatusLineManager();
        if (error) {
            slManager.setErrorMessage(message);
            //slManager.update(true);
        } else {
            slManager.setMessage(message);
            //slManager.update(true);
        }
    }
	

}
