package com.example.stickerjetpackcomp.utils.core.utils.hawk;

public interface Serializer {

    <T> String serialize(String cipherText, T value);

    DataInfo deserialize(String plainText);

}