package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AgentServer extends Thread{

	//Port d'ecoute du serveur
	protected int port;
	
	protected String name;
	//Services qu'offre le serveur
	
	protected ServerSocket serverSocket;
	//protected List<_Service> services;
	
	
	protected HashMap<String, _Service<?>> services;
	
	//AgentClassLoader associé
	protected BAMAgentClassLoader agentClassLoader;
	
	/**
	 * Initialisation du serveur
	 * @param name le nom du serveur
	 * @param port le numero du port d'ecoute
	 */
	public AgentServer(String name, int port, BAMAgentClassLoader loader){
		this.name = name;
		this.port = port;
		this.services = new HashMap<String, _Service<?>>();
		this.agentClassLoader = loader;
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
	public _Service<?> getService(String ClasseName){
		return this.services.get(ClasseName);
	}
	
	/**
	 * Lance un Agent
	 * @param agent l'agent a demarrer
	 */
	public void startAgent(_Agent agent){
		new Thread(agent).start();
	}
	
	public void run(){
		//Creation d'une socket
		try{
			this.serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true){
			try {
				Socket socketClient = this.serverSocket.accept();
				
				InputStream is;
				is = socketClient.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				
				//Recuperation de l'agent
				_Agent agent = (_Agent) ois.readObject();
				agent.init(this.agentClassLoader, this,this.name);
				
				startAgent(agent);
				
				ois.close();
				
				
			} catch (ClassNotFoundException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
