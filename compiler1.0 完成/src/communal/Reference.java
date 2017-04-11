package communal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import lexical_analysis.Lexer;
import lexical_analysis.Lexer.State;

public class Reference{
	private final static HashMap<Integer, information> ID_MAP;
	private final static HashMap<Integer ,String> REFERENCE_MAP;
	private final static HashSet<String> KEYWORD_SET;
	private final static HashSet<Character> OP_CHAR_SET;
	private final static HashSet<String> OP_SET;
	static{
		REFERENCE_MAP=new HashMap<>();
		KEYWORD_SET=new HashSet<>();
		String[] operators=new String[] {
				"+", "-", "*", "/", "%","(", ")", 	
				";",",","[","]","{","}",
				"<", ">", "<=", ">=", "=", "==", "!=","#"
//				"-", "*", "/", "%", "#","+", "(", ")", 
//				"<", ">", "<=", ">=", "=", "==", "!=", "<<", ">>",
//				"++","--", "+=", "-=", "*=", "/=", "%=",
				//"&&", "||", "!"//, "^",
			};
		OP_CHAR_SET=new HashSet<>();
		for(String op :operators){
			for(int i=0;i<op.length();i++){
				OP_CHAR_SET.add(op.charAt(i));
			}
		}
		OP_SET=new HashSet<>();
		for(String op:operators){
			OP_SET.add(op);
			REFERENCE_MAP.put(REFERENCE_MAP.size()+259,op);
		}
		String[] keyword=new String[] {
				"int", "char","float","double",
				"if","else","for","while","break","return","continue","void"
//				 "do", "try", "catch", "throw",
		};
		for(String key:keyword){
			KEYWORD_SET.add(key);
			REFERENCE_MAP.put(REFERENCE_MAP.size()+259,key);
		}
		ID_MAP=new HashMap<>();
	}
	public static boolean in_keyword_set(String s){
		return KEYWORD_SET.contains(s);
	}	
	public static boolean in_char_set(char c) {
		return OP_CHAR_SET.contains(c);
	}
	public static boolean in_set(String s){
		return OP_SET.contains(s);
	}
	public static boolean is_terminal(String s){
		if(KEYWORD_SET.contains(s)||OP_SET.contains(s)||s.equals("ID")||s.equals("INTEGER")||s.equals("FLOAT")) return true;
		else return false;
	}
	public static ArrayList<String>  get_terminals(){
		ArrayList<String> tempArrayList=new ArrayList<>();
		for(String s:KEYWORD_SET){
			tempArrayList.add(s);
		}
		for(String s:OP_SET){
			tempArrayList.add(s);
		}
		return tempArrayList;
	}
	public static void display(){
		System.out.println("id_map:");
		for(int i=0;i<ID_MAP.size();i++){
			System.out.println("("+i+","+ID_MAP.get(i)+")");
		}
		System.out.println("refernece map:");
		for(int i=259;i<REFERENCE_MAP.size()+259;i++){
			System.out.println("("+i+","+REFERENCE_MAP.get(i)+")");
		}
		
	}
	public static int get(String s){
		for(int i=259;i<REFERENCE_MAP.size()+259;i++){
			if(REFERENCE_MAP.get(i).equals(s)) return i;
		}
		return -1;
	}
	public static String get_id(String s){//get_id在token change中也起了作用
		for(int i=0;i<ID_MAP.size();i++){
			if(s.equals(ID_MAP.get(i).get_name())) return s+":\t\t\t"+Integer.toString(i)+"\t\t\t"+ID_MAP.get(i).state;
		}
		return s;
	}
	public static String get_content() {
		StringBuilder builder=new StringBuilder();
		builder.append("ID_table:\n");
		for(int i=259;i<REFERENCE_MAP.size()+259;i++){
			builder.append("("+i+","+REFERENCE_MAP.get(i)+")\n");
		}
		builder.append("Reference_table:\n");
		for(int i=0;i<ID_MAP.size();i++){
			builder.append("("+i+","+ID_MAP.get(i)+")\n");
		}
		return builder.toString();
	}
	public static String get_reference() {
		StringBuilder builder=new StringBuilder();
		for(int i=0;i<ID_MAP.size();i++){
			builder.append("("+i+","+ID_MAP.get(i)+")\n");
		}
		return builder.toString();
	}
	public static void add(String s, Lexer.State state ){
		boolean flag=true;
		for(int i=0;i<ID_MAP.size();i++)
		{
			if(ID_MAP.get(i).get_name().equals(s)&&ID_MAP.get(i).get_state()==state){
				flag=false;
				break;
			}
		}
		if(flag) ID_MAP.put(ID_MAP.size(), new information(s,state));
	}
	public static void show_id_table(){
		for(int i=0;i<ID_MAP.size();i++)
		{
			System.out.print(i+":");
			ID_MAP.get(i).show();
		}
	}
}
class information{
	String name;
	State state;
	public information(){}
	public information(String n,State s){
		name=n;
		state=s;
	}
	public void show(){
		System.out.println("("+name+","+state+")");
	}
	public String get_name(){
		return name;
	}
	public State get_state(){
		return state;
	}
}