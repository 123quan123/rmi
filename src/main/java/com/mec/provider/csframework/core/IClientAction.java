 package com.mec.provider.csframework.core;

import com.mec.rmi.node.INetNode;
/**
 * 
 * <ol>
 * ���ܣ���������ṩ�����ӡ�ע�ᡢע���º����
 * <li>canotConnect�������ӽӿ�</li>
 * <li>registryFailע��ʧ�ܽӿ�</li>
 * <li>outFailע��ʧ�ܽӿ�</li>
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
