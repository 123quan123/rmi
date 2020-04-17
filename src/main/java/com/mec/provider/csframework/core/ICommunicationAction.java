 package com.mec.provider.csframework.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.mec.provider.csframework.message.NetMessage;

public interface ICommunicationAction {
	void dealNetMessage(NetMessage message, DataInputStream dis, DataOutputStream dos);
}
