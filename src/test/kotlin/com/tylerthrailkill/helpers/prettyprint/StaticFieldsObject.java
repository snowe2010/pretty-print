package com.tylerthrailkill.helpers.prettyprint;

public class StaticFieldsObject {
    public static final StaticFieldsObject FOO;
    public static final StaticFieldsObject BAR;
    public static final StaticFieldsObject BAZ;
    public static final StaticFieldsObject BIZ;
    public static final StaticFieldsObject BOOZE;
    public int n;
    
    static {
        FOO = new StaticFieldsObject(0);
        BAR = new StaticFieldsObject(1);
        BAZ = new StaticFieldsObject(2);
        BIZ = new StaticFieldsObject(3);
        BOOZE = new StaticFieldsObject(4);
    }

    public StaticFieldsObject(int i) {
        n = i;
    }
}
