package miscsamples.ctrecon;

public interface CTSearchCriterion {
    
    public static final int NO_CRITERION = 0; // get all users
    public static final int USERID_STARTSWITH = 1;
    public static final int FIRSTNAME_STARTSWITH = 2;
    public static final int LASTNAME_STARTSWITH = 3;
    public static final int USERID_EQUALS = 4;
    public static final int FIRSTNAME_EQUALS = 5;
    public static final int LASTNAME_EQUALS = 6;
    public static final int MODIFIED_AFTER = 7;
}
