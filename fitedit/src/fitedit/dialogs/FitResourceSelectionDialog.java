package fitedit.dialogs;

import java.util.Comparator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

import fitedit.Activator;
import fitedit.resource.FitResource;
import fitedit.resource.FitResourceManager;

public class FitResourceSelectionDialog extends FilteredItemsSelectionDialog {

	public FitResourceSelectionDialog(Shell shell, boolean b) {
		super(shell, b);
	}

	@Override
	protected Control createExtendedContentArea(Composite parent) {
		return null;
	}

	@Override
	protected IDialogSettings getDialogSettings() {
		IDialogSettings settings = Activator.getDefault().getDialogSettings()
				.getSection("sectoin");
		if (settings == null) {
			settings = Activator.getDefault().getDialogSettings()
					.addNewSection("section");
		}
		return settings;
	}

	@Override
	protected IStatus validateItem(Object item) {
		return Status.OK_STATUS;
	}

	@Override
	protected ItemsFilter createFilter() {
		return new ItemsFilter() {
			public boolean matchItem(Object item) {
				FitResource r = (FitResource) item;
				return matches(r.getName().toString());
			}

			public boolean isConsistentItem(Object item) {
				return true;
			}
		};

	}

	@Override
	protected Comparator getItemsComparator() {
		return new Comparator() {
			public int compare(Object arg0, Object arg1) {
				FitResource a = (FitResource) arg0;
				FitResource b = (FitResource) arg1;

				int r = a.getName().compareTo(b.getName());
				if (r == 0) {
					r = a.getPath().compareTo(b.getPath());
				}

				return r;
			}
		};
	}

	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider,
			ItemsFilter itemsFilter, IProgressMonitor progressMonitor)
			throws CoreException {
		progressMonitor.beginTask("Open..", 10);

		for (FitResource r : FitResourceManager.getInstance().getResouces()) {
			contentProvider.add(r, itemsFilter);
		}

		progressMonitor.done();
	}

	@Override
	public String getElementName(Object item) {
		FitResource r = (FitResource) item;
		return r.getName().toString();
	}

}
