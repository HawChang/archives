package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javafx.util.Pair;


public class DecisionTree {
	public  static final int DEFAULT_MINSPLIT=10;
	public  static final int DEFAULT_MINBUCKET=30;
	private static final int ROUND=20;
	public static Node Generate(DataSet data) {
		return Generate(data, "bootstrap","infogain");
	}
	public static Node Generate(DataSet data,String type) {
		return Generate(data, type,"infogain");
	}
//	public static ArrayList<Pair<Node, Double>> adaBoost(DataSet data,String index,int minBucket,int minSplit){
//		return adaBoost(data, index, true, true, minBucket, minSplit);
//	}
//	public static ArrayList<Pair<Node, Double>> adaBoost(DataSet data,String index){
//		return adaBoost(data, index, true, true, DEFAULT_MINBUCKET, DEFAULT_MINSPLIT);
//	}
//	public static ArrayList<Pair<Node, Double>> adaBoost(DataSet data,String index,Boolean prePruning,Boolean postPruning){
//		return adaBoost(data, index, prePruning, postPruning, DEFAULT_MINBUCKET, DEFAULT_MINSPLIT);
//	}
	public static ArrayList<Pair<Node, Double>> adaBoost(DataSet data,String index,Boolean prePruning,Boolean postPruning,int minBucket,int minSplit){
		ArrayList<Pair<Node, Double>> results=new ArrayList<>();
		ArrayList<Integer> dataIndexs = new ArrayList<>();
		int size=0;
		for (; size < data.getRecords().size(); size++) {
			dataIndexs.add(size);
		}
		if(size==0) return null;
		Collections.shuffle(dataIndexs);
		HashMap<Integer, Double> trainSetWeight=new HashMap<Integer, Double>();//ѵ��Adaboost�ļ��� ��Pair<������data�е����,��ӦȨ��>
		ArrayList<Integer> trainSetIndexs=new ArrayList<>();//ѵ��Adaboost��������������data�����
		ArrayList<Integer> testArrayList=new ArrayList<>(dataIndexs);//������Adaboost���Եļ���
		Random rand=new Random();
		double totalWeight=0;
		for (int i = 0; i < size; i++) {
			int tempInt=rand.nextInt(size);
			//trainSetWeight.put(tempInt, initialWeight);
			if(!trainSetIndexs.contains(tempInt)){
				trainSetIndexs.add(tempInt);
				testArrayList.remove((Object)tempInt);
			}
			//totalWeight+=initialWeight;
			//if(testArrayList.contains(tempInt)) 
		}        //����bootstrap������ѵ��Adaboost��ѵ���������ղ���Adaboost�Ĳ��Լ�
		double initialWeight=1/(double)trainSetIndexs.size();   //����ѵ������ ÿ����ʼ����ȨֵΪ1/N NΪ������
		for (Integer integer : trainSetIndexs) {
			totalWeight+=initialWeight;
			trainSetWeight.put(integer, initialWeight);
		}
		//System.out.println(trainSetWeight);
		//��ʼ��ѵ����������Ȩ�� ��Ϊ1/size Ȼ����ݼ�Ȩ��� �õ�bootstrap��������ѵ������������
		for (int i = 0; i < ROUND; i++) {
			//System.out.println(trainSetWeight);
			ArrayList<Integer> trainSetChosen=new ArrayList<>();
			for (int j = 0; j < size/2; j++) {
				double randNum=rand.nextDouble()*totalWeight;
				//System.out.println("randNum:"+randNum+"    totalNum:"+totalWeight);
				//if(randNum>149)	System.err.println("***************");
				int pos=-1;
				do{
					randNum-=trainSetWeight.get(trainSetIndexs.get(++pos));
				}while (randNum>0&&pos<trainSetIndexs.size()-1); 
				trainSetChosen.add(trainSetIndexs.get(pos));
			}//ʹ��bootstrap�������һ��ѵ����
			DataSet tempdDataSet=new DataSet(data,trainSetChosen);
			//System.out.println("trainSetChosen="+trainSetChosen.size());
			Node root=new Node(tempdDataSet,index,minBucket,minSplit);
			root.run(prePruning);
			//root.displayTree();
			if(postPruning) root.post_pruning(); //ѵ��������
			ArrayList<Integer> errorIndexs=root.treeDeduction(data, trainSetIndexs);//��������������ѵ���Ĳ������ �õ�Ԥ�������������
			//System.out.println(errorIndexs);
			double eM=0;
			for (Integer integer : errorIndexs) {
				eM+=trainSetWeight.get(integer);
			}                  //�õ���Ȩ����ֵ
			double alphaM=Math.log(1-eM)/(2*Math.log(eM));
			results.add(new Pair<Node, Double>(root, alphaM));
			double Zm=0;
			for (Integer integer : trainSetWeight.keySet()) {
				if(errorIndexs.contains(integer)){
					Zm+=trainSetWeight.get(integer)*Math.exp(alphaM);
				}else{
					Zm+=trainSetWeight.get(integer)*Math.exp(-alphaM);
				}
			}   //���Zm ʹȨֵ���º� �ܺ���ȻΪ��
			totalWeight=0;
			for (Integer integer : trainSetWeight.keySet()) {
				double nextWeight;
				if(errorIndexs.contains(integer)){
					nextWeight=trainSetWeight.get(integer)*Math.exp(alphaM)/Zm;
				}else{
					nextWeight=trainSetWeight.get(integer)*Math.exp(-alphaM)/Zm;
					//trainSetWeight.put(integer, trainSetWeight.get(integer)*Math.exp(-alphaM)/Zm);
				}
				trainSetWeight.put(integer, nextWeight);
				totalWeight+=nextWeight;
			} //����ѵ������Ȩֵ
		}
		//��Adaboost���в���
		ArrayList<Record> records=data.getRecords();
		int correct=0;
		for (Integer integer : testArrayList) {
			HashMap<String, Double> targetTable=new HashMap<>();
			for (Pair<Node,Double> pair : results) {
				String predictClass=pair.getKey().treeDeduction(records.get(integer));
				if(!targetTable.containsKey(predictClass)) targetTable.put(predictClass, 0.0);
				targetTable.put(predictClass, targetTable.get(predictClass)+pair.getValue());
			}
			double max=-1;
			String tempTarget=null;
			for (String string : targetTable.keySet()) {
				if(targetTable.get(string)>max){
					tempTarget=string;
					max=targetTable.get(string);
				}
			}
			if(records.get(integer).getValues().get(data.getTargetID()).equals(tempTarget)){
				correct++;
			}
		}
		System.out.println("AdaboostInfo: precision="+correct/(double)testArrayList.size()+"    ("+correct+"/"+testArrayList.size()+")");
		return results;
	}
//	private static Node bootStrap(DataSet data,ArrayList<Integer> dataIndexs,String index,boolean prePruning, boolean postPruning){
//		return bootStrap(data, dataIndexs, index, prePruning, postPruning, DEFAULT_MINBUCKET, DEFAULT_MINSPLIT);
//	}
	private static Node bootStrap(DataSet data,ArrayList<Integer> dataIndexs,String index,boolean prePruning, boolean postPruning,int minBucket,int minSplit){
		Node root=null;
		ArrayList<Integer> trainArrayList=new ArrayList<>();
		ArrayList<Integer> testArrayList=new ArrayList<>(dataIndexs);
		Random rand=new Random();
		for (int i = 0; i < dataIndexs.size(); i++) {
			int tempInt=rand.nextInt(dataIndexs.size());
			trainArrayList.add(tempInt);
			if(testArrayList.contains(tempInt)) testArrayList.remove((Object)tempInt);
		}
		DataSet tempDataSet=new DataSet(data,trainArrayList);
		//tempDataSet.display();
		root=new Node(tempDataSet,index,minBucket,minSplit);
		//root=new Node(tempDataSet);
		root.run(prePruning);
		//root.displayTree();
		//root.treeDeduction(new DataSet(data,testArrayList));
		if(postPruning) {
			root.post_pruning();
			//root.displayTree();
		}
		root.treeDeduction(new DataSet(data,testArrayList));
		return root;
	}
//	private static Node holdout(DataSet data,ArrayList<Integer> dataIndexs,String index,boolean prePruning, boolean postPruning){
//		return holdout(data, dataIndexs, index, prePruning, postPruning, DEFAULT_MINBUCKET, DEFAULT_MINSPLIT);
//	}
	private static Node holdout(DataSet data,ArrayList<Integer> dataIndexs,String index,boolean prePruning, boolean postPruning,int minBucket,int minSplit){
		Node root=null;
		ArrayList<Integer> trainArrayList=new ArrayList<>(dataIndexs);
		ArrayList<Integer> testArrayList=new ArrayList<>();
		int testSize=dataIndexs.size()/3;
		for (int i = 2*testSize; i < dataIndexs.size(); i++) {
			trainArrayList.remove(dataIndexs.get(i));
			testArrayList.add(dataIndexs.get(i));
		}
		DataSet tempdaDataSet=new DataSet(data,trainArrayList);
		//tempdaDataSet.display();
		root=new Node(tempdaDataSet,index,minBucket,minSplit);
		root.run(prePruning);
		//root.displayTree();
		if(postPruning) {
			root.post_pruning();
			//root.displayTree();
		}

		root.treeDeduction(new DataSet(data,testArrayList));
		return root;
	}
//	private static Node crossFold(DataSet data,ArrayList<Integer> dataIndexs,String index,int k,boolean prePruning, boolean postPruning){
//		return crossFold(data, dataIndexs, index, k, prePruning, postPruning, DEFAULT_MINBUCKET, DEFAULT_MINSPLIT);	
//	}
	private static Node crossFold(DataSet data,ArrayList<Integer> dataIndexs,String index,int k,boolean prePruning, boolean postPruning,int minBucket,int minSplit){
		Node root=null;
		int testSize=dataIndexs.size()/k;
		double precision=0;
		for (int i = 0; i < k; i++) {
			ArrayList<Integer> testArrayList=new ArrayList<>();
			ArrayList<Integer> trainArrayList=new ArrayList<>(dataIndexs);
			for (int j = testSize*i; j < testSize*(i+1); j++) {
				trainArrayList.remove(dataIndexs.get(j));
				testArrayList.add(dataIndexs.get(j));
			}
			//������ѵ���Ͳ��Լ�
			root=new Node(new DataSet(data,trainArrayList),index,minBucket,minSplit);
			root.run(prePruning);
			//root.displayTree();
			//root.treeDeduction(new DataSet(data,testArrayList));
			if(postPruning) {
				root.post_pruning();
				//root.displayTree();
			}
			precision+=root.treeDeduction(new DataSet(data,testArrayList));
		}
		System.out.println("total precision:"+String.format("%.5f", precision/10));
		return root;
	}
	public static Node Generate(DataSet data, String type,String index){
		return Generate(data, type, index, true, true,DEFAULT_MINBUCKET,DEFAULT_MINSPLIT);
	}
	public static Node Generate(DataSet data, String type,String index,boolean prePruning,boolean postPruning){
		return Generate(data, type, index, prePruning, postPruning,DEFAULT_MINBUCKET,DEFAULT_MINSPLIT);
	}
	public static Node Generate(DataSet data, String type,String index,int minBucket,int minSplit){
		return Generate(data, type, index, true, true,minBucket,minSplit);
	}
	public static Node Generate(DataSet data, String type,String index,boolean prePruning, boolean postPruning,int minBucket,int minSplit) {
		//HashMap<Integer, ArrayList<Integer>> trainNTestSet = new HashMap<>();
		//ArrayList<Record> tempRecords = data.getRecords();
		ArrayList<Integer> dataIndexs = new ArrayList<>();
		int size=0;
		for (; size < data.getRecords().size(); size++) {
			dataIndexs.add(size);
		}
		Collections.shuffle(dataIndexs);
		//ArrayList<Integer> trainArrayList=null;
		//ArrayList<Integer> testArrayList=null;
		Node root=null;
		//int testSize;
		switch (type) {
		case "bootstrap":
			root=bootStrap(data,dataIndexs,index,prePruning,postPruning,minBucket,minSplit);
			break;
		case "holdout":
			root=holdout(data, dataIndexs, index,prePruning,postPruning,minBucket,minSplit);
			break;
		default:
			Pattern pattern = Pattern.compile("(\\d*)-fold");
			Matcher matcher = pattern.matcher(type);
			try {
				if (matcher.find()) {
					//System.out.println(matcher.group(1));
					int k = Integer.parseInt(matcher.group(1));
					root=crossFold(data, dataIndexs, index, k,prePruning,postPruning,minBucket,minSplit);
				} else	throw new Exception("wrong split method.");
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			break;
		}
//		System.out.print("tarin:");
//		for (Integer integer : trainArrayList) {
//			System.out.print(integer+"  ");
//		}System.out.println();
//		System.out.print("test:");
//		for (Integer integer : testArrayList) {
//			System.out.print(integer+"  ");
//		}System.out.println();
		return root;
	}
}
