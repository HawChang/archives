package informationExtractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




public class Options extends Information{
	private Map<String, String> optionMap;
	public void display(){
		//System.out.println("original op:"+originalInformation);
		//System.out.println("has attributes:");
		//System.out.print("\t");
		//for (String string : attributes) {
		//	System.out.print(string+"  ");
		//}
		//System.out.println();
		System.out.println("options:");
		for (String string : attributes) {
			System.out.println("\t"+string+"=>"+optionMap.get(string));
		}
	}
	public void remove(String tar){
		optionMap.remove(tar);
		attributes.remove(tar);
		//System.out.println(optionMap.containsKey(tar));
	}
	public boolean putIfExist(String target,String value){
		if(optionMap.containsKey(target)){
			optionMap.put(target,value);
			return true;
		}else return false;
	}
	public boolean alterIfExist(String target,String regex,String replacement){
		//System.out.println("in");
		if(optionMap.containsKey(target)){
			//System.out.println("regex "+regex+"replacemext "+replacement);
			//System.out.println("before :"+target+"="+optionMap.get(target));
			optionMap.put(target, optionMap.get(target).replaceAll(regex, replacement));
			//System.out.println("after :"+target+"="+optionMap.get(target));
			return true;
		}else return false;
	}
	public boolean isNull(String key){
		if(optionMap.containsKey(key)){
			if(optionMap.get(key).equals("")) return true;
			else return false;
		}else return true;
	}
	public String getValue(String key){
		if (optionMap.containsKey(key)) {
			return optionMap.get(key);
		}
		else return null;
	}
	public Options alterAll(Options alter){
		//A.alter(B) 遍历B中的options 只要B中有的 不管A中 全部添加到A中 
		for (String string : alter.attributes) {
			if(optionMap.containsKey(string)) addPair(string, alter.getValue(string),true);
			else addPair(string, alter.getValue(string),false);
		}
		return this;
	}
	public Options alterifExist(Options alter){
		
//		for (String string : alter.attributes) {
//			if(optionMap.containsKey(string)){
//				if(optionMap.get(string)==null) System.err.println("Default Options has Null attributes:"+string);
//				else if(alter.getValue(string)==null) System.err.println("You hava input Null options:"+string);
//				else{
//					optionMap.put(string, alter.getValue(string));
//				}
//			}else System.err.println("You have input unuseful options:"+string);
//		}
//		return this;
		//现在改为遍历A中的 看B中是否有 有就B覆盖A 没有就算了 所以B中有多余的不会检查  只看A中的属性
		for (String string : attributes) {
			if (!optionMap.containsKey(string)) System.err.println("default options has Null attr:"+string);
			if (alter.isNull(string)) ;//System.err.println("alter options has Null attr:"+string);
			else{
				optionMap.put(string, alter.getValue(string));
			}
		}
		//for(String string:alter.attributes){
		//	if(optionMap.get(string)==null) System.out.println("you have input unuseful options:"+string);
		//}//这句不需要 只是看看用户给的指令有没有多余的
		return this;
	}
	public boolean addPair(String k,String v){
		return addPair(k, v, false);
	}
	public boolean addPair(String k,String v,boolean exist){
		if (optionMap.containsKey(k)!=exist){
			if(exist) System.err.println(k+": this key doesn't exists.");
			else System.err.println(k+": this key already exists.");
		}
		else {
			//System.out.println("add succeed");
			if(!exist) attributes.add(k);
			optionMap.put(k, v);
			return true;
		}
		return false;
	}
	public Options(String k,String v){
		this(new HashMap<String, String>(),new ArrayList<String>(),"");
		addPair(k, v);
	}
	public Options(){
		this(new HashMap<String, String>(),new ArrayList<String>(),"");
	}
	public Options(Map<String, String> m){
		this(m,new ArrayList<String>(),"");
	}
	public Options(Map<String, String> m,String o){
		this(m,new ArrayList<String>(),o);
	}
	public Options(Map<String, String> m,ArrayList<String> a){
		this(m,a,"");
	}
	public Options(Map<String, String> m,ArrayList<String> a,String o){
		super(a,o);
		optionMap=m;
	}
}
