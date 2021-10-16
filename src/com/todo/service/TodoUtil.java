package com.todo.service;

import java.awt.desktop.PrintFilesEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import com.google.gson.Gson;
import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {

	public static void createItem(TodoList list) {

		String title, desc, category, due_date, with_who, repeat;
		int priority, repeat_times, repeat_interval;
		boolean is_add = false;
		List<TodoItem> tlist = new ArrayList<>();
		
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
			
			System.out.print("[마감 일자] (YYYY/MM/DD) ");
			due_date = bf.readLine();
		
			System.out.print("[참석자] (참석자 없을 시 enter) ");
			with_who = bf.readLine();
			if(with_who.equals("") || with_who.equals(" ")) with_who = "나";
//			System.out.println("with_who : "+with_who);
			
			System.out.print("[우선순위] (긴급 : 1, 보통 : 2, 여유 : 3) ");
			priority = Integer.parseInt(bf.readLine());
			while(priority < 1 || priority > 3) {
				System.out.println("잘 못 입력하셨습니다.");
				System.out.print("[우선순위] (긴급 : 1, 보통 : 2, 여유 : 3) ");
				priority = Integer.parseInt(bf.readLine());
			}
			
			System.out.print("반복 일정 (Y/N) ");
			repeat = bf.readLine();
			while (!repeat.equals("Y") && !repeat.equals("y") && !repeat.equals("N") && !repeat.equals("n")) {
				System.out.println("잘못 입력하셨습니다.");
				System.out.print("반복 일정 (Y/N) ");
				repeat = bf.readLine();
			}
			
			if(repeat.equals("Y") || repeat.equals("y")) {
				System.out.print("반복 횟수 : ");
				repeat_times = Integer.parseInt(bf.readLine());
				while (repeat_times < 1) {
					System.out.print("잘못 입력하셨습니다.");
					System.out.print("반복 횟수 : ");
					repeat_times = Integer.parseInt(bf.readLine());
				}
				System.out.print("반복 주기 : ");
				repeat_interval = Integer.parseInt(bf.readLine());
				while (repeat_interval < 1) {
					System.out.println("잘못 입력하셨습니다.");
					System.out.print("반복 주기 : ");
					repeat_interval = Integer.parseInt(bf.readLine());
				}
				
				String[] parsing = due_date.split("/",3);
				int[] parsing_int = new int[3];
				for(int i=0; i<3; i++) {
					parsing_int[i] = Integer.parseInt(parsing[i]);
				}
				
				System.out.println("[추가된 날짜]");
				for(int i=0; i<repeat_times; i++) {
					System.out.println(parsing_int[1]);
					System.out.println(parsing_int[2]);
					switch (parsing_int[1]) {
						case 1: case 3: case 5: case 7: case 8: case 10: {
							if(parsing_int[2]+repeat_interval > 31) {
								parsing_int[1] += 1;
								parsing_int[2] = parsing_int[2]+repeat_interval - 31;
							}
							else parsing_int[2] = parsing_int[2]+repeat_interval;
							break;
						}
						case 4: case 6: case 9: case 11: {
							if(parsing_int[2]+repeat_interval > 30) {
								parsing_int[1] += 1;
								parsing_int[2] = parsing_int[2]+repeat_interval - 30;
							}
							else parsing_int[2] = parsing_int[2]+repeat_interval;
							break;
						}
						case 12 : {
							if(parsing_int[2]+repeat_interval > 31) {
								parsing_int[0] += 1;
								parsing_int[1] = 1;
								parsing_int[2] = parsing_int[2]+repeat_interval - 31;
							}
							else parsing_int[2] = parsing_int[2]+repeat_interval;
							break;
						}
						default: {
							int feb_days;
							if(parsing_int[0]%400 == 0) feb_days = 29;
							else if(parsing_int[0]%100 == 0) feb_days = 28;
							else if(parsing_int[0]%4 == 0) feb_days = 29;
							else feb_days = 28;
							
							if(parsing_int[2]+repeat_interval > feb_days) {
								parsing_int[1] += 1;
								parsing_int[2] = parsing_int[2]+repeat_interval - feb_days;
							}
							else parsing_int[2] = parsing_int[2]+repeat_interval;
							break;
						}
					}
					if(parsing_int[1] < 10 && parsing_int[2] < 10) 
						due_date = Integer.toString(parsing_int[0])+"/0"+Integer.toString(parsing_int[1])+"/0"+Integer.toString(parsing_int[2]);
					else if(parsing_int[1] < 10) 
						due_date = Integer.toString(parsing_int[0])+"/0"+Integer.toString(parsing_int[1])+"/"+Integer.toString(parsing_int[2]);
					else if(parsing_int[2] < 10) 
						due_date = Integer.toString(parsing_int[0])+"/"+Integer.toString(parsing_int[1])+"/0"+Integer.toString(parsing_int[2]);
					
					System.out.println(due_date);
					TodoItem t = new TodoItem(title, desc, category, due_date, with_who, priority);
					tlist.add(t);
				}
			}
			else {
				TodoItem t = new TodoItem(title, desc, category, due_date, with_who, priority);
				tlist.add(t);
			}
			
			for(TodoItem tItem : tlist) {
				if(list.addItem(tItem) > 0) {
					is_add = true;
				}
			}
			
			if(is_add) System.out.println("\" <"+title+"> "+desc+" \"이(가) 저장되었습니다 :)");
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
			
			System.out.print("[변경할 마감 일자] (YYYY/MM/DD) ");
			String new_due_date = bf.readLine().trim();
			
			System.out.print("[변경할 참석자] (참석자 없을 시 enter) ");
			String new_with_who = bf.readLine();
			if(new_with_who.equals("") || new_with_who.equals(" ")) new_with_who = "나";
			
			System.out.print("[변경할 우선순위] (긴급 : 1, 보통 : 2, 여유 : 3) ");
			int new_priority = Integer.parseInt(bf.readLine());
			while(new_priority < 1 || new_priority > 3) {
				System.out.println("잘못 입력하셨습니다. (긴급 : 1, 보통 : 2, 여유 : 3) ");
				new_priority = Integer.parseInt(bf.readLine());
			}
			
			TodoItem t = new TodoItem(new_title, new_description, new_category, new_due_date, new_with_who, new_priority);
			t.setId(edit_num);
			if(l.updateItem(t) > 0) {
				System.out.println("해당 일정이 \" <"+new_title+"> "+new_description+" \"로 변경되었습니다 :)");
			}
		} catch (Exception e) {
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
	
	public static void exportJson(TodoList l){
		Scanner sc = new Scanner(System.in);
		System.out.print("내보내기 파일 형식 지정 (1: .json, 2: .txt) ");
		int file_type = sc.nextInt();
		while(file_type < 1 || file_type >2) {
			System.out.println("잘못 입력하셨습니다.");
			System.out.print("내보내기 파일 형식 지정 (1: .json, 2: .txt) ");
			file_type = sc.nextInt();
		}
		l.exportJson(file_type);
	}
}
