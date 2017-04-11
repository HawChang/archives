package lab6;

import java.util.ArrayList;
import java.util.Stack;
import java.util.TooManyListenersException;

public class Token {
	public void setFather(Token father) {
		this.father = father;
	}

	public Type getType() {
		return type;
	}

	public ArrayList<Token> getSons() {
		return sons;
	}

	public ArrayList<Restriction> getRestrictions() {
		return restrictions;
	}

	public ArrayList<String> getSelections() {
		return selections;
	}

	 public String getName() {
	 return name;
	 }
	enum Type {
		SELECT("select"), JOIN("join"), PROJECTION("projection"),NULL("terminal");
		private String value;

		private Type(String value) {
			// TODO Auto-generated constructor stub
			this.value = value;
		}

		public String toString() {
			return this.value;
		}
	};
	Token root;
	Type type;
	Token father;
	ArrayList<Token> sons;
	ArrayList<Restriction> restrictions;
	ArrayList<String> selections;
	String name;
	public String toString(){
		String showString = "";
		switch (type) {
		case SELECT:
			showString += type.toString() + " [ ";
			for (int i = 0; i < restrictions.size() - 1; i++) {
				showString += restrictions.get(i).toString() + " & ";
			}
			showString += restrictions.get(restrictions.size() - 1).toString();
			showString += " ]";
			break;
		case PROJECTION:
			showString += type.toString() + " [ ";
			for (int i = 0; i < selections.size() - 1; i++) {
				showString += selections.get(i) + " , ";
			}
			showString += selections.get(selections.size() - 1);
			showString += " ]";
			break;
		case JOIN:
			showString+="join -> ";
			for (int i = 0; i < sons.size(); i++) {
				showString+=sons.get(i).toString()+" ";
			}
			break;
		case NULL:
			showString+=name;
			break;
		default:
			break;
		}
		return showString;
	}
	public Token(String terminal,Token f){
		type=Type.NULL;
		name=terminal;
		sons=new ArrayList<>();
		father=f;
	}
	public Token(Type t) {
		type = t;
		father = null;
		sons = new ArrayList<>();
		restrictions = new ArrayList<>();
		selections = new ArrayList<>();
	}

	public Token(Type t, Token f) {
		type = t;
		father = f;
		sons = new ArrayList<>();
		restrictions = new ArrayList<>();
		selections = new ArrayList<>();
	}

	// public Token(Type t,String name, Token f) {
	// type = t;
	// father = f;
	// sons=new ArrayList<>();
	// //this.name=name;
	// }
	public Token getFather() {
		return father;
	}

	public void addSon(Token t) {
		sons.add(t);
	}

	public void addRestriction(Restriction r) {
		restrictions.add(r);
	}

	public void addSelection(String selection) {
		selections.add(selection);
	}

	public void show(int startlength) {
		String showString = "";
		for (int i = 0; i < startlength; i++) {
			showString += " ";
		}
		switch (type) {
		case SELECT:

			showString += "->";
			showString += type.toString() + " [ ";
			for (int i = 0; i < restrictions.size() - 1; i++) {
				showString += restrictions.get(i).toString() + " & ";
			}
			showString += restrictions.get(restrictions.size() - 1).toString();
			showString += " ]";
			break;
		case PROJECTION:
			showString += "->";
			showString += type.toString() + " [ ";
			for (int i = 0; i < selections.size() - 1; i++) {
				showString += selections.get(i) + " , ";
			}
			showString += selections.get(selections.size() - 1);
			showString += " ]";
			break;
		case JOIN:
			showString+="->join ";
			break;
		case NULL:
			showString+="->"+name;
			break;
		default:
			break;
		}
		System.out.println(showString);
		startlength = showString.length();
		for (int i = 0; i < sons.size(); i++) {
			sons.get(i).show(startlength);
		}
	}

	public void show() {
		show(0);
	}

	public void optimize() {
		//Token root = new Token(type);// 要改
		Token node = this;
		optimize_projection(node);
//		Stack<Token> stack=new Stack<>();
//		for(int i=0;i<sons.size();i++)
//			stack.push(sons.get(i));
//		while(!stack.empty()){
//			Token tempnode=stack.pop();
//			for(int i=0;i<tempnode.getSons().size();i++)
//				stack.push(tempnode.getSons().get(i));
//			optimize(node);
//		}
		//optimize_select(node);
		// switch (node.type) {
		// case SELECT:
		// for(int i=0;i<restrictions.size();i++){
		// String temp=restrictions.get(i).getLeft();
		// Token tempToken=find_relate(node,temp);
		// }
		// break;
		// case PROJECTION:
		// delete_unrelate(node,);
		// break;
		// case JOIN:
		// break;
		// default:
		// break;
		// }
		//return root;
	}
	//******************************************************************
//	public void optimize_select(Token node){
//		Stack<Token> stack = new Stack<>();
//		Token tempnode;
//		for (int i = 0; i < node.getSons().size(); i++) {
//			stack.push(node.getSons().get(i));
//		}
//		while (!stack.empty()) {
//			tempnode = stack.pop();
//			//System.out.println("tempnode:" + tempnode.getType().toString());
//			for (int i = 0; i < tempnode.getSons().size(); i++) {
//				stack.push(tempnode.getSons().get(i));
//			}
//			checknode_select(tempnode, tempnode.getFather());
//			//System.out.println("check node:" + tempnode.getType().toString()
//			//		+ "," + tempnode.getFather().getType().toString());
//		}
//	}
	public void checknode_select(Token tar, Token selection){
		switch (tar.getType()) {
		case SELECT:
			break;
		case PROJECTION:
			break;
		case JOIN:
			int joinNum = -1;
			for (int i = 0; i < tar.getSons().size(); i++) {
				joinNum = i;
				String string_to_check = tar.getSons().get(i).getName();
				boolean need = false;
				int selectionNum = -1;
				for (int j = 0; j < selection.getRestrictions().size(); j++) {
					String string_for_check = selection.getRestrictions().get(j).getLeft();
					switch (string_for_check) {
					case "ENAME":
						if (string_to_check.equals("EMPLOYEE")) {
							selectionNum = j;
							need = true;
						}
						break;
					case "DNAME":
						if (string_to_check.equals("DEPARTMENT")) {
							selectionNum = j;
							need = true;
						}
						break;
					default:
						break;
					}
				}
				if (need) {
					Token father;
					if(selection.getFather()==null) {
						father=new Token(Type.JOIN);
						root=father;
						selection.setFather(father);
						father.addSon(selection);
					}else{
						father=new Token(Type.JOIN,selection.getFather());
						father.addSon(selection);
						
						selection.getFather().getSons().remove(selection);
						selection.getFather().getSons().add(father);
						selection.setFather(father);
					}
					//father=selection.getFather();
					Token newSelection = new Token(Type.SELECT,
							selection.father);
					father.addSon(newSelection);
					newSelection.addRestriction(selection.getRestrictions().get(
							selectionNum));
					selection.getRestrictions().remove(selectionNum);
					//Token newJoin = new Token(Type.JOIN, newSelection);
					//newJoin.addSon();
					tar.getSons().remove(joinNum);
					newSelection.addSon(tar.getSons().get(joinNum));
					if (selection.getRestrictions().size() == 0) {
						for (int k = 0; k < selection.getSons().size(); k++) {
							Token tempNode = selection.getSons().get(k);
							tempNode.setFather(father);
							father.addSon(tempNode);
						}
						father
								.getSons()
								.remove(father.getSons()
										.indexOf(selection));
					}
					if(tar.getSons().size()==1){
						
						tar.getFather().addSon(tar.getSons().get(0));
						tar.getSons().get(0).setFather(tar.getFather());
						tar.getFather().getSons().remove(tar.getFather().getSons().indexOf(tar));
					}
					if (tar.getSons().size() == 0) {
						System.err.println("do not consider no join");
					}
				}
			}
			break;
		default:
			break;
		}
	}
	//******************************************************************
	public void optimize_projection(Token node) {
		Stack<Token> stack = new Stack<>();
		Token tempnode;
		for (int i = 0; i < node.getSons().size(); i++) {
			stack.push(node.getSons().get(i));
		}
		while (!stack.empty()) {
		
			tempnode = stack.pop();
			for (int i = 0; i < tempnode.getSons().size(); i++) {
				stack.push(tempnode.getSons().get(i));
			}
			if(tempnode.getFather().getType()==Type.PROJECTION)
				checknode_projection(tempnode, tempnode.getFather());
			if(tempnode.getFather().getType()==Type.SELECT)
				checknode_select(tempnode, tempnode.getFather());
			if(tempnode.getType()==Type.JOIN&&tempnode.getFather()!=null){
				if(tempnode.getFather().getFather()!=null){
					Token grandFather=tempnode.getFather().getFather();
					if(grandFather.getType()==Type.SELECT){
						int joinNum = -1;
						for (int i = 0; i < tempnode.getSons().size(); i++) {
							joinNum = i;
							String string_to_check;
							if(tempnode.getSons().get(i).getType()==Type.NULL) string_to_check = tempnode.getSons().get(i).getName();
							else break;
							boolean need = false;
							for (int j = 0; j < grandFather.getRestrictions().size(); j++) {
								String string_for_check = grandFather.getRestrictions().get(j).getLeft();
								switch (string_for_check) {
								case "ESSN":
									if (string_to_check.equals("WORKS_ON")) {
										//System.out.println("find");
										need = true;
									}
									break;
								default:
									break;
								}
							}
							if (need) {
								Token newRoot;
								if(grandFather.getFather()==null){
									if(grandFather.getSons().size()==1){
										root=grandFather.getSons().get(0);
										grandFather.getSons().remove(0);
									}else{
										newRoot=new Token(Type.JOIN);
										root=newRoot;
										for(int j=grandFather.getSons().size()-1;j>=0;j--){
											//System.out.println("j="+j);
											Token tempnode2=grandFather.getSons().get(j);
											newRoot.addSon(tempnode2);
											tempnode2.setFather(newRoot);
											grandFather.getSons().remove(j);
										}
									}
								}    //将grandfather取出来
								Token tempTerminal=tempnode.getSons().get(joinNum);
								grandFather.addSon(tempTerminal);
								tempTerminal.setFather(grandFather);
								tempnode.getSons().remove(joinNum);
								grandFather.setFather(tempnode);
								tempnode.addSon(grandFather);
							}
													
						}
					}
				}
			}
		}
	}

	public void checknode_projection(Token tar, Token projection) {
		switch (tar.getType()) {
		case SELECT:
			break;
		case PROJECTION:
			//System.err.println("not considered");
			break;
		case JOIN:
			// System.out.println("in");
			int joinNum = -1;
			for (int i = 0; i < tar.getSons().size(); i++) {
				joinNum = i;
				//String string_to_check = tar.getSons().get(i).getName();
				//System.out.println("string_to_check:" + string_to_check);
				boolean need = false;
				int projectionNum = -1;
				for (int j = 0; j < projection.getSelections().size(); j++) {
					//String string_for_check = projection.getSelections().get(j);
					//System.out.println("string_for_check:" + string_for_check);
//					switch (string_for_check) {
//					case "ESSN":
//						if (string_to_check.equals("WORKS_ON")) {
//							//System.out.println("find");
//							projectionNum = j;
//							need = true;
//						}
//						break;
//					case "PANEM":
//						if (string_to_check.equals("PROJECT")) {
//							projectionNum = j;
//							need = true;
//						}
//						break;
//					default:
//						break;
//					}
				}
				if (need) {
					Token newProjection = new Token(Type.PROJECTION,
							projection.father);
					projection.getFather().addSon(newProjection);
					newProjection.addSelection(projection.getSelections().get(
							projectionNum));
					projection.getSelections().remove(projectionNum);
					Token newJoin = new Token(Type.JOIN, newProjection);
					newJoin.addSon(tar.getSons().get(joinNum));
					tar.getSons().remove(joinNum);
					newProjection.addSon(newJoin);
					// projection.getSons().remove(projection.getSons().indexOf(tar));
					// newProjection.addSon(tar);
					// tar.setFather(newProjection);
					if (projection.getSelections().size() == 0) {
						for (int k = 0; k < projection.getSons().size(); k++) {
							Token tempNode = projection.getSons().get(k);
							tempNode.setFather(projection.getFather());
							projection.getFather().addSon(tempNode);
						}
						projection
								.getFather()
								.getSons()
								.remove(projection.getFather().getSons()
										.indexOf(projection));
					}
					if (tar.getSons().size() == 0) {
						System.err.println("do not consider no join");
					}
				}
			}
			break;
		default:
			break;
		}
	}
	// public Token find_relate(Token node,String des){
	// for(int i=0;i<node.getRestrictions().size();i++ ){
	//
	// }
	// }
}
