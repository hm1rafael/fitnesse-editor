package fitedit.resource;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import fitedit.utils.DBUtil;

public class ResourceDatabase {
	private static ResourceDatabase instance;

	private DBUtil db;

	private ResourceDatabase() {
		try {
			db = new DBUtil(DriverManager.getConnection(
					"jdbc:hsqldb:file:fitdb;shutdown=true", "sa", ""));
			init();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ResourceDatabase getInstance() {
		if (instance == null) {
			instance = new ResourceDatabase();
		}
		return instance;
	}

	public void init() {
		init(false);
	}

	public void init(boolean force) {
		if (force || isInitRequired()) {
			db.execute("drop table FitTest if exists");
			db.execute("drop index IDX1 if exists");
			db.execute("drop index IDX2 if exists");

			db.execute("create table FitTest ( name varchar(100), path varchar(1000) )");
			db.execute("create index IDX1 on FitTest ( name )");
			db.execute("create index IDX2 on FitTest ( path )");

			createIndex();
		}
	}

	private boolean isInitRequired() {
		List<Map<String, Object>> res = db
				.select("select count(*) as CNT from information_schema.system_tables where table_schem = 'PUBLIC' and table_name = 'FitTest'");
		Integer cnt = (Integer) res.get(0).get("CNT");
		return cnt != 0;
	}

	public void add(String name, String path) {
		db.execute("insert into FitTest (name, path) values (?, ?)", name, path);
	}

	public void delete(String path) {
		db.execute("delete from FitTest where path = ?", path);
	}

	public List<Map<String, Object>> get(String pattern) {
		if (pattern == null)
			return new ArrayList<Map<String, Object>>();

		pattern = pattern.replaceAll("_", "\\_");
		pattern.replaceAll("\\*", "%");
		pattern.replaceAll("\\?", "_");
		return db.select("select * from FitTest where name like ?", pattern);
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

		add(parent.getName(), parent.getFullPath().toString());
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
}
