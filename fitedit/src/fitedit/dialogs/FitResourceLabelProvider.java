package fitedit.dialogs;

import org.eclipse.jface.viewers.LabelProvider;

import fitedit.resource.FitResource;

public class FitResourceLabelProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		if (element instanceof FitResource) {
			return ((FitResource) element).getName();
		}

		return super.getText(element);
	}
}
