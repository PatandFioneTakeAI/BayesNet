package heaney.lebold.bayesnet;

public class Node {

	private String name;
	private String[] parents;
	private double[] cpt;
	
	private NodeType type;
	
	public Node(Node other){
		this.name = other.name;
		this.parents = other.parents;
		this.cpt = other.cpt;
		this.type = other.type;
	}
	
	public Node(String name, String[] parents, double[] cpt){
		this.name = name;
		this.parents = parents;
		this.cpt = cpt;
		
		this.type = NodeType.UNKNOWN;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setType(NodeType type){
		this.type = type;
	}
	
	public String[] getParents(){
		return this.parents;
	}
	
	public NodeType getType(){
		return this.type;
	}
	
	public double getProbability(NodeType[] types){
		int index = 0;
		for(int n=0; n<types.length; n++){
			if(types[n] == NodeType.TRUE)
				index += Math.pow(2, n);
		}
		return this.cpt[index];
	}
}
