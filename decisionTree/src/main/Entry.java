package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javafx.util.Pair;


import util.DataSet;
import util.DecisionTree;
import util.Node;
import file.Files;

public class Entry {
	public static void main(String[] args){
		Node node;
		DataSet data;
		int minBucket,minSplit;
		
		
		//不适合postPruning
		//data=Files.readFile("car.csv",true,"evaluate",true);minBucket=180;minSplit=30;
		
		//建的树太高
		//data=Files.readFile("tictactoe.csv","V9");minBucket=20;minSplit=7;
		
		
		//三份数据集
		//data=Files.readFile("iris.txt",true,"Species");minBucket=55;minSplit=25;
		data=Files.readFile("ecoli.csv","V8");minBucket=50;minSplit=10;
		//data=Files.readFile("zoo.csv","V17","V1+V2+V3+V4+V5+V6+V7+V8+V9+V10+V11+V12+V13+V14+V15+V16");minBucket=10;minSplit =3;
		
		//(a)
//		System.out.println("holdout:");
//		//for(int i=0;i<10;i++)
//		node=DecisionTree.Generate(data,"holdout","infogain",false,false,minBucket,minSplit);
//		node.displayTree();
//
//		System.out.println("bootstrap:");
//		//for(int i=0;i<10;i++)
//		node=DecisionTree.Generate(data,"bootstrap","infogain",false,false,minBucket,minSplit);
//		node.displayTree();
//
//		System.out.println("10-fold:");
//		//for(int i=0;i<10;i++)
//		node=DecisionTree.Generate(data,"10-fold","infogain",false,false,minBucket,minSplit);
//		node.displayTree();
		
//		//(b)
//		System.out.println("holdout:");
//		//for(int i=0;i<10;i++)
//		node=DecisionTree.Generate(data,"holdout","gini",false,false,minBucket,minSplit);
//		node.displayTree();
//
//		System.out.println("bootstrap:");
//		//for(int i=0;i<10;i++)
//		node=DecisionTree.Generate(data,"bootstrap","gini",false,false,minBucket,minSplit);
//		node.displayTree();
//
//		System.out.println("10-fold:");
//		//for(int i=0;i<10;i++)
//		node=DecisionTree.Generate(data,"10-fold","gini",false,false,minBucket,minSplit);
//		node.displayTree();
		
		//(c)
//		System.out.println("holdout:");
//		//for(int i=0;i<10;i++)
//		node=DecisionTree.Generate(data,"holdout","misclasserror",true,false,minBucket,minSplit);
//		node.displayTree();
//
//		System.out.println("bootstrap:");
//		//for(int i=0;i<10;i++)
//		node=DecisionTree.Generate(data,"bootstrap","misclasserror",true,false,minBucket,minSplit);
//		node.displayTree();
//
//		System.out.println("10-fold:");
//		//for(int i=0;i<10;i++)
//		node=DecisionTree.Generate(data,"10-fold","misclasserror",true,false,minBucket,minSplit);
//		node.displayTree();
		
		//(d)
//		System.out.println("Without prePruning or postPruning:");
//		node=DecisionTree.Generate(data,"holdout","infogain",false,false,minBucket,minSplit);
//		node.displayTree();
//		System.out.println("With prePruning:");
//		node=DecisionTree.Generate(data,"holdout","infogain",true,false,minBucket,minSplit);
//		node.displayTree();
//		System.out.println("With postPruning:");
//		node=DecisionTree.Generate(data,"holdout","infogain",false,true,minBucket,minSplit);
//		node.displayTree();
//		System.out.println("With prePruning and postPruning:");
//		node=DecisionTree.Generate(data,"holdout","infogain",minBucket,minSplit);
//		node.displayTree();
		
		//(e)
//		data=Files.readFile("iris.txt",true,"Species",true);minBucket=55;minSplit=25;
//		//data=Files.readFile("ecoli.csv","V8",true);minBucket=50;minSplit=10;
//		//data=Files.readFile("zoo.csv","V17","V1+V2+V3+V4+V5+V6+V7+V8+V9+V10+V11+V12+V13+V14+V15+V16",true);minBucket=10;minSplit =3;
//		data.display();
//		node=DecisionTree.Generate(data,"bootstrap","infogain",true,false,minBucket,minSplit);
//		node.displayTree();
		
		//(f)
		//data=Files.readFile("iris.txt",true,"Species");minBucket=20;minSplit=7;
		//data=Files.readFile("ecoli.csv","V8");minBucket=50;minSplit=30;
		data=Files.readFile("zoo.csv","V17","V1+V2+V3+V4+V5+V6+V7+V8+V9+V10+V11+V12+V13+V14+V15+V16");minBucket=20;minSplit =7;
		ArrayList<Pair<Node, Double>> nodes=DecisionTree.adaBoost(data, "infogain",true,false,minBucket,minSplit);
		int i=1;
		for (Pair<Node, Double> pair : nodes) {
			System.out.println("Tree "+i+++":");
			pair.getKey().displayTree();
		}
	}
}
