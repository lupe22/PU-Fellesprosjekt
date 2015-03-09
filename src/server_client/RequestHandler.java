package server_client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import calendar.Entry;
import dbms.DataBaseManager;
import exceptions.EntryDoesNotExistException;
import exceptions.GroupAlreadyExistsException;
import exceptions.GroupDoesNotExistException;
import exceptions.HasNotTheRightsException;
import exceptions.UserDoesNotExistException;
import exceptions.UserInGroupDoesNotExistsException;
import exceptions.UsernameAlreadyExistsException;
import user.Group;
import user.User;

public class RequestHandler{

	private static DataBaseManager dbm;
	private static ServerSocket server;
	private static Set<ServerClientHandler> currently_connected;
	
	public static final int PORT = 80;
	public static final long CHECK_FOR_EXPECTED_INPUT_INTERVAL = 100;
	public static final long WAIT_BEFORE_TIMOUT = 10000;
	
	public static void main(String[] args) {
		init();
		acceptClients();
		dispose();
	}
	
	private static void init() {
		try {
			currently_connected = new HashSet<>();
			dbm = new DataBaseManager();
			server = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
			dispose();
			System.exit(-1);
		}
	}
	
	private static void dispose() {
		try {
			for (ServerClientHandler handler : currently_connected)
				handler.close();
			server.close();
			dbm.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private static void acceptClients() {
		try {
			while (!server.isClosed()) {
				Socket new_client = server.accept();
				System.out.println("Accepting new client");
				ServerClientHandler client_handler = new ServerClientHandler(new_client);
				currently_connected.add(client_handler);
				Thread client_handler_thread = new Thread(client_handler);
				client_handler_thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			dispose();
			System.exit(-1);
		}
	}
	
	public static boolean isLoggedIn(String username) {
		//TODO:
		return false;
	}
	
	public static void disconnectUser(ServerClientHandler client) {
		currently_connected.remove(client);
	}
	
	/* ===============
	 * User functions
	 *================*/ 
	
	public static void logIn(User user){
		// TODO
	}
	
	public synchronized static void createUser(User user) throws UsernameAlreadyExistsException{
		dbm.addUser(user);
	}
	
	public synchronized static void editUser(User u) throws UserDoesNotExistException{
		dbm.editUser(u);
	}
	

	public synchronized static void makeAdmin(User admin, User newAdmin, Entry entry) throws HasNotTheRightsException, EntryDoesNotExistException, UserDoesNotExistException {
		dbm.makeAdmin(admin.getUsername(), newAdmin.getUsername(), entry.getEntryID());
	}
	
	
	/* ===============
	 * Entry functions
	 *================*/ 
	
	public synchronized static void createEntry(Entry e, User user) throws UserDoesNotExistException{
		dbm.addEntry(e, user.getUsername());
	}
	
	public synchronized static void deleteEntry(Entry e){
		dbm.deleteEntry(e.getEntryID());
	}
	
	public synchronized static void editEntry(Entry e, User user) throws EntryDoesNotExistException, HasNotTheRightsException, UserDoesNotExistException {
		dbm.editEntry(e, user.getUsername());
	}
	
	public synchronized static void kickUserFromEntry(User admin, User user, Entry entry) throws EntryDoesNotExistException, UserDoesNotExistException{
		dbm.hideEvent(user.getUsername(), entry.getEntryID());
	}
	
	public synchronized static void kickGroupFromEntry(User admin, Group group, Entry entry) throws GroupDoesNotExistException, UserInGroupDoesNotExistsException, EntryDoesNotExistException{
		dbm.hideEventGroup(group.getName(), entry.getEntryID());
	}	
	
	public synchronized static void inviteUserToEntry(User admin, User user, Entry entry) throws EntryDoesNotExistException, UserDoesNotExistException, HasNotTheRightsException{
		dbm.inviteUser(admin.getUsername(), user.getUsername(), entry.getEntryID());
	}
	
	public synchronized static void inviteGroupToEntry(User admin, Group group, Entry entry) throws GroupDoesNotExistException, EntryDoesNotExistException, UserDoesNotExistException, HasNotTheRightsException{
		dbm.inviteGroup(admin.getUsername(), group.getName(), entry.getEntryID());
	}
	
	/* ===============
	 * Group functions
	 *================*/ 
	
	public synchronized static void createGroup(Group group) throws UserDoesNotExistException, GroupAlreadyExistsException, UserInGroupDoesNotExistsException{
		dbm.addGroup(group);
	}
	
	public synchronized static void addUserToGroup(User user, Group group) throws UserDoesNotExistException, GroupDoesNotExistException{
		dbm.addUserToGroup(user.getUsername(), group.getName());
	}
	
	public synchronized static void removeUserFromGroup(User user, Group group) throws GroupDoesNotExistException{
		dbm.removeUserFromGroup(user.getUsername(), group.getName());
	}
	
	
	/* ===============
	 * 'Calendar' functions
	 *================*/ 
	
	public synchronized static void createCalendar( User user) throws UserDoesNotExistException{
		dbm.createCalendar(user.getUsername());
	}
	
	public synchronized static void invitationAnswer(User u, Entry e, boolean answer) throws EntryDoesNotExistException, UserDoesNotExistException{
		if (answer == true){
			dbm.going(u.getUsername(), e.getEntryID());
		}else{
			dbm.notGoing(u.getUsername(), e.getEntryID());
		}
	}
}
