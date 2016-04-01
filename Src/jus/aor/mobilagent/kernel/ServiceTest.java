package jus.aor.mobilagent.kernel;

public class ServiceTest implements _Service<String>{

	public String call(Object... params) throws IllegalArgumentException {
		return "ServiceTest :)";
	}
	
	public String getServiceName() {
		return "_Service-getServiceName";
	}

}