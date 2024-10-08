package dev.coms4156.project.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

/**
 * This class contains unit tests for RouteController.java.
 */
public class RouteControllerUnitTests {
    private RouteController routeController;

    /**
     * Set up steps to run before each method in this Unit test class.
     */
    @BeforeEach
    public void setUp() {
        routeController = new RouteController();
        IndividualProjectApplication app = new IndividualProjectApplication();
        // We are configuring the app so that myFileDatabase has data from data.txt
        // for us to test against.
        String[] args = {"start up"};
        app.run(args);
    }

    @Test
    public void indexTest() {
        String response = routeController.index();
        assertNotNull(response);
        assertTrue(response.contains("Welcome, in order to make"));
    }

    @Test
    public void isCourseFullTest() {
        // COMS 3134 is NOT full as we know from the data provided to us in data.txt.
        ResponseEntity<?> response = routeController.isCourseFull("COMS", 3134);
        assertNotNull(response);
        // Successfully received response since course exists.
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(Objects.requireNonNull(response.getBody()).toString().contains("false"));
    }

    @Test
    public void isCourseFullTest_NotFound() {
        // COMS 1000 is NOT an existing course as we know from the data provided to us in data.txt.
        ResponseEntity<?> response = routeController.isCourseFull("COMS", 1000);
        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        assertEquals("Course Not Found", response.getBody());
    }

    @Test
    public void retrieveDepartmentTest() {
        // We know from the data provided to us that ECON is valid department.
        // We know the expected response from IndividualProjectApplication.java's resetDataFile()
        // method which received its setup data from data.txt
        String deptCode = "ECON";
        String expectedResponse =
            "ECON 1105: \n"
            + "Instructor: Waseem Noor; Location: 309 HAV; Time: 2:40-3:55\n"
            + "ECON 1004: \n"
            + "Instructor: Murat Yilmaz; Location: 428 PUP; Time: 11:40-12:55\n"
            + "ECON 2257: \n"
            + "Instructor: Tamrat Gashaw; Location: 428 PUP; Time: 10:10-11:25\n"
            + "ECON 3412: \n"
            + "Instructor: Thomas Piskula; Location: 702 HAM; Time: 11:40-12:55\n"
            + "ECON 3213: \n"
            + "Instructor: Miles Leahey; Location: 702 HAM; Time: 4:10-5:25\n"
            + "ECON 3211: \n"
            + "Instructor: Murat Yilmaz; Location: 310 FAY; Time: 4:10-5:25\n"
            + "ECON 4840: \n"
            + "Instructor: Mark Dean; Location: 142 URIS; Time: 2:40-3:55\n"
            + "ECON 4710: \n"
            + "Instructor: Matthieu Gomez; Location: 517 HAM; Time: 8:40-9:55\n"
            + "ECON 4415: \n"
            + "Instructor: Evan D Sadler; Location: 309 HAV; Time: 10:10-11:25\n";
        ResponseEntity<?> response = routeController.retrieveDepartment(deptCode);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void retrieveCourseTest() {
        // We know from the data provided to us that chem1500 is valid course.
        // We know the expected response from IndividualProjectApplication.java's resetDataFile()
        // method which received its setup data from data.txt
        String deptCode = "CHEM";
        int courseCode = 1500;
        String expectedResponse =
            "\nInstructor: Joseph C Ulichny; Location: 302 HAV; Time: 6:10-9:50";
        ResponseEntity<?> response = routeController.retrieveCourse(deptCode, courseCode);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void retrieveDepartmentTest_NotFound() {
        // We know BIOL is not a valid dept in the data we are using.
        String deptCode = "BIOL";
        ResponseEntity<?> response = routeController.retrieveDepartment(deptCode);
        assertEquals(404, response.getStatusCode().value());
        assertEquals("Department Not Found", response.getBody());
    }

    @Test
    public void retrieveCourseTest_NotFound() {
        int courseCode = 0;
        String deptCode = "invalid_dept";
        ResponseEntity<?> response = routeController.retrieveCourse(deptCode, courseCode);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Department Not Found", response.getBody());
    }

    @Test
    public void retrieveCoursesTest() {
        // For testing purposes, I added a 1004 course in ECON and IEOR departments in the data
        // used by this test class. Hence, we see from the data in resetDataFile() that 1004 is now
        // a valid course in COMS, ECON, and IEOR departments.
        int courseCode = 1004;
        String expectedResponse =
            "[\n"
            + "COMS: \n"
            + "Instructor: Adam Cannon; Location: 417 IAB; Time: 11:40-12:55\n"
            + ", \n"
            + "ECON: \n"
            + "Instructor: Murat Yilmaz; Location: 428 PUP; Time: 11:40-12:55\n"
            + ", \n"
            + "IEOR: \n"
            + "Instructor: Daniel Lacker; Location: 501 NWC; Time: 11:40-12:55\n"
            + "]";
        ResponseEntity<?> response = routeController.retrieveCourses(courseCode);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void retrieveCoursesTest_invalidCourseCode() {
        // We know that there are no courses with the code 2000 in our data source.
        // Hence, we expect no courses to be found in this test case.
        int courseCode = 2000;
        String expectedResponse = "Courses with provided course code Not Found";
        ResponseEntity<?> response = routeController.retrieveCourses(courseCode);
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void getMajorCountFromDeptTest() {
        // We know from the data provided to us that the dept "IEOR" has 67 majors (students) in
        // it.
        String expectedResponse = "There are: 67 majors in the department";
        ResponseEntity<?> response = routeController.getMajorCtFromDept("IEOR");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void getMajorCountFromDeptTest_NotFound() {
        // We know from the data provided to us that the dept "MATH" does not exist.
        String expectedResponse = "Department Not Found";
        ResponseEntity<?> response = routeController.getMajorCtFromDept("MATH");
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void idDeptChairTest() {
        // We know from the data provided to us that the dept "COMS" has "Luca Carloni" as dept
        // chair.
        String expectedResponse = "Luca Carloni is the department chair.";
        ResponseEntity<?> response = routeController.identifyDeptChair("COMS");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void idDeptChairTest_NotFound() {
        // We know from the data provided to us that the dept "GEOL" does not exist.
        String expectedResponse = "Department Not Found";
        ResponseEntity<?> response = routeController.identifyDeptChair("GEOL");
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void findCourseLocationTest() {
        // We know from the data provided to us that ieor4540's location is in 633 MUDD.
        String expectedResponse = "633 MUDD is where the course is located.";
        ResponseEntity<?> response = routeController.findCourseLocation("IEOR", 4540);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void findCourseLocationTest_NotFound() {
        // We know from the data provided to us that ieor4550 does not exist.
        String expectedResponse = "Course Not Found";
        ResponseEntity<?> response = routeController.findCourseLocation("IEOR", 4550);
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void findCourseInstructorTest() {
        // We know from the data provided to us that ieor4540's instructor is Krzysztof M
        // Choromanski.
        String expectedResponse = "Krzysztof M Choromanski is the instructor for the course.";
        ResponseEntity<?> response = routeController.findCourseInstructor("IEOR", 4540);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void findCourseTimeTest() {
        // We know from the data provided to us that ieor4540's time is at 7:10-9:40
        String expectedResponse = "The course meets at: 7:10-9:40.";
        ResponseEntity<?> response = routeController.findCourseTime("IEOR", 4540);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void addMajorToDeptTest() {
        // We know from the data provided to us that CHEM has 250 majors (students).
        // Hence we should be able to successfully add a major to it and return 200 http code
        // response.
        String expectedResponse = "Attribute was updated successfully";
        ResponseEntity<?> response = routeController.addMajorToDept("CHEM");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void addMajorToDeptTest_deptNotFound() {
        // We know from the data provided to us that BIOL is not a valid major within the data.
        String expectedResponse = "Department Not Found";
        ResponseEntity<?> response = routeController.addMajorToDept("BIOL");
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void removeFromMajorTest() {
        // We know from the data provided to us that CHEM has 250 majors (students).
        // Hence we should be able to successfully remove a major from it and return
        // 200 http code response.
        String expectedResponse = "Attribute was updated successfully";
        ResponseEntity<?> response = routeController.addMajorToDept("CHEM");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void removeMajorFromDeptTest_deptNotFound() {
        // We know from the data provided to us that BIOL is not a valid major within the data.
        String expectedResponse = "Department Not Found";
        ResponseEntity<?> response = routeController.addMajorToDept("BIOL");
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void dropStudentFromCourseTest() {
        // We know from the data provided to us that phys1001 has 131 students enrolled in it.
        // Hence, we should be able to successfully drop a student using dropStudent()
        String expectedResponse = "Student has been dropped.";
        ResponseEntity<?> response = routeController.dropStudent("PHYS", 1001);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void dropStudentFromCourseTest_invalidCourse() {
        // We know from the data provided to us that phys1002 is not a valid course.
        // Hence, we should NOT be able to drop a student using dropStudent() and should return
        // a "course not found" response.
        String expectedResponse = "Course Not Found";
        ResponseEntity<?> response = routeController.dropStudent("PHYS", 1002);
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void enrollStudentInCourseTest() {
        // We know from the data provided to us that phys1001 has 131 students enrolled in it.
        // Hence, we should be able to successfully enroll a student using enrollStudent().
        String expectedResponse = "Student has been enrolled.";
        ResponseEntity<?> response = routeController.enrollStudent("PHYS", 1001);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void enrollStudentInCourseTest_invalidCourse() {
        // We know from the data provided to us that phys1005 is not a valid course.
        // Hence, we should NOT be able to enroll a student using enrollStudent() and should return
        // a 404 response.
        String expectedResponse = "Course Not Found";
        ResponseEntity<?> response = routeController.enrollStudent("PHYS", 1005);
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void enrollStudentInCourseTest_courseFull() {
        // We are adding a course ELEN 1202 to the data source in resetDataFile() which is full.
        // Hence, we should NOT be able to enroll a student using enrollStudent() and should return
        // a 400 response.
        String expectedResponse = "Student has not been enrolled.";
        ResponseEntity<?> response = routeController.enrollStudent("ELEN", 1202);
        assertEquals(400, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void setEnrollmentCountTest() {
        // We know from the data provided to us that ieor4511 has 50 students enrolled
        // in it, and we should be able to successfully set enrollment count.
        String expectedResponse =
            "Attribute was updated successfully if count <= course's capacity.";
        ResponseEntity<?> response = routeController.setEnrollmentCount("IEOR", 4511, 55);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void setEnrollmentCountTest_invalidCourse() {
        // We know from the data provided to us that ieor4512 is not a valid course.
        String expectedResponse = "Course Not Found";
        ResponseEntity<?> response = routeController.setEnrollmentCount("IEOR", 4512, 100);
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void changeCourseTimeTest() {
        // We know from the data provided to us that ieor4511 meets at 9:00-11:30
        // and we should be able to successfully change course time to 7:10-9:40
        // via changeCourseTime()
        String expectedResponse = "Attribute was updated successfully.";
        ResponseEntity<?> response = routeController.changeCourseTime("IEOR", 4511, "7:10-9:40");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void changeCourseTimeTest_invalidCourse() {
        // We know from the data provided to us that ieor5600 is an invalid course.
        String expectedResponse = "Course Not Found";
        ResponseEntity<?> response = routeController.changeCourseTime("IEOR", 5600, "7:10-9:40");
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void changeCourseTeacherTest() {
        // We know from the data provided to us that elen1201 has instructor David G Vallancourt
        // and we should be able to successfully change instructor to Kenneth Shepard
        // via changeCourseTeacher()
        String expectedResponse = "Attribute was updated successfully.";
        ResponseEntity<?> response =
            routeController.changeCourseTeacher("ELEN", 1201, "Kenneth Shepard");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void changeCourseTeacherTest_invalidCourse() {
        // We know from the data provided to us that elen1210 is an invalid course but if we made
        // a typo (vs 1201) and think it has instructor David G Vallancourt
        // and we try to change teacher to Kenneth Shepard, this changeCourseTeacher() would fail.
        String expectedResponse = "Course Not Found";
        ResponseEntity<?> response =
            routeController.changeCourseTeacher("ELEN", 1210, "Kenneth Shepard");
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void changeCourseLocationTest() {
        // We know from the data provided to us that elen1201 has location 301 PUP
        // and we should be able to successfully change location to 1205 MUDD
        // via changeCourseLocation()
        String expectedResponse = "Attribute was updated successfully.";
        ResponseEntity<?> response =
            routeController.changeCourseLocation("ELEN", 1201, "1205 MUDD");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void changeCourseLocationTest_invalidCourse() {
        // We know from the data provided to us that elen1122 is an invalid course
        String expectedResponse = "Course Not Found";
        ResponseEntity<?> response =
            routeController.changeCourseLocation("ELEN", 1122, "1205 MUDD");
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }
}
