package com.javalec.ex.command;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class SplitFiles {
	public void splitFile(String nFilePath, String nFileName, InputStream fi,long maxFileSize){
	      try {
	            int readCnt = 0;

	            int totCnt = 0;

	            int fileIdx = 0;

	            BufferedInputStream bfi = new BufferedInputStream(fi);

	            byte[] readBuffer = new byte[2048];

	            File nFile = new File(nFilePath + nFileName);
	            FileOutputStream fo = new FileOutputStream(nFilePath+ nFileName+(++fileIdx)+".txt");
	            do {
	                   readCnt = bfi.read(readBuffer); 
	                   if(readCnt == -1){
	                         break;
	                   }
	                   fo.write(readBuffer,0,readCnt);
	                   totCnt += readCnt;
	                   System.out.println(totCnt);
	                   if(totCnt/maxFileSize>0){
	                      totCnt = 0;
	                         fo.flush();
	                         fo.close();
	                         File nfile = new File(nFilePath+ nFileName+(++fileIdx)+".txt");
	                         System.out.println(nfile.getName());
	                         fo = new FileOutputStream(nfile);
	                   }
	            } while (true);
	            fi.close();
	            fo.flush();
	            fo.close();
	     } catch (Exception e) {
	            e.printStackTrace();
	     }
	     System.out.println("##########분할완료##########");
	   }
}
