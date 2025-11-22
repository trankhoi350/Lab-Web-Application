<%--
  Created by IntelliJ IDEA.
  User: Tran Gia Khoi
  Date: 11/15/2025
  Time: 8:28 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student List - MVC</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }

        h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 32px;
        }

        .subtitle {
            color: #666;
            margin-bottom: 30px;
            font-style: italic;
        }

        .message {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            font-weight: 500;
        }

        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .btn {
            display: inline-block;
            padding: 12px 24px;
            text-decoration: none;
            border-radius: 5px;
            font-weight: 500;
            transition: all 0.3s;
            border: none;
            cursor: pointer;
            font-size: 14px;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }

        .btn-danger {
            background-color: #dc3545;
            color: white;
            padding: 8px 16px;
            font-size: 13px;
        }

        .btn-danger:hover {
            background-color: #c82333;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        thead {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        th, td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            font-weight: 600;
            text-transform: uppercase;
            font-size: 13px;
            letter-spacing: 0.5px;
        }

        tbody tr {
            transition: background-color 0.2s;
        }

        tbody tr:hover {
            background-color: #f8f9fa;
        }

        .actions {
            display: flex;
            gap: 10px;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }

        .empty-state-icon {
            font-size: 64px;
            margin-bottom: 20px;
        }
        /* ... Keep your existing CSS ... */

        /* NEW CSS FOR SEARCH BAR */
        .toolbar {
            display: flex;
            justify-content: space-between; /* Pushes Add button left, Search right */
            align-items: center;
            margin-bottom: 20px;
            flex-wrap: wrap;
            gap: 15px;
        }

        .search-form {
            display: flex;
            gap: 10px;
            align-items: center;
        }

        .search-input {
            padding: 10px 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            width: 250px;
            outline: none;
            transition: border-color 0.3s;
        }

        .search-input:focus {
            border-color: #667eea;
        }

        .search-results-msg {
            margin-bottom: 15px;
            color: #555;
            font-size: 1.1em;
        }
        /* Update existing toolbar to handle more items */
        .toolbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            flex-wrap: wrap;
            gap: 10px;
            background-color: #f8f9fa; /* Light gray background for grouping */
            padding: 15px;
            border-radius: 8px;
        }

        .filter-form {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .filter-form select {
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            cursor: pointer;
        }

        /* Make table headers clickable links */
        th a {
            color: white;
            text-decoration: none;
            display: flex;
            align-items: center;
            gap: 5px;
        }

        th a:hover {
            text-decoration: underline;
            color: #e2e6ea;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <div class="navbar">
        <h2>📚 Student Management System</h2>
        <div class="navbar-right">
            <div class="user-info">
                <span>Welcome, ${sessionScope.fullName}</span>
                <span class="role-badge role-${sessionScope.role}">
                    ${sessionScope.role}
                </span>
            </div>
            <a href="dashboard" class="btn-nav">Dashboard</a>
            <a href="logout" class="btn-logout">Logout</a>
        </div>
    </div>
    <div class="container">
        <h1>📚 Student Management System</h1>
        <p class="subtitle">MVC Pattern with Jakarta EE & JSTL</p>

        <!-- Add button - Admin only -->
        <c:if test="${sessionScope.role eq 'admin'}">
            <div style="margin: 20px 0;">
                <a href="student?action=new" class="btn-add">➕ Add New Student</a>
            </div>
        </c:if>


        <!-- Success Message -->
        <c:if test="${not empty param.message}">
            <div class="message success">
                ✅ ${param.message}
            </div>
        </c:if>

        <!-- Error Message -->
        <c:if test="${not empty param.error}">
            <div class="message error">
                ❌ ${param.error}
            </div>
        </c:if>

        <!-- Add New Student Button -->
        <div class="toolbar">
            <a href="student?action=new" class="btn btn-primary">
                ➕ Add New Student
            </a>

            <form action="student" method="GET" class="filter-form">
                <input type="hidden" name="action" value="filter">

                <label for="majorFilter" style="margin:0; font-weight:500;">Filter by:</label>
                <select name="major" id="majorFilter">
                    <option value="">All Majors</option>
                    <option value="Computer Science" ${param.major == 'Computer Science' ? 'selected' : ''}>Computer Science</option>
                    <option value="Information Technology" ${param.major == 'Information Technology' ? 'selected' : ''}>Information Technology</option>
                    <option value="Software Engineering" ${param.major == 'Software Engineering' ? 'selected' : ''}>Software Engineering</option>
                    <option value="Business Administration" ${param.major == 'Business Administration' ? 'selected' : ''}>Business Administration</option>
                </select>

                <button type="submit" class="btn btn-secondary" style="padding: 8px 12px;">Apply</button>

                <c:if test="${not empty param.major}">
                    <a href="student?action=list" style="color: #dc3545; font-size: 0.9em;">❌ Clear</a>
                </c:if>
            </form>

            <form action="student" method="GET" class="search-form">
                <input type="hidden" name="action" value="search">
                <input type="text" name="keyword" class="search-input"
                       placeholder="Search..." value="${param.keyword}">
                <button type="submit" class="btn btn-primary" style="padding: 8px 12px;">🔍</button>
            </form>
        </div>

        <c:if test="${not empty param.keyword}">
            <p class="search-results-msg">
                Search results for: <strong>"${param.keyword}"</strong>
            </p>
        </c:if>



        <!-- Student Table -->
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Code</th>
                <th>Name</th>
                <th>Email</th>
                <th>Major</th>
                <c:if test="${sessionScope.role eq 'admin'}">
                    <th>Actions</th>
                </c:if>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="student" items="${students}">
                <tr>
                    <td>${student.id}</td>
                    <td>${student.studentCode}</td>
                    <td>${student.fullName}</td>
                    <td>${student.email}</td>
                    <td>${student.major}</td>

                    <!-- Action buttons - Admin only -->
                    <c:if test="${sessionScope.role eq 'admin'}">
                        <td>
                            <a href="student?action=edit&id=${student.id}"
                               class="btn-edit">Edit</a>
                            <a href="student?action=delete&id=${student.id}"
                               class="btn-delete"
                               onclick="return confirm('Delete this student?')">Delete</a>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>

            <c:if test="${empty students}">
                <tr>
                    <td colspan="6" style="text-align: center;">
                        No students found
                    </td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</body>
</html>
