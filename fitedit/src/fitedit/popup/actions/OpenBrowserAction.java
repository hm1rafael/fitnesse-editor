package fitedit.popup.actions;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;

import fitedit.Activator;
import fitedit.Constants;
import fitedit.preferences.PreferenceConstants;

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

		IResource resouce = getResouce();

		if (resouce == null) {
			return;
		}

		String prefix = Activator.getDefault().getPreferenceStore()
				.getString(PreferenceConstants.P_URL_PREFIX);
		String url = prefix + getFitnesseUrl(resouce.getFullPath().toString());
		System.out.println("done.." + url);
		openDesktopBrowser(url);
	}

	private IResource getResouce() {
		if (selection == null) {
			return null;
		}

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			Iterator e = structuredSelection.iterator();
			while (e.hasNext()) {
				Object element = e.next();
				if (element instanceof IResource) {
					return (IResource) element;
				} else if (element instanceof IAdaptable) {
					IAdaptable adaptable = (IAdaptable) element;
					Object adapter = adaptable.getAdapter(IResource.class);
					if (adapter instanceof IResource) {
						return (IResource) adapter;
					}
				}
			}
		}

		return null;
	}

	private void openDesktopBrowser(String url) {
		Desktop desktop = Desktop.getDesktop();

		if (!desktop.isSupported(Desktop.Action.BROWSE)) {
			System.err.println("Desktop doesn't support the browse action.");
			return;
		}

		URI uri;
		try {
			uri = new URI(url);
			desktop.browse(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	String getFitnesseUrl(String path) {
		if (path == null) {
			return null;
		}

		String[] paths = path.split(Constants.FITNESSE_ROOT);
		if (paths.length <= 1) {
			return "";
		}

		String fitPath = paths[1];
		fitPath = fitPath.replace(Constants.CONTENT_TXT, "");
		if (fitPath.startsWith("/")) {
			fitPath = fitPath.substring(1);
		}
		if (fitPath.endsWith("/")) {
			fitPath = fitPath.substring(0, fitPath.length() - 1);
		}
		fitPath = fitPath.replaceAll("/", ".");
		return fitPath;
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

}
