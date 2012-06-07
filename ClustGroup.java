import java.util.*;

public class ClustGroup 
{

	private ArrayList<Gene> listOfGenes = new ArrayList<Gene>();
	private ArrayList<ClustGroup> listOfClusters = new ArrayList<ClustGroup>();
	private double clusterAverageExpressionRatio;
	
	public ClustGroup()
	{
		
	}
	
	public ClustGroup(Gene g)
	{
		listOfGenes.add(g);
		calculateExpressionAverage();
		sortGeneList();
	}
	
	/**
	 * Combines the current cluster and given cluster
	 * @param cg cluster to merge with current cluster
	 */
	public void addCluster(ClustGroup cg)
	{
		listOfClusters.add(cg);
		
		//Add the individual genes from the new cluster into the gene list
		for(Gene g: cg.getGenes())
		{
			listOfGenes.add(g);
		}
		
		calculateExpressionAverage();
		sortGeneList();
	}
	
	/**
	 * Method that computes the average expression rate for 
	 * all genes in the cluster
	 */
	private void calculateExpressionAverage()
	{
		double sum = 0.0;
		
		for(Gene g: listOfGenes)
		{
			sum += g.getExpressionRatio();
		}
		
		clusterAverageExpressionRatio = sum/listOfGenes.size();
	}
	
	/**
	 * Method that sorts the gene list from smallest to largest based 
	 * on their individual average expression ratios
	 */
	private void sortGeneList()
	{
		ArrayList<Gene> tmp = new ArrayList<Gene>();
		while(!listOfGenes.isEmpty())
		{
			double min =Double.MAX_VALUE;
			Gene minGene = null;
			
			for(Gene g1: listOfGenes)
			{
				if(g1.getExpressionRatio() < min)
				{
					min = g1.getExpressionRatio();
					minGene = g1;
				}
			}
			
			tmp.add(minGene);
			listOfGenes.remove(minGene);
		}
		listOfGenes = tmp;
	}
	
	/**
	 * Returns the average expression ratio for this cluster
	 * @return average expression of instances within cluster
	 */
	public double getAverageExpressionRate()
	{
		return clusterAverageExpressionRatio;
	}
	
	/**
	 * Method that computes the single link distance between clusters
	 * @param cg cluster to compute distance between
	 * @return distance of most similar instances
	 */
	public double computeSingleLinkDistance(ClustGroup cg)
	{
		double minDistance = Double.MAX_VALUE;
		
		for(Gene g1: cg.getGenes())
		{
			for(Gene g2: listOfGenes)
			{
				double distance = g2.computeDistance(g1);
				
				if(distance < minDistance)
				{
					minDistance = distance;
				}
			}
		}
		
		return minDistance;
	}
	
	/**
	 * Computes the complete link distance between two clusters
	 * @param cg cluster to compute the distance between
	 * @return distance of most dissimilar instances
	 */
	public double computeCompleteLinkDistance(ClustGroup cg)
	{
		double maxDistance = Double.MIN_VALUE;
		
		for(Gene g1: cg.getGenes())
		{
			for(Gene g2: listOfGenes)
			{
				double distance = g1.computeDistance(g2);
				
				if(distance > maxDistance)
				{
					maxDistance = distance;
				}
			}
		}
		
		return maxDistance;
	}
	
	/**
	 * Computes the average link distance between two clusters
	 * @param cg cluster to compute average distance between
	 * @return average distance between instances
	 */
	public double computeAverageLinkDistance(ClustGroup cg)
	{
		double sum = 0.0;
		
		for(Gene g1: cg.getGenes())
		{
			for(Gene g2: listOfGenes)
			{
				
				sum += g2.computeDistance(g1);;
			}
		}
		
		double avgDistance = sum/(listOfGenes.size() * cg.getNumberOfGenes());
		
		return avgDistance;
	}
	
	/**
	 * Method that returns the total number of genes within this cluster
	 * @return
	 */
	public int getNumberOfGenes()
	{
		return listOfGenes.size();
	}
	
	/**
	 * Returns the number of sub clusters 
	 * @return
	 */
	public int getNumOfSubClusters()
	{
		return listOfClusters.size();
	}
	
	/**
	 * Returns complete list of genes
	 * @return
	 */
	public ArrayList<Gene> getGenes()
	{
		return listOfGenes;
	}
}
