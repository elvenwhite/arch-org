package framework;

public abstract class Application {
	
	public void execute(TargetSoftware target) throws ApplicationInvokeException{
		
		Pipe pipe = new Pipe();
				
		setUp();
		
		printStatus("PREPARING ANALYSIS");
		prepare(target, pipe);
		
		printStatus("ANALYZING "+target.getName());
		analyze(pipe);
		
		visualize(pipe);				
	}
	
	protected abstract void setUp();
	
	protected abstract void prepare(TargetSoftware target, Pipe pipe);
	
	protected abstract void analyze(Pipe pipe);
	
	protected abstract void visualize(Pipe pipe) throws ApplicationInvokeException;
	
	private void printStatus(String s) {
		System.out.println(s);
	}
}
