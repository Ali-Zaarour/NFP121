package dev.alizaarour.services;

import dev.alizaarour.models.CourseProcess;
import dev.alizaarour.models.Student;
import dev.alizaarour.models.User;
import dev.alizaarour.utils.Observable;

public class CourseProcessService extends Observable {

    private static final CourseProcessService instance = new CourseProcessService();

    private CourseProcessService() {
    }

    public static synchronized CourseProcessService getInstance() {
        return instance;
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

}
