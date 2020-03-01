package apartado.c.test2.http;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;


public class HTTPSChannel
{
    private int _communicationport;         
    private ServerBootstrap _bootstrap;     

    public HTTPSChannel(int port)
    {
        _communicationport = port;
    }

    protected ChannelPipelineFactory getPipelineFactory()
    {
        return new HTTPSPipelineFactory();
    }

    public void startChannel()
        throws Exception
    {
        try
        {
            Executor bossthrd = Executors.newCachedThreadPool();
            Executor workthrd = Executors.newCachedThreadPool();

            // Configure the server.
            ServerBootstrap bootstrap = new ServerBootstrap(
                    new NioServerSocketChannelFactory(
                            bossthrd, workthrd));

            // Set up the event pipeline factory.
            bootstrap.setPipelineFactory(getPipelineFactory());

            // Bind and start to accept incoming connections.
            bootstrap.bind(new InetSocketAddress(_communicationport));
            _bootstrap = bootstrap;
            System.out.println("Started server");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void shutDown()
        throws Exception
    {
        _bootstrap.releaseExternalResources();
    }

    public static void main(String[] args)
        throws Exception
    {
        HTTPSChannel chnl = new HTTPSChannel(9080);
        chnl.startChannel();
    }
}

