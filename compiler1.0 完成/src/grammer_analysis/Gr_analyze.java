package grammer_analysis;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;


import communal.Reference;
import communal.Token;
import lexical_analysis.Lexer;

public class Gr_analyze {
	private ArrayList<Token> list;
	private ArrayList<expression> expressions;
	private ArrayList<pairs> first,follow;
	private HashMap<String, HashMap<String, expression>> analyze_table;
	private Stack<String> stack;
	public Gr_analyze(){}
	public Gr_analyze(Lexer l,String dis){
		list=new ArrayList<Token>(l.getlist());
		expressions=new ArrayList<expression>();
		first=new ArrayList<>();
		follow=new ArrayList<>();
		analyze_table=new HashMap<>();
		stack=new Stack<>();
		stack.push("$");
		import_expressions(dis);
		for(expression e:expressions){
			HashMap<String, expression> temp=new HashMap<>();
			for(String s:Reference.get_terminals()){
				temp.put(s, new expression());
			}
			temp.put("$", new expression());
			temp.put("ID", new expression());
			temp.put("INTEGER", new expression());
			temp.put("FLOAT", new expression());
			analyze_table.put(e.get_left_expression(), temp);
		}
		create_table();
		
	}
	ArrayList<String> import_tokens(String s){
		ArrayList<String> tempArrayList=new ArrayList<>();
		String temp=new String();
		//String[] strings=new String[]{};
 		try{
			BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(s)));
			System.out.println("open file \""+s+"\" successfully.");
			temp=reader.readLine();
			while(temp!=null){
				System.out.println("("+temp.substring(1, 4)+","+Reference.get_id(temp.substring(5,temp.length()-1))+")");
				if(temp.substring(1, 4).equals("256")) tempArrayList.add("ID");
				else if(temp.substring(1, 4).equals("258")) tempArrayList.add("INTEGER");
				else if(temp.substring(1, 4).equals("257")) tempArrayList.add("FLOAT");
				else tempArrayList.add(temp.substring(5,temp.length()-1));
				temp=reader.readLine();
			}
			reader.close();
			}
			catch(IOException e){
				System.err.println("open file \""+s+"\" failed.");
				e.printStackTrace();
			}
		return tempArrayList;
	}
	public void analyze(String s){
		ArrayList<String> dis=import_tokens(s);
		System.out.printf("%-10s", "step:");
		System.out.printf("%-130s", "stack:");
		System.out.printf("%-60s", "input:");
		System.out.printf("%-50s","action:");
		System.out.println();
		int step=0;
		//---------------------------------------------------
		String input;
		stack.push(expressions.get(0).get_left_expression());
		dis.add("$");
		show_analyze(step++, dis, "");
		boolean flag=true;
		do{
			input=dis.get(0);
			//System.out.println(stack.peek());
		if(Reference.is_terminal(stack.peek())){
			if(input.equals(stack.peek())) {
				stack.pop();
				dis.remove(0);
				show_analyze(step++,dis,"match");
			}else {
				flag=false;
				System.out.println(dis);
				System.err.println("input err.");
			}
		}else if(stack.peek().equals("$")){
			if(input.equals("$")) {
				flag=false;
				System.out.println("finish");
			}
			else {
				flag=false;
				System.out.println(dis);
				System.err.println("input err.");
			}
		}else{
			//System.out.println("peek="+stack.peek());
			expression tempExpression=analyze_table.get(stack.peek()).get(input);
			if(tempExpression==null){
				System.err.println(input+" is not terminal.");
				flag=false;
			}else{
				//System.out.println(stack.peek()+"->"+input);
				//System.out.println(tempExpression.get_expression());
				if(!tempExpression.get_left_expression().equals("")){
					stack.pop();
					if(tempExpression.get_right_expressions().get(0).equals("$"));
					else{
						ArrayList<String> l=tempExpression.get_right_expressions();
						for(int i=l.size()-1;i>=0;i--){
							stack.push(l.get(i));
						}
					}
					show_analyze(step++,dis,tempExpression.get_expression());
				}else {
					flag=false;
					//System.out.println(stack.peek()+"->"+input);
					System.err.println("can not find the expression to recurse.");
				}
			}
		}	
		}while(flag);
		//Reference.show_id_table();
	}
	private void show_analyze(int step,ArrayList<String> dis,String action){
		StringBuffer tempBuffer=new StringBuffer();
		System.out.printf("%-10s", step);
		for(int i=0;i<stack.size();i++){
			tempBuffer.append(stack.get(i));
		}
		System.out.printf("%-130s", tempBuffer.toString());
		
		tempBuffer.delete(0, tempBuffer.length());
		for(String s:dis){
			tempBuffer.append(s);
		}
		System.out.printf("%-60s", tempBuffer.toString().substring(0, Math.min(10, tempBuffer.length())));
		System.out.printf("%-50s",action);
		System.out.println();	
	}
	private  void create_table(){
		find_firstSet();
		find_followSet();
		//System.err.println("sss");
		for(expression e:expressions){
			HashSet<String> set=get_first(e.get_right_expressions());
			for(String s:set){
				if(analyze_table.get(e.get_left_expression()).get(s).get_left_expression().equals("")) analyze_table.get(e.get_left_expression()).put(s,e);
				else {
					System.err.println("already have."+"  "+e.get_expression()+"  wants to replace   "+analyze_table.get(e.get_left_expression()).get(s).get_expression());
					System.exit(0);
				}
			}
			if(e.get_right_expressions().get(0).equals("$")){
				if(e.get_right_expressions().size()!=1) System.err.println("expression err.");
				else {
					for(String s:get_fellow(e.get_left_expression())){
						analyze_table.get(e.get_left_expression()).put(s,e);
					}
					//e.show();
				}
			}
		} 
		show_table();
	}
	private void show_table(){
		System.out.printf("%-30s","");
		for(String s:Reference.get_terminals()){
			System.out.printf("%-60s",s);
		}
		System.out.printf("%-60s","$");
		System.out.println();
		for(pairs p:first){
			System.out.printf("%-30s",p.get_terminal()+":");
			for(String s:Reference.get_terminals()){
				if(analyze_table.get(p.get_terminal()).get(s).get_left_expression().equals("")) System.out.printf("%-60s","---");
				else{
					System.out.printf("%-60s","|"+analyze_table.get(p.get_terminal()).get(s).get_expression()+"|");
				}
			}
			if(analyze_table.get(p.get_terminal()).get("$").get_left_expression().equals("")) System.out.printf("%-60s","---");
			else{
				System.out.printf("%-60s","|"+analyze_table.get(p.get_terminal()).get("$").get_expression()+"|");
			}
			System.out.println();
		}
	}
	private  ArrayList<String> get_fellow(String s){
		for(int i=0;i<follow.size();i++){
			if(follow.get(i).get_terminal().equals(s)){
				return follow.get(i).get_fellow();
			}
		}
		return null;
	}
	private  HashSet<String> get_first(ArrayList<String> alpha){
		HashSet<String> tempArrayList=new HashSet<>();
		for(String s:alpha){
			if(s.equals("$")) break;
			else if(Reference.is_terminal(s)){
				tempArrayList.add(s);
				break;
			}else{
				pairs p=get_in_Set(first, s);
				if(p==null) {
					System.err.println("can't find this variable");
					break;
				}
				else {
					tempArrayList.addAll(p.get_fellow());
					if(!p.get_nullable()) break;
				}
			}
		}
		return tempArrayList;
	}

	private pairs get_in_Set(ArrayList<pairs> set,String s){
		for(pairs p:set){
			if(s.equals(p.get_terminal())){
				return p;
			}
		}
		return null;
	}
	private void find_followSet(){
		ArrayList<String> temp_arrayList=new ArrayList<String>();
		StringBuffer tempBuffer=new StringBuffer();
		boolean flag;
		pairs start=new pairs(expressions.get(0).get_left_expression());
		start.add_fellow("$");
		follow.add(start);
		//int mmm=0;
		do{
			flag=false;
			for(int i=0;i<expressions.size()-1;i++){
				//System.out.println("--------------");
				//expressions.get(i).show();
				expression tar=expressions.get(i);
				temp_arrayList=tar.get_right_expressions();
				if(temp_arrayList.get(0).equals("$")){
					if(temp_arrayList.size()>1) System.err.println("err aaaaa");
					else {
						pairs p;
						tempBuffer=new StringBuffer(tar.get_left_expression());
						p=get_in_Set(follow, tempBuffer.toString());
						if (p==null){
							p=new pairs(tempBuffer.toString());
							follow.add(p);
						}
						p.set_nullable(true);
					}
				}else{
					int j=0;
					while(j<temp_arrayList.size()-1){
						if(Reference.is_terminal(temp_arrayList.get(j)));
						else{
							pairs p;
							tempBuffer=new StringBuffer(temp_arrayList.get(j));
							p=get_in_Set(follow, tempBuffer.toString());
							if (p==null){
								p=new pairs(tempBuffer.toString());
								follow.add(p);
							}
							//System.out.print("exam :");
							//p.show();
							int k=j+1;
							while(k<temp_arrayList.size()){
								tempBuffer=new StringBuffer(temp_arrayList.get(k));
								if (Reference.is_terminal(tempBuffer.toString())) {
									flag=p.add_fellow(tempBuffer.toString())||flag;
									break;
								}else{
									tempBuffer=new StringBuffer(temp_arrayList.get(k));
									pairs t=get_in_Set(first, tempBuffer.toString());
									if (t==null){
										System.err.println("can't find "+tempBuffer.toString()+"'s firstset");
										flag=true;
									}else{
										//System.out.print("get(first) :");
										//t.show();
										flag=p.add_fellow(t)||flag;
										if(k==temp_arrayList.size()-1&&t.get_nullable()){
											pairs f=get_in_Set(follow, tar.get_left_expression());
											if (f==null){
												flag=true;
											}else{
												//System.out.print("get(follow):");
												//f.show();
												flag=p.add_fellow(f)||flag;
											}
										}
										if(!t.get_nullable()) break;
									}
								}
								k++;
							}
						}
						j++;
					}
					tempBuffer=new StringBuffer(temp_arrayList.get(temp_arrayList.size()-1));
					if(!Reference.is_terminal(tempBuffer.toString())){
						pairs s;
						s=get_in_Set(follow, tempBuffer.toString());
						if(s==null){
							s=new pairs(tempBuffer.toString());
							follow.add(s);
						}
						pairs f=get_in_Set(follow, tar.get_left_expression());
						if (f==null){
							flag=true;
						}else{
							//System.out.print("get(follow):");
							//f.show();
							flag=s.add_fellow(f)||flag;
						}
					}
				}
	
			}
		}while(flag);
		//System.out.println("follow set:------------------");
		//show(follow);
	}
	public void find_firstSet(){
		ArrayList<String> temp_arrayList=new ArrayList<String>();
		StringBuffer tempBuffer=new StringBuffer();
		boolean flag=false;
		do{
			flag=false;
			for(int i=expressions.size()-1;i>=0;i--){
				tempBuffer=new StringBuffer(expressions.get(i).get_left_expression());
				//System.out.println(tempBuffer.toString()+"-------------------");
				if(Reference.is_terminal(tempBuffer.toString())){
					System.err.println("err: left expression if terminal.");
					break;
				}else {
					pairs p;
					//System.out.println("Gr_analyze.find_firstSet()");
					p=get_in_Set(first, tempBuffer.toString());
					if(p==null){
						p=new pairs(tempBuffer.toString());
						first.add(p);
						//System.out.println("add "+p.get_terminal());
					}else{
//						System.out.print("(exists)");
//						p.show();
					}
					temp_arrayList=expressions.get(i).get_right_expressions();
//					System.out.print("get:");
//					for(String s:temp_arrayList){
//						System.out.print(" "+s);
//					}
//					System.out.println();
					if(temp_arrayList.get(0).equals("$")){
						flag=p.add_fellow("$")||flag;
						//p.show();
						continue;  //--------------------------
					}else{
						int j=0;
						while(j<temp_arrayList.size()){
							String tempString=temp_arrayList.get(j);
							if(Reference.is_terminal(tempString)){
								flag=p.add_fellow(0,temp_arrayList.get(j))||flag;
								break;
							}else{
								pairs temp=get_in_Set(first, tempString);
								if(temp==null){
									flag=true;
									break;
								}else{
									//System.out.print("find ");
									//temp.show();
									if(j<temp_arrayList.size()-1)  {
										flag=p.add_fellow_without_null(temp)||flag;
										if(!temp.get_nullable()) break;
									}else {
										flag=p.add_fellow(temp)||flag;
									}
									
								}
								//System.out.println(temp_arrayList.get(j)+" is not terminal.");
							}
							j++;
						}
					}
					//p.show();
				}
			}
		}while(flag);
		//System.out.println("first set:--------------");
		//show(first);
	}
	private void import_expressions(String dis){
		String temp=new String();
		String[] strings=new String[]{};
		try{
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(dis)));
		System.out.println("open file \""+dis+"\" successfully.");
		temp=reader.readLine();
		while(temp!=null){
			strings =temp.split(" ");
			expressions.add(new expression(strings));
			temp=reader.readLine();
		}
		show_expressions();
		reader.close();
		}
		catch(IOException e){
			System.out.println("open file \""+dis+"\" failed.");
			e.printStackTrace();
		}
	}
	public void show(ArrayList<pairs> p){
		for(pairs s:p){
			s.show();
		}
	}
	public void show_expressions(){
		for(expression e:expressions){
			e.show();
		}
	}
	public void show(){
		for(Token token : list) {
			token.show();
		}
	}
}
class expression{
	private String left_expression;
	private ArrayList<String> right_expressions;
	public expression() {
		// TODO Auto-generated constructor stub
		left_expression="";
		right_expressions=new ArrayList<>();
		right_expressions.clear();
	}
	public expression(String[] s){
		right_expressions=new ArrayList<String>();
		if(s.length>1&&s[1].equals("->")){
			left_expression=s[0];
			if(Reference.is_terminal(left_expression)) {
				System.err.println("err occur");
				System.exit(0);
			}
			for(int i=2;i<s.length;i++){
				right_expressions.add(s[i]);
			}
		}else{
			for(String sa:s){
				System.out.print(sa);
			}
			System.out.println();
			System.err.println("informal expression");
			System.exit(0);
		}
	}
	public void show(){
		System.out.print(left_expression+" ->");
		for(String s:right_expressions){
			System.out.print(" "+s);
		}
		System.out.println();
	}
	public String get_expression(){
		StringBuffer tempBuffer=new StringBuffer();
		tempBuffer.append(left_expression);
		tempBuffer.append("->");
		for(String s:right_expressions){
			tempBuffer.append(s);
		}
		return tempBuffer.toString();
	}
	public String get_left_expression(){
		return left_expression;
	}
	public ArrayList<String> get_right_expressions(){
		return right_expressions;
	}
}
class pairs{
	private String terminal;
	private boolean nullable;
	private ArrayList<String>  fellow;
	public pairs(){
		terminal="";
		fellow=new ArrayList<>();
		nullable=false;
	}
	public pairs(String s){
		terminal=s;
		fellow=new ArrayList<>();
		nullable=false;
	}
	public void show(){
		System.out.print(terminal+" : ");
		for(String s:fellow){
			System.out.print(" "+s);
		}
		System.out.println();
	}
	public boolean add_fellow(String s){
		if(fellow.contains(s)) return false;	
		if(s.equals("$")) {
			nullable=true;
			//System.out.println(terminal+" turns nullable.");
		}
		fellow.add(s);
		return true;
	}
	public boolean add_fellow(int index,String s){
		if(fellow.contains(s)) return false;
		if(s.equals("$")) {
			nullable=true;
			//System.out.println(terminal+" turns nullable.");
		}
		fellow.add(index,s);
		return true;
	}
	public ArrayList<String> get_fellow(){
		return fellow;
	}
	public String get_terminal(){
		return terminal;
	}
	public boolean get_nullable(){
		return nullable;
	}
	public void set_nullable(boolean b){
		nullable=b;
	}
	public int get_fellow_size(){
		return fellow.size();
	}
	public boolean add_fellow(pairs p){
		boolean flag=false;
		for(String s:p.get_fellow()){
			if(fellow.contains(s)) continue;
			if(s.equals("$")) {
				nullable=true;
				fellow.add(s);
				flag=true;
				//System.out.println(terminal+" turns nullable.");
			}
			else {
				fellow.add(0,s);
				flag=true;
			}
		}
		return flag;
	}
	public boolean add_fellow_without_null(pairs p){
		boolean flag=false;
		for(String s:p.get_fellow()){
			if(fellow.contains(s)||s.equals("$")) continue;
			fellow.add(0,s);
			flag=true;
		}
		return flag;
	}
}