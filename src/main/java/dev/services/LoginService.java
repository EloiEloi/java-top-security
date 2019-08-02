package dev.services;

import java.util.List;
import java.util.Optional;

import dev.domains.User;
import dev.exceptions.AppException;
import dev.utils.DbUtils;

public class LoginService {

	public Optional<User> connect(String login) {
		List<User> results = new DbUtils().executeSelect(String.format("select * from user where login='%s'", login), resultSet -> new DbUtils().resultSetToUser(resultSet));

		if (results.size() > 1) {
			throw new AppException("at least 2 users with same login");
		}

		return results.stream().findAny();
	}

}
