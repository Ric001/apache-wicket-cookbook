package org.users.utils;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public class InstanceValidator implements Serializable {
    private static final long serialVersionUID = 1l;
    
    public static Boolean nullOrNonPresent(Optional<? extends Object> value) {
        return (Objects.isNull(value) || !value.isPresent());
    }
}