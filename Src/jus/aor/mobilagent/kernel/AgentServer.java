package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AgentServer extends Thread implements Serializable{

	private static final long serialVersionUID = 1L;

	//Port d'ecoute du serveur
	protected int port;
	
	protected String name;
	//Services qu'offre le serveur
	protected Map<String,_Service<?>> services;
	//AgentClassLoader associé
	//protected BAMAgentClassLoader agentClassLoader;
	//ServerlassLoader associé
	protected BAMAgentClassLoader serverClassLoader;
	protected ServerSocket serverSocket;
	
	
	
	/**
	 * Initialisation du serveur
	 * @param name le nom du serveur
	 * @param port le numero du port d'ecoute
	 */
	public AgentServer(String name, int port, BAMAgentClassLoader loader){
		this.name = name;
		this.port = port;
		this.services = new HashMap<String, _Service<?>>();
		this.serverClassLoader = loader;
	}
	
	/**
	 * Ajout un service sur le serveur
	 * @param service le service a ajouter
	 */
	public void addService(_Service<?> service, String classeName){
			this.services.put(classeName,service);
	}
	
	/**
	 * Recupere le service
	 */
	public _Service<?> getService(String classeName){
		return this.services.get(classeName);
	}
	
	/**
	 * Lance un Agent
	 * @param agent l'agent a demarrer
	 */
	public void startAgent(_Agent agent, BAMAgentClassLoader agentClassLoader){
		System.out.println("[AGENTSERVER] - Lancement d'un agent : "+name);
		agent.init(agentClassLoader, this, this.name);
		//Run dans un thread pour pouvour recevoir d'autres agents
		new Thread(agent).start();
	}
	
	public void run()
	{
		System.out.println("[AGENTSERVER] - Agent en route : " + this.name);
		//on crée un socket
		try {
			this.serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true)
		{
			System.out.println("[AGENTSERVER] - AgentServer " + this.name + " en attente d'agent");
			try {
			//Acceptation 
			Socket socketClient = this.serverSocket.accept();
			System.out.println("[AGENTSERVER] - AgentServer " + this.name + " a recu un agent");
			//on instancie les streams
			InputStream is = socketClient.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			
			//gets the serializable jar first
			BAMAgentClassLoader agentClassLoader = new BAMAgentClassLoader(new URL[]{}, serverClassLoader);

			Jar jar = (Jar) ois.readObject();
			//we load the jar in the new BAMAgent
			System.out.println("[AGENTSERVER] - Ajout du Jar au ClassLoader");
			agentClassLoader.addJar(jar);
			System.out.println("[AGENTSERVER] - Jar Ajouté");
			//on recupere l'agent
			System.out.println("[AGENTSERVER] - readObject - START");
			Agent agent = (Agent) ois.readObject();
			System.out.println("[AGENTSERVER] - readObject - END");
			//on initialise l'agent
			System.out.println("[AGENTSERVER] - Initialisation de l'agent");
			agent.init(agentClassLoader,this,this.name);
			System.out.println("[AGENTSERVER] - Agent Initialisé");
			System.out.println("[AGENTSERVER] - setJar - START");
			agent.setJar(jar);
			System.out.println("[AGENTSERVER] - setJar - DONE");
			System.out.println("[AGENTSERVER] - AgentServer " + this.name + " deploye un agent");
			//enfin on demarre l'agent
			startAgent(agent, agentClassLoader);
			//on ferme les streams
			ois.close();
			
			} catch (IOException | ClassNotFoundException e) {
				System.out.println(e);
				e.printStackTrace();
			}	
		}
	
	}
}
