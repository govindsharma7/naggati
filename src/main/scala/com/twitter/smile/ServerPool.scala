package com.twitter.smile

import java.util.concurrent.Executors
import org.apache.mina.core.session.{IdleStatus, IoSession}
import org.apache.mina.transport.socket.nio.{NioProcessor, NioSocketConnector}
import com.twitter.tomservo.{Decoder, MemcacheClientDecoder}
import org.apache.mina.filter.codec.{ProtocolCodecFilter, ProtocolEncoder, ProtocolEncoderOutput}


/**
 * Pool of memcache servers, and their shared config.
 */
class ServerPool {
  var servers: Array[MemcacheServer] = Array()

  private var DEFAULT_CONNECT_TIMEOUT = 250
  var retryDelay = 30000

  // note: this will create one thread per ServerPool
  var connector = SocketConnectorHack.get(ServerPool.threadPool)
  connector.setConnectTimeoutMillis(DEFAULT_CONNECT_TIMEOUT)
  connector.getSessionConfig.setTcpNoDelay(true)

  connector.getFilterChain.addLast("codec", MemcacheClientDecoder.filter)
  connector.setHandler(new IoHandlerActorAdapter((session: IoSession) => null))

/*  acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );
  acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));

  acceptor.setHandler(  new TimeServerHandler() );
*/
}

object ServerPool {
  var threadPool = Executors.newCachedThreadPool
}