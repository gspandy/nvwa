/**
 *Copyright [2009-2010] [dennis zhuang(killme2008@gmail.com)]
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *             http://www.apache.org/licenses/LICENSE-2.0 
 *Unless required by applicable law or agreed to in writing, 
 *software distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 *either express or implied. See the License for the specific language governing permissions and limitations under the License
 */
/**
 *Copyright [2009-2010] [dennis zhuang(killme2008@gmail.com)]
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *             http://www.apache.org/licenses/LICENSE-2.0
 *Unless required by applicable law or agreed to in writing,
 *software distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *either express or implied. See the License for the specific language governing permissions and limitations under the License
 */
package net.rubyeye.xmemcached.monitor;

/**
 * Statistics MBean for jmx
 * 
 * @author dennis
 * 
 */
public interface StatisticsHandlerMBean {
	public long getGetHitCount();

	public long getGetMissCount();

	public long getSetCount();

	public long getAppendCount();

	public long getPrependCount();

	public long getCASCount();

	public long getDeleteCount();

	public long getIncrCount();

	public long getDecrCount();

	public long getMultiGetCount();

	public long getMultiGetsCount();

	public long getAddCount();

	public long getReplaceCount();

	/**
	 * 是否开启统计
	 * 
	 * @return
	 */
	public boolean isStatistics();

	/**
	 * 开启或者关闭统计
	 * 
	 * @param statistics
	 */
	public void setStatistics(boolean statistics);

}
