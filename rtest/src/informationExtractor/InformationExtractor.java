package informationExtractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.Option;

import ranalysis.ranalysis;

public class InformationExtractor {
	private Pattern p;
	private Matcher m;

//	public InformationExtractor() {
//	}

	// 重载
	public Options getOptions(String target) {
		return this.getOptions(target, "=", ",");
	}

	// public Options getAssoAdjustOp(String target){
	// Options op=new Options();
	// p=Pattern.compile("(-|+)(\\d+)");
	// m=p.matcher(target);
	// while (m.find()) {
	// System.out.println("match:"+m.group(0));
	// System.out.println(m.group(1));
	// System.out.println(m.group(2));
	// op.addPair("direction",m.group(1));
	// }
	// }
	// 没用到sep pattern可以自己定义option的复制操作符 默认为=
	public Options getOptions(String target, String pattern, String sep) {
		ArrayList<String> frequnce = new ArrayList<>();
		Map<String, String> result = new HashMap<String, String>();
		result.clear();
		frequnce.clear();
		// String[] targets=target.split(sep);
		// for (String string : targets) {
		// System.out.println(string);
		// }
		p = Pattern.compile("([^" + sep + " ]*?)\\s*?" + pattern + "\\s*?\\\"\\s*?(.*?)\\s*?\\\"");
		try {
			m = p.matcher(target);
			while (m.find()) {
				if (result.containsKey(m.group(1).trim())) {// 以上正则保证不需要trim
															// 但还是需要注意空格
															// 以防改正则后出错
					throw new Exception("key duplicate.");
				} else {
					frequnce.add(m.group(1).trim());
					result.put(m.group(1).trim(), m.group(2).trim());
				}
			}
			/**
			 * for (String string : targets) { m=p.matcher(string);
			 * while(m.find()){ if(result.containsKey(m.group(1))) {
			 * System.out.println(m.group(1)); throw new
			 * Exception("key duplicate."); } else result.put(m.group(1),
			 * m.group(2)); } }
			 */
		} catch (Exception e) {
			System.out.println("InformationExtractor.getOptions():" + e);
		}
		return new Options(result, frequnce, target);
	}

	public Map<String, Options> getDiscretizeOptions(String target, String pattern, String sep) {
		Map<String, Options> result = new HashMap<>();
		// ArrayList<String> frequnce = new ArrayList<>();
		// Map<String, String> result = new HashMap<String, String>();
		result.clear();
		// frequnce.clear();
		// String[] targets=target.split(sep);
		// for (String string : targets) {
		// System.out.println(string);
		// }
		p = Pattern.compile("([^" + sep + " ]*?)\\s*" + pattern + "\\s*\\\"\\s*(.*?)\\s*\\\"");
		try {
			m = p.matcher(target);
			while (m.find()) {
				// System.out.println("match:"+m.group(0));
				String[] attrName = m.group(1).split("\\$");
				Options op;
				// System.out.println("attr ="+attrName[0]+",  method="+attrName[1]);
				if (attrName.length == 1) {
					if (result.containsKey(attrName[0])) {
						op = result.get(attrName[0]);
						 if ((op.getAttributes().contains("method")||op.getAttributes().contains("categories"))) continue;
					} else {
						// System.out.println("add new attr"+attrName[0]);
						op = new Options();
						result.put(attrName[0], op);
					}
					// frequnce.add(m.group(1));
					op.addPair("format", m.group(2).trim());
				} else {
					if (result.containsKey(attrName[0])) {
						op = result.get(attrName[0]);
						 if ((attrName[1].matches("(method|categorise)")&&op.getAttributes().contains("format"))||(attrName[1].equals("format")&&(op.getAttributes().contains("method")||op.getAttributes().contains("categories")))) continue;
					} else {
						// System.out.println("add new attr"+attrName[0]);
						op = new Options();
						result.put(attrName[0], op);
					}
					// frequnce.add(m.group(1));
					op.addPair(attrName[1], m.group(2).trim());
				}
			}
			/**
			 * for (String string : targets) { m=p.matcher(string);
			 * while(m.find()){ if(result.containsKey(m.group(1))) {
			 * System.out.println(m.group(1)); throw new
			 * Exception("key duplicate."); } else result.put(m.group(1),
			 * m.group(2)); } }
			 */
		} catch (Exception e) {
			System.out.println("InformationExtractor.getOptions():" + e);
		}
		return result;
	}

	// public Map<String, attrTypeNClass> extractDiscretize(String log,
	// ranalysis r) {
	// Map<String, attrTypeNClass> result = new HashMap<>();
	// result.clear();
	// p = Pattern
	// .compile("\"(.*?)\" discretized as \"(num|factor|int|ord\\.factor)\":([^\\n]*)");
	// m = p.matcher(log);
	// try {
	// // System.out.println("innnnnnnnnnnnnn");
	// while (m.find()) {
	// if (m.group(2).trim().equals("factor")) {// read.table好像只有Factor
	// // int num这三种
	// // trim还是可以不用
	// ArrayList<String> classes = new ArrayList<>();
	// classes.clear();
	// Pattern p2 = Pattern.compile("\"(.*?)\"");
	// Matcher m2 = p2.matcher(m.group(3));
	// // String[]
	// //
	// classList=r.getRe().eval("levels(temp.dis[,\""+m.group(1)+"\"])").asStringArray();
	// //
	// System.out.println("-----------------------------------------------------------");
	// // System.out.println("classList:");
	// // for (String string : classList) {
	// // System.out.println(string);
	// // classes.add(string);
	// // }
	// while (m2.find()) {
	// classes.add(m2.group(1));
	// }
	// // System.out.println("match:"+m.group(0));
	// // System.out.println(m.group(1));
	// // System.out.println(m.group(2));
	// // System.out.println(m.group(3));
	// //
	// System.out.println("-----------------------------------------------------------");
	// if (result.containsKey(m.group(1)))
	// System.err.println("duplicate discretize attributes");
	// else {
	// //
	// System.out.println("result.put("+m.group(1).trim()+", new attrTypeNClass(\"Factor\",classes));");
	// result.put(m.group(1).trim(), new attrTypeNClass(
	// "Factor", classes));
	// }
	// } else
	// System.err
	// .println("wrong in discretize, type is not Factor");
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return result;
	// }

	// public void extractSummary(ranalysis r) {
	// r.getRe().eval("summary()");
	// }

	// public Structure extractStr(ranalysis r) {
	// ArrayList<String> frequnce = new ArrayList<>();
	// // Map<String, String> result=new HashMap<String, String>();
	// Map<String, attrTypeNClass> result = new HashMap<>();
	// result.clear();
	// frequnce.clear();
	// r.getT().clearText();
	// r.getRe().eval("str(data,list.len=ncol(data))");
	// String log = r.getT().getText();
	// int obsNum=r.getRe().eval("nrow(data)").asInt();;
	// try {
	// p = Pattern.compile("\\$\\s*(.*?)\\s*:\\s*(.*?) [^\\n]*");
	// m = p.matcher(log);
	// while (m.find()) {
	// // System.out.println("match:"+m.group(0));
	// if (result.containsKey(m.group(1).trim())) {// 以上正则保证不需要trim
	// // 但还是需要注意空格
	// // 以防改正则后出错
	// throw new Exception("key duplicate.");
	// } else {
	// ArrayList<String> classes = new ArrayList<>();
	// classes.clear();
	// if (m.group(2).trim().matches("(Factor|Ord.factor)")) {//
	// read.table好像只有Factor
	// // int num这三种
	// String[] classList = r.getRe()
	// .eval("levels(data[,\"" + m.group(1) + "\"])")
	// .asStringArray();
	// for (String string : classList) {
	// classes.add(string);
	// }
	// }
	// frequnce.add(m.group(1));
	// result.put(m.group(1).trim(), new attrTypeNClass(m.group(2)
	// .trim(), classes));
	// }
	// }
	// } catch (Exception e) {
	// System.out.println("InformationExtractor.getOptions():" + e);
	// }
	// return new Structure(result, frequnce, r.getT().getText(),obsNum);
	// }

	public static void main(String[] args) {
		String options = "header = \"T\" , sep= \",\" , na.strings = \"NA,NULL\"";
		InformationExtractor x = new InformationExtractor();
		Options op = x.getOptions(options, "=", ",");
		op.display();
	}

}