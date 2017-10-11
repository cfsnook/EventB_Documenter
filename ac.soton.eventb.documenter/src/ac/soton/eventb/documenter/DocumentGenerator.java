package ac.soton.eventb.documenter;

import java.io.InputStream;

import org.apache.tools.ant.filters.StringInputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eventb.emf.core.machine.Machine;

public class DocumentGenerator {
	final static String documentsFolder = "EventB_Documents";
	public static void createMachineFile(Machine machine){
		String fileName = machine.getName() + ".tex";
		IFolder folder = WorkspaceSynchronizer.getFile(machine.eResource()).getProject().getFolder(documentsFolder);
		if (folder.exists()){
			IFile file = folder.getFile(fileName);
		if(!file.exists())
		{
			InputStream input = new StringInputStream("test");
			try {
				file.create(input,true , null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
			
		}
	}
	public static void includeDiagrams(String machineName){
		
	}

}
