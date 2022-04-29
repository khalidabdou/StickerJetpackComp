package com.example.stickerjetpackcomp.utils.core.utils.hawk;

public interface Converter {

  <T> String toString(T value);
  <T> T fromString(String value, DataInfo dataInfo) throws Exception;

}
