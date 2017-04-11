package implement;

import informationExtractor.DataFeature;
import informationExtractor.InformationExtractor;
import informationExtractor.Options;
import informationExtractor.Structure;
import informationExtractor.attrTypeNClass.Type;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Savepoint;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import ranalysis.ranalysis;

public class MainFrame {
	String log;
	String options;
	InformationExtractor x;
	ranalysis r;
	Scanner s;
	DataFeature d;
	Map<String, Options> formatOptions;

	public void readOptions() {
		Options userOptions = d.getOptions();
		// 在构造函数已经初始化了的选项
		// userOptions.addPair("header", "T");
		// userOptions.addPair("sep", ",");
		// userOptions.addPair("na.strings", "NA");
		// userOptions.addPair("sequential", "F");
		// userOptions.addPair("discretize", "T");
		// userOptions.addPair("rules", "F");
		// userOptions.addPair("ratio", "0.8");
		// userOptions.addPair("verbose", "F");
		// ****************************
		// userOptions.addPair("object", "");
		// userOptions.addPair("trainFile", "");
		// userOptions.addPair("testFile", "");
		// userOptions.addPair("values", "");
		// userOptions.addPair("target", "");
		// userOptions.addPair("DTree.method", "");
		// userOptions.addPair("pdf", "F");
		// userOptions.addPair("minsplit", "");
		// userOptions.addPair("minbucket", "");
		// userOptions.addPair("maxdepth", "");
		String option = "";
		Options op;
		do {
			option = s.nextLine();
			op = x.getOptions(option, "=", ",");
			if (option.equals("-help")) {
				displayFile("help.txt");
				continue;
			}
			if (op.isNull("verbose") || op.getValue("verbose").equals("F")) {
				System.out.println("user's option:");
				op.display();
			}
		} while (op.isNull("object") || op.isNull("trainFile"));
		// userOptions.addPair("expression",
		// userOptions.getValue("target")+"~"+getValuesExpression());
		// if (op.isNull("testFile"))
		// System.out.println("no testFile selected.");
		userOptions.setOriginalInformation(op.getOriginalInformation());
		op.alterIfExist("trainFile", "\\\\", "/");
		op.alterIfExist("testFile", "\\\\", "/");
		op.alterIfExist("outputTXT", "\\\\", "/");
		op.alterIfExist("outputPDF", "\\\\", "/");
		// System.out.println(op.getValue("addr"));
		op.alterIfExist("values", ",", "\",\"");
		op.alterIfExist("na.strings", ",", "\",\"");
		// if(op.isNull("values")) op.addPair("values",
		// getValuesExpression("\",\""),false);
		// else op.addPair("values", getValuesExpression("\",\""),true);
		userOptions.alterAll(op);
//		if (userOptions.getValue("verbose").equals("F")) {
//			System.out.println("full options set:");
//			userOptions.display();
//		}
	}

	public void inputData() {
		// System.out.println("reading files...");
		r.readData(d.getOptions());
		// System.out.println("reading succed.");
		d.setDataStructure(r.str());
	}

	// public void format() {
	// boolean
	// verbose=d.getOptions().getValue("verbose").equals("T")?true:false;
	// Options op = new Options();
	// Structure st = new Structure();
	// if(verbose) d.setDataStructure(r.str());
	// else{
	// do {
	// System.out.println("data structure:");
	// //st = x.extractStr(r);
	// st=r.str();
	// st.display();
	// displayFile("format.txt");
	// op = x.getOptions(s.nextLine());
	// r.format(op, st);
	// } while (!op.getAttributes().isEmpty());
	// d.setDataStructure(st);
	// //System.out.println("data format success.");
	// }
	// }
	// public void algorithmChoose(){
	// String algorithmChoose=r.chooseAlgorithm(d);
	//
	// }
	public void writeRecord(String record) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(log), true));
			writer.append(record);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void creatRecord() {
		// boolean verbose =
		// d.getOptions().getValue("verbose").matches("(T|TRUE|t|true)") ? true
		// : false;
		try {
			DecimalFormat df = new DecimalFormat("#.###");
			String temp = null;
			String record = null;
			BufferedReader reader = new BufferedReader(new FileReader(new File(options)));
			// writer.append(temp + "sep\n");
			temp = reader.readLine();
			while (temp != null) {
				record = "";
				Options op = x.getOptions(temp);
				op.alterIfExist("trainFile", "\\\\", "/");
				op.alterIfExist("testFile", "\\\\", "/");
				op.alterIfExist("outputTXT", "\\\\", "/");
				op.alterIfExist("outputPDF", "\\\\", "/");
				op.alterIfExist("values", ",", "\",\"");
				op.alterIfExist("na.strings", ",", "\",\"");
				d.getOptions().alterAll(op);
				System.out.println("processing file:" + op.getValue("trainFile"));
				inputData();
				if (op.isNull("values"))
					op.addPair("values", getValuesExpression("\",\""), false);
				else
					op.addPair("values", getValuesExpression("\",\""), true);
				// System.out.println(op.getValue("values"));
				// format();
				// d.setDataStructure(r.scale(op,d.getDataStructure()));
				// d.getDataStructure().display();
				d.setDataFeature(r.dataFeatureExtract(d.getOptions(), d.getDataStructure()));
				// **运行每个算法
				long startTime = System.currentTimeMillis();
				Map<String, Double> precises = new HashMap<>();
				Map<String, Double> times = new HashMap<>();

				// MLP***********************************
				 System.out.println("caculating MLP:");
				 precises.put("MLP",MLP(true,true));
				 double MLPTime=(System.currentTimeMillis()-startTime)/1000.0;
				 times.put("MLP", MLPTime);

				// GLMNET***********************************
				startTime = System.currentTimeMillis();
				System.out.println("caculating GLMNET:");
				precises.put("GLMNET", GLMNET(true,true));
				double GLMNETTime = (System.currentTimeMillis() - startTime) / 1000.0;
				times.put("GLMNET", GLMNETTime);

				// decisionTree***********************************
				// startTime=System.currentTimeMillis();
				// System.out.println("caculating decisionTree:");
				// precises.put("decisionTree",decisionTree(true));
				// double
				// DTreeTime=(System.currentTimeMillis()-startTime)/1000.0;
				// times.put("decisionTree", DTreeTime);

				// randomForest***********************************
				startTime = System.currentTimeMillis();
				System.out.println("caculating randomForest:");
				precises.put("randomForest", randomForest(true,true));
				double ranfomForestTime = (System.currentTimeMillis() - startTime) / 1000.0;
				times.put("randomForest", ranfomForestTime);

				// 写入文件
				// System.out.println("MLP="+df.format(MLP)+"   GLMNET="+df.format(GLMNET)+"    decisionTree="+df.format(decisionTree));
				Set<String> names = precises.keySet();
				String highestAlgorithm = null;
				double highest = -Double.MAX_VALUE;
				for (String string : names) {
					if (highest < precises.get(string)) {
						highestAlgorithm = string;
						highest = precises.get(string);
					} else if (highest > precises.get(string))
						continue;
					else {
						if (highestAlgorithm == null)
							continue;
						highestAlgorithm = (times.get(highestAlgorithm) < times.get(string)) ? highestAlgorithm : string;
						highest = precises.get(string);
					}
				}
				record += d.getOptions().getValue("trainFile") + "," + d.dataFeatureToString();
				if (highestAlgorithm == null)
					record += ",null,0";
				else
					record += "," + highestAlgorithm + "," + df.format(precises.get(highestAlgorithm));
				for (String string : names) {
					System.out.println(string + ":  precise=" + df.format(precises.get(string)) + "    time=" + times.get(string));
					// writer.append(","+df.format(precises.get(string))+","+times.get(string));
				}
				record += "\n";
				temp = reader.readLine();
				writeRecord(record);
				cleanUp();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void auto_run() {
		// boolean verbose =
		// d.getOptions().getValue("verbose").matches("(T|TRUE|t|true)") ? true
		// : false;
		try {
			String temp = null;
			BufferedReader reader = new BufferedReader(new FileReader(new File(options)));
			// writer.append(temp + "sep\n");
			temp = reader.readLine();
			while (temp != null) {
				Options op = x.getOptions(temp);
				op.alterIfExist("trainFile", "\\\\", "/");
				op.alterIfExist("testFile", "\\\\", "/");
				op.alterIfExist("outputTXT", "\\\\", "/");
				op.alterIfExist("outputPDF", "\\\\", "/");
				op.alterIfExist("values", ",", "\",\"");
				op.alterIfExist("na.strings", ",", "\",\"");
				d.getOptions().alterAll(op);
				System.out.println("processing file:" + op.getValue("trainFile"));
				inputData();
				if (op.isNull("values"))
					op.addPair("values", getValuesExpression("\",\""), false);
				else
					op.addPair("values", getValuesExpression("\",\""), true);
				d.setDataFeature(r.dataFeatureExtract(d.getOptions(), d.getDataStructure()));
				switch (d.getOptions().getValue("object")) {
				case "predict":
					if (d.getOptions().getValue("rules").matches("(T|t|TURE|true)")) {
						decisionTree(true);
					} else {
						String method = null;
						if (!d.getOptions().isNull("method")) {
							System.out.println("manually choose a method:" + d.getOptions().getValue("method"));
							method = d.getOptions().getValue("method");
							while (!method.matches("(MLP|GLMNET|randomForest)")) {
								System.out.println("no such method, you should choose from: MLP, GLMNET, randomForest");
								method = s.nextLine();
							}
						} else {
							method = r.chooseAlgorithm(d)[0];
							System.out.println("******************************************choose algorithm:" + method);
							//System.out.println("featur:"+d.dataFeatureToString());
						}
					}
					break;
				case "associate":
					associateAnalysis();
					break;
				case "cluster":
					cluster();
					break;
				default:
					System.err.println("undefined object");
					break;
				}
				temp = reader.readLine();
				cleanUp();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cleanUp() {
		// d.getOptions().display();
		Options op = d.getOptions();
		if (!op.isNull("values")) {
			// System.out.println(op.getValue("values"));
			op.remove("values");
			// System.out.println(op.getValue("values"));
		}
		op.addPair("header", "T", true);
		op.addPair("sep", ",", true);
		op.addPair("na.strings", "NA", true);
		op.addPair("sequential", "F", true);
		op.addPair("discretize", "T", true);
		op.addPair("rules", "F", true);
		op.addPair("ratio", "0.8", true);
		op.addPair("verbose", "F", true);
		// d.getOptions().display();
	}

	public void run() {
		displayFile("orderInputInstruction.txt");
		readOptions();
		inputData();
		Options op = d.getOptions();
		if (op.isNull("values"))
			op.addPair("values", getValuesExpression("\",\""), false);
		else
			op.addPair("values", getValuesExpression("\",\""), true);
		System.out.println("final options");
		op.display();
		format();
		switch (d.getOptions().getValue("object")) {
		case "predict":
			if(!op.isNull("sequential")&&op.getValue("sequential").matches("(t|T|true|TRUE)")){
				timeSeries();
			}else if(!op.isNull("rules")&&op.getValue("rules").matches("(t|T|true|TRUE)")){
				for(int i=0;i<100;i++)
					decisionTree(true);
			}else if (!d.getOptions().isNull("method")) {
				String method = null;
				System.out.println("manually choose a method:" + d.getOptions().getValue("method"));
				method = d.getOptions().getValue("method");
				while (!method.matches("(MLP|GLMNET|randomForest)")) {
					System.out.println("no such method, you should choose from: MLP, GLMNET, randomForest");
					method = s.nextLine();
				}
				DecimalFormat df = new DecimalFormat("#.###");
				double precise = 0;
				String record = "";
				switch (method) {
				case "MLP":
					System.out.println("process MLP:");
					precise = MLP(true,false);
					break;
				case "GLMNET":
					System.out.println("process GLMNET:");
					precise = GLMNET(true,false);
					break;
				case "randomForest":
					System.out.println("process randomForest:");
					precise = randomForest(true,false);
					break;
				default:
					System.out.println("no method selected");
					break;
				}
				System.out.println("Input Y(y/YES/yes) to confirm the result，input other thing(or directly press ENTER) to end the program, no record will be saved.");
				if (s.nextLine().matches("(Y|y|YES|yes)")) {
					record = d.getOptions().getValue("trainFile") + "," + d.dataFeatureToString() + "," + method + "," + df.format(precise) + "\n";
					writeRecord(record);
					cleanUp();
				}
			} else {
				d.setDataFeature(r.dataFeatureExtract(d.getOptions(), d.getDataStructure()));
				DecimalFormat df=new DecimalFormat("#.###");
				String[] method = r.chooseAlgorithm(d);
				ArrayList<String> candicateAlgorithm = new ArrayList<>();
				System.out.println("algorithm rank:");
				for (String string : method) {
					System.out.print(string + "   ");
					candicateAlgorithm.add(string);
				}
				System.out.println();
				boolean oneByOne=true;
				boolean display=true;
				boolean output=true;
				int num = 0;
				while (true) {
					double precise = 0;
					String record = "";
					if(oneByOne){
						long startTime=System.currentTimeMillis();
						switch (candicateAlgorithm.get(num)) {
						case "MLP":
							System.out.println("process MLP:");
							precise=MLP(display,false);
							System.out.println("finished.");
							break;
						case "GLMNET":
							System.out.println("process GLMNET:");
							precise=GLMNET(display,false);
							System.out.println("finished.");
							break;
						case "randomForest":
							System.out.println("process randomForest:");
							precise=randomForest(display,false);
							System.out.println("finished.");
							break;
						default:
							System.out.println("no method selected");
							break;
						}
						double time=(System.currentTimeMillis()-startTime)/(double)1000;
						System.out.println("algorithm "+candicateAlgorithm.get(num)+"=>  precise="+df.format(precise)+"   run time="+time+"s.");
					}else{
						Map<String, Double> precises = new HashMap<>();
						Map<String, Double> times = new HashMap<>();
						for (String string : candicateAlgorithm) {
							long startTime=System.currentTimeMillis();
							switch (string) {
							case "MLP":
								System.out.println("process MLP:");
								precise=MLP(display,false);
								System.out.println("finished.");
								break;
							case "GLMNET":
								System.out.println("process GLMNET:");
								precise=GLMNET(display,false);
								System.out.println("finished.");
								break;
							case "randomForest":
								System.out.println("process randomForest:");
								precise=randomForest(display,false);
								System.out.println("finished.");
								break;
							default:
								System.out.println("no method selected");
								break;
							}
							double time=(System.currentTimeMillis()-startTime)/(double)1000;
							//System.out.println("run time："+time+"s.");
							precises.put(string, precise);
							times.put(string, time);
						}
						System.out.println("algorithm situation:");
						for (String string : candicateAlgorithm) {
							System.out.println(string+" => precise="+ df.format(precises.get(string))+"   time="+times.get(string));
						}
					}
					boolean end=false;
					displayFile("algorithmChoose.txt");
					System.out.println("\n Configurations:");
					System.out.print("\tcandicate algorithms =>");
					for (String string : candicateAlgorithm) {
						System.out.print(string + "  ");
					}System.out.println();
					System.out.print("\ttype: ");
					if(oneByOne) System.out.print("one by one");
					else System.out.print("all at a time");
					System.out.println();
					System.out.println("\tdisplay:"+display);
					System.out.println("\toutput:"+output);
					if(oneByOne)System.out.println("\tnext algorithm to run:" + candicateAlgorithm.get((num + 1) % candicateAlgorithm.size()));
					do {
						String order=s.nextLine();
						if(order.isEmpty()) {
							if(candicateAlgorithm.size()==0) {
								System.out.println("Can't continue when list size is zero.");
								continue;
							}
							break;
						}
						if(order.equals("rank")){
							System.out.println("algorithm rank:");
							for (String string : method) {
								System.out.print(string + "   ");
							}
							System.out.println();
							continue;
						}
						if(order.equals("data")){
							r.getT().setDisplay(true);
							r.getRe().eval("str(data)");
							r.getT().setDisplay(false);
							d.getDataStructure().display();
							continue;
						}
						if(order.equals("help")){
							displayFile("algorithmChoose.txt");
							continue;
						}
						if(order.equals("example")){
							displayFile("algorithmChooseExample.txt");
							continue;
						}
						Options options = x.getOptions(order);
						while (!options.isNull("confirm") && !options.isNull("stop")) {
							System.out.println("attribute confirm and stop shouldn't exist at the same time.\nInput your order again.");
							options = x.getOptions(s.nextLine());
						}
						if(!options.isNull("type")){
							if(options.getValue("type").matches("(T|t|true|TRUE)")) oneByOne=true;
							else if(options.getValue("type").matches("(F|FALSE|f|false)")) oneByOne=false;
							else System.out.println("type can only be T or F.");
						}
						if(!options.isNull("display")){
							if(options.getValue("display").matches("(T|t|true|TRUE)")) display=true;
							else if(options.getValue("display").matches("(F|FALSE|f|false)")) display=false;
							else System.out.println("display can only be T or F.");
						}
						if(!options.isNull("output")){
							if(options.getValue("output").matches("(T|t|true|TRUE)")) output=true;
							else if(options.getValue("output").matches("(F|FALSE|f|false)")) output=false;
							else System.out.println("output can only be T or F.");
						}
						if (!options.isNull("remove")) {
							String[] remove = options.getValue("remove").split(",");
							for (String rm : remove) {
								if (candicateAlgorithm.indexOf(rm) != -1 && candicateAlgorithm.indexOf(rm) <= num)
									num--;
								if (candicateAlgorithm.remove(rm))
									;
								else
									System.out.println(rm + " doesn't exist");
							}
						}
						if (!options.isNull("add")) {
							String[] add = options.getValue("add").split(",");
							for (String rm : add) {
								if (candicateAlgorithm.indexOf(rm) != -1)
									System.out.println(rm + " already exist");
								else
									candicateAlgorithm.add(rm);
							}
						}
						if (!options.isNull("confirm")) {
							String result = options.getValue("confirm");
							if (result.equals("null")) {
								if(candicateAlgorithm.size()==0){
									System.out.println("Can't confirm current algorithm when list size is zero.");
									continue;
								}
								if(!oneByOne){
									System.out.println("Can't confirm current algorithm when type is all at a time.");
									continue;
								}
								result = candicateAlgorithm.get(num);
								System.out.println("confirm algorithm:" + result);
								record = d.getOptions().getValue("trainFile") + "," + d.dataFeatureToString() + "," + result + "\n";
								System.out.print("write record=>"+record);
								writeRecord(record);
								System.out.println("write succeed.");
								end=true;
								break;
							}else if (result.matches("(MLP|GLMNET|randomForest)")) {
								System.out.println("confirm algorithm:" + result);
								switch (result) {
								case "MLP":
									System.out.println("process MLP:");
									precise=MLP(display,output);
									System.out.println("finished.");
									break;
								case "GLMNET":
									System.out.println("process GLMNET:");
									precise=GLMNET(display,output);
									System.out.println("finished.");
									break;
								case "randomForest":
									System.out.println("process randomForest:");
									precise=randomForest(display,output);
									System.out.println("finished.");
									break;
								default:
									System.out.println("no method selected");
									System.out.println("finished.");
									break;
								}
								record = d.getOptions().getValue("trainFile") + "," + d.dataFeatureToString() + "," + result + "\n";
								System.out.print("write record=>"+record);
								writeRecord(record);
								System.out.println("write succeed.");
								end=true;
								break;
							} else {
								System.out.println("no such algorithm:" + result);
							}
						}
						if (!options.isNull("stop")) {
							String result = options.getValue("stop");
							if (result.equals("null")){
//								if(candicateAlgorithm.size()==0){
//									System.out.println("Can't confirm current algorithm when list size is zero.");
//									continue;
//								}
								end=true;
								break;
							}else if (result.matches("(MLP|GLMNET|randomForest)")) {
								System.out.println("confirm algorithm:" + result);
								switch (result) {
								case "MLP":
									System.out.println("process MLP:");
									precise=MLP(display,output);
									System.out.println("finished.");
									break;
								case "GLMNET":
									System.out.println("process GLMNET:");
									precise=GLMNET(display,output);
									System.out.println("finished.");
									break;
								case "randomForest":
									System.out.println("process randomForest:");
									precise=randomForest(display,output);
									System.out.println("finished.");
									break;
								default:
									System.out.println("no method selected");
									break;
								}
								end=true;
								break;
							} else {
								System.out.println("no such algorithm:" + result);
							}
						}
						System.out.println("\n Configurations:");
						System.out.print("\tcandicate algorithms =>");
						for (String string : candicateAlgorithm) {
							System.out.print(string + "  ");
						}System.out.println();
						System.out.print("\ttype: ");
						if(oneByOne) System.out.print("one by one");
						else System.out.print("all at a time");
						System.out.println();
						System.out.println("\tdisplay:"+display);
						System.out.println("\toutput:"+output);
						if(oneByOne)System.out.println("\tnext algorithm to run:" + candicateAlgorithm.get((num + 1) % candicateAlgorithm.size()));
						System.out.println("press ENTER if you finish editing the candicate list, and the algorithm will continue.");
					} while (true);
					if(end) break;
					cleanUp();
					num = (num + 1) % candicateAlgorithm.size();
				}
			}
			break;
		case "associate":
			associateAnalysis();
			break;
		case "cluster":
			cluster();
			break;
		//case "rules":
		//	decisionTree();
		//	break;
		default:
			System.err.println("undefined object");
			break;
		}
	}
	public void timeSeries(){
		r.getRe().eval("library(xts)");
		if(r.getRe().eval("length(apply(data,2,class)[(apply(data,2,class) %in% c(\"integer\",\"numeric\"))==FALSE])>0").asBool().isTRUE()){
			System.out.println("timeSeries must be integer or numeric.");
			return;
		}
		r.getRe().eval("data<-as.matrix(data)");
		if(r.getRe().eval("dim(data)[2]==1").asBool().isFALSE()){
			//System.out.println(r.getRe().eval("dim(data)"));
			System.out.println("the data has more than 1 column, the program will change it into 1 column.");
			r.getRe().eval("data<-t(data)");
			r.getRe().eval("dim(data)<-c(dim(data)[1]*dim(data)[2],1)");
		}
//		displayFile("timeSeqCreate.txt");
//		System.out.println("data's length:"+r.getRe().eval("length(data)").asInt());
//		String temp=s.nextLine();
//		while (true) {
//			if(temp.equals("")) break;
//			if(temp.equals("detail")){
//				displayFile("timeSeqCreateDetail.txt");
//				temp=s.nextLine();
//				continue;
//			}
//			r.getRe().eval("timeSeq<-NULL");
//			r.getRe().eval("timeSeq<-"+temp);
//			if(r.getRe().eval("is.null(timeSeq)").asBool().isTRUE()){
//				System.out.println("time sequence creation fail.");
//				temp=s.nextLine();
//				continue;
//			}
//			if(r.getRe().eval("length(timeSeq)!=length(data)").asBool().isTRUE()){
//				System.out.println("the length of the time sequence is not equal to the data's length.");
//				temp=s.nextLine();
//			}else {
//				r.getRe().eval("data<-xts(data,timeSeq)");
//				r.getRe().eval("str(data)");
//				break;
//			}
//		}
		Options op = new Options();
		op.addPair("ahead", "10");
		op.addPair("outputPDF", "");
		op.alterAll(d.getOptions());
		//op.display();
		//double precise = r.RandomForestBuild(op, d.getDataStructure());
		Options conf=r.timeSeriesBuild(op);
		conf.addPair("ahead", "10");
		conf.addPair("outputPDF", "");
		conf.alterifExist(op);
		String temp="";
		do{
			r.timeSeriesOutput(conf);
			System.out.println("confirm or change p d q");
			temp=s.nextLine();
			if(temp.equals("")) break;
			else {
					conf.alterAll(x.getOptions(temp));
			}
		}while(true);
		System.out.println("result confirmed");
		//r.RandomForestDisplay(op);
		//r.RandomForestOutput(op, display);
		//return precise;
	}
	public void cluster() {
		r.getRe().eval("library(e1071)");
		r.getRe().eval("library(Rcpp)");
		r.getRe().eval("library(RSNNS)");
		Options op = new Options();
		op.addPair("outputSep", ",");
		op.addPair("outputTXT", "");
		op.addPair("outputPDF", "");
		op.addPair("verbose", "F");
		op.alterifExist(d.getOptions());
		int adjust = -1;
		int cluster = 0;
		String cluster_string = "";
		if (d.getOptions().isNull("cluster")) {
			System.out.println("please input the initial number of the clusters( you can change it later). ");
			cluster_string = s.nextLine();
		} else
			cluster_string = d.getOptions().getValue("cluster");
		while (true) {
			try {
				cluster = Integer.valueOf(cluster_string);
				break;
			} catch (NumberFormatException e) {
				System.out.println("you must input an integer!");
				cluster_string = s.nextLine();
			}
		}
		op.addPair("cluster", String.valueOf(cluster));
		do {
			System.out.println("cluster num:" + op.getValue("cluster"));
			r.cluster(op, false);
			displayFile("clusterChange.txt");
			String adjustOption = s.nextLine();
			while (adjustOption.equals("example")) {
				displayFile("clusterChangeExample.txt");
				adjustOption=s.nextLine();
			}
			if (adjustOption.equals("")) break;
			try {
				adjust = Integer.parseInt(adjustOption);
				cluster += adjust;
				op.addPair("cluster", Integer.toString(cluster), true);
			} catch (NumberFormatException e) {
				System.out.println("you must input an integer!");
				adjustOption = s.nextLine();
			}
		} while (adjust != 0);
		System.out.println("Final:");
		System.out.println("cluster number:" + cluster);
		r.cluster(op, true);
	}

	public double randomForest() {
		return randomForest(false,false);
	}

	public double randomForest(boolean display,boolean output) {
		Options op = new Options();
		// op.addPair("target", "");
		// op.addPair("values", getValuesExpression("\",\""));
		op.addPair("expression", d.getOptions().getValue("target") + "~" + getValuesExpression("+"));
		// System.out.println(getValuesExpression("+"));
		Type type = d.getDataStructure().getStructMap().get(d.getOptions().getValue("target")).getT();
		op.addPair("target.type", type.toString());
		op.alterAll(d.getOptions());
		double precise = r.RandomForestBuild(op, d.getDataStructure());
		// System.out.println(precise);
		if (display) {
			r.RandomForestDisplay(op);
		}
		if(output){
			r.RandomForestOutput(op, display);
		}
		return precise;
	}

	public double GLMNET() {
		return GLMNET(false,false);
	}

	public double GLMNET(boolean display,boolean output) {
		Options op = new Options();
		op.addPair("target", "");
		op.addPair("values", getValuesExpression("\",\""));
		Type type = d.getDataStructure().getStructMap().get(d.getOptions().getValue("target")).getT();
		op.addPair("target.type", type.toString());
		if (type == Type.FACTOR || type == Type.ORD_FACTOR) {
			int classNum = r.getRe().eval("length(unique(data[,\"" + d.getOptions().getValue("target") + "\"]))").asInt();
			if (classNum == 1)
				System.err.println("no need to analysis, only one values");
			else if (classNum == 2) {
				op.addPair("family", "binomial");
				op.addPair("type.", "measure");
				op.addPair("predict.type", "class");
				op.addPair("measure", "class");
			} else {
				op.addPair("family", "multinomial");
				op.addPair("type.", "multinomial");
				op.addPair("predict.type", "class");
				op.addPair("measure", "grouped");
			}
		} else if (type == Type.NUM || type == Type.INT) {
			op.addPair("family", "gaussian");
			op.addPair("type.", "measure");
			op.addPair("predict.type", "link");
			op.addPair("measure", "deviance");
		}
		op.alterAll(d.getOptions());
		//op.display();
		double precise = r.GLMNETBuild(op);
		if (display && precise != -Double.MAX_VALUE) {
			r.GLMNETDisplay(op);
		}
		if(output&& precise != -Double.MAX_VALUE){
			r.GLMNETOutput(op, display);
		}
		return precise;
		// return r.GLMNET(op, display);
	}

	public double MLP() {
		return MLP(false,false);
	}

	public double MLP(boolean display,boolean output) {
		boolean verbose = d.getOptions().getValue("verbose").equals("T") ? true : false;
		Options op = new Options();
		Map<String, Param> attrs = new HashMap<>();
		op.addPair("target", "");
		op.addPair("maxit", "200");
		op.addPair("values", getValuesExpression("\",\""));
		op.addPair("encode.method", "WTA");
		op.addPair("strategy", "MLP");
		op.addPair("cut", "3");
		op.addPair("size", "");// 隐层数
		String type = d.getDataStructure().getStructMap().get(d.getOptions().getValue("target")).getType();
		op.addPair("target.type", type);
		if (type.equals("Factor"))
			op.addPair("linOut", "F");
		else
			op.addPair("linOut", "T");
		op.alterAll(d.getOptions());
		if (op.isNull("size"))
			attrs.put("size", new Param("size", Type.INT, 1, 1, d.getDataStructure().getAttributes().size() * 2, Integer.parseInt(op.getValue("cut"))));
		if (!attrs.isEmpty())
			attrs = optimalAttrs(op, attrs, false);
		if (!verbose && display)
			System.out.println("set  size=" + op.getValue("size"));
		double precise = r.MLPBuild(op, d.getDataStructure());
		if (display) {
			r.MLPDisplay(op);
		}
		if(output){
			r.MLPOutput(op, display);
		}
		return precise;
		// return r.MLP(op, display);
	}

	public void associateAnalysis() {
		// d.getDataStructure().display();
		// System.out.println("to associate analysis, int and num attributes should be discretized.");
		// System.out.println("input the attributes' names and their number of categories, like: V1=\"3\" , V5=\"5\"");
		// System.out.println("target attribute is strongly to discretized manually, or it will be discretized into three categories ");
		// displayFile("离散化操作说明.txt");//后期制作
		// Map<String, Options> optionMap;
		// do {
		// String discretizOption = s.nextLine();
		// optionMap = x.getDiscretizeOptions(discretizOption, "=", ",");
		// System.out.println(discretize(optionMap, false));
		// System.out
		// .println("if this result is fine ,input YES(yes/Y/y),or input NO(no/N/n) to redo the discretize");
		// } while (s.nextLine().matches("(NO|N|no|n)"));
		// System.out.println(discretize(optionMap, true));
		// d.getDataStructure().display();
		// r.getRe().eval("library(arulesViz)");
		r.getRe().eval("library(arules)");
		while (d.getDataStructure().getIntAttr().size() > 0 || d.getDataStructure().getNumAttr().size() > 0) {
			System.out.println("have continours value, must discretize all.");
			// d.getDataStructure().display();
			// displayFile("discretizeInstruction.txt");
			format(true);
		}
		Options op = new Options();
		op.addPair("support", "0.3");
		op.addPair("confidence", "0.8");
		op.addPair("maxlen", "10");
		// op.addPair("visible","F");
		op.addPair("outputSep", ",");
		op.addPair("outputTXT", "");
		op.addPair("outputPDF", "");
		op.addPair("format", "single");
		op.addPair("verbose", "F");
		op.alterifExist(d.getOptions());
		int digit = op.isNull("precise") ? 2 : Integer.parseInt(op.getValue("precise"));
		double precise = Math.pow(0.1, digit);
		int adjust = -1;
		double support = Double.parseDouble(op.getValue("support"));
		do {
			System.out.println("current support valve:" + Double.toString(support));
			r.associateAnalysis(op, false);
			displayFile("associateChange.txt");
			String adjustOption = s.nextLine();
			if (adjustOption.equals(""))
				break;
			try {
				adjust = Integer.parseInt(adjustOption);
				support += adjust * precise;
				op.addPair("support", Double.toString(support), true);
			} catch (Exception e) {

			}
		} while (adjust != 0);
		System.out.println("Final:");
		System.out.println("support valve:" + support);
		r.associateAnalysis(op, true);
	}

	public void format() {
		format(false);
	}

	public void format(boolean all) {
		r.getRe().eval("library(arules)");
		Map<String, Options> userOp;
		Map<String, Options> defaultOp = new HashMap<>();
		Map<String, Options> executeOp = new HashMap<>();
		// String discretizeResult = "";
		// defaultOp.clear();
		int defaultSize;
		if (d.getOptions().isNull("target"))
			defaultSize = 0;
		else{
			if(d.getDataStructure().getStructMap().containsKey(d.getOptions().getValue("target")))
				defaultSize = 2 * d.getDataStructure().getStructMap().get(d.getOptions().getValue("target")).getClasses().size();
			else {
				System.out.println("target attribute doesn't exist.");
				defaultSize = 0;
			}
		}
		defaultSize = defaultSize == 0 ? 3 : defaultSize;
		for (String s : d.getDataStructure().getNumAttr()) {
			Options defaultOptions = new Options();
			// System.out.println("add categories num"+Integer.toString(2*d.getDataStructure().getStructMap().get(d.getOptions().getValue("target")).getClasses().size()));
			defaultOptions.addPair("categories", Integer.toString(defaultSize));
			defaultOptions.addPair("method", "cluster");
			defaultOp.put(s, defaultOptions);
		}
		for (String s : d.getDataStructure().getIntAttr()) {
			Options defaultOptions = new Options();
			// System.out.println("add categories num"+Integer.toString(2*d.getDataStructure().getStructMap().get(d.getOptions().getValue("target")).getClasses().size()));
			defaultOptions.addPair("categories", Integer.toString(defaultSize));
			defaultOptions.addPair("method", "cluster");
			defaultOp.put(s, defaultOptions);
		}
		d.getDataStructure().display();
		//displayFile("format.txt");
		if (all) displayFile("formatTrue.txt");
			//System.out.println("press ENTER, and program will discretize the attributs automatically");
		else displayFile("formatFalse.txt");
			//System.out.println("press ENTER means nothing need to be done.");
		String discretizOption = s.nextLine();
		if (discretizOption.equals("")) {
			if (all) {
				d.getDataStructure().alterAttrTypeNClass(r.format(defaultOp, !d.getOptions().isNull("testFile")));
				formatOptions = defaultOp;
			}
		} else {
			// String choose = "";
			while (true) {
				executeOp.clear();
				userOp = x.getDiscretizeOptions(discretizOption, "=", ",");
				if (userOp.isEmpty()) {
					if (all)
						executeOp = defaultOp;
					break;
				}
				for (String s : userOp.keySet()) {
					if (userOp.get(s).getAttributes().contains("format"))
						executeOp.put(s, userOp.get(s));
					else if (defaultOp.containsKey(s)) {
						executeOp.put(s, defaultOp.get(s).alterAll(userOp.get(s)));
					} else
						System.err.println("MainFrame discretiz():no such default option:"+s);
				}
				r.format(executeOp).display();
				if (all) displayFile("formatTrue.txt");
				else displayFile("formatFalse.txt");
				System.out.println("Input NO(no/N/n) to redo the last format.");
				if (all)
					System.out.println("press ENTER, and program will discretize the attributs automatically");
				else
					System.out.println("press ENTER means nothing need to be done.");
				// System.out.println("if this result is fine ,input YES(yes/Y/y),or input NO(no/N/n) to redo the discretize");
				discretizOption = s.nextLine();
				if (discretizOption.matches("(NO|N|no|n)"))
					continue;
				d.getDataStructure().alterAttrTypeNClass(r.format(executeOp, !d.getOptions().isNull("testFile")));
				for (String string : executeOp.keySet()) {
					if (formatOptions.containsKey(string))
						formatOptions.get(string).alterAll(executeOp.get(string));
					else
						formatOptions.put(string, executeOp.get(string));
				}
				if (discretizOption.equals(""))
					break;
			}
		}
	}

	public double decisionTree() {
		return decisionTree(false);
	}

	public double decisionTree(boolean display) {
		boolean verbose = d.getOptions().getValue("verbose").equals("T") ? true : false;
		r.getRe().eval("library(arules)");
		r.getRe().eval("library(rpart)");
		r.getRe().eval("library(rpart.plot)");
		r.getRe().eval("library(rattle)");
		Map<String, Param> attrs = new HashMap<>();
		int minsplit = d.getDataStructure().getObsNum() / 10;
		int minbucket = 0;
		sepTrainNTest(false);
		Options op = getDecisionTreeOptions(display);
		if (op.isNull("minsplit"))
			attrs.put("minsplit", new Param("minsplit", Type.INT, minsplit, 0, d.getDataStructure().getObsNum(), Integer.parseInt(op.getValue("cut"))));
		if (op.isNull("minbucket"))
			attrs.put("minbucket", new Param("minbucket", Type.INT, 0, 0, d.getDataStructure().getObsNum() / 3, Integer.parseInt(op.getValue("cut"))));
		if (op.isNull("cp"))
			attrs.put("cp", new Param("cp", Type.NUM, 0.05, 0, 1, Integer.parseInt(op.getValue("cut")), Integer.parseInt(op.getValue("precise"))));
		if (!attrs.isEmpty())
			attrs = optimalAttrs(op, attrs, false);
		//if (!verbose && display)
		//	System.out.println("set  minsplit=" + op.getValue("minsplit") + "   minbucket=" + op.getValue("minbucket") + "   cp=" + op.getValue("cp"));
		double precise = r.decisionTreeBuild(op);
		if (display) {
			//op.addPair("outputPDF", "C:/Users/弘二/Desktop/rules.pdf");
			//r.decisionTreedisplay(op);
			//System.out.print(String.format("%-50s", "optimized precise:"+precise));
			//r.decisionTreeOutput(op, display);
			op.addPair("minsplit", "20",true);
			op.addPair("minbucket", "7",true);
			op.addPair("cp", "0.01",true);
			//op.addPair("outputPDF", "C:/Users/弘二/Desktop/rules2.pdf",true);
			//System.out.println(String.format("%-50s", "default precise:"+r.decisionTreeBuild(op)));
			//System.out.println("default precise:"+r.decisionTreeBuild(op));
			System.out.println(precise+","+r.decisionTreeBuild(op));
			//r.decisionTreeOutput(op, display);
		}
		return precise;
	}

	public void sepTrainNTest(boolean display) {
		boolean verbose = d.getOptions().getValue("verbose").equals("T") ? true : false;
		Options defaultOption = new Options();
		defaultOption.addPair("ratio", d.getOptions().getValue("ratio"));
		// defaultOption.addPair("seed",
		// Integer.toString((int)System.nanoTime()));
		// defaultOption.addPair("seed", "1234");
		if (!verbose && display)
			System.out.println("set sep ration=" + d.getOptions().getValue("ratio"));
		r.sepTrainNTest(defaultOption);
	}

	public Map<String, Param> optimalAttrs(Options op, Map<String, Param> attrs, boolean display) {
		Random random = new Random(System.currentTimeMillis());
		boolean verbose = d.getOptions().getValue("verbose").equals("T") ? true : false;
		Map<String, Param> optimal = new HashMap<>();
		for (String attr : attrs.keySet()) {
			//if (!verbose && display)
			//	System.err.println(attrs.get(attr).getName() + "  optimaling... type:" + attrs.get(attr).getType());
			ArrayList<Param> bounds = new ArrayList<>();
			bounds.add(attrs.get(attr));
			// ArrayList<Integer> paramSet = new ArrayList<>();
			double highestPrecise = -Double.MAX_VALUE;
			boolean flag;
			if (attrs.get(attr).getType() == Type.INT) {
				ArrayList<Integer> highestPoints = new ArrayList<>();
				int cut = op.isNull("cut") ? 10 : Integer.parseInt(op.getValue("cut"));
				while (bounds.size() > 0) {
					if(highestPoints.isEmpty()) highestPrecise = -Double.MAX_VALUE;
					ArrayList<Param> temp = new ArrayList<>();
					for (Param param : bounds) {
						flag = false;
						op.putIfExist(param.getName(), Integer.toString((int) param.getInitial()));
						double precise;
						switch (op.getValue("strategy")) {
						case "DTree":
							precise = r.decisionTreeBuild(op);
							break;
						case "MLP":
							precise = r.MLPBuild(op, d.getDataStructure(), false);
							break;
						default:
							System.err.println("no strategy:" + op.getValue("strategy"));
							precise = 0;
							break;
						}
						// System.out.println(attr+"="+param.getInitial()+"    precise="+precise+"    highestPrecise="+highestPrecise);
						// System.out.println("lowerBounds = "+param.getLowerBound()+"    upperBounds = "+param.getUpperBound());
						// System.out.println("---------------------------------------------------------------------------");
						if (precise > highestPrecise) {
							temp.clear();
							//if(highestPoints.size()>0){
							//	System.out.println("highestPrice="+highestPrecise+"      now:"+precise);
							//}
							highestPoints.clear();
							highestPrecise = precise;
							flag = true;
						} else if (precise == highestPrecise) {
							flag = true;
						}
						if (flag) {
							int distance = (int) (param.getUpperBound() - param.getLowerBound());
							if (distance == 0) {
								highestPoints.add((int) param.getInitial());
							} else if (distance < cut) {
								// System.out.print("cut ("+param.getLowerBound()+","+param.getUpperBound()+") to=>");
								for (int i = 1; i < cut && param.getLowerBound() + i <= param.getUpperBound(); i++) {
									double lowerBound = param.getLowerBound() + i;
									double upperBound = param.getLowerBound() + i;
									double initial = param.getLowerBound() + i;
									// System.out.print("("+lowerBound+","+upperBound+",initial="+initial+")");
									temp.add(new Param(param.getName(), initial, lowerBound, upperBound));
								}
								// System.out.println();
							} else {
								// System.out.print("cut ("+param.getLowerBound()+","+param.getUpperBound()+") to=>");
								double step = distance / (double) (2 * cut);
								for (int i = 0; i < cut; i++) {
									double lowerBound = Math.ceil(param.getLowerBound() + 2 * i * step);
									double upperBound = Math.ceil(param.getLowerBound() + (2 * i + 2) * step);
									// double ran=(random.nextInt(20))/10.0;
									// System.out.println("random="+ran);
									double initial = Math.ceil(param.getLowerBound() + (random.nextInt(20) / 10.0 + 2 * i) * step);
									// System.out.print("("+lowerBound+","+upperBound+",initial="+initial+")");
									temp.add(new Param(param.getName(), initial, lowerBound, upperBound));
								}
								// System.out.println();
							}
						}
					}
					bounds = temp;
				}
				if (highestPoints.size() > 0) {
					Collections.sort(highestPoints);
					if (!verbose && display) {
						System.out.println(attr + "'s  highestPoints:");
						for (int p : highestPoints)
							System.out.print(p + "    ");
						System.out.println();
						System.out.println("set " + attr + ":" + highestPoints.get(highestPoints.size() - 1));
					}
					optimal.put(attr, new Param(attr, Type.INT, highestPoints.get(highestPoints.size() - 1)));
					op.addPair(attr, Integer.toString(highestPoints.get(highestPoints.size() - 1)), true);
				} else
					System.err.println(attr + ":no optimal value selected.");
			} else if (attrs.get(attr).getType() == Type.NUM) {
				// 实数型数怎么判断
				ArrayList<Double> highestPoints = new ArrayList<>();
				int digit = op.isNull("precise") ? 4 : Integer.parseInt(op.getValue("precise"));
				int cut = op.isNull("cut") ? 10 : Integer.parseInt(op.getValue("cut"));
				double min = Math.pow(0.1, digit);
				while (bounds.size() > 0) {
					if(highestPoints.isEmpty()) highestPrecise = -Double.MAX_VALUE;
					ArrayList<Param> temp = new ArrayList<>();
					for (Param param : bounds) {
						flag = false;
						op.putIfExist(param.getName(), Double.toString(param.getInitial()));
						double precise;
						switch (op.getValue("strategy")) {
						case "DTree":
							precise = r.decisionTreeBuild(op);
							break;
						case "MLP":
							precise = r.MLPBuild(op, d.getDataStructure(), false);
							break;
						default:
							System.err.println("no strategy:" + op.getValue("strategy"));
							precise = 0;
							break;
						}
						// System.out.println("---------------------------------------------------------------------------");
						// System.out.println("para="+param.getInitial()+"    precise="+precise+"    highestPrecise="+highestPrecise);
						// System.out.println("lowerBounds = "+param.getLowerBound()+"    upperBounds = "+param.getUpperBound());
						if (precise > highestPrecise) {
							temp.clear();
							highestPoints.clear();
							highestPrecise = precise;
							flag = true;
						} else if (precise == highestPrecise) {
							flag = true;
						}
						if (flag) {
							DecimalFormat dformat = new DecimalFormat("#.####");
							double distance = param.getUpperBound() - param.getLowerBound();
							if (distance <= min) {
								highestPoints.add(param.getInitial());
							} else if (distance < min * cut) {
								// System.out.print("cut ("+param.getLowerBound()+","+param.getUpperBound()+") to=>");
								for (int i = 1; i < cut && param.getLowerBound() + i * min <= param.getUpperBound(); i++) {
									double lowerBound = Double.parseDouble(dformat.format(param.getLowerBound() + i * min));
									double upperBound = Double.parseDouble(dformat.format(param.getLowerBound() + i * min));
									double initial = Double.parseDouble(dformat.format(param.getLowerBound() + i * min));
									// System.out.print("("+lowerBound+","+upperBound+",initial="+initial+")");
									temp.add(new Param(param.getName(), initial, lowerBound, upperBound));
								}
								// System.out.println();
							} else {
								// System.out.print("cut ("+param.getLowerBound()+","+param.getUpperBound()+") to=>");
								double step = distance / (double) (2 * cut);
								for (int i = 0; i < cut; i++) {
									double lowerBound = Double.parseDouble(dformat.format(param.getLowerBound() + 2 * i * step));
									double upperBound = Double.parseDouble(dformat.format(param.getLowerBound() + (2 * i + 2) * step));
									double initial = Double.parseDouble(dformat.format(param.getLowerBound() + (random.nextInt(20) / 10.0 + 2 * i) * step));
									// System.out.print("("+lowerBound+","+upperBound+",initial="+initial+")");
									temp.add(new Param(param.getName(), initial, lowerBound, upperBound));
								}
								// System.out.println();
							}
						}
					}
					bounds = temp;
				}
				if (highestPoints.size() > 0) {
					Collections.sort(highestPoints);
					if (!verbose && display) {
						System.out.println(attr + "'s  highestPoints:");
						for (double p : highestPoints) {
							System.out.print(p + "    ");
						}
						System.out.println();
						System.out.println("set highest:" + highestPoints.get(highestPoints.size() - 1));
					}
					optimal.put(attr, new Param(attr, Type.NUM, highestPoints.get(highestPoints.size() - 1)));
					op.addPair(attr, Double.toString(highestPoints.get(highestPoints.size() - 1)), true);
				} else
					System.err.println(attr + ":no optimal value selected.");
			}
		}
		return optimal;
	}

	public Options getDecisionTreeOptions(boolean display) {
		boolean verbose = d.getOptions().getValue("verbose").equals("T") ? true : false;
		Options op = new Options();
		Options userOptions = d.getOptions();
		op.addPair("strategy", "DTree");// 给optimalAttr指明现在优化的是DTree的参数
		op.addPair("cut", "10");
		op.addPair("precise", "3");
		op.addPair("target", userOptions.getValue("target"));
		switch (d.getDataStructure().getStructMap().get(d.getOptions().getValue("target")).getType()) {
		case "num":
		case "int":
			op.addPair("method", "anova");
			op.addPair("predict.type", "vector");
			break;
		case "Factor":
		default:
			op.addPair("method", "class");
			op.addPair("predict.type", "class");
			break;
		}
		// op.addPair("values", getValuesExpression("\",\""));
		op.addPair("expression", userOptions.getValue("target") + "~" + getValuesExpression("+"));
		op.addPair("maxdepth", "30");
		op.addPair("cp", "");
		op.addPair("minsplit", "");
		op.addPair("target.type", d.getDataStructure().getStructMap().get(d.getOptions().getValue("target")).getType());
		// System.out.println("minsplit:"+Integer.toString(minsplit));
		// op.addPair("testFile", "");
		op.addPair("minbucket", "");
		// op.addPair("outputTXT", "");
		// op.addPair("outputPDF", "");
		op.alterAll(userOptions);
//		if (!verbose && display) {
//			System.out.println("decision tree option:");
//			op.display();
//		}
		return op;
	}

	public String getValuesExpression(String connect) {
		String result = "";
		if (d.getOptions().isNull("values")) {
			ArrayList<String> attrs = d.getDataStructure().getAttributes();
			for (String string : attrs) {
				// System.out.print(string+connect);
				if (string.equals(d.getOptions().getValue("target")))
					;
				else
					result += string + connect;
			}
			// System.out.println();
			result = result.substring(0, result.length() - connect.length());
		} else {
			String[] values = d.getOptions().getValue("values").split("\",\"");
			for (int i = 0; i < values.length - 1; i++) {
				result += values[i] + connect;
			}
			result += values[values.length - 1];

		}
		return result;
	}

	// public String chooseDecisionTreeMethod() {
	// String method = "";
	// // Structure temp=d.getDataStructure();
	// switch
	// (d.getDataStructure().getStructMap().get(d.getOptions().getValue("target")).getType())
	// {
	// case "num":
	// case "int":
	// method = "anova";
	// break;
	// case "Factor":
	// default:
	// method = "class";
	// break;
	// }
	// return method;
	// }

	public MainFrame() {
		x = new InformationExtractor();
		r = new ranalysis();
		s = new Scanner(System.in);
		d = new DataFeature();
		formatOptions = new HashMap<>();
		log = "F:\\学习\\编程文件\\JAVA类\\rtest\\log.txt";
		options = "F:\\学习\\编程文件\\JAVA类\\rtest\\options.txt";
		Options op = d.getOptions();
		op.addPair("header", "T");
		op.addPair("sep", ",");
		op.addPair("na.strings", "NA");
		op.addPair("sequential", "F");
		op.addPair("discretize", "T");
		op.addPair("rules", "F");
		op.addPair("ratio", "0.8");
		op.addPair("verbose", "F");
	}

	public void stop() {
		r.stop();
		s.close();
	}

	private void displayFile(String fileName) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(fileName)));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				System.out.println(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public static void main(String[] args) {
		MainFrame m = new MainFrame();
		//m.auto_run();
		m.run();
		// m.creatRecord();
		m.stop();		
	}
}
