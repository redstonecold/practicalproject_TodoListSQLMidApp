package com.todo.menu;
public class Menu {

    public static void displaymenu()
    {
        System.out.println("add - 일정 추가");
        System.out.println("del - 일정 삭제");
        System.out.println("edit - 일정 수정");
        System.out.println("ls - 목록 보기");
        System.out.println("ls_name - 제목순으로 정렬");
        System.out.println("ls_name_desc - 제목역순으로 정렬");
        System.out.println("ls_date - 날짜순 정렬");
        System.out.println("ls_date_desc - 날짜역순 정렬");
        System.out.println("ls_cate - 카테고리 목록 보기");
        System.out.println("find <키워드> - 제목 또는 내용이 해당 키워드를 포함하고 있는 일정 검색");
        System.out.println("find_cate <키워드> - 카테고리가 해당 키워드를 포함하고 있는 일정 검색");
        System.out.println("comp <id> - id번의 일정을 완료 표시");
        System.out.println("ls_comp - 완료한 일정 검색");
        System.out.println("exit - 종료");
    }
    
    public static void prompt()
    {
        System.out.print("\nCommand > ");
    }
}
