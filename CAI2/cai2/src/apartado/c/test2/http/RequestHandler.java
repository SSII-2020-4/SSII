package apartado.c.test2.http;

import java.io.InputStream;

import static org.jboss.netty.handler.codec.http.HttpHeaders.*;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.*;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.*;
import static org.jboss.netty.handler.codec.http.HttpVersion.*;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;

public class RequestHandler extends SimpleChannelUpstreamHandler {
	private final StringBuilder buf = new StringBuilder(); // The buffer of the data received.
	private Queue<RequestDataScope> _queueRequests = new ConcurrentLinkedQueue<RequestDataScope>();
	private Thread _worker;

	RequestHandler() {
		_worker = new Thread(new QueueProcessor());
		_worker.start();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		HttpRequest request = (HttpRequest) e.getMessage();
		if (is100ContinueExpected(request)) {
			send100Continue(e);
		}

		RequestDataScope scope = new RequestDataScope(getHost(request, "unknown"), e);
		_queueRequests.add(scope);
	}

	private void send100Continue(MessageEvent e) {
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, CONTINUE);
		e.getChannel().write(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		// TODO:
		e.getCause().printStackTrace();
		e.getChannel().close();
	}

	public class QueueProcessor implements Runnable {
		QueueProcessor() {
		}

		public void run() {
			while (true) {
				try {
					if (!_queueRequests.isEmpty()) {
						RequestDataScope process = _queueRequests.poll();
						InputStream str = process.data().datastream();
						StringBuffer buff = readStream(str);
						process.transmit(buff.toString());
					}
					Thread.currentThread().sleep(10000); // long sleep here
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public StringBuffer readStream(InputStream stream) throws Exception {
			StringBuffer buff = new StringBuffer();
			byte[] bytes = new byte[1024];
			int read = stream.read(bytes, 0, bytes.length);
			while (read > 0) {
				String str = new String(bytes, 0, read);
				buff.append(str);
				read = stream.read(bytes, 0, bytes.length);
			}

			return buff;
		}

	}
}
