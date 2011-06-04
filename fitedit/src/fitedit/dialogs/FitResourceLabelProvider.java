package fitedit.dialogs;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;

import fitedit.resource.FitResource;
import fitedit.utils.FitUtil;

public class FitResourceLabelProvider extends LabelProvider implements
		IStyledLabelProvider {

	private static final String CONCAT_STRING = " - ";

	@Override
	public String getText(Object element) {
		if (element instanceof FitResource) {
			FitResource r = (FitResource) element;
			return getBasicText(r);
		}

		return super.getText(element);
	}

	@Override
	public StyledString getStyledText(Object element) {
		if (element == null) {
			return new StyledString();
		}

		if (!(element instanceof FitResource)) {
			return new StyledString(element.toString());
		}

		String text = getBasicText((FitResource) element);

		StyledString string = new StyledString(text);
		int index = text.indexOf(CONCAT_STRING);

		if (index != -1) {
			string.setStyle(index, text.length() - index,
					StyledString.QUALIFIER_STYLER);
		}
		return string;
	}

	String getBasicText(FitResource r) {
		if (r == null) {
			return null;
		}

		return r.getName() + CONCAT_STRING
				+ FitUtil.getFitnesseUrl(r.getPath());
	}
}
