package lexical_analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import communal.Buffer;
import communal.Reference;
import communal.Token;




public class Lexer {	
	public static enum State{
		Normal, Num, Identifier, Operator, Annotation1, Annotation2, String, Float, Integer
	}
	private ArrayList<Token> list=new ArrayList<Token>();
	private Buffer buffer=new Buffer();
	private char temp=Buffer.EOF;
	private State state;
	public Lexer(String dis){
	    buffer.open(dis);
	    list.clear();
	    state=State.Normal;
	}
	public void next_token(){		
		StringBuilder stringBuilder=new StringBuilder("");
		Token token=new Token("");
	    while(token.get_ref_num()==-1){
	    	//System.out.println(state);
	    	switch (state){
	    	case Normal:
	    		if(Character.toString(temp).matches("[a-zA-Z]")){
	    			stringBuilder.append(temp);
	    			state=State.Identifier;
	    			temp=buffer.getnext();
	    		}else if(temp=='/'&&buffer.nextis()=='/'){
	    			stringBuilder.append(temp);
	    			stringBuilder.append(buffer.getnext());
	    			state=State.Annotation1;
	    			temp=buffer.getnext();
	    		}else if(temp=='/'&&buffer.nextis()=='*'){
	    			stringBuilder.append(temp);
	    			stringBuilder.append(buffer.getnext());
	    			state=State.Annotation2;
	    			temp=buffer.getnext();
	    		}else if(Reference.in_char_set(temp)){
	    			stringBuilder.append(temp);
	    			state=State.Operator;
	    			temp=buffer.getnext();
	    		}
	    		else if(Character.toString(temp).matches("[0-9]")){
	    			stringBuilder.append(temp);
	    			state=State.Num;
	    			temp=buffer.getnext();
	    		}else if(temp=='\"'||temp=='\''){
	    			stringBuilder.append(temp);
	    			state=State.String;
	    			temp=buffer.getnext();
	    		}else {
	    			if(!buffer.hasnext()) {
	    				System.out.println(temp);
	    				return;
	    			}
	    			else temp=buffer.getnext();
	    		}
	    		break;
	    	case Identifier:
	    		if(Character.toString(temp).matches("[a-zA-Z0-9_]")){
	    			stringBuilder.append(temp);
	    			temp=buffer.getnext();
	    		}else {
	    			if(Reference.get(stringBuilder.toString())==-1) Reference.add(stringBuilder.toString(),State.Identifier);
	    			token.change(stringBuilder.toString(),State.Identifier);
	    			list.add(token);
	    			state=State.Normal;
	    		}
	    		break;
	    	case Num:
	    		if(digit_suits_string(temp,stringBuilder.toString())){
	    			stringBuilder.append(temp);
	    			temp=buffer.getnext();
	    		}else {
	    			if(stringBuilder.toString().matches(".*[.e].*")) {
	    				Reference.add(stringBuilder.toString(),State.Float);
	    				token.change(stringBuilder.toString(),State.Float);
	    			}
	    			else {
	    				Reference.add(stringBuilder.toString(),State.Integer);
	    				token.change(stringBuilder.toString(),State.Integer);
	    			}
	    			list.add(token);
	    			state=State.Normal;
	    		}
	    		break;
	    	case Operator:
	    		stringBuilder.append(temp);
	    		if(Reference.in_set(stringBuilder.toString())){
	    			token.change(stringBuilder.toString());
	    			list.add(token);
	    			state=State.Normal;
	    			temp=buffer.getnext();
	    		}else {
	    			stringBuilder.deleteCharAt(stringBuilder.length()-1);
	    			token.change(stringBuilder.toString());
	    			list.add(token);
	    			state=State.Normal;
	    		}
	    		break;
	    	case Annotation1:
	    		if(temp!='\r'&&temp!='\n'&&temp!='\0'){
	    			stringBuilder.append(temp);
	    			temp=buffer.getnext();
	    		}else {
	    			//token.change(stringBuilder.toString());
	    			//list.add(token);
	    			state=State.Normal;
	    			temp=buffer.getnext();
	    			return;
	    		}
	    		break;
	    	case Annotation2:
	    		if(temp=='*'&&buffer.nextis()=='/'){
	    			stringBuilder.append(temp);
	    			stringBuilder.append(buffer.getnext());
	    			//token.change(stringBuilder.toString());
	    			buffer.getnext();
	    			state=State.Normal;
	    			buffer.getnext();
	    			temp=buffer.getnext();
	    			return;
	    		}else if(temp=='\0'){
	    			//token.change(stringBuilder.toString());
	    			//list.add(token);
	    			state=State.Normal;
	    			temp=buffer.getnext();
	    			return;
	    		}else {
	    			stringBuilder.append(temp);
	    			temp=buffer.getnext();	
	    		}
	    		break;
	    	case String:
	    		if(temp=='\n'&&temp=='\0'){
	    			token.change(stringBuilder.toString());
	    			list.add(token);
	    			state=State.Normal;
	    			temp=buffer.getnext();
	    		}else if(temp==stringBuilder.charAt(0)){
	    			stringBuilder.append(temp);
	    			temp=buffer.getnext();	
	    			token.change(stringBuilder.toString());
	    			list.add(token);
	    			state=State.Normal;
	    		}else {
	    			stringBuilder.append(temp);
	    			temp=buffer.getnext();	
	    		}
	    		break;
	    	default:
	    		break;
	    	}
		}
	}
	public boolean run_and_write_to(String dis){
		temp=buffer.getnext();
		//System.out.println(temp);
	    do{
	    	next_token();
	    	//System.out.println("\'"+temp+"\'");
	    }while(buffer.hasnext()||temp!=Buffer.EOF);
	    //show();
	    try{
	    	File file=new File(dis);
	    	if (!file.exists()) {
				file.createNewFile();
			}
	    	BufferedWriter writer=new BufferedWriter(new FileWriter(file));
	    	for(Token token : list) {
				writer.write(token.get_content()+"\n");
				System.out.println(token.get_content());
			}
	    	//writer.write(Reference.get_content());
	    	writer.close();
	    	return true;
	    }catch(Exception e){
	    	e.printStackTrace();
	    	return false;
	    }
	}
	private boolean digit_suits_string(char c, String s){
		boolean flag=false;
		if(s.matches("^([0-9]+)$")&&Character.toString(c).matches("[0-9.e]")) flag=true;
		else if(s.matches("^([0-9]+)\\.$")&&Character.toString(c).matches("[0-9]")) flag=true;
		else if(s.matches("^([0-9]+)\\.[0-9]+$")&&Character.toString(c).matches("[0-9e]")) flag=true;
		else if(s.matches("^([0-9]+)(\\.[0-9]+)?e$")&&Character.toString(c).matches("[-+1-9]")) flag=true;
		else if(s.matches("^([0-9]+)(\\.[0-9]+)?e[-+]$")&&Character.toString(c).matches("[1-9]")) flag=true;
		else if(s.matches("^([0-9]+)(\\.[0-9]+)?e[-+]?[1-9]$")&&Character.toString(c).matches("[0-9]")) flag=true;
		else if(s.matches("^([0-9]+)(\\.[0-9]+)?e[-+]?[1-9][0-9]*$")&&Character.toString(c).matches("[0-9]")) flag=true;
		return flag;
	}
	public void show(){
		for(Token token : list) {
			token.show();
		}
	    Reference.display();
	}
	public ArrayList<Token> getlist(){
		return list;
	}
}





