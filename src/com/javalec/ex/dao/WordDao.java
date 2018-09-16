package com.javalec.ex.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.javalec.ex.dto.Word;


public class WordDao {
DataSource dataSource;
	
	public WordDao() {
		// TODO Auto-generated constructor stub
		
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/myoracle");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public Map<String, Integer> wordCount(String[] strings) {
		  Map<String, Integer> map = new HashMap<String, Integer> ();
		  for (String s:strings) {
		    
		    if (!map.containsKey(s)) {  // first time we've seen this string
		      map.put(s, 1);
		    }
		    else {
		      int count = map.get(s);
		      map.put(s, count + 1);
		    }
		  }
		  return map;
		}

	
		//맵리듀스 자료구조 필요 (파라미터)
	public void write(HashMap<String, Integer> map) {
		// TODO Auto-generated method stub
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Set<String> word = map.keySet();
		Iterator<String> key = word.iterator();
		
		int count = 0;
		//for문 필요
		try {
			connection = dataSource.getConnection();
			String query = "insert into word (wId, word, count) values (word_seq.nextval, ?, ?)";
			preparedStatement = connection.prepareStatement(query);
			while(key.hasNext()) {
				String buf = key.next();
				preparedStatement.setString(1, buf);
				preparedStatement.setInt(2, map.get(buf));		
				int rn = preparedStatement.executeUpdate();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} 
			
		
		try {
				if(preparedStatement != null) preparedStatement.close();
				if(connection != null) connection.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		
		
	}
	
public ArrayList<Word> list() {
		
		ArrayList<Word> dtos = new ArrayList<Word>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = dataSource.getConnection();
			
			String query = "select * from word order by count desc";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				int wId = resultSet.getInt("wId");
				String word = resultSet.getString("word");
				int count = resultSet.getInt("count");
				
				Word dto = new Word();
				dto.setCount(count);
				dto.setwId(wId);
				dto.setWord(word);
				dtos.add(dto);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null) resultSet.close();
				if(preparedStatement != null) preparedStatement.close();
				if(connection != null) connection.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return dtos;
	}

public void delete() {
	
	Connection connection = null;
	PreparedStatement preparedStatement = null;

	try {
		connection = dataSource.getConnection();
		
		String query = "delete from word";
		preparedStatement = connection.prepareStatement(query);
		int rn = preparedStatement.executeUpdate();
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	} finally {
		try {
			if(preparedStatement != null) preparedStatement.close();
			if(connection != null) connection.close();
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}
	}
	
}

}
