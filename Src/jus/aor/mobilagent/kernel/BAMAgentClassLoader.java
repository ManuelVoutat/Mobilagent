package jus.aor.mobilagent.kernel;

import java.net.URL;

/**
 * Associé aux agentServer
 * Cotient les Agent.jar
 * @author Quentin
 *
 */
public class BAMAgentClassLoader extends BAMServerClassLoader {
	
	private ClassLoader parent = null;
	
	public BAMAgentClassLoader(URL[] urls)  {
		super(urls);
	} 
	
	public void setParent(ClassLoader parent) {
		if (parent == null)
			this.parent = parent;
		throw new RuntimeException("BAMAgentClassLoader a deja un parent de defini");
	}
}
