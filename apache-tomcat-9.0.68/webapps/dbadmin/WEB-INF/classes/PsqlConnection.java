import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.MessageFormat;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Connection")
public class PsqlConnection extends HttpServlet {
    
    // TODO A dynamiser
    //Pour connection iut : jdbc:postgresql://psqlserv/da2i
    //Pour connection hors iut : jdbc:postgresql://localhost/dbadminproject
    private String url = "jdbc:postgresql://localhost/dbadminproject";
    private static Connection con;

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        PrintWriter out = res.getWriter();

        String htmlError ="";
        if(req.getParameter("error") != null)
            htmlError = """
                <div style="margin-top:10px" class="alert alert-danger d-flex align-items-center" role="alert">
                <div>
                    Mauvais identifiant ou mot de passe.
                </div>
            """;

        String html = MessageFormat.format("""
        
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
                <title>Document</title>
            </head>
            <body>
                <nav class="navbar bg-light">
                    <div class="container-fluid">
                    <a class="navbar-brand" href="#">
                        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Bootstrap_logo.svg/512px-Bootstrap_logo.svg.png" alt="Logo" width="30" height="24" class="d-inline-block align-text-top">
                        Bootstrap DB admin project
                    </a>
                    </div>
                </nav>
                
                <div style="display; flex; padding-left: 40%; padding-right: 40%; padding-top: 100px">
                    <form action="Connection" method="post">
                        <div class="mb-3">
                          <label for="exampleInputEmail1" class="form-label">Nom :</label>
                          <input name="login" class="form-control" id="login" aria-describedby="emailHelp">
                        </div>
                        <div class="mb-3">
                          <label for="exampleInputPassword1" class="form-label">Mot de passe :</label>
                          <input name="password" type="password" class="form-control" id="password">
                        </div>
                        <button type="submit" class="btn btn-primary">Submit</button>
                      </form>

                      {0}

                      </div>
                      
            
                <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js" integrity="sha384-oBqDVmMz9ATKxIep9tiCxS/Z9fNfEXiDAYTujMAeBAsjFuCZSmKbSSUnQlmh/jp3" crossorigin="anonymous"></script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.min.js" integrity="sha384-IDwe1+LCz02ROU9k972gdyvl+AESN10+x7tBKgc9I5HFtuNz0wWnPclzo6p9vxnk" crossorigin="anonymous"></script>
            </body>
            </html>

        """, htmlError);

        out.println(html);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        
        String nom = req.getParameter("login");
        String mdp = req.getParameter("password");

        System.out.println("[LOG] nom :" + nom + ", mdp :" + mdp);

        try {
            con = DriverManager.getConnection(this.url, nom, mdp);
            System.out.println("[LOG] Connection psql OK !");
            res.sendRedirect("My-Admin");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            res.sendRedirect("Connection?error=\"no\"");
        }
    }

    public static Connection getConnection() {
        return con;
    }
}
