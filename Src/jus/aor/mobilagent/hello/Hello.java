package jus.aor.mobilagent.hello;


import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel.Agent;

/**
 * Classe de test élémentaire pour le bus à agents mobiles
 * @author  Morat
 */
public class Hello extends Agent{

	/**
	 * l'action à entreprendre sur les serveurs visités
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private _Action doIt = new _Action() {
		private static final long serialVersionUID = 1L;
		public void execute() {
			System.out.println("HELLO-ACTION");
		}
		
	};
	
	private _Action retour = new _Action() {
		private static final long serialVersionUID = 1L;
		public void execute() {
			System.out.println("HELLO-RETOUR");
		}
		
	};
	/**
	  * construction d'un agent de type hello.
	  * @param args aucun argument n'est requis
	  */
	public Hello(Object... args) {
		 super();
	}
	
	/* (non-Javadoc)
	 * @see jus.aor.mobilagent.kernel.Agent#retour()
	 */
	public _Action retour(){
		return this.retour;
	}
}
