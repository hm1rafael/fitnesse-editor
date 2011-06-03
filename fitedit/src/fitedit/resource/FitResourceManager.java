package fitedit.resource;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import fitedit.Constants;

public class FitResourceManager {
	private static FitResourceManager instance;
	private Set<FitResource> resouces = new TreeSet<FitResource>();

	private FitResourceManager() {
		createIndex();
	}

	public Set<FitResource> getResouces() {
		return resouces;
	}

	public void add(FitResource r) {
		resouces.add(r);
	}

	public void remvoe(FitResource r) {
		resouces.remove(r);
	}

	public static FitResourceManager getInstance() {
		if (instance == null) {
			instance = new FitResourceManager();
		}

		return instance;
	}

	private void createIndex() {
		Job job = new Job("Fitnesse Index") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IProject[] projects = root.getProjects();
				monitor.beginTask("Refreshing...", projects.length);
				try {
					int progress = 0;
					for (IProject prj : projects) {
						progress++;
						monitor.worked(progress);

						prj.accept(new IResourceVisitor() {

							@Override
							public boolean visit(IResource resource)
									throws CoreException {
								registerFitResouce(resource);
								return true;
							}
						});
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}

				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
	}

	public static boolean isFitResouce(IResource resource) {
		if (!Constants.CONTENT_TXT.equals(resource.getName())) {
			return false;
		}

		if (resource.getFullPath().toString().indexOf(Constants.FITNESSE_ROOT) < 0) {
			return false;
		}

		return true;
	}

	private void registerFitResouce(IResource resource) {
		if (!isFitResouce(resource)) {
			return;
		}

		IContainer parent = resource.getParent();

		if (parent == null) {
			return;
		}

		resouces.add(new FitResource(parent.getName(), parent.getFullPath()
				.toString()));
	}
}
