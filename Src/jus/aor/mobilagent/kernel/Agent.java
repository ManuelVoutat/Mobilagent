package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
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
	protected _Action doIt = _Action.NIHIL;
	
	public Agent(){
	/*	this.route = new Route(new Etape(new URI(agentServer._name+":"+agentServer._port), doIt));
		this.jar = new Jar(fileName);
		this.loader = new BAMAgentClassLoader(null);*/
	}
	
	public void run() {
		//Execution de l'action
		this.execute();
		
		//Passage au prochain serveur
		this.move();
		
	}

	public void init(AgentServer agentServer, String serverName) {
		// TODO Auto-generated method stub
		
	}

	public void init(BAMAgentClassLoader loader, AgentServer server,String serverName) {
		this.loader = loader;
		this.init(server, serverName);
		
	}
	
	public void reInit(AgentServer server, String serverName) {
		// TODO Auto-generated method stub
		
	}

	public void addEtape(Etape etape) {
		this.route.add(etape);
	}

	public void move() {
		

		if (this.route.hasNext()){
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
		}
		
	}
	
	public void execute() {
		if (this.route.hasNext()) {
			Etape etape = this.route.next();
			etape.action.execute();
			System.out.println("Etape : "+etape+" a effectué l'action "+etape.action);
		}
		
	}

}
