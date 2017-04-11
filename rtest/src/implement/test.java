package implement;


import java.util.Scanner;

import javax.swing.JFileChooser;

import ranalysis.ranalysis;

public class test {
	public static void main(String[] args){
		ranalysis analysis=new ranalysis();
		test t=new test();
		analysis.chooseFile();
		//String addr=t.FileChoose();
		//analysis.RST();
		//analysis.decisionTree();
		//analysis.rules();
	}
	public String FileChoose(){
		//String des;
		JFileChooser fileChooser=new JFileChooser();
		fileChooser.setFileHidingEnabled(false);
		int type=fileChooser.showOpenDialog(null);
		if(type==JFileChooser.APPROVE_OPTION){
			return fileChooser.getSelectedFile().getName();
		}
		return null;
	}
}
