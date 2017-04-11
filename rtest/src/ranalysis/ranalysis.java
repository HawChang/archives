package ranalysis;

import informationExtractor.*;
import informationExtractor.attrTypeNClass.Type;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
//import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
//import java.util.Vector;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.swing.text.html.Option;

import org.rosuda.JRI.RFactor;
//import org.rosuda.JRI.RVector;
import org.rosuda.JRI.Rengine;
//import org.rosuda.JRI.REXP;
//
//
//import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
//import com.sun.org.apache.bcel.internal.generic.SWAP;
//import com.sun.org.apache.regexp.internal.RE;
//import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
//import com.sun.swing.internal.plaf.basic.resources.basic;
//import com.sun.xml.internal.fastinfoset.Decoder;
//import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

public class ranalysis {
	private TextConsole t;
	private Rengine re;
	public Rengine getRe() {
		return re;
	}

	public TextConsole getT() {
		return t;
	}

	// public void openOutputFile(){
	// re.eval("");
	// }
	public ranalysis() {
		// TODO Auto-generated constructor stub
		if (!Rengine.versionCheck()) {
			System.err.println("** Version mismatch - Java files don't match library version.");
			System.exit(1);
		}
		// System.out.println("Creating Rengine (with arguments)");
		t = new TextConsole();
		re = new Rengine(null, false, t);
		// System.out.println("Rengine created, waiting for R");
		if (!re.waitForR()) {
			System.out.println("Cannot load R");
			return;
		}
		//System.out.println("succeed...");
	}
	public DataFeature dataFeatureExtract(Options op,Structure str){
		//String result="";
		//System.out.println(op.getValue("trainFile"));
		DecimalFormat df=new DecimalFormat("#.###");
		//离散属性数量、连续属性数量、目标属性类型、目标属性的变化值数、样本数量、缺失值比例、属性的相关性，互信息
		int discretizeNum=str.getFactorAttr().size()+str.getOrdFactorAttr().size();
		int continousNum=str.getIntAttr().size()+str.getNumAttr().size();
		//离散属性比例、目标属性类型、目标属性变化值数、样本数量、缺失值比例、数据集的信息熵、平均互信息熵
		int totalAttrNum=discretizeNum+continousNum;
		double prop_of_symnolic=Double.parseDouble(df.format(discretizeNum/(double)totalAttrNum));
		String targetType=str.getStructMap().get(op.getValue("target")).getType();
		int targetValuesNum=re.eval("length(unique(data[,\""+op.getValue("target")+"\"]))").asInt();
		int obsNum=str.getObsNum();
		double  naRatio=Double.parseDouble(df.format(1-re.eval("nrow(na.omit(data))").asInt()/(double)re.eval("nrow(data)").asInt()));
		re.eval("library(infotheo)");
		re.eval("multinfo<-mutinformation(discretize(data))");
		re.eval("size<-length(names(data))");
		double mutInformation=Double.parseDouble(df.format(re.eval("sum(mutinformation(discretize(data)))").asDouble()));
		double multiInformation=Double.parseDouble(df.format(re.eval("multiinformation(discretize(data))").asDouble()));
		double entropy=Double.parseDouble(df.format(re.eval("sum(apply(discretize(data),2,entropy))").asDouble()));
		return new DataFeature(prop_of_symnolic,totalAttrNum, targetType, targetValuesNum, obsNum, naRatio,entropy,multiInformation,mutInformation);
	}
	public Structure str() {
		return str("data");
	}
	public String[] chooseAlgorithm(DataFeature dataFeature){
//		data<-read.table("C:/Users/Zh/Desktop/data/log.csv",header=T,sep=",")
//		test<-data.frame(symbolicProp=c(0.029),attrNum=c(35),targetType=c("Factor"),targetValueNum=c(2),obsNum=c(351),missingValueProp=c(0),entropy=c(59.627),multiinformation=c(53.77),mutinformation=c(334.812))
//		temp<-rbind(test,data[,2:10])
//		temp[,"targetType"]<-decodeClassLabels(temp[,"targetType"])
//		temp<-scale(temp)
//		d<-dist(temp)
//		dd<-d[1:nrow(data)]
//		algorithmCandicate<-data$algorithm[order(dd)[1:3]]
//		table<-table(algorithmCandicate)
//		dimnames(table)[[1]][order(table,decreasing=T)[1]]
		//re.eval("library(Rcpp)");
		//re.eval("library(RSNNS)");
		re.eval("chooseAlgorithm.record<-read.table(\"C:/Users/弘二/Desktop/data/log.csv\",header=T,sep=\",\")");
		re.eval("chooseAlgorithm.test<-data.frame(symbolicProp=c("+dataFeature.getProp_of_symbolic()+"),attrNum=c("+dataFeature.getTotalAttrNum()+"),targetType=c(\""+dataFeature.getTargetType()+"\"),targetValueNum=c("+dataFeature.getTargetValuesNum()+"),obsNum=c("+dataFeature.getObsNum()+"),missingValueProp=c("+dataFeature.getNaRatio()+"),entropy=c("+dataFeature.getEntropy()+"),multiinformation=c("+dataFeature.getMultiInformation()+"),mutinformation=c("+dataFeature.getMutInformation()+"))");
		System.out.println("test<-data.frame(symbolicProp=c("+dataFeature.getProp_of_symbolic()+"),attrNum=c("+dataFeature.getTotalAttrNum()+"),targetType=c(\""+dataFeature.getTargetType()+"\"),targetValueNum=c("+dataFeature.getTargetValuesNum()+"),obsNum=c("+dataFeature.getObsNum()+"),missingValueProp=c("+dataFeature.getNaRatio()+"),entropy=c("+dataFeature.getEntropy()+"),multiinformation=c("+dataFeature.getMultiInformation()+"),mutinformation=c("+dataFeature.getMutInformation()+"))");
		re.eval("chooseAlgorithm.temp<-rbind(chooseAlgorithm.test,chooseAlgorithm.record[,2:10])");
		re.eval("chooseAlgorithm.temp[,\"targetType\"]<-as.integer(chooseAlgorithm.temp[,\"targetType\"])");
		re.eval("chooseAlgorithm.temp<-scale(chooseAlgorithm.temp)");
		re.eval("chooseAlgorithm.order<-order(dist(chooseAlgorithm.temp)[1:nrow(chooseAlgorithm.record)])");
		//re.eval("if(chooseAlgorithm.order[1]==0){chooseAlgorithm.order<-chooseAlgorithm.order[2:4]}else {chooseAlgorithm.order<-chooseAlgorithm.order[1:3]}");
		re.eval("chooseAlgorithm.order<-chooseAlgorithm.order[1:3]");
		//re.eval("chooseAlgorithm.vote<-chooseAlgorithm.record$algorithm[chooseAlgorithm.order]");
		RFactor votes=re.eval("chooseAlgorithm.record$algorithm[chooseAlgorithm.order]").asFactor();
		RFactor files=re.eval("chooseAlgorithm.record$fileName[chooseAlgorithm.order]").asFactor();
		//System.out.println("votes.size="+votes.size()+"    files.size="+files.size());
		if(votes.size()!=files.size()) System.err.println("votes size doesn't equals files size.");
		System.out.println("The most similiar data:");
		for(int i=0;i<votes.size();i++){
			System.out.println("No."+(i+1)+": file:\""+files.at(i)+"\"    algorithm:\""+votes.at(i)+"\"");
		}
		//re.eval("factor(algorithmCandicate,levels=c(\"randomForest\",\"GLMNET\",\"MLP\"))");
		re.eval("chooseAlgorithm.table<-table(factor(chooseAlgorithm.record$algorithm[chooseAlgorithm.order],levels=c(\"randomForest\",\"GLMNET\",\"MLP\")))");
		return re.eval("dimnames(chooseAlgorithm.table)[[1]][order(chooseAlgorithm.table,decreasing=T)]").asStringArray();
	}
	public Structure str(String dataName) {
		ArrayList<String> frequnce = new ArrayList<>();
		// Map<String, String> result=new HashMap<String, String>();
		Map<String, attrTypeNClass> result = new HashMap<>();
		result.clear();
		frequnce.clear();
		int obsNum = re.eval("nrow(" + dataName + ")").asInt();
		String[] temp = re.eval("names(" + dataName + ")").asStringArray();
		for (String string : temp) {
			frequnce.add(string);
			String[] className = re.eval("class(" + dataName + "[,\"" + string + "\"])").asStringArray();
			ArrayList<String> classes = new ArrayList<>();
			switch (className[0]) {
			case "numeric":
				result.put(string, new attrTypeNClass(Type.NUM, classes));
				break;
			case "integer":
				result.put(string, new attrTypeNClass(Type.INT, classes));
				break;
			case "factor":
				String[] classList = re.eval("levels(" + dataName + "[,\"" + string + "\"])").asStringArray();
				for (String string2 : classList)
					classes.add(string2);
				result.put(string, new attrTypeNClass(Type.FACTOR, classes));
				break;
			case "ordered":
				String[] classList2 = re.eval("levels(" + dataName + "[,\"" + string + "\"])").asStringArray();
				for (String string2 : classList2)
					classes.add(string2);
				result.put(string, new attrTypeNClass(Type.ORD_FACTOR, classes));
				break;
			default:
				break;
			}
		}
		t.clearText();
		re.eval("str(" + dataName + ")");
		return new Structure(result, frequnce, t.getText(), obsNum);
	}

	public void readData(Options op) {
		try {
			re.eval("data<-read.table(\"" + op.getValue("trainFile") + "\",header=" + op.getValue("header") + ",sep=\"" + op.getValue("sep") + "\",na.strings=c(\"" + op.getValue("na.strings") + "\"))");
			if(op.getValue("object").equals("predict")&&(!op.isNull("sequential")&&op.getValue("sequential").matches("(t|true|T|TRUE)")));
			else{
				if(re.eval("dim(data)[2]>1").asBool().isTRUE())
					re.eval("data<-data[sample(nrow(data)),]");
			}
			re.eval("logiHandle.classes<-lapply(data,class)");
			re.eval("logiHandle.attrs<-logiHandle.classes[logiHandle.classes==\"logical\"]");
			if(re.eval("length(logiHandle.attrs)==0").asBool().isFALSE())
				re.eval("data[,names(logiHandle.attrs)]<-lapply(data[,names(logiHandle.attrs)],factor)");
			if(!op.isNull("values")) {
				//System.out.println(op.getValue("values"));
				//System.out.println("data<-data[,c(\""+op.getValue("values")+"\",\""+op.getValue("target")+"\")]");
				if(op.getValue("object").equals("cluster")||op.getValue("object").equals("associate")||(op.getValue("object").equals("predict")&&(!op.isNull("sequential")&&op.getValue("sequential").matches("(t|true|T|TRUE)"))))
					re.eval("data<-data[,c(\""+op.getValue("values")+"\")]");
				else re.eval("data<-data[,c(\""+op.getValue("values")+"\",\""+op.getValue("target")+"\")]");
			}
			if (!op.isNull("testFile")){
				re.eval("test<-read.table(\"" + op.getValue("testFile") + "\",header=" + op.getValue("header") + ",sep=\"" + op.getValue("sep") + "\",na.strings=c(\"" + op.getValue("na.strings") + "\"))");
				re.eval("test[,names(logiHandle.attrs)]<-lapply(test[,names(logiHandle.attrs)],factor)");
				if(!op.isNull("values"))  re.eval("test<-test[,c(\""+op.getValue("values")+"\",\""+op.getValue("target")+"\")]");
			}
		} catch (Exception e) {
			System.out.println("ranalysis.readData():" + e);
		}
	}
	public Structure scale(Options op,Structure str){
		re.eval("library(infotheo)");
		ArrayList<String> attrs = str.getAttributes();
		for (String attr : attrs) {
			System.out.println(attr);
			if(op.getValue("target").equals(attr)) continue;
			System.out.println("not target");
			if (str.getStructMap().get(attr).getT() == Type.FACTOR || str.getStructMap().get(attr).getT() == Type.ORD_FACTOR) {
				re.eval("data[,\""+attr+"\"]<-discretize(data[,\""+attr+"\"])");
				if(!op.isNull("testFile")) re.eval("test[,\""+attr+"\"]<-discretize(test[,\""+attr+"\"])");
			}
		}
		System.out.println("scale");
		re.eval("data[,c(\""+op.getValue("values")+"\")]<-scale(data[,c(\""+op.getValue("values")+"\")])");
		if(!op.isNull("testFile")) re.eval("test[,c(\""+op.getValue("values")+"\")]<-scale(test[,c(\""+op.getValue("values")+"\")])");
		return str();
	}
	public String chooseFile() {
		return t.rChooseFile(re, 0).replace('\\', '/');
	}

	public void stop() {
		re.end();
	}
	public Options timeSeriesBuild(Options op){
		//re.eval("data<-xts(AirPassengers,seq(as.POSIXct(\"2014-01-01\"),len=length(AirPassengers),by=\"day\"))");
		re.eval("library(forecast)");
		re.eval("library(tseries)");
		re.eval("source(\"E:/R/selfWriteRPrograms/findPQ.R\")");
		re.eval("temp<-data");
		re.eval("pValue<-adf.test(temp)$p.value");
		re.eval("diffTime<-0");
		re.eval("while(pValue>0.05){"
		       +"	temp<-diff(temp)[-1];"
		       +"	pValue<-adf.test(temp)$p.value;"
		       +"	diffTime<-diffTime+1;"
		       +"	cat(\"succeed.\\n\");"
		       +"}");
		t.setDisplay(true);
		re.eval("parameter<-findPQ(data)");
		t.setDisplay(false);
		Options newOp=new  Options();
		newOp.addPair("p",Integer.toString((int)re.eval("parameter$p").asDouble()));
		newOp.addPair("d",Integer.toString((int)re.eval("diffTime").asDouble()));
		newOp.addPair("q",Integer.toString((int)re.eval("parameter$q").asDouble()));
		return newOp;
	}
	public void timeSeriesOutput(Options op){
		System.out.println("Arima configuration p="+op.getValue("p")+"    d="+op.getValue("d")+"   q="+op.getValue("q"));
		System.out.println("write pdf into file:"+op.getValue("outputPDF"));
		re.eval("pdf(\"" + op.getValue("outputPDF") + "\")");
		re.eval("plot(forecast(stats::arima(data,order=c("+op.getValue("p")+","+op.getValue("d")+","+op.getValue("q")+"),method=\"CSS-ML\"),h="+op.getValue("ahead")+"),main=\"time Series predict.\")");	
		re.eval("dev.off()");
		System.out.println("write succeed.");
	}
	public ArrayList<String> targetsEncode(String code) {
		String[] temp = re.eval(code).asStringArray();
		ArrayList<String> encodes = new ArrayList<>();
		for (String string : temp) {
			encodes.add(string);
		}
		return encodes;
	}
	public double RandomForestBuild(Options op,Structure str){
		boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ? true : false;
		double precise = 0;
		re.eval("library(randomForest)");
		re.eval("data<-na.omit(data)");
		re.eval("randomForest.temp<-data");
		ArrayList<String> attrs = str.getAttributes();
		for (String attr : attrs) {
			//System.out.println(attr);
			if (str.getStructMap().get(attr).getT() == Type.FACTOR || str.getStructMap().get(attr).getT() == Type.ORD_FACTOR) {
				//System.out.println(re.eval("length(levels(data$\""+attr+"\"))").asInt());
				if(re.eval("length(levels(data$\""+attr+"\"))").asInt()>53){
					if(attr.equals(op.getValue("target"))){
						System.err.println("Factor target has more than 53 class.");
						return -Double.MAX_VALUE;
					}
					re.eval("data<-data[!names(data)==\""+attr+"\"]");
					//System.out.println(op.getValue("expression"));
					//System.out.println("alter attr="+attr);
					if(op.getValue("expression").contains("+"+attr+"+")) op.alterIfExist("expression", "\\+"+attr+"\\+","+");
					else if(op.getValue("expression").contains(attr+"+")) op.alterIfExist("expression", attr+"\\+","");
					else if(op.getValue("expression").contains("+"+attr)) op.alterIfExist("expression", "\\+"+attr,"");
					//下面是对values进行调整 将类别超过53的从values中去除  但后面也没有用到values这个值 所以可以不用对其进行修改
//					if(!op.isNull("values")){
//						if(op.getValue("values").contains(",\""+attr+"\",")) op.alterIfExist("values", ",\\\""+attr+"\\\",",",");
//						else if(op.getValue("values").contains(attr+"\",\"")) op.alterIfExist("values",attr+"\\\",\\\"","");
//						else if(op.getValue("values").contains("\",\""+attr)) op.alterIfExist("values", "\\\",\\\""+attr,"");
//					}
					System.out.println("attr "+attr+"  has more than 53 classes, removed.");
				}
			}
		}
		sepTrainNTest(op);
		String type = op.getValue("target.type");
		if(type.equals("Factor")){
			//System.out.println(re.eval("levels(data.train$\""+op.getValue("target")+"\")"));
			//System.out.println(re.eval(""));
			re.eval("data.train$\""+op.getValue("target")+"\"<-droplevels(data.train$\""+op.getValue("target")+"\")");
			//System.out.println(re.eval("levels(data.train$\""+op.getValue("target")+"\")"));
		}
		
		//System.out.println(re.eval("data.train$V16"));
		//System.out.println("data.train$\""+op.getValue("target")+"\"<-droplevels(data.train$\""+op.getValue("target")+"\")");
		//System.out.println(re.eval("str(data.train$V16)"));
		re.eval("randomForest.rf<-NULL");
		re.eval("randomForest.rf <- randomForest(" + op.getValue("expression") + ", data.train, importance=TRUE)");
		//System.out.println(re.eval("randomForest.rf"));
		//System.out.println(op.getValue("expression"));
		// System.out.println(re.eval("is.na(sadasdas)"));
		//System.out.println("1");
		if (re.eval("is.null(randomForest.rf)").asBool().isTRUE()) {
			System.out.println("randomForest build fail.");
			re.eval("result<-NULL");
			re.eval("data<-randomForest.temp");
			return -Double.MAX_VALUE;
		}

		// re.eval("randomForest.imp <- importance(randomForest.rf)");
		// re.eval("randomForest.impvar <- rownames(randomForest.imp)[order(randomForest.imp[, 1], decreasing=TRUE)]");
		// re.eval("data.test<-data.test[data.test$\""+op.getValue("target")+"\" %in% levels(data.train$\""+op.getValue("target")+"\"),]");
		// System.out.println("data.test<-data.test[data.test$\""+op.getValue("target")+"\" %in% levels(data.train$\""+op.getValue("target")+"\"),]");
		// re.eval("data.test<-droplevels(data.test)");
		// System.out.println("data.test<-droplevels(data.test)");
		else {
			re.eval("result<-predict(randomForest.rf,data.test,type=\"response\")");
			re.eval("randomForest.target<-data.test$\"" + op.getValue("target") + "\"");
			if (type.equals("Factor"))
				re.eval("result<-factor(result,levels=levels(randomForest.target))");
			// String[] testString=re.eval("levels(result)").asStringArray();
			// for (String string : testString) {
			// System.out.print(string+"    ");
			// }
			// System.out.println();
			// String[]
			// testString2=re.eval("levels(randomForest.target)").asStringArray();
			// for (String string : testString2) {
			// System.out.print(string+"    ");
			// }
			// System.out.println();
			if (type.equals("Factor")) {
				precise = re.eval("(sum(result==randomForest.target))").asInt() / (double) re.eval("length(result)").asInt();
			}// else if(type.equals("int")){
				// precise=re.eval("(sum(round(result)==randomForest.target))/length(result)").asDouble();
				// }
			else if (type.matches("(num|int)")) {
				re.eval("dist<-(result-randomForest.target)");
				re.eval("dist<-dist[!is.na(dist)]");
				precise = re.eval("-sqrt(sum(dist^2)/length(result))").asDouble();
			}
			re.eval("data<-randomForest.temp");
			return precise;
		}
	}
	public void RandomForestDisplay(Options op){
		if(re.eval("is.null(randomForest.rf)").asBool().isTRUE()) return;
		boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ? true : false;
		String type = op.getValue("target.type");
		//System.out.println("result:"+re.eval("as.character(result)"));
		//System.out.println("target:"+re.eval("as.character(randomForest.target)"));
		if (type.matches("(Factor|Ord.factor)")) {
			RFactor results = re.eval("result").asFactor();
			RFactor target = re.eval("randomForest.target").asFactor();
			String[] pred = new String[results.size()];
			String[] real = new String[target.size()];
			for (int i = 0; i < results.size(); i++) {
				pred[i] = results.at(i);
			}
			for (int i = 0; i < target.size(); i++) {
				real[i] = target.at(i);
			}
			//String[] pred = re.eval("as.character(result)").asStringArray();
			//String[] real = re.eval("as.character(randomForest.target)").asStringArray();
			int total = re.eval("length(result)").asInt();
			int right = re.eval("(sum(result==randomForest.target))").asInt();
			if (!verbose) {
				modelTrainingSituation(pred, real);
				System.out.println("total number:" + total + " right number:" + right + "   precise:" + right / (double) total);
			}
		} else if (type.equals("num")) {
			double[] results = re.eval("result").asDoubleArray();
			DecimalFormat df = new DecimalFormat("#.###");
			String[] pred = new String[results.length];
			for (int i = 0; i < pred.length; i++) {
				pred[i] = df.format(results[i]);
			}
			double[] targets = re.eval("randomForest.target").asDoubleArray();
			String[] real = new String[targets.length];
			for (int i = 0; i < targets.length; i++) {
				real[i] = df.format(targets[i]);
			}
			if (!verbose) {
				modelTrainingSituation(pred, real);
				System.out.println("error:" + df.format(re.eval("-sqrt(sum((result-randomForest.target)^2)/length(result))").asDouble()));
			}
		} else if (type.equals("int")) {
			double[] results = re.eval("result").asDoubleArray();
			DecimalFormat df = new DecimalFormat("#.###");
			String[] longPred = new String[results.length];
			int[] targets = re.eval("randomForest.target").asIntArray();
			String[] originPred = new String[results.length];
			for (int i = 0; i < originPred.length; i++) {
				originPred[i] = df.format(results[i]);
				longPred[i] = Long.toString(Math.round(results[i]));
			}
			String[] real = new String[targets.length];
			for (int i = 0; i < targets.length; i++) {
				real[i] = Integer.toString(targets[i]);
			}

			int total = re.eval("length(result)").asInt();
			int right = re.eval("(sum(round(result)==randomForest.target))").asInt();
			if (!verbose) {
				System.out.println("original:");
				modelTrainingSituation(originPred, real);
				System.out.println("after round:");
				modelTrainingSituation(longPred, real);
				System.out.println("error:" + df.format(re.eval("-sqrt(sum((result-randomForest.target)^2)/length(result))").asDouble()));
				System.out.println("total number:" + total + " right number:" + right + "   precise:" + right / (double) total);
			}
		}
		re.eval("imp<-importance(randomForest.rf)");
		String[] importances=re.eval("rownames(imp)[order(imp[, 1], decreasing=TRUE)]").asStringArray();
		System.out.println("attributes' importance rank:");
		for (int i=0;i<importances.length-1;i++) {
			System.out.print(importances[i]+" > ");
		}
		System.out.println(importances[importances.length-1]);
	}
	public void RandomForestOutput(Options op,boolean display){
		if(re.eval("is.null(randomForest.rf)").asBool().isTRUE()) return;
		boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ? true : false;
		String type = op.getValue("target.type");
		if (!op.isNull("outputPDF")) {
			if (!verbose&&display)
				System.out.println("pdf out...");
				re.eval("pdf(\"" + op.getValue("outputPDF") + "\")");
			if (type.matches("(Factor|Ord.factor)")) {
				re.eval("plot(c(1:length(randomForest.target)),as.factor(randomForest.target),type=\"b\",lty=2,pch=16,col=\"red\",main=\"Predict vs. Reality\",xlab=\"Item Number\",ylab=\"Value\")");
				re.eval("lines(as.factor(result),type=\"b\",lty=2,pch=10,col=\"blue\")");
				re.eval("legend(\"topleft\",inset=.05,title=\"Lines example\",c(\"Reality\",\"Prediction\"),lty=c(1,2),pch=c(10,16),col=c(\"blue\",\"red\"))");
			} else {
				re.eval("opar<-par(no.readonly=T)");
				re.eval("layout(matrix(c(1,1,2,2),2,2,byrow=T),heights=c(4,3))");
				re.eval("plot(randomForest.target,type=\"b\",lty=2,pch=16,col=\"red\",main=\"Predict vs. Reality\",xlab=\"Item Number\",ylab=\"Value\",cex=0.85)");
				re.eval("lines(result,type=\"b\",lty=1,pch=10,col=\"blue\",cex=0.85)");
				re.eval("legend(\"topleft\",inset=.05,title=\"Lines example\",c(\"Reality\",\"Prediction\"),lty=c(1,2),pch=c(15,17),col=c(\"blue\",\"red\"),cex=0.85)");
				re.eval("plot((result-randomForest.target),type=\"b\",lty=2,pch=18,cex=0.85,col=\"red\",main=\"Error of Each Item\",xlab=\"Item Number\",ylab=\"Error\")");
				re.eval("par(opar)");
			}
			re.eval("dev.off()");
			if (!verbose&&display) System.out.println("pdf succeed.");
		}
		if (!op.isNull("testFile")) {
			//re.eval("test<-na.omit(test)");
			//re.eval("test.values<-data.matrix(test[,c(\"" + op.getValue("values") + "\")])");
			re.eval("result<-predict(randomForest.rf,newdata=test,type=\"response\")");
			if (type.equals("Factor")) {
				String[] pred = re.eval("as.character(result)").asStringArray();
				if (op.isNull("outputTXT")) {
					if (!verbose&&display)
						for (int i = 0; i < pred.length; i++)
							System.out.println("NUM " + i + ":" + pred[i]);
				} else
					writePredict(pred, op, true);
			} else {
				double[] results = re.eval("result").asDoubleArray();
				if (type.equals("num")) {
					DecimalFormat df = new DecimalFormat("#.###");
					String[] pred = new String[results.length];
					for (int i = 0; i < pred.length; i++)
						pred[i] = df.format(results[i]);
					if (op.isNull("outputTXT")) {
						if (!verbose&&display)
							for (int i = 0; i < pred.length; i++)
								System.out.println("NUM " + i + ":" + pred[i]);
					} else
						writePredict(pred, op, true);
				}
				if (type.equals("int")) {
					String[] pred = new String[results.length];
					for (int i = 0; i < pred.length; i++)
						pred[i] = Long.toString(Math.round(results[i]));
					if (op.isNull("outputTXT")) {
						if (!verbose&&display)
							for (int i = 0; i < pred.length; i++)
								System.out.println("NUM " + i + ":" + pred[i]);
					} else
						writePredict(pred, op, true);
				}
			}
		}
	}
	public double GLMNETBuild(Options op){
		boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ? true : false;
		double precise = 0;
		re.eval("library(glmnet)");
		re.eval("data.temp<-data");
		re.eval("data<-na.omit(data)");
		re.eval("data<-data[sample(nrow(data)),]");
		String type = op.getValue("target.type");
		if(type.equals("Factor")){
			re.eval("data<-data[!data$\""+op.getValue("target")+"\" %in% (levels(data$\""+op.getValue("target")+"\")[(table(data$\""+op.getValue("target")+"\")<3)]),]");
			re.eval("data$\""+op.getValue("target")+"\"<-droplevels(data$\""+op.getValue("target")+"\")");
			int num=0;
			do {
				if(num==20) return -Double.MAX_VALUE;
				num++;
				sepTrainNTest(op);
				re.eval("train.values<-data.matrix(data.train[,c(\"" + op.getValue("values") + "\")])");
				// System.out.println("train.values<-data.matrix(data.train[,c(\""
				// + op.getValue("values") + "\")])");
				re.eval("train.target<-data.matrix(data.train[,\"" + op.getValue("target") + "\"])");
				System.out.println("redo");
				//if (!verbose) System.out.println("train.target min classes number: " + re.eval("min(table(train.target))").asInt() + "(it must over 2, or should reSep)");
			} while (re.eval("min(table(train.target))").asInt() < 3);
		}else{
			sepTrainNTest(op);
			re.eval("train.values<-data.matrix(data.train[,c(\"" + op.getValue("values") + "\")])");
			re.eval("train.target<-data.matrix(data.train[,\"" + op.getValue("target") + "\"])");
		}
		// System.out.println("train.target<-data.matrix(data.train[,\"" +
		// op.getValue("target") + "\"])");
		re.eval("test.values<-data.matrix(data.test[,c(\"" + op.getValue("values") + "\")])");
		// System.out.println("test.values<-data.matrix(data.test[,c(\""
		// + op.getValue("values") + "\")])");
		re.eval("test.target<-data.matrix(data.test[,\"" + op.getValue("target") + "\"])");
		// System.out.println("test.target<-data.matrix(data.test[,\"" +
		// op.getValue("target") + "\"])");
		// System.out.println(re.eval("test.target"));
		re.eval("data.cvfit<-cv.glmnet(train.values,train.target,family=\"" + op.getValue("family") + "\",type." + op.getValue("type.") + "=\"" + op.getValue("measure") + "\")");
		// System.out.println("data.cvfit<-cv.glmnet(train.values,train.target,family=\""+op.getValue("family")+"\",type."+op.getValue("type.")+"=\""+op.getValue("measure")+"\")");
		re.eval("result<-predict(data.cvfit,newx=test.values,s=\"lambda.min\",type=\"" + op.getValue("predict.type") + "\")");
		//System.out.println(re.eval("result"));
		if (type.equals("Factor")) {
			precise = re.eval("(sum(result==test.target))/length(result)").asDouble();
		}//else if(type.equals("int")){
		//	precise = re.eval("(sum(round(result)==test.target))/length(result)").asDouble();
		//}
		else if (type.matches("(num|int)")) {
			re.eval("dist<-(result-test.target)");
			re.eval("dist<-dist[!is.na(dist)]");
			precise = re.eval("-sqrt(sum(dist^2)/length(result))").asDouble();	
		}
		re.eval("data<-data.temp");
		return precise;
		
	}
	public void GLMNETDisplay(Options op){
		boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ? true : false;
		String type = op.getValue("target.type");
		if (type.equals("Factor")) {
			String[] pred = re.eval("result").asStringArray();
			String[] real = re.eval("test.target").asStringArray();
			int total = re.eval("length(result)").asInt();
			int right = re.eval("(sum(result==test.target))").asInt();
			if (!verbose) {
				modelTrainingSituation(pred, real);
				System.out.println("total number:" + total + " right number:" + right + "   precise:" + right / (double) total);
			}
		} else if (type.equals("num")) {
			double[] results = re.eval("result").asDoubleArray();
			DecimalFormat df = new DecimalFormat("#.###");
			String[] pred = new String[results.length];
			for (int i = 0; i < pred.length; i++) {
				pred[i] = df.format(results[i]);
			}
			// System.out.println(re.eval("test.target"));
			double[] targets = re.eval("test.target").asDoubleArray();
			String[] real = new String[targets.length];
			for (int i = 0; i < targets.length; i++) {
				real[i] = df.format(targets[i]);
			}
			if (!verbose) {
				modelTrainingSituation(pred, real);
				System.out.println("error:" + df.format(re.eval("-sqrt(sum((result-test.target)^2)/length(result))").asDouble()));
			}
		} else if (type.equals("int")) {
			double[] results = re.eval("result").asDoubleArray();
			DecimalFormat df = new DecimalFormat("#.###");
			String[] longPred = new String[results.length];
			int[] targets = re.eval("test.target").asIntArray();
			String[] originPred = new String[results.length];
			for (int i = 0; i < originPred.length; i++) {
				originPred[i] = df.format(results[i]);
				longPred[i] = Long.toString(Math.round(results[i]));
			}
			String[] real = new String[targets.length];
			for (int i = 0; i < targets.length; i++) {
				real[i] = Integer.toString(targets[i]);
			}

			int total = re.eval("length(result)").asInt();
			int right = re.eval("(sum(round(result)==test.target))").asInt();
			if (!verbose) {
				System.out.println("original:");
				modelTrainingSituation(originPred, real);
				System.out.println("after round:");
				modelTrainingSituation(longPred, real);
				re.eval("dist<-(result-test.target)");
				re.eval("dist<-dist[!is.na(dist)]");
				System.out.println("error:" + df.format(re.eval("-sqrt(sum(dist^2)/length(result))").asDouble()));
				System.out.println("total number:" + total + " right number:" + right + "   precise:" + right / (double) total);
			}
		}
		if (!verbose) {
			String[] attrs = op.getValue("values").split("\",\"");
			switch (op.getValue("family")) {
			case "binomial":
			case "gaussian":
				GLMNETattrUseTable(attrs, null);
				break;
			case "multinomial":
				ArrayList<String> levels = targetsEncode("names(coef(data.cvfit))");
				GLMNETattrUseTable(attrs, levels);
				break;
			default:
				System.err.println("no such family.");
				break;
			}
		}
	}
	public void GLMNETOutput(Options op,boolean display){
		boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ? true : false;
		String type = op.getValue("target.type");
		if (!op.isNull("outputPDF")) {
			if (!verbose&&display)
				System.out.println("pdf out...");
			re.eval("pdf(\"" + op.getValue("outputPDF") + "\")");
			if (type.equals("Factor")) {
				re.eval("plot(c(1:length(test.target)),as.factor(test.target),type=\"b\",lty=2,pch=16,col=\"red\",main=\"Predict vs. Reality\",xlab=\"Item Number\",ylab=\"Value\")");
				re.eval("lines(as.factor(result[,1]),type=\"b\",lty=2,pch=10,col=\"blue\")");
				re.eval("legend(\"topleft\",inset=.05,title=\"Lines example\",c(\"Reality\",\"Prediction\"),lty=c(1,2),pch=c(10,16),col=c(\"blue\",\"red\"))");
			} else {
				re.eval("opar<-par(no.readonly=T)");
				re.eval("layout(matrix(c(1,1,2,2),2,2,byrow=T),heights=c(4,3))");
				re.eval("plot(test.target,type=\"b\",lty=2,pch=16,col=\"red\",main=\"Predict vs. Reality\",xlab=\"Item Number\",ylab=\"Value\",cex=0.85)");
				re.eval("lines(result,type=\"b\",lty=1,pch=10,col=\"blue\",cex=0.85)");
				re.eval("legend(\"topleft\",inset=.05,title=\"Lines example\",c(\"Reality\",\"Prediction\"),lty=c(1,2),pch=c(15,17),col=c(\"blue\",\"red\"),cex=0.85)");
				re.eval("plot((result-test.target),type=\"b\",lty=2,pch=18,cex=0.85,col=\"red\",main=\"Error of Each Item\",xlab=\"Item Number\",ylab=\"Error\")");
				re.eval("par(opar)");
			}
			re.eval("dev.off()");
			if (!verbose&&display) System.out.println("pdf succeed.");
		}
		if (!op.isNull("testFile")) {
			//re.eval("test<-read.table(\"" + op.getValue("testFile") + "\",header=" + op.getValue("header") + ",sep=\"" + op.getValue("sep") + "\",na.strings=c(\"" + op.getValue("na.strings") + "\"))");
			re.eval("test.temp<-test");
			re.eval("test<-na.omit(test)");
			re.eval("test.values<-data.matrix(test[,c(\"" + op.getValue("values") + "\")])");
			re.eval("result<-predict(data.cvfit,newx=test.values,s=\"lambda.1se\",type=\"" + op.getValue("predict.type") + "\")");
			if (type.equals("Factor")) {
				String[] pred = re.eval("result").asStringArray();
				if (op.isNull("outputTXT")) {
					if (!verbose&&display)
						for (int i = 0; i < pred.length; i++)
							System.out.println("NUM " + i + ":" + pred[i]);
				} else
					writePredict(pred, op, true);
			} else {
				double[] results = re.eval("result").asDoubleArray();
				if (type.equals("num")) {
					DecimalFormat df = new DecimalFormat("#.###");
					String[] pred = new String[results.length];
					for (int i = 0; i < pred.length; i++)
						pred[i] = df.format(results[i]);
					if (op.isNull("outputTXT")) {
						if (!verbose&&display)
							for (int i = 0; i < pred.length; i++)
								System.out.println("NUM " + i + ":" + pred[i]);
					} else
						writePredict(pred, op, true);
				}
				if (type.equals("int")) {
					String[] pred = new String[results.length];
					for (int i = 0; i < pred.length; i++)
						pred[i] = Long.toString(Math.round(results[i]));
					if (op.isNull("outputTXT")) {
						if (!verbose&&display)
							for (int i = 0; i < pred.length; i++)
								System.out.println("NUM " + i + ":" + pred[i]);
					} else
						writePredict(pred, op, true);
				}
			}
			re.eval("test<-test.temp");
		}
	}
//	public double GLMNET(Options op, boolean display) {
//		boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ? true : false;
//		double precise = 0;
//		re.eval("library(glmnet)");
//		re.eval("data<-na.omit(data)");
//		re.eval("data<-data[sample(nrow(data)),]");
//		do {
//			sepTrainNTest(op);
//			re.eval("train.values<-data.matrix(data.train[,c(\"" + op.getValue("values") + "\")])");
//			// System.out.println("train.values<-data.matrix(data.train[,c(\""
//			// + op.getValue("values") + "\")])");
//			re.eval("train.target<-data.matrix(data.train[,\"" + op.getValue("target") + "\"])");
//			if (!verbose && display)
//				System.out.println("train.target min classes number: " + re.eval("min(table(train.target))").asInt() + "(it must over 2, or should reSep)");
//		} while (re.eval("min(table(train.target))").asInt() < 3);
//		// System.out.println("train.target<-data.matrix(data.train[,\"" +
//		// op.getValue("target") + "\"])");
//		re.eval("test.values<-data.matrix(data.test[,c(\"" + op.getValue("values") + "\")])");
//		// System.out.println("test.values<-data.matrix(data.test[,c(\""
//		// + op.getValue("values") + "\")])");
//		re.eval("test.target<-data.matrix(data.test[,\"" + op.getValue("target") + "\"])");
//		// System.out.println("test.target<-data.matrix(data.test[,\"" +
//		// op.getValue("target") + "\"])");
//		// System.out.println(re.eval("test.target"));
//		re.eval("data.cvfit<-cv.glmnet(train.values,train.target,family=\"" + op.getValue("family") + "\",type." + op.getValue("type.") + "=\"" + op.getValue("measure") + "\")");
//		// System.out.println("data.cvfit<-cv.glmnet(train.values,train.target,family=\""+op.getValue("family")+"\",type."+op.getValue("type.")+"=\""+op.getValue("measure")+"\")");
//		re.eval("result<-predict(data.cvfit,newx=test.values,s=\"lambda.1se\",type=\"" + op.getValue("predict.type") + "\")");
//		// System.out.println("result<-predict(data.cvfit,newx=test.values,s=\"lambda.min\",type=\""
//		// + op.getValue("predict.type") + "\")");
//
//		// String[] temp = op.getValue("values").split("\",\"");
//		// for(String s:temp)
//		// System.out.println(s+" num min:"+re.eval("min(table(train.values[,\""+s+"\"]))").asInt());
//		// if(op.getValue("family").equals("multinomial")&&re.eval("length(unique(train.target))").asInt()<3)
//		// System.err.println("class number:"+re.eval("length(unique(train.target))").asInt());
//		// *********************************************************************************************
//		String type = op.getValue("target.type");
//		if (type.equals("Factor")) {
//			String[] pred = re.eval("result").asStringArray();
//			String[] real = re.eval("test.target").asStringArray();
//			int total = re.eval("length(result)").asInt();
//			int right = re.eval("(sum(result==test.target))").asInt();
//			if (!verbose && display) {
//				modelTrainingSituation(pred, real);
//				System.out.println("total number:" + total + " right number:" + right + "   precise:" + right / (double) total);
//			}
//			precise = right / (double) total;
//			// System.out.println("precise="+precise);
//		} else if (type.equals("num")) {
//			double[] results = re.eval("result").asDoubleArray();
//			DecimalFormat df = new DecimalFormat("#.###");
//			String[] pred = new String[results.length];
//			for (int i = 0; i < pred.length; i++) {
//				pred[i] = df.format(results[i]);
//			}
//			// System.out.println(re.eval("test.target"));
//			double[] targets = re.eval("test.target").asDoubleArray();
//			String[] real = new String[targets.length];
//			for (int i = 0; i < targets.length; i++) {
//				real[i] = df.format(targets[i]);
//			}
//			precise = re.eval("-sqrt(sum((result-test.target)^2))/length(result)").asDouble();
//			if (!verbose && display) {
//				modelTrainingSituation(pred, real);
//				System.out.println("error:" + df.format(precise));
//			}
//		} else if (type.equals("int")) {
//			double[] results = re.eval("result").asDoubleArray();
//			DecimalFormat df = new DecimalFormat("#.###");
//			String[] longPred = new String[results.length];
//			int[] targets = re.eval("test.target").asIntArray();
//			String[] originPred = new String[results.length];
//			for (int i = 0; i < originPred.length; i++) {
//				originPred[i] = df.format(results[i]);
//				longPred[i] = Long.toString(Math.round(results[i]));
//			}
//			String[] real = new String[targets.length];
//			for (int i = 0; i < targets.length; i++) {
//				real[i] = Integer.toString(targets[i]);
//			}
//
//			int total = re.eval("length(result)").asInt();
//			int right = re.eval("(sum(round(result)==test.target))").asInt();
//			precise = re.eval("-sqrt(sum((result-test.target)^2))/length(result)").asDouble();
//			if (!verbose && display) {
//				System.out.println("original:");
//				modelTrainingSituation(originPred, real);
//				System.out.println("after round:");
//				modelTrainingSituation(longPred, real);
//				System.out.println("error:" + df.format(precise));
//				System.out.println("total number:" + total + " right number:" + right + "   precise:" + right / (double) total);
//			}
//		}
//		if (display) {
//			// *****************************************************************
//			if (!verbose) {
//				String[] attrs = op.getValue("values").split("\",\"");
//				switch (op.getValue("family")) {
//				case "binomial":
//				case "gaussian":
//					GLMNETattrUseTable(attrs, null);
//					break;
//				case "multinomial":
//					ArrayList<String> levels = targetsEncode("names(coef(data.cvfit))");
//					GLMNETattrUseTable(attrs, levels);
//					break;
//				default:
//					System.err.println("no such family.");
//					break;
//				}
//			}
//			// if (display) {
//			// *******************************************************************
//			if (!op.isNull("outputPDF")) {
//				if (!verbose)
//					System.out.println("pdf out...");
//				re.eval("pdf(\"" + op.getValue("outputPDF") + "\")");
//				if (type.equals("Factor")) {
//					re.eval("plot(c(1:length(test.target)),as.factor(test.target),type=\"b\",lty=2,pch=16,col=\"red\",main=\"Predict vs. Reality\",xlab=\"Item Number\",ylab=\"Value\")");
//					re.eval("lines(as.factor(result[,1]),type=\"b\",lty=2,pch=10,col=\"blue\")");
//					re.eval("legend(\"topleft\",inset=.05,title=\"Lines example\",c(\"Reality\",\"Prediction\"),lty=c(1,2),pch=c(10,16),col=c(\"blue\",\"red\"))");
//				} else {
//					re.eval("opar<-par(no.readonly=T)");
//					re.eval("layout(matrix(c(1,1,2,2),2,2,byrow=T),heights=c(4,3))");
//					re.eval("plot(test.target,type=\"b\",lty=2,pch=17,col=\"red\",main=\"Predict vs. Reality\",xlab=\"Item Number\",ylab=\"Value\",cex=0.85)");
//					re.eval("lines(result,type=\"b\",lty=1,pch=15,col=\"blue\",cex=0.85)");
//					re.eval("legend(\"topleft\",inset=.05,title=\"Lines example\",c(\"Reality\",\"Prediction\"),lty=c(1,2),pch=c(15,17),col=c(\"blue\",\"red\"),cex=0.85)");
//					re.eval("plot((result-test.target),type=\"b\",lty=2,pch=18,cex=0.85,col=\"red\",main=\"Error of Each Item\",xlab=\"Item Number\",ylab=\"Error\")");
//					re.eval("par(opar)");
//				}
//				re.eval("dev.off()");
//				if (!verbose)
//					System.out.println("pdf succeed.");
//			}
//			if (!op.isNull("testFile")) {
//				re.eval("test<-read.table(\"" + op.getValue("testFile") + "\",header=" + op.getValue("header") + ",sep=\"" + op.getValue("sep") + "\",na.strings=c(\"" + op.getValue("na.strings") + "\"))");
//				re.eval("test<-na.omit(test)");
//				re.eval("test.values<-as.matrix(test[,c(\"" + op.getValue("values") + "\")])");
//				re.eval("result<-predict(data.cvfit,newx=test.values,s=\"lambda.1se\",type=\"" + op.getValue("predict.type") + "\")");
//				if (type.equals("Factor")) {
//					String[] pred = re.eval("result").asStringArray();
//					if (op.isNull("outputTXT")) {
//						if (!verbose)
//							for (int i = 0; i < pred.length; i++)
//								System.out.println("NUM " + i + ":" + pred[i]);
//					} else
//						writePredict(pred, op, true);
//				} else {
//					double[] results = re.eval("result").asDoubleArray();
//					if (type.equals("num")) {
//						DecimalFormat df = new DecimalFormat("#.###");
//						String[] pred = new String[results.length];
//						for (int i = 0; i < pred.length; i++)
//							pred[i] = df.format(results[i]);
//						if (op.isNull("outputTXT")) {
//							if (!verbose)
//								for (int i = 0; i < pred.length; i++)
//									System.out.println("NUM " + i + ":" + pred[i]);
//						} else
//							writePredict(pred, op, true);
//					}
//					if (type.equals("int")) {
//						String[] pred = new String[results.length];
//						for (int i = 0; i < pred.length; i++)
//							pred[i] = Long.toString(Math.round(results[i]));
//						if (op.isNull("outputTXT")) {
//							if (!verbose)
//								for (int i = 0; i < pred.length; i++)
//									System.out.println("NUM " + i + ":" + pred[i]);
//						} else
//							writePredict(pred, op, true);
//					}
//				}
//			}
//		}
//		// *********************************************************************************************
//		// System.out.println("2.precise="+precise);
//		return precise;
//	}

	public void modelTrainingSituation(String[] pred, String[] real) {
		if (pred.length == real.length) {
			int num = 1;
			int maxlength = 7;
			for (String p : pred) {
				maxlength = p.length() > maxlength ? p.length() : maxlength;
			}
			for (String r : real) {
				maxlength = r.length() > maxlength ? r.length() : maxlength;
			}
			System.out.println("model trainning situation:");
			System.out.println("\t" + String.format("%-" + maxlength * 2 + "s", "real") + String.format("%-" + maxlength * 2 + "s", "predict"));
			for (int i = 0; i < real.length; i++) {
				System.out.println(num++ + ":\t" + String.format("%-" + maxlength * 2 + "s", real[i]) + String.format("%-" + maxlength * 2 + "s", pred[i]));
			}
		} else
			System.err.println("pred and real length is not equal :" + pred.length + "  and  " + real.length);
	}

	public void GLMNETattrUseTable(String[] attrs, ArrayList<String> levels) {
		System.out.println("the attrs that used:('O' means used, 'X' means not)");
		int maxlength = 0;
		int rowWidth = 1;
		for (String string : attrs) {
			maxlength = maxlength > string.length() ? maxlength : string.length();
		}
		if (levels != null) {
			for (String string : levels)
				rowWidth = rowWidth > string.length() ? rowWidth : string.length();
			System.out.print(String.format("%-" + rowWidth * 2 + "s", ""));
		}
		for (String string : attrs) {
			System.out.print(String.format("%-" + maxlength * 2 + "s", string));
		}
		System.out.println();
		if (levels == null) {
			String[] temp = re.eval("rownames(data.matrix(coef(data.cvfit,s=\"lambda.min\")))[data.matrix(coef(data.cvfit,s=\"lambda.min\"))!=0][-1]").asStringArray();
			for (String string2 : attrs) {
				// System.out.print("\n want"+string2);
				boolean flag = false;
				for (String string3 : temp) {
					// System.out.print(" find "+string3);
					if (string2.equals(string3)) {
						// System.out.println("true");
						flag = true;
						break;
					}
					// System.out.print("  "+string2);
				}
				if (flag)
					System.out.print(String.format("%-" + maxlength * 2 + "s", "O"));
				else
					System.out.print(String.format("%-" + maxlength * 2 + "s", "X"));
			}
			System.out.println();
		} else
			for (String string : levels) {
				System.out.print(String.format("%-" + rowWidth * 2 + "s", string));
				String[] temp = re.eval("rownames(data.matrix(coef(data.cvfit)$\"" + string + "\"))[data.matrix(coef(data.cvfit)$\"" + string + "\")!=0][-1]").asStringArray();
				for (String string2 : attrs) {
					// System.out.print("\n want"+string2);
					boolean flag = false;
					for (String string3 : temp) {
						// System.out.print(" find "+string3);
						if (string2.equals(string3)) {
							// System.out.println("true");
							flag = true;
							break;
						}
						// System.out.print("  "+string2);
					}
					if (flag)
						System.out.print(String.format("%-" + maxlength * 2 + "s", "O"));
					else
						System.out.print(String.format("%-" + maxlength * 2 + "s", "X"));
				}
				System.out.println();
			}
	}
	public double MLPBuild(Options op,Structure str){
		return MLPBuild(op, str,false);
	}
	public double MLPBuild(Options op,Structure str,boolean display) {
		boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ? true : false;
		double precise = 0;
		re.eval("library(Rcpp)");
		re.eval("library(RSNNS)");
		re.eval("data.temp<-data");
		re.eval("test.temp<-test");
		re.eval("data<-na.omit(data)");
		re.eval("data<-data[sample(nrow(data)),]");
		String type = op.getValue("target.type");
		// re.eval("data.values<-data[,c(\"" + op.getValue("values") + "\")]");
		//Structure str = str();
		ArrayList<String> attrs = str.getAttributes();
		ArrayList<String> discretizeAttr = new ArrayList<>();
		for (String attr : attrs) {
			if (str.getStructMap().get(attr).getT() == Type.FACTOR || str.getStructMap().get(attr).getT() == Type.ORD_FACTOR) {
				discretizeAttr.add(attr);
			}
		}
		if (discretizeAttr.contains(op.getValue("target"))) {
			discretizeAttr.remove(op.getValue("target"));
			//System.out.println("targets decode.");
			re.eval("data.targets<-decodeClassLabels(data[,\"" + op.getValue("target") + "\"])");
			//System.out.println("decode out");
		} else
			re.eval("data.targets<-data[,\"" + op.getValue("target") + "\"]");
		if (discretizeAttr.size() > 0) {
			if (!op.isNull("testFile")) {
				re.eval("test <- na.omit(test)");
				re.eval("temp<-rbind(data,test)");
				// re.eval("test[,c(\"" + op.getValue("values") +
				// "\")]<-normalizeData(test[,c(\"" + op.getValue("values") +
				// "\")])");
				// re.eval("result <- predict(model,test.values)");
			} else
				re.eval("temp<-data");
			re.eval("temp.values<-temp[,c(\"" + op.getValue("values") + "\")]");
			// System.out.println("temp.values<-temp[,c(\"" +
			// op.getValue("values") + "\")]");
			// re.eval("temp.target<-temp[,\"" + op.getValue("target") + "\"]");
			//System.out.println("values decode");
			for (String attr : discretizeAttr) {
				re.eval("temp.values<-cbind(temp.values[,!names(temp.values)==\"" + attr + "\"],decodeClassLabels(temp.values[,\"" + attr + "\"]))");
				// System.out.println("temp.values<-cbind(temp.values[,!names(temp.values)==\""+attr+"\"],decodeClassLabels(temp.values[,\""
				// + attr + "\"]))");
			}
			if (!op.isNull("testFile")) {
				re.eval("data.values<-temp.values[1:nrow(data),]");
				// System.out.println("data.values<-temp.values[1:nrow(data),]");
				re.eval("test.values<-temp.values[(nrow(data)+1):nrow(temp.values),]");
				// System.out.println("test.values<-temp.values[norw(data)+1:nrow(temp.values),]");
				re.eval("test.values<-normalizeData(test.values)");
				// System.out.println("test.values<-normalizeData(test.values)");
			} else
				re.eval("data.values<-temp.values");
		} else {
			if (!op.isNull("testFile")) {
				// re.eval("test<-read.table(\"" + op.getValue("testFile") +
				// "\",header=" + op.getValue("header") + ",sep=\"" +
				// op.getValue("sep") + "\",na.strings=c(\"" +
				// op.getValue("na.strings") + "\"))");
				//System.out.println("test normalize.");
				re.eval("test <- na.omit(test)");
				re.eval("test.values<-test[,c(\"" + op.getValue("values") + "\")]");
				re.eval("test.values<-normalizeData(test.values)");
			}
			re.eval("data.values<-data[,c(\"" + op.getValue("values") + "\")]");
		}
		//System.out.println("data split and norm");
		re.eval("data.split<-splitForTrainingAndTest(data.values, data.targets, ratio=" + (1 - Double.parseDouble(op.getValue("ratio"))) + ")");
		re.eval("data.split<- normTrainingAndTestSet(data.split)");
		//long min = Math.round(Math.sqrt((double) (re.eval("ncol(data.values)").asInt() + re.eval("ncol(data.targets)").asInt())) / 2);
		//re.eval("parameterGrid <- expand.grid(2*c(" + min + ":" + (min + 10) + "), c(0.00316, 0.0147, 0.1))");
		re.eval("parameterGrid <- expand.grid(c(" + op.getValue("size")+ "), c(0.00316, 0.0147, 0.1))");
		// System.out.println("parameterGrid <- expand.grid(2*c(" + min + ":" +
		// (min + 10) + "), c(0.00316, 0.0147, 0.1))");
		re.eval("colnames(parameterGrid)<-c(\"nHidden\", \"learnRate\")");
		// System.out.println("colnames(parameterGrid)<-c(\"nHidden\", \"learnRate\")");
		re.eval("rownames(parameterGrid)<-paste(\"nnet-\", apply(parameterGrid, 1, function(x) {paste(x,sep=\"\", collapse=\"-\")}), sep=\"\")");
		// System.out.println("rownames(parameterGrid)<-paste(\"nnet-\", apply(parameterGrid, 1, function(x) {paste(x,sep=\"\", collapse=\"-\")}), sep=\"\")");
		if (!verbose&&display) System.out.println("modeling...");
		//System.out.println("models");
		re.eval("models<-apply(parameterGrid, 1, function(p) {" + "	mlp(data.split$inputsTrain, data.split$targetsTrain, size=p[1], learnFunc=\"Std_Backpropagation\"," + "	learnFuncParams=c(p[2], 0.1), maxit=" + op.getValue("maxit") + ", inputsTest=iris$inputsTest, " + "	targetsTest=iris$targetsTest, linOut=" + op.getValue("linOut") + ")" + "})");
		// System.out.println("models<-apply(parameterGrid, 1, function(p) {" +
		// "	mlp(data.split$inputsTrain, data.split$targetsTrain, size=p[1], learnFunc=\"Std_Backpropagation\","
		// + "	learnFuncParams=c(p[2], 0.1), maxit=" + op.getValue("maxit") +
		// ", inputsTest=iris$inputsTest, " +
		// "	targetsTest=iris$targetsTest, linOut=" + op.getValue("linOut") +
		// ")" + "})");
		//System.out.println("test errors");
		re.eval("testErrors <-  data.frame(lapply(models, function(mod) { " + "	pred  <-  predict(mod,data.split$inputsTest);" + " error <- sqrt(sum((pred - data.split$targetsTest)^2)/length(pred));" + "	error " + "}))");
		// System.out.println("testErrors <-  data.frame(lapply(models, function(mod) { "
		// + "	pred  <-  predict(mod,data.split$inputsTest);" +
		// "	error <-  sqrt(sum((pred - data.split$targetsTest)^2));" +
		// "	error " + "}))");
		re.eval("testErrors<-testErrors[!is.na(testErrors)]");
		re.eval("model<-models[[which(min(testErrors) == testErrors)]]");
		// System.out.println("testErrors:" + re.eval("testErrors"));
		// System.out.println("which min:" +
		// re.eval("which(min(testErrors) == testErrors)"));
		if (!verbose&&display) System.out.println("hidden num:" + re.eval("model$archParams$size[[1]]").asDouble());
		re.eval("result<-predict(model,data.split$inputsTest);");
		// System.out.println("predict:"+re.eval("result"));
		// System.out.println("real:"+re.eval("data.split$targetsTest"));
		// System.out.println("error:"+re.eval("min(testErrors)"));
		if (type.equals("Factor")) {
			//System.out.println("encode");
			if (op.getValue("encode.method").equals("402040")) {
				re.eval("pred<-encodeClassLabels(result,method = \"402040\", l = 0.4, h = 0.6)");
				re.eval("real<-encodeClassLabels(data.split$targetsTest,method = \"402040\", l = 0.4, h = 0.6)");
			} else {
				re.eval("pred<-encodeClassLabels(result,method = \"WTA\")");
				re.eval("real<-encodeClassLabels(data.split$targetsTest,method = \"WTA\")");
			}
			precise = re.eval("(sum(round(pred)==real))/length(pred)").asDouble();
		}//else if(type.equals("int")){
			//precise = re.eval("(sum(round(result)==data.split$targetsTest))/length(result)").asDouble();
		//}
		else	precise = re.eval("-min(testErrors)").asDouble();
		re.eval("data<-data.temp");
		re.eval("test<-test.temp");
		return precise;
	}

	public void MLPDisplay(Options op) {
		boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ? true : false;
		String type = op.getValue("target.type");
		if (type.equals("Factor")) {
			ArrayList<String> targetsLevels = targetsEncode("colnames(data.targets)");
			double[] result;
			int[] target;
			if (op.getValue("encode.method").equals("402040")) {
				result = re.eval("pred<-encodeClassLabels(result,method = \"402040\", l = 0.4, h = 0.6)").asDoubleArray();
				target = re.eval("real<-encodeClassLabels(data.split$targetsTest,method = \"402040\", l = 0.4, h = 0.6)").asIntArray();
			} else {
				result = re.eval("pred<-encodeClassLabels(result,method = \"WTA\")").asDoubleArray();
				target = re.eval("real<-encodeClassLabels(data.split$targetsTest,method = \"WTA\")").asIntArray();
			}
			String[] pred = new String[result.length];
			for (int i = 0; i < result.length; i++)
				pred[i] = (result[i] > 0) ? targetsLevels.get((int) result[i] - 1) : "NOT SURE";
			String[] real = new String[target.length];
			for (int i = 0; i < target.length; i++)
				real[i] = (target[i] > 0) ? targetsLevels.get(target[i] - 1) : "NOT SURE";
			int total = re.eval("length(pred)").asInt();
			int right = re.eval("(sum(round(pred)==real))").asInt();
			if (!verbose) {
				modelTrainingSituation(pred, real);
				System.out.println("total number:" + total + " right number:" + right + "   precise:" + right / (double) total);
			}
		} else {
			double[] results = re.eval("result").asDoubleArray();
			DecimalFormat df = new DecimalFormat("#.###");
			String[] pred = new String[results.length];
			for (int i = 0; i < results.length; i++)
				pred[i] = df.format(results[i]);
			if (type.equals("int")) {
				int[] target = re.eval("data.split$targetsTest").asIntArray();
				String[] real = new String[target.length];
				for (int i = 0; i < target.length; i++) {
					real[i] = Integer.toString(target[i]);
				}
				String[] longPred = new String[results.length];
				for (int i = 0; i < results.length; i++) {
					longPred[i] = Long.toString(Math.round(results[i]));
				}
				if (!verbose) {
					System.out.println("origin:");
					modelTrainingSituation(pred, real);
					System.out.println("after round:");
					modelTrainingSituation(longPred, real);
					int total = re.eval("length(result)").asInt();
					int right = re.eval("(sum(round(result)==data.split$targetsTest))").asInt();
					System.out.println("total number:" + total + " right number:" + right + "   precise:" + right / (double) total);
				}
			} else if (type.equals("num")) {
				double[] target = re.eval("data.split$targetsTest").asDoubleArray();
				String[] real = new String[target.length];
				for (int i = 0; i < target.length; i++) {
					real[i] = df.format(target[i]);
				}
				if (!verbose)
					modelTrainingSituation(pred, real);
			}
			if (!verbose)
				System.out.println("error:" + re.eval("-min(testErrors)").asDouble());
		}
	}

	public void MLPOutput(Options op, boolean display) {
		boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ? true : false;
		String type = op.getValue("target.type");
		if (!op.isNull("outputPDF")) {
			if (!verbose&&display)
				System.out.println("pdf out...");
			re.eval("pdf(\"" + op.getValue("outputPDF") + "\")");
			if (type.equals("Factor")) {
				re.eval("result<-encodeClassLabels(result,method = \"WTA\")");
				re.eval("target<-encodeClassLabels(data.split$targetsTest,method = \"WTA\")");
				re.eval("plot(c(1:length(target)),as.factor(target),type=\"b\",lty=2,pch=16,col=\"red\",main=\"Predict vs. Reality\",xlab=\"Item Number\",ylab=\"Value\")");
				re.eval("lines(as.factor(result),type=\"b\",lty=2,pch=10,col=\"blue\")");
				re.eval("legend(\"topleft\",inset=.05,title=\"Lines example\",c(\"Reality\",\"Prediction\"),lty=c(1,2),pch=c(10,16),col=c(\"blue\",\"red\"))");
			} else {
				re.eval("opar<-par(no.readonly=T)");
				re.eval("layout(matrix(c(1,1,2,2),2,2,byrow=T),heights=c(4,3))");
				re.eval("plot(data.split$targetsTest,type=\"b\",lty=2,pch=16,col=\"red\",main=\"Predict vs. Reality\",xlab=\"Item Number\",ylab=\"Value\",cex=0.85)");
				re.eval("lines(result,type=\"b\",lty=1,pch=10,col=\"blue\",cex=0.85)");
				re.eval("legend(\"topleft\",inset=.05,title=\"Lines example\",c(\"Reality\",\"Prediction\"),lty=c(1,2),pch=c(15,17),col=c(\"blue\",\"red\"),cex=0.85)");
				re.eval("plot((result-data.split$targetsTest),type=\"b\",lty=2,pch=18,cex=0.85,col=\"red\",main=\"Error of Each Item\",xlab=\"Item Number\",ylab=\"Error\")");
				re.eval("par(opar)");
			}
			re.eval("dev.off()");
			if (!verbose&&display) System.out.println("pdf succeed.");
		}
			if (!op.isNull("testFile")) {
				re.eval("result <- predict(model,test.values)");
				if (type.equals("Factor")) {
					ArrayList<String> targetsLevels = targetsEncode("colnames(data.targets)");
					double[] result;
					if (op.getValue("encode.method").equals("402040")) {
						result = re.eval("pred<-encodeClassLabels(result,method = \"402040\", l = 0.4, h = 0.6)").asDoubleArray();
					} else {
						result = re.eval("pred<-encodeClassLabels(result,method = \"WTA\")").asDoubleArray();
					}
					String[] pred = new String[result.length];
					for (int i = 0; i < result.length; i++)
						pred[i] = (result[i] > 0) ? targetsLevels.get((int) result[i] - 1) : "NOT SURE";
					for (String string : pred) {
						System.out.println(string);
					}
					if (op.isNull("outputTXT")) {
						if (!verbose && display)
							for (int i = 0; i < pred.length; i++)
								System.out.println("NUM " + i + ":" + pred[i]);
					} else
						writePredict(pred, op, true);
				} else {
					double[] results = re.eval("result").asDoubleArray();
					if (type.equals("num")) {
						DecimalFormat df = new DecimalFormat("#.###");
						String[] pred = new String[results.length];
						for (int i = 0; i < results.length; i++)
							pred[i] = df.format(results[i]);
						if (op.isNull("outputTXT")) {
							if (!verbose && display)
								for (int i = 0; i < pred.length; i++)
									System.out.println("NUM " + i + ":" + pred[i]);
						} else
							writePredict(pred, op, true);
					} else if (type.equals("int")) {
						String[] pred = new String[results.length];
						for (int i = 0; i < results.length; i++)
							pred[i] = Long.toString(Math.round(results[i]));
						if (op.isNull("outputTXT")) {
							if (!verbose && display)
								for (int i = 0; i < pred.length; i++)
									System.out.println("NUM " + i + ":" + pred[i]);
						} else
							writePredict(pred, op, true);
					}
				}
			}
	}

//	public double MLP(Options op, boolean display) {
//		boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ? true : false;
//		double precise = 0;
//		re.eval("library(Rcpp)");
//		re.eval("library(RSNNS)");
//		re.eval("data<-na.omit(data)");
//		re.eval("data<-data[sample(nrow(data)),]");
//		String type = op.getValue("target.type");
//		// re.eval("data.values<-data[,c(\"" + op.getValue("values") + "\")]");
//		Structure str = str();
//		ArrayList<String> attrs = str.getAttributes();
//		ArrayList<String> discretizeAttr = new ArrayList<>();
//		for (String attr : attrs) {
//			if (str.getStructMap().get(attr).getT() == Type.FACTOR || str.getStructMap().get(attr).getT() == Type.ORD_FACTOR) {
//				discretizeAttr.add(attr);
//			}
//		}
//		if (discretizeAttr.contains(op.getValue("target"))) {
//			discretizeAttr.remove(op.getValue("target"));
//			re.eval("data.targets<-decodeClassLabels(data[,\"" + op.getValue("target") + "\"])");
//			// System.out.println("data.targets<-decodeClassLabels(data[,\"" +
//			// op.getValue("target") + "\"])");
//		} else
//			re.eval("data.targets<-data[,\"" + op.getValue("target") + "\"]");
//		if (discretizeAttr.size() > 0) {
//			if (!op.isNull("testFile")) {
//				re.eval("test<-read.table(\"" + op.getValue("testFile") + "\",header=" + op.getValue("header") + ",sep=\"" + op.getValue("sep") + "\",na.strings=c(\"" + op.getValue("na.strings") + "\"))");
//				re.eval("test <- na.omit(test)");
//				re.eval("temp<-rbind(data,test)");
//				// re.eval("test[,c(\"" + op.getValue("values") +
//				// "\")]<-normalizeData(test[,c(\"" + op.getValue("values") +
//				// "\")])");
//				// re.eval("result <- predict(model,test.values)");
//			} else
//				re.eval("temp<-data");
//			re.eval("temp.values<-temp[,c(\"" + op.getValue("values") + "\")]");
//			// System.out.println("temp.values<-temp[,c(\"" +
//			// op.getValue("values") + "\")]");
//			// re.eval("temp.target<-temp[,\"" + op.getValue("target") + "\"]");
//			for (String attr : discretizeAttr) {
//				re.eval("temp.values<-cbind(temp.values[,!names(temp.values)==\"" + attr + "\"],decodeClassLabels(temp.values[,\"" + attr + "\"]))");
//				// System.out.println("temp.values<-cbind(temp.values[,!names(temp.values)==\""+attr+"\"],decodeClassLabels(temp.values[,\""
//				// + attr + "\"]))");
//			}
//			if (!op.isNull("testFile")) {
//				re.eval("data.values<-temp.values[1:nrow(data),]");
//				// System.out.println("data.values<-temp.values[1:nrow(data),]");
//				re.eval("test.values<-temp.values[(nrow(data)+1):nrow(temp.values),]");
//				// System.out.println("test.values<-temp.values[norw(data)+1:nrow(temp.values),]");
//				re.eval("test.values<-normalizeData(test.values)");
//				// System.out.println("test.values<-normalizeData(test.values)");
//			} else
//				re.eval("data.values<-temp.values");
//		} else {
//			if (!op.isNull("testFile")) {
//				re.eval("test<-read.table(\"" + op.getValue("testFile") + "\",header=" + op.getValue("header") + ",sep=\"" + op.getValue("sep") + "\",na.strings=c(\"" + op.getValue("na.strings") + "\"))");
//				re.eval("test <- na.omit(test)");
//				re.eval("test.values<-test[,c(\"" + op.getValue("values") + "\")]");
//				re.eval("test.values<-normalizeData(test.values)");
//			}
//			re.eval("data.values<-data[,c(\"" + op.getValue("values") + "\")]");
//		}
//		// if (type.equals("Factor")) {
//		// re.eval("data.targets<-decodeClassLabels(data[,\"" +
//		// op.getValue("target") + "\"])");
//		// } else
//		// re.eval("data.targets<-data[,\"" + op.getValue("target") + "\"]");
//		re.eval("data.split<-splitForTrainingAndTest(data.values, data.targets, ratio=" + (1 - Double.parseDouble(op.getValue("ratio"))) + ")");
//		re.eval("data.split<- normTrainingAndTestSet(data.split)");
//		long min = Math.round(Math.sqrt((double) (re.eval("ncol(data.values)").asInt() + re.eval("ncol(data.targets)").asInt())) / 2);
//		re.eval("parameterGrid <- expand.grid(2*c(" + min + ":" + (min + 10) + "), c(0.00316, 0.0147, 0.1))");
//		// System.out.println("parameterGrid <- expand.grid(2*c(" + min + ":" +
//		// (min + 10) + "), c(0.00316, 0.0147, 0.1))");
//		re.eval("colnames(parameterGrid)<-c(\"nHidden\", \"learnRate\")");
//		// System.out.println("colnames(parameterGrid)<-c(\"nHidden\", \"learnRate\")");
//		re.eval("rownames(parameterGrid)<-paste(\"nnet-\", apply(parameterGrid, 1, function(x) {paste(x,sep=\"\", collapse=\"-\")}), sep=\"\")");
//		// System.out.println("rownames(parameterGrid)<-paste(\"nnet-\", apply(parameterGrid, 1, function(x) {paste(x,sep=\"\", collapse=\"-\")}), sep=\"\")");
//		if (!verbose && display)
//			System.out.println("modeling...");
//		re.eval("models<-apply(parameterGrid, 1, function(p) {" + "	mlp(data.split$inputsTrain, data.split$targetsTrain, size=p[1], learnFunc=\"Std_Backpropagation\"," + "	learnFuncParams=c(p[2], 0.1), maxit=" + op.getValue("maxit") + ", inputsTest=iris$inputsTest, " + "	targetsTest=iris$targetsTest, linOut=" + op.getValue("linOut") + ")" + "})");
//		// System.out.println("models<-apply(parameterGrid, 1, function(p) {" +
//		// "	mlp(data.split$inputsTrain, data.split$targetsTrain, size=p[1], learnFunc=\"Std_Backpropagation\","
//		// + "	learnFuncParams=c(p[2], 0.1), maxit=" + op.getValue("maxit") +
//		// ", inputsTest=iris$inputsTest, " +
//		// "	targetsTest=iris$targetsTest, linOut=" + op.getValue("linOut") +
//		// ")" + "})");
//		re.eval("testErrors <-  data.frame(lapply(models, function(mod) { " + "	pred  <-  predict(mod,data.split$inputsTest);" + "	error <-  sum((pred - data.split$targetsTest)^2);" + "	error " + "}))");
//		// System.out.println("testErrors <-  data.frame(lapply(models, function(mod) { "
//		// + "	pred  <-  predict(mod,data.split$inputsTest);" +
//		// "	error <-  sqrt(sum((pred - data.split$targetsTest)^2));" +
//		// "	error " + "}))");
//		re.eval("testErrors<-testErrors[!is.na(testErrors)]");
//		re.eval("model<-models[[which(min(testErrors) == testErrors)]]");
//		// System.out.println("testErrors:" + re.eval("testErrors"));
//		// System.out.println("which min:" +
//		// re.eval("which(min(testErrors) == testErrors)"));
//		System.out.println("hidden num:" + re.eval("model$archParams$size[[1]]").asDouble());
//		re.eval("result<-predict(model,data.split$inputsTest);");
//		// System.out.println("predict:"+re.eval("result"));
//		// System.out.println("real:"+re.eval("data.split$targetsTest"));
//		// System.out.println("error:"+re.eval("min(testErrors)"));
//		precise = re.eval("-min(testErrors)/length(result)").asDouble();
//		if (type.equals("Factor")) {
//			ArrayList<String> targetsLevels = targetsEncode("colnames(data.targets)");
//			double[] result;
//			int[] target;
//			if (op.getValue("encode.method").equals("402040")) {
//				result = re.eval("pred<-encodeClassLabels(result,method = \"402040\", l = 0.4, h = 0.6)").asDoubleArray();
//				target = re.eval("real<-encodeClassLabels(data.split$targetsTest,method = \"402040\", l = 0.4, h = 0.6)").asIntArray();
//			} else {
//				result = re.eval("pred<-encodeClassLabels(result,method = \"WTA\")").asDoubleArray();
//				target = re.eval("real<-encodeClassLabels(data.split$targetsTest,method = \"WTA\")").asIntArray();
//			}
//			String[] pred = new String[result.length];
//			for (int i = 0; i < result.length; i++)
//				pred[i] = (result[i] > 0) ? targetsLevels.get((int) result[i] - 1) : "NOT SURE";
//			String[] real = new String[target.length];
//			for (int i = 0; i < target.length; i++)
//				real[i] = (target[i] > 0) ? targetsLevels.get(target[i] - 1) : "NOT SURE";
//			int total = re.eval("length(pred)").asInt();
//			int right = re.eval("(sum(round(pred)==real))").asInt();
//			if (!verbose && display) {
//				modelTrainingSituation(pred, real);
//				System.out.println("total number:" + total + " right number:" + right + "   precise:" + right / (double) total);
//			}
//			precise = right / (double) total;
//		} else {
//			double[] results = re.eval("result").asDoubleArray();
//			DecimalFormat df = new DecimalFormat("#.###");
//			String[] pred = new String[results.length];
//			for (int i = 0; i < results.length; i++)
//				pred[i] = df.format(results[i]);
//			if (type.equals("int")) {
//				int[] target = re.eval("data.split$targetsTest").asIntArray();
//				String[] real = new String[target.length];
//				for (int i = 0; i < target.length; i++) {
//					real[i] = Integer.toString(target[i]);
//				}
//				String[] longPred = new String[results.length];
//				for (int i = 0; i < results.length; i++) {
//					longPred[i] = Long.toString(Math.round(results[i]));
//				}
//				if (!verbose && display) {
//					System.out.println("origin:");
//					modelTrainingSituation(pred, real);
//					System.out.println("after round:");
//					modelTrainingSituation(longPred, real);
//					int total = re.eval("length(result)").asInt();
//					int right = re.eval("(sum(round(result)==data.split$targetsTest))").asInt();
//					System.out.println("total number:" + total + " right number:" + right + "   precise:" + right / (double) total);
//				}
//			} else if (type.equals("num")) {
//				double[] target = re.eval("data.split$targetsTest").asDoubleArray();
//				String[] real = new String[target.length];
//				for (int i = 0; i < target.length; i++) {
//					real[i] = df.format(target[i]);
//				}
//				if (!verbose && display)
//					modelTrainingSituation(pred, real);
//			}
//			if (!verbose && display)
//				System.out.println("error:" + re.eval("-min(testErrors)/length(result)").asDouble());
//		}
//		if (display) {
//			if (!op.isNull("testFile")) {
//				// re.eval("test<-read.table(\"" + op.getValue("testFile") +
//				// "\",header=" + op.getValue("header") + ",sep=\"" +
//				// op.getValue("sep") + "\",na.strings=c(\"" +
//				// op.getValue("na.strings") + "\"))");
//				// re.eval("test <- na.omit(test)");
//				// re.eval("test[,c(\"" + op.getValue("values") +
//				// "\")]<-normalizeData(test[,c(\"" + op.getValue("values") +
//				// "\")])");
//				// re.eval("result <- predict(model,test[,c(\"" +
//				// op.getValue("values") + "\")])");
//				re.eval("result <- predict(model,test.values)");
//				if (type.equals("Factor")) {
//					ArrayList<String> targetsLevels = targetsEncode("colnames(data.targets)");
//					double[] result;
//					if (op.getValue("encode.method").equals("402040")) {
//						result = re.eval("pred<-encodeClassLabels(result,method = \"402040\", l = 0.4, h = 0.6)").asDoubleArray();
//					} else {
//						result = re.eval("pred<-encodeClassLabels(result,method = \"WTA\")").asDoubleArray();
//					}
//					String[] pred = new String[result.length];
//					for (int i = 0; i < result.length; i++)
//						pred[i] = (result[i] > 0) ? targetsLevels.get((int) result[i] - 1) : "NOT SURE";
//					if (op.isNull("outputTXT")) {
//						if (!verbose)
//							for (int i = 0; i < pred.length; i++)
//								System.out.println("NUM " + i + ":" + pred[i]);
//					} else
//						writePredict(pred, op, true);
//				} else {
//					double[] results = re.eval("result").asDoubleArray();
//					if (type.equals("num")) {
//						DecimalFormat df = new DecimalFormat("#.###");
//						String[] pred = new String[results.length];
//						for (int i = 0; i < results.length; i++)
//							pred[i] = df.format(results[i]);
//						if (op.isNull("outputTXT")) {
//							if (!verbose)
//								for (int i = 0; i < pred.length; i++)
//									System.out.println("NUM " + i + ":" + pred[i]);
//						} else
//							writePredict(pred, op, true);
//					} else if (type.equals("int")) {
//						String[] pred = new String[results.length];
//						for (int i = 0; i < results.length; i++)
//							pred[i] = Long.toString(Math.round(results[i]));
//						if (op.isNull("outputTXT")) {
//							if (!verbose)
//								for (int i = 0; i < pred.length; i++)
//									System.out.println("NUM " + i + ":" + pred[i]);
//						} else
//							writePredict(pred, op, true);
//					}
//				}
//			}
//		}
//		return precise;
//	}

	public void writePredict(String[] predict, Options op, boolean naOmit) {
		boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ? true : false;
		try {
			boolean flag = false;
			int line = 0;
			String temp = null;
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(op.getValue("outputTXT"))));
			BufferedReader reader = new BufferedReader(new FileReader(new File(op.getValue("testFile"))));
			if (!verbose)
				System.out.println("writing to file :" + op.getValue("outputTXT") + "  ...");
			if (op.getValue("header").matches("(T|t|true|TRUE)"))
				writer.append(reader.readLine() + op.getValue("sep") + "predict." + op.getValue("target") + "\n");
			temp = reader.readLine();
			if (naOmit) {
				String[] naStrings = op.getValue("na.strings").split("\",\"");
				while (temp != null && line <= predict.length) {
					flag = true;
					for (String string : naStrings) {
						if (temp.contains(string)) {
							// System.out.println(temp + " :contains->" +
							// string);
							flag = false;
							break;
						}
					}
					if (flag) writer.append(temp + op.getValue("sep") + predict[line++] + "\n");
					temp = reader.readLine();
				}
			} else {
				while (temp != null && line <= predict.length) {
					writer.append(temp + op.getValue("sep") + predict[line++] + "\n");
					temp = reader.readLine();
				}
			}
			if (temp != null || line < predict.length) {
				System.err.println("the test file's length does not match with the result's length ");
				System.out.println("origin file has left:");
				do {
					System.out.println(temp);
				} while ((temp = reader.readLine()) != null);
			}
			writer.close();
			reader.close();
			if (!verbose)
				System.out.println("write succeed.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Structure format(Map<String, Options> attributes) {
		return format(attributes, false, false);
	}

	public Structure format(Map<String, Options> attributes, boolean testFile) {
		return format(attributes, true, testFile);
	}

	// public Structure format(Map<String, Options> attributes,boolean replace)
	// {
	// return format(attributes, replace,"data");
	// }
	/**
	 * @param attributes
	 *            需要离散化的属性集，属性名为key，关于离散操作的各设置 保存到Options中
	 * @param replace
	 *            TRUE表示直接在原数据集上离散 FALSE表示离散结果不放回原数据集。
	 * 
	 * @return null
	 * */
	public Structure format(Map<String, Options> attributes, boolean replace, boolean testFile) {
		// op包含 strategy选用什么方法 method cluster等
		// Map<String, attrTypeNClass> result=new HashMap<>();
		Set<String> attrs = attributes.keySet();
		// t.clearText();
		// String log = "";
		re.eval("temp<-data");
		for (String string : attrs) {
			// System.out.println("format :"+string);
			Options op = attributes.get(string);
			int values;
			try {
				values = re.eval("length(unique(data[,\"" + string + "\"]))").asInt();
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("no such attribute:" + string);
				continue;
			}
			// System.out.println(string+":  method="+op.getValue("method")+"   categories="+op.getValue("categories")+"  values="+values+"   format="+!op.isNull("format"));
			if (replace) {
				if (op.isNull("format")) {
					if (op.getValue("method").matches("(cluster|frequency)")) {
						int categories = Integer.parseInt(op.getValue("categories"));
						if (categories < values) {
							if (testFile)
								re.eval("test[,\"" + string + "\"]<-discretize(test[,\"" + string + "\"],method=\"" + op.getValue("method") + "\",categories=" + categories + ")");
							re.eval("data[,\"" + string + "\"]<-discretize(data[,\"" + string + "\"],method=\"" + op.getValue("method") + "\",categories=" + categories + ")");
						} else {
							if (testFile)
								re.eval("test[,\"" + string + "\"]<-as.factor(test[,\"" + string + "\"])");
							re.eval("data[,\"" + string + "\"]<-as.factor(data[,\"" + string + "\"])");
						}
					} else if (op.getValue("method").equals("fixed")) {
						if (testFile)
							re.eval("test[,\"" + string + "\"]<-discretize(test[,\"" + string + "\"],method=\"" + op.getValue("method") + "\",categories=" + op.getValue("categories") + ")");
						re.eval("data[,\"" + string + "\"]<-discretize(data[,\"" + string + "\"],method=\"" + op.getValue("method") + "\",categories=" + op.getValue("categories") + ")");
					} else
						System.err.println("undefined method");
				} else {
					String option = op.getValue("format");
					String[] temp = option.split("<");
					if (temp.length > 1) {
						String levels = "";
						for (String string2 : temp) {
							levels += "\"" + string2 + "\",";
						}
						levels = levels.substring(0, levels.length() - 1);
						if (testFile)
							re.eval("test[,\"" + string + "\"]<-factor(test[,\"" + string + "\"],order=TRUE,levels=c(" + levels + "))");
						re.eval("data[,\"" + string + "\"]<-factor(data[,\"" + string + "\"],order=TRUE,levels=c(" + levels + "))");
					} else {
						switch (option) {
						case "factor":
							if (testFile)
								re.eval("test[,\"" + string + "\"]<-as.factor(test[,\"" + string + "\"])");
							re.eval("data[,\"" + string + "\"]<-as.factor(data[,\"" + string + "\"])");
							break;
						case "int":
							if (testFile)
								re.eval("test[,\"" + string + "\"]<-as.integer(as.character(test[,\"" + string + "\"]))");
							re.eval("data[,\"" + string + "\"]<-as.integer(as.character(data[,\"" + string + "\"]))");
							break;
						case "num":
							if (testFile)
								re.eval("test[,\"" + string + "\"]<-as.numeric(as.character(test[,\"" + string + "\"]))");
							re.eval("data[,\"" + string + "\"]<-as.numeric(as.character(data[,\"" + string + "\"]))");
							break;
						default:
							System.err.println("ranalysis format():no such format");
							break;
						}
					}
				}
			} else {
				if (op.isNull("format")) {
					if (op.getValue("method").matches("(cluster|frequency)")) {
						int categories = Integer.parseInt(op.getValue("categories"));
						if (categories < values) {
							re.eval("temp[,\"" + string + "\"]<-discretize(temp[,\"" + string + "\"],method=\"" + op.getValue("method") + "\",categories=" + categories + ")");
						} else {
							re.eval("temp[,\"" + string + "\"]<-as.factor(temp[,\"" + string + "\"])");
						}
					} else if (op.getValue("method").equals("fixed")) {
						re.eval("temp[,\"" + string + "\"]<-discretize(temp[,\"" + string + "\"],method=\"" + op.getValue("method") + "\",categories=" + op.getValue("categories") + ")");
					} else
						System.err.println("undefined method");
				} else {
					String option = op.getValue("format");
					String[] temp = option.split("<");
					if (temp.length > 1) {
						String levels = "";
						for (String string2 : temp) {
							levels += "\"" + string2 + "\",";
						}
						levels = levels.substring(0, levels.length() - 1);
						re.eval("temp[,\"" + string + "\"]<-factor(temp[,\"" + string + "\"],order=TRUE,levels=c(" + levels + "))");
					} else {
						switch (option) {
						case "factor":
							re.eval("temp[,\"" + string + "\"]<-as.factor(temp[,\"" + string + "\"])");
							break;
						case "int":
							re.eval("temp[,\"" + string + "\"]<-as.integer(as.character(temp[,\"" + string + "\"]))");
							break;
						case "num":
							re.eval("temp[,\"" + string + "\"]<-as.numeric(as.character(temp[,\"" + string + "\"]))");
							break;
						default:
							System.err.println("ranalysis format():no such format");
							break;
						}
					}
				}
			}
		}
		if (replace)
			return str();
		else
			return str("temp");
	}

	public void associateAnalysis(Options op, boolean output) {
		try {
			re.eval("data.associateRules<-apriori(data,parameter=list(minlen=2,support=" + op.getValue("support") + ",confidence=" + op.getValue("confidence") + ",maxlen=" + op.getValue("maxlen") + "))");
			if (output && !op.isNull("outputTXT")) {
				// System.out.println("write(data.associateRules,file=\""+op.getValue("outputFile")+"\",format=\""+op.getValue("format")+"\",sep=\""+op.getValue("outputSep")+"\")");
				System.out.println("writing to file : " + op.getValue("outputTXT"));
				re.eval("sink(\"" + op.getValue("outputTXT") + "\",split=T)");
				t.clearText();
				re.eval("inspect(data.associateRules)");
				System.out.println(t.getText());
				re.eval("sink()");
				System.out.println("write finished.");
			} else{
				t.clearText();
				re.eval("inspect(data.associateRules)");
				System.out.println(t.getText());
			}
		} catch (Exception e) {
			System.out.println("EX:" + e);
		}
	}
	public void cluster(Options op,boolean output){
		try {
			re.eval("cluster.num<-"+op.getValue("cluster"));
			re.eval("cluster.classes<-lapply(data,class)");
			re.eval("cluster.temp<-data");
			re.eval("cluster.decodeAttrs<-names(cluster.classes[cluster.classes %in% c(\"factor\",\"ordered\")])");
			re.eval("if(length(cluster.decodeAttrs)>1) {cluster.temp[,cluster.decodeAttrs]<-lapply(cluster.temp[,cluster.decodeAttrs],decodeClassLabels)}else if(length(cluster.decodeAttrs)==1){cluster.temp[,cluster.decodeAttrs]<-decodeClassLabels(cluster.temp[,cluster.decodeAttrs])}");
			re.eval("cluster.result<-cmeans(cluster.temp,cluster.num)");
			
//			int cluster=Integer.valueOf(op.getValue("cluster"));
//			for(int i=1;i<=cluster;i++){
//				RVector vec=re.eval("data[result$cluster=="+i+",]").asVector();
//				Vector<Object> names=vec.getNames();
//				for (Object object : names) {
//					System.out.println(object);
//				}
//			}
			if (output && !op.isNull("outputTXT")) {
				System.out.println("write to file:"+op.getValue("outputTXT"));
				re.eval("sink(\"" + op.getValue("outputTXT") + "\",split=T)");
			}
			t.setDisplay(true);
			re.eval("for(i in 1:cluster.num){cat(\"cluster\",i,\":\n\");print(data[cluster.result$cluster==i,][order(as.integer(row.names(data[cluster.result$cluster==i,]))),])}");
			if (output && !op.isNull("outputTXT")) re.eval("sink()");
			re.eval("for(i in 1:cluster.num){cat(\"cluster\",i,\" size:\",cluster.result$size[i],\"\n\")}");
			t.setDisplay(false);
			re.eval("sink()");
//			if (output && !op.isNull("outputTXT")) {
//				System.out.println("write succeed.");
//				re.eval("sink()");
//			}
		} catch (Exception e) {
			System.out.println("ranalysis.cluster():"+e);
		}
	}
	public void sepTrainNTest(Options op) {
		re.eval("set.seed(" + Integer.toString((int) System.nanoTime()) + ")");
		re.eval("ind<-sample(2,nrow(data),replace=TRUE,prob=c(" + op.getValue("ratio") + "," + (1 - Double.parseDouble(op.getValue("ratio"))) + "));");
		re.eval("data.train<-data[ind==1,]");
		re.eval("data.test<-data[ind==2,]");
	}

	// public double decisionTree(Options op, boolean output){
	// return decisionTree(op, output,new HashMap<String, Options>());
	// }
	public double decisionTreeBuild(Options op) {
		// boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ?
		// true : false;
		double precise = 0;
		String type = op.getValue("target.type");
		// if(!verbose) System.out.println("tree esatablishing.");
		re.eval("data.tree<-rpart(" + op.getValue("expression") + ",data=data.train,method=\"" + op.getValue("method") + "\",control=rpart.control(minsplit=" + op.getValue("minsplit") + ",minbucket=" + op.getValue("minbucket") + ",maxdepth=" + op.getValue("maxdepth") + ",cp=" + op.getValue("cp") + "))");
		//System.out.println("data.tree<-rpart(" + op.getValue("expression") + ",data=data.train,method=\"" + op.getValue("method") + "\",control=rpart.control(minsplit=" + op.getValue("minsplit") + ",minbucket=" + op.getValue("minbucket") + ",maxdepth=" + op.getValue("maxdepth") + ",cp=" + op.getValue("cp") + "))");
		//System.out.println(re.eval("data.tree"));
		// if(!verbose) System.out.println("predicting...");
		re.eval("result<-predict(data.tree,newdata=data.test,type=\"" + op.getValue("predict.type") + "\")");
		// if(!verbose) System.out.println("predict finish");
		if (type.matches("(Factor|Ord.factor)")) {
			precise = re.eval("(sum(result==data.test[,\"" + op.getValue("target") + "\"]))/length(result)").asDouble();
		} else if (type.equals("num")) {
			re.eval("dist<-(result-data.test[,\"" + op.getValue("target") + "\"])");
			re.eval("dist<-dist[!is.na(dist)]");
			precise = re.eval("-sqrt(sum(dist^2)/length(result))").asDouble();
			//precise = re.eval("-sqrt(sum((result-data.test[,\"" + op.getValue("target") + "\"])^2)/length(result))").asDouble();
			// System.out.println("here  result:"+result);
			// System.out.println("error sum"+re.eval("sum((result-data.test[,5])^2)"));
		} else if (type.equals("int")) {
			re.eval("dist<-(result-data.test[,\"" + op.getValue("target") + "\"])");
			re.eval("dist<-dist[!is.na(dist)]");
			precise = re.eval("-sqrt(sum(dist^2)/length(result))").asDouble();
			//precise = re.eval("(sum(round(result)==data.test[,\"" + op.getValue("target") + "\"]))/length(result)").asDouble();
			// System.out.println(precise);
		} else
			System.err.println("ranalysis.decisioTree(): no such kind type:" + type);
		return precise;
	}

	public void decisionTreedisplay(Options op) {
		boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ? true : false;
		double precise = 0;
		String type = op.getValue("target.type");
		if (type.matches("(Factor|Ord.factor)")) {
			RFactor results = re.eval("result").asFactor();
			RFactor target = re.eval("data.test[,\"" + op.getValue("target") + "\"]").asFactor();
			String[] pred = new String[results.size()];
			String[] real = new String[target.size()];
			for (int i = 0; i < results.size(); i++) {
				pred[i] = results.at(i);
			}
			for (int i = 0; i < target.size(); i++) {
				real[i] = target.at(i);
			}
			int total = re.eval("length(result)").asInt();
			int right = re.eval("(sum(result==data.test[,\"" + op.getValue("target") + "\"]))").asInt();
			precise = right / (double) total;
			if (!verbose) {
				modelTrainingSituation(pred, real);
				System.out.println("total number:" + total + " right number:" + right + "   precise:" + right / (double) total);
			}
		} else {
			double[] results = re.eval("result").asDoubleArray();
			DecimalFormat df = new DecimalFormat("#.###");
			String[] pred = new String[results.length];
			for (int i = 0; i < results.length; i++)
				pred[i] = df.format(results[i]);
			if (type.equals("int")) {
				int[] target = re.eval("data.test[,\"" + op.getValue("target") + "\"]").asIntArray();
				String[] real = new String[target.length];
				for (int i = 0; i < target.length; i++) {
					real[i] = Integer.toString(target[i]);
				}
				String[] longPred = new String[results.length];
				for (int i = 0; i < results.length; i++) {
					longPred[i] = Long.toString(Math.round(results[i]));
				}
				if (!verbose) {
					System.out.println("origin:");
					modelTrainingSituation(pred, real);
					System.out.println("after round:");
					modelTrainingSituation(longPred, real);
					int total = re.eval("length(result)").asInt();
					int right = re.eval("(sum(round(result)==data.test[,\"" + op.getValue("target") + "\"]))").asInt();
					System.out.println("total number:" + total + " right number:" + right + "   precise:" + right / (double) total);
				}
			} else if (type.equals("num")) {
				double[] target = re.eval("data.test[,\"" + op.getValue("target") + "\"]").asDoubleArray();
				String[] real = new String[target.length];
				for (int i = 0; i < target.length; i++) {
					real[i] = df.format(target[i]);
				}
				if (!verbose)
					modelTrainingSituation(pred, real);
			}
			precise = re.eval("-sqrt(sum((result-data.test[,\"" + op.getValue("target") + "\"])^2)/length(result))").asDouble();
			if (!verbose)
				System.out.println("error:" + precise);
		}
	}

	public void decisionTreeOutput(Options op, boolean display) {
		boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ? true : false;
		double precise = 0;
		String type = op.getValue("target.type");
		if (!op.isNull("outputPDF")) {
			if (!verbose)
				System.out.println("writing to pdf :" + op.getValue("outputPDF"));
			re.eval("pdf(\"" + op.getValue("outputPDF") + "\")");
			re.eval("rpart.plot(data.tree)");
			re.eval("dev.off()");
			if (!verbose)
				System.out.println("pdf succeed.");
		}
		if (!op.isNull("testFile")) {
			// format(formatOptions,true,"test");
			re.eval("result <- predict(data.tree,newdata=test[,c(\"" + op.getValue("values") + "\")],type=\"" + op.getValue("predict.type") + "\")");
			if (type.matches("(Factor|Ord.factor)")) {
				RFactor results = re.eval("result").asFactor();
				String[] pred = new String[results.size()];
				for (int i = 0; i < results.size(); i++) {
					pred[i] = results.at(i);
				}
				if (op.isNull("outputTXT")) {
					if (!verbose && display)
						for (int i = 0; i < pred.length; i++)
							System.out.println("NUM " + i + ":" + pred[i]);
				} else
					writePredict(pred, op, false);
			} else {
				double[] results = re.eval("result").asDoubleArray();
				if (type.equals("num")) {
					DecimalFormat df = new DecimalFormat("#.###");
					String[] pred = new String[results.length];
					for (int i = 0; i < results.length; i++)
						pred[i] = df.format(results[i]);
					if (op.isNull("outputTXT")) {
						if (!verbose && display)
							for (int i = 0; i < pred.length; i++)
								System.out.println("NUM " + i + ":" + pred[i]);
					} else
						writePredict(pred, op, false);
				} else if (type.equals("int")) {
					String[] pred = new String[results.length];
					for (int i = 0; i < results.length; i++) {
						pred[i] = Long.toString(Math.round(results[i]));
					}
					if (op.isNull("outputTXT")) {
						if (!verbose && display)
							for (int i = 0; i < pred.length; i++)
								System.out.println("NUM " + i + ":" + pred[i]);
					} else
						writePredict(pred, op, false);
				}
			}
			if (op.isNull("outputTXT")) {
				if (!verbose && display)
					re.eval("asRules(data.tree)");
			} else {
				re.eval("sink(file=\"" + op.getValue("outputTXT") + "\",append=T)");
				re.eval("asRules(data.tree)");
				re.eval("sink()");
			}
		} else if (!op.isNull("outputTXT")) {
			re.eval("sink(file=\"" + op.getValue("outputTXT") + "\")");
			re.eval("asRules(data.tree)");
			re.eval("sink()");
		}
	}

	// public double decisionTree(Options op, boolean output,Map<String,
	// Options> formatOptions) {
	// boolean verbose = op.getValue("verbose").matches("(T|TRUE|t|true)") ?
	// true : false;
	// double precise = 0;
	// String type = op.getValue("target.type");
	// if(!verbose&&output) System.out.println("tree esatablishing.");
	// re.eval("data.tree<-rpart(" + op.getValue("expression") +
	// ",data=data.train,method=\"" + op.getValue("method") +
	// "\",control=rpart.control(minsplit=" + op.getValue("minsplit") +
	// ",minbucket=" + op.getValue("minbucket") + ",maxdepth=" +
	// op.getValue("maxdepth") + ",cp=" + op.getValue("cp") + "))");
	// if(!verbose&&output) System.out.println("predicting...");
	// re.eval("result<-predict(data.tree,newdata=data.test,type=\"" +
	// op.getValue("predict.type") + "\")");
	// if(!verbose&&output) System.out.println("predict finish");
	// if (output) {
	// if (type.matches("(Factor|Ord.factor)")) {
	// RFactor results = re.eval("result").asFactor();
	// RFactor target = re.eval("data.test[,\"" + op.getValue("target") +
	// "\"]").asFactor();
	// String[] pred = new String[results.size()];
	// String[] real = new String[target.size()];
	// for (int i = 0; i < results.size(); i++) {
	// pred[i] = results.at(i);
	// }
	// for (int i = 0; i < target.size(); i++) {
	// real[i] = target.at(i);
	// }
	// int total = re.eval("length(result)").asInt();
	// int right = re.eval("(sum(result==data.test[,\"" + op.getValue("target")
	// + "\"]))").asInt();
	// precise = right / (double) total;
	// if (!verbose) {
	// modelTrainingSituation(pred, real);
	// System.out.println("total number:" + total + " right number:" + right +
	// "   precise:" + right / (double) total);
	// }
	// } else {
	// double[] results = re.eval("result").asDoubleArray();
	// DecimalFormat df = new DecimalFormat("#.###");
	// String[] pred = new String[results.length];
	// for (int i = 0; i < results.length; i++)
	// pred[i] = df.format(results[i]);
	// if (type.equals("int")) {
	// int[] target = re.eval("data.test[,\"" + op.getValue("target") +
	// "\"]").asIntArray();
	// String[] real = new String[target.length];
	// for (int i = 0; i < target.length; i++) {
	// real[i] = Integer.toString(target[i]);
	// }
	// String[] longPred = new String[results.length];
	// for (int i = 0; i < results.length; i++) {
	// longPred[i] = Long.toString(Math.round(results[i]));
	// }
	// if (!verbose) {
	// System.out.println("origin:");
	// modelTrainingSituation(pred, real);
	// System.out.println("after round:");
	// modelTrainingSituation(longPred, real);
	// int total = re.eval("length(result)").asInt();
	// int right = re.eval("(sum(round(result)==data.test[,\"" +
	// op.getValue("target") + "\"]))").asInt();
	// System.out.println("total number:" + total + " right number:" + right +
	// "   precise:" + right / (double) total);
	// }
	// } else if (type.equals("num")) {
	// double[] target = re.eval("data.test[,\"" + op.getValue("target") +
	// "\"]").asDoubleArray();
	// String[] real = new String[target.length];
	// for (int i = 0; i < target.length; i++) {
	// real[i] = df.format(target[i]);
	// }
	// if (!verbose)
	// modelTrainingSituation(pred, real);
	// }
	// precise = re.eval("-sqrt(sum((result-data.test[,\"" +
	// op.getValue("target") + "\"])^2))/length(result)").asDouble();
	// if (!verbose)
	// System.out.println("error:" + precise);
	// }
	// if (!op.isNull("outputPDF")) {
	// if (!verbose)
	// System.out.println("writing to pdf :" + op.getValue("outputPDF"));
	// re.eval("pdf(\"" + op.getValue("outputPDF") + "\")");
	// re.eval("rpart.plot(data.tree)");
	// re.eval("dev.off()");
	// if (!verbose)
	// System.out.println("pdf succeed.");
	// }
	// if (!op.isNull("testFile")) {
	// //re.eval("test<-read.table(\"" + op.getValue("testFile") + "\",header="
	// + op.getValue("header") + ",sep=\"" + op.getValue("sep") +
	// "\",na.strings=c(\"" + op.getValue("na.strings") + "\"))");
	// //format(formatOptions,true,"test");
	// re.eval("result <- predict(data.tree,newdata=test[,c(\"" +
	// op.getValue("values") + "\")],type=\"" + op.getValue("predict.type") +
	// "\")");
	// if (type.matches("(Factor|Ord.factor)")) {
	// RFactor results = re.eval("result").asFactor();
	// String[] pred = new String[results.size()];
	// for (int i = 0; i < results.size(); i++) {
	// pred[i] = results.at(i);
	// }
	// if (op.isNull("outputTXT")) {
	// if (!verbose)
	// for (int i = 0; i < pred.length; i++)
	// System.out.println("NUM " + i + ":" + pred[i]);
	// } else
	// writePredict(pred, op, false);
	// } else {
	// double[] results = re.eval("result").asDoubleArray();
	// if (type.equals("num")) {
	// DecimalFormat df = new DecimalFormat("#.###");
	// String[] pred = new String[results.length];
	// for (int i = 0; i < results.length; i++)
	// pred[i] = df.format(results[i]);
	// if (op.isNull("outputTXT")) {
	// if (!verbose)
	// for (int i = 0; i < pred.length; i++)
	// System.out.println("NUM " + i + ":" + pred[i]);
	// } else
	// writePredict(pred, op, false);
	// } else if (type.equals("int")) {
	// String[] pred = new String[results.length];
	// for (int i = 0; i < results.length; i++) {
	// pred[i] = Long.toString(Math.round(results[i]));
	// }
	// if (op.isNull("outputTXT")) {
	// if (!verbose)
	// for (int i = 0; i < pred.length; i++)
	// System.out.println("NUM " + i + ":" + pred[i]);
	// } else
	// writePredict(pred, op, false);
	// }
	// }
	// if (op.isNull("outputTXT")) {
	// if (!verbose)
	// re.eval("asRules(data.tree)");
	// } else {
	// re.eval("sink(file=\"" + op.getValue("outputTXT") + "\",append=T)");
	// re.eval("asRules(data.tree)");
	// re.eval("sink()");
	// }
	// } else if (!op.isNull("outputTXT")) {
	// re.eval("sink(file=\"" + op.getValue("outputTXT") + "\")");
	// re.eval("asRules(data.tree)");
	// re.eval("sink()");
	// }
	// } else {
	// if (type.matches("(Factor|Ord.factor)")) {
	// precise = re.eval("(sum(result==data.test[,\"" + op.getValue("target") +
	// "\"]))/length(result)").asDouble();
	// } else if (type.equals("num")) {
	// precise = re.eval("-sqrt(sum((result-data.test[,\"" +
	// op.getValue("target") + "\"])^2))/length(result)").asDouble();
	// // System.out.println("here  result:"+result);
	// //
	// System.out.println("error sum"+re.eval("sum((result-data.test[,5])^2)"));
	// } else if (type.equals("int")) {
	// precise = re.eval("-sqrt(sum((result-data.test[,\"" +
	// op.getValue("target") + "\"])^2))/length(result)").asDouble();
	// // System.out.println(precise);
	// } else
	// System.err.println("ranalysis.decisioTree(): no such kind type:" + type);
	// }
	// // System.out.println(result);
	// return precise;
	// }

	public void RST() {
		String addr = t.rChooseFile(re, 0);
		addr = addr.replace('\\', '/');
		// System.out.println(addr+"   ===>analysis");
		try {
			System.out.println("1:" + re.eval("da<-read.table(\"" + addr + "\",header=TRUE,sep=\",\")"));
			System.out.println("2:" + re.eval("decision.table<-SF.asDecisionTable(dataset=da,decision.attr=8,indx.nominal=c(1:8))"));
		} catch (Exception e) {
			System.out.println("EX:" + e);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// just making sure we have the right version of everything
		if (!Rengine.versionCheck()) {
			System.err.println("** Version mismatch - Java files don't match library version.");
			System.exit(1);
		}
		System.out.println("Creating Rengine (with arguments)");
		// 1) we pass the arguments from the command line
		// 2) we won't use the main loop at first, we'll start it later
		// (that's the "false" as second argument)
		// 3) the callbacks are implemented by the TextConsole class above
		// TextConsole t=new TextConsole();
		// Rengine re=new Rengine(args, false, t);
		// System.out.println("Rengine created, waiting for R");
		// the engine creates R is a new thread, so we should wait until it's
		// ready
		// if (!re.waitForR()) {
		// System.out.println("Cannot load R");
		// return;
		// }

		/*
		 * High-level API - do not use RNI methods unless there is no other way
		 * to accomplish what you want
		 */
		/**
		 * // Part 2 - low-level API - for illustration purposes only!
		 * //System.exit(0);
		 * 
		 * // simple assignment like a<-"hello" (env=0 means use R_GlobalEnv)
		 * long xp1 = re.rniPutString("hello"); re.rniAssign("a", xp1, 0);
		 * 
		 * // Example: how to create a named list or data.frame double da[] =
		 * {1.2, 2.3, 4.5}; double db[] = {1.4, 2.6, 4.2}; long xp3 =
		 * re.rniPutDoubleArray(da); long xp4 = re.rniPutDoubleArray(db);
		 * 
		 * // now build a list (generic vector is how that's called in R) long
		 * la[] = {xp3, xp4}; long xp5 = re.rniPutVector(la);
		 * 
		 * // now let's add names String sa[] = {"a","b"}; long xp2 =
		 * re.rniPutStringArray(sa); re.rniSetAttr(xp5, "names", xp2);
		 * 
		 * // ok, we have a proper list now // we could use assign and then eval
		 * "b<-data.frame(b)", but for now let's build it by hand: String rn[] =
		 * {"1", "2", "3"}; long xp7 = re.rniPutStringArray(rn);
		 * re.rniSetAttr(xp5, "row.names", xp7);
		 * 
		 * long xp6 = re.rniPutString("data.frame"); re.rniSetAttr(xp5, "class",
		 * xp6);
		 * 
		 * // assign the whole thing to the "b" variable re.rniAssign("b", xp5,
		 * 0);
		 * 
		 * { System.out.println("Parsing"); long e=re.rniParse("data(iris)", 1);
		 * System.out.println("Result = "+e+", running eval"); long
		 * r=re.rniEval(e, 0);
		 * System.out.println("Result = "+r+", building REXP"); REXP x=new
		 * REXP(re, r); System.out.println("REXP result = "+x); } {
		 * System.out.println("Parsing"); long e=re.rniParse("iris", 1);
		 * System.out.println("Result = "+e+", running eval"); long
		 * r=re.rniEval(e, 0);
		 * System.out.println("Result = "+r+", building REXP"); REXP x=new
		 * REXP(re, r); System.out.println("REXP result = "+x); } {
		 * System.out.println("Parsing"); long e=re.rniParse("names(iris)", 1);
		 * System.out.println("Result = "+e+", running eval"); long
		 * r=re.rniEval(e, 0);
		 * System.out.println("Result = "+r+", building REXP"); REXP x=new
		 * REXP(re, r); System.out.println("REXP result = "+x); String
		 * s[]=x.asStringArray(); if (s!=null) { int i=0; while (i<s.length) {
		 * System.out.println("["+i+"] \""+s[i]+"\""); i++; } } } {
		 * System.out.println("Parsing"); long e=re.rniParse("rnorm(10)", 1);
		 * System.out.println("Result = "+e+", running eval"); long
		 * r=re.rniEval(e, 0);
		 * System.out.println("Result = "+r+", building REXP"); REXP x=new
		 * REXP(re, r); System.out.println("REXP result = "+x); double
		 * d[]=x.asDoubleArray(); if (d!=null) { int i=0; while (i<d.length) {
		 * System.out.print(((i==0)?"":", ")+d[i]); i++; }
		 * System.out.println(""); } System.out.println(""); } { REXP
		 * x=re.eval("1:10"); System.out.println("REXP result = "+x); int
		 * d[]=x.asIntArray(); if (d!=null) { int i=0; while (i<d.length) {
		 * System.out.print(((i==0)?"":", ")+d[i]); i++; }
		 * System.out.println(""); } }
		 * 
		 * re.eval("print(1:10/3)");
		 */
		/**
		 * if (true) { // so far we used R as a computational slave without REPL
		 * // now we start the loop, so the user can use the console
		 * System.out.println("Now the console is yours ... have fun");
		 * re.startMainLoop(); System.out.println("exit..."); } else { re.end();
		 * System.out.println("end"); }
		 */
	}

}
