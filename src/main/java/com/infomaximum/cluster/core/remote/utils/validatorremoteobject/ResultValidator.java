package com.infomaximum.cluster.core.remote.utils.validatorremoteobject;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ResultValidator {

    protected static ResultValidator SUCCESS = new ResultValidator(true, null, null);

    private final boolean success;

    private final Type type;
    private final List<String> trace;

    private ResultValidator(boolean success, Type type, List<String> trace) {
        this.success = success;
        this.type = type;
        this.trace = (trace != null) ? Collections.unmodifiableList(trace) : null;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<String> getTrace() {
        return trace;
    }

    @Override
    public String toString() {
        if (success) {
            return "ResultValidator(status: success)";
        } else {
            StringBuilder sBuilder = new StringBuilder()
                    .append("ResultValidator(status: fail, type: ")
                    .append(type);
            if (trace != null && !trace.isEmpty()) {
                sBuilder
                        .append(", trace: ")
                        .append(String.join(" => ", trace));
            }
            sBuilder.append(')');
            return sBuilder.toString();
        }
    }

    public void check() {
        if (success) return;
        throw new RuntimeException("Not serializable class. ResultValidator: " + this.toString());
    }

    protected static ResultValidator buildFailResultValidator(Type type, List<String> trace) {
        return new ResultValidator(false, type, trace);
    }
}
