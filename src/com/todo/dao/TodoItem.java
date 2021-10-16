package com.todo.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoItem {
	private int id;
	private String title;
    private String desc;
    private String current_date;
    private String category;
    private String due_date;
    private int is_completed;
	private String with_who;
    private int priority;
    


    public TodoItem(String title, String desc, String category, String due_date, String with_who, int priority){
        this.title=title;
        this.desc=desc;
        Date from = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String to = transFormat.format(from);
        this.current_date = to;
        this.category = category;
        this.due_date = due_date;
        this.with_who = with_who;
        this.priority = priority;
    }
    
    public TodoItem(String title, String desc, String current_date, String category, String due_date, String with_who, int priority, int is_completed){
        this.title=title;
        this.desc=desc;
        this.current_date = current_date;
        this.category = category;
        this.due_date = due_date;
        this.with_who = with_who;
        this.priority = priority;
        this.is_completed = is_completed;
    }
    
    public String toString() {
    	String reString = "";
    	
    	if(priority == 1) reString += "[긴급] ";
    	else if(priority == 2) reString += "[보통] ";
    	else reString += "[여유] ";
    	reString += "<"+category+ "> "+id + ". "+title;
    	if(is_completed == 1) reString += "[V]";
    	reString += " : "+desc+" (마감 : "+due_date+") - "+current_date;
    	if(!with_who.equals("나")) reString += " 참석자 : "+with_who;
    	
    	return reString;
    }
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

	public int getIs_completed() {
		return is_completed;
	}

	public void setIs_completed(int is_completed) {
		this.is_completed = is_completed;
	}
	
	public String getWith_who() {
		return with_who;
	}

	public void setWith_who(String with_who) {
		this.with_who = with_who;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
//  public String toSaveString() {
//	return category + "##" +title + "##" + desc + "##" + due_date + "##" + current_date + "##" +"\n";
//}
	
    
}
