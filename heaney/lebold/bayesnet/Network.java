package heaney.lebold.bayesnet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Network {
	
	private HashMap<String,Node> nodeGraph;
	private Node queryNode;
	
	public Network(){
		this.nodeGraph = new HashMap<String,Node>();
	}
	
	public Network(Network other){
		this.nodeGraph = new HashMap<String,Node>();
		for(String name: other.nodeGraph.keySet()){
			Node newNode = new Node(other.nodeGraph.get(name));
			this.nodeGraph.put(name, newNode);
			if(newNode.getType() == NodeType.QUERY)
				this.queryNode = newNode;
		}
	}
	
	private LinkedList<Node> buildNetwork(String filename){
		File file = new File(filename);
		try {
			LinkedList<Node> nodesAdded = new LinkedList<Node>();
			
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()){
				String inputLine = scanner.nextLine();
				String nodeName = inputLine.substring(0,inputLine.indexOf(":"));
				
				String nodeParentsStr = inputLine.substring(inputLine.indexOf("[")+1, inputLine.indexOf("]"));
				String[] nodeParents = nodeParentsStr.split(" ");
				if(nodeParents == null)
					nodeParents = new String[0];
				if(nodeParents.length==1 && nodeParents[0].equals(""))
					nodeParents = new String[0];
				
				String nodeCPTValsStr = inputLine.substring(inputLine.indexOf("]")+3, inputLine.length()-1);
				String[] nodeCPTVals = nodeCPTValsStr.split(" ");
				double[] cpt = new double[nodeCPTVals.length];
				for(int n=0; n<cpt.length; n++)
					cpt[n] = Double.parseDouble(nodeCPTVals[n]);
				
				Node node = new Node(nodeName, nodeParents, cpt);
				nodesAdded.add(node);
				this.pushNode(node);
			}
			scanner.close();
			return nodesAdded;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void setNodeTypes(String filename, LinkedList<Node> order){
		File file = new File(filename);
		try{
			Scanner scanner = new Scanner(file);
			String[] input = scanner.nextLine().split(",");
			
			for(int n=0; n<order.size(); n++){
				Node node = order.get(n);
				
				char indicator = input[n].charAt(0);
				switch(indicator){
				case 't':
					node.setType(NodeType.TRUE);
					break;
				case 'f':
					node.setType(NodeType.FALSE);
					break;
				case '?':
					node.setType(NodeType.QUERY);
					this.queryNode = node;
					break;
				case '-':
					node.setType(NodeType.UNKNOWN);
					break;
				}
			}
			scanner.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private double rejectionSampling(int numSamples){
		double numTrue = 0;
		double total = 0;
		for(int n=0; n<numSamples; n++){
			Network networkCopy = new Network(this);
			NodeType type = this.rejectionSamplingHelper(networkCopy.queryNode);
			if(type == NodeType.TRUE){
				numTrue++;
				total++;
			}
			else if(type == NodeType.FALSE)
				total++;
		}
		return numTrue/total;
	}
	
	private NodeType rejectionSamplingHelper(Node node){
		NodeType[] parentResults = new NodeType[node.getParents().length];
		for(int n=0; n<node.getParents().length; n++){
			String name = node.getParents()[n];
			Node parent = this.nodeGraph.get(name);
			NodeType result = this.rejectionSamplingHelper(parent);
			parentResults[n] = result;
		}
		double probability = node.getProbability(parentResults);
		double sample = Math.random();
		if(sample < probability)
			node.setType(NodeType.TRUE);
		else
			node.setType(NodeType.FALSE);
		return node.getType();
	}

	private double[] likelihoodWeightingSampling(int numSamples){
		double numTrue = 0;
		double weightTotal = 0;
		for(int n=0; n<numSamples; n++){
			Network networkCopy = new Network(this);
			double weight = this.likelihoodWeightingHelper(networkCopy.queryNode);
			if(weight >= 0)
				numTrue++;
			weightTotal += weight;
		}
		double[] returnArr = {numTrue/numSamples,Math.abs(weightTotal)/numSamples};
		return returnArr;
	}
	
	private double likelihoodWeightingHelper(Node node){
		NodeType[] parentResults = new NodeType[node.getParents().length];
		double weight = 1;
		for(int n=0; n<node.getParents().length; n++){
			String name = node.getParents()[n];
			Node parent = this.nodeGraph.get(name);
			weight = this.likelihoodWeightingHelper(parent);
			if(weight >= 0)
				parentResults[n] = NodeType.TRUE;
			else
				parentResults[n] = NodeType.FALSE;
			weight = Math.abs(weight);
		}
		if(node.getType() == NodeType.TRUE)
			return weight;
		else if(node.getType() == NodeType.FALSE)
			return -weight;
		double probability = node.getProbability(parentResults);
		double sample = Math.random();
		if(sample < probability){
			node.setType(NodeType.TRUE);
			return probability * weight;
		}
		else{
			node.setType(NodeType.FALSE);
			return -probability * weight;
		}
	}
	
	public void pushNode(Node node){
		this.nodeGraph.put(node.getName(),node);
	}
	
	public static void main(String[] args){
		if(args.length == 3){
			String filename = args[0];
			Network network = new Network();
			LinkedList<Node> nodesAdded = network.buildNetwork(filename);
			
			filename = args[1];
			network.setNodeTypes(filename,nodesAdded);
			
			int numSamples = Integer.parseInt(args[2]);
			double rejectionProbability = network.rejectionSampling(numSamples);
			double[] likelihoodProbability = network.likelihoodWeightingSampling(numSamples);
			
			System.out.println("Sampled " + numSamples + " times.");
			System.out.println("Rejection Sampling yielded true: " + (int)(rejectionProbability*100) + "%");
			System.out.println("Likelihood Weighting Sampling yielded true: " + (int)(likelihoodProbability[0]*100) + "%");
			System.out.println("Likelihood Weighting Sampling weight avg: " + likelihoodProbability[1]);
		}
		else{
			System.out.println("Incorrect args:");
			System.out.println("\t$: java -jar BayesNet.jar <filename> <filename> <num-samples>");
		}
	}
}
