package communal;


import java.io.File;
import java.io.PrintStream;

import grammer_analysis.Gr_analyze;
import lexical_analysis.Lexer;



public class Entry {
	public static void main(String[]args)
	{ 
		String src="test.cpp",dis="tokens.txt",grammer="grammar.txt",console="console.txt";
		try {
			File file=new File(console);
			if (!file.exists()) {
				file.createNewFile();
			PrintStream printStream=new PrintStream(file);
			System.setOut(printStream);
		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Lexer lexer=new Lexer(src);
		if(lexer.run_and_write_to(dis)) System.out.println("write to file \""+dis+"\" succeed.");
		else System.out.println("write to file \""+dis+"\" failed.");
		Gr_analyze gr_analyze=new Gr_analyze(lexer,grammer);
////		//gr_analyze.run();
////		//ArrayList<String> program=new ArrayList<>();
		gr_analyze.analyze(dis);		
		
	}

}
