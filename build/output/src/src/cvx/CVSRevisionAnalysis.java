package cvx;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CVSRevisionAnalysis {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		String projPath = System.getProperty("project-path");
		BufferedReader br=new BufferedReader(new FileReader("outputs.txt"));
		List<Double> db = new ArrayList<Double>() ;
		int i=0;
		while(true){
			String as = br.readLine();
			i++;
			if (as==null) break;
			else { 
				double a = Double.valueOf(as);
				db.add(a);
			}
		}
		double m=0;
		double std=0;
		
		m=util.StatisticalUtil.mean(db);
		std=util.StatisticalUtil.standardDeviation(db, 1);
		
		System.out.println(m+" std:"+std+"with "+i+" revision");
	}

}
