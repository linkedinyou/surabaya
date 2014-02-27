/*******************************************************************************
 * Copyright (c) 2014 Akira Sonoda.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Akira Sonoda - initial API and implementation
 ******************************************************************************/
/**
 *  Surabaya - a replacement http server for the OpenSimulator
 *  Copyright (C) 2012 Akira Sonoda
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.openjgrid.datatypes.llsd;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class LLSD {
	private static final Logger log = LoggerFactory.getLogger(LLSD.class);

	public static Object llsdDeserialize(String llsdString) throws XMLStreamException, LLSDParseException {
		try {
			return llsdDeserialize(new InputStreamReader(IOUtils.toInputStream(llsdString)));
		} catch (LLSDParseException ex) {
			throw new LLSDParseException(ex.getMessage() + "--" + llsdString + "--"); 
		}
	}

	public static Object llsdDeserialize(InputStreamReader llsdReader)
			throws XMLStreamException, LLSDParseException {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLStreamReader llsdStream = xmlInputFactory
				.createXMLStreamReader(llsdReader);
		llsdStream.next();
		if (llsdStream.getEventType() != XMLStreamConstants.START_ELEMENT
				|| !llsdStream.getLocalName().equalsIgnoreCase("llsd")) {
			throw new LLSDParseException("Expected <llsd>");
		}

		llsdStream.next();
		Object ret = llsdParseDatatype(llsdStream);

		if (llsdStream.getEventType() != XMLStreamConstants.END_ELEMENT
				|| !llsdStream.getLocalName().equalsIgnoreCase("llsd")) {
			throw new LLSDParseException("Expected </llsd>");
		}

		return ret;
	}

	public static String LLSDSerialize(Object obj) throws XMLStreamException,
			LLSDSerializeException {

		StringWriter llsdWriter = new StringWriter();
		XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
		XMLStreamWriter llsdStream = xmlOutputFactory
				.createXMLStreamWriter(llsdWriter);

		llsdStream.writeStartElement("llsd");
		LLSDWriteOne(llsdStream, obj);
		llsdStream.writeEndElement();
		llsdStream.flush();
		String result = llsdWriter.getBuffer().toString();

		return (result);
	}

	public static void LLSDWriteOne(XMLStreamWriter llsdStream, Object obj)
			throws XMLStreamException, LLSDSerializeException {
		if (obj == null) {
			llsdStream.writeStartElement("undef");
			llsdStream.writeEndElement();
			return;
		}
		
		String llsdMetadata = "";
		if(obj.getClass().isAnnotationPresent(LLSDMapping.class)) {
			llsdMetadata = obj.getClass().getAnnotation(LLSDMapping.class).mapTo();
		} else {
			llsdMetadata = obj.getClass().getName();
		}
		
		if (llsdMetadata.equalsIgnoreCase("string") || llsdMetadata.equalsIgnoreCase("java.lang.String")) {
			llsdStream.writeStartElement("string");
			llsdStream.writeCharacters((String) obj);
			llsdStream.writeEndElement();
		} else if (llsdMetadata.equalsIgnoreCase("integer") || llsdMetadata.equalsIgnoreCase("java.lang.Integer") || llsdMetadata.equalsIgnoreCase("java.lang.Long") || llsdMetadata.equalsIgnoreCase("java.lang.Byte")) {
			llsdStream.writeStartElement("integer");
			llsdStream.writeCharacters(obj.toString());
			llsdStream.writeEndElement();
		} else if (llsdMetadata.equalsIgnoreCase("real")) {
			Double doubleData = (Double) obj;
			llsdStream.writeStartElement("real");
			llsdStream.writeCharacters(doubleData.toString());
			llsdStream.writeEndElement();
		} else if (llsdMetadata.equalsIgnoreCase("boolean") || llsdMetadata.equalsIgnoreCase("java.lang.Boolean") ) {
			boolean boolData = (Boolean) obj;
			llsdStream.writeStartElement("boolean");
			llsdStream.writeCharacters(boolData ? "1" : "0");
			llsdStream.writeEndElement();
			// else if (obj is ulong)
			// {
			// throw new
			// Exception("ulong in LLSD is currently not implemented, fix me!");
			// }
		} else if (llsdMetadata.equalsIgnoreCase("uuid") || llsdMetadata.equalsIgnoreCase("java.util.UUID")) {
			UUID u = (UUID) obj;
			llsdStream.writeStartElement("uuid");
			llsdStream.writeCharacters(u.toString());
			llsdStream.writeEndElement();
		} else if (llsdMetadata.equalsIgnoreCase("map") || llsdMetadata.equalsIgnoreCase("java.util.Map")) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) obj;
			llsdStream.writeStartElement("map");
			Set<String> keys = map.keySet();
			Iterator<String> keysIter = keys.iterator();
			while (keysIter.hasNext()) {
				llsdStream.writeStartElement("key");
				String key = (String) keysIter.next();
				llsdStream.writeCharacters(key);
				llsdStream.writeEndElement();
				LLSDWriteOne(llsdStream, map.get(key));
			}
			llsdStream.writeEndElement();
		} else if (llsdMetadata.equalsIgnoreCase("array") || llsdMetadata.equalsIgnoreCase("java.util.ArrayList")) {
			@SuppressWarnings("unchecked")
			ArrayList<Object> array = (ArrayList<Object>) obj;
			llsdStream.writeStartElement("array");
			Iterator<Object> arrayIter = array.iterator();
			while (arrayIter.hasNext()) {
				LLSDWriteOne(llsdStream, arrayIter.next());
			}
			llsdStream.writeEndElement();
		} else if (llsdMetadata.equalsIgnoreCase("struct")) {
			llsdStream.writeStartElement("map");
			List<Field> fields = getInheritedPrivateFields(obj.getClass());
			Iterator<Field> fieldIter = fields.iterator();
			while ( fieldIter.hasNext() ) {
				Field field = fieldIter.next();
				try {
					if (!field.isAccessible()) {
						field.setAccessible(true);
						processField(llsdStream,obj,field);
						field.setAccessible(false);
					} else {
						processField(llsdStream,obj,field);
					}
				} catch (IllegalArgumentException ex) {
					log.debug("IllegalArgumentException in LLSDWriteOne(struct) field: {}", field.getName() );
				} catch (IllegalAccessException ex) {
					log.debug("IllegalAccessException in LLSDWriteOne(struct) field: {}", field.getName() );
				}
			}
			llsdStream.writeEndElement();
			// }
			// else if (obj is byte[])
			// {
			// byte[] b = (byte[])((obj instanceof byte[]) ? obj : null);
			// llsdStream.WriteStartElement(String.Empty, "binary",
			// String.Empty);
			//
			// llsdStream.WriteStartAttribute(String.Empty, "encoding",
			// String.Empty);
			// llsdStream.WriteString("base64");
			// llsdStream.WriteEndAttribute();
			//
			// //// Calculate the length of the base64 output
			// //long length = (long)(4.0d * b.Length / 3.0d);
			// //if (length % 4 != 0) length += 4 - (length % 4);
			//
			// //// Create the char[] for base64 output and fill it
			// //char[] tmp = new char[length];
			// //int i = Convert.ToBase64CharArray(b, 0, b.Length, tmp, 0);
			//
			// //llsdStream.WriteString(new String(tmp));
			//
			// llsdStream.WriteString(Convert.ToBase64String(b));
			// llsdStream.WriteEndElement();
			// }
		} else {
			throw new LLSDSerializeException("Unknown type "
					+ obj.getClass().getName());
		}
	}
	
	private static void processField(XMLStreamWriter llsdStream, Object obj, Field field) throws XMLStreamException, IllegalArgumentException, LLSDSerializeException, IllegalAccessException {
		String mappingName = "";
		if( field.isAnnotationPresent(LLSDMapping.class)) {
			// We only process annotated Fields
			mappingName = field.getAnnotation(LLSDMapping.class).mappedName();
			llsdStream.writeStartElement("key");
			llsdStream.writeCharacters(mappingName);
			llsdStream.writeEndElement();
			LLSDWriteOne(llsdStream, field.get(obj));		
		}
	}

	private static Object llsdParseDatatype(XMLStreamReader llsdStream)
			throws LLSDParseException, XMLStreamException {
		if (llsdStream.getEventType() != XMLStreamConstants.START_ELEMENT) {
			throw new LLSDParseException("Expected an element");
		}

		String datatype = llsdStream.getLocalName();
		Object ret = null;

		if (datatype.equalsIgnoreCase("undef")) {
			if (llsdStream.getElementText().isEmpty()) {
				llsdStream.next();
				return null;
			}

			llsdStream.next();
			ret = null;
		} else if (datatype.equalsIgnoreCase("boolean")) {
			// if (reader.IsEmptyElement)
			// {
			// reader.Read();
			// return false;
			// }

			String s = llsdStream.getElementText().trim();

			if (s.isEmpty() || s.equalsIgnoreCase("false") || s.equals("0")) {
				ret = false;
			} else if (s.equalsIgnoreCase("true") || s.equals("1")) {
				ret = true;
			} else {
				throw new LLSDParseException("Bad boolean value " + s);
			}

		} else if (datatype.equalsIgnoreCase("integer")) {
			// if (reader.IsEmptyElement)
			// {
			// reader.Read();
			// return 0;
			// }

			ret = Integer.decode(llsdStream.getElementText()).intValue();

			// }
			// case "real":
			// {
			// if (reader.IsEmptyElement)
			// {
			// reader.Read();
			// return 0.0f;
			// }
			//
			// reader.Read();
			// ret = Convert.ToDouble(reader.ReadString().Trim());
			// break;
			// }
		} else if (datatype.equalsIgnoreCase("uuid")) {
			// if (reader.IsEmptyElement)
			// {
			// reader.Read();
			// return UUID.Zero;
			// }
			ret = UUID.fromString(llsdStream.getElementText().trim());
		} else if ( datatype.equalsIgnoreCase("string")) {
			ret = llsdStream.getElementText().trim();
			// {
			// if (reader.IsEmptyElement)
			// {
			// reader.Read();
			// return String.Empty;
			// }
			//
			// reader.Read();
			// ret = reader.ReadString();
			// break;
			// }
			// case "binary":
			// {
			// if (reader.IsEmptyElement)
			// {
			// reader.Read();
			// return new byte[0];
			// }
			//
			// if (reader.GetAttribute("encoding") != null &&
			// reader.GetAttribute("encoding") != "base64")
			// {
			// throw new LLSDParseException("Unknown encoding: " +
			// reader.GetAttribute("encoding"));
			// }
			//
			// reader.Read();
			// FromBase64Transform b64 = new
			// FromBase64Transform(FromBase64TransformMode.IgnoreWhiteSpaces);
			// byte[] inp = Util.UTF8.GetBytes(reader.ReadString());
			// ret = b64.TransformFinalBlock(inp, 0, inp.Length);
			// break;
			// }
			// case "date":
			// {
			// reader.Read();
			// throw new Exception("LLSD TODO: date");
			// }
		} else if (datatype.equalsIgnoreCase("map")) {
			return llsdParseMap(llsdStream);
		} else if (datatype.equalsIgnoreCase("array")) {
			return llsdParseArray(llsdStream);
		} else {
			throw new LLSDParseException("Unknown element <" + datatype + ">");
		}

		if (llsdStream.getEventType() != XMLStreamConstants.END_ELEMENT
				|| llsdStream.getLocalName() != datatype) {
			throw new LLSDParseException("Expected </" + datatype + ">");
		}

		llsdStream.next();
		return ret;
	}

	public static HashMap<String, Object> llsdParseMap(
			XMLStreamReader llsdStream) throws LLSDParseException,
			XMLStreamException {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		if (llsdStream.getEventType() != XMLStreamConstants.START_ELEMENT
				|| !llsdStream.getLocalName().equalsIgnoreCase("map")) {
			throw new LLSDParseException("Expected <map>");
		}

		// if (llsdStream.)
		// {
		// llsdStream.next();
		// return ret;
		// }

		llsdStream.next();

		while (true) {
			if (llsdStream.isEndElement()
					&& llsdStream.getLocalName().equalsIgnoreCase("map")) {
				llsdStream.next();
				break;
			}

			if (!llsdStream.isStartElement()
					|| !llsdStream.getLocalName().equalsIgnoreCase("key")) {
				throw new LLSDParseException("Expected <key>");
			}

			String key = llsdStream.getElementText();

			if (llsdStream.getEventType() != XMLStreamConstants.END_ELEMENT
					|| !llsdStream.getLocalName().equalsIgnoreCase("key")) {
				throw new LLSDParseException("Expected </key>");
			}

			llsdStream.next();
			Object val = llsdParseDatatype(llsdStream);
			ret.put(key, val);
		}

		return ret;
	}

	public static ArrayList<Object> llsdParseArray(XMLStreamReader llsdStream)
			throws LLSDParseException, XMLStreamException {
		ArrayList<Object> ret = new ArrayList<Object>();

		if (!llsdStream.isStartElement()
				|| !llsdStream.getLocalName().equalsIgnoreCase("array")) {
			throw new LLSDParseException("Expected <array>");
		}

		// if (reader.IsEmptyElement)
		// {
		// reader.Read();
		// return ret;
		// }

		llsdStream.next();

		while (true) {
			if (llsdStream.isEndElement()
					&& llsdStream.getLocalName().equalsIgnoreCase("array")) {
				llsdStream.next();
				break;
			}

			ret.add(llsdParseDatatype(llsdStream));
		}

		return ret;
	}

	// private static string GetSpaces(int count)
	// {
	// StringBuilder b = new StringBuilder();
	// for (int i = 0; i < count; i++)
	// b.Append(" ");
	// return b.ToString();
	// }

	// public static String LLSDDump(object obj, int indent)
	// {
	// if (obj == null)
	// {
	// return GetSpaces(indent) + "- undef\n";
	// }
	// else if (obj is string)
	// {
	// return GetSpaces(indent) + "- string \"" + (string) obj + "\"\n";
	// }
	// else if (obj is int)
	// {
	// return GetSpaces(indent) + "- integer " + obj.ToString() + "\n";
	// }
	// else if (obj is double)
	// {
	// return GetSpaces(indent) + "- float " + obj.ToString() + "\n";
	// }
	// else if (obj is UUID)
	// {
	// return GetSpaces(indent) + "- uuid " + ((UUID) obj).ToString() +
	// Environment.NewLine;
	// }
	// else if (obj is Hashtable)
	// {
	// StringBuilder ret = new StringBuilder();
	// ret.Append(GetSpaces(indent) + "- map" + Environment.NewLine);
	// Hashtable map = (Hashtable) obj;
	//
	// foreach (string key in map.Keys)
	// {
	// ret.Append(GetSpaces(indent + 2) + "- key \"" + key + "\"" +
	// Environment.NewLine);
	// ret.Append(LLSDDump(map[key], indent + 3));
	// }
	//
	// return ret.ToString();
	// }
	// else if (obj is ArrayList)
	// {
	// StringBuilder ret = new StringBuilder();
	// ret.Append(GetSpaces(indent) + "- array\n");
	// ArrayList list = (ArrayList) obj;
	//
	// foreach (object item in list)
	// {
	// ret.Append(LLSDDump(item, indent + 2));
	// }
	//
	// return ret.ToString();
	// }
	// else if (obj is byte[])
	// {
	// return GetSpaces(indent) + "- binary\n" + Utils.BytesToHexString((byte[])
	// obj, GetSpaces(indent)) + Environment.NewLine;
	// }
	// else
	// {
	// return GetSpaces(indent) + "- unknown type " + obj.GetType().Name +
	// Environment.NewLine;
	// }
	// }

	// public static object ParseTerseLLSD(string llsd)
	// {
	// int notused;
	// return ParseTerseLLSD(llsd, out notused);
	// }

	// public static Object ParseTerseLLSD(String llsd, out int endPos)
	// {
	// if (llsd.Length == 0)
	// {
	// endPos = 0;
	// return null;
	// }
	//
	// // Identify what type of object this is
	// switch (llsd[0])
	// {
	// case throw new LLSDParseException("Undefined value type encountered");
	// case endPos = 1;
	// return true;
	// case endPos = 1;
	// return false;
	// case //i':
	// {
	// if (llsd.Length < 2)
	// throw new LLSDParseException("Integer value type with no value");
	// int value;
	// endPos = FindEnd(llsd, 1);
	//
	// if (Int32.TryParse(llsd.Substring(1, endPos - 1), out value))
	// return value;
	// else
	// throw new LLSDParseException("Failed to parse integer value type");
	// }
	// case //r':
	// {
	// if (llsd.Length < 2)
	// throw new LLSDParseException("Real value type with no value");
	// double value;
	// endPos = FindEnd(llsd, 1);
	//
	// if (Double.TryParse(llsd.Substring(1, endPos - 1), NumberStyles.Float,
	// Utils.EnUsCulture.NumberFormat, out value))
	// return value;
	// else
	// throw new LLSDParseException("Failed to parse double value type");
	// }
	// case //u':
	// {
	// if (llsd.Length < 17)
	// throw new LLSDParseException("UUID value type with no value");
	// UUID value;
	// endPos = FindEnd(llsd, 1);
	//
	// if (UUID.TryParse(llsd.Substring(1, endPos - 1), out value))
	// return value;
	// else
	// throw new LLSDParseException("Failed to parse UUID value type");
	// }
	// //byte[] value = new byte[llsd.Length - 1];
	// // This isn't the actual binary LLSD format, just the terse format sent
	// // at login so I don't even know if there is a binary type
	// case throw new LLSDParseException("Binary value type is unimplemented");
	// case case if (llsd.Length < 2) throw new
	// LLSDParseException("String value type with no value");
	// endPos = FindEnd(llsd, 1);
	// return llsd.Substring(1, endPos - 1);
	// // Never seen one before, don't know what the format is
	// case throw new LLSDParseException("Date value type is unimplemented");
	// case //[':
	// {
	// // Advance past comma if need be
	// // Allow a single whitespace character
	// // Advance past comma if need be
	// // Allow a single whitespace character
	// if (llsd.IndexOf(int pos = 0; ArrayList array = new ArrayList();
	// while (llsd[pos] != {
	// ++pos;
	// if (llsd[pos] == if (pos < llsd.Length && llsd[pos] == int end;
	// array.Add(ParseTerseLLSD(llsd.Substring(pos), out end)); pos += end;
	// } endPos = pos + 1;
	// return array;
	// }
	// case {
	// if (llsd.IndexOf(int pos = 0;
	// Hashtable hashtable = new Hashtable();
	// while (llsd[pos] != {
	// ++pos;
	// if (llsd[pos] == if (pos < llsd.Length && llsd[pos]
	// == if (llsd[pos] != int endquote = llsd.IndexOf(if (endquote == -1 ||
	// (endquote + 1) >= llsd.Length || llsd[endquote + 1] != throw new
	// LLSDParseException("Invalid map format"); string key =
	// llsd.Substring(pos, endquote - pos); key = key.Replace("'",
	// String.Empty); pos += (endquote - pos) + 2; int end; hashtable.Add(key,
	// ParseTerseLLSD(llsd.Substring(pos), out end)); pos += end; } endPos = pos
	// + 1; return hashtable; } default: throw new
	// Exception("Unknown value type"); } }

	// private static int FindEnd(string llsd, int start) {
	// int end = llsd.IndexOfAny(new char[] {
	// if (end == -1) end = llsd.Length - 1; return end;
	// }

	// private static void SkipWS(XmlTextReader reader) {
	// while (reader.NodeType == XmlNodeType.Comment ||
	// reader.NodeType == XmlNodeType.Whitespace ||
	// reader.NodeType == XmlNodeType.SignificantWhitespace || reader.NodeType
	// == XmlNodeType.XmlDeclaration)
	// { reader.Read(); } } }
	public static List<Field> getInheritedPrivateFields(Class<?> type) {
	    List<Field> result = new ArrayList<Field>();

	    Class<?> i = type;
	    while (i != null && i != Object.class) {
	        for (Field field : i.getDeclaredFields()) {
	            if (!field.isSynthetic()) {
	                result.add(field);
	            }
	        }
	        i = i.getSuperclass();
	    }

	    return result;
	}
}
