package jus.aor.mobilagent.kernel;

public class Agent implements _Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//route a suivre
	protected Route route;
	
	public Agent(){
		
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
	}

	public void init(AgentServer agentServer, String serverName) {
		// TODO Auto-generated method stub
		
	}

	public void init(BAMAgentClassLoader loader, AgentServer server,
			String serverName) {
		// TODO Auto-generated method stub
		
	}
	
	public void reInit(AgentServer server, String serverName) {
		// TODO Auto-generated method stub
		
	}

	public void addEtape(Etape etape) {
		route.add(etape);
	}

	public void move() {
		// TODO Auto-generated method stub
		
	}
	
	public void execute() {
		
	}

}
