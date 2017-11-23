package cz.gemrot.phd.utils.sockets;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("config")
public class ConfigXML implements IConfigEntry {

	public String key;
	
	public String value;

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public Object getValue() {
		return value;
	}
	
}
