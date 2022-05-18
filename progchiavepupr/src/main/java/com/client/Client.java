package com.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import com.rsa.*;

public class Client
{
    private int p;
    private int q;
    private int e;
    private int z;
    private int chiavePubblicaServer[] = new int[2];
    private int chiavePubblicaClient[] = new int[2];
    private int chiavePrivataClient[] = new int[2];
    private Socket client;
    private String indirizzo;
    private int porta;
    private BufferedReader inDalServer;
    private DataOutputStream outVersoServer;

    RSA rsa = new RSA();

    public Client(String indirizzo, int porta) 
    {
        this.p = 3;
        this.q = 11;

        this.indirizzo = indirizzo;
        this.porta = porta;

         setChiavePubblicaClient(rsa.ChiavePubblica(this.p, this.q));
         setChiavePrivataClient(rsa.ChiavePrivata(getChiavePubblicaClient()[0], getChiavePubblicaClient()[1]));
    }
    
    public Socket Connetti()
    {
        try
        {
            System.out.println("Client Connessione...");

            client = new Socket(indirizzo, porta);

            inDalServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outVersoServer = new DataOutputStream(client.getOutputStream());
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
            System.out.println(inDalServer.readLine());
            chiavePubblicaServer[0] = Integer.valueOf(inDalServer.readLine()); //e
            chiavePubblicaServer[1] = Integer.valueOf(inDalServer.readLine()); //n
            System.out.println(inDalServer.readLine());

            setChiavePubblicaServer(chiavePubblicaServer);

            outVersoServer.writeBytes("Client: Invio La Chiave Pubblica" + "\n");
            outVersoServer.writeBytes(getChiavePubblicaClient()[0] + "\n"); //e
            outVersoServer.writeBytes(getChiavePubblicaClient()[1] + "\n"); //n

            System.out.print("Chiave Pubblica Server: " + getChiavePubblicaServer()[0]);
            System.out.println(", " + getChiavePubblicaServer()[1]);

            System.out.print("Chiave Privata Client: " + getChiavePrivataClient()[0]);
            System.out.println(", " + getChiavePrivataClient()[1]);

            System.out.println(inDalServer.readLine());

            String messaggioCriptato = inDalServer.readLine();

            String test = rsa.Decifra(messaggioCriptato, getChiavePrivataClient()[0], getChiavePrivataClient()[1]);

            System.out.println("Messaggio: " + test);

            client.close();
        }   
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Errore Durante La Connessione");
            System.exit(1);
        }
    }

    public int[] getChiavePubblicaServer() {
        return chiavePubblicaServer;
    }

    public void setChiavePubblicaServer(int[] chiavePubblicaServer) {
        this.chiavePubblicaServer = chiavePubblicaServer;
    }

    public int[] getChiavePubblicaClient() {
        return chiavePubblicaClient;
    }

    public void setChiavePubblicaClient(int[] chiavePubblicaClient) {
        this.chiavePubblicaClient = chiavePubblicaClient;
    }

    public int[] getChiavePrivataClient() {
        return chiavePrivataClient;
    }

    public void setChiavePrivataClient(int[] chiavePrivataClient) {
        this.chiavePrivataClient = chiavePrivataClient;
    }

    
    
}
