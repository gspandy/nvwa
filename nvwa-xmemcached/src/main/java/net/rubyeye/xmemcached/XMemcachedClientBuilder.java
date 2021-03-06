package net.rubyeye.xmemcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.buffer.BufferAllocator;
import net.rubyeye.xmemcached.buffer.SimpleBufferAllocator;
import net.rubyeye.xmemcached.command.TextCommandFactory;
import net.rubyeye.xmemcached.impl.ArrayMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import net.rubyeye.xmemcached.transcoders.Transcoder;
import net.rubyeye.xmemcached.utils.Protocol;

import com.google.code.yanf4j.config.Configuration;
import com.google.code.yanf4j.core.SocketOption;
import com.google.code.yanf4j.core.impl.StandardSocketOption;

/**
 * Builder pattern.Configure XmemcachedClient's options,then build it
 * 
 * @author dennis
 * 
 */
public class XMemcachedClientBuilder implements MemcachedClientBuilder {

	private MemcachedSessionLocator sessionLocator = new ArrayMemcachedSessionLocator();
	private BufferAllocator bufferAllocator = new SimpleBufferAllocator();
	private Configuration configuration = getDefaultConfiguration();
	private List<InetSocketAddress> addressList = new ArrayList<InetSocketAddress>();

	private int[] weights;

	private int connectionPoolSize = MemcachedClient.DEFAULT_CONNECTION_POOL_SIZE;

	@SuppressWarnings("unchecked")
	final Map<SocketOption, Object> socketOptions = getDefaultSocketOptions();

	private List<MemcachedClientStateListener> stateListeners = new ArrayList<MemcachedClientStateListener>();

	private Map<InetSocketAddress, AuthInfo> authInfoMap = new HashMap<InetSocketAddress, AuthInfo>();

	private String name;
	
	private long opTimeout = MemcachedClient.DEFAULT_OP_TIMEOUT;
	private boolean addShutdownHook = true;

	public void addStateListener(MemcachedClientStateListener stateListener) {
		stateListeners.add(stateListener);
	}

	@SuppressWarnings("unchecked")
	public void setSocketOption(SocketOption socketOption, Object value) {
		if (socketOption == null) {
			throw new NullPointerException("Null socketOption");
		}
		if (value == null) {
			throw new NullPointerException("Null value");
		}
		if (!socketOption.type().equals(value.getClass())) {
			throw new IllegalArgumentException("Expected "
					+ socketOption.type().getSimpleName()
					+ " value,but givend " + value.getClass().getSimpleName());
		}
		socketOptions.put(socketOption, value);
	}

	@SuppressWarnings("unchecked")
	public Map<SocketOption, Object> getSocketOptions() {
		return socketOptions;
	}

	public final void setConnectionPoolSize(int poolSize) {
		if (connectionPoolSize <= 0) {
			throw new IllegalArgumentException("poolSize<=0");
		}
		connectionPoolSize = poolSize;
	}

	public void removeStateListener(MemcachedClientStateListener stateListener) {
		stateListeners.remove(stateListener);
	}

	public void setStateListeners(
			List<MemcachedClientStateListener> stateListeners) {
		if (stateListeners == null) {
			throw new IllegalArgumentException("Null state listeners");
		}
		this.stateListeners = stateListeners;
	}

	private CommandFactory commandFactory = new TextCommandFactory();

	@SuppressWarnings("unchecked")
	public static final Map<SocketOption, Object> getDefaultSocketOptions() {
		Map<SocketOption, Object> map = new HashMap<SocketOption, Object>();
		map.put(StandardSocketOption.TCP_NODELAY,
				MemcachedClient.DEFAULT_TCP_NO_DELAY);
		map.put(StandardSocketOption.SO_RCVBUF,
				MemcachedClient.DEFAULT_TCP_RECV_BUFF_SIZE);
		map.put(StandardSocketOption.SO_KEEPALIVE,
				MemcachedClient.DEFAULT_TCP_KEEPLIVE);
		map.put(StandardSocketOption.SO_SNDBUF,
				MemcachedClient.DEFAULT_TCP_SEND_BUFF_SIZE);
		map.put(StandardSocketOption.SO_LINGER, 0);
		map.put(StandardSocketOption.SO_REUSEADDR, true);
		return map;
	}

	public static final Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();
		configuration
				.setSessionReadBufferSize(MemcachedClient.DEFAULT_SESSION_READ_BUFF_SIZE);
		configuration
				.setReadThreadCount(MemcachedClient.DEFAULT_READ_THREAD_COUNT);
		configuration
				.setSessionIdleTimeout(MemcachedClient.DEFAULT_SESSION_IDLE_TIMEOUT);
		configuration.setWriteThreadCount(0);
		return configuration;
	}

	public final CommandFactory getCommandFactory() {
		return commandFactory;
	}

	public final void setCommandFactory(CommandFactory commandFactory) {
		this.commandFactory = commandFactory;
	}

	private @SuppressWarnings("unchecked")
	Transcoder transcoder = new SerializingTranscoder();

	public XMemcachedClientBuilder(List<InetSocketAddress> addressList) {
		this.addressList = addressList;
	}

	public XMemcachedClientBuilder(List<InetSocketAddress> addressList,
			int[] weights) {
		this.addressList = addressList;
		this.weights = weights;
	}

	public XMemcachedClientBuilder() {
		this(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.rubyeye.xmemcached.MemcachedClientBuilder#getSessionLocator()
	 */
	public MemcachedSessionLocator getSessionLocator() {
		return sessionLocator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.rubyeye.xmemcached.MemcachedClientBuilder#setSessionLocator(net.rubyeye
	 * .xmemcached.MemcachedSessionLocator)
	 */
	public void setSessionLocator(MemcachedSessionLocator sessionLocator) {
		if (sessionLocator == null) {
			throw new IllegalArgumentException("Null SessionLocator");
		}
		this.sessionLocator = sessionLocator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.rubyeye.xmemcached.MemcachedClientBuilder#getBufferAllocator()
	 */
	public BufferAllocator getBufferAllocator() {
		return bufferAllocator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.rubyeye.xmemcached.MemcachedClientBuilder#setBufferAllocator(net.
	 * rubyeye.xmemcached.buffer.BufferAllocator)
	 */
	public void setBufferAllocator(BufferAllocator bufferAllocator) {
		if (bufferAllocator == null) {
			throw new IllegalArgumentException("Null bufferAllocator");
		}
		this.bufferAllocator = bufferAllocator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.rubyeye.xmemcached.MemcachedClientBuilder#getConfiguration()
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.rubyeye.xmemcached.MemcachedClientBuilder#setConfiguration(com.google
	 * .code.yanf4j.config.Configuration)
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.rubyeye.xmemcached.MemcachedClientBuilder#build()
	 */
	public MemcachedClient build() throws IOException {
		XMemcachedClient memcachedClient;
		if (weights == null) {
			memcachedClient = new XMemcachedClient(sessionLocator,
					bufferAllocator, configuration, socketOptions,
					commandFactory, transcoder, addressList, stateListeners,
					authInfoMap, connectionPoolSize, name, addShutdownHook);

		} else {
			if (addressList == null) {
				throw new IllegalArgumentException("Null Address List");
			}
			if (addressList.size() > weights.length) {
				throw new IllegalArgumentException(
						"Weights Array's length is less than server's number");
			}
			memcachedClient = new XMemcachedClient(sessionLocator,
					bufferAllocator, configuration, socketOptions,
					commandFactory, transcoder, addressList, weights,
					stateListeners, authInfoMap, connectionPoolSize, name, addShutdownHook);
		}
		if (commandFactory.getProtocol() == Protocol.Kestrel) {
			memcachedClient.setOptimizeGet(false);
		}

		//added by nada on 20120422
		memcachedClient.setOpTimeout(opTimeout);
		
		memcachedClient.setAddShutdownHook(addShutdownHook);

		return memcachedClient;
	}

	@SuppressWarnings("unchecked")
	public Transcoder getTranscoder() {
		return transcoder;
	}

	@SuppressWarnings("unchecked")
	public void setTranscoder(Transcoder transcoder) {
		if (transcoder == null) {
			throw new IllegalArgumentException("Null Transcoder");
		}
		this.transcoder = transcoder;
	}

	public Map<InetSocketAddress, AuthInfo> getAuthInfoMap() {
		return authInfoMap;
	}

	public void addAuthInfo(InetSocketAddress address, AuthInfo authInfo) {
		authInfoMap.put(address, authInfo);
	}

	public void removeAuthInfo(InetSocketAddress address) {
		authInfoMap.remove(address);
	}

	public void setAuthInfoMap(Map<InetSocketAddress, AuthInfo> authInfoMap) {
		this.authInfoMap = authInfoMap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;

	}

	@Override
	public long getOpTimeout() {
		return opTimeout;
	}

	@Override
	public void setOpTimeout(long opTimeout) {
		this.opTimeout = opTimeout;
    }

    public boolean isAddShutdownHook() {
        return addShutdownHook;
    }

    public void setAddShutdownHook(boolean addShutdownHook) {
        this.addShutdownHook = addShutdownHook;
    }
}
