package com.mockrunner.mock.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;

/**
 * Mock implementation of <code>HttpSession</code>.
 */
public class MockHttpSession implements HttpSession
{
    public HashMap attributes = new HashMap();
    private String sessionId;
    private boolean isNew;
    private boolean isValid;
    private long creationTime;
    private ServletContext servletContext;
    private int maxInactiveInterval = -1;

    public MockHttpSession()
    {
        super();
        isValid = true;
        creationTime = System.currentTimeMillis();
        sessionId = new Double(Math.random()).toString();
    }

    public void setupServletContext(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext()
    {
        return servletContext;
    }
    
    public boolean isValid()
    {
        return isValid;
    }

    public boolean isNew()
    {
        return isNew;
    }

    public void setUpIsNew(boolean isNew)
    {
        this.isNew = isNew;
    }

    public long getCreationTime()
    {
        return creationTime;
    }

    public void invalidate()
    {
        Map clone = new HashMap(attributes);
        Iterator keys = clone.keySet().iterator();
        while (keys.hasNext())
        {
            removeAttribute((String) keys.next());
        }
        isValid = false;
    }

    public String getId()
    {
        return sessionId;
    }

    public Object getValue(String key)
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        return getAttribute(key);
    }

    public String[] getValueNames()
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        Vector attKeys = new Vector(attributes.keySet());
        return (String[]) attKeys.toArray();
    }

    public void putValue(String key, Object value)
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        setAttribute(key, value);
    }

    public void removeValue(String key)
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        removeAttribute(key);
    }
    
    public void clearAttributes()
    {
        attributes.clear();
    }

    public Object getAttribute(String key)
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        return attributes.get(key);
    }

    public Enumeration getAttributeNames()
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        Vector attKeys = new Vector(attributes.keySet());
        return attKeys.elements();
    }

    public void removeAttribute(String key)
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        callValueUnboundMethod(key, attributes.get(key));
        attributes.remove(key);
    }

    public void setAttribute(String key, Object value)
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        callValueBoundMethod(key, value);
        attributes.put(key, value);
    }

    private void callValueBoundMethod(String key, Object value)
    {
        if (value instanceof HttpSessionBindingListener)
        {
            MockHttpSessionBindingEvent event = new MockHttpSessionBindingEvent(this, key, attributes.get(key));
            ((HttpSessionBindingListener) value).valueBound(event);
        }
    }

    private void callValueUnboundMethod(String key, Object value)
    {
        if (value instanceof HttpSessionBindingListener)
        {
            MockHttpSessionBindingEvent event = new MockHttpSessionBindingEvent(this, key, attributes.get(key));
            ((HttpSessionBindingListener) value).valueUnbound(event);
        }
    }
    
    public long getLastAccessedTime()
    {
        return System.currentTimeMillis();
    }
    
    public void setMaxInactiveInterval(int maxInactiveInterval)
    {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public int getMaxInactiveInterval()
    {
        return maxInactiveInterval;
    }

    public HttpSessionContext getSessionContext()
    {
        return new MockSessionContext();
    }
}
