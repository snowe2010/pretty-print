package com.tylerthrailkill.helpers.prettyprint;

public class StaticFieldsObjectCyclicReference1 {
    public static final StaticFieldsObjectCyclicReference1 BAR;
    public StaticFieldsObject n;
    
    static {
        BAR = new StaticFieldsObjectCyclicReference1(StaticFieldsObject.FOO);
    }

    public StaticFieldsObjectCyclicReference1(StaticFieldsObject i) {
        n = i;
    }
}
