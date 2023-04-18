package com.brainstation23.erp.Decoder;

import java.util.Base64;

public class JwtDecoder {
    public JwtDecoder()
    {

    }
    public static String  getPayload(String authHeader)
    {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = authHeader.split("\\.");
        return new String(decoder.decode(chunks[1]));
    }
}
