package utd.persistentDataStore.datastoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.StreamUtil;

public class DatastoreClientImpl implements DatastoreClient
{
    private static Logger logger = Logger.getLogger(DatastoreClientImpl.class);

    private InetAddress address;
    private int port;

    public DatastoreClientImpl(InetAddress address, int port)
    {
        this.address = address;
        this.port = port;
    }

    /* (non-Javadoc)
     * @see utd.persistentDataStore.datastoreClient.DatastoreClient#write(java.lang.String, byte[])
     */
    @Override
    public void write(String name, byte data[]) throws ClientException
    {
        logger.debug("Executing Write Operation");
        try {
            logger.debug("Opening Socket");
            Socket socket = new Socket();
            SocketAddress saddr = new InetSocketAddress(address, port);
            socket.connect(saddr);
            //System.out.println("123"); //!!!!!!!!!!!
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            
            logger.debug("Writing Message");
            StreamUtil.writeLine("write\n", outputStream);
            StreamUtil.writeLine(name, outputStream);
            //////!!!write data size in ASCII
            StreamUtil.writeLine(String.valueOf(data.length), outputStream);
            StreamUtil.writeData(data, outputStream);
            
            logger.debug("Reading Response");
            String result = StreamUtil.readLine(inputStream);
            logger.debug("Response " + result);
            StreamUtil.closeSocket(inputStream);
            socket.close();
            if (!result.equalsIgnoreCase("ok")) {
                throw new ClientException("Fail to write the file");
            }
        }
        catch (IOException ex) {
            logger.error(ex.getMessage());
            //System.out.println("12345" + ex.getMessage()); //!!!!!!!!!!!
            //throw new ClientException(ex.getMessage(), ex); //!!!!
        }
    }

    /* (non-Javadoc)
     * @see utd.persistentDataStore.datastoreClient.DatastoreClient#read(java.lang.String)
     */
    @Override
    public byte[] read(String name) throws ClientException
    {
        logger.debug("Executing Read Operation");
        byte[] data = null;
        try {
            logger.debug("Opening Socket");
            Socket socket = new Socket();
            SocketAddress saddr = new InetSocketAddress(address, port);
            socket.connect(saddr);
            //System.out.println("1234567890");//!!!!!!!!!!
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            
            logger.debug("Writing Message");
            StreamUtil.writeLine("read\n", outputStream);
            StreamUtil.writeLine(name, outputStream);
            
            logger.debug("Reading Response");
            String responseCode = StreamUtil.readLine(inputStream);
            logger.debug("Response " + responseCode);
            
            if (responseCode.equalsIgnoreCase("ok")) {
                String size = StreamUtil.readLine(inputStream);
                logger.debug("Response " + size);
                data = StreamUtil.readData(Integer.valueOf(size), inputStream);
                logger.debug("Response " + data);
                StreamUtil.closeSocket(inputStream);
                socket.close();
            } else {
                StreamUtil.closeSocket(inputStream);
                socket.close();
                throw new ClientException("Fail to read the file");
            }
        }
        catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new ClientException(ex.getMessage());
        }
        return data;
    }

    /* (non-Javadoc)
     * @see utd.persistentDataStore.datastoreClient.DatastoreClient#delete(java.lang.String)
     */
    @Override
    public void delete(String name) throws ClientException
    {
        logger.debug("Executing Delete Operation");
        try {
            logger.debug("Opening Socket");
            Socket socket = new Socket();
            SocketAddress saddr = new InetSocketAddress(address, port);
            socket.connect(saddr);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            
            logger.debug("Writing Message");
            StreamUtil.writeLine("delete\n", outputStream);
            StreamUtil.writeLine(name, outputStream);
            
            logger.debug("Reading Response");
            String result = StreamUtil.readLine(inputStream);
            logger.debug("Response " + result);
            StreamUtil.closeSocket(inputStream);
            socket.close();
            if (!result.equalsIgnoreCase("ok")) {
                throw new ClientException("Fail to delete the file");
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new ClientException(ex.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see utd.persistentDataStore.datastoreClient.DatastoreClient#directory()
     */
    @Override
    public List<String> directory() throws ClientException
    {
        logger.debug("Executing Directory Operation");
        List<String> fileNames = new ArrayList<String>();
        try {
            logger.debug("Opening Socket");
            Socket socket = new Socket();
            SocketAddress saddr = new InetSocketAddress(address, port);
            socket.connect(saddr);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            
            logger.debug("Writing Message");
            StreamUtil.writeLine("directory\n", outputStream);
            
            logger.debug("Reading Response");
            String responseCode = StreamUtil.readLine(inputStream);
            logger.debug("Response " + responseCode);
            String size = StreamUtil.readLine(inputStream);
            logger.debug("Response " + size);
            
            String fileName;
            for(int i = 0; i < Integer.valueOf(size); i++) {
                fileName = StreamUtil.readLine(inputStream);
                fileNames.add(fileName);
                logger.debug("Response " + fileName);
            }
            StreamUtil.closeSocket(inputStream);
            socket.close();
            if (!responseCode.equalsIgnoreCase("ok")) {
                throw new ClientException("Fail to get the directory");
            }
        }
        catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new ClientException(ex.getMessage());
        }
        return fileNames;
    }

}
