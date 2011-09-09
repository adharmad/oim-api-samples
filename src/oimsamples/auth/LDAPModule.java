package oimsamples.auth;

import com.thortech.util.logging.Logger;
import com.thortech.xl.security.*;
import com.thortech.xl.util.logging.LoggerMessages;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

import javax.naming.*;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.security.auth.*;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import weblogic.security.principal.WLSGroupImpl;
import weblogic.security.principal.WLSUserImpl;

public class LDAPModule implements LoginModule {

	private Subject subject;
	private CallbackHandler callbackHandler;
	private String username;
	private char[] arPassword;
	private String password;

	private boolean succeeded = false;
	private boolean commitSucceeded = false;

	private Vector     principalsForSubject = new Vector(); 

	public LDAPModule() {
	}

	public void initialize(Subject subject, CallbackHandler callbackhandler,
			Map map, Map map1) {
		this.subject = subject;
		this.callbackHandler = callbackhandler;
		System.out.println("[LDAPModule]:initialize - LDAPModule: initialize()");
		System.out.println("[LDAPModule]:initialize - Subject: " + this.subject);
		System.out.println("[LDAPModule]:initialize - Callbackhandler: "
				+ this.callbackHandler);
		System.out.println("[LDAPModule]:initialize - callbackhandler instanceof "
				+ this.callbackHandler.getClass().getName());
		System.out.println("[LDAPModule]:initialize - map: " + map);
		System.out.println("[LDAPModule]:initialize - map1: " + map1);

	}

	public boolean login() throws LoginException {
		System.out.println("[LDAPModule] - login()");
		Callback[] callbacks = new Callback[2];
		System.out.println("[LDAPModule]:login - setting callbacks");

		callbacks[0] = new NameCallback("SampleModule username: ");
		callbacks[1] = new PasswordCallback("SampleModule password: ", false);
		System.out.println("[LDAPModule] - login()- SampleModule username: ");

		try {
			this.callbackHandler.handle(callbacks);
			System.out.println("[LDAPModule] - login()- handle");
			username = ((NameCallback) callbacks[0]).getName();
			System.out.println("[LDAPModule]:login - userName = " + username);

			char[] tmpPassword = ((PasswordCallback) callbacks[1])
					.getPassword();
			if (tmpPassword == null) {
				// treat a NULL password as an empty password
				tmpPassword = new char[0];
			}
			arPassword = new char[tmpPassword.length];
			System.arraycopy(tmpPassword, 0, arPassword, 0, tmpPassword.length);
			((PasswordCallback) callbacks[1]).clearPassword();
			password = new String(arPassword);
			System.out.println("[LDAPModule]:login - password = *******");
		} catch (Exception ex) {
			System.out.println("[LDAPModule]:initialize - Error = "
							+ ex.getMessage());
			ex.printStackTrace();
		}

		if (this.callbackHandler == null) {
			System.out.println("[LDAPModule]:login - callbackHandler == null: Returning");
			return false;
		}

		// check if user is xelsysadm
		/*
		if (username.equalsIgnoreCase("xelsysadm")) {
			System.out.println("[LDAPModule]:login - callbackHandler: XELSYSADM ");
			try {
				System.out.println("[LDAPModule]:login - callbackHandler: XELSYSADM - Calling Authenticate ");
				Authenticate auth = new Authenticate(true);
				auth.connect(username, password);
			} catch (Exception ex) {
				System.out.println("[LDAPModule]:login - callbackHandler: XELSYSADM "
						+ ex);
				succeeded = false;
				return false;
			}
			System.out.println("[LDAPModule]:login - callbackHandler: XELSYSADM Login Success");
			succeeded = true;
			return true;
		}
		*/

		// now authenticate other user

		// String HOST="cos-us-dc20.xxx.com";
		String BASE_DN = "dc=test,dc=com";
		String HOST = "ldaphost";
		String PORT = "389";
		String URL = "ldap://" + HOST + ":" + PORT + "/" + BASE_DN;
		String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

		/*
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("ldapAuthServer.properties"));
			BASE_DN = prop.getProperty("baseDN");
			HOST = prop.getProperty("host");
			PORT = prop.getProperty("port");
			URL = "ldap://" + HOST + ":" + PORT + "/" + BASE_DN;
			FACTORY = prop.getProperty("factory");

		} catch (IOException e) {
			System.out.println("[LDAPModule]:login - Error while reading properties file= "
							+ e.getMessage());
			// return false;
		}
		 */
		
		String PRINCIPAL = "CN=" + username + ",CN=Users," + BASE_DN;

		Hashtable env = new Hashtable();

		env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
		env.put(Context.PROVIDER_URL, URL);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, PRINCIPAL);

		System.out.println("[LDAPModule]:login - About to authenticate " + env);

		env.put(Context.SECURITY_CREDENTIALS, password);

		try {
			DirContext authContext = new InitialLdapContext(env, null);
			// Authentication succeed
			authContext.close();
			System.out.println("[LDAPModule]:login - authentication succed ");
			succeeded = true;
			
		} catch (NamingException e) {
			System.out.println("[LDAPModule]:login - authentication failed "
					+ e.getMessage());
			// Authentication failed
			e.printStackTrace();
			succeeded = true;
			
		}
		
		addGroupsForSubject(username);
		return succeeded;
	}

	public boolean commit() {
		System.out.println("[LDAPModule]: commit()");
		if (!succeeded) {
			System.out.println("[LDAPModule]:commit- succeed = false");
			return false;
		}

		System.out.println("[LDAPModule]:commit- succeed = true");

		subject.getPrincipals().addAll(principalsForSubject);
		
		//userPrincipal = new XLUser(username);
		//rolePrincipals = new XLRole[2];
		//rolePrincipals[0] = new XLRole("Guest");
		//rolePrincipals[1] = new XLRole("User");
		WLSUserImpl wlsUser = new WLSUserImpl(username);
		if (!principalsForSubject.contains(wlsUser)) {
			principalsForSubject.add(wlsUser);
		}
		
		
		/*
		if (!subject.getPrincipals().contains(userPrincipal))
			subject.getPrincipals().add(userPrincipal);
		if (!subject.getPrincipals().contains(rolePrincipals[0]))
			subject.getPrincipals().add(rolePrincipals[0]);
		if (username != null && username.equals("") == false) {
			if (!subject.getPrincipals().contains(rolePrincipals[1]))
				subject.getPrincipals().add(rolePrincipals[1]);
		}
		*/
		
		username = null;
		for (int i = 0; i < arPassword.length; i++)
			arPassword[i] = ' ';
		password = null;

		commitSucceeded = true;
		return true;
	}

	public boolean abort() {
		System.out.println("LDAPModule: abort()");
		if (!succeeded)
			return false;
		if (succeeded && !commitSucceeded) {
			succeeded = false;
			username = null;
			if (password != null) {
				for (int i = 0; i < arPassword.length; i++)
					arPassword[i] = ' ';
				password = null;
			}
			//userPrincipal = null;
			//rolePrincipals = null;
			principalsForSubject = new Vector();
		} else {
			logout();
		}
		return true;
	}

	public boolean logout() {
		System.out.println("LDAPModule: logout()");
		WLSUserImpl wlsUser = new WLSUserImpl(username);
		principalsForSubject.remove(wlsuser);
		/*
		subject.getPrincipals().remove(userPrincipal);
		succeeded = false;
		succeeded = commitSucceeded;
		username = null;
		if (password != null) {
			for (int i = 0; i < arPassword.length; i++)
				arPassword[i] = ' ';
			password = null;
		}
		userPrincipal = null;
		*/
		return true;
	}
	
	 /**
	  * Add the user's groups to the list of principals to be added to the subject.
	  *
	  * @param A String containing the user name the user's name.
	  */
	    private void addGroupsForSubject(String userName) throws LoginException {
	    // Get the user's list of groups (recursively - so, if user1 is a member
	    // of group1 and group1 is a member of group2, then it returns group1 and
	    // group2).  Iterate over the groups, adding each to the list of principals
	    // to add to the subject.

	    	System.out.println(LoggerMessages.getMessage("DataMethodDebug", "XLDBRegistry/getUsersForGroup", "userName", userName));
	    	principalsForSubject.add(new WLSGroupImpl("User"));
	  }
	

}