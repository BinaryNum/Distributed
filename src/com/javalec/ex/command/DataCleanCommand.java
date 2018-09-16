package com.javalec.ex.command;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class DataCleanCommand implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

		String path = request.getRealPath("pdf");
		System.out.println(path);
		int size = 1024 * 1024 * 10; //10M
		String file = "";
		String oriFile = "";
		try{
			MultipartRequest multi = new MultipartRequest(request, path, size, "UTF-8", new DefaultFileRenamePolicy());
			
			Enumeration files = multi.getFileNames();
			String str = (String)files.nextElement();
			file = multi.getFilesystemName(str);
			oriFile = multi.getOriginalFileName(str);
			HttpSession session=request.getSession();
			
			
			 PdfReader reader;
		        
		        try {
		        	
		            reader = new PdfReader(path+"/"+file);
		            String textFromPage=null;
		            BufferedWriter bw = new BufferedWriter(new FileWriter(path+"/"+file+".txt"));
		            session.setAttribute("file", file+".txt");
		            // pageNumber = 1
		            for(int i = 2;i<reader.getNumberOfPages();i++) {
		            textFromPage = PdfTextExtractor.getTextFromPage(reader, i);

		            System.out.println(textFromPage);
		            		bw.write(textFromPage);
		            }
					bw.close();
		            reader.close();

		        } catch (IOException e) {
		            e.printStackTrace();
		        }

			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
