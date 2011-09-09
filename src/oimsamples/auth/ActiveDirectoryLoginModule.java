package oimsamples.auth;

import java.io.IOException;
import java.security.Principal;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import weblogic.security.principal.WLSGroupImpl;
import weblogic.security.principal.WLSUserImpl;

import com.thortech.xl.security.Authenticate;

public class ActiveDirectoryLoginModule implements LoginModule {
	// the subject for this login
	private Subject subject;

	// where to get user names, passwords, ... for this login
	private CallbackHandler callbackHandler;

	// are we in authentication or identity assertion mode?
	private boolean isIdentityAssertion;

	// have we successfully logged in?
	private boolean loginSucceeded = false;

	// did we add principals to the subject?
	private boolean principalsInSubject;

	// if so, what principals did we add to the subject
	// (so we can remove the principals we added if the login is aborted)
	private Vector<Principal> principalsForSubject = new Vector<Principal>();

	@Override
	public boolean abort() throws LoginException {
		System.out.println("in abort");
		if (principalsInSubject) {
			System.out.println("in abort removing principals");
	    	subject.getPrincipals().removeAll(principalsForSubject);
	    	principalsInSubject = false;
	    }
		System.out.println("leaving abort");
	    return true;
	}

	@Override
	public boolean commit() throws LoginException {
		System.out.println("in commit");
		if (loginSucceeded) {
			System.out.println("in commit--");
			subject.getPrincipals().addAll(principalsForSubject);
	        principalsInSubject = true;
	        System.out.println("login succeeded");
	        return true;
		} else {
			System.out.println("in commit login did not succeed");
			return false;
		}
	}

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		System.out.println("in initialize");
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.isIdentityAssertion = "true".equalsIgnoreCase((String) options.get("IdentityAssertion"));
		System.out.println("leaving initialize");
	}

	@Override
	public boolean login() throws LoginException {
		System.out.println("in login");
	    Callback[] callbacks = getCallbacks();
	    String userName = ((NameCallback)callbacks[0]).getName();
	    
	    PasswordCallback passwordCallback = (PasswordCallback)callbacks[1];
	    char[] password = passwordCallback.getPassword();
	    passwordCallback.clearPassword();
	    String passwd = new String(password);
	    System.out.println("username = " + userName);
	    System.out.println("password = " + passwd);
	    
	    if (!isIdentityAssertion){
	    	System.out.println("not identity assertion");
	    	
	    	if ("xelsysadm".equalsIgnoreCase(userName)) {
	    		System.out.println("username is xelsysadm");
	    		Authenticate auth = new Authenticate(true);
	    		auth.connect(userName, passwd);
	    		loginSucceeded = true;
	    	} else {
	    		System.out.println("username is something else");
	    		String BASE_DN = "dc=test,dc=com";
	    		String HOST = "adhost";
	    		String PORT = "389";
	    		String URL = "ldap://" + HOST + ":" + PORT + "/" + BASE_DN;
	    		String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	    		
	    		String PRINCIPAL = "CN=" + userName + ",CN=Users," + BASE_DN;

	    		Hashtable<String, String> env = new Hashtable<String, String>();

	    		env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
	    		env.put(Context.PROVIDER_URL, URL);
	    		env.put(Context.SECURITY_AUTHENTICATION, "simple");
	    		env.put(Context.SECURITY_PRINCIPAL, PRINCIPAL);
	    		env.put(Context.SECURITY_CREDENTIALS, passwd);
	    		
	    		try {
	    			System.out.println("before authenticating with directory");
	    			DirContext authContext = new InitialLdapContext(env, null);
	    			System.out.println("bind with directory succeeded");
	    			// Authentication succeed
	    			authContext.close();
	    			loginSucceeded = true;
	    		} catch (NamingException ne) {
	    			ne.printStackTrace();
	    		}
	    		
	    	}
        }
	    
	    System.out.println("outside if");
	    if (loginSucceeded) {
	    	System.out.println("login siucceeded");
	    	principalsForSubject.add(new WLSUserImpl(userName));
	    	principalsForSubject.add(new WLSGroupImpl("User"));
	    }
		
	    System.out.println("leaving login");
		return loginSucceeded;
	}

	@Override
	public boolean logout() throws LoginException {
		System.out.println("inside logout");
		return false;
	}
	
    private Callback[] getCallbacks() throws LoginException {
    	System.out.println("inside get callbacks");
    	if (callbackHandler == null) {
    		throw new LoginException("No CallbackHandler Specified");
        }

    	Callback[] callbacks;
        if (isIdentityAssertion) {
        	callbacks = new Callback[1]; // need one for the user name
        } else {
        	callbacks = new Callback[2]; // need one for the user name and one for the password
        	callbacks[1] = new PasswordCallback("password: ",false);
        }

        callbacks[0] = new NameCallback("username: ");

        try {
        	System.out.println("before handling callbacks");
        	callbackHandler.handle(callbacks);
        	System.out.println("after handling callbacks");
        } catch (IOException e1) {
        	e1.printStackTrace();
        	throw new LoginException(e1.toString());
        } catch (UnsupportedCallbackException e2) {
        	e2.printStackTrace();
        	throw new LoginException(e2.toString());
        }

        System.out.println("leaving get callbacks");
        return callbacks;
    }
}
