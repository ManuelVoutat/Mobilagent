package jus.aor.mobilagent.kernel;

import java.net.URL;
import java.net.URLClassLoader;

public class BAMServerClassLoader extends URLClassLoader{

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