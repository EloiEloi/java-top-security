package dev.controllers.auth;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.domains.User;
import dev.services.LoginService;
import dev.services.ServicesFactory;
import dev.utils.HachageUtils;
import dev.utils.HmacSignature;

@WebServlet("/login")
public class LoginCrl extends HttpServlet {

	private LoginService loginService = ServicesFactory.LOGIN_SERVICE;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String login = req.getParameter("login");
		String password = req.getParameter("pass");

		Optional<User> userOpt = loginService.connect(login);

		if (userOpt.isPresent() && HachageUtils.verifierMotDePasse(password, userOpt.get().getPassword())) {
			User user = userOpt.get();

			String delimiter = "-";

			String message = user.getFirstname() + delimiter + user.getLastname() + delimiter + user.getLogin() + delimiter + user.getAdmin();
			String hmac = HmacSignature.hmacDigest(message, "key", "HmacSHA1");

			Cookie authCookie = new Cookie("auth_cookie", message + delimiter + hmac);
			authCookie.setHttpOnly(true);
			resp.addCookie(authCookie);

			// req.getSession().setAttribute("connectedUser", user);

			resp.sendRedirect(req.getContextPath() + "/users/list");
		} else {
			req.setAttribute("errors", "les informations fournies sont incorrectes");
			req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
		}
	}
}
