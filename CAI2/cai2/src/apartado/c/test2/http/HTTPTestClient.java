package apartado.c.test2.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HTTPTestClient
{
    private static final String HTTPSTART = "HTTP/1.1";

    private String _url;
    private URLConnection _connection;
    private URL _event;
    private InputStream _in;

    public HTTPTestClient(String url)
        throws Exception
    {
        _url = url;
        open();
    }

    private void open()
        throws Exception
    {
        _event = new URL(_url);
        System.out.println("Protocol is: " + _event.getProtocol());
        _connection = _event.openConnection();
        _connection.setDoOutput(true);
    }

    public void close()
        throws Exception
    {
        if (_in != null)
            _in.close();
    }

    public void writeRequest(String post)
        throws Exception
    {
        //((HttpURLConnection)_connection).setFixedLengthStreamingMode(post.length());
        OutputStream stream = _connection.getOutputStream();
        stream.write(post.getBytes());
        stream.close();
        System.out.println(":Sent request:" + _url);
    }

    public void getResponses()
        throws Exception
    {
        if (_in == null)
            _in = _connection.getInputStream();

        byte[] bytes = new byte[1024];

        StringBuffer buff = new StringBuffer();
        int status = ((HttpURLConnection)_connection).getResponseCode();
        int len = ((HttpURLConnection)_connection).getContentLength();
        InputStream istr = (InputStream)_connection.getContent();
        System.out.println("Trying to read: " + status + ":" + len );
        int read = istr.read(bytes, 0, bytes.length);
        while (read > 0)
        {
            String str = new String(bytes, 0, read);
            buff.append(str);
            read = istr.read(bytes, 0, bytes.length);
        }

        System.out.println("Received: " + buff.toString() + ":" + status);
    }

    public static void main(String[] args)
        throws Exception
    {
        HTTPTestClient clnt = new HTTPTestClient("http://localhost:9080/something");
        clnt.writeRequest("Just Testing here.");
        clnt.getResponses();
        clnt.close();
        clnt = new HTTPTestClient("http://localhost:9080/something");
        clnt.writeRequest("Another Testing");
        clnt.getResponses();
    }

}

