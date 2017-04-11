package util;

import java.util.ArrayList;

public class Record {
	private ArrayList<Object> values;
	//private Object objectClass;
	private double weight;

	public void display(){
		for (Object object : values) {
			System.out.print(object.toString()+"  ");
		}
		System.out.println("weight: "+weight);
	}
	public Record(){
		values = new ArrayList<>();
		//objectClass = "";
		weight=1;
	}
	public Record(String[] s,String NAstring){
		values = new ArrayList<>();
		for (String string : s) {
			String value=string.trim();
			if(NAstring.equals(" ")&&values.equals("")) value=" ";
			values.add(value);
		}
		//objectClass = "";
		weight=1;
	}
	public double getWeight() {
		return weight;
	}
//	public void setWeight(double weight) {
//		this.weight = weight;
//	}
	public ArrayList<Object> getValues() {
		return values;
	}
//	public void setValues(ArrayList<Object> values) {
//		this.values = values;
//	}
//	public Object getObjectClass() {
//		return objectClass;
//	}
//	public void setObjectClass(Object objectClass) {
//		this.objectClass = objectClass;
//	}
}	
