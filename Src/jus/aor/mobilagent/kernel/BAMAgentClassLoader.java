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
		super(new URL[] {}); //Fake
		this.contents = new HashMap<String, Class<?>>();
		for(URL url : urls)
			addJar(url);
	} 
	
	public BAMAgentClassLoader(URL[] urls, ClassLoader loader) {
		super(new URL[]{},loader);
		this.contents = new HashMap<String, Class<?>>();
		this.parent = loader;
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
		
		addJar(jar);
	}
	
	/**
	 * Fusion d'un jar avec les classes
	 */
	@SuppressWarnings("deprecation")
	public void addJar (Jar jar) {
		for (Iterator<?> it = jar.classIterator().iterator(); it.hasNext();) {
			@SuppressWarnings("unchecked")
			Entry<String, byte[]> entry = (Entry<String, byte[]>) it.next();
			String className = entry.getKey();
			className = className.substring(className.lastIndexOf('/')+1);
			byte[] b = entry.getValue();
			Class<?> c= null;
			c = defineClass(b, 0, entry.getValue().length);
			this.contents.put(className,  c);
		}
	}
	
	public Class<?> getClass(String classname) throws ClassNotFoundException {
		classname = classname.replace('.', '/').concat(".class");
		
		if(contents.containsKey(classname))
			return contents.get(classname);
		//Si on a un parent loader, lui demander
		else if (parent != null)
			return parent.loadClass(classname);
		throw new RuntimeException("Pas de classe trouvé : "+classname);
	}
}