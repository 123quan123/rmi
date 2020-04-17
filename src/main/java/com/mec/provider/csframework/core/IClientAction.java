 package com.mec.provider.csframework.core;

import com.mec.rmi.node.INetNode;
/**
 * 
 * <ol>
 * 功能：处理服务提供者连接、注册、注销事后服务。
 * <li>canotConnect不能连接接口</li>
 * <li>registryFail注册失败接口</li>
 * <li>outFail注销失败接口</li>
 * </ol>
 * @author Quan
 * @date 2020/03/05
 * @version 0.0.1
 */
public interface IClientAction {
     void canotConnect(Client client);
     void registryFail(String ServiceName, INetNode node);
     void outFail(String ServiceName, INetNode node);
}
