package lab6;

import java.util.Scanner;

import lab6.Token.Type;

public class TokenAnalysis {
	public TokenAnalysis() {

	}

	public Token run() {
		// ArrayList<Token> result=new ArrayList<>();
		Token root = null, upper=null, current = null;
		System.out.println("input the sql query.");
		Scanner sc = new Scanner(System.in);
		int knum=0;
		while (sc.hasNext()) {
			//System.out.println("in");
			String temp = sc.next();
			//System.out.println(temp);
			if (temp.equals("SELECT") && sc.next().equals("[")) {
				//System.out.println("choose select");
				if (root == null) {
					root = new Token(Type.SELECT);
					current = root;
				} else {
					//upper = current;
					if(upper!=null){
						current = new Token(Type.SELECT, upper);
						upper.addSon(current);
					}else{
						System.err.println("upper null");
					}
				}
				do {
					String left = sc.next();
					String op = sc.next();
					String right = sc.next();
					//System.out.println(left + op + right);
					if (op.matches("=|<|>|<=|>=") && left.length() > 0
							&& right.length() > 0) {
						current.addRestriction(new Restriction(left, op, right));
					} else {
						System.out.println(left + " " + op + " " + right);
						System.err.println("err in Token. Adding restriction.");
					}
					temp = sc.next();
				} while (temp.equals("&"));
				if(!temp.equals("]")){
					System.out.println(temp);
					System.err.println("no \']\'");
				}
			}else if(temp.equals("PROJECTION")&& sc.next().equals("[")){
				if (root == null) {
					root = new Token(Type.PROJECTION);
					current = root;
				} else {
//					upper = current;
//					current = new Token(Type.PROJECTION, upper);
//					upper.addSon(current);
					if(upper!=null){
						current = new Token(Type.PROJECTION, upper);
						upper.addSon(current);
					}else{
						System.err.println("upper null");
					}
				}
				do {
					String select = sc.next();
					//System.out.println("select");
					//current.show();
					if (select.length() > 0) {
						current.addSelection(select);
					}
					temp = sc.next();
				} while (temp.equals(","));
				if(!temp.equals("]")){
					System.out.println(temp);
						System.err.println("no \']\'");
				}
			}else if(temp.equals("(")){
				knum++;
				upper=current;
			}else if(temp.equals(")")){
				knum--;
				if(knum<0){
					System.err.println("kuohao wrong");
				}
				upper=upper.getFather();
			}else if (temp.equals("#")) {
				sc.close();
				// System.out.println("---------------------------");
				// current.show();
				// System.out.println("---------------------------");
				return root;
			}else{
//				if (root == null) {
//					root = new Token(Type.JOIN);
//					current = root;
//				} else {
//					if(upper!=null){
//						current = new Token(Type.JOIN, upper);
//						upper.addSon(current);
//					}else{
//						System.err.println("upper null");
//					}
//				}
				if(upper!=null){
					current = new Token(Type.JOIN, upper);
					upper.addSon(current);
					//upper.addSon(new Token(Type.JOIN,temp,upper));
					current.addSon(new Token(temp,current));
					temp=sc.next();
					while (temp.equals("JOIN")){
						String union = sc.next();
						if (union.length() > 0) {
							//upper.addSon(new Token(Type.JOIN,union,upper));
							//current.getSelections().add(union);
							current.addSon(new Token(union,current));
						}
						temp = sc.next();
					} 
				}else{
					System.err.println("upper null");
				} 
				if(temp.equals(")")){
					knum--;
					if(knum<0){
						System.err.println("kuohao wrong");
					}
				}else{
					System.out.println(temp);
						System.err.println("no \')\'");
				}
			}
		}
		sc.close();
		return root;
	}

}
