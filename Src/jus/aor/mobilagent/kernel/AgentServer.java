package jus.aor.mobilagent.kernel;

import java.util.LinkedList;
import java.util.List;

public class AgentServer extends Thread{

	//Port d'ecoute du serveur
	protected int port;
	
	protected String name;
	//Services qu'offre le serveur
	protected List<_Service> services;
	//AgentClassLoader associé
	protected BAMAgentClassLoader agentClassLoader;
	
	/**
	 * Initialisation du serveur
	 * @param name le nom du serveur
	 * @param port le numero du port d'ecoute
	 */
	public AgentServer(String name, int port){
		this.name = name;
		this.port = port;
		this.services = new LinkedList<>();
	}
	
	/**
	 * Ajout un service sur le serveur
	 * @param service le service a ajouter
	 */
	public void addService(_Service service){
			this.services.add(service);
	}
	
	/**
	 * Recupere le service
	 */
	public _Service getService(){
		//TODO
		return null;
	}
	
	/**
	 * Lance un Agent
	 * @param agent l'agent a demarrer
	 */
	public void startAgent(_Agent agent){
		agent.init(this.agentClassLoader,this, this.name);
	}
	
	public void run(){
		//TODO
	}
	
}
