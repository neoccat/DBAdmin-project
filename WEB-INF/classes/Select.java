import java.sql.Connection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Select")
public class Select extends HttpServlet {

    private Connection con = PsqlConnection.getConnection();
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        
    }

}