package com.mockrunner.mock.web;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

//import javax.el.ELContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
//import javax.servlet.jsp.el.ExpressionEvaluator;
//import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.tagext.BodyContent;

import com.mockrunner.base.NestedApplicationException;

/**
 * Mock implementation of <code>PageContext</code>.
 * Please note that EL support using the
 * the <b>Unified Expression Language</b> API is only available,
 * if the {@link JasperJspFactory} is configured as the default
 * <code>JspFactory</code>. By default, {@link #getELContext}
 * returns <code>null</code>.
 */
//Some methods of this class were copied from org.apache.struts.mock.MockPageContext
//and modified
public class MockPageContext extends PageContext
{
    protected ServletConfig config;
    protected ServletRequest request;
    protected ServletResponse response;
    private JspWriter jspWriter;
    private Stack outStack;
    private Exception exception;
    private Object page;
    private HashMap attributes;
    //private ExpressionEvaluator evaluator;
    //private VariableResolver resolver;
    //private ELContext elContext;
    
    public MockPageContext()
    {
        this(null, null, null);
    }

    public MockPageContext(ServletConfig config, ServletRequest request, ServletResponse response)
    {
        this.config = config;
        this.request = request;
        jspWriter = new MockJspWriter();
        initJspWriterWithResponse(response);
        outStack = new Stack();
        attributes = new HashMap();
        //evaluator = new MockExpressionEvaluator();
        //resolver = new MockVariableResolver();
    }

    private void initJspWriterWithResponse(ServletResponse response)
    {
        this.response = response;
        if((null != response) && (response instanceof HttpServletResponse))
        {
            try
            {
                jspWriter = new MockJspWriter((HttpServletResponse)response);
            } 
            catch(IOException exc)
            {
                throw new NestedApplicationException(exc);
            }
        }
    }
    
    /**
     * This method allows to set custom implementations
     * of <code>JspWriter</code>. Per default, {@link MockJspWriter}
     * is used.
     * @param jspWriter the <code>JspWriter</code>
     */
    public void setJspWriter(JspWriter jspWriter)
    {
        this.jspWriter = jspWriter;
    }
    
    public void setPage(Object page) 
    {
        this.page = page;
    }
    
    /**
     * Sets the <code>ServletConfig</code>.
     * @param config the <code>ServletConfig</code>
     */
    public void setServletConfig(ServletConfig config)
    {
        this.config = config;
    }
    
    /**
     * Sets the <code>ServletRequest</code>.
     * @param request the <code>ServletRequest</code>
     */
    public void setServletRequest(ServletRequest request)
    {
        this.request = request;
    }
    
    /**
     * Sets the <code>ServletResponse</code>.
     * @param response the <code>ServletResponse</code>
     */
    public void setServletResponse(ServletResponse response)
    {
        this.response = response;
        initJspWriterWithResponse(response);
    }
    
    public void setException(Exception exception) 
    {
        this.exception = exception;
    }
    
    public Object findAttribute(String name) 
    {
        Object value = getAttribute(name, PageContext.PAGE_SCOPE);
        if(value == null) 
        {
            value = getAttribute(name, PageContext.REQUEST_SCOPE);
        }
        if(value == null) 
        {
            value = getAttribute(name, PageContext.SESSION_SCOPE);
        }
        if(value == null) 
        {
            value = getAttribute(name, PageContext.APPLICATION_SCOPE);
        }
        return value;
    }
    
    public Object getAttribute(String name) 
    {
        return getAttribute(name, PageContext.PAGE_SCOPE);
    }

    public Object getAttribute(String name, int scope) 
    {
        if(scope == PageContext.PAGE_SCOPE) 
        {
            return attributes.get(name);
        } 
        else if(scope == PageContext.REQUEST_SCOPE) 
        {
            if(null == request) return null;
            return request.getAttribute(name);
        } 
        else if(scope == PageContext.SESSION_SCOPE) 
        {
            if(null == getSession()) return null;
            return getSession().getAttribute(name);
        } 
        else if(scope == PageContext.APPLICATION_SCOPE) 
        {
            if(null == getServletContext()) return null;
            return getServletContext().getAttribute(name);
        } 
        else 
        {
            throw new IllegalArgumentException("Invalid scope " + scope);
        }
    }
    
    public void removeAttribute(String name) 
    {
        int scope = getAttributesScope(name);
        if (scope != 0) 
        {
            removeAttribute(name, scope);
        }
    }

    public void removeAttribute(String name, int scope) 
    {
        if(scope == PageContext.PAGE_SCOPE) 
        {
            attributes.remove(name);
        } 
        else if(scope == PageContext.REQUEST_SCOPE) 
        {
            if(request != null) 
            {
                request.removeAttribute(name);
            }
        } 
        else if(scope == PageContext.SESSION_SCOPE) 
        {
            if(getSession() != null) 
            {
                getSession().removeAttribute(name);
            }
        } 
        else if(scope == PageContext.APPLICATION_SCOPE) 
        {
            if(getServletContext() != null) 
            {
                getServletContext().removeAttribute(name);
            }
        } 
        else 
        {
            throw new IllegalArgumentException("Invalid scope " + scope);
        }
    }
    
    public void setAttribute(String name, Object value) 
    {
        setAttribute(name, value, PageContext.PAGE_SCOPE);
    }


    public void setAttribute(String name, Object value, int scope) 
    {
        if(scope == PageContext.PAGE_SCOPE) 
        {
            attributes.put(name, value);
        } 
        else if(scope == PageContext.REQUEST_SCOPE) 
        {
            if(request != null) 
            {
                request.setAttribute(name, value);
            }
        } 
        else if(scope == PageContext.SESSION_SCOPE) 
        {
            if(getSession() != null) 
            {
                getSession().setAttribute(name, value);
            }
        } 
        else if(scope == PageContext.APPLICATION_SCOPE) 
        {
            if(getServletContext() != null) 
            {
                getServletContext().setAttribute(name, value);
            }
        } 
        else 
        {
            throw new IllegalArgumentException("Invalid scope " + scope);
        }
    }
    
    public int getAttributesScope(String name) 
    {
        if(getAttribute(name, PageContext.PAGE_SCOPE) != null) 
        {
            return PageContext.PAGE_SCOPE;
        } 
        else if(getAttribute(name, PageContext.REQUEST_SCOPE) != null) 
        {
            return PageContext.REQUEST_SCOPE;
        } 
        else if(getAttribute(name, PageContext.SESSION_SCOPE) != null)
        {
            return PageContext.SESSION_SCOPE;
        } 
        else if(getAttribute(name, PageContext.APPLICATION_SCOPE) != null) 
        {
            return PageContext.APPLICATION_SCOPE;
        } 
        return 0;
    }
    
    public Enumeration getAttributeNamesInScope(int scope) 
    {
        if(scope == PageContext.PAGE_SCOPE) 
        {
            return new WrappedEnumeration(attributes.keySet().iterator());
        } 
        else if(scope == PageContext.REQUEST_SCOPE) 
        {
            if(request == null) return new NullEnumeration();
            return request.getAttributeNames();
        } 
        else if(scope == PageContext.SESSION_SCOPE) 
        {
            if(getSession() == null) return new NullEnumeration();
            return getSession().getAttributeNames();
        } 
        else if(scope == PageContext.APPLICATION_SCOPE) 
        {
            if(getServletContext() == null) return new NullEnumeration();
            return getServletContext().getAttributeNames();
        } 
        else 
        {
            throw new IllegalArgumentException("Invalid scope " + scope);
        }
    }
    
    public JspWriter getOut()
    {
        return jspWriter;
    }
    
    public Exception getException() 
    {
        return exception;
    }
    
    public Object getPage() 
    {
        return page;
    }

    public ServletRequest getRequest() 
    {
        return request;
    }

    public ServletResponse getResponse() 
    {
        return response;
    }

    public ServletConfig getServletConfig() 
    {
        return config;
    }

    public ServletContext getServletContext() 
    {
        if(null == config) return null;
        return config.getServletContext();
    }


    public HttpSession getSession() 
    {
        if(null == request) return null;
        return ((HttpServletRequest)request).getSession();
    }

    public void handlePageException(Exception exc) 
    {
        
    }

    public void handlePageException(Throwable thr) 
    {
    
    }
    
    public void forward(String path) throws ServletException, IOException
    {
        if(null != request)
        {
            RequestDispatcher dispatcher = request.getRequestDispatcher(path);
            if(null != dispatcher)
            {
                dispatcher.forward(request, response); 
            }
        }
    }

    public void include(String path) throws ServletException, IOException
    {
        if(null != request)
        {
            RequestDispatcher dispatcher = request.getRequestDispatcher(path);
            if(null != dispatcher)
            {
                dispatcher.include(request, response); 
            }
        }
    }
    
    public void include(String path, boolean flush) throws ServletException, IOException
    {
        if(flush)
        {
            jspWriter.flush();
        }
        include(path);
    }

    public void initialize(Servlet servlet, ServletRequest request,
                           ServletResponse response, String errorPageURL,
                           boolean needsSession, int bufferSize,
                           boolean autoFlush) 
    {
        this.config = servlet.getServletConfig();
        this.request = request;
        this.response = response;
        jspWriter = new MockJspWriter();
        outStack = new Stack();
        attributes = new HashMap();
    }

    public JspWriter popBody() 
    {
        jspWriter = (JspWriter)outStack.pop();
        return jspWriter;
    }
    
    public BodyContent pushBody() 
    {
        outStack.push(jspWriter);
        jspWriter = new MockBodyContent(jspWriter);
        return (BodyContent)jspWriter;
    }
    
    public JspWriter pushBody(Writer writer)
    {
        outStack.push(jspWriter);
        jspWriter = new MockJspWriter(writer);
        return jspWriter;
    }
    
    public void release() 
    {
        jspWriter = new MockJspWriter();
        outStack = new Stack();
    }
    
    /**
     * Sets the expression evaluator. The default expression evaluator
     * is {@link MockExpressionEvaluator}.
     * @param evaluator the <code>ExpressionEvaluator</code>
     */
    /*public void setExpressionEvaluator(ExpressionEvaluator evaluator)
    {
        this.evaluator = evaluator;
    }*/

    /**
     * Sets the variable resolver. The default variable resolver
     * is {@link MockVariableResolver}.
     * @param resolver the <code>VariableResolver</code>
     */
    /*public void setVariableResolver(VariableResolver resolver)
    {
        this.resolver = resolver;
    }*/
    
    /**
     * Sets the <code>ELContext</code>. There is no default
     * <code>ELContext</code>. Configure the {@link JasperJspFactory}
     * to set one. 
     * @param elContext the <code>ELContext</code>
     */
    /*public void setELContext(ELContext elContext)
    {
        this.elContext = elContext;
    }*/
    
    /*public ExpressionEvaluator getExpressionEvaluator()
    {
        return evaluator;
    }*/

    /*public VariableResolver getVariableResolver()
    {
        return resolver;
    }*/
    
    /*public ELContext getELContext()
    {
        return elContext;
    }*/

    private class NullEnumeration implements Enumeration 
    {
        public boolean hasMoreElements() 
        {
            return false;
        }

        public Object nextElement() 
        {
            throw new NoSuchElementException();
        }
    }
    
    private class WrappedEnumeration implements Enumeration 
    {
        private Iterator iterator;
        
        public WrappedEnumeration(Iterator iterator) 
        {
            this.iterator = iterator;
        }

        public boolean hasMoreElements() 
        {
            return iterator.hasNext();
        }

        public Object nextElement() 
        {
            return iterator.next();
        }
    }
}
