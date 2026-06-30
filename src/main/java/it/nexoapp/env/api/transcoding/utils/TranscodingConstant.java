package it.nexoapp.env.api.transcoding.utils;

import it.nexoapp.common.exception.IllegalInstantiationException;

public class TranscodingConstant {

    private TranscodingConstant() {
        throw new IllegalInstantiationException();
    }

    public static final String ISTAT_DOMAIN = "istat";
    public static final String BELFIORE_DOMAIN = "belfiore";
    public static final String ZIPCODE_DOMAIN = "zipCode";

}