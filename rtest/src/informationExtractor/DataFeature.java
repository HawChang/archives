package informationExtractor;

import java.util.HashMap;
import java.util.Map;

import implement.Param;


public class DataFeature {
	Structure dataStructure;
	Map<String,Param> params;
	Options options;
	double prop_of_symbolic;
	public double getProp_of_symbolic() {
		return prop_of_symbolic;
	}
	public int getTotalAttrNum() {
		return totalAttrNum;
	}
	public String getTargetType() {
		return targetType;
	}
	public int getTargetValuesNum() {
		return targetValuesNum;
	}
	public int getObsNum() {
		return obsNum;
	}
	public double getNaRatio() {
		return naRatio;
	}
	public double getEntropy() {
		return entropy;
	}
	public double getMultiInformation() {
		return multiInformation;
	}
	public double getMutInformation() {
		return mutInformation;
	}
	int totalAttrNum;
	String targetType;
	int targetValuesNum;
	int obsNum;
	double naRatio;
	double entropy;
	double multiInformation;
	double mutInformation;
	public String dataFeatureToString(){
		return prop_of_symbolic+","+totalAttrNum+","+targetType+","+targetValuesNum+","+obsNum+","+naRatio+","+entropy+","+multiInformation+","+mutInformation;
	}
	public DataFeature(double prop,int totalAttrNum,String type,int valuesNum,int obsNum,double na,double entropy,double multiInformation,double mutInformation){
		this();
		prop_of_symbolic=prop;
		targetType=type;
		targetValuesNum=valuesNum;
		this.obsNum=obsNum;
		naRatio=na;
		this.entropy=entropy;
		this.multiInformation=multiInformation;
		this.mutInformation=mutInformation;
		this.totalAttrNum=totalAttrNum;
	}
	public DataFeature(){
		this(new Structure(),new HashMap<String, Param>(),new Options());
	}
	public DataFeature(Structure s){
		this(s,new HashMap<String, Param>(),new Options());
	}
	public DataFeature(Structure s,Options d){
		this(s,new HashMap<String, Param>(),d);
	}
	public DataFeature(Structure s,Map<String, Param> p){
		this(s,p,new Options());
	}
	public DataFeature(Structure s,Map<String, Param> p,Options d){
		dataStructure=s;
		params=p;
		options=d;
		prop_of_symbolic=-1;
		targetType="";
		targetValuesNum=0;
		obsNum=0;
		naRatio=0;
		entropy=0;
		multiInformation=0;
		mutInformation=0;
		totalAttrNum=0;
		//factorAttr=new ArrayList<>();
		//numAttr=new ArrayList<>();
		//intAttr=new ArrayList<>();
		//添加默认参数
		
		//options.addPair("", "");
	}
	public void setDataFeature(DataFeature d){
		if(d.params.size()!=0) params=d.params;
		if(d.options.getAttributes().size()!=0) options=d.options;
		if(!d.dataStructure.getStructMap().isEmpty()) dataStructure=d.dataStructure;
		this.prop_of_symbolic=d.prop_of_symbolic;
		this.targetType=d.targetType;
		this.targetValuesNum=d.targetValuesNum;
		this.obsNum=d.obsNum;
		this.naRatio=d.naRatio;
		this.entropy=d.entropy;
		this.multiInformation=d.multiInformation;
		this.mutInformation=d.mutInformation;
		this.totalAttrNum=d.totalAttrNum;
	}
	public Structure getDataStructure() {
		return dataStructure;
	}
	public void setDataStructure(Structure dataStructure) {
		this.dataStructure = dataStructure;
	}
	public Map<String, Param> getParams() {
		return params;
	}
	public void setParams(Map<String, Param> params) {
		this.params = params;
	}
	public Options getOptions() {
		return options;
	}
	public void setOptions(Options dataInformation) {
		options = dataInformation;
	}
}
