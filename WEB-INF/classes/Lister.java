import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Lister-Etu")
public class Lister extends HttpServlet {
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        
        try {
            Class.forName("org.postgresql.Driver");
    
            String url = "jdbc:postgresql://psqlserv/da2i";
            String nom = "theotaccoenetu";
            String mdp = "moi";
            
            Connection con = DriverManager.getConnection(url, nom, mdp);
            Statement stmt = con.createStatement();
            
            ResultSet rs = stmt.executeQuery("select * from etudiant");

            out.println("""
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
            """);

            out.println("<h1>Liste des étudiants</h1>");
            StringBuilder list = new StringBuilder();
            list.append("<div style=\"width:70%; margin:auto;\"><table class=\"table table-bordered table-striped\">");

            while(rs.next()) {
                list.append("<tr><th>" + rs.getString("nom") + "</th><th>" + rs.getString("prenom") + "</th><th>" + rs.getDate("dnaiss") + "</th></tr>");
            }

            list.append("</table></div>");
            String form = """
                    <br>
                    <form action="Lister-Etu" method="post">
                        <label for="nom">Nom : </label>
                        <input type="text" name="nom" id="nom" required>
                        <label for="nom">Prenom : </label>
                        <input type="text" name="prenom" id="prenom" required>
                        <label for="nom">Date de naissance : </label>
                        <input type="date" name="dnaiss" id="dnaiss" required>
                        <input class="control-form" type="submit" value="soumettre">
                    </form>
            """;
            list.append(form);

            out.println(list);

            

            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }    
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();

        try {
            Class.forName("org.postgresql.Driver");
    
            String url = "jdbc:postgresql://psqlserv/da2i";
            String nom = "theotaccoenetu";
            String mdp = "moi";
            
            Connection con = DriverManager.getConnection(url, nom, mdp);
            String query = "insert into etudiant(nom, prenom, dnaiss) values(?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, req.getParameter("prenom"));
            ps.setString(2, req.getParameter("nom"));
            Date parsed = Date.valueOf(req.getParameter("dnaiss"));
            ps.setDate(
                3,
                parsed
            );

            out.println(ps.toString());

            ps.execute();
            res.sendRedirect("Lister-Etu");
            con.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
