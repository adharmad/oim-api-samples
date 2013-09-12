import Thor.API.*;
import Thor.API.Exceptions.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.Hashtable;
import java.util.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import Thor.API.tcUtilityFactory;

import com.sun.crypto.provider.SunJCE;
import com.sun.net.ssl.internal.ssl.Provider;
import com.thortech.xl.crypto.tcCertKeyLoader;
import com.thortech.xl.crypto.tcSignatureMessage;
import com.thortech.xl.crypto.tcSignatureUtil;

import java.util.*;
import java.io.*;



import java.security.*;
import java.security.cert.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.crypto.interfaces.*;
import java.security.spec.*;

public class EnsembleRecon {

    public EnsembleRecon(String ns, String contextname, String db,
    	String user, String pwd, String fileName) throws Exception {

		Security.addProvider(new SunJCE());
		Security.addProvider(new Provider());
		Security.addProvider(new BouncyCastleProvider());

	        System.getProperties().put("OrbixWeb.IT_NAMES_SERVER_HOST", ns);
	        System.getProperties().put("XL.ContextName", contextname);
			System.out.println("Logging to xellerate with dbname " + db +
				" userId " + user);

			// try to load xl properties
			System.out.println("Reading xellerate_object.cfg");

			loadPropertiesFromFile("xellerate_object.cfg");

			System.out.println("xl properties file read");

			System.getProperties().put("SEC.KeyStoreLocation", "c:/thor1055/xellerate/");  
			System.getProperties().put("SEC.KeyStore", ".xlkeystore");
			//System.getProperties().put("SEC.KeyStorePassword", "xellerate");  
			System.getProperties().put("SEC.KeyAlias", "xell");  

			Class loaderClass = Class.forName("com.thortech.xl.crypto.tcCertKeyLoader");

			System.out.println("com.thortech.xl.crypto.tcCertKeyLoader loaded!");

			tcCertKeyLoader loaderInstance = (tcCertKeyLoader)loaderClass.newInstance();
			System.out.println(loaderInstance.getClass().getName());

			System.out.println("Created new instance of tcCertKeyLoader");


			//X509Certificate certificate = loaderInstance.getXellerateCertificate();  
			X509Certificate certificate = getXellerateCertificate("xell", "c:/thor1055/xellerate/.xlkeystore", "xellerate");  


			System.out.println("got xellerate certificate"); 

			if (certificate == null) {
				System.out.println("DIEEEEEEEEEEEEEEEE");
			}


			System.out.println(certificate.toString());

			PrivateKey privateKey = (PrivateKey)loaderInstance.getXelleratePrivateKey();
			tcSignatureMessage signatureMessage = tcSignatureUtil.createSignedMessage(
					user, privateKey, certificate);

			System.out.println("printing sig");
			System.out.println(signatureMessage.toString());			

			factory = new tcUtilityFactory(db, signatureMessage);


			System.out.println("LOGIN COMPLETE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    }

    public static void main(String[] args) throws Exception {

        EnsembleRecon ensembleRecon = new EnsembleRecon(args[0], args[1], args[2], args[3], args[4], args[5]);
        System.exit(0);
    }

     private void close() {
        try {
            factory.close();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

	public void loadPropertiesFromFile(String propsFileName) {
		FileInputStream propsInputStream;
		try {
			propsInputStream = new FileInputStream(propsFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		try {
			System.getProperties().load(
				new BufferedInputStream(propsInputStream));
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		/*
		try {

			Properties p = System.getProperties();
			Enumeration keys = p.propertyNames();
			while (keys.hasMoreElements()) {
				System.out.println(keys.nextElement());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

    public X509Certificate getXellerateCertificate(String psAlias, String psKeystoreName, String psKeystorePwd)
        throws Exception {
    KeyStore moKeyStore=KeyStore.getInstance("PKCS12", "BC");

	System.out.println(moKeyStore.toString());

    String msPassword = psKeystorePwd;
    char[] macPassword = msPassword.toCharArray();

	System.out.println(macPassword);

    String ksname = System.getProperty("XL.ConfigDir") + File.separator + ".xlkeystore";
    if((psKeystoreName != null) && (psKeystoreName.trim().equals("")==false))
      ksname = psKeystoreName;
    FileInputStream fis = new FileInputStream(ksname);
	System.out.println(fis);

    moKeyStore.load(fis, macPassword);
	System.out.println("after loading!");

	if (moKeyStore.containsAlias(psAlias)) {
		System.out.println("YES!!!!!!!!!!!!!!");
	}

    X509Certificate moCert = (X509Certificate) moKeyStore.getCertificate(psAlias);
	System.out.println("after getting certificate");
	System.out.println(moCert.toString()); 
    return moCert;
  }


    private tcUtilityFactory factory;
	private String fileName;
}
