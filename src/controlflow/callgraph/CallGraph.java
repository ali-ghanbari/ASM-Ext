package controlflow.callgraph;

import java.util.Collection;

import asmext.Method;
import asmext.instruction.Invocation;

/**
 * The general interface for a call graph
 * 
 * @author Ali Ghanbari
 *
 */
public interface CallGraph {	
	public Collection<Method> mayCall(Method m);
	
	public Collection<Method> mayCall(Invocation callSite);
}