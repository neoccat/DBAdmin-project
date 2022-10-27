import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Disconnect")
public class Disconnect extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            PsqlConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resp.sendRedirect("Connection");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    }
}
