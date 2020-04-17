 package com.mec.provider.csframework.message;

import java.io.DataOutputStream;

public interface IDealNetmessage {
     void dealCommand(Object node, NetMessage message, DataOutputStream dos);
}
