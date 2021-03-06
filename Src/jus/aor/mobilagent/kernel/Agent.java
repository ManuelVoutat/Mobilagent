package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;

import jus.aor.mobilagent.hello.Hello;

public class Agent implements _Agent,Serializable{

	private static final long serialVersionUID = 1L;

	/** La route à suivre */
	protected Route route;
	/** L'agentClassLoader , non serializable**/
	transient protected BAMAgentClassLoader loader;
	/** Le ficher Jar , non serializable */
	transient protected Jar jar;
	/** L'agent server, non serializable */
	transient protected AgentServer agentServer;
	/** Le nom du serveur */
	private String serverName;
	/** Boolean indiquant si il n'y a plus d'étapes */
	private boolean finish = false;
	
	public void run() {

		//System.out.println("[AGENT] - Agent sur ce server : "+this.serverName);
		//Charge la prochaine étape
		Etape etape = this.route.next();
		if(this.finish==true){
			//Action de retour
			execute(this.retour());
			//System.out.println("[AGENT] - Action effectué : "+this.serverName);
		}
		else
		{
			//Effectue l'action
			execute(etape.action);
			//Si il y a une prochaine etape
			if(this.route.hasNext())
			{
				//L'agent se connecte au server suivant
				//System.out.println("[AGENT] - Connection au prochain server : "+this.serverName);
				etape = this.route.get();
				//Changement de server
				move(etape.server);
			}
			//Sinon
			else if(this.finish == false)
			{
				//Mise à jour du booleen indiquant qu'il n'y a plus d'étapes
				this.finish = true;
				move(this.route.next().server);
			}
		}
	}

	public void init(AgentServer agentServer, String serverName) {
		//System.out.println("[AGENT] Deploiment de l'agent : " + serverName);
		this.agentServer = agentServer;
		this.serverName = serverName;
		
		if(this.route == null)
			try {
				//Chargement de la route de l'agent 
				URI uri = new URI(this.serverName);
				this.route = new Route(new Etape(uri, this.retour()));
				this.route.add(new Etape(new URI(this.serverName), _Action.NIHIL));
			} catch (URISyntaxException e){
				e.printStackTrace();
			}
		
	}

	public void init(BAMAgentClassLoader loader, AgentServer agentServer, String serverName) {
		//System.out.println("[AGENT] Deploiment de l'agent : " + serverName);
		this.loader = loader;
		this.agentServer = agentServer;
		this.serverName = serverName;
		if (this.route == null)
			try {
				//Chargement de la route de l'agent 
				URI uri = new URI(this.serverName);
				this.route = new Route(new Etape(uri, this.retour()));
				this.route.add(new Etape(new URI(this.serverName), _Action.NIHIL));
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	//Unused?
	public void reInit(AgentServer server, String serverName) {
	}

	public void addEtape(Etape etape) {
		this.route.add(etape);
	}

	public void move(URI Userver) {
		//Etape etape = this.route.get(); 
		Socket server = null;
		try {
			// Connection Client
			//System.out.println("[AGENT] Tentative de connection : " +Userver.getHost()+ ":" + etape.server.getPort());
			server = new Socket("localhost", Userver.getPort());
			//System.out.println("[AGENT] Connection : " + server.getInetAddress());
			OutputStream os = server.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			//Envoie de l'agent au serveur
			oos.writeObject(jar);
			oos.writeObject(this);
			oos.close();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void execute(_Action action) {
		action.execute();
	}
		
	public void setJar(Jar _jar) {
		this.jar = _jar;
	}
	
	public void setJar (URL url ) {
		Jar jar = null;
		try {
			jar = new Jar(url.getPath());
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		this.jar = jar;
	}
	
	/**
	 * Action a effectuer sur le serveur de retour
	 */
	protected _Action retour()
	{
		return ((Hello) this).retour();
	}

}
