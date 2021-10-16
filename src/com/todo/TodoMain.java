package com.todo;

import java.util.Scanner;

import com.todo.dao.TodoList;
import com.todo.menu.Menu;
import com.todo.service.DbConnect;
import com.todo.service.TodoUtil;

public class TodoMain {
	
	public static void start() {
		
		try {
			Scanner sc = new Scanner(System.in);
			TodoList l = new TodoList();
			boolean quit = false;
			String keyword;
//			boolean isList = false;
//			l.importData("todolistmid.txt");
			do {
				Menu.prompt();
				String choice = sc.next();
				switch (choice) {

				case "add":
					TodoUtil.createItem(l);
					break;
				
				case "del":
					TodoUtil.deleteItem(l);
					break;
					
				case "edit":
					TodoUtil.updateItem(l);
					break;
				
				case "help" :
					Menu.displaymenu();
					break;
					
				case "ls":
					TodoUtil.listAll(l);
					break;

				case "ls_name":
					System.out.println("제목순으로 정렬하였습니다.");
					TodoUtil.listAll(l,"title",1);
					break;

				case "ls_name_desc":
					System.out.println("제목역순으로 정렬하였습니다.");
					TodoUtil.listAll(l,"title",0);
					break;
					
				case "ls_date":
					System.out.println("날짜순으로 정렬하였습니다.");
					TodoUtil.listAll(l,"due_date",1);
//					isList = true;
					break;
				
				case "ls_date_desc":
					System.out.println("날짜역순으로 정렬하였습니다.");
					TodoUtil.listAll(l,"due_date",0);
//					isList = true;
					break;
				
				case "ls_cate" :
					TodoUtil.listCate(l);
					break;
				
				case "find" :
					keyword = sc.next().trim();
					TodoUtil.findKeyword(l,keyword);
					break;
				
				case "find_cate" :
					keyword = sc.next().trim();
					TodoUtil.findCateKeyword(l, keyword);
					break;
				
				case "comp" :
					keyword = sc.next().trim();
					int id = Integer.parseInt(keyword);
					TodoUtil.completeItem(l, id);
					break;
				
				case "ls_comp" :
					TodoUtil.listAll(l,1);
					break;
				
				case "expert" :
					TodoUtil.exportJson(l);
					break;
					
					
				case "exit":
					DbConnect.closeConnection();
					quit = true;
					break;

				default:
					System.out.println("정확한 명령어를 입력하세요. (명령어 보기 - help)");
					break;
				}
				
			} while (!quit);

//			TodoUtil.saveList(l, filename);
//			System.out.println("목록이 " + filename + "에 저장되었습니다.");
			System.out.println("TodoList가 종료되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
