package com.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.rsa.*;

public class Server 
{
    private int p;
    private int q;
    private int e;
    private int z;
    private int chiavePubblicaClient[] = new int[2];
    private int chiavePubblicaServer[] = new int[2];
    private int chiavePrivataServer[] = new int[2];
    private Socket client;
    private ServerSocket server;
    private BufferedReader inDalClient;
    private DataOutputStream outVersoClient;
    private int porta; 
    
    RSA rsa = new RSA();

    public Server(int porta) throws IOException 
    {
        this.porta = porta;
        this.p = 7;
        this.q = 13;

         setChiavePubblicaServer(rsa.ChiavePubblica(this.p, this.q));
         setChiavePrivataServer(rsa.ChiavePrivata(getChiavePubblicaServer()[0], getChiavePubblicaServer()[1]));
    }
    
    public Socket Attendi()
    {
        try
        {
            server = new ServerSocket(porta);
            System.out.println("Server partito in Esecuzione");

            client = server.accept();
            
            String hostName = client.getInetAddress().getCanonicalHostName();
            System.out.println(hostName);

            server.close();

            inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outVersoClient = new DataOutputStream(client.getOutputStream());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Errore Durante La Connessione");
            System.exit(1);
        }

        return client;
    }

    public void Comunica()
    {
        try
        {
            outVersoClient.writeBytes("Server: Invio La Chiave Pubblica" + "\n");
            outVersoClient.writeBytes(getChiavePubblicaServer()[0] + "\n"); //e
            outVersoClient.writeBytes(getChiavePubblicaServer()[1] + "\n"); //n
            outVersoClient.writeBytes("Server: Attendo La Chiave Pubblica" + "\n");

            System.out.println(inDalClient.readLine());
            chiavePubblicaClient[0] = Integer.valueOf(inDalClient.readLine()); //e
            chiavePubblicaClient[1] = Integer.valueOf(inDalClient.readLine()); //n

            setChiavePubblicaClient(chiavePubblicaClient);

            System.out.print("Chiave Pubblica Client: " + getChiavePubblicaClient()[0]);
            System.out.println(", " + getChiavePubblicaClient()[1]);

            System.out.print("Chiave Privata Server: " + getChiavePrivataServer()[0]);
            System.out.println(", " + getChiavePrivataServer()[1]);

            String fraseCifrata = rsa.Cifra("Messaggio Segreto", getChiavePubblicaClient()[0], getChiavePubblicaClient()[1]);

            outVersoClient.writeBytes("Server: Invio La Stringa Cifrata" + "\n");
            outVersoClient.writeBytes(fraseCifrata + "\n");

            client.close();
        }   
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Errore Durante La Connessione");
            System.exit(1);
        }
    }

    public int[] getChiavePubblicaClient() {
        return chiavePubblicaClient;
    }

    public void setChiavePubblicaClient(int[] chiavePubblicaClient) {
        this.chiavePubblicaClient = chiavePubblicaClient;
    }

    public int[] getChiavePubblicaServer() {
        return chiavePubblicaServer;
    }

    public void setChiavePubblicaServer(int[] chiavePubblicaServer) {
        this.chiavePubblicaServer = chiavePubblicaServer;
    }

    public int[] getChiavePrivataServer() {
        return chiavePrivataServer;
    }

    public void setChiavePrivataServer(int[] chiavePrivataServer) {
        this.chiavePrivataServer = chiavePrivataServer;
    }

    
    
}
