package cz.gemrot.phd.utils.sockets;

import java.util.Collection;

import cz.gemrot.phd.utils.Const;

public class Configure {
	
	public static boolean isConfigurable(Object object) {
		return object instanceof IConfigurable;
	}
	
	public static void configure(Object object, Collection<ConfigXML> configuration) {
		if (configuration != null && !configuration.isEmpty()) {
			ConfigMap config = new ConfigMap(configuration);	
			configure(object, config);
		}		
	}
	
	public static void configure(IConfigurable object, Collection<ConfigXML> configuration) {
		if (configuration != null && !configuration.isEmpty()) {
			ConfigMap config = new ConfigMap(configuration);	
			configure(object, config);
		}
	}
	
	public static void configure(Object object, ConfigMap configuration) {
		if (!isConfigurable(object)) return;
		if (configuration != null && configuration.size() > 0) {
			IConfigurable e = (IConfigurable)object;
			configure(e, configuration);
		}
	}
	
	public static void configure(IConfigurable object, ConfigMap configuration) {
		try {
			object.configure(configuration);
		} catch (Exception e) {
			throw new RuntimeException("Failed to configure instance of class: " + object.getClass().getName() + Const.NEW_LINE + "With values:" + Const.NEW_LINE + configuration.describe(), e);
		}
	}

}
