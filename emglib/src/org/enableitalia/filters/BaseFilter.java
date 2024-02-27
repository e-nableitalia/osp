package org.enableitalia.filters;

public interface BaseFilter {
    float[] filter(float[] values);
    void setTimeConstant(float timeConstant);
    void reset();
}