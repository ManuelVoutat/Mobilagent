package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarException;

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