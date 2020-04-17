 package com.mec.provider.csframework.message;

public class NetMessage {
	private ENetCommand command;
	private String action;
	private String para;
	
	public NetMessage() {
	}

	public NetMessage(String message) {
		int dotIndex;
		
		dotIndex = message.indexOf('.');
		if (dotIndex < 0) {
			return;
		}
		String str = message.substring(0, dotIndex);
		this.command = ENetCommand.valueOf(str);
		
		message = message.substring(dotIndex + 1);
		dotIndex = message.indexOf('.');
		if (dotIndex < 0) {
			this.command = null;
			return;
		}
		str = message.substring(0, dotIndex); 
		this.action = str.equals(" ") ? null : str;
		
		message = message.substring(dotIndex + 1);
		this.para = message;
	}

	public ENetCommand getCommand() {
		return command;
	}

	public void setCommand(ENetCommand command) {
		this.command = command;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer(command.name());
		result.append('.');
		result.append(action == null ? " " : action).append('.');
		result.append(para);
		
		return result.toString();
	}
	
}
