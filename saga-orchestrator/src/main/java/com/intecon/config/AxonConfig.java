package com.intecon.config;

import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

@Configuration
public class AxonConfig {
 
    @Bean
    public XStream xStream() {
    	System.out.println("Config applied!");
    	XStream xstream = new XStream();
    	// clear out existing permissions and set own ones
    	xstream.addPermission(NoTypePermission.NONE);
    	// allow some basics
    	xstream.addPermission(NullPermission.NULL);
    	xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
    	xstream.allowTypeHierarchy(Collection.class);
    	// allow any type from the same package
    	xstream.allowTypesByWildcard(new String[] {
        	    "com.**",
        	    "java.**"
    	});
        return xstream;
    }
}