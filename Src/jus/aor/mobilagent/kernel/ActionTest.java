package jus.aor.mobilagent.kernel;

/**
 * Afficher le serveur sur lequel on se situe
 * @author Quentin
 */
public class ActionTest implements _Action{

	private static final long serialVersionUID = 1L;

	@Override
	public void execute() {
		System.out.println("ActionTest");
		
	}
}