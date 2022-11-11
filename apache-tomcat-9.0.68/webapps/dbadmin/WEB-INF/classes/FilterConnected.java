import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/Connection")
public class FilterConnected implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        if(null != PsqlConnection.getConnection()) {
            System.out.println("[LOG] " + PsqlConnection.getConnection());
            ((HttpServletResponse)(res)).sendRedirect("My-Admin");
        } else {
            chain.doFilter(req, res);
        }
    }
    
}
