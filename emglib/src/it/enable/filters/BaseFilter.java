package it.enable.filters;

public interface BaseFilter {
    float[] filter(float[] values);
    void setTimeConstant(float timeConstant);
    void reset();
}