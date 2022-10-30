import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteValidation")
public class DeleteValidation extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder html = new StringBuilder();

        String tableName = req.getParameter("tablename");
        String id = req.getParameter("id");

        html.append(MessageFormat.format("""
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
        """, tableName, id));

        MyAdmin.putArgument("deleteModal", html.toString());
        resp.sendRedirect("My-Admin");
    }
}
