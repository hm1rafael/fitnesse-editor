package fitedit.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;

import fitedit.Constants;
import fitedit.dialogs.FitResourceLabelProvider;
import fitedit.dialogs.FitResourceSelectionDialog;
import fitedit.resource.FitResource;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class OpenFitnesseResouceHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public OpenFitnesseResouceHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);

		FitResourceSelectionDialog dialog = new FitResourceSelectionDialog(
				window.getShell(), true);
		dialog.setListLabelProvider(new FitResourceLabelProvider());
		dialog.setTitle("Open FitNesse");
		int returnCode = dialog.open();

		if (returnCode != FitResourceSelectionDialog.OK) {
			return null;
		}

		FitResource r = (FitResource) dialog.getFirstResult();
		if (r == null) {
			return null;
		}

		IFile file = ResourcesPlugin
				.getWorkspace()
				.getRoot()
				.getFile(
						new Path(r.getPath() + IPath.SEPARATOR
								+ Constants.CONTENT_TXT));
		if (file == null) {
			return null;
		}

		IWorkbenchPage page = window.getActivePage();
		try {
			IDE.openEditor(page, file, true);
		} catch (PartInitException e) {
			e.printStackTrace();
		}

		return null;
	}
}
