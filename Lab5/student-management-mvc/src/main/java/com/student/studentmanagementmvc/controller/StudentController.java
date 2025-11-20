package com.student.studentmanagementmvc.controller;

import com.student.studentmanagementmvc.model.Student;
import com.student.studentmanagementmvc.dao.StudentDAO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/student")
public class StudentController extends HttpServlet {
    private StudentDAO studentDAO;


    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteStudent(request, response);
                break;
            case "search":
                searchStudents(request, response);
                break;
            case "sort":
                sortStudents(request, response);
                break;
            case "filter":
                filterStudents(request, response);
                break;
            default:
                listStudents(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        switch (action) {
            case "insert":
                insertStudent(request, response);
                break;
            case "update":
                updateStudent(request, response);
                break;
        }
    }

    // List all students
    private void listStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Student> students = studentDAO.getAllStudents();
        request.setAttribute("students", students);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    // Show form for new student
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }

    // Show form for editing student
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Student existingStudent = studentDAO.getStudentById(id);

        request.setAttribute("student", existingStudent);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }

    // Insert new student
    private void insertStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");

        Student newStudent = new Student(studentCode, fullName, email, major);

        // Validate insert
        if(validateStudent(newStudent, request)){
            request.setAttribute("student", newStudent);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
            dispatcher.forward(request,response);
            return;
        }

        if (studentDAO.addStudent(newStudent)) {
            response.sendRedirect("student?action=list&message=Student added successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to add student");
        }
    }

    // Update student
    private void updateStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        int id = Integer.parseInt(request.getParameter("id"));
        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");

        Student student = new Student(studentCode, fullName, email, major);
        student.setId(id);

        // Validate update
        if(validateStudent(student, request)){
            request.setAttribute("student", student);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
            dispatcher.forward(request,response);
            return;
        }

        if (studentDAO.updateStudent(student)) {
            response.sendRedirect("student?action=list&message=Student updated successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to update student");
        }
    }

    // Delete student
    private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        if (studentDAO.deleteStudent(id)) {
            response.sendRedirect("student?action=list&message=Student deleted successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to delete student");
        }
    }

    // Search students
    private void searchStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get the keyword from the search form
        String keyword = request.getParameter("keyword");

        // 2. Call the DAO method (ensure you added the code from the previous step to StudentDAO)
        List<Student> resultList = studentDAO.searchStudents(keyword);

        // 3. Set the results as an attribute for the JSP
        request.setAttribute("students", resultList);

        // 4. Reuse the student-list.jsp to display the search results
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    // Validate student
    private boolean validateStudent(Student student, HttpServletRequest request) {
        boolean isValid = true;
        String codePattern = "[A-Z]{2}[0-9]{3,}";
        String emailPattern="^[A-Za-z0-0+_.-]+@(.+)$";
        String namePatter = "^[A-Za-z0-9_.-]+$";

        // Validate student code
        if (student.getStudentCode() == null) {
            request.setAttribute("errorCode", "Student code is required");
            isValid = false;
        } else if (!student.getStudentCode().matches(codePattern)) {
            request.setAttribute("errorCode", "Invalid format. Use 2 letters + 3+ digits (e.g., SV001)");
            isValid = false;
        }

        // TODO: Validate full name
        if (student.getFullName() == null) {
            request.setAttribute("errorName", "Full name is required");
            isValid = false;
        }else if (student.getFullName().trim().length() < 2){
            request.setAttribute("errorName", "Full name is too short");
            isValid = false;
        }

        // TODO: Validate email (only if provided)
        if(student.getEmail() == null) {
            request.setAttribute("errorEmail", "Email is required");
            isValid = false;
        } else if (!student.getEmail().matches(emailPattern)) {
            request.setAttribute("errorEmail", "Invalid email");
            isValid = false;
        }
        // TODO: Validate major
        if (student.getMajor() == null) {
            request.setAttribute("errorMajor", "Major is required");
            isValid = false;
        }

        return !isValid;
    }

    private void sortStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("order");

        List<Student> students = studentDAO.getStudentsSorted(sortBy, sortOrder);

        request.setAttribute("students", students);

        RequestDispatcher dispatcher = request.getRequestDispatcher("views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    private void filterStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String filter = request.getParameter("major");

        List<Student> students = studentDAO.getStudentsByMajor(filter);

        request.setAttribute("students", students);

        RequestDispatcher dispatcher = request.getRequestDispatcher("views/student-list.jsp");
        dispatcher.forward(request, response);
    }
}
