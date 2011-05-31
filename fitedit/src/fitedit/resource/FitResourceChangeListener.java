package fitedit.resource;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
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
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
			case IResourceDelta.REMOVED:
				processResource(delta);
				break;
			default:
				break;
			}

			return true;
		}

		private void processResource(IResourceDelta delta) {
			IResource resource = delta.getResource();
			if (resource == null) {
				return;
			}

			if (!ResourceDatabase.isFitResouce(resource)) {
				return;
			}

			IContainer parent = resource.getParent();

			if (delta.getKind() == IResourceDelta.ADDED) {
				ResourceDatabase.getInstance().add(parent.getName(),
						parent.getFullPath().toString());
			}
			if (delta.getKind() == IResourceDelta.REMOVED) {
				ResourceDatabase.getInstance().delete(
						parent.getFullPath().toString());
			}
		}

	}

}
