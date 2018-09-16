package com.javalec.ex.command;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javalec.ex.dao.WordDao;
import com.javalec.ex.dto.Word;


public class DWordCountCommand implements Command {
	public static boolean complete[] = new boolean[3];

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		long start = new Date().getTime();
		
		HttpSession session=request.getSession();
		SplitFiles count = new SplitFiles();
		String filePath = request.getRealPath("pdf");
        String fileName = filePath+"/"+session.getAttribute("file");
        File file = new File(fileName);
        FileInputStream fi = null;
        try{
           fi = new FileInputStream(file);
        }catch(Exception e){
        }
        String nFilePath = request.getRealPath("pdf")+"/";
        String nFileName = "nfile0";
        count.splitFile(nFilePath, nFileName, fi,file.length()/4);
        System.out.println(nFilePath);
        //연결
        ShellCommander shell = new ShellCommander();
        String rasFilePath = "/home/pi/Downloads";
        String command = null;
        try {
        	command="scp "+nFilePath+"nfile02.txt "+"pi@172.30.1.33:"+rasFilePath;
        	shell.shellCmd(command);
        	command="scp "+nFilePath+"nfile03.txt "+"pi@172.30.1.13:"+rasFilePath;
        	shell.shellCmd(command);
        	command="scp "+nFilePath+"nfile04.txt "+"pi@172.30.1.56:"+rasFilePath;
        	shell.shellCmd(command);
        }catch(Exception e) {
        	System.out.println(e);
        }
        String s[]= {"pi@172.30.1.33","pi@172.30.1.13","pi@172.30.1.56"};
        for(int i=0;i<3;i++){
            RaspiThread raspi = new RaspiThread(i,s[i],rasFilePath+"/nfile0"+(i+2)+".txt",rasFilePath+"/result"+(i+2)+".txt");
            raspi.start();
         }
        HashMap<String,Integer> word = new HashMap<>();
        
        Scanner filein = null;
        String buf;
		try
		{
			File f = new File(nFilePath+"nfile01.txt");
			filein = new Scanner(f);
		}catch(FileNotFoundException e){
			System.out.println("파일 없음 "+e.toString());
			throw(new RuntimeException());
		}
		while(filein.hasNext())
		{
			buf = filein.next();
			if(buf.matches("[a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")){ 
				if(word.get(buf)==null){
					word.put(buf,1);
				}
				else{
					word.put(buf, (int)word.get(buf)+1);
				}
			  }
		}
        /* while(true){
            if(complete[0]==true && complete[1]==true && complete[2]==true)
               break;
         }*/
        System.out.println("complete");
        
        
        
        try {
        	command="ssh pi@172.30.1.33 cat /home/pi/Downloads/result2.txt";
        	word = reduce(word,shell.shellCmdHash(command));
        	command="ssh pi@172.30.1.13 cat /home/pi/Downloads/result3.txt";
        	word = reduce(word,shell.shellCmdHash(command));
        	command="ssh pi@172.30.1.56 cat /home/pi/Downloads/result4.txt";
        	word = reduce(word,shell.shellCmdHash(command));
        }catch(Exception e) {
        	System.out.println(e);
        }
        WordDao dao = new WordDao();
        dao.delete();
        dao.write(word);
        ArrayList<Word> dtos = new ArrayList<>();
		dtos = dao.list();
		request.setAttribute("list", dtos);
		long time = new Date().getTime()-start;
		System.out.println("Time : " + time);
        
	}
	public HashMap<String,Integer> reduce(HashMap<String,Integer> word1,HashMap<String,Integer> word2){
		Set key = word2.keySet();
		for(Iterator<String> iterator=key.iterator();iterator.hasNext();) {
			String keyName = iterator.next();
			if(word1.get(keyName)==null) {
				word1.put(keyName, word2.get(keyName));
			}
			else {
				word1.put(keyName,word1.get(keyName)+word2.get(keyName));
			}
		}
		
		return word1;
	}
}
