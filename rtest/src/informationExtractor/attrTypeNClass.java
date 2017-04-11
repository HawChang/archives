package informationExtractor;

import java.util.ArrayList;

public class attrTypeNClass {
	String type;
	Type t;
	ArrayList<String> classes;
	
	public String getType() {
		return type;
	}
	public Type getT() {
		return t;
	}
	public ArrayList<String> getClasses() {
		return classes;
	}
	public attrTypeNClass(){
		type="other";
		t=Type.OTHER;
		classes=new ArrayList<>();
	}
	public attrTypeNClass(String ty){
		this.type=ty;
		this.t=getType(ty);
		classes=new ArrayList<>();
	}
	public attrTypeNClass(Type t,ArrayList<String> c){
		this.type=t.toString();
		this.t=t;
		classes=c;
	}
	public attrTypeNClass(String ty,ArrayList<String> c){
		this.type=ty;
		this.t=getType(ty);
		classes=c;
	}
	private Type getType(String t) {
		Type ret;
		switch (t) {
		case "Factor":
			ret = Type.FACTOR;
			break;
		case "int":
			ret = Type.INT;
			break;
		case "Ord.factor":
			ret = Type.ORD_FACTOR;
			break;
		case "num":
			ret = Type.NUM;
			break;
		default:
			ret = Type.OTHER;
			break;
		}
		return ret;
	}
	public static enum Type {
		NUM("num"), FACTOR("Factor"), INT("int"), ORD_FACTOR("Ord.factor"), OTHER(
				"other");
		private String s;

		private Type(String s) {
			this.s = s;
		}

		public String toString() {
			return s;
		}
	}
}