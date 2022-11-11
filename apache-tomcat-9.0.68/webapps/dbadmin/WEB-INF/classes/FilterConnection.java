import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = { "/My-Admin", "/Insert", "/Select", "/Delete", "/DeleteValidation", "/Lister-Etu", "/Update"})
public class FilterConnection implements Filter {

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
            throws IOException, ServletException {
        
        if(null == PsqlConnection.getConnection()) 
            ((HttpServletResponse) arg1).sendRedirect("Connection");
        else
            arg2.doFilter(arg0, arg1);
    }
    
}
