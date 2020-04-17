 package com.mec.loadbalance.core;

import java.util.ArrayList;
import java.util.List;

import com.mec.rmi.node.INetNode;

public class NetNodeSelector implements INetNodeSelector {
    private List<INetNode> netNodeList;
     
    public NetNodeSelector() {
        if (netNodeList == null) {
            netNodeList = new ArrayList<INetNode>();
        }
    }
    
    @Override
    public INetNode getRightNode() {
        // TODO Auto-generated method stub
         return null;
    }

}
