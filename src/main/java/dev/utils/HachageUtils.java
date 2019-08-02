package dev.utils;

import org.mindrot.jbcrypt.BCrypt;

public class HachageUtils {

	public static String hacherMotDePasse(String mdp) {

		String generatedSecuredPasswordHash = BCrypt.hashpw(mdp, BCrypt.gensalt(12));

		return generatedSecuredPasswordHash;

	}

	public static boolean verifierMotDePasse(String mdpEnClair, String mdpHache) {

		boolean matched = BCrypt.checkpw(mdpEnClair, mdpHache);

		return matched;
	}

}
