package main.db;

import java.util.List;
import java.util.Map;

import main.models.Course;

public class CourseManager {
	
	public static List<Course> getCourses(){
		List<Map<String, String>> courseMaps = DatabaseManager.sendQuery("SELECT * FROM course");
		return DatabaseUtil.MapsToCourses(courseMaps);
	}
	
	public static Course getCourse(String courseCode) {
		List<Map<String, String>> courseMaps = DatabaseManager.sendQuery(String.format("SELECT * FROM course WHERE course_code = '%s'", courseCode));
		if (courseMaps.size() == 0) {
			return null;	
		} else if(courseMaps.size() > 1) {
			throw new IllegalStateException(String.format("Too many courses matching primary key courseCode: %s", courseCode));
		}
				
		return DatabaseUtil.MapsToCourses(courseMaps).get(0);
	}

	public static void deleteCourse(String courseCode) {
		DatabaseManager.sendUpdate(String.format("DELETE FROM course WHERE course_code = '%s'", courseCode));
	}

	public static void deleteCourse(Course course) {
		DatabaseManager.sendUpdate(String.format("DELETE FROM course WHERE course_code = '%s'", course.getCourseCode()));
	}

	public static void deleteCourses(List<Course> courses) {
		System.err.println("deleteCourses(List<Course> courses) not implemented! :)");
	}
	
	public static void addCourse(String courseCode, String name, String description) {
		DatabaseManager.sendUpdate(String.format("INSERT INTO course VALUES('%s', '%s', '%s');", courseCode, name, description));
	}
	
	public static List<Course> coursesFromUser(String username){
		String query = "SELECT * FROM course WHERE course_code IN (SELECT course_code FROM user_course WHERE username = '" + username + "')";
		List<Map<String, String>> courseMaps = DatabaseManager.sendQuery(query);
		return DatabaseUtil.MapsToCourses(courseMaps);
	}
	

	/**
	 * Updates the database entry associated with input courses' primary key. 
	 */
	public static int updateCourse(Course course) {
		String query = String.format("UPDATE course SET name = %s, description = %s where course_code = %s;", 
				course.getName(), course.getDescription(), course.getCourseCode() );
		
		return DatabaseManager.sendUpdate(query);
	}

	/**
	 * Registers input course in database. 
	 */
	public static void registerCourse(Course course) {
		addCourse(course.getCourseCode(), course.getName(), course.getDescription());
	}
	
}