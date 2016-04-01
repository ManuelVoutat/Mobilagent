package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AgentServer extends Thread{

	//Port d'ecoute du serveur
	protected int port;
	
	protected String name;
	//Services qu'offre le serveur
	protected Map<String,_Service> services;
	//AgentClassLoader associé
	protected BAMAgentClassLoader agentClassLoader;
	//ServerlassLoader associé
	protected BAMServerClassLoader serverClassLoader;
	protected ServerSocket serverSocket;
	
	
	/**
	 * Initialisation du serveur
	 * @param name le nom du serveur
	 * @param port le numero du port d'ecoute
	 */
	public AgentServer(String name, int port){
		this.name = name;
		this.port = port;
		this.services = new HashMap<String, _Service>();
		try {
			serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ajout un service sur le serveur
	 * @param service le service a ajouter
	 */
	public void addService(_Service<?> service){
			this.services.put(service.getServiceName(),service);
	}
	
	/**
	 * Recupere le service
	 */
	public _Service<?> getService(String ServiceName){
		return this.services.get(ServiceName);
	}
	
	/**
	 * Lance un Agent
	 * @param agent l'agent a demarrer
	 */
	public void startAgent(_Agent agent){
		agent.init(this.agentClassLoader, this, this.name);
		//Run dnas un thread pour pouvour recevoir d'autres agents
		new Thread(agent).start();
	}
	
	public void run()
	{
		System.out.println("Agent en run sur " + this.name);
		for(;;) 
		{
			try 
			{
				//Attente d'une connection
				Socket socket = serverSocket.accept();
				InputStream is = socket.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				//Recuperation de la jar
				this.agentClassLoader = new BAMAgentClassLoader(null, this.serverClassLoader);
				Jar jar = (Jar) ois.readObject();
				//Chargment dans la jar dans le BAM getn
				this.agentClassLoader.addJar(jar);
				//Recup de l'agent 
				Agent _agent = (Agent) ois.readObject();
				startAgent(_agent);
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}	
		}
		
	}
}
