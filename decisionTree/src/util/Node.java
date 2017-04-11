package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;










import util.DataSet.TYPE;
import javafx.util.Pair;

public class Node {
	private static final String below="<";
	private static final String above=">=";
	private static int PART1 = 1;
	private static int PART2 = 2;
	private static int ALL = 3;
	private String index;
	int minsplit;
	int minbucket;
	HashMap<Integer, HashMap<Object, HashMap<String, Double>>> counts;
	HashMap<Integer, ArrayList<Object>> alternativeAttr;
	// ArrayList<Integer> trainingRecords;
	// ArrayList<Integer> testRecords;
	ArrayList<Integer> records;
	double evaluation;
	HashMap<Integer, ArrayList<String>> splitAttrSet;
	// public ArrayList<Integer> getRecords() {
	// return records;
	// }
	HashMap<String, Double> targetCounts;
	String label;
	int splitAttrID;
	double splitValue = 0.0;
	DataSet dataSet;
	//TYPE attrType;
	// String splitAttrName;
	HashMap<String, Node> links;
//	public void post_pruning(){
//		post_pruning(this);
//	}
	public Pair<Integer, Double> post_pruning(){
		if(splitAttrID==-1){
			//Pair<Integer, Double> tempHashMap=new Pair(arg0, arg1)<Integer, Double>();
			int leafNum=1;   //PART1存放子节点中叶子个数
			double errorNum=0.0;	//PART2存放总共的错误数
			double max=0;
			for (String string : targetCounts.keySet()) {
				if(targetCounts.get(string)>max){
					errorNum+=max;
					max=targetCounts.get(string);
				}else errorNum+=targetCounts.get(string);
			}
			return new Pair<Integer, Double>(leafNum, errorNum);
		}
		ArrayList<Pair<Integer, Double>> subInfMaps=new ArrayList<>();
		ArrayList<Node> tempNodeList=new ArrayList<>();
		for (Node node : links.values()) {
			if(!tempNodeList.contains(node)){
				tempNodeList.add(node);
				subInfMaps.add(node.post_pruning());
			}
		}
		int leaves=0;
		double subTreeErrorNum=0;
		for (Pair<Integer, Double> pair : subInfMaps) {
			leaves+=pair.getKey();
			subTreeErrorNum+=pair.getValue();
		}
		double max=0,thisErrorNum=0,totalNum=0;
		for (String string : targetCounts.keySet()) {
			double tempCounts=targetCounts.get(string);
			totalNum+=tempCounts;
			if(tempCounts>max){
				thisErrorNum+=max;
				max=tempCounts;
			}else thisErrorNum+=tempCounts;
		}
		double subTreeError=(subTreeErrorNum+0.5*leaves)/totalNum;
		//double thisError=(thisErrorNum+0.5)/totalNum;
		double subTreeSD=Math.sqrt(totalNum*subTreeError*(1-subTreeError));
		if(subTreeErrorNum+0.5*leaves+subTreeSD>thisErrorNum+0.5){
			splitAttrID=-1;
			return new Pair<Integer, Double>(1, thisErrorNum);
		}
		return new Pair<Integer, Double>(leaves, subTreeErrorNum);
	}
	public String treeDeduction(Record record){
		String targetClass = null;
		Stack<Pair<Node, Double>> nodes;
		HashMap<String, Double> votes;
		nodes = new Stack<>();
		votes = new HashMap<>();
		try {
			ArrayList<Object> values = record.getValues();
			nodes.push(new Pair<Node, Double>(this, 1.0));
			while (!nodes.isEmpty()) {
				Pair<Node, Double> tempPair = nodes.pop();
				Node tempNode = tempPair.getKey();
				double weight = tempPair.getValue();
				if (tempNode.splitAttrID == -1) {
					if (!votes.containsKey(tempNode.label))	votes.put(tempNode.label, 0.0);
					votes.put(tempNode.label, votes.get(tempNode.label)	+ weight);
				} else {
					if(dataSet.getAttrType().get(tempNode.splitAttrID)==TYPE.continuous){
						if(new Double((String)values.get(tempNode.splitAttrID))<tempNode.splitValue){
							nodes.push(new Pair<Node, Double>(tempNode.links.get(Node.below),weight));
						}else if(new Double((String)values.get(tempNode.splitAttrID))>=tempNode.splitValue){
							nodes.push(new Pair<Node, Double>(tempNode.links.get(Node.above),weight));
						}else System.err.println("hhhhhh11");
					}else if(dataSet.getAttrType().get(tempNode.splitAttrID)==TYPE.decritize){
						if (tempNode.links.containsKey(values.get(tempNode.splitAttrID))) {
							nodes.push(new Pair<Node, Double>(tempNode.links.get(values.get(tempNode.splitAttrID)),weight));
						} else {
							int num = tempNode.links.keySet().size();
							for (String string : tempNode.links.keySet()) {
								nodes.push(new Pair<Node, Double>(tempNode.links.get(string), weight/ num));
							}
						}
					}else System.err.println("hhhhhhhhhh");
				}
			}
			double tempWeight = -1;
			for (HashMap.Entry<String, Double> item : votes.entrySet()) {
				// if(votes.keySet().size()>1)
				// System.out.println("class:"+item.getKey()+"   votes:"+item.getValue());
				if (item.getValue() > tempWeight) {
					targetClass = item.getKey();
					tempWeight = item.getValue();
				}
			}
//				 System.out.print("record:");
//				 for (Object string : values) {
//				 System.out.print(string+" ");
//				 }
//				 System.out.println("(real/predict):  ("+values.get(data.getTargetID())+"/"+label+")");
			} catch (Exception e) {
				e.printStackTrace();
			}
		return  targetClass;
	}
	public ArrayList<Integer> treeDeduction(DataSet data,ArrayList<Integer> indexs){
		ArrayList<Integer> errors=new ArrayList<>();
		Stack<Pair<Node, Double>> nodes;
		HashMap<String, Double> votes;
		int correct = 0;
		for (Integer integer : indexs) {
			Record record=data.getRecords().get(integer);
			nodes = new Stack<>();
			votes = new HashMap<>();
			try {
				ArrayList<Object> values = record.getValues();
				nodes.push(new Pair<Node, Double>(this, 1.0));
				while (!nodes.isEmpty()) {
					Pair<Node, Double> tempPair = nodes.pop();
					Node tempNode = tempPair.getKey();
					double weight = tempPair.getValue();
					if (tempNode.splitAttrID == -1) {
						if (!votes.containsKey(tempNode.label))	votes.put(tempNode.label, 0.0);
						votes.put(tempNode.label, votes.get(tempNode.label)	+ weight);
					} else {
						if(dataSet.getAttrType().get(tempNode.splitAttrID)==TYPE.continuous){
							if(values.get(tempNode.splitAttrID).equals(dataSet.getNAstring())){
								int num = tempNode.links.keySet().size();
								for (String string : tempNode.links.keySet()) {
									nodes.push(new Pair<Node, Double>(tempNode.links.get(string), weight/ num));
								}
							}else if(new Double((String)values.get(tempNode.splitAttrID))<tempNode.splitValue){
								nodes.push(new Pair<Node, Double>(tempNode.links.get(Node.below),weight));
							}else if(new Double((String)values.get(tempNode.splitAttrID))>=tempNode.splitValue){
								nodes.push(new Pair<Node, Double>(tempNode.links.get(Node.above),weight));
							}else System.err.println("hhhhhh11");
						}else if(dataSet.getAttrType().get(tempNode.splitAttrID)==TYPE.decritize){
							if (tempNode.links.containsKey(values.get(tempNode.splitAttrID))) {
								nodes.push(new Pair<Node, Double>(tempNode.links.get(values.get(tempNode.splitAttrID)),weight));
							} else {
								// System.err.println("AttrName:"+dataSet.getAttributeNames().get(tempNode.splitAttrID)+" value:"+values.get(tempNode.splitAttrID));
								int num = tempNode.links.keySet().size();
								for (String string : tempNode.links.keySet()) {
									nodes.push(new Pair<Node, Double>(tempNode.links.get(string), weight/ num));
								}
							}
						}else System.err.println("hhhhhhhhhh");
					}
				}
				String label = null;
				double tempWeight = -1;
				for (HashMap.Entry<String, Double> item : votes.entrySet()) {
					// if(votes.keySet().size()>1)
					// System.out.println("class:"+item.getKey()+"   votes:"+item.getValue());
					if (item.getValue() > tempWeight) {
						label = item.getKey();
						tempWeight = item.getValue();
					}
				}
				if (values.get(data.getTargetID()).equals(label)) correct += 1;
				else errors.add(integer);

//				 System.out.print("record:");
//				 for (Object string : values) {
//				 System.out.print(string+" ");
//				 }
//				 System.out.println("(real/predict):  ("+values.get(data.getTargetID())+"/"+label+")");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("test information: precision:"
				+ String.format("%.5f", correct/ (double) indexs.size())
				+ "  (correct/total): (" + correct + "/"
				+ indexs.size() + ")");
		return  errors;
	}
	public double treeDeduction(DataSet data) {
		return treeDeduction(data,true);
	}
	public double treeDeduction(DataSet data,boolean display) {
		Stack<Pair<Node, Double>> nodes;
		HashMap<String, Double> votes;
		int correct = 0;
		for (Record record : data.getRecords()) {
			nodes = new Stack<>();
			votes = new HashMap<>();
			try {
				ArrayList<Object> values = record.getValues();
				nodes.push(new Pair<Node, Double>(this, 1.0));
				while (!nodes.isEmpty()) {
					Pair<Node, Double> tempPair = nodes.pop();
					Node tempNode = tempPair.getKey();
					double weight = tempPair.getValue();
					if (tempNode.splitAttrID == -1) {
						if (!votes.containsKey(tempNode.label))	votes.put(tempNode.label, 0.0);
						votes.put(tempNode.label, votes.get(tempNode.label)	+ weight);
					} else {
						if(dataSet.getAttrType().get(tempNode.splitAttrID)==TYPE.continuous){
							if(values.get(tempNode.splitAttrID).equals(dataSet.getNAstring())){
								int num = tempNode.links.keySet().size();
								for (String string : tempNode.links.keySet()) {
									nodes.push(new Pair<Node, Double>(tempNode.links.get(string), weight/ num));
								}
							}else if(new Double((String)values.get(tempNode.splitAttrID))<tempNode.splitValue){
								nodes.push(new Pair<Node, Double>(tempNode.links.get(Node.below),weight));
							}else if(new Double((String)values.get(tempNode.splitAttrID))>=tempNode.splitValue){
								nodes.push(new Pair<Node, Double>(tempNode.links.get(Node.above),weight));
							}else System.err.println("hhhhhh11");
						}else if(dataSet.getAttrType().get(tempNode.splitAttrID)==TYPE.decritize){
							if (tempNode.links.containsKey(values.get(tempNode.splitAttrID))) {
								nodes.push(new Pair<Node, Double>(tempNode.links.get(values.get(tempNode.splitAttrID)),weight));
							} else {
								// System.err.println("AttrName:"+dataSet.getAttributeNames().get(tempNode.splitAttrID)+" value:"+values.get(tempNode.splitAttrID));
								int num = tempNode.links.keySet().size();
								for (String string : tempNode.links.keySet()) {
									nodes.push(new Pair<Node, Double>(tempNode.links.get(string), weight/ num));
								}
							}
						}else System.err.println("hhhhhhhhhh");
					}
				}
				String label = null;
				double tempWeight = -1;
				for (HashMap.Entry<String, Double> item : votes.entrySet()) {
					// if(votes.keySet().size()>1)
					// System.out.println("class:"+item.getKey()+"   votes:"+item.getValue());
					if (item.getValue() > tempWeight) {
						label = item.getKey();
						tempWeight = item.getValue();
					}
				}
				if (values.get(data.getTargetID()).equals(label))
					correct += 1;

//				 System.out.print("record:");
//				 for (Object string : values) {
//				 System.out.print(string+" ");
//				 }
//				 System.out.println("(real/predict):  ("+values.get(data.getTargetID())+"/"+label+")");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(display)
		System.out.println("test information: precision:"
				+ String.format("%.5f", correct/ (double) data.getRecords().size())
				+ "  (correct/total): (" + correct + "/"
				+ data.getRecords().size() + ")");
		return correct/ (double) data.getRecords().size();
	}
	public void displayCounts(){
		for (Integer integer : counts.keySet()) {
			System.out.println("Attr┌-"+dataSet.getAttributeNames().get(integer));
			for (Object object : counts.get(integer).keySet()) {
				System.out.print("    ├---"+object+":    [");
				for (String string : counts.get(integer).get(object).keySet()) {
					System.out.print(" "+string+":"+counts.get(integer).get(object).get(string)+" ");
				}System.out.println("]");
			}
		}
	}
	public void displayTree() {
		Stack<Pair<Node, Pair<Integer, String>>> stack = new Stack<>();
		stack.push(new Pair<Node, Pair<Integer, String>>(this,new Pair<Integer, String>(0, null)));
		while (!stack.isEmpty()) {
			Pair<Node, Pair<Integer, String>> tempPair = stack.pop();
			int temp = tempPair.getValue().getKey();
			Node tempNode = tempPair.getKey();
			while (temp-- > 0)
				System.out.print("│  ");
			// System.out.print("├-");
			if (tempPair.getValue().getValue() == null)
				System.out.print("┌-root:");
			else {
				System.out.print("├-( "+tempPair.getValue().getValue()+"):");
				//System.out.println(tempNode.splitAttrID);
				//System.out.println(dataSet.getAttrType().containsKey(tempNode.splitAttrID));
//				if(dataSet.getAttrType().get(tempNode.splitAttrID)==TYPE.decritize){
//					for (String string : tempPair.getValue().getValue()) {
//						System.out.print("\"" + string + "\" ");
//				}else if(dataSet.getAttrType().get(tempNode.splitAttrID)==TYPE.continuous){
//					System.out.print("??"+splitValue);
//				}else System.err.println("wrong");
//				System.out.print("):");
			}
			if (tempNode.splitAttrID == -1)
				System.out.print(tempNode.label + "(leaf)");
			else {

				System.out.print(dataSet.getAttributeNames().get(tempNode.splitAttrID));
				// System.out.print("   Split into ( ");
				// for (String string : splitAttrSet.get(PART1)) {
				// System.out.print(string+" ");
				// }System.out.print(") & ( ");
				// for (String string : splitAttrSet.get(PART2)) {
				// System.out.print(string+" ");
				// }System.out.println(") ");
				if(dataSet.getAttrType().get(tempNode.splitAttrID)==TYPE.decritize){
					String tempString="";
					for (String string : tempNode.splitAttrSet.get(PART1)) {
						tempString+=string+" ";
					}
					stack.push(new Pair<Node, Pair<Integer, String>>(tempNode.links.get(tempNode.splitAttrSet.get(PART1).get(0)), new Pair<Integer, String>(tempPair.getValue().getKey() + 1,tempString)));
					tempString="";
					for (String string : tempNode.splitAttrSet.get(PART2)) {
						tempString+=string+" ";
					}
					stack.push(new Pair<Node, Pair<Integer, String>>(tempNode.links.get(tempNode.splitAttrSet.get(PART2).get(0)), new Pair<Integer, String>(tempPair.getValue().getKey() + 1,tempString)));
				}else if(dataSet.getAttrType().get(tempNode.splitAttrID)==TYPE.continuous){
					stack.push(new Pair<Node, Pair<Integer, String>>(tempNode.links.get(Node.below), new Pair<Integer, String>(tempPair.getValue().getKey() + 1,"<"+tempNode.splitValue)));
					stack.push(new Pair<Node, Pair<Integer, String>>(tempNode.links.get(Node.above), new Pair<Integer, String>(tempPair.getValue().getKey() + 1,">="+tempNode.splitValue)));
				}else System.err.println("wrong");
				// stack.push(new Pair<Node,
				// Integer>(tempNode.links.get(tempNode.splitAttrSet.get(PART2).get(0)),
				// tempPair.getValue()+1));
			}
			System.out.print(" [ ");
			//double total = 0.0;
			//for (String string : tempNode.targetCounts.keySet()) {
			//	total += tempNode.targetCounts.get(string);
				// System.out.print(string+":"+tempNode.targetCounts.get(string)+" ");
			//}
			for (String string : tempNode.targetCounts.keySet()) {
				//System.out.print(string+ ":"+ tempNode.targetCounts.get(string)+ "/"+ String.format("%.3f",100 * (tempNode.targetCounts.get(string) / total)) + "% ");
				System.out.print(string+ ":"+ tempNode.targetCounts.get(string)+" ");
			}
			System.out.print("]");
			if(index.equals("infogain")) System.out.print("  Entropy="+String.format("%.4f", tempNode.evaluation));
			if(index.equals("misclasserror")) System.out.print("  MisClassificationError="+String.format("%.4f", tempNode.evaluation));
			System.out.println();
		}
	}

	// public void display(){
	// System.out.println("splitAttrID: "+splitAttrID);
	// // for (Integer conditionID: counts.keySet()) {
	// // System.out.println("AttrID:"+conditionID);
	// // for (String AttrValue : counts.get(conditionID).keySet()) {
	// // System.out.println("\tAttrValue:"+AttrValue);
	// // System.out.println("\tcounts:");
	// // //System.out.println("\t\tAtt\ttargetValue\tcounts");
	// // for (String targetValue :
	// counts.get(conditionID).get(AttrValue).keySet()) {
	// //
	// System.out.println("\t\t"+targetValue+"\t"+counts.get(conditionID).get(AttrValue).get(targetValue));
	// // }
	// // }
	// // }
	// for (Integer integer : alternativeAttr.keySet()) {
	// System.out.print("AttrID:"+integer+" alternativeValues: ");
	// for (String string : alternativeAttr.get(integer)) {
	// System.out.print(string+"  ");
	// }System.out.println();
	// }
	// System.out.println("Gini: "+Gini);
	// }
	public Node() {
		// records=new ArrayList<>();
		// counts=new HashMap<>();
		// splitAttrID=-1;
		// dataSet=null;
		// //splitAttrName=null;
		// links=new HashMap<>();
	}
	private Node(Node node,int splitAttrID,String compare,double splitValue,double evaluation){
		this.index=node.index;
		this.alternativeAttr = new HashMap<>();
		this.records = new ArrayList<>();
		this.counts = new HashMap<>();
		this.splitAttrID = -1;
		// this.label=null;
		this.splitAttrSet = null;
		this.links = new HashMap<>();
		this.dataSet = node.dataSet;
		this.evaluation = evaluation;
		this.minsplit=node.minsplit;
		this.minbucket=node.minbucket;
		//ArrayList<Integer> NArecord=new ArrayList<>();
		for (Integer integer : node.records) {
			if(dataSet.getRecords().get(integer).getValues().get(splitAttrID).equals(dataSet.getNAstring())){
				//NArecord.add(integer);
				continue;
			}
			switch (compare) {
			case "<":
				if(new Double((String)dataSet.getRecords().get(integer).getValues().get(splitAttrID))<splitValue)	records.add(integer);
				break;
			case ">=":
				if(new Double((String)dataSet.getRecords().get(integer).getValues().get(splitAttrID))>=splitValue)	records.add(integer);
				break;
			default:
				System.err.println("has a value equal to the splitValue");
				break;
			}
		}
		HashMap<String, Double> votes = new HashMap<>();
		ArrayList<Integer> NARecords=new ArrayList<>();
		HashMap<Integer, HashMap<Object, Double>> attributeValueCounts=new HashMap<>();
		HashMap<Integer, Double> attributeTotalCounts=new HashMap<>();
		//HashMap<Integer, HashMap<Object, Double>> attributeValueCounts=new HashMap<>();
		for (Integer integer : records) {
			Record record = dataSet.getRecords().get(integer);
			ArrayList<Object> values = record.getValues();
			String targetClass = (String)values.get(dataSet.getTargetID());
			if (!votes.containsKey(targetClass))
				votes.put(targetClass, 0.0);
			votes.put(targetClass, votes.get(targetClass) + record.getWeight());
			try {
				if (values.size() != dataSet.getAttrNum())
					throw new Exception("haiya haoqia");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return;
			}
			boolean flag=true;
			for (Integer conditionID : dataSet.getConditionsID()) {
				if(values.get(conditionID).equals(dataSet.getNAstring())) {
					if(flag){
						flag=false;
						NARecords.add(integer);
					}
					continue;
				}
				if (!counts.containsKey(conditionID)) {
					attributeValueCounts.put(conditionID, new HashMap<>());
					attributeTotalCounts.put(conditionID, 0.0);
					alternativeAttr.put(conditionID, new ArrayList<>());
					counts.put(conditionID, new HashMap<>());
				}
				if (!counts.get(conditionID).containsKey(values.get(conditionID))) {
					alternativeAttr.get(conditionID).add(values.get(conditionID));
					attributeValueCounts.get(conditionID).put(values.get(conditionID), 0.0);
					counts.get(conditionID).put(values.get(conditionID), new HashMap<>());
				}
				if (!counts.get(conditionID).get(values.get(conditionID)).containsKey(targetClass)) {
					counts.get(conditionID).get(values.get(conditionID)).put(targetClass, 0.0);
				}
				attributeTotalCounts.put(conditionID, attributeTotalCounts.get(conditionID)+record.getWeight());
				attributeValueCounts.get(conditionID).put(values.get(conditionID),attributeValueCounts.get(conditionID).get(values.get(conditionID))+record.getWeight());
				counts.get(conditionID).get(values.get(conditionID)).put(targetClass,counts.get(conditionID).get(values.get(conditionID)).get(targetClass)+ record.getWeight());
			}
			//if(flag) total++;
		}
//		for (Integer conditionID : dataSet.getConditionsID()) {	//统计
//			if(counts.containsKey(conditionID)&&counts.get(conditionID).containsKey(dataSet.getNAstring())){
//				for (String string : counts.get(conditionID).get(dataSet.getNAstring()).keySet()) {
//					//double NAweight=
//				}
//			}
//		}
		for (Integer integer : attributeTotalCounts.keySet()) {
			double total=attributeTotalCounts.get(integer);
			for (Object obj : attributeValueCounts.get(integer).keySet()) {
				attributeValueCounts.get(integer).put(obj, attributeValueCounts.get(integer).get(obj)/total);
			}
		}
		for (Integer integer : NARecords) {
			Record record=dataSet.getRecords().get(integer);
			ArrayList<Object> values = record.getValues();
			//ArrayList<Integer> NAindexs=new ArrayList<>();
			String targetClass=(String)values.get(dataSet.getTargetID());
			for (Integer i : dataSet.getConditionsID()) {
				if(values.get(i).equals(dataSet.getNAstring())&&counts.containsKey(i)){
					//NAindexs.add(i);
					for (Object obj : counts.get(i).keySet()) {
						if(!counts.get(i).get(obj).containsKey(targetClass)) counts.get(i).get(obj).put(targetClass, 0.0);
						counts.get(i).get(obj).put(targetClass, counts.get(i).get(obj).get(targetClass)+record.getWeight()*attributeValueCounts.get(i).get(obj));
						//counts.get(naIndex).get(obj).put(values.get(dataSet.getTargetID()), counts.get(naIndex).get(obj).)
					}
				}
			}
			//double weight=record.getWeight()/(double)NAindexs.size();
//			for (Integer naIndex : NAindexs) {
//				for (Object obj : counts.get(naIndex).keySet()) {
//					if(!counts.get(naIndex).get(obj).containsKey(targetClass)) counts.get(naIndex).get(obj).put(targetClass, 0.0);
//					counts.get(naIndex).get(obj).put(targetClass, counts.get(naIndex).get(obj).get(targetClass)+weight*attributeValueCounts.get(naIndex).get(obj));
//					//counts.get(naIndex).get(obj).put(values.get(dataSet.getTargetID()), counts.get(naIndex).get(obj).)
//				}
//			}
		}
		for (Iterator<HashMap.Entry<Integer, ArrayList<Object>>> it = alternativeAttr.entrySet().iterator(); it.hasNext();) {
			HashMap.Entry<Integer, ArrayList<Object>> item = it.next();
			if(dataSet.getAttrType().get(item.getKey())==TYPE.decritize&&item.getValue().size()>15){
				System.out.println("decretize attr \""+dataSet.getAttributeNames().get(item.getKey())+"\" has more than 15 values.");
				it.remove();
			}else if (item.getValue().size() < 2)	it.remove();
		}

		targetCounts = new HashMap<>(votes);
		double tempVotes = -1;
		for (String string : votes.keySet()) {
			// System.out.println("string: " + string + "  votes:"
			// + votes.get(string));
			if (tempVotes < votes.get(string)) {
				// System.out.println("choose:" + string);
				this.label = new String(string);
				tempVotes = votes.get(string);
			}
		}
	}
	private Node(Node node, int splitAttrID, ArrayList<String> splitAttrSet,double evaluation) {
		this.alternativeAttr = new HashMap<>();
		this.records = new ArrayList<>();
		this.counts = new HashMap<>();
		this.splitAttrID = -1;
		// this.label=null;
		this.splitAttrSet = null;
		this.links = new HashMap<>();
		this.dataSet = node.dataSet;
		this.evaluation = evaluation;
		this.index=node.index;
		this.minsplit=node.minsplit;
		this.minbucket=node.minbucket;
		// alternativeAttr.remove(splitAttrID);
		// alternativeAttr.put(splitAttrID, new ArrayList<>());
		// for (String string : splitAttrSet) {
		// alternativeAttr.get(splitAttrID).add(string);
		// }
		// if (alternativeAttr.get(splitAttrID).size() < 2)
		// alternativeAttr.remove(splitAttrID);
		// int targetID = dataSet.getTargetID();
		for (Integer integer : node.records) {
			if (splitAttrSet.contains(dataSet.getRecords().get(integer).getValues().get(splitAttrID))) {
				records.add(integer);
			}
		}
		//new DataSet(dataSet, records).display();
		HashMap<String, Double> votes = new HashMap<>();
		ArrayList<Integer> NARecords=new ArrayList<>();
		HashMap<Integer, HashMap<Object, Double>> attributeValueCounts=new HashMap<>();
		HashMap<Integer, Double> attributeTotalCounts=new HashMap<>();
		for (Integer integer : records) {
			Record record = dataSet.getRecords().get(integer);
			ArrayList<Object> values = record.getValues();
			String targetClass = (String)values.get(dataSet.getTargetID());
			if (!votes.containsKey(targetClass))
				votes.put(targetClass, 0.0);
			votes.put(targetClass, votes.get(targetClass) + record.getWeight());
			try {
				if (values.size() != dataSet.getAttrNum())
					throw new Exception("haiya haoqia");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return;
			}
			boolean flag=true;
			for (Integer conditionID : dataSet.getConditionsID()) {
				if(values.get(conditionID).equals(dataSet.getNAstring())) {
					if(flag){
						flag=false;
						NARecords.add(integer);
					}
					continue;
				}
				if (!counts.containsKey(conditionID)) {
					attributeValueCounts.put(conditionID, new HashMap<>());
					attributeTotalCounts.put(conditionID, 0.0);
					alternativeAttr.put(conditionID, new ArrayList<>());
					counts.put(conditionID, new HashMap<>());
				}
				if (!counts.get(conditionID).containsKey(values.get(conditionID))) {
					attributeValueCounts.get(conditionID).put(values.get(conditionID), 0.0);
					alternativeAttr.get(conditionID).add(values.get(conditionID));
					counts.get(conditionID).put(values.get(conditionID), new HashMap<>());
				}
				if (!counts.get(conditionID).get(values.get(conditionID)).containsKey(targetClass)) {
					counts.get(conditionID).get(values.get(conditionID)).put(targetClass, 0.0);
				}
				attributeTotalCounts.put(conditionID, attributeTotalCounts.get(conditionID)+record.getWeight());
				attributeValueCounts.get(conditionID).put(values.get(conditionID),attributeValueCounts.get(conditionID).get(values.get(conditionID))+record.getWeight());
				counts.get(conditionID).get(values.get(conditionID)).put(targetClass,counts.get(conditionID).get(values.get(conditionID)).get(targetClass)+ record.getWeight());
			}
		}
		for (Integer integer : attributeTotalCounts.keySet()) {
			double total=attributeTotalCounts.get(integer);
			for (Object obj : attributeValueCounts.get(integer).keySet()) {
				attributeValueCounts.get(integer).put(obj, attributeValueCounts.get(integer).get(obj)/total);
			}
		}
		for (Integer integer : NARecords) {
			Record record=dataSet.getRecords().get(integer);
			ArrayList<Object> values = record.getValues();
			//ArrayList<Integer> NAindexs=new ArrayList<>();
			String targetClass=(String)values.get(dataSet.getTargetID());
			for (Integer i : dataSet.getConditionsID()) {
				if(values.get(i).equals(dataSet.getNAstring())&&counts.containsKey(i)){
					//NAindexs.add(i);
					for (Object obj : counts.get(i).keySet()) {
						if(!counts.get(i).get(obj).containsKey(targetClass)) counts.get(i).get(obj).put(targetClass, 0.0);
						counts.get(i).get(obj).put(targetClass, counts.get(i).get(obj).get(targetClass)+record.getWeight()*attributeValueCounts.get(i).get(obj));
						//counts.get(naIndex).get(obj).put(values.get(dataSet.getTargetID()), counts.get(naIndex).get(obj).)
					}
				}
			}
//			double weight=record.getWeight()/(double)NAindexs.size();
//			for (Integer naIndex : NAindexs) {
////				if(!counts.containsKey(naIndex)) {
////					System.out.println("this attr has no other values to imply, ignore.");
////				}
//				for (Object obj : counts.get(naIndex).keySet()) {
//					if(!counts.get(naIndex).get(obj).containsKey(targetClass)) counts.get(naIndex).get(obj).put(targetClass, 0.0);
//					counts.get(naIndex).get(obj).put(targetClass, counts.get(naIndex).get(obj).get(targetClass)+weight*attributeValueCounts.get(naIndex).get(obj));
//					//counts.get(naIndex).get(obj).put(values.get(dataSet.getTargetID()), counts.get(naIndex).get(obj).)
//				}
//			}
		}
		for (Iterator<HashMap.Entry<Integer, ArrayList<Object>>> it = alternativeAttr.entrySet().iterator(); it.hasNext();) {
			HashMap.Entry<Integer, ArrayList<Object>> item = it.next();
			if(dataSet.getAttrType().get(item.getKey())==TYPE.decritize&&item.getValue().size()>15){
				System.out.println("decretize attr \""+dataSet.getAttributeNames().get(item.getKey())+"\" has more than 15 values.");
				it.remove();
			}else if (item.getValue().size() < 2)
				it.remove();
		}

		targetCounts = new HashMap<>(votes);
		double tempVotes = -1;
		for (String string : votes.keySet()) {
			// System.out.println("string: " + string + "  votes:"
			// + votes.get(string));
			if (tempVotes < votes.get(string)) {
				// System.out.println("choose:" + string);
				this.label = new String(string);
				tempVotes = votes.get(string);
			}
		}
	}
//	public Node(DataSet data,String index){
//		this(data,index,DecisionTree.DEFAULT_MINBUCKET,DecisionTree.DEFAULT_MINSPLIT);
//	}
	
	public Node(DataSet data,String index,int minbucket,int minsplit) {
		this.index=index;
		evaluation = Double.MAX_VALUE;
		splitAttrSet = null;
		alternativeAttr = new HashMap<>();
		records = new ArrayList<>();
		// testRecords=new ArrayList<>();
		counts = new HashMap<>();
		splitAttrID = -1;
		//targetCounts = new HashMap<>();
		label = null;
		links = new HashMap<>();
		dataSet = data;
		this.minsplit=minsplit;
		this.minbucket=minbucket;
		int targetID = dataSet.getTargetID();
		int totalNum = -1;
		HashMap<String, Double> votes = new HashMap<>();
		ArrayList<Integer> NARecords=new ArrayList<>();
		HashMap<Integer, HashMap<Object, Double>> attributeValueCounts=new HashMap<>();
		HashMap<Integer, Double> attributeTotalCounts=new HashMap<>();
		for (Record record : dataSet.getRecords()) {
			records.add(++totalNum);
			ArrayList<Object> values = record.getValues();
			try {
				if (values.size() != dataSet.getAttrNum())
					throw new Exception("the record received is not accordance with the datatset.");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return;
			}
			String targetClass = (String)values.get(targetID);
			if (!votes.containsKey(targetClass)) votes.put(targetClass, 0.0);
			votes.put(targetClass, votes.get(targetClass) + record.getWeight());
			//if (!targetCounts.containsKey(targetClass))	targetCounts.put(targetClass, 0.0);
			//targetCounts.put(targetClass, targetCounts.get(targetClass)	+ record.getWeight());
			boolean flag=true;
			for (Integer conditionID : dataSet.getConditionsID()) {
				if(values.get(conditionID).equals(dataSet.getNAstring())) {
					//System.out.println("pos="+totalNum);
					if(flag){
						flag=false;
						NARecords.add(totalNum);
					}
					continue;
				}
				if (!counts.containsKey(conditionID)) {
					attributeValueCounts.put(conditionID, new HashMap<>());
					attributeTotalCounts.put(conditionID, 0.0);
					alternativeAttr.put(conditionID, new ArrayList<>());
					counts.put(conditionID, new HashMap<>());
				}
				if (!counts.get(conditionID).containsKey(values.get(conditionID))) {
					attributeValueCounts.get(conditionID).put(values.get(conditionID), 0.0);
					alternativeAttr.get(conditionID).add(values.get(conditionID));
					counts.get(conditionID).put(values.get(conditionID), new HashMap<>());
				}
				if (!counts.get(conditionID).get(values.get(conditionID)).containsKey(values.get(targetID))) {
					counts.get(conditionID).get(values.get(conditionID)).put(targetClass, 0.0);
				}
				attributeTotalCounts.put(conditionID, attributeTotalCounts.get(conditionID)+record.getWeight());
				attributeValueCounts.get(conditionID).put(values.get(conditionID),attributeValueCounts.get(conditionID).get(values.get(conditionID))+record.getWeight());
				counts.get(conditionID).get(values.get(conditionID)).put(targetClass,counts.get(conditionID).get(values.get(conditionID)).get(values.get(targetID))+ record.getWeight());
			}
		}
		//System.out.println("values:"+attributeValueCounts);
		//System.out.println("total:"+attributeTotalCounts);
		for (Integer integer : attributeTotalCounts.keySet()) {
			double total=attributeTotalCounts.get(integer);
			for (Object obj : attributeValueCounts.get(integer).keySet()) {
				attributeValueCounts.get(integer).put(obj, attributeValueCounts.get(integer).get(obj)/total);
			}
		}
		//System.out.println("weight:"+attributeValueCounts);
		for (Integer integer : NARecords) {
			//System.out.println("NA destribute NO."+integer);
			Record record=dataSet.getRecords().get(integer);
			ArrayList<Object> values = record.getValues();
			//ArrayList<Integer> NAindexs=new ArrayList<>();
			String targetClass=(String)values.get(dataSet.getTargetID());
			for (Integer i : dataSet.getConditionsID()) {
				if(values.get(i).equals(dataSet.getNAstring())&&counts.containsKey(i)){
					//NAindexs.add(i);
					for (Object obj : counts.get(i).keySet()) {
						if(!counts.get(i).get(obj).containsKey(targetClass)) counts.get(i).get(obj).put(targetClass, 0.0);
						counts.get(i).get(obj).put(targetClass, counts.get(i).get(obj).get(targetClass)+record.getWeight()*attributeValueCounts.get(i).get(obj));
						//counts.get(naIndex).get(obj).put(values.get(dataSet.getTargetID()), counts.get(naIndex).get(obj).)
					}
				}
			}
//			double weight=record.getWeight()/(double)NAindexs.size();
//			for (Integer naIndex : NAindexs) {
//				//System.out.println("NAindex:"+naIndex);
//				for (Object obj : counts.get(naIndex).keySet()) {
//					if(!counts.get(naIndex).get(obj).containsKey(targetClass)) counts.get(naIndex).get(obj).put(targetClass, 0.0);
//					counts.get(naIndex).get(obj).put(targetClass, counts.get(naIndex).get(obj).get(targetClass)+weight*attributeValueCounts.get(naIndex).get(obj));
//					//System.out.println("destribute to obj:"+obj+" by "+weight*attributeValueCounts.get(naIndex).get(obj));
//					//counts.get(naIndex).get(obj).put(values.get(dataSet.getTargetID()), counts.get(naIndex).get(obj).)
//				}
//			}
		}
		for (Iterator<HashMap.Entry<Integer, ArrayList<Object>>> it = alternativeAttr.entrySet().iterator(); it.hasNext();) {
			HashMap.Entry<Integer, ArrayList<Object>> item = it.next();
			if(dataSet.getAttrType().get(item.getKey())==TYPE.decritize&&item.getValue().size()>15){
				//System.out.println("decretize attr \""+dataSet.getAttributeNames().get(item.getKey())+"\" has more than 15 values.");
				dataSet.getConditionsID().remove((Object)item.getKey());
				it.remove();
			}else if (item.getValue().size() < 2)
				it.remove();
		}
		targetCounts = new HashMap<>(votes);
		evaluation=0;
		for (String string : targetCounts.keySet()) {
			double Pcit=targetCounts.get(string)/totalNum;
			evaluation-=Pcit*Math.log(Pcit)/Math.log(2);
		}
		double tempVotes = -1;
		for (String string : votes.keySet()) {
			// System.out.println("string: " + string + "  votes:"
			// + votes.get(string));
			if (tempVotes < votes.get(string)) {
				// System.out.println("choose:" + string);
				this.label = new String(string);
				tempVotes = votes.get(string);
			}
		}
	}

	public HashMap<Integer, Node> bestSplit(boolean prePruning) {
		double currentEval = evaluation ;
		if(index.toLowerCase().equals("infogain")) currentEval=-1;
		int splitAttrID = -1;
		double splitValue=0.0;
		HashMap<Integer, ArrayList<String>> splitAttrSet = new HashMap<>();
		HashMap<Integer, Double> splitEval = new HashMap<>();
		HashMap<Integer, Node> link = new HashMap<>();
		if (alternativeAttr.keySet().size() == 0)
			return null;
		//dataSet.display();
		boolean terminate=true;
		for (Integer integer : alternativeAttr.keySet()) {// 针对每一个属性
			//System.out.println("integer:"+integer);
			//dataSet.display();
			// System.out.println("AttrID:"+integer);
			ArrayList<Object> alternativeAttrs = alternativeAttr.get(integer);
			if (dataSet.getAttrType().get(integer) == TYPE.decritize) {
				int size = alternativeAttrs.size();
				if (size > 15) {
					System.err.println("decritize attr has more than 15 values.");
					continue;
				}
				boolean update=false;
				for (int i = 1; i < Math.pow(2, size - 1); i++) {// 对属性的值生成不同的两个集合
					HashMap<Integer, ArrayList<String>> partition = new HashMap<>();
					partition.put(PART1, new ArrayList<>());
					partition.put(PART2, new ArrayList<>());
					// ArrayList<String> AnotherPart=new
					// ArrayList<>(alternativeAttr.get(integer));
					String s = Integer.toBinaryString(i);
					while (s.length() < size)
						s = "0" + s;
					for (int j = 0; j < size; j++) {
						if (s.charAt(j) == '1')
							partition.get(PART1).add((String)alternativeAttrs.get(j)); // 生成不同的集合
						else
							partition.get(PART2).add((String)alternativeAttrs.get(j));
					}
					// 算partion集合和另一半的对应不同类别的数
					HashMap<Integer, HashMap<String, Double>> table = new HashMap<>();
					table.put(PART1, new HashMap<>());
					table.put(PART2, new HashMap<>());
					HashMap<Integer, Double> totalNum = new HashMap<>();
					totalNum.put(PART1, 0.0);
					totalNum.put(PART2, 0.0);
					totalNum.put(ALL, 0.0);
					for (String valueString : partition.get(PART1)) {// 对属性中出现的值
						if (!counts.get(integer).containsKey(valueString)) continue;
						for (String targetString : counts.get(integer).get(valueString).keySet()) {
							if (!table.get(PART1).containsKey(targetString)) table.get(PART1).put(targetString, 0.0);
							double tempNum = counts.get(integer).get(valueString).get(targetString);
							totalNum.put(PART1, tempNum + totalNum.get(PART1));
							totalNum.put(ALL, tempNum + totalNum.get(ALL));
							table.get(PART1).put(targetString,tempNum+ table.get(PART1).get(targetString));
						}
					}
					for (String valueString : partition.get(PART2)) {
						if (!counts.get(integer).containsKey(valueString))	continue;
						for (String targetString : counts.get(integer).get(valueString).keySet()) {
							if (!table.get(PART2).containsKey(targetString)) table.get(PART2).put(targetString, 0.0);
							double tempNum = counts.get(integer).get(valueString).get(targetString);
							totalNum.put(PART2, tempNum + totalNum.get(PART2));
							totalNum.put(ALL, tempNum + totalNum.get(ALL));
							table.get(PART2).put(targetString,tempNum+ table.get(PART2).get(targetString));
						}
					}
					if(prePruning&&(totalNum.get(ALL)<minbucket||totalNum.get(PART1)<minsplit||totalNum.get(PART2)<minsplit)) continue;
					HashMap<Integer, Double> EvaluationTable=null;
					//double Eval;
					boolean flag=false;
					switch (index.toLowerCase()) {
					case "gini":
							EvaluationTable=Gini_index(table, totalNum);
							if (EvaluationTable.get(ALL) < currentEval) flag=true;
							//Eval=Gini_index(table, totalNum)
						break;
					case "infogain":
							EvaluationTable=information_gain(table, totalNum);
							if (EvaluationTable.get(ALL) > currentEval) flag=true;
						break;
					case "misclasserror":
						EvaluationTable=misclassification_error(table, totalNum);
						if (EvaluationTable.get(ALL) < currentEval) flag=true;
						break;
					default:
						System.err.println("errrrrrrrrr");
						break;
					}
					if(flag){
						update=true;
						terminate=false;
						currentEval = EvaluationTable.get(ALL);
						splitAttrID = integer;
						splitAttrSet = partition;
						splitEval = EvaluationTable;
					}
					//HashMap<Integer, Double> Ginis=Gini_index(table, totalNum);
					//double gini=Ginis.get(ALL);
					
				}
				if(update){
					//System.out.println("in   integer="+integer);
					//if(prePruning){
						switch (index.toLowerCase()) {
						case "gini":
							if (currentEval >= evaluation) return null;
							break;
						case "infogain":
							if (currentEval <= 0.0) return null;
							break;
						case "misclasserror":
							if (currentEval >= evaluation) return null;
							break;
						default:
							break;
						}
					//}
					this.splitAttrSet = splitAttrSet;
					this.splitAttrID = splitAttrID;
					Node node1 = new Node(this, splitAttrID, splitAttrSet.get(PART1),splitEval.get(PART1));
					Node node2 = new Node(this, splitAttrID, splitAttrSet.get(PART2),splitEval.get(PART2));
					for (String string : splitAttrSet.get(PART1)) {
						links.put(string, node1);
					}
					for (String string : splitAttrSet.get(PART2)) {
						links.put(string, node2);
					}
					link.put(PART1, node1);
					link.put(PART2, node2);
				}
			}else if(dataSet.getAttrType().get(integer) == TYPE.continuous){
				try {
//					ArrayList<Double> alternativeValues=new ArrayList<>();
//					for (Object object : alternativeAttrs) {
//						//System.out.println(object);
//						//System.out.println((double)object);
//						if(!alternativeValues.contains(Double.parseDouble(object.toString())))
//							alternativeValues.add(Double.parseDouble(object.toString()));
//					}
//					Collections.sort(alternativeValues);
					Collections.sort(alternativeAttrs, new Comparator<Object>() {
						public int compare(Object o1,Object o2){
							return new Double((String)o1).compareTo(new Double((String)o2));
						}
					});
					int pos=0;
					while(pos<alternativeAttrs.size()-1){
						if(alternativeAttrs.get(pos).equals(alternativeAttrs.get(pos+1))){
							alternativeAttrs.remove(pos+1);
						}else pos++;
					}
					HashMap<Integer, HashMap<String, Double>> table = new HashMap<>();
					table.put(PART1, new HashMap<>());
					table.put(PART2, new HashMap<>());
					HashMap<Integer, Double> totalNum = new HashMap<>();
					totalNum.put(PART1, 0.0);
					totalNum.put(PART2, 0.0);
					totalNum.put(ALL, 0.0);
					//if(counts.get(integer).keySet().size()<2)System.err.println("attr just has one value.");
					//else {
						for (Object attrValues : counts.get(integer).keySet()) {  // 对于属性的每个数值
							for(String tarValues: counts.get(integer).get(attrValues).keySet()){ //统计所有数值的不同类记录
								if(!table.get(PART2).containsKey(tarValues)) table.get(PART2).put(tarValues, 0.0);
								double tempNum=counts.get(integer).get(attrValues).get(tarValues);
								table.get(PART2).put(tarValues, tempNum+table.get(PART2).get(tarValues));
								totalNum.put(PART2, totalNum.get(PART2)+tempNum);
								totalNum.put(ALL, totalNum.get(ALL)+tempNum);
							}
						}//首先将所有的记录放在PART2中
					//}
					boolean update=false;
					for (int i = 0; i < alternativeAttrs.size()-1; i++) {
						//System.out.println(alternativeValues.get(i));
						//System.out.println(counts.get(integer).get((Object)0));
						//System.out.println(counts.get(integer).get("0"));
						for(String string:counts.get(integer).get(alternativeAttrs.get(i)).keySet()){
							if(!table.get(PART1).containsKey(string)) table.get(PART1).put(string, 0.0);
							double tempNum=counts.get(integer).get(alternativeAttrs.get(i)).get(string);
							table.get(PART1).put(string, table.get(PART1).get(string)+tempNum);
							table.get(PART2).put(string, table.get(PART2).get(string)-tempNum);
							totalNum.put(PART1, totalNum.get(PART1)+tempNum);
							totalNum.put(PART2, totalNum.get(PART2)-tempNum);
							if(table.get(PART2).get(string)==0) table.get(PART2).remove(string);
						}
						if(prePruning&&(totalNum.get(ALL)<minbucket||totalNum.get(PART1)<minsplit||totalNum.get(PART2)<minsplit)) continue;
						HashMap<Integer, Double> EvaluationTable=null;
						//double Eval;
						boolean flag=false;
						switch (index.toLowerCase()) {
						case "gini":
								EvaluationTable=Gini_index(table, totalNum);
								if (EvaluationTable.get(ALL) < currentEval) flag=true;
								//Eval=Gini_index(table, totalNum)
							break;
						case "infogain":
							//System.out.println("infogain");
								EvaluationTable=information_gain(table, totalNum);
								if (EvaluationTable.get(ALL) > currentEval) flag=true;
								//System.out.println(EvaluationTable);
							break;
						case "misclasserror":
							EvaluationTable=misclassification_error(table, totalNum);
							if (EvaluationTable.get(ALL) < currentEval) flag=true;
							break;
						default:
							System.err.println("errrrrr1");
							//System.out.println("default");
							break;
						}
						if(flag){
							update=true;
							terminate=false;
							currentEval = EvaluationTable.get(ALL);
							splitAttrID = integer;
							//System.out.println("i="+i);
							//System.out.println(new Double((String)alternativeAttrs.get(i))+"+"+new Double((String)alternativeAttrs.get(i+1)));
							try {
								splitValue=(new Double((String)alternativeAttrs.get(i))+new Double((String)alternativeAttrs.get(i+1)))/2;
							} catch (Exception e) {
								// TODO: handle exception
								System.out.println("i="+i+"    "+alternativeAttrs.get(i)+"   "+alternativeAttrs.get(i+1));
								e.printStackTrace();
							}
							//System.out.println(splitValue);
							splitEval = EvaluationTable;
						}
						//HashMap<Integer, Double> Ginis = Gini_index(table, totalNum);
						//double gini=Ginis.get(ALL);
						
					}
					if(update){
						//System.out.println("in   integer="+integer);
						//if(prePruning){
							switch (index.toLowerCase()) {
							case "gini":
								if (currentEval >= evaluation) return null;
								break;
							case "infogain":
								if (currentEval <= 0) return null;
								break;
							case "misclasserror":
								if (currentEval >= evaluation) return null;
								break;	
							default:
								System.err.println("errrrr2:"+index);
								break;
							}
						//}
						this.splitValue = splitValue;
						this.splitAttrID = splitAttrID;
						//System.out.println("splitID="+splitAttrID);
						Node node1 = new Node(this, splitAttrID,Node.below, splitValue,splitEval.get(PART1));
						Node node2 = new Node(this, splitAttrID,Node.above,splitValue,splitEval.get(PART2));
						links.put(Node.below, node1);
						links.put(Node.above, node2);
						link.put(PART1, node1);
						link.put(PART2, node2);		
					}
				} catch (NumberFormatException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				//Collections.sort(a);
			}
		}
		if(terminate) return null;
		else return link;
	}
	private HashMap<Integer, Double> misclassification_error(HashMap<Integer, HashMap<String, Double>> table,HashMap<Integer, Double> totalNum){
		HashMap<Integer, Double> misClassErrorTable = new HashMap<>();
		//misclassErrorTable.put(PART1, 1.0);
		//misclassErrorTable.put(PART2, 1.0);
		double max=0;
		for (String string : table.get(PART1).keySet()) {
			double Pcit=table.get(PART1).get(string)/totalNum.get(PART1);
			if(Pcit>max){
				max=Pcit;
			}
			//misclassErrorTable.put(PART1,misclassErrorTable.get(PART1)-Pcit*Math.log(Pcit)/Math.log(2));
		}
		misClassErrorTable.put(PART1,1-max);
		max=0;
		for (String string : table.get(PART2).keySet()) {
			double Pcit=table.get(PART2).get(string)/totalNum.get(PART2);
			if(Pcit>max){
				max=Pcit;
			}
		}
		misClassErrorTable.put(PART2,1-max);
		double misClassError = (totalNum.get(PART1) / totalNum.get(ALL))* misClassErrorTable.get(PART1)+(totalNum.get(PART2) / totalNum.get(ALL))* misClassErrorTable.get(PART2);
		misClassErrorTable.put(ALL, misClassError);
		return misClassErrorTable;
	}
	private HashMap<Integer, Double> information_gain(HashMap<Integer, HashMap<String, Double>> table,HashMap<Integer, Double> totalNum){
		HashMap<Integer, Double> infGainTable = new HashMap<>();
		infGainTable.put(PART1, 0.0);
		infGainTable.put(PART2, 0.0);
		for (String string : table.get(PART1).keySet()) {
			double Pcit=table.get(PART1).get(string)/totalNum.get(PART1);
			infGainTable.put(PART1,infGainTable.get(PART1)-Pcit*Math.log(Pcit)/Math.log(2));
		}
		for (String string : table.get(PART2).keySet()) {
			double Pcit=table.get(PART2).get(string)/totalNum.get(PART2);
			infGainTable.put(PART2,infGainTable.get(PART2)-Pcit*Math.log(Pcit)/Math.log(2));
		}
		double infGain = evaluation-(totalNum.get(PART1) / totalNum.get(ALL))* infGainTable.get(PART1)-(totalNum.get(PART2) / totalNum.get(ALL))* infGainTable.get(PART2);
		infGainTable.put(ALL, infGain);
		return infGainTable;
	}
	private HashMap<Integer, Double> Gini_index(HashMap<Integer, HashMap<String, Double>> table,HashMap<Integer, Double> totalNum){
		HashMap<Integer, Double> Ginis = new HashMap<>();
		Ginis.put(PART1, 1.0);
		Ginis.put(PART2, 1.0);
		// System.out.println("split: ");
		// System.out.print("( ");
		// for (String string : partition.get(PART1)) {
		// System.out.print(string+" ");
		// }System.out.print(") ");
		// System.out.print("( ");
		// for (String string : partition.get(PART2)) {
		// System.out.print(string+" ");
		// }System.out.println(") ");
		// System.out.println("Gini PART1:   total:"+totalNum.get(PART1));
		for (String string : table.get(PART1).keySet()) {
			// System.out.println("Value: "+string+"   counts: "+ table.get(PART1).get(string));
			Ginis.put(PART1,Ginis.get(PART1)- Math.pow((table.get(PART1).get(string) / totalNum.get(PART1)), 2));
		}
		// System.out.println("Gini PART2:   total:"+totalNum.get(PART2));
		for (String string : table.get(PART2).keySet()) {
			// System.out.println("Value: "+string+"   counts: "+ table.get(PART2).get(string));
			Ginis.put(PART2,Ginis.get(PART2)- Math.pow((table.get(PART2).get(string) / totalNum.get(PART2)), 2));
		}
		double gini = (totalNum.get(PART1) / totalNum.get(ALL))	* Ginis.get(PART1)	+ (totalNum.get(PART2) / totalNum.get(ALL))	* Ginis.get(PART2);
		// System.out.println("gini:"+gini);
		Ginis.put(ALL, gini);
		return Ginis;
	}
	public void run(boolean prePruning) {
		//node.displayTree();
		HashMap<Integer, Node> root = this.bestSplit(prePruning);
		// System.out.println("display");
		//displayTree();
		//displayCounts();
		//&&root.get(PART1)!=null&&root.get(PART2)!=null
		if (root!=null) {
			if (prePruning&&(Math.abs(this.evaluation) < Math.pow(10, -4))) {
				System.err.println("no need to split");
				return;
			}
			root.get(PART1).run(prePruning);
			root.get(PART2).run(prePruning);
		}
	}
}
