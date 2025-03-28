import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String empId = request.getParameter("empId");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "password");

            String query = "SELECT * FROM employees";
            if (empId != null && !empId.isEmpty()) {
                query += " WHERE id = ?";
            }

            PreparedStatement ps = con.prepareStatement(query);
            if (empId != null && !empId.isEmpty()) {
                ps.setInt(1, Integer.parseInt(empId));
            }

            ResultSet rs = ps.executeQuery();
            out.println("<h2>Employee List</h2>");
            out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Department</th></tr>");

            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("name") + "</td><td>" + rs.getString("department") + "</td></tr>");
            }
            out.println("</table>");

            con.close();
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
