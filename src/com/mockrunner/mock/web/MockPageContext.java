package com.mockrunner.mock.web;

import java.util.Stack;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * Mock implementation of <code>PageContext</code>.
 */
public class MockPageContext extends org.apache.struts.mock.MockPageContext
{
    private JspWriter jspWriter;
    private Stack outStack;

    public MockPageContext(ServletConfig config, ServletRequest request, ServletResponse response)
    {
        super(config, request, response);
        jspWriter = new MockJspWriter();
        outStack = new Stack();
    }
  
    public JspWriter getOut()
    {
        return jspWriter;
    }
    
    public void handlePageException(Exception exc) 
    {
        
    }

    public void handlePageException(Throwable thr) 
    {
    
    }

    public void include(String path) 
    {
    
    }

    public void initialize(Servlet servlet, ServletRequest request,
                           ServletResponse response, String errorPageURL,
                           boolean needsSession, int bufferSize,
                           boolean autoFlush) 
    {
        
    }

    public JspWriter popBody() 
    {
        jspWriter = (JspWriter)outStack.pop();
        return jspWriter;
    }
    
    public BodyContent pushBody() 
    {
        outStack.push(jspWriter);
        MockBodyContent bodyContent = new MockBodyContent(jspWriter);
        jspWriter = bodyContent;
        return bodyContent;
    }

    public void release() 
    {
        jspWriter = new MockJspWriter();
        outStack = new Stack();
    }
}