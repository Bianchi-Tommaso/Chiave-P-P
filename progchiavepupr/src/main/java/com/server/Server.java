package com.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;

public class Server 
{
    private int p;
    private int q;
    private int e;
    private int n;
    private int z;
    private int chiavePubblicaServer[] = new int[2];
    private int chiavePubblicaClient[] = new int[2];
    private int chiavePrivataServer[] = new int[2];
    private int porta;
    private ServerSocket server;
    private Socket client;
    private BufferedReader inDalClient;

    private DataOutputStream outVersoClient;

    public Server(int porta) throws IOException 
    {
        this.porta = porta;
        this.p = 13;
        this.q = 29;

        this.chiavePubblicaServer = ChiavePubblica(this.p, this.q);
        this.chiavePrivataServer = ChiavePrivata(this.z, this.e);

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
            outVersoClient.writeBytes(getChiavePubblicaServer()[0] + "\n"); //e
            outVersoClient.writeBytes(getChiavePubblicaServer()[1] + "\n"); //n
            outVersoClient.writeBytes("Server: Attendo La Chiave Pubblica" + "\n");

            System.out.println(inDalClient.readLine());
            chiavePubblicaClient[0] = Integer.valueOf(inDalClient.readLine()); //e
            chiavePubblicaClient[1] = Integer.valueOf(inDalClient.readLine()); //n

            System.out.print("Chiave Pubblica Client: " + getChiavePubblicaClient()[0]);
            System.out.println(", " + getChiavePubblicaClient()[1]);

            System.out.print("Chiave Privata Server: " + getChiavePrivataServer()[0]);
            System.out.println(", " + getChiavePrivataServer()[1]);

            String fraseCifrata = Cifra("Messaggio Segreto");

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

    public int[]  ChiavePubblica(int p, int q)
    {
        int chiavePubblica[] = new int[2];
         n = 0;
         e = 2;
         z = 0;

        z = (p - 1) * (q - 1);
        n = p * q;
        e = MCD(e,z);

        chiavePubblica[0] = e;
        chiavePubblica[1] = n;

        return chiavePubblica;
    }

    public int MCD(int e, int z)
    {
        int i = e; 
        int j = z;
        int resto;

        do
        {
            while ((resto = i % j) != 0)
                {
                  i = j;
                  j = resto;
                }
            
                if(j != 1)
                {
                    e++;
                    i = e;
                    j = z;
                }

        } while(j != 1);

        return e;
    }

    public int[] ChiavePrivata(int z, int e)
    {
        int d = TrovaD(z, e);
        int chiavePrivata[]= new int[2];

        chiavePrivata[0] = d;
        chiavePrivata[1] = n;

        return chiavePrivata;
    }

    public int TrovaD(int z, int e)
    {
        int d = 0;

        for(int k = 1; ; k++)
        {
            if((k * z + 1) % e == 0)
            {
                d = (k * z + 1) / e;
                break;
            }
        }

        return d;
    }

    public String Cifra(String messaggio)
    {
        String messaggioCifrato = "";
        char c;
        int ascii = 0;
        Double z;
        int x;
        int y = 0;

        for(int i = 0; i < messaggio.length(); i++)
        {
            c = messaggio.charAt(i);
            ascii = (int)c;

            z =  Double.valueOf(Math.pow(ascii, getChiavePubblicaClient()[0]) / getChiavePubblicaClient()[1]);
            x = z.intValue();
            y = (int) ((int) x * getChiavePubblicaClient()[1] - Math.pow(ascii, getChiavePubblicaClient()[0]));

            messaggioCifrato +=(y * (-1)) + ";";
        }

        return messaggioCifrato;
    }

    public int[] getChiavePubblicaServer() {
        return chiavePubblicaServer;
    }

    public void setChiavePubblicaServer(int[] chiavePubblica) {
        this.chiavePubblicaServer = chiavePubblica;
    }

    public int[] getChiavePubblicaClient() {
        return chiavePubblicaClient;
    }

    public void setChiavePubblicaClient(int[] chiavePubblicaClient) {
        this.chiavePubblicaClient = chiavePubblicaClient;
    }

    public int[] getChiavePrivataServer() {
        return chiavePrivataServer;
    }

    public void setChiavePrivataServer(int[] chiavePrivataServer) {
        this.chiavePrivataServer = chiavePrivataServer;
    }

    
}
