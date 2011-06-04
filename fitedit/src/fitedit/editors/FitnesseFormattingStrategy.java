package fitedit.editors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy;
import org.eclipse.jface.text.formatter.FormattingContextProperties;
import org.eclipse.jface.text.formatter.IFormattingContext;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Scriptable;

public class FitnesseFormattingStrategy extends ContextBasedFormattingStrategy {

	/** Documents to be formatted by this strategy */
	private final List<IDocument> documents = new LinkedList<IDocument>();

	// /** Partitions to be formatted by this strategy */
	// private final LinkedList fPartitions = new LinkedList();

	@Override
	public String format(String arg0, boolean arg1, String arg2, int[] arg3) {
		return "orange";
	}

	@Override
	public void format() {
		super.format();
		if (documents.size() == 0) {
			return;
		}
		final IDocument document = documents.remove(0);
		String text = document.get();
		String formattedText = fitnesseFormat(text);
		document.set(formattedText);
	}

	String fitnesseFormat(String wikiText) {
		Context cx = Context.enter();
		try {
			// Initialize the standard objects (Object, Function, etc.)
			// This must be done before scripts can be executed. Returns
			// a scope object that we use in later calls.
			Scriptable scope = cx.initStandardObjects();
			cx.setErrorReporter(new ErrorReporter() {

				@Override
				public void warning(String arg0, String arg1, int arg2,
						String arg3, int arg4) {
					System.err.println(String.format("%s %s %s %s %s", arg0,
							arg1, arg2, arg3, arg4));

				}

				@Override
				public EvaluatorException runtimeError(String arg0,
						String arg1, int arg2, String arg3, int arg4) {
					System.err.println(String.format("%s %s %s %s %s", arg0,
							arg1, arg2, arg3, arg4));
					return null;
				}

				@Override
				public void error(String arg0, String arg1, int arg2,
						String arg3, int arg4) {
					System.err.println(String.format("%s %s %s %s %s", arg0,
							arg1, arg2, arg3, arg4));

				}
			});

			// Make the editor's text available in javascript
			scope.put("wikiText", scope, wikiText);

			Object result = null;
			cx.evaluateReader(scope, new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream("WikiFormatter.js"))),
					"WikiFormatter.js", 0, null);
			result = cx.evaluateString(scope,
					"new WikiFormatter().format(wikiText);", "<cmd>", 1, null);

			// Convert the result to a string and print it.
			return Context.toString(result);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Exit from the context.
			Context.exit();
		}

		return wikiText;
	}

	public void formatterStarts(final IFormattingContext context) {
		super.formatterStarts(context);

		// fPartitions.add(context
		// .getProperty(FormattingContextProperties.CONTEXT_PARTITION));

		Object obj = context
				.getProperty(FormattingContextProperties.CONTEXT_MEDIUM);
		if (obj instanceof IDocument) {
			documents.add((IDocument) obj);
		}

	}

	/*
	 * @see org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy#
	 * formatterStops()
	 */
	public void formatterStops() {
		super.formatterStops();

		// fPartitions.clear();
		documents.clear();
	}

}
