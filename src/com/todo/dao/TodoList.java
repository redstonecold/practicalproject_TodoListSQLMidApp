package com.todo.dao;

import java.sql.Statement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.todo.service.DbConnect;
import com.todo.service.TodoSortByDate;
import com.todo.service.TodoSortByName;

public class TodoList {
	private List<TodoItem> list = new ArrayList<>();
	Connection conn;

	public TodoList() {
		this.conn = DbConnect.getConnection();
	}
	
	public void importData(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader((filename)));
			String line;
			String sql = "insert into list (title, memo, category, current_date, due_date)"
				+ " values (?,?,?,?,?);";
			int records = 0;
			while((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "##");
				String category = st.nextToken();
				String title = st.nextToken();
				String description = st.nextToken();
				String due_date = st.nextToken();
				String current_date = st.nextToken();
				
				PreparedStatement pstmt = conn.prepareStatement(sql); //java.sql.PreparedStatement : SQL 구문 전달하는 역할, Statement 객체 기능 향상
				pstmt.setString(1, title);
				pstmt.setString(2, description);
				pstmt.setString(3, category);
				pstmt.setString(4, current_date);
				pstmt.setString(5, due_date);
				int count = pstmt.executeUpdate();
				if(count > 0) records++;
				pstmt.close();
			}
			System.out.println(records + " records read!!");
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int addItem(TodoItem t) {
		String sql = "insert into list (title, memo, category, current_date, due_date)"
				+ "values (?,?,?,?,?)";
		PreparedStatement pstmt;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setString(3, t.getCategory());
			pstmt.setString(4, t.getCurrent_date());
			pstmt.setString(5, t.getDue_date());
			count = pstmt.executeUpdate(); //해당 statement에 영향을 받은 record의 수가 return됨 
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public int deleteItem(int index) {
		String sql = "delete from list where id = ?;";
		PreparedStatement pstmt;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, index);
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public int updateItem(TodoItem t) {
		String sql = "update list set title=?, memo=?, category=?, current_date=?, due_date=?"
				+ " where id=?;";
		PreparedStatement pstmt;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setString(3, t.getCategory());
			pstmt.setString(4, t.getCurrent_date());
			pstmt.setString(5, t.getDue_date());
			pstmt.setInt(6, t.getId());
			count = pstmt.executeUpdate(); //해당 statement에 영향을 받은 record의 수가 return됨 
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int completeItem(int id) {
		String sql = "update list set is_completed = 1 where id = ?;";
		PreparedStatement pstmt;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			count = pstmt.executeUpdate(); //해당 statement에 영향을 받은 record의 수가 return됨 
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public ArrayList<TodoItem> getList() {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "select * from list";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_completed = rs.getInt("is_completed");
				TodoItem t = new TodoItem(title, description, current_date, category, due_date, is_completed);
				t.setId(id);
				list.add(t);
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getList(String keyword) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		keyword = "%" + keyword + "%";
		try {
			String sql = "select * from list where title like ? or memo like ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			pstmt.setString(2, keyword);
			ResultSet rSet = pstmt.executeQuery();
			while (rSet.next()) {
				int id = rSet.getInt("id");
				String category = rSet.getString("category");
				String title = rSet.getString("title");
				String description = rSet.getString("memo");
				String due_date = rSet.getString("due_date");
				String current_date = rSet.getString("current_date");
				int is_completed = rSet.getInt("is_completed");
				TodoItem t = new TodoItem(title, description, current_date, category, due_date, is_completed);
				t.setId(id);
				list.add(t);
			}
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getList(int select_id) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		try {
			String sql = "select * from list where is_completed like ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, select_id);
			ResultSet rSet = pstmt.executeQuery();
			while (rSet.next()) {
				int id = rSet.getInt("id");
				String category = rSet.getString("category");
				String title = rSet.getString("title");
				String description = rSet.getString("memo");
				String due_date = rSet.getString("due_date");
				String current_date = rSet.getString("current_date");
				int is_completed = rSet.getInt("is_completed");
				TodoItem t = new TodoItem(title, description, current_date, category, due_date, is_completed);
				t.setId(id);
				list.add(t);
			}
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int getCount() {
		Statement stmt;
		int count = 0;
		try {
			stmt = conn.createStatement();
			String sql = "select count(id) from list;";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			count = rs.getInt("count(id)");
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public ArrayList<String> getCategories(){
		ArrayList<String> list = new ArrayList<String> ();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "select distinct category from list";
			ResultSet rSet = stmt.executeQuery(sql);
			while(rSet.next()) {
				list.add(rSet.getString("category"));
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getListCategory(String keyword){
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		try {
			String sql = "select * from list where category = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			ResultSet rSet = pstmt.executeQuery();
			while(rSet.next()) {
				int id = rSet.getInt("id");
				String category = rSet.getString("category");
				String title = rSet.getString("title");
				String description = rSet.getString("memo");
				String due_date = rSet.getString("due_date");
				String current_date = rSet.getString("current_date");
				int is_completed = rSet.getInt("is_completed");
				TodoItem t = new TodoItem(title, description, current_date, category, due_date, is_completed);
				t.setId(id);
				list.add(t);
			}
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getOrderedList(String orderby, int ordering){
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "select * from list order by " + orderby;
			if (ordering == 0) 
				sql += " desc";
			ResultSet rSet = stmt.executeQuery(sql);
			while(rSet.next()) {
				int id = rSet.getInt("id");
				String category = rSet.getString("category");
				String title = rSet.getString("title");
				String description = rSet.getString("memo");
				String due_date = rSet.getString("due_date");
				String current_date = rSet.getString("current_date");
				int is_completed = rSet.getInt("is_completed");
				TodoItem t = new TodoItem(title, description, current_date, category, due_date, is_completed);
				t.setId(id);
				list.add(t);
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void sortByName() {
		Collections.sort(list, new TodoSortByName());

	}

	public void listAll() {
		for (TodoItem myitem : list) {
			System.out.println("<"+ myitem.getTitle() + "> " + myitem.getDesc() + " - " + myitem.getCurrent_date());
		}
	}
	
	public void reverseList() {
		Collections.reverse(list);
	}

	public void sortByDate() {
		Collections.sort(list, new TodoSortByDate());
	}

	public int indexOf(TodoItem t) {
		return list.indexOf(t);
	}

	public Boolean isDuplicate(String title) {
		String sql = "select * from list where title = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
