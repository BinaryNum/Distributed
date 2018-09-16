package com.javalec.ex.command;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RaspiThread extends Thread{
	DWordCountCommand co = new DWordCountCommand();
   String address;
   String fileName;
   int index;
   String resultFile;
   public RaspiThread(int index,String address,String fileName,String resultFile){
      this.index = index;
      this.address = address;
      this.fileName = fileName;
      this.resultFile = resultFile;
   }
   public void run(){
      Runtime rt = Runtime.getRuntime();
      Process proc = null;
      try{
    	  String command = "ssh "+address+" java -cp /home/pi/Downloads wordcount_raspi " + fileName+" "+resultFile;
         proc = rt.exec(command);
      }catch(Exception e){
    	  e.printStackTrace();
      }
      InputStream stdin = proc.getInputStream();
      InputStreamReader isr = new InputStreamReader(stdin);
      BufferedReader br = new BufferedReader(isr);
      String line;
      try {
         while ((line = br.readLine()) != null)
            if(line.contains("Complete!"))
               co.complete[index] = true; //public static
      } catch (Exception e) {

      }
   }
  
}