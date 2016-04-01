package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;

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
		
		if(this.route.hasNext) {
			Etape etape = this.route.get();
			
			Socket server = null;
			try {
				// Client connected
				server = new Socket(etape.server.getHost(), etape.server.getPort());
				System.out.println("Connection a " + server.getInetAddress());
			
				OutputStream os = server.getOutputStream();

				ObjectOutputStream oos = new ObjectOutputStream(os);
				//Envoie de l'agent au server
				oos.writeObject(jar);
				oos.writeObject(this);
				oos.close();
					
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void init(AgentServer agentServer, String serverName) {
		System.out.println(" Deploiment de l'agent sur " + serverName);
		this.agentServer = agentServer;
		this.serverName = serverName;
		
		if(this.route == null)
			try {
				this.route.add(new Etape(new URI(this.serverName),_Action.NIHIL));
			} catch (URISyntaxException e){
				e.printStackTrace();
			}
		
	}

	public void init(BAMAgentClassLoader loader, AgentServer agentServer, String serverName) {
		System.out.println(" Initiating the agent on " + serverName);
		this.agentServer = agentServer;
		this.serverName = serverName;
		this.loader = loader;
	}
	
	public void reInit(AgentServer server, String serverName) {
		// TODO Auto-generated method stub
		
	}

	public void addEtape(Etape etape) {
		this.route.add(etape);
	}

	public void move() {
	/*	if (this.route.hasNext()){
			//Socket pour le prochain server
			Socket socket;
			
			try {
				URI server = this.route.get().getServer();
				socket = new Socket(server.getHost(),server.getPort());
				
				//Envoie de l'agent sur le serveur
				OutputStream os;
				os = socket.getOutputStream();
				ObjectOutputStream oos;
				
				oos = new ObjectOutputStream(os);
				oos.writeObject(this);
			}
			catch (NoSuchElementException e){
				e.printStackTrace();
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}*/
	}
	
	public void execute() {
		if (this.route.hasNext) {
			Etape etape = this.route.next();
			etape.action.execute();
		}
		
	}

}
