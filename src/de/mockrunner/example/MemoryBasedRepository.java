package de.mockrunner.example;

import java.util.HashMap;

import javax.servlet.ServletContext;

public class MemoryBasedRepository
{
    private HashMap dataStore;
    
    private MemoryBasedRepository()
    {
        dataStore = new HashMap();
    }
    
    public static MemoryBasedRepository instance(ServletContext context)
    {
        MemoryBasedRepository instance = (MemoryBasedRepository)context.getAttribute(MemoryBasedRepository.class.getName());
        if(null != instance) return instance;
        instance = new MemoryBasedRepository();
        context.setAttribute(MemoryBasedRepository.class.getName(), instance);
        return instance;
    }
    
    public void set(String id, Object data)
    {
        dataStore.put(id, data);
    }
    
    public Object get(String id)
    {
        return dataStore.get(id);
    }
}