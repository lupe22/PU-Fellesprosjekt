package calendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import room_booking.RoomBuilder;
import dbms.DataBaseManager;
import user.User;
import user.UserBuilder;

/**
 * This class is used to model a calendar with several Entries and users
 * The class is immutable. That means no instance of this class can change any of its attributes.
 */
public class Calendar {
	private final Entry[] entries;
	private final User[] users;
	
	/**
	 * 
	 * @param entries: the entries of this calendar.
	 * @param users; the users belonging to this calendar.
	 */
	public Calendar(Entry[] entries, User[] users){
		this.entries = entries;
		this.users = users;
	}
	
	/**
	 * 
	 * @return a copy entries of this calendar in an array.
	 */
	public Entry[] getEntries() {
		Entry[] clonedList = new Entry[entries.length];
		for(int i = 0; i < clonedList.length; i++){
			clonedList[i] = entries[i].clone();
		}
		return clonedList;
	}
	
	/**
	 * 
	 * @return a copy of the users of this calendar in an array.
	 */
	public User[] getUsers() {
		User[] clonedList = new User[users.length];
		for(int i = 0; i < clonedList.length; i++){
			clonedList[i] = users[i].clone();
		}
		return clonedList;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Calendar for users [");
		for(User u: users){
			sb.append(u.toString());
			sb.append(", ");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append("]\n");
		sb.append("\n");
		sb.append("Entries: \n");
		for(Entry e : entries){
			sb.append(e.toString());
			sb.append("\n");
		}
		sb.append("---------------");
		return sb.toString();
	}
	
	/**
	 * main function for testing and eventually running, should really be in request handler
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		User u1 = new User("u1", "n1", "", "", "");
		User u2 = new User("u2", "n2", "", "", "");
		
		Entry e1 = new Entry(1, 13456543, 234345230, "l1", "d1", true, null);
		Entry e2 = new Entry(2, 13456543, 234345230, "l2", "d2", true, null);
		
		DataBaseManager dbm = new DataBaseManager();
//		dbm.addUser(u1);
//		dbm.addUser(u2);
		
		dbm.addEntry(e1, u1.getUsername());
		
	}
}
