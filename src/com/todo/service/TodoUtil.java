package com.todo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.*;
import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;
public class TodoUtil {

	public static void createItem(TodoList list) {

		String title, desc, category, due_date = null, with_who;
		int priority;
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in)); //선언

		System.out.println("------일정 추가------");
		
		try {
			System.out.print("[제목] ");
			title = bf.readLine();
			if (list.isDuplicate(title)) {
				System.out.printf("제목은 중복될 수 없습니다 ㅠ\n");
				return;
			}

			System.out.print("[설명] ");
			desc = bf.readLine();
			
			System.out.print("[카테고리] ");
			category = bf.readLine();
			
			System.out.print("[마감 일자] ");
			due_date = bf.readLine();
//		
//			System.out.print("[참석자] ");
//			with_who = bf.readLine();
//			
//			System.out.print("[우선순위] ");
//			priority = Integer.parseInt(bf.readLine());

			TodoItem t = new TodoItem(title, desc, category, due_date);
			
			if(list.addItem(t) > 0) {
				System.out.println("\" <"+title+"> "+desc+" \"이(가) 저장되었습니다 :)");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void deleteItem(TodoList l) {

		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in)); //선언
		Scanner sc = new Scanner(System.in); // Scanner 객체 생성
		
		System.out.println("------일정 삭제------");

		int del_num;
		try {
			List<TodoItem> list = l.getList();
			System.out.println("삭제할 일정의 번호를 입력하세요");
			System.out.print("[번호] ");
			del_num = Integer.parseInt(bf.readLine());
			if(l.deleteItem(del_num)>0) System.out.println("삭제되었습니다.");
			else System.out.println("삭제할 항목이 없습니다.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void updateItem(TodoList l) {

		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in)); //선언

		System.out.println("------일정 수정------");
		String title;
		try {
			List<TodoItem> list = l.getList();
			System.out.println("수정할 일정의 번호를 입력하세요");
			System.out.print("[번호] ");
			int edit_num = Integer.parseInt(bf.readLine());
			
			System.out.print("[변경할 제목] ");
			String new_title = bf.readLine().trim();
			if (l.isDuplicate(new_title)) {
				System.out.printf("제목은 중복될 수 없습니다 ㅠ\n");
				return;
			}
			System.out.print("[변경할 설명] ");
			String new_description = bf.readLine().trim();
			
			System.out.print("[변경할 카테고리] ");
			String new_category = bf.readLine().trim();
			
			System.out.print("[변경할 마감 일자] ");
			String new_due_date = bf.readLine().trim();
			
			TodoItem t = new TodoItem(new_title, new_description, new_category, new_due_date);
			t.setId(edit_num);
			if(l.updateItem(t) > 0) {
				System.out.println("해당 일정이 \" <"+new_title+"> "+new_description+" \"로 변경되었습니다 :)");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void completeItem(TodoList l, int id) {
		if(l.completeItem(id) > 0) {
			System.out.println("완료 체크하였습니다. :)");
		}
		else System.out.println("올바른 아이디가 아닙니다.");
	}

	public static void listAll(TodoList l) {
		int count = 0;
		System.out.println("------전체 목록------");
		System.out.println("[총 "+l.getCount()+"개의 일정]");
		for (TodoItem item : l.getList()) {
			System.out.println(item.toString());
		}
	}
	
	public static void listAll(TodoList l, String orderby, int ordering) {
		System.out.println("------전체 목록------");
		System.out.println("[총 "+l.getCount()+"개의 일정]");
		for (TodoItem item : l.getOrderedList(orderby, ordering)) {
			System.out.println(item.toString());
		}
	}
	
	public static void listAll(TodoList l, int is_completed) {
		for (TodoItem item : l.getList(is_completed)) {
			System.out.println(item.toString());
		}
	}

	
	public static void findKeyword (TodoList l, String keyword) {
		int count = 0;
		System.out.println("------검색 결과------");
		for(TodoItem item : l.getList(keyword)) {
			count ++;
			System.out.println(item.toString());
		}
		System.out.println("총 "+count+"개의 일정을 찾았습니다.");
	}
	
	public static void findCateKeyword (TodoList l, String keyword) {
		int count = 0;
		System.out.println("------검색 결과------");
		for(TodoItem item : l.getListCategory(keyword)) {
			count ++;
			System.out.println(item.toString());
		}
		System.out.println("총 "+count+"개의 일정을 찾았습니다.");
	}
	
	public static void listCate (TodoList l) {
		int count = 0;
		for(String item : l.getCategories()) {
			System.out.println(item + " ");
			count++;
		}
		System.out.println("총 "+count+"개의 카테고리가 등록되어 있습니다.");
	}
	
//	public static void saveList(TodoList l, String filename) {
//	try {
//		String filePath = Paths.get(".").toAbsolutePath().toString() +"/"+ filename;
//		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
//		for(TodoItem item : l.getList()) {
//			bw.write(item.toSaveString());
//            bw.flush();
//		}
//        bw.close();
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//}

//public static void loadList(TodoList l, String filename) throws FileNotFoundException {
//	
//	try {
//		String filePath = Paths.get(".").toAbsolutePath().toString() +"/"+ filename;
//		File file = new File(filePath);
//		
//		if(file.exists()) {
//            BufferedReader br = new BufferedReader(new FileReader(file));
//
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                StringTokenizer st = new StringTokenizer(line, "##");
//                String category = st.nextToken().trim();
//                String title = st.nextToken().trim();
//                String desc = st.nextToken().trim();
//                String due_date = st.nextToken().trim();
//                String current_date = st.nextToken().trim();
//                int is_completed = Integer.parseInt(st.nextToken().trim());
//
//                l.addItem(new TodoItem(title,desc,current_date,category,due_date,is_completed));
//            }
//            System.out.println("파일 로딩 완료!");
//            br.close();
//        }
//        else {
//            System.out.println("파일 없음");
//        }
//	} catch (IOException e) {
//		e.printStackTrace();
//	} catch (NoSuchElementException e){
//		e.printStackTrace();
//    } 
//}
}
