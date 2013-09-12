package miscsamples.cleartrust;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import sirrus.api.client.APIServerProxy;
import sirrus.api.client.IPasswordPolicy;
import sirrus.api.client.ISparseData;
import sirrus.api.client.IUser;
import sirrus.api.client.IUserProperty;
import sirrus.connect.ConnectionDescriptor;

public class PasswordValidator {
	public static void main(String[] args) throws Exception {
		String username = "testuser";
		String password = "password";
		PasswordValidator pv = new PasswordValidator();
		System.out.println(pv.validate(username, password));
	}

	public boolean validate(String username, String password) throws Exception {
		ConnectionDescriptor eserver = new ConnectionDescriptor(
				"host", 5601, ConnectionDescriptor.CLEAR);
		APIServerProxy myServerProxy = new APIServerProxy(eserver);

		myServerProxy.connect("admin", "admin",
				"Default Administrative Group", "Default Administrative Role");
		System.out.println("Connected.");

		// Create the user object
		IUser user = (IUser) myServerProxy.getUsers().getByName(username);

		// Get the user's password policy
		ISparseData passwdPolicySparse = myServerProxy.getPasswordPolicies();
		IPasswordPolicy passwdPolicy = (IPasswordPolicy) passwdPolicySparse
				.getByName(user.getAdministrativeGroup().getPasswordPolicy());

		// Retrieve password policy details
		String passwdDictionary = passwdPolicy.getDictionaryFile();
		String passwdExcludedChars = passwdPolicy.getExclusionCharacters();
		String passwdFailureResetInterval = passwdPolicy
				.getFailureResetInterval();
		boolean passwdForceNonLetter = passwdPolicy.getForceNonLetter();
		int passwdHistorySize = passwdPolicy.getHistorySize();
		int passwdMaxFailure = passwdPolicy.getMaxFailure();
		String passwdMinPassLifetime = passwdPolicy
				.getMinimumPasswordLifetime();
		String passwdPassLifetime = passwdPolicy.getPasswordLifetime();
		boolean passwdLockout = passwdPolicy.getPasswordLockout();
		String passwdLockoutDuration = passwdPolicy
				.getPasswordLockoutDuration();
		int passwdMaxLength = passwdPolicy.getPasswordMaximumLength();
		int passwdMinLength = passwdPolicy.getPasswordMinimumLength();
		boolean passwdAdminUnlock = passwdPolicy.getAdminUnlock();

		System.out
				.println("===============================================================================");
		System.out.println("Password Policy = " + passwdPolicy.getName());
		System.out.println("Dictionary File = " + passwdDictionary);
		System.out.println("Excluded Chars = " + passwdExcludedChars);
		System.out.println("Failure Reset Interval = "
				+ passwdFailureResetInterval);
		System.out.println("Force Non Letter = " + passwdForceNonLetter);
		System.out.println("History Size = " + passwdHistorySize);
		System.out.println("Max Failure = " + passwdMaxFailure);
		System.out.println("Min Lifetime = " + passwdMinPassLifetime);
		System.out.println("Pass Lifetime = " + passwdPassLifetime);
		System.out.println("Password Lockout = " + passwdLockout);
		System.out.println("Lockout Duration = " + passwdLockoutDuration);
		System.out.println("Max Length = " + passwdMaxLength);
		System.out.println("Min Length = " + passwdMinLength);
		System.out.println("Admin Unlock = " + passwdAdminUnlock);
		System.out
			.println("===============================================================================");

		// Get the user's password expiration date
		Date userPasswordExpiration = user.getPasswordExpirationDate();
		System.out.println("User's Password Expiration Date "
				+ userPasswordExpiration);

		// Get and parse the user's password history
		IUserProperty passwordHistory = (IUserProperty) user.getUserProperty("ctscPasswordHistory");
		Object[] passwordHistoryArray = passwordHistory.getValues();
		String passwordHistoryValue = (String) passwordHistoryArray[0];
		parse(passwordHistoryValue);

		/*
		 * Compare the new password with the encrypted passwords from the user's
		 * password history.
		 */
		Iterator it = parsedPasswordHistory.iterator();
		while (it.hasNext()) {
			String itNext = (String) it.next();
			if (comparePassword(password, itNext)) {
				System.out
						.println("Password is in Password History. " + itNext);
			} else {
				System.out.println("Password is not in Password History.");
			}
		}
		System.out
			.println("===============================================================================");

		return false;
	}

	private void parse(String passwordHistory) {
		StringTokenizer st = new StringTokenizer(passwordHistory, ";");
		while (st.hasMoreTokens()) {
			parsedPasswordHistory.add(st.nextToken());
		}
	}

	private boolean comparePassword(String oldPassword, String newPassword) {
		return true;
	}

	private ArrayList parsedPasswordHistory = new ArrayList();
}