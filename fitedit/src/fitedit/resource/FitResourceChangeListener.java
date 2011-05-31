package fitedit.resource;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

public class FitResourceChangeListener implements IResourceChangeListener {

	FitIndexRefresher refresher;

	public FitResourceChangeListener() {
		refresher = new FitIndexRefresher();
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		try {
			event.getDelta().accept(refresher);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	class FitIndexRefresher implements IResourceDeltaVisitor {

		final List<Integer> TARGET_CHANGES = Arrays.asList(
				IResourceDelta.ADDED, IResourceDelta.REMOVED);

		/**
		 * Returns: true if the resource delta's children should be visited;
		 * false if they should be skipped.
		 */
		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			StringBuffer buf = new StringBuffer(80);
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				buf.append("ADDED");
				break;
			case IResourceDelta.REMOVED:
				buf.append("REMOVED");
				break;
			case IResourceDelta.CHANGED:
				buf.append("CHANGED");
				break;
			default:
				buf.append("[");
				buf.append(delta.getKind());
				buf.append("]");
				break;
			}
			buf.append(" ");
			buf.append(delta.getResource());
			buf.append(" + ");
			buf.append(delta.getResource().getFullPath());
			buf.append(" + ");
			buf.append(delta.getResource().getLocation());
			buf.append(" + ");
			buf.append(delta.getResource().getName());

			System.out.println(buf);

			return true;
		}

	}

}
