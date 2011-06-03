package fitedit.resource;

public class FitResource implements Comparable<FitResource> {

	private String name;
	private String path;

	public FitResource(String name, String path) {
		super();
		this.name = name;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return name + " - " + path;
	}

	@Override
	public int compareTo(FitResource o) {
		int cmp = getName().compareTo(o.getName());
		if (cmp == 0) {
			return getPath().compareTo(o.getPath());
		}
		return cmp;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FitResource)) {
			return false;
		}

		FitResource o = (FitResource) obj;
		if (!getName().equals(o.getName())) {
			return false;
		}
		return getPath().equals(o.getPath());
	}

	@Override
	public int hashCode() {
		return name.hashCode() + path.hashCode();
	}
}
