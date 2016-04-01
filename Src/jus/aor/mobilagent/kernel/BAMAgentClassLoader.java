package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Associé aux agentServer
 * Cotient les Agent.jar
 * @author Quentin
 *
 */
public class BAMAgentClassLoader extends BAMServerClassLoader {
	
	protected Map<String, Class<?>> contents;
	
	private ClassLoader parent = null;
	
	public BAMAgentClassLoader(URL[] urls) {
		super(new URL[] {}); //Fake : to not allow the parent to have our classes
		this.contents = new HashMap<String, Class<?>>();
		for(URL url : urls)
			addJar(url);
	} 
	
	public BAMAgentClassLoader(URL[] urls, ClassLoader loader) {
		super(new URL[]{},loader);
		contents = new HashMap<String, Class<?>>();
		parent = loader;
		for(URL url : urls)
			addJar(url);
	}
	
	public void addJar (URL url ) {
		Jar jar = null;
		try {
			jar = new Jar(url.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (Iterator it = jar.classIterator().iterator(); it.hasNext();) {
			Entry<String, byte[]> entry = (Entry<String, byte[]>) it.next();
			contents.put(entry.getKey(),  defineClass(entry.getValue(), 0, entry.getValue().length));
		}
	}
	
	/**
	 * Fusion d'un jar avec les classes
	 */
	public void addJar (Jar jar) {
		for (Iterator it = jar.classIterator().iterator(); it.hasNext();) {
			Entry<String, byte[]> entry = (Entry<String, byte[]>) it.next();
			contents.put(entry.getKey(),  defineClass(entry.getValue(), 0, entry.getValue().length));
		}
	}
	
	public Class<?> getClass(String classname) throws ClassNotFoundException {
		classname.replace('.', '/').concat(".class");
		if(contents.containsKey(classname))
			return contents.get(classname);
		//if we have a parent loader let's ask him
		else if (parent != null)
			return parent.loadClass(classname);
		throw new RuntimeException("No class of that name found : "+classname);
	}
}