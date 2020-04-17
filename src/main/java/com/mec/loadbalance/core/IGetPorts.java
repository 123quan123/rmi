 package com.mec.loadbalance.core;

import java.util.List;

import com.mec.rmi.node.INetNode;

public interface IGetPorts {
     List<INetNode> getPorts(String service);
}
