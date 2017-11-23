package cz.gemrot.phd.utils.sockets;

public interface IConfigurable<DATA extends ConfigMap> {

	public void configure(DATA config);
	
}
