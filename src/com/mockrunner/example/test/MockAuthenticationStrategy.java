package com.mockrunner.example.test;

import com.mockrunner.example.AuthenticationStrategy;

/**
 * Mock implementation of {@link com.mockrunner.example.AuthenticationStrategy}
 * because we do not want a database connection into the tests.
 * Used by {@link AuthenticationActionTest}.
 */
public class MockAuthenticationStrategy implements AuthenticationStrategy
{
    private boolean isLoginOk;
    private boolean isUserKnown;
    private boolean isPasswordOk;
    
    public void setupLoginOk(boolean isLoginOk)
    {
        this.isLoginOk = isLoginOk;
    }

    public void setupPasswordOk(boolean isPasswordOk)
    {
        this.isPasswordOk = isPasswordOk;
    }

    public void setupUserKnown(boolean isUserKnown)
    {
        this.isUserKnown = isUserKnown;
    }
    
    public boolean authenticate(String username, String password)
    {        
        return isLoginOk;
    }

    public boolean wasLastPasswordOk()
    {
        return isPasswordOk;
    }

    public boolean wasLastUserKnown()
    {
        return isUserKnown;
    }
}