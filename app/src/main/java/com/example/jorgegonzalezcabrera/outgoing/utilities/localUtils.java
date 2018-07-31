package com.example.jorgegonzalezcabrera.outgoing.utilities;

import com.example.jorgegonzalezcabrera.outgoing.models.entry.type;

public class localUtils {

    public static type getTypeFromOrdinal(int ordinal) {
        return (ordinal == type.OUTGOING.ordinal()) ? type.OUTGOING : type.INCOME;
    }
}
