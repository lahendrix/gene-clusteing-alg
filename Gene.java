import java.util.*;
import java.text.*;

public class Gene 
{

	private String identifier;
	private String description;
	private double[] values;
	private double expressionRatio;
	
	public Gene(String i, String d)
	{
		identifier = i;
		description = d;
		
	}
	
	
	public void addValues(ArrayList<Double> v)
	{
		values = new double[v.size()];
		double sum = 0.0;
		
		for(int i = 0; i < values.length; i++)
		{
			values[i] = v.get(i);
			sum += values[i];
		}
		
		expressionRatio = sum/values.length;
	}
	
	public double getExpressionRatio()
	{
		return expressionRatio;
	}
	
	/**
	 * Returns the identifier of the gene
	 * @return
	 */
	public String getIdentifier()
	{
		return identifier;
	}
	
	/**
	 * Returns the description of the gene
	 * @return
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Returns the gene expression values of this gene
	 * @return
	 */
	public double[] getValues()
	{
		return values;
	}
	
	/**
	 * Computes Euclidean distance
	 * @param y gene to compute distance between
	 * @return euclidean distance between the two genes
	 */
	public double computeDistance(Gene y)
	{
		
		double sum = 0.0;
		
		for(int i = 0; i < values.length ; i++)
		{
			double tmp = Math.pow((values[i] - y.values[i]), 2); 
			sum += tmp;
		}
		
		return Math.sqrt(sum);
	}
	
	public void printGene()
	{
		java.text.DecimalFormat f = new DecimalFormat("#.###");
		
		System.out.println(identifier + " " + description + " " + f.format(expressionRatio));
	}
}
