package heaney.lebold.bayesnet;

/* Type: public class Node
 * 	- Extends: Object
 * 	- Description: This class represents one node on a Bayesian network
 */
public class Node {

	/* Fields: 
	 * 	- private String name : The name of this Node 
	 * 	- private String[] parents : The names of this Node's parents
	 * 	- private double[] cpt : The conditional probability table for this Node
	 * 
	 * 	- private NodeType type : The classification of this Node
	 */
	private String name;
	private String[] parents;
	private double[] cpt;
	
	private NodeType type;
	
	/* Constructor: public Node(Node other)
	 * 	- Params:
	 * 		- Node other : Data from this object is copied into this class
	 */
	public Node(Node other){
		this.name = other.name;
		this.parents = other.parents;
		this.cpt = other.cpt;
		this.type = other.type;
	}
	
	/* Constructor: public Node(String name, String[] parents, double[] cpt)
	 * 	- Params:
	 * 		- String name : The name of this Node
	 * 		- String[] parents : The names of this Node's parents
	 * 		- double[] cpt : The conditional probability of this Node
	 */
	public Node(String name, String[] parents, double[] cpt){
		this.name = name;
		this.parents = parents;
		this.cpt = cpt;
		
		this.type = NodeType.UNKNOWN;
	}
	
	/* Method: public String getName()
	 * 	- Params:
	 * 		- n/a
	 * 	- Returns:
	 * 		- String : The name of this Node
	 * 
	 * 	- Description: Accessor method for name
	 */
	public String getName(){
		return this.name;
	}

	/* Method: public String[] getParents()
	 * 	- Params:
	 * 		- n/a
	 * 	- Returns:
	 * 		- String[] : The names of this Node's parents
	 */
	public String[] getParents(){
		return this.parents;
	}

	/* Method: public void setType(NodeType type)
	 * 	- Params:
	 * 		- NodeType type : The classification of this Node
	 * 	- Returns:
	 * 		- void
	 * 
	 * 	- Description: Mutator method for type
	 */
	public void setType(NodeType type){
		this.type = type;
	}
	
	/* Method: public NodeType getType()
	 * 	- Params:
	 * 		- n/a
	 * 	- Returns:
	 * 		- NodeType : The classification of this Node
	 * 
	 * 	- Description: Accessor method for type
	 */
	public NodeType getType(){
		return this.type;
	}
	
	/* Method: public double getProbability(NodeType[] types)
	 * 	- Params:
	 * 		- NodeType[] types : An array mapping this Node's parents to their classifications in the network
	 * 	- Returns:
	 * 		- double : The conditional probability that this node will return TRUE
	 * 
	 * 	- Description:
	 * 		- This method returns the probability that this node will be true given its parents' classifications
	 */
	public double getProbability(NodeType[] types){
		int index = 0;
		for(int n=0; n<types.length; n++){
			if(types[n] == NodeType.TRUE)
				index += Math.pow(2, n);
		}
		return this.cpt[index];
	}
}
