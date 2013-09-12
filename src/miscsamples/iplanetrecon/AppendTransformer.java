package miscsamples.iplanetrecon;

public class AppendTransformer implements AttributeTransformer {
    public String transform(String value) {
        return value + "123";
    }
}
