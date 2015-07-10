package com.denimexpertexpo.denimexpo.Interfaces;

/**
 * Created by ratul on 7/9/2015.
 */


/*
Actually this interface is used, when Fragment switching is done in the registration activity,
registration activity actually involved with two fragment. when registration process completed,
the activity need to know about this event, so that it can switch to another fragment :)
 */
public interface RegistrationEventHandler {
    public void registrationProcessCompleted(String emailAddr);
}