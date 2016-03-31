package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarException;

/**
 * Associé aux agentServer
 * Cotient les Agent.jar
 * @author Quentin
 *
 */
public class BAMAgentClassLoader extends URLClassLoader {
	
	private Map<String, Class> contents;

	public BAMAgentClassLoader(URL[] urls) throws JarException, IOException {
		super(urls);
		this.contents = new HashMap<>();
		
		for(URL url : urls) 
			addToContents(url);
	}

	/**
	 * Fusionne le contenu d'un jar avec les class pré-chargés
	 * @param url l'url
	 * @throws JarException
	 * @throws IOException
	 */
	public void addToContents (URL url) throws JarException, IOException {
		Jar jar = new Jar(url.getPath());
		
		for (Iterator it = jar.classIterator().iterator(); it.hasNext();) {
			Entry<String, byte[]> entry = (Entry<String,byte[]>) it.next();
			contents.put(entry.getKey(),  defineClass(entry.getKey(), entry.getValue(), 0, entry.getValue().length));
		}
	}
	
	public Class getClass(String className){
		if(contents.containsKey(className))
			return contents.get(className);
		else
			return null;
	}
	
}
