package com.mockrunner.gen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mockrunner.ejb.EJBTestModule;
import com.mockrunner.gen.proc.AdapterProcessor;
import com.mockrunner.gen.proc.BasicAdapterProcessor;
import com.mockrunner.gen.proc.StandardAdapterProcessor;
import com.mockrunner.jdbc.JDBCTestModule;
import com.mockrunner.jms.JMSTestModule;
import com.mockrunner.servlet.ServletTestModule;
import com.mockrunner.struts.ActionTestModule;
import com.mockrunner.tag.TagTestModule;

public class AdapterGenerator
{
    private final static String SRCDIR = "src";
    
    private List units;
    
    public static void main(String[] args) throws Exception
    {
        AdapterGenerator generator = new AdapterGenerator();
        generator.generate();
    }
    
    public AdapterGenerator()
    {
        units = new ArrayList();
        List actionExcluded = new ArrayList();
        actionExcluded.add("getOutput");
        units.add(new ProcessingUnit(ActionTestModule.class, new StandardAdapterProcessor(), actionExcluded));
        units.add(new ProcessingUnit(ActionTestModule.class, new BasicAdapterProcessor(), actionExcluded));
        List servletExcluded = new ArrayList();
        servletExcluded.add("getOutput");
        units.add(new ProcessingUnit(ServletTestModule.class, new StandardAdapterProcessor(), servletExcluded));
        units.add(new ProcessingUnit(ServletTestModule.class, new BasicAdapterProcessor(), servletExcluded));
        List tagExcluded = new ArrayList();
        tagExcluded.add("getOutput");
        units.add(new ProcessingUnit(TagTestModule.class, new StandardAdapterProcessor(), tagExcluded));
        units.add(new ProcessingUnit(TagTestModule.class, new BasicAdapterProcessor(), tagExcluded));
        units.add(new ProcessingUnit(EJBTestModule.class, new StandardAdapterProcessor(), null));
        units.add(new ProcessingUnit(EJBTestModule.class, new BasicAdapterProcessor(), null));
        units.add(new ProcessingUnit(JDBCTestModule.class, new StandardAdapterProcessor(), null));
        units.add(new ProcessingUnit(JDBCTestModule.class, new BasicAdapterProcessor(), null));
        units.add(new ProcessingUnit(JMSTestModule.class, new StandardAdapterProcessor(), null));
        units.add(new ProcessingUnit(JMSTestModule.class, new BasicAdapterProcessor(), null));
    }
    
    private void generate() throws Exception
    {
        Iterator iterator = units.iterator();
        while(iterator.hasNext())
        {
            ProcessingUnit nextUnit = (ProcessingUnit)iterator.next();
            AdapterProcessor processor = nextUnit.getProcessor();
            processor.process(nextUnit.getModule(), nextUnit.getExcludedMethods());
            writeOutputFile(processor);
        }
        System.out.println("Adapters successfully created");
    }
    
    private void writeOutputFile(AdapterProcessor processor) throws FileNotFoundException, IOException
    {
        System.out.println("Writing output file " + processor.getName());
        File currentFile = new File(SRCDIR + "/" + processor.getName());
        FileOutputStream currentStream = new FileOutputStream(currentFile);
        Writer currentWriter = new OutputStreamWriter(currentStream, Charset.forName("ISO-8859-1"));
        currentWriter.write(processor.getOutput());
        currentWriter.flush();
        currentWriter.close();
    }

    private class ProcessingUnit
    {
        private Class module;
        private AdapterProcessor processor;
        private List excludedMethods;
        
        public ProcessingUnit(Class module, AdapterProcessor processor)
        {
            this(module, processor, new ArrayList());
        }
        
        public ProcessingUnit(Class module, AdapterProcessor processor, List excludedMethods)
        {
            this.module = module;
            this.processor = processor;
            this.excludedMethods = excludedMethods;
        }
        
        public Class getModule()
        {
            return module;
        }
        
        public List getExcludedMethods()
        {
            return excludedMethods;
        }
        
        public AdapterProcessor getProcessor()
        {
            return processor;
        }
    }
}