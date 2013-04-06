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
import cvx.cvsdata.CVSFile;
import cvx.cvsdata.CVSModule;
import cvx.cvsdata.CVSRevision;
import framework.Pipe;

public class ModuleOrganizationAnalysis 
extends CVSAnalysis {
		
	public void analyze(CVSDatabase db, Pipe pipe) {
		analyzeFileLevel(db, pipe);
//		analyzeModuleLevel(db, pipe);
	}
	
	private void analyzeFileLevel(CVSDatabase db, Pipe pipe) {
			
		ImmutableCollection<CVSFile> files = db.getFiles();		
		int numFiles = files.size();
			
		int[][] coModificationFactor = new int[numFiles][numFiles];
		for( int i=0; i<numFiles; i++ )
			Arrays.fill(coModificationFactor[i], 0);
		
		for(CVSAuthor a: db.getAuthors()) {
			
			Set<CVSFile> transaction = new HashSet<CVSFile>();
			long prevTimeInMillis = 0;
			for(CVSRevision rev: db.getRevisionsByAuthor(a)) {
				
				long curTimeInMillis = rev.getDateInMillis();
				long diff = curTimeInMillis - prevTimeInMillis;
				prevTimeInMillis = curTimeInMillis;
				
				if( diff > 30000 && transaction.size()>0 ) {
//					System.out.println(transaction);					
					for(CVSFile f1: transaction)
						for(CVSFile f2: transaction)
							if( f1.getId()>f2.getId() )
								coModificationFactor[f1.getId()][f2.getId()] += 1;
					transaction.clear();
				}
				
				CVSFile file = db.getRevisionFile(rev);
				transaction.add(file);
//				System.out.println(a+" "+rev.getDateInString()+"("+rev.getDateInMillis()+") "+file.getId()+" "+diff);
				
			}
			if( transaction.size()>0 ) {
//				System.out.println(transaction);	
				for(CVSFile f1: transaction)
					for(CVSFile f2: transaction)
						if( f1.getId()>f2.getId() )
							coModificationFactor[f1.getId()][f2.getId()] += 1;
			}
		}
		
		Graph graph = new Graph();
		
		for(CVSFile f: files) {	
			double val = 0.3;
			Node node = graph.getNode(f.toString());
			node.setData(val);
            node.setValue(Constants.KEY_ID, f.getId() + "");
            node.setValue(Constants.KEY_NAME, f.getName());
		}
		
		int numEdges = 0;
		for(CVSFile f1: files) {
			for(CVSFile f2: files) {
				if( f1.getId()>f2.getId() ) {
					double val = coModificationFactor[f1.getId()][f2.getId()];
					if( val>0 )
						val = 1/val;
					else if( val==0 )
						val = 1;
					else
						System.out.println("value is less than zero??");
				
					Node src = graph.getNode(f1.toString());
					Node dstn = graph.getNode(f2.toString());
					Edge edge = graph.addEdge(src, dstn);
					edge.setData(val);
					numEdges++;
				}
			}
		}
		System.out.println("#nodes="+numFiles);
		System.out.println("#edges="+numEdges+" ... "+(numFiles*(numFiles-1)/2));
		
		pipe.addData(Constants.KEY_GRAPH, graph);	    
	}

	private void analyzeModuleLevel(CVSDatabase db, Pipe pipe) {
		
		ImmutableCollection<CVSModule> modules = db.getModules();		
		int numModules = modules.size();
			
		int[][] coModificationFactor = new int[numModules][numModules];
		for( int i=0; i<numModules; i++ )
			Arrays.fill(coModificationFactor[i], 0);
		
		for(CVSAuthor a: db.getAuthors()) {
			
			Set<CVSModule> transaction = new HashSet<CVSModule>();
			long prevTimeInMillis = 0;
			for(CVSRevision rev: db.getRevisionsByAuthor(a)) {
				
				long curTimeInMillis = rev.getDateInMillis();
				long diff = curTimeInMillis - prevTimeInMillis;
				prevTimeInMillis = curTimeInMillis;
				
				if( diff > 30000 && transaction.size()>0 ) {
//					System.out.println(transaction);					
					for(CVSModule m1: transaction)
						for(CVSModule m2: transaction)
							if( m1.getId()>m2.getId() )
								coModificationFactor[m1.getId()][m2.getId()] += 1;
					transaction.clear();
				}
				
				CVSModule module = db.getRevisionModule(rev);
				transaction.add(module);
//				System.out.println(a+" "+rev.getDateInString()+"("+rev.getDateInMillis()+") "+module.getId()+" "+diff);
				
			}
			if( transaction.size()>0 ) {
//				System.out.println(transaction);	
				for(CVSModule m1: transaction)
					for(CVSModule m2: transaction)
						if( m1.getId()>m2.getId() )
							coModificationFactor[m1.getId()][m2.getId()] += 1;
			}
		}
		
		Graph graph = new Graph();
		
		for(CVSModule m: modules) {	
			double val = 0.3;
			Node node = graph.getNode(m.toString());
			node.setData(val);
            node.setValue(Constants.KEY_ID, m.getId()+"");
            node.setValue(Constants.KEY_NAME, m.getName());
		}
		
		int numEdges = 0;
		for(CVSModule m1: modules) {
			for(CVSModule m2: modules) {
				if( m1.getId()>m2.getId() ) {
					double val = coModificationFactor[m1.getId()][m2.getId()];
					if( val>0 )
						val = 1/val;
					else if( val==0 )
						val = 1;
					else
						System.out.println("value is less than zero??");
				
					Node src = graph.getNode(m1.toString());
					Node dstn = graph.getNode(m2.toString());
					Edge edge = graph.addEdge(src, dstn);
					edge.setData(val);
					numEdges++;
				}
			}
		}
		
		pipe.addData(Constants.KEY_GRAPH, graph);	    
	}
}
