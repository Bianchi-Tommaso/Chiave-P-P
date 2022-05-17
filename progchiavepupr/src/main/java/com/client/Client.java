package com.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client
{
    private int p;
    private int q;
    private int e;
    private int n;
    private int z;
    private int chiavePubblicaServer[] = new int[2];
    private int chiavePubblicaClient[] = new int[2];
    private int chiavePrivataClient[] = new int[2];
    private Socket client;
    private String indirizzo;
    private int porta;
    private BufferedReader inDalServer;
    private DataOutputStream outVersoServer;

    public Client(String indirizzo, int porta) 
    {
        this.p = 3;
        this.q = 11;

        this.indirizzo = indirizzo;
        this.porta = porta;

        this.chiavePubblicaClient = ChiavePubblica(this.p, this.q);
        this.chiavePrivataClient = ChiavePrivata(this.z, this.e);
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

            outVersoServer.writeBytes("Client: Invio La Chiave Pubblica" + "\n");
            outVersoServer.writeBytes(getChiavePubblicaClient()[0] + "\n"); //e
            outVersoServer.writeBytes(getChiavePubblicaClient()[1] + "\n"); //n

            System.out.print("Chiave Pubblica Server: " + getChiavePubblicaServer()[0]);
            System.out.println(", " + getChiavePubblicaServer()[1]);

            System.out.print("Chiave Privata Client: " + getChiavePrivataClient()[0]);
            System.out.println(", " + getChiavePrivataClient()[1]);

            System.out.println(inDalServer.readLine());
            String test = Decifra(inDalServer.readLine());

            System.out.println(test);

            client.close();
        }   
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Errore Durante La Connessione");
            System.exit(1);
        }
    }

    public int[] ChiavePubblica(int p, int q)
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
        int chiavePrivata[] = new int[2];

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

            z =  Double.valueOf(Math.pow(ascii, getChiavePubblicaServer()[0]) / getChiavePubblicaServer()[1]);
            x = z.intValue();
            y = (int) ((int) x * getChiavePubblicaServer()[1] - Math.pow(ascii, getChiavePubblicaServer()[0]));

            messaggioCifrato += Math.abs(y) + ";";
        }

        return messaggioCifrato;
    }

    public String Decifra(String messaggio)
    {
        String messaggioDecifrato = "";
        String[] messaggioCifrato = messaggio.split(";");
        char c;
        Double z;
        int x;
        int y = 0;

        for(int i = 0; i < messaggioCifrato.length; i++)
        {

            z =  Double.valueOf(Math.pow(Integer.valueOf(messaggioCifrato[i]), getChiavePrivataClient()[0]) / getChiavePrivataClient()[1]);
            x = z.intValue();
            y = (int) ((int) x * getChiavePrivataClient()[1] - Math.pow(Integer.valueOf(messaggioCifrato[i]), getChiavePrivataClient()[0]));
            y = (y * (-1));
            c = (char)y;

            messaggioDecifrato += c;
        }

        return messaggioDecifrato;
    }

    public int[] getChiavePubblicaClient() {
        return chiavePubblicaClient;
    }

    public void setChiavePubblicaClient(int[] chiavePubblica) {
        this.chiavePubblicaClient = chiavePubblica;
    }

    public int[] getChiavePubblicaServer() {
        return chiavePubblicaServer;
    }

    public void setChiavePubblicaServer(int[] chiavePubblicaServer) {
        this.chiavePubblicaServer = chiavePubblicaServer;
    }

    public int[] getChiavePrivataClient() {
        return chiavePrivataClient;
    }

    public void setChiavePrivataClient(int[] chiavePrivataClient) {
        this.chiavePrivataClient = chiavePrivataClient;
    }

    
}
