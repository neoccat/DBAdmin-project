import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Update")
public class Update extends HttpServlet {

    private Connection con;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       
        this.con = PsqlConnection.getConnection();
        StringBuilder html = new StringBuilder();
        String tableName = req.getParameter("tablename");

        try {
            Statement stmt = this.con.createStatement();
            String query = "select * from " + tableName + " where " + req.getParameter("idname") + "=" + req.getParameter("id");
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            
            String column, value, disabled = "";
            html.append("<form action=\"Update?tablename=" + tableName + "&idname=" + req.getParameter("idname") + "&id=" + req.getParameter("id") + "\" method=\"post\" class=\"row g-3\">");
            while(rs.next()) {
                for(int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                    column = rsmd.getColumnLabel(i);
                    value = String.valueOf(rs.getObject(column));
                    html.append("<div class=\"col-md-4\">");
                    html.append("<label for=\"" + column + "\" class=\"form-label\">" + column + " : </label>");
                    if(column.contains("id"))
                        disabled = "disabled";
                    html.append("<input value=\"" + value + "\" name=\"" + column + "\" type=\"text\" class=\"form-control\" id=\"" + column + "\" " + disabled + ">");
                    html.append("</div>");
                    disabled = "";
                }
            }
            html.append("<div class=\"col-12\"><button type=\"submit\" class=\"btn btn-success\">Update</button></div>");
            html.append("</form>");
            

            MyAdmin.putArgument("insertOrUpdateForm", html.toString());
            resp.sendRedirect("Select?table=" + tableName);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            con = PsqlConnection.getConnection();
            Statement stmt = con.createStatement();
            String tableName = req.getParameter("tablename");
            System.out.println("select : " + "select * from " + tableName + " where " + req.getParameter("idname") + "=" + req.getParameter("id"));
            ResultSet rs = stmt.executeQuery("select * from " + tableName + " where " + req.getParameter("idname") + "=" + req.getParameter("id"));
            ResultSetMetaData rsmd = rs.getMetaData();

            StringBuilder query = new StringBuilder();

            query.append("update " + req.getParameter("tablename") + " ");
            query.append("set ");
            String column, value;
            while(rs.next()) {
                for(int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                    column = rsmd.getColumnLabel(i);
                    value = req.getParameter(column);
                    if(!column.contains("id"))
                        query.append(column + " = '" + value + "', ");
                }
                query.delete(query.length() - 2, query.length());
            }
            query.append(" where " + req.getParameter("idname") + " = '" + req.getParameter("id") + "'");

            System.out.println(query);
            PreparedStatement pstmt = con.prepareStatement(query.toString());
            pstmt.execute();

            resp.sendRedirect("Insert?table=" + tableName);

        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
