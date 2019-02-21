package main.db;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import main.models.Course;
import main.models.Course.Role;
import main.models.User;

public class UserManager {
	
	public static List<User> getUsers(){
		List<Map<String, String>> userMaps = DatabaseManager.sendQuery("SELECT * FROM user");
		return DatabaseUtil.MapsToUsers(userMaps);
	}
	
	public static User getUser(String username) {
		List<Map<String, String>> userMaps = DatabaseManager.sendQuery("SELECT * FROM user WHERE username = '" + username + "'");
		if (userMaps.size() == 0) {
			return null;
		} else if(userMaps.size() > 1) {
			throw new IllegalStateException("Two primary keys in user");
		}
		return DatabaseUtil.MapsToUsers(userMaps).get(0);
	}

	public static int deleteUser(String username) {
		return DatabaseManager.sendUpdate(String.format("DELETE FROM user WHERE username = '%s';", username));
	}
	
	public static int deleteUser(User user) {
		return DatabaseManager.sendUpdate(String.format("DELETE FROM user WHERE username = '%s';", user.getUsername()));
	}

	public static int deleteUsers(List<String> usernames) {
		String parsedUsernames = usernames.stream().collect(Collectors.joining("', '", "('", "');"));
		String query = String.format("DELETE FROM user WHERE user.username in %s", parsedUsernames); //'" + user.userName + "'
		return DatabaseManager.sendUpdate(query);
	}
	
	public static int addUser(String username, String password, String firstName, String lastName, String email) {
		return DatabaseManager.sendUpdate(String.format("INSERT INTO user VALUES('%s','%s','%s','%s','%s');", username, password, firstName, lastName, email));
	}
	
	public static int addUser(User user) {
		return DatabaseManager.sendUpdate(String.format("INSERT INTO user VALUES('%s','%s','%s','%s','%s');", 
						user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail() ));
	}

	public static List<User> usersFromCourse(String courseCode){
		String query = String.format("SELECT * FROM user WHERE username IN (SELECT username FROM user_course WHERE course_code = '%s')", courseCode);
		List<Map<String, String>> userMaps = DatabaseManager.sendQuery(query);
		return DatabaseUtil.MapsToUsers(userMaps);
	}
	
	
	public static List<User> getUsersByRole(Course course, Role role) {
		String query = String.format("SELECT * FROM user WHERE username IN "
				+ "(SELECT username FROM user_course WHERE course_code = '%s' and role = '%s');", course.getCourseCode(), role.name());
		List<Map<String, String>> userMaps = DatabaseManager.sendQuery(query);
		return DatabaseUtil.MapsToUsers(userMaps);
	}
	
	public static List<User> getUsersExcludingRole(Course course, Role role) {
		String query = "SELECT * FROM user WHERE username not IN "
				+ "(SELECT username FROM user_course WHERE course_code = '" 
				+ course.getCourseCode() + "' and role = '" + role.name() + "');";
		List<Map<String, String>> userMaps = DatabaseManager.sendQuery(query);
		return DatabaseUtil.MapsToUsers(userMaps);
	}
	
	public static int addUsersToCourse(List<User> users, Course course, Role role) {
		List<String> usernames = users.stream().map(User::getUsername).collect(Collectors.toList());
		
		String parsedUserCourseList = usernames.stream()
				.map(username -> String.format("('%s', '%s', '%s')", 
						username, 
						course.getCourseCode(), 
						role))
				.collect(Collectors.joining(", ", "", ";"));
		
		String myQuery = String.format("INSERT INTO user_course values %s;", parsedUserCourseList);
		System.out.printf("My query: %s%n", myQuery);
		return DatabaseManager.sendUpdate(myQuery);
	}
	
	// TODO: Implement method below
	public static int deleteUsersFromCourse(List<User> users, Course course, Role role) {
		System.err.println("deleteUsersFromCourse(List<User> users, Course course, Role role) not implemented! :)");
		return -1;
	}
	
	public static int updateUser(User user) {
		String query = String.format("UPDATE user SET first_name = '%s', last_name = '%s', email = '%s', password = '%s' where username = '%s';", 
				user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getUsername());
		return DatabaseManager.sendUpdate(query);
	}
		
}











