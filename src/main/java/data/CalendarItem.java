package data;

import java.time.LocalDateTime;

public class CalendarItem {
	
	public static enum Priority {IF_POSSIBLE, LOW, MEDIUM, URGENT, VERY_URGENT}
	
	private Priority priority;
	private LocalDateTime begin, due;
	private String descripition, tag;
	
	public CalendarItem(String description) {
		this.descripition = description;
	}
	
	public CalendarItem setBeginDate(LocalDateTime begin) {
		this.begin = begin;
		return this;
	}
	
	public CalendarItem setDueDate(LocalDateTime due) {
		this.due = due;
		return this;
	}
	
	public CalendarItem setTag(String tag) {
		this.tag = tag;
		return this;
	}
	
	public CalendarItem setDescripition(String descripition) {
		this.descripition = descripition;
		return this;
	}
	
	public CalendarItem setPriority(Priority priority) {
		this.priority = priority;
		return this;
	}

	public Priority getPriority() {
		return priority;
	}

	public LocalDateTime getBegin() {
		return begin;
	}

	public LocalDateTime getDue() {
		return due;
	}

	public String getDescripition() {
		return descripition;
	}

	public String getTag() {
		return tag;
	}
	
}
