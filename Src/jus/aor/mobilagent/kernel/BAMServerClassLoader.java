package jus.aor.mobilagent.kernel;

import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;

public class BAMServerClassLoader extends URLClassLoader implements Serializable{

	private static final long serialVersionUID = 1L;

	public BAMServerClassLoader(URL[] urls) {
		super(urls);
	}
	
	public BAMServerClassLoader(URL[] urls, ClassLoader loader) {
		super(urls, loader);
	}

	protected void addURL(URL url) {
		super.addURL(url);
	}
	
}