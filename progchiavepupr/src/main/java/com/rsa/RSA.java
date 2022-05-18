package com.rsa;

public class RSA
{
    private int chiavePubblicaServer[] = new int[2];
    private int chiavePubblicaClient[] = new int[2];
    private int chiavePrivataServer[] = new int[2];
    private int chiavePrivataClient[] = new int[2];
    private int n;

    public RSA ()
    {

    }

    public int[]  ChiavePubblica(int p, int q)
        {
            int chiavePubblica[] = new int[2];
            int n = 0;
            int e = 2;
            int z = 0;

            z = (p - 1) * (q - 1);
            n = p * q;
            e = MCD(e,z);

            chiavePubblica[0] = e;
            chiavePubblica[1] = n;

            this.n = n;

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
            chiavePrivata[1] = this.n;

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

        public String Cifra(String messaggio, int e, int n)
        {
            String messaggioCifrato = "";
            char c;
            int ascii = 0;
            int z;

            for(int i = 0; i < messaggio.length(); i++)
            {
                c = messaggio.charAt(i);
                ascii = (int)c;

                z =  (int) (Math.pow(ascii, e) % n);

                messaggioCifrato += z;
            }

            return messaggioCifrato;
        }

        public String Decifra(String messaggio, int d, int n)
    {
        String messaggioDecifrato = "";
        String[] messaggioCifrato = messaggio.split(";");
        char c;
        Double z;
        int x;
        int y = 0;

        for(int i = 0; i < messaggioCifrato.length; i++)
        {

            z =  Double.valueOf(Math.pow(Integer.valueOf(messaggioCifrato[i]), d) / n);
            x = z.intValue();
            y = (int) ((int) x * n - Math.pow(Integer.valueOf(messaggioCifrato[i]), d));
            y = (y * (-1));
            c = (char)y;

            messaggioDecifrato += c;
        }

        return messaggioDecifrato;
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

    public void setChiavePrivataServer(int[] chiavePrivataClient) {
        this.chiavePrivataClient = chiavePrivataClient;
    }

    public int[] getChiavePrivataClient() {
        return chiavePrivataServer;
    }

    public void setChiavePrivataClient(int[] chiavePrivataClient) {
        this.chiavePrivataClient = chiavePrivataClient;
    }
}