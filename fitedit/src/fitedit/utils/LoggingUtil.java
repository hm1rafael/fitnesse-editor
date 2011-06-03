package fitedit.utils;

import org.eclipse.core.runtime.Status;

import fitedit.Activator;

public class LoggingUtil {
	public static void log(int severity, String msg) {
		Activator.getDefault().getLog()
				.log(new Status(severity, Activator.PLUGIN_ID, msg));
	}
}
