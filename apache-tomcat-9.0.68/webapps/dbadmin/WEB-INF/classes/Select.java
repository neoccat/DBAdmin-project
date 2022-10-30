import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Select")
public class Select extends HttpServlet {

    private Connection con;
    
    /**
     * Selectionner la liste des colonnes :
     * select column_name from information_schema.columns where table_schema = 'public' and table_name = 'etudiant';
     */

    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        try {

            this.con = PsqlConnection.getConnection();
            String tableName = req.getParameter("table");
            StringBuilder html = new StringBuilder();

            html.append("""
                <table class="table table-bordered table-hover table-sm mb-0">
                <thead style=\"position: sticky; top: 0; z-index: 1\" class="table-light">
                <tr>
          """);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select column_name from information_schema.columns where table_schema = 'public' and table_name = '" + tableName + "'");
            
            while(rs.next())
                html.append("<th scope=\"col\">" + rs.getString("column_name") + "</th>");
            html.append("<th class=\"d-flex justify-content-center\">Edit</th>");
            
            html.append("""
                </tr>
                </thead">
            """);

            html.append("<tbody>");
            rs = stmt.executeQuery("select * from " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            while(rs.next()) {
                html.append("<tr>");
                html.append("<th id=\"idDelete\" style=\"font-weight: normal\" scope=\"row\">" + rs.getInt("id") + "</th>");
                for(int i = 2; i <= rsmd.getColumnCount(); i++) {
                    html.append("<th style=\"font-weight: normal\" scope=\"row\">" + rs.getObject(rsmd.getColumnLabel(i)) + "</th>");
                }
                html.append("<th style=\"padding:2px\" class=\"d-flex justify-content-end\">");
                html.append("<button type=\"button\" style=\"margin:5px\" class = \"btn btn-primary btn-sm\">Edit</button>");
                html.append("<a href=\"Delete?tablename=" + tableName + "&id=" + rs.getInt("id") + "\" type=\"button\" style=\"margin:5px\" class=\"btn btn-danger btn-sm\">Delete</a></th>");

                html.append("</tr>");
            }
            html.append("</tbody></table>");

            MyAdmin.putArgument("select", html.toString());
            MyAdmin.putArgument("tablename", tableName);
            res.sendRedirect("My-Admin");

        } catch(Exception e) {
            e.printStackTrace();
        }
        
        
    }

}