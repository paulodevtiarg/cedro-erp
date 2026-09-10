package br.com.dpsistemas.cedroerp.configs;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
    	
    	 String uri = request.getRequestURI();

    	// LIBERA TUDO QUE É PÚBLICO
    	 if (uri.equals("/") || // 🔥 ESSA LINHA QUE FALTA
    	     uri.startsWith("/login") ||
    	     uri.startsWith("/landing") ||
    	     uri.startsWith("/css") ||
    	     uri.startsWith("/js") ||
    	     uri.startsWith("/images") ||
    	     uri.startsWith("/img") ||
    	     uri.startsWith("/uploads") ||
    		 uri.startsWith("/esqueci-senha") ||
    		 uri.startsWith("/reset-senha")||
    		 uri.startsWith("/validar-reset-senha")){
    	     return true;
    	 }

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("usuarioLogado") == null) {

            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
