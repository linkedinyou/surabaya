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

package org.openjgrid.util;

import org.apache.commons.io.IOUtils;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class LLSD {

	public static Object LLSDDeserialize(String llsdString)
			throws XMLStreamException, LLSDParseException {
		return LLSDDeserialize(new InputStreamReader(
				IOUtils.toInputStream(llsdString)));
	}

	public static Object LLSDDeserialize(InputStreamReader llsdReader)
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
		Object ret = LLSDParseOne(llsdStream);

		if (llsdStream.getEventType() != XMLStreamConstants.END_ELEMENT
				|| !llsdStream.getLocalName().equalsIgnoreCase("llsd")) {
			throw new LLSDParseException("Expected </llsd>");
		}

		return ret;
	}

	// public static byte[] LLSDSerialize(Object obj)
	// {
	// StringWriter sw = new StringWriter();
	// XmlTextWriter writer = new XmlTextWriter(sw);
	// writer.Formatting = Formatting.None;
	//
	// writer.WriteStartElement(String.Empty, "llsd", String.Empty);
	// LLSDWriteOne(writer, obj);
	// writer.WriteEndElement();
	//
	// writer.Close();
	//
	// return Util.UTF8.GetBytes(sw.ToString());
	// }

	// public static void LLSDWriteOne(XmlTextWriter writer, object obj)
	// {
	// if (obj == null)
	// {
	// writer.WriteStartElement(String.Empty, "undef", String.Empty);
	// writer.WriteEndElement();
	// return;
	// }
	//
	// if (obj is string)
	// {
	// writer.WriteStartElement(String.Empty, "string", String.Empty);
	// writer.WriteString((string) obj);
	// writer.WriteEndElement();
	// }
	// else if (obj is int)
	// {
	// writer.WriteStartElement(String.Empty, "integer", String.Empty);
	// writer.WriteString(obj.ToString());
	// writer.WriteEndElement();
	// }
	// else if (obj is double)
	// {
	// writer.WriteStartElement(String.Empty, "real", String.Empty);
	// writer.WriteString(obj.ToString());
	// writer.WriteEndElement();
	// }
	// else if (obj is bool)
	// {
	// bool b = (bool) obj;
	// writer.WriteStartElement(String.Empty, "boolean", String.Empty);
	// writer.WriteString(b ? "1" : "0");
	// writer.WriteEndElement();
	// }
	// else if (obj is ulong)
	// {
	// throw new
	// Exception("ulong in LLSD is currently not implemented, fix me!");
	// }
	// else if (obj is UUID)
	// {
	// UUID u = (UUID) obj;
	// writer.WriteStartElement(String.Empty, "uuid", String.Empty);
	// writer.WriteString(u.ToString());
	// writer.WriteEndElement();
	// }
	// else if (obj is Hashtable)
	// {
	// Hashtable h = (java.util.Hashtable)((obj instanceof java.util.Hashtable)
	// ? obj : null);
	// writer.WriteStartElement(String.Empty, "map", String.Empty);
	// foreach (string key in h.Keys)
	// {
	// writer.WriteStartElement(String.Empty, "key", String.Empty);
	// writer.WriteString(key);
	// writer.WriteEndElement();
	// LLSDWriteOne(writer, h[key]);
	// }
	// writer.WriteEndElement();
	// }
	// else if (obj is ArrayList)
	// {
	// ArrayList a = (java.util.ArrayList)((obj instanceof java.util.ArrayList)
	// ? obj : null);
	// writer.WriteStartElement(String.Empty, "array", String.Empty);
	// foreach (object item in a)
	// {
	// LLSDWriteOne(writer, item);
	// }
	// writer.WriteEndElement();
	// }
	// else if (obj is byte[])
	// {
	// byte[] b = (byte[])((obj instanceof byte[]) ? obj : null);
	// writer.WriteStartElement(String.Empty, "binary", String.Empty);
	//
	// writer.WriteStartAttribute(String.Empty, "encoding", String.Empty);
	// writer.WriteString("base64");
	// writer.WriteEndAttribute();
	//
	// //// Calculate the length of the base64 output
	// //long length = (long)(4.0d * b.Length / 3.0d);
	// //if (length % 4 != 0) length += 4 - (length % 4);
	//
	// //// Create the char[] for base64 output and fill it
	// //char[] tmp = new char[length];
	// //int i = Convert.ToBase64CharArray(b, 0, b.Length, tmp, 0);
	//
	// //writer.WriteString(new String(tmp));
	//
	// writer.WriteString(Convert.ToBase64String(b));
	// writer.WriteEndElement();
	// }
	// else
	// {
	// throw new LLSDSerializeException("Unknown type " + obj.GetType().Name);
	// }
	// }

	private static Object LLSDParseOne(XMLStreamReader llsdStream)
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
		} else if (datatype.equalsIgnoreCase("uuid") ) {
			// if (reader.IsEmptyElement)
			// {
			// reader.Read();
			// return UUID.Zero;
			// }
			ret = UUID.fromString(llsdStream.getElementText().trim());
			// case "string":
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
			return LLSDParseMap(llsdStream);
		} else if (datatype.equalsIgnoreCase("array")) {
			return LLSDParseArray(llsdStream);
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

	public static HashMap<String, Object> LLSDParseMap(
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
			Object val = LLSDParseOne(llsdStream);
			ret.put(key, val);
		}

		return ret;
		// TODO
	}

	public static ArrayList<Object> LLSDParseArray(XMLStreamReader llsdStream)
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

			ret.add(LLSDParseOne(llsdStream));
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
}
