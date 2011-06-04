package fitedit.popup.actions;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;

import fitedit.utils.FitUtil;

public class OpenBrowserAction implements IActionDelegate {

	/**
	 * Latest selected resource.
	 */
	ISelection selection = null;

	@Override
	public void run(IAction action) {
		if (selection == null) {
			return;
		}

		IResource resource = FitUtil.getResource(selection);

		if (resource == null) {
			return;
		}

		FitUtil.openFitnesseInBrowser(resource);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

}
