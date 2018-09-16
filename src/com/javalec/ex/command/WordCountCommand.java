package com.javalec.ex.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javalec.ex.dao.WordDao;
import com.javalec.ex.dto.Word;


public class WordCountCommand implements Command {
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		long start = new Date().getTime();
		WordDao dao = new WordDao();
		HashMap<String, Integer> word = new HashMap<>();
		String buf="";
		HttpSession session=request.getSession();
		Scanner filein = null;
		try
		{
			File f = new File(request.getRealPath("pdf")+"/"+session.getAttribute("file"));
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
		dao.delete();
		dao.write(word);
		ArrayList<Word> dtos = new ArrayList<>();
		dtos = dao.list();
		request.setAttribute("list", dtos);
		long time = new Date().getTime()-start;
		System.out.println("Time : " + time);
	}

}
