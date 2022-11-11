import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Insert")
public class Insert extends HttpServlet {

    private Connection con;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.con = PsqlConnection.getConnection();
        String tableName = req.getParameter("table");
        StringBuilder html = new StringBuilder();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select column_name from information_schema.columns where table_schema = 'public' and table_name = '" + tableName + "'");
    
            html.append("<form action=\"Insert\" method=\"post\" class=\"row g-3\">");
            html.append("<input value=\"" + tableName + "\" name=\"table\" type=\"text\" class=\"d-lg-none\" id=\"table\">");
            while(rs.next()) {
                if(!rs.getString("column_name").contains("id")) {
                    String column = rs.getString("column_name");
                    html.append("<div class=\"col-md-4\">");
                    html.append("<label for=\"" + column + "\" class=\"form-label\">" + column + " : </label>");
                    html.append("<input name=\"" + column + "\" type=\"text\" class=\"form-control\" id=\"" + column + "\">");
                    html.append("</div>");
                }
            }
            html.append("<div class=\"col-12\"><button type=\"submit\" class=\"btn btn-success\">Insert</button></div>");
            html.append("</form>");

            MyAdmin.putArgument("insertOrUpdateForm", html.toString());
            resp.sendRedirect("Select?table=" + tableName);

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    // select column_name from information_schema.columns where table_schema = 'public' and table_name = 'etudiant';
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String tableName = req.getParameter("table");

            con = PsqlConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select column_name from information_schema.columns where table_schema = 'public' and table_name = '" + tableName + "'");

            StringBuilder query = new StringBuilder();
            Map<String, String[]> paramValues = req.getParameterMap();
            
            query.append("insert into " + req.getParameter("table") + " (");
            while(rs.next()) {
                if(!rs.getString("column_name").contains("id"))
                    query.append(rs.getString("column_name") + ", ");
            }
            query.delete(query.length() - 2, query.length());
            query.append(") values (");

            for(Map.Entry<String, String[]> entry : paramValues.entrySet()) {
                if(!entry.getValue()[0].equals(tableName))
                    query.append("'" + entry.getValue()[0] + "', ");
            }

            query.delete(query.length() - 2, query.length());
            query.append(")");

            System.out.println(query.toString());
            PreparedStatement pstmt = con.prepareStatement(query.toString());
            pstmt.execute();
            
            resp.sendRedirect("Insert?table=" + tableName);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
