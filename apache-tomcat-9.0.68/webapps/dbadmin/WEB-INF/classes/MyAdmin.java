import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openide.util.MapFormat;

/**
 * toute les tables de la base :
 * select tablename from pg_catalog.pg_tables where schemaname != 'pg_catalog' and schemaname != 'information_schema'; 
 */

// a filtré = pas accessible sans connection
@WebServlet("/My-Admin")
public class MyAdmin extends HttpServlet {

    private Connection con;
    private static Map<String, String> htmlArguments = new HashMap<>();

    public MyAdmin() {
        htmlArguments.put("select", "");
        htmlArguments.put("listTables", "");
        htmlArguments.put("tablename", "Please select a table...");
        htmlArguments.put("deleteModal", "");
        htmlArguments.put("insertOrUpdateForm", "");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.con = PsqlConnection.getConnection();
            PrintWriter out = resp.getWriter();
            Statement stmt = this.con.createStatement();
            ResultSet rs = stmt.executeQuery("select tablename from pg_catalog.pg_tables where schemaname != 'pg_catalog' and schemaname != 'information_schema'");
            
            StringBuilder listTables = new StringBuilder();

            while(rs.next())
                listTables.append(
                    "<li><a class=\"btn btn-light btn-sm\" href=\"Insert?table=" + 
                    rs.getString("tablename") + 
                    "\">" + rs.getString("tablename") + "</a></li>"
                );
            htmlArguments.replace("listTables", listTables.toString());

            String html = MapFormat.format("""
                
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
                    <link rel="canonical" href="https://getbootstrap.com/docs/5.2/examples/sidebars/">
                    <link rel="apple-touch-icon" href="/docs/5.2/assets/img/favicons/apple-touch-icon.png" sizes="180x180">
                    <link rel="icon" href="/docs/5.2/assets/img/favicons/favicon-32x32.png" sizes="32x32" type="image/png">
                    <link rel="icon" href="/docs/5.2/assets/img/favicons/favicon-16x16.png" sizes="16x16" type="image/png">
                    <link rel="manifest" href="/docs/5.2/assets/img/favicons/manifest.json">
                    <link rel="mask-icon" href="/docs/5.2/assets/img/favicons/safari-pinned-tab.svg" color="#712cf9">
                    <link rel="icon" href="/docs/5.2/assets/img/favicons/favicon.ico">
                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

                    <title>Document</title>
                    <style>
                        .my-custom-scrollbar {
                            position: relative;
                            overflow: auto;
                        }
                        .table-wrapper-scroll-y {
                            display: block;
                        }
                    </style>
                </head>
                
                <body cz-shortcut-listen="true">

                    <nav class="navbar bg-light">
                        <div class="container-fluid">
                        <a class="navbar-brand" href="#">
                            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Bootstrap_logo.svg/512px-Bootstrap_logo.svg.png" alt="Logo" width="30" height="24" class="d-inline-block align-text-top">
                            Bootstrap DB admin project
                        </a>
                        <a class="btn btn-danger" href="Disconnect" role="button">Disconnect</a>
                        </div>
                    </nav>

                    <main class="d-flex flex-nowrap">
                    
                    <div class="flex-shrink-0 p-3 bg-light" style="width: 20%; height:100%; position:fixed">
                        <ul class="list-unstyled ps-3">
                        <li class="mb-1">
                            <button class="btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed" data-bs-toggle="collapse" data-bs-target="#home-collapse" aria-expanded="true">
                            Tables
                            </button>
                            <div class="collapse show" id="home-collapse">
                            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
                                {listTables}
                            </ul>
                            </div>
                        </li>
                        </ul>
                    </div>
                    
                    <div class="container" style="position:fixed; width:77%; margin-left:22%; height:87%; margin-top:15px">
                    {tablename}
                        <div class="container overflow-auto" style="height:42%;border:solid 1px rgb(236, 236, 238); border-radius: 7px; margin-top:15px">
                            {insertOrUpdateForm}
                        </div>
                        <div class="container my-custom-scrollbar table-wrapper-scroll-y" style="height:50%; margin-top:15px; padding-bottom:15px">
                            {select}
                        </div>
                    </div>

                    </main>

                    <!-- modals -->
                    {deleteModal}
                    <div class="modal fade" id="deleteModal" tabindex="-1" data-bs-backdrop="static" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                Are you sure you want to delete this line ?
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <a type="button" href="Delete?tablename={0}&id={1}" class="btn btn-danger">Delete</a>
                            </div>
                            </div>
                        </div>
                    </div>
                    

                    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js" integrity="sha384-oBqDVmMz9ATKxIep9tiCxS/Z9fNfEXiDAYTujMAeBAsjFuCZSmKbSSUnQlmh/jp3" crossorigin="anonymous"></script>
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.min.js" integrity="sha384-IDwe1+LCz02ROU9k972gdyvl+AESN10+x7tBKgc9I5HFtuNz0wWnPclzo6p9vxnk" crossorigin="anonymous"></script>
                </body>
                </html>

            """, this.htmlArguments);

            out.println(html);

        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    public static void putArgument(String k, String v) {
        htmlArguments.replace(k, v);
    }

}
