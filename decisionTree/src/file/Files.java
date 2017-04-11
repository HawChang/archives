package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import util.DataSet;

public class Files {
	public static DataSet readFile(String addr , boolean header , String tar, String attr,boolean withNA, String NA){
		DataSet dataSet = null;
		try{
			BufferedReader reader=new BufferedReader(new FileReader(new File(addr)));
			String tempString;
			Random rand=new Random();
			if(header) {
				tempString = reader.readLine();
				if(tempString!=null){
					String[] s = tempString.split(",");
					dataSet=new DataSet(s.length,s,NA);
					if(dataSet.setTarNAttr(tar,attr));
					else {
						reader.close();
						throw new Exception("wrong in tar or attr.");
					}
				}else {
					System.err.println("file no header");
				}
			}
			tempString= reader.readLine();
			if(tempString!=null){
				//String[] =tempString.split(",");
				if(dataSet==null) {
					dataSet=new DataSet(tempString.split(",").length,NA);
					if(dataSet.setTarNAttr(tar,attr));
					else {
						reader.close();
						throw new Exception("wrong in tar or attr.");
					}
				}
				while(tempString!=null){
					String[] values=tempString.split(",");
					if(withNA){
						int roll=rand.nextInt(100);
						if(roll<20){
							int index;
							do{
								index=rand.nextInt(values.length);
							}while(index==dataSet.getTargetID());
							values[index]=NA;
						}
					}
					dataSet.addRecord(values);
					tempString = reader.readLine();
				}
			}
			reader.close();
			//if(dataSet.setTarNAttr(tar,attr));
			//else throw new Exception("wrong in tar or attr.");
			//return dataSet;
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return dataSet;
	}
	public static DataSet readFile(String addr , Boolean header, String tar,boolean withNA){
		return readFile(addr , header , tar, null,withNA,"NA");
	}
	public static DataSet readFile(String addr ,String NA, Boolean header, String tar){
		return readFile(addr , header , tar, null,false,NA);
	}
	public static DataSet readFile(String addr , Boolean header, String tar){
		return readFile(addr , header , tar, null,false,"NA");
	}
	public static DataSet readFile(String addr , String tar,String attr,boolean withNA){
		return readFile(addr , false , tar, attr,withNA,"NA");
	}
	public static DataSet readFile(String addr , String tar,String attr){
		return readFile(addr , false , tar, attr,false,"NA");
	}
	public static DataSet readFile(String addr , String tar,boolean withNA){
		return readFile(addr , false , tar, null,withNA,"NA");
	}
	public static DataSet readFile(String addr , String tar){
		return readFile(addr , false , tar, null,false,"NA");
	}
	public static DataSet readFile(String addr){
		return readFile(addr, false , null , null,false,"NA");
	}
}
