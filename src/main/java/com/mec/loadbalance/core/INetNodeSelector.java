 package com.mec.loadbalance.core;

import com.mec.rmi.node.INetNode;
/**
 * 
 * <ol>
 * ���ܣ��ڵ�ѡ�����
 * <li>���ӻ����л�ȡ�������б����������Ҫ����ѡ����</li>
 * <li>Ϊ����ע�����ĸ���ѹ����������⣬���ؾ���������Ѷ˽��У�</li>
 * <li>���ؾ���ʵ�ֵ��ַ�����ͬ�����ӿ�����ʵ��</li>
 * </ol>
 * @author Quan
 * @date 2020/03/05
 * @version 0.0.1
 */
public interface INetNodeSelector {
     INetNode getRightNode();
}
