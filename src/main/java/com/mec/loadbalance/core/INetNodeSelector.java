 package com.mec.loadbalance.core;

import com.mec.rmi.node.INetNode;
/**
 * 
 * <ol>
 * 功能：节点选择策略
 * <li>当从缓冲中获取服务器列表后，消费者需要自行选择结点</li>
 * <li>为避免注册中心负载压力过大的问题，负载均衡放在消费端进行；</li>
 * <li>负载均衡实现的手法个不同，留接口自行实现</li>
 * </ol>
 * @author Quan
 * @date 2020/03/05
 * @version 0.0.1
 */
public interface INetNodeSelector {
     INetNode getRightNode();
}
