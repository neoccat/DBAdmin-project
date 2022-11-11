import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Delete")
public class Delete extends HttpServlet {
    
    private Connection con;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.con = PsqlConnection.getConnection();
        String tableName = req.getParameter("tablename");
        String id = req.getParameter("id");
        try {
            String query = "delete from " + tableName + " where id = " + id;
            System.out.println(query);
            PreparedStatement stmt = con.prepareStatement(query);
            System.out.println(stmt);
            stmt.execute();
            resp.sendRedirect("Insert?table=" + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
