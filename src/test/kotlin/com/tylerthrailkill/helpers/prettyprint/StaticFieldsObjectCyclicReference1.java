package com.tylerthrailkill.helpers.prettyprint;

public class StaticFieldsObjectCyclicReference1 {
//    public static final StaticFieldsObjectCyclicReference1 FOO;
    public static final StaticFieldsObjectCyclicReference1 BAR;
//    public static final StaticFieldsObject BAZ;
//    public static final StaticFieldsObject BIZ;
//    public static final StaticFieldsObject BOOZE;
    public StaticFieldsObject n;
    
    static {
//        FOO = new StaticFieldsObjectCyclicReference1(BAR);
        BAR = new StaticFieldsObjectCyclicReference1(StaticFieldsObject.FOO);
//        BAZ = new StaticFieldsObject(2);
//        BIZ = new StaticFieldsObject(3);
//        BOOZE = new StaticFieldsObject(4);
    }

    public StaticFieldsObjectCyclicReference1(StaticFieldsObject i) {
        n = i;
    }
}
