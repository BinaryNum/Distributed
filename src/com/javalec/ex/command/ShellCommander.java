package com.javalec.ex.command;
import java.io.BufferedReader;

import java.io.InputStream;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;




public class ShellCommander {




   public static void shellCmd(String command) throws Exception {
               Runtime runtime = Runtime.getRuntime();
               Process process = runtime.exec(command);
               InputStream is = process.getInputStream();
               InputStreamReader isr = new InputStreamReader(is);
               BufferedReader br = new BufferedReader(isr);
               String line;
               while((line = br.readLine()) != null) {

                              System.out.println(line);

               }
   }
   public static HashMap<String,Integer> shellCmdHash(String command)throws Exception {
       Runtime runtime = Runtime.getRuntime();
       Process process = runtime.exec(command);
       InputStream is = process.getInputStream();
       InputStreamReader isr = new InputStreamReader(is);
       Scanner sc = new Scanner(isr);
       HashMap<String,Integer> word = new HashMap<String,Integer>();
       while(sc.hasNext()) {
    	   word.put(sc.next(), sc.nextInt());
       }
       return word;
}
}
