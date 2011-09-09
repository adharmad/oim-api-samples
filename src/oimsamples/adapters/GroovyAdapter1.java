package oimsamples.adapters;

import com.thortech.xl.dataobj.tcADPClassLoader;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;

public class GroovyAdapter1 {
	public static void main(String[] args) throws Exception {
		invokeGroovyScript();
	}
	public static void invokeGroovyScript() throws Exception {

		tcADPClassLoader classLoader = tcADPClassLoader.getClassLoader();
		//ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		String[] roots = new String[] { "c:/tmp/groovy/"};
		GroovyScriptEngine gse = new GroovyScriptEngine(roots, classLoader);
		Binding binding = new Binding();
		binding.setVariable("driver", "oracle.jdbc.driver.OracleDriver");
		binding.setVariable("user", "foo");
		binding.setVariable("password", "bar");
		binding.setVariable("url", "jdbc:oracle:thin:@dbhost:1521:orcl");
		gse.run("foo.groovy", binding);
	}
}
