package jus.aor.mobilagent.kernel;

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
		this.route = new Route(new Etape(new URI(agentServer._name+":"+agentServer._port), doIt));
		this.jar = new Jar(fileName);
		this.loader = new BAMAgentClassLoader(null);
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
	}

	public void init(AgentServer agentServer, String serverName) {
		// TODO Auto-generated method stub
		
	}

	public void init(BAMAgentClassLoader loader, AgentServer server,String serverName) {
		// TODO Auto-generated method stub
		
	}
	
	public void reInit(AgentServer server, String serverName) {
		// TODO Auto-generated method stub
		
	}

	public void addEtape(Etape etape) {
		this.route.add(etape);
	}

	public void move() {
		// TODO Auto-generated method stub
		
	}
	
	public void execute() {
		if (this.route.hasNext()) {
			Etape etape = this.route.next();
			etape.action.execute();
			System.out.println("Etape : "+etape+" a effectué l'action "+etape.action);
		}
		
	}

}
