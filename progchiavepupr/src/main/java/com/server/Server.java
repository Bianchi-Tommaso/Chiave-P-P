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
    private int n;
    private int z;
    private int porta;
    private ServerSocket server;
    private Socket client;
    private BufferedReader inDalClient;
    private DataOutputStream outVersoClient;
    private int chiavePubblicaClient[] = new int[2];

    RSA rsa = new RSA();

    public Server(int porta) throws IOException 
    {
        this.porta = porta;
        this.p = 13;
        this.q = 29;

         rsa.setChiavePubblicaServer(rsa.ChiavePubblica(this.p, this.q));
         rsa.setChiavePrivataServer(rsa.ChiavePrivata(this.z, this.e));

        server = new ServerSocket(porta);
    }
    
    public Socket Attendi()
    {
        try
        {
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
            outVersoClient.writeBytes(rsa.getChiavePubblicaServer()[0] + "\n"); //e
            outVersoClient.writeBytes(rsa.getChiavePubblicaServer()[1] + "\n"); //n
            outVersoClient.writeBytes("Server: Attendo La Chiave Pubblica" + "\n");

            System.out.println(inDalClient.readLine());
            chiavePubblicaClient[0] = Integer.valueOf(inDalClient.readLine()); //e
            chiavePubblicaClient[1] = Integer.valueOf(inDalClient.readLine()); //n

            rsa.setChiavePubblicaClient(chiavePubblicaClient);

            System.out.print("Chiave Pubblica Client: " + rsa.getChiavePubblicaClient()[0]);
            System.out.println(", " + rsa.getChiavePubblicaClient()[1]);

            System.out.print("Chiave Privata Server: " + rsa.getChiavePrivataServer()[0]);
            System.out.println(", " + rsa.getChiavePrivataServer()[1]);

            String fraseCifrata = rsa.Cifra("Messaggio Segreto", rsa.getChiavePubblicaClient()[0], rsa.getChiavePubblicaClient()[1]);

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
    
}
