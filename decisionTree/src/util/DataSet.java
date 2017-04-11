package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DataSet {
	private ArrayList<Record> records;
	private ArrayList<String> attributeNames;
	private int attrNum;
	private int targetID;
	private String NAstring;
	public String getNAstring() {
		return NAstring;
	}
	private ArrayList<Integer> conditionsID;
	public enum TYPE{ continuous , decritize};
	private HashMap<Integer,TYPE> attrType;
	public HashMap<Integer,TYPE> getAttrType() {
		return attrType;
	}
//	public void setAttrType(ArrayList<TYPE> attrType) {
//		this.attrType = attrType;
//	}
	//	public DataSet(){
//		s=new ArrayList<>();
//	}
	public boolean setTarNAttr(String tar, String attr){
		if(!attributeNames.contains(tar)){
			System.err.println("target value does not exist.");
			return false;
		}
//		if(tar==null) target=attributeNames.get(attributeNames.size()-1);
//		else target=tar;
//		targetID=attributeNames.indexOf(target);
		if(tar==null) targetID=attrNum-1;
		else targetID=attributeNames.indexOf(tar);
		if(attr==null) {
			//conditionsName=new ArrayList<>(attributeNames);
			//conditionsName.remove(target);
			conditionsID=new ArrayList<>();
			//int pos=attributeNames.indexOf(target);
			for (int i = 0; i < attrNum; i++) {
				if(i!=targetID) conditionsID.add(i);
			}
		}
		else {
			//conditionsName= new ArrayList<>(Arrays.asList(attr.split("\\+")));
			conditionsID=new ArrayList<>();
			for (String string : attr.split("\\+")) {
				conditionsID.add(attributeNames.indexOf(string));
				if(!attributeNames.contains(string)){
					System.err.println("wrong condition attrs.");
					return false;
				}
				if(string.equals(attributeNames.get(targetID))){
					System.err.println("condition attr is equal to target.");
					return false;
				}
			}
		}
		return true;
	}
	public void display(){
		System.out.println("attrNums:"+attrNum);
		System.out.print("attributesNames:");
		for (int i = 0; i < attributeNames.size(); i++) {
			System.out.print(attributeNames.get(i)+"("+attrType.get(i)+")"+"  ");
		}System.out.println();
		System.out.print("attributesTypes:");
		System.out.println("targetID:" + targetID);
		//System.out.println("target:" + target);
		System.out.print("conditionsID:");
		for (int i : conditionsID) {
			System.out.print(i+"  ");
		}System.out.println();
		System.out.println("NA.string:\""+NAstring+"\"");
		//System.out.print("conditionsName:");
		//for (String string : conditionsName) {
		//	System.out.print(string+"  ");
		//}System.out.println();
		int i=0;
		for (Record r : records) {
			System.out.print(i+++": ");
			r.display();
		}
	}
	public DataSet(DataSet data,ArrayList<Integer> recordList){
		try{
//			private ArrayList<Record> records;

			this.conditionsID=new ArrayList<>(data.conditionsID);
			this.NAstring=data.NAstring;
			this.targetID=data.targetID;
			this.attributeNames=new ArrayList<>(data.attributeNames);
			this.attrType=data.attrType;
			this.attrNum=data.attrNum;
			this.records=new ArrayList<>();
			ArrayList<Record> tempRecords=data.getRecords();
			for (Integer integer : recordList) {
				if(integer<tempRecords.size()){
					records.add(tempRecords.get(integer));
				}else throw new Exception("??? WTF?: integer="+integer+"   size="+tempRecords.size());
			}
			//display();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public DataSet(int num,String NA){
		conditionsID=null;
		targetID=-1;
		records=new ArrayList<>();
		attributeNames= new ArrayList<>();
		NAstring=NA;
		attrNum=num;
		for (int i = 0; i < num; i++) {
			attributeNames.add("V"+i);
		}
		attrType=new HashMap<>();
		for (int i = 0; i < attributeNames.size(); i++) {
			attrType.put(i,TYPE.continuous);
		}
	}
	public DataSet(int num, String[] attributes,String NA){
		conditionsID=null;
		targetID=-1;
		records=new ArrayList<>();
		attrNum=num;
		NAstring=NA;
		attributeNames=new ArrayList<>();
		attrType=new HashMap<>();
		for (int i = 0; i < attributes.length; i++) {
			attrType.put(i,TYPE.continuous);
			attributeNames.add(attributes[i].trim());
		}
	}
	public boolean addRecord(Record r){
		if(r.getValues().size()!=attrNum) return false;
		//if(records.contains(r)) return false;
		else {
			records.add(r);
			for (int i = 0; i < attrType.size(); i++) {
				if(attrType.get(i)==TYPE.continuous&&!r.getValues().get(i).equals(NAstring)){
					try{
						Double.parseDouble((String)r.getValues().get(i));
					}catch(Exception e){
						//System.out.println("put "+i+" to descritize");
						attrType.put(i, TYPE.decritize);
					}
				}
			}
			return true;
		}
	}
	public boolean addRecord(String[] strings){
		return addRecord(new Record(strings,NAstring));
	}
//	public String getTarget() {
//		return target;
//	}
	public int getTargetID() {
		return targetID;
	}
//	public void setTarget(String target) {
//		this.target = target;
//	}
	public ArrayList<Record> getRecords() {
		return records;
	}
	public ArrayList<Integer> getConditionsID() {
		return conditionsID;
	}
//	public void setConditionsID(ArrayList<Integer> conditionsID) {
//		this.conditionsID = conditionsID;
//	}
//	public void setS(ArrayList<Record> s) {
//		this.s = s;
//	}
	public ArrayList<String> getAttributeNames() {
		return attributeNames;
	}
//	public void setAttributeNames(ArrayList<String> attributeNames) {
//		this.attributeNames = attributeNames;
//	}
	public int getAttrNum() {
		return attrNum;
	}
//	public void setAttrNum(int attrNum) {
//		this.attrNum = attrNum;
//	}
}
