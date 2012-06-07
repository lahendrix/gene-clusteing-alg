import java.util.*;
import java.io.*;
import java.text.*;

public class Cluster 
{
	
public static ArrayList<Gene> readExpressionFile(String fileName)
{
	ArrayList<Gene> tmp = new ArrayList<Gene>();
	
    try 
    {
      
    	BufferedReader input = new BufferedReader( new FileReader(fileName) );
    	String line = null; 
    	while (( line = input.readLine()) != null)
    	{
    		String s[] = line.split("\t");
    		String identifier = s[0];
    		String description = s[1];
    		Gene g = new Gene(identifier, description);
    		ArrayList<Double> val = new ArrayList<Double>();
    		
    		for(int i = 2; i < s.length; i++)
    		{
    			val.add(Double.parseDouble(s[i]));
    		}
    		g.addValues(val);
    		tmp.add(g);
    	    
    	}
    	
    }
	      
   
    catch (FileNotFoundException ex) 
    {
      System.out.println("File: " + fileName + " not found");
    }
    
    catch(IOException e)
    {
    	System.out.println("IO exception!");
    }
    
    return tmp;
}

/**
 * Method that sorts and prints the k highest clusters
 * @param list
 */
public static void sortAndPrintClusters(ArrayList<ClustGroup> list)
{
	ArrayList<ClustGroup> clustList = new ArrayList<ClustGroup>();
	
	while(!list.isEmpty())
	{
		double min =Double.MAX_VALUE;
		ClustGroup minClust = null;
		
		for(ClustGroup cg: list)
		{
			if(cg.getAverageExpressionRate() < min)
			{
				min = cg.getAverageExpressionRate();
				minClust = cg;
			}
		}
		
		clustList.add(minClust);
		list.remove(minClust);
	}
	
	for(ClustGroup c: clustList)
	{
		ArrayList<Gene> a = c.getGenes();
		for(Gene g: a)
		{
			g.printGene();
		}

		DecimalFormat f = new DecimalFormat("#.###");
		
		System.out.println(f.format(c.getAverageExpressionRate()));
		System.out.println();
	}
}

public static ArrayList<ClustGroup> singleLink(ArrayList<ClustGroup> clusterList, int k)
{
	while(clusterList.size() > k)
	{
		
		double min = Double.MAX_VALUE;
		ClustGroup tmp1 = null;
		ClustGroup tmp2 = null;
		
		for(ClustGroup cg1: clusterList)
		{
			for(ClustGroup cg2: clusterList)
			{
				if(cg1.computeSingleLinkDistance(cg2) < min && !cg1.equals(cg2))
				{
					tmp1 = cg1;
					tmp2 = cg2;
					min = cg1.computeSingleLinkDistance(cg2);
				}
			}
		}
		
		ClustGroup newCluster = new ClustGroup();
		newCluster.addCluster(tmp1);
		newCluster.addCluster(tmp2);
		clusterList.remove(tmp1);
		clusterList.remove(tmp2);
		clusterList.add(newCluster);
	}
	
	ArrayList<ClustGroup> kHighest = new ArrayList<ClustGroup>();
	
	for(int i = k; i >= 1; i--)
	{
		kHighest.add(clusterList.get(clusterList.size() - i));
	}
	
	return kHighest;
}

public static ArrayList<ClustGroup> completeLink(ArrayList<ClustGroup> clusterList, int k)
{
	while(clusterList.size() > k)
	{
		
		double min = Double.MAX_VALUE;
		ClustGroup tmp1 = null;
		ClustGroup tmp2 = null;
		
		for(ClustGroup cg1: clusterList)
		{
			
			for(ClustGroup cg2: clusterList)
			{
				if(cg1.computeCompleteLinkDistance(cg2) < min && !cg1.equals(cg2))
				{
					tmp1 = cg1;
					tmp2 = cg2;
					min = cg1.computeCompleteLinkDistance(cg2);
					
				}
			}
		}
		
		//Create a new cluster 
		ClustGroup newCluster = new ClustGroup();
		newCluster.addCluster(tmp1);
		newCluster.addCluster(tmp2);
		
		//Remove old clusters
		clusterList.remove(tmp1);
		clusterList.remove(tmp2);
		
		//Add new cluster to cluster list
		clusterList.add(newCluster);
	}
	
	ArrayList<ClustGroup> kHighest = new ArrayList<ClustGroup>();
	
	for(int i = k; i >= 1; i--)
	{
		kHighest.add(clusterList.get(clusterList.size() - i));
	}
	
	return kHighest;
}
	
public static ArrayList<ClustGroup> averageLink(ArrayList<ClustGroup> clusterList, int k)
{
	while(clusterList.size() > k)
	{
		
		double min = Double.MAX_VALUE;
		ClustGroup tmp1 = null;
		ClustGroup tmp2 = null;
		
		for(ClustGroup cg1: clusterList)
		{
			for(ClustGroup cg2: clusterList)
			{
				if(cg1.computeAverageLinkDistance(cg2) < min && !cg1.equals(cg2))
				{
					tmp1 = cg1;
					tmp2 = cg2;
					min = cg1.computeAverageLinkDistance(cg2);
				}
			}
		}
		
		ClustGroup newCluster = new ClustGroup();
		newCluster.addCluster(tmp1);
		newCluster.addCluster(tmp2);
		clusterList.remove(tmp1);
		clusterList.remove(tmp2);
		clusterList.add(newCluster);
	}
	
	ArrayList<ClustGroup> kHighest = new ArrayList<ClustGroup>();
	
	for(int i = k; i >= 1; i--)
	{
		kHighest.add(clusterList.get(clusterList.size() - i));
	}
	return kHighest;
}



/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		ArrayList<Gene> geneList = null;
		ArrayList<ClustGroup> clusterList = new ArrayList<ClustGroup>();
		String fileName = null;
		String clusterType = null;
		int k = 0;
		
		if(args.length != 3)
		{
			System.out.println("Incorrect number of cmd line args");
			System.exit(1);
		}
		
		else
		{
			fileName = args[0];
			clusterType = args[1];
			k = Integer.parseInt(args[2]);
			geneList = readExpressionFile(fileName);
		}
		
		//Create individual clusters for each gene and add to the cluster list
		for(Gene g: geneList)
		{
			clusterList.add(new ClustGroup(g));
		}
		
		ArrayList<ClustGroup> kHighest;
		
		if(clusterType.equalsIgnoreCase("S"))
		{
			kHighest = singleLink(clusterList, k);
			sortAndPrintClusters(kHighest);
		}
		
		else if(clusterType.equalsIgnoreCase("C"))
		{
			kHighest = completeLink(clusterList, k);
			sortAndPrintClusters(kHighest);
		}
		
		else if(clusterType.equalsIgnoreCase("A"))
		{
			kHighest = averageLink(clusterList, k);
			sortAndPrintClusters(kHighest);
		}
	}
}
