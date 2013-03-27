package tc.fab.app;

import java.awt.Color;

import org.jdesktop.application.ResourceMap;

public class ResourceReader {

	private ResourceMap resourceMap;

	public ResourceReader(ResourceMap resourceMap) {
		this.resourceMap = resourceMap;
	}

	public String getString(String key, Object... arguments) {
		if (resourceMap == null) {
			return "???";
		}
		return resourceMap.getString(key, arguments);
	}

	public Color getColor(String key) {
		if (resourceMap == null) {
			return null;
		}
		return resourceMap.getColor(key);
	}
}
