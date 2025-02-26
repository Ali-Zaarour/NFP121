package dev.alizaarour.services;

import dev.alizaarour.models.CourseProcess;
import dev.alizaarour.models.Student;
import dev.alizaarour.models.User;
import dev.alizaarour.services.pack.CountDoneVisitor;
import dev.alizaarour.services.pack.CountEnrolledVisitor;
import dev.alizaarour.services.pack.CourseProcessVisitor;
import dev.alizaarour.services.pack.TotalFeesVisitor;
import dev.alizaarour.utils.Observable;

public class CourseProcessService extends Observable {

    private static final CourseProcessService instance = new CourseProcessService();

    private CourseProcessService() {
    }

    public static synchronized CourseProcessService getInstance() {
        return instance;
    }

    //create a new course process
    public int createCourseProcess(int courseId, int levelId) {
        //get current user details
        User activeUser = UserService.getInstance().getActiveUser();
        if (activeUser instanceof Student) {
            var newCourse = new CourseProcess(courseId, levelId);
            ((Student) activeUser).getCourseProcesses().add(newCourse);
            notifyObservers();
            return newCourse.getProcessId();
        }
        return -1;
    }

    // Get the course process by its id.
    public CourseProcess getCourseProcess(int courseProcessId) {
        //get current user details
        User activeUser = UserService.getInstance().getActiveUser();
        if (activeUser instanceof Student) {
            return ((Student) activeUser).getCourseProcesses().stream()
                    .filter(cp -> cp.getProcessId() == courseProcessId)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    //get an array of int with the current course taken level
    public int getTheIdForTheNextLevel(int courseId) {
        User currentUser = UserService.getInstance().getActiveUser();
        int index = 0;
        if (currentUser instanceof Student) {
            var x = ((Student) currentUser).getCourseProcesses();
            for (CourseProcess courseProcess : x) {
                if (courseProcess.getCourseId() == courseId)
                    index++;
            }
        }
        return index;
    }

    // Generic method to accept a visitor over the active student's course processes.
    public void acceptVisitor(CourseProcessVisitor visitor) {
        User activeUser = UserService.getInstance().getActiveUser();
        if (activeUser instanceof Student) {
            ((Student) activeUser).getCourseProcesses().forEach(visitor::visit);
        }
    }

    // Operation: get total fees paid by the student.
    public double getTotalFeesPaid() {
        TotalFeesVisitor visitor = new TotalFeesVisitor();
        acceptVisitor(visitor);
        return visitor.getTotalFees();
    }

    // Operation: get the total number of enrolled courses.
    public int getTotalEnrolledCourses() {
        CountEnrolledVisitor visitor = new CountEnrolledVisitor();
        acceptVisitor(visitor);
        return visitor.getCount();
    }

    // Operation: get the total number of completed (done) courses.
    public int getTotalDoneCourses() {
        CountDoneVisitor visitor = new CountDoneVisitor();
        acceptVisitor(visitor);
        return visitor.getCount();
    }

}
