package communal;

import lexical_analysis.Lexer.State;


public class Token {
	private int ref_num;
	private String value;
	public Token(){
		ref_num=-1;value="";
	}
	public Token(String v){
		ref_num=-1;
		value=v;
	}
	public void change(String v){
		ref_num=Reference.get(v);
		if(ref_num==256) value=Reference.get_id(v);
		else value=v;
	}
	public void change(String v,State s){
		switch (s) {
		case Identifier:
			ref_num=Reference.get(v);
			if(ref_num==-1)ref_num=256;
			break;
		case Float:
			ref_num=257;
			break;
		case Integer:
			ref_num=258;
			break;
		default:
			ref_num=Reference.get(v);
			break;
		}
		value=v;//
	}
	public void show(){
		System.out.println("("+ref_num+","+value+")");
	}
	public int get_ref_num(){
		return ref_num;
	}
	public String get_content(){
		return "("+ref_num+","+value+")";
	}
	public String get_value(){
		return value;
	}
}
