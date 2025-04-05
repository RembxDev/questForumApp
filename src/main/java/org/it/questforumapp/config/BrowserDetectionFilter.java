package org.it.questforumapp.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter("/*")
public class BrowserDetectionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            String userAgent = httpRequest.getHeader("User-Agent");
            String browser = detectBrowser(userAgent);
            httpRequest.setAttribute("browser", browser);
        }
        chain.doFilter(request, response);
    }

    private String detectBrowser(String userAgent) {
        if (userAgent == null) {
            return "other";
        }
        String userAgentLower = userAgent.toLowerCase();

        if (userAgentLower.contains("edg")) {
            return "edge";
        } else if (userAgentLower.contains("chrome") && !userAgentLower.contains("edg")) {
            return "chrome";
        } else if (userAgentLower.contains("firefox")) {
            return "firefox";
        } else if (userAgentLower.contains("opera") || userAgentLower.contains("opr")) {
            return "opera";
        } else if (userAgentLower.contains("safari")) {
            return "safari";
        }
        return "other";
    }

}
