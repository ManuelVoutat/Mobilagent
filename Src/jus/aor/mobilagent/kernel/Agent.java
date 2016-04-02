package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
	private boolean finish;
	
	public void run() {

		System.out.println("Agent sur ce server : "+this.serverName);
		
		Etape etape = this.route.next();
		//effectue l'action
		if(this.finish==true)
			execute(this.retour());
		else
		{
			execute(etape.action);
			if(this.route.hasNext)
			{
				//L'agent se connecte au server suivant
				etape = this.route.get();
				move(etape.server);
			}
			else if(this.finish == false)
			{
				this.finish = true;
				move(this.route.next().server);
			}
		}
	}

	public void init(AgentServer agentServer, String serverName) {
		System.out.println(" Deploiment de l'agent sur " + serverName);
		this.agentServer = agentServer;
		this.serverName = serverName;
		
		if(this.route == null)
			try {
				URI uri = new URI(this.serverName);
				this.route = new Route(new Etape(uri, this.retour()));
				this.route.add(new Etape(new URI(this.serverName), _Action.NIHIL));
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
				URI uri = new URI(this.serverName);
				this.route = new Route(new Etape(uri, this.retour()));
				this.route.add(new Etape(new URI(this.serverName), _Action.NIHIL));
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public void reInit(AgentServer server, String serverName) {
		// TODO Auto-generated method stub
		
	}

	public void addEtape(Etape etape) {
		this.route.add(etape);
	}

	public void move(URI Userver) {
		Etape etape = this.route.get(); 
		
		Socket server = null;
		try {
			// Connection Client
			System.out.println(" Tentative de connection : " +Userver.getHost()+ ":" + etape.server.getPort());
			server = new Socket("localhost", Userver.getPort());

			//server = new Socket(/*etape.server.getHost()*/"localhost", etape.server.getPort());
			System.out.println("Connection a " + server.getInetAddress());

			OutputStream os = server.getOutputStream();

			ObjectOutputStream oos = new ObjectOutputStream(os);
			//Envoie de l'agent au serveur
			System.out.println("Envoie du jar");
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
		
	public void setJar(Jar jar) {
		this.jar = jar;
	}
	
	public void setJar (URL url ) {
		Jar jar = null;
		try {
			jar = new Jar(url.getPath());
		} catch (IOException e) {
			System.out.println("agent l138");
			System.out.println(e);
		}
		this.jar = jar;
	}
	/**
	 * Action a effectuer sur le server de retour
	 */
	protected _Action retour()
	{
		return _Action.NIHIL;
	}

}
