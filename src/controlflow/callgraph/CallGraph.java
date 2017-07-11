package controlflow.callgraph;

import java.util.List;

import asmext.Method;
import asmext.instruction.Invocation;

/**
 * The general interface for a call graph
 * 
 * @author Ali Ghanbari
 *
 */
public interface CallGraph {	
	public List<Method> mayCall(Method m);
	
	public List<Method> mayCall(Invocation callSite);
}