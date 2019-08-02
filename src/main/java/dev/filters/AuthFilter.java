package dev.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.domains.User;
import dev.utils.HmacSignature;

@WebFilter("/*")
public class AuthFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		HttpServletResponse resp = (HttpServletResponse) servletResponse;

		Cookie[] authCookie = req.getCookies();

		User connectedUser = null;

		if (authCookie != null) {
			for (Cookie cookie : authCookie) {
				if (cookie.getName().equals("auth_cookie")) {

					String delimiter = "-";

					String contenuCookie = cookie.getValue();

					if (contenuCookie != null && contenuCookie != "") {

						String[] parts = contenuCookie.split(delimiter);

						String message = parts[0] + delimiter + parts[1] + delimiter + parts[2] + delimiter + parts[3];

						String signatureCookie = parts[4];

						String hmac = HmacSignature.hmacDigest(message, "key", "HmacSHA1");

						if (signatureCookie.equals(hmac)) {

							connectedUser = new User();

							connectedUser.setFirstname(parts[0]);
							connectedUser.setLastname(parts[1]);
							connectedUser.setLogin(parts[2]);

							// connectedUser = (User) req.getSession().getAttribute("connectedUser");

							req.setAttribute("connectedUser", connectedUser);
						}
					}

					break;

				}
			}
		}

		if (connectedUser != null || req.getRequestURI().contains("/login")) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			resp.sendRedirect(req.getContextPath() + "/login");
		}
	}

	@Override
	public void destroy() {

	}
}
