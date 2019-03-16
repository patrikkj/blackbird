package main.db;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import main.models.Announcement;
import main.models.Course;
import main.models.User;

public class AnnouncementManager {
	public static List<Announcement> getAnnouncements(){
		List<Map<String, String>> announcementMaps = DatabaseManager.sendQuery("SELECT * FROM announcement");
		return DatabaseUtil.mapsToAnnouncements(announcementMaps);
	}
	
	public static List<Announcement> getAnnouncementsFromCourse(Course course){
		List<Map<String, String>> announcementMaps = DatabaseManager.sendQuery(
				String.format("SELECT * FROM announcement WHERE course_code = '%s'", course.getCourseCode()));
		return DatabaseUtil.mapsToAnnouncements(announcementMaps);
	}
	
	public static void addAnnouncement(Course course, User user, Timestamp timestamp, String title, String text){
		DatabaseManager.sendUpdate(String.format("INSERT INTO announcement(course_code, username, timestamp, title, text) "
				+ "VALUES('%s', '%s', '%s', '%s', '%s');", course.getCourseCode(), user.getUsername(), timestamp.toString(), title, text));
	}
	
}
