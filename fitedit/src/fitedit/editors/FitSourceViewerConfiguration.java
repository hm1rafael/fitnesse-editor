package fitedit.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.formatter.MultiPassContentFormatter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import fitedit.editors.syntaxrules.ColorManager;
import fitedit.editors.syntaxrules.FitScanner;
import fitedit.editors.syntaxrules.FitSourcePartitionScanner;
import fitedit.editors.syntaxrules.IFitColorConstants;
import fitedit.editors.syntaxrules.NonRuleBasedDamagerRepairer;

public class FitSourceViewerConfiguration extends SourceViewerConfiguration {
	private static FitScanner fitScanner = null;
	private static ColorManager colorManager = new ColorManager();

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			FitSourcePartitionScanner.FIT_COMMENT,
			};
	}
	
	protected FitScanner getFitScanner() {
		if (fitScanner == null) {
			fitScanner = new FitScanner(colorManager);
			fitScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IFitColorConstants.DEFAULT))));
		}
		return fitScanner;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr =
			new DefaultDamagerRepairer(getFitScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(IFitColorConstants.COMMENT)));
		reconciler.setDamager(ndr, FitSourcePartitionScanner.FIT_COMMENT);
		reconciler.setRepairer(ndr, FitSourcePartitionScanner.FIT_COMMENT);

		return reconciler;
	}
	
	@Override
	public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
		MultiPassContentFormatter formatter= 
			new MultiPassContentFormatter(
					getConfiguredDocumentPartitioning(sourceViewer), 
					IDocument.DEFAULT_CONTENT_TYPE);
		
		formatter.setMasterStrategy(new FitnesseFormattingStrategy());
		
		return formatter;
	}

}