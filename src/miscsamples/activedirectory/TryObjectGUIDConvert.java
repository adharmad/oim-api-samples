package miscsamples.activedirectory;

public class TryObjectGUIDConvert {

    public static void main(String[] args) {
      
        byte [] bb = new byte[128];
        for (int ii=0 ; ii<128 ; ii++)
        {
            bb[ii] = (byte)ii;
        }
        
        byte[] b = {12, 23, 44, 56, 8, 16, 32, 64, 127, 3, 99, 77, 37, 88, 43, 66};
        String s = toHexString(bb);
        System.out.println(s);
        
        byte[] b1 = toByteArray(s);

        for (int i=0 ; i<b1.length ; i++)
        {
            System.out.print(b1[i] + " " );
        }
        System.out.println();
        for (int i=0 ; i<bb.length ; i++)
        {
            System.out.print(bb[i] + " " );
        }
        
    }
    
    public static byte[] toByteArray(String s)
    {
        int len = s.length()/2;
        byte[] b = new byte[len];
        
        int i=0;
        int cnt = 0;
        
        while (i<s.length())
        {
            char c1 = s.charAt(i);
            char c2 = s.charAt(i+1);
            i += 2;
            
            String s1 = c1 + "" + c2;
            
            int ii = Integer.parseInt(s1, 16);
            b[cnt] = (byte)ii;
            cnt++;
        }
        
        return b;
    }
    
    public static String toHexString(byte bytes[])
    {
        StringBuffer retString = new StringBuffer();
        
        for (int i = 0; i < bytes.length; ++i)
        {
            retString.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1));
        }
        return retString.toString();
    }
    
}
