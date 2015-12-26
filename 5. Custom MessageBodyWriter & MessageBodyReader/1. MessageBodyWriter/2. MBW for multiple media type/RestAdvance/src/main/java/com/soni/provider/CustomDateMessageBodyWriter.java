package com.soni.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.soni.model.MyDate;

@Provider	// This annotation is essential to register this class as MessageBodyWriter.
@Produces(value = {MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})		// It indicates that this message body writer should be used only when jersey needs 
																					// to send the response either in text/plain or in application/json format after actual conversion. We could have different
																					// message body writers for different media types.
public class CustomDateMessageBodyWriter<T> implements MessageBodyWriter<T> {
	@Override
	public boolean isWriteable(Class<?> type, 
															Type genericType, 
															Annotation[] annotations, 
															MediaType mediaType) {
		if(type==MyDate.class)
			return true;
		else 
			return false;
	}

	@Override
	public void writeTo(T t, 
										Class<?> type, 
										Type genericType,
										Annotation[] annotations, 
										MediaType mediaType,
										MultivaluedMap<String, Object> httpHeaders, 
										OutputStream out) throws IOException, WebApplicationException {
		if(type==MyDate.class) {
			if( MediaType.TEXT_PLAIN.equals(mediaType.toString()) ) {
				out.write(t.toString().getBytes());
			}
			else if( MediaType.APPLICATION_JSON.equals(mediaType.toString()) ) {	// We have to remove moxy/jackson/gson dependencies from pom.xml
				MyDate myDate = (MyDate) t;																					// In order to force jersey to use our own message body writer for application/json media type.
				String jsonString = "{"
						+ "\"DAY\": " + myDate.getDay() + ", "
						+ "\"MONTH\": " + myDate.getMonth() + ", "
						+ "\"YEAR\": " + myDate.getYear()
						+ "}";
				out.write(jsonString.getBytes());
			}
		}
	}
	
	// As of JAX-RS 2.0, the method has been deprecated and the value returned by the method is ignored
   // by a JAX-RS runtime. All {@code MessageBodyWriter} implementations are advised to return {@code -1}
   // from the method.
	@Override
	public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

}
