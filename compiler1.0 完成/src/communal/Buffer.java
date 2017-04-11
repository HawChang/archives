package communal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Buffer {
	BufferedReader reader=null;
	char[] buf=new char[2*BOUNDARY];
	private int pointer;
	public Buffer() {
		// TODO Auto-generated constructor stub
		pointer=0;
	}
	public void open(String dis) {
		pointer=0;
		try{
		reader=new BufferedReader(new InputStreamReader(new FileInputStream(new File(dis))));
		System.out.println("open file \""+dis+"\" successfully.");
		refill(BUFFER_1);
		refill(BUFFER_2);
		}
		catch(Exception e){
			System.out.println("open file failed.");
			e.printStackTrace();
		}
	}
	public void refill(int dis){
		int temp,i;
	    if(dis==BUFFER_1){
	    	for(i=0;i<BOUNDARY;i++){
	    		try {
	    			temp=reader.read();
	    			if(temp!=-1) buf[i]=(char)temp;
	    			else buf[i]=EOF;
	    		}catch(IOException e){
	    			e.printStackTrace();
	    		}
	    	}
	    }
	    else if(dis==BUFFER_2){
	    	for(i=BOUNDARY;i<2*BOUNDARY;i++)
	    	{
	    		try {
	    			temp=reader.read();
	    			if(temp!=-1) buf[i]=(char)temp;
	    			else buf[i]=EOF;
	    		}catch(IOException e){
	    			e.printStackTrace();
	    		}
	    	}
	    }
	    else System.out.println("Buffer.refill()");
	}
    public void display(int dis){
   	   int i;
       if(dis==BUFFER_1){
    	   System.out.println("\nbuffer1:------------------------------------------------------");
    	   for(i=0;i<BOUNDARY;i++)
    	   {
    		   if(buf[i]==EOF) {
    			   System.out.print("!(this is the end)");
    			   break;
    		   }
    		   else System.out.print(buf[i]);
    	   }
    	   //if(buf[i-1]==EOF) System.out.println("!(this is the end)");
    	   System.out.println("||\n-------------------------------------------------------------");
       }
       else if(dis==BUFFER_2){
    	   System.out.println("\nbuffer2:------------------------------------------------------");
    	   for(i=BOUNDARY;i<2*BOUNDARY;i++)
    	   {
    		   if(buf[i]==EOF) {
    			   System.out.print("!(this is the end)");
    			   break;
    		   }
    		   else System.out.print(buf[i]);
    	   }
    	   //if(buf[i-1]==EOF) System.out.println("!(this is the end)");
    	   System.out.println("||\n-------------------------------------------------------------");
       }
    	   else System.out.println("err");
    }
    public void display(){
       display(BUFFER_1);
       display(BUFFER_2);
       System.out.println("pointer(at "+pointer+"):"+buf[pointer]);
    }
    public boolean hasnext(){
        if(buf[pointer]==EOF) return false;
        else return true;
    }
    public char nextis(){
        return buf[(pointer)%(2*BOUNDARY)];
    }
    public char getnext(){
    	char temp;
    	temp=buf[pointer];
    	if(temp!=EOF) {
    		pointer++;
    		if (pointer==BOUNDARY){
    			
    			refill(BUFFER_1);
    		}
    		else if(pointer==2*BOUNDARY){
    			pointer=0;
    			refill(BUFFER_2);
    		}
    	}
    	return temp;
    }
	private static final int BUFFER_1=1;
	private static final int BUFFER_2=2;
	private static final int BOUNDARY=200;
	public  static final char EOF='\0';
}
