/*
 * $Id: CsvWriter.java,v 1.10 2008/02/22 18:25:40 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.util;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.tapestry.IMarkupWriter;

/**
 * @author Jesus Ramos
 * Provides means to export CSV files. Created for the sole purpose of having different content type.
 */
public class CsvWriter implements IMarkupWriter {
	
	/**
	 * Constructor de la clase.
	 * 
	 * @param writer El writer del archivo. 
	 */
    public CsvWriter(PrintWriter writer) {
        _writer = writer;
    }

    /**
     * Constructor que recibe como parametro el stream a escribir del archivo csv.
     * 
     * @param stream El stream a escribir.
     */
    public CsvWriter(OutputStream stream) {
        OutputStreamWriter owriter = new OutputStreamWriter(stream);
        Writer bwriter = new BufferedWriter(owriter);
        _writer = new PrintWriter(bwriter);
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#getNestedWriter()
     */
    public IMarkupWriter getNestedWriter() {
        return new CsvWriter(_writer);
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#checkError()
     */
    public boolean checkError() {
        return _writer.checkError();
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#close()
     */
    public void close() {
        _writer.close();
        _writer = null;
        _buffer = null;
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#flush()
     */
    public void flush() {
        _writer.flush();
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#print(char[], int, int)
     */
    public void print(char[] data, int offset, int length) {
        if (data == null)
            return;
        _writer.write(data, offset, length);
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#print(char)
     */
    public void print(char value) {
        _writer.print(value);
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#print(int)
     */
    public void print(int value) {
        _writer.print(value);
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#print(java.lang.String)
     */
    public void print(String value) {
        if (value == null)
            return;
        int length = value.length();
        if (_buffer == null || _buffer.length < length)
            _buffer = new char[length];
        value.getChars(0, length, _buffer, 0);
        print(_buffer, 0, length);
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#println()
     */
    public void println() {
        _writer.println();
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#printRaw(char[], int, int)
     */
    public void printRaw(char[] buffer, int offset, int length) {
        if (buffer == null)
            return;
        _writer.write(buffer, offset, length);
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#printRaw(java.lang.String)
     */
    public void printRaw(String value) {
        if (value == null)
            return;
        _writer.print(value);
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#getContentType()
     */
    public String getContentType() {
        return "application/vnd.ms-excel";
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#attribute(java.lang.String, int)
     */
    public void attribute(String name, int value) {
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#attribute(java.lang.String, boolean)
     */
    public void attribute(String name, boolean value) {
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#attribute(java.lang.String, java.lang.String)
     */
    public void attribute(String name, String value) {
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#attributeRaw(java.lang.String, java.lang.String)
     */
    public void attributeRaw(String name, String value) {
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#begin(java.lang.String)
     */
    public void begin(String name) {
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#beginEmpty(java.lang.String)
     */
    public void beginEmpty(String name) {
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#closeTag()
     */
    public void closeTag() {
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#comment(java.lang.String)
     */
    public void comment(String value) {
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#end()
     */
    public void end() {
    }

    /**
     * @see org.apache.tapestry.IMarkupWriter#end(java.lang.String)
     */
    public void end(String name) {
    }

    /**
     * Arreglo de chars. 
     */
    private char[] _buffer;
    
    /**
     * Writer del archivo.
     */
    private PrintWriter _writer;
}