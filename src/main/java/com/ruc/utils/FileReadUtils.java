package com.ruc.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReadUtils
{
	public static InputStream createByteRead(String fileName) throws IOException{
		return getStreamByFileName(fileName);
	}

	public static Reader createCharRead(String fileName) throws IOException{
		return new InputStreamReader(getStreamByFileName(fileName),Charset.forName("UTF-8"));
	}
	
	public static InputStream getStreamByFileName(String fileName) throws IOException
	{
		check(fileName);
		if(fileName.startsWith("http")){
			URL url = new URL(fileName);
			return  url.openStream();
		}else if(fileName.startsWith("/")){
			Path path = Paths.get(fileName);
			return Files.newInputStream(path);
		}else
			return FileReadUtils.class.getClassLoader().getResourceAsStream(fileName);
	}
	
	public static File getFile(String fileName) throws MalformedURLException{
		check(fileName);
		if(fileName.startsWith("http")){
			URL url = new URL(fileName);
			fileName = url.getFile();
		}else if(!fileName.startsWith("/")){
			URL url = FileReadUtils.class.getClassLoader().getResource(fileName);
			check(url,"System do not have this file : "+fileName);
			fileName = url.getFile();
		}
		return new File(fileName);
	}

	private static void check(Object arg, String msg)
	{
		if(arg == null){
			throw new IllegalArgumentException(msg);
		}
		
	}

	private static void check(Object arg)
	{
		check(arg,"params should not be null!");
		
		
	}

}
