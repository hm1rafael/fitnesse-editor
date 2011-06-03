package fitedit.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import fitedit.dialogs.FitResourceSelectionDialog;

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
		dialog.setTitle("Open Fitnesse");
		dialog.open();

		return null;
	}
}
