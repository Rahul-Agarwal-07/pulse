package com.rahul.pulse.auth.application.ports;

public interface TokenHasher {

    String hash(String token);

}
