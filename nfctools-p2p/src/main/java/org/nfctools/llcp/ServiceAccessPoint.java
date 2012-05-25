/**
 * Copyright 2011-2012 Adrian Stabiszewski, as@nfctools.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nfctools.llcp;

/**
 * Interface specification for Service Points that can communicate over a LLCP stack.
 * 
 */
public interface ServiceAccessPoint {

	/**
	 * Called if there is a device in range and the LLCP stack is active.
	 * 
	 * @param llcp
	 */
	void onLlcpActive(Llcp llcp);

	/**
	 * Called if this Service has an active connection. If the implementation does not invoke any method on the
	 * LlcpSocket the LLCP stack will send a Symmetry packet,
	 * 
	 * @param llcpSocket
	 */
	void onConnectionActive(LlcpSocket llcpSocket);

	/**
	 * Called if a connection failed/timed out.
	 */
	void onConnectFailed();

	/**
	 * Called if a connection succeeded and the other party is ready to receive data.
	 * 
	 * @param llcpSocket
	 */
	void onConnectSucceeded(LlcpSocket llcpSocket);

	/**
	 * Called if a sendMessage was successful and the other party responded with receive ready.
	 * 
	 * @param llcpSocket
	 */
	void onSendSucceeded(LlcpSocket llcpSocket);

	/**
	 * Called if a sendMessage failed.
	 */
	void onSendFailed();

	/**
	 * Called if the other party wants to connect to this service.
	 * 
	 * @param parameters
	 * @return
	 */
	boolean canAcceptConnection(Object[] parameters);

	/**
	 * Called if the other party has send a message or as a response to a send message from this implementation.
	 * 
	 * If this implementation has something to respond this data has to be returned here. If the returned byte array is
	 * null the LLCP stack will answer with receive ready.
	 * 
	 * @param serviceDataUnit
	 * @return nullable byte array as response
	 */
	byte[] onInformation(byte[] serviceDataUnit);

	/**
	 * Called if the other party ended the connection or the LLCP stack ended.
	 */
	void onDisconnect();

	//	void onParameterChange(Object[] parameters);
}
