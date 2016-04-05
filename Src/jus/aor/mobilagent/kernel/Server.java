/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import jus.aor.mobilagent.kernel.BAMAgentClassLoader;
import jus.aor.mobilagent.kernel._Agent;

/**
 * Le serveur principal permettant le lancement d'un serveur d'agents mobiles et les fonctions permettant de déployer des services et des agents.
 * @author     Morat
 */
public final class Server {
	/** le nom logique du serveur */
	protected String name;
	/** le port où sera ataché le service du bus à agents mobiles. Pafr défaut on prendra le port 10140 */
	protected int port=10140;
	/** le server d'agent démarré sur ce noeud */
	protected AgentServer agentServer;
	/** le nom du logger */
	protected String loggerName;
	/** le logger de ce serveur */
	protected Logger logger=null;
	/** Le ClassLoader pour les agents */
	//protected BAMAgentClassLoader loader;
	/** Le ClassLoader pour le server */
	protected BAMAgentClassLoader serverLoader;
	
	/**
	 * Démarre un serveur de type mobilagent 
	 * @param port le port d'écuote du serveur d'agent 
	 * @param name le nom du serveur
	 */
	public Server(final int port, final String name){
		this.name=name;
		this.port=port;
		try {
			/* mise en place du logger pour tracer l'application */
			this.loggerName = "jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.name;
			this.logger=Logger.getLogger(loggerName);

			System.out.println("[SERVER] - Instanciation du Loader");
			this.serverLoader = new BAMAgentClassLoader(new URL[]{}, this.getClass().getClassLoader());
			/* démarrage du server d'agents mobiles attaché à cette machine */
			System.out.println("[SERVER] - Demarage du server d'agents mobile");
			agentServer = new AgentServer(name, port, this.serverLoader);
			agentServer.start();
			/* temporisation de mise en place du server d'agents */
			Thread.sleep(1000);
		}catch(Exception ex){
			logger.log(Level.FINE,"[SERVER] - Erreur lors du lancement du serveur"+this,ex);
			return;
		}
	}
	/**
	 * Ajoute le service caractérisé par les arguments
	 * @param name nom du service
	 * @param classeName classe du service
	 * @param codeBase codebase du service
	 * @param args arguments de construction du service
	 */
	public final void addService(String name, String classeName, String codeBase, Object... args) {
		try {
			System.out.println("[SERVER] - Ajout d'un service ");
			logger.log(Level.FINE," [SERVER] - Ajout d'un service ");
			//Charge le code du service dans le BAMServerClassLoader
			String jarPath = "file:///"+System.getProperty("user.dir")+codeBase;
			
			this.serverLoader.addJar(new URL(jarPath));
			//Recupere l'objet class de la classe className du Jar
			System.out.println("[SERVER] - Recuperation de l'object class :" +classeName);
			Class<?> serviceClass = this.serverLoader.findClass(classeName);
			//Instancie ce service au sein d'un objet de type _Service
			System.out.println("[SERVER] - Instantiation de l'objet ");
			_Service<?> service = (_Service<?>) serviceClass.getConstructor(Object[].class).newInstance(new Object[]{args});
			//Ajoute le service a l'agentServer
			System.out.println("[SERVER] - Ajout du service a l'agentServer");
			this.agentServer.addService(service,name);

		}catch(Exception ex){
			System.out.println("[SERVER] - Erreur lors du lancement du serveur ");
			this.logger.log(Level.FINE,"[SERVER] - Erreur lors du lancement du serveur"+this,ex);
			return;
		}
	}
	/**
	 * deploie l'agent caractérisé par les arguments sur le serveur
	 * @param classeName classe du service
	 * @param args arguments de construction de l'agent
	 * @param codeBase codebase du service
	 * @param etapeAddress la liste des adresse des étapes
	 * @param etapeAction la liste des actions des étapes
	 */
	public final void deployAgent(String classeName, Object[] args, String codeBase, List<String> etapeAddress, List<String> etapeAction) {
		try {
			System.out.println("[SERVER] - Deploiment d'un agent ");
			logger.log(Level.FINE,"[SERVER] - Deploiment d'un agent ");
			String jarPath = "file:///"+System.getProperty("user.dir")+codeBase;
			//Le deploiement d'un agent se fait sur un classLoader fils du classLOader actuel
			@SuppressWarnings("resource")
			BAMAgentClassLoader agentLoader = new BAMAgentClassLoader(new URL[]{}, this.serverLoader);
			agentLoader.addJar(new URL(jarPath));
			Class<?> agentClass = agentLoader.findClass(classeName);
			System.out.println("[SERVER] - Instanciation de l'agent ");
			Agent agent = (Agent) agentClass.getConstructor(Object[].class).newInstance(new Object[]{args});
			agent.setJar(new URL(jarPath));
			System.out.println("[SERVER] - Initialisation l'agent ");
			agent.init(agentServer, "mobilagent://" + name + ":" + port +"/");
			System.out.println("[SERVER] - Initialisation de la route ");
			for(int i=0; i<etapeAddress.size(); i++) {
				Field field = agentClass.getDeclaredField(etapeAction.get(i));
				field.setAccessible(true);
				_Action action = (_Action) field.get(agent);
				
				URI server = new URI(etapeAddress.get(i));
				Etape etape =  new Etape(server, action);
				agent.addEtape(etape);
			}
			new Thread(agent).start();
			System.out.println("[SERVER] Agent deployé ");
		}catch(Exception ex){
			System.out.println("[SERVER] - Erreur lors du deploiment de l'agent");
			logger.log(Level.FINE,"[SERVER] - Erreur lors du deploiment de l'agent"+this,ex);
			return;
		}
	}
	
	//-.- 
	/**
	 * Primitive permettant de "mover" un agent sur ce serveur en vue de son exécution
	 * immédiate.
	 * @param agent l'agent devant être exécuté
	 * @param loader le loader à utiliser pour charger les classes.
	 * @throws Exception
	 */
	protected void startAgent(_Agent agent, BAMAgentClassLoader loader) throws Exception {
		System.out.println("[SERVER] - START AGENT?");
		//A COMPLETER
	}
}
