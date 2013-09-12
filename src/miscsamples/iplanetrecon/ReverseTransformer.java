package miscsamples.iplanetrecon;

public class ReverseTransformer implements AttributeTransformer {
    public String transform(String value) {
    	String newvalue="";
    	for(int i=1; i <= value.length(); i++)
    		newvalue+=value.charAt(value.length()-i);
        return newvalue;
    }
}
