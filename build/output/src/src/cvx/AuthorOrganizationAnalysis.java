package cvx;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import util.Constants;
import util.Edge;
import util.Graph;
import util.ImmutableCollection;
import util.Node;
import cvx.cvsdata.CVSAuthor;
import cvx.cvsdata.CVSDatabase;
import cvx.cvsdata.CVSModule;
import framework.Pipe;

public class AuthorOrganizationAnalysis 
extends CVSAnalysis {

	public void analyze(CVSDatabase db, Pipe pipe) {
		
		ImmutableCollection<CVSAuthor> authors = db.getAuthors();

		int numAuthors = authors.size();
		
		int[] accessFactor = new int[numAuthors];
		double[][] coModificationFactor = new double[numAuthors][numAuthors];

		Arrays.fill(accessFactor,0);
		for( int i=0; i<numAuthors; i++ )
			Arrays.fill(coModificationFactor[i], -1);
				
		double accessFactorMin = 10000;
		double coModificationFactorMax = 0;
		
		for(CVSAuthor a: authors) {
			int r = db.numRevisionsByAuthor(a); 
			accessFactor[a.getId()] = r;
			if( r < accessFactorMin ) 
				accessFactorMin = r;
		}
	    
		ImmutableCollection<CVSModule> modules = db.getModules();
		for(CVSModule m: modules) {
			
			Set<CVSAuthor> checked = new HashSet<CVSAuthor>();
			
			for(CVSAuthor a1: authors) {
				int r1 = db.numRevisionsByAuthorAndModule(a1, m);
				checked.add(a1);
				
				for(CVSAuthor a2: authors) {
					if( checked.contains(a2) ) continue;
					
					int r2 = db.numRevisionsByAuthorAndModule(a2, m);
					
					if( coModificationFactor[a1.getId()][a2.getId()] == -1 )
						coModificationFactor[a1.getId()][a2.getId()] = 0;
					
					coModificationFactor[a1.getId()][a2.getId()] += r1*r2;
					
					double maxCandidate = coModificationFactor[a1.getId()][a2.getId()];
					if( maxCandidate > coModificationFactorMax )
						coModificationFactorMax = maxCandidate;
				}
			}
		}
		
		Graph graph = new Graph();
		
		double node_size_ratio = 0.3;
		double node_size_down = 4;
		for(CVSAuthor author: authors) {		
			double val = Math.pow( accessFactor[author.getId()]/accessFactorMin, node_size_ratio )/node_size_down;
			Node node = graph.getNode(author.toString());
			node.setData(val);
            node.setValue(Constants.KEY_ID, author.getId() + "");
            node.setValue(Constants.KEY_NAME, author.getName());
		}
		
		double edge_len_ratio = 0.3;
		for(CVSAuthor a1: authors) {
			for(CVSAuthor a2: authors) {
				
				double val = coModificationFactor[a1.getId()][a2.getId()];
				if( val > 0 )
					val = Math.pow( coModificationFactorMax/val, edge_len_ratio );
				else if( val == 0 ) 
					val = Math.pow( coModificationFactorMax*2, edge_len_ratio );
				else
					continue;
				
				Node src = graph.getNode(a1.toString());
				Node dstn = graph.getNode(a2.toString());
				Edge edge = graph.addEdge(src, dstn);
				edge.setData(val);
                System.out.println("val: " + val);
			}
		}
		
		pipe.addData(Constants.KEY_GRAPH, graph);
	}
}
