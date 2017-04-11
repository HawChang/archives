package informationExtractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sun.org.apache.xml.internal.security.Init;

public class Structure extends Information {
	private int obsNum;
	private Map<String, attrTypeNClass> structMap;
	private ArrayList<String> factorAttr;	
	private ArrayList<String> numAttr;
	private ArrayList<String> intAttr;
	private ArrayList<String> ordFactorAttr;
	public void display() {
		System.out.println("origin str:"+originalInformation);
		System.out.println("obs num:"+obsNum);
		System.out.println("has attributes:");
		System.out.print("\t");
		for (String string : attributes) {
			System.out.print(string + "  ");
		}
		System.out.println();
//		System.out.println("type:");
//		for (String string : attributes) {
//			attrTypeNClass temp=structMap.get(string);
//			System.out.print("\t" + string + "=>"
//					+ temp.getT().toString());
//			if(temp.getClasses().size()>0){
//				System.out.print(" w/ "+temp.getClasses().size()+" levels:");
//				for (String s : temp.getClasses()) {
//					System.out.print("\""+s+"\" ");
//				}
//			}
//			System.out.println();
//		}
		System.out.println("attr type:");
		System.out.print("int:");
		for (String string : intAttr) {
			System.out.print(string+"  ");
		}System.out.print("\nnum:");
		for (String string : numAttr) {
			System.out.print(string+"  ");
		}System.out.println("\nFactor:");
		for (String string : factorAttr) {
			System.out.print("\t"+string+":  levels=>  ");
			ArrayList<String> temp=structMap.get(string).getClasses();
			for (String string2 : temp) {
				System.out.print(string2+" ");
			}
			System.out.println();
		}System.out.println("Ord.factor:");
		for (String string : ordFactorAttr) {
			System.out.print("\t"+string+":  levels=>  ");
			ArrayList<String> temp=structMap.get(string).getClasses();
			for (String string2 : temp) {
				System.out.print(string2+" ");
			}
			System.out.println();
		}
	}
	public boolean alterAttrTypeNClass(Structure alt){
		boolean flag=true;
		for (String string : alt.getStructMap().keySet()) {
			if(structMap.containsKey(string)){
				//System.out.println("structMap.put("+string+", alt.getStructMap().get("+string+"));");
				structMap.put(string, alt.getStructMap().get(string));
			}else {
				System.err.println("try to alter an attribute that doesn't exsit.");
				flag=false;
			}
		}
		Set<String> temp=structMap.keySet();
		intAttr.clear();
		numAttr.clear();
		factorAttr.clear();
		ordFactorAttr.clear();
		for (String string : temp) {
			switch (structMap.get(string).getType().toString()) {
			case "int":
				intAttr.add(string);
				break;
			case "num":
				numAttr.add(string);
				break;
			case "Factor":
				factorAttr.add(string);
				break;
			case "Ord.factor":
				ordFactorAttr.add(string);
				break;
			default:
				System.err.println(string+"£º unknown type. in alter");
				break;
			}
		}
		return flag;
	}
	public int getObsNum() {
		return obsNum;
	}
	public void setObsNum(int obsNum) {
		this.obsNum = obsNum;
	}
	public Map<String, attrTypeNClass> getStructMap() {
		return structMap;
	}
	public ArrayList<String> getFactorAttr() {
		return factorAttr;
	}
	public ArrayList<String> getNumAttr() {
		return numAttr;
	}
	public ArrayList<String> getIntAttr() {
		return intAttr;
	}
	public ArrayList<String> getOrdFactorAttr() {
		return ordFactorAttr;
	}
	public Structure() {
		this(new HashMap<String, attrTypeNClass>(),new ArrayList<String>(),"",0);
		//structMap=new HashMap<String, attrTypeNClass>();
	}

	public Structure(Map<String, attrTypeNClass> m) {
		this(m,new ArrayList<String>(),"",0);
		
		//super();
		//structMap=m;
	}

	//public Structure(Map<String, attrTypeNClass> m, String o) {
	//	super(new ArrayList<String>(),o);
	//	structMap=m;
	//}

//	public Structure(Map<String, attrTypeNClass> m, ArrayList<String> a) {
//		super(a,"");
//		structMap=m;
//		factorAttr=new ArrayList<>();
//		numAttr=new ArrayList<>();
//		intAttr=new ArrayList<>();
//		for (String string : a) {
//			switch (m.get(string).toString()) {
//			case "int":
//				intAttr.add(string);
//				break;
//			case "num":
//				numAttr.add(string);
//				break;
//			case "Factor":
//				factorAttr.add(string);
//				break;
//			default:
//				System.err.println(string+"£º unknown type.");
//				break;
//			}
//		}
//	}
	public Structure(Map<String, attrTypeNClass> m, ArrayList<String> a,int obs) {
		this(m,a,"",obs);
	}
	public Structure(Map<String, attrTypeNClass> m, ArrayList<String> a, String o,int obs) {
		super(a, o);
		obsNum=obs;
		structMap=m;
		ordFactorAttr=new ArrayList<>();
		factorAttr=new ArrayList<>();
		numAttr=new ArrayList<>();
		intAttr=new ArrayList<>();
		for (String string : a) {
			//System.out.println(string+"   type:"+m.get(string).getType().toString());
			switch (m.get(string).getType().toString()) {
			case "int":
				intAttr.add(string);
				break;
			case "num":
				numAttr.add(string);
				break;
			case "Factor":
				factorAttr.add(string);
				break;
			case "Ord.factor":
				ordFactorAttr.add(string);
				break;
			default:
				System.err.println(string+"£º unknown type. in constructure");
				break;
			}
		}
	}
}


