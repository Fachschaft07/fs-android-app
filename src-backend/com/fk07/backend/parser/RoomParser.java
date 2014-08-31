package com.fk07.backend.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;

import com.fk07.backend.data.Room;
import com.fk07.backend.data.Room.Type;
import com.fk07.backend.parser.async.AsyncDownloader;
import com.fk07.rooms.RoomsAdapter;

/**
 * @author Fabio
 * 
 */
public class RoomParser extends AsyncDownloader<List<Room>> {
	private static final String TAG = RoomParser.class.getSimpleName();
	private final RoomsAdapter adapter;

	/**
	 * @param context
	 * @param adapter
	 */
	public RoomParser(final Context context, final RoomsAdapter adapter) {
		super(context);
		this.adapter = adapter;
	}

	@Override
	protected List<Room> onParse(final InputStream openFileStream) {
		final List<Room> roomList = new ArrayList<Room>();

		try {
			final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			final Document doc = builder.parse(openFileStream);

			final NodeList nodes = doc.getElementsByTagName("room");
			for (int i = 0; i < nodes.getLength(); i++) {
				final Element element = (Element) nodes.item(i);
				final String parentName = nodes.item(i).getParentNode().getNodeName();

				if (parentName.equals("auditoriums")) {
					roomList.add(new Room(Type.HALL, getElementValue(element, "roomName"), "bis "
							+ getElementValue(element, "freeUntil")));
				}

				if (parentName.equals("laboratories")) {
					roomList.add(new Room(Type.LABORATORY, getElementValue(element, "roomName"), "bis "
							+ getElementValue(element, "freeUntil")));
				}
			}
		} catch (final ParserConfigurationException e) {
			Log.e(TAG, "", e);
		} catch (final SAXException e) {
			Log.e(TAG, "", e);
		} catch (final IOException e) {
			Log.e(TAG, "", e);
		} catch (final NullPointerException e) {
			Log.e(TAG, "", e);
		}

		return roomList;
	}

	@Override
	protected void onPostExecute(final List<Room> content) {
		if (content != null) {
			adapter.clear();
			adapter.addAll(content);
		}
		super.onPostExecute(content);
	}

	private String getElementValue(final Element parent, final String label) {
		return parent.getElementsByTagName(label).item(0).getTextContent();
	}
}
