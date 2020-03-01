package apartado.c.test2.http;

import static org.jboss.netty.channel.Channels.*;

import org.jboss.netty.handler.ssl.SslHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;

public class HTTPSPipelineFactory implements ChannelPipelineFactory
{
    HTTPSPipelineFactory()
    {
    }

    public ChannelPipeline getPipeline() 
        throws Exception 
    {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = pipeline();

        pipeline.addLast("decoder", new HttpRequestDecoder());
        // Uncomment the following line if you don't want to handle HttpChunks.
        pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        // Remove the following line if you don't want automatic content compression.
        pipeline.addLast("deflater", new HttpContentCompressor());
        pipeline.addLast("handler", new RequestHandler());
        return pipeline;
    }
}

