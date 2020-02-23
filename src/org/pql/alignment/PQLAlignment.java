package org.pql.alignment;

import java.util.ArrayList;
import java.util.List;

import org.jbpt.petri.ITransition;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A.P.
 */
public class PQLAlignment {
	
	private List<PQLMove> alignment = null; 
	
	public PQLAlignment() {
		
		alignment = new ArrayList<PQLMove>(); 
	}
	
	public void addMove(PQLMove move){
		
		this.alignment.add(move);
		
	};
	
	public void addMoveToPosition(PQLMove move, int position){
		
		this.alignment.add(position,move);
		
	};
	
	public void removeMove(int position){
		
		this.alignment.remove(position);
		
	};
	
	public List<PQLMove> getAlignment(){
		return this.alignment;
	};
	
	public void print(){
		for(int i=0; i<this.alignment.size(); i++)
		{System.out.println(alignment.get(i).getMoM() + " - " + alignment.get(i).getMoL() + " - " + alignment.get(i).getMoMT() + " - " + alignment.get(i).getT());}

	}
	
	
	public int getInsertAlignmentCost()
	{
		int result = 0;
		
		for(int i=0; i<this.alignment.size(); i++)
		{
			if(alignment.get(i).getMoM().equals("SKIP_STEP"))
			{result++;}
			
		}
		
		return result;
	}

	
	public int getAlignmentCost()
	{
		int result = 0;
		
		for(int i=0; i<this.alignment.size(); i++)
		{
			if(alignment.get(i).getMoM().equals("SKIP_STEP"))
			{result++;}
			else {if(!(alignment.get(i).getMoM().equals("INV_TRANS")) && alignment.get(i).getMoL().equals("SKIP_STEP"))
			{result++;}}
		}
		
		return result;
	}

	
	public int getAlignmentCostForAsterisk()
	{
		int result = 0;
		
		for(int i=0; i<this.alignment.size(); i++)
		{
			if(alignment.get(i).getMoM().equals("SKIP_STEP"))
			{result++;}
		}
		
		return result;
	}
	
	// newly defined for AAAI
	public int getAlignmentCostForMoL()
	{
		int result = 0;
		
		for(int i=0; i<this.alignment.size(); i++)
		{
			if(alignment.get(i).getMoM().equals("SKIP_STEP"))
			{
				result+=1;
			}
		}
		return result;
	}
	
	//  for BPIC
	public int getAlignmentCostForMoL(String name)
	{
		int result = 0;
		int count = 0;
		int consecutive = 0;
		double base = 1.1;
		double multiplier = 1.0;
		for(int i=0; i<this.alignment.size(); i++)
		{
			
			
			if(!alignment.get(i).getMoL().equals("SKIP_STEP"))
			{
				count++;  // this is increasing cost
			}
			
			if(alignment.get(i).getMoM().equals("SKIP_STEP"))
			{
				consecutive++;
				multiplier = pow(base, consecutive);
				result += count;
			} else {
				consecutive = 0;
				multiplier = 1.0;
			}
			
		}
		return (int) (result * multiplier);
	}
	
	
	// =============================================================================
	// the cost we decided to use MoL + Boltzmann
	public int getAlignmentCostForMoLDiagonal(String flag)
	{
		int result = 0;
		int count = 0;
		int consecutive = 0;
		double base = 1.1;
		
		double multiplier = 1.0;
		for(int i=0; i<this.alignment.size(); i++)
		{

			// count step number
			if(!alignment.get(i).getMoL().equals("SKIP_STEP"))
			{
				if (flag.contentEquals("IncreasingCost")) {
					count++;
				}
				if (flag.contentEquals("FlatCost")) {
					count = 1;
				}
			}
			
			
			
			if(alignment.get(i).getMoM().equals("SKIP_STEP"))
			{
				consecutive++;
				multiplier = pow(base, consecutive);
				result += (int) (distance(alignment.get(i).getMoL()) * count );
			} else {
				consecutive = 0;
				multiplier = 1.0;
			}
		}
		return (int) (result * multiplier);
	}
	
	
	public double pow(double x, int n) {
        if(n==0) return 1.0;
        double half=pow(x,n/2);
        if(n%2==0) return half*half;
        else if(n>0) return half*half*x;
        else return half*half/x;
    }
	
	
	public int getAlignmentCostForMoLDiagonal()
	{
		int result = 0;
		for(int i=0; i<this.alignment.size(); i++)
		{	
			if(alignment.get(i).getMoM().equals("SKIP_STEP"))
			{
				result += distance(alignment.get(i).getMoL());
			}
		}
		return result;
	}
	// ===============================================================================
	
	public int getAlignmentCostDiagonal()
	{
		int result = 0;
		
		for(int i=0; i<this.alignment.size(); i++)
		{
			if(alignment.get(i).getMoM().equals("SKIP_STEP"))
			{result += distance(alignment.get(i).getMoL());}
			else {if(!(alignment.get(i).getMoM().equals("INV_TRANS")) && alignment.get(i).getMoL().equals("SKIP_STEP"))
			{result += distance(alignment.get(i).getMoM());}}
		}
		
		return result;
	}
	
	public int distance(String s) {
		Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(s);
        ArrayList<Integer> array = new ArrayList<Integer>();
        while(m.find()) {
        	array.add(Integer.parseInt(m.group()));
        }
        double dis = Math.sqrt(Math.pow((array.get(0) - array.get(2)), 2)
				 + Math.pow((array.get(1) - array.get(3)), 2));
        if (dis == 1.0) {
        	return 10;
        } else {
        	return 14;
        }
	}
	
	public int getIndex(ITransition t)
	{
		int result = -1;
		
		for(int i=0; i<alignment.size(); i++)
		{
			if(alignment.get(i).getT() != null)
			{
				if(alignment.get(i).getT().equals(t)) {result = i; break;}
			}
		}
		
		return result;
	}
	
	public int getUTIndex(ITransition t2, ITransition ut)
	{
		int result = -1;
		
		for(int i=getIndex(t2)-1; i>0; i--)
		{
			if(alignment.get(i).getT() != null)
			{
				if(alignment.get(i).getT().equals(ut)) {result = i; break;}
			}
		}
		
		return result;
	}

	
}
