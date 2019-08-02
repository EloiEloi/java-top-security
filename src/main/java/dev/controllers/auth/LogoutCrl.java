package dev.controllers.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/logout")
public class LogoutCrl extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// req.getSession().invalidate();

		Cookie authCookie = new Cookie("auth_cookie", "");
		authCookie.setMaxAge(0);
		resp.addCookie(authCookie);

		resp.sendRedirect(req.getContextPath() + "/login");
	}
}
