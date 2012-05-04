package softwarehuset.scheduler.exceptions;

public class ArgumentException extends Exception {
	private Object argument;
	
	public ArgumentException(Object argument, String message) {
		super(message);
		this.argument = argument;
	}

	public Object getArgument() {
		return argument;
	}
}
