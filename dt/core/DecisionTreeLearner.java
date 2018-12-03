package dt.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.*;

import dt.util.ArraySet;

/**
 * Implementation of the decision-tree learning algorithm in AIMA Fig 18.5.
 * This is based on ID3 (AIMA p. 758).
 */
public class DecisionTreeLearner extends AbstractDecisionTreeLearner {
	
	/**
	 * Construct and return a new DecisionTreeLearner for the given Problem.
	 */
	public DecisionTreeLearner(Problem problem) {
		super(problem);
	}
	
	/**
	 * Main recursive decision-tree learning (ID3) method.  
	 */
	@Override
	protected DecisionTree learn(Set<Example> examples, List<Variable> attributes, Set<Example> parent_examples) {
	    
		
		/** Pseudo code  AIMA PAGE 702
		function DECISION-TREE-LEARNING(examples, attributes, parent_examples) returns a tree
			if (examples is empty)
				then return PLURALITY-VALUE(parent_examples)
			else if (all examples have same classification)
				then return the classification
			else if (attributes is empty)
				then return PLURALITY-VALUE(examples)
			else
				A = most important variable .... full description on AIMA page 702
				tree = new decision tree with root test A
				for each value vk of A
					exs = example with value vk for attribute A
					subtree = decision-tree-learning(exs, attributes, examples)
					add branch to tree with label (A=vk) and subtree
				return tree			
		*/
		
		if (examples.size() == 0) {
			return new DecisionTree( this.pluralityVALUE(parent_examples) );
		}
		
		else if (this.uniqueOutputValue(examples) != null) {
			return new DecisionTree(this.uniqueOutputValue(examples));
		}
		
		else if (attributes.size() == 0) {
			return new DecisionTree(this.pluralityValue(examples));
		}
		
		
		
		else {
			
			Variable A = this.mostImportantVariable(attributes, examples);
			DecisionTree tree = new DecisionTree(A);
		
			for(String vk : A.domain) {
				Set<Example> exs = examplesWithValueForAttribute(examples,A,vk);
				attributes.remove(A);
				DecisionTree subtree = learn(exs,attributes,examples);
				tree.children.add(subtree);
			}
			
			return tree;
		}
			

	}
	
	
	/**
	public static void main(String a[]) {
		
		
		//creating 5 new examples
		Example example1 = new Example();
		Example example2 = new Example();
		Example example3 = new Example();
		Example example4 = new Example();
		Example example5 = new Example();
		
		//setting output values for the examples
		example1.setOutputValue("one");
		example2.setOutputValue("two");
		example3.setOutputValue("three");
		example4.setOutputValue("four");
		example5.setOutputValue("four");
		
		//adding values to set
		Set<Example> examples = new Set<Example>();
		examples.add(example1);
		examples.add(example2);
		examples.add(example3);
		examples.add(example4);
		examples.add(example5);
		
		String pluralityVALUE = pluralityValue(examples);
		
		System.out.println("Hello Java");
		System.out.println("Hello " + pluralityVALUE);
		
	}
	
	*/
	
	/**
	 * Returns the most common output value among a set of Examples,
	 * breaking ties randomly.
	 * I don't do the random part yet.
	 */
	@Override
	protected String pluralityValue(Set<Example> examples) {
		
		List outputValueList = new ArrayList<String>();
		
		//adding all output values to a list
		Iterator iterator = examples.iterator();
		while (iterator.hasNext()) {
			outputValueList.add( iterator.getOutputValue() );
		}
		
		//finding the most common output value
		int frequency = 0;
		String mostCommonOutput = "";
		for( int i=0; i<outputValueList.size(); i++ ) {
			
			String outputValue = outputValueList.get(i);
			int currentItemFrequency = Collections.frequency(outputValueList, outputValue );
			
			if( currentItemFrequency > frequency ) {
				frequency = currentItemFrequency;
				mostCommonOutput = outputValue;
			}
		}		
		
		return mostCommonOutput;
		
	}
	
	/**
	 * Returns the single unique output value among the given examples
	 * if there is only one, otherwise null.
	 */
	@Override
	protected String uniqueOutputValue(Set<Example> examples) {
	    
		List outputValueList = new ArrayList<String>();
		
		//adding all output values to a list
		Iterator iterator = examples.iterator();
		while (iterator.hasNext()) {
			outputValueList.add( iterator.getOutputValue() );
		}
		
		//finding the single unique output value
		int frequency = 0;
		String uniqueOutput = "";
		for( int i=0; i<outputValueList.size(); i++ ) {
			
			String outputValue = outputValueList.get(i);
			int currentItemFrequency = Collections.frequency(outputValueList, outputValue );
			
			if( currentItemFrequency == 1) {
				return outputValue;
			}
		}		
		
		return null;
	}
	
	//
	// Utility methods required by the AbstractDecisionTreeLearner
	//

	/**
	 * Return the subset of the given examples for which Variable a has value vk.
	 */
	@Override
	protected Set<Example> examplesWithValueForAttribute(Set<Example> examples, Variable a, String vk) {
	    
		Set<Example> subset = new Set<Example>();
		
		for (Example e : examples) {
			if (e.getInputValue(a).equals(vk)) {
				Example example = new Example();
				example = e;
				subset.add(example);
			}			
		}
		
		return subset;	
	}
	
	/**
	 * Return the number of the given examples for which Variable a has value vk.
	 */
	@Override
	protected int countExamplesWithValueForAttribute(Set<Example> examples, Variable a, String vk) {
		int result = 0;
		for (Example e : examples) {
			if (e.getInputValue(a).equals(vk)) {
				result += 1;
			}
		}
		return result;
		
	}

	/**
	 * Return the number of the given examples for which the output has value vk.
	 */
	@Override
	protected int countExamplesWithValueForOutput(Set<Example> examples, String vk) {
	    int result = 0;
		for (Example e: examples) {
			if (e.getOutputValue().equals(vk)) {
				result++;
			}			
		}		
		return result;
		
	}

}
