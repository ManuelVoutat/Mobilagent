package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class Agent implements _Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//route a suivre
	protected Route route;
	protected BAMAgentClassLoader loader;
	protected Jar jar;
	protected AgentServer agentServer;
	private String serverName;
	
	public void run() {

		System.out.println("Agent sur ce server : "+this.serverName);
		
		this.execute();
		
		this.move();
	}

	public void init(AgentServer agentServer, String serverName) {
		System.out.println(" Deploiment de l'agent sur " + serverName);
		this.agentServer = agentServer;
		this.serverName = serverName;
		
		if(this.route == null)
			try {
				this.route = new Route(new Etape(new URI(this.serverName), _Action.NIHIL));
				//this.route.add(new Etape(new URI(this.serverName),_Action.NIHIL));
			} catch (URISyntaxException e){
				e.printStackTrace();
			}
		
	}

	public void init(BAMAgentClassLoader loader, AgentServer agentServer, String serverName) {
		System.out.println(" Deploiment de l'agent sur " + serverName);
		this.loader = loader;
		this.agentServer = agentServer;
		this.serverName = serverName;
		if (this.route == null)
			try {
				this.route = new Route(new Etape(new URI(this.serverName), _Action.NIHIL));
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
	}
	
	public void reInit(AgentServer server, String serverName) {
		// TODO Auto-generated method stub
		
	}

	public void addEtape(Etape etape) {
		this.route.add(etape);
	}

	public void move() {
		Etape etape = this.route.get(); 
		
		Socket server = null;
		try {
			// Connection Client
			System.out.println(" Tentative de connection : " +etape.server.getHost()+ ":" + etape.server.getPort());
			server = new Socket(/*etape.server.getHost()*/"localhost", etape.server.getPort());
			System.out.println("Connection a " + server.getInetAddress());

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
	
	public void execute() {
		if (this.route.hasNext) {
			Etape etape = this.route.next();
			etape.action.execute();
		}
	}
		
	public void setJar(Jar jar) {
		this.jar = jar;
	}

}
