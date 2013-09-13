package iamsamples.auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.Signature;
import java.security.SignedObject;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Properties;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.crypto.tcSignatureMessage;
import com.thortech.xl.util.config.ConfigurationClient;

public class SigLogin {

    public static void main(String[] args) throws Exception
    {
        PrivateKey privateKey;
        X509Certificate cert;
        KeyStore keyStore;
        Signature signatureEngine;
        
        // Below is configuration information regarding the keystore, passwords,
        // providers, algorithm etc.
        // Security of the keystore/passwords etc. is responsibility of the customer
        // This is just test code and so I have put everything in clear text here 
        String keystoreFile = "xlclient.jks";
        String ksPassword = "********";
        String keyPass = "********";
        String providerClassName = "sun.security.provider.Sun";
        String keystoreType = "JKS";
        File ksFile = new File(keystoreFile);
        String alias = "xlclient";
        String signatureAlgorithm = "SHA1withRSA";
        String signatureProviderClassName = "sun.security.rsa.SunRsaSign";
        //String signatureProviderClassName = "sun.security.provider.Sun";
        
        // Initialize the signature engine
        Class sigProviderClass = Class.forName(signatureProviderClassName);
        Provider sigProvider = (Provider) sigProviderClass.newInstance();
        signatureEngine = Signature.getInstance(signatureAlgorithm, sigProvider.getName());

        // Load the keystore
        Class providerClass = Class.forName(providerClassName);
        Provider provider = (Provider) providerClass.newInstance();
        Security.addProvider(provider);

        keyStore = KeyStore.getInstance(keystoreType, provider.getName());
        keyStore.load(new FileInputStream(ksFile), ksPassword.toCharArray());
        
        // Get the private key and certificate
        privateKey = (PrivateKey)keyStore.getKey(alias, keyPass.toCharArray());
        cert = (X509Certificate)keyStore.getCertificate(alias);
        
        // Create a signed object. Use the user login that you need to login as
        SignedObject so = new SignedObject("xelsysadm", privateKey, signatureEngine);
        
        // OIM specific code starts here! All code above is generic and just provided
        // as an example
        System.out.println("performing signature login");
        Properties jndi = ConfigurationClient.getComplexSettingByPath(
            "Discovery.CoreServer").getAllSettings();
        tcSignatureMessage signedMsg = new tcSignatureMessage(so, cert);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(signedMsg);
        oos.flush();
        byte[] buf = bos.toByteArray();
        Base64 base64 = new Base64();
        String password = "xlSigned::" + base64.encodeBase64String(buf);
        
        //tcUtilityFactory factory = new tcUtilityFactory(jndi, signedMsg);
        tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", password);
        tcUserOperationsIntf usrIntf = (tcUserOperationsIntf)factory.getUtility(
            "Thor.API.Operations.tcUserOperationsIntf");

        System.out.println("signature login complete");
        
        tcResultSet rs = usrIntf.findUsersFiltered(new HashMap(), new String[] {"Users.User ID", "Users.First Name", "Users.Last Name"});
        
        for (int i = 0; i < rs.getRowCount(); ++i) {
            rs.goToRow(i);
            System.out.println(rs.getStringValue("Users.User ID") + " " + rs.getStringValue("Users.First Name") + " " + rs.getStringValue("Users.Last Name"));
        }

        factory.close();
        System.out.println("logout complete");
        System.exit(0);
        
    }

}

