package vn.danghung.dev.adapter;

public interface EntityAdapter<F,T>{
    T transform(F f);
}
