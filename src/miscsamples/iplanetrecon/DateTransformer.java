package miscsamples.iplanetrecon;

public class DateTransformer implements AttributeTransformer {
    public String transform(String value) {
        return  value.substring(4,6)+"/" +
				value.substring(6,8)+"/" +
				value.substring(0,4)+" " +
				" 18:15:00 PST"				;
    }
}
